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
package com.nucleon.porttasks.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import com.nucleon.porttasks.PortTask;
import com.nucleon.porttasks.SailingHelperConfig;
import com.nucleon.porttasks.SailingHelperPlugin;
import com.nucleon.porttasks.ui.adapters.ReloadPortTasksAdapter;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.ui.components.PluginErrorPanel;
import net.runelite.client.util.ImageUtil;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import java.awt.image.BufferedImage;
import java.util.List;


public class SailingHelperPluginPanel extends PluginPanel
{
		private static final ImageIcon RELOAD_ICON;
		private final PluginErrorPanel errorPanel = new PluginErrorPanel();
		public final SailingHelperPlugin plugin;
		private final SailingHelperConfig config;
		private final JPanel markerView = new JPanel();

		static
		{
			final BufferedImage addIcon = ImageUtil.loadImageResource(SailingHelperPlugin.class, "reload.png");
			RELOAD_ICON = new ImageIcon(addIcon);
		}

		public SailingHelperPluginPanel(SailingHelperPlugin plugin, SailingHelperConfig config)
		{
			this.plugin = plugin;
			this.config = config;

			setLayout(new BorderLayout());
			setBorder(new EmptyBorder(10, 10, 10, 10));
			setupErrorPanel(true);

			// title panel
			JPanel northPanel = new JPanel(new BorderLayout());
			northPanel.setBorder(new EmptyBorder(1, 0, 10, 0));

			JPanel titlePanel = new JPanel(new BorderLayout());
			titlePanel.setBorder(new EmptyBorder(1, 3, 10, 7));

			JLabel title = new JLabel("Port Tasks", SwingConstants.CENTER);
			title.setHorizontalAlignment(SwingConstants.CENTER);
			title.setForeground(Color.WHITE);

			JLabel markerAdd = new JLabel(RELOAD_ICON);
			markerAdd.setToolTipText("reload");
			markerAdd.addMouseListener(new ReloadPortTasksAdapter(markerAdd, plugin, this::addMarker));

			JPanel markerButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 7, 3));
			markerButtons.add(markerAdd);

			titlePanel.add(title, BorderLayout.WEST);
			titlePanel.add(markerButtons, BorderLayout.EAST);
			northPanel.add(titlePanel, BorderLayout.NORTH);

			// marker view panels, these are dynamically added in rebuild()
			JPanel centerPanel = new JPanel(new BorderLayout());
			centerPanel.setBackground(ColorScheme.DARK_GRAY_COLOR);

			markerView.setLayout(new BoxLayout(markerView, BoxLayout.Y_AXIS));
			markerView.setBackground(ColorScheme.DARK_GRAY_COLOR);
			markerView.add(errorPanel);

			centerPanel.add(markerView, BorderLayout.NORTH);

			// setup panels border layout
			add(northPanel, BorderLayout.NORTH);
			add(centerPanel, BorderLayout.CENTER);
		}

		public void rebuild()
		{
			markerView.removeAll();
			List<PortTask> currentTasks = plugin.getCurrentTasks();
			for (PortTask task : currentTasks)
			{
				markerView.add(new PortTaskPanel(plugin, task, task.getSlot()));
				markerView.add(Box.createRigidArea(new Dimension(0, 10)));
			}
			if (currentTasks.isEmpty())
			{
				setupErrorPanel(true);
			}
			repaint();
			revalidate();
		}

		private void addMarker()
		{
			setupErrorPanel(false);
		}

		private void setupErrorPanel(boolean enabled)
		{
			PluginErrorPanel errorPanel = this.errorPanel;
			errorPanel.setVisible(enabled);
			if (enabled)
			{
				errorPanel.setContent("Port Tasks", "Click the 'reload' button to read the Port Task client data.");
				markerView.removeAll();
				markerView.setLayout(new BoxLayout(markerView, BoxLayout.Y_AXIS));
				markerView.setBackground(ColorScheme.DARK_GRAY_COLOR);
				markerView.add(errorPanel);
			}
		}
}