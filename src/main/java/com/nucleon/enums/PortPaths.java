/*
 * Copyright (c) 2025, nucleon <https://github.com/nucleon>
 * Copyright (c) 2025, Cooper Morris <https://github.com/coopermor>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.nucleon.enums;

import java.util.ArrayList;
import java.util.List;
import net.runelite.api.coords.WorldPoint;

public enum PortPaths
{
	PORT_SARIM_PANDEMONIUM(
		PortLocation.PORT_SARIM,
		PortLocation.PANDEMONIUM,
		new WorldPoint(3056, 3151, 0),
		new WorldPoint(3034, 3123, 0),
		new WorldPoint(3034, 3040, 0),
		new WorldPoint(3078, 3015, 0)
	),
	CATHERBY_BRIMHAVEN(
		PortLocation.CATHERBY,
		PortLocation.BRIMHAVEN,
		new WorldPoint(2781, 3393, 0),
		new WorldPoint(2781, 3315, 0),
		new WorldPoint(2754, 3300, 0)
	),
	BRIMHAVEN_MUSA_POINT(
		PortLocation.BRIMHAVEN,
		PortLocation.MUSA_POINT,
		new WorldPoint(2754, 3244, 0),
		new WorldPoint(2759, 3248, 0),
		new WorldPoint(2787, 3248, 0),
		new WorldPoint(2819, 3212, 0),
		new WorldPoint(2900, 3212, 0),
		new WorldPoint(2965, 3165, 0)
		//new WorldPoint(2965, 3150, 0)
	);

	private final PortLocation start;
	private final PortLocation end;
	private final List<WorldPoint> pathPoints;

	PortPaths(PortLocation start, PortLocation end, WorldPoint... pathPoints)
	{
		this.start = start;
		this.end = end;
		this.pathPoints = List.of(pathPoints);
	}

	public PortLocation getStart()
	{
		return start;
	}

	public PortLocation getEnd()
	{
		return end;
	}

	public List<WorldPoint> getPathPoints()
	{
		return pathPoints;
	}

	public List<WorldPoint> getFullPath()
	{
		List<WorldPoint> fullPath = new ArrayList<>();
		fullPath.add(start.getNavigationLocation());
		fullPath.addAll(pathPoints);
		fullPath.add(end.getNavigationLocation());
		return fullPath;
	}

	@Override
	public String toString()
	{
		return String.format("%s -> %s (%d points)", start.name(), end.name(), pathPoints.size());
	}

}