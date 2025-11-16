package com.nucleon.porttasks.overlay;

import com.nucleon.porttasks.PortTasksConfig;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.events.BeforeRender;
import net.runelite.client.eventbus.Subscribe;
import javax.inject.Inject;
import javax.inject.Singleton;

public class TracerConfig
{
	@Getter
	private int frameTick = 0;
	@Getter
	@Setter
	private int tracerSpeed = 30;
	@Getter
	@Setter
	private float tracerIntensity = 0.6f;
	@Getter
	@Setter
	private boolean tracerEnabled;
	private long lastUpdateNanos = 0;
	private final PortTasksConfig config;

	@Inject
	@Singleton
	public TracerConfig(PortTasksConfig config)
	{
		this.config = config;
	}

	@SuppressWarnings("unused")
	@Subscribe
	public void onBeforeRender(BeforeRender event)
	{
		if (!tracerEnabled)
		{
			return;
		}

		long FRAME_INTERVAL_NANOS = 1_000_000_000L / tracerSpeed;
		long now = System.nanoTime();
		if (now - lastUpdateNanos >= FRAME_INTERVAL_NANOS)
		{
			frameTick = (frameTick + 1) % 1000;
			lastUpdateNanos = now;
		}
	}
	public void loadConfigs(PortTasksConfig config)
	{
		this.tracerEnabled = config.enableTracer();
		this.tracerIntensity = 1f - (config.tracerIntensity() / 100f);
		this.tracerSpeed = config.tracerSpeed();
	}
}
