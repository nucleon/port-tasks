package com.nucleon.porttasks;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.util.Set;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.Perspective;
import net.runelite.api.WorldView;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;

public class PortTaskModelRenderer extends Overlay
{
	private final Client client;
	private final PortTasksPlugin plugin;
	private final ModelOutlineRenderer modelOutlineRenderer;

	@Inject
	private PortTaskModelRenderer(final Client client, final PortTasksPlugin plugin, final ModelOutlineRenderer modelOutlineRenderer)
	{
		this.client = client;
		this.plugin = plugin;
		this.modelOutlineRenderer = modelOutlineRenderer;

		setPosition(OverlayPosition.DYNAMIC);
		setPriority(PRIORITY_DEFAULT);
		setLayer(OverlayLayer.ABOVE_SCENE);
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (plugin.isHighlightGangplanks())
		{
			highlightGangplanks(graphics);
		}
		if (plugin.isHighlightNoticeboards())
		{
			highlightNoticeboards(graphics);
		}
		return null;
	}

	private void highlightGangplanks(Graphics2D graphics)
	{
		final Set<GameObject> objects = plugin.getGangplanks();
		final Color color = plugin.getHighlightGangplanksColor();
		final Stroke stroke = new BasicStroke(1f);

		if (objects.isEmpty())
		{
			return;
		}
		for (GameObject object : objects)
		{
			WorldView wv = object.getWorldView();
			if (wv == null || object.getPlane() != wv.getPlane())
			{
				continue;
			}
			final Shape polygon = Perspective.getCanvasTilePoly(client, object.getLocalLocation());
			if (polygon == null)
			{
				continue;
			}
			OverlayUtil.renderPolygon(graphics, polygon, color, stroke);
		}
	}

	private void highlightNoticeboards(Graphics2D graphics)
	{
		final Set<GameObject> objects = plugin.getNoticeboards();
		final Color color = plugin.getHighlightNoticeboardsColor();
		final Stroke stroke = new BasicStroke(1f);
		if (objects.isEmpty())
		{
			return;
		}
		for (GameObject object : objects)
		{
			WorldView wv = object.getWorldView();
			if (wv == null || object.getPlane() != wv.getPlane())
			{
				continue;
			}
			final Shape polygon = object.getConvexHull();
			if (polygon == null)
			{
				continue;
			}
			OverlayUtil.renderPolygon(graphics, polygon, color, stroke);
		}
	}
}
