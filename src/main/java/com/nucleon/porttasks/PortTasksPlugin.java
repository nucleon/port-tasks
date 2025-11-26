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
import com.nucleon.porttasks.enums.BountyTaskData;
import com.nucleon.porttasks.enums.LedgerID;
import com.nucleon.porttasks.enums.PortLocation;
import com.nucleon.porttasks.optimizer.*;
import com.nucleon.porttasks.overlay.NoticeBoardSlotOverlay;
import com.nucleon.porttasks.overlay.NoticeBoardTooltip;
import java.awt.Color;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Named;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import com.nucleon.porttasks.enums.PortPaths;
import com.nucleon.porttasks.enums.CourierTaskData;
import com.nucleon.porttasks.enums.PortTaskTrigger;
import com.nucleon.porttasks.overlay.TracerConfig;
import com.nucleon.porttasks.ui.PortTasksPluginPanel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.GameState;
import net.runelite.api.events.GameObjectDespawned;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.VarbitChanged;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.WidgetClosed;
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
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.ImageUtil;
import java.util.List;

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
	NoticeBoardTooltip noticeBoardTooltip;
	@Inject
	NoticeBoardSlotOverlay noticeBoardSlotOverlay;
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
	Map<Integer, Widget> offeredTasks = new HashMap<>();
	@Getter
	private boolean lockedIn = false;
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
	@Getter
	@Setter
	public PortPaths developerPathSelected;
	@Getter
	private PortLocation cachedDeparturePort = null;
	private List<net.runelite.api.coords.WorldPoint> cachedGpsRoutePath = null;
	@Getter
	private DistanceCache distanceCache;
	private RouteOptimizer routeOptimizer;
	@Getter
	private TaskSelectionEngine taskSelectionEngine;
	@Getter
	private List<TaskRecommendation> currentRecommendations = new ArrayList<>();
	@Getter
	private OptimizedRoute currentRoute = null;

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
		
		// Initialize optimizer components
		initializeOptimizer();
	}
	
	/**
	 * Initialize the route optimizer and related components.
	 */
	private void initializeOptimizer()
	{
		log.info("Initializing Port Tasks optimizer...");
		
		// Create distance cache (pre-computed port-to-port distances)
		distanceCache = new DistanceCache();
		
		// Create route optimizer
		routeOptimizer = new RouteOptimizer(distanceCache);
		
		// Create task selection engine
		taskSelectionEngine = new TaskSelectionEngine(distanceCache);
		taskSelectionEngine.setTeleportOptimizationEnabled(config.enableTeleportOptimization());
		
		log.info("Optimizer initialized successfully");
	}
	
	/**
	 * Get the port nearest to the player's current world location.
	 * 
	 * @return Nearest port, or null if player location can't be determined
	 */
	/**
	 * Get the actual world position - handles both on-land and on-ship cases.
	 * When on ship, returns the boat's position on the main world using transformToMainWorld.
	 * 
	 * @return The current world position, or null if unavailable
	 */
	public net.runelite.api.coords.WorldPoint getActualWorldPosition()
	{
		if (client.getLocalPlayer() == null)
		{
			return null;
		}
		
		net.runelite.api.coords.LocalPoint playerLocalPoint = client.getLocalPlayer().getLocalLocation();
		if (playerLocalPoint == null)
		{
			return null;
		}
		
		net.runelite.api.WorldView playerWorldView = client.getLocalPlayer().getWorldView();
		
		// Check if we're on a ship (not top level world view)
		if (playerWorldView != null && !playerWorldView.isTopLevel())
		{
			// We're on a ship - need to translate coordinates to main world
			net.runelite.api.WorldView topLevelView = client.getTopLevelWorldView();
			if (topLevelView != null)
			{
				net.runelite.api.WorldEntity worldEntity = topLevelView.worldEntities().byIndex(playerWorldView.getId());
				if (worldEntity != null)
				{
					// Use transformToMainWorld to properly convert the local point
					net.runelite.api.coords.LocalPoint mainWorldLocal = worldEntity.transformToMainWorld(playerLocalPoint);
					if (mainWorldLocal != null)
					{
						return net.runelite.api.coords.WorldPoint.fromLocal(
							topLevelView,
							mainWorldLocal.getX(),
							mainWorldLocal.getY(),
							topLevelView.getPlane()
						);
					}
				}
			}
			// If we can't get boat position, return null rather than falling back incorrectly
			return null;
		}
		
		// We're on land - use normal player location
		return net.runelite.api.coords.WorldPoint.fromLocalInstance(client, playerLocalPoint);
	}
	
	/**
	 * Check if the player is currently on a ship.
	 * 
	 * @return true if on a ship, false otherwise
	 */
	public boolean isOnShip()
	{
		if (client.getLocalPlayer() == null)
		{
			return false;
		}
		
		net.runelite.api.WorldView playerWorldView = client.getLocalPlayer().getWorldView();
		return playerWorldView != null && !playerWorldView.isTopLevel() && playerWorldView.getId() != -1;
	}
	
	/**
	 * Get the port nearest to the player's current location.
	 * When on a ship, returns the cached departure port to avoid expensive recomputation each frame.
	 * 
	 * @return Nearest port, or cached departure port if sailing
	 */
	public PortLocation getNearestPort()
	{
		// When on ship, use cached departure port to avoid expensive computation each frame
		if (isOnShip() && cachedDeparturePort != null)
		{
			return cachedDeparturePort;
		}
		
		return computeNearestPort();
	}
	
	/**
	 * Compute the actual nearest port based on current world position.
	 * This is an expensive operation that iterates through all ports.
	 * 
	 * @return Nearest port, or null if player location can't be determined
	 */
	private PortLocation computeNearestPort()
	{
		net.runelite.api.coords.WorldPoint playerLocation = getActualWorldPosition();
		
		if (playerLocation == null)
		{
			return null;
		}
		
		PortLocation nearest = null;
		double minDistance = Double.MAX_VALUE;
		
		for (PortLocation port : PortLocation.values())
		{
			if (port == PortLocation.EMPTY)
			{
				continue;
			}
			
			net.runelite.api.coords.WorldPoint portLocation = port.getNavigationLocation();
			double distance = playerLocation.distanceTo(portLocation);
			
			if (distance < minDistance)
			{
				minDistance = distance;
				nearest = port;
			}
		}
		
		return nearest;
	}
	
	/**
	 * Get the maximum task slots based on sailing level.
	 * 
	 * Task slots unlock at these sailing levels:
	 * - Level 1: 1 slot
	 * - Level 7: 2 slots
	 * - Level 28: 3 slots
	 * - Level 56: 4 slots
	 * - Level 84: 5 slots
	 * 
	 * @return Maximum task slots (1-5)
	 */
	public int getMaxTaskSlots()
	{
		// Get player's sailing level directly from the client
		int sailingLevel = client.getRealSkillLevel(net.runelite.api.Skill.SAILING);
		
		// Determine max slots based on sailing level
		if (sailingLevel >= 84)
		{
			return 5;
		}
		else if (sailingLevel >= 56)
		{
			return 4;
		}
		else if (sailingLevel >= 28)
		{
			return 3;
		}
		else if (sailingLevel >= 7)
		{
			return 2;
		}
		else
		{
			return 1;
		}
	}
	
	/**
	 * Get the ship's cargo capacity.
	 * Detects actual capacity from cargo hold GameObject or estimates from usage.
	 * 
	 * Real cargo capacities by ship type and upgrade level:
	 * Raft: 20-105 (Basic-Ironwood), Skiff: 30-150, Sloop: 40-240
	 * UIM: 4-10 (significantly lower)
	 * 
	 * @return Cargo capacity in units
	 */
	public int getShipCargoCapacity()
	{
		// Try to detect actual capacity from cargo hold GameObject
		int[] cargoHoldInfo = detectCargoHoldInfo();
		if (cargoHoldInfo != null)
		{
			int detectedCapacity = cargoHoldInfo[0];
			int tier = cargoHoldInfo[1];
			int shipSize = cargoHoldInfo[2];
			
			String[] tierNames = {"Basic", "Oak", "Teak", "Mahogany", "Camphor", "Ironwood", "Rosewood"};
			String[] shipNames = {"Raft", "Skiff", "Sloop"};
			
			log.debug("Detected cargo hold: {} {} (capacity: {})", 
				tierNames[tier], shipNames[shipSize], detectedCapacity);
			
			return detectedCapacity;
		}
		
		// Fallback: Estimate capacity based on current cargo usage
		log.debug("No cargo hold detected, estimating from usage");
		
		// Calculate current cargo usage from active tasks
		int currentCargoUsed = 0;
		for (CourierTask task : courierTasks)
		{
			// Count cargo for tasks where cargo is picked up
			if (task.getCargoTaken() > 0)
			{
				currentCargoUsed += task.getData().getCargoAmount();
			}
		}
		
		// Estimate capacity based on observed usage
		// If player has cargo loaded, we know their capacity is at least currentCargoUsed
		// Add buffer based on the current usage to estimate upgrade level:
		// - If using < 20: probably basic cargo hold (estimate 40-60)
		// - If using 20-60: probably mid-tier (estimate 80-120)
		// - If using 60-120: probably high-tier (estimate 160-210)
		// - If using 120+: probably maxed (estimate 240)
		
		int estimatedCapacity;
		if (currentCargoUsed == 0)
		{
			// No cargo loaded - assume mid-tier ship for conservative recommendations
			// Use 120 (Mahogany Sloop) as default to avoid over-recommending
			estimatedCapacity = 120;
		}
		else if (currentCargoUsed < 20)
		{
			// Low usage - estimate basic/oak cargo hold
			estimatedCapacity = 60;
		}
		else if (currentCargoUsed < 60)
		{
			// Medium usage - estimate teak/mahogany cargo hold
			estimatedCapacity = 120;
		}
		else if (currentCargoUsed < 120)
		{
			// High usage - estimate camphor/ironwood cargo hold
			estimatedCapacity = 210;
		}
		else
		{
			// Very high usage - must be rosewood or close to it
			estimatedCapacity = 240;
		}
		
		// Ensure capacity is at least current usage + small buffer
		return Math.max(estimatedCapacity, currentCargoUsed + 10);
	}
	
	/**
	 * Update the UI with current optimizer state.
	 */
	public void updateOptimizerUI()
	{
		if (pluginPanel == null)
		{
			return;
		}
		
		// Detect current mode
		OptimizerMode mode = OptimizerMode.detect(courierTasks);
		
		// Calculate capacity - bounty tasks occupy slots but not cargo
		int usedSlots = courierTasks.size() + bountyTasks.size();
		int maxSlots = getMaxTaskSlots();
		
		int usedCargo = 0;
		for (CourierTask task : courierTasks)
		{
			if (task.getCargoTaken() > 0)
			{
				usedCargo += task.getData().getCargoAmount();
			}
		}
		int maxCargo = getShipCargoCapacity();
		
		// Update panel
		pluginPanel.updateModeIndicator(mode.name(), usedSlots, maxSlots, usedCargo, maxCargo);
		
		// Update GPS navigation info
		updateGpsNavigationUI();
		
		// Update progress indicators for all tasks
		updateAllProgressIndicators();
	}
	
	/**
	 * Update the GPS navigation display in the panel.
	 */
	private void updateGpsNavigationUI()
	{
		if (pluginPanel == null)
		{
			return;
		}
		
		if (!config.gpsRouteMode())
		{
			pluginPanel.setGpsPanelVisible(false);
			return;
		}
		
		// Check if there are pending cargo pickups at the current location
		PortLocation currentLocation = getNearestPort();
		if (currentLocation != null)
		{
			List<CourierTask> pendingPickupsHere = getPendingPickupsAtLocation(currentLocation);
			if (!pendingPickupsHere.isEmpty())
			{
				// Show "Load cargo at current location" instead of navigating away
				CourierTask firstPendingPickup = pendingPickupsHere.get(0);
				String taskNames = pendingPickupsHere.size() == 1 
					? firstPendingPickup.getData().getTaskName()
					: pendingPickupsHere.size() + " tasks";
				pluginPanel.updateGpsNavigation(currentLocation.getName(), "LOAD_CARGO", taskNames);
				return;
			}
		}
		
		RouteStop nextStop = getFirstRouteStop();
		if (nextStop == null)
		{
			pluginPanel.updateGpsNavigation(null, "", "");
			return;
		}
		
		String portName = nextStop.getPort().getName();
		String stopType = nextStop.getType().name();
		String taskName = nextStop.getTask().getData().getTaskName();
		
		pluginPanel.updateGpsNavigation(portName, stopType, taskName);
	}
	
	/**
	 * Get all active tasks that have pending cargo pickups at the specified location.
	 * 
	 * @param location The port location to check
	 * @return List of tasks with pending pickups at this location
	 */
	private List<CourierTask> getPendingPickupsAtLocation(PortLocation location)
	{
		List<CourierTask> result = new ArrayList<>();
		
		for (CourierTask task : courierTasks)
		{
			// Task must be active and have cargo not yet loaded
			boolean isActive = taskSelectionEngine != null ? taskSelectionEngine.isTaskActive(task) : task.isActive();
			if (isActive && task.getCargoTaken() == 0 && task.getData().getPickupPort() == location)
			{
				result.add(task);
			}
		}
		
		return result;
	}
	
	/**
	 * Recalculate the optimized route for current tasks.
	 * Also invalidates the cached GPS route path.
	 */
	public void recalculateRoute()
	{
		// Invalidate GPS route cache since route is changing
		cachedGpsRoutePath = null;
		
		if (courierTasks.isEmpty() || routeOptimizer == null)
		{
			currentRoute = null;
			return;
		}
		
		// Get current location based on position (works both on land and on ship)
		PortLocation currentLocation = getNearestPort();
		if (currentLocation == null)
		{
			currentRoute = null;
			return;
		}
		
		int cargoCapacity = getShipCargoCapacity();
		
		// Only include active tasks in route calculation
		List<CourierTask> activeTasks = taskSelectionEngine != null 
			? taskSelectionEngine.getActiveTasks(courierTasks)
			: courierTasks;
		
		if (activeTasks.isEmpty())
		{
			currentRoute = null;
			return;
		}
		
		try
		{
			currentRoute = routeOptimizer.optimizeRoute(activeTasks, currentLocation, cargoCapacity);
			log.debug("Route recalculated: {} stops (from {} active tasks)", 
				currentRoute != null ? currentRoute.getStops().size() : 0, activeTasks.size());
		}
		catch (Exception e)
		{
			log.error("Failed to optimize route", e);
			currentRoute = null;
		}
	}
	
	/**
	 * Refresh noticeboard recommendations if the noticeboard is currently open.
	 * Called when task state changes (e.g., checkbox toggled) to update swap recommendations.
	 */
	public void refreshNoticeboardRecommendations()
	{
		// Only refresh if noticeboard is currently showing tasks
		if (!offeredTasks.isEmpty())
		{
			generateNoticeBoardRecommendations();
		}
	}
	
	/**
	 * Calculate route preview strings for recommendations.
	 * This shows what the optimal route would look like if the recommendation is accepted.
	 */
	private void calculateRoutePreviewsForRecommendations()
	{
		if (currentRecommendations == null || routeOptimizer == null)
		{
			return;
		}
		
		int cargoCapacity = getShipCargoCapacity();
		
		// Calculate current route for comparison (only for SWAP recommendations)
		String currentRoutePreview = null;
		List<CourierTask> currentActiveTasks = new ArrayList<>();
		for (CourierTask task : courierTasks)
		{
			if (taskSelectionEngine.isTaskActive(task))
			{
				currentActiveTasks.add(task);
			}
		}
		
		if (!currentActiveTasks.isEmpty())
		{
			try
			{
				OptimizedRoute currentRoute = 
					routeOptimizer.optimizeRoute(currentActiveTasks, null, cargoCapacity);
				if (currentRoute != null && !currentRoute.getStops().isEmpty())
				{
					currentRoutePreview = buildRoutePreviewString(currentRoute);
				}
			}
			catch (Exception e)
			{
				log.debug("Failed to calculate current route preview", e);
			}
		}
		
		for (TaskRecommendation rec : currentRecommendations)
		{
			// Only calculate route preview for actionable recommendations
			if (rec.getType() == RecommendationType.SKIP)
			{
				continue;
			}
			
			// Set current route for SWAP recommendations
			if (rec.getType() == RecommendationType.SWAP && currentRoutePreview != null)
			{
				rec.setCurrentRoutePreview(currentRoutePreview);
			}
			
			try
			{
				// Build the hypothetical task list after accepting this recommendation
				List<CourierTask> hypotheticalTasks = new ArrayList<>();
				
				if (rec.getType() == RecommendationType.SWAP)
				{
					// For swap: replace the swapped-out task with the new one
					CourierTask swapOut = rec.getSwapWithTask();
					for (CourierTask task : courierTasks)
					{
						if (taskSelectionEngine.isTaskActive(task) && task != swapOut)
						{
							hypotheticalTasks.add(task);
						}
					}
					// Add a mock task for the new task data
					hypotheticalTasks.add(createMockTask(rec.getTask()));
				}
				else
				{
					// For TAKE_NOW/CONSIDER: add the new task to existing active tasks
					for (CourierTask task : courierTasks)
					{
						if (taskSelectionEngine.isTaskActive(task))
						{
							hypotheticalTasks.add(task);
						}
					}
					hypotheticalTasks.add(createMockTask(rec.getTask()));
				}
				
				if (hypotheticalTasks.isEmpty())
				{
					continue;
				}
				
				// Calculate optimal route for this hypothetical scenario
				OptimizedRoute hypotheticalRoute = 
					routeOptimizer.optimizeRoute(hypotheticalTasks, null, cargoCapacity);
				
				if (hypotheticalRoute != null && !hypotheticalRoute.getStops().isEmpty())
				{
					String preview = buildRoutePreviewString(hypotheticalRoute);
					rec.setRoutePreview(preview);
				}
			}
			catch (Exception e)
			{
				log.debug("Failed to calculate route preview for recommendation", e);
			}
		}
	}
	
	/**
	 * Build a human-readable route preview string from an optimized route.
	 * Format: "Ardougne > Port Roberts > Prifddinas > ..."
	 */
	private String buildRoutePreviewString(OptimizedRoute route)
	{
		if (route == null || route.getStops().isEmpty())
		{
			return null;
		}
		
		StringBuilder sb = new StringBuilder();
		PortLocation lastPort = null;
		
		for (RouteStop stop : route.getStops())
		{
			PortLocation port = stop.getPort();
			// Only add if different from last port (avoid duplicates for pickup+delivery at same location)
			if (port != lastPort)
			{
				if (sb.length() > 0)
				{
					sb.append(" > ");
				}
				sb.append(formatPortName(port));
				lastPort = port;
			}
		}
		
		return sb.toString();
	}
	
	/**
	 * Format a port location name for display using its short name.
	 * Uses the shorthand defined in PortLocation (e.g., "Prif", "Ardy", "Dpfin").
	 */
	private String formatPortName(PortLocation port)
	{
		return port.getShortName();
	}
	
	/**
	 * Create a mock CourierTask for route calculation purposes.
	 * The mock task represents a task that would be accepted (cargo not yet loaded).
	 */
	private CourierTask createMockTask(CourierTaskData data)
	{
		// CourierTask fields: data, slot, taken, delivered, tracking, active, overlayColor, cargoTaken
		return new CourierTask(data, -1, false, 0, false, true, null, 0);
	}
	
	/**
	 * Update progress indicator for a specific task.
	 * Uses route-based progress calculation when route is available.
	 */
	private void updateTaskProgressIndicator(CourierTask task, PortLocation currentLocation)
	{
		// This method is now handled by updateAllProgressIndicators()
		// Individual task updates happen through that method
	}
	
	/**
	 * Update progress indicators for all tasks.
	 * Called after route recalculation or state changes.
	 */
	private void updateAllProgressIndicators()
	{
		if (pluginPanel == null || courierTasks.isEmpty())
		{
			return;
		}
		
		PortLocation currentLocation = getNearestPort();
		if (currentLocation == null)
		{
			return;
		}
		
		// Update each courier task panel with progress indicator
		for (int i = 0; i < courierTasks.size(); i++)
		{
			CourierTask task = courierTasks.get(i);
			ProgressState progressState;
			
			// Try to use route-based progress if we have a current route
			if (currentRoute != null && currentRoute.getStops() != null)
			{
				int currentPortIndex = findCurrentPortIndex(currentLocation);
				progressState = ProgressState.calculateWithRoute(task, currentRoute.getStops(), currentPortIndex);
			}
			else
			{
				// Fall back to distance-based progress
				progressState = ProgressState.calculate(task, currentLocation, distanceCache);
			}
			
			java.awt.Color indicatorColor;
			String tooltip;
			
			switch (progressState)
			{
				case NOT_STARTED:
					indicatorColor = new java.awt.Color(76, 175, 80); // Green
					tooltip = "Safe to swap - no cargo picked up";
					break;
				case EARLY:
					indicatorColor = new java.awt.Color(76, 175, 80); // Green
					tooltip = "Safe to swap - early progress (<25%)";
					break;
				case MID:
					indicatorColor = new java.awt.Color(255, 193, 7); // Yellow
					tooltip = "Moderate sunk cost - mid progress (25-75%)";
					break;
				case LATE:
					indicatorColor = new java.awt.Color(244, 67, 54); // Red
					tooltip = "High sunk cost - late progress (>75%)";
					break;
				default:
					indicatorColor = java.awt.Color.GRAY;
					tooltip = "Unknown progress state";
			}
			
			// Update the specific panel's progress indicator
			pluginPanel.updateCourierTaskProgress(task, indicatorColor, tooltip);
		}
	}
	
	/**
	 * Find the current port index in the route.
	 * Simplified: returns the index of the first occurrence of current port.
	 */
	private int findCurrentPortIndex(PortLocation currentLocation)
	{
		if (currentRoute == null || currentRoute.getStops() == null || currentLocation == null)
		{
			return 0;
		}
		
		for (int i = 0; i < currentRoute.getStops().size(); i++)
		{
			if (currentRoute.getStops().get(i).getPort().equals(currentLocation))
			{
				return i;
			}
		}
		
		return 0;
	}
	
	/**
	 * Build a continuous GPS route path from the optimized route.
	 * This concatenates the paths between each consecutive stop in the optimal route,
	 * starting from the player's current/nearest port to the first stop.
	 * 
	 * Uses caching to avoid expensive recomputation on every frame.
	 * 
	 * @return List of WorldPoints representing the full GPS route, or null if no route available
	 */
	public List<net.runelite.api.coords.WorldPoint> buildGpsRoutePath()
	{
		// Return cached path if available
		if (cachedGpsRoutePath != null)
		{
			return cachedGpsRoutePath;
		}
		
		if (currentRoute == null || currentRoute.getStops() == null || currentRoute.getStops().isEmpty())
		{
			return null;
		}
		
		List<RouteStop> stops = currentRoute.getStops();
		List<net.runelite.api.coords.WorldPoint> fullPath = new ArrayList<>();
		
		// Get the starting port for GPS navigation based on current position
		// Uses cached departure port when on ship to avoid expensive recomputation
		PortLocation startingPort = getNearestPort();
		
		// Add path from starting port to first stop if we have one
		if (startingPort != null && !stops.isEmpty())
		{
			PortLocation firstStop = stops.get(0).getPort();
			
			// Only add this segment if we're not already at the first stop
			if (startingPort != firstStop)
			{
				PortPaths toFirstStop = PortPaths.getPath(startingPort, firstStop);
				if (toFirstStop != null && toFirstStop != PortPaths.DEFAULT)
				{
					List<net.runelite.api.coords.WorldPoint> toFirstStopPoints = toFirstStop.getFullPath();
					if (!toFirstStopPoints.isEmpty())
					{
						fullPath.addAll(toFirstStopPoints);
					}
				}
			}
		}
		
		// Build path by concatenating the paths between consecutive stops
		for (int i = 0; i < stops.size() - 1; i++)
		{
			PortLocation fromPort = stops.get(i).getPort();
			PortLocation toPort = stops.get(i + 1).getPort();
			
			// Get the path between these two ports
			PortPaths segmentPath = PortPaths.getPath(fromPort, toPort);
			if (segmentPath != null && segmentPath != PortPaths.DEFAULT)
			{
				List<net.runelite.api.coords.WorldPoint> segmentPoints = segmentPath.getFullPath();
				
				// Add all points except the first if it duplicates the last point of previous segment
				if (fullPath.isEmpty())
				{
					fullPath.addAll(segmentPoints);
				}
				else if (!segmentPoints.isEmpty())
				{
					// Skip the first point if it's the same as the last point in fullPath
					int startIndex = 0;
					if (!fullPath.isEmpty() && fullPath.get(fullPath.size() - 1).equals(segmentPoints.get(0)))
					{
						startIndex = 1;
					}
					for (int j = startIndex; j < segmentPoints.size(); j++)
					{
						fullPath.add(segmentPoints.get(j));
					}
				}
			}
		}
		
		// Cache the computed path
		cachedGpsRoutePath = fullPath.isEmpty() ? null : fullPath;
		return cachedGpsRoutePath;
	}
	
	/**
	 * Get the next stop in the optimized route from the current location.
	 * Returns the first stop that hasn't been completed yet based on player position.
	 * 
	 * @return The next RouteStop, or null if no route or at end of route
	 */
	public RouteStop getNextRouteStop()
	{
		if (currentRoute == null || currentRoute.getStops() == null || currentRoute.getStops().isEmpty())
		{
			return null;
		}
		
		// Return the first stop in the route
		return currentRoute.getStops().get(0);
	}
	
	/**
	 * Get the first/starting stop for the GPS route (optimal starting point in Planning Mode).
	 * 
	 * @return The first RouteStop, or null if no route available
	 */
	public RouteStop getFirstRouteStop()
	{
		if (currentRoute == null || currentRoute.getStops() == null || currentRoute.getStops().isEmpty())
		{
			return null;
		}
		
		return currentRoute.getStops().get(0);
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
		overlayManager.remove(noticeBoardSlotOverlay);
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
			case "highlightNoticeboardSlot":
				if (event.getNewValue().contains("true"))
				{
					overlayManager.add(noticeBoardSlotOverlay);
				}
				if (event.getNewValue().contains("false"))
				{
					overlayManager.remove(noticeBoardSlotOverlay);
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
			case "gpsRouteMode":
				clientThread.invokeLater(this::updateOptimizerUI);
				return;
			case "enableTeleportOptimization":
				taskSelectionEngine.setTeleportOptimizationEnabled(config.enableTeleportOptimization());
				clientThread.invokeLater(() -> {
					recalculateRoute();
					updateOptimizerUI();
				});
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
		else if (varbitId == VarbitID.SAILING_BOARDED_BOAT || varbitId == VarbitID.SAILING_PLAYER_IS_ON_PLAYER_BOAT)
		{
			// Player boarding or leaving their boat - update cached departure port
			handleBoatBoardingChanged(event.getValue() != 0);
		}
	}
	
	/**
	 * Handle player boarding or leaving their boat.
	 * When boarding, cache the current port as the departure port.
	 * When leaving (docking), clear the cache.
	 * 
	 * @param isOnBoat true if player just boarded, false if just left
	 */
	private void handleBoatBoardingChanged(boolean isOnBoat)
	{
		if (isOnBoat)
		{
			// Player just boarded - cache the current location as departure port
			cachedDeparturePort = computeNearestPort();
			cachedGpsRoutePath = null; // Invalidate GPS path cache
			log.debug("Boarded boat at port: {}", cachedDeparturePort);
		}
		else
		{
			// Player just disembarked - they've docked at a new port
			// Clear the cache so next boarding will use fresh location
			cachedDeparturePort = null;
			cachedGpsRoutePath = null;
			log.debug("Disembarked from boat, cleared departure port cache");
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
		else if (LedgerID.isLedger(id))
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
		else if (LedgerID.isLedger(id))
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
					int itemId = task.getData().getItemId();
					int count = getCount(current, itemId);
					task.setItemsCollected(Math.max(0, count));
					pluginPanel.updateBountyPanel(task);
				}
				return;
			}

			for (BountyTask task : bountyTasks)
			{
				int itemId = task.getData().getItemId();
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
	private void onWidgetClosed(final WidgetClosed event)
	{
		if (event.getGroupId() != InterfaceID.PORT_TASK_BOARD)
		{
			return;
		}
		// Hide the noticeboard advisor when the noticeboard is closed
		offeredTasks.clear();
		currentRecommendations.clear();
		if (pluginPanel != null)
		{
			pluginPanel.hideNoticeboardAdvisor();
		}
	}

	private void scanPortTaskBoard()
	{
		final Widget widget = client.getWidget(InterfaceID.PortTaskBoard.CONTAINER);
		if (widget == null)
		{
			return;
		}
		
		// Check if we should enter planning mode (at noticeboard with no cargo loaded)
		if (taskSelectionEngine != null && taskSelectionEngine.shouldEnterPlanningMode(courierTasks, true))
		{
			taskSelectionEngine.enterPlanningMode();
			log.debug("Entered planning mode - re-activated all tasks");
			// Refresh UI to show updated active states
			pluginPanel.rebuild();
		}
		
		List<Widget> kids = new ArrayList<>();
		if (widget.getDynamicChildren() != null)
		{
			kids.addAll(Arrays.asList(widget.getDynamicChildren()));
		}

		for (Widget child : kids)
		{
			if (child == null)
			{
				continue;
			}
			Object[] ops = child.getOnOpListener();
			if (ops == null || ops.length < 4)
			{
				continue;
			}
			offeredTasks.put((Integer) ops[3], child);
		}
		
		// Generate recommendations for the noticeboard tasks
		generateNoticeBoardRecommendations();
	}
	
	/**
	 * Generate recommendations for tasks currently displayed on the noticeboard.
	 */
	private void generateNoticeBoardRecommendations()
	{
		if (taskSelectionEngine == null || offeredTasks.isEmpty())
		{
			currentRecommendations.clear();
			return;
		}
		
		// Check if there are available task slots
		int usedSlots = courierTasks.size() + bountyTasks.size();
		int maxSlots = getMaxTaskSlots();
		int availableSlots = maxSlots - usedSlots;
		
		log.debug("Slot check: used={}, max={}, available={} (sailing level: {})", 
			usedSlots, maxSlots, availableSlots, 
			client.getRealSkillLevel(net.runelite.api.Skill.SAILING));
		
		// Build list of available tasks from noticeboard (excluding already-taken tasks)
		List<CourierTaskData> availableTasks = new ArrayList<>();
		for (Integer dbrow : offeredTasks.keySet())
		{
			CourierTaskData taskData = CourierTaskData.getByDbrow(dbrow);
			if (taskData != null && !isTaskAlreadyTaken(taskData))
			{
				availableTasks.add(taskData);
			}
		}
		
		// Get current location (board location)
		PortLocation currentLocation = getNearestPort();
		
		// Generate ranked recommendations using the task selection engine
		// This handles both normal recommendations (slots available) and swap recommendations (slots full)
		// IMPORTANT: Pass ALL held tasks, not just active ones. The engine needs to see inactive tasks
		// so it can identify them as swap-out candidates (user has marked them as "reserved for later")
		currentRecommendations = taskSelectionEngine.rankTasksWithSwapEvaluation(
			availableTasks,
			currentLocation,
			courierTasks,  // All tasks, not just active - engine separates them internally
			availableSlots
		);
		
		// Calculate route previews for recommendations (especially useful for SWAP recommendations)
		calculateRoutePreviewsForRecommendations();
		
		// Comprehensive debug logging for recommendations (useful for debugging/test creation)
		logRecommendationDebugInfo(currentLocation, availableSlots);
		
		// Update the noticeboard advisor panel with the best recommendation
		updateNoticeboardAdvisorPanel();
	}
	
	/**
	 * Check if a task is already taken by the player.
	 */
	private boolean isTaskAlreadyTaken(CourierTaskData taskData)
	{
		for (CourierTask task : courierTasks)
		{
			if (task.getData() == taskData)
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Update the noticeboard advisor panel with the best recommendation.
	 * The recommendations are already sorted by score (highest first).
	 */
	private void updateNoticeboardAdvisorPanel()
	{
		if (pluginPanel == null || currentRecommendations == null || currentRecommendations.isEmpty())
		{
			if (pluginPanel != null)
			{
				pluginPanel.hideNoticeboardAdvisor();
			}
			return;
		}
		
		// Find the best actionable recommendation (first non-SKIP, non-UNAVAILABLE)
		TaskRecommendation bestRec = null;
		for (TaskRecommendation rec : currentRecommendations)
		{
			RecommendationType type = rec.getType();
			if (type != RecommendationType.SKIP &&
				type != RecommendationType.UNAVAILABLE)
			{
				bestRec = rec;
				break;
			}
		}
		
		// If no actionable recommendation found, show "no recommendations" message
		if (bestRec == null)
		{
			pluginPanel.showNoRecommendations();
			return;
		}
		
		pluginPanel.updateNoticeboardAdvisor(bestRec);
	}

	/**
	 * Log comprehensive debug information about recommendations.
	 * This is useful for debugging and creating test cases.
	 * 
	 * Output format includes task IDs for easy test case creation.
	 */
	private void logRecommendationDebugInfo(PortLocation boardLocation, int availableSlots)
	{
		if (currentRecommendations == null || currentRecommendations.isEmpty())
		{
			log.info("=== RECOMMENDATION DEBUG: No recommendations generated ===");
			return;
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append("\n=== RECOMMENDATION DEBUG ===\n");
		sb.append("Board: ").append(boardLocation).append("\n");
		sb.append("Available slots: ").append(availableSlots).append("\n");
		sb.append("Teleport optimization: ").append(taskSelectionEngine.isTeleportOptimizationEnabled()).append("\n");
		sb.append("\n");
		
		// Log held tasks with full details for test case creation
		sb.append("HELD TASKS (").append(courierTasks.size()).append("):\n");
		for (CourierTask task : courierTasks)
		{
			boolean isActive = taskSelectionEngine.isTaskActive(task);
			boolean hasCargoLoaded = task.getCargoTaken() > 0;
			sb.append(String.format("  [%s] %s -> %s (%s, %d XP)%s\n",
				isActive ? "ACTIVE" : "INACTIVE",
				task.getData().getPickupPort(),
				task.getData().getDeliveryPort(),
				task.getData().getCargoTypeName(),
				task.getData().getReward(),
				hasCargoLoaded ? " [CARGO LOADED]" : ""));
			sb.append(String.format("      TaskData: %s (board: %s)\n",
				task.getData().name(),
				task.getData().getNoticeboardLocation()));
		}
		sb.append("\n");
		
		// Log recommendations with full details
		sb.append("RECOMMENDATIONS (").append(currentRecommendations.size()).append("):\n");
		for (TaskRecommendation rec : currentRecommendations)
		{
			sb.append(String.format("  #%d: %s -> %s (%s, %d XP)\n",
				rec.getRank() != null ? rec.getRank() : 0,
				rec.getTask().getPickupPort(),
				rec.getTask().getDeliveryPort(),
				rec.getTask().getCargoTypeName(),
				rec.getTask().getReward()));
			sb.append(String.format("      TaskData: %s (board: %s)\n",
				rec.getTask().name(),
				rec.getTask().getNoticeboardLocation()));
			sb.append(String.format("      Type: %s, Score: %.1f\n",
				rec.getType(), rec.getScore()));
			sb.append(String.format("      Reason: %s\n", rec.getReason()));
			if (rec.getSwapWithTask() != null)
			{
				CourierTask swapOut = rec.getSwapWithTask();
				sb.append(String.format("      SWAP OUT: %s -> %s (%s, %d XP)\n",
					swapOut.getData().getPickupPort(),
					swapOut.getData().getDeliveryPort(),
					swapOut.getData().getCargoTypeName(),
					swapOut.getData().getReward()));
				sb.append(String.format("          SwapOut TaskData: %s\n",
					swapOut.getData().name()));
			}
			if (rec.getCurrentRoutePreview() != null && !rec.getCurrentRoutePreview().isEmpty())
			{
				sb.append(String.format("      Current Route: %s\n", rec.getCurrentRoutePreview()));
			}
			if (rec.getRoutePreview() != null && !rec.getRoutePreview().isEmpty())
			{
				sb.append(String.format("      New Route: %s\n", rec.getRoutePreview()));
			}
		}
		sb.append("=== END RECOMMENDATION DEBUG ===\n");
		
		log.info(sb.toString());
	}
	
	private boolean isInHelmRange(int id)
	{
		return id >= ObjectID.SAILING_BOAT_STEERING_KANDARIN_1X3_WOOD && id <= ObjectID.SAILING_INTRO_HELM_NOT_IN_USE;
	}

	private boolean isInCargoHoldRange(int id)
	{
		return id >= ObjectID.SAILING_BOAT_CARGO_HOLD_REGULAR_RAFT && id <= ObjectID.SAILING_BOAT_CARGO_HOLD_ROSEWOOD_LARGE_OPEN;
	}
	
	/**
	 * Detect cargo hold tier and ship size from the player's cargo hold GameObject.
	 * Returns capacity in format: [capacity, tier, shipSize]
	 * where tier: 0=Basic, 1=Oak, 2=Teak, 3=Mahogany, 4=Camphor, 5=Ironwood, 6=Rosewood
	 * and shipSize: 0=Raft, 1=Skiff, 2=Sloop
	 * 
	 * @return Array [capacity, tier, shipSize] or null if no cargo hold detected
	 */
	private int[] detectCargoHoldInfo()
	{
		if (cargoHolds.isEmpty())
		{
			return null;
		}
		
		// Get the first cargo hold object (player should only have one ship)
		GameObject cargoHold = cargoHolds.iterator().next();
		int objectId = cargoHold.getId();
		
		// ObjectID naming pattern analysis:
		// SAILING_BOAT_CARGO_HOLD_REGULAR_RAFT = Basic Raft
		// SAILING_BOAT_CARGO_HOLD_OAK_MEDIUM = Oak Skiff
		// SAILING_BOAT_CARGO_HOLD_ROSEWOOD_LARGE_OPEN = Rosewood Sloop
		// Pattern: material determines tier, size suffix determines ship type
		
		// Cargo hold capacities by material and ship size:
		// Material levels: Regular(1), Oak(18), Teak(29), Mahogany(46), Camphor(60), Ironwood(80), Rosewood(89)
		// Ship sizes: Raft (small), Skiff (medium), Sloop (large)
		
		// Map: [Raft, Skiff, Sloop] capacities for each tier
		int[][] capacityTable = {
			{20, 30, 40},   // Basic/Regular
			{30, 45, 60},   // Oak
			{45, 60, 75},   // Teak
			{60, 90, 120},  // Mahogany
			{80, 120, 160}, // Camphor
			{105, 150, 210},// Ironwood
			{0, 0, 240}     // Rosewood (only available for Sloop)
		};
		
		// Detect tier by object ID range analysis
		// The ObjectIDs should be sequential: Basic < Oak < Teak < Mahogany < Camphor < Ironwood < Rosewood
		int baseId = ObjectID.SAILING_BOAT_CARGO_HOLD_REGULAR_RAFT;
		int idOffset = objectId - baseId;
		
		// Estimate tier and ship size from object ID offset
		// Assuming ~3 variants per tier (one for each ship size)
		int estimatedTier = idOffset / 3;
		int estimatedShipSize = idOffset % 3;
		
		// Clamp values to valid ranges
		estimatedTier = Math.min(estimatedTier, 6); // 0-6 (Basic to Rosewood)
		estimatedShipSize = Math.min(estimatedShipSize, 2); // 0-2 (Raft to Sloop)
		
		int capacity = capacityTable[estimatedTier][estimatedShipSize];
		
		// Fallback if capacity is 0 (invalid combination like Raft+Rosewood)
		if (capacity == 0)
		{
			return null;
		}
		
		return new int[]{capacity, estimatedTier, estimatedShipSize};
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
					int itemId = task.getData().getItemId();
					int count = getCount(current, itemId);
					task.setItemsCollected(Math.max(0, count));
					pluginPanel.updateBountyPanel(task);
				}
				return;
			}

			for (BountyTask task : bountyTasks)
			{
				int itemId = task.getData().getItemId();
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
					CourierTask newTask = new CourierTask(data, trigger.getSlot(), false, 0, true, true, getNavColorForSlot(trigger.getSlot()), 0);
					courierTasks.add(newTask);
					
					// Auto-exclude if task doesn't fit current route
					PortLocation boardLocation = getNearestPort();
					if (taskSelectionEngine != null && boardLocation != null)
					{
						if (taskSelectionEngine.shouldAutoExclude(data, boardLocation, courierTasks))
						{
							taskSelectionEngine.setTaskActive(newTask, false);
							log.debug("Auto-excluded task {} (doesn't fit current route)", data.getTaskName());
						}
					}
					
					recalculateRoute();
					pluginPanel.rebuild();
					updateOptimizerUI();
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
					// Note: We no longer call exitPlanningMode here.
					// Auto-deactivation of off-route tasks is handled when tasks are ACCEPTED
					// from the noticeboard (via shouldAutoExclude), not when cargo is picked up.
					// This prevents re-evaluating tasks that the user has manually toggled.
					recalculateRoute();
					pluginPanel.rebuild();
					updateOptimizerUI();
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
					recalculateRoute();
					pluginPanel.rebuild();
					updateOptimizerUI();
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
			recalculateRoute();
			pluginPanel.rebuild();
			updateOptimizerUI();
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
		updateOptimizerUI();
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
		if (config.highlightNoticeboardSlot())
		{
			overlayManager.add(noticeBoardSlotOverlay);
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
}
