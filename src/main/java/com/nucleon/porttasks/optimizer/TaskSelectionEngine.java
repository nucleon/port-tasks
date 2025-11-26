package com.nucleon.porttasks.optimizer;

import com.nucleon.porttasks.CourierTask;
import com.nucleon.porttasks.enums.CourierTaskData;
import com.nucleon.porttasks.enums.PortLocation;
import com.nucleon.porttasks.enums.PortPaths;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.awt.Color;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class TaskSelectionEngine {
    // Distance divisor - controls how much insertion cost affects score
    private static final double DISTANCE_DIVISOR = 500.0;

    // Bonus for picking up at current location (non-teleport mode)
    private static final double PICKUP_HERE_BONUS = 2.0;

    // Bonus for tasks delivering to board (round-trip efficiency)
    private static final double ROUND_TRIP_BONUS = 1.5;

    // Score for cargo conflicts
    private static final double CARGO_CONFLICT_SCORE = -1.0;

    // Ship capacity for route optimization
    private static final int DEFAULT_SHIP_CAPACITY = 50;

    private final RouteOptimizer routeOptimizer;
    private final Set<CourierTask> inactiveTasks = new HashSet<>();

    @Getter
    @Setter
    private boolean teleportOptimizationEnabled = false;

    private boolean inPlanningModeSession = false;

    public TaskSelectionEngine(DistanceCache distanceCache) {
        this.routeOptimizer = new RouteOptimizer(distanceCache);
    }

    private int calculateInsertionCost(CourierTaskData candidate, List<CourierTask> heldTasks,
                                       PortLocation boardLocation) {
        List<CourierTask> activeTasks = getActiveTasks(heldTasks);

        // Calculate base route distance (without candidate)
        int baseDistance = calculateRouteDistance(activeTasks, boardLocation);

        // Create a virtual task for the candidate
        CourierTask virtualTask = createVirtualTask(candidate);

        // Add to active tasks and calculate new route distance
        List<CourierTask> withCandidate = new ArrayList<>(activeTasks);
        withCandidate.add(virtualTask);

        int newDistance = calculateRouteDistance(withCandidate, boardLocation);

        if (newDistance == Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }

        return Math.max(0, newDistance - baseDistance);
    }

    private int calculateRouteDistance(List<CourierTask> tasks, PortLocation boardLocation) {
        if (tasks.isEmpty()) {
            return 0;
        }

        boolean hasCargoLoaded = tasks.stream().anyMatch(t -> t.getCargoTaken() > 0);

        // In teleport mode without cargo, we can start anywhere (null start)
        // Otherwise, we start from board
        PortLocation startLocation = (teleportOptimizationEnabled && !hasCargoLoaded) ? null : boardLocation;
        OptimizedRoute optimized = routeOptimizer.optimizeRoute(tasks, startLocation, DEFAULT_SHIP_CAPACITY);

        if (optimized == null) {
            return Integer.MAX_VALUE;
        }

        return optimized.getTotalDistance();
    }

    private CourierTask createVirtualTask(CourierTaskData taskData) {
        return new CourierTask(
                taskData,
                99,             // slot (doesn't matter for routing)
                false,          // taken
                0,              // delivered
                true,           // tracking
                true,           // active
                Color.WHITE,    // overlayColor
                0               // cargoTaken (not picked up yet)
        );
    }

    private double calculateScore(CourierTaskData task, List<CourierTask> heldTasks,
                                  PortLocation boardLocation) {
        double xp = task.getReward();
        if (xp <= 0) {
            return 0;
        }

        List<CourierTask> activeTasks = getActiveTasks(heldTasks);
        boolean hasCargoLoaded = activeTasks.stream().anyMatch(t -> t.getCargoTaken() > 0);

        // Build route location sets
        Set<PortLocation> pendingPickups = new HashSet<>();
        Set<PortLocation> pendingDeliveries = new HashSet<>();
        for (CourierTask t : activeTasks) {
            pendingDeliveries.add(t.getData().getDeliveryPort());
            if (t.getCargoTaken() == 0) {
                pendingPickups.add(t.getData().getPickupPort());
            }
        }

        PortLocation taskPickup = task.getPickupPort();
        PortLocation taskDelivery = task.getDeliveryPort();

        // Check for backtrack pattern: pickup NOT here, delivery TO here
        boolean isBacktrack = taskPickup != boardLocation && taskDelivery == boardLocation;

        // In execution mode, backtrack tasks are bad - return 0
        if (hasCargoLoaded && isBacktrack) {
            return 0;
        }

        // PATTERN: Delivery to pending pickup can create loops
        // Check if delivering to a pending pickup creates a loop back to board
        boolean deliversToPendingPickup = false;
        if (pendingPickups.contains(taskDelivery) && taskDelivery != boardLocation) {
            // Check if any task picking up at taskDelivery delivers to board
            // That would create: board -> taskDelivery (deliver) -> board (deliver pickup) = loop
            for (CourierTask t : activeTasks) {
                if (t.getCargoTaken() == 0 &&
                        t.getData().getPickupPort() == taskDelivery &&
                        t.getData().getDeliveryPort() == boardLocation) {
                    deliversToPendingPickup = true;
                    break;
                }
            }
            // Also check if it's both a pickup AND delivery location (original pattern)
            if (pendingDeliveries.contains(taskDelivery)) {
                deliversToPendingPickup = true;
            }
        }

        // PATTERN: All held tasks deliver TO board (final destination scenario)
        // Any outbound task from board adds unnecessary travel
        boolean allDeliverToBoard = !activeTasks.isEmpty() &&
                activeTasks.stream().allMatch(t -> t.getData().getDeliveryPort() == boardLocation);
        boolean isOutboundFromFinalDest = allDeliverToBoard && taskPickup == boardLocation && taskDelivery != boardLocation;

        // FIX #2: Outbound from final destination should score 0 (SKIP), not 10%
        if (isOutboundFromFinalDest) {
            return 0;  // Complete penalty - these tasks add unnecessary travel
        }

        int insertionCost = calculateInsertionCost(task, heldTasks, boardLocation);
        if (insertionCost == Integer.MAX_VALUE) {
            return 0;
        }

        // Apply bonuses
        double bonus = 1.0;

        // Pickup HERE bonus (only in non-teleport mode AND delivery is on-route)
        boolean deliveryOnRoute = taskDelivery == boardLocation || pendingDeliveries.contains(taskDelivery);
        if (!teleportOptimizationEnabled && taskPickup == boardLocation) {
            if (deliveryOnRoute) {
                bonus *= PICKUP_HERE_BONUS;
            }
            // Extra synergy bonus: pickup HERE + delivery to cargo-loaded destination
            if (hasCargoLoaded && pendingDeliveries.contains(taskDelivery)) {
                bonus *= 1.5;  // Additional 1.5x for synergy = total 3.0x
            }
        }

        // FIX #4 & #5: Boost score for delivering to on-route locations (pending delivery OR pending pickup)
        // This helps differentiate "delivery to on-route" vs "delivery backwards"
        boolean deliveryToOnRouteLocation = pendingDeliveries.contains(taskDelivery) ||
                pendingPickups.contains(taskDelivery);
        if (deliveryToOnRouteLocation && taskDelivery != boardLocation && !deliversToPendingPickup) {
            bonus *= 1.5;  // On-route delivery bonus
        }

        // FIX #6: Round-trip bonus - stronger when starting fresh (no held tasks)
        // Round-trip tasks deliver to board location, giving us XP without extending our route
        if (taskDelivery == boardLocation) {
            if (activeTasks.isEmpty()) {
                bonus *= 3.0;  // Strong round-trip preference when starting fresh
            } else if (!hasCargoLoaded) {
                // In planning mode with existing tasks, still prefer round-trips
                bonus *= ROUND_TRIP_BONUS;
            }
            // Note: In execution mode (hasCargoLoaded), backtrack tasks already return 0 above
        }

        // Penalty for delivering to pending pickup that creates loop
        if (deliversToPendingPickup) {
            bonus *= 0.5;  // 50% penalty
        }

        return (xp * bonus) / (1.0 + insertionCost / DISTANCE_DIVISOR);
    }


    public List<TaskRecommendation> rankTasks(List<CourierTaskData> availableTasks,
                                              PortLocation boardLocation,
                                              List<CourierTask> heldTasks) {
        Set<String> heldCargoKeys = buildCargoDeliverySet(heldTasks);
        boolean hasCargoLoaded = heldTasks.stream().anyMatch(t ->
                isTaskActive(t) && t.getCargoTaken() > 0);

        List<ScoredCandidate> scoredTasks = new ArrayList<>();
        for (CourierTaskData task : availableTasks) {
            ScoredCandidate scored = scoreAvailableTask(task, heldTasks, boardLocation, heldCargoKeys);
            scoredTasks.add(scored);
        }

        scoredTasks.sort((a, b) -> Double.compare(b.getScore(), a.getScore()));
        return buildRecommendations(scoredTasks, hasCargoLoaded);
    }


    private ScoredCandidate scoreAvailableTask(CourierTaskData task, List<CourierTask> heldTasks,
                                               PortLocation boardLocation, Set<String> heldCargoKeys) {
        String cargoKey = task.getCargoTypeName() + ":" + task.getDeliveryPort().name();

        // Check for cargo conflict
        if (heldCargoKeys.contains(cargoKey)) {
            return new ScoredCandidate(task, CARGO_CONFLICT_SCORE,
                    "Conflicts with held " + task.getCargoTypeName() + " task", true, false);
        }

        // Calculate score using insertion cost
        double score = calculateScore(task, heldTasks, boardLocation);
        int insertionCost = calculateInsertionCost(task, heldTasks, boardLocation);

        // Check if task fits the current route strictly (for execution mode decisions)
        boolean fitsRoute = doesTaskFitRouteStrictly(task, heldTasks, boardLocation);

        // Build reason string
        String reason = buildReasonString(task, boardLocation, heldTasks, insertionCost);

        return new ScoredCandidate(task, score, reason, false, fitsRoute);
    }


    private boolean doesTaskFitRouteStrictly(CourierTaskData task, List<CourierTask> heldTasks,
                                             PortLocation boardLocation) {
        List<CourierTask> activeTasks = getActiveTasks(heldTasks);
        if (activeTasks.isEmpty()) {
            // No active tasks - any task "fits" since there's no route to conflict with
            return true;
        }

        PortLocation taskPickup = task.getPickupPort();
        PortLocation taskDelivery = task.getDeliveryPort();

        // Build route location sets
        Set<PortLocation> pendingPickups = new HashSet<>();
        Set<PortLocation> pendingDeliveries = new HashSet<>();
        for (CourierTask t : activeTasks) {
            pendingDeliveries.add(t.getData().getDeliveryPort());
            if (t.getCargoTaken() == 0) {
                pendingPickups.add(t.getData().getPickupPort());
            }
        }

        boolean hasCargoLoaded = activeTasks.stream().anyMatch(t -> t.getCargoTaken() > 0);

        // PATTERN: Delivery to pending pickup can create loops
        // Check if delivering to a pending pickup creates a loop back to board
        if (pendingPickups.contains(taskDelivery) && taskDelivery != boardLocation) {
            // Check if any task picking up at taskDelivery delivers to board
            // That would create: board -> taskDelivery (deliver) -> board (deliver pickup) = loop
            for (CourierTask t : activeTasks) {
                if (t.getCargoTaken() == 0 &&
                        t.getData().getPickupPort() == taskDelivery &&
                        t.getData().getDeliveryPort() == boardLocation) {
                    return false;  // Loop back to board
                }
            }
            // Also check if it's both a pickup AND delivery location (double-visit)
            if (pendingDeliveries.contains(taskDelivery)) {
                return false;
            }
        }

        // PATTERN: All tasks deliver TO board = final destination scenario
        // Any outbound task doesn't fit
        boolean allDeliverToBoard = activeTasks.stream()
                .allMatch(t -> t.getData().getDeliveryPort() == boardLocation);
        if (allDeliverToBoard && taskPickup == boardLocation && taskDelivery != boardLocation) {
            return false;  // Outbound from final destination
        }

        // Check if board is genuinely on route
        boolean boardIsOnRoute = pendingDeliveries.contains(boardLocation) ||
                pendingPickups.contains(boardLocation);

        // Delivery to board only counts as "fits" if board is genuinely on route
        // OR we're in non-teleport mode (where we always start from board)
        // OR we have cargo loaded (execution mode)
        if (taskDelivery == boardLocation) {
            return !teleportOptimizationEnabled || hasCargoLoaded || boardIsOnRoute;
        }

        // Check if delivery is to an existing route location
        // Pending deliveries = always good
        // Pending pickups = good if not also a delivery (handled by isDoubleVisit check above)
        return pendingDeliveries.contains(taskDelivery) || pendingPickups.contains(taskDelivery);
    }


    private String buildReasonString(CourierTaskData task, PortLocation boardLocation,
                                     List<CourierTask> heldTasks, int insertionCost) {
        StringBuilder reason = new StringBuilder();

        if (task.getPickupPort() == boardLocation) {
            reason.append("Cargo here");
        } else if (isOnRoute(task.getPickupPort(), heldTasks)) {
            reason.append("Cargo on-route");
        } else {
            reason.append("New pickup");
        }

        reason.append(", ");

        if (task.getDeliveryPort() == boardLocation) {
            reason.append("delivers here");
        } else if (isOnRoute(task.getDeliveryPort(), heldTasks)) {
            reason.append("delivers on-route");
        } else {
            reason.append("new delivery");
        }

        if (insertionCost > 0) {
            reason.append(String.format(" (+%d tiles)", insertionCost));
        }

        return reason.toString();
    }


    private boolean isOnRoute(PortLocation location, List<CourierTask> heldTasks) {
        for (CourierTask task : heldTasks) {
            if (!isTaskActive(task)) continue;

            if (task.getData().getPickupPort() == location && task.getCargoTaken() == 0) {
                return true;
            }
            if (task.getData().getDeliveryPort() == location) {
                return true;
            }
        }
        return false;
    }


    private List<TaskRecommendation> buildRecommendations(List<ScoredCandidate> scoredTasks,
                                                          boolean hasCargoLoaded) {
        double topScore = scoredTasks.isEmpty() ? 0 :
                scoredTasks.stream().filter(s -> !s.isHasConflict()).mapToDouble(ScoredCandidate::getScore).max().orElse(0);

        List<TaskRecommendation> recommendations = new ArrayList<>();
        for (int i = 0; i < scoredTasks.size(); i++) {
            ScoredCandidate scored = scoredTasks.get(i);
            int rank = i + 1;
            RecommendationType type = determineRecommendationType(scored, rank, topScore, hasCargoLoaded);

            TaskRecommendation rec = new TaskRecommendation(
                    scored.getTask(), scored.getScore(), scored.getReason(), type, rank);
            recommendations.add(rec);
        }

        return recommendations;
    }


    private RecommendationType determineRecommendationType(ScoredCandidate scored, int rank,
                                                           double topScore, boolean hasCargoLoaded) {
        if (scored.isHasConflict()) {
            return RecommendationType.UNAVAILABLE;
        }
        if (scored.getScore() <= 0) {
            return RecommendationType.SKIP;
        }

        // In execution mode (cargo loaded), only route-fitting tasks should be TAKE_NOW
        if (hasCargoLoaded && !scored.isFitsRoute()) {
            if (scored.getScore() > 0 && topScore > 0 && scored.getScore() >= topScore * 0.5) {
                return RecommendationType.CONSIDER;
            }
            return RecommendationType.SKIP;
        }

        // In teleport planning mode, tasks that don't fit the route should be CONSIDER, not TAKE_NOW
        // (fitsRoute is false when delivery goes to a new location not on route)
        if (teleportOptimizationEnabled && !hasCargoLoaded && !scored.isFitsRoute()) {
            if (scored.getScore() > 0 && topScore > 0 && scored.getScore() >= topScore * 0.5) {
                return RecommendationType.CONSIDER;
            }
            return RecommendationType.SKIP;
        }

        // Top scorer is TAKE_NOW (only if it fits the route)
        if (rank == 1 && scored.getScore() > 0 && scored.isFitsRoute()) {
            return RecommendationType.TAKE_NOW;
        }

        // High scorer that fits route is TAKE_NOW
        if (scored.isFitsRoute() && topScore > 0 && scored.getScore() >= topScore * 0.9) {
            return RecommendationType.TAKE_NOW;
        }

        // Within 70% of top score is CONSIDER
        if (topScore > 0 && scored.getScore() >= topScore * 0.7) {
            return RecommendationType.CONSIDER;
        }

        // Positive score but below threshold
        if (scored.getScore() > 0) {
            return RecommendationType.CONSIDER;
        }

        return RecommendationType.SKIP;
    }

    public TaskRecommendation recommendTask(List<CourierTaskData> availableTasks,
                                            PortLocation boardLocation,
                                            List<CourierTask> heldTasks) {
        List<TaskRecommendation> ranked = rankTasks(availableTasks, boardLocation, heldTasks);
        return ranked.isEmpty() ? null : ranked.get(0);
    }


    public SwapRecommendation evaluateSwapOpportunity(List<CourierTaskData> availableTasks,
                                                      PortLocation boardLocation,
                                                      List<CourierTask> heldTasks) {
        if (availableTasks.isEmpty() || heldTasks.isEmpty()) {
            return null;
        }

        Set<String> heldCargoKeys = buildCargoDeliverySet(heldTasks);
        List<CourierTask> swapCandidates = findSwapOutCandidates(heldTasks);
        if (swapCandidates.isEmpty()) {
            return null;
        }

        SwapRecommendation bestSwap = null;
        double bestImprovement = 0;

        boolean hasCargoLoadedGlobal = heldTasks.stream()
                .anyMatch(t -> isTaskActive(t) && t.getCargoTaken() > 0);

        for (CourierTask taskToSwapOut : swapCandidates) {
            int swapOutXP = taskToSwapOut.getData().getReward();
            boolean isInactive = inactiveTasks.contains(taskToSwapOut);
            CourierTaskData oldTaskData = taskToSwapOut.getData();

            // In execution mode (cargo loaded), don't swap out a task that picks up HERE
            // You're already at the pickup location - just pick up the cargo now!
            // This only applies when we have cargo loaded (committed to a route)
            if (hasCargoLoadedGlobal && taskToSwapOut.getCargoTaken() == 0 
                    && oldTaskData.getPickupPort() == boardLocation) {
                continue;
            }

            List<CourierTask> tasksAfterSwap = heldTasks.stream()
                    .filter(t -> t != taskToSwapOut)
                    .collect(Collectors.toList());

            Set<String> cargoKeysAfterSwap = new HashSet<>(heldCargoKeys);
            cargoKeysAfterSwap.remove(taskToSwapOut.getData().getCargoTypeName() + ":"
                    + taskToSwapOut.getData().getDeliveryPort().name());

            for (CourierTaskData newTask : availableTasks) {
                String newCargoKey = newTask.getCargoTypeName() + ":" + newTask.getDeliveryPort().name();
                if (cargoKeysAfterSwap.contains(newCargoKey)) {
                    continue;
                }

                int newXP = newTask.getReward();
                int insertionCost = calculateInsertionCost(newTask, tasksAfterSwap, boardLocation);
                if (insertionCost == Integer.MAX_VALUE) {
                    continue;
                }

                double improvement;
                CourierTaskData oldTask = taskToSwapOut.getData();

                // Check for "pickup HERE + same delivery" scenario (direct upgrade)
                boolean newPicksUpHere = newTask.getPickupPort() == boardLocation;
                boolean oldPicksUpElsewhere = oldTask.getPickupPort() != boardLocation;
                boolean sameDelivery = newTask.getDeliveryPort() == oldTask.getDeliveryPort();
                boolean isDirectUpgrade = newPicksUpHere && oldPicksUpElsewhere && sameDelivery;

                // Check if new task fits route
                boolean newTaskFitsRoute = doesTaskFitRouteStrictly(newTask, tasksAfterSwap, boardLocation);
                boolean hasCargoLoaded = heldTasks.stream().anyMatch(t -> isTaskActive(t) && t.getCargoTaken() > 0);

                // Check if old task (being swapped out) fits the route
                boolean oldTaskFitsRoute = doesTaskFitRouteStrictly(oldTask, tasksAfterSwap, boardLocation);

                if (isInactive) {
                    // FIX #3: INACTIVE task swap logic
                    // The user has explicitly marked this task as "don't want to do now"
                    // We should compare whether the new task is BETTER than the old task

                    // Check if the OLD (inactive) task fits the active route
                    // If it doesn't, we should be willing to swap it out for something better
                    boolean oldTaskOffRoute = !oldTaskFitsRoute;

                    if (oldTaskOffRoute) {
                        // Inactive task is OFF the active route
                        // We want to replace it with something better

                        // Check if new task creates a loop to a pending pickup
                        Set<PortLocation> pendingPickups = getPendingPickups(tasksAfterSwap);
                        boolean createsLoop = pendingPickups.contains(newTask.getDeliveryPort()) &&
                                newTask.getDeliveryPort() != boardLocation;

                        if (newTaskFitsRoute) {
                            // New task fits route - this is a great swap
                            improvement = 1000 + newXP;
                        } else if (createsLoop) {
                            // New task creates a loop to a pending pickup location - avoid this
                            // Only accept if XP is significantly higher (2x+)
                            if (newXP > swapOutXP * 2) {
                                improvement = newXP - swapOutXP;
                            } else {
                                continue;
                            }
                        } else if (newPicksUpHere && oldPicksUpElsewhere && !hasCargoLoaded && !teleportOptimizationEnabled) {
                            // New task picks up HERE (old task doesn't), doesn't create a loop
                            // This saves travel for pickup - but only in non-teleport mode!
                            // In teleport mode, "pickup HERE" has no value since we start from the first pickup
                            improvement = 800 + newXP;
                        } else if (newXP > swapOutXP * 3) {
                            // New task doesn't fit route, but has massively higher XP (200%+)
                            // Need a very substantial XP gain to justify adding an off-route destination
                            improvement = newXP - swapOutXP;
                        } else {
                            // New task also doesn't fit route and no significant XP gain
                            continue;
                        }
                    } else if (!newTaskFitsRoute) {
                        // Inactive task IS on-route, new task is NOT
                        // Only swap for significant XP gain (3x+)
                        if (newXP < swapOutXP * 3) continue;
                        improvement = newXP - swapOutXP;
                    } else {
                        // Both fit route - prefer equal or higher XP
                        if (newXP >= swapOutXP) {
                            improvement = 1000 + (newXP - swapOutXP);
                        } else {
                            // Accept small XP loss if new task has better route fit
                            improvement = 500 + (500.0 / (1.0 + insertionCost));
                        }
                    }
                } else if (isDirectUpgrade && newXP >= swapOutXP && !teleportOptimizationEnabled) {
                    // Direct upgrade: same destination, but pickup HERE saves travel
                    // Only in non-teleport mode (pickup HERE has value)
                    improvement = (newXP - swapOutXP) + 500;  // Bonus for saved travel
                } else if (!hasCargoLoaded && !newTaskFitsRoute) {
                    // In planning mode, don't swap to a task that adds a new destination
                    // unless it's a massive XP improvement (50% more)
                    if (newXP <= swapOutXP * 1.5) continue;
                    improvement = newXP - swapOutXP;
                } else if (hasCargoLoaded && isTaskRepickable(taskToSwapOut, heldTasks, boardLocation)) {
                    // REPICKABLE TASK SWAP LOGIC
                    // The task to swap out can be picked up again at its pickup location,
                    // which is on our route. So we can swap it for something we can complete
                    // before or at that pickup location.
                    
                    PortLocation repickLocation = oldTask.getPickupPort();
                    
                    // Check if new task can be completed before or at the repick location
                    if (canCompleteBeforeOrAtLocation(newTask, repickLocation, tasksAfterSwap, boardLocation)) {
                        // New task can be done before/at repick location
                        // We're not "losing" the old task - we'll repick it later
                        // So any XP from new task is bonus XP!
                        
                        // Base improvement depends on how well the new task fits
                        double baseImprovement;
                        if (newTaskFitsRoute && newPicksUpHere) {
                            // Best case: new task picks up HERE and fits route
                            baseImprovement = 600 + newXP;
                        } else if (newTaskFitsRoute) {
                            // Good case: new task fits route
                            baseImprovement = 400 + newXP;
                        } else if (newPicksUpHere) {
                            // Decent case: picks up here but doesn't fit route perfectly
                            baseImprovement = 300 + newXP;
                        } else {
                            // New task doesn't fit well - skip
                            continue;
                        }
                        
                        // Prefer swapping out LOWER XP tasks (less risk if we forget to repick)
                        // Add a bonus inversely proportional to the swapped-out task's XP
                        // This ensures we prefer the 3774 XP task over the 7549 XP task
                        double lowXpBonus = 10000.0 / (1.0 + swapOutXP);
                        improvement = baseImprovement + lowXpBonus;
                        
                        // Build a clear reason for repickable swaps
                        if (improvement > bestImprovement) {
                            bestImprovement = improvement;
                            String reason = String.format("+%d XP, repick at %s",
                                    newXP, repickLocation.getShortName());
                            bestSwap = new SwapRecommendation(newTask, taskToSwapOut, improvement, reason);
                        }
                        continue;  // Skip the generic reason building below
                    } else {
                        // New task can't be completed before/at repick location - not beneficial
                        continue;
                    }
                } else {
                    // Normal active swap: require 30% XP improvement
                    if (newXP <= swapOutXP * 1.3) continue;
                    improvement = newXP - swapOutXP;
                }

                if (improvement > bestImprovement) {
                    bestImprovement = improvement;
                    int xpDiff = newXP - swapOutXP;
                    String xpChange = xpDiff >= 0 ? "+" + xpDiff : String.valueOf(xpDiff);
                    String reason = String.format("%s -> %s (%s XP)",
                            newTask.getPickupPort().getShortName(),
                            newTask.getDeliveryPort().getShortName(), xpChange);
                    bestSwap = new SwapRecommendation(newTask, taskToSwapOut, improvement, reason);
                }
            }
        }

        return bestSwap;
    }

    private List<CourierTask> findSwapOutCandidates(List<CourierTask> heldTasks) {

        // Inactive tasks first (sorted by XP ascending)
        List<CourierTask> candidates = heldTasks.stream()
                .filter(inactiveTasks::contains)
                .sorted(Comparator.comparingInt(a -> a.getData().getReward())).collect(Collectors.toList());

        // Then active tasks without cargo
        heldTasks.stream()
                .filter(t -> isTaskActive(t) && t.getCargoTaken() == 0)
                .forEach(candidates::add);

        return candidates;
    }

    public List<TaskRecommendation> rankTasksWithSwapEvaluation(List<CourierTaskData> availableTasks,
                                                                PortLocation boardLocation,
                                                                List<CourierTask> heldTasks,
                                                                int availableSlots) {
        if (availableSlots > 0) {
            return rankTasks(availableTasks, boardLocation, heldTasks);
        }

        SwapRecommendation swap = evaluateSwapOpportunity(availableTasks, boardLocation, heldTasks);

        if (swap == null) {
            return availableTasks.stream()
                    .map(task -> new TaskRecommendation(task, 0, "No beneficial swap",
                            RecommendationType.SKIP, null, null))
                    .collect(Collectors.toList());
        }

        List<TaskRecommendation> recommendations = new ArrayList<>();
        recommendations.add(new TaskRecommendation(
                swap.getNewTask(), swap.getScoreImprovement(), swap.getReason(),
                RecommendationType.SWAP, 1, swap.getTaskToSwap()
        ));

        int rank = 2;
        for (CourierTaskData task : availableTasks) {
            if (task != swap.getNewTask()) {
                recommendations.add(new TaskRecommendation(task, 0, "Slots full",
                        RecommendationType.SKIP, rank++, null));
            }
        }

        return recommendations;
    }

    public boolean isTaskActive(CourierTask task) {
        return !inactiveTasks.contains(task);
    }

    public void setTaskActive(CourierTask task, boolean active) {
        if (active) {
            inactiveTasks.remove(task);
        } else {
            inactiveTasks.add(task);
        }
    }

    public List<CourierTask> getActiveTasks(List<CourierTask> allTasks) {
        return allTasks.stream()
                .filter(this::isTaskActive)
                .collect(Collectors.toList());
    }

    public Set<PortLocation> getPendingPickups(List<CourierTask> allTasks) {
        return allTasks.stream()
                .filter(task -> isTaskActive(task) && task.getCargoTaken() == 0)
                .map(task -> task.getData().getPickupPort())
                .collect(Collectors.toSet());
    }

    /**
     * Checks if a task is "repickable" - meaning it can be dropped now and picked up again
     * later when we visit its pickup location.
     *
     * A task is repickable if:
     * 1. It has no cargo loaded (not committed)
     * 2. Its pickup location is on our current route (we'll visit it anyway)
     * 3. Its pickup location is NOT the current board location (otherwise just pick it up now!)
     * 4. We're in execution mode (have cargo loaded for other tasks)
     *
     * @param task The task to check
     * @param allTasks All held tasks
     * @param boardLocation Current board location
     * @return true if the task can be dropped and repicked later
     */
    public boolean isTaskRepickable(CourierTask task, List<CourierTask> allTasks, PortLocation boardLocation) {
        // Task must have no cargo loaded to be repickable
        if (task.getCargoTaken() > 0) {
            return false;
        }

        // Get the pickup location of this task
        PortLocation taskPickup = task.getData().getPickupPort();

        // If the task picks up HERE, it's NOT repickable - just pick it up now!
        // Swapping it out to "repick later" makes no sense when you're already at the pickup location
        if (taskPickup == boardLocation) {
            return false;
        }

        // Must be in execution mode (have cargo loaded somewhere)
        boolean hasCargoLoaded = allTasks.stream()
                .anyMatch(t -> isTaskActive(t) && t.getCargoTaken() > 0 && t != task);
        if (!hasCargoLoaded) {
            return false;
        }

        // Build route locations from OTHER active tasks (excluding this one)
        Set<PortLocation> routeLocations = new HashSet<>();
        for (CourierTask t : allTasks) {
            if (t == task || !isTaskActive(t)) continue;

            // Add delivery destinations
            routeLocations.add(t.getData().getDeliveryPort());
            // Add pending pickups (from tasks without cargo)
            if (t.getCargoTaken() == 0) {
                routeLocations.add(t.getData().getPickupPort());
            }
        }

        // Task is repickable if its pickup is on our route
        return routeLocations.contains(taskPickup);
    }

    /**
     * Checks if a new task can be completed before or at a specific location.
     * This is useful to determine if swapping out a repickable task for a new task
     * makes sense.
     *
     * A repickable swap is beneficial if:
     * 1. New task completes BEFORE repick location → we gain extra XP on the way
     * 2. New task delivers AT repick location → we complete it there, then repick original task
     *
     * Both scenarios work because we don't "lose" the original task - we repick it at that location.
     *
     * @param newTask The new task we're considering
     * @param repickLocation The location where we can repick the original task
     * @param allTasks Current held tasks
     * @param boardLocation Current location
     * @return true if the new task can be completed before or at repickLocation
     */
    private boolean canCompleteBeforeOrAtLocation(CourierTaskData newTask, PortLocation repickLocation,
                                                  List<CourierTask> allTasks, PortLocation boardLocation) {
        PortLocation newPickup = newTask.getPickupPort();
        PortLocation newDelivery = newTask.getDeliveryPort();

        // If new task PICKS UP at the repick location, no benefit - we'd still go there first
        // and we could have just kept the original task
        if (newPickup == repickLocation) {
            return false;
        }

        // Build route locations
        Set<PortLocation> routeDeliveries = new HashSet<>();
        Set<PortLocation> routePickups = new HashSet<>();
        for (CourierTask t : allTasks) {
            if (!isTaskActive(t)) continue;
            routeDeliveries.add(t.getData().getDeliveryPort());
            if (t.getCargoTaken() == 0) {
                routePickups.add(t.getData().getPickupPort());
            }
        }

        // Case 1: New task delivers AT the repick location
        // This is great - we complete the delivery, then immediately repick the original task
        if (newDelivery == repickLocation) {
            // Pickup must be convenient (at board or on-route)
            return newPickup == boardLocation || 
                   routeDeliveries.contains(newPickup) || 
                   routePickups.contains(newPickup);
        }

        // Case 2: New task delivers somewhere else (before reaching repick location)
        // Pickup should be at board location (we can start immediately)
        if (newPickup == boardLocation) {
            // Task can be completed as a side-trip before continuing to repickLocation
            return true;
        }

        // Case 3: Pickup is on-route (we'll pass it anyway)
        boolean pickupOnRoute = routeDeliveries.contains(newPickup) || routePickups.contains(newPickup);
        if (pickupOnRoute) {
            // Delivery is somewhere we can reach without going through repickLocation first
            // This is generally fine as long as it doesn't require a huge detour
            return true;
        }

        // Otherwise, the task requires a detour for pickup - not ideal for repickable swap
        return false;
    }


    public boolean taskFitsCurrentRoute(CourierTaskData task, PortLocation boardLocation,
                                        List<CourierTask> heldTasks) {
        return taskFitsCurrentRoute(task, boardLocation, heldTasks, null);
    }

    public boolean taskFitsCurrentRoute(CourierTaskData task, PortLocation boardLocation,
                                        List<CourierTask> heldTasks, CourierTask excludeTask) {
        List<CourierTask> tasksToConsider = excludeTask != null ?
                heldTasks.stream().filter(t -> t != excludeTask).collect(Collectors.toList()) :
                heldTasks;

        List<CourierTask> activeTasks = getActiveTasks(tasksToConsider);

        if (activeTasks.isEmpty()) {
            return true;
        }

        // Build route location sets
        Set<PortLocation> routePickups = new HashSet<>();
        Set<PortLocation> routeDeliveries = new HashSet<>();  // All deliveries
        Set<PortLocation> outboundDeliveries = new HashSet<>();  // Deliveries we need to travel TO (not at board)

        for (CourierTask t : activeTasks) {
            PortLocation delivery = t.getData().getDeliveryPort();
            routeDeliveries.add(delivery);
            // Outbound deliveries = destinations away from board that we need to travel to
            if (delivery != boardLocation) {
                outboundDeliveries.add(delivery);
            }
            if (t.getCargoTaken() == 0) {
                routePickups.add(t.getData().getPickupPort());
            }
        }

        PortLocation taskPickup = task.getPickupPort();
        PortLocation taskDelivery = task.getDeliveryPort();

        // Pickup is on route if it's at board, a pending pickup, or a delivery location
        boolean pickupOnRoute = taskPickup == boardLocation ||
                routePickups.contains(taskPickup) ||
                routeDeliveries.contains(taskPickup);

        // Delivery is on route if it matches an outbound delivery destination (somewhere we're traveling to)
        boolean deliveryMatchesOutbound = outboundDeliveries.contains(taskDelivery);
        boolean deliveryIsBoard = taskDelivery == boardLocation;
        boolean deliveryOnRoute = deliveryMatchesOutbound || deliveryIsBoard;

        // Both on route = fits
        if (pickupOnRoute && deliveryOnRoute) {
            return true;
        }

        // Check if we're in execution mode (cargo loaded)
        boolean hasCargoLoaded = activeTasks.stream().anyMatch(t -> t.getCargoTaken() > 0);

        // In execution mode: tasks delivering BACK to board (without an outbound delivery) create loops
        // These should be treated more strictly - they add distance without progressing the route
        if (hasCargoLoaded && deliveryIsBoard && !deliveryMatchesOutbound && !pickupOnRoute) {
            // Pickup is off-route and delivery is back to board = creates a loop
            // Only accept if pickup is very close (< 200 distance)
            int pickupDistance = (int) PortPaths.getPath(boardLocation, taskPickup).getDistance();
            return pickupDistance < 200;
        }

        // One on route = might fit with small detour
        // Neither on route = very unlikely to fit (requires major detour)
        boolean oneOnRoute = pickupOnRoute || deliveryOnRoute;

        // In execution mode with neither pickup nor delivery on route = doesn't fit
        // This is a strict check: we're committed to our current route
        if (hasCargoLoaded && !oneOnRoute) {
            return false;  // Completely off-route tasks don't fit during execution
        }

        // Use insertion cost for other cases
        int insertionCost = calculateInsertionCost(task, tasksToConsider, boardLocation);
        if (insertionCost == Integer.MAX_VALUE) {
            return false;
        }
        int currentDistance = calculateRouteDistance(activeTasks, boardLocation);
        if (currentDistance == 0 || currentDistance == Integer.MAX_VALUE) {
            return insertionCost < 500;  // Stricter when no current distance
        }
        double ratio = (double) insertionCost / currentDistance;

        // Use different thresholds based on route alignment
        // Delivery matches outbound route = more lenient (we're already going there)
        // Just delivery to board (not outbound) = stricter (creates return trip)
        if (deliveryMatchesOutbound) {
            return ratio < 0.60;  // 60% threshold when delivery matches outbound destination
        } else if (oneOnRoute) {
            return ratio < 0.35;  // 35% threshold for partial fit
        } else {
            return ratio < 0.20;  // 20% threshold for off-route tasks in planning mode
        }
    }

    public boolean shouldAutoExclude(CourierTaskData task, PortLocation boardLocation,
                                     List<CourierTask> heldTasks) {
        List<CourierTask> activeTasks = getActiveTasks(heldTasks);
        if (activeTasks.isEmpty()) {
            return false;
        }
        return !taskFitsCurrentRoute(task, boardLocation, heldTasks);
    }

    public boolean isInPlanningMode(List<CourierTask> tasks) {
        return tasks.stream().noneMatch(task -> task.getCargoTaken() > 0);
    }

    public boolean shouldEnterPlanningMode(List<CourierTask> tasks, boolean atNoticeboard) {
        if (!atNoticeboard || !isInPlanningMode(tasks)) {
            return false;
        }
        return !inPlanningModeSession;
    }

    public void enterPlanningMode() {
        inactiveTasks.clear();
        inPlanningModeSession = true;
    }

    public void exitPlanningMode(List<CourierTask> heldTasks, PortLocation boardLocation) {
        boolean isFirstTransition = inPlanningModeSession;
        inPlanningModeSession = false;

        if (!isFirstTransition) {
            return;
        }

        for (CourierTask task : heldTasks) {
            if (task.getCargoTaken() > 0 || !isTaskActive(task)) {
                continue;
            }

            if (!taskFitsCurrentRoute(task.getData(), boardLocation, heldTasks, task)) {
                setTaskActive(task, false);
                log.debug("Auto-deactivated task {} on execution mode entry",
                        task.getData().getTaskName());
            }
        }
    }


    private Set<String> buildCargoDeliverySet(List<CourierTask> heldTasks) {
        return heldTasks.stream()
                .map(task -> task.getData().getCargoTypeName() + ":" + task.getData().getDeliveryPort().name())
                .collect(Collectors.toSet());
    }

}
