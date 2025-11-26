package com.nucleon.porttasks.optimizer;

import com.nucleon.porttasks.CourierTask;

import java.util.List;

/**
 * Operating mode for the route optimizer.
 *
 * PLANNING: All tasks have cargoTaken == 0 (complete flexibility)
 * EXECUTION: At least one task has cargoTaken > 0 (route partially locked)
 */
public enum OptimizerMode
{
	PLANNING,
	EXECUTION;

	public static OptimizerMode detect(List<CourierTask> tasks)
	{
		if (tasks == null || tasks.isEmpty())
		{
			return PLANNING;
		}

		for (CourierTask task : tasks)
		{
			if (task.getCargoTaken() > 0)
			{
				return EXECUTION;
			}
		}

		return PLANNING;
	}

	public boolean isPlanning()
	{
		return this == PLANNING;
	}
}
