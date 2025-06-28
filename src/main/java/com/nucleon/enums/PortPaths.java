package com.nucleon.enums;

import com.nucleon.RelativeMove;
import java.util.ArrayList;
import java.util.List;
import net.runelite.api.coords.WorldPoint;


public enum PortPaths
{
	DEFAULT(
		PortLocation.EMPTY,
		PortLocation.EMPTY
	),
	CATHERBY_BRIMHAVEN(
		PortLocation.CATHERBY,
		PortLocation.BRIMHAVEN,
		new RelativeMove(-42, -42)
	),
	BRIMHAVEN_MUSA_POINT(
		PortLocation.BRIMHAVEN,
		PortLocation.MUSA_POINT,
		new RelativeMove(0, 8),
		new RelativeMove(7, 7),
		new RelativeMove(33, 0),
		new RelativeMove(30, -30),
		new RelativeMove(76, 0),
		new RelativeMove(47, -47),
		new RelativeMove(13, 0),
		new RelativeMove(5, -5)
	),

	BRIMHAVEN_PANDEMONIUM(
		PortLocation.BRIMHAVEN,
		PortLocation.PANDEMONIUM,
		new RelativeMove(0, 8),
		new RelativeMove(7, 7),
		new RelativeMove(33, 0),
		new RelativeMove(30, -30),
		new RelativeMove(76, 0),
		new RelativeMove(47, -47),
		new RelativeMove(13, 0),
		new RelativeMove(10, -10),
		new RelativeMove(0, -63),
		new RelativeMove(63, -63),
		new RelativeMove(37, 0),
		new RelativeMove(8, -8)

	),

	BRIMHAVEN_PORT_KHAZARD(
			PortLocation.BRIMHAVEN,
			PortLocation.PORT_KHAZARD,
			new RelativeMove(0, 0),
			new RelativeMove(0, 0),
			new RelativeMove(0, 0),
			new RelativeMove(0, 0)
	),

	CATHERBY_EAST_ARDOUGNE(
			PortLocation.CATHERBY,
			PortLocation.EAST_ARDOUGNE,
			new RelativeMove(0, 0),
			new RelativeMove(0, 0),
			new RelativeMove(0, 0),
			new RelativeMove(0, 0)
	),

	CATHERBY_MUSA_POINT(
			PortLocation.CATHERBY,
			PortLocation.MUSA_POINT,
			new RelativeMove(0, 0),
			new RelativeMove(0, 0),
			new RelativeMove(0, 0),
			new RelativeMove(0, 0)
	),

	CATHERBY_PANDEMONIUM(
			PortLocation.CATHERBY,
			PortLocation.PANDEMONIUM,
			new RelativeMove(0, 0),
			new RelativeMove(0, 0),
			new RelativeMove(0, 0),
			new RelativeMove(0, 0)
	),

	CATHERBY_PORT_KHAZARD(
			PortLocation.CATHERBY,
			PortLocation.PORT_KHAZARD,
			new RelativeMove(0, 0),
			new RelativeMove(0, 0),
			new RelativeMove(0, 0),
			new RelativeMove(0, 0)
	),

	CATHERBY_PORT_SARIM(
			PortLocation.CATHERBY,
			PortLocation.PORT_SARIM,
			new RelativeMove(0, 0),
			new RelativeMove(0, 0),
			new RelativeMove(0, 0),
			new RelativeMove(0, 0)
	),

	EAST_ARDOUGNE_PORT_KHAZARD(
			PortLocation.EAST_ARDOUGNE,
			PortLocation.PORT_KHAZARD,
			new RelativeMove(0, 0),
			new RelativeMove(0, 0),
			new RelativeMove(0, 0),
			new RelativeMove(0, 0)
	),

	EAST_ARDOUGNE_RUINS_OF_UNKAH(
			PortLocation.EAST_ARDOUGNE,
			PortLocation.RUINS_OF_UNKAH,
			new RelativeMove(0, 0),
			new RelativeMove(0, 0),
			new RelativeMove(0, 0),
			new RelativeMove(0, 0)
	),

	ENTRANA_MUSA_POINT(
			PortLocation.ENTRANA,
			PortLocation.MUSA_POINT,
			new RelativeMove(0, 0),
			new RelativeMove(0, 0),
			new RelativeMove(0, 0),
			new RelativeMove(0, 0)
	),

	MUSA_POINT_PANDEMONIUM(
			PortLocation.MUSA_POINT,
			PortLocation.PANDEMONIUM,
			new RelativeMove(0, 0),
			new RelativeMove(0, 0),
			new RelativeMove(0, 0),
			new RelativeMove(0, 0)
	),

	MUSA_POINT_PORT_KHAZARD(
			PortLocation.MUSA_POINT,
			PortLocation.PORT_KHAZARD,
			new RelativeMove(0, 0),
			new RelativeMove(0, 0),
			new RelativeMove(0, 0),
			new RelativeMove(0, 0)
	),

	MUSA_POINT_PORT_SARIM(
			PortLocation.MUSA_POINT,
			PortLocation.PORT_SARIM,
			new RelativeMove(0, 0),
			new RelativeMove(0, 0),
			new RelativeMove(0, 0),
			new RelativeMove(0, 0)
	),

	MUSA_POINT_RUINS_OF_UNKAH(
			PortLocation.MUSA_POINT,
			PortLocation.RUINS_OF_UNKAH,
			new RelativeMove(0, 0),
			new RelativeMove(0, 0),
			new RelativeMove(0, 0),
			new RelativeMove(0, 0)
	),

	PANDEMONIUM_PORT_KHAZARD(
			PortLocation.PANDEMONIUM,
			PortLocation.PORT_KHAZARD,
			new RelativeMove(0, 0),
			new RelativeMove(0, 0),
			new RelativeMove(0, 0),
			new RelativeMove(0, 0)
	),

	PANDEMONIUM_PORT_SARIM(
			PortLocation.PANDEMONIUM,
			PortLocation.PORT_SARIM,
			new RelativeMove(0, 0),
			new RelativeMove(0, 0),
			new RelativeMove(0, 0),
			new RelativeMove(0, 0)
	),

	PANDEMONIUM_RUINS_OF_UNKAH(
			PortLocation.PANDEMONIUM,
			PortLocation.RUINS_OF_UNKAH,
			new RelativeMove(0, 0),
			new RelativeMove(0, 0),
			new RelativeMove(0, 0),
			new RelativeMove(0, 0)
	),

	PORT_KHAZARD_PORT_SARIM(
			PortLocation.PORT_KHAZARD,
			PortLocation.PORT_SARIM,
			new RelativeMove(0, 0),
			new RelativeMove(0, 0),
			new RelativeMove(0, 0),
			new RelativeMove(0, 0)
	),

	PORT_KHAZARD_RUINS_OF_UNKAH(
			PortLocation.PORT_KHAZARD,
			PortLocation.RUINS_OF_UNKAH,
			new RelativeMove(0, 0),
			new RelativeMove(0, 0),
			new RelativeMove(0, 0),
			new RelativeMove(0, 0)
	),

	RUINS_OF_UNKAH_SUMMER_SHORE(
			PortLocation.RUINS_OF_UNKAH,
			PortLocation.SUMMER_SHORE,
			new RelativeMove(0, 0),
			new RelativeMove(0, 0),
			new RelativeMove(0, 0),
			new RelativeMove(0, 0)
	),
	PORT_SARIM_PANDEMONIUM(
		PortLocation.PORT_SARIM,
		PortLocation.PANDEMONIUM,
		new RelativeMove(0, -43),
		new RelativeMove(-22, -22),
		new RelativeMove(0, -91),
		new RelativeMove(44, -44)
	);

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