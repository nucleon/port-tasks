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
	),
	BRIMHAVEN_PORT_SARIM(
		PortLocation.BRIMHAVEN,
		PortLocation.PORT_SARIM
	),
	PORT_SARIM_RUINS_OF_UNKAH(
		PortLocation.PORT_SARIM,
		PortLocation.RUINS_OF_UNKAH
	),
	PORT_PISCARILIUS_PORT_SARIM(
		PortLocation.PORT_PISCARILIUS,
		PortLocation.PORT_SARIM
	),
	PORT_SARIM_VOID_KNIGHTS_OUTPOST(
		PortLocation.PORT_SARIM,
		PortLocation.VOID_KNIGHT
	),
	CORSAIR_COVE_PANDEMONIUM(
		PortLocation.CORSAIR_COVE,
		PortLocation.PANDEMONIUM
	),
	CATHERBY_VOID_KNIGHTS_OUTPOST(
		PortLocation.CATHERBY,
		PortLocation.VOID_KNIGHT
	),
	BRIMHAVEN_CORSAIR_COVE(
		PortLocation.BRIMHAVEN,
		PortLocation.CORSAIR_COVE
	),
	BRIMHAVEN_RED_ROCK(
		PortLocation.BRIMHAVEN,
		PortLocation.RED_ROCK
	),
	BRIMHAVEN_SUMMER_SHORE(
		PortLocation.BRIMHAVEN,
		PortLocation.SUMMER_SHORE
	),
	ARDOUGNE_PORT_TYRAS(
		PortLocation.ARDOUGNE,
		PortLocation.PORT_TYRAS
	),
	ARDOUGNE_PORT_PISCARILIUS(
		PortLocation.ARDOUGNE,
		PortLocation.PORT_PISCARILIUS
	),
	ARDOUGNE_RED_ROCK(
		PortLocation.ARDOUGNE,
		PortLocation.RED_ROCK
	),
	CIVITAS_ILLA_FORTIS_PORT_KHAZARD(
		PortLocation.CIVITAS_ILLA_FORTIS,
		PortLocation.PORT_KHAZARD
	),
	CORSAIR_COVE_VOID_KNIGHTS_OUTPOST(
		PortLocation.CORSAIR_COVE,
		PortLocation.VOID_KNIGHT
	),
	CORSAIR_COVE_PORT_TYRAS(
		PortLocation.CORSAIR_COVE,
		PortLocation.PORT_TYRAS
	),
	CORSAIR_COVE_PORT_PISCARILIUS(
		PortLocation.CORSAIR_COVE,
		PortLocation.PORT_PISCARILIUS
	),
	CAIRN_ISLE_CORSAIR_COVE(
		PortLocation.CAIRN_ISLE,
		PortLocation.CORSAIR_COVE
	),
	RED_ROCK_RUINS_OF_UNKAH(
		PortLocation.RED_ROCK,
		PortLocation.RUINS_OF_UNKAH
	),
	RUINS_OF_UNKAH_VOID_KNIGHTS_OUTPOST(
		PortLocation.RUINS_OF_UNKAH,
		PortLocation.VOID_KNIGHT
	),
	RED_ROCK_VOID_KNIGHTS_OUTPOST(
		PortLocation.RED_ROCK,
		PortLocation.VOID_KNIGHT
	),
	SUMMER_SHORE_VOID_KNIGHTS_OUTPOST(
		PortLocation.SUMMER_SHORE,
		PortLocation.VOID_KNIGHT
	),
	DEEPFIN_POINT_VOID_KNIGHTS_OUTPOST(
		PortLocation.DEEPFIN_POINT,
		PortLocation.VOID_KNIGHT
	),
	LANDS_END_PRIFDDINAS(
		PortLocation.LANDS_END,
		PortLocation.PRIFDDINAS
	),
	LANDS_END_PISCATORIS(
		PortLocation.LANDS_END,
		PortLocation.PISCATORIS
	),
	CORSAIR_COVE_LANDS_END(
		PortLocation.CORSAIR_COVE,
		PortLocation.LANDS_END
	),
	LANDS_END_PORT_PISCARILIUS(
		PortLocation.LANDS_END,
		PortLocation.PORT_PISCARILIUS
	),
	PORT_PISCARILIUS_PORT_ROBERTS(
		PortLocation.PORT_PISCARILIUS,
		PortLocation.PORT_ROBERTS
	),
	PISCATORIS_PORT_PISCARILIUS(
		PortLocation.PISCATORIS,
		PortLocation.PORT_PISCARILIUS
	),
	HOSIDIUS_PORT_PISCARILIUS(
		PortLocation.HOSIDIUS,
		PortLocation.PORT_PISCARILIUS
	),
	LUNAR_ISLE_PORT_PISCARILIUS(
		PortLocation.LUNAR_ISLE,
		PortLocation.PORT_PISCARILIUS
	),
	PORT_PISCARILIUS_PORT_TYRAS(
		PortLocation.PORT_PISCARILIUS,
		PortLocation.PORT_TYRAS
	),
	PORT_PISCARILIUS_RELLEKKA(
		PortLocation.PORT_PISCARILIUS,
		PortLocation.RELLEKKA
	),
	PORT_PISCARILIUS_PRIFDDINAS(
		PortLocation.PORT_PISCARILIUS,
		PortLocation.PRIFDDINAS
	),
	ALDARIN_CIVITAS_ILLA_FORTIS(
		PortLocation.ALDARIN,
		PortLocation.CIVITAS_ILLA_FORTIS
	),
	CIVITAS_ILLA_FORTIS_PORT_PISCARILIUS(
		PortLocation.CIVITAS_ILLA_FORTIS,
		PortLocation.PORT_PISCARILIUS
	),
	CIVITAS_ILLA_FORTIS_PORT_ROBERTS(
		PortLocation.CIVITAS_ILLA_FORTIS,
		PortLocation.PORT_ROBERTS
	),
	CIVITAS_ILLA_FORTIS_DEEPFIN_POINT(
		PortLocation.CIVITAS_ILLA_FORTIS,
		PortLocation.DEEPFIN_POINT
	),
	CIVITAS_ILLA_FORTIS_PRIFDDINAS(
		PortLocation.CIVITAS_ILLA_FORTIS,
		PortLocation.PRIFDDINAS
	),
	CIVITAS_ILLA_FORTIS_SUNSET_COAST(
		PortLocation.CIVITAS_ILLA_FORTIS,
		PortLocation.SUNSET_COAST
	),
	PORT_ROBERTS_PORT_TYRAS(
		PortLocation.PORT_ROBERTS,
		PortLocation.PORT_TYRAS
	),
	DEEPFIN_POINT_PORT_TYRAS(
		PortLocation.DEEPFIN_POINT,
		PortLocation.PORT_TYRAS
	),
	PORT_TYRAS_PRIFDDINAS(
		PortLocation.PORT_TYRAS,
		PortLocation.PRIFDDINAS
	),
	LANDS_END_PORT_TYRAS(
		PortLocation.LANDS_END,
		PortLocation.PORT_TYRAS
	),
	ALDARIN_PORT_TYRAS(
		PortLocation.ALDARIN,
		PortLocation.PORT_TYRAS
	),
	PORT_ROBERTS_PRIFDDINAS(
		PortLocation.PORT_ROBERTS,
		PortLocation.PRIFDDINAS
	),
	ARDOUGNE_PRIFDDINAS(
		PortLocation.ARDOUGNE,
		PortLocation.PRIFDDINAS
	),
	DEEPFIN_POINT_PRIFDDINAS(
		PortLocation.DEEPFIN_POINT,
		PortLocation.PRIFDDINAS
	),
	ALDARIN_PRIFDDINAS(
		PortLocation.ALDARIN,
		PortLocation.PRIFDDINAS
	),
	LUNAR_ISLE_PRIFDDINAS(
		PortLocation.LUNAR_ISLE,
		PortLocation.PRIFDDINAS
	),
	ETCETERIA_RELLEKKA(
		PortLocation.ETCETERIA,
		PortLocation.RELLEKKA
	),
	NEITIZNOT_RELLEKKA(
		PortLocation.NEITIZNOT,
		PortLocation.RELLEKKA
	),
	RELLEKKA_SUNSET_COAST(
		PortLocation.RELLEKKA,
		PortLocation.SUNSET_COAST
	),
	PORT_ROBERTS_RELLEKKA(
		PortLocation.PORT_ROBERTS,
		PortLocation.RELLEKKA
	),
	PISCATORIS_RELLEKKA(
		PortLocation.PISCATORIS,
		PortLocation.RELLEKKA
	),
	JATIZO_RELLEKKA(
		PortLocation.JATIZO,
		PortLocation.RELLEKKA
	),
	PORT_TYRAS_RELLEKKA(
		PortLocation.PORT_TYRAS,
		PortLocation.RELLEKKA
	),
	ETCETERIA_JATIZO(
		PortLocation.ETCETERIA,
		PortLocation.JATIZO
	),
	ETCETERIA_PORT_ROBERTS(
		PortLocation.ETCETERIA,
		PortLocation.PORT_ROBERTS
	),
	ETCETERIA_PORT_PISCARILIUS(
		PortLocation.ETCETERIA,
		PortLocation.PORT_PISCARILIUS
	),
	ETCETERIA_NEITIZNOT(
		PortLocation.ETCETERIA,
		PortLocation.NEITIZNOT
	),
	ETCETERIA_SUNSET_COAST(
		PortLocation.ETCETERIA,
		PortLocation.SUNSET_COAST
	),
	ETCETERIA_PISCATORIS(
		PortLocation.ETCETERIA,
		PortLocation.PISCATORIS
	),
	ETCETERIA_HOSIDIUS(
		PortLocation.ETCETERIA,
		PortLocation.HOSIDIUS
	),
	LUNAR_ISLE_PISCATORIS(
		PortLocation.LUNAR_ISLE,
		PortLocation.PISCATORIS
	),
	DEEPFIN_POINT_LUNAR_ISLE(
		PortLocation.DEEPFIN_POINT,
		PortLocation.LUNAR_ISLE
	),
	LUNAR_ISLE_PORT_ROBERTS(
		PortLocation.LUNAR_ISLE,
		PortLocation.PORT_ROBERTS
	),
	CIVITAS_ILLA_FORTIS_LUNAR_ISLE(
		PortLocation.CIVITAS_ILLA_FORTIS,
		PortLocation.LUNAR_ISLE
	),
	LUNAR_ISLE_RED_ROCK(
		PortLocation.LUNAR_ISLE,
		PortLocation.RED_ROCK
	),
	ALDARIN_LUNAR_ISLE(
		PortLocation.ALDARIN,
		PortLocation.LUNAR_ISLE
	),
	PORT_SARIM_RELLEKKA(
		PortLocation.PORT_SARIM,
		PortLocation.RELLEKKA
	),
	PANDEMONIUM_CAIRN_ISLE(
		PortLocation.PANDEMONIUM,
		PortLocation.CAIRN_ISLE
	),
	MUSA_POINT_CORSAIR_COVE(
		PortLocation.MUSA_POINT,
		PortLocation.CORSAIR_COVE
	),
	MUSA_POINT_SUMMER_SHORE(
		PortLocation.MUSA_POINT,
		PortLocation.SUMMER_SHORE
	),
	MUSA_POINT_PORT_TYRAS(
		PortLocation.MUSA_POINT,
		PortLocation.PORT_TYRAS
	),
	CATHERBY_PORT_PISCARILIUS(
		PortLocation.CATHERBY,
		PortLocation.PORT_PISCARILIUS
	),
	BRIMHAVEN_CIVITAS_ILLA_FORTIS(
		PortLocation.BRIMHAVEN,
		PortLocation.CIVITAS_ILLA_FORTIS
	),
	ARDOUGNE_SUMMER_SHORE(
		PortLocation.ARDOUGNE,
		PortLocation.SUMMER_SHORE
	),
	ARDOUGNE_CIVITAS_ILLA_FORTIS(
		PortLocation.ARDOUGNE,
		PortLocation.CIVITAS_ILLA_FORTIS
	),
	ARDOUGNE_VOID_KNIGHTS_OUTPOST(
		PortLocation.ARDOUGNE,
		PortLocation.VOID_KNIGHT
	),
	PORT_KHAZARD_RELLEKKA(
		PortLocation.PORT_KHAZARD,
		PortLocation.RELLEKKA
	),
	PORT_KHAZARD_PORT_PISCARILIUS(
		PortLocation.PORT_KHAZARD,
		PortLocation.PORT_PISCARILIUS
	),
	CORSAIR_COVE_CIVITAS_ILLA_FOTRIS(
		PortLocation.CORSAIR_COVE,
		PortLocation.CIVITAS_ILLA_FORTIS
	),
	CORSAIR_COVE_ALDARIN(
		PortLocation.CORSAIR_COVE,
		PortLocation.ALDARIN
	),
	DEEPFIN_POINT_RUINS_OF_UNKAH(
		PortLocation.DEEPFIN_POINT,
		PortLocation.RUINS_OF_UNKAH
	),
	DEEPFIN_POINT_LANDS_END(
		PortLocation.DEEPFIN_POINT,
		PortLocation.LANDS_END
	),
	LANDS_END_PORT_KHAZARD(
		PortLocation.LANDS_END,
		PortLocation.PORT_KHAZARD
	),
	LANDS_END_LUNAR_ISLE(
		PortLocation.LANDS_END,
		PortLocation.LUNAR_ISLE
	),
	MUSA_POINT_PORT_PISCARILIUS(
		PortLocation.MUSA_POINT,
		PortLocation.PORT_PISCARILIUS
	),
	CIVITAS_ILLA_FORTIS_PORT_SARIM(
		PortLocation.CIVITAS_ILLA_FORTIS,
		PortLocation.PORT_SARIM
	),
	BRIMHAVEN_PORT_TYRAS(
		PortLocation.BRIMHAVEN,
		PortLocation.PORT_TYRAS
	),
	LUNAR_ISLE_PORT_TYRAS(
		PortLocation.LUNAR_ISLE,
		PortLocation.PORT_TYRAS
	),
	PORT_TYRAS_RED_ROCK(
		PortLocation.PORT_TYRAS,
		PortLocation.RED_ROCK
	),
	PRIFDDINAS_RELLEKKA(
		PortLocation.PRIFDDINAS,
		PortLocation.RELLEKKA
	),
	PRIFDDINAS_VOID_KNIGHTS_OUTPOST(
		PortLocation.PRIFDDINAS,
		PortLocation.VOID_KNIGHT
	),
	DEEPFIN_POINT_RELLEKKA(
		PortLocation.DEEPFIN_POINT,
		PortLocation.RELLEKKA
	),
	DEEPFIN_POINT_ETCETERIA(
		PortLocation.DEEPFIN_POINT,
		PortLocation.ETCETERIA
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