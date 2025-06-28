package com.nucleon.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import com.nucleon.PortTask;
import com.nucleon.SailingHelperConfig;
import com.nucleon.SailingHelperPlugin;
import com.nucleon.ui.adapters.ReloadPortTasksAdapter;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.ui.components.PluginErrorPanel;
import net.runelite.client.util.ImageUtil;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
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

			JLabel title = new JLabel("Port Tasks");
			title.setForeground(Color.WHITE);

			JLabel markerAdd = new JLabel(RELOAD_ICON);
			markerAdd.setToolTipText("reload ");
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