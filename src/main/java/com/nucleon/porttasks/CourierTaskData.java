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
package com.nucleon.porttasks;

import com.nucleon.porttasks.enums.PortLocation;
import com.nucleon.porttasks.enums.PortPaths;
import com.nucleon.porttasks.enums.TaskReward;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import lombok.Getter;
import net.runelite.api.Client;
import net.runelite.api.gameval.DBTableID;

@Getter
public final class CourierTaskData
{
	private final int dbrow;
	private final int id;
	private final PortLocation noticeBoard;
	private final PortLocation cargoLocation;
	private final PortLocation deliveryLocation;
	public final PortPaths dockMarkers;
	public final boolean reversePath;
	public final String taskName;
	public final int cargo;
	public final int cargoAmount;
	private final double xpPerTile;

	private static final Set<Integer> VARBIT_VALUES = new HashSet<>();
	private static final Map<Integer, CourierTaskData> BY_DBROW = new HashMap<>();
	private static final Map<Integer, CourierTaskData> BY_ID = new HashMap<>();

	private static double MAX_XP_PER_TILE;

	private CourierTaskData(int dbrow, int id, PortLocation noticeBoard, PortLocation cargoLocation, PortLocation deliveryLocation, PortPaths dockMarkers, boolean reversePath, String taskName, int cargo, int cargoAmount, double xpPerTile)
	{
		this.dbrow = dbrow;
		this.id = id;
		this.noticeBoard = noticeBoard;
		this.cargoLocation = cargoLocation;
		this.deliveryLocation = deliveryLocation;
		this.dockMarkers = dockMarkers;
		this.reversePath = reversePath;
		this.taskName = taskName;
		this.cargo = cargo;
		this.cargoAmount = cargoAmount;
		this.xpPerTile = xpPerTile;
	}

	public static void loadFromCache(Client client)
	{
		BY_DBROW.clear();
		BY_ID.clear();
		VARBIT_VALUES.clear();
		MAX_XP_PER_TILE = 0.0;

		for (int rowId : client.getDBTableRows(DBTableID.PortTask.ID))
		{
			CourierTaskData data = fromRow(client, rowId);

			if (data == null)
			{
				continue;
			}

			BY_DBROW.put(data.dbrow, data);
			BY_ID.put(data.id, data);
			VARBIT_VALUES.add(data.id);

			if (data.xpPerTile > MAX_XP_PER_TILE)
			{
				MAX_XP_PER_TILE = data.xpPerTile;
			}
		}
	}

	private static CourierTaskData fromRow(Client client, int dbrow)
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
		PortLocation noticeBoard = PortLocation.fromDbRow(noticeBoardDbRow);

		Integer cargoLocationDbRow = getIntField(client, dbrow, DBTableID.PortTask.COL_CARGO_PORT, 0);
		if (cargoLocationDbRow == null)
		{
			return null;
		}
		PortLocation cargoLocation = PortLocation.fromDbRow(cargoLocationDbRow);

		Integer deliveryLocationDbRow = getIntField(client, dbrow, DBTableID.PortTask.COL_ENDING_PORT, 0);
		if (deliveryLocationDbRow == null)
		{
			return null;
		}
		PortLocation deliveryLocation = PortLocation.fromDbRow(deliveryLocationDbRow);

		if (cargoLocationDbRow.equals(deliveryLocationDbRow))
		{
			// This is a bounty task
			return null;
		}

		if (cargoLocation == PortLocation.EMPTY || deliveryLocation == PortLocation.EMPTY)
		{
			// Used for quests
			return null;
		}

		PortPathMatch match = PortPaths.findPath(cargoLocation, deliveryLocation);
		PortPaths dockMarkers = match.getPath();
		boolean reversePath = match.isReversed();

		String taskName = (String) client.getDBTableField(dbrow, DBTableID.PortTask.COL_NAME, 0)[0];


		Integer cargo = getIntField(client, dbrow, DBTableID.PortTask.COL_CARGO, 0);
		Integer cargoAmount = getIntField(client, dbrow, DBTableID.PortTask.COL_CARGO, 1);

		int reward = TaskReward.getIntRewardForTask(dbrow);
		double distance = dockMarkers.getDistance();
		double xpPerTile = distance > 0 ? ( reward / distance) : 0.0;

		return new CourierTaskData(dbrow, id, noticeBoard, cargoLocation, deliveryLocation, dockMarkers, reversePath, taskName, cargo, cargoAmount, xpPerTile);
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

	public static CourierTaskData getByDbrow(int dbrow)
	{
		return BY_DBROW.get(dbrow);
	}

	public static boolean isCargoTask(int varbitValue)
	{
		return VARBIT_VALUES.contains(varbitValue);
	}

	public static CourierTaskData fromId(int id)
	{
		return BY_ID.get(id);
	}

	public double getXpPerTileRatio()
	{
		return MAX_XP_PER_TILE > 0.0 ? (xpPerTile / MAX_XP_PER_TILE) : 0.0;
	}

}

