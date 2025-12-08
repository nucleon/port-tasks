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
			name = "Task 1 Color",
			description = "The color of the navigation line",
			section = pathSection
	)
	default Color getNavColor()
	{
		return new Color(201, 51, 255);
	}
	@ConfigItem(
			keyName = "navColor2",
			name = "Task 2 Color",
			description = "The color of the navigation line",
			section = pathSection
	)
	default Color getNavColor2()
	{
		return new Color(255, 51, 105);
	}
	@ConfigItem(
			keyName = "navColor3",
			name = "Task 3 Color",
			description = "The color of the navigation line",
			section = pathSection
	)
	default Color getNavColor3()
	{
		return new Color(255, 201, 51);
	}
	@ConfigItem(
			keyName = "navColor4",
			name = "Task 4 Color",
			description = "The color of the navigation line",
			section = pathSection
	)
	default Color getNavColor4()
	{
		return new Color(105, 255, 51);
	}
	@ConfigItem(
			keyName = "navColor5",
			name = "Task 5 Color",
			description = "The color of the navigation line",
			section = pathSection
	)
	default Color getNavColor5()
	{
		return new Color(51, 255, 201);
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

	@Range(min = 100, max = 250)
	@ConfigItem(
			keyName = "pathDrawDistance",
			name = "Draw Distance",
			description = "Path Draw Distance",
			section = pathSection
	)
	default int pathDrawDistance()
	{
		return 150;
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
		name = "Color",
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
		return true;
	}

	@ConfigItem(
		keyName = "highlightNoticeboardsColor",
		name = "Color",
		description = "Color used to outline noticeboards in the world",
		position = 4,
		section = overlaySection
	)
	default Color highlightNoticeboardsColor()
	{
		return Color.RED;
	}
	@ConfigItem(
			keyName = "highlightCargoHolds",
			name = "Highlight Cargo Holds",
			description = "Outline cargo holds in your boat",
			position = 5,
			section = overlaySection
	)
	default boolean highlightCargoHolds()
	{
		return true;
	}

	@ConfigItem(
			keyName = "highlightCargoHoldsColor",
			name = "Color",
			description = "Color used to outline cargo holds in your boat",
			position = 6,
			section = overlaySection
	)
	default Color highlightCargoHoldsColor()
	{
		return Color.green;
	}

	@ConfigItem(
			keyName = "highlightHelmMissingCargo",
			name = "Highlight Helm Missing Cargo",
			description = "Outline helm on your boat",
			position = 7,
			section = overlaySection
	)
	default boolean highlightHelmMissingCargo()
	{
		return true;
	}

	@ConfigItem(
		keyName = "noticeBoardTooltip",
		name = "Noticeboard tooltip",
		description = "Task information in a tooltip",
		position = 8,
		section = overlaySection
	)
	default boolean noticeBoardTooltip()
	{
		return true;
	}

	@ConfigItem(
		keyName = "minColor",
		name = "Tooltip minimum color",
		description = "Color to use for the minimum range",
		position = 9,
		section = overlaySection
	)
	default Color minColor()
	{
		return Color.RED;
	}

	@ConfigItem(
		keyName = "maxColor",
		name = "Tooltip maximum color",
		description = "Color to use for the maximum range",
		position = 10,
		section = overlaySection
	)
	default Color maxColor()
	{
		return Color.GREEN;
	}

	@Range(min = 0, max = 100)
	@ConfigItem(
		keyName = "noticeBoardHideOpacity",
		name = "Notice board hider opacity",
		description = "Opacity to obscure notice board tasks. 0-100%",
		position = 11,
		section = overlaySection
	)
	default int noticeBoardHideOpacity()
	{
		return 70;
	}

	@ConfigItem(
		keyName = "noticeBoardHideIncompletable",
		name = "Hide incompletable tasks",
		description = "Hide tasks you do not have the level to complete.",
		position = 12,
		section = overlaySection
	)
	default boolean noticeBoardHideIncompletable()
	{
		return true;
	}

	@ConfigItem(
		keyName = "noticeBoardHideBounty",
		name = "Hide bounty tasks",
		description = "Hide bounty tasks.",
		position = 13,
		section = overlaySection
	)
	default boolean noticeBoardHideBounty()
	{
		return false;
	}

	@ConfigItem(
		keyName = "noticeBoardHideCourier",
		name = "Hide courier tasks",
		description = "Hide courier tasks.",
		position = 14,
		section = overlaySection
	)
	default boolean noticeBoardHideCourier()
	{
		return false;
	}

	@ConfigItem(
		keyName = "noticeBoardHideUntagged",
		name = "Hide untagged tasks",
		description = "Hide tasks you have not tagged. (Shift right-click a task to tag)",
		position = 15,
		section = overlaySection
	)
	default boolean noticeBoardHideUntagged()
	{
		return false;
	}

	@ConfigSection(
		name = "Notice Board Tracker",
		description = "Configuration for notice board reset tracking",
		position = 3
	)
	String noticeBoardTracker = "noticeBoardTrackerSection";

	@ConfigItem(
		keyName = "noticeBoardResetTracker",
		name = "Board reset tracker",
		description = "(Experimental) Adds a chat message indicating how soon until the notice board resets",
		position = 1,
		section = noticeBoardTracker
	)
	default boolean noticeBoardResetTracker()
	{
		return false;
	}

	@Range(min = 0, max = 7)
	@ConfigItem(
		keyName = "noticeBoardState",
		name = "Tasks since reset",
		description = "Number of tasks completed since board reset (0-7)",
		position = 1,
		section = noticeBoardTracker
	)
	default int noticeBoardState()
	{
		return 0;
	}

	enum Overlay
	{
		NONE,
		MAP,
		WORLD,
		BOTH
	}

}
