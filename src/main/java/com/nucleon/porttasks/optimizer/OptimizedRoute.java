package com.nucleon.porttasks.optimizer;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class OptimizedRoute {
    private final List<RouteStop> stops;
    private final int totalDistance;
}
