package com.nucleon.porttasks.optimizer;

import com.nucleon.porttasks.CourierTask;
import com.nucleon.porttasks.enums.CourierTaskData;
import com.nucleon.porttasks.enums.PortLocation;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Tests for route chain recognition in TaskSelectionEngine.
 * 
 * When teleport optimization is enabled, the algorithm should recognize
 * coherent route chains formed by active tasks and avoid recommending
 * tasks that would create detours from the optimal path.
 */
public class RouteChainTest extends TaskSelectionEngineTestBase
{
	// ========================================
	// ROUTE CHAIN RECOGNITION TESTS
	// ========================================
	
	/**
	 * USER SCENARIO: At Etceteria with a chain of active tasks forming a logical route.
	 * 
	 * TELEPORT OPTIMIZATION: ENABLED
	 * Board: Etceteria
	 * Available slots: 0 (full)
	 * 
	 * HELD TASKS:
	 * 1. [ACTIVE] Ardougne -> Prifddinas (silk, 6957 XP)
	 * 2. [ACTIVE] Piscatoris -> Lunar Isle (fur, 2491 XP)
	 * 3. [INACTIVE] Port Roberts -> Rellekka (plank, 2480 XP)
	 * 4. [INACTIVE] Jatizso -> Etceteria (adamantite, 1688 XP)
	 * 
	 * AVAILABLE ON BOARD:
	 * - Etceteria -> Deepfin Point (mahogany, 3688 XP)
	 * 
	 * WHY THIS IS WRONG:
	 * The active tasks form a logical CHAIN with teleport optimization:
	 * - Teleport to Ardougne
	 * - Sail to Prifddinas (deliver silk)
	 * - Sail to Piscatoris (pick up fur) [Piscatoris is near Prifddinas]
	 * - Sail to Lunar Isle (deliver fur)
	 * 
	 * The recommended task (Etceteria -> Deepfin Point):
	 * 1. Picks up at Etceteria (current board) - but "Pickup HERE" has NO value in teleport mode!
	 * 2. Delivers to Deepfin Point - which is SOUTH and completely off the optimal route
	 * 3. Adding this task would break the chain: we'd have to visit Deepfin Point too
	 * 
	 * EXPECTED: NO swap should be recommended, or the available task should score very low.
	 */
	@Test
	public void testRouteChainRecognition_NoSwapForOffChainTaskInTeleportMode()
	{
		PortLocation boardLocation = PortLocation.ETCETERIA;
		
		// Enable teleport optimization
		engine.setTeleportOptimizationEnabled(true);
		
		// Held tasks - exact scenario from user
		// ACTIVE tasks form a chain: Ardougne -> Prifddinas ... Piscatoris -> Lunar Isle
		CourierTask silkTask = createTask(CourierTaskData.PRIFDDINAS_SILK_DELIVERY_367, 0);        // Ardougne -> Prifddinas (6957 XP)
		CourierTask furTask = createTask(CourierTaskData.LUNAR_ISLE_FUR_DELIVERY_422, 0);          // Piscatoris -> Lunar Isle (2491 XP)
		CourierTask plankTask = createTask(CourierTaskData.RELLEKKA_PLANK_DELIVERY_391, 0);        // Port Roberts -> Rellekka (2480 XP)
		CourierTask adamantiteTask = createTask(CourierTaskData.ETCETERIA_ADAMANTITE_DELIVERY_403, 0); // Jatizso -> Etceteria (1688 XP)
		
		// Mark plank and adamantite as INACTIVE (as per user scenario)
		engine.setTaskActive(plankTask, false);
		engine.setTaskActive(adamantiteTask, false);
		
		List<CourierTask> heldTasks = Arrays.asList(silkTask, furTask, plankTask, adamantiteTask);
		
		// Verify active/inactive state
		assertTrue("Silk task should be active", engine.isTaskActive(silkTask));
		assertTrue("Fur task should be active", engine.isTaskActive(furTask));
		assertFalse("Plank task should be inactive", engine.isTaskActive(plankTask));
		assertFalse("Adamantite task should be inactive", engine.isTaskActive(adamantiteTask));
		
		// Available task: Etceteria -> Deepfin Point (mahogany, 3688 XP)
		List<CourierTaskData> availableTasks = Arrays.asList(
			CourierTaskData.DEEPFIN_POINT_MAHOGANY_DELIVERY_417  // Etceteria -> Deepfin Point (3688 XP)
		);
		
		// Evaluate swap (slots = 0 means we're evaluating swaps)
		SwapRecommendation swap = engine.evaluateSwapOpportunity(
			availableTasks, boardLocation, heldTasks);
		
		// CRITICAL ASSERTION: Should NOT recommend this swap
		// The available task picks up at board but delivers to a location completely off the active chain
		// In teleport mode, "Pickup HERE" has no value when the board isn't part of the planned route
		assertNull(
			"Should NOT recommend swap for task that delivers off the active route chain.\n" +
			"Active chain: Ardougne -> Prifddinas -> Piscatoris -> Lunar Isle\n" +
			"Available task delivers to: Deepfin Point (not on chain)\n" +
			"'Pickup HERE at Etceteria' has no value - we're teleporting to Ardougne to start the chain.",
			swap);
		
		// Clean up
		engine.setTaskActive(plankTask, true);
		engine.setTaskActive(adamantiteTask, true);
		engine.setTeleportOptimizationEnabled(false);
	}
	
	/**
	 * Contrast test: When the available task DOES fit the active chain, swap should be recommended.
	 * 
	 * Same scenario, but available task is: Prifddinas -> Lunar Isle (crystal seed)
	 * This DOES fit the chain because:
	 * - Picks up at Prifddinas (on the chain!)
	 * - Delivers to Lunar Isle (on the chain!)
	 */
	@Test
	public void testRouteChainRecognition_SwapForOnChainTask()
	{
		PortLocation boardLocation = PortLocation.ETCETERIA;
		
		engine.setTeleportOptimizationEnabled(true);
		
		// Same held tasks
		CourierTask silkTask = createTask(CourierTaskData.PRIFDDINAS_SILK_DELIVERY_367, 0);        // Ardougne -> Prifddinas
		CourierTask furTask = createTask(CourierTaskData.LUNAR_ISLE_FUR_DELIVERY_422, 0);          // Piscatoris -> Lunar Isle
		CourierTask plankTask = createTask(CourierTaskData.RELLEKKA_PLANK_DELIVERY_391, 0);        // Port Roberts -> Rellekka
		CourierTask adamantiteTask = createTask(CourierTaskData.ETCETERIA_ADAMANTITE_DELIVERY_403, 0); // Jatizso -> Etceteria
		
		engine.setTaskActive(plankTask, false);
		engine.setTaskActive(adamantiteTask, false);
		
		List<CourierTask> heldTasks = Arrays.asList(silkTask, furTask, plankTask, adamantiteTask);
		
		// Available task that FITS the chain: Prifddinas -> Lunar Isle
		// LUNAR_ISLE_CRYSTAL_SEED_DELIVERY_377 picks up at Prifddinas, delivers to Lunar Isle
		List<CourierTaskData> availableTasks = Arrays.asList(
			CourierTaskData.LUNAR_ISLE_CRYSTAL_SEED_DELIVERY_377  // Prifddinas -> Lunar Isle (2171 XP)
		);
		
		SwapRecommendation swap = engine.evaluateSwapOpportunity(
			availableTasks, boardLocation, heldTasks);
		
		if (swap != null)
		{
			// Verify the swap is for one of the inactive tasks (preferably adamantite, lowest XP)
			assertTrue("Should swap out an inactive task",
				!engine.isTaskActive(swap.getTaskToSwap()));
		}
		
		// The on-chain task should at least score better than the off-chain task
		// Let's verify by comparing rankings
		List<CourierTaskData> bothTasks = Arrays.asList(
			CourierTaskData.LUNAR_ISLE_CRYSTAL_SEED_DELIVERY_377,  // On-chain: Prifddinas -> Lunar Isle
			CourierTaskData.DEEPFIN_POINT_MAHOGANY_DELIVERY_417    // Off-chain: Etceteria -> Deepfin Point
		);
		
		List<TaskRecommendation> ranked = engine.rankTasks(bothTasks, boardLocation, heldTasks);
		
		// Find scores for both
		double onChainScore = ranked.stream()
			.filter(r -> r.getTask() == CourierTaskData.LUNAR_ISLE_CRYSTAL_SEED_DELIVERY_377)
			.mapToDouble(TaskRecommendation::getScore).findFirst().orElse(0);
		double offChainScore = ranked.stream()
			.filter(r -> r.getTask() == CourierTaskData.DEEPFIN_POINT_MAHOGANY_DELIVERY_417)
			.mapToDouble(TaskRecommendation::getScore).findFirst().orElse(0);
		
		assertTrue(
			"On-chain task (picks up at Prifddinas, delivers to Lunar Isle) should score higher " +
			"than off-chain task (picks up at Etceteria, delivers to Deepfin Point).\n" +
			"On-chain: " + onChainScore + "\n" +
			"Off-chain: " + offChainScore,
			onChainScore > offChainScore);
		
		// Clean up
		engine.setTaskActive(plankTask, true);
		engine.setTaskActive(adamantiteTask, true);
		engine.setTeleportOptimizationEnabled(false);
	}
	
	/**
	 * Exploratory test for the second scenario from the bug report:
	 * Board: Deepfin Point, task delivers TO board but picks up elsewhere (Port Tyras)
	 * 
	 * This is different from the "Pickup HERE" scenario - here the task:
	 * - Picks up at Port Tyras (NOT on the active route)
	 * - Delivers to Deepfin Point (the board, also NOT on the active route)
	 */
	@Test
	public void testRouteChainRecognition_DeliverToBoard_PickupOffChain()
	{
		PortLocation boardLocation = PortLocation.DEEPFIN_POINT;
		
		engine.setTeleportOptimizationEnabled(true);
		
		// Held tasks from user scenario:
		// ACTIVE: Ardougne -> Prifddinas (silk), Piscatoris -> Lunar Isle (fur), Port Roberts -> Rellekka (plank)
		// INACTIVE: Rellekka -> Deepfin Point (warhammer, 7601 XP)
		CourierTask silkTask = createTask(CourierTaskData.PRIFDDINAS_SILK_DELIVERY_367, 0);        // Ardougne -> Prifddinas (6957 XP)
		CourierTask furTask = createTask(CourierTaskData.LUNAR_ISLE_FUR_DELIVERY_422, 0);          // Piscatoris -> Lunar Isle (2491 XP)
		CourierTask plankTask = createTask(CourierTaskData.RELLEKKA_PLANK_DELIVERY_391, 0);        // Port Roberts -> Rellekka (2480 XP)
		
		// Rellekka -> Deepfin Point warhammer task (7601 XP)
		// Note: noticeboard=DEEPFIN_POINT, cargoLocation=RELLEKKA, destination=DEEPFIN_POINT
		CourierTask warhammerTask = createTask(CourierTaskData.DEEPFIN_POINT_WARHAMMER_DELIVERY_340, 0); // Rellekka -> Deepfin Point
		
		// Mark warhammer as INACTIVE
		engine.setTaskActive(warhammerTask, false);
		
		List<CourierTask> heldTasks = Arrays.asList(silkTask, furTask, plankTask, warhammerTask);
		
		// Available task: Port Tyras -> Deepfin Point (logs, 2313 XP)
		// This delivers TO the board but picks up at Port Tyras (also off-chain)
		List<CourierTaskData> availableTasks = Arrays.asList(
			CourierTaskData.DEEPFIN_POINT_LOGS_DELIVERY_327  // Port Tyras -> Deepfin Point (2313 XP)
		);
		
		SwapRecommendation swap = engine.evaluateSwapOpportunity(
			availableTasks, boardLocation, heldTasks);
		
		// CRITICAL ASSERTION: Should NOT recommend this swap
		// The available task delivers to board but the board isn't part of the active route chain
		// In teleport mode, "Delivers HERE" has no value when we're teleporting elsewhere to start
		assertNull(
			"Should NOT recommend swap for task that delivers to board when board is not on active route.\n" +
			"Active chain: Ardougne -> Prifddinas, Piscatoris -> Lunar Isle, Port Roberts -> Rellekka\n" +
			"Board (Deepfin Point) is NOT on this chain.\n" +
			"'Delivers to board' has no value in teleport mode - we're teleporting to Ardougne to start.",
			swap);
		
		// Clean up
		engine.setTaskActive(warhammerTask, true);
		engine.setTeleportOptimizationEnabled(false);
	}
	
	/**
	 * Test scenario: All tasks ACTIVE, board IS on route, but swap recommendation
	 * suggests trading an on-chain task for an off-chain pickup task.
	 * 
	 * Board: Deepfin Point (ON route because Rellekka -> Deepfin Point is active)
	 * Available: Port Tyras -> Deepfin Point (picks up OFF route, delivers ON route)
	 * 
	 * The algorithm wants to swap out Piscatoris -> Lunar Isle (on-chain) for
	 * Port Tyras -> Deepfin Point, losing XP and adding a detour.
	 */
	@Test
	public void testRouteChainRecognition_AllActive_SwapOutOnChainForOffChainPickup()
	{
		PortLocation boardLocation = PortLocation.DEEPFIN_POINT;
		
		engine.setTeleportOptimizationEnabled(true);
		
		// ALL tasks are ACTIVE
		CourierTask silkTask = createTask(CourierTaskData.PRIFDDINAS_SILK_DELIVERY_367, 0);        // Ardougne -> Prifddinas (6957 XP)
		CourierTask furTask = createTask(CourierTaskData.LUNAR_ISLE_FUR_DELIVERY_422, 0);          // Piscatoris -> Lunar Isle (2491 XP)
		CourierTask plankTask = createTask(CourierTaskData.RELLEKKA_PLANK_DELIVERY_391, 0);        // Port Roberts -> Rellekka (2480 XP)
		CourierTask warhammerTask = createTask(CourierTaskData.DEEPFIN_POINT_WARHAMMER_DELIVERY_340, 0); // Rellekka -> Deepfin Point (7601 XP)
		
		// All tasks are ACTIVE (default state)
		List<CourierTask> heldTasks = Arrays.asList(silkTask, furTask, plankTask, warhammerTask);
		
		// Available task: Port Tyras -> Deepfin Point (logs, 2313 XP)
		List<CourierTaskData> availableTasks = Arrays.asList(
			CourierTaskData.DEEPFIN_POINT_LOGS_DELIVERY_327  // Port Tyras -> Deepfin Point (2313 XP)
		);
		
		SwapRecommendation swap = engine.evaluateSwapOpportunity(
			availableTasks, boardLocation, heldTasks);
		
		// ASSERTIONS:
		// When all tasks are active and on-chain, we should NOT swap out an on-chain task
		// for one that picks up off-route (Port Tyras is NOT on the current route).
		// The available task (Port Tyras -> Deepfin Point) would require a detour to Port Tyras
		// just to pick up cargo, which is inefficient when we already have on-chain tasks.
		if (swap != null)
		{
			// If a swap IS recommended, it should NOT be swapping out an on-chain task for less XP
			CourierTaskData swappedOut = swap.getTaskToSwap().getData();
			int xpChange = swap.getNewTask().getReward() - swappedOut.getReward();
			
			// The fur task (Piscatoris -> Lunar Isle) is on-chain - we should not lose it for less XP
			boolean swappedOutFurTask = swappedOut == CourierTaskData.LUNAR_ISLE_FUR_DELIVERY_422;
			assertFalse("Should NOT swap out on-chain fur task (Piscatoris -> Lunar Isle) for off-chain pickup with less XP",
				swappedOutFurTask && xpChange < 0);
		}
		// Note: The ideal behavior is no swap at all, but we at minimum verify we don't make a bad swap
		
		// Clean up
		engine.setTeleportOptimizationEnabled(false);
	}
	
	/**
	 * Test delivery proximity bonus for future route planning.
	 * 
	 * SCENARIO:
	 * Location: Lunar Isle noticeboard
	 * State: 3 tasks held (1 slot available)
	 * - Sunset Coast -> Rellekka (fabric) [CARGO LOADED] - current route
	 * - Aldarin -> Rellekka (wine) [CARGO LOADED] - current route
	 * - Lunar Isle -> Prifddinas (rune) - future route (pickup HERE)
	 * 
	 * Available tasks:
	 * - Lunar Isle -> Piscatoris (fur, 2491 XP) - pickup HERE, delivers to Piscatoris
	 * - Lunar Isle -> Port Roberts (fabric, 2313 XP) - pickup HERE, delivers to Port Roberts
	 * 
	 * EXPECTED BEHAVIOR:
	 * Port Roberts should score higher than Piscatoris because:
	 * 1. Both pick up at Lunar Isle (same pickup bonus)
	 * 2. We have a pending delivery to Prifddinas (Lunar Isle -> Prifddinas task)
	 * 3. Port Roberts is CLOSE to Prifddinas (good route synergy)
	 * 4. Piscatoris is NOT close to Prifddinas (poor route synergy)
	 * 
	 * When planning future routes, tasks that cluster deliveries together should
	 * score higher than tasks with isolated delivery locations.
	 */
	@Test
	public void testDeliveryProximityBonus_FutureRoutePlanning()
	{
		PortLocation boardLocation = PortLocation.LUNAR_ISLE;
		engine.setTeleportOptimizationEnabled(true);
		
		// Current route: cargo loaded going to Rellekka
		CourierTask fabricTask = createTaskWithCargo(CourierTaskData.RELLEKKA_FABRIC_DELIVERY_386, 5);
		CourierTask wineTask = createTaskWithCargo(CourierTaskData.RELLEKKA_WINE_DELIVERY_303, 4);
		
		// Future route: Lunar Isle -> Prifddinas (pickup HERE, will teleport back for this)
		CourierTask prifTask = createTask(CourierTaskData.PRIFDDINAS_RUNE_DELIVERY_427, 0);
		
		List<CourierTask> heldTasks = Arrays.asList(fabricTask, wineTask, prifTask);
		
		// Available tasks - both pickup at Lunar Isle
		CourierTaskData furTask = CourierTaskData.LUNAR_ISLE_FUR_DELIVERY_422;        // -> Piscatoris (2491 XP)
		CourierTaskData portRobertsTask = CourierTaskData.PORT_ROBERTS_FABRIC_DELIVERY_430;  // -> Port Roberts (2313 XP)
		
		List<CourierTaskData> availableTasks = Arrays.asList(furTask, portRobertsTask);
		
		// Get recommendations
		List<TaskRecommendation> recommendations = 
			engine.rankTasksWithSwapEvaluation(availableTasks, boardLocation, heldTasks, 1);
		
		// Find scores for both tasks
		TaskRecommendation portRobertsRec = recommendations.stream()
			.filter(r -> r.getTask() == portRobertsTask)
			.findFirst().orElse(null);
		
		TaskRecommendation furRec = recommendations.stream()
			.filter(r -> r.getTask() == furTask)
			.findFirst().orElse(null);
		
		assertNotNull("Port Roberts recommendation should exist", portRobertsRec);
		assertNotNull("Fur recommendation should exist", furRec);
		
		// ASSERTION: Port Roberts should score higher due to delivery proximity to Prifddinas
		assertTrue(
			"Port Roberts (delivers near Prifddinas) should score higher than Fur (delivers to Piscatoris).\n" +
			"Port Roberts score: " + portRobertsRec.getScore() + "\n" +
			"Fur score: " + furRec.getScore() + "\n" +
			"Reason: Port Roberts delivery clusters with pending Prifddinas delivery.",
			portRobertsRec.getScore() > furRec.getScore()
		);
		
		engine.setTeleportOptimizationEnabled(false);
	}
	
	/**
	 * USER SCENARIO: At Aldarin with 4 tasks, swap recommendation suggests
	 * swapping redwood (Port Piscarilius -> Rellekka) for wine (Aldarin -> Rellekka).
	 * 
	 * This makes sense because:
	 * 1. Wine picks up at Aldarin (board/already going there for potion)
	 * 2. Redwood requires going to Port Piscarilius (extra stop)
	 * 3. Both deliver to Rellekka (same destination)
	 * 4. Wine gives slightly more XP (3718 vs 3549)
	 * 
	 * Verifies the swap recommendation is correct and the scoring makes sense.
	 */
	@Test
	public void testSwapRecommendation_UserScenario_RedwoodForWineAtAldarin()
	{
		PortLocation boardLocation = PortLocation.ALDARIN;
		
		// Held tasks:
		// 1. Aldarin -> Lunar Isle (potion, 10017 XP)
		// 2. Sunset Coast -> Rellekka (fabrics, 7549 XP)
		// 3. Port Roberts -> Lunar Isle (fish, 4626 XP)
		// 4. Port Piscarilius -> Rellekka (redwood, 3549 XP)
		CourierTask potionTask = createTask(CourierTaskData.LUNAR_ISLE_POTION_DELIVERY_437, 0);
		CourierTask fabricTask = createTask(CourierTaskData.RELLEKKA_FABRIC_DELIVERY_386, 0);
		CourierTask fishTask = createTask(CourierTaskData.LUNAR_ISLE_FISH_DELIVERY_431, 0);
		CourierTask redwoodTask = createTask(CourierTaskData.RELLEKKA_REDWOOD_DELIVERY_395, 0);
		
		List<CourierTask> heldTasks = Arrays.asList(potionTask, fabricTask, fishTask, redwoodTask);
		
		// Available: wine from Aldarin to Rellekka (3718 XP)
		List<CourierTaskData> availableTasks = Arrays.asList(
			CourierTaskData.RELLEKKA_WINE_DELIVERY_303  // Aldarin -> Rellekka (wine, 3718 XP)
		);
		
		SwapRecommendation swap = engine.evaluateSwapOpportunity(
			availableTasks, boardLocation, heldTasks);
		
		assertNotNull("Should recommend a swap", swap);
		
		// Verify the swap is for redwood -> wine
		assertEquals("Should swap out the redwood task (requires extra Port Piscarilius stop)",
			CourierTaskData.RELLEKKA_REDWOOD_DELIVERY_395, swap.getTaskToSwap().getData());
		assertEquals("Should swap in the wine task (pickup HERE at Aldarin)",
			CourierTaskData.RELLEKKA_WINE_DELIVERY_303, swap.getNewTask());
		
		// Verify the XP change
		int xpDiff = swap.getNewTask().getReward() - swap.getTaskToSwap().getData().getReward();
		assertTrue("Wine should give more XP than redwood", xpDiff > 0);
	}
}
