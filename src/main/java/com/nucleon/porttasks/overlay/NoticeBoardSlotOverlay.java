package com.nucleon.porttasks.overlay;

import com.nucleon.porttasks.PortTasksConfig;
import com.nucleon.porttasks.PortTasksPlugin;
import com.nucleon.porttasks.enums.CourierTaskData;
import com.nucleon.porttasks.optimizer.RecommendationType;
import com.nucleon.porttasks.optimizer.TaskRecommendation;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.widgets.Widget;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

/**
 * Overlay that highlights the recommended task slot on the noticeboard widget.
 * 
 * The noticeboard widget (941.3) has task slots at specific child indices:
 * - Slot 0: child 0
 * - Slot 1: child 6
 * - Slot 2: child 12
 * - Slot 3: child 18
 * - Slot 4: child 24
 * - Slot 5: child 30
 * - Slot 6: child 36
 * - Slot 7: child 42
 */
@Slf4j
public class NoticeBoardSlotOverlay extends Overlay
{
	private static final int STROKE_WIDTH = 2;
	
	private final Client client;
	private final PortTasksPlugin plugin;
	private final PortTasksConfig config;

	@Inject
	public NoticeBoardSlotOverlay(Client client, PortTasksPlugin plugin, PortTasksConfig config)
	{
		this.client = client;
		this.plugin = plugin;
		this.config = config;
		
		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.ABOVE_WIDGETS);
		drawAfterInterface(InterfaceID.TOPLEVEL_DISPLAY);
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (!config.highlightNoticeboardSlot())
		{
			return null;
		}
		
		Widget containerWidget = client.getWidget(InterfaceID.PortTaskBoard.CONTAINER);
		if (containerWidget == null || containerWidget.isHidden())
		{
			return null;
		}
		
		// Hide highlights when the task info window is open (user clicked on a task)
		Widget taskInfoWindow = client.getWidget(InterfaceID.PortTaskInfo.WINDOW);
		if (taskInfoWindow != null && !taskInfoWindow.isHidden())
		{
			return null;
		}
		
		// The world map obscures the notice board - hide highlights when world map is open
		Widget worldMap = client.getWidget(InterfaceID.Worldmap.CONTENT);
		if (worldMap != null && !worldMap.isHidden())
		{
			return null;
		}
		
		// Get the best recommendation to highlight
		List<TaskRecommendation> recommendations = plugin.getCurrentRecommendations();
		if (recommendations == null || recommendations.isEmpty())
		{
			return null;
		}
		
		// Find the best actionable recommendation (first non-SKIP)
		TaskRecommendation bestRec = findBestRecommendation(recommendations);
		if (bestRec == null)
		{
			return null;
		}
		
		// Find the widget for this task directly
		Widget taskWidget = findTaskWidget(bestRec.getTask());
		if (taskWidget == null || taskWidget.isHidden())
		{
			return null;
		}
		
		// Draw the highlight
		Color highlightColor = getHighlightColor(bestRec.getType());
		drawSlotHighlight(graphics, taskWidget, highlightColor);
		
		return null;
	}
	
	/**
	 * Find the best actionable recommendation to highlight.
	 * Returns the first actionable recommendation (they're already sorted by score).
	 * Skips SKIP and UNAVAILABLE types as they are not actionable.
	 */
	private TaskRecommendation findBestRecommendation(
		List<TaskRecommendation> recommendations)
	{
		for (TaskRecommendation rec : recommendations)
		{
			RecommendationType type = rec.getType();
			if (type != RecommendationType.SKIP &&
				type != RecommendationType.UNAVAILABLE)
			{
				return rec;
			}
		}
		return null;
	}
	
	/**
	 * Find the widget for a task on the noticeboard.
	 * 
	 * @param taskData The task to find
	 * @return The widget for this task, or null if not found
	 */
	private Widget findTaskWidget(CourierTaskData taskData)
	{
		Map<Integer, Widget> offeredTasks = plugin.getOfferedTasks();
		if (offeredTasks == null || offeredTasks.isEmpty())
		{
			return null;
		}
		
		// Find the widget for this task by matching the dbrow
		for (Map.Entry<Integer, Widget> entry : offeredTasks.entrySet())
		{
			CourierTaskData entryTask = CourierTaskData.getByDbrow(entry.getKey());
			if (entryTask == taskData)
			{
				return entry.getValue();
			}
		}
		
		return null;
	}
	
	/**
	 * Get the highlight color for a recommendation type.
	 */
	private Color getHighlightColor(RecommendationType type)
	{
		switch (type)
		{
			case TAKE_NOW:
				return new Color(0, 255, 0, 180); // Bright green
			case SWAP:
				return new Color(255, 165, 0, 180); // Orange
			case CONSIDER:
				return new Color(255, 255, 0, 180); // Yellow
			case SKIP:
			default:
				return new Color(255, 0, 0, 180); // Red
		}
	}
	
	/**
	 * Draw a highlight around a widget slot.
	 */
	private void drawSlotHighlight(Graphics2D graphics, Widget widget, Color color)
	{
		Rectangle bounds = widget.getBounds();
		if (bounds == null)
		{
			return;
		}
		
		// Draw filled rectangle with transparency
		Color fillColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), 65);
		graphics.setColor(fillColor);
		graphics.fill(bounds);
		
		// Draw border
		graphics.setColor(color);
		graphics.setStroke(new BasicStroke(STROKE_WIDTH));
		graphics.draw(bounds);
	}
}
