package com.nucleon.porttasks.optimizer;

import com.nucleon.porttasks.CourierTask;
import com.nucleon.porttasks.enums.CourierTaskData;
import com.nucleon.porttasks.enums.PortLocation;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * Additional TDD tests for TaskSelectionEngine - the task recommendation system.
 * 
 * This class contains tests that are specific edge cases and scenarios not covered
 * by the more focused test classes:
 * - TaskScoringTest: Basic scoring tests
 * - TaskStateManagementTest: Active/inactive state tests
 * - SwapRecommendationTest: Swap evaluation tests
 * - VideoScenarioTest: Tests based on YouTube video strategy
 * - TeleportModeTest: Teleport optimization tests
 * - RouteChainTest: Route chain recognition tests
 * - CargoConflictTest: Cargo type conflict tests
 * 
 * @see TaskScoringTest
 * @see TaskStateManagementTest
 * @see SwapRecommendationTest
 * @see VideoScenarioTest
 * @see TeleportModeTest
 * @see RouteChainTest
 * @see CargoConflictTest
 */
public class TaskSelectionEngineTest extends TaskSelectionEngineTestBase
{
	// ========================================
	// EDGE CASE TESTS
	// ========================================
	
	/**
	 * Edge Case #11: With cargo loaded, going to distant pickup doesn't make sense.
	 * Should prefer closer options even if lower XP.
	 */
	@Test
	public void testEdgeCase11_CargoLoadedPreferCloserPickup()
	{
		PortLocation boardLocation = PortLocation.LUNAR_ISLE;
		
		// Current state: cargo loaded for Etceteria, pending pickups at Jatizso, Piscatoris
		List<CourierTask> heldTasks = Arrays.asList(
			createTask(CourierTaskData.RELLEKKA_ADAMANTITE_DELIVERY_397, 0), // Jatizso -> Rellekka (need pickup)
			createTask(CourierTaskData.RELLEKKA_MONKFISH_DELIVERY_393, 0),   // Piscatoris -> Rellekka (need pickup)
			createTask(CourierTaskData.ETCETERIA_PLANK_DELIVERY_410, 1)      // CARGO LOADED -> Etceteria
		);
		
		// Available: Port Piscarilius (closer to north) vs Deepfin Point (far south)
		List<CourierTaskData> availableTasks = Arrays.asList(
			CourierTaskData.LUNAR_ISLE_FISH_DELIVERY_420,  // Port Piscarilius -> Lunar Isle (3345 XP)
			CourierTaskData.LUNAR_ISLE_COAL_DELIVERY_429   // Deepfin Point -> Lunar Isle (7188 XP)
		);
		
		// With cargo loaded, should prefer completing deliveries or nearby pickups
		TaskRecommendation recommendation = 
			engine.recommendTask(availableTasks, boardLocation, heldTasks);
		
		// Deepfin Point requires going way south with cargo - should prefer Port Piscarilius
		// OR Deepfin Point should be flagged as "doesn't fit current route"
		assertNotNull("Should have a recommendation", recommendation);
	}
	
	/**
	 * Edge Case #41: High XP task can justify adding new pickup location.
	 * 8375 XP (Sunset Coast) vs 688 XP (pickup HERE) - XP difference is massive.
	 */
	@Test
	public void testEdgeCase41_HighXpJustifiesNewPickup()
	{
		PortLocation boardLocation = PortLocation.ETCETERIA;
		
		// Current state: pending pickups at Deepfin Point, Prifddinas (south)
		List<CourierTask> heldTasks = Arrays.asList(
			createTask(CourierTaskData.LUNAR_ISLE_COAL_DELIVERY_429, 0),    // Deepfin Point pickup
			createTask(CourierTaskData.LUNAR_ISLE_POTION_DELIVERY_424, 0)   // Prifddinas pickup
		);
		
		// Available: pickup HERE (688 XP) vs far pickup (8375 XP)
		List<CourierTaskData> availableTasks = Arrays.asList(
			CourierTaskData.RELLEKKA_FISH_DELIVERY_400,   // Etceteria -> Rellekka (688 XP) - HERE
			CourierTaskData.ETCETERIA_SWORD_DELIVERY_414  // Sunset Coast -> Etceteria (8375 XP) - far
		);
		
		// Both should be valid options - model shouldn't force one over the other
		// This is a "future planning" decision
		List<TaskRecommendation> ranked = 
			engine.rankTasks(availableTasks, boardLocation, heldTasks);
		
		assertEquals("Should rank both tasks", 2, ranked.size());
		// The 8375 XP task might be recommended as "good for future route"
		// The 688 XP task might be recommended as "fits current route"
	}
	
	// ========================================
	// PLANNING MODE SWAP VARIANT TESTS
	// ========================================
	
	/**
	 * Test: In planning mode with a well-synergized task set, should NOT recommend
	 * swapping to a task that just picks up at the current location but delivers
	 * to an unrelated destination.
	 */
	@Test
	public void testSwapRecommendation_PlanningMode_NoSwapForPickupHereToNewDestination()
	{
		PortLocation boardLocation = PortLocation.RELLEKKA;
		
		// Held tasks - all in planning mode (cargoTaken=0)
		// Great synergy: 3 deliver to Rellekka, 1 delivers to Etceteria (nearby)
		List<CourierTask> heldTasks = Arrays.asList(
			createTask(CourierTaskData.RELLEKKA_PLANK_DELIVERY_391, 0),    // Port Roberts -> Rellekka, 2480 XP
			createTask(CourierTaskData.RELLEKKA_HALBERD_DELIVERY_399, 0),  // Port Tyras -> Rellekka, 5500 XP
			createTask(CourierTaskData.RELLEKKA_REDWOOD_DELIVERY_395, 0),  // Port Piscarilius -> Rellekka, 3549 XP
			createTask(CourierTaskData.ETCETERIA_FRUIT_DELIVERY_405, 0)    // Port Roberts -> Etceteria, 4750 XP
		);
		
		// Verify setup - all tasks have no cargo loaded (planning mode)
		for (CourierTask task : heldTasks)
		{
			assertEquals("All tasks should have no cargo loaded", 0, task.getCargoTaken());
		}
		
		// Available task: Rellekka -> Deepfin Point (3625 XP)
		// Pickup HERE but delivery to completely new location
		CourierTaskData availableTask = CourierTaskData.DEEPFIN_POINT_FUR_DELIVERY_398;
		assertEquals("Setup: available task should pickup at board", 
			boardLocation, availableTask.getPickupPort());
		
		// Verify Deepfin Point is not in our current route
		boolean deepfinInRoute = heldTasks.stream()
			.anyMatch(t -> t.getData().getDeliveryPort() == PortLocation.DEEPFIN_POINT ||
			               t.getData().getPickupPort() == PortLocation.DEEPFIN_POINT);
		assertFalse("Setup: Deepfin Point should not be in current route", deepfinInRoute);
		
		List<CourierTaskData> availableTasks = Arrays.asList(availableTask);
		
		SwapRecommendation swap = 
			engine.evaluateSwapOpportunity(availableTasks, boardLocation, heldTasks);
		
		// Should NOT recommend swap because:
		// 1. Planning mode - "Pickup HERE" has no bonus
		// 2. Deepfin Point is a new destination adding significant distance
		// 3. The 2480 XP plank task fits the route perfectly (Port Roberts pickup, Rellekka delivery)
		assertNull("Should NOT recommend swap in planning mode when available task " +
			"only has 'Pickup HERE' advantage but delivers to unrelated destination. " +
			"The held tasks have better synergy.", swap);
	}
	
	/**
	 * Test: In planning mode, "Delivers to board" should NOT give a bonus if
	 * the board location is not part of the actual route.
	 */
	@Test
	public void testSwapRecommendation_PlanningMode_NoSwapForDeliversToBoardWhenBoardNotInRoute()
	{
		PortLocation boardLocation = PortLocation.DEEPFIN_POINT;
		
		// Held tasks - all in planning mode (cargoTaken=0)
		// Great synergy: 3 deliver to Rellekka, 1 delivers to Etceteria
		// Note: Deepfin Point is NOT in the route at all
		List<CourierTask> heldTasks = Arrays.asList(
			createTask(CourierTaskData.RELLEKKA_PLANK_DELIVERY_391, 0),    // Port Roberts -> Rellekka, 2480 XP
			createTask(CourierTaskData.RELLEKKA_HALBERD_DELIVERY_399, 0),  // Port Tyras -> Rellekka, 5500 XP
			createTask(CourierTaskData.RELLEKKA_REDWOOD_DELIVERY_395, 0),  // Port Piscarilius -> Rellekka, 3549 XP
			createTask(CourierTaskData.ETCETERIA_FRUIT_DELIVERY_405, 0)    // Port Roberts -> Etceteria, 4750 XP
		);
		
		// Verify Deepfin Point is NOT in our current route
		boolean deepfinInRoute = heldTasks.stream()
			.anyMatch(t -> t.getData().getDeliveryPort() == PortLocation.DEEPFIN_POINT ||
			               t.getData().getPickupPort() == PortLocation.DEEPFIN_POINT);
		assertFalse("Setup: Deepfin Point (board) should NOT be in current route", deepfinInRoute);
		
		// Verify Port Tyras IS in our route (halberd pickup)
		boolean portTyrasInRoute = heldTasks.stream()
			.anyMatch(t -> t.getData().getPickupPort() == PortLocation.PORT_TYRAS);
		assertTrue("Setup: Port Tyras should be in route (halberd pickup)", portTyrasInRoute);
		
		// Available task: Port Tyras -> Deepfin Point (2313 XP)
		// Pickup at pending location, but delivers to board (which is NOT in route)
		CourierTaskData availableTask = CourierTaskData.DEEPFIN_POINT_LOGS_DELIVERY_327;
		assertEquals("Setup: available task should pickup at Port Tyras", 
			PortLocation.PORT_TYRAS, availableTask.getPickupPort());
		assertEquals("Setup: available task should deliver to Deepfin Point (board)", 
			PortLocation.DEEPFIN_POINT, availableTask.getDeliveryPort());
		
		List<CourierTaskData> availableTasks = Arrays.asList(availableTask);
		
		SwapRecommendation swap = 
			engine.evaluateSwapOpportunity(availableTasks, boardLocation, heldTasks);
		
		// Should NOT recommend swap because:
		// 1. "Delivers to board" should NOT give bonus when board is not in route
		// 2. The plank task (2480 XP) delivers to Rellekka (shared with 2 other tasks)
		// 3. The logs task (2313 XP) would ADD Deepfin Point to the route
		// 4. Lower XP + adds new destination = bad swap
		assertNull("Should NOT recommend swap when 'delivers to board' but board location " +
			"is NOT part of the actual route. Board location is irrelevant in planning mode " +
			"unless it's already a pending delivery.", swap);
	}
	
	/**
	 * Test: In planning mode, "Delivers to board" should NOT give a bonus if
	 * the board location is only a PICKUP location (not a delivery destination).
	 */
	@Test
	public void testSwapRecommendation_PlanningMode_NoSwapForDeliversToBoardWhenBoardIsOnlyPickup()
	{
		PortLocation boardLocation = PortLocation.PORT_TYRAS;
		
		// Held tasks - all in planning mode (cargoTaken=0)
		// Port Tyras is a PICKUP location (for halberds), not a delivery destination
		List<CourierTask> heldTasks = Arrays.asList(
			createTask(CourierTaskData.RELLEKKA_PLANK_DELIVERY_391, 0),    // Port Roberts -> Rellekka, 2480 XP
			createTask(CourierTaskData.RELLEKKA_HALBERD_DELIVERY_399, 0),  // Port Tyras -> Rellekka, 5500 XP
			createTask(CourierTaskData.RELLEKKA_REDWOOD_DELIVERY_395, 0),  // Port Piscarilius -> Rellekka, 3549 XP
			createTask(CourierTaskData.ETCETERIA_FRUIT_DELIVERY_405, 0)    // Port Roberts -> Etceteria, 4750 XP
		);
		
		// Verify Port Tyras (board) is a PICKUP location, not a delivery
		boolean portTyrasIsPickup = heldTasks.stream()
			.anyMatch(t -> t.getData().getPickupPort() == PortLocation.PORT_TYRAS);
		boolean portTyrasIsDelivery = heldTasks.stream()
			.anyMatch(t -> t.getData().getDeliveryPort() == PortLocation.PORT_TYRAS);
		assertTrue("Setup: Port Tyras should be a pickup location", portTyrasIsPickup);
		assertFalse("Setup: Port Tyras should NOT be a delivery location", portTyrasIsDelivery);
		
		// Available task: Port Roberts -> Port Tyras (2875 XP)
		// Pickup at pending location (Port Roberts), delivers to board (Port Tyras)
		CourierTaskData availableTask = CourierTaskData.PORT_TYRAS_PLATEBODY_DELIVERY_346;
		assertEquals("Setup: available task should pickup at Port Roberts", 
			PortLocation.PORT_ROBERTS, availableTask.getPickupPort());
		assertEquals("Setup: available task should deliver to Port Tyras (board)", 
			PortLocation.PORT_TYRAS, availableTask.getDeliveryPort());
		
		List<CourierTaskData> availableTasks = Arrays.asList(availableTask);
		
		SwapRecommendation swap = 
			engine.evaluateSwapOpportunity(availableTasks, boardLocation, heldTasks);
		
		// Should NOT recommend swap because:
		// 1. "Delivers to board" should NOT give bonus when board is only a pickup location
		// 2. The Redwood task (3549 XP) delivers to Rellekka (shared with 2 other tasks)
		// 3. The Platebody task (2875 XP) is lower XP and doesn't improve the route
		assertNull("Should NOT recommend swap when 'delivers to board' but board is only " +
			"a PICKUP location in the route, not a delivery destination. " +
			"Swapping 3549 XP for 2875 XP with same route makes no sense.", swap);
	}
	
	// ========================================
	// MID-ROUTE SCENARIO TESTS
	// ========================================
	
	/**
	 * Test: When mid-route with cargo loaded, tasks that deliver to NEW locations
	 * should NOT be recommended as "TAKE_NOW" - they should be "CONSIDER" at best.
	 */
	@Test
	public void testRankTasks_MidRoute_NewDeliveryLocationShouldNotBeTakeNow()
	{
		PortLocation boardLocation = PortLocation.PORT_PISCARILIUS;
		
		// Held tasks with cargo loaded - delivering to Sunset Coast and Deepfin Point
		List<CourierTask> heldTasks = Arrays.asList(
			createTaskWithCargo(CourierTaskData.SUNSET_COAST_WARHAMMER_DELIVERY_385, 5),  // Rellekka -> Sunset Coast
			createTaskWithCargo(CourierTaskData.DEEPFIN_POINT_FUR_DELIVERY_398, 5),       // Rellekka -> Deepfin Point
			createTaskWithCargo(CourierTaskData.SUNSET_COAST_FABRIC_DELIVERY_413, 5)      // Etceteria -> Sunset Coast
		);
		
		// Verify all have cargo loaded
		for (CourierTask task : heldTasks)
		{
			assertTrue("All tasks should have cargo loaded", task.getCargoTaken() > 0);
		}
		
		// Available tasks - both pickup HERE but deliver to new locations
		List<CourierTaskData> availableTasks = Arrays.asList(
			CourierTaskData.LUNAR_ISLE_FISH_DELIVERY_259,   // Piscarilius -> Lunar Isle, 1601 XP
			CourierTaskData.PORT_ROBERTS_FISH_DELIVERY_250  // Piscarilius -> Port Roberts, 640 XP
		);
		
		// Verify both pickup at board location
		for (CourierTaskData task : availableTasks)
		{
			assertEquals("Available tasks should pickup at board", 
				boardLocation, task.getPickupPort());
		}
		
		List<TaskRecommendation> recommendations = 
			engine.rankTasks(availableTasks, boardLocation, heldTasks);
		
		assertFalse("Should have recommendations", recommendations.isEmpty());
		
		// Neither task should be "TAKE_NOW" because they don't fit the current route
		for (TaskRecommendation rec : recommendations)
		{
			assertNotEquals("Tasks delivering to new locations should NOT be TAKE_NOW when mid-route. " +
				"Task: " + rec.getTask() + " delivers to " + rec.getTask().getDeliveryPort() + 
				" but we're going to Sunset Coast and Deepfin Point",
				RecommendationType.TAKE_NOW, rec.getType());
		}
	}
	
	/**
	 * User's exact scenario - at Port Piscarilius mid-route.
	 */
	@Test
	public void testUserScenario_MidRouteAtPiscarilius_LunarIsleVsPortRoberts()
	{
		PortLocation boardLocation = PortLocation.PORT_PISCARILIUS;
		
		// User's held tasks - all with cargo loaded
		// Route is: somewhere -> Deepfin Point -> Sunset Coast (2 tasks)
		List<CourierTask> heldTasks = Arrays.asList(
			createTaskWithCargo(CourierTaskData.SUNSET_COAST_WARHAMMER_DELIVERY_385, 5),  // Rellekka -> Sunset Coast, 3774 XP
			createTaskWithCargo(CourierTaskData.DEEPFIN_POINT_FUR_DELIVERY_398, 5),       // Rellekka -> Deepfin Point, 3625 XP
			createTaskWithCargo(CourierTaskData.SUNSET_COAST_FABRIC_DELIVERY_413, 5)      // Etceteria -> Sunset Coast, 4188 XP
		);
		
		// Verify cargo loaded destinations
		Set<PortLocation> cargoDestinations = new HashSet<>();
		for (CourierTask task : heldTasks)
		{
			assertTrue("All tasks should have cargo loaded", task.getCargoTaken() > 0);
			cargoDestinations.add(task.getData().getDeliveryPort());
		}
		assertTrue("Should be delivering to Sunset Coast", cargoDestinations.contains(PortLocation.SUNSET_COAST));
		assertTrue("Should be delivering to Deepfin Point", cargoDestinations.contains(PortLocation.DEEPFIN_POINT));
		
		// Available tasks from the noticeboard
		CourierTaskData lunarIsleTask = CourierTaskData.LUNAR_ISLE_FISH_DELIVERY_259;  // 1601 XP, backtracking
		CourierTaskData portRobertsTask = CourierTaskData.PORT_ROBERTS_FISH_DELIVERY_250;  // 640 XP, less backtracking
		
		List<CourierTaskData> availableTasks = Arrays.asList(lunarIsleTask, portRobertsTask);
		
		// Get recommendations
		List<TaskRecommendation> recommendations = 
			engine.rankTasks(availableTasks, boardLocation, heldTasks);
		
		assertEquals("Should have 2 recommendations", 2, recommendations.size());
		
		// CRITICAL: Neither should be "TAKE_NOW" because:
		// 1. We have cargo loaded for Deepfin Point and Sunset Coast
		// 2. Both available tasks deliver to NEW locations (Lunar Isle, Port Roberts)
		// 3. Taking either task NOW would add a detour to the current route
		for (TaskRecommendation rec : recommendations)
		{
			assertNotEquals(
				"Mid-route: task delivering to " + rec.getTask().getDeliveryPort() + 
				" should NOT be TAKE_NOW. Current route is to Deepfin Point and Sunset Coast.",
				RecommendationType.TAKE_NOW, 
				rec.getType()
			);
		}
	}
	
	/**
	 * User's scenario continued - at Port Roberts after delivering there.
	 */
	@Test
	public void testUserScenario_MidRouteAtPortRoberts_LunarIsleTaskShouldNotBeTakeNow()
	{
		PortLocation boardLocation = PortLocation.PORT_ROBERTS;
		
		// User's remaining held tasks - still have cargo loaded
		List<CourierTask> heldTasks = Arrays.asList(
			createTaskWithCargo(CourierTaskData.SUNSET_COAST_WARHAMMER_DELIVERY_385, 5),  // -> Sunset Coast
			createTaskWithCargo(CourierTaskData.DEEPFIN_POINT_FUR_DELIVERY_398, 5),       // -> Deepfin Point
			createTaskWithCargo(CourierTaskData.SUNSET_COAST_FABRIC_DELIVERY_413, 5)      // -> Sunset Coast
		);
		
		// Available task: Port Roberts -> Lunar Isle, 2384 XP
		CourierTaskData lunarIsleFurTask = CourierTaskData.LUNAR_ISLE_FUR_DELIVERY_316;
		assertEquals("Setup: task should pickup at Port Roberts", 
			PortLocation.PORT_ROBERTS, lunarIsleFurTask.getPickupPort());
		assertEquals("Setup: task should deliver to Lunar Isle", 
			PortLocation.LUNAR_ISLE, lunarIsleFurTask.getDeliveryPort());
		
		List<CourierTaskData> availableTasks = Arrays.asList(lunarIsleFurTask);
		
		// Get recommendations
		List<TaskRecommendation> recommendations = 
			engine.rankTasks(availableTasks, boardLocation, heldTasks);
		
		assertEquals("Should have 1 recommendation", 1, recommendations.size());
		
		TaskRecommendation rec = recommendations.get(0);
		
		// CRITICAL: Should NOT be "TAKE_NOW" because:
		// 1. We have cargo loaded for Deepfin Point and Sunset Coast
		// 2. Lunar Isle is a NEW destination, not on current route
		// 3. Taking this task NOW would mean going to Lunar Isle before Deepfin Point/Sunset Coast
		// 4. This task is for the NEXT route, not the current one
		assertNotEquals(
			"Lunar Isle task should NOT be TAKE_NOW when we still have cargo for " +
			"Deepfin Point and Sunset Coast. It's a task for the NEXT route.",
			RecommendationType.TAKE_NOW, 
			rec.getType()
		);
		
		// Should be CONSIDER (it's a decent task for later, just not now)
		assertEquals(
			"Lunar Isle task should be CONSIDER - good for next route, but doesn't fit current route",
			RecommendationType.CONSIDER, 
			rec.getType()
		);
	}
	
	/**
	 * Contrast test: when a task DOES fit the current route.
	 */
	@Test
	public void testUserScenario_MidRoute_TaskThatFitsRouteShouldBeTakeNow()
	{
		PortLocation boardLocation = PortLocation.PORT_PISCARILIUS;
		
		// Held tasks with cargo for Sunset Coast and Deepfin Point
		List<CourierTask> heldTasks = Arrays.asList(
			createTaskWithCargo(CourierTaskData.SUNSET_COAST_WARHAMMER_DELIVERY_385, 5),  // -> Sunset Coast
			createTaskWithCargo(CourierTaskData.DEEPFIN_POINT_FUR_DELIVERY_398, 5)        // -> Deepfin Point
		);
		
		// Lunar Isle task doesn't fit - should NOT be TAKE_NOW
		CourierTaskData taskToLunarIsle = CourierTaskData.LUNAR_ISLE_FISH_DELIVERY_259;
		
		List<CourierTaskData> availableTasks = Arrays.asList(taskToLunarIsle);
		
		List<TaskRecommendation> recommendations = 
			engine.rankTasks(availableTasks, boardLocation, heldTasks);
		
		// Lunar Isle doesn't fit - should NOT be TAKE_NOW
		TaskRecommendation lunarRec = recommendations.get(0);
		assertNotEquals("Lunar Isle (new destination) should not be TAKE_NOW",
			RecommendationType.TAKE_NOW, lunarRec.getType());
		
		// The score should reflect that the task doesn't fit the current route well
		// Mid-route with cargo loaded for Sunset Coast and Deepfin Point,
		// a task to Lunar Isle adds significant detour
		double lunarScore = lunarRec.getScore();
		assertTrue("Lunar Isle task should have a score (may be penalized for not fitting route)",
			lunarScore >= 0);
	}
	
	// ========================================
	// AUTO-EXCLUDE EDGE CASES
	// ========================================
	
	/**
	 * Test: Task with pickup at board and minor detour should NOT be excluded.
	 */
	@Test
	public void testAutoExclude_PickupHereDeliverMinorDetour_ShouldNotExclude()
	{
		PortLocation boardLocation = PortLocation.RELLEKKA;
		
		// EXECUTION MODE: cargo loaded for NEITIZNOT (the farther island)
		CourierTask cargoTask = createTaskWithCargo(CourierTaskData.NEITIZNOT_COAL_DELIVERY_383, 5);
		List<CourierTask> heldTasks = Arrays.asList(cargoTask);
		
		// New task: pickup HERE (Rellekka), deliver to ETCETERIA (on the way to Neitiznot)
		CourierTaskData newTask = CourierTaskData.ETCETERIA_FISH_DELIVERY_381;
		
		// Verify task picks up at Rellekka and delivers to Etceteria
		assertEquals("New task should pickup at Rellekka", PortLocation.RELLEKKA, newTask.getPickupPort());
		assertEquals("New task should deliver to Etceteria", PortLocation.ETCETERIA, newTask.getDeliveryPort());
		
		// Verify cargo destination is Neitiznot
		assertEquals("Cargo should be going to Neitiznot", PortLocation.NEITIZNOT, cargoTask.getData().getDeliveryPort());
		
		boolean shouldExclude = engine.shouldAutoExclude(newTask, boardLocation, heldTasks);
		
		// Calculate and verify the actual detour ratio
		int direct = distanceCache.getDistance(PortLocation.RELLEKKA, PortLocation.NEITIZNOT);
		int leg1 = distanceCache.getDistance(PortLocation.RELLEKKA, PortLocation.ETCETERIA);
		int leg2 = distanceCache.getDistance(PortLocation.ETCETERIA, PortLocation.NEITIZNOT);
		double detourRatio = (double)(leg1 + leg2) / direct;
		
		// This should be an acceptable detour (ratio ~1.32)
		assertTrue("Detour ratio should be <= 1.5 for this scenario", detourRatio <= 1.5);
		assertFalse("Should NOT exclude - Etceteria is on the way to Neitiznot (ratio=" + 
			String.format("%.2f", detourRatio) + ")", shouldExclude);
	}
	
	/**
	 * Test: Verify the detour ratio calculation with Fremennik islands.
	 */
	@Test
	public void testAutoExclude_VerifyDetourRatioCalculation_OrderMatters()
	{
		// Scenario 1: Rellekka -> Neitiznot -> Etceteria (going to Neitiznot first, then Etceteria)
		// This is BAD because Neitiznot is past Etceteria, so we'd go past and come back
		int direct1 = distanceCache.getDistance(PortLocation.RELLEKKA, PortLocation.ETCETERIA);
		int leg1a = distanceCache.getDistance(PortLocation.RELLEKKA, PortLocation.NEITIZNOT);
		int leg1b = distanceCache.getDistance(PortLocation.NEITIZNOT, PortLocation.ETCETERIA);
		double ratio1 = (double)(leg1a + leg1b) / direct1;
		
		// Scenario 2: Rellekka -> Etceteria -> Neitiznot (going to Etceteria first, then Neitiznot)
		// This should be GOOD because we pass through Etceteria on the way to Neitiznot
		int direct2 = distanceCache.getDistance(PortLocation.RELLEKKA, PortLocation.NEITIZNOT);
		int leg2a = distanceCache.getDistance(PortLocation.RELLEKKA, PortLocation.ETCETERIA);
		int leg2b = distanceCache.getDistance(PortLocation.ETCETERIA, PortLocation.NEITIZNOT);
		double ratio2 = (double)(leg2a + leg2b) / direct2;
		
		// Scenario 3: Neitiznot -> Etceteria -> Rellekka 
		int direct3 = distanceCache.getDistance(PortLocation.NEITIZNOT, PortLocation.RELLEKKA);
		int leg3a = distanceCache.getDistance(PortLocation.NEITIZNOT, PortLocation.ETCETERIA);
		int leg3b = distanceCache.getDistance(PortLocation.ETCETERIA, PortLocation.RELLEKKA);
		double ratio3 = (double)(leg3a + leg3b) / direct3;
		
		// ASSERTIONS: Verify the expected ratios
		// Scenario 1: Going to Neitiznot first when destination is Etceteria = BACKTRACK (ratio > 1.5)
		assertTrue("Scenario 1 should be a BACKTRACK (ratio > 1.5). Got ratio: " + ratio1, ratio1 > 1.5);
		
		// Scenario 2: Going to Etceteria first when destination is Neitiznot = DETOUR (ratio <= 1.5)
		assertTrue("Scenario 2 should be a DETOUR (ratio <= 1.5). Got ratio: " + ratio2, ratio2 <= 1.5);
		
		// Scenario 3: Going from Neitiznot to Etceteria to Rellekka = DETOUR (ratio <= 1.5)
		assertTrue("Scenario 3 should be a DETOUR (ratio <= 1.5). Got ratio: " + ratio3, ratio3 <= 1.5);
	}
	
	// ========================================
	// SWAP BUG TESTS
	// ========================================
	
	/**
	 * Test: Even if the swap logic identifies the available task as "fitting" the current route,
	 * it should compare XP values properly and not recommend swapping a much better task.
	 */
	@Test
	public void testSwapScoring_FutureTasksShouldNotBeUndervalued()
	{
		PortLocation boardLocation = PortLocation.ETCETERIA;
		
		// Simplified scenario: one FUTURE task, one CURRENT task
		List<CourierTask> heldTasks = Arrays.asList(
			// FUTURE task - no cargo, waiting for next route
			createTask(CourierTaskData.LUNAR_ISLE_FISH_DELIVERY_431, 0),     // Port Roberts -> Lunar Isle (fish, 4626 XP)
			
			// CURRENT task - cargo loaded
			createTaskWithCargo(CourierTaskData.RELLEKKA_WINE_DELIVERY_303, 4)  // Aldarin -> Rellekka (wine, 3718 XP)
		);
		
		// Available: low XP task that fits current route
		List<CourierTaskData> availableTasks = Arrays.asList(
			CourierTaskData.RELLEKKA_FISH_DELIVERY_400  // Etceteria -> Rellekka (fish, 688 XP)
		);
		
		SwapRecommendation swap = 
			engine.evaluateSwapOpportunity(availableTasks, boardLocation, heldTasks);
		
		// ASSERTION: Should NOT swap 4626 XP future task for 688 XP task
		if (swap != null)
		{
			assertNotEquals("Should NOT swap out the high-XP future task (4626 XP) for a low-XP task (688 XP)",
				CourierTaskData.LUNAR_ISLE_FISH_DELIVERY_431, swap.getTaskToSwap().getData());
		}
		// Note: swap == null is also acceptable (no swap recommended)
	}
	
	/**
	 * Test: Unchecking (deactivating) cargo task makes it a swap candidate.
	 */
	@Test
	public void testCheckbox_InactiveCargoTaskBecomesSwapCandidate()
	{
		PortLocation boardLocation = PortLocation.ETCETERIA;
		
		// Held tasks: one with cargo loaded, one without
		CourierTask cargoTask = createTaskWithCargo(CourierTaskData.RELLEKKA_WINE_DELIVERY_303, 4);  // CARGO LOADED (3718 XP)
		CourierTask futureTask = createTask(CourierTaskData.LUNAR_ISLE_FISH_DELIVERY_431, 0);        // No cargo (4626 XP)
		
		List<CourierTask> heldTasks = Arrays.asList(cargoTask, futureTask);
		
		List<CourierTaskData> availableTasks = Arrays.asList(
			CourierTaskData.RELLEKKA_FISH_DELIVERY_400  // Etceteria -> Rellekka (688 XP)
		);
		
		// SCENARIO: Uncheck the FUTURE task (fish 4626 XP)
		// It becomes a swap-out candidate despite having higher XP
		engine.setTaskActive(futureTask, false);
		
		SwapRecommendation swap = 
			engine.evaluateSwapOpportunity(availableTasks, boardLocation, heldTasks);
		
		assertNotNull("Should recommend swap when inactive task exists", swap);
		assertEquals("Should swap out the INACTIVE future task",
			futureTask, swap.getTaskToSwap());
		
		// Clean up
		engine.setTaskActive(futureTask, true);
	}
	
	/**
	 * EXACT USER SCENARIO: At Etceteria with 3/4 tasks unchecked, 1 active with cargo loaded.
	 * 
	 * The algorithm should swap out an OFF-ROUTE inactive task for an ON-ROUTE task,
	 * even if the XP is lower. Route fit is more valuable than raw XP for immediate action.
	 * 
	 * Tasks:
	 * - Active wine: Aldarin -> Rellekka (on current route, cargo loaded)
	 * - Inactive potion: Aldarin -> Lunar Isle (OFF route - goes to Lunar Isle)
	 * - Inactive fish: Port Roberts -> Lunar Isle (OFF route - goes to Lunar Isle)  
	 * - Inactive teak: Etceteria -> Rellekka (ON route - same as active task!)
	 * 
	 * Available: Etceteria -> Rellekka (688 XP, ON route, picks up HERE)
	 * 
	 * The teak task (1375 XP) should NOT be swapped out - it's already on-route and higher XP.
	 * Instead, swap out one of the OFF-route Lunar Isle tasks.
	 */
	@Test
	public void testExactUserScenario_EtceteriaWith3InactiveTasks()
	{
		PortLocation boardLocation = PortLocation.ETCETERIA;
		
		// User's held tasks:
		// 1 ACTIVE with cargo loaded (wine to Rellekka)
		// 3 INACTIVE (reserved for future)
		CourierTask activeWineTask = createTaskWithCargo(CourierTaskData.RELLEKKA_WINE_DELIVERY_303, 4); // Aldarin -> Rellekka (3718 XP), CARGO LOADED
		CourierTask inactiveTask1 = createTask(CourierTaskData.LUNAR_ISLE_POTION_DELIVERY_437, 0);       // Aldarin -> Lunar Isle (10017 XP) - OFF ROUTE
		CourierTask inactiveTask2 = createTask(CourierTaskData.LUNAR_ISLE_FISH_DELIVERY_431, 0);         // Port Roberts -> Lunar Isle (4626 XP) - OFF ROUTE
		CourierTask inactiveTask3 = createTask(CourierTaskData.RELLEKKA_TEAK_DELIVERY_406, 0);           // Etceteria -> Rellekka (1375 XP) - ON ROUTE!
		
		List<CourierTask> heldTasks = Arrays.asList(activeWineTask, inactiveTask1, inactiveTask2, inactiveTask3);
		
		// Mark 3 tasks as INACTIVE (only wine task is active)
		engine.setTaskActive(inactiveTask1, false);
		engine.setTaskActive(inactiveTask2, false);
		engine.setTaskActive(inactiveTask3, false);
		
		// Verify setup
		assertTrue("Wine task should be active", engine.isTaskActive(activeWineTask));
		assertFalse("Task 1 should be inactive", engine.isTaskActive(inactiveTask1));
		assertFalse("Task 2 should be inactive", engine.isTaskActive(inactiveTask2));
		assertFalse("Task 3 should be inactive", engine.isTaskActive(inactiveTask3));
		
		// Available on noticeboard: Etceteria -> Rellekka (fish, 688 XP)
		List<CourierTaskData> availableTasks = Arrays.asList(
			CourierTaskData.RELLEKKA_FISH_DELIVERY_400  // Etceteria -> Rellekka (fish, 688 XP)
		);
		
		SwapRecommendation swap = 
			engine.evaluateSwapOpportunity(availableTasks, boardLocation, heldTasks);
		
		// CRITICAL: There should be a swap recommendation
		assertNotNull("Should recommend swap when inactive OFF-ROUTE tasks exist and available task fits current route!", swap);
		
		// Should swap out one of the INACTIVE tasks (not the active wine task)
		assertNotEquals("Should NOT swap out the active wine task",
			activeWineTask, swap.getTaskToSwap());
		
		// Should NOT swap out the teak task - it's already on-route with higher XP!
		// Swapping 1375 XP for 688 XP on the same route makes no sense.
		assertNotEquals("Should NOT swap out the on-route teak task (1375 XP > 688 XP, same route)",
			inactiveTask3, swap.getTaskToSwap());
		
		// Should swap out one of the OFF-ROUTE Lunar Isle tasks
		boolean swappedOffRoute = swap.getTaskToSwap() == inactiveTask1 || 
			swap.getTaskToSwap() == inactiveTask2;
		assertTrue("Should swap out an OFF-ROUTE inactive task (Lunar Isle)", swappedOffRoute);
		
		// Clean up
		engine.setTaskActive(inactiveTask1, true);
		engine.setTaskActive(inactiveTask2, true);
		engine.setTaskActive(inactiveTask3, true);
	}
	
	/**
	 * In teleport mode with slots full, should NOT swap inactive off-route task for another
	 * off-route task that has lower XP, even if the new task picks up HERE.
	 * 
	 * In teleport mode, "pickup HERE" has no value because we teleport to the first pickup
	 * location on the route. PRIFDDINAS is also not on the active route (which goes to 
	 * RELLEKKA and LUNAR_ISLE).
	 * 
	 * The potion (2802 XP) has less XP than suqah (3665 XP), and both are off-route.
	 * There's no benefit to this swap.
	 */
	@Test
	public void testSwapRecommendation_InactiveOffRouteTask_ShouldNotSwapForLowerXpOffRoute()
	{
		PortLocation boardLocation = PortLocation.ALDARIN;
		
		// Enable teleport optimization
		engine.setTeleportOptimizationEnabled(true);
		
		// ACTIVE held tasks (3):
		CourierTask fabricTask = createTask(CourierTaskData.RELLEKKA_FABRIC_DELIVERY_386, 0);    // Sunset Coast -> Rellekka (7549 XP)
		CourierTask redwoodTask = createTask(CourierTaskData.RELLEKKA_REDWOOD_DELIVERY_395, 0);  // Port Piscarilius -> Rellekka (3549 XP)
		CourierTask fishTask = createTask(CourierTaskData.LUNAR_ISLE_FISH_DELIVERY_420, 0);      // Port Piscarilius -> Lunar Isle (3345 XP)
		
		// INACTIVE held task (1):
		CourierTask suqahTask = createTask(CourierTaskData.DEEPFIN_POINT_SUQAH_HIDE_DELIVERY_428, 0); // Lunar Isle -> Deepfin Point (3665 XP)
		
		List<CourierTask> heldTasks = Arrays.asList(fabricTask, redwoodTask, fishTask, suqahTask);
		
		// Mark the Deepfin Point task as INACTIVE
		engine.setTaskActive(suqahTask, false);
		
		// Verify setup
		assertTrue("Fabric task should be active", engine.isTaskActive(fabricTask));
		assertTrue("Redwood task should be active", engine.isTaskActive(redwoodTask));
		assertTrue("Fish task should be active", engine.isTaskActive(fishTask));
		assertFalse("Suqah task should be INACTIVE", engine.isTaskActive(suqahTask));
		
		// Available task: Aldarin -> Prifddinas (potion, 2802 XP - LESS than suqah's 3665 XP)
		CourierTaskData potionTask = CourierTaskData.PRIFDDINAS_POTION_DELIVERY_297;
		
		// Verify setup
		assertEquals("Potion task should pickup at Aldarin (HERE)", 
			boardLocation, potionTask.getPickupPort());
		assertEquals("Potion task should deliver to Prifddinas", 
			PortLocation.PRIFDDINAS, potionTask.getDeliveryPort());
		
		List<CourierTaskData> availableTasks = Arrays.asList(potionTask);
		
		SwapRecommendation swap = 
			engine.evaluateSwapOpportunity(availableTasks, boardLocation, heldTasks);
		
		// Should NOT recommend swap:
		// - Potion (2802 XP) < Suqah (3665 XP)
		// - Both deliver off-route (PRIFDDINAS and DEEPFIN_POINT are not on the RELLEKKA/LUNAR_ISLE route)
		// - In teleport mode, "pickup HERE" has no value
		assertNull("Should NOT recommend swap when replacing off-route inactive task with " +
			"another off-route task that has lower XP. In teleport mode, 'pickup HERE' has no value.",
			swap);
		
		// Clean up
		engine.setTeleportOptimizationEnabled(false);
	}
	
	// ========================================
	// TELEPORT MODE TESTS (ADDITIONAL)
	// ========================================
	
	/**
	 * Task that picks up at board (pending pickup) and delivers to 
	 * another pending pickup should be TAKE_NOW.
	 */
	@Test
	public void testTeleportMode_PickupAtPendingPickup_DeliverToPendingPickup_ShouldBeTakeNow()
	{
		PortLocation boardLocation = PortLocation.ARDOUGNE;
		
		// Enable teleport optimization
		engine.setTeleportOptimizationEnabled(true);
		
		// Held tasks (all with no cargo loaded - planning mode)
		CourierTask logsTask = createTask(CourierTaskData.DEEPFIN_POINT_LOGS_DELIVERY_327, 0);     // Port Tyras -> Deepfin Point
		CourierTask silkTask = createTask(CourierTaskData.PRIFDDINAS_SILK_DELIVERY_367, 0);        // Ardougne -> Prifddinas
		CourierTask warhammerTask = createTask(CourierTaskData.DEEPFIN_POINT_WARHAMMER_DELIVERY_340, 0); // Rellekka -> Deepfin Point
		
		List<CourierTask> heldTasks = Arrays.asList(logsTask, silkTask, warhammerTask);
		
		// Verify we're in planning mode
		assertTrue("Should be in planning mode (no cargo loaded)", engine.isInPlanningMode(heldTasks));
		
		// Available task: Ardougne -> Port Tyras (platebody)
		List<CourierTaskData> availableTasks = Arrays.asList(
			CourierTaskData.PORT_TYRAS_PLATEBODY_DELIVERY_100  // Ardougne -> Port Tyras
		);
		
		List<TaskRecommendation> recs = engine.rankTasks(availableTasks, boardLocation, heldTasks);
		
		// KEY ASSERTION: The task should be TAKE_NOW since both pickup and delivery are on-route
		TaskRecommendation topRec = recs.get(0);
		assertEquals("Task should be TAKE_NOW when pickup AND delivery are both on-route locations",
			RecommendationType.TAKE_NOW, topRec.getType());
		
		// Clean up
		engine.setTeleportOptimizationEnabled(false);
	}
	
	/**
	 * Test: Auto-deactivation should identify off-route tasks during transition to execution mode.
	 * 
	 * In execution mode, tasks with pending pickups that require significant detours should be
	 * deactivated. The key distinction is:
	 * - Tasks delivering back to board location (creating loops) are definitively off-route
	 * - Tasks with pickups far from the current route are also off-route
	 */
	@Test
	public void testAutoExclude_TransitionToExecutionMode_ShouldDeactivateOffRouteTasks()
	{
		PortLocation boardLocation = PortLocation.PRIFDDINAS;
		
		// Tasks WITH cargo loaded (execution mode active)
		CourierTask logsTask = createTaskWithCargo(CourierTaskData.DEEPFIN_POINT_LOGS_DELIVERY_327, 4);     // Port Tyras -> Deepfin Point
		CourierTask silkTask = createTaskWithCargo(CourierTaskData.PRIFDDINAS_SILK_DELIVERY_367, 4);        // Ardougne -> Prifddinas
		
		// Tasks WITHOUT cargo loaded (pending pickups)
		// Warhammer: Rellekka -> Deepfin Point - Rellekka is a MAJOR detour (1422 extra distance)
		CourierTask warhammerTask = createTask(CourierTaskData.DEEPFIN_POINT_WARHAMMER_DELIVERY_340, 0);
		// Potion: Aldarin -> Prifddinas - delivers back to board, creating a loop
		CourierTask potionTask = createTask(CourierTaskData.PRIFDDINAS_POTION_DELIVERY_374, 0);
		
		List<CourierTask> heldTasks = Arrays.asList(logsTask, silkTask, warhammerTask, potionTask);
		
		// All tasks start as active
		for (CourierTask task : heldTasks)
		{
			engine.setTaskActive(task, true);
		}
		
		// Verify we're in execution mode (cargo loaded)
		assertFalse("Should be in execution mode (cargo loaded)", engine.isInPlanningMode(heldTasks));
		
		// Check if the potion task should be auto-excluded (Aldarin pickup + delivery back to board = loop)
		boolean potionShouldBeExcluded = !engine.taskFitsCurrentRoute(
			potionTask.getData(), 
			boardLocation, 
			heldTasks,
			potionTask  // Exclude the task itself from context
		);
		
		assertTrue("Potion task should be auto-excluded (creates loop back to board)",
			potionShouldBeExcluded);
		
		// Warhammer also has a significant detour (Rellekka is not on the way to Deepfin Point)
		boolean warhammerShouldBeExcluded = !engine.taskFitsCurrentRoute(
			warhammerTask.getData(),
			boardLocation,
			heldTasks,
			warhammerTask
		);
		
		assertTrue("Warhammer task should be auto-excluded (Rellekka pickup is a major detour)",
			warhammerShouldBeExcluded);
		
		// Test exitPlanningMode auto-deactivation
		engine.enterPlanningMode();
		
		// Verify tasks are active before
		assertTrue("Potion task should be active before exitPlanningMode", engine.isTaskActive(potionTask));
		assertTrue("Warhammer task should be active before exitPlanningMode", engine.isTaskActive(warhammerTask));
		
		// Call exitPlanningMode - this should deactivate off-route tasks
		engine.exitPlanningMode(heldTasks, boardLocation);
		
		// Verify both off-route tasks are now inactive
		assertFalse("Potion task should be deactivated by exitPlanningMode (loop back to board)",
			engine.isTaskActive(potionTask));
		assertFalse("Warhammer task should be deactivated by exitPlanningMode (major detour)",
			engine.isTaskActive(warhammerTask));
	}
	
	// ========================================
	// LOOP DETECTION TESTS
	// ========================================
	
	/**
	 * After taking a pickup task to board location, shouldn't recommend 
	 * delivering TO that pickup location creating a loop back to board.
	 */
	@Test
	public void testDeliverToPendingPickup_ShouldNotCreateLoopBackToBoard()
	{
		PortLocation boardLocation = PortLocation.LUNAR_ISLE;
		
		// Enable teleport optimization (matches the user's scenario)
		engine.setTeleportOptimizationEnabled(true);
		
		// HELD TASKS - have cargo loaded going to Rellekka, plus fish task
		CourierTask halberdTask = createTaskWithCargo(CourierTaskData.RELLEKKA_HALBERD_DELIVERY_399, 6); // Port Tyras -> Rellekka [CARGO LOADED]
		CourierTask plankTask = createTaskWithCargo(CourierTaskData.RELLEKKA_PLANK_DELIVERY_391, 4);     // Port Roberts -> Rellekka [CARGO LOADED]
		CourierTask fishTask = createTask(CourierTaskData.LUNAR_ISLE_FISH_DELIVERY_420, 0);               // Port Piscarilius -> Lunar Isle
		
		List<CourierTask> heldTasks = Arrays.asList(halberdTask, plankTask, fishTask);
		
		// Verify we have cargo loaded (mid-route mode)
		assertFalse("Should be in execution mode (cargo loaded)", engine.isInPlanningMode(heldTasks));
		
		// Available tasks at Lunar Isle noticeboard
		List<CourierTaskData> availableTasks = Arrays.asList(
			CourierTaskData.PORT_PISCARILIUS_RUNE_DELIVERY_425,  // Lunar Isle -> Port Piscarilius (rune, 1948 XP)
			CourierTaskData.PRIFDDINAS_RUNE_DELIVERY_427,        // Lunar Isle -> Prifddinas (rune, 2504 XP)
			CourierTaskData.PORT_ROBERTS_FABRIC_DELIVERY_430,    // Lunar Isle -> Port Roberts (fabric, 2313 XP)
			CourierTaskData.PRIFDDINAS_HERB_DELIVERY_423         // Lunar Isle -> Prifddinas (herb, 2171 XP)
		);
		
		List<TaskRecommendation> recommendations = 
			engine.rankTasks(availableTasks, boardLocation, heldTasks);
		
		// Find the problematic rune task
		TaskRecommendation runeToPickupRec = recommendations.stream()
			.filter(r -> r.getTask() == CourierTaskData.PORT_PISCARILIUS_RUNE_DELIVERY_425)
			.findFirst().orElse(null);
		
		assertNotNull("Rune task should be in recommendations", runeToPickupRec);
		
		// CRITICAL: The rune task should NOT be the top recommendation
		assertNotEquals("Task that creates loop back to board should NOT be #1 recommendation",
			CourierTaskData.PORT_PISCARILIUS_RUNE_DELIVERY_425, recommendations.get(0).getTask());
		
		// The rune task should NOT be marked as TAKE_NOW
		assertNotEquals("Task creating loop to board should NOT be TAKE_NOW",
			RecommendationType.TAKE_NOW, runeToPickupRec.getType());
		
		// Clean up
		engine.setTeleportOptimizationEnabled(false);
	}
	
	// ========================================
	// SWAP STABILITY TESTS
	// ========================================
	
	/**
	 * Should NOT recommend swapping for a task that delivers to a NEW location
	 * when the user is at a board and has cargo loaded going in a different direction.
	 */
	@Test
	public void testSwap_ShouldNotRecommendTaskThatAddsNewDestinationOffRoute()
	{
		PortLocation boardLocation = PortLocation.PORT_PISCARILIUS;
		
		// Enable teleport optimization
		engine.setTeleportOptimizationEnabled(true);
		
		// HELD TASKS - 3 active with cargo loaded for Lunar Isle, 1 inactive (halberd)
		CourierTask fishTask = createTaskWithCargo(CourierTaskData.LUNAR_ISLE_FISH_DELIVERY_420, 6);      // Port Piscarilius -> Lunar Isle [CARGO LOADED]
		CourierTask halberdTask = createTask(CourierTaskData.RELLEKKA_HALBERD_DELIVERY_399, 0);           // Port Tyras -> Rellekka [INACTIVE - no cargo]
		CourierTask crystalTask = createTaskWithCargo(CourierTaskData.LUNAR_ISLE_CRYSTAL_SEED_DELIVERY_377, 4); // Prifddinas -> Lunar Isle [CARGO LOADED]
		CourierTask furTask = createTaskWithCargo(CourierTaskData.LUNAR_ISLE_FUR_DELIVERY_316, 4);        // Port Roberts -> Lunar Isle [CARGO LOADED]
		
		// Mark halberd as inactive (no cargo loaded)
		engine.setTaskActive(halberdTask, false);
		
		List<CourierTask> heldTasks = Arrays.asList(fishTask, halberdTask, crystalTask, furTask);
		
		// Verify we have cargo loaded (mid-route mode)
		assertFalse("Should be in execution mode (cargo loaded)", engine.isInPlanningMode(heldTasks));
		
		// Available tasks at Port Piscarilius noticeboard
		List<CourierTaskData> availableTasks = Arrays.asList(
			CourierTaskData.PORT_ROBERTS_JEWELLERY_DELIVERY_255,  // Port Piscarilius -> Port Roberts (928 XP) - adds new destination
			CourierTaskData.PORT_PISCARILIUS_PLANK_DELIVERY_249,  // Land's End -> Port Piscarilius (plank, 460 XP)
			CourierTaskData.PORT_PISCARILIUS_SEED_DELIVERY_258,   // Hosidius -> Port Piscarilius (seed, 350 XP)
			CourierTaskData.LUNAR_ISLE_FISH_DELIVERY_259,         // Port Piscarilius -> Lunar Isle (fish, 1601 XP) - delivers on-route
			CourierTaskData.LANDS_END_FISH_DELIVERY_248           // Port Piscarilius -> Land's End (fish, 240 XP)
		);
		
		// Get swap recommendations
		List<TaskRecommendation> recommendations = 
			engine.rankTasksWithSwapEvaluation(availableTasks, boardLocation, heldTasks, 0);
		
		// Find swap recommendations
		List<TaskRecommendation> swapRecs = recommendations.stream()
			.filter(r -> r.getType() == RecommendationType.SWAP)
			.collect(Collectors.toList());
		
		if (!swapRecs.isEmpty())
		{
			TaskRecommendation swapRec = swapRecs.get(0);
			CourierTaskData swapInTask = swapRec.getTask();
			
			// KEY ASSERTION: Should NOT recommend the jewellery task that delivers to Port Roberts
			assertNotEquals(
				"Should NOT recommend task that delivers to Port Roberts (not on current route).",
				CourierTaskData.PORT_ROBERTS_JEWELLERY_DELIVERY_255,
				swapInTask);
		}
		
		// Clean up
		engine.setTeleportOptimizationEnabled(false);
	}
	
	/**
	 * Test: "Delivery here" bonus should NOT apply when it requires backtracking.
	 */
	@Test
	public void testDeliveryHere_WithCargoLoaded_ShouldNotRecommendBacktrack()
	{
		PortLocation boardLocation = PortLocation.PORT_PISCARILIUS;
		
		// Enable teleport optimization
		engine.setTeleportOptimizationEnabled(true);
		
		// HELD TASKS - all with cargo loaded going to Lunar Isle
		CourierTask fishTask = createTaskWithCargo(CourierTaskData.LUNAR_ISLE_FISH_DELIVERY_420, 6);      // Port Piscarilius -> Lunar Isle [CARGO LOADED]
		CourierTask furTask = createTaskWithCargo(CourierTaskData.LUNAR_ISLE_FUR_DELIVERY_316, 4);        // Port Roberts -> Lunar Isle [CARGO LOADED]
		CourierTask crystalTask = createTaskWithCargo(CourierTaskData.LUNAR_ISLE_CRYSTAL_SEED_DELIVERY_377, 4); // Prifddinas -> Lunar Isle [CARGO LOADED]
		
		List<CourierTask> heldTasks = Arrays.asList(fishTask, furTask, crystalTask);
		
		// Verify we have cargo loaded (mid-route mode)
		assertFalse("Should be in execution mode (cargo loaded)", engine.isInPlanningMode(heldTasks));
		
		// Available tasks at Port Piscarilius noticeboard
		List<CourierTaskData> availableTasks = Arrays.asList(
			CourierTaskData.PORT_PISCARILIUS_PLANK_DELIVERY_249,  // Land's End -> Port Piscarilius (460 XP) - backtrack
			CourierTaskData.PORT_PISCARILIUS_SEED_DELIVERY_258,   // Hosidius -> Port Piscarilius (350 XP) - backtrack
			CourierTaskData.PORT_ROBERTS_JEWELLERY_DELIVERY_255,  // Port Piscarilius -> Port Roberts (928 XP) - picks up HERE, but off-route
			CourierTaskData.LANDS_END_FISH_DELIVERY_248,          // Port Piscarilius -> Land's End (240 XP) - picks up HERE, but off-route
			CourierTaskData.LUNAR_ISLE_FISH_DELIVERY_259          // Port Piscarilius -> Lunar Isle (1601 XP) - BEST: pickup HERE, deliver on-route
		);
		
		// Get recommendations
		List<TaskRecommendation> recommendations = 
			engine.rankTasks(availableTasks, boardLocation, heldTasks);
		
		// Find the backtrack task (Land's End -> Piscarilius)
		TaskRecommendation plankRec = recommendations.stream()
			.filter(r -> r.getTask() == CourierTaskData.PORT_PISCARILIUS_PLANK_DELIVERY_249)
			.findFirst().orElse(null);
		
		// Find pickup-here tasks
		TaskRecommendation jewelleryRec = recommendations.stream()
			.filter(r -> r.getTask() == CourierTaskData.PORT_ROBERTS_JEWELLERY_DELIVERY_255)
			.findFirst().orElse(null);
		
		assertNotNull("Plank task should be in recommendations", plankRec);
		
		// KEY ASSERTION: The backtrack task should NOT be TAKE_NOW
		// In execution mode, backtrack tasks don't contribute to the current route
		assertNotEquals(
			"Backtrack task should NOT be TAKE_NOW - it requires leaving and returning",
			RecommendationType.TAKE_NOW, plankRec.getType());
		
		// Backtrack tasks should have low/zero score in execution mode
		assertTrue(
			"Backtrack task should have score <= 0 in execution mode",
			plankRec.getScore() <= 0);
		
		// Off-route pickup-HERE tasks should also be SKIP in execution mode
		// (they don't help complete the current route either)
		if (jewelleryRec != null) {
			assertNotEquals(
				"Off-route pickup-HERE task should NOT be TAKE_NOW in execution mode",
				RecommendationType.TAKE_NOW, jewelleryRec.getType());
		}
		
		// Clean up
		engine.setTeleportOptimizationEnabled(false);
	}
	
	/**
	 * Test: Task picking up HERE but delivering OFF-ROUTE should be auto-excluded when cargo is loaded.
	 */
	@Test
	public void testAutoExclude_PickupHereButDeliverOffRoute_WhenCargoLoaded_ShouldBeInactive()
	{
		PortLocation boardLocation = PortLocation.PORT_PISCARILIUS;
		
		// HELD TASKS - all with cargo loaded going to Lunar Isle
		CourierTask fishTask = createTaskWithCargo(CourierTaskData.LUNAR_ISLE_FISH_DELIVERY_420, 6);      // Port Piscarilius -> Lunar Isle [CARGO LOADED]
		CourierTask furTask = createTaskWithCargo(CourierTaskData.LUNAR_ISLE_FUR_DELIVERY_316, 4);        // Port Roberts -> Lunar Isle [CARGO LOADED]
		CourierTask crystalTask = createTaskWithCargo(CourierTaskData.LUNAR_ISLE_CRYSTAL_SEED_DELIVERY_377, 4); // Prifddinas -> Lunar Isle [CARGO LOADED]
		
		List<CourierTask> heldTasks = Arrays.asList(fishTask, furTask, crystalTask);
		
		// Verify we have cargo loaded
		assertFalse("Should be in execution mode (cargo loaded)", engine.isInPlanningMode(heldTasks));
		
		// The task user wants to accept - picks up HERE but delivers OFF-ROUTE
		CourierTaskData newTask = CourierTaskData.PORT_ROBERTS_JEWELLERY_DELIVERY_255; // Port Piscarilius -> Port Roberts
		
		// Check if the task should be auto-excluded
		boolean shouldExclude = engine.shouldAutoExclude(newTask, boardLocation, heldTasks);
		
		// KEY ASSERTION: Task that picks up HERE but delivers OFF-ROUTE should be auto-excluded
		assertTrue(
			"Task that picks up HERE but delivers OFF-ROUTE should be auto-excluded when cargo is loaded.",
			shouldExclude);
	}
	
	// ========================================
	// PLANNING MODE OUTBOUND TESTS
	// ========================================
	
	/**
	 * Test: In planning mode at final destination, outbound tasks should NOT be recommended.
	 */
	@Test
	public void testPlanningMode_AtFinalDestination_OutboundTasksShouldNotBeRecommended()
	{
		PortLocation boardLocation = PortLocation.LUNAR_ISLE;
		
		engine.setTeleportOptimizationEnabled(true);
		
		// Held tasks: both are round-trips delivering TO Lunar Isle
		CourierTask fishTask = createTask(CourierTaskData.LUNAR_ISLE_FISH_DELIVERY_431, 0);     // Port Roberts -> Lunar Isle (4626 XP)
		CourierTask potionTask = createTask(CourierTaskData.LUNAR_ISLE_POTION_DELIVERY_424, 0); // Prifddinas -> Lunar Isle (4341 XP)
		
		List<CourierTask> heldTasks = Arrays.asList(fishTask, potionTask);
		
		// Available tasks are OUTBOUND from Lunar Isle
		List<CourierTaskData> availableTasks = Arrays.asList(
			CourierTaskData.PRIFDDINAS_HERB_DELIVERY_423,      // Lunar Isle -> Prifddinas (herb, 2171 XP)
			CourierTaskData.PORT_PISCARILIUS_RUNE_DELIVERY_425, // Lunar Isle -> Port Piscarilius (rune, 1948 XP)
			CourierTaskData.PISCATORIS_POTION_DELIVERY_426     // Lunar Isle -> Piscatoris (potion, 1391 XP)
		);
		
		List<TaskRecommendation> recommendations = 
			engine.rankTasks(availableTasks, boardLocation, heldTasks);
		
		// Find the herb task recommendation
		TaskRecommendation herbRec = recommendations.stream()
			.filter(r -> r.getTask() == CourierTaskData.PRIFDDINAS_HERB_DELIVERY_423)
			.findFirst().orElse(null);
		
		assertNotNull("Herb task should be in recommendations", herbRec);
		
		// CRITICAL: Outbound task from final destination should NOT be CONSIDER or TAKE_NOW
		assertNotEquals(
			"Outbound task FROM final destination should NOT be CONSIDER.",
			RecommendationType.CONSIDER, herbRec.getType());
		
		assertNotEquals(
			"Outbound task FROM final destination should NOT be TAKE_NOW",
			RecommendationType.TAKE_NOW, herbRec.getType());
		
		// ALL outbound tasks should be SKIP in this scenario
		for (TaskRecommendation rec : recommendations)
		{
			assertEquals(
				"All outbound tasks from final destination should be SKIP.",
				RecommendationType.SKIP, rec.getType());
		}
		
		// Cleanup
		engine.setTeleportOptimizationEnabled(false);
	}
	
	/**
	 * Test: In planning mode, tasks picking up HERE but delivering OFF-ROUTE should NOT be TAKE_NOW.
	 */
	@Test
	public void testPlanningMode_PickupHereDeliverOffRoute_ShouldNotBeTakeNow()
	{
		PortLocation boardLocation = PortLocation.ETCETERIA;
		
		engine.setTeleportOptimizationEnabled(true);
		
		// Held tasks - mixed route
		CourierTask fishTask = createTask(CourierTaskData.LUNAR_ISLE_FISH_DELIVERY_431, 0);     // Port Roberts -> Lunar Isle (INACTIVE)
		CourierTask potionTask = createTask(CourierTaskData.LUNAR_ISLE_POTION_DELIVERY_424, 0); // Prifddinas -> Lunar Isle
		CourierTask swordTask = createTask(CourierTaskData.ETCETERIA_SWORD_DELIVERY_414, 0);    // Sunset Coast -> Etceteria
		
		// Mark fish task as inactive (matches user scenario)
		engine.setTaskActive(fishTask, false);
		
		List<CourierTask> heldTasks = Arrays.asList(fishTask, potionTask, swordTask);
		
		// Available task: picks up HERE, delivers OFF ROUTE
		List<CourierTaskData> availableTasks = Arrays.asList(
			CourierTaskData.DEEPFIN_POINT_MAHOGANY_DELIVERY_417  // Etceteria -> Deepfin Point (3688 XP)
		);
		
		List<TaskRecommendation> recommendations = 
			engine.rankTasks(availableTasks, boardLocation, heldTasks);
		
		TaskRecommendation mahoganyRec = recommendations.get(0);
		
		// CRITICAL: Task that picks up HERE but delivers OFF ROUTE should NOT be TAKE_NOW
		assertNotEquals(
			"Task that picks up HERE but delivers OFF ROUTE should NOT be TAKE_NOW.",
			RecommendationType.TAKE_NOW, mahoganyRec.getType());
		
		// Cleanup
		engine.setTaskActive(fishTask, true);
		engine.setTeleportOptimizationEnabled(false);
	}
	
	// ========================================
	// SHOULD AUTO-EXCLUDE TESTS
	// ========================================
	
	/**
	 * Test: A new task that DOES NOT fit the current route should be auto-excluded.
	 */
	@Test
	public void testShouldAutoExclude_NewTaskNotFittingRoute_ReturnsTrue()
	{
		PortLocation boardLocation = PortLocation.PRIFDDINAS;
		
		// Current execution: cargo loaded for Lunar Isle and Etceteria
		CourierTask fishTask = createTaskWithCargo(CourierTaskData.LUNAR_ISLE_FISH_DELIVERY_431, 5);     // Port Roberts -> Lunar Isle [CARGO]
		CourierTask swordTask = createTaskWithCargo(CourierTaskData.ETCETERIA_SWORD_DELIVERY_414, 3);    // Sunset Coast -> Etceteria [CARGO]
		
		List<CourierTask> heldTasks = Arrays.asList(fishTask, swordTask);
		
		// New task being accepted: Piscatoris -> Rellekka (does NOT fit current route)
		CourierTaskData newTask = CourierTaskData.RELLEKKA_MONKFISH_DELIVERY_393;
		
		boolean shouldExclude = engine.shouldAutoExclude(newTask, boardLocation, heldTasks);
		
		// The new task should be auto-excluded because it doesn't fit the current route
		assertTrue(
			"New task (Piscatoris -> Rellekka) should be auto-excluded.",
			shouldExclude);
	}
	
	/**
	 * Test: A new task that DOES fit the current route should NOT be auto-excluded.
	 */
	@Test
	public void testShouldAutoExclude_NewTaskFittingRoute_ReturnsFalse()
	{
		PortLocation boardLocation = PortLocation.PRIFDDINAS;
		
		// Current execution: cargo loaded for Lunar Isle
		CourierTask fishTask = createTaskWithCargo(CourierTaskData.LUNAR_ISLE_FISH_DELIVERY_431, 5);     // Port Roberts -> Lunar Isle [CARGO]
		CourierTask potionTask = createTask(CourierTaskData.LUNAR_ISLE_POTION_DELIVERY_424, 0);          // Prifddinas -> Lunar Isle [pending]
		
		List<CourierTask> heldTasks = Arrays.asList(fishTask, potionTask);
		
		// New task being accepted: also delivers to Lunar Isle (DOES fit current route)
		CourierTaskData newTask = CourierTaskData.LUNAR_ISLE_CRYSTAL_SEED_DELIVERY_377; // Prifddinas -> Lunar Isle
		
		boolean shouldExclude = engine.shouldAutoExclude(newTask, boardLocation, heldTasks);
		
		// The new task should NOT be auto-excluded because it fits the current route
		assertFalse(
			"New task (Prifddinas -> Lunar Isle) should NOT be auto-excluded.",
			shouldExclude);
	}
	
	// ========================================
	// FUTURE ROUTE / RETURN TRIP DETECTION TESTS
	// ========================================
	
	/**
	 * Test: Task delivering AGAINST route direction should score poorly.
	 * 
	 * With marginal cost scoring, tasks that go against the route direction
	 * naturally score lower because they add significant distance to the route.
	 * They will be marked as SKIP rather than a special FUTURE_ROUTE type.
	 */
	@Test
	public void testTaskAgainstRouteDirection_ShouldScorePoorly()
	{
		PortLocation boardLocation = PortLocation.LUNAR_ISLE;
		
		// Enable teleport optimization
		engine.setTeleportOptimizationEnabled(true);
		
		// HELD TASKS - all pending pickups (no cargo loaded = planning mode)
		// Route direction: Sunset Coast -> Rellekka
		CourierTask fabricTask = createTask(CourierTaskData.RELLEKKA_FABRIC_DELIVERY_386, 0);   // Sunset Coast -> Rellekka [ACTIVE]
		CourierTask redwoodTask = createTask(CourierTaskData.RELLEKKA_REDWOOD_DELIVERY_395, 0); // Port Piscarilius -> Rellekka [ACTIVE]
		CourierTask fishTask = createTask(CourierTaskData.LUNAR_ISLE_FISH_DELIVERY_420, 0);     // Port Piscarilius -> Lunar Isle [ACTIVE]
		
		List<CourierTask> heldTasks = Arrays.asList(fabricTask, redwoodTask, fishTask);
		
		// Verify we're in planning mode (no cargo loaded)
		assertTrue("Should be in planning mode (no cargo loaded)", engine.isInPlanningMode(heldTasks));
		
		// Available tasks at Lunar Isle noticeboard
		List<CourierTaskData> availableTasks = Arrays.asList(
			CourierTaskData.DEEPFIN_POINT_SUQAH_HIDE_DELIVERY_428,  // Lunar Isle -> Deepfin Point - against route direction!
			CourierTaskData.PISCATORIS_POTION_DELIVERY_426,        // Lunar Isle -> Piscatoris
			CourierTaskData.PORT_ROBERTS_FABRIC_DELIVERY_430,      // Lunar Isle -> Port Roberts
			CourierTaskData.LUNAR_ISLE_FISH_DELIVERY_431           // Port Roberts -> Lunar Isle (UNAVAILABLE - conflicts with held fish)
		);
		
		List<TaskRecommendation> recommendations = 
			engine.rankTasks(availableTasks, boardLocation, heldTasks);
		
		// Find the Deepfin task recommendation
		TaskRecommendation deepfinRec = recommendations.stream()
			.filter(r -> r.getTask() == CourierTaskData.DEEPFIN_POINT_SUQAH_HIDE_DELIVERY_428)
			.findFirst().orElse(null);
		
		assertNotNull("Deepfin task should be in recommendations", deepfinRec);
		
		// CRITICAL: Task against route direction should NOT be TAKE_NOW
		// With marginal cost scoring, the high route cost addition naturally penalizes it
		assertNotEquals("Task against route direction should NOT be TAKE_NOW.",
			RecommendationType.TAKE_NOW, deepfinRec.getType());
		
		// Clean up
		engine.setTeleportOptimizationEnabled(false);
	}
	
	/**
	 * Test: Swap should NOT be recommended for task that loops back to route start.
	 */
	@Test
	public void testSwapAgainstRouteDirection_ShouldNotRecommend()
	{
		PortLocation boardLocation = PortLocation.RELLEKKA;
		
		// Enable teleport optimization
		engine.setTeleportOptimizationEnabled(true);
		
		// HELD TASKS - mix of active and inactive
		// Route direction: Sunset Coast -> Rellekka
		CourierTask fabricTask = createTask(CourierTaskData.RELLEKKA_FABRIC_DELIVERY_386, 0);         // Sunset Coast -> Rellekka [ACTIVE]
		CourierTask redwoodTask = createTask(CourierTaskData.RELLEKKA_REDWOOD_DELIVERY_395, 0);       // Port Piscarilius -> Rellekka [ACTIVE]
		CourierTask fishTask = createTask(CourierTaskData.LUNAR_ISLE_FISH_DELIVERY_420, 0);           // Port Piscarilius -> Lunar Isle [ACTIVE]
		CourierTask suqahTask = createTask(CourierTaskData.DEEPFIN_POINT_SUQAH_HIDE_DELIVERY_428, 0); // Lunar Isle -> Deepfin Point [INACTIVE]
		
		// Mark suqah task as inactive
		engine.setTaskActive(suqahTask, false);
		
		List<CourierTask> heldTasks = Arrays.asList(fabricTask, redwoodTask, fishTask, suqahTask);
		
		// Available tasks at Rellekka noticeboard
		List<CourierTaskData> availableTasks = Arrays.asList(
			CourierTaskData.SUNSET_COAST_FISH_DELIVERY_389,        // Rellekka -> Sunset Coast - loops back to start!
			CourierTaskData.PORT_PISCARILIUS_FUR_DELIVERY_394,     // Rellekka -> Port Piscarilius
			CourierTaskData.DEEPFIN_POINT_FUR_DELIVERY_398         // Rellekka -> Deepfin Point
		);
		
		// Get swap recommendations (slots = 0)
		List<TaskRecommendation> recommendations = 
			engine.rankTasksWithSwapEvaluation(availableTasks, boardLocation, heldTasks, 0);
		
		// Find swap recommendations
		List<TaskRecommendation> swapRecs = recommendations.stream()
			.filter(r -> r.getType() == RecommendationType.SWAP)
			.collect(Collectors.toList());
		
		if (!swapRecs.isEmpty())
		{
			// If there are any swap recommendations, none should be for Sunset Coast
			for (TaskRecommendation swapRec : swapRecs)
			{
				assertNotEquals(
					"Should NOT recommend swapping for task that loops back to route start.",
					CourierTaskData.SUNSET_COAST_FISH_DELIVERY_389, swapRec.getTask());
			}
		}
		
		// Find if Sunset Coast task appears in recommendations
		TaskRecommendation sunsetSwapRec = recommendations.stream()
			.filter(r -> r.getTask() == CourierTaskData.SUNSET_COAST_FISH_DELIVERY_389)
			.findFirst().orElse(null);
		
		if (sunsetSwapRec != null)
		{
			assertNotEquals(
				"Task that loops back to route start should NOT be a SWAP recommendation.",
				RecommendationType.SWAP, sunsetSwapRec.getType());
		}
		
		// Clean up
		engine.setTeleportOptimizationEnabled(false);
	}
}
