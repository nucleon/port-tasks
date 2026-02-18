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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Inject;

import com.nucleon.porttasks.enums.PortLocation;
import com.nucleon.porttasks.enums.PortPaths;
import com.nucleon.porttasks.overlay.WorldLines;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.Overlay;

import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

@Slf4j
class PortTasksMapOverlay extends Overlay
{
	private final Client client;
	private final PortTasksPlugin plugin;
	private final PortTasksConfig config;

	@Inject
	private PortTasksMapOverlay(Client client, PortTasksPlugin plugin, PortTasksConfig config)
	{
		this.client = client;
		this.plugin = plugin;
		this.config = config;

		setPosition(OverlayPosition.DYNAMIC);
		setPriority(Overlay.PRIORITY_HIGHEST);
		setLayer(OverlayLayer.ALWAYS_ON_TOP);
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		renderOverlayLines(graphics);
		renderOverlayLinesDeveloper(graphics);
		return null;
	}

	private void renderOverlayLines(Graphics2D g)
	{
		if (plugin.developerMode) {
			renderOverlayLinesSmartRouting(g);
		} else {
			for (CourierTask tasks : plugin.courierTasks)
			{
				Color overlayColor = tasks.getOverlayColor();
				List<WorldPoint> journey = tasks.getData().dockMarkers.getFullPath();
				if (tasks.getData().reversePath)
				{
					Collections.reverse(journey);
				}
				if (tasks.isTracking())
				{
					WorldLines.createWorldMapLines(g, client, journey, overlayColor);
				}
			}
		}

	}

	private void renderOverlayLinesSmartRouting(Graphics2D g) {
		// idk what isTracking is
		List<PortLocation> rawPortLocations = plugin.courierTasks.stream().filter(CourierTask::isTracking).flatMap(ct -> Stream.of(ct.getData().getCargoLocation(), ct.getData().getDeliveryLocation())).collect(Collectors.toList());
		List<PortLocation> orderedPortLocations = rawPortLocations.stream().sorted(Comparator.comparingInt(PortLocation::getLocationOrdering)).collect(Collectors.toList());
		List<List<WorldPoint>> journeys = new ArrayList<>();
		for (int i = 0; i < orderedPortLocations.size() - 1; i++) {
			PortLocation start = orderedPortLocations.get(i);
			PortLocation end = orderedPortLocations.get(i + 1);
			if(start == end) {
				continue;
			}
			PortPathMatch portPathMatch = PortPaths.findPath(start, end);

			if(portPathMatch.getPath() == PortPaths.DEFAULT) {
				log.info("Because we failed to find valid path between points, we will not attempt to do smart rendering");
				return;
			}

			List<WorldPoint> journey = portPathMatch.getPath().getFullPath();
			if(portPathMatch.isReversed()) {
				Collections.reverse(journey);
			}

			journeys.add(journey);
		}

		for(List<WorldPoint> journey : journeys) {
			WorldLines.createWorldMapLines(g, client, journey, Color.WHITE);
		}
	}

	private void renderOverlayLinesDeveloper(Graphics2D g)
	{
		if (plugin.developerMode)
		{
			if (plugin.developerPathSelected != null)
			{
				WorldLines.createWorldMapLines(g, client, plugin.getDeveloperPathSelected().getFullPath(), Color.CYAN);
			}
		}
	}
}