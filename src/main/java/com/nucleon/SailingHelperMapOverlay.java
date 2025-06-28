package com.nucleon;

import com.nucleon.enums.PortPaths;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.List;

import javax.inject.Inject;

import com.nucleon.overlay.WorldLines;
import net.runelite.api.Client;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.Overlay;

import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

class SailingHelperMapOverlay extends Overlay
{
	private final Client client;
	private final SailingHelperPlugin plugin;
	private final SailingHelperConfig config;

	@Inject
	private SailingHelperMapOverlay(Client client, SailingHelperPlugin plugin, SailingHelperConfig config)
	{
		this.client = client;
		this.plugin = plugin;
		this.config = config;

		setPosition(OverlayPosition.DYNAMIC);
		setPriority(Overlay.PRIORITY_HIGHEST);
		setLayer(OverlayLayer.ABOVE_WIDGETS);
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		List<WorldPoint> linePoints = PortPaths.BRIMHAVEN_MUSA_POINT.getFullPath();
		renderOverlayLines(graphics, true, linePoints);

		return null;
	}

	private void renderOverlayLines(Graphics2D g, boolean map, List<WorldPoint> lp)
	{
		WorldLines.createWorldMapLines(g, client, lp, config.getNavColor());
	}
}