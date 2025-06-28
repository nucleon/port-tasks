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
package com.nucleon.porttasks;

import com.google.gson.Gson;
import com.google.inject.Provides;
import javax.inject.Inject;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.nucleon.porttasks.enums.PortTaskData;
import com.nucleon.porttasks.enums.PortTaskTrigger;
import com.nucleon.porttasks.ui.PortTasksPluginPanel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.VarbitChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.ui.components.colorpicker.ColorPickerManager;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.ImageUtil;

import java.util.List;

@Slf4j
@PluginDescriptor(
	name = "Port Tasks"
)
public class PortTasksPlugin extends Plugin
{
	@Inject
	private PortTasksDelegate delegate;
	@Inject
	private Client client;
	@Inject
	private PortTasksConfig config;
	@Inject
	private OverlayManager overlayManager;
	@Inject
	private ClientToolbar clientToolbar;
	@Inject
	private ConfigManager configManager;
	@Inject
	private Gson gson;
	@Getter
	@Inject
	private ColorPickerManager colorPickerManager;
	@Inject
	private PortTasksMapOverlay sailingHelperMapOverlay;
	@Inject
	private PortTasksWorldOverlay sailingHelperWorldOverlay;
	@Inject
	private PortTasksMiniMapOverlay sailingHelperMiniMapOverlay;
	@Getter
	List<PortTask> currentTasks = new ArrayList<>();
	private int[] varPlayers;
	private int varPlayerReadDelay = 5;
	private PortTasksPluginPanel pluginPanel;
	private NavigationButton navigationButton;
	private static final String PLUGIN_NAME = "Port Tasks";
	private static final String ICON_FILE = "icon.png";
	public static final String CONFIG_GROUP = "porttasks";
	private static final String CONFIG_KEY = "porttaskslots";

	@Override
	protected void startUp() throws Exception
	{

		pluginPanel = new PortTasksPluginPanel(this, config);

		final BufferedImage icon = ImageUtil.loadImageResource(getClass(), ICON_FILE);

		navigationButton = NavigationButton.builder()
				.tooltip(PLUGIN_NAME)
				.icon(icon)
				.priority(5)
				.panel(pluginPanel)
				.build();

		clientToolbar.addNavigation(navigationButton);

		log.info("Example started!");
		if (config.getDrawOverlay() == PortTasksConfig.Overlay.BOTH || config.getDrawOverlay() == PortTasksConfig.Overlay.MAP)
		{
			overlayManager.add(sailingHelperMapOverlay);
			overlayManager.add(sailingHelperMiniMapOverlay);
		}
		if (config.getDrawOverlay() == PortTasksConfig.Overlay.BOTH || config.getDrawOverlay() == PortTasksConfig.Overlay.WORLD)
		{
			overlayManager.add(sailingHelperWorldOverlay);
		}
		pluginPanel.rebuild();
	}

	@Override
	protected void shutDown() throws Exception
	{
		clientToolbar.removeNavigation(navigationButton);
		pluginPanel = null;
		navigationButton = null;
		overlayManager.remove(sailingHelperWorldOverlay);
		overlayManager.remove(sailingHelperMapOverlay);
		overlayManager.remove(sailingHelperMiniMapOverlay);

	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
		{
			delegate.isLoggedIn = true;
		}
	}

	@Subscribe
	public void onVarbitChanged(VarbitChanged event)
	{
		// Check if the varbit changed is in our triggers
		if (PortTaskTrigger.contains(event.getVarbitId()))
		{
			PortTaskTrigger varbit = PortTaskTrigger.fromId(event.getVarbitId());
			int value = client.getVarbitValue(varbit.getId());
			log.info("Changed: {} (value {})", varbit.name(), value);


			// we accepted a new task, took cargo, delivered cargo or canceled a task
			handlePortTaskTrigger(varbit, value);
		}
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		if (varPlayerReadDelay > 0)
		{
			varPlayerReadDelay--;
		}
		else if (varPlayerReadDelay == 0)
		{
			//readPortDataFromClientVarps();
			varPlayerReadDelay = -1;
		}
	}

	@Provides
	PortTasksConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(PortTasksConfig.class);
	}

	private void handlePortTaskTrigger(PortTaskTrigger trigger, int value)
	{
		// we need to handle the other trigger types, like taken, delivered, and value = 0 is canceled
		if (trigger.getType() == PortTaskTrigger.TaskType.ID)
		{
			log.debug("Changed: {} (value {})", trigger, value);
			PortTaskData data = PortTaskData.fromId(value);
			if (data != null && value != 0)
			{
				currentTasks.add(new PortTask(data, trigger.getSlot(), false, 0, true, true, Color.green, 0));
				pluginPanel.rebuild();
			}
			if (value == 0)
			{
				currentTasks.removeIf(task -> task.getSlot() == trigger.getSlot());
				pluginPanel.rebuild();
			}
		}
		if (trigger.getType() == PortTaskTrigger.TaskType.TAKEN)
		{
			int slot = trigger.getSlot();

			for (PortTask task : currentTasks)
			{
				if (task.getSlot() == slot)
				{
					task.setCargoTaken(value); // Update the cargoTaken value
					break;
				}
			}
			pluginPanel.rebuild(); // Refresh UI if necessary
		}
		if (trigger.getType() == PortTaskTrigger.TaskType.DELIVERED)
		{
			int slot = trigger.getSlot();

			for (PortTask task : currentTasks)
			{
				if (task.getSlot() == slot)
				{
					task.setDelivered(value); // Update the cargoTaken value
					break;
				}
			}
			pluginPanel.rebuild(); // Refresh UI if necessary
		}
	}

	public void readPortDataFromClientVarps()
	{
		assert client.getVarps() != null : "client.getVarps() is null";
		varPlayers = client.getVarps().clone();

		for (PortTaskTrigger varbit : PortTaskTrigger.values())
		{
			if (varbit.getType() == PortTaskTrigger.TaskType.ID)
			{
				int value = client.getVarbitValue(varPlayers, varbit.getId());
				if (value != 0 && currentTasks.stream().noneMatch(task -> task.getSlot() == varbit.getSlot()))
				{
					PortTaskData data = PortTaskData.fromId(value);
					currentTasks.add(new PortTask(data, varbit.getSlot(), false, 0, true, true, Color.green, 0));
					pluginPanel.rebuild();
				}
				else
				{
					currentTasks.removeIf(task -> task.getSlot() == varbit.getSlot());
					pluginPanel.rebuild();
				}
			}
		}
	}

	public void saveSlotSettings()
	{
		if (currentTasks == null || currentTasks.isEmpty())
		{
			configManager.unsetConfiguration(CONFIG_GROUP, CONFIG_KEY);
			return;
		}
		String json = gson.toJson(currentTasks);
		configManager.setConfiguration(CONFIG_GROUP, CONFIG_KEY, json);
	}
}
