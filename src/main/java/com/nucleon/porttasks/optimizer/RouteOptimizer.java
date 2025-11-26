package com.nucleon.porttasks.optimizer;

import com.nucleon.porttasks.CourierTask;
import com.nucleon.porttasks.enums.PortLocation;

import java.util.*;

public class RouteOptimizer
{
	private final DistanceCache distanceCache;

	public RouteOptimizer(DistanceCache distanceCache)
	{
		this.distanceCache = distanceCache;
	}

	public OptimizedRoute optimizeRoute(List<CourierTask> tasks, PortLocation currentLocation, int shipCapacity)
	{
		if (tasks == null || tasks.isEmpty())
		{
			return null;
		}

		List<RouteStop> stops = buildStops(tasks);
		if (stops.isEmpty())
		{
			return null;
		}

		OptimizerMode mode = OptimizerMode.detect(tasks);
		int preloadedCargo = calculatePreloadedCargo(tasks);

		List<RouteStop> route = mode == OptimizerMode.PLANNING
			? findOptimalRoute(stops, shipCapacity, preloadedCargo, null)
			: findOptimalRoute(stops, shipCapacity, preloadedCargo, currentLocation);

		if (route.isEmpty())
		{
			return null;
		}

		// Use the provided currentLocation for distance calculation if specified.
		// This allows callers to force inclusion of the outbound leg even in planning mode
		// (e.g., when teleport optimization is disabled).
		int totalDistance = calculateTotalDistance(route, currentLocation);

		return new OptimizedRoute(route, totalDistance);
	}

	private List<RouteStop> buildStops(List<CourierTask> tasks)
	{
		List<RouteStop> stops = new ArrayList<>();

		for (CourierTask task : tasks)
		{
			// Only add pickup if cargo not yet loaded
			if (task.getCargoTaken() == 0)
			{
				stops.add(new RouteStop(task.getData().getPickupPort(), RouteStopType.PICKUP, task));
			}
			// Always add delivery
			stops.add(new RouteStop(task.getData().getDeliveryPort(), RouteStopType.DELIVERY, task));
		}

		return stops;
	}

	private int calculatePreloadedCargo(List<CourierTask> tasks)
	{
		int cargo = 0;
		for (CourierTask task : tasks)
		{
			if (task.getCargoTaken() > 0)
			{
				cargo += task.getData().cargoAmount;
			}
		}
		return cargo;
	}

	private List<RouteStop> findOptimalRoute(List<RouteStop> stops, int shipCapacity, int initialCargo, PortLocation startLocation)
	{
		if (stops.isEmpty())
		{
			return new ArrayList<>();
		}
		
		// Identify preloaded tasks (have delivery but no pickup in stops)
		Set<CourierTask> preloadedTasks = new HashSet<>();
		Set<CourierTask> hasPickup = new HashSet<>();
		Set<CourierTask> hasDelivery = new HashSet<>();
		
		for (RouteStop stop : stops)
		{
			if (stop.getType() == RouteStopType.PICKUP)
			{
				hasPickup.add(stop.getTask());
			}
			else
			{
				hasDelivery.add(stop.getTask());
			}
		}
		
		for (CourierTask task : hasDelivery)
		{
			if (!hasPickup.contains(task))
			{
				preloadedTasks.add(task);
			}
		}
		
		// Use branch-and-bound search
		List<RouteStop> bestRoute = new ArrayList<>();
		int[] bestDistance = {Integer.MAX_VALUE};
		
		searchRoutes(
			stops,
			new ArrayList<>(),
			new HashSet<>(),
			new HashSet<>(preloadedTasks),
			initialCargo,
			shipCapacity,
			startLocation, // null for planning mode, fixed location for execution mode
			0,
			bestRoute,
			bestDistance
		);
		
		return bestRoute;
	}

	private void searchRoutes(
		List<RouteStop> allStops,
		List<RouteStop> currentRoute,
		Set<RouteStop> visited,
		Set<CourierTask> pickedUp,
		int currentCargo,
		int shipCapacity,
		PortLocation currentPort,
		int currentDistance,
		List<RouteStop> bestRoute,
		int[] bestDistance)
	{
		// Pruning: if current distance already exceeds best, abandon this branch
		if (currentDistance >= bestDistance[0])
		{
			return;
		}
		
		// Base case: all stops visited
		if (visited.size() == allStops.size())
		{
            bestDistance[0] = currentDistance;
            bestRoute.clear();
            bestRoute.addAll(currentRoute);
            return;
		}
		
		// Try each unvisited stop that we can legally visit
		for (RouteStop candidate : allStops)
		{
			if (visited.contains(candidate))
			{
				continue;
			}
			
			// Check precedence: can only deliver if picked up
			if (candidate.getType() == RouteStopType.DELIVERY && !pickedUp.contains(candidate.getTask()))
			{
				continue;
			}
			
			// Check capacity: can only pickup if there's room
			if (candidate.getType() == RouteStopType.PICKUP)
			{
				int newCargo = currentCargo + candidate.getTask().getData().cargoAmount;
				if (newCargo > shipCapacity)
				{
					continue;
				}
			}
			
			// Calculate distance to this candidate
			int addedDistance = 0;
			if (currentPort != null)
			{
				addedDistance = distanceCache.getDistance(currentPort, candidate.getPort());
				if (addedDistance == Integer.MAX_VALUE)
				{
					continue; // No path exists
				}
			}
			
			// Recurse
			visited.add(candidate);
			currentRoute.add(candidate);
			
			int newCargo = currentCargo;
			if (candidate.getType() == RouteStopType.PICKUP)
			{
				pickedUp.add(candidate.getTask());
				newCargo += candidate.getTask().getData().cargoAmount;
			}
			else
			{
				newCargo -= candidate.getTask().getData().cargoAmount;
			}
			
			searchRoutes(
				allStops,
				currentRoute,
				visited,
				pickedUp,
				newCargo,
				shipCapacity,
				candidate.getPort(),
				currentDistance + addedDistance,
				bestRoute,
				bestDistance
			);
			
			// Backtrack
			visited.remove(candidate);
			currentRoute.remove(currentRoute.size() - 1);
			if (candidate.getType() == RouteStopType.PICKUP)
			{
				pickedUp.remove(candidate.getTask());
			}
		}
	}

	private int calculateTotalDistance(List<RouteStop> route, PortLocation startLocation)
	{
		if (route.isEmpty())
		{
			return 0;
		}

		int distance = 0;
		PortLocation current = startLocation != null ? startLocation : route.get(0).getPort();

		for (RouteStop stop : route)
		{
			distance += distanceCache.getDistance(current, stop.getPort());
			current = stop.getPort();
		}

		return distance;
	}
}
