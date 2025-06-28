package com.nucleon;

import com.google.inject.Provides;
import javax.inject.Inject;
import java.util.ArrayList;

import com.nucleon.enums.PortTaskData;
import com.nucleon.enums.PortTaskTrigger;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.VarbitChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

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
	private SailingHelperMapOverlay sailingHelperMapOverlay;
	@Inject
	private SailingHelperWorldOverlay sailingHelperWorldOverlay;

	List<PortTask> currentTasks = new ArrayList<>();
	private int[] varPlayers;
	private int varPlayerReadDelay = 5;

	@Override
	protected void startUp() throws Exception
	{
		log.info("Example started!");
		if(config.getDrawOverlay() == SailingHelperConfig.Overlay.BOTH || config.getDrawOverlay() == SailingHelperConfig.Overlay.MAP)
			overlayManager.add(sailingHelperMapOverlay);
		if(config.getDrawOverlay() == SailingHelperConfig.Overlay.BOTH || config.getDrawOverlay() == SailingHelperConfig.Overlay.WORLD)
			overlayManager.add(sailingHelperWorldOverlay);
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Example stopped!");
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
			readPortDataFromClientVarps();
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
		// we need to handle the other trigger types, like taken, delivered, and id = 0 is canceled
		if (trigger.getType() == PortTaskTrigger.TaskType.ID)
		{
			log.debug("Changed: {} (value {})", trigger, value);
			PortTaskData data = PortTaskData.fromId(value);
			if (data != null)
			{
				currentTasks.add(new PortTask(data, trigger.getSlot(), false, false, true, true));
			}
		}
	}

	private void readPortDataFromClientVarps()
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
					currentTasks.add(new PortTask(data, varbit.getSlot(), false, false, true, true));
					//System.out.println(currentTasks.size());
				}
				else
				{
					currentTasks.removeIf(task -> task.getSlot() == varbit.getSlot());
					//System.out.println(currentTasks.size());
				}
			}
		}
	}
}
