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
		// Sailing >= 0, used in 0 tasks
	),
	CATHERBY_BRIMHAVEN(
		PortLocation.CATHERBY,
		PortLocation.BRIMHAVEN,
		// Sailing >= 25, used in 4 tasks
		new RelativeMove(-42, -42)
	),
	BRIMHAVEN_MUSA_POINT(
		PortLocation.BRIMHAVEN,
		PortLocation.MUSA_POINT,
		// Sailing >= 25, used in 6 tasks
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
		// Sailing >= 25, used in 5 tasks
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
		// Sailing >= 30, used in 4 tasks
		new RelativeMove(0, 15),
		new RelativeMove(-7, 7),
		new RelativeMove(-26, 0),
		new RelativeMove(-33, -33)
	),

	CATHERBY_ARDOUGNE(
		PortLocation.CATHERBY,
		PortLocation.ARDOUGNE,
		// Sailing >= 28, used in 6 tasks
		new RelativeMove(-42, -42),
		new RelativeMove(0, -91),
		new RelativeMove(-16, -16)
	),

	CATHERBY_MUSA_POINT(
		PortLocation.CATHERBY,
		PortLocation.MUSA_POINT,
		// Sailing >= 20, used in 4 tasks
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
		// Sailing >= 20, used in 4 tasks
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
		// Sailing >= 30, used in 5 tasks
		new RelativeMove(-42, -42),
		new RelativeMove(0, -91),
		new RelativeMove(-50, -50),
		new RelativeMove(-13, 0),
		new RelativeMove(-3, -3)
	),

	CATHERBY_PORT_SARIM(
		PortLocation.CATHERBY,
		PortLocation.PORT_SARIM,
		// Sailing >= 20, used in 6 tasks
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
		// Sailing >= 30, used in 5 tasks
		new RelativeMove(5, 0),
		new RelativeMove(7, -7),
		new RelativeMove(0, -60),
		new RelativeMove(6, -6)
	),

	ARDOUGNE_RUINS_OF_UNKAH(
		PortLocation.ARDOUGNE,
		PortLocation.RUINS_OF_UNKAH,
		// Sailing >= 48, used in 2 tasks
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
		// Sailing >= 36, used in 2 tasks
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
		// Sailing >= 10, used in 6 tasks
		new RelativeMove(5, 0),
		new RelativeMove(5, -5),
		new RelativeMove(0, -35),
		new RelativeMove(103, -103),
		new RelativeMove(0, -20)
	),

	MUSA_POINT_PORT_KHAZARD(
		PortLocation.MUSA_POINT,
		PortLocation.PORT_KHAZARD,
		// Sailing >= 30, used in 1 task
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
		// Sailing >= 10, used in 6 tasks
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
		// Sailing >= 48, used in 0 tasks
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
		// Sailing >= 30, used in 2 tasks
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
		// Sailing >= 48, used in 4 tasks
		new RelativeMove(0, -20),
		new RelativeMove(45, -45),
		new RelativeMove(0, -98),
		new RelativeMove(20, 0)
	),

	PORT_KHAZARD_PORT_SARIM(
		PortLocation.PORT_KHAZARD,
		PortLocation.PORT_SARIM,
		// Sailing >= 30, used in 5 tasks
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
		// Sailing >= 48, used in 3 tasks
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
		// Sailing >= 48, used in 3 tasks
		new RelativeMove(-7, 0),
		new RelativeMove(0, -31),
		new RelativeMove(-43, -43),
		new RelativeMove(0, -110),
		new RelativeMove(-22, -22),
		new RelativeMove(0, -207),
		new RelativeMove(44, -44)
	),
	PORT_SARIM_PANDEMONIUM(
		PortLocation.PORT_SARIM,
		PortLocation.PANDEMONIUM,
		// Sailing >= 1, used in 6 tasks
		new RelativeMove(0, -43),
		new RelativeMove(-22, -22),
		new RelativeMove(0, -75),
		new RelativeMove(44, -44)
	),
	PORT_SARIM_ARDOUGNE(
		PortLocation.PORT_SARIM,
		PortLocation.ARDOUGNE,
		// Sailing >= 28, used in 3 tasks
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
		// Sailing >= 36, used in 2 tasks
		new RelativeMove(60, 0),
		new RelativeMove(16, -16),
		new RelativeMove(0, -40),
		new RelativeMove(11, -11)
	),
	CATHERBY_RUINS_OF_UNKAH(
		PortLocation.CATHERBY,
		PortLocation.RUINS_OF_UNKAH,
		// Sailing >= 48, used in 1 task
		new RelativeMove(62, 0),
		new RelativeMove(20, -20),
		new RelativeMove(0, -42),
		new RelativeMove(4, -4),
		new RelativeMove(0, -105),
		new RelativeMove(60, -60),
		new RelativeMove(18, 0),
		new RelativeMove(14, -14),
		new RelativeMove(0, -51),
		new RelativeMove(153, -153),
		new RelativeMove(0, -131),
		new RelativeMove(5, -5),
		new RelativeMove(10, 0)
	),
	BRIMHAVEN_ARDOUGNE(
		PortLocation.BRIMHAVEN,
		PortLocation.ARDOUGNE,
		// Sailing >= 28, used in 4 tasks
		new RelativeMove(0, 16),
		new RelativeMove(-12, 12)
	),
	PORT_KHAZARD_ENTRANA(
		PortLocation.PORT_KHAZARD,
		PortLocation.ENTRANA,
		// Sailing >= 36, used in 1 task
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
		// Sailing >= 40, used in 6 tasks
		new RelativeMove(0, -20),
		new RelativeMove(24, -24),
		new RelativeMove(0, -175),
		new RelativeMove(-99, -99)
	),
	RUINS_OF_UNKAH_BRIMHAVEN(
		PortLocation.RUINS_OF_UNKAH,
		PortLocation.BRIMHAVEN,
		// Sailing >= 48, used in 1 task
		new RelativeMove(-20, 0),
		new RelativeMove(0, 98),
		new RelativeMove(-45, 45),
		new RelativeMove(0, 25),
		new RelativeMove(-110, 110),
		new RelativeMove(0, 60),
		new RelativeMove(-35, 35),
		new RelativeMove(-20, 0),
		new RelativeMove(-25, 25),
		new RelativeMove(-85, 0),
		new RelativeMove(-20, 20),
		new RelativeMove(-24, 0),
		new RelativeMove(-5, -5)
	),
	RUINS_OF_UNKAH_PORT_SARIM(
		PortLocation.RUINS_OF_UNKAH,
		PortLocation.PORT_SARIM,
		// Sailing >= 48, used in 0 tasks
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
		// Sailing >= 48, used in 0 tasks
		new RelativeMove(-7, 0),
		new RelativeMove(0, -31),
		new RelativeMove(-43, -43),
		new RelativeMove(-208, 0),
		new RelativeMove(-94, 94)
	),
	PANDEMONIUM_ARDOUGNE(
		PortLocation.PANDEMONIUM,
		PortLocation.ARDOUGNE,
		// Sailing >= 28, used in 1 task
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
		// Sailing >= 28, used in 2 tasks
		new RelativeMove(0, 16),
		new RelativeMove(-28, 28),
		new RelativeMove(-23, 0),
		new RelativeMove(-27, 27),
		new RelativeMove(-76, 0),
		new RelativeMove(-48, 48)
	),
	BRIMHAVEN_PORT_SARIM(
		PortLocation.BRIMHAVEN,
		PortLocation.PORT_SARIM,
		// Sailing >= 25, used in 3 tasks
		new RelativeMove(0, 7),
		new RelativeMove(8, 8),
		new RelativeMove(24, 0),
		new RelativeMove(26, -26),
		new RelativeMove(88, 0),
		new RelativeMove(65, -65),
		new RelativeMove(0, -40),
		new RelativeMove(18, -18),
		new RelativeMove(39, 0),
		new RelativeMove(41, 41)
	),
	PORT_SARIM_RUINS_OF_UNKAH(
		PortLocation.PORT_SARIM,
		PortLocation.RUINS_OF_UNKAH,
		// Sailing >= 48, used in 4 tasks
		new RelativeMove(0, -50),
		new RelativeMove(-25, -25),
		new RelativeMove(0, -68),
		new RelativeMove(95, -95),
		new RelativeMove(0, -127),
		new RelativeMove(5, -5)
	),
	PORT_PISCARILIUS_PORT_SARIM(
		PortLocation.PORT_PISCARILIUS,
		PortLocation.PORT_SARIM,
		// Sailing >= 15, used in 3 tasks
		new RelativeMove(0, -27),
		new RelativeMove(150, -150),
		new RelativeMove(0, -637),
		new RelativeMove(254, -254),
		new RelativeMove(253, 0),
		new RelativeMove(228, 228),
		new RelativeMove(0, 271),
		new RelativeMove(-41, 41),
		new RelativeMove(0, 69),
		new RelativeMove(35, 35),
		new RelativeMove(47, 0),
		new RelativeMove(39, -39),
		new RelativeMove(93, 0),
		new RelativeMove(64, -64),
		new RelativeMove(0, -44),
		new RelativeMove(11, -11),
		new RelativeMove(32, 0),
		new RelativeMove(56, 56)
	),
	PORT_SARIM_VOID_KNIGHTS_OUTPOST(
		PortLocation.PORT_SARIM,
		PortLocation.VOID_KNIGHT
		// Sailing >= 50, used in 1 task
	),
	CORSAIR_COVE_PANDEMONIUM(
		PortLocation.CORSAIR_COVE,
		PortLocation.PANDEMONIUM,
		// Sailing >= 40, used in 4 tasks
		// Can you safely navigate around The Storm Tempor?
		// I don't think so. Routing improvement if you can
		new RelativeMove(105, 0),
		new RelativeMove(25, 25),
		new RelativeMove(0, 245),
		new RelativeMove(-29, 29),
		new RelativeMove(0, 77),
		new RelativeMove(34, 34),
		new RelativeMove(57, 0),
		new RelativeMove(38, -38),
		new RelativeMove(75, 0),
		new RelativeMove(30, -30),
		new RelativeMove(10, 0),
		new RelativeMove(36, -36),
		new RelativeMove(0, -40),
		new RelativeMove(111, -111)
	),
	CATHERBY_VOID_KNIGHTS_OUTPOST(
		PortLocation.CATHERBY,
		PortLocation.VOID_KNIGHT
		// Sailing >= 50, used in 1 task
	),
	BRIMHAVEN_CORSAIR_COVE(
		PortLocation.BRIMHAVEN,
		PortLocation.CORSAIR_COVE,
		// Sailing >= 40, used in 5 tasks
		new RelativeMove(0, 17),
		new RelativeMove(-5, 5),
		new RelativeMove(-30, 0),
		new RelativeMove(-31, -31),
		new RelativeMove(0, -80),
		new RelativeMove(30, -30),
		new RelativeMove(00, -200),
		new RelativeMove(-68, -68)
	),
	BRIMHAVEN_RED_ROCK(
		PortLocation.BRIMHAVEN,
		PortLocation.RED_ROCK
		// Sailing >= 25, used in 2 tasks
	),
	BRIMHAVEN_SUMMER_SHORE(
		PortLocation.BRIMHAVEN,
		PortLocation.SUMMER_SHORE
		// Sailing >= 45, used in 1 task
	),
	ARDOUGNE_PORT_TYRAS(
		PortLocation.ARDOUGNE,
		PortLocation.PORT_TYRAS,
		// Sailing >= 66, used in 5 tasks
		new RelativeMove(7, 0),
		new RelativeMove(10, -10),
		new RelativeMove(0, -120),
		new RelativeMove(25, -25),
		new RelativeMove(0, -230),
		new RelativeMove(-70, -70),
		new RelativeMove(0, -100),
		new RelativeMove(-30, -30),
		new RelativeMove(-170, 0),
		new RelativeMove(-70, -70),
		new RelativeMove(-130, 0),
		new RelativeMove(-180, 180),
		new RelativeMove(0, 190),
		new RelativeMove(70, 70),
		new RelativeMove(0, 62)
	),
	ARDOUGNE_PORT_PISCARILIUS(
		PortLocation.ARDOUGNE,
		PortLocation.PORT_PISCARILIUS,
		// Sailing >= 28, used in 2 tasks
		new RelativeMove(8, 0),
		new RelativeMove(10, -10),
		new RelativeMove(0, -106),
		new RelativeMove(31, -31),
		new RelativeMove(0, -223),
		new RelativeMove(-72, -72),
		new RelativeMove(0, -70),
		new RelativeMove(-130, -130),
		new RelativeMove(-222, 0),
		new RelativeMove(-306, 306),
		new RelativeMove(0, 584),
		new RelativeMove(-142, 142)
	),
	ARDOUGNE_RED_ROCK(
		PortLocation.ARDOUGNE,
		PortLocation.RED_ROCK
		// Sailing >= 28, used in 2 tasks
	),
	CIVITAS_ILLA_FORTIS_PORT_KHAZARD(
		PortLocation.CIVITAS_ILLA_FORTIS,
		PortLocation.PORT_KHAZARD,
		// Sailing >= 38, used in 2 tasks
		new RelativeMove(-4, 0),
		new RelativeMove(-6, 6),
		new RelativeMove(0, 192),
		new RelativeMove(137, 137),
		new RelativeMove(0, 118),
		new RelativeMove(-54, 54)
	),
	CORSAIR_COVE_VOID_KNIGHTS_OUTPOST(
		PortLocation.CORSAIR_COVE,
		PortLocation.VOID_KNIGHT,
		// Sailing >= 50, used in 3 tasks
		new RelativeMove(32, 0),
		new RelativeMove(33, -33)
	),
	CORSAIR_COVE_PORT_TYRAS(
		PortLocation.CORSAIR_COVE,
		PortLocation.PORT_TYRAS,
		// Sailing >= 66, used in 3 tasks
		new RelativeMove(0, -102),
		new RelativeMove(-64, -64),
		new RelativeMove(-142, 0),
		new RelativeMove(-15, 15),
		new RelativeMove(-100, 0),
		new RelativeMove(-28, -28),
		new RelativeMove(-49, 0),
		new RelativeMove(-116, 116),
		new RelativeMove(0, 205),
		new RelativeMove(58, 58),
		new RelativeMove(0, 49),
		new RelativeMove(11, 11)
	),
	CORSAIR_COVE_PORT_PISCARILIUS(
		PortLocation.CORSAIR_COVE,
		PortLocation.PORT_PISCARILIUS,
		// Sailing >= 40, used in 2 tasks
		new RelativeMove(0, -99),
		new RelativeMove(-66, -66),
		new RelativeMove(-152, 0),
		new RelativeMove(-18, 18),
		new RelativeMove(-134, 0),
		new RelativeMove(-229, 229),
		new RelativeMove(0, 579),
		new RelativeMove(-142, 142)
	),
	CAIRN_ISLE_CORSAIR_COVE(
		PortLocation.CAIRN_ISLE,
		PortLocation.CORSAIR_COVE,
		// Sailing >= 42, used in 2 tasks
		new RelativeMove(-13, 0),
		new RelativeMove(-108, -108)
	),
	RED_ROCK_RUINS_OF_UNKAH(
		PortLocation.RED_ROCK,
		PortLocation.RUINS_OF_UNKAH
		// Sailing >= 48, used in 3 tasks
	),
	RUINS_OF_UNKAH_VOID_KNIGHTS_OUTPOST(
		PortLocation.RUINS_OF_UNKAH,
		PortLocation.VOID_KNIGHT,
		// Sailing >= 50, used in 2 tasks
		new RelativeMove(-12, -12),
		new RelativeMove(0, -23),
		new RelativeMove(-33, -33),
		new RelativeMove(-217, 0),
		new RelativeMove(-40, -40),
		new RelativeMove(0, -24),
		new RelativeMove(-10, -10)
	),
	RED_ROCK_VOID_KNIGHTS_OUTPOST(
		PortLocation.RED_ROCK,
		PortLocation.VOID_KNIGHT
		// Sailing >= 50, used in 2 tasks
	),
	SUMMER_SHORE_VOID_KNIGHTS_OUTPOST(
		PortLocation.SUMMER_SHORE,
		PortLocation.VOID_KNIGHT,
		// Sailing >= 50, used in 2 tasks
		new RelativeMove(-160, 0),
		new RelativeMove(-316, 316)
	),
	DEEPFIN_POINT_VOID_KNIGHTS_OUTPOST(
		PortLocation.DEEPFIN_POINT,
		PortLocation.VOID_KNIGHT,
		// Sailing >= 67, used in 2 tasks
		new RelativeMove(0, -7),
		new RelativeMove(9, -9),
		new RelativeMove(49, 0),
		new RelativeMove(23, 23),
		new RelativeMove(287, 0),
		new RelativeMove(76, -76)
	),
	LANDS_END_PRIFDDINAS(
		PortLocation.LANDS_END,
		PortLocation.PRIFDDINAS
		// Sailing >= 70, used in 1 task
	),
	LANDS_END_PISCATORIS(
		PortLocation.LANDS_END,
		PortLocation.PISCATORIS,
		// Sailing >= 75, used in 2 tasks
		new RelativeMove(0, -9),
		new RelativeMove(15, -15),
		new RelativeMove(267, 0),
		new RelativeMove(18, 18),
		new RelativeMove(93, 0),
		new RelativeMove(254, 254),
		new RelativeMove(99, 0),
		new RelativeMove(36, 36)
	),
	CORSAIR_COVE_LANDS_END(
		PortLocation.CORSAIR_COVE,
		PortLocation.LANDS_END,
		// Sailing >= 40, used in 1 task
		new RelativeMove(0, -78),
		new RelativeMove(-81, -81),
		new RelativeMove(-324, 0),
		new RelativeMove(-185, 185),
		new RelativeMove(0, 449),
		new RelativeMove(-72, 72),
		new RelativeMove(-406, 0),
		new RelativeMove(-7, 7)
	),
	LANDS_END_PORT_PISCARILIUS(
		PortLocation.LANDS_END,
		PortLocation.PORT_PISCARILIUS,
		// Sailing >= 15, used in 3 tasks
		new RelativeMove(310, 0),
		new RelativeMove(73, 73),
		new RelativeMove(0, 130),
		new RelativeMove(-50, 50)
	),
	PORT_PISCARILIUS_PORT_ROBERTS(
		PortLocation.PORT_PISCARILIUS,
		PortLocation.PORT_ROBERTS,
		// Sailing >= 50, used in 3 tasks
		new RelativeMove(0, -18),
		new RelativeMove(69, -69),
		new RelativeMove(0, -214),
		new RelativeMove(-56, -56)
	),
	PISCATORIS_PORT_PISCARILIUS(
		PortLocation.PISCATORIS,
		PortLocation.PORT_PISCARILIUS,
		// Sailing >= 75, used in 3 tasks
		new RelativeMove(-10, 0),
		new RelativeMove(-13, -13),
		new RelativeMove(-84, 0),
		new RelativeMove(-45, 45),
		new RelativeMove(-172, 0),
		new RelativeMove(-40, -40)
	),
	HOSIDIUS_PORT_PISCARILIUS(
		PortLocation.HOSIDIUS,
		PortLocation.PORT_PISCARILIUS,
		// Sailing >= 15, used in 2 tasks
		new RelativeMove(0, -25),
		new RelativeMove(21, -21),
		new RelativeMove(131, 0),
		new RelativeMove(38, 38),
		new RelativeMove(0, 141),
		new RelativeMove(-71, 71)
	),
	LUNAR_ISLE_PORT_PISCARILIUS(
		PortLocation.LUNAR_ISLE,
		PortLocation.PORT_PISCARILIUS,
		// Sailing >= 76, used in 5 tasks
		new RelativeMove(12, 0),
		new RelativeMove(5, -5),
		new RelativeMove(0, -120),
		new RelativeMove(-20, -20),
		new RelativeMove(-180, 0),
		new RelativeMove(-55, -55)
	),
	PORT_PISCARILIUS_PORT_TYRAS(
		PortLocation.PORT_PISCARILIUS,
		PortLocation.PORT_TYRAS,
		// Sailing >= 66, used in 3 tasks
		new RelativeMove(37, 0),
		new RelativeMove(50, -50),
		new RelativeMove(0, -381),
		new RelativeMove(94, -94),
		new RelativeMove(34, 0),
		new RelativeMove(41, -41)
	),
	PORT_PISCARILIUS_RELLEKKA(
		PortLocation.PORT_PISCARILIUS,
		PortLocation.RELLEKKA,
		// Sailing >= 62, used in 3 tasks
		new RelativeMove(17, 0),
		new RelativeMove(83, 83),
		new RelativeMove(488, 0),
		new RelativeMove(55, -55)
	),
	PORT_PISCARILIUS_PRIFDDINAS(
		PortLocation.PORT_PISCARILIUS,
		PortLocation.PRIFDDINAS,
		// Sailing >= 70, used in 2 tasks
		new RelativeMove(113, 0),
		new RelativeMove(100, -100),
		new RelativeMove(0, -202),
		new RelativeMove(60, -60)
	),
	ALDARIN_CIVITAS_ILLA_FORTIS(
		PortLocation.ALDARIN,
		PortLocation.CIVITAS_ILLA_FORTIS,
		// Sailing >= 46, used in 3 tasks
		new RelativeMove(0, 6),
		new RelativeMove(3, 3),
		new RelativeMove(3, 0),
		new RelativeMove(117, -117),
		new RelativeMove(135, 0),
		new RelativeMove(47, 47),
		new RelativeMove(18, 0),
		new RelativeMove(37, 37),
		new RelativeMove(0, 61),
		new RelativeMove(66, 66),
		new RelativeMove(0, 43),
		new RelativeMove(-22, 22),
		new RelativeMove(0, 40),
		new RelativeMove(-29, 29),
		new RelativeMove(-59, 0),
		new RelativeMove(-9, -9),
		new RelativeMove(0, -54)
	),
	CIVITAS_ILLA_FORTIS_PORT_PISCARILIUS(
		PortLocation.CIVITAS_ILLA_FORTIS,
		PortLocation.PORT_PISCARILIUS
		// Sailing >= 38, used in 3 tasks
	),
	CIVITAS_ILLA_FORTIS_PORT_ROBERTS(
		PortLocation.CIVITAS_ILLA_FORTIS,
		PortLocation.PORT_ROBERTS,
		// Sailing >= 50, used in 3 tasks
		new RelativeMove(-8, 8),
		new RelativeMove(0, 103),
		new RelativeMove(53, 53)
	),
	CIVITAS_ILLA_FORTIS_DEEPFIN_POINT(
		PortLocation.CIVITAS_ILLA_FORTIS,
		PortLocation.DEEPFIN_POINT,
		// Sailing >= 67, used in 2 tasks
		new RelativeMove(-8, 8),
		new RelativeMove(0, 41),
		new RelativeMove(9, 9),
		new RelativeMove(63, 0),
		new RelativeMove(83, -83),
		new RelativeMove(0, -179),
		new RelativeMove(-20, -20),
		new RelativeMove(0, -143),
		new RelativeMove(25, -25)
	),
	CIVITAS_ILLA_FORTIS_PRIFDDINAS(
		PortLocation.CIVITAS_ILLA_FORTIS,
		PortLocation.PRIFDDINAS,
		// Sailing >= 70, used in 4 tasks
		new RelativeMove(-5, 0),
		new RelativeMove(-5, 5),
		new RelativeMove(0, 48),
		new RelativeMove(10, 10),
		new RelativeMove(64, 0),
		new RelativeMove(9, -9),
		new RelativeMove(40, 0),
		new RelativeMove(45, 45),
		new RelativeMove(170, 0),
		new RelativeMove(61, 61)
	),
	CIVITAS_ILLA_FORTIS_SUNSET_COAST(
		PortLocation.CIVITAS_ILLA_FORTIS,
		PortLocation.SUNSET_COAST,
		// Sailing >= 44, used in 2 tasks
		new RelativeMove(-8, 8),
		new RelativeMove(0, 41),
		new RelativeMove(10, 10),
		new RelativeMove(62, 0),
		new RelativeMove(59, -59),
		new RelativeMove(0, -62),
		new RelativeMove(-78, -78),
		new RelativeMove(0, -53),
		new RelativeMove(-74, -74),
		new RelativeMove(-175, 0),
		new RelativeMove(-59, 59)
	),
	PORT_ROBERTS_PORT_TYRAS(
		PortLocation.PORT_ROBERTS,
		PortLocation.PORT_TYRAS,
		// Sailing >= 66, used in 2 tasks
		new RelativeMove(0, -27),
		new RelativeMove(11, -11),
		new RelativeMove(111, 0),
		new RelativeMove(154, -154)
	),
	DEEPFIN_POINT_PORT_TYRAS(
		PortLocation.DEEPFIN_POINT,
		PortLocation.PORT_TYRAS,
		// Sailing >= 67, used in 3 tasks
		new RelativeMove(-27, 27),
		new RelativeMove(0, 48),
		new RelativeMove(145, 145),
		new RelativeMove(25, 0),
		new RelativeMove(67, 67),
		new RelativeMove(0, 54),
		new RelativeMove(8, 8)
	),
	PORT_TYRAS_PRIFDDINAS(
		PortLocation.PORT_TYRAS,
		PortLocation.PRIFDDINAS,
		// Sailing >= 70, used in 4 tasks
		new RelativeMove(-8, 8),
		new RelativeMove(0, 12),
		new RelativeMove(18, 18),
		new RelativeMove(0, 158)
	),
	LANDS_END_PORT_TYRAS(
		PortLocation.LANDS_END,
		PortLocation.PORT_TYRAS,
		// Sailing >= 66, used in 2 tasks
		new RelativeMove(0, -14),
		new RelativeMove(25, -25),
		new RelativeMove(157, 0),
		new RelativeMove(99, -99),
		new RelativeMove(186, 0),
		new RelativeMove(152, -152)
	),
	ALDARIN_PORT_TYRAS(
		PortLocation.ALDARIN,
		PortLocation.PORT_TYRAS
		// Sailing >= 66, used in 1 task
	),
	PORT_ROBERTS_PRIFDDINAS(
		PortLocation.PORT_ROBERTS,
		PortLocation.PRIFDDINAS,
		// Sailing >= 70, used in 3 tasks
		new RelativeMove(0, 16),
		new RelativeMove(53, 53),
		new RelativeMove(154, 0),
		new RelativeMove(58, -58)
	),
	ARDOUGNE_PRIFDDINAS(
		PortLocation.ARDOUGNE,
		PortLocation.PRIFDDINAS,
		// Sailing >= 70, used in 3 tasks
		new RelativeMove(8, 0),
		new RelativeMove(10, -10),
		new RelativeMove(0, -106),
		new RelativeMove(31, -31),
		new RelativeMove(0, -236),
		new RelativeMove(-69, -69),
		new RelativeMove(0, -75),
		new RelativeMove(-49, -49),
		new RelativeMove(-239, 0),
		new RelativeMove(-12, 12),
		new RelativeMove(-98, 0),
		new RelativeMove(-14, -14),
		new RelativeMove(-58, 0),
		new RelativeMove(-105, 105),
		new RelativeMove(0, 196),
		new RelativeMove(-38, 38),
		new RelativeMove(0, 187),
		new RelativeMove(113, 113)
	),
	DEEPFIN_POINT_PRIFDDINAS(
		PortLocation.DEEPFIN_POINT,
		PortLocation.PRIFDDINAS,
		// Sailing >= 70, used in 2 tasks
		new RelativeMove(-37, 37),
		new RelativeMove(0, 115),
		new RelativeMove(148, 148),
		new RelativeMove(0, 158),
		new RelativeMove(109, 109)
	),
	ALDARIN_PRIFDDINAS(
		PortLocation.ALDARIN,
		PortLocation.PRIFDDINAS,
		// Sailing >= 70, used in 2 tasks
		new RelativeMove(17, 0),
		new RelativeMove(100, -100),
		new RelativeMove(166, 0),
		new RelativeMove(46, 46),
		new RelativeMove(119, 0),
		new RelativeMove(126, 126),
		new RelativeMove(0, 158),
		new RelativeMove(112, 112)
	),
	LUNAR_ISLE_PRIFDDINAS(
		PortLocation.LUNAR_ISLE,
		PortLocation.PRIFDDINAS,
		// Sailing >= 76, used in 4 tasks
		new RelativeMove(14, 0),
		new RelativeMove(15, -15),
		new RelativeMove(0, -15),
		new RelativeMove(-19, -19),
		new RelativeMove(0, -122),
		new RelativeMove(-10, -10),
		new RelativeMove(0, -54),
		new RelativeMove(-97, -97),
		new RelativeMove(0, -164),
		new RelativeMove(70, -70)
	),
	ETCETERIA_RELLEKKA(
		PortLocation.ETCETERIA,
		PortLocation.RELLEKKA,
		// Sailing >= 65, used in 6 tasks
		new RelativeMove(0, -10),
		new RelativeMove(18, -18)
	),
	NEITIZNOT_RELLEKKA(
		PortLocation.NEITIZNOT,
		PortLocation.RELLEKKA,
		// Sailing >= 68, used in 3 tasks
		new RelativeMove(0, -15),
		new RelativeMove(50, -50),
		new RelativeMove(131, 0),
		new RelativeMove(12, -12),
		new RelativeMove(109, 0),
		new RelativeMove(6, 6)
	),
	RELLEKKA_SUNSET_COAST(
		PortLocation.RELLEKKA,
		PortLocation.SUNSET_COAST,
		// Sailing >= 62, used in 3 tasks
		new RelativeMove(-137, 0),
		new RelativeMove(-51, 51),
		new RelativeMove(-388, 0),
		new RelativeMove(-96, -96),
		new RelativeMove(0, -514),
		new RelativeMove(-142, -142),
		new RelativeMove(0, -51),
		new RelativeMove(-79, -79),
		new RelativeMove(-174, 0),
		new RelativeMove(-57, 57)
	),
	PORT_ROBERTS_RELLEKKA(
		PortLocation.PORT_ROBERTS,
		PortLocation.RELLEKKA,
		// Sailing >= 62, used in 2 tasks
		new RelativeMove(0, 19),
		new RelativeMove(193, 193),
		new RelativeMove(0, 37),
		new RelativeMove(200, 200),
		new RelativeMove(197, 0),
		new RelativeMove(47, -47)
	),
	PISCATORIS_RELLEKKA(
		PortLocation.PISCATORIS,
		PortLocation.RELLEKKA,
		// Sailing >= 75, used in 2 tasks
		new RelativeMove(0, 15),
		new RelativeMove(12, 12),
		new RelativeMove(168, 0),
		new RelativeMove(6, -6)
	),
	JATIZO_RELLEKKA(
		PortLocation.JATIZO,
		PortLocation.RELLEKKA,
		// Sailing >= 68, used in 2 tasks
		new RelativeMove(67, -67)
	),
	PORT_TYRAS_RELLEKKA(
		PortLocation.PORT_TYRAS,
		PortLocation.RELLEKKA
		// Sailing >= 66, used in 1 task
	),
	ETCETERIA_JATIZO(
		PortLocation.ETCETERIA,
		PortLocation.JATIZO,
		// Sailing >= 68, used in 3 tasks
		new RelativeMove(0, -12),
		new RelativeMove(-29, -29),
		new RelativeMove(-86, 0),
		new RelativeMove(-19, -19)
	),
	ETCETERIA_PORT_ROBERTS(
		PortLocation.ETCETERIA,
		PortLocation.PORT_ROBERTS,
		// Sailing >= 65, used in 3 tasks
		new RelativeMove(0, -11),
		new RelativeMove(-32, -32),
		new RelativeMove(-94, 0),
		new RelativeMove(-33, -33),
		new RelativeMove(-402, 0),
		new RelativeMove(-80, -80),
		new RelativeMove(0, -243),
		new RelativeMove(-113, -113)
	),
	ETCETERIA_PORT_PISCARILIUS(
		PortLocation.ETCETERIA,
		PortLocation.PORT_PISCARILIUS,
		// Sailing >= 65, used in 2 tasks
		new RelativeMove(0, -18),
		new RelativeMove(-26, -26),
		new RelativeMove(-101, 0),
		new RelativeMove(-34, -34),
		new RelativeMove(-514, 0),
		new RelativeMove(-77, -77)
	),
	ETCETERIA_NEITIZNOT(
		PortLocation.ETCETERIA,
		PortLocation.NEITIZNOT,
		// Sailing >= 68, used in 2 tasks
		new RelativeMove(0, -22),
		new RelativeMove(-22, -22),
		new RelativeMove(-122, 0),
		new RelativeMove(-27, -27),
		new RelativeMove(-133, 0),
		new RelativeMove(-6, 6)
	),
	ETCETERIA_SUNSET_COAST(
		PortLocation.ETCETERIA,
		PortLocation.SUNSET_COAST,
		// Sailing >= 65, used in 2 tasks
		new RelativeMove(0, -26),
		new RelativeMove(-18, -18),
		new RelativeMove(-115, 0),
		new RelativeMove(-33, -33),
		new RelativeMove(-393, 0),
		new RelativeMove(-107, -107),
		new RelativeMove(0, -507),
		new RelativeMove(-131, -131),
		new RelativeMove(0, -59),
		new RelativeMove(-79, -79),
		new RelativeMove(-168, 0),
		new RelativeMove(-62, 62)
	),
	ETCETERIA_PISCATORIS(
		PortLocation.ETCETERIA,
		PortLocation.PISCATORIS,
		// Sailing >= 75, used in 2 tasks
		new RelativeMove(0, -20),
		new RelativeMove(-26, -26),
		new RelativeMove(-100, 0),
		new RelativeMove(-51, -51),
		new RelativeMove(-103, 0),
		new RelativeMove(-32, -32)
	),
	ETCETERIA_HOSIDIUS(
		PortLocation.ETCETERIA,
		PortLocation.HOSIDIUS
		// Sailing >= 65, used in 1 task
	),
	LUNAR_ISLE_PISCATORIS(
		PortLocation.LUNAR_ISLE,
		PortLocation.PISCATORIS,
		// Sailing >= 76, used in 3 tasks
		new RelativeMove(30, 0),
		new RelativeMove(116, -116)
	),
	DEEPFIN_POINT_LUNAR_ISLE(
		PortLocation.DEEPFIN_POINT,
		PortLocation.LUNAR_ISLE,
		// Sailing >= 76, used in 2 tasks
		new RelativeMove(-31, 31),
		new RelativeMove(0, 134),
		new RelativeMove(122, 122),
		new RelativeMove(0, 473),
		new RelativeMove(159, 159),
		new RelativeMove(0, 197),
		new RelativeMove(-13, 13)
	),
	LUNAR_ISLE_PORT_ROBERTS(
		PortLocation.LUNAR_ISLE,
		PortLocation.PORT_ROBERTS,
		// Sailing >= 76, used in 2 tasks
		new RelativeMove(3, 0),
		new RelativeMove(14, -14),
		new RelativeMove(0, -187),
		new RelativeMove(-124, -124),
		new RelativeMove(0, -45),
		new RelativeMove(-192, -192)
	),
	CIVITAS_ILLA_FORTIS_LUNAR_ISLE(
		PortLocation.CIVITAS_ILLA_FORTIS,
		PortLocation.LUNAR_ISLE,
		// Sailing >= 76, used in 2 tasks
		new RelativeMove(-9, 9),
		new RelativeMove(0, 73),
		new RelativeMove(285, 285),
		new RelativeMove(0, 32),
		new RelativeMove(127, 127),
		new RelativeMove(0, 200),
		new RelativeMove(-11, 11)
	),
	LUNAR_ISLE_RED_ROCK(
		PortLocation.LUNAR_ISLE,
		PortLocation.RED_ROCK
		// Sailing >= 76, used in 2 tasks
	),
	ALDARIN_LUNAR_ISLE(
		PortLocation.ALDARIN,
		PortLocation.LUNAR_ISLE
		// Sailing >= 76, used in 1 task
	),
	PORT_SARIM_RELLEKKA(
		PortLocation.PORT_SARIM,
		PortLocation.RELLEKKA
		// Sailing >= 62, used in 1 task
	),
	PANDEMONIUM_CAIRN_ISLE(
		PortLocation.PANDEMONIUM,
		PortLocation.CAIRN_ISLE
		// Sailing >= 42, used in 1 task
	),
	MUSA_POINT_CORSAIR_COVE(
		PortLocation.MUSA_POINT,
		PortLocation.CORSAIR_COVE
		// Sailing >= 40, used in 1 task
	),
	MUSA_POINT_SUMMER_SHORE(
		PortLocation.MUSA_POINT,
		PortLocation.SUMMER_SHORE
		// Sailing >= 45, used in 1 task
	),
	MUSA_POINT_PORT_TYRAS(
		PortLocation.MUSA_POINT,
		PortLocation.PORT_TYRAS
		// Sailing >= 66, used in 1 task
	),
	CATHERBY_PORT_PISCARILIUS(
		PortLocation.CATHERBY,
		PortLocation.PORT_PISCARILIUS,
		// Sailing >= 20, used in 1 task
		new RelativeMove(0, -16),
		new RelativeMove(-41, -41),
		new RelativeMove(0, -60),
		new RelativeMove(-66, -66),
		new RelativeMove(0, -90),
		new RelativeMove(32, -32),
		new RelativeMove(0, -212),
		new RelativeMove(-212, -212),
		new RelativeMove(-125, 0),
		new RelativeMove(-16, 16),
		new RelativeMove(-153, 0),
		new RelativeMove(-18, -18),
		new RelativeMove(-13, 0),
		new RelativeMove(-187, 187),
		new RelativeMove(0, 638),
		new RelativeMove(-152, 152)
	),
	BRIMHAVEN_CIVITAS_ILLA_FORTIS(
		PortLocation.BRIMHAVEN,
		PortLocation.CIVITAS_ILLA_FORTIS,
		// Sailing >= 38, used in 1 task
		new RelativeMove(0, 18),
		new RelativeMove(-5, 5),
		new RelativeMove(-34, 0),
		new RelativeMove(-27, -27),
		new RelativeMove(0, -83),
		new RelativeMove(32, -32),
		new RelativeMove(0, -283),
		new RelativeMove(-148, -148),
		new RelativeMove(-202, 0),
		new RelativeMove(-17, 17),
		new RelativeMove(-141, 0),
		new RelativeMove(-332, 332),
		new RelativeMove(0, 124),
		new RelativeMove(-49, 49),
		new RelativeMove(-60, 0),
		new RelativeMove(-11, -11),
		new RelativeMove(0, -39)
	),
	ARDOUGNE_SUMMER_SHORE(
		PortLocation.ARDOUGNE,
		PortLocation.SUMMER_SHORE
		// Sailing >= 45, used in 1 task
	),
	ARDOUGNE_CIVITAS_ILLA_FORTIS(
		PortLocation.ARDOUGNE,
		PortLocation.CIVITAS_ILLA_FORTIS,
		// Sailing >= 38, used in 1 task
		new RelativeMove(7, 0),
		new RelativeMove(12, -12),
		new RelativeMove(0, -101),
		new RelativeMove(33, -33),
		new RelativeMove(0, -290),
		new RelativeMove(-141, -141),
		new RelativeMove(-218, 0),
		new RelativeMove(-14, 14),
		new RelativeMove(-139, 0),
		new RelativeMove(-14, -14),
		new RelativeMove(-16, 0),
		new RelativeMove(-275, 275),
		new RelativeMove(0, 172),
		new RelativeMove(-76, 76),
		new RelativeMove(-56, 0),
		new RelativeMove(-12, -12),
		new RelativeMove(0, -41)
	),
	ARDOUGNE_VOID_KNIGHTS_OUTPOST(
		PortLocation.ARDOUGNE,
		PortLocation.VOID_KNIGHT
		// Sailing >= 50, used in 1 task
	),
	PORT_KHAZARD_RELLEKKA(
		PortLocation.PORT_KHAZARD,
		PortLocation.RELLEKKA
		// Sailing >= 62, used in 1 task
	),
	PORT_KHAZARD_PORT_PISCARILIUS(
		PortLocation.PORT_KHAZARD,
		PortLocation.PORT_PISCARILIUS,
		// Sailing >= 30, used in 1 task
		new RelativeMove(0, -18),
		new RelativeMove(32, -32),
		new RelativeMove(0, -287),
		new RelativeMove(-142, -142),
		new RelativeMove(-225, 0),
		new RelativeMove(-12, 12),
		new RelativeMove(-102, 0),
		new RelativeMove(-24, -24),
		new RelativeMove(-26, 0),
		new RelativeMove(-199, 199),
		new RelativeMove(0, 647),
		new RelativeMove(-145, 145)
	),
	CORSAIR_COVE_CIVITAS_ILLA_FOTRIS(
		PortLocation.CORSAIR_COVE,
		PortLocation.CIVITAS_ILLA_FORTIS,
		// Sailing >= 40, used in 1 task
		new RelativeMove(0, -101),
		new RelativeMove(-57, -57),
		new RelativeMove(-348, 0),
		new RelativeMove(-277, 277),
		new RelativeMove(0, 170),
		new RelativeMove(-69, 69),
		new RelativeMove(-63, 0),
		new RelativeMove(-11, -11),
		new RelativeMove(0, -41),
		new RelativeMove(6, -6)
	),
	CORSAIR_COVE_ALDARIN(
		PortLocation.CORSAIR_COVE,
		PortLocation.ALDARIN
		// Sailing >= 46, used in 1 task
	),
	DEEPFIN_POINT_RUINS_OF_UNKAH(
		PortLocation.DEEPFIN_POINT,
		PortLocation.RUINS_OF_UNKAH
		// Sailing >= 67, used in 1 task
	),
	DEEPFIN_POINT_LANDS_END(
		PortLocation.DEEPFIN_POINT,
		PortLocation.LANDS_END
		// Sailing >= 67, used in 1 task
	),
	LANDS_END_PORT_KHAZARD(
		PortLocation.LANDS_END,
		PortLocation.PORT_KHAZARD,
		// Sailing >= 30, used in 1 task
		new RelativeMove(0, -10),
		new RelativeMove(12, -12),
		new RelativeMove(134, 0),
		new RelativeMove(116, -116),
		new RelativeMove(150, 0),
		new RelativeMove(95, -95),
		new RelativeMove(0, -335),
		new RelativeMove(163, -163),
		new RelativeMove(99, 0),
		new RelativeMove(11, 11),
		new RelativeMove(288, 0),
		new RelativeMove(135, 135),
		new RelativeMove(0, 289),
		new RelativeMove(-26, 26)
	),
	LANDS_END_LUNAR_ISLE(
		PortLocation.LANDS_END,
		PortLocation.LUNAR_ISLE,
		// Sailing >= 76, used in 2 tasks
		new RelativeMove(0, -9),
		new RelativeMove(17, -17),
		new RelativeMove(267, 0),
		new RelativeMove(118, 118),
		new RelativeMove(87, 0),
		new RelativeMove(173, 173),
		new RelativeMove(0, 196),
		new RelativeMove(-15, 15)
	),
	MUSA_POINT_PORT_PISCARILIUS(
		PortLocation.MUSA_POINT,
		PortLocation.PORT_PISCARILIUS,
		// Sailing >= 15, used in 1 task
		new RelativeMove(0, 11),
		new RelativeMove(-54, 54),
		new RelativeMove(-87, 0),
		new RelativeMove(-44, 44),
		new RelativeMove(-58, 0),
		new RelativeMove(-34, -34),
		new RelativeMove(0, -81),
		new RelativeMove(32, -32),
		new RelativeMove(0, -218),
		new RelativeMove(-214, -214),
		new RelativeMove(-65, 0),
		new RelativeMove(-60, -60),
		new RelativeMove(-133, 0),
		new RelativeMove(-258, 258),
		new RelativeMove(0, 628),
		new RelativeMove(-145, 145)
	),
	CIVITAS_ILLA_FORTIS_PORT_SARIM(
		PortLocation.CIVITAS_ILLA_FORTIS,
		PortLocation.PORT_SARIM,
		// Sailing >= 38, used in 1 task
		new RelativeMove(-8, 8),
		new RelativeMove(0, 39),
		new RelativeMove(15, 15),
		new RelativeMove(51, 0),
		new RelativeMove(92, -92),
		new RelativeMove(0, -177),
		new RelativeMove(252, -252),
		new RelativeMove(410, 0),
		new RelativeMove(142, 142),
		new RelativeMove(0, 278),
		new RelativeMove(-34, 34),
		new RelativeMove(0, 82),
		new RelativeMove(34, 34),
		new RelativeMove(54, 0),
		new RelativeMove(34, -34),
		new RelativeMove(90, 0),
		new RelativeMove(69, -69),
		new RelativeMove(0, -44),
		new RelativeMove(10, -10),
		new RelativeMove(34, 0),
		new RelativeMove(42, 42)
	),
	BRIMHAVEN_PORT_TYRAS(
		PortLocation.BRIMHAVEN,
		PortLocation.PORT_TYRAS
		// Sailing >= 66, used in 1 task
	),
	LUNAR_ISLE_PORT_TYRAS(
		PortLocation.LUNAR_ISLE,
		PortLocation.PORT_TYRAS
		// Sailing >= 76, used in 1 task
	),
	PORT_TYRAS_RED_ROCK(
		PortLocation.PORT_TYRAS,
		PortLocation.RED_ROCK
		// Sailing >= 66, used in 1 task
	),
	PRIFDDINAS_RELLEKKA(
		PortLocation.PRIFDDINAS,
		PortLocation.RELLEKKA
		// Sailing >= 70, used in 1 task
	),
	PRIFDDINAS_VOID_KNIGHTS_OUTPOST(
		PortLocation.PRIFDDINAS,
		PortLocation.VOID_KNIGHT
		// Sailing >= 70, used in 1 task
	),
	DEEPFIN_POINT_RELLEKKA(
		PortLocation.DEEPFIN_POINT,
		PortLocation.RELLEKKA
		// Sailing >= 67, used in 1 task
	),
	DEEPFIN_POINT_ETCETERIA(
		PortLocation.DEEPFIN_POINT,
		PortLocation.ETCETERIA
		// Sailing >= 67, used in 1 task
	),
	;

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