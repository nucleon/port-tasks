package com.nucleon.ui.adapters;


import com.nucleon.SailingHelperPlugin;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.util.ImageUtil;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class ReloadPortTasksAdapter extends MouseAdapter
{
	@Inject
	private ClientThread clientThread;

	private static final BufferedImage RELOAD_ICON = ImageUtil.loadImageResource(SailingHelperPlugin.class, "reload.png");
	private static final ImageIcon RELOAD = new ImageIcon(RELOAD_ICON);
	private static final ImageIcon ADD_HOVER_ICON = new ImageIcon(ImageUtil.alphaOffset(RELOAD_ICON, 0.53f));
	private final SailingHelperPlugin plugin;
	private final JLabel markerAdd;
	private final Runnable onClick;

	public ReloadPortTasksAdapter(JLabel markerAdd, SailingHelperPlugin plugin, Runnable onClick)
	{
		this.markerAdd = markerAdd;
		this.plugin = plugin;
		this.onClick = onClick;
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		clientThread.invoke(plugin::readPortDataFromClientVarps);
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
