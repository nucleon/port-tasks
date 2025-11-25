package com.nucleon.porttasks;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.util.Set;
import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.GroundObject;
import net.runelite.api.ObjectComposition;
import net.runelite.api.Point;
import net.runelite.api.Scene;
import net.runelite.api.Tile;
import net.runelite.api.TileObject;
import net.runelite.api.WorldView;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;
@Slf4j
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
		if (plugin.isHighlightHelmMissingCargo())
		{
			for (CourierTask courierTask : plugin.courierTasks)
			{
				if (courierTask.getCargoTaken() > 0 && courierTask.getCargoTaken() < courierTask.getData().cargoAmount)
				{
					highlightLocalPlayerBoatHelm(graphics, (courierTask.getData().cargoAmount - courierTask.getCargoTaken()), courierTask.getOverlayColor());
				}
			}
		}
		if (plugin.isHighlightCargoHolds() && !plugin.isLockedIn())
		{
			highlightLocalPlayerCargoHold(graphics, plugin.getHighlightCargoHoldsColor());
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
			Scene scene = wv.getScene();
			Tile[][][] tiles = scene.getTiles();
			Tile tile = tiles[0][object.getLocalLocation().getSceneX()][object.getLocalLocation().getSceneY()];
			GroundObject groundObject = tile.getGroundObject();
			if (groundObject == null)
			{
				continue;
			}
			final Shape polygon = groundObject.getConvexHull();
			if (polygon == null)
			{
				continue;
			}
			OverlayUtil.renderPolygon(graphics, polygon, color, stroke);
		}
	}

	public void highlightLocalPlayerBoatHelm(Graphics2D graphics, int cargoMissing, Color color)
	{
		for (GameObject helm : plugin.getHelms())
		{
			modelOutlineRenderer.drawOutline(helm, 2, color, 250);
			drawObjectLabel(graphics, helm, cargoMissing);
		}
	}
	public void highlightLocalPlayerCargoHold(Graphics2D graphics, Color color)
	{
		for (GameObject cargoHold : plugin.getCargoHolds())
		{
			modelOutlineRenderer.drawOutline(cargoHold, 2, color, 250);
		}
	}

	private void drawObjectLabel(Graphics2D g, TileObject obj, int cargoMissing)
	{
		if (obj != null)
		{
			ObjectComposition composition = client.getObjectDefinition(obj.getId());
			Point loc = obj.getCanvasTextLocation(g, composition.getName(), 0);
			if (loc == null)
			{
				return;
			}
			String text = "missing " + cargoMissing + " cargo";
			OverlayUtil.renderTextLocation(g, loc, text, Color.WHITE);
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
