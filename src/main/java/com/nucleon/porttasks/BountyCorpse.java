package com.nucleon.porttasks;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.runelite.api.Actor;

@AllArgsConstructor
@Data
public class BountyCorpse
{
	private final Actor npc;
	private int lenX;
	private int lenY;
	private final Instant startTime;
	private final int despawnTime;
}
