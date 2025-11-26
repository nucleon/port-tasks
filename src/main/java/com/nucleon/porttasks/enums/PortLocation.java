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
package com.nucleon.porttasks.enums;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.gameval.ObjectID;

@Getter
public enum PortLocation
{
	MUSA_POINT("Musa Point", "MUSA", 10, ObjectID.SAILING_GANGPLANK_MUSA_POINT, ObjectID.PORT_TASK_BOARD_MUSA_POINT, new WorldPoint(2965, 3146, 0)),
	PORT_SARIM("Port Sarim", "SARIM", 1, ObjectID.SAILING_GANGPLANK_PORT_SARIM, ObjectID.PORT_TASK_BOARD_PORT_SARIM, new WorldPoint(3056, 3194, 0)),
	PANDEMONIUM("The Pandemonium", "PAND", 1, ObjectID.SAILING_GANGPLANK_THE_PANDEMONIUM, ObjectID.PORT_TASK_BOARD_PANDEMONIUM, new WorldPoint(3078, 2987, 0)),
	ENTRANA("Entrana", "ENTR", 36, ObjectID.SAILING_GANGPLANK_ENTRANA, -1, new WorldPoint(2883, 3336, 0)),
	RUINS_OF_UNKAH("Ruins of Unkah", "UNKAH", 48, ObjectID.SAILING_GANGPLANK_RUINS_OF_UNKAH, ObjectID.PORT_TASK_BOARD_RUINS_OF_UNKAH, new WorldPoint(3143, 2824, 0)),
	RED_ROCK("Red Rock", "REDRK", null, ObjectID.SAILING_GANGPLANK_RED_ROCK, ObjectID.PORT_TASK_BOARD_RED_ROCK, new WorldPoint(2814, 2510, 0)),
	ARDOUGNE("Ardougne", "ARDY", 28, ObjectID.SAILING_GANGPLANK_ARDOUGNE, ObjectID.PORT_TASK_BOARD_ARDOUGNE, new WorldPoint(2670, 3259, 0)),
	BRIMHAVEN("Brimhaven", "BRIM", 25, ObjectID.SAILING_GANGPLANK_BRIMHAVEN, ObjectID.PORT_TASK_BOARD_BRIMHAVEN, new WorldPoint(2754, 3231, 0)),
	CATHERBY("Catherby", "CATH", 20, ObjectID.SAILING_GANGPLANK_CATHERBY, ObjectID.PORT_TASK_BOARD_CATHERBY, new WorldPoint(2796, 3408, 0)),
	PORT_KHAZARD("Port Khazard", "KHZRD", 30, ObjectID.SAILING_GANGPLANK_PORT_KHAZARD, ObjectID.PORT_TASK_BOARD_PORT_KHAZARD, new WorldPoint(2688, 3162, 0)),
	CORSAIR_COVE("Corsair Cove", "CORSR", 40, ObjectID.SAILING_GANGPLANK_CORSAIR_COVE, ObjectID.PORT_TASK_BOARD_CORSAIR_COVE, new WorldPoint(2586, 2844, 0)),
	DEEPFIN_POINT("Deepfin Point", "DPFIN", 67, ObjectID.SAILING_GANGPLANK_DEEPFIN_POINT, ObjectID.PORT_TASK_BOARD_DEEPFIN_POINT, new WorldPoint(1923, 2752, 0)),
	SUNSET_COAST("Sunset Coast", "SUNST", 44, ObjectID.SAILING_GANGPLANK_SUNSET_COAST, -1, new WorldPoint(1506, 2971, 0)),
	ALDARIN("Aldarin", "ALDRN", 46, ObjectID.SAILING_GANGPLANK_ALDARIN, ObjectID.PORT_TASK_BOARD_ALDARIN, new WorldPoint(1454, 2977, 0)),
	SUMMER_SHORE("The Summer Shore", "SUMMR", 45, ObjectID.SAILING_GANGPLANK_THE_SUMMER_SHORE, ObjectID.PORT_TASK_BOARD_THE_SUMMER_SHORE, new WorldPoint(3174, 2367, 0)),
	VOID_KNIGHTS_OUTPOST("Void Knights' Outpost", "VOID", 50, ObjectID.SAILING_GANGPLANK_VOID_KNIGHTS_OUTPOST, ObjectID.PORT_TASK_BOARD_VOID_KNIGHTS_OUTPOST, new WorldPoint(2651, 2683, 0)),
	PORT_TYRAS("Port Tyras", "TYRAS", 66, ObjectID.SAILING_GANGPLANK_PORT_TYRAS, ObjectID.PORT_TASK_BOARD_PORT_TYRAS, new WorldPoint(2141, 3115, 0)),
	PORT_ROBERTS("Port Roberts", "ROBRT", 50, ObjectID.SAILING_GANGPLANK_PORT_ROBERTS, ObjectID.PORT_TASK_BOARD_PORT_ROBERTS, new WorldPoint(1858, 3307, 0)),
	LANDS_END("Land's End", "LANDS", 5, ObjectID.SAILING_GANGPLANK_LANDS_END, ObjectID.PORT_TASK_BOARD_LANDS_END, new WorldPoint(1511, 3405, 0)),
	HOSIDIUS("Hosidius", "HOSDS", 5, ObjectID.SAILING_GANGPLANK_HOSIDIUS, -1, new WorldPoint(1726, 3447, 0)),
	CIVITAS_ILLA_FORTIS("Civitas illa Fortis", "FORTS", 38, ObjectID.SAILING_GANGPLANK_CIVITAS_ILLA_FORTIS, ObjectID.PORT_TASK_BOARD_CIVITAS_ILLA_FORTIS, new WorldPoint(1769, 3144, 0)),
	PORT_PISCARILIUS("Port Piscarilius", "PSCRL", 15, ObjectID.SAILING_GANGPLANK_PORT_PISCARILIUS, ObjectID.PORT_TASK_BOARD_PORT_PISCARILIUS, new WorldPoint(1845, 3681, 0)),
	CAIRN_ISLE("Cairn Isle", "CAIRN", 42, ObjectID.SAILING_GANGPLANK_CAIRN_ISLE, -1, new WorldPoint(2745, 2952, 0)),
	PRIFDDINAS("Prifddinas", "PRIF", 70, ObjectID.SAILING_GANGPLANK_PRIFDDINAS, ObjectID.PORT_TASK_BOARD_PRIFDDINAS, new WorldPoint(2158, 3319, 0)),
	PISCATORIS("Piscatoris", "PSCTR", 75, ObjectID.SAILING_GANGPLANK_PISCATORIS, -1, new WorldPoint(2300, 3689, 0)),
	LUNAR_ISLE("Lunar Isle", "LUNAR", 76, ObjectID.SAILING_GANGPLANK_LUNAR_ISLE, ObjectID.PORT_TASK_BOARD_LUNAR_ISLE, new WorldPoint(2157, 3881, 0)),
	RELLEKKA("Rellekka", "RELL", 62, ObjectID.SAILING_GANGPLANK_RELLEKKA, ObjectID.PORT_TASK_BOARD_RELLEKKA, new WorldPoint(2630, 3709, 0)),
	JATIZSO("Jatizso", "JATIZ", 68, ObjectID.SAILING_GANGPLANK_JATIZSO, -1, new WorldPoint(2412, 3776, 0)),
	ETCETERIA("Etceteria", "ETC", 65, ObjectID.SAILING_GANGPLANK_ETCETERIA, ObjectID.PORT_TASK_BOARD_ETCETERIA, new WorldPoint(2612, 3836, 0)),
	NEITIZNOT("Neitiznot", "NEITZ", 68, ObjectID.SAILING_GANGPLANK_NEITIZNOT, -1, new WorldPoint(2302, 3782, 0)),
	EMPTY("Default", "", null, -1, -1, new WorldPoint(0, 0, 0)),
	;

	private final String name;
	private final String shortName;
	private final Integer sailingLevelRequired;
	private final int gangplankObject;
	private final int noticeboardObject;
	private final WorldPoint navigationLocation;

	private static final Set<Integer> GANGPLANK_IDS;
	private static final Set<Integer> NOTICEBOARD_IDS;


	PortLocation(String name, String shortName, Integer sailingLevelRequired, int gangplankObject, int noticeboardObject, WorldPoint navigationLocation)
	{
		this.name = name;
		this.shortName = shortName;
		this.sailingLevelRequired = sailingLevelRequired;
		this.gangplankObject = gangplankObject;
		this.noticeboardObject = noticeboardObject;
		this.navigationLocation = navigationLocation;
	}

	static
	{
		Set<Integer> gangplanks = new HashSet<>();
		Set<Integer> noticeboards = new HashSet<>();
		for (PortLocation p : values())
		{
			gangplanks.add(p.gangplankObject);
			if (p.noticeboardObject != -1)
			{
				noticeboards.add(p.noticeboardObject);
			}
		}
		GANGPLANK_IDS = Collections.unmodifiableSet(gangplanks);
		NOTICEBOARD_IDS = Collections.unmodifiableSet(noticeboards);
	}

	public static boolean isGangplank(int objectId)
	{
		return GANGPLANK_IDS.contains(objectId);
	}

	public static boolean isNoticeboard(int objectId)
	{
		return NOTICEBOARD_IDS.contains(objectId);
	}

	@Override
	public String toString()
	{
		return name;
	}

	/**
	 * Gets the location name as used in task names.
	 * Some locations use shortened names in tasks compared to their full display names.
	 */
	public String getTaskName()
	{
		switch (this)
		{
			case PANDEMONIUM:
				return "Pandemonium";  // Instead of "The Pandemonium"
			case RUINS_OF_UNKAH:
				return "Ruin of Unkah";  // Instead of "Ruins of Unkah" (singular)
			case SUMMER_SHORE:
				return "Summer Shore";  // Instead of "The Summer Shore"
			default:
				return name;
		}
	}

}