package com.nucleon.porttasks.optimizer;

import com.nucleon.porttasks.CourierTask;
import com.nucleon.porttasks.enums.PortLocation;

import java.util.List;

public enum ProgressState
{
	NOT_STARTED(),
	EARLY(),
	MID(),
	LATE();

    public static ProgressState calculate(CourierTask task, PortLocation currentLocation, DistanceCache distanceCache)
	{
		if (task.getCargoTaken() == 0)
		{
			return NOT_STARTED;
		}

		PortLocation pickup = task.getData().getPickupPort();
		PortLocation delivery = task.getData().getDeliveryPort();

		int totalDistance = distanceCache.getDistance(pickup, delivery);
		if (totalDistance <= 0)
		{
			return LATE;
		}

		int sailedDistance = distanceCache.getDistance(pickup, currentLocation);
		double progress = Math.min(1.0, Math.max(0.0, (double) sailedDistance / totalDistance));

		return classifyProgress(progress);
	}

	public static ProgressState calculateWithRoute(CourierTask task, List<RouteStop> routeStops, int currentPortIndex)
	{
		if (task.getCargoTaken() == 0)
		{
			return NOT_STARTED;
		}

		int pickupIndex = -1;
		int deliveryIndex = -1;

		for (int i = 0; i < routeStops.size(); i++)
		{
			RouteStop stop = routeStops.get(i);
			if (stop.getTask() == task)
			{
				if (stop.getType() == RouteStopType.PICKUP)
				{
					pickupIndex = i;
				}
				else if (stop.getType() == RouteStopType.DELIVERY)
				{
					deliveryIndex = i;
				}
			}
		}

		if (deliveryIndex < 0)
		{
			return LATE;
		}

		if (pickupIndex < 0)
		{
			pickupIndex = 0;
		}

		int totalStops = deliveryIndex - pickupIndex;
		if (totalStops <= 0)
		{
			return LATE;
		}

		int stopsCompleted = Math.max(0, currentPortIndex - pickupIndex);
		double progress = (double) stopsCompleted / totalStops;

		return classifyProgress(progress);
	}

	private static ProgressState classifyProgress(double progress)
	{
		if (progress < 0.25)
		{
			return EARLY;
		}
		else if (progress <= 0.75)
		{
			return MID;
		}
		else
		{
			return LATE;
		}
	}
}
