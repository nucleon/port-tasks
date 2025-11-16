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

import net.runelite.api.gameval.ObjectID;

public enum LedgerID
	{
		PORT_SARIM(ObjectID.DOCK_LOADING_BAY_LEDGER_TABLE_PORT_SARIM, "Port Sarim"),
		MUSA_POINT(ObjectID.DOCK_LOADING_BAY_LEDGER_TABLE_MUSA_POINT, "Musa Point"),
		PANDEMONIUM(ObjectID.DOCK_LOADING_BAY_LEDGER_TABLE_PANDEMONIUM, "The Pandemonium"),
		CATHERBY(ObjectID.DOCK_LOADING_BAY_LEDGER_TABLE_CATHERBY, "Catherby"),
		ENTRANA(ObjectID.DOCK_LOADING_BAY_LEDGER_TABLE_ENTRANA, "Entrana"),
		ARDOUGNE(ObjectID.DOCK_LOADING_BAY_LEDGER_TABLE_ARDOUGNE, "Ardougne"),
		BRIMHAVEN(ObjectID.DOCK_LOADING_BAY_LEDGER_TABLE_BRIMHAVEN, "Brimhaven"),
		PORT_KHAZARD(ObjectID.DOCK_LOADING_BAY_LEDGER_TABLE_PORT_KHAZARD, "Port Khazard"),
		CORSAIR_COVE(ObjectID.DOCK_LOADING_BAY_LEDGER_TABLE_CORSAIR_COVE, "Corsair Cove"),
		RUINS_OF_UNKAH(ObjectID.DOCK_LOADING_BAY_LEDGER_TABLE_RUINS_OF_UNKAH, "Ruins of Unkah"),
		PORT_PISCARILIUS(ObjectID.DOCK_LOADING_BAY_LEDGER_TABLE_PORT_PISCARILIUS, "Port Piscarilius"),
		CIVITAS_ILLA_FORTIS(ObjectID.DOCK_LOADING_BAY_LEDGER_TABLE_CIVITAS_ILLA_FORTIS, "Civitas illa Fortis"),
		RELLEKKA(ObjectID.DOCK_LOADING_BAY_LEDGER_TABLE_RELLEKKA, "Rellekka"),
		LANDS_END(ObjectID.DOCK_LOADING_BAY_LEDGER_TABLE_LANDS_END, "Land's End"),
		HOSIDIUS(ObjectID.DOCK_LOADING_BAY_LEDGER_TABLE_HOSIDIUS, "Hosidius"),
		CAIRN_ISLE(ObjectID.DOCK_LOADING_BAY_LEDGER_TABLE_CAIRN_ISLE, "Cairn Isle"),
		SUNSET_COAST(ObjectID.DOCK_LOADING_BAY_LEDGER_TABLE_SUNSET_COAST, "Sunset Coast"),
		THE_SUMMER_SHORE(ObjectID.DOCK_LOADING_BAY_LEDGER_TABLE_THE_SUMMER_SHORE, "Summer Shore"),
		ALDARIN(ObjectID.DOCK_LOADING_BAY_LEDGER_TABLE_ALDARIN, "Aldarin"),
		VOID_KNIGHTS_OUTPOST(ObjectID.DOCK_LOADING_BAY_LEDGER_TABLE_VOID_KNIGHTS_OUTPOST, "Void Knights' Outpost"),
		PORT_ROBERTS(ObjectID.DOCK_LOADING_BAY_LEDGER_TABLE_PORT_ROBERTS, "Port Roberts"),
		RED_ROCK(ObjectID.DOCK_LOADING_BAY_LEDGER_TABLE_RED_ROCK, "Red Rock"),
		ETCETERIA(ObjectID.DOCK_LOADING_BAY_LEDGER_TABLE_ETCETERIA, "Etceteria"),
		NEITIZNOT(ObjectID.DOCK_LOADING_BAY_LEDGER_TABLE_NEITIZNOT, "Neitiznot"),
		JATIZSO(ObjectID.DOCK_LOADING_BAY_LEDGER_TABLE_JATIZSO, "Jatizo"),
		PORT_TYRAS(ObjectID.DOCK_LOADING_BAY_LEDGER_TABLE_PORT_TYRAS, "Port Tyras"),
		DEEPFIN_POINT(ObjectID.DOCK_LOADING_BAY_LEDGER_TABLE_DEEPFIN_POINT, "Deepfin Point"),
		PRIFDDINAS(ObjectID.DOCK_LOADING_BAY_LEDGER_TABLE_PRIFDDINAS, "Prifddinas"),
		PISCATORIS(ObjectID.DOCK_LOADING_BAY_LEDGER_TABLE_PISCATORIS, "Piscatoris"),
		LUNAR_ISLE(ObjectID.DOCK_LOADING_BAY_LEDGER_TABLE_LUNAR_ISLE, "Lunar Isle"),
		;

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
