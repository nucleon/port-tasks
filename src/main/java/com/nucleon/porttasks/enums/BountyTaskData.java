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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lombok.Getter;
import net.runelite.api.Client;
import net.runelite.api.gameval.DBTableID;

@Getter
public final class BountyTaskData
{

	private final int dbrow;
	private final int id;
	private final int levelRequired;
	private final PortLocation bountyLocation;
	public final String taskName;
	public final int itemId;
	public final int npcId;
	private final int deadNpcId;
	public final int itemQuantity;
	public final int itemRarity;

	private static final Set<Integer> VARBIT_VALUES = new HashSet<>();

	private static final Map<Integer, BountyTaskData> BY_DBROW = new HashMap<>();
	private static final Map<Integer, BountyTaskData> BY_ID = new HashMap<>();
	private static final Set<Integer> BOUNTY_NPCS = new HashSet<>();

	private BountyTaskData(int dbrow, int id, int levelRequired, PortLocation bountyLocation, String taskName, int itemId, int npcId, int deadNpcId, int itemQuantity, int itemRarity)
	{
		this.dbrow = dbrow;
		this.id = id;
		this.levelRequired = levelRequired;
		this.bountyLocation = bountyLocation;
		this.taskName = taskName;
		this.itemId = itemId;
		this.npcId = npcId;
		this.deadNpcId = deadNpcId;
		this.itemQuantity = itemQuantity;
		this.itemRarity = itemRarity;
	}

	public static void loadFromCache(Client client)
	{
		BY_DBROW.clear();
		BY_ID.clear();
		VARBIT_VALUES.clear();

		for (int rowId : client.getDBTableRows(DBTableID.PortTask.ID))
		{
			BountyTaskData data = fromRow(client, rowId);

			if (data == null)
			{
				continue;
			}

			BY_DBROW.put(data.dbrow, data);
			BY_ID.put(data.id, data);
			VARBIT_VALUES.add(data.id);
			BOUNTY_NPCS.add(data.deadNpcId);
		}
	}

	private static BountyTaskData fromRow(Client client, int dbrow)
	{
		Integer id = getIntField(client, dbrow, DBTableID.PortTask.COL_TASK_ID, 0);
		if (id == null)
		{
			return null;
		}

		Integer noticeBoardDbRow = getIntField(client, dbrow, DBTableID.PortTask.COL_STARTING_PORT, 0);
		if (noticeBoardDbRow == null)
		{
			return null;
		}
		PortLocation bountyLocation = PortLocation.fromDbRow(noticeBoardDbRow);

		Integer cargoPort = getIntField(client, dbrow, DBTableID.PortTask.COL_CARGO_PORT, 0);
		Integer endingPort = getIntField(client, dbrow, DBTableID.PortTask.COL_ENDING_PORT, 0);
		if (cargoPort == null || endingPort == null || !cargoPort.equals(endingPort))
		{
			return null;
		}

		Integer level = getIntField(client, dbrow, DBTableID.PortTask.COL_LEVEL_REQUIRED, 0);
		if (level == null)
		{
			return null;
		}

		String taskName = (String) client.getDBTableField(dbrow, DBTableID.PortTask.COL_NAME, 0)[0];

		Integer itemId = getIntField(client, dbrow, DBTableID.PortTask.COL_BOUNTY_OBJECT, 0);
		if (itemId == null)
		{
			return null;
		}

		Integer itemQuantity = getIntField(client, dbrow, DBTableID.PortTask.COL_BOUNTY_OBJECT_AMOUNT, 0);
		if (itemQuantity == null)
		{
			return null;
		}

		Integer itemRarity = getIntField(client, dbrow, DBTableID.PortTask.COL_BOUNTY_OBJECT_RARITY, 0);
		if (itemRarity == null)
		{
			return null;
		}

		Integer npcId = getIntField(client, dbrow, DBTableID.PortTask.COL_BOUNTY_TARGET_ALIVE, 0);
		if (npcId == null)
		{
			return null;
		}

		Integer deadNpcId = getIntField(client, dbrow, DBTableID.PortTask.COL_BOUNTY_TARGET_DEAD, 0);
		if (deadNpcId == null)
		{
			return null;
		}

		return new BountyTaskData(dbrow, id, level, bountyLocation, taskName, itemId, npcId, deadNpcId, itemQuantity, itemRarity);

	}

	private static Integer getIntField(Client client, int rowId, int col, int tupleIndex, int objectIndex)
	{
		Object[] field = client.getDBTableField(rowId, col, tupleIndex);
		if (field == null || field.length == 0)
		{
			return null;
		}
		return (int) field[objectIndex];
	}

	private static Integer getIntField(Client client, int rowId, int col, int tupleIndex)
	{
		return getIntField(client, rowId, col, tupleIndex, 0);
	}

	public static BountyTaskData getByDbrow(int dbrow)
	{
		return BY_DBROW.get(dbrow);
	}

	public static BountyTaskData fromId(int id)
	{
		return BY_ID.get(id);
	}

	public static boolean isBountyTask(int id)
	{
		return VARBIT_VALUES.contains(id);
	}

	public static boolean isBountyNpc(int id)
	{
		return BOUNTY_NPCS.contains(id);
	}
}
