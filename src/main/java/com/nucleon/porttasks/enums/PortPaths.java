package com.nucleon.porttasks.enums;

import com.nucleon.porttasks.RelativeMove;
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
		new RelativeMove(14, -14),
		new RelativeMove(0, -63),
		new RelativeMove(59, -59),
		new RelativeMove(37, 0),
		new RelativeMove(8, -8)

	),

	BRIMHAVEN_PORT_KHAZARD(
		PortLocation.BRIMHAVEN,
		PortLocation.PORT_KHAZARD,
		new RelativeMove(0, 15),
		new RelativeMove(-7, 7),
		new RelativeMove(-26, 0),
		new RelativeMove(-33, -33)
	),

	CATHERBY_ARDOUGNE(
		PortLocation.CATHERBY,
		PortLocation.ARDOUGNE,
		new RelativeMove(-42, -42),
		new RelativeMove(0, -91),
		new RelativeMove(-16, -16)
	),

	CATHERBY_MUSA_POINT(
		PortLocation.CATHERBY,
		PortLocation.MUSA_POINT,
		new RelativeMove(62, 0),
		new RelativeMove(20, -20),
		new RelativeMove(0, -42),
		new RelativeMove(4, -4),
		new RelativeMove(0, -105),
		new RelativeMove(60, -60),
		new RelativeMove(18, 0),
		new RelativeMove(5, -5)
	),

	CATHERBY_PANDEMONIUM(
		PortLocation.CATHERBY,
		PortLocation.PANDEMONIUM,
		new RelativeMove(62, 0),
		new RelativeMove(20, -20),
		new RelativeMove(0, -42),
		new RelativeMove(4, -4),
		new RelativeMove(0, -105),
		new RelativeMove(60, -60),
		new RelativeMove(18, 0),
		new RelativeMove(13, -13),
		new RelativeMove(0, -67),
		new RelativeMove(75, -75),
		new RelativeMove(25, 0),
		new RelativeMove(5, -5)
	),

	CATHERBY_PORT_KHAZARD(
		PortLocation.CATHERBY,
		PortLocation.PORT_KHAZARD,
		new RelativeMove(-42, -42),
		new RelativeMove(0, -91),
		new RelativeMove(-50, -50),
		new RelativeMove(-13, 0),
		new RelativeMove(-3, -3)
	),

	CATHERBY_PORT_SARIM(
		PortLocation.CATHERBY,
		PortLocation.PORT_SARIM,
		new RelativeMove(62, 0),
		new RelativeMove(20, -20),
		new RelativeMove(0, -42),
		new RelativeMove(4, -4),
		new RelativeMove(0, -105),
		new RelativeMove(60, -60),
		new RelativeMove(18, 0),
		new RelativeMove(10, -10),
		new RelativeMove(0, -51),
		new RelativeMove(13, -13),
		new RelativeMove(24, 0),
		new RelativeMove(49, 49)
	),

	ARDOUGNE_PORT_KHAZARD(
		PortLocation.ARDOUGNE,
		PortLocation.PORT_KHAZARD,
		new RelativeMove(5, 0),
		new RelativeMove(7, -7),
		new RelativeMove(0, -60),
		new RelativeMove(6, -6)
	),

	ARDOUGNE_RUINS_OF_UNKAH(
			PortLocation.ARDOUGNE,
			PortLocation.RUINS_OF_UNKAH,

			new RelativeMove(90, 0),
			new RelativeMove(34, -34),
			new RelativeMove(17, 0),
			new RelativeMove(12, -12),

			new RelativeMove(85, 0),
			new RelativeMove(55, -55),

			new RelativeMove(0, -42),
			new RelativeMove(121, -121),
			new RelativeMove(0, -20),
			new RelativeMove(43, -43),
			new RelativeMove(0, -108)


	),

	ENTRANA_MUSA_POINT(
		PortLocation.ENTRANA,
		PortLocation.MUSA_POINT,
		new RelativeMove(0, -118),
		new RelativeMove(20, -20),
		new RelativeMove(20, 0),
		new RelativeMove(20, -20),
		new RelativeMove(17, 0),
		new RelativeMove(5, -5)
	),

	MUSA_POINT_PANDEMONIUM(
		PortLocation.MUSA_POINT,
		PortLocation.PANDEMONIUM,
		new RelativeMove(5, 0),
		new RelativeMove(5, -5),
		new RelativeMove(0, -35),
		new RelativeMove(103, -103),
		new RelativeMove(0, -20)
	),

	MUSA_POINT_PORT_KHAZARD(
		PortLocation.MUSA_POINT,
		PortLocation.PORT_KHAZARD,
		new RelativeMove(5, 0),
		new RelativeMove(0, 10),
		new RelativeMove(-35, 35),
		new RelativeMove(-20, 0),
		new RelativeMove(-25, 25),
		new RelativeMove(-85, 0),
		new RelativeMove(-41, 41),
		new RelativeMove(-40, 0),
		new RelativeMove(-40, -40),
		new RelativeMove(0, -15),
		new RelativeMove(5, -5)
	),

	MUSA_POINT_PORT_SARIM(
		PortLocation.MUSA_POINT,
		PortLocation.PORT_SARIM,
		new RelativeMove(5, 0),
		new RelativeMove(5, -5),
		new RelativeMove(0, -35),
		new RelativeMove(14, -14),
		new RelativeMove(11, 0),
		new RelativeMove(56, 56)
	),

	MUSA_POINT_RUINS_OF_UNKAH(
		PortLocation.MUSA_POINT,
		PortLocation.RUINS_OF_UNKAH,
		new RelativeMove(5, 0),
		new RelativeMove(5, -5),
		new RelativeMove(0, -35),
		new RelativeMove(103, -103),
		new RelativeMove(0, -20),
		new RelativeMove(45, -45),
		new RelativeMove(0, -117),
		new RelativeMove(20, 0)
	),

	PANDEMONIUM_PORT_KHAZARD(
		PortLocation.PANDEMONIUM,
		PortLocation.PORT_KHAZARD,
		new RelativeMove(0, 20),
		new RelativeMove(-103, 103),
		new RelativeMove(0, 35),
		new RelativeMove(-5, 5),
		new RelativeMove(0, 10),
		new RelativeMove(-35, 35),
		new RelativeMove(-20, 0),
		new RelativeMove(-25, 25),
		new RelativeMove(-85, 0),
		new RelativeMove(-41, 41),
		new RelativeMove(-40, 0),
		new RelativeMove(-40, -40),
		new RelativeMove(0, -15),
		new RelativeMove(5, -5)

	),

	PANDEMONIUM_RUINS_OF_UNKAH(
		PortLocation.PANDEMONIUM,
		PortLocation.RUINS_OF_UNKAH,
		new RelativeMove(0, -20),
		new RelativeMove(45, -45),
		new RelativeMove(0, -98),
		new RelativeMove(20, 0)
	),

	PORT_KHAZARD_PORT_SARIM(
		PortLocation.PORT_KHAZARD,
		PortLocation.PORT_SARIM,
		new RelativeMove(0, 60),
		new RelativeMove(35, 35),
		new RelativeMove(45, 0),
		new RelativeMove(41, -41),
		new RelativeMove(85, 0),
		new RelativeMove(25, -25),
		new RelativeMove(20, 0),
		new RelativeMove(35, -35),
		new RelativeMove(0, -44),
		new RelativeMove(12, -12),
		new RelativeMove(22, 0),
		new RelativeMove(48, 48)
	),

	PORT_KHAZARD_RUINS_OF_UNKAH(
		PortLocation.PORT_KHAZARD,
		PortLocation.RUINS_OF_UNKAH,
		new RelativeMove(0, 25),
		new RelativeMove(-20, 20),
		new RelativeMove(50, 50),
		new RelativeMove(45, 0),
		new RelativeMove(41, -41),
		new RelativeMove(85, 0),
		new RelativeMove(25, -25),
		new RelativeMove(20, 0),
		new RelativeMove(35, -35),
		new RelativeMove(0, -10),
		new RelativeMove(5, -5),
		new RelativeMove(0, -35),
		new RelativeMove(103, -103),
		new RelativeMove(0, -20),
		new RelativeMove(0, -20),
		new RelativeMove(45, -45),
		new RelativeMove(0, -98),
		new RelativeMove(20, 0)
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
		new RelativeMove(0, -75),
		new RelativeMove(44, -44)
	),
	PORT_SARIM_ARDOUGNE(
		PortLocation.PORT_SARIM,
		PortLocation.ARDOUGNE,
		new RelativeMove(0, -43),
		new RelativeMove(-50, -50),
		new RelativeMove(-22, 0),
		new RelativeMove(-8, 8),
		new RelativeMove(0, 40),
		new RelativeMove(-50, 50),
		new RelativeMove(-48, 0),
		new RelativeMove(-10, 10),
		new RelativeMove(-40, 0),
		new RelativeMove(-50, 50)
	),
	CATHERBY_ENTRANA(
		PortLocation.CATHERBY,
		PortLocation.ENTRANA,
		new RelativeMove(60, 0),
		new RelativeMove(16, -16),
		new RelativeMove(0, -40),
		new RelativeMove(11, -11)
	),
	CATHERBY_RUINS_OF_UNKAH(
		PortLocation.CATHERBY,
		PortLocation.RUINS_OF_UNKAH,
		new RelativeMove(0, 0)
	),
	BRIMHAVEN_ARDOUGNE(
		PortLocation.BRIMHAVEN,
		PortLocation.ARDOUGNE,
		new RelativeMove(0, 16),
		new RelativeMove(-12, 12)
	),
	PORT_KHAZARD_ENTRANA(
		PortLocation.PORT_KHAZARD,
		PortLocation.ENTRANA,
		new RelativeMove(0, 59),
		new RelativeMove(38, 38),
		new RelativeMove(66, 0),
		new RelativeMove(9, 9),
		new RelativeMove(0, 41),
		new RelativeMove(14, 14),
		new RelativeMove(63, 0),
		new RelativeMove(5, 5)
	),
	PORT_KHAZARD_CORSAIR_COVE(
		PortLocation.PORT_KHAZARD,
		PortLocation.CORSAIR_COVE,
		new RelativeMove(0, -20),
		new RelativeMove(24, -24),
		new RelativeMove(0, -175),
		new RelativeMove(-99, -99)
	),
	RUINS_OF_UNKAH_BRIMHAVEN(
		PortLocation.RUINS_OF_UNKAH,
		PortLocation.BRIMHAVEN,
		new RelativeMove(0, 0)
	),
	RUINS_OF_UNKAH_PORT_SARIM(
		PortLocation.RUINS_OF_UNKAH,
		PortLocation.PORT_SARIM,
		new RelativeMove(-20, 0),
		new RelativeMove(0, 98),
		new RelativeMove(-45, 45),
		new RelativeMove(0, 25),
		new RelativeMove(-44, 44),
		new RelativeMove(0, 80),
		new RelativeMove(22, 22)
	),
	RUINS_OF_UNKAH_CORSAIR_COVE(
		PortLocation.RUINS_OF_UNKAH,
		PortLocation.CORSAIR_COVE,
		new RelativeMove(-7, 0),
		new RelativeMove(0, -31),
		new RelativeMove(-43, -43),
		new RelativeMove(-208, 0),
		new RelativeMove(-94, 94)
	),
	PANDEMONIUM_ARDOUGNE(
		PortLocation.PANDEMONIUM,
		PortLocation.ARDOUGNE,
		new RelativeMove(0, 22),
		new RelativeMove(-105, 105),
		new RelativeMove(0, 42),
		new RelativeMove(-28, 28),
		new RelativeMove(-23, 0),
		new RelativeMove(-27, 27),
		new RelativeMove(-76, 0),
		new RelativeMove(-48, 48)
	),
	MUSA_POINT_ARDOUGNE(
		PortLocation.MUSA_POINT,
		PortLocation.ARDOUGNE,
		new RelativeMove(0, 16),
		new RelativeMove(-28, 28),
		new RelativeMove(-23, 0),
		new RelativeMove(-27, 27),
		new RelativeMove(-76, 0),
		new RelativeMove(-48, 48)
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