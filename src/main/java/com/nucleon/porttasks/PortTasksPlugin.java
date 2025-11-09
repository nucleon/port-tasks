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
import javax.inject.Named;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import com.nucleon.porttasks.enums.PortPaths;
import com.nucleon.porttasks.enums.PortTaskData;
import com.nucleon.porttasks.enums.PortTaskTrigger;
import com.nucleon.porttasks.overlay.TracerConfig;
import com.nucleon.porttasks.ui.PortTasksPluginPanel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.events.VarbitChanged;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.ItemManager;
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
	@Inject
	private PortTasksLedgerOverlay portTasksLedgerOverlay;
	@Getter
	List<PortTask> currentTasks = new ArrayList<>();
	@Inject
	private ClientThread clientThread;
	@Inject
	private ItemManager itemManager;
	@Inject
	public TracerConfig tracerConfig;
	@Inject
	private EventBus eventBus;
	@Inject
	@Named("developerMode")
	public boolean developerMode;
	private int[] varPlayers;
	private PortTasksPluginPanel pluginPanel;
	private NavigationButton navigationButton;
	private static final String PLUGIN_NAME = "Port Tasks";
	private static final String ICON_FILE = "icon.png";
	public static final String CONFIG_GROUP = "porttasks";
	private static final String CONFIG_KEY = "porttaskslots";
	@Getter
	@Setter
	public PortPaths developerPathSelected;

	@Override
	protected void startUp()
	{
		log.info("Starting plugin Port Tasks");
		pluginPanel = new PortTasksPluginPanel(this, clientThread, itemManager, config);

		final BufferedImage icon = ImageUtil.loadImageResource(getClass(), ICON_FILE);
		navigationButton = NavigationButton.builder()
				.tooltip(PLUGIN_NAME)
				.icon(icon)
				.priority(5)
				.panel(pluginPanel)
				.build();

		clientToolbar.addNavigation(navigationButton);
		registerOverlays();
		pluginPanel.rebuild();
		eventBus.register(tracerConfig);
	}

	@Override
	protected void shutDown()
	{
		log.info("Stopping Port Tasks");
		clientToolbar.removeNavigation(navigationButton);
		pluginPanel = null;
		navigationButton = null;
		overlayManager.remove(sailingHelperWorldOverlay);
		overlayManager.remove(sailingHelperMapOverlay);
		overlayManager.remove(portTasksLedgerOverlay);
	}

	@SuppressWarnings("unused")
	@Subscribe
	public void onConfigChanged(final ConfigChanged event)
	{
		if (!event.getGroup().equals(PortTasksConfig.CONFIG_GROUP))
			return;
		if (event.getKey().equals("drawOverlay"))
		{
			overlayManager.remove(sailingHelperWorldOverlay);
			overlayManager.remove(sailingHelperMapOverlay);
			overlayManager.remove(portTasksLedgerOverlay);
			registerOverlays();
		}
	}

	@SuppressWarnings("unused")
	@Subscribe
	public void onVarbitChanged(VarbitChanged event)
	{
		if (PortTaskTrigger.contains(event.getVarbitId()))
		{
			PortTaskTrigger varbit = PortTaskTrigger.fromId(event.getVarbitId());
			int value = client.getVarbitValue(varbit.getId());
			handlePortTaskTrigger(varbit, value);
		}
	}

	@SuppressWarnings("unused")
	@Provides
	PortTasksConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(PortTasksConfig.class);
	}

	private void handlePortTaskTrigger(PortTaskTrigger trigger, int value)
	{
		if (trigger.getType() == PortTaskTrigger.TaskType.ID)
		{
			log.debug("Changed: {} (value {})", trigger, value);
			PortTaskData data = PortTaskData.fromId(value);
			if (data != null && value != 0)
			{
				currentTasks.add(new PortTask(data, trigger.getSlot(), false, 0, true, true, config.getNavColor(), 0));
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
					task.setCargoTaken(value);
					break;
				}
			}
			pluginPanel.rebuild();
		}

		if (trigger.getType() == PortTaskTrigger.TaskType.DELIVERED)
		{
			int slot = trigger.getSlot();

			for (PortTask task : currentTasks)
			{
				if (task.getSlot() == slot)
				{
					task.setDelivered(value);
					break;
				}
			}
			pluginPanel.rebuild();
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
					currentTasks.add(new PortTask(data, varbit.getSlot(), false, 0, true, true, config.getNavColor(), 0));
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

	private void registerOverlays()
	{
		if (config.getDrawOverlay() == PortTasksConfig.Overlay.BOTH || config.getDrawOverlay() == PortTasksConfig.Overlay.MAP)
		{
			overlayManager.add(sailingHelperMapOverlay);
		}

		if (config.getDrawOverlay() == PortTasksConfig.Overlay.BOTH || config.getDrawOverlay() == PortTasksConfig.Overlay.WORLD)
		{
			overlayManager.add(sailingHelperWorldOverlay);
		}
		overlayManager.add(portTasksLedgerOverlay);
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
