package com.nucleon.porttasks.ui.adapters;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import com.nucleon.porttasks.PortTask;
import com.nucleon.porttasks.SailingHelperPlugin;
import com.nucleon.porttasks.ui.PortTaskPanel;
import net.runelite.client.util.ImageUtil;

public class HidePortTaskSlotOverlay extends MouseAdapter
{
private final JLabel hideMarker;
private final PortTask portTask;
private final PortTaskPanel panel;
private final SailingHelperPlugin plugin;
private static final ImageIcon VISIBLE_HOVER_ICON;
private static final ImageIcon INVISIBLE_HOVER_ICON;

static
{
	final BufferedImage visibleImg = ImageUtil.loadImageResource(SailingHelperPlugin.class, "visible_icon.png");
	VISIBLE_HOVER_ICON = new ImageIcon(ImageUtil.alphaOffset(visibleImg, -100));

	final BufferedImage invisibleImg = ImageUtil.loadImageResource(SailingHelperPlugin.class, "invisible_icon.png");
	INVISIBLE_HOVER_ICON = new ImageIcon(ImageUtil.alphaOffset(invisibleImg, -100));
}

public HidePortTaskSlotOverlay(JLabel hideMarker, PortTask portTask, PortTaskPanel panel, SailingHelperPlugin plugin)
{
	this.hideMarker = hideMarker;
	this.portTask = portTask;
	this.panel = panel;
	this.plugin = plugin;
}

@Override
public void mousePressed(MouseEvent mouseEvent)
{
	portTask.setTracking(!portTask.isTracking());
	panel.updateVisibility();
	plugin.saveSlotSettings();
}

@Override
public void mouseEntered(MouseEvent mouseEvent)
{
	hideMarker.setIcon(portTask.isTracking() ? VISIBLE_HOVER_ICON : INVISIBLE_HOVER_ICON);
}

@Override
public void mouseExited(MouseEvent mouseEvent)
{
	panel.updateVisibility();
}
}
