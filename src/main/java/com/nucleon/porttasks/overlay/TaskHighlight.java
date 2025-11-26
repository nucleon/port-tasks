package com.nucleon.porttasks.overlay;

import com.nucleon.porttasks.PortTasksPlugin;
import com.nucleon.porttasks.WidgetTag;
import net.runelite.api.Client;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.widgets.Widget;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;

import javax.inject.Inject;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskHighlight extends Overlay {
	private final Client client;
	private final PortTasksPlugin plugin;

	@Inject
	private TaskHighlight(Client client, PortTasksPlugin plugin)
	{
		this.client = client;
		this.plugin = plugin;

		setPosition(OverlayPosition.DYNAMIC);
		setPriority(PRIORITY_HIGHEST);
		setLayer(OverlayLayer.ABOVE_WIDGETS);
	}

	@Override
	public Dimension render (Graphics2D graphics)
	{
		Widget taskBoard = client.getWidget(InterfaceID.PortTaskBoard.CONTAINER);
		if (taskBoard == null)
		{
			return null;
		}

		Widget worldMap = client.getWidget(InterfaceID.Worldmap.CONTENT);
		if (worldMap != null && !worldMap.isHidden())
		{
			return null;
		}

		Widget taskInfo = client.getWidget(InterfaceID.PortTaskInfo.WINDOW);
		if (taskInfo != null && !taskInfo.isHidden())
		{
			return null;
		}

		List<Widget> children = new ArrayList<>();
		if (taskBoard.getDynamicChildren() != null)
		{
			children.addAll(Arrays.asList(taskBoard.getDynamicChildren()));
		}

		Map<Integer, Widget> visibleWidgets = new HashMap<>();
		for (Widget child : children)
		{
			Integer dbrow = plugin.getDbrowFromWidget(child);
			if (dbrow == null)
			{
				continue;
			}
			visibleWidgets.put(dbrow, child);
		}

		if (visibleWidgets.isEmpty())
		{
			return null;
		}

		Map<Integer, Color> tagColors = new HashMap<>();
		for (WidgetTag tag : plugin.getWidgetTags())
		{
			if (tag != null)
			{
				tagColors.put(tag.getDbrow(), tag.getColor());
			}
		}

		for (Map.Entry<Integer, Widget> entry : visibleWidgets.entrySet())
		{
			Color color = tagColors.get(entry.getKey());
			if (color == null)
			{
				continue;
			}
			renderWidgetOverlay(graphics, entry.getValue(), color, 2);
		}

		return null;
	}

	private static Rectangle renderWidgetOverlay(Graphics2D graphics, Widget widget, Color color, float borderWidth)
	{
		Rectangle widgetBounds = widget.getBounds();
		Stroke stroke = new BasicStroke(borderWidth);
		Color clear = new Color(0, 0, 0, 0);
		OverlayUtil.renderPolygon(graphics, rectangleToPolygon(widgetBounds), color, clear, stroke);
		return widgetBounds;
	}

	private static Polygon rectangleToPolygon(Rectangle rectangle)
	{
		int[] x_points = {rectangle.x, rectangle.x + rectangle.width, rectangle.x + rectangle.width, rectangle.x};
		int[] y_points = {rectangle.y, rectangle.y, rectangle.y + rectangle.height, rectangle.y + rectangle.height};
		return new Polygon(x_points, y_points, 4);
	}
}
