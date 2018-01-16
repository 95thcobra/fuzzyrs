package com.rs.world.item;

import com.rs.content.player.PlayerRank;
import com.rs.core.cache.loaders.ItemDefinitions;
import com.rs.player.Player;
import com.rs.player.QuestManager.Quests;

public class ItemConstants {

	/*
     * public static int getDegradeItemWhenWear(int id) { // pvp armors if (id
	 * == 1) return id + 2; // if you wear it it becomes corrupted LOL return
	 * -1; }
	 */

    public static int getDegradeItemWhenWear(final int id) {
        // corrupt pvp armours
        if (id == 13908 || id == 13911 || id == 13914 || id == 13917
                || id == 13932 || id == 13935 || id == 13935 || id == 13938
                || id == 13944 || id == 13947 || id == 13950)
            return id + 2; // if you wear it it becomes corrupted LOL
        return -1;
    }

    // return amt of charges
    public static int getItemDefaultCharges(final int id) {
        // pvp armors
        if (id == 13908 || id == 13911 || id == 13914 || id == 13917
                || id == 13932 || id == 13935 || id == 13935 || id == 13938
                || id == 13944 || id == 13947 || id == 13950)
            return 18000;
        return -1;
    }

    // return what id it degrades to, -1 for disapear which is default so we
    // dont add -1
    public static int getItemDegrade(final int id) {
        if (id == 11285) // DFS
            return 11283;
        // nex armors
        if (id == 20137 || id == 20141 || id == 20145 || id == 20149
                || id == 20153 || id == 20157 || id == 20161 || id == 20165
                || id == 20169 || id == 20173)
            return id + 1;
        return -1;
    }

    public static int getDegradeItemWhenCombating(final int id) {
        // nex armors
        if (id == 20135 || id == 20139 || id == 20143 || id == 20147
                || id == 20151 || id == 20155 || id == 20159 || id == 20163
                || id == 20167 || id == 20171)
            return id + 2;
        return -1;
    }

    public static boolean itemDegradesWhileHit(final int id) {
        return id == 2550;
    }

    public static boolean itemDegradesWhileWearing(final int id) {
        final String name = ItemDefinitions.getItemDefinitions(id).getName()
                .toLowerCase();
        return name.contains("c. dragon") || name.contains("corrupt dragon")
                || name.contains("vesta's") || name.contains("statius'")
                || name.contains("morrigan's") || name.contains("zuriel's");
    }

    public static boolean itemDegradesWhileCombating(final int id) {
        final String name = ItemDefinitions.getItemDefinitions(id).getName()
                .toLowerCase();
        // nex armors
        return name.contains("torva") || name.contains("pernix")
                || name.contains("virtux") || name.contains("zaryte");
    }

    public static boolean canWear(final Item item, final Player player) {
        if (player.getRank().isMinimumRank(PlayerRank.ADMIN))
            return true;
        if ((item.getId() == 20771)) {
            if (player.getDominionTower().getKilledBossesCount() < 100) {
                player.getPackets()
                        .sendGameMessage(
                                "You must kill atleast 100 bosses in the Dominion Tower.");
                return false;
            }
            if (!player.getQuestManager().completedQuest(Quests.NOMADS_REQUIEM)) {
                player.getPackets().sendGameMessage(
                        "You must complete Nomads Requiem.");
                return false;
            }
            if (!player.isKilledQueenBlackDragon()) {
                player.getPackets().sendGameMessage(
                        "You must kill the Queen Black Dragon.");
                return false;
            }
            if (player.bandos < 20) {
                player.getPackets().sendGameMessage(
                        "You must kill General Graardor atleast 20 times.");
                return false;
            }
            if (player.armadyl < 10) {
                player.getPackets().sendGameMessage(
                        "You must kill Kree'arra atleast 10 times.");
                return false;
            }
        } else if (item.getId() == 20769) {
            if (player.bandos < 20) {
                player.getPackets().sendGameMessage(
                        "You must kill General Graardor atleast 20 times.");
                return false;
            }
            if (player.armadyl < 10) {
                player.getPackets().sendGameMessage(
                        "You must kill Kree'arra atleast 10 times.");
                return false;
            }
        } else if (item.getId() == 19893) {
            if (player.getRank().getDonateRank().isMinimumRank(PlayerRank.DonateRank.VIP)) {
                player.getPackets().sendGameMessage(
                        "You need to be a V.I.P. to wear this cape.");
                return false;
            }
        } else if (item.getId() == 17291) {
            if (player.getRank().getDonateRank().isMinimumRank(PlayerRank.DonateRank.VIP)) {
                player.getPackets().sendGameMessage(
                        "You need to be a V.I.P. to wear this amulet.");
                return false;
            }
        } else if (item.getId() == 6570) {
            if (!player.isCompletedFightCaves()) {
                player.getPackets()
                        .sendGameMessage(
                                "You need to complete fightcaves before you can equip firecape.");
                return false;
            }
        } else if (item.getId() == 14642 || item.getId() == 14645
                || item.getId() == 15433 || item.getId() == 15435
                || item.getId() == 14641 || item.getId() == 15432
                || item.getId() == 15434) {
            if (!player.getQuestManager().completedQuest(Quests.NOMADS_REQUIEM)) {
                player.getPackets()
                        .sendGameMessage(
                                "You need to have completed Nomad's Requiem miniquest to use this cape.");
                return false;
            }
        }
        final String itemName = item.getName();
		/*
		 * if (itemName.contains("goliath gloves") ||
		 * itemName.contains("spellcaster glove") ||
		 * itemName.contains("swift glove")) { if
		 * (player.getDominionTower().getKilledBossesCount() < 50) {
		 * player.getPackets().sendGameMessage(
		 * "You need to have kill atleast 50 bosses in the Dominion tower to wear these gloves."
		 * ); return true; } }
		 */
        return true;
    }

    public static boolean isTradeable(final Item item) {
        if (item.getDefinitions().getName().toLowerCase()
                .contains("flaming skull"))
            return false;
        final String name = ItemDefinitions.getItemDefinitions(item.getId())
                .getName().toLowerCase();
        if (name.contains("lucky") || name.contains("lucky")
                || name.contains("casket (easy)") || name.contains("clue")
                || name.contains("dice") || name.contains("slayer helm")
                || name.contains("mithril seeds")
                || name.contains("spirit cape") || name.contains("hailstorm")
                || name.contains("easter ring") || name.contains("classic")
                || name.contains("demonflesh") || name.contains("moia's hands")
                || name.contains("ring of stone") || name.contains("quest")
                || name.contains("completionist")
                || name.contains("primal full helm")
                || name.contains("primal platelegs")
                || name.contains("primal platebody")
                || name.contains("max cape") || name.contains("max hood")
                || name.contains("guy fawkes mask")
                || name.contains("moia's dagger")
                || name.contains("flaming lash")
                || name.contains("flaming skull")
                || name.contains("abyssal whip (lime)")
                || name.contains("fist of guthix") || name.contains("pk token")
                || name.contains("fire cape") || name.contains("tokhaar")
                || name.contains("rusty coins") || name.contains("easter egg")
                || name.contains("archery ticket"))
            return false;
        switch (item.getId()) {
            case 6570: // firecape
            case 6529: // tokkul
            case 24155:
            case 24154:
                // case 7462: //barrow gloves
            case 23659: // tokhaar-kal
                return false;
            default:
                return true;
        }
    }
}
