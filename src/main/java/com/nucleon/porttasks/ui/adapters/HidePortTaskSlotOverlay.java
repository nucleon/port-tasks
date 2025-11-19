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

import com.nucleon.porttasks.CourierTask;
import com.nucleon.porttasks.PortTasksPlugin;
import com.nucleon.porttasks.ui.CourierTaskPanel;
import net.runelite.client.util.ImageUtil;

public class HidePortTaskSlotOverlay extends MouseAdapter
{
private final JLabel hideMarker;
private final CourierTask courierTask;
private final CourierTaskPanel panel;
private final PortTasksPlugin plugin;
private static final ImageIcon VISIBLE_HOVER_ICON;
private static final ImageIcon INVISIBLE_HOVER_ICON;

static
{
	final BufferedImage visibleImg = ImageUtil.loadImageResource(PortTasksPlugin.class, "visible_icon.png");
	VISIBLE_HOVER_ICON = new ImageIcon(ImageUtil.alphaOffset(visibleImg, -100));

	final BufferedImage invisibleImg = ImageUtil.loadImageResource(PortTasksPlugin.class, "invisible_icon.png");
	INVISIBLE_HOVER_ICON = new ImageIcon(ImageUtil.alphaOffset(invisibleImg, -100));
}

public HidePortTaskSlotOverlay(JLabel hideMarker, CourierTask courierTask, CourierTaskPanel panel, PortTasksPlugin plugin)
{
	this.hideMarker = hideMarker;
	this.courierTask = courierTask;
	this.panel = panel;
	this.plugin = plugin;
}

@Override
public void mousePressed(MouseEvent mouseEvent)
{
	courierTask.setTracking(!courierTask.isTracking());
	panel.updateVisibility();
	plugin.saveSlotSettings();
}

@Override
public void mouseEntered(MouseEvent mouseEvent)
{
	hideMarker.setIcon(courierTask.isTracking() ? VISIBLE_HOVER_ICON : INVISIBLE_HOVER_ICON);
}

@Override
public void mouseExited(MouseEvent mouseEvent)
{
	panel.updateVisibility();
}
}
