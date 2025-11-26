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

import com.nucleon.porttasks.BountyTask;
import com.nucleon.porttasks.CourierTask;
import com.nucleon.porttasks.PortTasksConfig;
import com.nucleon.porttasks.PortTasksPlugin;
import com.nucleon.porttasks.Task;
import com.nucleon.porttasks.enums.PortPaths;
import com.nucleon.porttasks.optimizer.RecommendationType;
import com.nucleon.porttasks.optimizer.TaskRecommendation;
import com.nucleon.porttasks.ui.adapters.ReloadPortTasks;

import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.ui.components.PluginErrorPanel;
import net.runelite.client.util.ImageUtil;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;


public class PortTasksPluginPanel extends PluginPanel {
    private static final String ERROR_PANEL_RELOAD_INSTRUCTION = "Click the 'reload' button to read the Port Task client data.";
    private static final String ERROR_PANEL_LOGIN_INSTRUCTION = "Please log in to the game to read the Port Task client data.";

    private static final ImageIcon RELOAD_ICON;
    private final PluginErrorPanel errorPanel = new PluginErrorPanel();
    public final PortTasksPlugin plugin;
    private final PortTasksConfig config;
    private final JPanel markerView = new JPanel();
    private final JPanel statusPanel = new JPanel(new BorderLayout());
    private final JLabel modeIndicator = new JLabel();
    private final JLabel capacityIndicator = new JLabel();
	private final JLabel nextStopLabel = new JLabel();
	private final JPanel gpsPanel = new JPanel();
	private final JPanel noticeboardPanel = new JPanel();
	private final JLabel noticeboardActionLabel = new JLabel();
    private final JLabel reloadButton = new JLabel(RELOAD_ICON);
    private ClientThread clientThread;
    private ItemManager itemManager;
    private Client client;

    static {
        final BufferedImage addIcon = ImageUtil.loadImageResource(PortTasksPlugin.class, "reload.png");
        RELOAD_ICON = new ImageIcon(addIcon);
    }

    public PortTasksPluginPanel(PortTasksPlugin plugin, ClientThread clientThread, ItemManager itemManager, Client client, PortTasksConfig config) {
        this.plugin = plugin;
        this.config = config;
        this.clientThread = clientThread;
        this.itemManager = itemManager;
        this.client = client;
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

        reloadButton.setToolTipText("reload");
        reloadButton.addMouseListener(new ReloadPortTasks(reloadButton, plugin, clientThread, this::addMarker));
        reloadButton.setVisible(false); // Hidden until logged in

        JPanel markerButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 7, 3));
        markerButtons.add(reloadButton);

        titlePanel.add(title, BorderLayout.WEST);
        titlePanel.add(markerButtons, BorderLayout.EAST);
        northPanel.add(titlePanel, BorderLayout.NORTH);

        // Add mode and capacity indicators
        statusPanel.setBorder(new EmptyBorder(0, 3, 5, 7));
        statusPanel.setBackground(ColorScheme.DARKER_GRAY_COLOR);

        modeIndicator.setHorizontalAlignment(SwingConstants.CENTER);
        modeIndicator.setForeground(Color.LIGHT_GRAY);
        capacityIndicator.setHorizontalAlignment(SwingConstants.CENTER);
        capacityIndicator.setForeground(Color.LIGHT_GRAY);

        statusPanel.add(modeIndicator, BorderLayout.NORTH);
        statusPanel.add(capacityIndicator, BorderLayout.CENTER);
        statusPanel.setVisible(false);
        northPanel.add(statusPanel, BorderLayout.CENTER);

        gpsPanel.setLayout(new BorderLayout());
        gpsPanel.setBorder(new EmptyBorder(5, 3, 5, 7));
        gpsPanel.setBackground(new Color(40, 55, 71)); // Dark blue-gray for GPS feel

        JLabel gpsTitle = new JLabel("GPS Navigation");
        gpsTitle.setForeground(new Color(0, 200, 200)); // Cyan to match GPS route color
        gpsTitle.setHorizontalAlignment(SwingConstants.CENTER);
        gpsTitle.setBorder(new EmptyBorder(0, 0, 3, 0));

        nextStopLabel.setHorizontalAlignment(SwingConstants.CENTER);
        nextStopLabel.setForeground(Color.WHITE);
        nextStopLabel.setText("No route calculated");

		gpsPanel.add(gpsTitle, BorderLayout.NORTH);
		gpsPanel.add(nextStopLabel, BorderLayout.CENTER);
		gpsPanel.setVisible(client.getGameState() == GameState.LOGGED_IN && config.gpsRouteMode());

		// Noticeboard recommendation panel - shown when noticeboard is open
		noticeboardPanel.setLayout(new BorderLayout());
		noticeboardPanel.setBorder(new EmptyBorder(5, 3, 5, 7));
		noticeboardPanel.setBackground(new Color(55, 45, 35)); // Warm brown for noticeboard feel

		JLabel noticeboardTitle = new JLabel("Noticeboard Advisor");
		noticeboardTitle.setForeground(new Color(255, 200, 100)); // Warm yellow/gold
		noticeboardTitle.setHorizontalAlignment(SwingConstants.CENTER);
		noticeboardTitle.setBorder(new EmptyBorder(0, 0, 3, 0));

		noticeboardActionLabel.setHorizontalAlignment(SwingConstants.CENTER);
		noticeboardActionLabel.setForeground(Color.WHITE);
		noticeboardActionLabel.setText("Scanning tasks...");

		noticeboardPanel.add(noticeboardTitle, BorderLayout.NORTH);
		noticeboardPanel.add(noticeboardActionLabel, BorderLayout.CENTER);
		noticeboardPanel.setVisible(false);

		// Use a wrapper panel to hold both GPS and noticeboard panels
		JPanel southPanels = new JPanel();
		southPanels.setLayout(new BoxLayout(southPanels, BoxLayout.Y_AXIS));
		southPanels.setBackground(ColorScheme.DARKER_GRAY_COLOR);
		southPanels.add(gpsPanel);
		southPanels.add(noticeboardPanel);
		northPanel.add(southPanels, BorderLayout.SOUTH);

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

        if (plugin.developerMode) {
            addDeveloperPanel();
        }
    }

    @SuppressWarnings("unused")
    @Subscribe
    public void onGameStateChanged(final GameStateChanged event) {
        switch (event.getGameState()) {
            case LOGGED_IN:
                reloadButton.setVisible(true);
                gpsPanel.setVisible(config.gpsRouteMode());
                errorPanel.setContent("Port Tasks", ERROR_PANEL_RELOAD_INSTRUCTION);
                break;
            case LOGIN_SCREEN:
                reloadButton.setVisible(false);
                gpsPanel.setVisible(false);
                errorPanel.setContent("Port Tasks", ERROR_PANEL_LOGIN_INSTRUCTION);
                break;
        }
    }

//		public void rebuild()
//		{
//			markerView.removeAll();
//			List<CourierTask> courierTasks = plugin.getCourierTasks();
//			for (CourierTask courierTask : courierTasks)
//			{
//				markerView.add(new CourierTaskPanel(plugin, courierTask, clientThread, itemManager, courierTask.getSlot()));
//				markerView.add(Box.createRigidArea(new Dimension(0, 10)));
//			}
//			List<BountyTask> bountyTasks = plugin.getBountyTasks();
//
//			for (BountyTask bountyTask : bountyTasks)
//			{
//				markerView.add(new BountyTaskPanel(plugin, bountyTask, clientThread, itemManager, bountyTask.getSlot()));
//				markerView.add(Box.createRigidArea(new Dimension(0, 10)));
//			}
//
//			if (courierTasks.isEmpty() || bountyTasks.isEmpty())
//			{
//				setupErrorPanel(true);
//			}
//			repaint();
//			revalidate();
//		}

		public void rebuild()
		{
			markerView.removeAll();
			List<Task> allTasks = new ArrayList<>();
			allTasks.addAll(plugin.getCourierTasks());
			allTasks.addAll(plugin.getBountyTasks());
			allTasks.sort(Comparator.comparingInt(Task::getSlot));
			for (Task task : allTasks)
			{
				if (task instanceof CourierTask)
				{
					CourierTask courier = (CourierTask) task;
					markerView.add(new CourierTaskPanel(plugin, courier, clientThread, itemManager, courier.getSlot()));
				}
				else if (task instanceof BountyTask)
				{
					BountyTask bounty = (BountyTask) task;
					markerView.add(new BountyTaskPanel(plugin, bounty, clientThread, itemManager, client, bounty.getSlot()));
				}
				markerView.add(Box.createRigidArea(new Dimension(0, 10)));
			}
			if (allTasks.isEmpty())
			{
				setupErrorPanel(true);
			}
			repaint();
			revalidate();
		}

	public void updateBountyPanel(BountyTask task) // avoid rebuilding the entire JPanel lol
	{
		BountyTaskPanel panel = (BountyTaskPanel) markerView.getComponent(task.getSlot());
		if (panel != null)
		{
			panel.refresh();
		}
	}

	/**
	 * Update progress indicator for a specific courier task.
	 *
	 * @param task The courier task to update
	 * @param indicatorColor Color for the progress indicator (green/yellow/red)
	 * @param tooltip Tooltip text explaining the progress state
	 */
	public void updateCourierTaskProgress(CourierTask task, Color indicatorColor, String tooltip)
	{
		// Find the corresponding panel in markerView
		for (int i = 0; i < markerView.getComponentCount(); i++)
		{
			java.awt.Component comp = markerView.getComponent(i);
			if (comp instanceof CourierTaskPanel)
			{
				CourierTaskPanel panel = (CourierTaskPanel) comp;
				// Check if this panel matches the task (compare by slot or data)
				if (panel.plugin.getCourierTasks().contains(task))
				{
					panel.updateProgressIndicator(indicatorColor, tooltip);
					break;
				}
			}
		}
	}

		/**
		 * Update mode indicator (Planning vs Execution) and capacity display.
		 *
		 * @param mode Current optimizer mode
		 * @param usedSlots Number of used task slots
		 * @param maxSlots Maximum task slots
		 * @param usedCargo Used cargo capacity
		 * @param maxCargo Maximum cargo capacity
		 */
		public void updateModeIndicator(String mode, int usedSlots, int maxSlots, int usedCargo, int maxCargo)
		{
			statusPanel.setVisible(true);

			// Update mode indicator
			if (mode.equals("PLANNING"))
			{
				modeIndicator.setText("Planning Mode - No cargo loaded");
				modeIndicator.setForeground(new Color(76, 175, 80)); // Green
			}
			else
			{
				modeIndicator.setText("Execution Mode - Cargo loaded");
				modeIndicator.setForeground(new Color(33, 150, 243)); // Blue
			}

			// Update capacity indicator
			Color capacityColor = new Color(76, 175, 80); // Green
			double slotUsage = (double) usedSlots / maxSlots;
			double cargoUsage = (double) usedCargo / maxCargo;

			if (slotUsage > 0.9 || cargoUsage > 0.9)
			{
				capacityColor = new Color(244, 67, 54); // Red
			}
			else if (slotUsage > 0.7 || cargoUsage > 0.7)
			{
				capacityColor = new Color(255, 193, 7); // Yellow/Amber
			}

			capacityIndicator.setText(String.format("Slots: %d/%d | Cargo: %d/%d",
				usedSlots, maxSlots, usedCargo, maxCargo));
			capacityIndicator.setForeground(capacityColor);
		}

		/**
		 * Update the GPS navigation display with the next stop information.
		 *
		 * @param nextStopPort Name of the next port to visit
		 * @param stopType Type of stop (PICKUP or DELIVERY)
		 * @param taskName Name of the task this stop is for
		 */
		public void updateGpsNavigation(String nextStopPort, String stopType, String taskName)
		{
			if (!config.gpsRouteMode() || nextStopPort == null || nextStopPort.isEmpty())
			{
				gpsPanel.setVisible(false);
				return;
			}

			gpsPanel.setVisible(true);

			String actionText;
			switch (stopType)
			{
				case "PICKUP":
					actionText = "Pick up at";
					break;
				case "LOAD_CARGO":
					actionText = "Load cargo at";
					break;
				case "DELIVERY":
				default:
					actionText = "Deliver to";
					break;
			}
			nextStopLabel.setText("<html><center><b>" + actionText + ":</b><br>" + nextStopPort + "</center></html>");
			nextStopLabel.setToolTipText("Task: " + taskName);
		}

		/**
		 * Show/hide the GPS panel based on config.
		 */
		public void setGpsPanelVisible(boolean visible)
		{
			gpsPanel.setVisible(visible);
		}

		/**
		 * Update the noticeboard advisor panel with the best recommendation.
		 *
		 * @param recommendation The best task recommendation, or null to hide panel
		 */
		public void updateNoticeboardAdvisor(TaskRecommendation recommendation)
		{
			if (recommendation == null)
			{
				noticeboardPanel.setVisible(false);
				return;
			}

			noticeboardPanel.setVisible(true);

			RecommendationType type = recommendation.getType();
			String taskName = recommendation.getTask().getTaskName();
			String action;
			Color actionColor;

			switch (type)
			{
				case TAKE_NOW:
					action = "Take";
					actionColor = new Color(76, 175, 80); // Green
					break;
				case SWAP:
					String swapTaskName = recommendation.getSwapWithTask() != null 
						? recommendation.getSwapWithTask().getData().getTaskName()
						: "a task";
					action = "Swap with " + swapTaskName;
					actionColor = new Color(255, 152, 0); // Orange
					break;
				case CONSIDER:
					action = "Consider taking";
					actionColor = new Color(255, 193, 7); // Yellow/Amber
					break;
				case SKIP:
				default:
					action = "Skip";
					actionColor = new Color(158, 158, 158); // Gray
					break;
			}

			String labelText = String.format("<html><center><b>%s:</b><br>%s</center></html>", action, taskName);
			noticeboardActionLabel.setText(labelText);
			noticeboardActionLabel.setForeground(actionColor);
			noticeboardActionLabel.setToolTipText(recommendation.getReason());
		}

		/**
		 * Hide the noticeboard advisor panel.
		 */
		public void hideNoticeboardAdvisor()
		{
			noticeboardPanel.setVisible(false);
		}

		/**
		 * Show the noticeboard advisor panel with a "no recommendations" message.
		 * Displayed when current tasks are already optimal.
		 */
		public void showNoRecommendations()
		{
			noticeboardPanel.setVisible(true);
			noticeboardActionLabel.setText("<html><center>No recommendations<br><small>Current tasks are optimal</small></center></html>");
			noticeboardActionLabel.setForeground(new Color(158, 158, 158)); // Gray
			noticeboardActionLabel.setToolTipText("No better tasks available on this noticeboard");
		}
		
		/**
		 * Show the noticeboard advisor panel with a "slots full" message.
		 * Displayed when all task slots are occupied.
		 */
		public void showSlotsFull(int usedSlots, int maxSlots)
		{
			noticeboardPanel.setVisible(true);
			noticeboardActionLabel.setText(String.format(
				"<html><center>Task slots full<br><small>%d/%d slots used</small></center></html>", 
				usedSlots, maxSlots));
			noticeboardActionLabel.setForeground(new Color(244, 67, 54)); // Red
			noticeboardActionLabel.setToolTipText("Complete or abandon a task to free up a slot");
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
				errorPanel.setContent("Port Tasks", client.getGameState() == GameState.LOGGED_IN ? ERROR_PANEL_RELOAD_INSTRUCTION : ERROR_PANEL_LOGIN_INSTRUCTION);
				markerView.removeAll();
				markerView.setLayout(new BoxLayout(markerView, BoxLayout.Y_AXIS));
				markerView.setBackground(ColorScheme.DARK_GRAY_COLOR);
				markerView.add(errorPanel);
			}
		}

		private void addDeveloperPanel()
		{
			JPanel developerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
			developerPanel.setBackground(ColorScheme.DARK_GRAY_HOVER_COLOR);
			developerPanel.setBorder(new EmptyBorder(2, 2, 2, 2));

			JComboBox<String> portPathDropdown = new JComboBox<>();
			PortPaths[] paths = PortPaths.values();
			Arrays.sort(paths, Comparator.comparing(PortPaths::name));

			for (PortPaths path : paths)
			{
				portPathDropdown.addItem(path.name());
			}
			portPathDropdown.setFocusable(false);
			portPathDropdown.setToolTipText("Developer actions");

			portPathDropdown.addActionListener(e ->
			{
				String selected = (String) portPathDropdown.getSelectedItem();
				plugin.setDeveloperPathSelected(PortPaths.valueOf(selected));
			});

			developerPanel.add(portPathDropdown);
			add(developerPanel, BorderLayout.SOUTH);
		}
}
