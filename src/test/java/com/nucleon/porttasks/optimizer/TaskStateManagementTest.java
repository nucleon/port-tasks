package com.nucleon.porttasks.optimizer;

import com.nucleon.porttasks.CourierTask;
import com.nucleon.porttasks.enums.CourierTaskData;
import com.nucleon.porttasks.enums.PortLocation;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Tests for TaskSelectionEngine active/inactive state management and planning mode.
 * 
 * Key concepts:
 * - Active tasks: Included in current route calculations
 * - Inactive tasks: Held for future routes, excluded from current calculations
 * - Auto-exclude: Tasks that don't fit the current route are automatically set inactive
 * - Planning mode: When all cargo is delivered, re-include all tasks
 */
public class TaskStateManagementTest extends TaskSelectionEngineTestBase
{
	// ========================================
	// ACTIVE/INACTIVE STATE TESTS
	// ========================================
	
	/**
	 * Test: Task can be marked as inactive (excluded from route).
	 */
	@Test
	public void testTaskState_CanBeSetInactive()
	{
		CourierTask task = createTask(CourierTaskData.ETCETERIA_PLANK_DELIVERY_410, 0);
		
		// By default, task should be active
		assertTrue("New task should be active by default", engine.isTaskActive(task));
		
		// Mark as inactive
		engine.setTaskActive(task, false);
		assertFalse("Task should be inactive after setting", engine.isTaskActive(task));
		
		// Re-activate
		engine.setTaskActive(task, true);
		assertTrue("Task should be active after re-enabling", engine.isTaskActive(task));
	}
	
	/**
	 * Test: Inactive tasks are excluded from route calculations.
	 */
	@Test
	public void testInactiveTasks_ExcludedFromRouteCalculations()
	{
		PortLocation boardLocation = PortLocation.LUNAR_ISLE;
		
		// Two tasks, one active, one inactive
		CourierTask activeTask = createTask(CourierTaskData.LUNAR_ISLE_FUR_DELIVERY_422, 0);
		CourierTask inactiveTask = createTask(CourierTaskData.LUNAR_ISLE_POTION_DELIVERY_424, 0);
		
		engine.setTaskActive(inactiveTask, false);
		
		List<CourierTask> allTasks = Arrays.asList(activeTask, inactiveTask);
		List<CourierTask> activeTasks = engine.getActiveTasks(allTasks);
		
		assertEquals("Should only return active tasks", 1, activeTasks.size());
		assertEquals("Active task should be the non-excluded one", activeTask, activeTasks.get(0));
	}
	
	/**
	 * Test: Get pending pickups only considers active tasks.
	 */
	@Test
	public void testPendingPickups_OnlyFromActiveTasks()
	{
		CourierTask activeTask = createTask(CourierTaskData.LUNAR_ISLE_FUR_DELIVERY_422, 0); // Piscatoris pickup
		CourierTask inactiveTask = createTask(CourierTaskData.LUNAR_ISLE_POTION_DELIVERY_424, 0); // Prifddinas pickup
		
		engine.setTaskActive(inactiveTask, false);
		
		List<CourierTask> allTasks = Arrays.asList(activeTask, inactiveTask);
		Set<PortLocation> pendingPickups = engine.getPendingPickups(allTasks);
		
		assertTrue("Should include pickup from active task", 
			pendingPickups.contains(PortLocation.PISCATORIS));
		assertFalse("Should NOT include pickup from inactive task", 
			pendingPickups.contains(PortLocation.PRIFDDINAS));
	}
	
	// ========================================
	// AUTO-EXCLUDE LOGIC TESTS
	// ========================================
	
	/**
	 * Test: Task with pickup far from current route is flagged as "doesn't fit".
	 */
	@Test
	public void testAutoExclude_TaskDoesntFitCurrentRoute()
	{
		PortLocation boardLocation = PortLocation.ETCETERIA;
		
		// Current route is in the north (Etceteria area)
		// Held tasks all have pickups/deliveries in the north
		List<CourierTask> heldTasks = Arrays.asList(
			createTask(CourierTaskData.RELLEKKA_FISH_DELIVERY_400, 1), // Cargo loaded, deliver to Rellekka
			createTask(CourierTaskData.ETCETERIA_IRON_DELIVERY_401, 0)  // Need pickup at Rellekka
		);
		
		// New task: Sunset Coast -> Etceteria (pickup way south)
		CourierTaskData farTask = CourierTaskData.ETCETERIA_SWORD_DELIVERY_414;
		
		boolean fitsRoute = engine.taskFitsCurrentRoute(farTask, boardLocation, heldTasks);
		
		// Sunset Coast is very far from the current northern route
		assertFalse("Task with distant pickup should not fit current route", fitsRoute);
	}
	
	/**
	 * Test: Task with pickup on current route is NOT flagged for exclusion.
	 */
	@Test
	public void testAutoExclude_TaskFitsCurrentRoute()
	{
		PortLocation boardLocation = PortLocation.ETCETERIA;
		
		// Current route is in the north
		// RELLEKKA_FISH_DELIVERY_400: Pickup at Etceteria, Deliver to Rellekka
		List<CourierTask> heldTasks = Arrays.asList(
			createTask(CourierTaskData.RELLEKKA_FISH_DELIVERY_400, 1) // Cargo loaded, deliver to Rellekka
		);
		
		// New task: ETCETERIA_IRON_DELIVERY_401: Pickup at Rellekka, Deliver to Etceteria
		// This should fit perfectly: pick up at Rellekka (where we're going) and deliver back to Etceteria (board)
		CourierTaskData nearTask = CourierTaskData.ETCETERIA_IRON_DELIVERY_401;
		
		boolean fitsRoute = engine.taskFitsCurrentRoute(nearTask, boardLocation, heldTasks);
		
		assertTrue("Task with pickup at pending delivery should fit route", fitsRoute);
	}
	
	/**
	 * Test: Should auto-exclude returns true for nonsensical tasks.
	 */
	@Test
	public void testShouldAutoExclude_ReturnsTrueForNonsensicalTask()
	{
		PortLocation boardLocation = PortLocation.LUNAR_ISLE;
		
		// Current route: pickups at Piscatoris, Fossil Island (northern area)
		List<CourierTask> heldTasks = Arrays.asList(
			createTask(CourierTaskData.ETCETERIA_MONKFISH_DELIVERY_416, 0), // Piscatoris -> Etceteria
			createTask(CourierTaskData.RELLEKKA_MONKFISH_DELIVERY_393, 0)   // Piscatoris -> Rellekka
		);
		
		// New task accepted: Sunset Coast -> Etceteria (pickup way south, doesn't fit)
		CourierTaskData newTask = CourierTaskData.ETCETERIA_SWORD_DELIVERY_414;
		
		boolean shouldExclude = engine.shouldAutoExclude(newTask, boardLocation, heldTasks);
		
		assertTrue("Should auto-exclude task that doesn't fit current route", shouldExclude);
	}
	
	// ========================================
	// PLANNING MODE TESTS
	// ========================================
	
	/**
	 * Test: Detect planning mode when no cargo is loaded.
	 */
	@Test
	public void testDetectPlanningMode_NoCargoLoaded()
	{
		List<CourierTask> tasks = Arrays.asList(
			createTask(CourierTaskData.ETCETERIA_PLANK_DELIVERY_410, 0),
			createTask(CourierTaskData.LUNAR_ISLE_POTION_DELIVERY_424, 0)
		);
		
		assertTrue("Should be in planning mode when no cargo loaded", 
			engine.isInPlanningMode(tasks));
	}
	
	/**
	 * Test: Detect execution mode when cargo is loaded.
	 */
	@Test
	public void testDetectExecutionMode_CargoLoaded()
	{
		List<CourierTask> tasks = Arrays.asList(
			createTask(CourierTaskData.ETCETERIA_PLANK_DELIVERY_410, 1), // Cargo loaded
			createTask(CourierTaskData.LUNAR_ISLE_POTION_DELIVERY_424, 0)
		);
		
		assertFalse("Should NOT be in planning mode when cargo is loaded", 
			engine.isInPlanningMode(tasks));
	}
	
	/**
	 * Test: Re-include all tasks when entering planning mode.
	 */
	@Test
	public void testReIncludeTasks_WhenEnteringPlanningMode()
	{
		CourierTask task1 = createTask(CourierTaskData.ETCETERIA_PLANK_DELIVERY_410, 0);
		CourierTask task2 = createTask(CourierTaskData.LUNAR_ISLE_POTION_DELIVERY_424, 0);
		
		// Mark task2 as inactive
		engine.setTaskActive(task2, false);
		assertFalse("Task2 should be inactive", engine.isTaskActive(task2));
		
		// Enter planning mode
		engine.enterPlanningMode();
		
		assertTrue("Task1 should be active after entering planning mode", engine.isTaskActive(task1));
		assertTrue("Task2 should be re-activated after entering planning mode", engine.isTaskActive(task2));
	}
	
	/**
	 * Test: All tasks with no cargo and at a noticeboard triggers planning mode.
	 */
	@Test
	public void testShouldEnterPlanningMode_AllCargoDelivered()
	{
		// All tasks delivered (empty list or all tasks have cargoTaken == 0 and delivered > 0)
		List<CourierTask> tasks = Arrays.asList(
			createTask(CourierTaskData.ETCETERIA_PLANK_DELIVERY_410, 0),
			createTask(CourierTaskData.LUNAR_ISLE_POTION_DELIVERY_424, 0)
		);
		
		// At a noticeboard, all cargo delivered
		boolean shouldEnter = engine.shouldEnterPlanningMode(tasks, true);
		
		assertTrue("Should enter planning mode when at noticeboard with no cargo", shouldEnter);
	}
	
	// ========================================
	// AUTO-EXCLUDE FUTURE ROUTE TESTS (EXECUTION MODE)
	// ========================================
	
	/**
	 * Test: At Lunar Isle in execution mode (cargo loaded for Rellekka),
	 * picking up a task for "Aldarin -> Lunar Isle" should auto-exclude it.
	 */
	@Test
	public void testAutoExclude_FutureRouteTaskWhileInExecutionMode()
	{
		PortLocation boardLocation = PortLocation.LUNAR_ISLE;
		
		// Current state: EXECUTION MODE - cargo loaded for Rellekka
		CourierTask activeCargoTask = createTaskWithCargo(CourierTaskData.RELLEKKA_WINE_DELIVERY_303, 4);
		List<CourierTask> heldTasks = Arrays.asList(activeCargoTask);
		
		// Verify we're in execution mode
		assertFalse("Should be in execution mode (cargo loaded)", engine.isInPlanningMode(heldTasks));
		
		// New task: Aldarin -> Lunar Isle - pickup at Aldarin doesn't fit route
		CourierTaskData newTask = CourierTaskData.LUNAR_ISLE_POTION_DELIVERY_437;
		
		boolean shouldExclude = engine.shouldAutoExclude(newTask, boardLocation, heldTasks);
		
		assertTrue("Should auto-exclude task for future route while in execution mode",
			shouldExclude);
	}
	
	/**
	 * Test: Task that picks up HERE but delivers OFF-ROUTE should be auto-excluded in execution mode.
	 */
	@Test
	public void testAutoExclude_PickupHereDeliverOffRoute_InExecutionMode_ShouldExclude()
	{
		PortLocation boardLocation = PortLocation.LUNAR_ISLE;
		
		// EXECUTION MODE: cargo loaded for Rellekka
		CourierTask activeCargoTask = createTaskWithCargo(CourierTaskData.RELLEKKA_WINE_DELIVERY_303, 4);
		List<CourierTask> heldTasks = Arrays.asList(activeCargoTask);
		
		// New task: pickup HERE (Lunar Isle), deliver to Port Piscarilius (OFF-ROUTE)
		CourierTaskData pickupHereTask = CourierTaskData.PORT_PISCARILIUS_FABRIC_DELIVERY_419;
		
		boolean shouldExclude = engine.shouldAutoExclude(pickupHereTask, boardLocation, heldTasks);
		
		assertTrue("Should auto-exclude task that picks up HERE but delivers OFF-ROUTE in execution mode",
			shouldExclude);
	}
	
	/**
	 * Test: Task that picks up HERE and delivers ON-ROUTE should NOT be auto-excluded.
	 */
	@Test
	public void testAutoExclude_PickupHereDeliverOnRoute_InExecutionMode_ShouldNotExclude()
	{
		PortLocation boardLocation = PortLocation.LUNAR_ISLE;
		
		// EXECUTION MODE: cargo loaded for Port Piscarilius
		CourierTask cargoToPiscarilius = createTaskWithCargo(CourierTaskData.PORT_PISCARILIUS_FABRIC_DELIVERY_419, 4);
		List<CourierTask> heldTasksForThisTest = Arrays.asList(cargoToPiscarilius);
		
		// New task: picks up at Lunar Isle, delivers to Port Piscarilius (same destination = ON-ROUTE)
		CourierTaskData newTask = CourierTaskData.PORT_PISCARILIUS_RUNE_DELIVERY_425;
		
		boolean shouldExclude = engine.shouldAutoExclude(newTask, boardLocation, heldTasksForThisTest);
		
		assertFalse("Should NOT auto-exclude task that picks up HERE and delivers ON-ROUTE",
			shouldExclude);
	}
	
	/**
	 * Test: Task that picks up HERE and delivers to a PENDING PICKUP location should NOT be excluded.
	 */
	@Test
	public void testAutoExclude_PickupHereDeliverToPendingPickup_ShouldNotExclude()
	{
		PortLocation boardLocation = PortLocation.LUNAR_ISLE;
		
		// EXECUTION MODE: cargo loaded for Rellekka + pending pickup at Port Piscarilius
		CourierTask cargoTask = createTaskWithCargo(CourierTaskData.RELLEKKA_WINE_DELIVERY_303, 4);
		CourierTask pendingPickupTask = createTask(CourierTaskData.LUNAR_ISLE_FISH_DELIVERY_420, 0);
		List<CourierTask> heldTasks = Arrays.asList(cargoTask, pendingPickupTask);
		
		// New task: pickup HERE (Lunar Isle), deliver to Port Piscarilius (pending pickup location)
		CourierTaskData newTask = CourierTaskData.PORT_PISCARILIUS_RUNE_DELIVERY_425;
		
		boolean shouldExclude = engine.shouldAutoExclude(newTask, boardLocation, heldTasks);
		
		assertFalse("Should NOT auto-exclude task that delivers to a pending pickup location",
			shouldExclude);
	}
	
	/**
	 * Test: In PLANNING mode (no cargo loaded), any task should fit.
	 */
	@Test
	public void testAutoExclude_PlanningMode_AnyTaskFits()
	{
		PortLocation boardLocation = PortLocation.LUNAR_ISLE;
		
		// PLANNING MODE: no cargo loaded
		CourierTask planningTask = createTask(CourierTaskData.LUNAR_ISLE_POTION_DELIVERY_437, 0);
		List<CourierTask> heldTasks = Arrays.asList(planningTask);
		
		// Verify we're in planning mode
		assertTrue("Should be in planning mode (no cargo loaded)", engine.isInPlanningMode(heldTasks));
		
		// New task from a different pickup location
		CourierTaskData newTask = CourierTaskData.LUNAR_ISLE_FISH_DELIVERY_431;
		
		boolean shouldExclude = engine.shouldAutoExclude(newTask, boardLocation, heldTasks);
		
		assertFalse("Should NOT auto-exclude any task in planning mode", shouldExclude);
	}
	
	/**
	 * Test: Task with pickup at pending DELIVERY location fits execution mode.
	 */
	@Test
	public void testAutoExclude_TaskFitsExecutionMode_PickupAtDeliveryDestination()
	{
		PortLocation boardLocation = PortLocation.LUNAR_ISLE;
		
		// EXECUTION MODE: cargo loaded for Rellekka
		CourierTask activeCargoTask = createTaskWithCargo(CourierTaskData.RELLEKKA_WINE_DELIVERY_303, 4);
		List<CourierTask> heldTasks = Arrays.asList(activeCargoTask);
		
		// New task: pickup at Rellekka (our delivery destination)
		CourierTaskData pickupAtDeliveryTask = CourierTaskData.ETCETERIA_FISH_DELIVERY_381;
		
		boolean shouldExclude = engine.shouldAutoExclude(pickupAtDeliveryTask, boardLocation, heldTasks);
		
		assertFalse("Should NOT auto-exclude task with pickup at delivery destination (Rellekka)",
			shouldExclude);
	}
}
