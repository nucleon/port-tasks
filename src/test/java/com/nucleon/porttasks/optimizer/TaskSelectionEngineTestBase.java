package com.nucleon.porttasks.optimizer;

import com.nucleon.porttasks.CourierTask;
import com.nucleon.porttasks.enums.CourierTaskData;
import org.junit.Before;

import java.awt.*;

/**
 * Base class for TaskSelectionEngine tests providing common setup and helper methods.
 */
public abstract class TaskSelectionEngineTestBase
{
	protected TaskSelectionEngine engine;
	protected DistanceCache distanceCache;
	
	@Before
	public void setUp()
	{
		distanceCache = new DistanceCache();
		engine = new TaskSelectionEngine(distanceCache);
	}
	
	/**
	 * Create a task with specified cargo amount.
	 * Use cargoTaken=0 for planning mode (no cargo loaded).
	 */
	protected CourierTask createTask(CourierTaskData taskData, int cargoTaken)
	{
		return new CourierTask(
			taskData,
			1,                  // slot
			cargoTaken > 0,     // taken
			0,                  // delivered
			true,               // tracking
			true,               // active
			Color.WHITE,        // overlayColor
			cargoTaken          // cargoTaken
		);
	}
	
	/**
	 * Create a task with specific cargo amount loaded.
	 * Use this when testing mid-route scenarios where cargo is already picked up.
	 */
	protected CourierTask createTaskWithCargo(CourierTaskData taskData, int cargoAmount)
	{
		return new CourierTask(
			taskData,
			1,                  // slot
			true,               // taken
			0,                  // delivered
			true,               // tracking
			true,               // active
			Color.WHITE,        // overlayColor
			cargoAmount         // cargoTaken
		);
	}
}
