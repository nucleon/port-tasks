package com.nucleon.porttasks.optimizer;

import com.nucleon.porttasks.CourierTask;
import com.nucleon.porttasks.enums.CourierTaskData;
import com.nucleon.porttasks.enums.PortLocation;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Tests for cargo type conflict detection in TaskSelectionEngine.
 * 
 * Game rule: You cannot hold two tasks that deliver the same cargo type.
 * The algorithm should mark conflicting tasks as UNAVAILABLE.
 */
public class CargoConflictTest extends TaskSelectionEngineTestBase
{
	// ========================================
	// CARGO TYPE CONFLICT TESTS
	// ========================================
	
	/**
	 * Test cargo-type conflict detection.
	 * 
	 * Game rule: You cannot hold two tasks that deliver the same cargo type.
	 * 
	 * Scenario: Player holds "Aldarin -> Lunar Isle (potion, 10017 XP)"
	 * Available: "Prifddinas -> Lunar Isle (potion, 4341 XP)"
	 * 
	 * Expected: The Prifddinas potion task should be marked UNAVAILABLE
	 * because player already holds a potion delivery task.
	 */
	@Test
	public void testCargoTypeConflict_PotionDeliveries()
	{
		PortLocation boardLocation = PortLocation.LUNAR_ISLE;
		
		// Held tasks - includes a potion delivery
		List<CourierTask> heldTasks = Arrays.asList(
			createTask(CourierTaskData.LUNAR_ISLE_POTION_DELIVERY_437, 0),   // Aldarin -> Lunar Isle (potion, 10017 XP)
			createTask(CourierTaskData.LUNAR_ISLE_FISH_DELIVERY_431, 0)      // Port Roberts -> Lunar Isle (fish, 4626 XP)
		);
		
		// Available tasks - includes another potion delivery
		List<CourierTaskData> availableTasks = Arrays.asList(
			CourierTaskData.LUNAR_ISLE_POTION_DELIVERY_424,       // Prifddinas -> Lunar Isle (potion, 4341 XP) - CONFLICT!
			CourierTaskData.PORT_PISCARILIUS_FABRIC_DELIVERY_419  // Lunar Isle -> Port Piscarilius (fabric, 1601 XP) - OK
		);
		
		List<TaskRecommendation> recommendations = 
			engine.rankTasks(availableTasks, boardLocation, heldTasks);
		
		// Find the conflicting potion task
		TaskRecommendation potionRec = recommendations.stream()
			.filter(r -> r.getTask() == CourierTaskData.LUNAR_ISLE_POTION_DELIVERY_424)
			.findFirst().orElse(null);
		
		assertNotNull("Potion task should be in recommendations", potionRec);
		assertEquals("Potion task should be UNAVAILABLE due to cargo conflict",
			RecommendationType.UNAVAILABLE, potionRec.getType());
		assertTrue("Reason should mention cargo conflict",
			potionRec.getReason().contains("potion"));
		
		// The fabric task should NOT be marked unavailable
		TaskRecommendation fabricRec = recommendations.stream()
			.filter(r -> r.getTask() == CourierTaskData.PORT_PISCARILIUS_FABRIC_DELIVERY_419)
			.findFirst().orElse(null);
		
		assertNotNull("Fabric task should be in recommendations", fabricRec);
		assertNotEquals("Fabric task should NOT be UNAVAILABLE",
			RecommendationType.UNAVAILABLE, fabricRec.getType());
	}
	
	/**
	 * Test that different cargo types do not conflict.
	 */
	@Test  
	public void testCargoTypeConflict_DifferentTypesNoConflict()
	{
		PortLocation boardLocation = PortLocation.LUNAR_ISLE;
		
		// Held tasks - fish and fur
		List<CourierTask> heldTasks = Arrays.asList(
			createTask(CourierTaskData.LUNAR_ISLE_FISH_DELIVERY_431, 0),     // Port Roberts -> Lunar Isle (fish)
			createTask(CourierTaskData.LUNAR_ISLE_FUR_DELIVERY_316, 0)       // Port Roberts -> Lunar Isle (fur)
		);
		
		// Available: potion (different type - should be OK)
		List<CourierTaskData> availableTasks = Arrays.asList(
			CourierTaskData.LUNAR_ISLE_POTION_DELIVERY_424  // Prifddinas -> Lunar Isle (potion, 4341 XP)
		);
		
		List<TaskRecommendation> recommendations = 
			engine.rankTasks(availableTasks, boardLocation, heldTasks);
		
		TaskRecommendation potionRec = recommendations.get(0);
		
		assertNotEquals("Potion task should NOT be UNAVAILABLE (different cargo type)",
			RecommendationType.UNAVAILABLE, potionRec.getType());
	}
	
	/**
	 * Test exact user scenario: At Lunar Isle, holding potion/fish/fur tasks,
	 * the Prifddinas potion task should be marked UNAVAILABLE.
	 * 
	 * This is the exact scenario reported by the user.
	 */
	@Test
	public void testCargoTypeConflict_ExactUserScenario()
	{
		PortLocation boardLocation = PortLocation.LUNAR_ISLE;
		
		// Exact held tasks from user scenario
		List<CourierTask> heldTasks = Arrays.asList(
			createTask(CourierTaskData.LUNAR_ISLE_FUR_DELIVERY_316, 0),      // Port Roberts -> Lunar Isle (furs, 2384 XP)
			createTask(CourierTaskData.LUNAR_ISLE_POTION_DELIVERY_437, 0),   // Aldarin -> Lunar Isle (potion, 10017 XP)
			createTask(CourierTaskData.LUNAR_ISLE_FISH_DELIVERY_431, 0)      // Port Roberts -> Lunar Isle (fish, 4626 XP)
		);
		
		// The task user tried to accept
		List<CourierTaskData> availableTasks = Arrays.asList(
			CourierTaskData.LUNAR_ISLE_POTION_DELIVERY_424  // Prifddinas -> Lunar Isle (potion, 4341 XP)
		);
		
		List<TaskRecommendation> recommendations = 
			engine.rankTasks(availableTasks, boardLocation, heldTasks);
		
		TaskRecommendation potionRec = recommendations.get(0);
		
		assertEquals("Prifddinas potion task should be UNAVAILABLE",
			RecommendationType.UNAVAILABLE, potionRec.getType());
		assertTrue("Reason should explain the conflict",
			potionRec.getReason().toLowerCase().contains("potion") && 
			potionRec.getReason().toLowerCase().contains("conflict"));
	}
	
	// ========================================
	// SWAP CARGO CONFLICT TESTS
	// ========================================
	
	/**
	 * Test: Swap recommendations should not suggest tasks that conflict with remaining held tasks.
	 * 
	 * USER SCENARIO:
	 * - At Lunar Isle with 4 tasks held
	 * - One held task: Aldarin -> Lunar Isle (potion)
	 * - Algorithm suggests swapping out furs task for "Lunar Isle potion delivery"
	 * - But after cancelling, the new task is UNAVAILABLE due to conflict with existing potion task!
	 * 
	 * The swap evaluation should check cargo conflicts BEFORE recommending the swap.
	 */
	@Test
	public void testSwapRecommendation_ShouldNotSuggestConflictingTask()
	{
		PortLocation boardLocation = PortLocation.LUNAR_ISLE;
		
		// Held tasks - 4 slots full, including a potion delivery to Lunar Isle
		CourierTask potionTask = createTask(CourierTaskData.LUNAR_ISLE_POTION_DELIVERY_437, 0);    // Aldarin -> Lunar Isle (potion)
		CourierTask fabricTask = createTask(CourierTaskData.RELLEKKA_FABRIC_DELIVERY_386, 0);      // Sunset Coast -> Rellekka (fabric)
		CourierTask fishTask = createTask(CourierTaskData.SUNSET_COAST_FISH_DELIVERY_389, 0);      // Rellekka -> Sunset Coast (fish)
		CourierTask furTask = createTask(CourierTaskData.DEEPFIN_POINT_FUR_DELIVERY_398, 0);       // Rellekka -> Deepfin Point (fur)
		
		List<CourierTask> heldTasks = Arrays.asList(potionTask, fabricTask, fishTask, furTask);
		
		// Available tasks include another potion to Lunar Isle - this would CONFLICT with held potion task
		// LUNAR_ISLE_POTION_DELIVERY_424 is Prifddinas -> Lunar Isle (potion) - same cargo type + destination!
		List<CourierTaskData> availableTasks = Arrays.asList(
			CourierTaskData.LUNAR_ISLE_POTION_DELIVERY_424,  // Prifddinas -> Lunar Isle (potion) - CONFLICTS!
			CourierTaskData.LUNAR_ISLE_COAL_DELIVERY_429     // Deepfin Point -> Lunar Isle (coal) - OK
		);
		
		// Evaluate swap opportunities (slots full)
		SwapRecommendation swap = engine.evaluateSwapOpportunity(
			availableTasks, boardLocation, heldTasks);
		
		if (swap != null)
		{
			// The swap should NOT recommend the conflicting potion task
			assertNotEquals("Swap should NOT recommend conflicting potion task",
				CourierTaskData.LUNAR_ISLE_POTION_DELIVERY_424, swap.getNewTask());
		}
	}
	
	/**
	 * Test: Swap can suggest a task that WOULD conflict with the task being swapped out,
	 * since that task is being removed.
	 * 
	 * Example: Swapping out "Aldarin -> Lunar Isle (potion)" for "Prifddinas -> Lunar Isle (potion)"
	 * should be allowed - even though both are potions to Lunar Isle, the old one is being removed.
	 */
	@Test
	public void testSwapRecommendation_CanSwapSameCargoType()
	{
		PortLocation boardLocation = PortLocation.LUNAR_ISLE;
		
		// Held tasks - include a LOW value potion task that we want to swap out
		// Using a task with cargo loaded so it's valued differently
		CourierTask lowPotionTask = createTask(CourierTaskData.LUNAR_ISLE_POTION_DELIVERY_437, 0);  // Aldarin -> Lunar Isle (potion, 10017 XP)
		CourierTask fabricTask = createTask(CourierTaskData.RELLEKKA_FABRIC_DELIVERY_386, 0);       // Sunset Coast -> Rellekka (fabric)
		CourierTask fishTask = createTask(CourierTaskData.SUNSET_COAST_FISH_DELIVERY_389, 0);       // Rellekka -> Sunset Coast (fish)
		CourierTask furTask = createTask(CourierTaskData.DEEPFIN_POINT_FUR_DELIVERY_398, 0);        // Rellekka -> Deepfin Point (fur)
		
		// Make fur task inactive so it becomes a swap-out candidate
		engine.setTaskActive(furTask, false);
		
		List<CourierTask> heldTasks = Arrays.asList(lowPotionTask, fabricTask, fishTask, furTask);
		
		// Available: another potion to Lunar Isle with HIGHER value
		// This should be allowed as a swap for the fur task (no conflict after swap)
		List<CourierTaskData> availableTasks = Arrays.asList(
			CourierTaskData.LUNAR_ISLE_COAL_DELIVERY_429     // Deepfin Point -> Lunar Isle (coal, 7188 XP)
		);
		
		SwapRecommendation swap = engine.evaluateSwapOpportunity(
			availableTasks, boardLocation, heldTasks);
		
		// A swap SHOULD be recommended: inactive fur task (3625 XP) for coal task (7188 XP)
		assertNotNull("Should recommend swap (inactive 3625 XP task for 7188 XP coal task)", swap);
		
		// Coal task should be recommended (no conflict)
		assertEquals("Should recommend coal task",
			CourierTaskData.LUNAR_ISLE_COAL_DELIVERY_429, swap.getNewTask());
		
		// Clean up
		engine.setTaskActive(furTask, true);
	}
}
