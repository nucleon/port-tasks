package com.nucleon.porttasks.optimizer;

import com.nucleon.porttasks.CourierTask;
import com.nucleon.porttasks.enums.PortLocation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RouteStop {
    private final PortLocation port;
    private final RouteStopType type;
    private final CourierTask task;
}
