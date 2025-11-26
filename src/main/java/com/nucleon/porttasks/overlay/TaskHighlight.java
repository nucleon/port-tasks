package com.nucleon.porttasks.overlay;

import com.nucleon.porttasks.OfferedTaskData;
import com.nucleon.porttasks.PortTasksPlugin;
import com.nucleon.porttasks.WidgetTag;
import com.nucleon.porttasks.enums.BountyTaskData;
import com.nucleon.porttasks.enums.CourierTaskData;
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

		Map<Integer, OfferedTaskData> offered = plugin.getOfferedTasks();
		if (offered.isEmpty())
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


		for (Map.Entry<Integer, OfferedTaskData> entry : offered.entrySet())
		{
			int dbrow = entry.getKey();
			OfferedTaskData data = entry.getValue();
			Widget widget = data.getTaskWidget();
			if (widget == null || widget.isHidden())
			{
				continue;
			}
			BountyTaskData bounty = BountyTaskData.getByDbrow(dbrow);
			boolean isBounty = bounty != null;

			CourierTaskData courier = CourierTaskData.getByDbrow(dbrow);
			boolean isCourier = courier != null;

			Color tagColor = tagColors.get(dbrow);
			// Always draw a border if the task is tagged
			if (tagColor != null)
			{
				renderWidgetTag(graphics, widget, tagColor, 2);
			}
			// In order to inadvertently make the tint darker than intended
			// the first time the widget is hidden we should continue to the next widget
			else if (plugin.isNoticeBoardHideUntagged())
			{
				renderWidgetHider(graphics, widget, plugin.getNoticeBoardHideOpactity());
				continue;
			}
			if (plugin.isNoticeBoardHideIncompletable() && plugin.getSailingLevel() < data.getLevelRequired())
			{
				renderWidgetHider(graphics, widget, plugin.getNoticeBoardHideOpactity());
				continue;
			}
			if (plugin.isNoticeBoardHideBounty() && isBounty)
			{
				renderWidgetHider(graphics, widget, plugin.getNoticeBoardHideOpactity());
				continue;
			}
			if (plugin.isNoticeBoardHideCourier() && isCourier)
			{
				renderWidgetHider(graphics, widget, plugin.getNoticeBoardHideOpactity());
				continue;
			}
		}

		return null;
	}

	private static Rectangle renderWidgetTag(Graphics2D graphics, Widget widget, Color color, float borderWidth)
	{
		Rectangle widgetBounds = widget.getBounds();
		Stroke stroke = new BasicStroke(borderWidth);
		Color clear = new Color(0, 0, 0, 0);
		OverlayUtil.renderPolygon(graphics, rectangleToPolygon(widgetBounds), color, clear, stroke);
		return widgetBounds;
	}

	private static Rectangle renderWidgetHider(Graphics2D graphics, Widget widget, int opacity)
	{
		Rectangle widgetBounds = widget.getBounds();
		Stroke stroke = new BasicStroke(0);
		Color color = new Color(0, 0, 0, opacity);
		// No stroke, just fill
		Color transparent = new Color(0, 0, 0, 0);
		OverlayUtil.renderPolygon(graphics, rectangleToPolygon(widgetBounds), transparent, color, stroke);
		return widgetBounds;
	}

	private static Polygon rectangleToPolygon(Rectangle rectangle)
	{
		int[] x_points = {rectangle.x, rectangle.x + rectangle.width, rectangle.x + rectangle.width, rectangle.x};
		int[] y_points = {rectangle.y, rectangle.y, rectangle.y + rectangle.height, rectangle.y + rectangle.height};
		return new Polygon(x_points, y_points, 4);
	}
}
