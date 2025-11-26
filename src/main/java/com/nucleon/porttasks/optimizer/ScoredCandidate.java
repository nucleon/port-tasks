package com.nucleon.porttasks.optimizer;

import com.nucleon.porttasks.enums.CourierTaskData;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
class ScoredCandidate {
    private final CourierTaskData task;
    private final double score;
    private final String reason;
    private final boolean hasConflict;
    private final boolean fitsRoute;
}
