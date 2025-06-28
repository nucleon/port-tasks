package com.nucleon.enums;

// todo: replace with net.runelite.api.gameval.ItemID when released
import com.nucleon.gameval.ItemID;

public enum PortTaskData
{
	COURIER_WANTED(null, PortLocation.PORT_SARIM, PortLocation.PORT_SARIM, PortLocation.PANDEMONIUM, "Courier wanted", ItemID.CARGO_CRATE_OF_STEEL_SWORDS, 1),
	LONELY_CRATE_LOOKING_FOR_LOCAL_COURIER(null, PortLocation.PORT_SARIM, PortLocation.PORT_SARIM, PortLocation.MUSA_POINT, "Lonely crate looking for local courier", ItemID.CARGO_CRATE_OF_RAW_FISH, 1),
	BANANA_DELIVERY(null, PortLocation.PORT_SARIM, PortLocation.MUSA_POINT, PortLocation.PORT_SARIM, "Banana delivery", ItemID.CARGO_CRATE_OF_BANANAS, 1),
	BANANA_COMPENSATION(null, PortLocation.PORT_SARIM, PortLocation.PORT_SARIM, PortLocation.MUSA_POINT, "Banana compensation", ItemID.CARGO_CRATE_OF_ROTTEN_BANANAS, 1),
	A_BARTENDERS_PROBLEM(null, PortLocation.PORT_SARIM, PortLocation.PORT_SARIM, PortLocation.PANDEMONIUM, "A bartender's problem", ItemID.CARGO_CRATE_OF_BEER, 3),
	LOGS_ASTRAY(null, PortLocation.PORT_SARIM, PortLocation.MUSA_POINT, PortLocation.PORT_SARIM, "Logs astray", ItemID.CARGO_CRATE_OF_LOGS, 2),
	RUM_RUN(null, PortLocation.PORT_SARIM, PortLocation.PORT_SARIM, PortLocation.MUSA_POINT, "Rum run", ItemID.CARGO_CRATE_OF_RUM, 1),
	LEGAL_COURIER_SERVICES_REQUESTED(null, PortLocation.PORT_SARIM, PortLocation.MUSA_POINT, PortLocation.PORT_SARIM, "Legal' courier services requested", ItemID.CARGO_CRATE_OF_SMUGGLED_RUM, 1),
	KEG_REMOVAL_SERVICES_REQUIRED(null, PortLocation.PORT_SARIM, PortLocation.PANDEMONIUM, PortLocation.PORT_SARIM, "Keg removal services required", ItemID.CARGO_CRATE_OF_KEGS, 2),
	BAIT_TO_CATHERBY(null, PortLocation.PORT_SARIM, PortLocation.PORT_SARIM, PortLocation.CATHERBY, "Bait to catherby", ItemID.CARGO_CRATE_OF_BAIT, 2),
	SARAHS_POTATOES(null, PortLocation.PORT_SARIM, PortLocation.PORT_SARIM, PortLocation.CATHERBY, "Sarah's potatoes", ItemID.CARGO_CRATE_OF_POTATOES, 3),
	A_MISLAID_PORT_ORDER(null, PortLocation.PORT_SARIM, PortLocation.CATHERBY, PortLocation.BRIMHAVEN, "A mislaid port order", ItemID.CARGO_CRATE_OF_COMPOST, 4),
	SALAMANDER_PROTECTION(null, PortLocation.PORT_SARIM, PortLocation.PORT_SARIM, PortLocation.EAST_ARDOUGNE, "Salamander protection", ItemID.CARGO_CRATE_OF_RED_SALAMANDERS, 2),
	TAR_EXPORT_REQUIRED(null, PortLocation.PORT_SARIM, PortLocation.PORT_SARIM, PortLocation.PORT_KHAZARD, "Tar export required", ItemID.CARGO_CRATE_OF_SWAMP_PASTE, 3),
	SWORD_IDENTIFICATION(null, PortLocation.PORT_SARIM, PortLocation.PORT_KHAZARD, PortLocation.PORT_SARIM, "Sword identification", ItemID.CARGO_CRATE_OF_IDENTIFIED_SWORDS, 4),
	ARMOUR_COURIER(null, PortLocation.PORT_SARIM, PortLocation.PORT_SARIM, PortLocation.PANDEMONIUM, "Armour courier", ItemID.CARGO_CRATE_OF_MITHRIL_ARMOUR, 1),
	SPICES_NEEDED(null, PortLocation.PORT_SARIM, PortLocation.PANDEMONIUM, PortLocation.PORT_SARIM, "Spices needed", ItemID.CARGO_CRATE_OF_SPICES, 1),
	FRESH_LOBSTER_DELIVERY(null, PortLocation.CATHERBY, PortLocation.CATHERBY, PortLocation.MUSA_POINT, "Fresh lobster delivery", ItemID.CARGO_CRATE_OF_LIVE_LOBSTERS, 3),
	WAX_WAX_AND_MORE_WAX(null, PortLocation.CATHERBY, PortLocation.CATHERBY, PortLocation.PANDEMONIUM, "Wax, wax and more wax", ItemID.CARGO_CRATE_OF_WAX, 2),
	CALEBS_COCONUTS(null, PortLocation.CATHERBY, PortLocation.PANDEMONIUM, PortLocation.CATHERBY, "Caleb's coconuts", ItemID.CARGO_CRATE_OF_COCONUTS, 2),
	VIALS_TO_THE_HOLY_ISLAND(null, PortLocation.CATHERBY, PortLocation.CATHERBY, PortLocation.ENTRANA, "Vials to the holy island", ItemID.CARGO_CRATE_OF_VIALS, 8),
	MISPLACED_SILK(null, PortLocation.CATHERBY, PortLocation.CATHERBY, PortLocation.EAST_ARDOUGNE, "Misplaced silk", ItemID.CARGO_CRATE_OF_SILK, 5),
	PINEAPPLE_MADNESS(null, PortLocation.CATHERBY, PortLocation.BRIMHAVEN, PortLocation.CATHERBY, "Pineapple madness", ItemID.CARGO_CRATE_OF_PINEAPPLES, 3),
	SHRIMP_FOR_THE_SHRIMP_AND_PARROT(null, PortLocation.CATHERBY, PortLocation.CATHERBY, PortLocation.BRIMHAVEN, "Shrimp for the shrimp and parrot", ItemID.CARGO_CRATE_OF_FRESH_FISH, 3),
	CABBAGE_CRAZY(null, PortLocation.CATHERBY, PortLocation.CATHERBY, PortLocation.EAST_ARDOUGNE, "Cabbage crazy", ItemID.CARGO_CRATE_OF_CABBAGES, 5),
	GPDT_FASTTRACK_REQUIRED(null, PortLocation.CATHERBY, PortLocation.EAST_ARDOUGNE, PortLocation.CATHERBY, "Gpdt fast-track required", ItemID.CARGO_CRATE_OF_STEEL_ARROWTIPS, 2),
	NEED_HELP_WITH_A_KARAMBWAN_DELIVERY(null, PortLocation.CATHERBY, PortLocation.BRIMHAVEN, PortLocation.CATHERBY, "Need help with a karambwan delivery", ItemID.CARGO_CRATE_OF_RAW_KARAMBWAN, 1),
	KHAZARD_RENNOVATIONS(null, PortLocation.CATHERBY, PortLocation.CATHERBY, PortLocation.PORT_KHAZARD, "Khazard rennovations", ItemID.CARGO_CRATE_OF_OAK_PLANKS, 4),
	CHARTER_STOCK_REDISTRIBUTION(null, PortLocation.CATHERBY, PortLocation.PORT_KHAZARD, PortLocation.CATHERBY, "Charter stock redistribution", ItemID.CARGO_CRATE_OF_GLASS_MAKE_SUPPLIES, 4),
	GOT_SOME_COAL_THAT_NEEDS_EXPORTING(null, PortLocation.CATHERBY, PortLocation.CATHERBY, PortLocation.PORT_KHAZARD, "Got some coal that needs exporting", ItemID.CARGO_CRATE_OF_COAL, 5),
	SILVER_FOR_ARDOUGNE(null, PortLocation.CATHERBY, PortLocation.CATHERBY, PortLocation.EAST_ARDOUGNE, "Silver for ardougne", ItemID.CARGO_CRATE_OF_SILVER_JEWELLERY, 6),
	URGENT_REQUEST_FOR_THE_SPIRIT_ANGLERS_OF_UNKAH(null, PortLocation.CATHERBY, PortLocation.CATHERBY, PortLocation.RUINS_OF_UNKAH, "Urgent request for the spirit anglers of unkah!", ItemID.CARGO_CRATE_OF_BUCKETS, 8),
	MURPHYS_CATCH_OF_THE_DAY(null, PortLocation.PORT_KHAZARD, PortLocation.PORT_KHAZARD, PortLocation.EAST_ARDOUGNE, "Murphy's catch of the day", ItemID.CARGO_CRATE_OF_TRAWLER_FISH, 6),
	GOLD_RUSH(null, PortLocation.PORT_KHAZARD, PortLocation.BRIMHAVEN, PortLocation.EAST_ARDOUGNE, "Gold rush", ItemID.CARGO_CRATE_OF_GOLD_ORE, 5),
	MITHRIL_FOR_OUR_FLEET(null, PortLocation.PORT_KHAZARD, PortLocation.EAST_ARDOUGNE, PortLocation.PORT_KHAZARD, "Mithril for our fleet", ItemID.CARGO_CRATE_OF_MITHRIL_ARMOUR, 5),
	BERTS_SAND_DELIVERY(null, PortLocation.PORT_KHAZARD, PortLocation.PORT_KHAZARD, PortLocation.ENTRANA, "Bert's sand delivery", ItemID.CARGO_CRATE_OF_SAND, 6),
	DISCRETION_REQUIRED(null, PortLocation.PORT_KHAZARD, PortLocation.CATHERBY, PortLocation.PORT_KHAZARD, "Discretion required", ItemID.CARGO_CRATE_OF_SECRET_STUFF, 3),
	DRAGON_EQUIPMENT_TO_MYTHS_GUILD(null, PortLocation.PORT_KHAZARD, PortLocation.PORT_KHAZARD, PortLocation.CORSAIR_COVE, "Dragon equipment to myths' guild", ItemID.CARGO_CRATE_OF_DRAGON_DAGGERS, 3),
	EMERGENCY_BEER_SUPPLY_REQUEST(null, PortLocation.PORT_KHAZARD, PortLocation.MUSA_POINT, PortLocation.PORT_KHAZARD, "Emergency beer supply request", ItemID.CARGO_CRATE_OF_BEER, 5),
	SPIRIT_ANGLER_SUPPLY_ORDER(null, PortLocation.PORT_KHAZARD, PortLocation.PORT_KHAZARD, PortLocation.RUINS_OF_UNKAH, "Spirit angler supply order", ItemID.CARGO_CRATE_OF_SHIP_PARTS, 5),
	NEED_HELP_WITH_CARGO_RECOVERY(null, PortLocation.PORT_KHAZARD, PortLocation.RUINS_OF_UNKAH, PortLocation.PORT_KHAZARD, "Need help with cargo recovery", ItemID.CARGO_CRATE_OF_DRAGON_EQUIPMENT, 3),
	OUTFITTING_THE_ANGLERS(null, PortLocation.PORT_KHAZARD, PortLocation.EAST_ARDOUGNE, PortLocation.RUINS_OF_UNKAH, "Outfitting the anglers", ItemID.CARGO_CRATE_OF_SPIRIT_ANGLERS_GARB, 6),
	ANGLERS_OUTFIT_RECOVERY(null, PortLocation.PORT_KHAZARD, PortLocation.RUINS_OF_UNKAH, PortLocation.PORT_KHAZARD, "Angler's outfit recovery", ItemID.CARGO_CRATE_OF_ANGLERS_OUTFIT, 5),
	BANANA_COLLECTION(null, PortLocation.RUINS_OF_UNKAH, PortLocation.MUSA_POINT, PortLocation.RUINS_OF_UNKAH, "Banana collection", ItemID.CARGO_CRATE_OF_BANANAS, 5),
	EQUIPMENT_REQUISITION(null, PortLocation.RUINS_OF_UNKAH, PortLocation.EAST_ARDOUGNE, PortLocation.RUINS_OF_UNKAH, "Equipment requisition", ItemID.CARGO_CRATE_OF_FISHING_EQUIPMENT, 2),
	GOT_SOME_GRANITE_THAT_NEEDS_DELIVERING(null, PortLocation.RUINS_OF_UNKAH, PortLocation.RUINS_OF_UNKAH, PortLocation.BRIMHAVEN, "Got some granite that needs delivering", ItemID.CARGO_CRATE_OF_GRANITE, 4),
	SILK_REQUEST(null, PortLocation.RUINS_OF_UNKAH, PortLocation.RUINS_OF_UNKAH, PortLocation.CATHERBY, "Silk request", ItemID.CARGO_CRATE_OF_SILK, 6),
	IMPORTANT_CARGO(null, PortLocation.RUINS_OF_UNKAH, PortLocation.RUINS_OF_UNKAH, PortLocation.PORT_KHAZARD, "Important cargo", ItemID.CARGO_CRATE_OF_IMPORTANCE, 1),
	IN_NEED_OF_A_SILK_RESUPPLY(null, PortLocation.RUINS_OF_UNKAH, PortLocation.RUINS_OF_UNKAH, PortLocation.EAST_ARDOUGNE, "In need of a silk resupply", ItemID.CARGO_CRATE_OF_SILK, 8),
	COCKTAIL_EXPRESS(null, PortLocation.RUINS_OF_UNKAH, PortLocation.PANDEMONIUM, PortLocation.RUINS_OF_UNKAH, "Cocktail express", ItemID.CARGO_CRATE_OF_COCKTAILS, 2),
	CACTUS_EXPERIMENTS(null, PortLocation.RUINS_OF_UNKAH, PortLocation.RUINS_OF_UNKAH, PortLocation.PORT_SARIM, "Cactus experiments", ItemID.CARGO_CRATE_OF_CACTUS, 2),
	FOOD_STORAGE_RESUPPLY(null, PortLocation.RUINS_OF_UNKAH, PortLocation.RUINS_OF_UNKAH, PortLocation.CORSAIR_COVE, "Food storage resupply", ItemID.CARGO_CRATE_OF_FRESH_FISH, 4),
	CORAL_EXPERIMENTS(null, PortLocation.RUINS_OF_UNKAH, PortLocation.SUMMER_SHORE, PortLocation.RUINS_OF_UNKAH, "Coral experiments", ItemID.CARGO_CRATE_OF_CORAL, 4),
	CONSTRUCTION_SUPPLIES(null, PortLocation.RUINS_OF_UNKAH, PortLocation.RUINS_OF_UNKAH, PortLocation.SUMMER_SHORE, "Construction supplies", ItemID.CARGO_CRATE_OF_SANDSTONE, 7),
	FISH_DELIVERY(null, PortLocation.PANDEMONIUM, PortLocation.PANDEMONIUM, PortLocation.PORT_SARIM, "Fish delivery", ItemID.CARGO_CRATE_OF_RAW_FISH, 1),
	FISH_FOR_THE_FACE(null, PortLocation.PANDEMONIUM, PortLocation.PANDEMONIUM, PortLocation.PORT_SARIM, "Fish for 'the face'", ItemID.CARGO_CRATE_OF_FRESH_FISH, 1),
	BILLYNOSHIPPARTS(null, PortLocation.PANDEMONIUM, PortLocation.PANDEMONIUM, PortLocation.MUSA_POINT, "Billy-no-ship-parts", ItemID.CARGO_CRATE_OF_SHIP_PARTS, 1),
	SECRET_PIRATE_BUISNESS(null, PortLocation.PANDEMONIUM, PortLocation.PANDEMONIUM, PortLocation.MUSA_POINT, "Secret pirate buisness", ItemID.CARGO_CRATE_OF_SECRET_STUFF, 1),
	WE_NEED_MORE_RUM(null, PortLocation.PANDEMONIUM, PortLocation.MUSA_POINT, PortLocation.PANDEMONIUM, "We need more rum!", ItemID.CARGO_CRATE_OF_RUM, 1),
	STEEL_COLLECTION(null, PortLocation.PANDEMONIUM, PortLocation.PORT_SARIM, PortLocation.PANDEMONIUM, "Steel collection", ItemID.CARGO_CRATE_OF_STEEL_BARS, 1),
	JEWELLERY_DELIVERY(null, PortLocation.PANDEMONIUM, PortLocation.PANDEMONIUM, PortLocation.PORT_SARIM, "Jewellery delivery", ItemID.CARGO_CRATE_OF_SILVER_JEWELLERY, 2),
	RETURN_TO_SENDER(null, PortLocation.PANDEMONIUM, PortLocation.PANDEMONIUM, PortLocation.MUSA_POINT, "Return to sender", ItemID.CARGO_CRATE_OF_ROTTEN_BANANAS, 3),
	PIRATE_GROG_DELIVERY(null, PortLocation.PANDEMONIUM, PortLocation.PANDEMONIUM, PortLocation.CATHERBY, "Pirate grog delivery", ItemID.CARGO_CRATE_OF_GROG, 2),
	IN_NEED_OF_ARROWTIPS(null, PortLocation.PANDEMONIUM, PortLocation.CATHERBY, PortLocation.PANDEMONIUM, "In need of arrowtips", ItemID.CARGO_CRATE_OF_STEEL_ARROWTIPS, 2),
	PINEAPPLE_COLLECTION(null, PortLocation.PANDEMONIUM, PortLocation.BRIMHAVEN, PortLocation.PANDEMONIUM, "Pineapple collection", ItemID.CARGO_CRATE_OF_BRIMHAVEN_PINEAPPLES, 3),
	SENDING_SAND(null, PortLocation.PANDEMONIUM, PortLocation.PANDEMONIUM, PortLocation.EAST_ARDOUGNE, "Sending sand", ItemID.CARGO_CRATE_OF_SAND, 4),
	COCKTAILS_FOR_ALL(null, PortLocation.PANDEMONIUM, PortLocation.PANDEMONIUM, PortLocation.RUINS_OF_UNKAH, "Cocktails for all", ItemID.CARGO_CRATE_OF_COCKTAILS, 2),
	URGENT_DELIVERY(null, PortLocation.PANDEMONIUM, PortLocation.PANDEMONIUM, PortLocation.PORT_KHAZARD, "Urgent delivery", ItemID.CARGO_CRATE_OF_KYATT_TEETH, 3),
	TAR_NEEDED(null, PortLocation.PANDEMONIUM, PortLocation.PORT_KHAZARD, PortLocation.PANDEMONIUM, "Tar needed", ItemID.CARGO_CRATE_OF_SWAMP_PASTE, 3),
	CARGO_RECLAIMS(null, PortLocation.MUSA_POINT, PortLocation.PORT_SARIM, PortLocation.MUSA_POINT, "Cargo reclaims", ItemID.CARGO_CRATE_MUSA_POINT_0, 1),
	PATCH_NOTE(null, PortLocation.MUSA_POINT, PortLocation.PANDEMONIUM, PortLocation.MUSA_POINT, "Patch note", ItemID.CARGO_CRATE_MUSA_POINT_1, 1),
	PRECIOUS_DELIVERY(null, PortLocation.MUSA_POINT, PortLocation.MUSA_POINT, PortLocation.PORT_SARIM, "Precious delivery", ItemID.CARGO_CRATE_MUSA_POINT_2, 2),
	PINING_FOR_PINEAPPLES(null, PortLocation.MUSA_POINT, PortLocation.MUSA_POINT, PortLocation.PANDEMONIUM, "Pining for pineapples", ItemID.CARGO_CRATE_MUSA_POINT_3, 2),
	STOP_YOUR_WINING(null, PortLocation.MUSA_POINT, PortLocation.PANDEMONIUM, PortLocation.MUSA_POINT, "Stop your wining!", ItemID.CARGO_CRATE_MUSA_POINT_4, 2),
	NO_LEG_TO_STAND_ON(null, PortLocation.MUSA_POINT, PortLocation.PORT_SARIM, PortLocation.MUSA_POINT, "No leg to stand on", ItemID.CARGO_CRATE_MUSA_POINT_5, 3),
	SMOOTH_SAILING(null, PortLocation.MUSA_POINT, PortLocation.MUSA_POINT, PortLocation.PANDEMONIUM, "Smooth sailing", ItemID.CARGO_CRATE_MUSA_POINT_6, 3),
	GENERAL_SUPPLIES(null, PortLocation.MUSA_POINT, PortLocation.CATHERBY, PortLocation.MUSA_POINT, "General supplies", ItemID.CARGO_CRATE_MUSA_POINT_7, 3),
	GOING_BANANAS(null, PortLocation.MUSA_POINT, PortLocation.MUSA_POINT, PortLocation.CATHERBY, "Going bananas", ItemID.CARGO_CRATE_MUSA_POINT_8, 1),
	CONTAIN_THE_BEER(null, PortLocation.MUSA_POINT, PortLocation.ENTRANA, PortLocation.MUSA_POINT, "Contain the beer", ItemID.CARGO_CRATE_MUSA_POINT_9, 4),
	A_MONKS_BEST_FRIEND(null, PortLocation.MUSA_POINT, PortLocation.MUSA_POINT, PortLocation.ENTRANA, "A monk's best friend", ItemID.CARGO_CRATE_MUSA_POINT_10, 4),
	SECRET_INGREDIENT(null, PortLocation.MUSA_POINT, PortLocation.BRIMHAVEN, PortLocation.MUSA_POINT, "Secret ingredient", ItemID.CARGO_CRATE_MUSA_POINT_11, 2),
	THE_LONG_CON(null, PortLocation.MUSA_POINT, PortLocation.MUSA_POINT, PortLocation.BRIMHAVEN, "The long con", ItemID.CARGO_CRATE_MUSA_POINT_12, 1),
	LETS_TEAK_BUSINESS(null, PortLocation.MUSA_POINT, PortLocation.MUSA_POINT, PortLocation.EAST_ARDOUGNE, "Let's teak business", ItemID.CARGO_CRATE_MUSA_POINT_13, 3),
	GNOME_DELIVERY(null, PortLocation.MUSA_POINT, PortLocation.MUSA_POINT, PortLocation.EAST_ARDOUGNE, "Gnome delivery", ItemID.CARGO_CRATE_MUSA_POINT_14, 5);

	private final int id;
	private final PortLocation noticeBoard;
	private final PortLocation cargoLocation;
	private final PortLocation deliveryLocation;
	private final String taskName;
	private final int cargo;
	private final int cargoAmount;

	PortTaskData(Integer id, PortLocation noticeBoard, PortLocation cargoLocation, PortLocation deliveryLocation, String taskName, int cargo, int cargoAmount)
	{
		this.id = id;
		this.noticeBoard = noticeBoard;
		this.cargoLocation = cargoLocation;
		this.deliveryLocation = deliveryLocation;
		this.taskName = taskName;
		this.cargo = cargo;
		this.cargoAmount = cargoAmount;
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

