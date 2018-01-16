package com.rs.core.utils.npc;

import com.rs.world.World;
import com.rs.world.WorldObject;
import com.rs.world.WorldTile;

public class NPCSpawning {

    /**
     * Contains the custom npc spawning
     */

    public static void spawnNPCS() {

        // tarn
        World.deleteObject(new WorldTile(3200, 5487, 0));
        World.deleteObject(new WorldTile(3206, 5487, 0));
        World.deleteObject(new WorldTile(3206, 5493, 0));
        World.deleteObject(new WorldTile(3200, 5493, 0));

        // newest home
        World.deleteObject(new WorldTile(4381, 5933, 0));
        World.deleteObject(new WorldTile(3002, 9684, 0));
        World.deleteObject(new WorldTile(4571, 5092, 0));
        World.deleteObject(new WorldTile(4382, 5933, 0));
        World.deleteObject(new WorldTile(4384, 5931, 0));
        World.deleteObject(new WorldTile(3002, 9677, 0));
        World.deleteObject(new WorldTile(4379, 5931, 0));
        World.deleteObject(new WorldTile(4382, 5936, 0));
        World.deleteObject(new WorldTile(4381, 5936, 0));
        World.deleteObject(new WorldTile(4381, 5937, 0));
        World.deleteObject(new WorldTile(4382, 5937, 0));
        World.deleteObject(new WorldTile(4384, 5940, 0));
        World.deleteObject(new WorldTile(4383, 5939, 0));
        World.deleteObject(new WorldTile(4379, 5936, 0));
        World.deleteObject(new WorldTile(4384, 5936, 0));
        World.deleteObject(new WorldTile(4384, 5937, 0));
        World.deleteObject(new WorldTile(4384, 5938, 0));
        World.deleteObject(new WorldTile(4384, 5939, 0));
        World.deleteObject(new WorldTile(2985, 4383, 0));
        World.deleteObject(new WorldTile(4380, 5941, 0));
        World.deleteObject(new WorldTile(4385, 5941, 0));
        World.deleteObject(new WorldTile(4384, 5941, 0));
        World.deleteObject(new WorldTile(4385, 5940, 0));
        World.spawnNPC(9434, new WorldTile(4378, 5918, 0), -2, false); // wizard
        // teleport
        World.spawnNPC(494, new WorldTile(2976, 4384, 0), -2, false); // corp
        // banker
        World.spawnNPC(7892, new WorldTile(4386, 5918, 0), -2, false); // prestige
        World.spawnNPC(7909, new WorldTile(2994, 9677, 0), -2, false); // reflection
        // guide
        World.spawnNPC(15532, new WorldTile(4385, 5930, 0), -1, false); // ShopData
        World.spawnNPC(15533, new WorldTile(4384, 5930, 0), -1, false); // shop1
        World.spawnNPC(15534, new WorldTile(4383, 5930, 0), -1, false); // shop2
        World.spawnNPC(15548, new WorldTile(4385, 5931, 0), 0, false); // levelup
        // ShopData
        World.spawnNPC(13955, new WorldTile(4385, 5932, 0), 0, false); // the
        // raptor
        // pkp
        World.spawnNPC(7935, new WorldTile(4384, 5920, 0), 0, false); // hans
        World.spawnObject(new WorldObject(12356, 10, 2, 4383, 5922, 0), true); // RFD
        // portal
        World.spawnObject(new WorldObject(409, 10, 1, 4375, 5912, 0), true);
        World.spawnObject(new WorldObject(411, 10, 1, 4375, 5910, 0), true);
        World.spawnObject(new WorldObject(2562, 10, 2, 4378, 5930, 0), true);
        World.spawnObject(new WorldObject(4708, 10, 2, 3296, 4512, 0), true);
        World.spawnObject(new WorldObject(66114, 10, 2, 3561, 3298, 0), true);
        World.spawnNPC(545, new WorldTile(3152, 5712, 0), 0, false); // f2p ShopData
        World.spawnNPC(569, new WorldTile(3153, 5712, 0), 0, false); // iron man
        // mode
        // ShopData
        World.spawnObject(new WorldObject(29945, 10, 0, 4375, 5909, 0), true);
        World.spawnObject(new WorldObject(2644, 10, 0, 2736, 3447, 0), true);
        World.spawnObject(new WorldObject(12963, 10, 0, 2486, 3421, 3), true);
        World.spawnNPC(3705, new WorldTile(4379, 5930, 0), -1, false); // max
        World.spawnNPC(528, new WorldTile(2986, 9675, 0), -1, false); // max
        World.spawnNPC(542, new WorldTile(3150, 5700, 0), -1, false); // outfit
        // ShopData
        World.spawnNPC(15501, new WorldTile(2736, 3556, 0), -1, false); // supression
        // quest

        World.spawnNPC(13926, new WorldTile(3001, 9686, 0), -1, false); // pvp
        // guild

        // newest dzone combined
        World.spawnNPC(9634, new WorldTile(3228, 1501, 0), -1, false);
        // World.spawnNPC(14, new WorldTile(1, 1, 0), -1, false);
        World.spawnNPC(568, new WorldTile(3227, 1501, 0), 0, true); // vip ShopData
        World.spawnNPC(6892, new WorldTile(3226, 1501, 0), -1, false); // Petshop
        World.spawnNPC(1918, new WorldTile(3225, 1501, 0), -1, false); // Mandrith
        World.spawnObject(new WorldObject(36786, 10, 2, 3224, 1501, 0), true);
        World.spawnObject(new WorldObject(36786, 10, 2, 3223, 1501, 0), true);
        World.spawnObject(new WorldObject(36786, 10, 2, 3222, 1501, 0), true);
        World.spawnObject(new WorldObject(36786, 10, 2, 3221, 1501, 0), true);
        World.spawnNPC(14854, new WorldTile(3220, 1501, 0), -1, false); // Kaqemeex
        World.spawnNPC(1576, new WorldTile(3219, 1501, 0), 0, true);
        World.spawnNPC(14722, new WorldTile(3218, 1501, 0), 0, true);
        World.spawnNPC(15612, new WorldTile(3217, 1501, 0), 0, true);
        World.spawnNPC(925, new WorldTile(2730, 3556, 0), 0, true);

        World.spawnObject(new WorldObject(14860, 10, -3, 3217, 1513, 0), true);
        World.spawnObject(new WorldObject(14860, 10, -3, 3218, 1513, 0), true);
        World.spawnObject(new WorldObject(14860, 10, -3, 3219, 1513, 0), true);
        World.spawnObject(new WorldObject(14860, 10, -3, 3220, 1513, 0), true);
        World.spawnObject(new WorldObject(14860, 10, -3, 3221, 1513, 0), true);
        World.spawnObject(new WorldObject(14860, 10, -3, 3222, 1513, 0), true);
        World.spawnObject(new WorldObject(14860, 10, -3, 3223, 1513, 0), true);
        World.spawnObject(new WorldObject(14860, 10, -3, 3224, 1513, 0), true);
        World.spawnObject(new WorldObject(14860, 10, -3, 3225, 1513, 0), true);
        World.spawnObject(new WorldObject(14860, 10, -3, 3226, 1513, 0), true);
        World.spawnObject(new WorldObject(14860, 10, -3, 3227, 1513, 0), true);
        World.spawnObject(new WorldObject(14860, 10, -3, 3228, 1513, 0), true);
        World.spawnObject(new WorldObject(14860, 10, -3, 3229, 1513, 0), true);

        World.spawnObject(new WorldObject(1, 10, 0, 1968, 3245, 0), true); // avatar
        // safespot
        // boxes
        World.spawnObject(new WorldObject(1, 10, 0, 1967, 3245, 0), true);
        World.spawnObject(new WorldObject(1, 10, 0, 1966, 3245, 0), true);
        World.spawnObject(new WorldObject(1, 10, 0, 1972, 3251, 0), true);
        World.spawnObject(new WorldObject(1, 10, 0, 1972, 3252, 0), true);
        World.spawnObject(new WorldObject(1, 10, 0, 1972, 3253, 0), true);
        World.spawnObject(new WorldObject(1, 10, 0, 1972, 3254, 0), true);
        World.spawnObject(new WorldObject(1, 10, 0, 1972, 3255, 0), true);

        World.spawnObject(new WorldObject(70757, 10, 0, 3223, 1508, 0), true);
        World.spawnObject(new WorldObject(14921, 10, 1, 3217, 1504, 0), true);

        World.spawnObject(new WorldObject(1306, 10, 0, 3229, 1507, 0), true);
        World.spawnObject(new WorldObject(1306, 10, 0, 3217, 1507, 0), true);
        // end of newest dzone

        // Hunter ShopData
        World.spawnNPC(5112, new WorldTile(2525, 2915, 0), -1, false);

        // color changer and amazing donator ShopData
        World.spawnNPC(9634, new WorldTile(1605, 4509, 0), -1, false);
        World.spawnNPC(14, new WorldTile(1, 1, 0), -1, false);

        // construction
        World.spawnNPC(4250, new WorldTile(3320, 3502, 0), -1, false);
        World.spawnNPC(6715, new WorldTile(3321, 3502, 0), -1, false);
        World.spawnNPC(494, new WorldTile(3322, 3502, 0), -1, false);

        // livid farm
        World.spawnNPC(7530, new WorldTile(2109, 3943, 0), -1, false);
        World.spawnNPC(494, new WorldTile(2116, 3952, 0), -1, false);

        // quests and minigames
        World.spawnNPC(1430, new WorldTile(2731, 3555, 0), -1, false);
        World.spawnNPC(13698, new WorldTile(2734, 3555, 0), -1, false);
        World.spawnNPC(5566, new WorldTile(2735, 3555, 0), -1, false);

        // Max ShopData
        World.spawnNPC(3705, new WorldTile(3001, 9687, 0), -1, false);
        World.spawnNPC(3373, new WorldTile(2419, 2824, 0), -1, false);

        // troll invasion
        World.spawnNPC(13698, new WorldTile(4378, 5936, 0), -1, false);

        // Server Guide
        World.spawnNPC(13927, new WorldTile(2412, 2828, 0), -1, false);

        // Npcs and Objects made on 5/23/2013
        World.spawnObject(new WorldObject(13715, 10, 2, 1, 1, 0), true); // Nex
        // item
        // repairing
        World.spawnNPC(5566, new WorldTile(4378, 5935, -1), 0, true); // new
        // minigame
        World.spawnObject(new WorldObject(1, 10, 2, 2342, 4321, 0), true);
        World.spawnObject(new WorldObject(1, 10, 2, 2343, 4321, 0), true);
        World.deleteObject(new WorldTile(3182, 5706, 0));
        World.deleteObject(new WorldTile(3182, 5705, 0));
        World.deleteObject(new WorldTile(3181, 5706, 0));
        World.deleteObject(new WorldTile(3183, 5706, 0));
        World.deleteObject(new WorldTile(3182, 5707, 0));

        // Mr Ex
        World.spawnNPC(9434, new WorldTile(2993, 9684, 0), -1, false);

        // farming minion
        World.spawnNPC(418, new WorldTile(3053, 3304, 0), -1, false);

        // cook's quest
        World.spawnNPC(278, new WorldTile(3210, 3213, 0), -1, false);
        World.spawnNPC(758, new WorldTile(3189, 3273, 0), -1, false);
        World.spawnNPC(3807, new WorldTile(3256, 3274, 0), -1, false);

        // quest Korasi
        World.spawnNPC(15019, new WorldTile(2960, 3374, 0), -1, false);
        World.spawnNPC(494, new WorldTile(4514, 5589, 0), -1, false);
        World.spawnNPC(6334, new WorldTile(2732, 3555, 0), 2, false);
        World.spawnNPC(6361, new WorldTile(2733, 3555, 0), 2, false);
        World.deleteObject(new WorldTile(4511, 5607, 0));

        // Clanwars Pk ShopData
        World.spawnNPC(13955, new WorldTile(2994, 9684, 0), -1, false);

        // dragon hunter ShopData
        World.spawnNPC(943, new WorldTile(3420, 5276, 0), 0, true);

        // Polypore Dungeon ShopData
        World.spawnNPC(875, new WorldTile(4724, 5466, 0), -1, false);

        // 742 ShopData
        World.spawnNPC(539, new WorldTile(1, 1, 0), -1, false);

        // island spawns
        World.spawnNPC(918, new WorldTile(1, 1, 0), 0, true); // living sailor
        World.spawnNPC(14343, new WorldTile(3803, 2844, 0), 0, true); // criple
        // dude
        // for
        // barrelchest
        World.spawnNPC(1701, new WorldTile(3787, 2824, 0), 0, true); // ghost
        // farmer
        World.spawnNPC(13212, new WorldTile(3803, 2849, 0), 0, true); // monkey
        // pickpocket
        World.spawnNPC(1703, new WorldTile(3794, 2872, 0), 0, true); // death
        // sailor
        World.spawnObject(new WorldObject(29577, 10, 2, 3833, 2837, 0), true); // treasure
        // chest

        // lucien's mystery
        World.spawnNPC(5566, new WorldTile(4378, 5935, 0), 0, true);

        // noticeboard
        World.spawnObject(new WorldObject(40760, 10, 2, 2999, 9670, 0), true); // treasure
        // chest

        // Xtreme NPC
        World.spawnNPC(1576, new WorldTile(1605, 4504, 0), 0, true);

        // Corporeal ShopData
        World.spawnNPC(13191, new WorldTile(2959, 4382, 2), -1, false);

        // other
        World.spawnObject(new WorldObject(29589, 10, 2, 3553, 9703, 0), true);
        World.spawnObject(new WorldObject(4390, 10, 2, 2375, 9487, 0), true);
        World.spawnNPC(1835, new WorldTile(2512, 5357, 0), -1, false);
        // World.spawnNPC(3805, new WorldTile(3222, 3218, 0), -1, true);
        World.spawnNPC(12, new WorldTile(3002, 9682, 0), -1, true);
        World.spawnNPC(494, new WorldTile(2904, 5205, 0), -1, true);
        World.spawnNPC(1526, new WorldTile(2441, 3089, 0), -1, true);
        World.spawnObject(new WorldObject(1, 10, 2, 3305, 3133, 0), true);
        World.spawnObject(new WorldObject(1, 10, 2, 3304, 3133, 0), true);
        World.spawnObject(new WorldObject(29945, 10, 2, 2990, 9688, 0), true); // small
        // obelisk
        World.spawnNPC(14629, new WorldTile(3309, 3126, 0), 0, true);
        World.spawnNPC(2274, new WorldTile(3298, 3123, 0), 0, true);
        World.spawnNPC(21, new WorldTile(3304, 3126, 0), 0, true);
        World.spawnNPC(589, new WorldTile(3149, 5700, 0), 0, true); // flag ShopData
        World.spawnNPC(589, new WorldTile(2404, 2831, 0), 0, true); // flag ShopData

		/* Start of new home */
        /**************************************************************************/
        /**************************************************************************/
        World.spawnObject(new WorldObject(-1, 0, 0, 3671, 2976, 0), false);
        // Bankers
        World.spawnNPC(495, new WorldTile(3050, 3304, 0), -1, false, false);
        World.spawnNPC(15548, new WorldTile(2984, 9683, 0), -1, false, false); // lvl
        // ShopData
        World.spawnNPC(590, new WorldTile(2401, 2831, 0), -1, false, false);
        World.spawnNPC(495, new WorldTile(2518, 10254, 0), -1, false, false);
        World.spawnNPC(494, new WorldTile(2519, 10254, 0), -1, false, false);
        World.spawnNPC(494, new WorldTile(3682, 2983, 0), -1, false, false);
        // dung ShopData
        World.spawnNPC(9711, new WorldTile(206, 5016, 0), -1, false, false);
        // estate ShopData
        World.spawnNPC(6715, new WorldTile(3148, 5700, 0), -1, false, false);
        World.spawnNPC(6715, new WorldTile(2405, 2831, 0), -1, false, false);
        // postiepete
        // World.spawnNPC(3805, new WorldTile(3324, 3218, 0), -1, false);
        // Kurdal
        World.spawnNPC(9085, new WorldTile(2991, 9680, 0), 2, false, false);
        World.spawnNPC(9085, new WorldTile(4380, 5930, 0), 2, false, false);
        // Xuan
        World.spawnNPC(13727, new WorldTile(2994, 9688, 0), 2, false, false);
        World.spawnNPC(13727, new WorldTile(4385, 5933, 0), 2, false, false);
        // Ghost
        World.spawnNPC(457, new WorldTile(3150, 5712, 0), 2, false);
        World.spawnNPC(457, new WorldTile(2405, 2824, 0), 2, false);
        // WiseOldMan
        // World.spawnNPC(3820, new WorldTile(3152, 5712, 0), 0, false);
        // World.spawnNPC(3820, new WorldTile(1, 1, 0), 0, false);
        // Ajjat
        World.spawnNPC(4288, new WorldTile(3149, 5712, 0), 2, false);
        World.spawnNPC(4288, new WorldTile(2404, 2824, 0), 2, false);
        // Tamayu
        World.spawnNPC(1167, new WorldTile(3148, 5712, 0), 2, false);
        World.spawnNPC(1167, new WorldTile(2403, 2824, 0), 2, false);
        // Lowe
        World.spawnNPC(550, new WorldTile(3144, 5712, 0), 2, false);
        World.spawnNPC(550, new WorldTile(2400, 2824, 0), 2, false);
        // Zaff
        World.spawnNPC(546, new WorldTile(3145, 5712, 0), 2, false);
        World.spawnNPC(546, new WorldTile(2401, 2824, 0), 2, false);
        // Horvik
        World.spawnNPC(549, new WorldTile(3143, 5712, 0), 2, false);
        World.spawnNPC(549, new WorldTile(2402, 2824, 0), 2, false);
        // Hank
        World.spawnNPC(8864, new WorldTile(3152, 5700, 0), 0, false);
        World.spawnNPC(8864, new WorldTile(2406, 2829, 0), 0, false);
        // Harry
        World.spawnNPC(576, new WorldTile(3142, 5712, 0), 0, false);
        World.spawnNPC(576, new WorldTile(2406, 2830, 0), 0, false);
        // Peska
        World.spawnNPC(538, new WorldTile(3147, 5712, 0), 0, false);
        World.spawnNPC(538, new WorldTile(2399, 2824, 0), 0, false);
        // Bob
        World.spawnNPC(519, new WorldTile(3144, 5700, 0), 0, false);
        World.spawnNPC(519, new WorldTile(2406, 2828, 0), 0, false);
        // Jatix
        World.spawnNPC(587, new WorldTile(3145, 5700, 0), 0, false);
        World.spawnNPC(587, new WorldTile(2406, 2827, 0), 0, false);
        // Rommik
        World.spawnNPC(585, new WorldTile(3147, 5700, 0), 0, false);
        World.spawnNPC(585, new WorldTile(2406, 2831, 0), 0, false);
        // GeneralStore
        World.spawnNPC(529, new WorldTile(4385, 5936, 0), 0, false);
        World.spawnNPC(529, new WorldTile(2528, 3377, 0), 0, false);
        // Hairdresser
        World.spawnNPC(598, new WorldTile(3143, 5700, 0), 0, false);
        World.spawnNPC(598, new WorldTile(4385, 5935, 0), 0, false);
        // Thessalia
        World.spawnNPC(548, new WorldTile(3142, 5700, 0), 0, false);
        World.spawnNPC(548, new WorldTile(4385, 5934, 0), 0, false);
        // Object spawning
        World.spawnObject(new WorldObject(47120, 10, -1, 2982, 9675, 0), true);
        World.spawnObject(new WorldObject(409, 10, 2, 2996, 9689, 0), true);
        World.spawnObject(new WorldObject(2562, 10, 2, 3000, 9688, 0), true);
        World.spawnObject(new WorldObject(2079, 10, 1, 3002, 9681, 0), true);
        World.spawnObject(new WorldObject(2079, 10, -1, 4380, 5922, 0), true);
        World.spawnObject(new WorldObject(6282, 10, -2, 4381, 5907, 0), true);
        World.spawnObject(new WorldObject(66007, 10, 0, 2679, 2660, 0), true);
        World.spawnObject(new WorldObject(1, 10, 0, 2981, 4392, 2), true);
        World.spawnObject(new WorldObject(1, 10, 0, 2989, 4395, 2), true);
        World.spawnObject(new WorldObject(1, 10, 0, 2989, 4395, 2), true);
        World.spawnObject(new WorldObject(1, 10, 0, 2988, 4371, 2), true);
        World.spawnObject(new WorldObject(1, 10, 0, 2987, 4371, 2), true);
        World.spawnObject(new WorldObject(1, 10, 0, 2986, 4371, 2), true);
        World.spawnObject(new WorldObject(1, 10, 0, 2985, 4371, 2), true);
        World.spawnObject(new WorldObject(1, 10, 0, 2984, 4371, 2), true);
        World.spawnObject(new WorldObject(1, 10, 0, 2988, 4371, 2), true);
        // XuanTent
        World.spawnObject(new WorldObject(11448, 10, -1, 2407, 2841, 0), true);
        // Thieving stalls
        World.spawnObject(new WorldObject(4874, 10, 1, 2983, 9672, 0), true);
        World.spawnObject(new WorldObject(4874, 10, 2, 2533, 3377, 0), true);
        World.spawnObject(new WorldObject(4875, 10, 1, 2984, 9672, 0), true);
        World.spawnObject(new WorldObject(4875, 10, 2, 2532, 3377, 0), true);
        World.spawnObject(new WorldObject(4876, 10, 1, 2985, 9672, 0), true);
        World.spawnObject(new WorldObject(4876, 10, 2, 2531, 3377, 0), true);
        World.spawnObject(new WorldObject(4877, 10, 1, 2986, 9672, 0), true);
        World.spawnObject(new WorldObject(4877, 10, 2, 2530, 3377, 0), true);
        World.spawnObject(new WorldObject(4878, 10, 1, 2987, 9672, 0), true);
        World.spawnObject(new WorldObject(4878, 10, 2, 2529, 3377, 0), true);
        /**************************************************************************/
        /**************************************************************************/
        /* End of new Home */

        // Tzhaar
        World.spawnNPC(2625, new WorldTile(2617, 5132, 0), -1, false);
        World.spawnNPC(2614, new WorldTile(4666, 5082, 0), -1, false);

        // Donator Zone NPCS
        World.spawnNPC(6892, new WorldTile(1605, 4508, 0), -1, false); // Petshop
        World.spawnNPC(1918, new WorldTile(1605, 4506, 0), -1, false); // Mandrith
        World.spawnNPC(14854, new WorldTile(1605, 4505, 0), -1, false); // Kaqemeex

        // Donator Zone Objects A.k.A Banks
        World.spawnObject(new WorldObject(36786, 10, 2, 4455, 4528, 0), true);
        World.spawnObject(new WorldObject(36786, 10, 2, 4456, 4528, 0), true);
        World.spawnObject(new WorldObject(36786, 10, 2, 4457, 4528, 0), true);
        World.spawnObject(new WorldObject(36786, 10, 2, 4458, 4528, 0), true);
        World.spawnObject(new WorldObject(36786, 10, 2, 4459, 4528, 0), true);
        World.spawnObject(new WorldObject(36786, 10, 2, 4460, 4528, 0), true);
        World.spawnObject(new WorldObject(36786, 10, 2, 4461, 4528, 0), true);
        World.spawnObject(new WorldObject(36786, 10, 2, 4462, 4528, 0), true);
        World.spawnObject(new WorldObject(36786, 10, 2, 4462, 4512, 0), true);
        World.spawnObject(new WorldObject(36786, 10, 2, 4461, 4512, 0), true);
        World.spawnObject(new WorldObject(36786, 10, 2, 4460, 4512, 0), true);
        World.spawnObject(new WorldObject(12356, 10, 2, 2988, 9684, 0), true);// rfd
        World.spawnObject(new WorldObject(12356, 10, 2, 2425, 2831, 0), true);// rfd2
        World.spawnObject(new WorldObject(36786, 10, 2, 4459, 4512, 0), true);
        World.spawnObject(new WorldObject(36786, 10, 2, 4459, 4512, 0), true);
        World.spawnObject(new WorldObject(3827, 10, 2, 1610, 4500, 0), true);
        World.spawnObject(new WorldObject(36786, 10, 2, 4457, 4512, 0), true);
        World.spawnObject(new WorldObject(36786, 10, 2, 4456, 4512, 0), true);
        World.spawnObject(new WorldObject(36786, 10, 2, 4455, 4512, 0), true);
        World.spawnObject(new WorldObject(36786, 10, 2, 2967, 4379, 2), true);
        // runite ores
        World.spawnObject(new WorldObject(11010, 10, -3, 3296, 3297, 0), true);
        World.spawnObject(new WorldObject(14859, 10, -3, 1595, 4505, 0), true);
        World.spawnObject(new WorldObject(14859, 10, -3, 1595, 4506, 0), true);
        World.spawnObject(new WorldObject(14859, 10, -3, 1595, 4507, 0), true);
        World.spawnObject(new WorldObject(14859, 10, -3, 1595, 4508, 0), true);
        World.spawnObject(new WorldObject(14859, 10, -3, 1595, 4509, 0), true);
        World.spawnObject(new WorldObject(14859, 10, -3, 2602, 9561, 0), true);
        // magic tree
        World.spawnObject(new WorldObject(1306, 10, 0, 1595, 4503, 0), true);
        World.spawnObject(new WorldObject(1306, 10, 0, 1595, 4510, 0), true);
        // Furnace and Anvil
        World.spawnObject(new WorldObject(11010, 10, -4, 1599, 4512, 0), true);
        World.spawnObject(new WorldObject(2783, 10, -4, 3217, 1503, 0), true);
        // Thieving stall
        // World.spawnObject(new WorldObject(34385, 10, 0, 4464, 4524, 0),
        // true);
        World.spawnObject(new WorldObject(2090, 10, 0, 2596, 9553, 0), true);
        World.spawnObject(new WorldObject(2090, 10, 0, 2597, 9553, 0), true);
        World.spawnObject(new WorldObject(2094, 10, 0, 2598, 9553, 0), true);
        World.spawnObject(new WorldObject(2094, 10, 0, 2599, 9553, 0), true);
        World.spawnObject(new WorldObject(2092, 10, 0, 2600, 9553, 0), true);
        World.spawnObject(new WorldObject(2092, 10, 0, 2601, 9553, 0), true);
        World.spawnObject(new WorldObject(2098, 10, 0, 2602, 9553, 0), true);
        World.spawnObject(new WorldObject(2098, 10, 0, 2603, 9553, 0), true);
        World.spawnObject(new WorldObject(2096, 10, 0, 2602, 9559, 0), true);
        World.spawnObject(new WorldObject(2096, 10, 0, 2602, 9560, 0), true);
        World.spawnObject(new WorldObject(2100, 10, 0, 2603, 9554, 0), true);
        World.spawnObject(new WorldObject(2100, 10, 0, 2603, 9555, 0), true);
        World.spawnObject(new WorldObject(2102, 10, 0, 2603, 9556, 0), true);
        World.spawnObject(new WorldObject(2102, 10, 0, 2602, 9556, 0), true);
        World.spawnObject(new WorldObject(2104, 10, 0, 2602, 9557, 0), true);
        World.spawnObject(new WorldObject(2104, 10, 0, 2602, 9558, 0), true);

        // Fishing SPOT spawnings!
        World.spawnNPC(327, new WorldTile(2604, 3419, 0), -1, true, true);
        World.spawnNPC(6267, new WorldTile(2605, 3420, 0), -1, true, true);
        World.spawnNPC(312, new WorldTile(2605, 3421, 0), -1, true, true);
        World.spawnNPC(313, new WorldTile(2604, 3422, 0), -1, true, true);
        World.spawnNPC(952, new WorldTile(2603, 3422, 0), -1, true, true);
        World.spawnNPC(327, new WorldTile(2604, 3423, 0), -1, true, true);
        World.spawnNPC(6267, new WorldTile(2605, 3424, 0), -1, true, true);
        World.spawnNPC(312, new WorldTile(2605, 3425, 0), -1, true, true);
        World.spawnNPC(327, new WorldTile(2599, 3419, 0), -1, true, true);
        World.spawnNPC(6267, new WorldTile(2598, 3422, 0), -1, true, true);
        World.spawnNPC(8864, new WorldTile(2590, 3419, 0), -1, true, true);

        // Fishing spot
        World.spawnObject(new WorldObject(36786, 10, 2, 2587, 3422, 0), true);

        // Runecrafting skill ALTARS + NPC's + Banks
        World.spawnObject(new WorldObject(2478, 10, -2, 2600, 3155, 0), true);// 1-Air
        // altar
        World.spawnObject(new WorldObject(2479, 10, -2, 2597, 3155, 0), true);// 2-Mind
        // altar
        World.spawnObject(new WorldObject(2480, 10, -2, 2594, 3157, 0), true);// 3-Water
        // altar
        World.spawnObject(new WorldObject(2481, 10, -2, 2594, 3160, 0), true);// 4-Earth
        // altar
        World.spawnObject(new WorldObject(2482, 10, -2, 2594, 3163, 0), true);// 5-Fire
        // altar
        World.spawnObject(new WorldObject(2483, 10, -2, 2594, 3166, 0), true);// 6-Body
        // altar
        World.spawnObject(new WorldObject(2484, 10, -2, 2603, 3157, 0), true);// 7-Cosmic
        // altar
        World.spawnObject(new WorldObject(2487, 10, -2, 2603, 3160, 0), true);// 8-Chaos
        // altar
        World.spawnObject(new WorldObject(17010, 10, -2, 2603, 3163, 0), true);// 9-Astral
        // altar
        World.spawnObject(new WorldObject(2486, 10, -2, 2603, 3166, 0), true);// 10-Nature
        // altar
        World.spawnObject(new WorldObject(2485, 10, -2, 2600, 3168, 0), true);// 11-Law
        // altar
        World.spawnObject(new WorldObject(2488, 10, -2, 2597, 3168, 0), true);// 12-Death
        // altar
        World.spawnObject(new WorldObject(30624, 10, -2, 2510, 3169, 0), true);// 13-Blood
        // altar
        World.spawnObject(new WorldObject(2489, 10, -2, 2513, 3169, 0), true);// 14-Soul
        // altar
        World.spawnObject(new WorldObject(27663, 10, -1, 2599, 3165, 0), true);// Bank
        // chest
        World.spawnObject(new WorldObject(27663, 10, -3, 2600, 3165, 0), true);// Bank
        // chest
        World.spawnObject(new WorldObject(27663, 10, -1, 2599, 3164, 0), true);// Bank
        // chest
        World.spawnObject(new WorldObject(27663, 10, -3, 2600, 3164, 0), true);// Bank
        // chest
        World.spawnObject(new WorldObject(27663, 10, -1, 2599, 3163, 0), true);// Bank
        // chest
        World.spawnObject(new WorldObject(27663, 10, -3, 2600, 3163, 0), true);// Bank
        // chest
        World.spawnObject(new WorldObject(46300, 10, 1, 4375, 5917, 0), true);// contable
        World.spawnObject(new WorldObject(27663, 10, -3, 2600, 3162, 0), true);// Bank
        // chest
        World.spawnObject(new WorldObject(27663, 10, -1, 2599, 3161, 0), true);// Bank
        // chest
        World.spawnObject(new WorldObject(27663, 10, -3, 2600, 3161, 0), true);// Bank
        // chest
        World.spawnObject(new WorldObject(2286, 10, -3, 2538, 3545, 0), true);// barb
        // net
        World.spawnObject(new WorldObject(1, 10, -3, 2989, 4371, 0), true);// barb
        // net
        World.spawnNPC(537, new WorldTile(3040, 4843, 0), -1, true, true);// Aubury
        // (rc
        // ShopData)
        World.deleteObject(new WorldTile(2983, 9673, 0));
        World.deleteObject(new WorldTile(2983, 9673, 0));
        World.deleteObject(new WorldTile(2438, 3082, 0));
        World.deleteObject(new WorldTile(2989, 9677, 0));
        World.deleteObject(new WorldTile(2988, 9677, 0));
        World.deleteObject(new WorldTile(2988, 9678, 0));
        World.deleteObject(new WorldTile(2990, 9678, 0));
        World.deleteObject(new WorldTile(3000, 9671, 0));
        World.deleteObject(new WorldTile(3000, 9674, 0));
        World.deleteObject(new WorldTile(3000, 9675, 0));
        World.deleteObject(new WorldTile(4382, 5914, 0));
        // bank chest
        World.deleteObject(new WorldTile(3003, 9679, 0));
        World.spawnObject(new WorldObject(2213, 10, 1, 3002, 9678, 0), true); // bank
        // booth
        World.spawnObject(new WorldObject(2213, 10, 1, 3002, 9680, 0), true); // bank
        // booth
        World.spawnObject(new WorldObject(2213, 10, 1, 3002, 9679, 0), true); // bank
        // booth
        World.spawnNPC(494, new WorldTile(3003, 9679, 0), -1, false); // banker
        // new home
        World.deleteObject(new WorldTile(2410, 2834, 0));
        World.deleteObject(new WorldTile(2408, 2832, 0));
        World.deleteObject(new WorldTile(2413, 2825, 0));
        World.deleteObject(new WorldTile(2410, 2835, 0));
        World.deleteObject(new WorldTile(2411, 2836, 0));
        World.deleteObject(new WorldTile(2411, 2834, 0));
        World.deleteObject(new WorldTile(2410, 2834, 0));
        World.deleteObject(new WorldTile(2410, 2836, 0));
        World.deleteObject(new WorldTile(2409, 2835, 0));
        World.deleteObject(new WorldTile(2407, 2836, 0));
        World.deleteObject(new WorldTile(2424, 2824, 0));
        World.deleteObject(new WorldTile(2413, 2838, 0));
        World.spawnNPC(13955, new WorldTile(2401, 2839, 0), 1, false); // the
        // raptor
        // pkp
        World.spawnNPC(14712, new WorldTile(2400, 2839, 0), 0, false); // wizard
        // mr ex
        World.spawnNPC(15527, new WorldTile(2404, 2843, 0), 1, false); // happy
        // banker
        World.spawnNPC(13926, new WorldTile(2404, 2839, 0), 0, false); // home
        // changer
        World.spawnNPC(7935, new WorldTile(3002, 9685, 0), 0, false); // home
        // changer
        // old
        // home
        World.spawnNPC(7892, new WorldTile(2998, 9670, 0), 0, false); // quests
        // and
        // minigames
        World.spawnNPC(14712, new WorldTile(2997, 9670, 0), 0, false); // shops
        // teleport
        World.deleteObject(new WorldTile(2413, 2832, 0));
        World.deleteObject(new WorldTile(2413, 2833, 0));
        World.deleteObject(new WorldTile(2413, 2836, 0));
        World.deleteObject(new WorldTile(2414, 2836, 0));
        World.deleteObject(new WorldTile(2413, 2839, 0));
        World.deleteObject(new WorldTile(2408, 2839, 0));
        World.deleteObject(new WorldTile(2408, 2838, 0));
        World.deleteObject(new WorldTile(2408, 2835, 0));
        World.deleteObject(new WorldTile(2408, 2833, 0));
        World.deleteObject(new WorldTile(2410, 2838, 0));
        World.deleteObject(new WorldTile(2422, 2831, 0));
        World.deleteObject(new WorldTile(2425, 2828, 0));
        World.deleteObject(new WorldTile(2418, 2828, 0));
        World.deleteObject(new WorldTile(2420, 2827, 0));
        World.deleteObject(new WorldTile(2422, 2827, 0));
        World.deleteObject(new WorldTile(2421, 2825, 0));
        World.deleteObject(new WorldTile(2421, 2826, 0));
        World.deleteObject(new WorldTile(2413, 2835, 0));
        World.deleteObject(new WorldTile(2408, 2836, 0));
        World.deleteObject(new WorldTile(2409, 2834, 0));
        World.deleteObject(new WorldTile(2425, 2827, 0));
        World.deleteObject(new WorldTile(2422, 2826, 0));
        World.deleteObject(new WorldTile(2420, 2826, 0));
        World.deleteObject(new WorldTile(2418, 2827, 0));
        World.deleteObject(new WorldTile(2418, 2829, 0));
        World.deleteObject(new WorldTile(2421, 2831, 0));
        World.deleteObject(new WorldTile(2860, 9639, 0));
        World.deleteObject(new WorldTile(2857, 9642, 0));
        World.deleteObject(new WorldTile(2859, 9644, 0));
        // end of new home
        // summoning area NPC
        World.spawnNPC(6970, new WorldTile(2207, 5345, 0), -1, true, true);// Pikkupstix
        // (summoning
        // shops)

    }
}