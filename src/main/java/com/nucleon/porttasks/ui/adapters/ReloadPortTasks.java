package com.nucleon.porttasks.ui.adapters;


import com.nucleon.porttasks.PortTasksPlugin;
import net.runelite.client.util.ImageUtil;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class ReloadPortTasks extends MouseAdapter
{
	private static final BufferedImage RELOAD_ICON = ImageUtil.loadImageResource(PortTasksPlugin.class, "reload.png");
	private static final ImageIcon RELOAD = new ImageIcon(RELOAD_ICON);
	private static final ImageIcon ADD_HOVER_ICON = new ImageIcon(ImageUtil.alphaOffset(RELOAD_ICON, 0.53f));
	private final PortTasksPlugin plugin;
	private final JLabel markerAdd;
	private final Runnable onClick;

	public ReloadPortTasks(JLabel markerAdd, PortTasksPlugin plugin, Runnable onClick)
	{
		this.markerAdd = markerAdd;
		this.plugin = plugin;
		this.onClick = onClick;
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		//clientThread.invoke(plugin::readPortDataFromClientVarps); //todo:actually call from ct thats passed in
		onClick.run();
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		markerAdd.setIcon(ADD_HOVER_ICON);
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		markerAdd.setIcon(RELOAD);
	}
}
