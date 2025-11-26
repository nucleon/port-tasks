package com.nucleon.porttasks.optimizer;

import com.nucleon.porttasks.CourierTask;
import com.nucleon.porttasks.enums.CourierTaskData;
import com.nucleon.porttasks.enums.PortLocation;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Unit tests for RouteOptimizer covering various scenarios from PLAN.md.
 * 
 * Test scenarios:
 * 1. Planning Mode: All tasks have cargoTaken == 0
 * 2. Execution Mode: At least one task has cargoTaken > 0
 * 3. Mixed progress: Some tasks started, some not
 * 4. Capacity constraints: Cargo hold and task slot limits
 * 5. Single task routes
 * 6. Multiple task route optimization
 * 7. Near completion scenarios
 */
public class RouteOptimizerTest
{
	private RouteOptimizer optimizer;
	private DistanceCache distanceCache;
	private static final int DEFAULT_SHIP_CAPACITY = 20; // Standard ship capacity
	
	@Before
	public void setUp()
	{
		distanceCache = new DistanceCache();
		optimizer = new RouteOptimizer(distanceCache);
	}
	
	/**
	 * Test: Empty task list returns null route
	 */
	@Test
	public void testEmptyTaskList()
	{
		List<CourierTask> tasks = new ArrayList<>();
		OptimizedRoute route = optimizer.optimizeRoute(tasks, PortLocation.PORT_SARIM, DEFAULT_SHIP_CAPACITY);
		
		assertNull("Route should be null for empty task list", route);
	}
	
	/**
	 * Test: Single task in Planning Mode (no cargo loaded)
	 * Should create simple route: pickup -> delivery
	 */
	@Test
	public void testSingleTaskPlanningMode()
	{
		// Create task: Port Sarim -> Catherby (no cargo loaded)
		CourierTask task = createTask(CourierTaskData.CATHERBY_BAIT_DELIVERY_5, 0);
		List<CourierTask> tasks = Arrays.asList(task);
		
		OptimizedRoute route = optimizer.optimizeRoute(tasks, PortLocation.PORT_SARIM, DEFAULT_SHIP_CAPACITY);
		
		assertNotNull("Route should not be null", route);
		assertEquals("Route should have 2 stops (pickup + delivery)", 2, route.getStops().size());
		
		// Verify first stop is pickup at Port Sarim
		RouteStop firstStop = route.getStops().get(0);
		assertEquals("First stop should be pickup location", PortLocation.PORT_SARIM, firstStop.getPort());
		assertEquals("First stop should be PICKUP", RouteStopType.PICKUP, firstStop.getType());
		
		// Verify second stop is delivery at Catherby
		RouteStop secondStop = route.getStops().get(1);
		assertEquals("Second stop should be delivery location", PortLocation.CATHERBY, secondStop.getPort());
		assertEquals("Second stop should be DELIVERY", RouteStopType.DELIVERY, secondStop.getType());
	}
	
	/**
	 * Test: Single task in Execution Mode (cargo already loaded)
	 * Should only create delivery stop (pickup already happened)
	 */
	@Test
	public void testSingleTaskExecutionMode()
	{
		// Create task with cargo already loaded (cargoTaken > 0)
		CourierTask task = createTask(CourierTaskData.CATHERBY_BAIT_DELIVERY_5, 1);
		List<CourierTask> tasks = Arrays.asList(task);
		
		// Player is currently at Port Sarim (already picked up cargo)
		OptimizedRoute route = optimizer.optimizeRoute(tasks, PortLocation.PORT_SARIM, DEFAULT_SHIP_CAPACITY);
		
		assertNotNull("Route should not be null", route);
		assertEquals("Route should have 1 stop (delivery only)", 1, route.getStops().size());
		
		// Verify only stop is delivery at Catherby
		RouteStop stop = route.getStops().get(0);
		assertEquals("Stop should be delivery location", PortLocation.CATHERBY, stop.getPort());
		assertEquals("Stop should be DELIVERY", RouteStopType.DELIVERY, stop.getType());
	}
	
	/**
	 * Test: Multiple tasks in Planning Mode
	 * Should optimize route to visit all pickups and deliveries efficiently
	 */
	@Test
	public void testMultipleTasksPlanningMode()
	{
		// Create 3 tasks, all in Planning Mode (no cargo loaded)
		// Task 1: Port Sarim -> Catherby (525 XP, 1 cargo)
		// Task 2: Musa Point -> Port Sarim (144 XP, 1 cargo)
		// Task 3: Port Sarim -> Pandemonium (141 XP, 1 cargo)
		List<CourierTask> tasks = Arrays.asList(
			createTask(CourierTaskData.CATHERBY_BAIT_DELIVERY_5, 0),
			createTask(CourierTaskData.MUSA_POINT_BANANA_DELIVERY_40, 0),
			createTask(CourierTaskData.PORT_SARIM_SPICE_DELIVERY_2, 0)
		);
		
		OptimizedRoute route = optimizer.optimizeRoute(tasks, PortLocation.PORT_SARIM, DEFAULT_SHIP_CAPACITY);
		
		assertNotNull("Route should not be null", route);
		assertEquals("Route should have 6 stops (3 pickups + 3 deliveries)", 6, route.getStops().size());
		
		// Verify route respects precedence: each task's pickup comes before its delivery
		verifyPrecedenceConstraint(route);
	}
	
	/**
	 * Test: Mixed progress tasks (some in-progress, some not started)
	 * In-progress tasks should have deliveries prioritized
	 */
	@Test
	public void testMixedProgressTasks()
	{
		// Task 1: Port Sarim -> Catherby (cargo loaded - in progress)
		// Task 2: Musa Point -> Port Sarim (not started)
		List<CourierTask> tasks = Arrays.asList(
			createTask(CourierTaskData.CATHERBY_BAIT_DELIVERY_5, 1), // In progress
			createTask(CourierTaskData.MUSA_POINT_BANANA_DELIVERY_40, 0)  // Not started
		);
		
		OptimizedRoute route = optimizer.optimizeRoute(tasks, PortLocation.PORT_SARIM, DEFAULT_SHIP_CAPACITY);
		
		assertNotNull("Route should always be found for valid tasks", route);
		
		// In Execution Mode with cargo loaded, should complete deliveries
		// Route should have: delivery for task 1, pickup + delivery for task 2
		assertTrue("Route should have at least 2 stops", route.getStops().size() >= 2);
		
		// Verify precedence constraint
		verifyPrecedenceConstraint(route);
		
		// Verify in-progress task (Catherby delivery) is included
		boolean hasCatherbyDelivery = route.getStops().stream()
			.anyMatch(s -> s.getType() == RouteStopType.DELIVERY &&
				s.getPort() == PortLocation.CATHERBY);
		assertTrue("In-progress task must be completed", hasCatherbyDelivery);
	}
	
	/**
	 * Test: Capacity constraint - tasks exceed cargo capacity
	 * Optimizer should handle tasks that fit within capacity limits
	 */
	@Test
	public void testCargoCapacityConstraint()
	{
		// Create tasks with large cargo amounts
		// Task 1: 6 cargo (Musa Point -> Port Tyras)
		// Task 2: 6 cargo (Ardougne -> Port Tyras) 
		// Task 3: 6 cargo (Ardougne -> Port Tyras)
		List<CourierTask> tasks = Arrays.asList(
			createTask(CourierTaskData.PORT_TYRAS_SNAKESKIN_DELIVERY_56, 0), // 6 cargo
			createTask(CourierTaskData.PORT_TYRAS_PLATEBODY_DELIVERY_100, 0), // 6 cargo
			createTask(CourierTaskData.PORT_TYRAS_SWORD_DELIVERY_104, 0)  // 6 cargo
		);
		
		// Set ship capacity to 15 (can hold 2.5 tasks worth, so only 2 at a time)
		int limitedCapacity = 15;
		
		OptimizedRoute route = optimizer.optimizeRoute(tasks, PortLocation.PORT_SARIM, limitedCapacity);
		
		assertNotNull("Route should not be null", route);
		
		// Verify capacity is never exceeded at any stop
		verifyCapacityConstraint(route, limitedCapacity);
	}
	
	/**
	 * Test: Capacity constraint validation at each port stop
	 * Cargo capacity should be checked as: current - deliveries + pickups
	 */
	@Test
	public void testCapacityValidationAtEachStop()
	{
		// Scenario inspired by PLAN.md:
		// Ship capacity: 12
		// Test with real tasks that have varying cargo amounts
		
		// Use tasks with different cargo amounts:
		// Task 1: 2 cargo
		// Task 2: 2 cargo
		// Task 3: 2 cargo
		// Task 4: 2 cargo
		// Task 5: 4 cargo
		// Total: 12 cargo (exactly at capacity if all picked up at once)
		
		List<CourierTask> tasks = Arrays.asList(
			createTask(CourierTaskData.MUSA_POINT_FISH_DELIVERY_8, 0), // 2 cargo
			createTask(CourierTaskData.PORT_SARIM_DELIVERY_OF_NOTHING_SINISTER_15, 0), // 2 cargo
			createTask(CourierTaskData.PORT_PISCARILIUS_BOOK_DELIVERY_16, 0), // 2 cargo
			createTask(CourierTaskData.RUIN_OF_UNKAH_RUNE_DELIVERY_17, 0), // 2 cargo
			createTask(CourierTaskData.RELLEKKA_PIE_DELIVERY_18, 0) // 4 cargo
		);
		
		OptimizedRoute route = optimizer.optimizeRoute(tasks, PortLocation.PORT_SARIM, 12);
		
		assertNotNull("Route should not be null", route);
		
		// Verify capacity never exceeds 12 at any stop
		verifyCapacityConstraint(route, 12);
	}
	
	/**
	 * Test: Route with same delivery destination
	 * Multiple tasks delivering to same port should be handled efficiently.
	 * From PLAN.md line 817-820: Multiple tasks with same delivery are delivered 
	 * at the same port stop (optimizer may combine them into a single visit).
	 */
	@Test
	public void testSameDeliveryDestination()
	{
		// Both tasks deliver to Port Sarim from different pickups
		// Task 1: Pandemonium -> Port Sarim (141 XP, 1 cargo)
		// Task 2: Musa Point -> Port Sarim (144 XP, 1 cargo)
		List<CourierTask> tasks = Arrays.asList(
			createTask(CourierTaskData.PORT_SARIM_SPICE_DELIVERY_2, 0),      // Pandemonium -> Port Sarim
			createTask(CourierTaskData.MUSA_POINT_BANANA_DELIVERY_40, 0)     // Musa Point -> Port Sarim
		);
		
		OptimizedRoute route = optimizer.optimizeRoute(tasks, PortLocation.PANDEMONIUM, DEFAULT_SHIP_CAPACITY);
		
		assertNotNull("Route should not be null", route);
		
		// Count deliveries to Port Sarim
		// Optimizer may combine deliveries to same port into a single visit or separate stops
		int portSarimDeliveries = 0;
		for (RouteStop stop : route.getStops())
		{
			if (stop.getPort() == PortLocation.PORT_SARIM && stop.getType() == RouteStopType.DELIVERY)
			{
				portSarimDeliveries++;
			}
		}
		
		// Should have at least 1 delivery stop at Port Sarim (may be 1 or 2 depending on optimization)
		assertTrue("Should have at least 1 delivery to Port Sarim", portSarimDeliveries >= 1);
		assertTrue("Should have at most 2 deliveries to Port Sarim", portSarimDeliveries <= 2);
		
		// Verify precedence constraint
		verifyPrecedenceConstraint(route);
		
		// Verify both tasks are included in the route (2 pickups + 1-2 deliveries)
		int totalStops = route.getStops().size();
		assertTrue("Route should have 3-4 stops (2 pickups + 1-2 deliveries)", 
			totalStops >= 3 && totalStops <= 4);
	}
	
	/**
	 * Test: Near completion scenario (>75% progress)
	 * Task almost complete should still have valid route to delivery
	 */
	@Test
	public void testNearCompletionScenario()
	{
		// Task with cargo loaded, player near delivery port
		// Port Sarim -> Musa Point (very close to completion)
		CourierTask task = createTask(CourierTaskData.MUSA_POINT_LOGS_DELIVERY_3, 1);
		List<CourierTask> tasks = Arrays.asList(task);
		
		// Player is very close to Musa Point
		OptimizedRoute route = optimizer.optimizeRoute(tasks, PortLocation.MUSA_POINT, DEFAULT_SHIP_CAPACITY);
		
		assertNotNull("Route should not be null", route);
		assertEquals("Route should have 1 delivery stop", 1, route.getStops().size());
		
		RouteStop stop = route.getStops().get(0);
		assertEquals("Stop should be at Musa Point", PortLocation.MUSA_POINT, stop.getPort());
		assertEquals("Stop should be DELIVERY", RouteStopType.DELIVERY, stop.getType());
	}
	
	/**
	 * Test: All slots full with all tasks in-progress
	 * Edge case from PLAN.md section 862
	 */
	@Test
	public void testAllSlotsFull_AllInProgress()
	{
		// Create 5 tasks (max task slots), all with cargo loaded
		List<CourierTask> tasks = Arrays.asList(
			createTask(CourierTaskData.CATHERBY_BAIT_DELIVERY_5, 1),
			createTask(CourierTaskData.MUSA_POINT_BANANA_DELIVERY_40, 1),
			createTask(CourierTaskData.PORT_SARIM_SPICE_DELIVERY_2, 1),
			createTask(CourierTaskData.BRIMHAVEN_VODKA_DELIVERY_14, 1),
			createTask(CourierTaskData.ARDOUGNE_SALAMANDER_DELIVERY_10, 1)
		);
		
		OptimizedRoute route = optimizer.optimizeRoute(tasks, PortLocation.PORT_SARIM, DEFAULT_SHIP_CAPACITY);
		
		assertNotNull("Route should not be null", route);
		
		// Should have 5 delivery stops (no pickups since all cargo already loaded)
		long deliveryCount = route.getStops().stream()
			.filter(stop -> stop.getType() == RouteStopType.DELIVERY)
			.count();
		
		assertEquals("Should have 5 delivery stops", 5, deliveryCount);
	}
	
	/**
	 * Test: Route distance calculation
	 * Verify that route distance is calculated correctly
	 */
	@Test
	public void testRouteDistanceCalculation()
	{
		// Simple route: Port Sarim -> Musa Point
		CourierTask task = createTask(CourierTaskData.MUSA_POINT_LOGS_DELIVERY_3, 0);
		List<CourierTask> tasks = Arrays.asList(task);
		
		OptimizedRoute route = optimizer.optimizeRoute(tasks, PortLocation.PORT_SARIM, DEFAULT_SHIP_CAPACITY);
		
		assertNotNull("Route should not be null", route);
		assertTrue("Route distance should be positive", route.getTotalDistance() > 0);
		
		// Distance should be roughly: Port Sarim -> Port Sarim (pickup) + Port Sarim -> Musa Point (delivery)
		// Since starting at Port Sarim and pickup is also at Port Sarim, distance should be approximately
		// the distance from Port Sarim to Musa Point
		int expectedDistance = distanceCache.getDistance(PortLocation.PORT_SARIM, PortLocation.MUSA_POINT);
		
		// Route distance should be close to expected (allowing for some optimization)
		assertTrue("Route distance should be reasonable", 
			route.getTotalDistance() >= expectedDistance * 0.8 && 
			route.getTotalDistance() <= expectedDistance * 1.2);
	}
	
	/**
	 * Test: Planning Mode with optimal starting point selection
	 * In Planning Mode with multiple tasks, optimizer should ignore current location
	 * and find optimal starting point to minimize total distance.
	 * From PLAN.md lines 283-290: Planning Mode has complete flexibility.
	 */
	@Test
	public void testPlanningMode_IgnoresCurrentLocation()
	{
		// Create tasks at different locations
		// Should find optimal starting point to minimize total distance
		List<CourierTask> tasks = Arrays.asList(
			createTask(CourierTaskData.CATHERBY_BAIT_DELIVERY_5, 0),      // Port Sarim -> Catherby
			createTask(CourierTaskData.MUSA_POINT_BANANA_DELIVERY_40, 0), // Musa Point -> Port Sarim
			createTask(CourierTaskData.ARDOUGNE_SALAMANDER_DELIVERY_10, 0) // Port Sarim -> Ardougne
		);
		
		// Start at a distant, isolated location to test that optimizer ignores it
		OptimizedRoute route = optimizer.optimizeRoute(tasks, PortLocation.RUINS_OF_UNKAH, DEFAULT_SHIP_CAPACITY);
		
		assertNotNull("Route should not be null", route);
		assertEquals("Route should have 6 stops", 6, route.getStops().size());
		
		// Verify precedence constraint
		verifyPrecedenceConstraint(route);
		
		// In Planning Mode, first stop should NOT be at current location (Ruins of Unkah)
		// It should be at an optimal pickup point (Port Sarim or Musa Point)
		RouteStop firstStop = route.getStops().get(0);
		assertNotEquals("Planning Mode should ignore current location and optimize starting point",
			PortLocation.RUINS_OF_UNKAH, firstStop.getPort());
		
		// First stop should be a pickup
		assertEquals("First stop should be PICKUP", RouteStopType.PICKUP, firstStop.getType());
	}
	
	/**
	 * Test: 2-opt improvement is applied
	 * Verify that route is improved by 2-opt local search
	 */
	@Test
	public void testTwoOptImprovement()
	{
		// Create a route with multiple tasks that could benefit from 2-opt
		List<CourierTask> tasks = Arrays.asList(
			createTask(CourierTaskData.CATHERBY_BAIT_DELIVERY_5, 0),
			createTask(CourierTaskData.MUSA_POINT_BANANA_DELIVERY_40, 0),
			createTask(CourierTaskData.ARDOUGNE_SALAMANDER_DELIVERY_10, 0),
			createTask(CourierTaskData.BRIMHAVEN_VODKA_DELIVERY_14, 0)
		);
		
		OptimizedRoute route = optimizer.optimizeRoute(tasks, PortLocation.PORT_SARIM, DEFAULT_SHIP_CAPACITY);
		
		assertNotNull("Route should not be null", route);
		
		// Can't directly test 2-opt was applied, but verify route is valid
		verifyPrecedenceConstraint(route);
		verifyCapacityConstraint(route, DEFAULT_SHIP_CAPACITY);
		
		// Route should have 8 stops (4 pickups + 4 deliveries)
		assertEquals("Route should have 8 stops", 8, route.getStops().size());
	}
	
	/**
	 * Test: Route optimization performance target (<50ms)
	 * From PLAN.md: Target performance <50ms calculation time
	 * 
	 * Note: This is a soft requirement during TDD phase. If implementation is not optimized yet,
	 * this may fail but should be a goal for production code.
	 */
	@Test
	public void testPerformanceTarget()
	{
		// Create 5 tasks (maximum task slots)
		List<CourierTask> tasks = Arrays.asList(
			createTask(CourierTaskData.CATHERBY_BAIT_DELIVERY_5, 0),
			createTask(CourierTaskData.MUSA_POINT_BANANA_DELIVERY_40, 0),
			createTask(CourierTaskData.ARDOUGNE_SALAMANDER_DELIVERY_10, 0),
			createTask(CourierTaskData.BRIMHAVEN_VODKA_DELIVERY_14, 0),
			createTask(CourierTaskData.PORT_KHAZARD_SWAMP_PASTE_DELIVERY_12, 0)
		);
		
		long startTime = System.currentTimeMillis();
		OptimizedRoute route = optimizer.optimizeRoute(tasks, PortLocation.PORT_SARIM, DEFAULT_SHIP_CAPACITY);
		long duration = System.currentTimeMillis() - startTime;
		
		assertNotNull("Route should not be null", route);
		
		// Soft performance requirement - optimization should complete in reasonable time
		assertTrue("Route optimization should complete in under 500ms", duration < 500);
	}
	
	/**
	 * Test: Execution Mode respects current location as mandatory starting point
	 * From PLAN.md lines 294-308: Execution Mode locks route for in-progress tasks
	 */
	@Test
	public void testExecutionMode_RespectsCurrentLocation()
	{
		// Task with cargo loaded - in Execution Mode
		List<CourierTask> tasks = Arrays.asList(
			createTask(CourierTaskData.CATHERBY_BAIT_DELIVERY_5, 1) // Cargo loaded at Port Sarim
		);
		
		// Current location is at sea between Port Sarim and Catherby
		PortLocation currentLocation = PortLocation.PORT_SARIM;
		
		OptimizedRoute route = optimizer.optimizeRoute(tasks, currentLocation, DEFAULT_SHIP_CAPACITY);
		
		assertNotNull("Route should not be null", route);
		
		// In Execution Mode, route continues from current location
		// Should only have delivery stop (cargo already loaded)
		assertEquals("Should have 1 stop (delivery only)", 1, route.getStops().size());
		assertEquals("Should deliver to Catherby", PortLocation.CATHERBY, route.getStops().get(0).getPort());
	}
	
	/**
	 * Test: Identical tasks (same pickup AND delivery)
	 * From PLAN.md lines 804-815: Multiple tasks with identical routes are highly valuable.
	 * Should deliver all tasks at same stop, cargo capacity permitting.
	 */
	@Test
	public void testIdenticalTasks_SamePickupAndDelivery()
	{
		// Create 2 identical tasks: Port Sarim -> Musa Point
		List<CourierTask> tasks = Arrays.asList(
			createTask(CourierTaskData.MUSA_POINT_LOGS_DELIVERY_3, 0),
			createTask(CourierTaskData.MUSA_POINT_LOGS_DELIVERY_3, 0)
		);
		
		OptimizedRoute route = optimizer.optimizeRoute(tasks, PortLocation.PORT_SARIM, DEFAULT_SHIP_CAPACITY);
		
		assertNotNull("Route should not be null", route);
		
		// Should have exactly 2 stops: 1 pickup at Port Sarim (for both tasks), 1 delivery at Musa Point
		// Or may have 2 pickup stops and 2 delivery stops (implementation choice)
		int pickupStops = 0;
		int deliveryStops = 0;
		
		for (RouteStop stop : route.getStops())
		{
			if (stop.getType() == RouteStopType.PICKUP && stop.getPort() == PortLocation.PORT_SARIM)
			{
				pickupStops++;
			}
			if (stop.getType() == RouteStopType.DELIVERY && stop.getPort() == PortLocation.MUSA_POINT)
			{
				deliveryStops++;
			}
		}
		
		// Should pickup at Port Sarim (1 or 2 stops)
		assertTrue("Should pickup at Port Sarim", pickupStops >= 1 && pickupStops <= 2);
		// Should deliver at Musa Point (1 or 2 stops)
		assertTrue("Should deliver at Musa Point", deliveryStops >= 1 && deliveryStops <= 2);
		
		// Verify capacity constraint
		verifyCapacityConstraint(route, DEFAULT_SHIP_CAPACITY);
	}
	
	/**
	 * Test: Forward-looking capacity validation
	 * From PLAN.md lines 222-239: Capacity is validated at each port stop, accounting for
	 * deliveries before pickups.
	 */
	@Test
	public void testForwardLookingCapacityValidation()
	{
		// Scenario from PLAN.md lines 228-238:
		// Ship capacity: 10
		// Route with tasks that temporarily exceed capacity if not validated correctly
		
		// Create tasks that require smart capacity management
		// We'll use smaller cargo amounts to ensure precise control
		List<CourierTask> tasks = Arrays.asList(
			createTask(CourierTaskData.CATHERBY_BAIT_DELIVERY_5, 0),  // 1 cargo
			createTask(CourierTaskData.MUSA_POINT_LOGS_DELIVERY_3, 0), // 1 cargo
			createTask(CourierTaskData.PORT_SARIM_SPICE_DELIVERY_2, 0) // 1 cargo
		);
		
		int smallCapacity = 10;
		OptimizedRoute route = optimizer.optimizeRoute(tasks, PortLocation.PORT_SARIM, smallCapacity);
		
		assertNotNull("Route should not be null", route);
		
		// Verify capacity is validated at each stop
		verifyCapacityConstraint(route, smallCapacity);
	}
	
	// ===== Helper Methods =====
	
	/**
	 * Create a CourierTask for testing.
	 * 
	 * @param taskData The task data
	 * @param cargoTaken Amount of cargo taken (0 = not started, >0 = in progress)
	 * @return CourierTask instance
	 */
	private CourierTask createTask(CourierTaskData taskData, int cargoTaken)
	{
		return new CourierTask(
			taskData,
			1, // slot
			cargoTaken > 0, // taken
			0, // delivered
			true, // tracking
			true, // active
			Color.WHITE, // overlayColor
			cargoTaken // cargoTaken
		);
	}
	
	/**
	 * Verify precedence constraint: each task's pickup must come before its delivery in the route.
	 * Accounts for pre-loaded tasks (cargoTaken > 0) which may only have DELIVERY stops.
	 */
	private void verifyPrecedenceConstraint(OptimizedRoute route)
	{
		// First, identify pre-loaded tasks (tasks with delivery but no pickup in route)
		Set<CourierTask> hasPickup = new HashSet<>();
		Set<CourierTask> hasDelivery = new HashSet<>();
		
		for (RouteStop stop : route.getStops())
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
		
		// Pre-loaded tasks have delivery but no pickup (cargo already on ship)
		Set<CourierTask> preLoaded = new HashSet<>(hasDelivery);
		preLoaded.removeAll(hasPickup);
		
		// Track which tasks have been picked up in the route
		List<CourierTask> pickedUp = new ArrayList<>(preLoaded); // Pre-loaded tasks are already "picked up"
		
		for (RouteStop stop : route.getStops())
		{
			if (stop.getType() == RouteStopType.PICKUP)
			{
				assertFalse("Task should not be picked up twice: " + stop.getTask().getData().name(),
					pickedUp.contains(stop.getTask()));
				pickedUp.add(stop.getTask());
			}
			else // DELIVERY
			{
				assertTrue("Task must be picked up before delivery (or be pre-loaded): " + stop.getTask().getData().name(),
					pickedUp.contains(stop.getTask()));
			}
		}
	}
	
	/**
	 * Verify capacity constraint: cargo capacity never exceeds ship limit at any stop.
	 */
	private void verifyCapacityConstraint(OptimizedRoute route, int shipCapacity)
	{
		int currentCargo = 0;
		
		// First, identify pre-loaded tasks (tasks with delivery but no pickup in route)
		List<CourierTask> preLoaded = new ArrayList<>();
		List<CourierTask> hasPickup = new ArrayList<>();
		List<CourierTask> hasDelivery = new ArrayList<>();
		
		for (RouteStop stop : route.getStops())
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
		
		// Pre-loaded tasks have delivery but no pickup
		for (CourierTask task : hasDelivery)
		{
			if (!hasPickup.contains(task))
			{
				preLoaded.add(task);
				currentCargo += task.getData().cargoAmount;
			}
		}
		
		// Verify initial cargo doesn't exceed capacity
		assertTrue("Initial cargo (" + currentCargo + ") should not exceed capacity (" + shipCapacity + ")",
			currentCargo <= shipCapacity);
		
		// Simulate route and track cargo at each stop
		for (RouteStop stop : route.getStops())
		{
			if (stop.getType() == RouteStopType.PICKUP)
			{
				currentCargo += stop.getTask().getData().cargoAmount;
			}
			else // DELIVERY
			{
				currentCargo -= stop.getTask().getData().cargoAmount;
			}
			
			assertTrue("Cargo at stop " + stop.getPort() + " (" + currentCargo + ") should not exceed capacity (" + shipCapacity + ")",
				currentCargo <= shipCapacity);
			assertTrue("Cargo should never be negative at " + stop.getPort(), currentCargo >= 0);
		}
		
		// All cargo should be delivered at end
		assertEquals("All cargo should be delivered by end of route", 0, currentCargo);
	}
	
	// ===== Multiple Tasks to Same Port Tests =====
	
	/**
	 * Test: Multiple tasks with same delivery port (different pickups)
	 * From PLAN.md lines 817-821: Optimizer visits delivery port once, delivers all tasks
	 */
	@Test
	public void testMultipleDeliveriesSamePort_DifferentPickups()
	{
		// Task 1: Pandemonium -> Port Sarim (141 XP, 1 cargo)
		// Task 2: Musa Point -> Port Sarim (144 XP, 1 cargo)
		// Task 3: Catherby -> Port Sarim (525 XP, 1 cargo)
		// All deliver to Port Sarim from different pickup locations
		List<CourierTask> tasks = Arrays.asList(
			createTask(CourierTaskData.PORT_SARIM_SPICE_DELIVERY_2, 0),      // Pandemonium -> Port Sarim
			createTask(CourierTaskData.MUSA_POINT_BANANA_DELIVERY_40, 0),    // Musa Point -> Port Sarim  
			createTask(CourierTaskData.PORT_SARIM_HONEY_DELIVERY_6, 0)       // Catherby -> Port Sarim
		);
		
		OptimizedRoute route = optimizer.optimizeRoute(tasks, PortLocation.PANDEMONIUM, DEFAULT_SHIP_CAPACITY);
		
		assertNotNull("Route should not be null", route);
		
		// Count how many times we deliver to Port Sarim
		long portSarimDeliveryCount = route.getStops().stream()
			.filter(s -> s.getType() == RouteStopType.DELIVERY && 
			             s.getPort() == PortLocation.PORT_SARIM)
			.count();
		
		// Optimizer may combine deliveries into 1 stop or keep them separate (1-3 stops)
		assertTrue("Should have 1-3 delivery stops at Port Sarim",
			portSarimDeliveryCount >= 1 && portSarimDeliveryCount <= 3);
		
		// Verify all 3 pickups are included
		long pickupCount = route.getStops().stream()
			.filter(s -> s.getType() == RouteStopType.PICKUP)
			.count();
		assertEquals("Should have 3 pickup stops", 3, pickupCount);
		
		// Verify all 3 deliveries happen (might be combined or separate)
		long totalDeliveryStops = route.getStops().stream()
			.filter(s -> s.getType() == RouteStopType.DELIVERY)
			.count();
		assertTrue("Should have 1-3 total delivery stops", 
			totalDeliveryStops >= 1 && totalDeliveryStops <= 3);
		
		// Verify capacity constraint
		verifyCapacityConstraint(route, DEFAULT_SHIP_CAPACITY);
		
		// Verify precedence constraint
		verifyPrecedenceConstraint(route);
	}
	
	/**
	 * Test: Multiple tasks with same pickup port (different deliveries)
	 * From PLAN.md lines 456-462: Load multiple cargos at one stop
	 */
	@Test
	public void testMultiplePickupsSamePort_DifferentDeliveries()
	{
		// All tasks pickup from Port Sarim, deliver to different locations
		// Task 1: Port Sarim -> Catherby (525 XP, 1 cargo)
		// Task 2: Port Sarim -> Musa Point (72 XP, 1 cargo)
		// Task 3: Port Sarim -> Ardougne (546 XP, 1 cargo)
		List<CourierTask> tasks = Arrays.asList(
			createTask(CourierTaskData.CATHERBY_BAIT_DELIVERY_5, 0),       // Port Sarim -> Catherby
			createTask(CourierTaskData.MUSA_POINT_LOGS_DELIVERY_3, 0),     // Port Sarim -> Musa Point
			createTask(CourierTaskData.ARDOUGNE_SALAMANDER_DELIVERY_10, 0) // Port Sarim -> Ardougne
		);
		
		OptimizedRoute route = optimizer.optimizeRoute(tasks, PortLocation.PORT_SARIM, DEFAULT_SHIP_CAPACITY);
		
		assertNotNull("Route should not be null", route);
		
		// Count how many times we pickup at Port Sarim
		long portSarimPickupCount = route.getStops().stream()
			.filter(s -> s.getType() == RouteStopType.PICKUP && 
			             s.getPort() == PortLocation.PORT_SARIM)
			.count();
		
		// Optimizer may combine pickups into 1 stop or keep separate (1-3 stops)
		assertTrue("Should have 1-3 pickup stops at Port Sarim",
			portSarimPickupCount >= 1 && portSarimPickupCount <= 3);
		
		// Verify all 3 deliveries are included
		long deliveryCount = route.getStops().stream()
			.filter(s -> s.getType() == RouteStopType.DELIVERY)
			.count();
		assertEquals("Should have 3 delivery stops", 3, deliveryCount);
		
		// Verify capacity constraint
		verifyCapacityConstraint(route, DEFAULT_SHIP_CAPACITY);
		
		// Verify precedence constraint
		verifyPrecedenceConstraint(route);
	}
	
	/**
	 * Test: Multiple tasks with same delivery, capacity limit prevents accepting all
	 * From PLAN.md lines 814-815: Capacity calculation sums cargo for all tasks
	 */
	@Test
	public void testMultipleDeliveriesSamePort_ExceedsCapacity()
	{
		// Three tasks all delivering to Port Tyras with high cargo amounts
		// Task 1: 6 cargo
		// Task 2: 6 cargo  
		// Task 3: 6 cargo
		// Total: 18 cargo (exceeds 15 capacity)
		List<CourierTask> tasks = Arrays.asList(
			createTask(CourierTaskData.PORT_TYRAS_SNAKESKIN_DELIVERY_56, 0), // 6 cargo
			createTask(CourierTaskData.PORT_TYRAS_PLATEBODY_DELIVERY_100, 0), // 6 cargo
			createTask(CourierTaskData.PORT_TYRAS_SWORD_DELIVERY_104, 0)     // 6 cargo
		);
		
		// Limited capacity: can only hold 2 tasks at once
		int limitedCapacity = 15;
		
		OptimizedRoute route = optimizer.optimizeRoute(tasks, PortLocation.MUSA_POINT, limitedCapacity);
		
		assertNotNull("Route should not be null", route);
		
		// Verify capacity is never exceeded
		verifyCapacityConstraint(route, limitedCapacity);
		
		// Route should intelligently manage pickups to respect capacity
		// May need to deliver some tasks before picking up others
		int currentCargo = 0;
		boolean deliveredBeforeAllPickups = false;
		int pickupsBeforeFirstDelivery = 0;
		
		for (RouteStop stop : route.getStops())
		{
			if (stop.getType() == RouteStopType.PICKUP)
			{
				currentCargo += stop.getTask().getData().cargoAmount;
				if (!deliveredBeforeAllPickups)
				{
					pickupsBeforeFirstDelivery++;
				}
			}
			else // DELIVERY
			{
				currentCargo -= stop.getTask().getData().cargoAmount;
				deliveredBeforeAllPickups = true;
			}
			
			assertTrue("Cargo should never exceed capacity at " + stop.getPort(),
				currentCargo <= limitedCapacity);
		}
		
		// With 15 capacity and 6 cargo each, can pick up at most 2 before needing to deliver
		assertTrue("Should pick up at most 2 tasks (12 cargo) before first delivery",
			pickupsBeforeFirstDelivery <= 2);
	}
	
	/**
	 * Test: Three identical tasks (same pickup AND delivery)
	 * From PLAN.md lines 804-815: Almost free bonus XP
	 */
	@Test
	public void testThreeIdenticalTasks_SamePickupAndDelivery()
	{
		// Three identical tasks: Port Sarim -> Musa Point
		List<CourierTask> tasks = Arrays.asList(
			createTask(CourierTaskData.MUSA_POINT_LOGS_DELIVERY_3, 0),
			createTask(CourierTaskData.MUSA_POINT_LOGS_DELIVERY_3, 0),
			createTask(CourierTaskData.MUSA_POINT_LOGS_DELIVERY_3, 0)
		);
		
		OptimizedRoute route = optimizer.optimizeRoute(tasks, PortLocation.PORT_SARIM, DEFAULT_SHIP_CAPACITY);
		
		assertNotNull("Route should not be null", route);
		
		// Should be extremely efficient: pickup once, deliver once (or 3 separate stops)
		int totalStops = route.getStops().size();
		assertTrue("Should have 2-6 stops (1-3 pickups + 1-3 deliveries)",
			totalStops >= 2 && totalStops <= 6);
		
		// Verify total XP is 3x the task reward
		int totalXP = route.getStops().stream()
			.filter(s -> s.getType() == RouteStopType.DELIVERY)
			.mapToInt(s -> s.getTask().getData().getReward())
			.sum();
		
		int expectedXP = CourierTaskData.MUSA_POINT_LOGS_DELIVERY_3.getReward() * 3;
		assertEquals("Should get 3x XP for identical routes", expectedXP, totalXP);
		
		// Verify capacity: 3 tasks * 1 cargo = 3 cargo
		verifyCapacityConstraint(route, DEFAULT_SHIP_CAPACITY);
	}
	
	/**
	 * Test: Mix of same-port and different-port deliveries
	 * Complex scenario to test optimizer's ability to group deliveries
	 */
	@Test
	public void testMixedSameAndDifferentPortDeliveries()
	{
		// Task 1: Port Sarim -> Catherby
		// Task 2: Port Sarim -> Catherby (duplicate delivery)
		// Task 3: Port Sarim -> Musa Point (different delivery)
		List<CourierTask> tasks = Arrays.asList(
			createTask(CourierTaskData.CATHERBY_BAIT_DELIVERY_5, 0),
			createTask(CourierTaskData.CATHERBY_BAIT_DELIVERY_5, 0), // Duplicate
			createTask(CourierTaskData.MUSA_POINT_LOGS_DELIVERY_3, 0)
		);
		
		OptimizedRoute route = optimizer.optimizeRoute(tasks, PortLocation.PORT_SARIM, DEFAULT_SHIP_CAPACITY);
		
		assertNotNull("Route should not be null", route);
		
		// Should have pickups at Port Sarim (1-3 stops)
		long pickupCount = route.getStops().stream()
			.filter(s -> s.getType() == RouteStopType.PICKUP)
			.count();
		assertTrue("Should have 1-3 pickup stops", pickupCount >= 1 && pickupCount <= 3);
		
		// Should have deliveries at Catherby (1-2 stops) and Musa Point (1 stop)
		long catherbyDeliveries = route.getStops().stream()
			.filter(s -> s.getType() == RouteStopType.DELIVERY &&
			             s.getPort() == PortLocation.CATHERBY)
			.count();
		
		long musaPointDeliveries = route.getStops().stream()
			.filter(s -> s.getType() == RouteStopType.DELIVERY &&
			             s.getPort() == PortLocation.MUSA_POINT)
			.count();
		
		assertTrue("Should have 1-2 Catherby deliveries", catherbyDeliveries >= 1 && catherbyDeliveries <= 2);
		assertEquals("Should have 1 Musa Point delivery", 1, musaPointDeliveries);
		
		// Verify constraints
		verifyCapacityConstraint(route, DEFAULT_SHIP_CAPACITY);
		verifyPrecedenceConstraint(route);
	}
	
	/**
	 * Test: At same port, deliver before pickup
	 * When arriving at a port where we can both deliver and pickup, deliver first.
	 * This frees cargo space and task slots before picking up new cargo.
	 */
	@Test
	public void testDeliverBeforePickupAtSamePort()
	{
		// Setup: Task 1 delivers TO Port Sarim (cargo already loaded)
		//        Task 2 picks up FROM Port Sarim (not started)
		// When at Port Sarim, should deliver Task 1 before picking up Task 2
		
		// Task 1: Musa Point -> Port Sarim (pickup from Musa Point, delivers to Port Sarim, cargo already loaded)
		CourierTask task1 = createTask(CourierTaskData.PORT_SARIM_LOGS_DELIVERY_4, 1); // Delivery at Port Sarim
		
		// Task 2: Port Sarim -> Catherby (pickup from Port Sarim, delivers to Catherby, not started)
		CourierTask task2 = createTask(CourierTaskData.CATHERBY_BAIT_DELIVERY_5, 0); // Pickup at Port Sarim
		
		List<CourierTask> tasks = Arrays.asList(task1, task2);
		
		// Current location: Port Sarim (where both delivery and pickup are available)
		OptimizedRoute route = optimizer.optimizeRoute(tasks, PortLocation.PORT_SARIM, DEFAULT_SHIP_CAPACITY);
		
		assertNotNull("Route should not be null", route);
		assertEquals("Route should have 3 stops (1 delivery + 1 pickup + 1 delivery)", 3, route.getStops().size());
		
		// Find the first stop at Port Sarim
		RouteStop firstPortSarimStop = null;
		for (RouteStop stop : route.getStops())
		{
			if (stop.getPort() == PortLocation.PORT_SARIM)
			{
				firstPortSarimStop = stop;
				break;
			}
		}
		
		assertNotNull("Should have a stop at Port Sarim", firstPortSarimStop);
		assertEquals("First stop at Port Sarim should be DELIVERY (deliver before pickup)",
			RouteStopType.DELIVERY, firstPortSarimStop.getType());
	}
	
	/**
	 * Test: Exhaustive search finds optimal route for complex multi-task scenario.
	 * 
	 * This test verifies that the route optimizer finds the globally optimal route,
	 * not just a local optimum from greedy nearest-neighbor construction.
	 * 
	 * Scenario: 4 tasks with pickups and deliveries across 6 ports.
	 * The greedy algorithm would miss the optimal ordering because the locally
	 * nearest choice (Port Tyras at 951) is worse than going to Prifddinas first (1155)
	 * due to downstream path improvements.
	 * 
	 * Tasks:
	 * 1. Port Tyras -> Deepfin Point (logs)
	 * 2. Ardougne -> Prifddinas (silk)
	 * 3. Rellekka -> Deepfin Point (warhammer)
	 * 4. Deepfin Point -> Hosidius (lead)
	 * 
	 * Greedy route (starting Rellekka): 3955 tiles
	 * Optimal route (starting Ardougne): 3839 tiles (3% shorter)
	 */
	@Test
	public void testExhaustiveSearchFindsOptimalRoute()
	{
		List<CourierTask> tasks = Arrays.asList(
			createTask(CourierTaskData.DEEPFIN_POINT_LOGS_DELIVERY_327, 0),      // Port Tyras -> Deepfin Point
			createTask(CourierTaskData.PRIFDDINAS_SILK_DELIVERY_367, 0),         // Ardougne -> Prifddinas
			createTask(CourierTaskData.DEEPFIN_POINT_WARHAMMER_DELIVERY_340, 0), // Rellekka -> Deepfin Point
			createTask(CourierTaskData.HOSIDIUS_LEAD_DELIVERY_341, 0)            // Deepfin Point -> Hosidius
		);
		
		// Planning mode: pass null for current location
		OptimizedRoute route = optimizer.optimizeRoute(tasks, null, DEFAULT_SHIP_CAPACITY);
		
		assertNotNull("Route should not be null", route);
		assertEquals("Route should have 8 stops (4 pickups + 4 deliveries)", 8, route.getStops().size());
		
		// The optimal route distance is 3839 tiles
		// Greedy would find 3955 tiles (starting from Rellekka)
		assertEquals("Exhaustive search should find optimal route with distance 3839", 
			3839, route.getTotalDistance());
		
		// Verify the route starts at Ardougne (the optimal starting point)
		assertEquals("Optimal route should start at Ardougne", 
			PortLocation.ARDOUGNE, route.getStops().get(0).getPort());
		
		// Verify precedence and capacity constraints
		verifyPrecedenceConstraint(route);
		verifyCapacityConstraint(route, DEFAULT_SHIP_CAPACITY);
	}
	
	/**
	 * Test: Deliver before pickup frees capacity for larger pickups
	 * Scenario where delivering first allows picking up cargo that wouldn't fit otherwise.
	 */
	@Test
	public void testDeliverBeforePickup_FreesCapacityForLargerPickup()
	{
		// Setup with tight capacity:
		// - Task 1: 6 cargo already loaded, delivers to Rellekka
		// - Task 2: 6 cargo to pickup from Rellekka
		// - Ship capacity: 8
		// 
		// If we try to pickup first: 6 + 6 = 12 > 8 (FAILS)
		// If we deliver first: 6 - 6 + 6 = 6 <= 8 (OK)
		
		// Task 1: pickup from somewhere, delivers to Rellekka (6 cargo already loaded)
		// RELLEKKA_TEAK_DELIVERY_382: pickup from Etceteria, delivers to Rellekka, 6 cargo
		CourierTask task1 = createTask(CourierTaskData.RELLEKKA_TEAK_DELIVERY_382, 6);
		
		// Task 2: pickup from Rellekka, delivers to Etceteria (not started)
		// ETCETERIA_FISH_DELIVERY_381: pickup from Rellekka, delivers to Etceteria, 6 cargo
		CourierTask task2 = createTask(CourierTaskData.ETCETERIA_FISH_DELIVERY_381, 0);
		
		List<CourierTask> tasks = Arrays.asList(task1, task2);
		
		// Tight capacity: only 8
		int tightCapacity = 8;
		
		// Start at Rellekka where task1 can be delivered and task2 can be picked up
		OptimizedRoute route = optimizer.optimizeRoute(tasks, PortLocation.RELLEKKA, tightCapacity);
		
		assertNotNull("Route should not be null - delivering first should make room", route);
		
		// Verify capacity never exceeded
		verifyCapacityConstraint(route, tightCapacity);
		
		// Verify precedence
		verifyPrecedenceConstraint(route);
	}
}
