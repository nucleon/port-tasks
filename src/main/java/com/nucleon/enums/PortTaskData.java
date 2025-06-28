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
package com.nucleon.enums;

// todo: replace with net.runelite.api.gameval.ItemID when released
import com.nucleon.gameval.ItemID;

public enum PortTaskData
{
	COURIER_WANTED(1, PortLocation.PORT_SARIM, PortLocation.PORT_SARIM, PortLocation.PANDEMONIUM, PortPaths.BRIMHAVEN_MUSA_POINT, "Courier wanted", ItemID.CARGO_CRATE_OF_STEEL_SWORDS, 1),
	LONELY_CRATE_LOOKING_FOR_LOCAL_COURIER(2, PortLocation.PORT_SARIM, PortLocation.PORT_SARIM, PortLocation.MUSA_POINT, PortPaths.BRIMHAVEN_MUSA_POINT, "Lonely crate looking for local courier", ItemID.CARGO_CRATE_OF_RAW_FISH, 1),
	BANANA_DELIVERY(3, PortLocation.PORT_SARIM, PortLocation.MUSA_POINT, PortLocation.PORT_SARIM, PortPaths.BRIMHAVEN_MUSA_POINT, "Banana delivery", ItemID.CARGO_CRATE_OF_BANANAS, 1),
	BANANA_COMPENSATION(4, PortLocation.PORT_SARIM, PortLocation.PORT_SARIM, PortLocation.MUSA_POINT, PortPaths.BRIMHAVEN_MUSA_POINT, "Banana compensation", ItemID.CARGO_CRATE_OF_ROTTEN_BANANAS, 1),
	A_BARTENDERS_PROBLEM(5, PortLocation.PORT_SARIM, PortLocation.PORT_SARIM, PortLocation.PANDEMONIUM, PortPaths.BRIMHAVEN_MUSA_POINT, "A bartender's problem", ItemID.CARGO_CRATE_OF_BEER, 3),
	LOGS_ASTRAY(6, PortLocation.PORT_SARIM, PortLocation.MUSA_POINT, PortLocation.PORT_SARIM, PortPaths.BRIMHAVEN_MUSA_POINT, "Logs astray", ItemID.CARGO_CRATE_OF_LOGS, 2),
	RUM_RUN(7, PortLocation.PORT_SARIM, PortLocation.PORT_SARIM, PortLocation.MUSA_POINT, PortPaths.BRIMHAVEN_MUSA_POINT, "Rum run", ItemID.CARGO_CRATE_OF_RUM, 1),
	LEGAL_COURIER_SERVICES_REQUESTED(8, PortLocation.PORT_SARIM, PortLocation.MUSA_POINT, PortLocation.PORT_SARIM, PortPaths.BRIMHAVEN_MUSA_POINT, "Legal' courier services requested", ItemID.CARGO_CRATE_OF_SMUGGLED_RUM, 1),
	KEG_REMOVAL_SERVICES_REQUIRED(9, PortLocation.PORT_SARIM, PortLocation.PANDEMONIUM, PortLocation.PORT_SARIM, PortPaths.BRIMHAVEN_MUSA_POINT, "Keg removal services required", ItemID.CARGO_CRATE_OF_KEGS, 2),
	BAIT_TO_CATHERBY(10, PortLocation.PORT_SARIM, PortLocation.PORT_SARIM, PortLocation.CATHERBY, PortPaths.BRIMHAVEN_MUSA_POINT, "Bait to catherby", ItemID.CARGO_CRATE_OF_BAIT, 2),
	SARAHS_POTATOES(11, PortLocation.PORT_SARIM, PortLocation.PORT_SARIM, PortLocation.CATHERBY, PortPaths.BRIMHAVEN_MUSA_POINT, "Sarah's potatoes", ItemID.CARGO_CRATE_OF_POTATOES, 3),
	A_MISLAID_PORT_ORDER(12, PortLocation.PORT_SARIM, PortLocation.CATHERBY, PortLocation.BRIMHAVEN, PortPaths.BRIMHAVEN_MUSA_POINT, "A mislaid port order", ItemID.CARGO_CRATE_OF_COMPOST, 4),
	SALAMANDER_PROTECTION(13, PortLocation.PORT_SARIM, PortLocation.PORT_SARIM, PortLocation.EAST_ARDOUGNE, PortPaths.BRIMHAVEN_MUSA_POINT, "Salamander protection", ItemID.CARGO_CRATE_OF_RED_SALAMANDERS, 2),
	TAR_EXPORT_REQUIRED(14, PortLocation.PORT_SARIM, PortLocation.PORT_SARIM, PortLocation.PORT_KHAZARD, PortPaths.BRIMHAVEN_MUSA_POINT, "Tar export required", ItemID.CARGO_CRATE_OF_SWAMP_PASTE, 3),
	SWORD_IDENTIFICATION(15, PortLocation.PORT_SARIM, PortLocation.PORT_KHAZARD, PortLocation.PORT_SARIM, PortPaths.BRIMHAVEN_MUSA_POINT, "Sword identification", ItemID.CARGO_CRATE_OF_IDENTIFIED_SWORDS, 4),
	ARMOUR_COURIER(16, PortLocation.PORT_SARIM, PortLocation.PORT_SARIM, PortLocation.PANDEMONIUM, PortPaths.BRIMHAVEN_MUSA_POINT, "Armour courier", ItemID.CARGO_CRATE_OF_MITHRIL_ARMOUR, 1),
	SPICES_NEEDED(17, PortLocation.PORT_SARIM, PortLocation.PANDEMONIUM, PortLocation.PORT_SARIM, PortPaths.BRIMHAVEN_MUSA_POINT, "Spices needed", ItemID.CARGO_CRATE_OF_SPICES, 1),
	FRESH_LOBSTER_DELIVERY(18, PortLocation.CATHERBY, PortLocation.CATHERBY, PortLocation.MUSA_POINT, PortPaths.BRIMHAVEN_MUSA_POINT, "Fresh lobster delivery", ItemID.CARGO_CRATE_OF_LIVE_LOBSTERS, 3),
	WAX_WAX_AND_MORE_WAX(19, PortLocation.CATHERBY, PortLocation.CATHERBY, PortLocation.PANDEMONIUM, PortPaths.BRIMHAVEN_MUSA_POINT, "Wax, wax and more wax", ItemID.CARGO_CRATE_OF_WAX, 2),
	CALEBS_COCONUTS(20, PortLocation.CATHERBY, PortLocation.PANDEMONIUM, PortLocation.CATHERBY, PortPaths.BRIMHAVEN_MUSA_POINT, "Caleb's coconuts", ItemID.CARGO_CRATE_OF_COCONUTS, 2),
	VIALS_TO_THE_HOLY_ISLAND(21, PortLocation.CATHERBY, PortLocation.CATHERBY, PortLocation.ENTRANA, PortPaths.BRIMHAVEN_MUSA_POINT, "Vials to the holy island", ItemID.CARGO_CRATE_OF_VIALS, 8),
	MISPLACED_SILK(22, PortLocation.CATHERBY, PortLocation.CATHERBY, PortLocation.EAST_ARDOUGNE, PortPaths.BRIMHAVEN_MUSA_POINT, "Misplaced silk", ItemID.CARGO_CRATE_OF_SILK, 5),
	PINEAPPLE_MADNESS(23, PortLocation.CATHERBY, PortLocation.BRIMHAVEN, PortLocation.CATHERBY, PortPaths.BRIMHAVEN_MUSA_POINT, "Pineapple madness", ItemID.CARGO_CRATE_OF_PINEAPPLES, 3),
	SHRIMP_FOR_THE_SHRIMP_AND_PARROT(24, PortLocation.CATHERBY, PortLocation.CATHERBY, PortLocation.BRIMHAVEN, PortPaths.BRIMHAVEN_MUSA_POINT, "Shrimp for the shrimp and parrot", ItemID.CARGO_CRATE_OF_FRESH_FISH, 3),
	CABBAGE_CRAZY(25, PortLocation.CATHERBY, PortLocation.CATHERBY, PortLocation.EAST_ARDOUGNE, PortPaths.BRIMHAVEN_MUSA_POINT, "Cabbage crazy", ItemID.CARGO_CRATE_OF_CABBAGES, 5),
	GPDT_FASTTRACK_REQUIRED(26, PortLocation.CATHERBY, PortLocation.EAST_ARDOUGNE, PortLocation.CATHERBY, PortPaths.BRIMHAVEN_MUSA_POINT, "Gpdt fast-track required", ItemID.CARGO_CRATE_OF_STEEL_ARROWTIPS, 2),
	NEED_HELP_WITH_A_KARAMBWAN_DELIVERY(27, PortLocation.CATHERBY, PortLocation.BRIMHAVEN, PortLocation.CATHERBY, PortPaths.BRIMHAVEN_MUSA_POINT, "Need help with a karambwan delivery", ItemID.CARGO_CRATE_OF_RAW_KARAMBWAN, 1),
	KHAZARD_RENNOVATIONS(28, PortLocation.CATHERBY, PortLocation.CATHERBY, PortLocation.PORT_KHAZARD, PortPaths.BRIMHAVEN_MUSA_POINT, "Khazard rennovations", ItemID.CARGO_CRATE_OF_OAK_PLANKS, 4),
	CHARTER_STOCK_REDISTRIBUTION(29, PortLocation.CATHERBY, PortLocation.PORT_KHAZARD, PortLocation.CATHERBY, PortPaths.BRIMHAVEN_MUSA_POINT, "Charter stock redistribution", ItemID.CARGO_CRATE_OF_GLASS_MAKE_SUPPLIES, 4),
	GOT_SOME_COAL_THAT_NEEDS_EXPORTING(30, PortLocation.CATHERBY, PortLocation.CATHERBY, PortLocation.PORT_KHAZARD, PortPaths.BRIMHAVEN_MUSA_POINT, "Got some coal that needs exporting", ItemID.CARGO_CRATE_OF_COAL, 5),
	SILVER_FOR_ARDOUGNE(31, PortLocation.CATHERBY, PortLocation.CATHERBY, PortLocation.EAST_ARDOUGNE, PortPaths.BRIMHAVEN_MUSA_POINT, "Silver for ardougne", ItemID.CARGO_CRATE_OF_SILVER_JEWELLERY, 6),
	URGENT_REQUEST_FOR_THE_SPIRIT_ANGLERS_OF_UNKAH(32, PortLocation.CATHERBY, PortLocation.CATHERBY, PortLocation.RUINS_OF_UNKAH, PortPaths.BRIMHAVEN_MUSA_POINT, "Urgent request for the spirit anglers of unkah!", ItemID.CARGO_CRATE_OF_BUCKETS, 8),
	MURPHYS_CATCH_OF_THE_DAY(33, PortLocation.PORT_KHAZARD, PortLocation.PORT_KHAZARD, PortLocation.EAST_ARDOUGNE, PortPaths.BRIMHAVEN_MUSA_POINT, "Murphy's catch of the day", ItemID.CARGO_CRATE_OF_TRAWLER_FISH, 6),
	GOLD_RUSH(34, PortLocation.PORT_KHAZARD, PortLocation.BRIMHAVEN, PortLocation.EAST_ARDOUGNE, PortPaths.BRIMHAVEN_MUSA_POINT, "Gold rush", ItemID.CARGO_CRATE_OF_GOLD_ORE, 5),
	MITHRIL_FOR_OUR_FLEET(35, PortLocation.PORT_KHAZARD, PortLocation.EAST_ARDOUGNE, PortLocation.PORT_KHAZARD, PortPaths.BRIMHAVEN_MUSA_POINT, "Mithril for our fleet", ItemID.CARGO_CRATE_OF_MITHRIL_ARMOUR, 5),
	BERTS_SAND_DELIVERY(36, PortLocation.PORT_KHAZARD, PortLocation.PORT_KHAZARD, PortLocation.ENTRANA, PortPaths.BRIMHAVEN_MUSA_POINT, "Bert's sand delivery", ItemID.CARGO_CRATE_OF_SAND, 6),
	DISCRETION_REQUIRED(37, PortLocation.PORT_KHAZARD, PortLocation.CATHERBY, PortLocation.PORT_KHAZARD, PortPaths.BRIMHAVEN_MUSA_POINT, "Discretion required", ItemID.CARGO_CRATE_OF_SECRET_STUFF, 3),
	DRAGON_EQUIPMENT_TO_MYTHS_GUILD(38, PortLocation.PORT_KHAZARD, PortLocation.PORT_KHAZARD, PortLocation.CORSAIR_COVE, PortPaths.BRIMHAVEN_MUSA_POINT, "Dragon equipment to myths' guild", ItemID.CARGO_CRATE_OF_DRAGON_DAGGERS, 3),
	EMERGENCY_BEER_SUPPLY_REQUEST(39, PortLocation.PORT_KHAZARD, PortLocation.MUSA_POINT, PortLocation.PORT_KHAZARD, PortPaths.BRIMHAVEN_MUSA_POINT, "Emergency beer supply request", ItemID.CARGO_CRATE_OF_BEER, 5),
	SPIRIT_ANGLER_SUPPLY_ORDER(40, PortLocation.PORT_KHAZARD, PortLocation.PORT_KHAZARD, PortLocation.RUINS_OF_UNKAH, PortPaths.BRIMHAVEN_MUSA_POINT, "Spirit angler supply order", ItemID.CARGO_CRATE_OF_SHIP_PARTS, 5),
	NEED_HELP_WITH_CARGO_RECOVERY(41, PortLocation.PORT_KHAZARD, PortLocation.RUINS_OF_UNKAH, PortLocation.PORT_KHAZARD, PortPaths.BRIMHAVEN_MUSA_POINT, "Need help with cargo recovery", ItemID.CARGO_CRATE_OF_DRAGON_EQUIPMENT, 3),
	OUTFITTING_THE_ANGLERS(42, PortLocation.PORT_KHAZARD, PortLocation.EAST_ARDOUGNE, PortLocation.RUINS_OF_UNKAH, PortPaths.BRIMHAVEN_MUSA_POINT, "Outfitting the anglers", ItemID.CARGO_CRATE_OF_SPIRIT_ANGLERS_GARB, 6),
	ANGLERS_OUTFIT_RECOVERY(43, PortLocation.PORT_KHAZARD, PortLocation.RUINS_OF_UNKAH, PortLocation.PORT_KHAZARD, PortPaths.BRIMHAVEN_MUSA_POINT, "Angler's outfit recovery", ItemID.CARGO_CRATE_OF_ANGLERS_OUTFIT, 5),
	BANANA_COLLECTION(44, PortLocation.RUINS_OF_UNKAH, PortLocation.MUSA_POINT, PortLocation.RUINS_OF_UNKAH, PortPaths.BRIMHAVEN_MUSA_POINT, "Banana collection", ItemID.CARGO_CRATE_OF_BANANAS, 5),
	EQUIPMENT_REQUISITION(45, PortLocation.RUINS_OF_UNKAH, PortLocation.EAST_ARDOUGNE, PortLocation.RUINS_OF_UNKAH, PortPaths.BRIMHAVEN_MUSA_POINT, "Equipment requisition", ItemID.CARGO_CRATE_OF_FISHING_EQUIPMENT, 2),
	GOT_SOME_GRANITE_THAT_NEEDS_DELIVERING(46, PortLocation.RUINS_OF_UNKAH, PortLocation.RUINS_OF_UNKAH, PortLocation.BRIMHAVEN, PortPaths.BRIMHAVEN_MUSA_POINT, "Got some granite that needs delivering", ItemID.CARGO_CRATE_OF_GRANITE, 4),
	SILK_REQUEST(47, PortLocation.RUINS_OF_UNKAH, PortLocation.RUINS_OF_UNKAH, PortLocation.CATHERBY, PortPaths.BRIMHAVEN_MUSA_POINT, "Silk request", ItemID.CARGO_CRATE_OF_SILK, 6),
	IMPORTANT_CARGO(48, PortLocation.RUINS_OF_UNKAH, PortLocation.RUINS_OF_UNKAH, PortLocation.PORT_KHAZARD, PortPaths.BRIMHAVEN_MUSA_POINT, "Important cargo", ItemID.CARGO_CRATE_OF_IMPORTANCE, 1),
	IN_NEED_OF_A_SILK_RESUPPLY(49, PortLocation.RUINS_OF_UNKAH, PortLocation.RUINS_OF_UNKAH, PortLocation.EAST_ARDOUGNE, PortPaths.BRIMHAVEN_MUSA_POINT, "In need of a silk resupply", ItemID.CARGO_CRATE_OF_SILK, 8),
	COCKTAIL_EXPRESS(50, PortLocation.RUINS_OF_UNKAH, PortLocation.PANDEMONIUM, PortLocation.RUINS_OF_UNKAH, PortPaths.BRIMHAVEN_MUSA_POINT, "Cocktail express", ItemID.CARGO_CRATE_OF_COCKTAILS, 2),
	CACTUS_EXPERIMENTS(51, PortLocation.RUINS_OF_UNKAH, PortLocation.RUINS_OF_UNKAH, PortLocation.PORT_SARIM, PortPaths.BRIMHAVEN_MUSA_POINT, "Cactus experiments", ItemID.CARGO_CRATE_OF_CACTUS, 2),
	FOOD_STORAGE_RESUPPLY(52, PortLocation.RUINS_OF_UNKAH, PortLocation.RUINS_OF_UNKAH, PortLocation.CORSAIR_COVE, PortPaths.BRIMHAVEN_MUSA_POINT, "Food storage resupply", ItemID.CARGO_CRATE_OF_FRESH_FISH, 4),
	CORAL_EXPERIMENTS(53, PortLocation.RUINS_OF_UNKAH, PortLocation.SUMMER_SHORE, PortLocation.RUINS_OF_UNKAH, PortPaths.BRIMHAVEN_MUSA_POINT, "Coral experiments", ItemID.CARGO_CRATE_OF_CORAL, 4),
	CONSTRUCTION_SUPPLIES(54, PortLocation.RUINS_OF_UNKAH, PortLocation.RUINS_OF_UNKAH, PortLocation.SUMMER_SHORE, PortPaths.BRIMHAVEN_MUSA_POINT, "Construction supplies", ItemID.CARGO_CRATE_OF_SANDSTONE, 7),
	FISH_DELIVERY(81, PortLocation.PANDEMONIUM, PortLocation.PANDEMONIUM, PortLocation.PORT_SARIM, PortPaths.BRIMHAVEN_MUSA_POINT, "Fish delivery", ItemID.CARGO_CRATE_OF_RAW_FISH, 1),
	FISH_FOR_THE_FACE(82, PortLocation.PANDEMONIUM, PortLocation.PANDEMONIUM, PortLocation.PORT_SARIM, PortPaths.BRIMHAVEN_MUSA_POINT, "Fish for 'the face'", ItemID.CARGO_CRATE_OF_FRESH_FISH, 1),
	BILLYNOSHIPPARTS(83, PortLocation.PANDEMONIUM, PortLocation.PANDEMONIUM, PortLocation.MUSA_POINT, PortPaths.BRIMHAVEN_MUSA_POINT, "Billy-no-ship-parts", ItemID.CARGO_CRATE_OF_SHIP_PARTS, 1),
	SECRET_PIRATE_BUISNESS(84, PortLocation.PANDEMONIUM, PortLocation.PANDEMONIUM, PortLocation.MUSA_POINT, PortPaths.BRIMHAVEN_MUSA_POINT, "Secret pirate buisness", ItemID.CARGO_CRATE_OF_SECRET_STUFF, 1),
	WE_NEED_MORE_RUM(85, PortLocation.PANDEMONIUM, PortLocation.MUSA_POINT, PortLocation.PANDEMONIUM, PortPaths.BRIMHAVEN_MUSA_POINT, "We need more rum!", ItemID.CARGO_CRATE_OF_RUM, 1),
	STEEL_COLLECTION(86, PortLocation.PANDEMONIUM, PortLocation.PORT_SARIM, PortLocation.PANDEMONIUM, PortPaths.BRIMHAVEN_MUSA_POINT, "Steel collection", ItemID.CARGO_CRATE_OF_STEEL_BARS, 1),
	JEWELLERY_DELIVERY(87, PortLocation.PANDEMONIUM, PortLocation.PANDEMONIUM, PortLocation.PORT_SARIM, PortPaths.BRIMHAVEN_MUSA_POINT, "Jewellery delivery", ItemID.CARGO_CRATE_OF_SILVER_JEWELLERY, 2),
	RETURN_TO_SENDER(88, PortLocation.PANDEMONIUM, PortLocation.PANDEMONIUM, PortLocation.MUSA_POINT, PortPaths.BRIMHAVEN_MUSA_POINT, "Return to sender", ItemID.CARGO_CRATE_OF_ROTTEN_BANANAS, 3),
	PIRATE_GROG_DELIVERY(89, PortLocation.PANDEMONIUM, PortLocation.PANDEMONIUM, PortLocation.CATHERBY, PortPaths.BRIMHAVEN_MUSA_POINT, "Pirate grog delivery", ItemID.CARGO_CRATE_OF_GROG, 2),
	IN_NEED_OF_ARROWTIPS(90, PortLocation.PANDEMONIUM, PortLocation.CATHERBY, PortLocation.PANDEMONIUM, PortPaths.BRIMHAVEN_MUSA_POINT, "In need of arrowtips", ItemID.CARGO_CRATE_OF_STEEL_ARROWTIPS, 2),
	PINEAPPLE_COLLECTION(91, PortLocation.PANDEMONIUM, PortLocation.BRIMHAVEN, PortLocation.PANDEMONIUM, PortPaths.BRIMHAVEN_MUSA_POINT, "Pineapple collection", ItemID.CARGO_CRATE_OF_BRIMHAVEN_PINEAPPLES, 3),
	SENDING_SAND(92, PortLocation.PANDEMONIUM, PortLocation.PANDEMONIUM, PortLocation.EAST_ARDOUGNE, PortPaths.BRIMHAVEN_MUSA_POINT, "Sending sand", ItemID.CARGO_CRATE_OF_SAND, 4),
	COCKTAILS_FOR_ALL(93, PortLocation.PANDEMONIUM, PortLocation.PANDEMONIUM, PortLocation.RUINS_OF_UNKAH, PortPaths.BRIMHAVEN_MUSA_POINT, "Cocktails for all", ItemID.CARGO_CRATE_OF_COCKTAILS, 2),
	URGENT_DELIVERY(94, PortLocation.PANDEMONIUM, PortLocation.PANDEMONIUM, PortLocation.PORT_KHAZARD, PortPaths.BRIMHAVEN_MUSA_POINT, "Urgent delivery", ItemID.CARGO_CRATE_OF_KYATT_TEETH, 3),
	TAR_NEEDED(95, PortLocation.PANDEMONIUM, PortLocation.PORT_KHAZARD, PortLocation.PANDEMONIUM, PortPaths.BRIMHAVEN_MUSA_POINT, "Tar needed", ItemID.CARGO_CRATE_OF_SWAMP_PASTE, 3),
	/*
	removed due to not having a noticeboard location in the sailing beta
	SECRET_PIRATE_LOOTY(55, null, PortLocation.PORT_SARIM, PortLocation.PANDEMONIUM, "Secret pirate looty", ItemID.CARGO_CRATE_OF_PIRATE_LOOTY, 1),
	 */
	CARGO_RECLAIMS(102, PortLocation.MUSA_POINT, PortLocation.PORT_SARIM, PortLocation.MUSA_POINT, PortPaths.BRIMHAVEN_MUSA_POINT, "Cargo reclaims", ItemID.CARGO_CRATE_MUSA_POINT_0, 1),
	PATCH_NOTE(103, PortLocation.MUSA_POINT, PortLocation.PANDEMONIUM, PortLocation.MUSA_POINT, PortPaths.BRIMHAVEN_MUSA_POINT, "Patch note", ItemID.CARGO_CRATE_MUSA_POINT_1, 1),
	PRECIOUS_DELIVERY(104, PortLocation.MUSA_POINT, PortLocation.MUSA_POINT, PortLocation.PORT_SARIM, PortPaths.BRIMHAVEN_MUSA_POINT, "Precious delivery", ItemID.CARGO_CRATE_MUSA_POINT_2, 2),
	PINING_FOR_PINEAPPLES(105, PortLocation.MUSA_POINT, PortLocation.MUSA_POINT, PortLocation.PANDEMONIUM, PortPaths.BRIMHAVEN_MUSA_POINT, "Pining for pineapples", ItemID.CARGO_CRATE_MUSA_POINT_3, 2),
	STOP_YOUR_WINING(106, PortLocation.MUSA_POINT, PortLocation.PANDEMONIUM, PortLocation.MUSA_POINT, PortPaths.BRIMHAVEN_MUSA_POINT, "Stop your wining!", ItemID.CARGO_CRATE_MUSA_POINT_4, 2),
	NO_LEG_TO_STAND_ON(107, PortLocation.MUSA_POINT, PortLocation.PORT_SARIM, PortLocation.MUSA_POINT, PortPaths.BRIMHAVEN_MUSA_POINT, "No leg to stand on", ItemID.CARGO_CRATE_MUSA_POINT_5, 3),
	SMOOTH_SAILING(108, PortLocation.MUSA_POINT, PortLocation.MUSA_POINT, PortLocation.PANDEMONIUM, PortPaths.BRIMHAVEN_MUSA_POINT, "Smooth sailing", ItemID.CARGO_CRATE_MUSA_POINT_6, 3),
	GENERAL_SUPPLIES(109, PortLocation.MUSA_POINT, PortLocation.CATHERBY, PortLocation.MUSA_POINT, PortPaths.BRIMHAVEN_MUSA_POINT, "General supplies", ItemID.CARGO_CRATE_MUSA_POINT_7, 3),
	GOING_BANANAS(110, PortLocation.MUSA_POINT, PortLocation.MUSA_POINT, PortLocation.CATHERBY, PortPaths.BRIMHAVEN_MUSA_POINT, "Going bananas", ItemID.CARGO_CRATE_MUSA_POINT_8, 1),
	CONTAIN_THE_BEER(111, PortLocation.MUSA_POINT, PortLocation.ENTRANA, PortLocation.MUSA_POINT, PortPaths.BRIMHAVEN_MUSA_POINT, "Contain the beer", ItemID.CARGO_CRATE_MUSA_POINT_9, 4),
	A_MONKS_BEST_FRIEND(112, PortLocation.MUSA_POINT, PortLocation.MUSA_POINT, PortLocation.ENTRANA, PortPaths.BRIMHAVEN_MUSA_POINT, "A monk's best friend", ItemID.CARGO_CRATE_MUSA_POINT_10, 4),
	SECRET_INGREDIENT(113, PortLocation.MUSA_POINT, PortLocation.BRIMHAVEN, PortLocation.MUSA_POINT, PortPaths.BRIMHAVEN_MUSA_POINT, "Secret ingredient", ItemID.CARGO_CRATE_MUSA_POINT_11, 2),
	THE_LONG_CON(114, PortLocation.MUSA_POINT, PortLocation.MUSA_POINT, PortLocation.BRIMHAVEN, PortPaths.BRIMHAVEN_MUSA_POINT, "The long con", ItemID.CARGO_CRATE_MUSA_POINT_12, 1),
	LETS_TEAK_BUSINESS(115, PortLocation.MUSA_POINT, PortLocation.MUSA_POINT, PortLocation.EAST_ARDOUGNE, PortPaths.BRIMHAVEN_MUSA_POINT, "Let's teak business", ItemID.CARGO_CRATE_MUSA_POINT_13, 3),
	GNOME_DELIVERY(116, PortLocation.MUSA_POINT, PortLocation.MUSA_POINT, PortLocation.EAST_ARDOUGNE, PortPaths.BRIMHAVEN_MUSA_POINT, "Gnome delivery", ItemID.CARGO_CRATE_MUSA_POINT_14, 5);

	private final int id;
	private final PortLocation noticeBoard;
	private final PortLocation cargoLocation;
	private final PortLocation deliveryLocation;
	public final PortPaths dockMarkers;
	public final String taskName;
	private final int cargo;
	public final int cargoAmount;

	PortTaskData(Integer id, PortLocation noticeBoard, PortLocation cargoLocation, PortLocation deliveryLocation, PortPaths dockMarkers, String taskName, int cargo, int cargoAmount)
	{
		this.id = id;
		this.noticeBoard = noticeBoard;
		this.cargoLocation = cargoLocation;
		this.deliveryLocation = deliveryLocation;
		this.dockMarkers = dockMarkers;
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

	public PortLocation getNoticeBoard()
	{
		return noticeBoard;
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

	public int getCargoAmount()
	{
		return this.cargoAmount;
	}


}

