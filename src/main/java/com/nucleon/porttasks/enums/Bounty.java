package com.nucleon.porttasks.enums;

import lombok.Value;
import net.runelite.api.gameval.ItemID;
import net.runelite.api.gameval.NpcID;
import java.util.HashMap;
import java.util.Map;

@Value
public class Bounty
{
	int itemId;
	int npcId;
	int deadNpcId;
	String label;

	private static final Map<Integer, Bounty> BY_ITEM_ID = new HashMap<>();

	static
	{
		// Tern bounties
		register(ItemID.SAILING_TERN_FEATHER, NpcID.SAILING_TERN, NpcID.SAILING_TERN_DEAD, "tern feather");
		register(ItemID.SAILING_TERN_BEAK, NpcID.SAILING_TERN, NpcID.SAILING_TERN_DEAD, "tern beak");

		// Mogre bounties
		register(ItemID.SAILING_SEA_MOGRE_MACE, NpcID.SAILING_SEA_MOGRE, NpcID.SAILING_SEA_MOGRE_DEAD, "mogre mace");
		register(ItemID.SAILING_SEA_MOGRE_HEAD, NpcID.SAILING_SEA_MOGRE, NpcID.SAILING_SEA_MOGRE_DEAD, "mogre head");

		// Bull shark bounties
		register(ItemID.SAILING_BULL_SHARK_JAW, NpcID.SAILING_BULL_SHARK, NpcID.SAILING_BULL_SHARK_DEAD, "bull shark jaw");
		register(ItemID.SAILING_BULL_SHARK_LIVER, NpcID.SAILING_BULL_SHARK, NpcID.SAILING_BULL_SHARK_DEAD, "bull shark liver");

		// Tiger shark bounties
		register(ItemID.SAILING_TIGER_SHARK_JAW, NpcID.SAILING_TIGER_SHARK, NpcID.SAILING_TIGER_SHARK_DEAD, "tiger shark jaw");
		register(ItemID.SAILING_TIGER_SHARK_LIVER, NpcID.SAILING_TIGER_SHARK, NpcID.SAILING_TIGER_SHARK_DEAD, "tiger shark liver");

		// Osprey bounties
		register(ItemID.SAILING_OSPREY_BEAK, NpcID.SAILING_OSPREY, NpcID.SAILING_OSPREY_DEAD, "osprey beak");
		register(ItemID.SAILING_OSPREY_FEATHER, NpcID.SAILING_OSPREY, NpcID.SAILING_OSPREY_DEAD, "osprey feather");

		// Pygmy kraken bounties
		register(ItemID.SAILING_PYGMY_KRAKEN_INK_SAC, NpcID.SAILING_PYGMY_KRAKEN, NpcID.SAILING_PYGMY_KRAKEN_DEAD, "pygmy kraken ink sac");
		register(ItemID.SAILING_PYGMY_KRAKEN_TENTACLE, NpcID.SAILING_PYGMY_KRAKEN, NpcID.SAILING_PYGMY_KRAKEN_DEAD, "pygmy kraken tentacle");

		// Butterfly ray bounties
		register(ItemID.SAILING_BUTTERFLY_RAY_FIN, NpcID.SAILING_BUTTERFLY_RAY, NpcID.SAILING_BUTTERFLY_RAY_DEAD, "butterfly ray fin");
		register(ItemID.SAILING_BUTTERFLY_RAY_SKIN, NpcID.SAILING_BUTTERFLY_RAY, NpcID.SAILING_BUTTERFLY_RAY_DEAD, "butterfly ray skin");

		// Hammerhead shark bounties
		register(ItemID.SAILING_HAMMERHEAD_SHARK_LIVER, NpcID.SAILING_HAMMERHEAD_SHARK, NpcID.SAILING_HAMMERHEAD_SHARK_DEAD, "hammerhead shark liver");
		register(ItemID.SAILING_HAMMERHEAD_SHARK_JAW, NpcID.SAILING_HAMMERHEAD_SHARK, NpcID.SAILING_HAMMERHEAD_SHARK_DEAD, "hammerhead shark jaw");

		// Frigatebird bounties
		register(ItemID.SAILING_FRIGATEBIRD_BEAK, NpcID.SAILING_FRIGATEBIRD, NpcID.SAILING_FRIGATEBIRD_DEAD, "frigatebird beak");
		register(ItemID.SAILING_FRIGATEBIRD_FEATHER, NpcID.SAILING_FRIGATEBIRD, NpcID.SAILING_FRIGATEBIRD_DEAD, "frigatebird feather");

		// Eagle ray bounties
		register(ItemID.SAILING_EAGLE_RAY_SKIN, NpcID.SAILING_EAGLE_RAY, NpcID.SAILING_EAGLE_RAY_DEAD, "eagle ray skin");
		register(ItemID.SAILING_EAGLE_RAY_FIN, NpcID.SAILING_EAGLE_RAY, NpcID.SAILING_EAGLE_RAY_DEAD, "eagle ray fin");

		// Albatross bounties
		register(ItemID.SAILING_ALBATROSS_BEAK, NpcID.SAILING_ALBATROSS, NpcID.SAILING_ALBATROSS_DEAD, "albatross beak");
		register(ItemID.SAILING_ALBATROSS_FEATHER, NpcID.SAILING_ALBATROSS, NpcID.SAILING_ALBATROSS_DEAD, "albatross feather");

		// Great white shark bounties
		register(ItemID.SAILING_GREAT_WHITE_SHARK_JAW, NpcID.SAILING_GREAT_WHITE_SHARK, NpcID.SAILING_GREAT_WHITE_SHARK_DEAD, "great white shark jaw");
		register(ItemID.SAILING_GREAT_WHITE_SHARK_LIVER, NpcID.SAILING_GREAT_WHITE_SHARK, NpcID.SAILING_GREAT_WHITE_SHARK_DEAD, "great white shark liver");

		// Stingray bounties
		register(ItemID.SAILING_STINGRAY_SKIN, NpcID.SAILING_STINGRAY, NpcID.SAILING_STINGRAY_DEAD, "stingray skin");
		register(ItemID.SAILING_STINGRAY_FIN, NpcID.SAILING_STINGRAY, NpcID.SAILING_STINGRAY_DEAD, "stingray fin");

		// Spined kraken bounties
		register(ItemID.SAILING_SPINED_KRAKEN_INK_SAC, NpcID.SAILING_SPINED_KRAKEN, NpcID.SAILING_SPINED_KRAKEN_DEAD, "spined kraken ink sac");
		register(ItemID.SAILING_SPINED_KRAKEN_TENTACLE, NpcID.SAILING_SPINED_KRAKEN, NpcID.SAILING_SPINED_KRAKEN_DEAD, "spined kraken tentacle");

		// Armoured kraken bounties
		register(ItemID.SAILING_ARMOURED_KRAKEN_INK_SAC, NpcID.SAILING_ARMOURED_KRAKEN, NpcID.SAILING_ARMOURED_KRAKEN_DEAD, "armoured kraken ink sac");
		register(ItemID.SAILING_ARMOURED_KRAKEN_TENTACLE, NpcID.SAILING_ARMOURED_KRAKEN, NpcID.SAILING_ARMOURED_KRAKEN_DEAD, "armoured kraken tentacle");

		// Manta ray bounty
		register(ItemID.SAILING_MANTA_RAY_SKIN, NpcID.SAILING_MANTA_RAY, NpcID.SAILING_MANTA_RAY_DEAD, "manta ray skin");

		// Orca bounties
		register(ItemID.SAILING_ORCA_TEETH, NpcID.SAILING_ORCA, NpcID.SAILING_ORCA_DEAD, "orca teeth");
		register(ItemID.SAILING_ORCA_BLUBBER, NpcID.SAILING_ORCA, NpcID.SAILING_ORCA_DEAD, "orca blubber");

		// Narwhal bounties
		register(ItemID.SAILING_NARWHAL_BLUBBER, NpcID.SAILING_NARWHAL, NpcID.SAILING_NARWHAL_DEAD, "narwhal blubber");
		register(ItemID.SAILING_NARWHAL_TUSK, NpcID.SAILING_NARWHAL, NpcID.SAILING_NARWHAL_DEAD, "narwhal tusk");

		// Vampyre kraken bounties
		register(ItemID.SAILING_VAMPYRE_KRAKEN_INK_SAC, NpcID.SAILING_VAMPYRE_KRAKEN, NpcID.SAILING_VAMPYRE_KRAKEN_DEAD, "vampyre kraken ink sac");
		register(ItemID.SAILING_VAMPYRE_KRAKEN_TENTACLE, NpcID.SAILING_VAMPYRE_KRAKEN, NpcID.SAILING_VAMPYRE_KRAKEN_DEAD, "vampyre kraken tentacle");
	}

	private static void register(int itemId, int npcId, int deadNpcId, String label)
	{
		BY_ITEM_ID.put(itemId, new Bounty(itemId, npcId, deadNpcId, label));
	}

	/**
	 * Get the Bounty instance for a given ItemID.
	 * @param itemId the ItemID integer value
	 * @return the Bounty instance, or null if not found
	 */
	public static Bounty fromItemId(int itemId)
	{
		Bounty bounty = BY_ITEM_ID.get(itemId);
		if (bounty == null) {
			throw new IllegalArgumentException("No bounty found for ItemID: " + itemId);
		}

		return bounty;
	}
}
