package com.nucleon.porttasks.optimizer;

import com.nucleon.porttasks.enums.PortLocation;
import com.nucleon.porttasks.enums.PortPaths;

import java.util.EnumMap;
import java.util.Map;

public class DistanceCache
{
	private final Map<PortLocation, Map<PortLocation, Integer>> distances;

	public DistanceCache()
	{
		this.distances = new EnumMap<>(PortLocation.class);
		precomputeDistances();
	}

	public int getDistance(PortLocation from, PortLocation to)
	{
		if (from == to)
		{
			return 0;
		}

		Map<PortLocation, Integer> fromMap = distances.get(from);
		if (fromMap == null)
		{
			return Integer.MAX_VALUE;
		}

		Integer distance = fromMap.get(to);
		return distance != null ? distance : Integer.MAX_VALUE;
	}

	private void precomputeDistances()
	{
		for (PortLocation from : PortLocation.values())
		{
			if (from == PortLocation.EMPTY)
			{
				continue;
			}

			Map<PortLocation, Integer> fromMap = new EnumMap<>(PortLocation.class);

			for (PortLocation to : PortLocation.values())
			{
				if (to == PortLocation.EMPTY || from == to)
				{
					continue;
				}

				PortPaths path = PortPaths.getPath(from, to);
				if (path != null && path != PortPaths.DEFAULT)
				{
					fromMap.put(to, (int) Math.round(path.getDistance()));
				}
			}

			if (!fromMap.isEmpty())
			{
				distances.put(from, fromMap);
			}
		}
	}
}
