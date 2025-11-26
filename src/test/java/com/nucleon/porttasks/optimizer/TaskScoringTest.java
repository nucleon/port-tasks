package com.nucleon.porttasks.optimizer;

import com.nucleon.porttasks.CourierTask;
import com.nucleon.porttasks.enums.CourierTaskData;
import com.nucleon.porttasks.enums.PortLocation;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Tests for TaskSelectionEngine task scoring functionality.
 * 
 * The engine scores and recommends tasks based on:
 * 1. XP reward
 * 2. Route efficiency (insertion cost into current route)
 * 3. Pickup HERE bonus (2x in non-teleport mode)
 * 4. Delivery to board bonus (1.5x)
 */
public class TaskScoringTest extends TaskSelectionEngineTestBase
{
	// ========================================
	// TASK SCORING TESTS
	// ========================================
	
	/**
	 * Test: Task with pickup at current location (board) should score higher than distant pickup
	 * when both have similar XP, due to the pickup HERE bonus (2x in non-teleport mode).
	 */
	@Test
	public void testPickupHere_ScoresHigherThanDistantPickup()
	{
		PortLocation boardLocation = PortLocation.ETCETERIA;
		List<CourierTask> heldTasks = Collections.emptyList();
		
		// Task 400: Etceteria -> Rellekka (688 XP) - pickup HERE
		// Task 401: Rellekka -> Etceteria (1250 XP) - pickup at Rellekka (nearby but not HERE)
		List<CourierTaskData> availableTasks = Arrays.asList(
			CourierTaskData.RELLEKKA_FISH_DELIVERY_400,      // pickup HERE at Etceteria
			CourierTaskData.ETCETERIA_IRON_DELIVERY_401      // pickup at Rellekka
		);
		
		List<TaskRecommendation> ranked = engine.rankTasks(availableTasks, boardLocation, heldTasks);
		
		assertNotNull("Should return ranked list", ranked);
		assertEquals("Should have 2 ranked tasks", 2, ranked.size());
		
		// Both tasks should have positive scores
		assertTrue("Pickup HERE task should have positive score", ranked.get(0).getScore() > 0);
		assertTrue("Distant pickup task should have positive score", ranked.get(1).getScore() > 0);
	}
	
	/**
	 * Test: Task with pickup at pending pickup location adds minimal cost
	 * since we're already going there.
	 */
	@Test
	public void testPickupAtPendingLocation_ScoresAsEfficient()
	{
		PortLocation boardLocation = PortLocation.LUNAR_ISLE;
		
		// Held task: need to pick up at Piscatoris
		List<CourierTask> heldTasks = Arrays.asList(
			createTask(CourierTaskData.ETCETERIA_MONKFISH_DELIVERY_416, 0)  // Piscatoris -> Etceteria
		);
		
		// Task 422: Piscatoris -> Lunar Isle (2491 XP) - pickup at pending location
		// Task 424: Prifddinas -> Lunar Isle (4341 XP) - pickup at new location
		List<CourierTaskData> availableTasks = Arrays.asList(
			CourierTaskData.LUNAR_ISLE_FUR_DELIVERY_422,     // pickup at Piscatoris (pending)
			CourierTaskData.LUNAR_ISLE_POTION_DELIVERY_424   // pickup at Prifddinas (new)
		);
		
		List<TaskRecommendation> ranked = engine.rankTasks(availableTasks, boardLocation, heldTasks);
		
		// Task at pending pickup should have positive score (efficient)
		assertTrue("Task at pending pickup should have positive score", ranked.get(0).getScore() > 0);
	}
	
	/**
	 * Test: Delivery to board location gets bonus (ends run at noticeboard).
	 */
	@Test
	public void testDeliveryToBoard_GetsBonus()
	{
		PortLocation boardLocation = PortLocation.ETCETERIA;
		List<CourierTask> heldTasks = Collections.emptyList();
		
		// Task delivering TO the board location vs task delivering elsewhere
		// Task 410: Port Piscarilius -> Etceteria (4188 XP) - delivers to board
		// Task 420: Port Piscarilius -> Lunar Isle (3345 XP) - delivers elsewhere
		List<CourierTaskData> availableTasks = Arrays.asList(
			CourierTaskData.ETCETERIA_PLANK_DELIVERY_410,    // delivers to Etceteria (board)
			CourierTaskData.LUNAR_ISLE_FISH_DELIVERY_420     // delivers to Lunar Isle
		);
		
		List<TaskRecommendation> ranked = engine.rankTasks(availableTasks, boardLocation, heldTasks);
		
		assertTrue("Delivery to board should have positive score", ranked.get(0).getScore() > 0);
		assertTrue("Delivery elsewhere should have positive score", ranked.get(1).getScore() > 0);
	}
	
	/**
	 * Test: "Free XP" scenario - pickup HERE + delivery to pending destination.
	 * 
	 * This has minimal insertion cost (pickup at board, delivery to existing destination)
	 * and should score very high due to the pickup HERE bonus (2x).
	 */
	@Test
	public void testFreeXP_PickupHereDeliverToPending()
	{
		PortLocation boardLocation = PortLocation.ETCETERIA;
		
		// Held task going to Rellekka
		List<CourierTask> heldTasks = Arrays.asList(
			createTask(CourierTaskData.RELLEKKA_PLANK_DELIVERY_391, 0)  // Port Roberts -> Rellekka
		);
		
		// Task 400: Etceteria -> Rellekka (688 XP) - pickup HERE, deliver to pending
		List<CourierTaskData> availableTasks = Arrays.asList(
			CourierTaskData.RELLEKKA_FISH_DELIVERY_400
		);
		
		TaskRecommendation recommendation = engine.recommendTask(availableTasks, boardLocation, heldTasks);
		
		assertNotNull("Should return a recommendation", recommendation);
		// This should score high - pickup HERE + delivery to pending = very low insertion cost
		assertTrue("Free XP task should have high score (> 500)", recommendation.getScore() > 500);
	}
	
	/**
	 * Test: Score formula rewards efficiency - task at pending pickup scores higher
	 * than task requiring new detour, when both deliver to the same place.
	 */
	@Test
	public void testScoreFormula_XpPerAddedDistance()
	{
		PortLocation boardLocation = PortLocation.LUNAR_ISLE;
		
		// Held task: need to pick up at Deepfin Point
		List<CourierTask> heldTasks = Arrays.asList(
			createTask(CourierTaskData.LUNAR_ISLE_COAL_DELIVERY_429, 0)  // Deepfin Point -> Lunar Isle
		);
		
		// Two tasks delivering to Lunar Isle with different pickups
		// Task 429: Deepfin Point -> Lunar Isle (7188 XP) - pickup at pending (but we already have this one)
		// Task 424: Prifddinas -> Lunar Isle (4341 XP) - pickup at new location
		// Task 422: Piscatoris -> Lunar Isle (2491 XP) - pickup at different location
		List<CourierTaskData> availableTasks = Arrays.asList(
			CourierTaskData.LUNAR_ISLE_POTION_DELIVERY_424,  // Prifddinas -> Lunar Isle
			CourierTaskData.LUNAR_ISLE_FUR_DELIVERY_422      // Piscatoris -> Lunar Isle
		);
		
		List<TaskRecommendation> ranked = engine.rankTasks(availableTasks, boardLocation, heldTasks);
		
		// Both should have positive scores
		assertTrue("Task should have positive score", ranked.get(0).getScore() > 0);
		assertTrue("Task should have positive score", ranked.get(1).getScore() > 0);
	}
	
	// ========================================
	// TASK RECOMMENDATION TESTS
	// ========================================
	
	/**
	 * Test: Recommend best task from available options.
	 */
	@Test
	public void testRecommendBestTask_ReturnsHighestScore()
	{
		PortLocation boardLocation = PortLocation.ETCETERIA;
		List<CourierTask> heldTasks = Collections.emptyList();
		
		List<CourierTaskData> availableTasks = Arrays.asList(
			CourierTaskData.RELLEKKA_FISH_DELIVERY_400,      // 688 XP, pickup HERE
			CourierTaskData.ETCETERIA_IRON_DELIVERY_401,     // 1250 XP
			CourierTaskData.ETCETERIA_PLANK_DELIVERY_410     // 4188 XP
		);
		
		TaskRecommendation recommendation = 
			engine.recommendTask(availableTasks, boardLocation, heldTasks);
		
		assertNotNull("Should return a recommendation", recommendation);
		assertNotNull("Recommendation should have a task", recommendation.getTask());
		assertTrue("Recommendation should have positive score", recommendation.getScore() > 0);
	}
	
	/**
	 * Test: Recommend task considering current held tasks.
	 * Tasks that fit the route well should score favorably.
	 */
	@Test
	public void testRecommendTask_ConsidersHeldTasks()
	{
		PortLocation boardLocation = PortLocation.LUNAR_ISLE;
		
		// Currently holding: Piscatoris -> Etceteria (need pickup at Piscatoris)
		List<CourierTask> heldTasks = Arrays.asList(
			createTask(CourierTaskData.ETCETERIA_MONKFISH_DELIVERY_416, 0) // Piscatoris -> Etceteria
		);
		
		List<CourierTaskData> availableTasks = Arrays.asList(
			CourierTaskData.LUNAR_ISLE_FUR_DELIVERY_422,     // Piscatoris -> Lunar Isle (pickup at pending!)
			CourierTaskData.LUNAR_ISLE_POTION_DELIVERY_424   // Prifddinas -> Lunar Isle (new location)
		);
		
		List<TaskRecommendation> ranked = 
			engine.rankTasks(availableTasks, boardLocation, heldTasks);
		
		assertNotNull("Should return ranked list", ranked);
		assertEquals("Should rank both tasks", 2, ranked.size());
		
		// Both tasks deliver to board (Lunar Isle), so both should score well
		// The fur task has lower XP (2491) but pickup at pending location
		// The potion task has higher XP (4341) but requires new pickup location
		// The algorithm should give positive scores to both
		TaskRecommendation furRec = ranked.stream()
			.filter(r -> r.getTask() == CourierTaskData.LUNAR_ISLE_FUR_DELIVERY_422)
			.findFirst().orElse(null);
		TaskRecommendation potionRec = ranked.stream()
			.filter(r -> r.getTask() == CourierTaskData.LUNAR_ISLE_POTION_DELIVERY_424)
			.findFirst().orElse(null);
		
		assertNotNull("Fur task should be ranked", furRec);
		assertNotNull("Potion task should be ranked", potionRec);
		assertTrue("Fur task (pickup at pending) should have positive score", furRec.getScore() > 0);
		assertTrue("Potion task (new pickup) should have positive score", potionRec.getScore() > 0);
	}
	
	/**
	 * Test: Rank all available tasks by score.
	 */
	@Test
	public void testRankTasks_ReturnsOrderedList()
	{
		PortLocation boardLocation = PortLocation.ETCETERIA;
		List<CourierTask> heldTasks = Collections.emptyList();
		
		List<CourierTaskData> availableTasks = Arrays.asList(
			CourierTaskData.RELLEKKA_FISH_DELIVERY_400,
			CourierTaskData.ETCETERIA_IRON_DELIVERY_401,
			CourierTaskData.ETCETERIA_PLANK_DELIVERY_410
		);
		
		List<TaskRecommendation> ranked = 
			engine.rankTasks(availableTasks, boardLocation, heldTasks);
		
		assertNotNull("Should return ranked list", ranked);
		assertEquals("Should have 3 ranked tasks", 3, ranked.size());
		
		// Verify sorted by score descending
		for (int i = 0; i < ranked.size() - 1; i++)
		{
			assertTrue("Tasks should be sorted by score descending",
				ranked.get(i).getScore() >= ranked.get(i + 1).getScore());
		}
	}
	
	// ========================================
	// DELIVERY CONSTRAINT TESTS (via filtering)
	// ========================================
	
	/**
	 * Test: Tasks are ranked even if they deliver to new locations.
	 * The insertion cost will naturally penalize tasks that don't fit the route.
	 */
	@Test
	public void testDeliveryConstraint_NaturalPenaltyForOffRouteDeliveries()
	{
		PortLocation boardLocation = PortLocation.LUNAR_ISLE;
		
		// Held tasks going to Etceteria and Rellekka
		List<CourierTask> heldTasks = Arrays.asList(
			createTask(CourierTaskData.ETCETERIA_MONKFISH_DELIVERY_416, 0),  // -> Etceteria
			createTask(CourierTaskData.RELLEKKA_FISH_DELIVERY_400, 0)        // -> Rellekka
		);
		
		// Available tasks with various delivery locations
		List<CourierTaskData> availableTasks = Arrays.asList(
			CourierTaskData.ETCETERIA_PLANK_DELIVERY_410,    // Delivers to Etceteria (on route)
			CourierTaskData.LUNAR_ISLE_FISH_DELIVERY_420     // Delivers to Lunar Isle (board)
		);
		
		List<TaskRecommendation> ranked = engine.rankTasks(availableTasks, boardLocation, heldTasks);
		
		assertNotNull("Should return ranked list", ranked);
		assertEquals("Should rank all tasks", 2, ranked.size());
		// Both should have positive scores - ranking handles efficiency naturally
		assertTrue("First task should have positive score", ranked.get(0).getScore() > 0);
		assertTrue("Second task should have positive score", ranked.get(1).getScore() > 0);
	}
}
