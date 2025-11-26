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

import com.google.common.base.MoreObjects;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.inject.Provides;
import com.nucleon.porttasks.enums.BountyTaskData;
import com.nucleon.porttasks.enums.PortLocation;
import com.nucleon.porttasks.overlay.NoticeBoardTooltip;
import java.awt.Color;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Named;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import com.nucleon.porttasks.enums.PortPaths;
import com.nucleon.porttasks.enums.CourierTaskData;
import com.nucleon.porttasks.enums.PortTaskTrigger;
import com.nucleon.porttasks.overlay.TaskHighlight;
import com.nucleon.porttasks.overlay.TracerConfig;
import com.nucleon.porttasks.ui.PortTasksPluginPanel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.KeyCode;
import net.runelite.api.Menu;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.Skill;
import net.runelite.api.events.GameObjectDespawned;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.StatChanged;
import net.runelite.api.events.VarbitChanged;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.events.WorldViewUnloaded;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.gameval.InventoryID;
import net.runelite.api.gameval.ObjectID;
import net.runelite.api.gameval.VarbitID;
import net.runelite.api.widgets.Widget;
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
import net.runelite.client.ui.components.colorpicker.RuneliteColorPicker;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.ColorUtil;
import net.runelite.client.util.ImageUtil;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@PluginDescriptor(
	name = "Port Tasks",
	description = "Provides navigation and overlays for sailing cargo and bounty tasks",
	tags = {"sailing", "port", "bounty", "cargo", "tasks"}
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
	@Inject
	private PortTaskModelRenderer portTaskModelRenderer;
	@Inject
	private PortTaskCargoOverlay portTaskCargoOverlay;
	@Inject
	private TaskHighlight taskHighlight;
	@Inject
	NoticeBoardTooltip noticeBoardTooltip;
	@Getter
	List<CourierTask> courierTasks = new ArrayList<>();
	@Getter
	List<BountyTask> bountyTasks = new ArrayList<>();
	@Getter
	Set<GameObject> gangplanks = new HashSet<>();
	@Getter
	Set<GameObject> noticeboards = new HashSet<>();
	@Getter
	Set<GameObject> ledgers = new HashSet<>();
	@Getter
	private final Set<GameObject> helms = new HashSet<>();
	@Getter
	private final Set<GameObject> cargoHolds = new HashSet<>();
	@Getter
	Map<Integer, OfferedTaskData> offeredTasks = new HashMap<>();
	@Getter
	private final Set<WidgetTag> widgetTags = new HashSet<>();
	@Getter
	private boolean lockedIn = false;
	@Getter
	private int sailingLevel;
	@Getter
	private boolean noticeBoardHideIncompletable;
	@Getter
	private boolean noticeBoardHideBounty;
	@Getter
	private boolean noticeBoardHideCourier;
	@Getter
	private boolean noticeBoardHideUntagged;
	@Getter
	private boolean highlightGangplanks;
	@Getter
	private Color highlightGangplanksColor;
	@Getter
	private boolean highlightNoticeboards;
	@Getter
	private boolean highlightCargoHolds;
	@Getter
	private Color highlightCargoHoldsColor;
	@Getter
	private boolean highlightHelmMissingCargo;
	@Getter
	private Color highlightNoticeboardsColor;
	@Getter
	private boolean taskHeightOffset;
	@Getter
	private int pathDrawDistance;
	@Getter
	private int noticeBoardHideOpactity;
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
	private Item[] previousInventory;
	private static final String PLUGIN_NAME = "Port Tasks";
	private static final String ICON_FILE = "icon.png";
	public static final String CONFIG_GROUP = "porttasks";
	private static final String CONFIG_KEY = "porttaskslots";
	private static final String CONFIG_KEY_TAGS = "task_tags";

	private static final String MARK = "Mark task";
	private static final String UNMARK = "Unmark task";
	@Getter
	@Setter
	public PortPaths developerPathSelected;

	@Override
	protected void startUp()
	{
		log.info("Starting plugin Port Tasks");
		pluginPanel = new PortTasksPluginPanel(this, clientThread, itemManager, client, config);

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

		loadWidgetTags();
		overlayManager.add(taskHighlight);

		migrateConfiguration();
		tracerConfig.loadConfigs(config);
		highlightGangplanks = config.highlightGangplanks();
		highlightGangplanksColor = config.highlightGangplanksColor();
		highlightCargoHolds = config.highlightCargoHolds();
		highlightCargoHoldsColor = config.highlightCargoHoldsColor();
		highlightNoticeboards = config.highlightNoticeboards();
		highlightNoticeboardsColor = config.highlightNoticeboardsColor();
		highlightHelmMissingCargo = config.highlightHelmMissingCargo();
		taskHeightOffset = config.enableHeightOffset();
		pathDrawDistance = config.pathDrawDistance();
		noticeBoardHideOpactity = mapOpacity(config.noticeBoardHideOpacity());
		noticeBoardHideIncompletable = config.noticeBoardHideIncompletable();
		noticeBoardHideBounty = config.noticeBoardHideBounty();
		noticeBoardHideCourier = config.noticeBoardHideCourier();
		noticeBoardHideUntagged = config.noticeBoardHideUntagged();
	}

	@Override
	protected void shutDown()
	{
		log.info("Stopping Port Tasks");
		clientToolbar.removeNavigation(navigationButton);
		pluginPanel = null;
		navigationButton = null;
		gangplanks.clear();
		noticeboards.clear();
		ledgers.clear();
		helms.clear();
		cargoHolds.clear();

		eventBus.unregister(tracerConfig);

		overlayManager.remove(sailingHelperWorldOverlay);
		overlayManager.remove(sailingHelperMapOverlay);
		overlayManager.remove(portTasksLedgerOverlay);
		overlayManager.remove(portTaskModelRenderer);
		overlayManager.remove(portTaskCargoOverlay);
		overlayManager.remove(noticeBoardTooltip);
		overlayManager.remove(taskHighlight);
	}

	@SuppressWarnings("unused")
	@Subscribe
	private void onConfigChanged(final ConfigChanged event)
	{
		if (!event.getGroup().equals(PortTasksConfig.CONFIG_GROUP))
			return;
		switch (event.getKey())
		{
			case "drawOverlay":
				overlayManager.remove(sailingHelperWorldOverlay);
				overlayManager.remove(sailingHelperMapOverlay);
				overlayManager.remove(portTasksLedgerOverlay);
				registerOverlays();
				return;
			case "noticeBoardTooltip":
				if (event.getNewValue().contains("true"))
				{
					overlayManager.add(noticeBoardTooltip);
				}
				if (event.getNewValue().contains("false"))
				{
					overlayManager.remove(noticeBoardTooltip);
				}
				return;
			case "highlightGangplanks":
				highlightGangplanks = config.highlightGangplanks();
				return;
			case "highlightGangplanksColor":
				highlightGangplanksColor = config.highlightGangplanksColor();
				return;
			case "highlightNoticeboards":
				highlightNoticeboards = config.highlightNoticeboards();
				return;
			case "highlightNoticeboardsColor":
				highlightNoticeboardsColor = config.highlightNoticeboardsColor();
				return;
			case "highlightHelmMissingCargo":
				highlightHelmMissingCargo = config.highlightHelmMissingCargo();
				return;
			case "highlightCargoHolds":
				highlightCargoHolds = config.highlightCargoHolds();
				return;
			case "highlightCargoHoldsColor":
				highlightCargoHoldsColor = config.highlightCargoHoldsColor();
				return;
			case "enableTracer":
				tracerConfig.setTracerEnabled(config.enableTracer());
				return;
			case "tracerSpeed":
				tracerConfig.setTracerSpeed(config.tracerSpeed());
				return;
			case "tracerIntensity":
				tracerConfig.setTracerIntensity(1f - (config.tracerIntensity() / 100f));
				return;
			case "pathOffset":
				taskHeightOffset = config.enableHeightOffset();
				return;
			case "pathDrawDistance":
				pathDrawDistance = config.pathDrawDistance();
				return;
			case "noticeBoardHideOpacity":
				noticeBoardHideOpactity = mapOpacity(config.noticeBoardHideOpacity());
				return;
			case "noticeBoardHideIncompletable":
				noticeBoardHideIncompletable = config.noticeBoardHideIncompletable();
				return;
			case "noticeBoardHideBounty":
				noticeBoardHideBounty = config.noticeBoardHideBounty();
				return;
			case "noticeBoardHideCourier":
				noticeBoardHideCourier = config.noticeBoardHideCourier();
				return;
			case "noticeBoardHideUntagged":
				noticeBoardHideUntagged = config.noticeBoardHideUntagged();
				return;
		}
	}

	@SuppressWarnings("unused")
	@Subscribe
	private void onVarbitChanged(final VarbitChanged event)
	{
		final int varbitId = event.getVarbitId();
		if (PortTaskTrigger.contains(varbitId))
		{
			PortTaskTrigger varbit = PortTaskTrigger.fromId(event.getVarbitId());
			int value = client.getVarbitValue(varbit.getId());
			handlePortTaskTrigger(varbit, value);
		}
		else if (varbitId == VarbitID.SAILING_BOAT_FACILITY_LOCKEDIN)
		{
			lockedIn = event.getValue() != 0;
		}
	}

	@SuppressWarnings("unused")
	@Subscribe
	private void onGameObjectSpawned(final GameObjectSpawned event)
	{
		final GameObject gameObject = event.getGameObject();
		final int id = gameObject.getId();

		if (id == ObjectID.SAILING_GANGPLANK_PROXY || PortLocation.isGangplank(id))
		{
			gangplanks.add(gameObject);
		}
		else if (PortLocation.isNoticeboard(id))
		{
			noticeboards.add(gameObject);
		}
		else if (PortLocation.isLedger(id))
		{
			ledgers.add(gameObject);
		}
		else if (isInHelmRange(id))
		{
			helms.add(gameObject);
		}
		else if (isInCargoHoldRange(id))
		{
			cargoHolds.add(gameObject);
		}
	}

	@SuppressWarnings("unused")
	@Subscribe
	public void onGameObjectDespawned(final GameObjectDespawned event)
	{
		final GameObject gameObject = event.getGameObject();
		final int id = gameObject.getId();
		final int worldViewId = gameObject.getWorldView().getId();

		if (id == ObjectID.SAILING_GANGPLANK_PROXY || PortLocation.isGangplank(id))
		{
			gangplanks.remove(gameObject);
		}
		else if (PortLocation.isNoticeboard(id))
		{
			noticeboards.remove(gameObject);
		}
		else if (PortLocation.isLedger(id))
		{
			ledgers.remove(gameObject);
		}
		else if (isInCargoHoldRange(id))
		{
			cargoHolds.remove(gameObject);
		}
		else if (isInHelmRange(id))
		{
			helms.remove(gameObject);
		}
	}

	@SuppressWarnings("unused")
	@Subscribe
	public void onWorldViewUnloaded(WorldViewUnloaded event)
	{
		helms.removeIf(o -> o.getWorldView() == event.getWorldView());
		cargoHolds.removeIf(o -> o.getWorldView() == event.getWorldView());
	}

	@SuppressWarnings("unused")
	@Subscribe
	public void onGameStateChanged(final GameStateChanged event)
	{
		switch (event.getGameState())
		{
			case HOPPING:
			case LOADING:
			case LOGGING_IN:
				gangplanks.clear();
				noticeboards.clear();
				ledgers.clear();
				break;
		}
	}
	@Subscribe
	@SuppressWarnings("unused")
	public void onItemContainerChanged(ItemContainerChanged event)
	{
		if (bountyTasks.size() > 0)
		{
			if (event.getContainerId() != InventoryID.INV)
			{
				return;
			}

			ItemContainer inv = event.getItemContainer();
			Item[] current = inv.getItems();
			if (previousInventory == null)
			{
				previousInventory = Arrays.copyOf(current, current.length);
				for (BountyTask task : bountyTasks)
				{
					int itemId = task.getData().itemId;
					int count = getCount(current, itemId);
					task.setItemsCollected(Math.max(0, count));
					pluginPanel.updateBountyPanel(task);
				}
				return;
			}

			for (BountyTask task : bountyTasks)
			{
				int itemId = task.getData().itemId;
				int before = getCount(previousInventory, itemId);
				int after = getCount(current, itemId);
				if (after != before)
				{
					int newValue = Math.max(0, task.getItemsCollected() + (after - before));
					task.setItemsCollected(newValue);
					pluginPanel.updateBountyPanel(task);
				}
			}

			previousInventory = Arrays.copyOf(current, current.length);
		}
	}

	@SuppressWarnings("unused")
	@Subscribe
	private void onWidgetLoaded(final WidgetLoaded event)
	{
		if (event.getGroupId() != InterfaceID.PORT_TASK_BOARD)
		{
			return;
		}
		offeredTasks.clear();
		clientThread.invokeLater(this::scanPortTaskBoard);
	}

	@SuppressWarnings("unused")
	@Subscribe
	private void onMenuEntryAdded(final MenuEntryAdded event)
	{
		if (event.getType() != MenuAction.CC_OP.getId() || !client.isKeyPressed(KeyCode.KC_SHIFT))
		{
			return;
		}
		MenuEntry baseEntry = event.getMenuEntry();
		Widget widget = baseEntry.getWidget();
		Integer dbrow = getDbrowFromWidget(widget);
		if (dbrow == null)
		{
			return;
		}
		WidgetTag existing = getTagForDbrow(dbrow);

		int idx = -1;
		client.createMenuEntry(idx--)
			.setOption(existing == null ? MARK : UNMARK)
			.setTarget(event.getTarget())
			.setParam0(event.getActionParam0())
			.setParam1(event.getActionParam1())
			.setIdentifier(event.getIdentifier())
			.setType(MenuAction.RUNELITE_WIDGET)
			.onClick(this::markTask);

		if (existing != null)
		{
			createTaskColorMenu(idx, baseEntry.getTarget(), widget, existing);
		}
	}

	@SuppressWarnings("unused")
	@Subscribe
	private void onStatChanged(final StatChanged event)
	{
		if (event.getSkill() != Skill.SAILING)
		{
			return;
		}
		final int sailingLevel = client.getRealSkillLevel(Skill.SAILING);
		if (sailingLevel != this.sailingLevel)
		{
			this.sailingLevel = sailingLevel;
		}
	}

	private void markTask(MenuEntry entry)
	{
		Widget taskToTag = entry.getWidget();
		Integer dbrow = getDbrowFromWidget(taskToTag);
		if (dbrow == null)
		{
			return;
		}

		WidgetTag existing = getTagForDbrow(dbrow);

		if (existing != null)
		{
			widgetTags.remove(existing);
		}
		else
		{
			WidgetTag tag = new WidgetTag(dbrow, Color.YELLOW);
			widgetTags.add(tag);
		}
		saveWidgetTags();
	}

	private int createTaskColorMenu(int idx, String target, Widget widget, WidgetTag tag)
	{
		List<Color> colors = getUsedTagColors();

		for (Color defaultColor : new Color[]{
			Color.YELLOW, Color.RED, Color.GREEN, Color.ORANGE, Color.BLUE
		})
		{
			if (colors.size() < 5 && ! colors.contains(defaultColor))
			{
				colors.add(defaultColor);
			}
		}
		MenuEntry parent = client.createMenuEntry(idx--)
			.setOption("Task color")
			.setTarget(target)
			.setType(MenuAction.RUNELITE);

		Menu subMenu = parent.createSubMenu();

		for (final Color c : colors)
		{
			subMenu.createMenuEntry(0)
				.setOption(ColorUtil.prependColorTag("Set color", c))
				.setType(MenuAction.RUNELITE)
				.onClick(
					e -> clientThread.invokeLater(() -> updateWidgetTagColor(tag.getDbrow(), c))
				);
		}

		subMenu.createMenuEntry(0)
			.setOption("Pick color")
			.setType(MenuAction.RUNELITE)
			.onClick(e -> SwingUtilities.invokeLater(() ->
			{
				Color initial = MoreObjects.firstNonNull(tag.getColor(), Color.YELLOW);
				RuneliteColorPicker colorPicker = colorPickerManager.create(
					client,
					initial,
					"Task tag color",
					false
				);

				colorPicker.setOnClose(c ->
					clientThread.invokeLater(() -> updateWidgetTagColor(tag.getDbrow(), c))
				);

				colorPicker.setVisible(true);
			}));

		return idx;
	}

	private WidgetTag getTagForDbrow(int dbrow)
	{
		return widgetTags.stream()
			.filter(t -> t.getDbrow() == dbrow)
			.findFirst()
			.orElse(null);
	}

	private void updateWidgetTagColor(int dbrow, Color color)
	{
		WidgetTag tag = getTagForDbrow(dbrow);
		if (tag != null)
		{
			tag.setColor(color);
		}
		else
		{
			tag = new WidgetTag(dbrow, color);
			widgetTags.add(tag);
		}
		saveWidgetTags();
	}

	private List<Color> getUsedTagColors()
	{
		return widgetTags.stream()
			.map(WidgetTag::getColor)
			.filter(Objects::nonNull)
			.distinct()
			.collect(Collectors.toList());
	}

	private void saveWidgetTags()
	{
		if (widgetTags.isEmpty())
		{
			configManager.unsetConfiguration(CONFIG_GROUP, CONFIG_KEY_TAGS);
		}
		else
		{
			final String json = gson.toJson(widgetTags);
			configManager.setConfiguration(CONFIG_GROUP, CONFIG_KEY_TAGS, json);
		}
	}

	private void loadWidgetTags()
	{
		final String json = configManager.getConfiguration(CONFIG_GROUP, CONFIG_KEY_TAGS);
		if (json == null || json.isEmpty())
		{
			return;
		}

		//CHECKSTYLE:OFF
		Type type = new TypeToken<Set<WidgetTag>>() {}.getType();
		//CHECKSTYLE:ON

		try
		{
			Set<WidgetTag> loaded = gson.fromJson(json, type);
			if (loaded != null)
			{
				widgetTags.clear();
				widgetTags.addAll(loaded);
			}
		}
		catch (Exception e)
		{
			log.info("Failed to load widget tags");
		}
	}

	private void scanPortTaskBoard()
	{
		final Widget widget = client.getWidget(InterfaceID.PortTaskBoard.CONTAINER);
		if (widget == null)
		{
			return;
		}
		Widget[] children = widget.getDynamicChildren();
		if (children == null)
		{
			return;
		}

		for (int i = 0; i < children.length; i++)
		{
			Widget child = children[i];
			Integer dbrow = getDbrowFromWidget(child);
			if (dbrow == null)
			{
				continue;
			}
			int levelRequired = -1;
			if (i + 2 < children.length)
			{
				Widget lvlWidget = children[i + 2];
				String text = lvlWidget.getText();
				if (text != null && !text.isEmpty())
				{
					try
					{
						levelRequired = Integer.parseInt(text);
					}
					catch (NumberFormatException ex)
					{
						log.warn("Port-Tasks: Could not parse level from '{}'", text);
					}
				}
			}
			offeredTasks.put(dbrow, new OfferedTaskData(child, levelRequired));
		}
	}

	public Integer getDbrowFromWidget(Widget widget)
	{
		if (widget == null)
		{
			return null;
		}
		Object[] ops = widget.getOnOpListener();
		if (ops == null || ops.length < 4)
		{
			return null;
		}
		return (Integer) ops[3];

	}

	private boolean isInHelmRange(int id)
	{
		return id >= ObjectID.SAILING_BOAT_STEERING_KANDARIN_1X3_WOOD && id <= ObjectID.SAILING_INTRO_HELM_NOT_IN_USE;
	}

	private boolean isInCargoHoldRange(int id)
	{
		return id >= ObjectID.SAILING_BOAT_CARGO_HOLD_REGULAR_RAFT && id <= ObjectID.SAILING_BOAT_CARGO_HOLD_ROSEWOOD_LARGE_OPEN;
	}

	private void checkInventoryForBountyItems()
	{
		if (bountyTasks.size() > 0)
		{
			ItemContainer inv = client.getItemContainer(InventoryID.INV);
			assert inv != null;
			Item[] current = inv.getItems();
			if (previousInventory == null)
			{
				previousInventory = Arrays.copyOf(current, current.length);
				for (BountyTask task : bountyTasks)
				{
					int itemId = task.getData().itemId;
					int count = getCount(current, itemId);
					task.setItemsCollected(Math.max(0, count));
					pluginPanel.updateBountyPanel(task);
				}
				return;
			}

			for (BountyTask task : bountyTasks)
			{
				int itemId = task.getData().itemId;
				int before = getCount(previousInventory, itemId);
				int after = getCount(current, itemId);
				if (after != before)
				{
					int newValue = Math.max(0, task.getItemsCollected() + (after - before));
					task.setItemsCollected(newValue);
					pluginPanel.updateBountyPanel(task);
				}
				else // satisfies reloading data
				{
					task.setItemsCollected(before);
					pluginPanel.updateBountyPanel(task);
				}
			}
			previousInventory = Arrays.copyOf(current, current.length);
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
		if (!BountyTaskData.isBountyTask(value))
		{
			if (trigger.getType() == PortTaskTrigger.TaskType.ID)
			{
				log.debug("Changed: {} (value {})", trigger, value);
				CourierTaskData data = CourierTaskData.fromId(value);
				if (data != null && value != 0)
				{
					courierTasks.add(new CourierTask(data, trigger.getSlot(), false, 0, true, true, getNavColorForSlot(trigger.getSlot()), 0));
					pluginPanel.rebuild();
				}
			}

			if (trigger.getType() == PortTaskTrigger.TaskType.TAKEN)
			{
				int slot = trigger.getSlot();

				for (CourierTask task : courierTasks)
				{
					if (task.getSlot() == slot)
					{
						task.setCargoTaken(value);
						pluginPanel.rebuild();
						break;
					}
				}
			}

			if (trigger.getType() == PortTaskTrigger.TaskType.DELIVERED)
			{
				int slot = trigger.getSlot();

				for (CourierTask task : courierTasks)
				{
					if (task.getSlot() == slot)
					{
						task.setDelivered(value);
						pluginPanel.rebuild();
						break;
					}
				}
			}
		}
		else
		{
			if (trigger.getType() == PortTaskTrigger.TaskType.ID)
			{
				log.debug("Changed: {} (value {})", trigger, value);
				BountyTaskData data = BountyTaskData.fromId(value);
				if (data != null && value != 0)
				{
					bountyTasks.add(new BountyTask(data, trigger.getSlot(), false, 0, true, true, getNavColorForSlot(trigger.getSlot()), 0));
					pluginPanel.rebuild();
				}
			}
		}

		if (trigger.getType() == PortTaskTrigger.TaskType.ID && value == 0)
		{
			removeTasksForSlot(trigger.getSlot());
			pluginPanel.rebuild();
		}
	}

	private void removeTasksForSlot(int slot)
	{
		courierTasks.removeIf(t -> t.getSlot() == slot);
		bountyTasks.removeIf(t -> t.getSlot() == slot);
	}
	public void readPortDataFromClientVarps()
	{
		assert client.getVarps() != null : "client.getVarps() is null";
		varPlayers = client.getVarps().clone();
		clearTasksForReload();
		for (PortTaskTrigger varbit : PortTaskTrigger.values())
		{
			int value = client.getVarbitValue(varPlayers, varbit.getId());
			handlePortTaskTrigger(varbit, value);
		}
		checkInventoryForBountyItems();
	}
	private void clearTasksForReload()
	{
		courierTasks.clear();
		bountyTasks.clear();
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
		if (config.noticeBoardTooltip())
		{
			overlayManager.add(noticeBoardTooltip);
		}
		overlayManager.add(portTasksLedgerOverlay);
		overlayManager.add(portTaskModelRenderer);
		overlayManager.add(portTaskCargoOverlay);
	}

	public void saveSlotSettings()
	{
		if (courierTasks == null || courierTasks.isEmpty())
		{
			configManager.unsetConfiguration(CONFIG_GROUP, CONFIG_KEY);
			return;
		}
		String json = gson.toJson(courierTasks);
		configManager.setConfiguration(CONFIG_GROUP, CONFIG_KEY, json);
	}

	private Color getNavColorForSlot(int slot)
	{
		switch (slot)
		{
			case 0: return config.getNavColor();
			case 1: return config.getNavColor2();
			case 2: return config.getNavColor3();
			case 3: return config.getNavColor4();
			case 4: return config.getNavColor5();
			default: return Color.GREEN;
		}
	}
	int getInventoryItemCount(int itemId)
	{
		ItemContainer inv = client.getItemContainer(InventoryID.INV);
		if (inv == null)
		{
			return 0;
		}
		int total = 0;
		for (Item item : inv.getItems())
		{
			if (item.getId() == itemId)
			{
				total += item.getQuantity();
			}
		}
		return total;
	}

	private int getCount(Item[] items, int itemId)
	{
		int amt = 0;
		for (Item item : items)
		{
			if (item.getId() == itemId)
			{
				amt += item.getQuantity();
			}
		}
		return amt;
	}


	private void migrateConfiguration()
	{	// min 5 max 25 <- version 1.4.0 -> min 100 max 250
		if (config.pathDrawDistance() < 100)
		{
			configManager.setConfiguration(
					config.CONFIG_GROUP,
					"pathDrawDistance",
					150
			);
		}
	}

	private int mapOpacity(int configValue)
	{
		return 0 + (configValue - 0) * (255 - 0) / (100 - 0);
	}
}
