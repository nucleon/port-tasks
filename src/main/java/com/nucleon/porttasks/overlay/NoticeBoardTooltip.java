package com.nucleon.porttasks.overlay;

import com.nucleon.porttasks.OfferedTaskData;
import com.nucleon.porttasks.PortTasksPlugin;
import com.nucleon.porttasks.enums.BountyTaskData;
import com.nucleon.porttasks.enums.CourierTaskData;
import com.nucleon.porttasks.enums.TaskReward;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Map;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Point;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.widgets.Widget;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.tooltip.Tooltip;
import net.runelite.client.ui.overlay.tooltip.TooltipManager;

@Slf4j
public class NoticeBoardTooltip extends Overlay
{
	private final TooltipManager tooltipManager;
	private final Client client;
	private final PortTasksPlugin plugin;


	@Inject
	NoticeBoardTooltip(Client client, TooltipManager tooltipManager, PortTasksPlugin plugin)
	{
		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.ABOVE_WIDGETS);
		drawAfterInterface(InterfaceID.TOPLEVEL_DISPLAY);
		this.client = client;
		this.tooltipManager = tooltipManager;
		this.plugin = plugin;
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		Widget widget = client.getWidget(InterfaceID.PortTaskBoard.CONTAINER);
		if (widget == null || widget.isHidden())
		{
			return null;
		}

		// The world map obscures the notice board. Hide tooltips when world map is open
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

		Integer dbrow = getHoveredTask();
		if (dbrow == null)
		{
			return null;
		}
		Object task = getTask(dbrow);

		if (task instanceof CourierTaskData)
		{
			CourierTaskData data = (CourierTaskData) task;
			Color isAtCurLocation = data.getNoticeBoard() == data.getCargoLocation() ? Color.WHITE : Color.RED;
			String sourceColorTag = toColTag(isAtCurLocation);
			String endTag = "</col>";
			int distance = (int) Math.round(data.getDockMarkers().getDistance());

			double xpPerTileRatio = data.getXpPerTileRatio();
			int xpPerTilePercent = (int) Math.round(xpPerTileRatio * 100.0);
			Color xpColor = interpolateColor(plugin.getMinColor(), plugin.getMaxColor(), xpPerTileRatio);
			String xpColorTag = toColTag(xpColor);

			String tooltip = String.format(
				"Source: %s%s%s<br>" +
				"Destination: %s<br>" +
				"Experience: %s xp<br>" +
				"Distance: %d tiles<br>" +
				"XP/Tile: %s%d%%%s<br>" +
				"Amount of cargo: %d",
				sourceColorTag,
				data.getCargoLocation(),
				endTag,
				data.getDeliveryLocation(),
				TaskReward.getRewardForTask(data.getDbrow()),
				distance,
				xpColorTag,
				xpPerTilePercent,
				endTag,
				data.getCargoAmount()
			);
			tooltipManager.add(new Tooltip(tooltip));
		}
		if (task instanceof BountyTaskData)
		{
			BountyTaskData data = (BountyTaskData) task;
			String tooltip = String.format(
				"Experience: %s<br>" +
				"Items required: %d<br>" +
				"Item rarity: 1 in %d",
				TaskReward.getRewardForTask(data.getDbrow()),
				data.getItemQuantity(),
				data.getItemRarity()
			);
			tooltipManager.add(new Tooltip(tooltip));
		}
		return null;
	}

	private static String toColTag(Color c)
	{
		return String.format("<col=%02x%02x%02x>", c.getRed(), c.getGreen(), c.getBlue());
	}

	private Integer getHoveredTask()
	{
		Point mouse = client.getMouseCanvasPosition();
		for (Map.Entry<Integer, OfferedTaskData> entry : plugin.getOfferedTasks().entrySet())
		{
			Integer dbrow = entry.getKey();
			OfferedTaskData data = entry.getValue();
			Widget w = data.getTaskWidget();

			Rectangle bounds = w.getBounds();
			if (bounds != null & bounds.contains(mouse.getX(), mouse.getY()))
			{
				return dbrow;
			}
		}
		return null;
	}

	private Object getTask(int dbrow)
	{
		CourierTaskData courier = CourierTaskData.getByDbrow(dbrow);
		if (courier != null)
		{
			return courier;
		}

		BountyTaskData bounty = BountyTaskData.getByDbrow(dbrow);
		if (bounty != null)
		{
			return bounty;
		}
		return null;
	}

	private Color interpolateColor (Color min, Color max, double t)
	{
		t = Math.max(0.0, Math.min(1.0, t));

		int r = (int) Math.round(min.getRed()   + (max.getRed()   - min.getRed())   * t);
		int g = (int) Math.round(min.getGreen() + (max.getGreen() - min.getGreen()) * t);
		int b = (int) Math.round(min.getBlue()  + (max.getBlue()  - min.getBlue())  * t);

		return new Color(r, g, b);
	}
}
