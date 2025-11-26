package com.nucleon.porttasks.optimizer;

import lombok.Getter;

@Getter
public enum RecommendationType
{
	TAKE_NOW("Take Now"),
	SWAP("Swap"),
	CONSIDER("Consider"),
	SKIP("Skip"),
	UNAVAILABLE("Unavailable");

	private final String label;

	RecommendationType(String label)
	{
		this.label = label;
	}
}
