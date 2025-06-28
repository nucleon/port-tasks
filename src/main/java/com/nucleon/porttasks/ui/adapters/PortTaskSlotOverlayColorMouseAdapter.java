package com.nucleon.porttasks.ui.adapters;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;


import com.nucleon.porttasks.SailingHelperPlugin;
import com.nucleon.porttasks.ui.PortTaskPanel;
import net.runelite.client.util.ImageUtil;

public class PortTaskSlotOverlayColorMouseAdapter extends MouseAdapter
{
private final JLabel portSlotOverlay;
private final PortTaskPanel panel;
private static final ImageIcon BORDER_COLOR_ICON;
private static final ImageIcon BORDER_COLOR_HOVER_ICON;

static
{
	BufferedImage borderImg = ImageUtil.loadImageResource(SailingHelperPlugin.class, "border_color_icon.png");
	BufferedImage borderImgHover = ImageUtil.luminanceOffset(borderImg, -150);

	BORDER_COLOR_ICON = new ImageIcon(borderImg);
	BORDER_COLOR_HOVER_ICON = new ImageIcon(borderImgHover);
}
public PortTaskSlotOverlayColorMouseAdapter(JLabel prayerMarkerColorLabel, PortTaskPanel panel)
{
	this.portSlotOverlay = prayerMarkerColorLabel;
	this.panel = panel;
}

@Override
public void mousePressed(MouseEvent mouseEvent)
{
	panel.openPortTaskColorPicker();
}

@Override
public void mouseEntered(MouseEvent mouseEvent)
{
	portSlotOverlay.setIcon(BORDER_COLOR_HOVER_ICON);
}

@Override
public void mouseExited(MouseEvent mouseEvent)
{
	portSlotOverlay.setIcon(BORDER_COLOR_ICON);
}
}
