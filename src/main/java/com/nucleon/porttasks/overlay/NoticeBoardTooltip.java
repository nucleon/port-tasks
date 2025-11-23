package com.nucleon.porttasks.overlay;

import com.nucleon.porttasks.PortTasksPlugin;
import com.nucleon.porttasks.enums.BountyTaskData;
import com.nucleon.porttasks.enums.CourierTaskData;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Map;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.MenuEntry;
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
		MenuEntry[] menuEntries = client.getMenuEntries();
		int last = menuEntries.length - 1;

		if (last < 0)
		{
			return null;
		}

		MenuEntry menuEntry = menuEntries[last];
		String option = menuEntry.getOption();
		if (!option.contains("Select"))
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
			String tooltip = String.format(
				"Source: %s%s%s<br>" +
				"Destination: %s<br>" +
				"Amount of cargo: %d",
				sourceColorTag,
				data.getCargoLocation(),
				endTag,
				data.getDeliveryLocation(),
				data.getCargoAmount()
			);
			tooltipManager.add(new Tooltip(tooltip));
		}
		if (task instanceof BountyTaskData)
		{
			BountyTaskData data = (BountyTaskData) task;
			String tooltip = String.format(
				"Items required: %d<br>" +
				"Item rarity: 1 in %d",
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
		for (Map.Entry<Integer, Widget> entry : plugin.getOfferedTasks().entrySet())
		{
			Integer dbrow = entry.getKey();
			Widget w = entry.getValue();

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
}
