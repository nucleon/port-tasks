package com.nucleon.porttasks.overlay;

import com.nucleon.porttasks.PortTasksPlugin;
import com.nucleon.porttasks.enums.BountyTaskData;
import com.nucleon.porttasks.enums.CourierTaskData;
import com.nucleon.porttasks.optimizer.RecommendationType;
import com.nucleon.porttasks.optimizer.TaskRecommendation;
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
		if (widget == null)
		{
			return null;
		}

		// Hide tooltips when the task info window is open (user clicked on a task)
		Widget taskInfoWindow = client.getWidget(InterfaceID.PortTaskInfo.WINDOW);
		if (taskInfoWindow != null && !taskInfoWindow.isHidden())
		{
			return null;
		}

		// The world map obscures the notice board. Hide tooltips when world map is open
		Widget worldMap = client.getWidget(InterfaceID.Worldmap.CONTENT);
		if (worldMap != null && worldMap.isHidden() == false)
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
			Color isAtCurLocation = ! data.isRoundTripTask() ? Color.WHITE : Color.RED;
			String sourceColorTag = toColTag(isAtCurLocation);
			String endTag = "</col>";
			int distance = (int) Math.round(data.getDockMarkers().getDistance());
			
			StringBuilder tooltipBuilder = new StringBuilder();
			tooltipBuilder.append(String.format(
				"Source: %s%s%s<br>" +
				"Destination: %s<br>" +
				"Experience: %s xp<br>" +
				"Distance: %d tiles<br>" +
				"Amount of cargo: %d",
				sourceColorTag,
				data.getCargoLocation(),
				endTag,
				data.getCargo().getDestination(),
				data.getReward(),
				distance,
				data.getCargoAmount()
			));
			
			// Add optimizer recommendation if available
			TaskRecommendation recommendation = findRecommendation(data);
			if (recommendation != null)
			{
				Color recColor = getRecommendationColor(recommendation.getType());
				String recColorTag = toColTag(recColor);
				
				tooltipBuilder.append("<br><br>");
				
				// Show recommendation type and reason
				// Special handling for SWAP recommendations
				if (recommendation.getType() == RecommendationType.SWAP
					&& recommendation.getSwapWithTask() != null)
				{
					String swapTaskName = toTitleCase(recommendation.getSwapWithTask().getData().getTaskName());
					String swapOutColorTag = toColTag(new Color(255, 100, 100)); // Light red
					tooltipBuilder.append(String.format("%s%s: %s%s%s<br>%sReason: %s%s", 
						recColorTag,
						recommendation.getType().getLabel(),
						swapOutColorTag,
						swapTaskName,
						endTag,
						recColorTag,
						recommendation.getReason(),
						endTag
					));
				}
				else
				{
					tooltipBuilder.append(String.format("%s%s%s: %s", 
						recColorTag,
						recommendation.getType().getLabel(),
						endTag,
						recommendation.getReason()
					));
				}
				
				// Add route previews if available
				if (recommendation.getCurrentRoutePreview() != null && !recommendation.getCurrentRoutePreview().isEmpty())
				{
					String currentRouteColorTag = toColTag(new Color(255, 180, 180)); // Light red for current route
					tooltipBuilder.append(String.format("<br>%sCurrent: %s%s",
						currentRouteColorTag,
						recommendation.getCurrentRoutePreview(),
						endTag
					));
				}
				if (recommendation.getRoutePreview() != null && !recommendation.getRoutePreview().isEmpty())
				{
					String routeColorTag = toColTag(new Color(180, 255, 180)); // Light green for new route
					tooltipBuilder.append(String.format("<br>%sNew: %s%s",
						routeColorTag,
						recommendation.getRoutePreview(),
						endTag
					));
				}
			}
			
			tooltipManager.add(new Tooltip(tooltipBuilder.toString()));
		}
		if (task instanceof BountyTaskData)
		{
			BountyTaskData data = (BountyTaskData) task;
			String tooltip = String.format(
				"Experience: %s<br>" +
				"Items required: %d<br>" +
				"Item rarity: 1 in %d",
				data.getReward(),
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
	
	/**
	 * Convert a string to Title Case.
	 */
	private static String toTitleCase(String input)
	{
		if (input == null || input.isEmpty())
		{
			return input;
		}
		
		StringBuilder result = new StringBuilder();
		boolean capitalizeNext = true;
		
		for (char c : input.toCharArray())
		{
			if (Character.isWhitespace(c))
			{
				capitalizeNext = true;
				result.append(c);
			}
			else if (capitalizeNext)
			{
				result.append(Character.toUpperCase(c));
				capitalizeNext = false;
			}
			else
			{
				result.append(c);
			}
		}
		
		return result.toString();
	}
	
	/**
	 * Get color for recommendation type.
	 */
	private Color getRecommendationColor(RecommendationType type)
	{
		switch (type)
		{
			case TAKE_NOW:
				return new Color(0, 255, 0); // Bright green
			case SWAP:
				return new Color(255, 165, 0); // Orange
			case CONSIDER:
				return new Color(255, 255, 0); // Yellow
			case SKIP:
				return new Color(255, 0, 0); // Red
			default:
				return Color.WHITE;
		}
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
	
	/**
	 * Find recommendation for a specific task.
	 */
	private TaskRecommendation findRecommendation(CourierTaskData taskData)
	{
		if (plugin.getCurrentRecommendations() == null)
		{
			return null;
		}
		
		for (TaskRecommendation rec : plugin.getCurrentRecommendations())
		{
			if (rec.getTask() == taskData)
			{
				return rec;
			}
		}
		return null;
	}
}
