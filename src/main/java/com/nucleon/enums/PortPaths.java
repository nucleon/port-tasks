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

  public PortLocation getStart() { return start; }

	public PortLocation getEnd() { return end; }

	public List<WorldPoint> getPathPoints() { return pathPoints; }

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