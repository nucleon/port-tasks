package com.nucleon.porttasks.optimizer;

import com.nucleon.porttasks.CourierTask;
import com.nucleon.porttasks.enums.CourierTaskData;
import com.nucleon.porttasks.enums.PortLocation;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * Tests for TaskSelectionEngine swap recommendation functionality.
 * 
 * When task slots are full, the engine evaluates swap opportunities:
 * - Should recommend swapping if a new task scores better than the worst held task
 * - Inactive tasks are prioritized as swap-out candidates
 * - Swap recommendations should consider cargo conflicts
 */
public class SwapRecommendationTest extends TaskSelectionEngineTestBase
{
	// ========================================
	// BASIC SWAP RECOMMENDATION TESTS
	// ========================================
	
	/**
	 * Test: When slots are full, evaluate swap opportunities.
	 * Should recommend swapping if a new task scores better than the worst held task.
	 */
	@Test
	public void testSwapRecommendation_WhenBetterTaskAvailable()
	{
		PortLocation boardLocation = PortLocation.ETCETERIA;
		
		// Holding a low-value task
		List<CourierTask> heldTasks = Arrays.asList(
			createTask(CourierTaskData.RELLEKKA_FISH_DELIVERY_400, 0) // 688 XP
		);
		
		// A much better task is available on the board
		List<CourierTaskData> availableTasks = Arrays.asList(
			CourierTaskData.ETCETERIA_PLANK_DELIVERY_410 // 4188 XP, same general area
		);
		
		SwapRecommendation swap = 
			engine.evaluateSwapOpportunity(availableTasks, boardLocation, heldTasks);
		
		assertNotNull("Should recommend a swap when better task available", swap);
		assertEquals("Should recommend the better task", 
			CourierTaskData.ETCETERIA_PLANK_DELIVERY_410, swap.getNewTask());
		assertEquals("Should recommend swapping the worst task",
			heldTasks.get(0), swap.getTaskToSwap());
		assertTrue("Swap should show positive score improvement", 
			swap.getScoreImprovement() > 0);
	}
	
	/**
	 * Test: No swap recommendation when available task provides lower value.
	 * 
	 * In the new algorithm, we compare XP and route fit. If the held task has
	 * higher XP and the available task doesn't provide significant route improvement,
	 * no swap should be recommended.
	 */
	@Test
	public void testSwapRecommendation_NoSwapWhenAvailableScoresLower()
	{
		PortLocation boardLocation = PortLocation.RELLEKKA;
		
		// Holding a high-value task: 3625 XP, picks up at board (Rellekka)
		CourierTaskData heldTaskData = CourierTaskData.DEEPFIN_POINT_FUR_DELIVERY_398;
		
		// Available: a much lower-value task: 688 XP, picks up at Etceteria
		// Both tasks pick up locally to their boards - neither has a pickup advantage
		CourierTaskData availTaskData = CourierTaskData.RELLEKKA_FISH_DELIVERY_400;
		
		List<CourierTask> heldTasks = Arrays.asList(createTask(heldTaskData, 0));
		List<CourierTaskData> availableTasks = Arrays.asList(availTaskData);
		
		SwapRecommendation swap = 
			engine.evaluateSwapOpportunity(availableTasks, boardLocation, heldTasks);
		
		// Should not recommend swap - held task has much higher XP
		// and available task doesn't fit the route well (picks up at Etceteria, not Rellekka)
		assertNull("Should not recommend swap when held task has higher XP and available doesn't fit route", swap);
	}
	
	/**
	 * Test: Swap evaluation considers the "after swap" state.
	 */
	@Test
	public void testSwapRecommendation_EvaluatesAfterSwapState()
	{
		PortLocation boardLocation = PortLocation.LUNAR_ISLE;
		
		// Holding two tasks - one delivers to Rellekka
		CourierTask taskToRellekka = createTask(CourierTaskData.RELLEKKA_FISH_DELIVERY_400, 0);
		CourierTask taskToEtceteria = createTask(CourierTaskData.ETCETERIA_MONKFISH_DELIVERY_416, 0);
		List<CourierTask> heldTasks = Arrays.asList(taskToRellekka, taskToEtceteria);
		
		// Available: task that delivers to Rellekka (synergy with remaining task)
		List<CourierTaskData> availableTasks = Arrays.asList(
			CourierTaskData.RELLEKKA_ADAMANTITE_DELIVERY_397
		);
		
		SwapRecommendation swap = 
			engine.evaluateSwapOpportunity(availableTasks, boardLocation, heldTasks);
		
		// A swap should be recommended for this scenario
		assertNotNull("Should recommend a swap (new task has better synergy)", swap);
		
		// Verify the score was calculated with the swapped-out task removed
		assertTrue("Score improvement should reflect actual benefit", 
			swap.getScoreImprovement() != 0);
	}
	
	/**
	 * Test: After canceling the task to swap, the recommended new task
	 * should still be the best choice (recommendations shouldn't flip-flop).
	 */
	@Test
	public void testSwapRecommendation_StableAfterCancel()
	{
		PortLocation boardLocation = PortLocation.ETCETERIA;
		
		// Initial state: holding one task
		CourierTask heldTask = createTask(CourierTaskData.RELLEKKA_FISH_DELIVERY_400, 0);
		List<CourierTask> heldTasks = new ArrayList<>(Arrays.asList(heldTask));
		
		// Better task available
		CourierTaskData betterTask = CourierTaskData.ETCETERIA_PLANK_DELIVERY_410;
		List<CourierTaskData> availableTasks = Arrays.asList(betterTask);
		
		// Step 1: Get swap recommendation (slots full scenario)
		SwapRecommendation swap = 
			engine.evaluateSwapOpportunity(availableTasks, boardLocation, heldTasks);
		
		assertNotNull("Should recommend swap", swap);
		CourierTaskData recommendedTask = swap.getNewTask();
		
		// Step 2: Simulate canceling the task (now have empty slot)
		heldTasks.clear();
		
		// Step 3: Get normal recommendation (slots available)
		TaskRecommendation normalRec = 
			engine.recommendTask(availableTasks, boardLocation, heldTasks);
		
		// The same task should be recommended
		assertNotNull("Should get a recommendation", normalRec);
		assertEquals("Recommendation should be stable after cancel",
			recommendedTask, normalRec.getTask());
	}
	
	/**
	 * Test: Swap recommendation finds the worst task to swap out.
	 */
	@Test
	public void testSwapRecommendation_SwapsWorstTask()
	{
		PortLocation boardLocation = PortLocation.LUNAR_ISLE;
		
		// Holding multiple tasks of varying value
		CourierTask lowValueTask = createTask(CourierTaskData.RELLEKKA_FISH_DELIVERY_400, 0);
		CourierTask midValueTask = createTask(CourierTaskData.ETCETERIA_IRON_DELIVERY_401, 0);
		CourierTask highValueTask = createTask(CourierTaskData.ETCETERIA_PLANK_DELIVERY_410, 0);
		
		List<CourierTask> heldTasks = Arrays.asList(lowValueTask, midValueTask, highValueTask);
		
		// Available: a very high value task
		List<CourierTaskData> availableTasks = Arrays.asList(
			CourierTaskData.LUNAR_ISLE_COAL_DELIVERY_429
		);
		
		SwapRecommendation swap = 
			engine.evaluateSwapOpportunity(availableTasks, boardLocation, heldTasks);
		
		// A swap should be recommended (7188 XP available vs 688 XP held)
		assertNotNull("Should recommend a swap (much higher XP available)", swap);
		
		// Should swap out the lowest value task, not the higher ones
		assertEquals("Should recommend swapping the lowest value task",
			lowValueTask, swap.getTaskToSwap());
	}
	
	// ========================================
	// PLANNING MODE SWAP TESTS
	// ========================================
	
	/**
	 * Test: In planning mode (no cargo loaded), "Pickup HERE" doesn't give a bonus
	 * because the user can teleport to any port and start their route there.
	 */
	@Test
	public void testSwapRecommendation_PlanningModeIgnoresPickupHereBonus()
	{
		PortLocation boardLocation = PortLocation.ETCETERIA;
		
		// Holding a LOW value task with pickup HERE (no cargo loaded = planning mode)
		CourierTaskData heldTaskData = CourierTaskData.RELLEKKA_FISH_DELIVERY_400;
		assertEquals("Setup: held task should pickup at board location",
			boardLocation, heldTaskData.getPickupPort());
		
		// Available: HIGHER value task with pickup elsewhere
		CourierTaskData availTaskData = CourierTaskData.ETCETERIA_PLANK_DELIVERY_410;
		assertNotEquals("Setup: available task should pickup elsewhere",
			boardLocation, availTaskData.getPickupPort());
		assertTrue("Setup: available task should have higher XP",
			availTaskData.getReward() > heldTaskData.getReward());
		
		List<CourierTask> heldTasks = Arrays.asList(createTask(heldTaskData, 0));
		List<CourierTaskData> availableTasks = Arrays.asList(availTaskData);
		
		SwapRecommendation swap = 
			engine.evaluateSwapOpportunity(availableTasks, boardLocation, heldTasks);
		
		// In planning mode, should recommend swapping to the higher XP task
		assertNotNull("Should recommend swap in planning mode despite held task having Pickup HERE", swap);
		assertEquals("Should recommend the higher XP task", availTaskData, swap.getNewTask());
	}
	
	/**
	 * Test: Swap type appears in recommendations when slots are full.
	 */
	@Test
	public void testRankTasks_IncludesSwapType()
	{
		PortLocation boardLocation = PortLocation.ETCETERIA;
		
		// Holding one task (simulating full slots scenario)
		CourierTask heldTask = createTask(CourierTaskData.RELLEKKA_FISH_DELIVERY_400, 0);
		List<CourierTask> heldTasks = Arrays.asList(heldTask);
		
		// Better task available
		List<CourierTaskData> availableTasks = Arrays.asList(
			CourierTaskData.ETCETERIA_PLANK_DELIVERY_410
		);
		
		// Use the swap-aware ranking method
		List<TaskRecommendation> ranked = 
			engine.rankTasksWithSwapEvaluation(availableTasks, boardLocation, heldTasks, 0);
		
		assertFalse("Should have recommendations", ranked.isEmpty());
		TaskRecommendation rec = ranked.get(0);
		
		// When slots are full (availableSlots=0), should recommend SWAP
		assertEquals("Should recommend SWAP type when slots full",
			RecommendationType.SWAP, rec.getType());
		assertNotNull("Should include task to swap", rec.getSwapWithTask());
	}
	
	// ========================================
	// INACTIVE TASK SWAP TESTS
	// ========================================
	
	/**
	 * Test: Unchecking (deactivating) tasks makes them primary swap-out candidates.
	 */
	@Test
	public void testCheckbox_InactiveTasksArePrimarySwapOutCandidates()
	{
		PortLocation boardLocation = PortLocation.ETCETERIA;
		
		// Setup scenario with active and inactive tasks
		CourierTask potionTask = createTask(CourierTaskData.LUNAR_ISLE_POTION_DELIVERY_437, 0);
		CourierTask fishTask = createTask(CourierTaskData.LUNAR_ISLE_FISH_DELIVERY_431, 0);
		CourierTask wineTask = createTaskWithCargo(CourierTaskData.RELLEKKA_WINE_DELIVERY_303, 4);
		CourierTask teakTask = createTask(CourierTaskData.RELLEKKA_TEAK_DELIVERY_406, 0);
		
		List<CourierTask> heldTasks = Arrays.asList(potionTask, fishTask, wineTask, teakTask);
		
		// Available task - fits current route
		List<CourierTaskData> availableTasks = Arrays.asList(
			CourierTaskData.RELLEKKA_FISH_DELIVERY_400
		);
		
		// SCENARIO 1: All tasks active (default)
		SwapRecommendation swap1 = 
			engine.evaluateSwapOpportunity(availableTasks, boardLocation, heldTasks);
		
		// SCENARIO 2: Uncheck the FUTURE fish task (4626 XP)
		engine.setTaskActive(fishTask, false);
		assertFalse("Fish task should be inactive", engine.isTaskActive(fishTask));
		
		SwapRecommendation swap2 = 
			engine.evaluateSwapOpportunity(availableTasks, boardLocation, heldTasks);
		
		assertNotNull("Should recommend swap when inactive task exists", swap2);
		assertEquals("Should swap out the INACTIVE fish task",
			fishTask, swap2.getTaskToSwap());
		
		// Clean up
		engine.setTaskActive(fishTask, true);
	}
	
	/**
	 * Test: When ALL tasks are inactive, the algorithm should still work.
	 */
	@Test
	public void testCheckbox_AllInactiveTasksStillAllowsSwap()
	{
		PortLocation boardLocation = PortLocation.ETCETERIA;
		
		// Two inactive tasks
		CourierTask highXpTask = createTask(CourierTaskData.LUNAR_ISLE_POTION_DELIVERY_437, 0);
		CourierTask lowXpTask = createTask(CourierTaskData.RELLEKKA_TEAK_DELIVERY_406, 0);
		
		List<CourierTask> heldTasks = Arrays.asList(highXpTask, lowXpTask);
		
		// Mark both as inactive
		engine.setTaskActive(highXpTask, false);
		engine.setTaskActive(lowXpTask, false);
		
		// Higher value available task
		List<CourierTaskData> availableTasks = Arrays.asList(
			CourierTaskData.RELLEKKA_WINE_DELIVERY_303
		);
		
		SwapRecommendation swap = 
			engine.evaluateSwapOpportunity(availableTasks, boardLocation, heldTasks);
		
		assertNotNull("Should recommend swap even when all tasks are inactive", swap);
		// Should swap out the LOWER XP task (1375 XP) to preserve higher XP (10017 XP)
		assertEquals("Should swap out the lower XP inactive task",
			lowXpTask, swap.getTaskToSwap());
		
		// Clean up
		engine.setTaskActive(highXpTask, true);
		engine.setTaskActive(lowXpTask, true);
	}
	
	// ========================================
	// SWAP WITH CARGO LOADED (MID-ROUTE) TESTS
	// ========================================
	
	/**
	 * Test: In mid-route mode (cargo loaded), should NOT recommend swapping a task
	 * that delivers to a SHARED destination for a lower-XP task that adds a new destination.
	 */
	@Test
	public void testSwapRecommendation_MidRoute_NoSwapForPickupHereWhenDeliveringToNewLocation()
	{
		PortLocation boardLocation = PortLocation.PORT_PISCARILIUS;
		
		// Held tasks - ALL with cargo loaded for Rellekka (3x) and Etceteria (1x)
		List<CourierTask> heldTasks = Arrays.asList(
			createTaskWithCargo(CourierTaskData.RELLEKKA_PLANK_DELIVERY_391, 4),
			createTaskWithCargo(CourierTaskData.RELLEKKA_HALBERD_DELIVERY_399, 6),
			createTaskWithCargo(CourierTaskData.RELLEKKA_REDWOOD_DELIVERY_395, 4),
			createTaskWithCargo(CourierTaskData.ETCETERIA_FRUIT_DELIVERY_405, 5)
		);
		
		// Verify all tasks have cargo loaded (mid-route mode)
		for (CourierTask task : heldTasks)
		{
			assertTrue("All tasks should have cargo loaded", task.getCargoTaken() > 0);
		}
		
		// Available task: Port Piscarilius -> Lunar Isle (1601 XP)
		// Pickup HERE but delivery to completely new location
		CourierTaskData availableTask = CourierTaskData.LUNAR_ISLE_FISH_DELIVERY_259;
		
		List<CourierTaskData> availableTasks = Arrays.asList(availableTask);
		
		SwapRecommendation swap = 
			engine.evaluateSwapOpportunity(availableTasks, boardLocation, heldTasks);
		
		// Should NOT recommend swap because:
		// 1. Plank task delivers to Rellekka where 2 other tasks also deliver (synergy!)
		// 2. Fish task would ADD Lunar Isle as new destination (bad!)
		assertNull("Should NOT recommend swap in mid-route mode when available task " +
			"has 'Pickup HERE' but delivers to a NEW location", swap);
	}
	
	/**
	 * Test: When mid-route with cargo loaded for a nearby destination, should NOT
	 * recommend swapping a task for one that delivers somewhere completely different.
	 */
	@Test
	public void testSwapRecommendation_MidRoute_NoSwapWhenNearDestinationWithCargoLoaded()
	{
		PortLocation boardLocation = PortLocation.ETCETERIA;
		
		// Held tasks - 3 with cargo for Rellekka, 1 without cargo
		List<CourierTask> heldTasks = Arrays.asList(
			createTaskWithCargo(CourierTaskData.RELLEKKA_PLANK_DELIVERY_391, 4),
			createTaskWithCargo(CourierTaskData.RELLEKKA_HALBERD_DELIVERY_399, 6),
			createTaskWithCargo(CourierTaskData.RELLEKKA_REDWOOD_DELIVERY_395, 4),
			createTask(CourierTaskData.SUNSET_COAST_FABRIC_DELIVERY_413, 0)
		);
		
		// Verify: 3 tasks with cargo loaded for Rellekka
		long cargoLoadedForRellekka = heldTasks.stream()
			.filter(t -> t.getCargoTaken() > 0 && t.getData().getDeliveryPort() == PortLocation.RELLEKKA)
			.count();
		assertEquals("Setup: should have 3 tasks with cargo loaded for Rellekka", 3, cargoLoadedForRellekka);
		
		// Available task: Etceteria -> Deepfin Point (3688 XP)
		// Pickup HERE but delivery to completely different direction
		CourierTaskData availableTask = CourierTaskData.DEEPFIN_POINT_MAHOGANY_DELIVERY_417;
		
		List<CourierTaskData> availableTasks = Arrays.asList(availableTask);
		
		SwapRecommendation swap = 
			engine.evaluateSwapOpportunity(availableTasks, boardLocation, heldTasks);
		
		// Should NOT recommend swap - we have cargo loaded for Rellekka
		assertNull("Should NOT recommend swap when mid-route with cargo loaded for nearby destination",
			swap);
	}
	
	// ========================================
	// SWAP CARGO CONFLICT TESTS
	// ========================================
	
	/**
	 * Test: Swap recommendations should not suggest tasks that conflict with remaining held tasks.
	 */
	@Test
	public void testSwapRecommendation_ShouldNotSuggestConflictingTask()
	{
		PortLocation boardLocation = PortLocation.LUNAR_ISLE;
		
		// Held tasks - includes a potion delivery to Lunar Isle
		CourierTask potionTask = createTask(CourierTaskData.LUNAR_ISLE_POTION_DELIVERY_437, 0);
		CourierTask fabricTask = createTask(CourierTaskData.RELLEKKA_FABRIC_DELIVERY_386, 0);
		CourierTask fishTask = createTask(CourierTaskData.SUNSET_COAST_FISH_DELIVERY_389, 0);
		CourierTask furTask = createTask(CourierTaskData.DEEPFIN_POINT_FUR_DELIVERY_398, 0);
		
		List<CourierTask> heldTasks = Arrays.asList(potionTask, fabricTask, fishTask, furTask);
		
		// Available tasks include another potion to Lunar Isle - this would CONFLICT
		List<CourierTaskData> availableTasks = Arrays.asList(
			CourierTaskData.LUNAR_ISLE_POTION_DELIVERY_424,  // CONFLICTS!
			CourierTaskData.LUNAR_ISLE_COAL_DELIVERY_429     // OK
		);
		
		SwapRecommendation swap = engine.evaluateSwapOpportunity(
			availableTasks, boardLocation, heldTasks);
		
		if (swap != null)
		{
			// The swap should NOT recommend the conflicting potion task
			assertNotEquals("Swap should NOT recommend conflicting potion task",
				CourierTaskData.LUNAR_ISLE_POTION_DELIVERY_424, swap.getNewTask());
		}
	}
	
	// ========================================
	// SWAP BUG FIX TESTS
	// ========================================
	
	/**
	 * Test: Should NOT swap a high-value FUTURE task for a low-value CURRENT task.
	 */
	@Test
	public void testSwapBug_ShouldNotSwapFutureTaskForLowValueCurrentTask()
	{
		PortLocation boardLocation = PortLocation.ETCETERIA;
		
		// Held tasks - mixed: some for CURRENT run (cargo loaded), some for NEXT run (no cargo)
		List<CourierTask> heldTasks = Arrays.asList(
			// NEXT RUN tasks - no cargo loaded
			createTask(CourierTaskData.LUNAR_ISLE_POTION_DELIVERY_437, 0),
			createTask(CourierTaskData.LUNAR_ISLE_FISH_DELIVERY_431, 0),
			// CURRENT RUN tasks - cargo loaded
			createTaskWithCargo(CourierTaskData.RELLEKKA_WINE_DELIVERY_303, 4),
			createTask(CourierTaskData.RELLEKKA_TEAK_DELIVERY_406, 0)
		);
		
		// Available task: Etceteria -> Rellekka (fish, 688 XP)
		CourierTaskData availableTask = CourierTaskData.RELLEKKA_FISH_DELIVERY_400;
		List<CourierTaskData> availableTasks = Arrays.asList(availableTask);
		
		SwapRecommendation swap = 
			engine.evaluateSwapOpportunity(availableTasks, boardLocation, heldTasks);
		
		if (swap != null)
		{
			// Should NOT swap out the high-value Lunar Isle tasks (4626 XP, 10017 XP) for 688 XP
			assertNotEquals(
				"Should NOT swap out the Lunar Isle fish task (4626 XP) for 688 XP task",
				CourierTaskData.LUNAR_ISLE_FISH_DELIVERY_431, swap.getTaskToSwap().getData());
			
			assertNotEquals(
				"Should NOT swap out the Lunar Isle potion task (10017 XP)",
				CourierTaskData.LUNAR_ISLE_POTION_DELIVERY_437, swap.getTaskToSwap().getData());
		}
	}
	
	/**
	 * Test: A task that was just recommended as TAKE_NOW should not be immediately
	 * suggested for swap-out after being accepted.
	 */
	@Test
	public void testSwap_ShouldNotImmediatelySwapJustAcceptedTask()
	{
		PortLocation boardLocation = PortLocation.PORT_ROBERTS;
		
		engine.setTeleportOptimizationEnabled(true);
		
		// Initial held tasks
		CourierTask coalTask = createTask(CourierTaskData.LUNAR_ISLE_COAL_DELIVERY_429, 0);
		CourierTask potionTask = createTask(CourierTaskData.LUNAR_ISLE_POTION_DELIVERY_424, 0);
		CourierTask furTask = createTask(CourierTaskData.LUNAR_ISLE_FUR_DELIVERY_316, 0);
		
		List<CourierTask> initialHeldTasks = Arrays.asList(coalTask, potionTask, furTask);
		
		// Available tasks
		List<CourierTaskData> availableTasks = Arrays.asList(
			CourierTaskData.PORT_ROBERTS_FISH_DELIVERY_306,
			CourierTaskData.RELLEKKA_SHIP_PART_DELIVERY_321
		);
		
		// Get initial recommendations (1 slot available)
		List<TaskRecommendation> initialRecs = engine.rankTasksWithSwapEvaluation(
			availableTasks, boardLocation, initialHeldTasks, 1);
		
		// Find the fish task
		TaskRecommendation fishRec = initialRecs.stream()
			.filter(r -> r.getTask() == CourierTaskData.PORT_ROBERTS_FISH_DELIVERY_306)
			.findFirst().orElse(null);
		
		assertNotNull("Fish task should be in recommendations", fishRec);
		assertTrue("Fish task should have a positive score", fishRec.getScore() > 0);
		
		// User accepts the fish task - add it to held tasks
		CourierTask fishTaskHeld = createTask(CourierTaskData.PORT_ROBERTS_FISH_DELIVERY_306, 0);
		List<CourierTask> afterAcceptHeldTasks = Arrays.asList(coalTask, potionTask, furTask, fishTaskHeld);
		
		// Remove fish from available tasks
		List<CourierTaskData> remainingAvailable = Arrays.asList(
			CourierTaskData.RELLEKKA_SHIP_PART_DELIVERY_321
		);
		
		// Get recommendations after accepting (0 slots)
		List<TaskRecommendation> afterAcceptRecs = engine.rankTasksWithSwapEvaluation(
			remainingAvailable, boardLocation, afterAcceptHeldTasks, 0);
		
		// Check if there's a swap that swaps out the just-accepted fish task
		TaskRecommendation swapRec = afterAcceptRecs.stream()
			.filter(r -> r.getType() == RecommendationType.SWAP)
			.findFirst().orElse(null);
		
		if (swapRec != null)
		{
			// The fish task should NOT be suggested for swap-out
			assertNotEquals("Should NOT suggest swapping out the just-accepted fish task",
				CourierTaskData.PORT_ROBERTS_FISH_DELIVERY_306,
				swapRec.getSwapWithTask().getData());
		}
		
		engine.setTeleportOptimizationEnabled(false);
	}
	
	// ========================================
	// INACTIVE TASK SWAP XP LOSS TESTS
	// ========================================
	
	/**
	 * BUG FIX TEST: Should NOT swap an inactive task for a lower-XP task that's only
	 * marginally "on the way" to route destinations.
	 * 
	 * EXACT USER SCENARIO:
	 * - Board: Aldarin
	 * - Teleport optimization enabled
	 * - Held tasks:
	 *   1. [ACTIVE] Sunset Coast -> Rellekka (fabric, 7549 XP)
	 *   2. [ACTIVE] Port Piscarilius -> Rellekka (redwood, 3549 XP)
	 *   3. [ACTIVE] Port Piscarilius -> Lunar Isle (fish, 3345 XP)
	 *   4. [INACTIVE] Aldarin -> Prifddinas (potion, 2802 XP) <-- user unchecked this
	 * 
	 * Available at Aldarin:
	 * - Aldarin -> Port Tyras (shield, 2125 XP) - cargo here, delivers "on-way" (ratio 1.14)
	 * 
	 * PROBLEM: Algorithm recommends swapping potion (2802 XP) for shield (2125 XP)
	 * just because shield picks up HERE and delivers "on the way".
	 * 
	 * WHY THIS IS WRONG:
	 * 1. User loses 677 XP
	 * 2. The "on-way" benefit is marginal (ratio 1.14 means only 14% longer route)
	 * 3. User unchecked the potion task for a reason - but not to replace with lower XP!
	 * 
	 * EXPECTED: No swap recommendation. Losing XP for marginal route improvement is bad.
	 */
	@Test
	public void testSwapInactiveTask_ShouldNotSwapForLowerXpOnWayTask()
	{
		PortLocation boardLocation = PortLocation.ALDARIN;
		
		// Enable teleport optimization
		engine.setTeleportOptimizationEnabled(true);
		
		// Held tasks from the user's scenario
		CourierTask fabricTask = createTask(CourierTaskData.RELLEKKA_FABRIC_DELIVERY_386, 0);    // Sunset Coast -> Rellekka
		CourierTask redwoodTask = createTask(CourierTaskData.RELLEKKA_REDWOOD_DELIVERY_395, 0);  // Port Piscarilius -> Rellekka
		CourierTask fishTask = createTask(CourierTaskData.LUNAR_ISLE_FISH_DELIVERY_420, 0);      // Port Piscarilius -> Lunar Isle
		CourierTask potionTask = createTask(CourierTaskData.PRIFDDINAS_POTION_DELIVERY_297, 0);  // Aldarin -> Prifddinas
		
		List<CourierTask> heldTasks = Arrays.asList(fabricTask, redwoodTask, fishTask, potionTask);
		
		// Mark potionTask as inactive (user unchecked it)
		engine.setTaskActive(potionTask, false);
		assertFalse("Potion task should be inactive", engine.isTaskActive(potionTask));
		
		// Available tasks at Aldarin
		List<CourierTaskData> availableTasks = Arrays.asList(
			CourierTaskData.PORT_TYRAS_SHIELD_DELIVERY_301  // Aldarin -> Port Tyras (2125 XP)
		);
		
		SwapRecommendation swap = 
			engine.evaluateSwapOpportunity(availableTasks, boardLocation, heldTasks);
		
		// KEY ASSERTION: Should NOT recommend swap that loses XP for marginal route improvement
		// The shield task is 677 XP less than the potion task
		assertNull("Should NOT recommend swapping inactive task (2802 XP) for lower XP task (2125 XP) " +
			"even if the new task delivers 'on the way'. Losing XP for marginal route improvement is bad.",
			swap);
		
		// Clean up
		engine.setTaskActive(potionTask, true);
		engine.setTeleportOptimizationEnabled(false);
	}
	
	/**
	 * Contrast test: SHOULD swap inactive task for higher-XP task that's on-way.
	 * 
	 * If the new task delivers on-way AND gains XP, then the swap makes sense.
	 */
	@Test
	public void testSwapInactiveTask_ShouldSwapForHigherXpOnWayTask()
	{
		PortLocation boardLocation = PortLocation.ALDARIN;
		
		// Enable teleport optimization
		engine.setTeleportOptimizationEnabled(true);
		
		// Held tasks - similar to above but with a LOWER XP inactive task
		CourierTask fabricTask = createTask(CourierTaskData.RELLEKKA_FABRIC_DELIVERY_386, 0);
		CourierTask redwoodTask = createTask(CourierTaskData.RELLEKKA_REDWOOD_DELIVERY_395, 0);
		CourierTask fishTask = createTask(CourierTaskData.LUNAR_ISLE_FISH_DELIVERY_420, 0);
		// Use a lower XP task as inactive: Aldarin -> Sunset Coast (fur, 335 XP)
		CourierTask furTask = createTask(CourierTaskData.ALDARIN_FUR_DELIVERY_287, 0);
		
		List<CourierTask> heldTasks = Arrays.asList(fabricTask, redwoodTask, fishTask, furTask);
		
		// Mark furTask as inactive
		engine.setTaskActive(furTask, false);
		assertFalse("Fur task should be inactive", engine.isTaskActive(furTask));
		
		// Available: a HIGHER XP task that picks up here
		// Aldarin -> Port Tyras (shield, 2125 XP) - much higher than 335 XP
		List<CourierTaskData> availableTasks = Arrays.asList(
			CourierTaskData.PORT_TYRAS_SHIELD_DELIVERY_301
		);
		
		SwapRecommendation swap = 
			engine.evaluateSwapOpportunity(availableTasks, boardLocation, heldTasks);
		
		// Should recommend swap: gains 1790 XP AND picks up here
		assertNotNull("Should recommend swapping low-XP inactive task (335 XP) for higher-XP task (2125 XP)",
			swap);
		assertEquals("Should swap out the inactive fur task",
			furTask, swap.getTaskToSwap());
		assertEquals("Should swap in the shield task",
			CourierTaskData.PORT_TYRAS_SHIELD_DELIVERY_301, swap.getNewTask());
		
		// Clean up
		engine.setTaskActive(furTask, true);
		engine.setTeleportOptimizationEnabled(false);
	}
	
	// ========================================
	// REPICKABLE TASK SWAP TESTS
	// ========================================
	
	/**
	 * Test: Should recommend swapping out a task that can be re-picked at a route location.
	 * 
	 * SCENARIO (from user logs):
	 * - Board: Corsair Cove
	 * - Held tasks:
	 *   1. [ACTIVE] Sunset Coast -> Rellekka (fabric, 7549 XP)
	 *   2. [ACTIVE] Rellekka -> Sunset Coast (fish, 4250 XP)
	 *   3. [ACTIVE] Rellekka -> Sunset Coast (warhammer, 3774 XP) <-- REPICKABLE!
	 *   4. [ACTIVE] Ardougne -> Port Roberts (silk, 6413 XP) [CARGO LOADED]
	 *   5. [ACTIVE] Rellekka -> Deepfin Point (warhammer, 7601 XP)
	 * 
	 * The warhammer task (Rellekka -> Sunset Coast, 3774 XP) was picked up at Rellekka.
	 * Since we're going to Rellekka anyway (for the fabric delivery and other pickups),
	 * we can swap this task out now and pick it up again when we reach Rellekka.
	 * 
	 * Available tasks at Corsair Cove include tasks that could be completed BEFORE
	 * reaching Rellekka. By swapping, we gain extra XP on the way without losing
	 * the option to do the warhammer task later.
	 * 
	 * KEY INSIGHT: A task with no cargo loaded that picks up at a location we're 
	 * already visiting is "repickable" - we can drop it and grab it again later.
	 */
	@Test
	public void testSwap_ShouldSwapOutRepickableTaskForTaskCompletableBefore()
	{
		PortLocation boardLocation = PortLocation.CORSAIR_COVE;
		
		// Held tasks from the user's scenario
		// Task 1: Sunset Coast -> Rellekka (fabric) - pickup at Sunset Coast
		CourierTask fabricTask = createTask(CourierTaskData.RELLEKKA_FABRIC_DELIVERY_386, 0);
		// Task 2: Rellekka -> Sunset Coast (fish) - pickup at Rellekka  
		CourierTask fishTask = createTask(CourierTaskData.SUNSET_COAST_FISH_DELIVERY_389, 0);
		// Task 3: Rellekka -> Sunset Coast (warhammer) - pickup at Rellekka - THIS IS REPICKABLE!
		CourierTask warhammerTask = createTask(CourierTaskData.SUNSET_COAST_WARHAMMER_DELIVERY_385, 0);
		// Task 4: Ardougne -> Port Roberts (silk) - HAS CARGO LOADED
		CourierTask silkTask = createTaskWithCargo(CourierTaskData.PORT_ROBERTS_SILK_DELIVERY_323, 3);
		// Task 5: Rellekka -> Deepfin Point (warhammer) - pickup at Rellekka
		CourierTask deepfinWarhammerTask = createTask(CourierTaskData.DEEPFIN_POINT_WARHAMMER_DELIVERY_340, 0);
		
		List<CourierTask> heldTasks = Arrays.asList(
			fabricTask, fishTask, warhammerTask, silkTask, deepfinWarhammerTask
		);
		
		// Verify setup: warhammerTask picks up at Rellekka (a route location)
		assertEquals("Warhammer task should pick up at Rellekka",
			PortLocation.RELLEKKA, warhammerTask.getData().getPickupPort());
		
		// Verify that Rellekka is on the route (multiple tasks go there or pick up there)
		long tasksInvolvingRellekka = heldTasks.stream()
			.filter(t -> t.getData().getPickupPort() == PortLocation.RELLEKKA ||
			             t.getData().getDeliveryPort() == PortLocation.RELLEKKA)
			.count();
		assertTrue("Multiple tasks should involve Rellekka", tasksInvolvingRellekka >= 3);
		
		// Verify warhammerTask has NO cargo loaded (can be dropped and repicked)
		assertEquals("Warhammer task should have no cargo loaded", 0, warhammerTask.getCargoTaken());
		
		// Available task: Something that can be completed before reaching Rellekka
		// Using Corsair Cove -> Port Tyras (herb, 1781 XP) - completes at Port Tyras
		// This route doesn't require going to Rellekka first
		List<CourierTaskData> availableTasks = Arrays.asList(
			CourierTaskData.PORT_TYRAS_HERB_DELIVERY_138  // Corsair Cove -> Port Tyras
		);
		
		SwapRecommendation swap = 
			engine.evaluateSwapOpportunity(availableTasks, boardLocation, heldTasks);
		
		// KEY ASSERTION: Should recommend swapping out the repickable warhammer task
		// Even though we're "losing" the 3774 XP task, we can pick it up again at Rellekka
		// So we should swap it for the herb task we can complete on the way
		assertNotNull("Should recommend swap for repickable task", swap);
		
		// The swap should target the repickable warhammer task (lowest XP among Rellekka pickups)
		assertEquals("Should swap out the repickable warhammer task (lowest XP, repickable at Rellekka)",
			warhammerTask, swap.getTaskToSwap());
		assertEquals("Should swap in the herb task",
			CourierTaskData.PORT_TYRAS_HERB_DELIVERY_138, swap.getNewTask());
	}
	
	/**
	 * Test: Should NOT swap a task with cargo loaded even if pickup is on route.
	 * Once cargo is loaded, the task is committed and can't be "repicked".
	 */
	@Test  
	public void testSwap_ShouldNotSwapTaskWithCargoLoadedEvenIfPickupOnRoute()
	{
		PortLocation boardLocation = PortLocation.CORSAIR_COVE;
		
		// Task with cargo LOADED at Rellekka
		CourierTask cargoLoadedTask = createTaskWithCargo(CourierTaskData.SUNSET_COAST_FISH_DELIVERY_389, 6);
		// Another task going to Rellekka (so Rellekka is on route)
		CourierTask fabricTask = createTask(CourierTaskData.RELLEKKA_FABRIC_DELIVERY_386, 0);
		
		List<CourierTask> heldTasks = Arrays.asList(cargoLoadedTask, fabricTask);
		
		// Verify: cargo is loaded
		assertTrue("Task should have cargo loaded", cargoLoadedTask.getCargoTaken() > 0);
		
		// Available task
		List<CourierTaskData> availableTasks = Arrays.asList(
			CourierTaskData.PORT_TYRAS_HERB_DELIVERY_138
		);
		
		SwapRecommendation swap = 
			engine.evaluateSwapOpportunity(availableTasks, boardLocation, heldTasks);
		
		// Should NOT recommend swapping out the cargo-loaded task
		if (swap != null)
		{
			assertNotEquals("Should NOT swap out task with cargo loaded",
				cargoLoadedTask, swap.getTaskToSwap());
		}
	}
	
	/**
	 * Test: Among multiple repickable tasks, should prefer swapping the lowest XP one.
	 */
	@Test
	public void testSwap_ShouldPreferLowestXpRepickableTask()
	{
		PortLocation boardLocation = PortLocation.CORSAIR_COVE;
		
		// Multiple tasks that pick up at Rellekka (all repickable)
		// Fish: 4250 XP
		CourierTask fishTask = createTask(CourierTaskData.SUNSET_COAST_FISH_DELIVERY_389, 0);
		// Warhammer: 3774 XP (lower)
		CourierTask warhammerTask = createTask(CourierTaskData.SUNSET_COAST_WARHAMMER_DELIVERY_385, 0);
		// Deepfin warhammer: 7601 XP (highest)
		CourierTask deepfinTask = createTask(CourierTaskData.DEEPFIN_POINT_WARHAMMER_DELIVERY_340, 0);
		// A task delivering TO Rellekka (ensures Rellekka is on route)
		CourierTask fabricTask = createTask(CourierTaskData.RELLEKKA_FABRIC_DELIVERY_386, 0);
		// A task with cargo loaded (to be in execution mode)
		CourierTask silkTask = createTaskWithCargo(CourierTaskData.PORT_ROBERTS_SILK_DELIVERY_323, 3);
		
		List<CourierTask> heldTasks = Arrays.asList(
			fishTask, warhammerTask, deepfinTask, fabricTask, silkTask
		);
		
		// Available task that's worth taking
		List<CourierTaskData> availableTasks = Arrays.asList(
			CourierTaskData.PORT_TYRAS_HERB_DELIVERY_138
		);
		
		SwapRecommendation swap = 
			engine.evaluateSwapOpportunity(availableTasks, boardLocation, heldTasks);
		
		assertNotNull("Should recommend swap", swap);
		
		// Should swap out the LOWEST XP repickable task (warhammer at 3774 XP)
		// Not the fish (4250 XP) or deepfin warhammer (7601 XP)
		assertEquals("Should swap out the lowest XP repickable task",
			warhammerTask, swap.getTaskToSwap());
	}
	
	/**
	 * Test: Should NOT swap a repickable task if no cargo loaded (pure planning mode).
	 * In planning mode, we haven't committed to any route yet, so "repickable" 
	 * doesn't provide the same benefit.
	 */
	@Test
	public void testSwap_ShouldNotSwapRepickableInPurePlanningMode()
	{
		PortLocation boardLocation = PortLocation.CORSAIR_COVE;
		
		// All tasks have no cargo loaded (pure planning mode)
		CourierTask fabricTask = createTask(CourierTaskData.RELLEKKA_FABRIC_DELIVERY_386, 0);
		CourierTask fishTask = createTask(CourierTaskData.SUNSET_COAST_FISH_DELIVERY_389, 0);
		CourierTask warhammerTask = createTask(CourierTaskData.SUNSET_COAST_WARHAMMER_DELIVERY_385, 0);
		
		List<CourierTask> heldTasks = Arrays.asList(fabricTask, fishTask, warhammerTask);
		
		// Verify: NO cargo loaded (planning mode)
		assertTrue("Should be in planning mode (no cargo loaded)",
			heldTasks.stream().noneMatch(t -> t.getCargoTaken() > 0));
		
		// Available: lower XP task
		List<CourierTaskData> availableTasks = Arrays.asList(
			CourierTaskData.PORT_TYRAS_HERB_DELIVERY_138  // 1781 XP
		);
		
		SwapRecommendation swap = 
			engine.evaluateSwapOpportunity(availableTasks, boardLocation, heldTasks);
		
		// In pure planning mode, shouldn't swap high-XP tasks for low-XP ones
		// just because they're "repickable"
		if (swap != null)
		{
			// If a swap is recommended, it should NOT be swapping out a high-XP task
			// for the low-XP herb task
			assertTrue("In planning mode, should not swap high-XP task (3774 XP) for low-XP task (1781 XP)",
				swap.getTaskToSwap().getData().getReward() < 
				CourierTaskData.PORT_TYRAS_HERB_DELIVERY_138.getReward() * 1.5);
		}
	}
	
	/**
	 * Test: Should NOT consider a task "repickable" if its pickup is at the current board location.
	 * 
	 * SCENARIO (from user logs):
	 * - Board: Deepfin Point
	 * - Held tasks include: Deepfin Point -> Port Roberts (nickel, 1656 XP) - NO cargo loaded
	 * - Available: Rellekka -> Deepfin Point (warhammer, 7601 XP)
	 * 
	 * The nickel task picks up at Deepfin Point (current location).
	 * It should NOT be flagged as "repickable" because:
	 * 1. You're already at the pickup location - just pick it up now!
	 * 2. Swapping it out to "repick later" makes no sense when you're already there
	 * 
	 * The warhammer task picks up at Rellekka, so accepting it means going to Rellekka first,
	 * then coming back to Deepfin Point to deliver AND repick the nickel task - wasteful!
	 */
	@Test
	public void testSwap_ShouldNotConsiderTaskRepickableIfPickupIsAtCurrentLocation()
	{
		PortLocation boardLocation = PortLocation.DEEPFIN_POINT;
		
		// Held tasks from the user's scenario
		// Silk task: Ardougne -> Port Roberts - HAS CARGO LOADED (execution mode)
		CourierTask silkTask = createTaskWithCargo(CourierTaskData.PORT_ROBERTS_SILK_DELIVERY_323, 3);
		// Fabric task: Sunset Coast -> Rellekka - no cargo
		CourierTask fabricTask = createTask(CourierTaskData.RELLEKKA_FABRIC_DELIVERY_386, 0);
		// Herb task: Corsair Cove -> Port Tyras - HAS CARGO LOADED
		CourierTask herbTask = createTaskWithCargo(CourierTaskData.PORT_TYRAS_HERB_DELIVERY_138, 6);
		// Nickel task: Deepfin Point -> Port Roberts - NO cargo, picks up HERE!
		CourierTask nickelTask = createTask(CourierTaskData.PORT_ROBERTS_NICKEL_DELIVERY_324, 0);
		// Lead task: Deepfin Point -> Rellekka - NO cargo, picks up HERE!
		CourierTask leadTask = createTask(CourierTaskData.RELLEKKA_LEAD_DELIVERY_339, 0);
		
		List<CourierTask> heldTasks = Arrays.asList(
			silkTask, fabricTask, herbTask, nickelTask, leadTask
		);
		
		// Verify setup: nickel task picks up at board location
		assertEquals("Nickel task should pick up at Deepfin Point (board location)",
			boardLocation, nickelTask.getData().getPickupPort());
		
		// Verify we're in execution mode (have cargo loaded)
		assertTrue("Should be in execution mode",
			heldTasks.stream().anyMatch(t -> t.getCargoTaken() > 0));
		
		// Verify nickel task has NO cargo loaded
		assertEquals("Nickel task should have no cargo loaded", 0, nickelTask.getCargoTaken());
		
		// KEY CHECK: Nickel task should NOT be repickable (pickup is at current location)
		assertFalse("Task with pickup at current board location should NOT be repickable",
			engine.isTaskRepickable(nickelTask, heldTasks, boardLocation));
		
		// Available task: Rellekka -> Deepfin Point (warhammer, 7601 XP)
		// This picks up at Rellekka (not here) and delivers to Deepfin Point (here)
		List<CourierTaskData> availableTasks = Arrays.asList(
			CourierTaskData.DEEPFIN_POINT_WARHAMMER_DELIVERY_340
		);
		
		SwapRecommendation swap = 
			engine.evaluateSwapOpportunity(availableTasks, boardLocation, heldTasks);
		
		// Should NOT recommend swapping out the nickel task for the warhammer
		// because the nickel task picks up HERE - just pick it up now!
		if (swap != null)
		{
			assertNotEquals("Should NOT swap out task that picks up at current location",
				nickelTask, swap.getTaskToSwap());
		}
	}
	
	/**
	 * Contrast test: A task that picks up ELSEWHERE (not at board) should still be repickable
	 * if its pickup location is on the route.
	 */
	@Test
	public void testSwap_TaskPickingUpElsewhereShouldStillBeRepickable()
	{
		PortLocation boardLocation = PortLocation.CORSAIR_COVE;
		
		// Fabric task: Sunset Coast -> Rellekka - no cargo, picks up at Sunset Coast (not here)
		CourierTask fabricTask = createTask(CourierTaskData.RELLEKKA_FABRIC_DELIVERY_386, 0);
		// Silk task with cargo loaded (execution mode)
		CourierTask silkTask = createTaskWithCargo(CourierTaskData.PORT_ROBERTS_SILK_DELIVERY_323, 3);
		// Another task delivering to Sunset Coast (so Sunset Coast is on route)
		CourierTask warhammerTask = createTask(CourierTaskData.SUNSET_COAST_WARHAMMER_DELIVERY_385, 0);
		
		List<CourierTask> heldTasks = Arrays.asList(fabricTask, silkTask, warhammerTask);
		
		// Verify: fabric task picks up at Sunset Coast (not board location)
		assertNotEquals("Fabric task should NOT pick up at board location",
			boardLocation, fabricTask.getData().getPickupPort());
		assertEquals("Fabric task should pick up at Sunset Coast",
			PortLocation.SUNSET_COAST, fabricTask.getData().getPickupPort());
		
		// Fabric task SHOULD be repickable (picks up elsewhere, on route)
		assertTrue("Task with pickup elsewhere on route SHOULD be repickable",
			engine.isTaskRepickable(fabricTask, heldTasks, boardLocation));
	}
}
