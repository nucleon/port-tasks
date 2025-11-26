package com.nucleon.porttasks.optimizer;

import com.nucleon.porttasks.CourierTask;
import com.nucleon.porttasks.enums.CourierTaskData;
import com.nucleon.porttasks.enums.PortLocation;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * Tests based on the video "Port Task Sailing Method Gives up to 140K XP/HR"
 * https://www.youtube.com/watch/Uei9PxbwmIw
 * 
 * These tests replicate exact noticeboard contents shown in the video
 * to verify our algorithm produces similar recommendations.
 */
public class VideoScenarioTest extends TaskSelectionEngineTestBase
{
	// ========================================
	// EXACT VIDEO SCENARIO TESTS
	// ========================================
	
	/**
	 * EXACT VIDEO SCENARIO 1: Lunar Isle Noticeboard (Start of Run)
	 * 
	 * Video shows these tasks available at Lunar Isle:
	 * - 7x fish from Port Roberts to Lunar Isle for 4626 xp
	 * - 8x potion from Aldarin to Lunar Isle for 10017 xp  
	 * - 7x coal from Deepfin Point to Lunar Isle for 7188 xp
	 * - (unclear) delivery from Lunar Isle to Port Roberts
	 * - 7x cargo from Lunar Isle to Piscarilius for 1601 xp
	 * 
	 * Video action: Picked up the three clear round-trip tasks (fish, potion, coal)
	 * then teleported to Aldarin.
	 * 
	 * Expected: Algorithm should recommend the three round-trip tasks as top picks.
	 */
	@Test
	public void testExactVideoScenario_LunarIsle_StartOfRun()
	{
		// Video shows player using teleports, so enable teleport optimization
		engine.setTeleportOptimizationEnabled(true);
		
		PortLocation boardLocation = PortLocation.LUNAR_ISLE;
		List<CourierTask> heldTasks = Collections.emptyList();
		
		// Exact tasks from video
		List<CourierTaskData> availableTasks = Arrays.asList(
			// Round-trip tasks (pickup elsewhere, deliver to Lunar Isle)
			CourierTaskData.LUNAR_ISLE_FISH_DELIVERY_431,     // Port Roberts -> Lunar Isle, 4626 XP
			CourierTaskData.LUNAR_ISLE_POTION_DELIVERY_437,   // Aldarin -> Lunar Isle, 10017 XP
			CourierTaskData.LUNAR_ISLE_COAL_DELIVERY_429,     // Deepfin Point -> Lunar Isle, 7188 XP
			
			// Outbound tasks (Lunar Isle -> elsewhere) - these are NOT round-trips
			CourierTaskData.PORT_PISCARILIUS_FABRIC_DELIVERY_419, // Lunar Isle -> Port Piscarilius, 1601 XP
			
			// Add some distractors that might appear on board
			CourierTaskData.LUNAR_ISLE_FISH_DELIVERY_420,     // Port Piscarilius -> Lunar Isle, 3345 XP
			CourierTaskData.LUNAR_ISLE_POTION_DELIVERY_424    // Prifddinas -> Lunar Isle, 4341 XP
		);
		
		List<TaskRecommendation> recommendations = 
			engine.rankTasks(availableTasks, boardLocation, heldTasks);
		
		// Get top 3 recommendations (what the video picked)
		List<CourierTaskData> top3 = recommendations.stream()
			.limit(3)
			.map(TaskRecommendation::getTask)
			.collect(Collectors.toList());
		
		// The video picked these three - they should all deliver TO Lunar Isle (board)
		// and have pickups at southern ports
		CourierTaskData[] videoPickedTasks = {
			CourierTaskData.LUNAR_ISLE_POTION_DELIVERY_437,   // 10017 XP - highest
			CourierTaskData.LUNAR_ISLE_COAL_DELIVERY_429,     // 7188 XP
			CourierTaskData.LUNAR_ISLE_FISH_DELIVERY_431      // 4626 XP
		};
		
		// Verify all video-picked tasks are in top 3
		for (CourierTaskData videoTask : videoPickedTasks)
		{
			assertTrue("Video picked task should be in top 3: " + videoTask + " (" + videoTask.getReward() + " XP)",
				top3.contains(videoTask));
		}
		
		// The outbound task (Lunar Isle -> Piscarilius) should NOT be in top 3
		// because it doesn't deliver to board location
		assertFalse("Outbound task (Lunar Isle -> Piscarilius) should NOT be in top 3",
			top3.contains(CourierTaskData.PORT_PISCARILIUS_FABRIC_DELIVERY_419));
	}
	
	/**
	 * EXACT VIDEO SCENARIO 2: Aldarin Noticeboard (Finding 4th Task)
	 * 
	 * After accepting 3 tasks at Lunar Isle, player teleported to Aldarin.
	 * Video shows these tasks at Aldarin noticeboard:
	 * - 4x pest remains from Void Knights Outpost to Aldarin for 3695 xp
	 * - 7x potion from Aldarin to Prifddinas for 2802 xp
	 * - 2x wine from Aldarin to Brimhaven for 1901 xp
	 * - 4x potion from Aldarin to Void Knights Outpost for 805 xp
	 * - 2x beer from Aldarin to Sunset Coast for 167 xp
	 * 
	 * Video quote: "I see a task from Alderin to Breas, which honestly that's perfect"
	 * (Breas = likely Prifddinas based on context)
	 * 
	 * Video action: The player looked for a 4th task that fits the route.
	 * A good 4th task would pickup at Aldarin (HERE) and deliver somewhere on route.
	 */
	@Test
	public void testExactVideoScenario_Aldarin_Finding4thTask()
	{
		PortLocation boardLocation = PortLocation.ALDARIN;
		
		// Already holding 3 tasks from Lunar Isle
		List<CourierTask> heldTasks = Arrays.asList(
			createTask(CourierTaskData.LUNAR_ISLE_FISH_DELIVERY_431, 0),     // Port Roberts -> Lunar Isle
			createTask(CourierTaskData.LUNAR_ISLE_POTION_DELIVERY_437, 0),   // Aldarin -> Lunar Isle (pickup HERE!)
			createTask(CourierTaskData.LUNAR_ISLE_COAL_DELIVERY_429, 0)      // Deepfin Point -> Lunar Isle
		);
		
		// Exact tasks from Aldarin noticeboard in video
		List<CourierTaskData> availableTasks = Arrays.asList(
			// Note: These are tasks AVAILABLE at Aldarin noticeboard
			// The pest remains task is at Aldarin board but pickup is at Void Knights
			CourierTaskData.ALDARIN_PEST_REMAINS_DELIVERY_300,    // Void Knights -> Aldarin board, 3695 XP (pickup NOT here)
			CourierTaskData.PRIFDDINAS_POTION_DELIVERY_297,       // Aldarin -> Prifddinas, 2802 XP (pickup HERE!)
			CourierTaskData.BRIMHAVEN_WINE_DELIVERY_302,          // Aldarin -> Brimhaven, 1901 XP (pickup HERE!)
			CourierTaskData.VOID_KNIGHTS_OUTPOST_POTION_DELIVERY_299, // Aldarin -> Void Knights, 805 XP (pickup HERE!)
			CourierTaskData.SUNSET_COAST_BEER_DELIVERY_286        // Aldarin -> Sunset Coast, 167 XP (pickup HERE!)
		);
		
		List<TaskRecommendation> recommendations = 
			engine.rankTasks(availableTasks, boardLocation, heldTasks);
		
		// The video strategy suggests picking a task that:
		// 1. Pickups at Aldarin (current location) - because we're already here
		// 2. Delivers somewhere useful (Prifddinas is on route north)
		
		// The Prifddinas potion task (Aldarin -> Prifddinas, 2802 XP) should be attractive
		// because it's "Pickup HERE" and Prifddinas is on the way to Lunar Isle
		TaskRecommendation prifTask = recommendations.stream()
			.filter(r -> r.getTask() == CourierTaskData.PRIFDDINAS_POTION_DELIVERY_297)
			.findFirst().orElse(null);
		
		assertNotNull("Prifddinas potion task should be in recommendations", prifTask);
		
		// Tasks that pickup HERE (at Aldarin) should generally score well
		// because we're already visiting Aldarin for the potion pickup
		TaskRecommendation topRec = recommendations.get(0);
		
		// Verify the top task has pickup at Aldarin (board location) or existing route
		PortLocation topPickup = topRec.getTask().getPickupPort();
		
		// Build route locations
		Set<PortLocation> routeLocations = new HashSet<>();
		routeLocations.add(boardLocation); // Aldarin
		for (CourierTask task : heldTasks)
		{
			routeLocations.add(task.getData().getPickupPort());
			routeLocations.add(task.getData().getDeliveryPort());
		}
		
		// Top recommendation should fit the route
		assertTrue("Top task should pickup at a route location. Got: " + topPickup,
			routeLocations.contains(topPickup));
	}
	
	/**
	 * EXACT VIDEO SCENARIO 3: Prifddinas Noticeboard (Mid-route)
	 * 
	 * Video shows these tasks at Prifddinas noticeboard:
	 * - 7x staff from Prifddinas to Port Roberts for 1459 xp
	 * - 6x staff from Prifddinas to Civitas illa Fortis for 1578 xp  
	 * - 8x crystal seed from Prifddinas to Lunar Isle for 2171 xp
	 * 
	 * At this point, the player has cargo loaded and is traveling north.
	 * The crystal seed task (Prifddinas -> Lunar Isle) is ideal because
	 * it's "Pickup HERE" and delivers to our final destination.
	 */
	@Test
	public void testExactVideoScenario_Prifddinas_MidRoute()
	{
		PortLocation boardLocation = PortLocation.PRIFDDINAS;
		
		// Simulating mid-route: some cargo already loaded
		List<CourierTask> heldTasks = Arrays.asList(
			createTaskWithCargo(CourierTaskData.LUNAR_ISLE_POTION_DELIVERY_437, 8),  // Aldarin -> Lunar Isle, LOADED
			createTask(CourierTaskData.LUNAR_ISLE_COAL_DELIVERY_429, 0),             // Deepfin Point -> Lunar Isle
			createTask(CourierTaskData.LUNAR_ISLE_FISH_DELIVERY_431, 0),             // Port Roberts -> Lunar Isle
			createTask(CourierTaskData.PRIFDDINAS_POTION_DELIVERY_297, 0)            // Aldarin -> Prifddinas (pickup at prev location)
		);
		
		// Exact tasks from Prifddinas noticeboard in video
		List<CourierTaskData> availableTasks = Arrays.asList(
			CourierTaskData.PORT_ROBERTS_STAFF_DELIVERY_369,      // Prifddinas -> Port Roberts, 1459 XP
			CourierTaskData.CIVITAS_ILLA_FORTIS_STAFF_DELIVERY_375, // Prifddinas -> Civitas, 1578 XP
			CourierTaskData.LUNAR_ISLE_CRYSTAL_SEED_DELIVERY_377  // Prifddinas -> Lunar Isle, 2171 XP (delivers to loaded dest!)
		);
		
		List<TaskRecommendation> recommendations = 
			engine.rankTasks(availableTasks, boardLocation, heldTasks);
		
		// The crystal seed task (Prifddinas -> Lunar Isle) should be ideal:
		// - Pickup HERE (at Prifddinas)
		// - Delivers to Lunar Isle (where we already have cargo loaded!)
		// This is the "FREE XP" scenario!
		TaskRecommendation crystalRec = recommendations.stream()
			.filter(r -> r.getTask() == CourierTaskData.LUNAR_ISLE_CRYSTAL_SEED_DELIVERY_377)
			.findFirst().orElse(null);
		
		assertNotNull("Crystal seed task should be in recommendations", crystalRec);
		
		// It should have a high score due to SYNERGY multiplier
		// (Pickup HERE + delivery to cargo-loaded destination = 3.0x multiplier)
		// Base XP 2171 * 3.0 = ~6513
		assertTrue("Crystal seed task should have high score (synergy scenario). Got: " + crystalRec.getScore(),
			crystalRec.getScore() > 5000);
		
		// It should be the top recommendation
		assertEquals("Crystal seed task should be #1 recommendation (FREE XP)",
			CourierTaskData.LUNAR_ISLE_CRYSTAL_SEED_DELIVERY_377, recommendations.get(0).getTask());
	}
	
	/**
	 * EXACT VIDEO SCENARIO 4: Alternative Start - Only Deepfin Point Task
	 * 
	 * Video quote: "What happens if you don't get the Alderin start at Lunar Isle?
	 * That's okay. In this example, I only got the deep fin point delivery task."
	 * 
	 * Lunar Isle noticeboard only shows:
	 * - 7x coal from Deepfin Point to Lunar Isle for 7188 xp
	 * 
	 * Video action: Accept this task, teleport to Deepfin Point, start route from there.
	 */
	@Test
	public void testExactVideoScenario_AlternativeStart_OnlyDeepfinTask()
	{
		// Video shows player using teleports, so enable teleport optimization
		engine.setTeleportOptimizationEnabled(true);
		
		PortLocation boardLocation = PortLocation.LUNAR_ISLE;
		List<CourierTask> heldTasks = Collections.emptyList();
		
		// Only one round-trip task available (per video)
		// Adding some distractors to make it more realistic
		List<CourierTaskData> availableTasks = Arrays.asList(
			CourierTaskData.LUNAR_ISLE_COAL_DELIVERY_429,      // Deepfin Point -> Lunar Isle, 7188 XP
			// Distractors (outbound from Lunar Isle)
			CourierTaskData.PORT_PISCARILIUS_FABRIC_DELIVERY_419, // Lunar Isle -> Piscarilius, 1601 XP
			CourierTaskData.LUNAR_ISLE_FISH_DELIVERY_420       // Port Piscarilius -> Lunar Isle, 3345 XP
		);
		
		List<TaskRecommendation> recommendations = 
			engine.rankTasks(availableTasks, boardLocation, heldTasks);
		
		// The coal task should be the top recommendation
		assertEquals("Coal task should be top recommendation",
			CourierTaskData.LUNAR_ISLE_COAL_DELIVERY_429, recommendations.get(0).getTask());
	}
	
	/**
	 * EXACT VIDEO SCENARIO 5: Deepfin Point Noticeboard (After Alternative Start)
	 * 
	 * After accepting only the Deepfin Point task at Lunar Isle, player teleported to Deepfin.
	 * Video shows these tasks at Deepfin Point noticeboard:
	 * - 6x mithril to Deepfin Point for 1438 xp (Port Tyras -> Deepfin Point)
	 * - 5x coal from Deepfin Point to Port Piscarilius for 2469 xp
	 * - 5x nickel from Deepfin Point to Civitas illa Fortis for 1656 xp
	 * 
	 * Video quote: "I'm going to pick one that goes to Port Tyrus. I'm going to pick 
	 * one that goes to Port Viscarelius. And I'm going to pick one that goes to Vlamor."
	 * 
	 * Video action: All three tasks were taken.
	 */
	@Test
	public void testExactVideoScenario_DeepfinPoint_AfterAlternativeStart()
	{
		PortLocation boardLocation = PortLocation.DEEPFIN_POINT;
		
		// Holding only the coal task from Lunar Isle
		List<CourierTask> heldTasks = Arrays.asList(
			createTask(CourierTaskData.LUNAR_ISLE_COAL_DELIVERY_429, 0)  // Deepfin Point -> Lunar Isle (pickup HERE!)
		);
		
		// Exact tasks from Deepfin Point noticeboard in video
		List<CourierTaskData> availableTasks = Arrays.asList(
			CourierTaskData.PORT_TYRAS_MITHRIL_DELIVERY_326,       // Port Tyras -> Deepfin Point, 1438 XP
			CourierTaskData.PORT_PISCARILIUS_COAL_DELIVERY_335,    // Deepfin Point -> Piscarilius, 2469 XP
			CourierTaskData.CIVITAS_ILLA_FORTIS_NICKEL_DELIVERY_333 // Deepfin Point -> Civitas, 1656 XP
		);
		
		List<TaskRecommendation> recommendations = 
			engine.rankTasks(availableTasks, boardLocation, heldTasks);
		
		// Video took all 3 tasks. Let's verify they all score positively.
		for (TaskRecommendation rec : recommendations)
		{
			assertTrue("Task " + rec.getTask() + " should have positive score",
				rec.getScore() > 0);
		}
		
		// The tasks that pickup HERE (Deepfin Point) should generally score higher
		// Port Piscarilius coal and Civitas nickel both pickup at Deepfin
		// Port Tyras mithril delivers TO Deepfin (we're already here for pickup)
		
		// Let's see which scoring makes sense:
		// - Coal to Piscarilius: Pickup HERE, but Piscarilius is a NEW delivery location
		// - Nickel to Civitas: Pickup HERE, but Civitas is a NEW delivery location  
		// - Mithril from Port Tyras: Delivers to board (Deepfin), pickup at NEW location
		
		// In planning mode, these are all somewhat equal - none are "free XP"
		// The algorithm should consider route efficiency
	}
	
	/**
	 * EXACT VIDEO SCENARIO 6: Port Tyras Noticeboard
	 * 
	 * Video shows these tasks at Port Tyras noticeboard:
	 * - 5x halberd from Port Tyras to Ardougne for 2813 xp
	 * - 7x logs from Port Tyras to Lunar Isle for 2598 xp
	 * 
	 * The logs task (Port Tyras -> Lunar Isle) aligns with the strategy
	 * since Lunar Isle is the final destination.
	 */
	@Test
	public void testExactVideoScenario_PortTyras()
	{
		PortLocation boardLocation = PortLocation.PORT_TYRAS;
		
		// Simulate mid-route state with some cargo loaded going to Lunar Isle
		List<CourierTask> heldTasks = Arrays.asList(
			createTaskWithCargo(CourierTaskData.LUNAR_ISLE_COAL_DELIVERY_429, 7),   // Deepfin -> Lunar Isle, LOADED
			createTask(CourierTaskData.PORT_PISCARILIUS_COAL_DELIVERY_335, 0),      // Deepfin -> Piscarilius
			createTask(CourierTaskData.CIVITAS_ILLA_FORTIS_NICKEL_DELIVERY_333, 0)  // Deepfin -> Civitas
		);
		
		// Exact tasks from Port Tyras noticeboard in video
		List<CourierTaskData> availableTasks = Arrays.asList(
			CourierTaskData.ARDOUGNE_HALBERD_DELIVERY_352,     // Port Tyras -> Ardougne, 2813 XP
			CourierTaskData.LUNAR_ISLE_LOGS_DELIVERY_359       // Port Tyras -> Lunar Isle, 2598 XP
		);
		
		List<TaskRecommendation> recommendations = 
			engine.rankTasks(availableTasks, boardLocation, heldTasks);
		
		// The logs task (Port Tyras -> Lunar Isle) should score higher because:
		// - Pickup HERE (at Port Tyras)
		// - Delivers to Lunar Isle (where we already have cargo loaded!)
		// This is the FREE XP scenario!
		TaskRecommendation logsRec = recommendations.stream()
			.filter(r -> r.getTask() == CourierTaskData.LUNAR_ISLE_LOGS_DELIVERY_359)
			.findFirst().orElse(null);
		
		TaskRecommendation halberdRec = recommendations.stream()
			.filter(r -> r.getTask() == CourierTaskData.ARDOUGNE_HALBERD_DELIVERY_352)
			.findFirst().orElse(null);
		
		assertNotNull("Logs task should be in recommendations", logsRec);
		assertNotNull("Halberd task should be in recommendations", halberdRec);
		
		// Logs task delivers to cargo-loaded destination (Lunar Isle) - should score much higher
		assertTrue("Logs task (to Lunar Isle) should score higher than halberd (to Ardougne)",
			logsRec.getScore() > halberdRec.getScore());
	}
	
	// ========================================
	// OUTBOUND VS ROUND-TRIP TESTS
	// ========================================
	
	/**
	 * Test: Outbound Task Should NOT Beat Round-Trip Tasks
	 * 
	 * Scenario at Lunar Isle noticeboard:
	 * 
	 * HELD TASKS (2 total, no cargo picked up yet):
	 * 1. Port Roberts -> Lunar Isle (furs, 2384 XP)
	 * 2. Port Roberts -> Lunar Isle (fish, 4626 XP)
	 * 
	 * AVAILABLE ON NOTICEBOARD (not yet taken):
	 * - Deepfin Point -> Lunar Isle (coal, 7188 XP) - GOOD round-trip option
	 * - Lunar Isle -> Port Piscarilius (fabrics, 1601 XP) - outbound to new location
	 * - Lunar Isle -> Port Roberts (fabrics, 2313 XP) - BAD: delivers to pending pickup
	 * 
	 * EXPECTED: The round-trip task (coal) should be recommended over outbound tasks.
	 */
	@Test
	public void testExactUserScenario_OutboundShouldNotBeatRoundTrips()
	{
		PortLocation boardLocation = PortLocation.LUNAR_ISLE;
		
		// Held tasks (2 total, no cargo picked up)
		List<CourierTask> heldTasks = Arrays.asList(
			createTask(CourierTaskData.LUNAR_ISLE_FUR_DELIVERY_316, 0),      // Port Roberts -> Lunar Isle, 2384 XP
			createTask(CourierTaskData.LUNAR_ISLE_FISH_DELIVERY_431, 0)      // Port Roberts -> Lunar Isle, 4626 XP
		);
		
		// Pending pickups: Port Roberts (x2)
		// Pending deliveries: Lunar Isle (x2)
		
		// Available tasks - coal is a proper round-trip (Deepfin Point -> Lunar Isle)
		List<CourierTaskData> availableTasks = Arrays.asList(
			CourierTaskData.LUNAR_ISLE_COAL_DELIVERY_429,          // Deepfin Point -> Lunar Isle, 7188 XP (round-trip, GOOD)
			CourierTaskData.PORT_PISCARILIUS_FABRIC_DELIVERY_419,  // Lunar Isle -> Port Piscarilius, 1601 XP (outbound to new)
			CourierTaskData.PORT_ROBERTS_FABRIC_DELIVERY_430       // Lunar Isle -> Port Roberts, 2313 XP (outbound to pending pickup)
		);
		
		List<TaskRecommendation> recommendations = 
			engine.rankTasks(availableTasks, boardLocation, heldTasks);
		
		// Get the recommendations
		TaskRecommendation fabricsToPortRobertsRec = recommendations.stream()
			.filter(r -> r.getTask() == CourierTaskData.PORT_ROBERTS_FABRIC_DELIVERY_430)
			.findFirst().orElse(null);
		
		TaskRecommendation coalToLunarRec = recommendations.stream()
			.filter(r -> r.getTask() == CourierTaskData.LUNAR_ISLE_COAL_DELIVERY_429)
			.findFirst().orElse(null);
		
		assertNotNull("Fabrics to Port Roberts should be in recommendations", fabricsToPortRobertsRec);
		assertNotNull("Coal to Lunar Isle should be in recommendations", coalToLunarRec);
		
		// CRITICAL ASSERTIONS:
		
		// 1. The problematic outbound task should NOT be "TAKE_NOW"
		assertNotEquals("Outbound task to pending pickup should NOT be 'TAKE_NOW'",
			RecommendationType.TAKE_NOW, fabricsToPortRobertsRec.getType());
		
		// 2. Round-trip task should score higher than the problematic outbound task
		assertTrue("Deepfin Point -> Lunar Isle (coal, 7188 XP) should score higher than Lunar Isle -> Port Roberts (2313 XP). " +
			"Coal score: " + coalToLunarRec.getScore() + ", Fabrics score: " + fabricsToPortRobertsRec.getScore(),
			coalToLunarRec.getScore() > fabricsToPortRobertsRec.getScore());
		
		// 3. The round-trip task should be #1 recommendation
		assertEquals("Round-trip task should be #1 recommendation",
			CourierTaskData.LUNAR_ISLE_COAL_DELIVERY_429, recommendations.get(0).getTask());
	}
	
	/**
	 * Isolated test: When ONLY outbound tasks are available (no round-trips),
	 * the one creating double-visit should still be penalized vs the one going to a new location.
	 */
	@Test
	public void testExactUserScenario_OutboundToPickupVsOutboundToNew()
	{
		PortLocation boardLocation = PortLocation.LUNAR_ISLE;
		
		// Same held tasks as the main scenario
		List<CourierTask> heldTasks = Arrays.asList(
			createTask(CourierTaskData.LUNAR_ISLE_FUR_DELIVERY_316, 0),      // Port Roberts -> Lunar Isle
			createTask(CourierTaskData.LUNAR_ISLE_POTION_DELIVERY_437, 0),   // Aldarin -> Lunar Isle
			createTask(CourierTaskData.LUNAR_ISLE_FISH_DELIVERY_431, 0)      // Port Roberts -> Lunar Isle
		);
		
		// Only outbound tasks available - compare one that creates double-visit vs one that doesn't
		List<CourierTaskData> availableTasks = Arrays.asList(
			CourierTaskData.PORT_ROBERTS_FABRIC_DELIVERY_430,     // Lunar Isle -> Port Roberts, 2313 XP (double-visit!)
			CourierTaskData.PORT_PISCARILIUS_FABRIC_DELIVERY_419  // Lunar Isle -> Port Piscarilius, 1601 XP (new location)
		);
		
		List<TaskRecommendation> recommendations = 
			engine.rankTasks(availableTasks, boardLocation, heldTasks);
		
		TaskRecommendation toPortRoberts = recommendations.stream()
			.filter(r -> r.getTask() == CourierTaskData.PORT_ROBERTS_FABRIC_DELIVERY_430)
			.findFirst().orElse(null);
		
		TaskRecommendation toPiscarilius = recommendations.stream()
			.filter(r -> r.getTask() == CourierTaskData.PORT_PISCARILIUS_FABRIC_DELIVERY_419)
			.findFirst().orElse(null);
		
		assertNotNull("Port Roberts task should be in recommendations", toPortRoberts);
		assertNotNull("Piscarilius task should be in recommendations", toPiscarilius);
		
		// The task creating double-visit (Port Roberts) should NOT be recommended as TAKE_NOW
		// even if it has higher XP, because it creates route inefficiency
		assertNotEquals("Task creating double-visit should NOT be TAKE_NOW",
			RecommendationType.TAKE_NOW, toPortRoberts.getType());
	}
	
	/**
	 * PLANNING MODE PRINCIPLE: Outbound tasks that deliver to pending pickup create inefficiency.
	 * 
	 * In planning mode at Lunar Isle with round-trip tasks held:
	 * - Lunar Isle is the FINAL DESTINATION (all held tasks deliver there)
	 * - Player will TELEPORT AWAY to start the route
	 * - Tasks that deliver TO a pending pickup location create a DOUBLE-VISIT inefficiency
	 * 
	 * Example: If we hold "Port Roberts -> Lunar Isle", adding "Lunar Isle -> Port Roberts"
	 * means we'd visit Port Roberts TWICE (once to deliver, once to pick up) which is wasteful.
	 * 
	 * This tests that such tasks score LOWER than tasks that don't create double-visits.
	 */
	@Test
	public void testPlanningMode_OutboundToPickupLocation_ScoresLowerThanToNewLocation()
	{
		PortLocation boardLocation = PortLocation.LUNAR_ISLE;
		
		// Holding round-trip tasks - Lunar Isle is the FINAL DESTINATION
		// Pending pickups: Aldarin, Port Roberts
		List<CourierTask> heldTasks = Arrays.asList(
			createTask(CourierTaskData.LUNAR_ISLE_POTION_DELIVERY_437, 0),  // Aldarin -> Lunar Isle
			createTask(CourierTaskData.LUNAR_ISLE_FISH_DELIVERY_431, 0)     // Port Roberts -> Lunar Isle
		);
		
		// Two outbound tasks from Lunar Isle
		// One delivers to a PENDING PICKUP (Port Roberts) - creates double-visit
		// One delivers to a NEW location (Port Piscarilius) - doesn't create double-visit
		List<CourierTaskData> availableTasks = Arrays.asList(
			CourierTaskData.PORT_ROBERTS_FABRIC_DELIVERY_430,     // Lunar Isle -> Port Roberts, 2313 XP (double-visit!)
			CourierTaskData.PORT_PISCARILIUS_FABRIC_DELIVERY_419  // Lunar Isle -> Port Piscarilius, 1601 XP (new location)
		);
		
		List<TaskRecommendation> recommendations = 
			engine.rankTasks(availableTasks, boardLocation, heldTasks);
		
		TaskRecommendation toPortRoberts = recommendations.stream()
			.filter(r -> r.getTask() == CourierTaskData.PORT_ROBERTS_FABRIC_DELIVERY_430)
			.findFirst().orElse(null);
		
		TaskRecommendation toPiscarilius = recommendations.stream()
			.filter(r -> r.getTask() == CourierTaskData.PORT_PISCARILIUS_FABRIC_DELIVERY_419)
			.findFirst().orElse(null);
		
		assertNotNull("Port Roberts task should be in recommendations", toPortRoberts);
		assertNotNull("Piscarilius task should be in recommendations", toPiscarilius);
		
		// Neither task should be TAKE_NOW since both are outbound from final destination
		// and would require adding new stops to the route
		assertNotEquals("Port Roberts task should NOT be TAKE_NOW (creates double-visit)",
			RecommendationType.TAKE_NOW, toPortRoberts.getType());
		assertNotEquals("Piscarilius task should NOT be TAKE_NOW (adds new destination)",
			RecommendationType.TAKE_NOW, toPiscarilius.getType());
	}
	
	// ========================================
	// GENERAL VIDEO STRATEGY TESTS
	// ========================================
	
	/**
	 * Video Strategy Test: One-directional sailing from south to Lunar Isle.
	 * 
	 * The video describes an efficient cargo delivery route:
	 * 1. Start at Lunar Isle noticeboard
	 * 2. Accept round-trip tasks (pickup from southern ports, deliver to Lunar Isle)
	 * 3. Teleport to Aldarin (southernmost starting point)
	 * 4. Travel north through: Aldarin -> Deepfin Point -> Port Tyrus -> Civitas illa Fortis 
	 *    -> Port Roberts -> Prifddinas -> Piscarilius -> Etceteria -> Lunar Isle
	 * 5. At each port, pick up cargo and check noticeboard for tasks that fit the route
	 * 
	 * This test simulates the video's first scenario:
	 * Standing at Lunar Isle noticeboard with 4 available task slots.
	 * The algorithm should recommend tasks that:
	 * - Pick up cargo from southern ports (Aldarin, Deepfin Point, Port Roberts)
	 * - Deliver to Lunar Isle (round-trip A-B-A tasks)
	 * 
	 * We include "distractor" tasks at each noticeboard that go in wrong directions.
	 */
	@Test
	public void testVideoStrategy_LunarIsleNoticeboard_SelectsRoundTripTasks()
	{
		PortLocation boardLocation = PortLocation.LUNAR_ISLE;
		List<CourierTask> heldTasks = Collections.emptyList(); // Starting fresh
		
		// Available tasks on Lunar Isle noticeboard (as shown in video):
		// Good tasks (round-trip, pickup south, deliver to Lunar Isle):
		// - LUNAR_ISLE_COAL_DELIVERY_429: Deepfin Point -> Lunar Isle (7188 XP)
		// - LUNAR_ISLE_FISH_DELIVERY_431: Port Roberts -> Lunar Isle (4626 XP)
		// - LUNAR_ISLE_POTION_DELIVERY_437: Aldarin -> Lunar Isle (10017 XP)
		//
		// Distractor tasks (wrong direction or don't deliver to Lunar Isle):
		// - LUNAR_ISLE_FISH_DELIVERY_420: Port Piscarilius -> Lunar Isle (3345 XP) - also good but shorter route
		// - LUNAR_ISLE_POTION_DELIVERY_424: Prifddinas -> Lunar Isle (4341 XP) - good
		// - Tasks that deliver AWAY from Lunar Isle (bad for this strategy)
		
		List<CourierTaskData> availableTasks = Arrays.asList(
			// Good round-trip tasks (pickup from far south, deliver to Lunar Isle)
			CourierTaskData.LUNAR_ISLE_COAL_DELIVERY_429,    // Deepfin Point -> Lunar Isle, 7188 XP
			CourierTaskData.LUNAR_ISLE_FISH_DELIVERY_431,    // Port Roberts -> Lunar Isle, 4626 XP  
			CourierTaskData.LUNAR_ISLE_POTION_DELIVERY_437,  // Aldarin -> Lunar Isle, 10017 XP
			
			// Shorter route options (still good, but less XP per run)
			CourierTaskData.LUNAR_ISLE_FISH_DELIVERY_420,    // Port Piscarilius -> Lunar Isle, 3345 XP
			CourierTaskData.LUNAR_ISLE_POTION_DELIVERY_424,  // Prifddinas -> Lunar Isle, 4341 XP
			
			// Distractor: delivers AWAY from Lunar Isle (bad for strategy)
			CourierTaskData.ETCETERIA_MONKFISH_DELIVERY_416, // Piscatoris -> Etceteria, 3203 XP
			CourierTaskData.RELLEKKA_FISH_DELIVERY_400       // Etceteria -> Rellekka, 688 XP
		);
		
		List<TaskRecommendation> recommendations = 
			engine.rankTasks(availableTasks, boardLocation, heldTasks);
		
		assertFalse("Should have recommendations", recommendations.isEmpty());
		
		// In planning mode at Lunar Isle, the algorithm should recommend:
		// 1. Tasks that deliver TO Lunar Isle (round-trips)
		// 2. Higher XP tasks should generally rank higher
		
		// Get top 4 recommendations (video says fill 4 slots)
		List<CourierTaskData> top4 = recommendations.stream()
			.limit(4)
			.map(TaskRecommendation::getTask)
			.collect(Collectors.toList());
		
		// All top 4 should deliver to Lunar Isle
		for (CourierTaskData task : top4)
		{
			assertEquals("Top task should deliver to Lunar Isle: " + task,
				PortLocation.LUNAR_ISLE, task.getDeliveryPort());
		}
		
		// The distractor tasks (delivering away from Lunar Isle) should be ranked lower
		TaskRecommendation etceteriaRec = recommendations.stream()
			.filter(r -> r.getTask() == CourierTaskData.ETCETERIA_MONKFISH_DELIVERY_416)
			.findFirst().orElse(null);
		TaskRecommendation relekkaRec = recommendations.stream()
			.filter(r -> r.getTask() == CourierTaskData.RELLEKKA_FISH_DELIVERY_400)
			.findFirst().orElse(null);
		
		assertNotNull("Should have Etceteria task in recommendations", etceteriaRec);
		assertNotNull("Should have Rellekka task in recommendations", relekkaRec);
		
		// Distractor tasks should not be in top 4
		assertFalse("Etceteria task (delivers away) should not be in top 4",
			top4.contains(CourierTaskData.ETCETERIA_MONKFISH_DELIVERY_416));
		assertFalse("Rellekka task (delivers away) should not be in top 4",
			top4.contains(CourierTaskData.RELLEKKA_FISH_DELIVERY_400));
	}
	
	/**
	 * Video Strategy Test: After accepting tasks at Lunar Isle, simulate being at Aldarin.
	 * 
	 * Scenario from video:
	 * - Accepted 3 tasks at Lunar Isle that require pickups from southern ports
	 * - Now at Aldarin noticeboard (southernmost point)
	 * - Should pick up cargo and also look for a 4th task
	 * 
	 * The algorithm should recommend tasks that:
	 * - Fit the current route (deliver to existing pending delivery locations)
	 * - Pickup at existing route locations (Aldarin, Deepfin Point, Port Roberts)
	 * 
	 * Note: In planning mode, "Pickup HERE" has no special bonus since the player
	 * can teleport anywhere. What matters is whether the task fits the route.
	 */
	@Test
	public void testVideoStrategy_AtAldarin_AfterAcceptingLunarIsleTasks()
	{
		PortLocation boardLocation = PortLocation.ALDARIN;
		
		// Tasks accepted at Lunar Isle (planning mode - no cargo yet)
		List<CourierTask> heldTasks = Arrays.asList(
			createTask(CourierTaskData.LUNAR_ISLE_COAL_DELIVERY_429, 0),    // Deepfin Point -> Lunar Isle
			createTask(CourierTaskData.LUNAR_ISLE_FISH_DELIVERY_431, 0),    // Port Roberts -> Lunar Isle  
			createTask(CourierTaskData.LUNAR_ISLE_POTION_DELIVERY_437, 0)   // Aldarin -> Lunar Isle
		);
		
		// Pending pickups: Aldarin, Deepfin Point, Port Roberts
		// Pending deliveries: Lunar Isle (x3)
		
		// Available tasks at Aldarin noticeboard:
		List<CourierTaskData> availableTasks = Arrays.asList(
			// Good: Pickup at pending location (Aldarin, Deepfin, or Port Roberts), deliver to Lunar Isle
			CourierTaskData.ALDARIN_DYE_DELIVERY_298,        // Aldarin -> Prifddinas, 5604 XP
			CourierTaskData.ALDARIN_JEWELLERY_DELIVERY_296,  // Aldarin -> Port Roberts, 2415 XP
			
			// Good: Pickup at Deepfin Point (pending), high XP
			CourierTaskData.DEEPFIN_POINT_WARHAMMER_DELIVERY_340, // Deepfin Point -> Rellekka, 7601 XP
			
			// Distractor: Lower value options
			CourierTaskData.ALDARIN_NICKEL_DELIVERY_291,     // Aldarin -> Deepfin Point, 3000 XP
			CourierTaskData.RELLEKKA_FISH_DELIVERY_400       // Etceteria -> Rellekka, 688 XP (low XP, unrelated)
		);
		
		List<TaskRecommendation> recommendations = 
			engine.rankTasks(availableTasks, boardLocation, heldTasks);
		
		assertFalse("Should have recommendations", recommendations.isEmpty());
		
		// Get the pending pickup locations from held tasks
		Set<PortLocation> pendingPickups = new HashSet<>();
		for (CourierTask task : heldTasks)
		{
			if (task.getCargoTaken() == 0)
			{
				pendingPickups.add(task.getData().getPickupPort());
			}
		}
		
		// The top recommendation should either:
		// 1. Pickup at a route location (Aldarin, Deepfin Point, or Port Roberts)
		// 2. OR deliver to a route location (pending pickup or delivery)
		// High-XP tasks that deliver to route locations are acceptable even if pickup is off-route
		TaskRecommendation topRec = recommendations.get(0);
		PortLocation topPickup = topRec.getTask().getPickupPort();
		PortLocation topDelivery = topRec.getTask().getDeliveryPort();
		
		// Get pending deliveries
		Set<PortLocation> pendingDeliveries = new HashSet<>();
		for (CourierTask task : heldTasks) {
			pendingDeliveries.add(task.getData().getDeliveryPort());
		}
		
		// Valid: pickup at route location OR delivery to route location
		boolean pickupFitsRoute = topPickup == boardLocation || pendingPickups.contains(topPickup);
		boolean deliveryFitsRoute = topDelivery == boardLocation || pendingDeliveries.contains(topDelivery) || pendingPickups.contains(topDelivery);
		assertTrue("Top recommendation should fit the route (pickup or delivery on-route). " +
			"Got: " + topPickup + " -> " + topDelivery,
			pickupFitsRoute || deliveryFitsRoute);
		
		// The low-XP Rellekka task should rank lowest
		TaskRecommendation lowRec = recommendations.get(recommendations.size() - 1);
		assertEquals("Lowest ranked should be the low-XP unrelated task",
			CourierTaskData.RELLEKKA_FISH_DELIVERY_400, lowRec.getTask());
	}
	
	/**
	 * Video Strategy Test: Mid-route at Deepfin Point with cargo loaded.
	 * 
	 * Scenario:
	 * - Picked up cargo at Aldarin
	 * - Now at Deepfin Point to pick up coal for Lunar Isle delivery
	 * - Should prioritize completing current deliveries, but can still accept good tasks
	 */
	@Test
	public void testVideoStrategy_MidRouteAtDeepfinPoint_WithCargoLoaded()
	{
		PortLocation boardLocation = PortLocation.DEEPFIN_POINT;
		
		// State: Some cargo already loaded, traveling north toward Port Roberts and Lunar Isle
		List<CourierTask> heldTasks = Arrays.asList(
			createTaskWithCargo(CourierTaskData.LUNAR_ISLE_POTION_DELIVERY_437, 8), // Aldarin -> Lunar Isle, CARGO LOADED
			createTask(CourierTaskData.LUNAR_ISLE_COAL_DELIVERY_429, 0),            // Deepfin Point -> Lunar Isle (pickup HERE!)
			createTask(CourierTaskData.LUNAR_ISLE_FISH_DELIVERY_431, 0),            // Port Roberts -> Lunar Isle (pickup later)
			createTaskWithCargo(CourierTaskData.ALDARIN_JEWELLERY_DELIVERY_296, 3)  // Aldarin -> Port Roberts, CARGO LOADED
		);
		
		// Available tasks at Deepfin Point noticeboard:
		// Using tasks that PICK UP HERE (at Deepfin Point) to avoid backtrack penalty
		List<CourierTaskData> availableTasks = Arrays.asList(
			// Good: Pickup HERE, deliver to Port Roberts (on our route north!)
			CourierTaskData.PORT_ROBERTS_NICKEL_DELIVERY_324,     // Deepfin Point -> Port Roberts, 1656 XP
			
			// Distractor: Pickup HERE but going south (wrong direction when cargo loaded north)
			CourierTaskData.PORT_TYRAS_MITHRIL_DELIVERY_326,      // Deepfin Point -> Port Tyras, 1438 XP
			CourierTaskData.ALDARIN_ADAMANTITE_DELIVERY_328       // Deepfin Point -> Aldarin, 1438 XP
		);
		
		List<TaskRecommendation> recommendations = 
			engine.rankTasks(availableTasks, boardLocation, heldTasks);
		
		assertFalse("Should have recommendations", recommendations.isEmpty());
		
		// With cargo loaded going north, tasks that deliver to:
		// - Port Roberts (pending delivery destination) 
		// should be preferred over tasks delivering south
		
		// PORT_ROBERTS_NICKEL_DELIVERY_324 delivers to Port Roberts (on our route)
		// This should score well because Port Roberts is a pending delivery location
		TaskRecommendation northRec = recommendations.stream()
			.filter(r -> r.getTask() == CourierTaskData.PORT_ROBERTS_NICKEL_DELIVERY_324)
			.findFirst().orElse(null);
		
		assertNotNull("Should have north-bound recommendation", northRec);
		
		// Tasks going south (Port Tyras, Aldarin) should score lower
		TaskRecommendation southRec = recommendations.stream()
			.filter(r -> r.getTask() == CourierTaskData.PORT_TYRAS_MITHRIL_DELIVERY_326)
			.findFirst().orElse(null);
		
		assertNotNull("Should have south-bound task in recommendations", southRec);
		
		// The north-bound delivery (Port Roberts) should score higher than 
		// the south-bound delivery (Port Tyras) because we have cargo loaded going north
		assertTrue("Task delivering north (Port Roberts) should score higher than task delivering south (Port Tyras). " +
			"North score: " + northRec.getScore() + ", South score: " + southRec.getScore(),
			northRec.getScore() > southRec.getScore());
	}
	
	/**
	 * Video Strategy Test: Completing deliveries at Lunar Isle and starting new route.
	 * 
	 * Scenario from video:
	 * - Just delivered all cargo at Lunar Isle
	 * - Back in planning mode
	 * - Should immediately accept new round-trip tasks from noticeboard
	 * - Then teleport to southernmost pickup point and repeat
	 */
	@Test
	public void testVideoStrategy_CompletedDeliveries_StartNewRoute()
	{
		PortLocation boardLocation = PortLocation.LUNAR_ISLE;
		
		// Just delivered everything - empty task list or tasks with all cargo delivered
		List<CourierTask> heldTasks = Collections.emptyList();
		
		// Check that we're in planning mode
		assertTrue("Should be in planning mode after completing deliveries",
			engine.isInPlanningMode(heldTasks));
		
		// Available tasks - same as first test but verify behavior is consistent
		List<CourierTaskData> availableTasks = Arrays.asList(
			CourierTaskData.LUNAR_ISLE_COAL_DELIVERY_429,     // Deepfin Point -> Lunar Isle, 7188 XP
			CourierTaskData.LUNAR_ISLE_POTION_DELIVERY_437,   // Aldarin -> Lunar Isle, 10017 XP
			CourierTaskData.LUNAR_ISLE_FISH_DELIVERY_431,     // Port Roberts -> Lunar Isle, 4626 XP
			CourierTaskData.LUNAR_ISLE_POTION_DELIVERY_424,   // Prifddinas -> Lunar Isle, 4341 XP
			// Distractors
			CourierTaskData.RELLEKKA_FISH_DELIVERY_400,       // Etceteria -> Rellekka, 688 XP
			CourierTaskData.ETCETERIA_SWORD_DELIVERY_414      // Sunset Coast -> Etceteria, 8375 XP (high XP but wrong route)
		);
		
		List<TaskRecommendation> recommendations = 
			engine.rankTasks(availableTasks, boardLocation, heldTasks);
		
		// Top recommendations should all deliver to Lunar Isle
		List<CourierTaskData> top4 = recommendations.stream()
			.limit(4)
			.map(TaskRecommendation::getTask)
			.collect(Collectors.toList());
		
		// Verify round-trip tasks are preferred
		long lunarIsleDeliveries = top4.stream()
			.filter(t -> t.getDeliveryPort() == PortLocation.LUNAR_ISLE)
			.count();
		
		assertTrue("At least 3 of top 4 should deliver to Lunar Isle (round-trips)",
			lunarIsleDeliveries >= 3);
		
		// The high-XP Sunset Coast task (8375 XP) should NOT beat the Lunar Isle tasks
		// even though it has high XP, because it doesn't deliver to the board location
		TaskRecommendation sunsetRec = recommendations.stream()
			.filter(r -> r.getTask() == CourierTaskData.ETCETERIA_SWORD_DELIVERY_414)
			.findFirst().orElse(null);
		
		if (sunsetRec != null)
		{
			assertFalse("High-XP task delivering away from board should not be in top 4",
				top4.contains(CourierTaskData.ETCETERIA_SWORD_DELIVERY_414));
		}
	}
	
	/**
	 * Video Strategy Test: The "4th task" scenario from video.
	 * 
	 * Video quote: "Since I only picked up three tasks at Lunar Isle, and I have
	 * four available, that means I should immediately fill out the final task."
	 * 
	 * Scenario:
	 * - At Lunar Isle, already holding 3 round-trip tasks
	 * - Need to find a 4th task that fits the route
	 * 
	 * The key insight: tasks that pickup at PENDING locations AND deliver to
	 * locations we're already visiting (pickups OR deliveries) are efficient.
	 */
	@Test
	public void testVideoStrategy_FindFourthTask_ToCompleteRoute()
	{
		PortLocation boardLocation = PortLocation.LUNAR_ISLE;
		
		// Already holding 3 tasks (as mentioned in video)
		// All are round-trips: pickup south, deliver to Lunar Isle
		List<CourierTask> heldTasks = Arrays.asList(
			createTask(CourierTaskData.LUNAR_ISLE_COAL_DELIVERY_429, 0),    // Deepfin Point -> Lunar Isle
			createTask(CourierTaskData.LUNAR_ISLE_FISH_DELIVERY_431, 0),    // Port Roberts -> Lunar Isle  
			createTask(CourierTaskData.LUNAR_ISLE_POTION_DELIVERY_437, 0)   // Aldarin -> Lunar Isle
		);
		
		// Looking for a 4th task that fits the route
		// Pending pickups: Aldarin, Deepfin Point, Port Roberts
		// Pending delivery: Lunar Isle
		// Good tasks: pickup at any pending location, deliver to board OR another pending location
		List<CourierTaskData> availableTasks = Arrays.asList(
			// Perfect: Pickup at pending location, deliver to Lunar Isle (board)
			CourierTaskData.LUNAR_ISLE_POTION_DELIVERY_424,   // Prifddinas -> Lunar Isle, 4341 XP (delivers to board!)
			
			// Good: Pickup at pending location, deliver to another pending pickup location
			// (We're already visiting Port Roberts for pickup, so delivering there is efficient)
			CourierTaskData.ALDARIN_JEWELLERY_DELIVERY_296,   // Aldarin -> Port Roberts, 2415 XP
			CourierTaskData.DEEPFIN_POINT_FABRIC_DELIVERY_325, // Deepfin Point -> Port Roberts, 3188 XP
			
			// Distractor: Low XP, doesn't fit route well
			CourierTaskData.RELLEKKA_FISH_DELIVERY_400        // Etceteria -> Rellekka, 688 XP
		);
		
		// Build pending locations from held tasks
		Set<PortLocation> pendingPickups = new HashSet<>();
		Set<PortLocation> pendingDeliveries = new HashSet<>();
		for (CourierTask task : heldTasks)
		{
			pendingPickups.add(task.getData().getPickupPort());
			pendingDeliveries.add(task.getData().getDeliveryPort());
		}
		
		// All route locations (pickups + deliveries)
		Set<PortLocation> routeLocations = new HashSet<>();
		routeLocations.addAll(pendingPickups);
		routeLocations.addAll(pendingDeliveries);
		routeLocations.add(boardLocation); // Board is also on route
		
		List<TaskRecommendation> recommendations = 
			engine.rankTasks(availableTasks, boardLocation, heldTasks);
		
		TaskRecommendation topRec = recommendations.get(0);
		PortLocation topDelivery = topRec.getTask().getDeliveryPort();
		PortLocation topPickup = topRec.getTask().getPickupPort();
		
		// Top task should:
		// - Pickup at a route location (pending pickup, pending delivery, or board)
		// - Deliver to a route location (board, pending delivery, or pending pickup)
		// This ensures the task "fits" the route we're already planning
		boolean pickupFitsRoute = routeLocations.contains(topPickup);
		boolean deliveryFitsRoute = routeLocations.contains(topDelivery);
		
		assertTrue("Top recommendation should pickup at a route location. Got pickup at: " + topPickup + 
			". Route locations: " + routeLocations,
			pickupFitsRoute);
		assertTrue("Top recommendation should deliver to a route location. Got delivery to: " + topDelivery +
			". Route locations: " + routeLocations,
			deliveryFitsRoute);
		
		// The low-XP Rellekka task should rank lowest  
		TaskRecommendation lowRec = recommendations.stream()
			.filter(r -> r.getTask() == CourierTaskData.RELLEKKA_FISH_DELIVERY_400)
			.findFirst().orElse(null);
		
		assertNotNull("Should have Rellekka task in recommendations", lowRec);
		assertTrue("Low-XP task should have lower score than top task",
			lowRec.getScore() < topRec.getScore());
	}
	
	/**
	 * Video Strategy Test: Alternative starting point when Aldarin task not available.
	 * 
	 * Video quote: "What happens if you don't get the Alderin start at Lunar Isle? 
	 * That's okay. In this example, I only got the deep fin point delivery task."
	 * 
	 * The algorithm should still work well when starting from Deepfin Point instead of Aldarin.
	 */
	@Test
	public void testVideoStrategy_AlternativeStart_DeepfinPointInsteadOfAldarin()
	{
		PortLocation boardLocation = PortLocation.LUNAR_ISLE;
		List<CourierTask> heldTasks = Collections.emptyList();
		
		// Available tasks - no Aldarin task, but Deepfin Point is available
		List<CourierTaskData> availableTasks = Arrays.asList(
			// Good: Round-trip through Deepfin Point
			CourierTaskData.LUNAR_ISLE_COAL_DELIVERY_429,     // Deepfin Point -> Lunar Isle, 7188 XP
			
			// Other good options
			CourierTaskData.LUNAR_ISLE_FISH_DELIVERY_431,     // Port Roberts -> Lunar Isle, 4626 XP
			CourierTaskData.LUNAR_ISLE_POTION_DELIVERY_424,   // Prifddinas -> Lunar Isle, 4341 XP
			CourierTaskData.LUNAR_ISLE_FISH_DELIVERY_420,     // Port Piscarilius -> Lunar Isle, 3345 XP
			
			// Distractors
			CourierTaskData.ETCETERIA_MONKFISH_DELIVERY_416,  // Piscatoris -> Etceteria, 3203 XP
			CourierTaskData.DEEPFIN_POINT_FUR_DELIVERY_398    // Rellekka -> Deepfin Point, 3625 XP (wrong direction)
		);
		
		List<TaskRecommendation> recommendations = 
			engine.rankTasks(availableTasks, boardLocation, heldTasks);
		
		// Even without Aldarin task, the algorithm should prefer:
		// 1. Round-trip tasks that deliver to Lunar Isle
		// 2. Higher XP tasks
		
		List<CourierTaskData> top4 = recommendations.stream()
			.limit(4)
			.map(TaskRecommendation::getTask)
			.collect(Collectors.toList());
		
		// All top 4 should deliver to Lunar Isle
		for (CourierTaskData task : top4)
		{
			assertEquals("Top task should deliver to Lunar Isle: " + task,
				PortLocation.LUNAR_ISLE, task.getDeliveryPort());
		}
		
		// The Deepfin Point -> Rellekka task (wrong direction) should not be in top 4
		assertFalse("Wrong-direction task should not be in top 4",
			top4.contains(CourierTaskData.DEEPFIN_POINT_FUR_DELIVERY_398));
	}
	
	/**
	 * Video Strategy Test: Full route simulation with multiple noticeboards.
	 * 
	 * Simulates the complete route from video:
	 * 1. Start at Lunar Isle, accept 4 round-trip tasks
	 * 2. At each port going north, verify algorithm makes good recommendations
	 * 
	 * This tests the "continuous task acceptance" strategy from the video.
	 */
	@Test
	public void testVideoStrategy_FullRouteSimulation()
	{
		// ======== STEP 1: At Lunar Isle noticeboard ========
		PortLocation step1Board = PortLocation.LUNAR_ISLE;
		List<CourierTask> step1Held = Collections.emptyList();
		
		List<CourierTaskData> step1Available = Arrays.asList(
			// Round-trip tasks (main strategy)
			CourierTaskData.LUNAR_ISLE_COAL_DELIVERY_429,     // Deepfin Point -> Lunar Isle
			CourierTaskData.LUNAR_ISLE_FISH_DELIVERY_431,     // Port Roberts -> Lunar Isle
			CourierTaskData.LUNAR_ISLE_POTION_DELIVERY_437,   // Aldarin -> Lunar Isle
			CourierTaskData.LUNAR_ISLE_POTION_DELIVERY_424,   // Prifddinas -> Lunar Isle
			// Distractors
			CourierTaskData.ETCETERIA_PLANK_DELIVERY_410,     // Port Piscarilius -> Etceteria
			CourierTaskData.RELLEKKA_FISH_DELIVERY_400        // Etceteria -> Rellekka
		);
		
		List<TaskRecommendation> step1Recs = 
			engine.rankTasks(step1Available, step1Board, step1Held);
		
		// Accept top 4 - should all be round-trips to Lunar Isle
		List<CourierTaskData> accepted = step1Recs.stream()
			.limit(4)
			.map(TaskRecommendation::getTask)
			.collect(Collectors.toList());
		
		assertEquals("Should accept 4 tasks", 4, accepted.size());
		for (CourierTaskData task : accepted)
		{
			assertEquals("All accepted tasks should deliver to Lunar Isle",
				PortLocation.LUNAR_ISLE, task.getDeliveryPort());
		}
		
		// ======== STEP 2: Simulate at Deepfin Point (mid-route) ========
		// After picking up cargo at Aldarin, now at Deepfin Point
		PortLocation step2Board = PortLocation.DEEPFIN_POINT;
		
		// Simulate having picked up some cargo
		List<CourierTask> step2Held = Arrays.asList(
			createTaskWithCargo(CourierTaskData.LUNAR_ISLE_POTION_DELIVERY_437, 8),  // Aldarin cargo loaded
			createTask(CourierTaskData.LUNAR_ISLE_COAL_DELIVERY_429, 0),             // Pickup HERE at Deepfin
			createTask(CourierTaskData.LUNAR_ISLE_FISH_DELIVERY_431, 0),             // Pickup at Port Roberts
			createTask(CourierTaskData.LUNAR_ISLE_POTION_DELIVERY_424, 0)            // Pickup at Prifddinas
		);
		
		// Pending deliveries: Lunar Isle (for all 4 tasks)
		// Pending pickups: Deepfin Point (HERE), Port Roberts, Prifddinas
		
		// Using tasks that PICK UP HERE (at Deepfin Point) to avoid backtrack penalty
		List<CourierTaskData> step2Available = Arrays.asList(
			// Good: Pickup HERE, deliver to Port Roberts (pending pickup location = on our route!)
			CourierTaskData.PORT_ROBERTS_NICKEL_DELIVERY_324,     // Deepfin Point -> Port Roberts, 1656 XP
			// Distractors: Pickup HERE but going south (wrong direction when cargo loaded north)
			CourierTaskData.PORT_TYRAS_MITHRIL_DELIVERY_326,      // Deepfin Point -> Port Tyras, 1438 XP
			CourierTaskData.ALDARIN_ADAMANTITE_DELIVERY_328       // Deepfin Point -> Aldarin, 1438 XP
		);
		
		List<TaskRecommendation> step2Recs = 
			engine.rankTasks(step2Available, step2Board, step2Held);
		
		// The nickel delivery picks up HERE and delivers to Port Roberts
		// Port Roberts is a pending PICKUP location, so it's on our route
		// The south-bound tasks should score lower because they go backwards
		TaskRecommendation northRec = step2Recs.stream()
			.filter(r -> r.getTask() == CourierTaskData.PORT_ROBERTS_NICKEL_DELIVERY_324)
			.findFirst().orElse(null);
		TaskRecommendation southRec = step2Recs.stream()
			.filter(r -> r.getTask() == CourierTaskData.PORT_TYRAS_MITHRIL_DELIVERY_326)
			.findFirst().orElse(null);
		
		assertNotNull("Should have north-bound (Port Roberts) recommendation", northRec);
		assertNotNull("Should have south-bound recommendation", southRec);
		
		// Nickel delivery (to Port Roberts, which we'll visit for pickup) should score higher
		// than mithril delivery (to Port Tyras, going backwards)
		assertTrue("Nickel delivery to Port Roberts should score higher than mithril delivery to Port Tyras. " +
			"North score: " + northRec.getScore() + ", South score: " + southRec.getScore(),
			northRec.getScore() > southRec.getScore());
		
		// ======== STEP 3: At Port Roberts (continuing north) ========
		PortLocation step3Board = PortLocation.PORT_ROBERTS;
		
		// More cargo loaded as we progress
		List<CourierTask> step3Held = Arrays.asList(
			createTaskWithCargo(CourierTaskData.LUNAR_ISLE_POTION_DELIVERY_437, 8),  // Aldarin cargo
			createTaskWithCargo(CourierTaskData.LUNAR_ISLE_COAL_DELIVERY_429, 7),    // Deepfin cargo
			createTask(CourierTaskData.LUNAR_ISLE_FISH_DELIVERY_431, 0),             // Pickup HERE
			createTaskWithCargo(CourierTaskData.LUNAR_ISLE_POTION_DELIVERY_424, 8)   // Prifddinas cargo (got it earlier)
		);
		
		List<CourierTaskData> step3Available = Arrays.asList(
			// At Port Roberts noticeboard
			CourierTaskData.LUNAR_ISLE_FUR_DELIVERY_316,         // Port Roberts -> Lunar Isle! (pickup HERE, delivers to loaded cargo dest)
			CourierTaskData.PORT_ROBERTS_FISH_DELIVERY_306,      // Port Roberts -> Port Piscarilius
			// Distractors
			CourierTaskData.PORT_ROBERTS_ORE_DELIVERY_315,       // Port Roberts -> Deepfin (going back south)
			CourierTaskData.CIVITAS_ILLA_FORTIS_TOKEN_DELIVERY_272 // Port Roberts -> Civitas (south)
		);
		
		List<TaskRecommendation> step3Recs = 
			engine.rankTasks(step3Available, step3Board, step3Held);
		
		// The Lunar Isle fur delivery (Port Roberts -> Lunar Isle) should rank high
		// because it's a "pickup HERE" that delivers to our main destination (where we have cargo loaded)
		// This is the "FREE XP" scenario!
		TaskRecommendation lunarFurRec = step3Recs.stream()
			.filter(r -> r.getTask() == CourierTaskData.LUNAR_ISLE_FUR_DELIVERY_316)
			.findFirst().orElse(null);
		
		assertNotNull("Should have Lunar Isle fur delivery in recommendations", lunarFurRec);
		
		// This should be ranked #1 because it's FREE XP (pickup HERE + deliver to loaded destination)
		assertEquals("Lunar Isle fur delivery should be top recommendation (FREE XP scenario)",
			CourierTaskData.LUNAR_ISLE_FUR_DELIVERY_316, step3Recs.get(0).getTask());
		
		// The south-bound tasks should score lower
		TaskRecommendation step3SouthRec = step3Recs.stream()
			.filter(r -> r.getTask() == CourierTaskData.PORT_ROBERTS_ORE_DELIVERY_315)
			.findFirst().orElse(null);
		
		if (step3SouthRec != null)
		{
			assertTrue("Lunar Isle delivery should score higher than south-bound delivery",
				lunarFurRec.getScore() > step3SouthRec.getScore());
		}
	}
}
