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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.nucleon.porttasks.enums.LedgerID;
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
		// we need to track if a port courier task is sharing a ledger for delivery or cargo
		Map<Integer, List<PortTask>> ledgerUsageMap = new HashMap<>();
		// we need to store a reference to an objectid and an overlay
		Map<Integer, Integer> overlayCount = new HashMap<>();

		//  looping through all the port tasks currently assigned
		for (PortTask task : plugin.currentTasks)
		{	// get the port locations and check them against the ledger port locations in our LedgerID enum
			String cargoPickupLocation = task.getData().getCargoLocation().getName();
			String cargoDeliveryLocation = task.getData().getDeliveryLocation().getName();

			Integer pickupLedgerObjectID = LedgerID.containsName(cargoPickupLocation)
					? LedgerID.getObjectIdByName(cargoPickupLocation)
					: null;

			Integer deliveryLedgerObjectID = LedgerID.containsName(cargoDeliveryLocation)
					? LedgerID.getObjectIdByName(cargoDeliveryLocation)
					: null;
			// store a reference to them in the map, so we can render
			// multicolored overlays for shared ledgers in port tasks
			if (pickupLedgerObjectID != null)
			{
				ledgerUsageMap.computeIfAbsent(pickupLedgerObjectID, k -> new ArrayList<>()).add(task);
			}
			if (deliveryLedgerObjectID != null)
			{
				ledgerUsageMap.computeIfAbsent(deliveryLedgerObjectID, k -> new ArrayList<>()).add(task);
			}
		}

		// loop through the scene, find the ledger object
		Tile[][][] sceneTiles = client.getTopLevelWorldView().getScene().getTiles();
		for (Tile[][] sceneTile : sceneTiles)
		{
			for (Tile[] tiles : sceneTile)
			{
				for (Tile tile : tiles)
				{
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
						// if the ledger in this scene isn't a ledger with a port task, escape
						int objectId = object.getId();
						List<PortTask> tasksAtLedger = ledgerUsageMap.get(objectId);
						if (tasksAtLedger == null || tasksAtLedger.isEmpty())
						{
							continue;
						}

						ObjectComposition comp = client.getObjectDefinition(objectId);
						int size = comp.getSizeX();

						Polygon poly = Perspective.getCanvasTileAreaPoly(client, object.getLocalLocation(), size);
						if (poly != null)
						{	// we stored the tasks that are using this ledger,
							// so we can draw a dynamic tile
							Color[] colors = getOverlayColors(tasksAtLedger);
							renderMultiColoredSquare(g, poly, colors);
						}
						// loop through the tasks at this ledger object, get the cargo information and render a text overlay
						// for more than one task, store them in a overlayCount map and stack the text
						int offsetIndex = overlayCount.getOrDefault(objectId, 0);
						for (PortTask task : tasksAtLedger)
						{
							String cargoPickupLocation = task.getData().getCargoLocation().getName();
							String cargoDeliveryLocation = task.getData().getDeliveryLocation().getName();
							int cargoTakenFromLedger = task.getCargoTaken();
							int cargoDeliveredToLedger = task.getDelivered();
							int cargoRequired = task.getData().getCargoAmount();

							Integer pickupId = LedgerID.getObjectIdByName(cargoPickupLocation);
							Integer deliveryId = LedgerID.getObjectIdByName(cargoDeliveryLocation);
							boolean isPickup = pickupId != null && objectId == pickupId && cargoTakenFromLedger < cargoRequired;
							boolean isDelivery = deliveryId != null && objectId == deliveryId && cargoDeliveredToLedger < cargoRequired;

							if (!isPickup && !isDelivery)
							{
								continue;
							}
							// so we know it's either a pickup or delivery, display the data of either
							String label = isPickup
									? String.format("Cargo: %d/%d", cargoTakenFromLedger, cargoRequired)
									: String.format("Delivered: %d/%d", cargoDeliveredToLedger, cargoRequired);

							Point textLocation = Perspective.getCanvasTextLocation(client, g, object.getLocalLocation(), label, 0);
							if (textLocation != null)
							{
								int yOffset = 15 * offsetIndex;
								Point raisedLocation = new Point(textLocation.getX(), textLocation.getY() - yOffset);
								OverlayUtil.renderTextLocation(g, raisedLocation, label, Color.WHITE);
								offsetIndex++;
							}
						}
						// +1 overlay on this ledger object
						overlayCount.put(objectId, offsetIndex);
					}
				}
			}
		}
	}

	private void renderMultiColoredSquare(Graphics2D g, Polygon poly, Color... colors)
	{
		if (poly == null || poly.npoints < 2 || colors.length == 0)
		{
			return;
		}

		g.setColor(new Color(0, 0, 0, 50));
		g.fillPolygon(poly);

		int nPoints = poly.npoints;
		int edgesPerColor = nPoints / colors.length;
		int remainder = nPoints % colors.length;

		int edgeIndex = 0;
		for (int colorIndex = 0; colorIndex < colors.length; colorIndex++)
		{
			int count = edgesPerColor + (colorIndex < remainder ? 1 : 0);
			g.setColor(colors[colorIndex]);
			g.setStroke(new BasicStroke(2));

			for (int i = 0; i < count; i++, edgeIndex++)
			{
				int p1 = edgeIndex % nPoints;
				int p2 = (edgeIndex + 1) % nPoints;

				int x1 = poly.xpoints[p1];
				int y1 = poly.ypoints[p1];
				int x2 = poly.xpoints[p2];
				int y2 = poly.ypoints[p2];

				g.drawLine(x1, y1, x2, y2);
			}
		}
	}

	private Color[] getOverlayColors(List<PortTask> tasks)
	{
		Color[] colors = new Color[tasks.size()];
		for (int i = 0; i < tasks.size(); i++)
		{
			colors[i] = tasks.get(i).getOverlayColor();
		}
		return colors;
	}
}