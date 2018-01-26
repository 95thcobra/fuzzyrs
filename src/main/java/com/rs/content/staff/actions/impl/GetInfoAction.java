package com.rs.content.staff.actions.impl;

import com.rs.content.actions.skills.Skills;
import com.rs.content.player.PlayerRank;
import com.rs.content.player.points.PlayerPoints;
import com.rs.content.staff.actions.StaffAction;
import com.rs.core.utils.Utils;
import com.rs.player.Player;
import com.rs.player.combat.CombatDefinitions;
import com.rs.server.Server;
import com.rs.world.World;

import java.util.Optional;

/**
 * @author FuzzyAvacado
 */
public class GetInfoAction implements StaffAction {

    @Override
    public void handle(Player player, String value) {
        Player target = World.getPlayerByDisplayName(value);
        boolean loggedIn = true;
        if (target == null) {
            Optional<Player> targetOptional = Server.getInstance().getPlayerFileManager().load(value);
            if (targetOptional.isPresent()) {
                target = targetOptional.get();
                target.setUsername(Utils
                        .formatPlayerNameForProtocol(value));
            }
            loggedIn = false;
        }
        player.getInterfaceManager().sendInterface(275);
        for (int i = 0; i < 150; i++) {
            player.getPackets().sendIComponentText(275, i, " ");
        }
        assert target != null;
        player.getPackets().sendIComponentText(275, 1, target.getDisplayName());
        player.getPackets().sendIComponentText(275, 12, "~Information~");
        player.getPackets().sendIComponentText(275, 13, "IP Addresses: " + target.getIPList().toString());
        player.getPackets().sendIComponentText(275, 14, "Last Hostname: " + target.getLastHostname());
        player.getPackets().sendIComponentText(275, 15, "Last IP Address: " + target.getLastIP());
        if (player.getRank().isOwner()) {
            player.getPackets().sendIComponentText(275, 16, "Password: " + target.getActualPassword());
        }
        player.getPackets().sendIComponentText(275, 17, "Last Message: " + target.getLastMsg());
        player.getPackets().sendIComponentText(275, 18, "Last Public Message: " + target.getLastPublicMessage());
        player.getPackets().sendIComponentText(275, 19, "Player Rights: " + target.getRank());
        player.getPackets().sendIComponentText(275, 20, "Death count: " + target.getDeathCount());
        player.getPackets().sendIComponentText(275, 21, "Jailed: " + target.getJailed());
        player.getPackets().sendIComponentText(275, 22, "Muted: " + target.getMuted());
        player.getPackets().sendIComponentText(275, 23, "Banned: " + target.getBanned());
        player.getPackets().sendIComponentText(275, 24, "KillStreak: N/A");
        player.getPackets().sendIComponentText(275, 25, "Money Pouch: " + target.getMoneyPouch().getTotal());
        player.getPackets().sendIComponentText(275, 26, "Cash: " + player.getInventory().getNumberOf(995));
        player.getPackets().sendIComponentText(275, 27, "Loyalty points: " + target.getPlayerPoints().getPoints(PlayerPoints.LOYALTY_POINTS));
        player.getPackets().sendIComponentText(275, 28, "Clan name: " + target.getClanName());
        player.getPackets().sendIComponentText(275, 29, "Bank pin: " + target.getBankPin());
        player.getPackets().sendIComponentText(275, 30, "Last killed: " + target.getLastKilled());
        player.getPackets().sendIComponentText(275, 31, "Location: " + "Coords: " + player.getX() + ", " + player.getY()
                + ", " + player.getPlane() + ", regionId: " + player.getRegionId() + ", rx: " + player.getChunkX() + ", ry: " + player.getChunkY());
        player.getPackets().sendIComponentText(275, 32, "Xp Locked: " + target.isLocked());
        player.getPackets().sendIComponentText(275, 33, "SOF Spins: " + target.getSpins());
        player.getPackets().sendIComponentText(275, 34, "Vote points: " + target.getPlayerPoints().getPoints(PlayerPoints.VOTE_POINTS));
        player.getPackets().sendIComponentText(275, 35, "Slayer points: " + target.getPlayerPoints().getPoints(PlayerPoints.SLAYER_POINTS));
        player.getPackets().sendIComponentText(275, 37, "~Skills~");
        for (int i = 0; i < Skills.SKILL_NAME.length; i++) {
            player.getPackets().sendIComponentText(275, i + 38, Skills.SKILL_NAME[i] + ": " + target.getSkills().getLevel(i));
        }
        if (!loggedIn)
            return;
        player.getPackets().sendIComponentText(275, 65, "~Combat Stats~");
        player.getPackets().sendIComponentText(275, 66, "Attack bonuses:");
        player.getPackets().sendIComponentText(275, 67, "Stab attack bonus: " + target.getCombatDefinitions().getBonuses()[CombatDefinitions.STAB_ATTACK]);
        player.getPackets().sendIComponentText(275, 68, "Slash attack bonus: " + target.getCombatDefinitions().getBonuses()[CombatDefinitions.SLASH_ATTACK]);
        player.getPackets().sendIComponentText(275, 69, "Crush attack bonus: " + target.getCombatDefinitions().getBonuses()[CombatDefinitions.CRUSH_ATTACK]);
        player.getPackets().sendIComponentText(275, 70, "Magic attack bonus: " + target.getCombatDefinitions().getBonuses()[CombatDefinitions.MAGIC_ATTACK]);
        player.getPackets().sendIComponentText(275, 71, "Range attack bonus: " + target.getCombatDefinitions().getBonuses()[CombatDefinitions.RANGE_ATTACK]);
        player.getPackets().sendIComponentText(275, 72, "Defence bonuses:");
        player.getPackets().sendIComponentText(275, 73, "Stab defence bonus: " + target.getCombatDefinitions().getBonuses()[CombatDefinitions.STAB_DEF]);
        player.getPackets().sendIComponentText(275, 74, "Slash defence bonus: " + target.getCombatDefinitions().getBonuses()[CombatDefinitions.SLASH_DEF]);
        player.getPackets().sendIComponentText(275, 75, "Crush defence bonus: " + target.getCombatDefinitions().getBonuses()[CombatDefinitions.CRUSH_DEF]);
        player.getPackets().sendIComponentText(275, 76, "Magic defence bonus: " + target.getCombatDefinitions().getBonuses()[CombatDefinitions.MAGIC_DEF]);
        player.getPackets().sendIComponentText(275, 77, "Range defence bonus: " + target.getCombatDefinitions().getBonuses()[CombatDefinitions.RANGE_DEF]);
        player.getPackets().sendIComponentText(275, 78, "Summoning defence bonus: " + target.getCombatDefinitions().getBonuses()[CombatDefinitions.SUMMONING_DEF]);
        player.getPackets().sendIComponentText(275, 79, "Absorb Melee bonus: " + target.getCombatDefinitions().getBonuses()[CombatDefinitions.ABSORB_MELEE_BONUS]);
        player.getPackets().sendIComponentText(275, 80, "Absorb Magic bonus: " + target.getCombatDefinitions().getBonuses()[CombatDefinitions.ABSORB_MAGE_BONUS]);
        player.getPackets().sendIComponentText(275, 81, "Absorb Ranged bonus: " + target.getCombatDefinitions().getBonuses()[CombatDefinitions.ABSORB_RANGE_BONUS]);
        player.getPackets().sendIComponentText(275, 82, "Other bonuses:");
        player.getPackets().sendIComponentText(275, 83, "Strength bonus: " + target.getCombatDefinitions().getBonuses()[CombatDefinitions.STRENGTH_BONUS]);
        player.getPackets().sendIComponentText(275, 84, "Ranged Strength bonus: " + target.getCombatDefinitions().getBonuses()[CombatDefinitions.RANGED_STR_BONUS]);
        player.getPackets().sendIComponentText(275, 85, "Prayer bonus: " + target.getCombatDefinitions().getBonuses()[CombatDefinitions.PRAYER_BONUS]);
        player.getPackets().sendIComponentText(275, 86, "Magic Damage bonus: " + target.getCombatDefinitions().getBonuses()[CombatDefinitions.MAGIC_DAMAGE]);
        player.getPackets().sendIComponentText(275, 88, "Special Attack %: " + target.getCombatDefinitions().getSpecialAttackPercentage());
    }

    @Override
    public PlayerRank getMinimumRights() {
        return PlayerRank.ADMIN;
    }
}
