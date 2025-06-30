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
package com.nucleon.porttasks.enums;

// todo: replace with net.runelite.api.gameval.ItemID when released
import com.nucleon.porttasks.gameval.ItemID;

public enum PortTaskData
{
	COURIER_WANTED(1, PortLocation.PORT_SARIM, PortLocation.PORT_SARIM, PortLocation.PANDEMONIUM, PortPaths.PORT_SARIM_PANDEMONIUM, false, "Courier wanted", ItemID.CARGO_CRATE_OF_STEEL_SWORDS.getId(), 1),
	LONELY_CRATE_LOOKING_FOR_LOCAL_COURIER(2, PortLocation.PORT_SARIM, PortLocation.PORT_SARIM, PortLocation.MUSA_POINT, PortPaths.MUSA_POINT_PORT_SARIM, true, "Lonely crate looking for local courier", ItemID.CARGO_CRATE_OF_RAW_FISH.getId(), 1),
	BANANA_DELIVERY(3, PortLocation.PORT_SARIM, PortLocation.MUSA_POINT, PortLocation.PORT_SARIM, PortPaths.MUSA_POINT_PORT_SARIM, false, "Banana delivery", ItemID.CARGO_CRATE_OF_BANANAS.getId(), 1),
	BANANA_COMPENSATION(4, PortLocation.PORT_SARIM, PortLocation.PORT_SARIM, PortLocation.MUSA_POINT, PortPaths.MUSA_POINT_PORT_SARIM, true, "Banana compensation", ItemID.CARGO_CRATE_OF_ROTTEN_BANANAS.getId(), 1),
	A_BARTENDERS_PROBLEM(5, PortLocation.PORT_SARIM, PortLocation.PORT_SARIM, PortLocation.PANDEMONIUM, PortPaths.PORT_SARIM_PANDEMONIUM, false, "A bartender's problem", ItemID.CARGO_CRATE_OF_BEER.getId(), 3),
	LOGS_ASTRAY(6, PortLocation.PORT_SARIM, PortLocation.MUSA_POINT, PortLocation.PORT_SARIM, PortPaths.MUSA_POINT_PORT_SARIM, false, "Logs astray", ItemID.CARGO_CRATE_OF_LOGS.getId(), 2),
	RUM_RUN(7, PortLocation.PORT_SARIM, PortLocation.PORT_SARIM, PortLocation.MUSA_POINT, PortPaths.MUSA_POINT_PORT_SARIM, true, "Rum run", ItemID.CARGO_CRATE_OF_RUM.getId(), 1),
	LEGAL_COURIER_SERVICES_REQUESTED(8, PortLocation.PORT_SARIM, PortLocation.MUSA_POINT, PortLocation.PORT_SARIM, PortPaths.MUSA_POINT_PORT_SARIM, false, "Legal' courier services requested", ItemID.CARGO_CRATE_OF_SMUGGLED_RUM.getId(), 1),
	KEG_REMOVAL_SERVICES_REQUIRED(9, PortLocation.PORT_SARIM, PortLocation.PANDEMONIUM, PortLocation.PORT_SARIM, PortPaths.PORT_SARIM_PANDEMONIUM, true, "Keg removal services required", ItemID.CARGO_CRATE_OF_KEGS.getId(), 2),
	BAIT_TO_CATHERBY(10, PortLocation.PORT_SARIM, PortLocation.PORT_SARIM, PortLocation.CATHERBY, PortPaths.CATHERBY_PORT_SARIM, true, "Bait to catherby", ItemID.CARGO_CRATE_OF_BAIT.getId(), 2),
	SARAHS_POTATOES(11, PortLocation.PORT_SARIM, PortLocation.PORT_SARIM, PortLocation.CATHERBY, PortPaths.CATHERBY_PORT_SARIM, true, "Sarah's potatoes", ItemID.CARGO_CRATE_OF_POTATOES.getId(), 3),
	A_MISLAID_PORT_ORDER(12, PortLocation.PORT_SARIM, PortLocation.CATHERBY, PortLocation.BRIMHAVEN, PortPaths.CATHERBY_BRIMHAVEN, false, "A mislaid port order", ItemID.CARGO_CRATE_OF_COMPOST.getId(), 4),
	SALAMANDER_PROTECTION(13, PortLocation.PORT_SARIM, PortLocation.PORT_SARIM, PortLocation.ARDOUGNE, PortPaths.PORT_SARIM_ARDOUGNE, false, "Salamander protection", ItemID.CARGO_CRATE_OF_RED_SALAMANDERS.getId(), 2),
	TAR_EXPORT_REQUIRED(14, PortLocation.PORT_SARIM, PortLocation.PORT_SARIM, PortLocation.PORT_KHAZARD, PortPaths.PORT_KHAZARD_PORT_SARIM, true, "Tar export required", ItemID.CARGO_CRATE_OF_SWAMP_PASTE.getId(), 3),
	SWORD_IDENTIFICATION(15, PortLocation.PORT_SARIM, PortLocation.PORT_KHAZARD, PortLocation.PORT_SARIM, PortPaths.PORT_KHAZARD_PORT_SARIM, false, "Sword identification", ItemID.CARGO_CRATE_OF_IDENTIFIED_SWORDS.getId(), 4),
	ARMOUR_COURIER(16, PortLocation.PORT_SARIM, PortLocation.PORT_SARIM, PortLocation.PANDEMONIUM, PortPaths.PORT_SARIM_PANDEMONIUM, false, "Armour courier", ItemID.CARGO_CRATE_OF_MITHRIL_ARMOUR.getId(), 1),
	SPICES_NEEDED(17, PortLocation.PORT_SARIM, PortLocation.PANDEMONIUM, PortLocation.PORT_SARIM, PortPaths.PORT_SARIM_PANDEMONIUM, true, "Spices needed", ItemID.CARGO_CRATE_OF_SPICES.getId(), 1),
	FRESH_LOBSTER_DELIVERY(18, PortLocation.CATHERBY, PortLocation.CATHERBY, PortLocation.MUSA_POINT, PortPaths.CATHERBY_MUSA_POINT, false, "Fresh lobster delivery", ItemID.CARGO_CRATE_OF_LIVE_LOBSTERS.getId(), 3),
	WAX_WAX_AND_MORE_WAX(19, PortLocation.CATHERBY, PortLocation.CATHERBY, PortLocation.PANDEMONIUM, PortPaths.CATHERBY_PANDEMONIUM, false, "Wax, wax and more wax", ItemID.CARGO_CRATE_OF_WAX.getId(), 2),
	CALEBS_COCONUTS(20, PortLocation.CATHERBY, PortLocation.PANDEMONIUM, PortLocation.CATHERBY, PortPaths.CATHERBY_PANDEMONIUM, true, "Caleb's coconuts", ItemID.CARGO_CRATE_OF_COCONUTS.getId(), 2),
	VIALS_TO_THE_HOLY_ISLAND(21, PortLocation.CATHERBY, PortLocation.CATHERBY, PortLocation.ENTRANA, PortPaths.CATHERBY_ENTRANA, false, "Vials to the holy island", ItemID.CARGO_CRATE_OF_VIALS.getId(), 8),
	MISPLACED_SILK(22, PortLocation.CATHERBY, PortLocation.CATHERBY, PortLocation.ARDOUGNE, PortPaths.CATHERBY_ARDOUGNE, false, "Misplaced silk", ItemID.CARGO_CRATE_OF_SILK.getId(), 5),
	PINEAPPLE_MADNESS(23, PortLocation.CATHERBY, PortLocation.BRIMHAVEN, PortLocation.CATHERBY, PortPaths.CATHERBY_BRIMHAVEN, true, "Pineapple madness", ItemID.CARGO_CRATE_OF_PINEAPPLES.getId(), 3),
	SHRIMP_FOR_THE_SHRIMP_AND_PARROT(24, PortLocation.CATHERBY, PortLocation.CATHERBY, PortLocation.BRIMHAVEN, PortPaths.CATHERBY_BRIMHAVEN, false, "Shrimp for the shrimp and parrot", ItemID.CARGO_CRATE_OF_FRESH_FISH.getId(), 3),
	CABBAGE_CRAZY(25, PortLocation.CATHERBY, PortLocation.CATHERBY, PortLocation.ARDOUGNE, PortPaths.CATHERBY_ARDOUGNE, false, "Cabbage crazy", ItemID.CARGO_CRATE_OF_CABBAGES.getId(), 5),
	GPDT_FASTTRACK_REQUIRED(26, PortLocation.CATHERBY, PortLocation.ARDOUGNE, PortLocation.CATHERBY, PortPaths.CATHERBY_ARDOUGNE, true, "Gpdt fast-track required", ItemID.CARGO_CRATE_OF_STEEL_ARROWTIPS.getId(), 2),
	NEED_HELP_WITH_A_KARAMBWAN_DELIVERY(27, PortLocation.CATHERBY, PortLocation.BRIMHAVEN, PortLocation.CATHERBY, PortPaths.CATHERBY_BRIMHAVEN, true, "Need help with a karambwan delivery", ItemID.CARGO_CRATE_OF_RAW_KARAMBWAN.getId(), 1),
	KHAZARD_RENNOVATIONS(28, PortLocation.CATHERBY, PortLocation.CATHERBY, PortLocation.PORT_KHAZARD, PortPaths.CATHERBY_PORT_KHAZARD, false, "Khazard rennovations", ItemID.CARGO_CRATE_OF_OAK_PLANKS.getId(), 4),
	CHARTER_STOCK_REDISTRIBUTION(29, PortLocation.CATHERBY, PortLocation.PORT_KHAZARD, PortLocation.CATHERBY, PortPaths.CATHERBY_PORT_KHAZARD, true, "Charter stock redistribution", ItemID.CARGO_CRATE_OF_GLASS_MAKE_SUPPLIES.getId(), 4),
	GOT_SOME_COAL_THAT_NEEDS_EXPORTING(30, PortLocation.CATHERBY, PortLocation.CATHERBY, PortLocation.PORT_KHAZARD, PortPaths.CATHERBY_PORT_KHAZARD, false, "Got some coal that needs exporting", ItemID.CARGO_CRATE_OF_COAL.getId(), 5),
	SILVER_FOR_ARDOUGNE(31, PortLocation.CATHERBY, PortLocation.CATHERBY, PortLocation.ARDOUGNE, PortPaths.CATHERBY_ARDOUGNE, false, "Silver for ardougne", ItemID.CARGO_CRATE_OF_SILVER_JEWELLERY.getId(), 6),
	URGENT_REQUEST_FOR_THE_SPIRIT_ANGLERS_OF_UNKAH(32, PortLocation.CATHERBY, PortLocation.CATHERBY, PortLocation.RUINS_OF_UNKAH, PortPaths.CATHERBY_RUINS_OF_UNKAH, false, "Urgent request for the spirit anglers of unkah!", ItemID.CARGO_CRATE_OF_BUCKETS.getId(), 8),
	MURPHYS_CATCH_OF_THE_DAY(33, PortLocation.PORT_KHAZARD, PortLocation.PORT_KHAZARD, PortLocation.ARDOUGNE, PortPaths.ARDOUGNE_PORT_KHAZARD, true, "Murphy's catch of the day", ItemID.CARGO_CRATE_OF_TRAWLER_FISH.getId(), 6),
	GOLD_RUSH(34, PortLocation.PORT_KHAZARD, PortLocation.BRIMHAVEN, PortLocation.ARDOUGNE, PortPaths.BRIMHAVEN_ARDOUGNE, false, "Gold rush", ItemID.CARGO_CRATE_OF_GOLD_ORE.getId(), 5),
	MITHRIL_FOR_OUR_FLEET(35, PortLocation.PORT_KHAZARD, PortLocation.ARDOUGNE, PortLocation.PORT_KHAZARD, PortPaths.ARDOUGNE_PORT_KHAZARD, false, "Mithril for our fleet", ItemID.CARGO_CRATE_OF_MITHRIL_ARMOUR.getId(), 5),
	BERTS_SAND_DELIVERY(36, PortLocation.PORT_KHAZARD, PortLocation.PORT_KHAZARD, PortLocation.ENTRANA, PortPaths.PORT_KHAZARD_ENTRANA, false, "Bert's sand delivery", ItemID.CARGO_CRATE_OF_SAND.getId(), 6),
	DISCRETION_REQUIRED(37, PortLocation.PORT_KHAZARD, PortLocation.CATHERBY, PortLocation.PORT_KHAZARD, PortPaths.CATHERBY_PORT_KHAZARD, false, "Discretion required", ItemID.CARGO_CRATE_OF_SECRET_STUFF.getId(), 3),
	DRAGON_EQUIPMENT_TO_MYTHS_GUILD(38, PortLocation.PORT_KHAZARD, PortLocation.PORT_KHAZARD, PortLocation.CORSAIR_COVE, PortPaths.PORT_KHAZARD_CORSAIR_COVE, false, "Dragon equipment to myths' guild", ItemID.CARGO_CRATE_OF_DRAGON_DAGGERS.getId(), 3),
	EMERGENCY_BEER_SUPPLY_REQUEST(39, PortLocation.PORT_KHAZARD, PortLocation.MUSA_POINT, PortLocation.PORT_KHAZARD, PortPaths.MUSA_POINT_PORT_KHAZARD, false, "Emergency beer supply request", ItemID.CARGO_CRATE_OF_BEER.getId(), 5),
	SPIRIT_ANGLER_SUPPLY_ORDER(40, PortLocation.PORT_KHAZARD, PortLocation.PORT_KHAZARD, PortLocation.RUINS_OF_UNKAH, PortPaths.PORT_KHAZARD_RUINS_OF_UNKAH, false, "Spirit angler supply order", ItemID.CARGO_CRATE_OF_SHIP_PARTS.getId(), 5),
	NEED_HELP_WITH_CARGO_RECOVERY(41, PortLocation.PORT_KHAZARD, PortLocation.RUINS_OF_UNKAH, PortLocation.PORT_KHAZARD, PortPaths.PORT_KHAZARD_RUINS_OF_UNKAH, true, "Need help with cargo recovery", ItemID.CARGO_CRATE_OF_DRAGON_EQUIPMENT.getId(), 3),
	OUTFITTING_THE_ANGLERS(42, PortLocation.PORT_KHAZARD, PortLocation.ARDOUGNE, PortLocation.RUINS_OF_UNKAH, PortPaths.ARDOUGNE_RUINS_OF_UNKAH, false, "Outfitting the anglers", ItemID.CARGO_CRATE_OF_SPIRIT_ANGLERS_GARB.getId(), 6),
	ANGLERS_OUTFIT_RECOVERY(43, PortLocation.PORT_KHAZARD, PortLocation.RUINS_OF_UNKAH, PortLocation.PORT_KHAZARD, PortPaths.PORT_KHAZARD_RUINS_OF_UNKAH, true, "Angler's outfit recovery", ItemID.CARGO_CRATE_OF_ANGLERS_OUTFIT.getId(), 5),
	BANANA_COLLECTION(44, PortLocation.RUINS_OF_UNKAH, PortLocation.MUSA_POINT, PortLocation.RUINS_OF_UNKAH, PortPaths.MUSA_POINT_RUINS_OF_UNKAH, false, "Banana collection", ItemID.CARGO_CRATE_OF_BANANAS.getId(), 5),
	EQUIPMENT_REQUISITION(45, PortLocation.RUINS_OF_UNKAH, PortLocation.ARDOUGNE, PortLocation.RUINS_OF_UNKAH, PortPaths.ARDOUGNE_RUINS_OF_UNKAH, false, "Equipment requisition", ItemID.CARGO_CRATE_OF_FISHING_EQUIPMENT.getId(), 2),
	GOT_SOME_GRANITE_THAT_NEEDS_DELIVERING(46, PortLocation.RUINS_OF_UNKAH, PortLocation.RUINS_OF_UNKAH, PortLocation.BRIMHAVEN, PortPaths.RUINS_OF_UNKAH_BRIMHAVEN, false, "Got some granite that needs delivering", ItemID.CARGO_CRATE_OF_GRANITE.getId(), 4),
	SILK_REQUEST(47, PortLocation.RUINS_OF_UNKAH, PortLocation.RUINS_OF_UNKAH, PortLocation.CATHERBY, PortPaths.CATHERBY_RUINS_OF_UNKAH, true, "Silk request", ItemID.CARGO_CRATE_OF_SILK.getId(), 6),
	IMPORTANT_CARGO(48, PortLocation.RUINS_OF_UNKAH, PortLocation.RUINS_OF_UNKAH, PortLocation.PORT_KHAZARD, PortPaths.PORT_KHAZARD_RUINS_OF_UNKAH, true, "Important cargo", ItemID.CARGO_CRATE_OF_IMPORTANCE.getId(), 1),
	IN_NEED_OF_A_SILK_RESUPPLY(49, PortLocation.RUINS_OF_UNKAH, PortLocation.RUINS_OF_UNKAH, PortLocation.ARDOUGNE, PortPaths.ARDOUGNE_RUINS_OF_UNKAH, true, "In need of a silk resupply", ItemID.CARGO_CRATE_OF_SILK.getId(), 8),
	COCKTAIL_EXPRESS(50, PortLocation.RUINS_OF_UNKAH, PortLocation.PANDEMONIUM, PortLocation.RUINS_OF_UNKAH, PortPaths.PANDEMONIUM_RUINS_OF_UNKAH, false, "Cocktail express", ItemID.CARGO_CRATE_OF_COCKTAILS.getId(), 2),
	CACTUS_EXPERIMENTS(51, PortLocation.RUINS_OF_UNKAH, PortLocation.RUINS_OF_UNKAH, PortLocation.PORT_SARIM, PortPaths.RUINS_OF_UNKAH_PORT_SARIM, false, "Cactus experiments", ItemID.CARGO_CRATE_OF_CACTUS.getId(), 2),
	FOOD_STORAGE_RESUPPLY(52, PortLocation.RUINS_OF_UNKAH, PortLocation.RUINS_OF_UNKAH, PortLocation.CORSAIR_COVE, PortPaths.RUINS_OF_UNKAH_CORSAIR_COVE, false, "Food storage resupply", ItemID.CARGO_CRATE_OF_FRESH_FISH.getId(), 4),
	CORAL_EXPERIMENTS(53, PortLocation.RUINS_OF_UNKAH, PortLocation.SUMMER_SHORE, PortLocation.RUINS_OF_UNKAH, PortPaths.RUINS_OF_UNKAH_SUMMER_SHORE, true, "Coral experiments", ItemID.CARGO_CRATE_OF_CORAL.getId(), 4),
	CONSTRUCTION_SUPPLIES(54, PortLocation.RUINS_OF_UNKAH, PortLocation.RUINS_OF_UNKAH, PortLocation.SUMMER_SHORE, PortPaths.RUINS_OF_UNKAH_SUMMER_SHORE, false, "Construction supplies", ItemID.CARGO_CRATE_OF_SANDSTONE.getId(), 7),
	FISH_DELIVERY(81, PortLocation.PANDEMONIUM, PortLocation.PANDEMONIUM, PortLocation.PORT_SARIM, PortPaths.PORT_SARIM_PANDEMONIUM, true, "Fish delivery", ItemID.CARGO_CRATE_OF_RAW_FISH.getId(), 1),
	FISH_FOR_THE_FACE(82, PortLocation.PANDEMONIUM, PortLocation.PANDEMONIUM, PortLocation.PORT_SARIM, PortPaths.PORT_SARIM_PANDEMONIUM, true, "Fish for 'the face'", ItemID.CARGO_CRATE_OF_FRESH_FISH.getId(), 1),
	BILLYNOSHIPPARTS(83, PortLocation.PANDEMONIUM, PortLocation.PANDEMONIUM, PortLocation.MUSA_POINT, PortPaths.MUSA_POINT_PANDEMONIUM, true, "Billy-no-ship-parts", ItemID.CARGO_CRATE_OF_SHIP_PARTS.getId(), 1),
	SECRET_PIRATE_BUISNESS(84, PortLocation.PANDEMONIUM, PortLocation.PANDEMONIUM, PortLocation.MUSA_POINT, PortPaths.MUSA_POINT_PANDEMONIUM, true, "Secret pirate buisness", ItemID.CARGO_CRATE_OF_SECRET_STUFF.getId(), 1),
	WE_NEED_MORE_RUM(85, PortLocation.PANDEMONIUM, PortLocation.MUSA_POINT, PortLocation.PANDEMONIUM, PortPaths.MUSA_POINT_PANDEMONIUM, false, "We need more rum!", ItemID.CARGO_CRATE_OF_RUM.getId(), 1),
	STEEL_COLLECTION(86, PortLocation.PANDEMONIUM, PortLocation.PORT_SARIM, PortLocation.PANDEMONIUM, PortPaths.PORT_SARIM_PANDEMONIUM, false, "Steel collection", ItemID.CARGO_CRATE_OF_STEEL_BARS.getId(), 1),
	JEWELLERY_DELIVERY(87, PortLocation.PANDEMONIUM, PortLocation.PANDEMONIUM, PortLocation.PORT_SARIM, PortPaths.PORT_SARIM_PANDEMONIUM, true, "Jewellery delivery", ItemID.CARGO_CRATE_OF_SILVER_JEWELLERY.getId(), 2),
	RETURN_TO_SENDER(88, PortLocation.PANDEMONIUM, PortLocation.PANDEMONIUM, PortLocation.MUSA_POINT, PortPaths.MUSA_POINT_PANDEMONIUM, true, "Return to sender", ItemID.CARGO_CRATE_OF_ROTTEN_BANANAS.getId(), 3),
	PIRATE_GROG_DELIVERY(89, PortLocation.PANDEMONIUM, PortLocation.PANDEMONIUM, PortLocation.CATHERBY, PortPaths.CATHERBY_PANDEMONIUM, true, "Pirate grog delivery", ItemID.CARGO_CRATE_OF_GROG.getId(), 2),
	IN_NEED_OF_ARROWTIPS(90, PortLocation.PANDEMONIUM, PortLocation.CATHERBY, PortLocation.PANDEMONIUM, PortPaths.CATHERBY_PANDEMONIUM, false, "In need of arrowtips", ItemID.CARGO_CRATE_OF_STEEL_ARROWTIPS.getId(), 2),
	PINEAPPLE_COLLECTION(91, PortLocation.PANDEMONIUM, PortLocation.BRIMHAVEN, PortLocation.PANDEMONIUM, PortPaths.BRIMHAVEN_PANDEMONIUM, false, "Pineapple collection", ItemID.CARGO_CRATE_OF_BRIMHAVEN_PINEAPPLES.getId(), 3),
	SENDING_SAND(92, PortLocation.PANDEMONIUM, PortLocation.PANDEMONIUM, PortLocation.ARDOUGNE, PortPaths.PANDEMONIUM_ARDOUGNE, false, "Sending sand", ItemID.CARGO_CRATE_OF_SAND.getId(), 4),
	COCKTAILS_FOR_ALL(93, PortLocation.PANDEMONIUM, PortLocation.PANDEMONIUM, PortLocation.RUINS_OF_UNKAH, PortPaths.PANDEMONIUM_RUINS_OF_UNKAH, false, "Cocktails for all", ItemID.CARGO_CRATE_OF_COCKTAILS.getId(), 2),
	URGENT_DELIVERY(94, PortLocation.PANDEMONIUM, PortLocation.PANDEMONIUM, PortLocation.PORT_KHAZARD, PortPaths.PANDEMONIUM_PORT_KHAZARD, false, "Urgent delivery", ItemID.CARGO_CRATE_OF_KYATT_TEETH.getId(), 3),
	TAR_NEEDED(95, PortLocation.PANDEMONIUM, PortLocation.PORT_KHAZARD, PortLocation.PANDEMONIUM, PortPaths.PANDEMONIUM_PORT_KHAZARD, true, "Tar needed", ItemID.CARGO_CRATE_OF_SWAMP_PASTE.getId(), 3),
	/*
	removed due to not having a noticeboard location in the sailing beta
	SECRET_PIRATE_LOOTY(55, null, PortLocation.PORT_SARIM, PortLocation.PANDEMONIUM, PortPaths.DEFAULT, false, "Secret pirate looty", ItemID.CARGO_CRATE_OF_PIRATE_LOOTY.getId(), 1),
	 */
	CARGO_RECLAIMS(102, PortLocation.MUSA_POINT, PortLocation.PORT_SARIM, PortLocation.MUSA_POINT, PortPaths.MUSA_POINT_PORT_SARIM, true, "Cargo reclaims", ItemID.CARGO_CRATE_MUSA_POINT_0.getId(), 1),
	PATCH_NOTE(103, PortLocation.MUSA_POINT, PortLocation.PANDEMONIUM, PortLocation.MUSA_POINT, PortPaths.MUSA_POINT_PANDEMONIUM, true, "Patch note", ItemID.CARGO_CRATE_MUSA_POINT_1.getId(), 1),
	PRECIOUS_DELIVERY(104, PortLocation.MUSA_POINT, PortLocation.MUSA_POINT, PortLocation.PORT_SARIM, PortPaths.MUSA_POINT_PORT_SARIM, false, "Precious delivery", ItemID.CARGO_CRATE_MUSA_POINT_2.getId(), 2),
	PINING_FOR_PINEAPPLES(105, PortLocation.MUSA_POINT, PortLocation.MUSA_POINT, PortLocation.PANDEMONIUM, PortPaths.MUSA_POINT_PANDEMONIUM, false, "Pining for pineapples", ItemID.CARGO_CRATE_MUSA_POINT_3.getId(), 2),
	STOP_YOUR_WINING(106, PortLocation.MUSA_POINT, PortLocation.PANDEMONIUM, PortLocation.MUSA_POINT, PortPaths.MUSA_POINT_PANDEMONIUM, true, "Stop your wining!", ItemID.CARGO_CRATE_MUSA_POINT_4.getId(), 2),
	NO_LEG_TO_STAND_ON(107, PortLocation.MUSA_POINT, PortLocation.PORT_SARIM, PortLocation.MUSA_POINT, PortPaths.MUSA_POINT_PORT_SARIM, true, "No leg to stand on", ItemID.CARGO_CRATE_MUSA_POINT_5.getId(), 3),
	SMOOTH_SAILING(108, PortLocation.MUSA_POINT, PortLocation.MUSA_POINT, PortLocation.PANDEMONIUM, PortPaths.MUSA_POINT_PANDEMONIUM, false, "Smooth sailing", ItemID.CARGO_CRATE_MUSA_POINT_6.getId(), 3),
	GENERAL_SUPPLIES(109, PortLocation.MUSA_POINT, PortLocation.CATHERBY, PortLocation.MUSA_POINT, PortPaths.CATHERBY_MUSA_POINT, false, "General supplies", ItemID.CARGO_CRATE_MUSA_POINT_7.getId(), 3),
	GOING_BANANAS(110, PortLocation.MUSA_POINT, PortLocation.MUSA_POINT, PortLocation.CATHERBY, PortPaths.CATHERBY_MUSA_POINT, true, "Going bananas", ItemID.CARGO_CRATE_MUSA_POINT_8.getId(), 1),
	CONTAIN_THE_BEER(111, PortLocation.MUSA_POINT, PortLocation.ENTRANA, PortLocation.MUSA_POINT, PortPaths.ENTRANA_MUSA_POINT, false, "Contain the beer", ItemID.CARGO_CRATE_MUSA_POINT_9.getId(), 4),
	A_MONKS_BEST_FRIEND(112, PortLocation.MUSA_POINT, PortLocation.MUSA_POINT, PortLocation.ENTRANA, PortPaths.ENTRANA_MUSA_POINT, true, "A monk's best friend", ItemID.CARGO_CRATE_MUSA_POINT_10.getId(), 4),
	SECRET_INGREDIENT(113, PortLocation.MUSA_POINT, PortLocation.BRIMHAVEN, PortLocation.MUSA_POINT, PortPaths.BRIMHAVEN_MUSA_POINT, false, "Secret ingredient", ItemID.CARGO_CRATE_MUSA_POINT_11.getId(), 2),
	THE_LONG_CON(114, PortLocation.MUSA_POINT, PortLocation.MUSA_POINT, PortLocation.BRIMHAVEN, PortPaths.BRIMHAVEN_MUSA_POINT, true, "The long con", ItemID.CARGO_CRATE_MUSA_POINT_12.getId(), 1),
	LETS_TEAK_BUSINESS(115, PortLocation.MUSA_POINT, PortLocation.MUSA_POINT, PortLocation.ARDOUGNE, PortPaths.MUSA_POINT_ARDOUGNE, false, "Let's teak business", ItemID.CARGO_CRATE_MUSA_POINT_13.getId(), 3),
	GNOME_DELIVERY(116, PortLocation.MUSA_POINT, PortLocation.MUSA_POINT, PortLocation.ARDOUGNE, PortPaths.MUSA_POINT_ARDOUGNE, false, "Gnome delivery", ItemID.CARGO_CRATE_MUSA_POINT_14.getId(), 5);

	private final int id;
	private final PortLocation noticeBoard;
	private final PortLocation cargoLocation;
	private final PortLocation deliveryLocation;
	public final PortPaths dockMarkers;
	public final boolean reversePath;
	public final String taskName;
	public final int cargo;
	public final int cargoAmount;

	PortTaskData(Integer id, PortLocation noticeBoard, PortLocation cargoLocation, PortLocation deliveryLocation, PortPaths dockMarkers, boolean reversePath, String taskName, int cargo, int cargoAmount)
	{
		this.id = id;
		this.noticeBoard = noticeBoard;
		this.cargoLocation = cargoLocation;
		this.deliveryLocation = deliveryLocation;
		this.dockMarkers = dockMarkers;
		this.reversePath = reversePath;
		this.taskName = taskName;
		this.cargo = cargo;
		this.cargoAmount = cargoAmount;
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

