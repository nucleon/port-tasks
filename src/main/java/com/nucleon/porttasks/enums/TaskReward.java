package com.nucleon.porttasks.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum TaskReward
{
	TASK_8664(8664, 78), // Pandemonium platebody delivery
	TASK_8665(8665, 155), // Port Sarim spice delivery
	TASK_8666(8666, 79), // Musa Point logs delivery
	TASK_8668(8668, 289), // Catherby bait delivery
	TASK_8669(8669, 578), // Port Sarim honey delivery
	TASK_8670(8670, 78), // Pandemonium battleaxe delivery
	TASK_8673(8673, 300), // Ardougne salamander delivery
	TASK_8675(8675, 353), // Port Khazard swamp paste delivery
	TASK_8676(8676, 706), // Port Sarim sword delivery
	TASK_8677(8677, 262), // Brimhaven vodka delivery
	TASK_8678(8678, 554), // Port Sarim delivery of nothing sinister
	TASK_8679(8679, 1011), // Port Piscarilius book delivery
	TASK_8680(8680, 655), // Ruin of Unkah rune delivery
	TASK_8681(8681, 6525), // Rellekka pie delivery
	TASK_8683(8683, 78), // Port Sarim jewellery delivery
	TASK_8684(8684, 155), // Pandemonium steel delivery
	TASK_8685(8685, 89), // Musa Point rotten banana delivery
	TASK_8686(8686, 178), // Pandemonium rum delivery
	TASK_8687(8687, 300), // Brimhaven jewellery delivery
	TASK_8689(8689, 78), // Port Sarim fish delivery
	TASK_8690(8690, 99), // Musa Point ship part delivery
	TASK_8692(8692, 296), // Catherby grog delivery
	TASK_8693(8693, 591), // Pandemonium arrowtip delivery
	TASK_8696(8696, 377), // Port Khazard kyatt teeth delivery
	TASK_8697(8697, 755), // Pandemonium swamp paste delivery
	TASK_8698(8698, 308), // Ruin of Unkah cocktail delivery
	TASK_8701(8701, 331), // Ardougne sand delivery
	TASK_8702(8702, 79), // Port Sarim coconut delivery
	TASK_8703(8703, 158), // Musa Point banana delivery
	TASK_8704(8704, 89), // Pandemonium banana delivery
	TASK_8705(8705, 178), // Musa Point eye patch delivery
	TASK_8706(8706, 169), // Brimhaven rum delivery
	TASK_8708(8708, 88), // Port Sarim logs delivery
	TASK_8709(8709, 110), // Pandemonium rum delivery
	TASK_8711(8711, 193), // Catherby coconut delivery
	TASK_8718(8718, 1001), // Summer Shore banana delivery
	TASK_8721(8721, 289), // Port Sarim flax delivery
	TASK_8722(8722, 578), // Catherby bait delivery
	TASK_8723(8723, 154), // Ardougne fish delivery
	TASK_8725(8725, 197), // Port Khazard bow delivery
	TASK_8726(8726, 394), // Catherby glassmaking supplies delivery
	TASK_8732(8732, 296), // Pandemonium wax delivery
	TASK_8733(8733, 591), // Catherby coconut delivery
	TASK_8734(8734, 116), // Brimhaven fish delivery
	TASK_8736(8736, 206), // Musa Point lobster delivery
	TASK_8737(8737, 413), // Catherby banana delivery
	TASK_8738(8738, 1078), // Port Piscarilius honey delivery
	TASK_8740(8740, 185), // Musa Point cocktail delivery
	TASK_8741(8741, 339), // Brimhaven fish delivery
	TASK_8744(8744, 720), // Corsair Cove sword delivery
	TASK_8749(8749, 285), // Pandemonium meat delivery
	TASK_8750(8750, 601), // Brimhaven spice delivery
	TASK_8751(8751, 131), // Catherby pineapple delivery
	TASK_8752(8752, 231), // Brimhaven compost delivery
	TASK_8753(8753, 1597), // Red Rock karambwan delivery
	TASK_8755(8755, 277), // Port Sarim amulet delivery
	TASK_8756(8756, 69), // Ardougne fish delivery
	TASK_8759(8759, 69), // Brimhaven spice delivery
	TASK_8761(8761, 139), // Catherby fur delivery
	TASK_8765(8765, 120), // Brimhaven beer delivery
	TASK_8770(8770, 1736), // Red Rock protective clothing delivery
	TASK_8777(8777, 631), // Ardougne rune delivery
	TASK_8779(8779, 164), // Port Khazard platebody delivery
	TASK_8782(8782, 369), // Port Sarim sword delivery
	TASK_8783(8783, 738), // Port Khazard swamp paste delivery
	TASK_8784(8784, 130), // Ardougne sand delivery
	TASK_8787(8787, 180), // Catherby gold delivery
	TASK_8788(8788, 361), // Port Khazard secret delivery
	TASK_8789(8789, 944), // Civitas illa Fortis pineapple delivery
	TASK_8791(8791, 982), // Ruin of Unkah ship part delivery
	TASK_8794(8794, 976), // Port Piscarilius platebody delivery
	TASK_8797(8797, 514), // Port Khazard scimitar delivery
	TASK_8799(8799, 509), // Void Knights' Outpost dragonhide delivery
	TASK_8804(8804, 669), // Void Knights' Outpost rune delivery
	TASK_8810(8810, 720), // Brimhaven jewellery delivery
	TASK_8811(8811, 1440), // Corsair Cove rum delivery
	TASK_8812(8812, 720), // Pandemonium spice delivery
	TASK_8813(8813, 1440), // Corsair Cove ship part delivery
	TASK_8816(8816, 693), // Port Sarim cacti delivery
	TASK_8817(8817, 1386), // Ruin of Unkah rune delivery
	TASK_8818(8818, 789), // Summer Shore sandstone delivery
	TASK_8819(8819, 1656), // Ruin of Unkah coral delivery
	TASK_8820(8820, 926), // Red Rock granite delivery
	TASK_8823(8823, 1506), // Summer Shore cacti delivery
	TASK_8827(8827, 1088), // Void Knights' Outpost granite delivery
	TASK_8831(8831, 1296), // Brimhaven sandstone delivery
	TASK_8843(8843, 2727), // Deepfin Point pest remains delivery
	TASK_8846(8846, 3212), // Civitas illa Fortis rune delivery
	TASK_8848(8848, 2176), // Port Roberts fish delivery
	TASK_8850(8850, 1018), // Port Khazard arrowtip delivery
	TASK_8852(8852, 1574), // Catherby arrowtip delivery
	TASK_8856(8856, 1227), // Void Knights' Outpost coral delivery
	TASK_8858(8858, 1001), // Pandemonium coral delivery
	TASK_8859(8859, 2002), // Summer Shore gem delivery
	TASK_8862(8862, 1250), // Pandemonium sea shell delivery
	TASK_8863(8863, 1348), // Port Sarim camphor delivery
	TASK_8864(8864, 2618), // Summer Shore shield delivery
	TASK_8865(8865, 2714), // Aldarin crab paste delivery
	TASK_8869(8869, 4227), // Deepfin Point camphor delivery
	TASK_8870(8870, 1463), // Port Khazard crab paste delivery
	TASK_8873(8873, 1319), // Port Khazard red coral delivery
	TASK_8874(8874, 2639), // Red Rock fish delivery
	TASK_8875(8875, 3319), // Deepfin Point plank delivery
	TASK_8877(8877, 1319), // Port Sarim red coral delivery
	TASK_8881(8881, 1840), // Port Sarim fish delivery
	TASK_8882(8882, 3513), // Port Roberts red coral delivery
	TASK_8883(8883, 7026), // Red Rock spice delivery
	TASK_8884(8884, 625), // Void Knights' Outpost ore delivery
	TASK_8892(8892, 91), // Hosidius rope delivery
	TASK_8896(8896, 810), // Port Roberts berry delivery
	TASK_8909(8909, 3826), // Lunar Isle berry delivery
	TASK_8911(8911, 264), // Land's End fish delivery
	TASK_8912(8912, 506), // Port Piscarilius plank delivery
	TASK_8913(8913, 810), // Port Roberts fish delivery
	TASK_8917(8917, 330), // Land's End meat delivery
	TASK_8921(8921, 385), // Port Piscarilius seed delivery
	TASK_8922(8922, 2025), // Lunar Isle fish delivery
	TASK_8926(8926, 2252), // Port Tyras fur delivery
	TASK_8927(8927, 990), // Musa Point important delivery
	TASK_8928(8928, 2318), // Rellekka beer delivery
	TASK_8933(8933, 907), // Civitas illa Fortis vegetable delivery
	TASK_8934(8934, 486), // Port Roberts gem delivery
	TASK_8936(8936, 1528), // Aldarin fur delivery
	TASK_8937(8937, 435), // Port Piscarilius jewellery delivery
	TASK_8940(8940, 4188), // Civitas illa Fortis ore delivery
	TASK_8941(8941, 2076), // Prifddinas clothes delivery
	TASK_8946(8946, 1937), // Ardougne spice delivery
	TASK_8949(8949, 212), // Sunset Coast beer delivery
	TASK_8950(8950, 424), // Aldarin fur delivery
	TASK_8951(8951, 1232), // Civitas illa Fortis fruit delivery
	TASK_8952(8952, 2464), // Aldarin spear delivery
	TASK_8953(8953, 1896), // Deepfin Point pickaxe delivery
	TASK_8954(8954, 3793), // Aldarin nickel delivery
	TASK_8955(8955, 347), // Sunset Coast wine delivery
	TASK_8960(8960, 3541), // Prifddinas potion delivery
	TASK_8964(8964, 2687), // Port Tyras shield delivery
	TASK_8966(8966, 4707), // Rellekka wine delivery
	TASK_8970(8970, 486), // Civitas illa Fortis spice delivery
	TASK_8972(8972, 810), // Land's End ore delivery
	TASK_8974(8974, 1171), // Port Piscarilius jewellery delivery
	TASK_8977(8977, 2015), // Deepfin Point plank delivery
	TASK_8979(8979, 3016), // Lunar Isle fur delivery
	TASK_8982(8982, 9913), // Port Roberts honey delivery
	TASK_8984(8984, 2639), // Rellekka ship part delivery
	TASK_8985(8985, 3744), // Red Rock herb delivery
	TASK_8987(8987, 2094), // Port Roberts nickel delivery
	TASK_8988(8988, 4030), // Deepfin Point fabric delivery
	TASK_8989(8989, 1817), // Port Tyras mithril delivery
	TASK_8990(8990, 3635), // Deepfin Point logs delivery
	TASK_8991(8991, 1817), // Aldarin adamantite delivery
	TASK_8992(8992, 3793), // Deepfin Point fruit delivery
	TASK_8994(8994, 1872), // Port Tyras coal delivery
	TASK_8996(8996, 2094), // Civitas illa Fortis nickel delivery
	TASK_8997(8997, 4346), // Deepfin Point wool delivery
	TASK_8998(8998, 3121), // Port Piscarilius coal delivery
	TASK_9000(9000, 3961), // Red Rock coal delivery
	TASK_9001(9001, 7921), // Deepfin Point fabric delivery
	TASK_9002(9002, 4884), // Rellekka lead delivery
	TASK_9006(9006, 1221), // Prifddinas logs delivery
	TASK_9012(9012, 1140), // Prifddinas halberd delivery
	TASK_9022(9022, 3286), // Lunar Isle logs delivery
	TASK_9024(9024, 5531), // Port Tyras fruit delivery
	TASK_9025(9025, 1140), // Port Tyras fabric delivery
	TASK_9026(9026, 2279), // Prifddinas logs delivery
	TASK_9027(9027, 1669), // Port Roberts logs delivery
	TASK_9028(9028, 3175), // Prifddinas fish delivery
	TASK_9029(9029, 4477), // Ardougne crystal seed delivery
	TASK_9030(9030, 8791), // Prifddinas silk delivery
	TASK_9031(9031, 1440), // Port Tyras ore delivery
	TASK_9034(9034, 2442), // Deepfin Point plank delivery
	TASK_9035(9035, 4721), // Prifddinas ore delivery
	TASK_9036(9036, 3541), // Aldarin crystal seed delivery
	TASK_9037(9037, 7082), // Prifddinas potion delivery
	TASK_9040(9040, 2746), // Lunar Isle crystal seed delivery
	TASK_9041(9041, 5277), // Rellekka plank delivery
	TASK_9044(9044, 869), // Etceteria fish delivery
	TASK_9047(9047, 2529), // Rellekka rope delivery
	TASK_9048(9048, 4778), // Sunset Coast warhammer delivery
	TASK_9049(9049, 9556), // Rellekka fabric delivery
	TASK_9053(9053, 2710), // Port Roberts fish delivery
	TASK_9054(9054, 5277), // Rellekka plank delivery
	TASK_9056(9056, 3691), // Rellekka monkfish delivery
	TASK_9057(9057, 2246), // Port Piscarilius fur delivery
	TASK_9058(9058, 4493), // Rellekka redwood delivery
	TASK_9061(9061, 4583), // Deepfin Point fur delivery
	TASK_9063(9063, 869), // Rellekka fish delivery
	TASK_9064(9064, 1580), // Etceteria iron delivery
	TASK_9067(9067, 3003), // Port Roberts teak delivery
	TASK_9068(9068, 6005), // Etceteria fruit delivery
	TASK_9069(9069, 977), // Rellekka teak delivery
	TASK_9073(9073, 5294), // Etceteria plank delivery
	TASK_9074(9074, 1422), // Neitiznot mahogany delivery
	TASK_9076(9076, 5294), // Sunset Coast fabric delivery
	TASK_9077(9077, 10588), // Etceteria sword delivery
	TASK_9078(9078, 1935), // Piscatoris flax delivery
	TASK_9079(9079, 4051), // Etceteria monkfish delivery
	TASK_9082(9082, 2025), // Port Piscarilius fabric delivery
	TASK_9083(9083, 4231), // Lunar Isle fish delivery
	TASK_9084(9084, 1575), // Piscatoris fabric delivery
	TASK_9085(9085, 3151), // Lunar Isle fur delivery
	TASK_9086(9086, 2746), // Prifddinas herb delivery
	TASK_9087(9087, 5491), // Lunar Isle potion delivery
	TASK_9088(9088, 2464), // Port Piscarilius rune delivery
	TASK_9090(9090, 3168), // Prifddinas rune delivery
	TASK_9092(9092, 9092), // Lunar Isle coal delivery
	TASK_9094(9094, 5851), // Lunar Isle fish delivery
	TASK_9096(9096, 7381), // Lunar Isle fur delivery
	TASK_9097(9097, 6571), // Red Rock rune delivery
	TASK_9098(9098, 13142), // Lunar Isle red coral delivery
	TASK_9099(9099, 4374), // Land's End gem delivery
	TASK_9100(9100, 12670), // Lunar Isle potion delivery
	TASK_9108(9108, 3465), // Pandemonium bull shark bounty
	TASK_9109(9109, 3465), // Pandemonium mogre bounty
	TASK_9111(9111, 8800), // Pandemonium butterfly ray bounty
	TASK_9112(9112, 3465), // Pandemonium tern bounty
	TASK_9114(9114, 11825), // Pandemonium frigatebird bounty
	TASK_9119(9119, 3465), // Musa Point eagle ray bounty
	TASK_9122(9122, 3465), // Catherby tern bounty
	TASK_9123(9123, 3465), // Catherby osprey bounty
	TASK_9141(9141, 11825), // Ardougne stingray bounty
	TASK_9144(9144, 3465), // Port Khazard bull shark bounty
	TASK_9153(9153, 11825), // Corsair Cove stingray bounty
	TASK_9156(9156, 40370), // Corsair Cove great white shark bounty
	TASK_9160(9160, 8800), // Ruins of Unkah butterfly ray bounty
	TASK_9163(9163, 40370), // Ruins of Unkah great white shark bounty
	TASK_9164(9164, 11825), // Void Knights' Outpost stingray bounty
	TASK_9167(9167, 14575), // Void Knights' Outpost pygmy kraken bounty
	TASK_9169(9169, 40370), // Void Knights' Outpost great white shark bounty
	TASK_9173(9173, 11825), // Summer Shore frigatebird bounty
	TASK_9175(9175, 14575), // Summer Shore albatross bounty
	TASK_9177(9177, 11825), // Summer Shore stingray bounty
	TASK_9181(9181, 40370), // Red Rock great white shark bounty
	TASK_9182(9182, 11825), // Red Rock stingray bounty
	TASK_9187(9187, 3465), // Land's End tern bounty
	TASK_9190(9190, 40370), // Land's End great white shark bounty
	TASK_9194(9194, 3465), // Port Piscarilius tern bounty
	TASK_9196(9196, 19910), // Port Piscarilius tiger shark bounty
	TASK_9198(9198, 40370), // Port Piscarilius armoured kraken bounty
	TASK_9206(9206, 11825), // Aldarin stingray bounty
	TASK_9207(9207, 3465), // Aldarin osprey bounty
	TASK_9209(9209, 40370), // Aldarin great white shark bounty
	TASK_9215(9215, 40370), // Port Roberts armoured kraken bounty
	TASK_9216(9216, 40370), // Port Roberts great white shark bounty
	TASK_9218(9218, 47080), // Port Roberts orca bounty
	TASK_9219(9219, 14575), // Port Roberts albatross bounty
	TASK_9221(9221, 3465), // Deepfin Point eagle ray bounty
	TASK_9224(9224, 11825), // Deepfin Point stingray bounty
	TASK_9232(9232, 40370), // Port Tyras great white shark bounty
	TASK_9233(9233, 25135), // Port Tyras manta ray bounty
	TASK_9236(9236, 40370), // Prifddinas armoured kraken bounty
	TASK_9238(9238, 40370), // Prifddinas great white shark bounty
	TASK_9241(9241, 14575), // Rellekka albatross bounty
	TASK_9242(9242, 8800), // Rellekka hammerhead shark bounty
	TASK_9243(9243, 19910), // Rellekka narwhal bounty
	TASK_9244(9244, 40370), // Rellekka armoured kraken bounty
	TASK_9245(9245, 47080), // Rellekka orca bounty
	TASK_9246(9246, 47080), // Rellekka vampyre kraken bounty
	TASK_9248(9248, 14575), // Etceteria albatross bounty
	TASK_9249(9249, 47080), // Etceteria orca bounty
	TASK_9250(9250, 47080), // Etceteria vampyre kraken bounty
	TASK_9251(9251, 19910), // Etceteria narwhal bounty
	TASK_9253(9253, 47080), // Etceteria orca bounty
	TASK_9254(9254, 40370), // Etceteria armoured kraken bounty
	TASK_9255(9255, 40370), // Lunar Isle armoured kraken bounty
	TASK_9256(9256, 47080), // Lunar Isle vampyre kraken bounty
	TASK_9257(9257, 19910), // Lunar Isle narwhal bounty
	TASK_9259(9259, 14575), // Lunar Isle albatross bounty
	TASK_9260(9260, 47080), // Lunar Isle vampyre kraken bounty
	TASK_9261(9261, 19910), // Lunar Isle narwhal bounty
	TASK_13310(13310, 47080), // Deepfin Point veiled kraken bounty
	TASK_13311(13311, 47080); // Port Tyras veiled kraken bounty
	;

	private final int dbow;
	private final int reward;

	private static final Map<Integer, TaskReward> BY_DBROW =
		Arrays.stream(values())
			.collect(Collectors.toMap(tr -> tr.dbow, tr -> tr));

	TaskReward(int dbrow, int reward)
	{
		this.dbow = dbrow;
		this.reward = reward;
	}

	public static int getIntRewardForTask(int dbrow)
	{
		TaskReward tr = BY_DBROW.get(dbrow);
		if (tr == null || tr.reward < 0)
		{
			return 0;
		}
		return tr.reward;
	}

	public static String getRewardForTask(int dbrow)
	{
		TaskReward tr = BY_DBROW.get(dbrow);
		if (tr == null || tr.reward < 0)
		{
			return "Unknown";
		}
		return String.valueOf(tr.reward);
	}
}
