package com.nucleon.enums;

import java.util.HashMap;
import java.util.Map;

public enum PortTaskTrigger
{
	TASK_SLOT_0_ID(18017, "port_task_slot_0_id", TaskType.ID, 0),
	TASK_SLOT_0_TAKEN(18018, "port_task_slot_0_cargo_taken", TaskType.TAKEN, 0),
	TASK_SLOT_0_DELIVERED(18019, "port_task_slot_0_cargo_delivered", TaskType.DELIVERED, 0),
	TASK_SLOT_1_ID(18020, "port_task_slot_1_id", TaskType.ID, 1),
	TASK_SLOT_1_TAKEN(18021, "port_task_slot_1_cargo_taken", TaskType.TAKEN, 1),
	TASK_SLOT_1_DELIVERED(18022, "port_task_slot_1_cargo_delivered", TaskType.DELIVERED, 1),
	TASK_SLOT_2_ID(18023, "port_task_slot_2_id", TaskType.ID, 2),
	TASK_SLOT_2_TAKEN(18024, "port_task_slot_2_cargo_taken", TaskType.TAKEN, 2),
	TASK_SLOT_2_DELIVERED(18025, "port_task_slot_2_cargo_delivered", TaskType.DELIVERED, 2),
	TASK_SLOT_3_ID(18026, "port_task_slot_3_id", TaskType.ID, 3),
	TASK_SLOT_3_TAKEN(18027, "port_task_slot_3_cargo_taken", TaskType.TAKEN, 3),
	TASK_SLOT_3_DELIVERED(18028, "port_task_slot_3_cargo_delivered", TaskType.DELIVERED, 3),
	TASK_SLOT_4_ID(18029, "port_task_slot_4_id", TaskType.ID, 4),
	TASK_SLOT_4_TAKEN(18030, "port_task_slot_4_cargo_taken", TaskType.TAKEN, 4),
	TASK_SLOT_4_DELIVERED(18031, "port_task_slot_4_cargo_delivered", TaskType.DELIVERED, 4),
	LAST_CARGO_TAKEN(18033, "port_task_last_cargo_taken", TaskType.OTHER, -1);

	private final int id;
	private final String name;
	private final TaskType type;
	private final int slot;

	public enum TaskType
	{
		ID, TAKEN, DELIVERED, OTHER
	}

	PortTaskTrigger(int id, String name, TaskType type, int slot)
	{
		this.id = id;
		this.name = name;
		this.type = type;
		this.slot = slot;
	}

	public int getId()
	{
		return id;
	}

	public String getName()
	{
		return name;
	}

	public TaskType getType()
	{
		return type;
	}

	public int getSlot()
	{
		return slot;
	}

	private static final Map<Integer, PortTaskTrigger> lookup = new HashMap<>();
	static
	{
		for (PortTaskTrigger v : values())
		{
			lookup.put(v.id, v);
		}
	}

	public static PortTaskTrigger fromId(int id)
	{
		return lookup.get(id);
	}

	public static boolean contains(int id)
	{
		return lookup.containsKey(id);
	}
}