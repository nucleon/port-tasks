package com.nucleon.porttasks.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum TaskReward
{
	TASK_4966(4966, 4474), // Void Knights' Outpost clothes delivery
	TASK_4967(4967, 8947), // Rellekka arrowtip delivery
	TASK_8664(8664, 71), // Pandemonium platebody delivery
	TASK_8665(8665, 141), // Port Sarim spice delivery
	TASK_8666(8666, 72), // Musa Point logs delivery
	TASK_8667(8667, 80), // Port Sarim logs delivery
	TASK_8668(8668, 263), // Catherby bait delivery
	TASK_8669(8669, 525), // Port Sarim honey delivery
	TASK_8670(8670, 71), // Pandemonium battleaxe delivery
	TASK_8671(8671, 90), // Musa Point fish delivery
	TASK_8672(8672, 294), // Catherby potato delivery
	TASK_8673(8673, 273), // Ardougne salamander delivery
	TASK_8674(8674, 546), // Port Sarim seed delivery
	TASK_8675(8675, 321), // Port Khazard swamp paste delivery
	TASK_8676(8676, 336), // Port Sarim sword delivery
	TASK_8677(8677, 238), // Brimhaven vodka delivery
	TASK_8678(8678, 504), // Port Sarim delivery of nothing sinister
	TASK_8679(8679, 919), // Port Piscarilius book delivery
	TASK_8680(8680, 595), // Ruin of Unkah rune delivery
	TASK_8681(8681, 5932), // Rellekka pie delivery
	TASK_8682(8682, 2273), // Port Sarim pest remains delivery
	TASK_8683(8683, 71), // Port Sarim jewellery delivery
	TASK_8684(8684, 141), // Pandemonium steel delivery
	TASK_8685(8685, 81), // Musa Point rotten banana delivery
	TASK_8686(8686, 100), // Pandemonium rum delivery
	TASK_8687(8687, 273), // Brimhaven jewellery delivery
	TASK_8688(8688, 546), // Pandemonium pineapple delivery
	TASK_8689(8689, 71), // Port Sarim fish delivery
	TASK_8690(8690, 90), // Musa Point ship part delivery
	TASK_8691(8691, 276), // Brimhaven cacti delivery
	TASK_8692(8692, 269), // Catherby grog delivery
	TASK_8693(8693, 538), // Pandemonium arrowtip delivery
	TASK_8694(8694, 595), // Corsair Cove scimitar delivery
	TASK_8695(8695, 1190), // Pandemonium gold delivery
	TASK_8696(8696, 343), // Port Khazard kyatt teeth delivery
	TASK_8697(8697, 686), // Pandemonium swamp paste delivery
	TASK_8698(8698, 243), // Ruin of Unkah cocktail delivery
	TASK_8699(8699, 548), // Pandemonium granite delivery
	TASK_8700(8700, 538), // Cairn Isle secret delivery
	TASK_8701(8701, 118), // Ardougne sand delivery
	TASK_8702(8702, 72), // Port Sarim coconut delivery
	TASK_8703(8703, 144), // Musa Point banana delivery
	TASK_8704(8704, 81), // Pandemonium banana delivery
	TASK_8705(8705, 162), // Musa Point eye patch delivery
	TASK_8706(8706, 154), // Brimhaven rum delivery
	TASK_8707(8707, 308), // Musa Point mahogany delivery
	TASK_8708(8708, 144), // Port Sarim logs delivery
	TASK_8709(8709, 162), // Pandemonium rum delivery
	TASK_8710(8710, 179), // Brimhaven silver delivery
	TASK_8711(8711, 175), // Catherby coconut delivery
	TASK_8712(8712, 375), // Musa Point honey delivery
	TASK_8713(8713, 168), // Entrana banana delivery
	TASK_8714(8714, 337), // Musa Point beer glass delivery
	TASK_8715(8715, 203), // Ardougne banana delivery
	TASK_8716(8716, 378), // Musa Point silk delivery
	TASK_8717(8717, 751), // Corsair Cove peg leg delivery
	TASK_8718(8718, 791), // Summer Shore banana delivery
	TASK_8719(8719, 3313), // Port Tyras snakeskin delivery
	TASK_8720(8720, 492), // Musa Point secret delivery
	TASK_8721(8721, 263), // Port Sarim flax delivery
	TASK_8722(8722, 525), // Catherby bait delivery
	TASK_8723(8723, 63), // Ardougne fish delivery
	TASK_8724(8724, 280), // Catherby arrowtip delivery
	TASK_8725(8725, 179), // Port Khazard bow delivery
	TASK_8726(8726, 358), // Catherby glassmaking supplies delivery
	TASK_8727(8727, 294), // Port Sarim beer delivery
	TASK_8728(8728, 134), // Ardougne poison delivery
	TASK_8729(8729, 219), // Port Khazard coal delivery
	TASK_8730(8730, 118), // Entrana barley delivery
	TASK_8731(8731, 236), // Catherby vial delivery
	TASK_8732(8732, 269), // Pandemonium wax delivery
	TASK_8733(8733, 538), // Catherby coconut delivery
	TASK_8734(8734, 105), // Brimhaven fish delivery
	TASK_8735(8735, 210), // Catherby karambwan delivery
	TASK_8736(8736, 188), // Musa Point lobster delivery
	TASK_8737(8737, 375), // Catherby banana delivery
	TASK_8738(8738, 980), // Port Piscarilius honey delivery
	TASK_8739(8739, 2488), // Catherby javelin delivery
	TASK_8740(8740, 168), // Musa Point cocktail delivery
	TASK_8741(8741, 308), // Brimhaven fish delivery
	TASK_8742(8742, 97), // Port Khazard spear delivery
	TASK_8743(8743, 224), // Brimhaven iron delivery
	TASK_8744(8744, 595), // Corsair Cove sword delivery
	TASK_8745(8745, 1133), // Brimhaven gem delivery
	TASK_8746(8746, 164), // Musa Point meat delivery
	TASK_8747(8747, 143), // Port Khazard fruit delivery
	TASK_8748(8748, 639), // Corsair Cove beer delivery
	TASK_8749(8749, 259), // Pandemonium meat delivery
	TASK_8750(8750, 546), // Brimhaven spice delivery
	TASK_8751(8751, 119), // Catherby pineapple delivery
	TASK_8752(8752, 210), // Brimhaven compost delivery
	TASK_8753(8753, -1), // Red Rock karambwan delivery
	TASK_8754(8754, -1), // Brimhaven machinery delivery
	TASK_8755(8755, 252), // Port Sarim amulet delivery
	TASK_8756(8756, 63), // Ardougne fish delivery
	TASK_8757(8757, 976), // Civitas illa Fortis pineapple delivery
	TASK_8758(8758, 2768), // Brimhaven camphor delivery
	TASK_8759(8759, 63), // Brimhaven spice delivery
	TASK_8760(8760, 126), // Ardougne gold delivery
	TASK_8761(8761, 126), // Catherby fur delivery
	TASK_8762(8762, 252), // Ardougne arrowtip delivery
	TASK_8763(8763, 2802), // Port Tyras platebody delivery
	TASK_8764(8764, 6465), // Ardougne meat delivery
	TASK_8765(8765, 109), // Brimhaven beer delivery
	TASK_8766(8766, 149), // Catherby seed delivery
	TASK_8767(8767, 1836), // Port Tyras sword delivery
	TASK_8768(8768, 955), // Port Piscarilius wine delivery
	TASK_8769(8769, 1880), // Ardougne fur delivery
	TASK_8770(8770, -1), // Red Rock protective clothing delivery
	TASK_8771(8771, -1), // Ardougne red coral delivery
	TASK_8772(8772, 90), // Port Khazard bread delivery
	TASK_8773(8773, 149), // Ardougne fish delivery
	TASK_8774(8774, 1698), // Summer Shore jewellery delivery
	TASK_8775(8775, 951), // Civitas illa Fortis artefact delivery
	TASK_8776(8776, 1094), // Void Knights' Outpost platebody delivery
	TASK_8777(8777, 574), // Ardougne rune delivery
	TASK_8778(8778, 140), // Ardougne fish delivery
	TASK_8779(8779, 149), // Port Khazard platebody delivery
	TASK_8780(8780, 468), // Corsair Cove dagger delivery
	TASK_8781(8781, 873), // Port Khazard vodka delivery
	TASK_8782(8782, 336), // Port Sarim sword delivery
	TASK_8783(8783, 671), // Port Khazard swamp paste delivery
	TASK_8784(8784, 301), // Ardougne sand delivery
	TASK_8785(8785, 525), // Corsair Cove khali brew delivery
	TASK_8786(8786, 379), // Port Sarim fish delivery
	TASK_8787(8787, 164), // Catherby gold delivery
	TASK_8788(8788, 328), // Port Khazard secret delivery
	TASK_8789(8789, 859), // Civitas illa Fortis pineapple delivery
	TASK_8790(8790, 1751), // Port Khazard huasca delivery
	TASK_8791(8791, 893), // Ruin of Unkah ship part delivery
	TASK_8792(8792, 1785), // Port Khazard angler outfit delivery
	TASK_8793(8793, 5025), // Rellekka dragon bitter delivery
	TASK_8794(8794, 888), // Port Piscarilius platebody delivery
	TASK_8795(8795, 236), // Entrana sand delivery
	TASK_8796(8796, 224), // Port Khazard herb delivery
	TASK_8797(8797, 468), // Port Khazard scimitar delivery
	TASK_8798(8798, 873), // Corsair Cove platebody delivery
	TASK_8799(8799, 463), // Void Knights' Outpost dragonhide delivery
	TASK_8800(8800, 842), // Corsair Cove arrowtip delivery
	TASK_8801(8801, 2047), // Port Tyras herb delivery
	TASK_8802(8802, 3438), // Corsair Cove meat delivery
	TASK_8803(8803, 525), // Port Khazard gunpowder delivery
	TASK_8804(8804, 608), // Void Knights' Outpost rune delivery
	TASK_8805(8805, 6609), // Port Tyras sword delivery
	TASK_8806(8806, 1065), // Port Piscarilius jewellery delivery
	TASK_8807(8807, 2961), // Corsair Cove fish delivery
	TASK_8808(8808, 312), // Cairn Isle gold delivery
	TASK_8809(8809, 686), // Corsair Cove gem delivery
	TASK_8810(8810, 655), // Brimhaven jewellery delivery
	TASK_8811(8811, 1309), // Corsair Cove rum delivery
	TASK_8812(8812, 655), // Pandemonium spice delivery
	TASK_8813(8813, 1309), // Corsair Cove ship part delivery
	TASK_8814(8814, 1216), // Civitas illa Fortis book delivery
	TASK_8815(8815, 1593), // Aldarin dagger delivery
	TASK_8816(8816, 630), // Port Sarim cacti delivery
	TASK_8817(8817, 1260), // Ruin of Unkah rune delivery
	TASK_8818(8818, 718), // Summer Shore sandstone delivery
	TASK_8819(8819, 1505), // Ruin of Unkah coral delivery
	TASK_8820(8820, -1), // Red Rock granite delivery
	TASK_8821(8821, -1), // Ruin of Unkah plank delivery
	TASK_8822(8822, 800), // Port Sarim kebab delivery
	TASK_8823(8823, 1369), // Summer Shore cacti delivery
	TASK_8824(8824, -1), // Red Rock fish delivery
	TASK_8825(8825, 315), // Pandemonium cacti delivery
	TASK_8826(8826, 560), // Ruin of Unkah cocktail delivery
	TASK_8827(8827, 989), // Void Knights' Outpost granite delivery
	TASK_8828(8828, 1978), // Ruin of Unkah javelin delivery
	TASK_8829(8829, 1015), // Ardougne silk delivery
	TASK_8830(8830, 1960), // Ruin of Unkah fishing supplies delivery
	TASK_8831(8831, 1178), // Brimhaven sandstone delivery
	TASK_8832(8832, 1015), // Catherby silk delivery
	TASK_8833(8833, 3556), // Deepfin Point granite delivery
	TASK_8834(8834, 893), // Ruin of Unkah ship part delivery
	TASK_8835(8835, -1), // Red Rock rune delivery
	TASK_8836(8836, -1), // Void Knights' Outpost herb delivery
	TASK_8837(8837, 1073), // Summer Shore pest remains delivery
	TASK_8838(8838, 2230), // Void Knights' Outpost logs delivery
	TASK_8839(8839, 2406), // Deepfin Point javelin delivery
	TASK_8840(8840, 4669), // Void Knights' Outpost ore delivery
	TASK_8841(8841, -1), // Red Rock fish delivery
	TASK_8842(8842, 2146), // Summer Shore javelin delivery
	TASK_8843(8843, 2479), // Deepfin Point pest remains delivery
	TASK_8844(8844, 1178), // Port Sarim rune delivery
	TASK_8845(8845, 1976), // Void Knights' Outpost potion delivery
	TASK_8846(8846, 2544), // Civitas illa Fortis rune delivery
	TASK_8847(8847, 4982), // Void Knights' Outpost mace delivery
	TASK_8848(8848, 1280), // Port Roberts fish delivery
	TASK_8849(8849, 3366), // Void Knights' Outpost seed delivery
	TASK_8850(8850, 805), // Port Khazard arrowtip delivery
	TASK_8851(8851, 2000), // Port Tyras arrowtip delivery
	TASK_8852(8852, 1244), // Catherby arrowtip delivery
	TASK_8853(8853, 926), // Void Knights' Outpost potion delivery
	TASK_8854(8854, 718), // Ruin of Unkah calquat delivery
	TASK_8855(8855, 1435), // Summer Shore rope delivery
	TASK_8856(8856, 1115), // Void Knights' Outpost coral delivery
	TASK_8857(8857, 1551), // Summer Shore javelin delivery
	TASK_8858(8858, 910), // Pandemonium coral delivery
	TASK_8859(8859, 1820), // Summer Shore gem delivery
	TASK_8860(8860, 947), // Ruin of Unkah fish delivery
	TASK_8861(8861, 1551), // Void Knights' Outpost spear delivery
	TASK_8862(8862, 1136), // Pandemonium sea shell delivery
	TASK_8863(8863, 1225), // Port Sarim camphor delivery
	TASK_8864(8864, 2380), // Summer Shore shield delivery
	TASK_8865(8865, 2468), // Aldarin crab paste delivery
	TASK_8866(8866, 4865), // Summer Shore potion delivery
	TASK_8867(8867, 2799), // Port Roberts coral delivery
	TASK_8868(8868, 5597), // Summer Shore meat delivery
	TASK_8869(8869, 3843), // Deepfin Point camphor delivery
	TASK_8870(8870, 1330), // Port Khazard crab paste delivery
	TASK_8871(8871, 2988), // Civitas illa Fortis coral delivery
	TASK_8872(8872, 1820), // Summer Shore pineapple delivery
	TASK_8873(8873, -1), // Port Khazard red coral delivery
	TASK_8874(8874, -1), // Red Rock fish delivery
	TASK_8875(8875, -1), // Deepfin Point plank delivery
	TASK_8876(8876, -1), // Red Rock ore delivery
	TASK_8877(8877, -1), // Port Sarim red coral delivery
	TASK_8878(8878, -1), // Red Rock battleaxe delivery
	TASK_8879(8879, -1), // Port Khazard plank delivery
	TASK_8880(8880, -1), // Deepfin Point logs delivery
	TASK_8881(8881, -1), // Port Sarim fish delivery
	TASK_8882(8882, -1), // Port Roberts red coral delivery
	TASK_8883(8883, -1), // Red Rock spice delivery
	TASK_8884(8884, 4669), // Void Knights' Outpost ore delivery
	TASK_8885(8885, -1), // Red Rock rune delivery
	TASK_8886(8886, -1), // Port Piscarilius red coral delivery
	TASK_8887(8887, -1), // Red Rock fish delivery
	TASK_8888(8888, -1), // Land's End fish delivery
	TASK_8889(8889, -1), // Ardougne red coral delivery
	TASK_8890(8890, -1), // Catherby jewellery delivery
	TASK_8891(8891, -1), // Red Rock fur delivery
	TASK_8892(8892, 83), // Hosidius rope delivery
	TASK_8893(8893, 166), // Land's End vegetable delivery
	TASK_8894(8894, 230), // Port Piscarilius plank delivery
	TASK_8895(8895, 460), // Land's End fish delivery
	TASK_8896(8896, 736), // Port Roberts berry delivery
	TASK_8897(8897, 1389), // Land's End wine delivery
	TASK_8898(8898, 83), // Hosidius fabric delivery
	TASK_8899(8899, 288), // Port Piscarilius fur delivery
	TASK_8900(8900, 1065), // Port Roberts meat delivery
	TASK_8901(8901, 2263), // Port Tyras chinchompa delivery
	TASK_8902(8902, 300), // Land's End meat delivery
	TASK_8903(8903, 2294), // Prifddinas fabric delivery
	TASK_8904(8904, 3977), // Land's End gem delivery
	TASK_8905(8905, 1759), // Piscatoris fur delivery
	TASK_8906(8906, 2191), // Land's End fur delivery
	TASK_8907(8907, 1776), // Deepfin Point logs delivery
	TASK_8908(8908, 873), // Port Khazard chainbody delivery
	TASK_8909(8909, 3478), // Lunar Isle berry delivery
	TASK_8910(8910, 2691), // Land's End sword delivery
	TASK_8911(8911, 240), // Land's End fish delivery
	TASK_8912(8912, 460), // Port Piscarilius plank delivery
	TASK_8913(8913, 736), // Port Roberts fish delivery
	TASK_8914(8914, 1473), // Port Piscarilius gin delivery
	TASK_8915(8915, 2046), // Piscatoris fish delivery
	TASK_8916(8916, 4092), // Port Piscarilius fur delivery
	TASK_8917(8917, 300), // Land's End meat delivery
	TASK_8918(8918, 1065), // Port Roberts jewellery delivery
	TASK_8919(8919, -1), // Piscatoris vodka delivery
	TASK_8920(8920, 175), // Hosidius fish delivery
	TASK_8921(8921, 350), // Port Piscarilius seed delivery
	TASK_8922(8922, 1841), // Lunar Isle fish delivery
	TASK_8923(8923, 3846), // Port Piscarilius book delivery
	TASK_8924(8924, 906), // Port Sarim book delivery
	TASK_8925(8925, 1838), // Port Piscarilius book delivery
	TASK_8926(8926, 2047), // Port Tyras fur delivery
	TASK_8927(8927, 900), // Musa Point important delivery
	TASK_8928(8928, 2107), // Rellekka beer delivery
	TASK_8929(8929, 3774), // Port Piscarilius gem delivery
	TASK_8930(8930, 1155), // Aldarin platebody delivery
	TASK_8931(8931, 2240), // Civitas illa Fortis orange delivery
	TASK_8932(8932, 412), // Port Piscarilius meat delivery
	TASK_8933(8933, 825), // Civitas illa Fortis vegetable delivery
	TASK_8934(8934, 442), // Port Roberts gem delivery
	TASK_8935(8935, 968), // Civitas illa Fortis token delivery
	TASK_8936(8936, 1389), // Aldarin fur delivery
	TASK_8937(8937, 396), // Port Piscarilius jewellery delivery
	TASK_8938(8938, 700), // Port Roberts silk delivery
	TASK_8939(8939, 1975), // Deepfin Point wool delivery
	TASK_8940(8940, 3807), // Civitas illa Fortis ore delivery
	TASK_8941(8941, 1887), // Prifddinas clothes delivery
	TASK_8942(8942, 3774), // Civitas illa Fortis seed delivery
	TASK_8943(8943, 997), // Sunset Coast mace delivery
	TASK_8944(8944, 1932), // Civitas illa Fortis fish delivery
	TASK_8945(8945, 2026), // Port Sarim sunbeam ale delivery
	TASK_8946(8946, 1761), // Ardougne spice delivery
	TASK_8947(8947, 2415), // Summer Shore leather delivery
	TASK_8948(8948, -1), // Civitas illa Fortis red coral delivery
	TASK_8949(8949, 193), // Sunset Coast beer delivery
	TASK_8950(8950, 335), // Aldarin fur delivery
	TASK_8951(8951, 1120), // Civitas illa Fortis fruit delivery
	TASK_8952(8952, 2240), // Aldarin spear delivery
	TASK_8953(8953, 1724), // Deepfin Point pickaxe delivery
	TASK_8954(8954, 3448), // Aldarin nickel delivery
	TASK_8955(8955, 316), // Sunset Coast wine delivery
	TASK_8956(8956, 1431), // Civitas illa Fortis gem delivery
	TASK_8957(8957, 1850), // Deepfin Point machinery delivery
	TASK_8958(8958, 1389), // Port Roberts wine delivery
	TASK_8959(8959, 2778), // Aldarin jewellery delivery
	TASK_8960(8960, 3219), // Prifddinas potion delivery
	TASK_8961(8961, 6438), // Aldarin dye delivery
	TASK_8962(8962, 926), // Void Knights' Outpost potion delivery
	TASK_8963(8963, 4250), // Aldarin pest remains delivery
	TASK_8964(8964, 2442), // Port Tyras shield delivery
	TASK_8965(8965, 2188), // Brimhaven wine delivery
	TASK_8966(8966, 4279), // Rellekka wine delivery
	TASK_8967(8967, 9493), // Aldarin monkfish delivery
	TASK_8968(8968, 779), // Port Piscarilius cannonball delivery
	TASK_8969(8969, 1978), // Port Roberts fish delivery
	TASK_8970(8970, 442), // Civitas illa Fortis spice delivery
	TASK_8971(8971, 968), // Port Roberts fur delivery
	TASK_8972(8972, 736), // Land's End ore delivery
	TASK_8973(8973, 1389), // Port Roberts seed delivery
	TASK_8974(8974, 1480), // Port Piscarilius jewellery delivery
	TASK_8975(8975, 639), // Civitas illa Fortis ship part delivery
	TASK_8976(8976, 1065), // Land's End secret delivery
	TASK_8977(8977, 1832), // Deepfin Point plank delivery
	TASK_8978(8978, 3664), // Port Roberts ore delivery
	TASK_8979(8979, 2741), // Lunar Isle fur delivery
	TASK_8980(8980, -1), // Port Roberts rune delivery
	TASK_8981(8981, 4506), // Catherby silver delivery
	TASK_8982(8982, 9012), // Port Roberts honey delivery
	TASK_8983(8983, 4603), // Port Sarim seed delivery
	TASK_8984(8984, 2399), // Rellekka ship part delivery
	TASK_8985(8985, -1), // Red Rock herb delivery
	TASK_8986(8986, 7361), // Port Roberts silk delivery
	TASK_8987(8987, 1904), // Port Roberts nickel delivery
	TASK_8988(8988, 3664), // Deepfin Point fabric delivery
	TASK_8989(8989, 1652), // Port Tyras mithril delivery
	TASK_8990(8990, 2658), // Deepfin Point logs delivery
	TASK_8991(8991, 1652), // Aldarin adamantite delivery
	TASK_8992(8992, 3448), // Deepfin Point fruit delivery
	TASK_8993(8993, 1961), // Port Roberts silver delivery
	TASK_8994(8994, 1702), // Port Tyras coal delivery
	TASK_8995(8995, 1776), // Aldarin lead delivery
	TASK_8996(8996, 1904), // Civitas illa Fortis nickel delivery
	TASK_8997(8997, 3951), // Deepfin Point wool delivery
	TASK_8998(8998, 2837), // Port Piscarilius coal delivery
	TASK_8999(8999, 2220), // Deepfin Point plank delivery
	TASK_9000(9000, -1), // Red Rock coal delivery
	TASK_9001(9001, 3664), // Deepfin Point fabric delivery
	TASK_9002(9002, 4440), // Rellekka lead delivery
	TASK_9003(9003, 8732), // Deepfin Point warhammer delivery
	TASK_9004(9004, 2590), // Hosidius lead delivery
	TASK_9005(9005, -1), // Summer Shore nickel delivery
	TASK_9006(9006, 1110), // Prifddinas logs delivery
	TASK_9007(9007, 2220), // Port Tyras vegetable delivery
	TASK_9008(9008, 1401), // Port Roberts fur delivery
	TASK_9009(9009, 3304), // Port Tyras platebody delivery
	TASK_9010(9010, 1832), // Deepfin Point plank delivery
	TASK_9011(9011, 1309), // Port Tyras ore delivery
	TASK_9012(9012, 1036), // Prifddinas halberd delivery
	TASK_9013(9013, 1473), // Port Roberts bow delivery
	TASK_9014(9014, 3304), // Deepfin Point logs delivery
	TASK_9015(9015, 3233), // Ardougne halberd delivery
	TASK_9016(9016, 3404), // Port Tyras sword delivery
	TASK_9017(9017, 2191), // Land's End fur delivery
	TASK_9018(9018, 4382), // Port Tyras seed delivery
	TASK_9019(9019, 2119), // Port Piscarilius plank delivery
	TASK_9020(9020, 4095), // Port Tyras fish delivery
	TASK_9021(9021, 3340), // Brimhaven meat delivery
	TASK_9022(9022, 2987), // Lunar Isle logs delivery
	TASK_9023(9023, -1), // Red Rock ship part delivery
	TASK_9024(9024, 5028), // Port Tyras fruit delivery
	TASK_9025(9025, 1036), // Port Tyras fabric delivery
	TASK_9026(9026, 2072), // Prifddinas logs delivery
	TASK_9027(9027, 1517), // Port Roberts logs delivery
	TASK_9028(9028, 2886), // Prifddinas fish delivery
	TASK_9029(9029, 4070), // Ardougne crystal seed delivery
	TASK_9030(9030, 7992), // Prifddinas silk delivery
	TASK_9031(9031, 3161), // Port Tyras ore delivery
	TASK_9032(9032, 1678), // Port Roberts staff delivery
	TASK_9033(9033, -1), // Ardougne jewellery delivery
	TASK_9034(9034, 5675), // Deepfin Point plank delivery
	TASK_9035(9035, 4292), // Prifddinas ore delivery
	TASK_9036(9036, 3219), // Aldarin crystal seed delivery
	TASK_9037(9037, 6438), // Prifddinas potion delivery
	TASK_9038(9038, 1813), // Civitas illa Fortis staff delivery
	TASK_9039(9039, 3774), // Prifddinas spice delivery
	TASK_9040(9040, 2496), // Lunar Isle crystal seed delivery
	TASK_9041(9041, 4798), // Rellekka plank delivery
	TASK_9042(9042, 2960), // Void Knights' Outpost rune delivery
	TASK_9043(9043, 3922), // Prifddinas book delivery
	TASK_9044(9044, 790), // Etceteria fish delivery
	TASK_9045(9045, 888), // Rellekka teak delivery
	TASK_9046(9046, 1149), // Neitiznot coal delivery
	TASK_9047(9047, 2299), // Rellekka rope delivery
	TASK_9048(9048, 4344), // Sunset Coast warhammer delivery
	TASK_9049(9049, 8688), // Rellekka fabric delivery
	TASK_9050(9050, 888), // Etceteria warhammer delivery
	TASK_9051(9051, 1258), // Neitiznot sand delivery
	TASK_9052(9052, 4885), // Sunset Coast fish delivery
	TASK_9053(9053, 2464), // Port Roberts fish delivery
	TASK_9054(9054, 2489), // Rellekka plank delivery
	TASK_9055(9055, 1759), // Piscatoris fur delivery
	TASK_9056(9056, 3355), // Rellekka monkfish delivery
	TASK_9057(9057, 2042), // Port Piscarilius fur delivery
	TASK_9058(9058, 4085), // Rellekka redwood delivery
	TASK_9059(9059, 1006), // Jatizso logs delivery
	TASK_9060(9060, 2155), // Rellekka adamantite delivery
	TASK_9061(9061, 4166), // Deepfin Point fur delivery
	TASK_9062(9062, 6321), // Rellekka halberd delivery
	TASK_9063(9063, 790), // Rellekka fish delivery
	TASK_9064(9064, 1437), // Etceteria iron delivery
	TASK_9065(9065, 898), // Jatizso fish delivery
	TASK_9066(9066, 1940), // Etceteria adamantite delivery
	TASK_9067(9067, 2730), // Port Roberts teak delivery
	TASK_9068(9068, 5459), // Etceteria fruit delivery
	TASK_9069(9069, 1580), // Rellekka teak delivery
	TASK_9070(9070, 1073), // Jatizso flax delivery
	TASK_9071(9071, 2886), // Port Roberts flax delivery
	TASK_9072(9072, 2335), // Port Piscarilius mahogany delivery
	TASK_9073(9073, 4813), // Etceteria plank delivery
	TASK_9074(9074, 1293), // Neitiznot mahogany delivery
	TASK_9075(9075, 2442), // Etceteria yak hair delivery
	TASK_9076(9076, 4813), // Sunset Coast fabric delivery
	TASK_9077(9077, 9626), // Etceteria sword delivery
	TASK_9078(9078, -1), // Piscatoris flax delivery
	TASK_9079(9079, -1), // Etceteria monkfish delivery
	TASK_9080(9080, 4238), // Deepfin Point mahogany delivery
	TASK_9081(9081, 6034), // Etceteria onion delivery
	TASK_9082(9082, 1841), // Port Piscarilius fabric delivery
	TASK_9083(9083, 3846), // Lunar Isle fish delivery
	TASK_9084(9084, 1432), // Piscatoris fabric delivery
	TASK_9085(9085, 2864), // Lunar Isle fur delivery
	TASK_9086(9086, 2496), // Prifddinas herb delivery
	TASK_9087(9087, 4992), // Lunar Isle potion delivery
	TASK_9088(9088, 2240), // Port Piscarilius rune delivery
	TASK_9089(9089, 1600), // Piscatoris potion delivery
	TASK_9090(9090, 2880), // Prifddinas rune delivery
	TASK_9091(9091, 4214), // Deepfin Point suqah hide delivery
	TASK_9092(9092, 8265), // Lunar Isle coal delivery
	TASK_9093(9093, 2660), // Port Roberts fabric delivery
	TASK_9094(9094, 5319), // Lunar Isle fish delivery
	TASK_9095(9095, 3437), // Civitas illa Fortis rune delivery
	TASK_9096(9096, 6710), // Lunar Isle fur delivery
	TASK_9097(9097, -1), // Red Rock rune delivery
	TASK_9098(9098, -1), // Lunar Isle red coral delivery
	TASK_9099(9099, -1), // Land's End gem delivery
	TASK_9100(9100, 11519), // Lunar Isle potion delivery
	TASK_9101(9101, 1790), // Port Sarim tern bounty
	TASK_9102(9102, 1790), // Port Sarim mogre bounty
	TASK_9103(9103, 1790), // Port Sarim bull shark bounty
	TASK_9104(9104, 6760), // Port Sarim tiger shark bounty
	TASK_9105(9105, 1790), // Port Sarim osprey bounty
	TASK_9106(9106, 1790), // Port Sarim bull shark bounty
	TASK_9107(9107, 6360), // Port Sarim pygmy kraken bounty
	TASK_9108(9108, 1790), // Pandemonium bull shark bounty
	TASK_9109(9109, 1790), // Pandemonium mogre bounty
	TASK_9110(9110, 1790), // Pandemonium osprey bounty
	TASK_9111(9111, 3400), // Pandemonium butterfly ray bounty
	TASK_9112(9112, 1790), // Pandemonium tern bounty
	TASK_9113(9113, 3400), // Pandemonium hammerhead shark bounty
	TASK_9114(9114, 4390), // Pandemonium frigatebird bounty
	TASK_9115(9115, 1790), // Musa Point bull shark bounty
	TASK_9116(9116, 1790), // Musa Point mogre bounty
	TASK_9117(9117, 1790), // Musa Point tern bounty
	TASK_9118(9118, 3400), // Musa Point butterfly ray bounty
	TASK_9119(9119, 1790), // Musa Point eagle ray bounty
	TASK_9120(9120, 1790), // Musa Point bull shark bounty
	TASK_9121(9121, 3400), // Musa Point hammerhead shark bounty
	TASK_9122(9122, 1790), // Catherby tern bounty
	TASK_9123(9123, 1790), // Catherby osprey bounty
	TASK_9124(9124, 1790), // Catherby bull shark bounty
	TASK_9125(9125, 6360), // Catherby pygmy kraken bounty
	TASK_9126(9126, 1790), // Catherby mogre bounty
	TASK_9127(9127, 4390), // Catherby frigatebird bounty
	TASK_9128(9128, 6360), // Catherby albatross bounty
	TASK_9129(9129, 1790), // Brimhaven osprey bounty
	TASK_9130(9130, 1790), // Brimhaven tern bounty
	TASK_9131(9131, 1790), // Brimhaven bull shark bounty
	TASK_9132(9132, 6360), // Brimhaven albatross bounty
	TASK_9133(9133, 3400), // Brimhaven hammerhead shark bounty
	TASK_9134(9134, 6760), // Brimhaven tiger shark bounty
	TASK_9135(9135, 1790), // Brimhaven mogre bounty
	TASK_9136(9136, 1790), // Ardougne mogre bounty
	TASK_9137(9137, 1790), // Ardougne osprey bounty
	TASK_9138(9138, 1790), // Ardougne bull shark bounty
	TASK_9139(9139, 6360), // Ardougne pygmy kraken bounty
	TASK_9140(9140, 3400), // Ardougne hammerhead shark bounty
	TASK_9141(9141, 4390), // Ardougne stingray bounty
	TASK_9142(9142, 7730), // Ardougne spined kraken bounty
	TASK_9143(9143, 1790), // Port Khazard mogre bounty
	TASK_9144(9144, 1790), // Port Khazard bull shark bounty
	TASK_9145(9145, 3400), // Port Khazard hammerhead shark bounty
	TASK_9146(9146, 8540), // Port Khazard great white shark bounty
	TASK_9147(9147, 1790), // Port Khazard mogre bounty
	TASK_9148(9148, 1790), // Port Khazard eagle ray bounty
	TASK_9149(9149, 6360), // Port Khazard albatross bounty
	TASK_9150(9150, 3400), // Corsair Cove hammerhead shark bounty
	TASK_9151(9151, 6360), // Corsair Cove pygmy kraken bounty
	TASK_9152(9152, 3400), // Corsair Cove mogre bounty
	TASK_9153(9153, 4390), // Corsair Cove stingray bounty
	TASK_9154(9154, 3400), // Corsair Cove osprey bounty
	TASK_9155(9155, 6760), // Corsair Cove tiger shark bounty
	TASK_9156(9156, 8540), // Corsair Cove great white shark bounty
	TASK_9157(9157, 3650), // Ruins of Unkah bull shark bounty
	TASK_9158(9158, 3650), // Ruins of Unkah hammerhead shark bounty
	TASK_9159(9159, 6760), // Ruins of Unkah tiger shark bounty
	TASK_9160(9160, 3650), // Ruins of Unkah butterfly ray bounty
	TASK_9161(9161, 3650), // Ruins of Unkah eagle ray bounty
	TASK_9162(9162, 6360), // Ruins of Unkah albatross bounty
	TASK_9163(9163, 8540), // Ruins of Unkah great white shark bounty
	TASK_9164(9164, 4390), // Void Knights' Outpost stingray bounty
	TASK_9165(9165, 4390), // Void Knights' Outpost osprey bounty
	TASK_9166(9166, 4390), // Void Knights' Outpost frigatebird bounty
	TASK_9167(9167, 6360), // Void Knights' Outpost pygmy kraken bounty
	TASK_9168(9168, 4390), // Void Knights' Outpost eagle ray bounty
	TASK_9169(9169, 8540), // Void Knights' Outpost great white shark bounty
	TASK_9170(9170, 7500), // Void Knights' Outpost manta ray bounty
	TASK_9171(9171, 3650), // Summer Shore butterfly ray bounty
	TASK_9172(9172, 3650), // Summer Shore eagle ray bounty
	TASK_9173(9173, 4390), // Summer Shore frigatebird bounty
	TASK_9174(9174, 8540), // Summer Shore great white shark bounty
	TASK_9175(9175, 6360), // Summer Shore albatross bounty
	TASK_9176(9176, 6760), // Summer Shore tiger shark bounty
	TASK_9177(9177, 4390), // Summer Shore stingray bounty
	TASK_9178(9178, -1), // Red Rock tiger shark bounty
	TASK_9179(9179, -1), // Red Rock frigatebird bounty
	TASK_9180(9180, -1), // Red Rock eagle ray bounty
	TASK_9181(9181, -1), // Red Rock great white shark bounty
	TASK_9182(9182, -1), // Red Rock stingray bounty
	TASK_9183(9183, -1), // Red Rock tiger shark bounty
	TASK_9184(9184, -1), // Red Rock spined kraken bounty
	TASK_9185(9185, 1790), // Land's End bull shark bounty
	TASK_9186(9186, 1790), // Land's End osprey bounty
	TASK_9187(9187, 1790), // Land's End tern bounty
	TASK_9188(9188, 6360), // Land's End pygmy kraken bounty
	TASK_9189(9189, 3400), // Land's End hammerhead shark bounty
	TASK_9190(9190, 8540), // Land's End great white shark bounty
	TASK_9191(9191, 3400), // Land's End butterfly ray bounty
	TASK_9192(9192, 1790), // Port Piscarilius osprey bounty
	TASK_9193(9193, 1790), // Port Piscarilius bull shark bounty
	TASK_9194(9194, 1790), // Port Piscarilius tern bounty
	TASK_9195(9195, 7730), // Port Piscarilius spined kraken bounty
	TASK_9196(9196, 6760), // Port Piscarilius tiger shark bounty
	TASK_9197(9197, 3400), // Port Piscarilius butterfly ray bounty
	TASK_9198(9198, 9540), // Port Piscarilius armoured kraken bounty
	TASK_9199(9199, 2020), // Civitas illa Fortis osprey bounty
	TASK_9200(9200, 3400), // Civitas illa Fortis hammerhead shark bounty
	TASK_9201(9201, 2020), // Civitas illa Fortis bull shark bounty
	TASK_9202(9202, 9540), // Civitas illa Fortis armoured kraken bounty
	TASK_9203(9203, 2020), // Civitas illa Fortis tern bounty
	TASK_9204(9204, 4390), // Civitas illa Fortis frigatebird bounty
	TASK_9205(9205, 2020), // Civitas illa Fortis eagle ray bounty
	TASK_9206(9206, 4390), // Aldarin stingray bounty
	TASK_9207(9207, 3650), // Aldarin osprey bounty
	TASK_9208(9208, 3650), // Aldarin butterfly ray bounty
	TASK_9209(9209, 8540), // Aldarin great white shark bounty
	TASK_9210(9210, 3650), // Aldarin hammerhead shark bounty
	TASK_9211(9211, 7730), // Aldarin spined kraken bounty
	TASK_9212(9212, 4390), // Aldarin frigatebird bounty
	TASK_9213(9213, 4390), // Port Roberts hammerhead shark bounty
	TASK_9214(9214, 4390), // Port Roberts osprey bounty
	TASK_9215(9215, 9540), // Port Roberts armoured kraken bounty
	TASK_9216(9216, 8540), // Port Roberts great white shark bounty
	TASK_9217(9217, 4390), // Port Roberts tern bounty
	TASK_9218(9218, 9540), // Port Roberts orca bounty
	TASK_9219(9219, 6360), // Port Roberts albatross bounty
	TASK_9220(9220, 7500), // Deepfin Point frigatebird bounty
	TASK_9221(9221, 7500), // Deepfin Point eagle ray bounty
	TASK_9222(9222, 7730), // Deepfin Point spined kraken bounty
	TASK_9223(9223, 7500), // Deepfin Point osprey bounty
	TASK_9224(9224, 7500), // Deepfin Point stingray bounty
	TASK_9225(9225, 7500), // Deepfin Point tiger shark bounty
	TASK_9226(9226, 7500), // Deepfin Point hammerhead shark bounty
	TASK_9227(9227, 7500), // Port Tyras butterfly ray bounty
	TASK_9228(9228, 7500), // Port Tyras hammerhead shark bounty
	TASK_9229(9229, 7500), // Port Tyras bull shark bounty
	TASK_9230(9230, 9540), // Port Tyras armoured kraken bounty
	TASK_9231(9231, 7500), // Port Tyras pygmy kraken bounty
	TASK_9232(9232, 8540), // Port Tyras great white shark bounty
	TASK_9233(9233, 7500), // Port Tyras manta ray bounty
	TASK_9234(9234, 7730), // Prifddinas hammerhead shark bounty
	TASK_9235(9235, 7730), // Prifddinas pygmy kraken bounty
	TASK_9236(9236, 9540), // Prifddinas armoured kraken bounty
	TASK_9237(9237, 7730), // Prifddinas osprey bounty
	TASK_9238(9238, 8540), // Prifddinas great white shark bounty
	TASK_9239(9239, 7730), // Prifddinas manta ray bounty
	TASK_9240(9240, 7730), // Prifddinas hammerhead shark bounty
	TASK_9241(9241, 6760), // Rellekka albatross bounty
	TASK_9242(9242, 6760), // Rellekka hammerhead shark bounty
	TASK_9243(9243, 6760), // Rellekka narwhal bounty
	TASK_9244(9244, 9540), // Rellekka armoured kraken bounty
	TASK_9245(9245, 9540), // Rellekka orca bounty
	TASK_9246(9246, 9540), // Rellekka vampyre kraken bounty
	TASK_9247(9247, 7730), // Rellekka spined kraken bounty
	TASK_9248(9248, 7500), // Etceteria albatross bounty
	TASK_9249(9249, 9540), // Etceteria orca bounty
	TASK_9250(9250, 9540), // Etceteria vampyre kraken bounty
	TASK_9251(9251, 7500), // Etceteria narwhal bounty
	TASK_9252(9252, 7730), // Etceteria spined kraken bounty
	TASK_9253(9253, 9540), // Etceteria orca bounty
	TASK_9254(9254, 9540), // Etceteria armoured kraken bounty
	TASK_9255(9255, 9540), // Lunar Isle armoured kraken bounty
	TASK_9256(9256, 9540), // Lunar Isle vampyre kraken bounty
	TASK_9257(9257, 8450), // Lunar Isle narwhal bounty
	TASK_9258(9258, 9540), // Lunar Isle orca bounty
	TASK_9259(9259, 8450), // Lunar Isle albatross bounty
	TASK_9260(9260, 9540), // Lunar Isle vampyre kraken bounty
	TASK_9261(9261, 8450), // Lunar Isle narwhal bounty
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
