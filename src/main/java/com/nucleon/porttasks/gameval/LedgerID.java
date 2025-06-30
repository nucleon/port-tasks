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
package com.nucleon.porttasks.gameval;

	public enum LedgerID
	{
		MUSA_POINT(58213, "Musa Point"),
		PORT_SARIM(58212, "Port Sarim"),
		PANDEMONIUM(58214, "The Pandemonium"),
		CATHERBY(58215, "Catherby"),
		ENTRANA(58216, "Entrana"),
		ARDOUGNE(58217, "Ardougne"),
		BRIMHAVEN(58218, "Brimhaven"),
		PORT_KHAZARD(58219, "Port Khazard"),
		CORSAIR_COVE(58220, "Corsair Cove"),
		RUINS_OF_UNKAH(58221, "Ruins of Unkah"),
		PORT_PISCARILIUS(58222, "Port Piscarilius"),
		CIVITAS_ILLA_FORTIS(58223, "Civitas illa Fortis"),
		RELLEKKA(58224, "Rellekka"),
		LANDS_END(58225, "Land's End"),
		HOSIDIUS(58226, "Hosidius"),
		CAIRN_ISLE(58227, "Cairn Isle"),
		SUNSET_COAST(58228, "Sunset Coast"),
		THE_SUMMER_SHORE(58229, "Summer Shore"),
		ALDARIN(58230, "Aldarin"),
		VOID_KNIGHTS_OUTPOST(58231, "Void Knights' Outpost"),
		PORT_ROBERTS(58232, "Port Roberts"),
		RED_ROCK(58233, "Red Rock"),
		BARRACUDA_HQ(58234, "Barracuda HQ"),
		ETCETERIA(58235, "Etceteria"),
		PORT_TYRAS(58236, "Port Tyras"),
		DEEPFIN_POINT(58237, "Deepfin Point"),
		PRIFDDINAS(58238, "Prifddinas"),
		PISCATORIS(58239, "Piscatoris"),
		LUNAR_ISLE(58240, "Lunar Isle");

	private final int objectId;
	private final String name;

	LedgerID(int objectId, String name)
	{
		this.objectId = objectId;
		this.name = name;
	}

	public static Integer getObjectIdByName(String name)
	{
		for (LedgerID ledger : values())
		{
			if (ledger.name.equalsIgnoreCase(name))
			{
				return ledger.objectId;
			}
		}
		return null;
	}

	public static boolean containsName(String name)
	{
		for (LedgerID ledger : values())
		{
			if (ledger.name.equalsIgnoreCase(name))
			{
				return true;
			}
		}
		return false;
	}
}
