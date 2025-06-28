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

import net.runelite.api.coords.WorldPoint;

public enum PortLocation
{
	MUSA_POINT("Musa Point", new WorldPoint(2961, 3146, 0), new WorldPoint(3952, 3150, 0), new WorldPoint(2965, 3146, 0)),
	PORT_SARIM("Port Sarim", new WorldPoint(3051, 3193, 0), new WorldPoint(3030, 3198, 0), new WorldPoint(3056, 3194, 0)),
	PANDEMONIUM("Pandemonium", new WorldPoint(3070, 2987, 0), new WorldPoint(3058, 2986, 0), new WorldPoint(3078, 2987, 0)),
	ENTRANA("Entrana", new WorldPoint(2879, 3335, 0), new WorldPoint(2874, 3339, 0)),
	RUINS_OF_UNKAH("Ruins of Unkah", new WorldPoint(3144, 2825, 0), new WorldPoint(3145, 2828, 0)),
	RED_ROCK("Red Rock", new WorldPoint(2809, 2509, 0), new WorldPoint(2805, 2512, 0)),
	EAST_ARDOUGNE("Ardougne", new WorldPoint(2683, 3265, 0), new WorldPoint(2674, 3269, 0)),
	BRIMHAVEN("Brimhaven", new WorldPoint(2758, 3230, 0), new WorldPoint(2768, 3225, 0), new WorldPoint(2754, 3231, 0)),
	CATHERBY("Catherby", new WorldPoint(2796, 3412, 0), new WorldPoint(2799, 3413, 0), new WorldPoint(2796, 3408, 0)),
	PORT_KHAZARD("Port Khazard", new WorldPoint(2686, 3162, 0), new WorldPoint(2678, 3162, 0), new WorldPoint(2688, 3162, 0)),
	SHILO_VILLAGE("Shilo Village", new WorldPoint(2750, 2952, 0), new WorldPoint(2756, 2949, 0)),
	CORSAIR_COVE("Corsair Cove", new WorldPoint(2580, 2844, 0), new WorldPoint(2580, 2848, 0)),
	BARRACUDA_HQ("Barracuda HQ", new WorldPoint(2294, 2520, 0), new WorldPoint(2289, 2530, 0)),
	DEEPFIN_POINT("Deepfin Point", new WorldPoint(1932, 2791, 0), new WorldPoint(1926, 2791, 0)),
	SUNSET_COAST("Sunset Coast", new WorldPoint(1511, 2975, 0), new WorldPoint(1514, 2977, 0)),
	ALDARIN("Aldarin", new WorldPoint(1452, 2970, 0), new WorldPoint(1448, 2969, 0)),
	SUMMER_SHORE("Summer Shore", new WorldPoint(3174, 2367, 0), new WorldPoint(3172, 2370, 0)),
	VOID_KNIGHT("Void Knight", new WorldPoint(2651, 2678, 0), new WorldPoint(2651, 2673, 0)),
	PORT_TYRAS("Port Tyras", new WorldPoint(2144, 3120, 0), new WorldPoint(2150, 3123, 0)),
	PORT_ROBERTS("Port Roberts", new WorldPoint(1871, 3300, 0), new WorldPoint(1863, 3297, 0)),
	LANDS_END("Lands End", new WorldPoint(1506, 3402, 0), new WorldPoint(1505, 3407, 0)),
	CRABCLAW("Crabclaw", new WorldPoint(1726, 3452, 0), new WorldPoint(1724, 3461, 0)),
	FORTIS("Fortis", new WorldPoint(1775, 3142, 0), new WorldPoint(1780, 3147, 0)),
	EMPTY("Default", new WorldPoint(0, 0, 0), new WorldPoint(0, 0, 0));

	private final String name;
	private final WorldPoint worldPoint;
	private final WorldPoint cargoLocation;
	private final WorldPoint navigationLocation;


	// temp constructor until navigation location is added for all ports
	PortLocation(String name, WorldPoint worldPoint, WorldPoint cargoLocation)
	{
		this(name, worldPoint, cargoLocation, null);
	}

	PortLocation(String name, WorldPoint worldPoint, WorldPoint cargoLocation, WorldPoint navigationLocation)
	{
		this.name = name;
		this.worldPoint = worldPoint;
		this.cargoLocation = cargoLocation;
		this.navigationLocation = navigationLocation;
	}

	public String getName()
	{
		return name;
	}

	public WorldPoint getWorldPoint()
	{
		return worldPoint;
	}

	public WorldPoint getCargoLocation()
	{
		return cargoLocation;
	}
	public WorldPoint getNavigationLocation()
	{
		return navigationLocation;
	}
}