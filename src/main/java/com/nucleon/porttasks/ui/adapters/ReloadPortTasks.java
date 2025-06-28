/*
 * Copyright (c) 2025, nucleon <https://github.com/nucleon>
 * Copyright (c) 2025, Cooper Morris <https://github.com/coopermor>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
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
