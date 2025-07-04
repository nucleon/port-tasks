/*
 * Copyright (c) 2021, Zoinkwiz <https://github.com/Zoinkwiz>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 * nucleon <https://github.com/nucleon>
 * Modification: drawLinesOnWorld()
 * Interpolates long lines into shorter segments to bypass RuneLite’s overlay draw distance limit.
 */

package com.nucleon.porttasks.overlay;

import java.util.ArrayList;
import net.runelite.api.Client;
import net.runelite.api.Constants;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.widgets.Widget;
import net.runelite.client.ui.overlay.OverlayUtil;

import javax.annotation.Nonnull;
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.List;

public class WorldLines
{
	public static void createWorldMapLines(Graphics2D graphics, Client client, List<WorldPoint> linePoints, Color color)
	{
		Rectangle mapViewArea = WorldPerspective.getWorldMapClipArea(client);

		for (int i = 0; i < linePoints.size() - 1; i++)
		{
			Point startPoint = WorldPerspective.mapWorldPointToGraphicsPoint(client, linePoints.get(i));
			Point endPoint = WorldPerspective.mapWorldPointToGraphicsPoint(client, linePoints.get(i + 1));

			WorldLines.renderWorldMapLine(graphics, client, mapViewArea, startPoint, endPoint, color);
		}
	}

	public static void createMinimapLines(Graphics2D graphics, Client client, List<WorldPoint> linePoints, Color color)
	{
		if (linePoints == null || linePoints.size() < 2)
		{
			return;
		}
		for (int i = 0; i < linePoints.size() - 1; i++)
		{
			WorldPoint dontRenderPoint = new WorldPoint(0, 0, 0);
			WorldPoint currentPoint = linePoints.get(i);
			WorldPoint nextPoint = linePoints.get(i + 1);

			if (currentPoint == null || currentPoint.equals(dontRenderPoint) || nextPoint == null || nextPoint.equals(dontRenderPoint))
			{
			continue;
			}

			List<LocalPoint> startPoints = WorldPerspective.getInstanceLocalPointFromReal(client, currentPoint);
			List<LocalPoint> destinationPoints = WorldPerspective.getInstanceLocalPointFromReal(client, nextPoint);
			if (startPoints.isEmpty() || destinationPoints.isEmpty()) continue;
			LocalPoint startPoint = startPoints.get(0);
			LocalPoint destinationPoint = destinationPoints.get(0);

			Point startPosOnMinimap = net.runelite.api.Perspective.localToMinimap(client, startPoint, 10000000);
			Point destinationPosOnMinimap = net.runelite.api.Perspective.localToMinimap(client, destinationPoint, 10000000);

			if (destinationPosOnMinimap == null || startPosOnMinimap == null)
			{
				continue;
			}

			Line2D.Double line = new Line2D.Double(startPosOnMinimap.getX(), startPosOnMinimap.getY(), destinationPosOnMinimap.getX(), destinationPosOnMinimap.getY());

			Rectangle bounds = new Rectangle(0, 0, client.getCanvasWidth(), client.getCanvasHeight());
			Widget minimapWidget = client.getWidget(InterfaceID.ToplevelOsrsStretch.MINIMAP);

			if (minimapWidget == null)
			{
				minimapWidget = client.getWidget(InterfaceID.ToplevelPreEoc.MINIMAP);
			}
			if (minimapWidget == null)
			{
				minimapWidget = client.getWidget(InterfaceID.Toplevel.MINIMAP);
			}

			if (minimapWidget != null)
			{
				bounds = minimapWidget.getBounds();
			}

			DirectionArrow.drawLine(graphics, line, color, bounds);
		}
	}

	public static void renderWorldMapLine(Graphics2D graphics, Client client, Rectangle mapViewArea, Point startPoint, Point endPoint, Color color)
	{
		if (mapViewArea == null || startPoint == null || endPoint == null)
		{
			return;
		}
		if (!mapViewArea.contains(startPoint.getX(), startPoint.getY()) && !mapViewArea.contains(endPoint.getX(), endPoint.getY()))
		{
			return;
		}

		Line2D.Double line = new Line2D.Double(startPoint.getX(), startPoint.getY(), endPoint.getX(), endPoint.getY());
		DirectionArrow.drawLine(graphics, line, color, WorldPerspective.getWorldMapClipArea(client));
	}

	public static Line2D.Double getWorldLines(@Nonnull Client client, @Nonnull LocalPoint startLocation, LocalPoint endLocation)
	{
		final int plane = client.getPlane();

		final int startX = startLocation.getX();
		final int startY = startLocation.getY();
		final int endX = endLocation.getX();
		final int endY = endLocation.getY();

		final int sceneX = startLocation.getSceneX();
		final int sceneY = startLocation.getSceneY();

		if (sceneX < 0 || sceneY < 0 || sceneX >= Constants.SCENE_SIZE || sceneY >= Constants.SCENE_SIZE)
		{
			return null;
		}

		final int startHeight = net.runelite.api.Perspective.getTileHeight(client, startLocation, plane);
		final int endHeight = net.runelite.api.Perspective.getTileHeight(client, endLocation, plane);

		Point p1 = net.runelite.api.Perspective.localToCanvas(client, startX, startY, startHeight);
		Point p2 = net.runelite.api.Perspective.localToCanvas(client, endX, endY, endHeight);

		if (p1 == null || p2 == null)
		{
			return null;
		}

		return new Line2D.Double(p1.getX(), p1.getY(), p2.getX(), p2.getY());
	}

	public static void drawLinesOnWorld(Graphics2D graphics, Client client, List<WorldPoint> linePoints, Color color)
	{
		for (int i = 0; i < linePoints.size() - 1; i++)
		{
			WorldPoint startWp = linePoints.get(i);
			WorldPoint endWp = linePoints.get(i + 1);

			if (startWp == null || endWp == null) continue;
			if (startWp.equals(new WorldPoint(0, 0, 0))) continue;
			if (endWp.equals(new WorldPoint(0, 0, 0))) continue;
			if (startWp.getPlane() != endWp.getPlane()) continue;

			List<WorldPoint> interpolated = interpolateLine(startWp, endWp);

			for (int j = 0; j < interpolated.size() - 1; j++)
			{
				WorldPoint wp1 = interpolated.get(j);
				WorldPoint wp2 = interpolated.get(j + 1);

				List<LocalPoint> points1 = WorldPerspective.getInstanceLocalPointFromReal(client, wp1);
				List<LocalPoint> points2 = WorldPerspective.getInstanceLocalPointFromReal(client, wp2);

				if (points1.isEmpty() || points2.isEmpty()) continue;

				LocalPoint lp1 = points1.get(0);
				LocalPoint lp2 = points2.get(0);

				Line2D.Double newLine = getWorldLines(client, lp1, lp2);
				if (newLine != null)
				{
						OverlayUtil.renderPolygon(graphics, newLine, color);
				}
			}
		}
	}

	private static List<WorldPoint> interpolateLine(WorldPoint start, WorldPoint end)
	{
		List<WorldPoint> result = new ArrayList<>();
		int steps = Math.max(start.distanceTo(end), 1);

		for (int i = 0; i <= steps; i++)
		{
			double t = i / (double) steps;
			int x = (int) Math.round(lerp(start.getX(), end.getX(), t));
			int y = (int) Math.round(lerp(start.getY(), end.getY(), t));
			int plane = start.getPlane();
			result.add(new WorldPoint(x, y, plane));
		}

		return result;
	}

	private static double lerp(int a, int b, double t)
	{
		return a + (b - a) * t;
	}
}
