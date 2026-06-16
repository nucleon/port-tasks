package com.nucleon.porttasks;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.runelite.api.NPC;

@AllArgsConstructor
@Data
public class BountyCorpse
{
	private final NPC npc;
	private final Instant startTime;
	private final int tickCount;
	private final int despawnTime;
}
