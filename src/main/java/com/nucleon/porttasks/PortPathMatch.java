package com.nucleon.porttasks;

import com.nucleon.porttasks.enums.PortPaths;

public final class PortPathMatch
{
	private final PortPaths path;
	private final boolean reversed;

	public PortPathMatch(PortPaths path, boolean reversed)
	{
		this.path = path;
		this.reversed = reversed;
	}

	public PortPaths getPath()
	{
		return path;
	}

	public boolean isReversed()
	{
		return reversed;
	}
}
