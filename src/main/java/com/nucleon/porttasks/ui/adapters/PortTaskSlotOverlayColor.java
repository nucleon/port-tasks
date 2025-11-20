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


import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import com.nucleon.porttasks.PortTasksPlugin;

import com.nucleon.porttasks.ui.TaskPanel;
import net.runelite.client.util.ImageUtil;

public class PortTaskSlotOverlayColor extends MouseAdapter
{
private final JLabel portSlotOverlay;
private final TaskPanel panel;
private static final ImageIcon BORDER_COLOR_ICON;
private static final ImageIcon BORDER_COLOR_HOVER_ICON;

static
{
	BufferedImage borderImg = ImageUtil.loadImageResource(PortTasksPlugin.class, "border_color_icon.png");
	BufferedImage borderImgHover = ImageUtil.luminanceOffset(borderImg, -150);

	BORDER_COLOR_ICON = new ImageIcon(borderImg);
	BORDER_COLOR_HOVER_ICON = new ImageIcon(borderImgHover);
}
public PortTaskSlotOverlayColor(JLabel prayerMarkerColorLabel, TaskPanel panel)
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
