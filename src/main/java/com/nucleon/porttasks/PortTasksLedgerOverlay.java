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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;

import javax.inject.Inject;

import com.nucleon.porttasks.gameval.LedgerID;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.ObjectComposition;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.api.Tile;

import net.runelite.client.ui.overlay.Overlay;

import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;

class PortTasksLedgerOverlay extends Overlay
{
	private final Client client;
	private final PortTasksPlugin plugin;
	private final PortTasksConfig config;

	@Inject
	private PortTasksLedgerOverlay(Client client, PortTasksPlugin plugin, PortTasksConfig config)
	{
		this.client = client;
		this.plugin = plugin;
		this.config = config;

		setPosition(OverlayPosition.DYNAMIC);
		setPriority(PRIORITY_HIGHEST);
		setLayer(OverlayLayer.UNDER_WIDGETS);
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		renderOverlay(graphics);
		return null;
	}

	private void renderOverlay(Graphics2D g)
	{
		for (PortTask task : plugin.currentTasks)
		{
			String cargoPickupLocation = task.getData().getCargoLocation().getName();
			String cargoDeliveryLocation = task.getData().getDeliveryLocation().getName();
			int cargoTakenFromLedger = task.getCargoTaken();
			int cargoDeliveredToLedger = task.getDelivered();
			int cargoRequired = task.getData().getCargoAmount();

			Integer pickupLedgerObjectID = LedgerID.containsName(cargoPickupLocation)
					? LedgerID.getObjectIdByName(cargoPickupLocation)
					: null;

			Integer deliveryLedgerObjectID = LedgerID.containsName(cargoDeliveryLocation)
					? LedgerID.getObjectIdByName(cargoDeliveryLocation)
					: null;

			if (pickupLedgerObjectID == null || deliveryLedgerObjectID == null)
			{
				continue;
			}

			Tile[][][] sceneTiles = client.getTopLevelWorldView().getScene().getTiles();
			for (Tile[][] sceneTile : sceneTiles)
			{
				for (int x = 0; x < sceneTile.length; x++)
				{
					for (int y = 0; y < sceneTile[x].length; y++)
					{
						Tile tile = sceneTile[x][y];
						if (tile == null)
						{
							continue;
						}

						for (GameObject object : tile.getGameObjects())
						{
							if (object == null)
							{
								continue;
							}

							int objectId = object.getId();

							boolean isPickup = objectId == pickupLedgerObjectID && cargoTakenFromLedger < cargoRequired;
							boolean isDelivery = objectId == deliveryLedgerObjectID && cargoDeliveredToLedger < cargoRequired;

							if (!isPickup && !isDelivery)
							{
								continue;
							}

							ObjectComposition comp = client.getObjectDefinition(objectId);
							int sizeX = comp.getSizeX();

							Polygon poly = Perspective.getCanvasTileAreaPoly(client, object.getLocalLocation(), sizeX);
							if (poly != null)
							{
								Color color = isPickup ? Color.YELLOW : Color.CYAN;
								OverlayUtil.renderPolygon(g, poly, color);
							}

							String label = isPickup
									? String.format("Cargo: %d/%d", cargoTakenFromLedger, cargoRequired)
									: String.format("Delivered: %d/%d", cargoDeliveredToLedger, cargoRequired);

							Point textLocation = Perspective.getCanvasTextLocation(client, g, object.getLocalLocation(), label, 0);
							if (textLocation != null)
							{
								OverlayUtil.renderTextLocation(g, textLocation, label, Color.WHITE);
							}
						}
					}
				}
			}
		}
	}
}