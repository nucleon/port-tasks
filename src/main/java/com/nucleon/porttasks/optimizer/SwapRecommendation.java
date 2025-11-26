package com.nucleon.porttasks.optimizer;

import com.nucleon.porttasks.CourierTask;
import com.nucleon.porttasks.enums.CourierTaskData;
import lombok.Getter;


@Getter
public class SwapRecommendation
{
	private final CourierTaskData newTask;
	private final CourierTask taskToSwap;
	private final double scoreImprovement;
	private final String reason;

	public SwapRecommendation(CourierTaskData newTask, CourierTask taskToSwap,
		double scoreImprovement, String reason)
	{
		this.newTask = newTask;
		this.taskToSwap = taskToSwap;
		this.scoreImprovement = scoreImprovement;
		this.reason = reason;
	}
}
