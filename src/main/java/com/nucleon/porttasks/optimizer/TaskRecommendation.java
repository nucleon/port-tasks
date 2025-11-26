package com.nucleon.porttasks.optimizer;

import com.nucleon.porttasks.CourierTask;
import com.nucleon.porttasks.enums.CourierTaskData;
import lombok.Getter;
import lombok.Setter;

@Getter
public class TaskRecommendation
{
	private final CourierTaskData task;
	private final double score;
	private final String reason;
	private final RecommendationType type;
	private final Integer rank;
	private final CourierTask swapWithTask;

    @Setter
    private String routePreview;
	@Setter
    private String currentRoutePreview;

	public TaskRecommendation(CourierTaskData task, double score, String reason)
	{
		this(task, score, reason, RecommendationType.CONSIDER, null, null);
	}

	public TaskRecommendation(CourierTaskData task, double score, String reason,
		RecommendationType type, Integer rank)
	{
		this(task, score, reason, type, rank, null);
	}

	public TaskRecommendation(CourierTaskData task, double score, String reason,
		RecommendationType type, Integer rank, CourierTask swapWithTask)
	{
		this.task = task;
		this.score = score;
		this.reason = reason;
		this.type = type;
		this.rank = rank;
		this.swapWithTask = swapWithTask;
	}
}
