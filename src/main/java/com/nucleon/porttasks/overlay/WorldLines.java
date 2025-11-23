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

import com.nucleon.porttasks.CourierTask;
import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.api.WorldEntity;
import net.runelite.api.WorldView;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
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

	public static void drawPortTaskLinesOnWorld(Graphics2D graphics, Client client, CourierTask task, TracerConfig tracerConfig, boolean offset, int clip)
	{
		if (tracerConfig.isTracerEnabled())
		{
			renderTaskLinesTracer(graphics, client, task, clip, offset, tracerConfig);
		}
		else
		{
			renderTaskLines(graphics, client, task, clip, offset);
		}
	}

	private static void renderTaskLines(Graphics2D g, Client client, CourierTask task, int clip, boolean offset)
	{
		int heightOffset = offset ? (task.getSlot() * 100) : 0;
		clip += (clip * 1000);
		WorldView playerWorldView = client.getLocalPlayer().getWorldView();
		WorldPoint playerWorldPoint = client.getLocalPlayer().getWorldLocation();
		if (playerWorldView == null || playerWorldPoint == null)
		{
			return;
		}

		WorldPoint boatMainWorldPoint = null;
		if (!playerWorldView.isTopLevel() && playerWorldView.getId() != -1)
		{
			WorldEntity playerWorldEntity = client.getTopLevelWorldView().worldEntities().byIndex(playerWorldView.getId());
			if (playerWorldEntity != null)
			{
				boatMainWorldPoint = WorldPoint.fromLocalInstance(client, playerWorldEntity.getLocalLocation());
			}

			if (boatMainWorldPoint != null)
			{
				List<WorldPoint> journey = task.getData().dockMarkers.getFullPath();
				Color overlayColor = task.getOverlayColor();
				LocalPoint boatMainLocalPoint = WorldPerspective.worldToLocal(client, boatMainWorldPoint);

				for (int i = 0; i < journey.size() - 1; i++)
				{
					renderLineWorld(g, client, boatMainWorldPoint, boatMainLocalPoint, journey.get(i), heightOffset, journey.get(i + 1), heightOffset, overlayColor, 2, (float) clip);
				}
			}
		}
	}
	private static void renderTaskLinesTracer(Graphics2D g, Client client, CourierTask task, int clip, boolean offset, TracerConfig tracerConfig)
	{
		int heightOffset = offset ? (task.getSlot() * 100) : 0;
		clip += (clip * 1000);
		WorldView playerWorldView = client.getLocalPlayer().getWorldView();
		WorldPoint playerWorldPoint = client.getLocalPlayer().getWorldLocation();
		if (playerWorldView == null || playerWorldPoint == null)
		{
			return;
		}

		WorldPoint boatMainWorldPoint = null;
		if (!playerWorldView.isTopLevel() && playerWorldView.getId() != -1)
		{
			WorldEntity playerWorldEntity = client.getTopLevelWorldView().worldEntities().byIndex(playerWorldView.getId());
			if (playerWorldEntity != null)
			{
				boatMainWorldPoint = WorldPoint.fromLocalInstance(client, playerWorldEntity.getLocalLocation());
			}

			if (boatMainWorldPoint != null)
			{
				List<WorldPoint> journey = task.getData().dockMarkers.getFullPath();
				Color overlayColor = task.getOverlayColor();
				LocalPoint boatMainLocalPoint = WorldPerspective.worldToLocal(client, boatMainWorldPoint);

				for (int i = 0; i < journey.size() - 1; i++)
				{
					renderLineWorld(g, client, boatMainWorldPoint, boatMainLocalPoint, journey.get(i), heightOffset, journey.get(i + 1), heightOffset, overlayColor, 2, (float) clip, tracerConfig);
				}
			}
		}
	}

	private static Color dimColor(Color color, float factor)
	{
		factor = Math.min(Math.max(factor, 0f), 1f);
		int r = (int)(color.getRed() * factor);
		int g = (int)(color.getGreen() * factor);
		int b = (int)(color.getBlue() * factor);
		return new Color(r, g, b, color.getAlpha());
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

	private static void renderLineWorld(final Graphics2D graphics, Client client, final WorldPoint refWp, final LocalPoint refLp, final WorldPoint startWp, int startHeight, final WorldPoint endWp, int endHeight, final Color c, final float lineWidth, final float distanceClip)
	{
		// Now convert the start and end into local points based on an offset
		LocalPoint startLp = new LocalPoint(refLp.getX() + (startWp.getX() - refWp.getX()) * Perspective.LOCAL_TILE_SIZE, refLp.getY() + (startWp.getY() - refWp.getY()) * Perspective.LOCAL_TILE_SIZE, -1);
		LocalPoint endLp   = new LocalPoint(refLp.getX() + (  endWp.getX() - refWp.getX()) * Perspective.LOCAL_TILE_SIZE, refLp.getY() + (  endWp.getY() - refWp.getY()) * Perspective.LOCAL_TILE_SIZE, -1);

		renderLineLocal(graphics, client, startLp, startHeight, endLp, endHeight, c, lineWidth, distanceClip);
	}

private static void renderLineWorld(Graphics2D graphics, Client client, WorldPoint refWp, LocalPoint refLp, WorldPoint startWp, int startHeight, WorldPoint endWp, int endHeight, Color c, float lineWidth, float distanceClip, TracerConfig tracerConfig)
{
	List<WorldPoint> fullInterp = interpolateLine(startWp, endWp);
	if (fullInterp.isEmpty())
	{
		return;
	}

	final int CHUNK_SIZE = 50;
	int totalPoints = fullInterp.size();
	int totalSegments = totalPoints - 1;
	if (totalSegments <= 0)
	{
		return;
	}

	int pulse = tracerConfig.getFrameTick() % CHUNK_SIZE;

	for (int i = 0; i < totalSegments; i++)
	{
		int chunkIndex = i / CHUNK_SIZE;
		int segmentInChunk = i % CHUNK_SIZE;
		int segmentsRemaining = totalSegments - chunkIndex * CHUNK_SIZE;
		int segmentsInChunk = Math.min(CHUNK_SIZE, segmentsRemaining);

		boolean isPulse;
		if (segmentsInChunk == CHUNK_SIZE)
		{
			int reversedIndex = CHUNK_SIZE - 1 - segmentInChunk;
			isPulse = ((reversedIndex + 1) % CHUNK_SIZE) == pulse;
		}
		else
		{
			int scaledPulse = (pulse * segmentsInChunk) / CHUNK_SIZE;
			int reversedIndex = segmentsInChunk - 1 - scaledPulse;
			isPulse = segmentInChunk == reversedIndex;
		}

		WorldPoint wp1 = fullInterp.get(i);
		WorldPoint wp2 = fullInterp.get(i + 1);

		LocalPoint lp1 = new LocalPoint(
				refLp.getX() + (wp1.getX() - refWp.getX()) * Perspective.LOCAL_TILE_SIZE,
				refLp.getY() + (wp1.getY() - refWp.getY()) * Perspective.LOCAL_TILE_SIZE,
				-1
		);

		LocalPoint lp2 = new LocalPoint(
				refLp.getX() + (wp2.getX() - refWp.getX()) * Perspective.LOCAL_TILE_SIZE,
				refLp.getY() + (wp2.getY() - refWp.getY()) * Perspective.LOCAL_TILE_SIZE,
				-1
		);

		Color col = isPulse
				? c
				: dimColor(c, tracerConfig.getTracerIntensity());

		renderLineLocal(graphics, client, lp1, startHeight, lp2, endHeight, col, lineWidth, distanceClip);
	}
}




private static void renderLineLocal(final Graphics2D graphics, Client client, final LocalPoint start, int startHeight, final LocalPoint end, int endHeight, final Color c, final float lineWidth, final float distanceClip)
	{
		Polygon poly = getLinePoly(client, start, startHeight, end, endHeight, distanceClip);

		if (poly != null)
		{
			OverlayUtil.renderPolygon(graphics, poly, c, c, new BasicStroke(lineWidth));
		}
	}

	public static Point cameraToCanvas(@Nonnull Client client, float x, float y, float z)
	{
		final float viewportXMiddle = client.getViewportWidth() / 2f;
		final float viewportYMiddle = client.getViewportHeight() / 2f;
		final float viewportXOffset = client.getViewportXOffset();
		final float viewportYOffset = client.getViewportYOffset();

		final float zoom3d = client.getScale();

		int viewX, viewY;
		float sX, sY;

		if (z < 0)
		{
			// Never happens since we clip to the near plane, but a failsafe just in case
			return null;
		}
		else
		{
			sX = (viewportXMiddle + (x * zoom3d) / z);
			sY = (viewportYMiddle + (y * zoom3d) / z);
		}

		viewX = Math.round(sX + viewportXOffset);
		viewY = Math.round(sY + viewportYOffset);

		return new Point(viewX, viewY);
	}

	public static float[] localToCamera(@Nonnull Client client, float x, float y, float z)
	{
		final double cameraPitch = client.getCameraFpPitch();
		final double cameraYaw = client.getCameraFpYaw();

		final float pitchSin = (float) Math.sin(cameraPitch);
		final float pitchCos = (float) Math.cos(cameraPitch);
		final float yawSin = (float) Math.sin(cameraYaw);
		final float yawCos = (float) Math.cos(cameraYaw);

		final float cx = (float) client.getCameraFpX();
		final float cy = (float) client.getCameraFpY();
		final float cz = (float) client.getCameraFpZ();

		x -= cx;
		y -= cy;
		z -= cz;

		float x1 = x * yawCos + y * yawSin;
		float y1 = y * yawCos - x * yawSin;
		float y2 = z * pitchCos - y1 * pitchSin;
		float z1 = y1 * pitchCos + z * pitchSin;

		return new float[] { x1, y2, z1 };
	}

	private static void lerpPos(float[] srcDst, float[] target, float amount)
	{
		srcDst[0] = srcDst[0] + (target[0] - srcDst[0]) * amount;
		srcDst[1] = srcDst[1] + (target[1] - srcDst[1]) * amount;
		srcDst[2] = srcDst[2] + (target[2] - srcDst[2]) * amount;
	}

	private static Polygon getLinePoly(@Nonnull Client client, @Nonnull LocalPoint localLocation1, int z1, @Nonnull LocalPoint localLocation2, int z2, float distanceClip)
	{
		final int x1 = localLocation1.getX();
		final int y1 = localLocation1.getY();

		final int x2 = localLocation2.getX();
		final int y2 = localLocation2.getY();

		// First turn it into cameraspace so we can clip to the near plane, before turning it into screenspace
		float[] p1Cam = localToCamera(client, x1, y1, z1);
		float[] p2Cam = localToCamera(client, x2, y2, z2);

		final float near = 50; // arbitrary near plane

		if (p1Cam[2] < near && p2Cam[2] < near)
		{
			// both points behind cam, skip this line entirely
			return null;
		}

		if (distanceClip > 0)
		{
			// find points along the line that are x distance away
			final float cx = (float)client.getCameraFpX();
			final float cy = (float)client.getCameraFpY();

			final double rayX = x2 - x1;
			final double rayY = y2 - y1;

			final double camToAX = x1 - (int)cx;
			final double camToAY = y1 - (int)cy;

			// so we dont have to sqrt
			final double distanceClipSquared = distanceClip * distanceClip;
			final double p1DistanceSquared = Math.pow(cx - x1, 2) + Math.pow(cy - y1, 2);
			final double p2DistanceSquared = Math.pow(cx - x2, 2) + Math.pow(cy - y2, 2);

			// Stop calculating if they're both in bounds
			if (p1DistanceSquared > distanceClipSquared || p2DistanceSquared > distanceClipSquared)
			{
				// do some math to figure out the proportions along the line where the distance would be equal to the clip distance
				// sorry this math is messy but at least it works
				final double rayDistSquared = Math.pow(rayX, 2) + Math.pow(rayY, 2);

				final double camDotRay = 2f * ((camToAX * rayX) + (camToAY * rayY));
				final double p1DistanceDifferenceSquared = p1DistanceSquared - distanceClipSquared;

				final double discriminantSquared = Math.pow(camDotRay, 2) - 4.0 * rayDistSquared * p1DistanceDifferenceSquared;

				// If discriminant <= 0, no intersections, discard the line
				if (discriminantSquared <= 0)
				{
					return null;
				}

				final double discriminant = Math.sqrt(discriminantSquared);

				// two possible intersection ratios
				final double tIntersection1 = (-camDotRay - discriminant) / (2 * rayDistSquared);
				final double tIntersection2 = (-camDotRay + discriminant) / (2 * rayDistSquared);

				// intersection happens exactly on a tangent, or both are past either endpoint, discard this line
				if (tIntersection1 == tIntersection2 || (tIntersection1 <= 0 && tIntersection2 <= 0) || (tIntersection1 >= 1 && tIntersection2 >= 1))
				{
					return null;
				}

				final float tIntersectionClosestToA = (float)Math.min(tIntersection1, tIntersection2);
				final float tIntersectionClosestToB = (float)Math.max(tIntersection1, tIntersection2);

				// keep p1 original just so when we lerp it we're not lerping to the lerped point
				float[] p1CamOriginal = p1Cam.clone();

				if (p1DistanceSquared > distanceClipSquared && tIntersectionClosestToA > 0 && tIntersectionClosestToA < 1)
				{
					lerpPos(p1Cam, p2Cam, tIntersectionClosestToA);
				}

				if (p2DistanceSquared > distanceClipSquared && tIntersectionClosestToB > 0 && tIntersectionClosestToB < 1)
				{
					lerpPos(p2Cam, p1CamOriginal, 1 - tIntersectionClosestToB);
				}
			}
		}

		// If point is behind the cam, lerp the point between itself and p1 proportionally until it hits the intersection
		if (p1Cam[2] < near)
		{
			lerpPos(p1Cam, p2Cam, 1 - ((near - p2Cam[2]) / (p1Cam[2] - p2Cam[2])));
			p1Cam[2] = near;
		}

		if (p2Cam[2] < near)
		{
			lerpPos(p2Cam, p1Cam, 1 - ((near - p1Cam[2]) / (p2Cam[2] - p1Cam[2])));
			p2Cam[2] = near;
		}

		Point p1 = cameraToCanvas(client, p1Cam[0], p1Cam[1], p1Cam[2]);
		Point p2 = cameraToCanvas(client, p2Cam[0], p2Cam[1], p2Cam[2]);

		if (p1 == null || p2 == null)
		{
			return null;
		}

		Polygon poly = new Polygon();
		poly.addPoint(p1.getX(), p1.getY());
		poly.addPoint(p2.getX(), p2.getY());

		return poly;
	}
}
