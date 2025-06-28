package com.nucleon.enums;

import com.nucleon.RelativeMove;
import java.util.ArrayList;
import java.util.List;
import net.runelite.api.coords.WorldPoint;


public enum PortPaths
{

	PORT_SARIM_PANDEMONIUM(
		PortLocation.PORT_SARIM,
		PortLocation.PANDEMONIUM,
		new RelativeMove(0, -43),
		new RelativeMove(-22, -22),
		new RelativeMove(0, -91),
		new RelativeMove(44, -44)
	);/*
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
	);*/

	private final PortLocation start;
	private final PortLocation end;
	private final List<RelativeMove> pathPoints;

	PortPaths(PortLocation start, PortLocation end, RelativeMove... pathPoints)
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

	public List<WorldPoint> getFullPath()
	{
		List<WorldPoint> fullPath = new ArrayList<>();
		WorldPoint current = start.getNavigationLocation();
		fullPath.add(current);
		for (RelativeMove delta : pathPoints)
		{
			current = new WorldPoint(current.getX() + delta.getDx(), current.getY() + delta.getDy(), current.getPlane());
			fullPath.add(current);
		}
		fullPath.add(end.getNavigationLocation());
		return fullPath;
	}

	@Override
	public String toString()
	{
		return String.format("%s -> %s (%d points)", start.name(), end.name(), pathPoints.size());
	}

}