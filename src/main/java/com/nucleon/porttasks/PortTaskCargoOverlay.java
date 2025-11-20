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
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.inject.Inject;

import net.runelite.api.Client;
import net.runelite.api.Point;
import net.runelite.api.widgets.Widget;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.overlay.Overlay;

import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

class PortTaskCargoOverlay extends Overlay
{
	private final Client client;
	private final PortTasksPlugin plugin;
	private final PortTasksConfig config;
	private final ItemManager itemManager;

	@Inject
	private PortTaskCargoOverlay(Client client, PortTasksPlugin plugin, PortTasksConfig config, ItemManager itemManager)
	{
		this.client = client;
		this.plugin = plugin;
		this.config = config;
		this.itemManager = itemManager;

		setPosition(OverlayPosition.DYNAMIC);
		setPriority(Overlay.PRIORITY_HIGHEST);
		setLayer(OverlayLayer.ABOVE_WIDGETS);
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		renderOverlayLines(graphics);
		return null;
	}

	private void renderOverlayLines(Graphics2D g)
	{
		Widget cargoHold = client.getWidget(61800458);
		if (cargoHold == null)
		{
			return;
		}
		Widget[] children = cargoHold.getChildren();
		if (children == null)
		{
			return;
		}
		for (Widget itemWidget : children)
		{
			if (itemWidget == null)
			{
				continue;
			}
			int itemId = itemWidget.getItemId();
			if (itemId <= 0)
			{
				continue;
			}
			Color overlayColor = getMatchingTaskColor(itemId);
			if (overlayColor == null)
			{
				continue;
			}
			drawItemHighlight(g, itemWidget, itemId, overlayColor);
		}
	}

	private Color getMatchingTaskColor(int itemId)
	{
		for (CourierTask task : plugin.courierTasks)
		{
			if (task.getData().cargo == itemId)
			{
				return task.getOverlayColor();
			}
		}
		for (BountyTask task : plugin.bountyTasks)
		{
			if (task.getData().itemId == itemId)
			{
				return task.getOverlayColor();
			}
		}
		return null;
	}

	private void drawItemHighlight(Graphics2D g, Widget itemWidget, int itemId, Color overlayColor)
	{
		Rectangle bounds = itemWidget.getBounds();
		if (bounds == null)
		{
			return;
		}
		BufferedImage outline = itemManager.getItemOutline(itemId, itemWidget.getItemQuantity(), overlayColor);
		BufferedImage icon = itemManager.getImage(itemId);
		Point p = itemWidget.getCanvasLocation();
		if (p == null)
		{
			return;
		}
		drawItemOutline(g, outline, p, overlayColor);
	}

	private void drawItemOutline(Graphics2D g, BufferedImage outline, Point p, Color overlayColor)
	{
		if (outline == null)
		{
			return;
		}
		g.drawImage(outline, p.getX(), p.getY(), null);
		g.setColor(overlayColor);
		g.setStroke(new BasicStroke(1));
		for (int x = 0; x < outline.getWidth(); x++)
		{
			for (int y = 0; y < outline.getHeight(); y++)
			{
				int argb = outline.getRGB(x, y);
				if ((argb >>> 24) > 0)
				{
					g.drawRect(p.getX() + x, p.getY() + y, 1, 1);
				}
			}
		}
	}
}