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

import java.awt.Color;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.Range;

@ConfigGroup(PortTasksConfig.CONFIG_GROUP)
public interface PortTasksConfig extends Config
{
	String CONFIG_GROUP = "porttasks";

	@ConfigSection(
			name = "Path Settings",
			description = "Configure animation and visual settings for tracers",
			position = 1
	)
	String pathSection = "pathSection";
	@ConfigItem(
			keyName = "navColor",
			name = "Default Line Color",
			description = "The color of the navigation line",
			section = pathSection
	)
	default Color getNavColor()
	{
		return Color.GREEN;
	}

	@ConfigItem(
			keyName = "drawOverlay",
			name = "Draw Path",
			description = "Draw path for port task",
			section = pathSection
	)
	default Overlay getDrawOverlay()
	{
		return Overlay.BOTH;
	}

	@ConfigItem(
			keyName = "pathOffset",
			name = "Offset Height Per Task",
			description = "each path will be drawn at a different height",
			section = pathSection
	)
	default boolean enableHeightOffset()
	{
		return false;
	}
	@ConfigSection(
			name = "Tracer Settings",
			description = "Configure animation and visual settings for overlay direction tracers",
			position = 1
	)
	String tracerSection = "tracerSection";

	@ConfigItem(
			keyName = "enableTracer",
			name = "Enable Tracer Overlay",
			description = "Toggle tracer animation on path lines",
			section = tracerSection
	)
	default boolean enableTracer()
	{
		return false;
	}

	@Range(min = 0, max = 60)
	@ConfigItem(
			keyName = "tracerSpeed",
			name = "Tracer Speed",
			description = "Adjust how fast the tracer animation moves (lower = slower)",
			section = tracerSection
	)
	default int tracerSpeed()
	{
		return 30;
	}

	@Range(min = 0, max = 100)
	@ConfigItem(
			keyName = "tracerIntensity",
			name = "Tracer Intensity",
			description = "Control brightness or visibility of the tracer (0–100%)",
			section = tracerSection
	)
	default int tracerIntensity()
	{
		return 50;
	}

	@ConfigSection(
		name = "Overlay Settings",
		description = "Configuration for overlays",
		position = 2
	)
	String overlaySection = "overlaySection";

	@ConfigItem(
		keyName = "highlightGangplanks",
		name = "Highlight Gangplanks",
		description = "Outline gangplanks in the world",
		position = 1,
		section = overlaySection
	)
	default boolean highlightGangplanks()
	{
		return true;
	}

	@ConfigItem(
		keyName = "highlightGangplanksColor",
		name = "Highlight Gangplanks Color",
		description = "Color used to outline gangplanks in the world",
		position = 2,
		section = overlaySection
	)
	default Color highlightGangplanksColor()
	{
		return Color.YELLOW;
	}

	@ConfigItem(
		keyName = "highlightNoticeboards",
		name = "Highlight Noticeboards",
		description = "Outline noticeboards in the world",
		position = 3,
		section = overlaySection
	)
	default boolean highlightNoticeboards()
	{
		return false;
	}

	@ConfigItem(
		keyName = "highlightNoticeboardsColor",
		name = "Highlight Noticeboards Color",
		description = "Color used to outline noticeboards in the world",
		position = 4,
		section = overlaySection
	)
	default Color highlightNoticeboardsColor()
	{
		return Color.RED;
	}

	enum Overlay
	{
		NONE,
		MAP,
		WORLD,
		BOTH
	}
}
