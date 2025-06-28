package com.nucleon;

import java.awt.Color;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("example")
public interface SailingHelperConfig extends Config
{
	@ConfigItem(
		keyName = "navColor",
		name = "Navigation Line Color",
		description = "The color of the navigation line"
	)
	default Color getNavColor()
	{
		return Color.GREEN;
	}
	enum Overlay
	{
		NONE,
		MAP,
		WORLD,
		BOTH
	}
  	@ConfigItem(
		keyName = "drawOverlay",
		name = "Draw path",
		description = "Draw path for port task"
	)
	default Overlay getDrawOverlay()
	{
		return Overlay.BOTH;
	}
}
