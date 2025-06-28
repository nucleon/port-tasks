package com.nucleon;

import com.google.gson.Gson;
import com.google.inject.Provides;
import javax.inject.Inject;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.nucleon.enums.PortTaskData;
import com.nucleon.enums.PortTaskTrigger;
import com.nucleon.ui.SailingHelperPluginPanel;
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
	name = "Sailing Helper"
)
public class SailingHelperPlugin extends Plugin
{
	@Inject
	private SailingHelperDelegate delegate;
	@Inject
	private Client client;
	@Inject
	private SailingHelperConfig config;
	@Inject
	private OverlayManager overlayManager;
	@Inject
	private SailingHelperOverlay sailingHelperOverlay;
	@Inject
	private ClientToolbar clientToolbar;
	@Inject
	private ConfigManager configManager;
	@Inject
	private Gson gson;
	@Getter
	@Inject
	private ColorPickerManager colorPickerManager;
	private SailingHelperMapOverlay sailingHelperMapOverlay;
	@Inject
	private SailingHelperWorldOverlay sailingHelperWorldOverlay;
	@Getter
	List<PortTask> currentTasks = new ArrayList<>();
	private int[] varPlayers;
	private int varPlayerReadDelay = 5;
	private SailingHelperPluginPanel pluginPanel;
	private NavigationButton navigationButton;
	private static final String PLUGIN_NAME = "Port Tasks";
	private static final String ICON_FILE = "icon.png";
	public static final String CONFIG_GROUP = "porttasks";
	private static final String CONFIG_KEY = "porttaskslots";

	@Override
	protected void startUp() throws Exception
	{

		pluginPanel = new SailingHelperPluginPanel(this, config);

		final BufferedImage icon = ImageUtil.loadImageResource(getClass(), ICON_FILE);

		navigationButton = NavigationButton.builder()
				.tooltip(PLUGIN_NAME)
				.icon(icon)
				.priority(5)
				.panel(pluginPanel)
				.build();

		clientToolbar.addNavigation(navigationButton);
		overlayManager.add(sailingHelperOverlay);
		pluginPanel.rebuild();

		log.info("Example started!");
		if (config.getDrawOverlay() == SailingHelperConfig.Overlay.BOTH || config.getDrawOverlay() == SailingHelperConfig.Overlay.MAP)
			overlayManager.add(sailingHelperMapOverlay);
		if (config.getDrawOverlay() == SailingHelperConfig.Overlay.BOTH || config.getDrawOverlay() == SailingHelperConfig.Overlay.WORLD)
			overlayManager.add(sailingHelperWorldOverlay);
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Example stopped!");

		overlayManager.remove(sailingHelperOverlay);
		clientToolbar.removeNavigation(navigationButton);
		pluginPanel = null;
		navigationButton = null;
		overlayManager.remove(sailingHelperWorldOverlay);
		overlayManager.remove(sailingHelperMapOverlay);

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
			// log.debug("Changed: {} (value {})", varbit.getName(), value);


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
	SailingHelperConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(SailingHelperConfig.class);
	}

	private void handlePortTaskTrigger(PortTaskTrigger trigger, int value)
	{
		// we need to handle the other trigger types, like taken, delivered, and value = 0 is canceled
		if (trigger.getType() == PortTaskTrigger.TaskType.ID)
		{
			log.debug("Changed: {} (value {})", trigger, value);
			PortTaskData data = PortTaskData.fromId(value);
			if (data != null)
			{
				currentTasks.add(new PortTask(data, trigger.getSlot(), false, false, true, true, Color.green));
				pluginPanel.rebuild();
			}
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
					currentTasks.add(new PortTask(data, varbit.getSlot(), false, false, true, true, Color.green));
					pluginPanel.rebuild();
					//System.out.println(currentTasks.size());
				}
				else
				{
					currentTasks.removeIf(task -> task.getSlot() == varbit.getSlot());
					pluginPanel.rebuild();
					//System.out.println(currentTasks.size());
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
