package com.nucleon;

import com.nucleon.enums.PortTaskData;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

import java.awt.Color;

@Getter
@Setter
@AllArgsConstructor
public class PortTask
{
	private PortTaskData data;
	private int slot;
	private boolean taken;
	private boolean delivered;
	private boolean tracking;
	private boolean active;
	private Color overlayColor;
}
