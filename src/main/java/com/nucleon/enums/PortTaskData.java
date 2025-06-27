package com.nucleon.enums;

public enum PortTaskData
{
	TASK_102(102, PortLocation.PORT_SARIM, PortLocation.PORT_SARIM);

	private final int id;
	private final PortLocation cargoLocation;
	private final PortLocation deliveryLocation;

	PortTaskData(int id, PortLocation startArea, PortLocation destination)
	{
		this.id = id;
		this.cargoLocation = startArea;
		this.deliveryLocation = destination;
	}

	public int getId()
	{
		return id;
	}

	public PortLocation getCargoLocation()
	{
		return cargoLocation;
	}

	public PortLocation getDeliveryLocation()
	{
		return deliveryLocation;
	}

	public static PortTaskData fromId(int id)
	{
		for (PortTaskData task : values())
		{
			if (task.id == id)
				return task;
		}
		return null;
	}
}

