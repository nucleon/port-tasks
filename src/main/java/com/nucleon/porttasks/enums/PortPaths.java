package com.nucleon.porttasks.enums;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.nucleon.porttasks.RelativeMove;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.coords.WorldPoint;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Port-to-port sailing paths.
 * <p>
 * Paths are loaded from a JSON resource file to avoid Java's "code too large" error
 * that occurs when defining too many large enum values.
 */
@Slf4j
public class PortPaths {

    @Getter
    private final PortLocation start;

    @Getter
    private final PortLocation end;

    @Getter
    private final List<RelativeMove> pathPoints;

    @Getter
    private final double distance;

    public static final PortPaths DEFAULT = new PortPaths(PortLocation.EMPTY, PortLocation.EMPTY, new ArrayList<>());
    private static final Map<RouteKey, PortPaths> ROUTE_LOOKUP = new HashMap<>();

    static {
        loadPathsFromJson();
    }

    private PortPaths(PortLocation start, PortLocation end, List<RelativeMove> pathPoints) {
        this.start = start;
        this.end = end;
        this.pathPoints = pathPoints;
        this.distance = computeDistance();
    }

    /**
     * Load all paths from the JSON resource file
     */
    private static void loadPathsFromJson() {
        try (InputStream in = PortPaths.class.getResourceAsStream("/port-paths.json")) {
            if (in == null) {
                log.error("Failed to load port-paths.json - resource not found");
                return;
            }

            Gson gson = new Gson();
            JsonArray pathsArray = gson.fromJson(new InputStreamReader(in), JsonArray.class);

            int loadedCount = 0;
            for (JsonElement element : pathsArray) {
                JsonObject pathObj = element.getAsJsonObject();

                String fromName = pathObj.get("from").getAsString();
                String toName = pathObj.get("to").getAsString();
                JsonArray movesArray = pathObj.getAsJsonArray("moves");

                // Parse port locations
                PortLocation from = PortLocation.valueOf(fromName);
                PortLocation to = PortLocation.valueOf(toName);

                // Parse moves (supports both [dx, dy] and [count, dx, dy] formats)
                List<RelativeMove> moves = new ArrayList<>();
                for (JsonElement moveElement : movesArray) {
                    JsonArray move = moveElement.getAsJsonArray();
                    
                    if (move.size() == 2) {
                        // Simple format: [dx, dy]
                        int dx = move.get(0).getAsInt();
                        int dy = move.get(1).getAsInt();
                        moves.add(new RelativeMove(dx, dy));
                    } else if (move.size() == 3) {
                        // Consolidated format: [count, dx, dy]
                        int count = move.get(0).getAsInt();
                        int dx = move.get(1).getAsInt();
                        int dy = move.get(2).getAsInt();
                        // Expand the consolidated move
                        for (int i = 0; i < count; i++) {
                            moves.add(new RelativeMove(dx, dy));
                        }
                    }
                }

                // Create path and add to lookup
                PortPaths path = new PortPaths(from, to, moves);
                ROUTE_LOOKUP.put(new RouteKey(from, to), path);
                loadedCount++;
            }

            log.info("Loaded {} port-to-port paths from JSON", loadedCount);

        } catch (Exception e) {
            log.error("Failed to load port paths from JSON", e);
        }
    }

    /**
     * Get the path between two ports.
     * If the exact route isn't found, tries the reverse route and inverts it.
     * Returns DEFAULT if no path is found.
     */
    public static PortPaths getPath(PortLocation start, PortLocation end) {
        if (start == null || end == null || start == PortLocation.EMPTY || end == PortLocation.EMPTY) {
            return DEFAULT;
        }

        // Try direct lookup first
        PortPaths path = ROUTE_LOOKUP.get(new RouteKey(start, end));
        if (path != null) {
            return path;
        }

        // Try reverse lookup and invert the path
        PortPaths reversePath = ROUTE_LOOKUP.get(new RouteKey(end, start));
        if (reversePath != null) {
            return reversePath.reverse();
        }

        return DEFAULT;
    }

    /**
     * Create a reversed version of this path (swaps start/end and inverts all moves)
     */
    private PortPaths reverse() {
        List<RelativeMove> reversedMoves = new ArrayList<>();
        // Reverse order and invert each move
        for (int i = pathPoints.size() - 1; i >= 0; i--) {
            RelativeMove move = pathPoints.get(i);
            reversedMoves.add(new RelativeMove(-move.getDx(), -move.getDy()));
        }
        return new PortPaths(end, start, reversedMoves);
    }

    /**
     * Compute the total sailing distance for this path
     */
    private double computeDistance() {
        if (pathPoints.isEmpty()) {
            return 0.0;
        }

        double totalDistance = 0.0;
        for (RelativeMove move : pathPoints) {
            // Use Chebyshev distance (diagonal movement counts as 1)
            totalDistance += Math.max(Math.abs(move.getDx()), Math.abs(move.getDy()));
        }
        return totalDistance;
    }

    /**
     * Get all world points along this path, starting from a given origin
     */
    public List<WorldPoint> getWorldPoints(WorldPoint origin) {
        List<WorldPoint> points = new ArrayList<>();

        if (pathPoints.isEmpty()) {
            return points;
        }

        int x = origin.getX();
        int y = origin.getY();
        int plane = origin.getPlane();

        // Add the starting point
        points.add(new WorldPoint(x, y, plane));

        // Follow each move
        for (RelativeMove move : pathPoints) {
            x += move.getDx();
            y += move.getDy();
            points.add(new WorldPoint(x, y, plane));
        }

        return points;
    }

    /**
     * Get the full path as world points.
     * Alias for getWorldPoints() using start location as origin.
     */
    public List<WorldPoint> getFullPath() {
        if (start == null || start == PortLocation.EMPTY) {
            return new ArrayList<>();
        }

        return getWorldPoints(start.getNavigationLocation());
    }

    /**
     * Get the name of this path (for compatibility with enum usage)
     */
    public String name() {
        return start.name() + "_" + end.name();
    }

    /**
     * Get all loaded paths (for compatibility with enum values() usage)
     */
    public static PortPaths[] values() {
        return ROUTE_LOOKUP.values().toArray(new PortPaths[0]);
    }

    /**
     * Find a path by name (for compatibility with enum valueOf() usage)
     */
    public static PortPaths valueOf(String name) {
        String[] parts = name.split("_", 2);
        if (parts.length == 2) {
            try {
                PortLocation from = PortLocation.valueOf(parts[0]);
                PortLocation to = PortLocation.valueOf(parts[1]);
                return getPath(from, to);
            } catch (IllegalArgumentException e) {
                // Fall through to return DEFAULT
            }
        }
        return DEFAULT;
    }

    /**
     * Key for looking up routes by start/end ports
     */
    private static class RouteKey {
        private final PortLocation start;
        private final PortLocation end;

        RouteKey(PortLocation start, PortLocation end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            RouteKey routeKey = (RouteKey) o;
            return start == routeKey.start && end == routeKey.end;
        }

        @Override
        public int hashCode() {
            return 31 * start.hashCode() + end.hashCode();
        }
    }
}
