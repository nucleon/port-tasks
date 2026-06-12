package com.nucleon.porttasks;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.time.Instant;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Point;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.ProgressPieComponent;

@Slf4j
public class DespawnTimerOverlay extends Overlay
{
	private final Client client;
	private final PortTasksPlugin plugin;
	private final PortTasksConfig config;

	@Inject
	private DespawnTimerOverlay(Client client, PortTasksPlugin plugin, PortTasksConfig config)
	{
		this.client = client;
		this.plugin = plugin;
		this.config = config;

		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.ABOVE_SCENE);
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		renderDeadNpcTimer(graphics);
		return null;
	}

	private void renderDeadNpcTimer(Graphics2D graphics)
	{
		Instant now = Instant.now();
		for (BountyCorpse tracker : plugin.bountyCorpses)
		{
			if (tracker.getNpc() == null)
			{
				continue;
			}

			float percent = ((float)tracker.getDespawnTime() - (now.toEpochMilli() - tracker.getStartTime().toEpochMilli())) / ((float)tracker.getDespawnTime());
			Point point = tracker.getNpc().getCanvasTextLocation(graphics, "",0);

			if (point == null || percent > 1.0f) {
				continue;
			}

			Color lerpedColor = lerpColor(Color.RED, Color.YELLOW, percent);
			ProgressPieComponent ppc = new ProgressPieComponent();
			ppc.setBorderColor(lerpedColor.darker());
			ppc.setFill(lerpedColor);
			ppc.setPosition(point);
			ppc.setProgress(percent);
			ppc.render(graphics);
		}
	}

	private Color lerpColor(Color start, Color end, float percent)
	{
		return new Color(
			lerp(start.getRed(), end.getRed(), percent),
			lerp(start.getGreen(), end.getGreen(), percent),
			lerp(start.getBlue(), end.getBlue(), percent),
			lerp(start.getAlpha(), end.getAlpha(), percent)
		);
	}

	private int lerp(int start, int end, float percent)
	{
		float clamped = Math.max(0f, Math.min(1f, percent));
		return (int) Math.round(start + (end - start) * clamped);
	}
}
