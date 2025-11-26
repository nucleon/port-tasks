package com.nucleon.porttasks;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.runelite.api.widgets.Widget;

@Getter
@AllArgsConstructor
public class OfferedTaskData
{
	private final Widget taskWidget;
	private final int levelRequired;
}
