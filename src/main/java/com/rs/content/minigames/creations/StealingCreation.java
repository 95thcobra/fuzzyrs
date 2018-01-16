package com.rs.content.minigames.creations;

import com.rs.content.actions.skills.Skills;
import com.rs.player.Player;
import com.rs.world.Animation;
import com.rs.world.WorldObject;
import com.rs.world.WorldTile;
import com.rs.world.item.Item;
import com.rs.world.task.gametask.GameTask;
import com.rs.world.task.gametask.GameTaskManager;
import com.rs.world.task.worldtask.WorldTask;
import com.rs.world.task.worldtask.WorldTasksManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Richard (Flamable)
 * @author Khaled
 */
public class StealingCreation {

    public static final int[] SACRED_CLAY = {14182, 14184, 14186, 14188, 14190};
    public static final int[] CLASS_ITEMS_BASE = {14132, 14122, 14142, 14152,
            14172, 14162, 14367, 14357, 14347, 14411, 14391, 14401, 14337,
            14317, 14327, 14297, 14287, 14307, 14192, 14202, 12850, 12851,
            14422, 14377, 14421, -1, -1, 14215, 14225, 14235, 14245, 14255,
            14265, 14275, 14285};
    public static final WorldTile LOBBY_WORLDTILE = new WorldTile(2968, 9701, 0);
    private static final int[] TOTAL_SKILL_IDS = {Skills.WOODCUTTING,
            Skills.MINING, Skills.FISHING, Skills.HUNTER, Skills.COOKING,
            Skills.HERBLORE, Skills.CRAFTING, Skills.SMITHING,
            Skills.FLETCHING, Skills.RUNECRAFTING, Skills.CONSTRUCTION};
    private static final int[] TOTAL_COMBAT_IDS = {Skills.ATTACK,
            Skills.STRENGTH, Skills.DEFENCE, Skills.HITPOINTS, Skills.RANGE,
            Skills.MAGIC, Skills.PRAYER, Skills.SUMMONING};
    private static final int[] BASE_ANIMATIONS = {10603, 10608, 10613, 10618};
    private static List<Player> redTeam = new ArrayList<Player>(),
            blueTeam = new ArrayList<>();// no maximum limit on it i

    public static void enterTeamLobby(final Player player,
                                      final boolean inRedTeam) {
        if (!canEnter(player, inRedTeam))
            return;
        else if (!hasRequiredPlayers()) {
            GameTaskManager.scheduleTask(new LobbyTask(GameTask.ExecutionType.FIXED_RATE, 0, 60, TimeUnit.SECONDS));
        }
        player.setNextAnimation(new Animation(1560));
        WorldTasksManager.schedule(new WorldTask() {
            @Override
            public void run() {
                player.setNextWorldTile(inRedTeam ? new WorldTile(2965, 9703, 0) : new WorldTile(2965, 9696, 0));
                player.getControllerManager().startController(StealingCreationLobby.class, inRedTeam);
            }
        });
    }

    public static void leaveTeamLobby(final Player player, boolean inRedTeam) {
        player.setNextAnimation(new Animation(1560));
        WorldTasksManager.schedule(new WorldTask() {
            @Override
            public void run() {
                player.setNextWorldTile(inRedTeam ? new WorldTile(2967, 9703, 0) : new WorldTile(2967, 9696, 0));
                if (inRedTeam) {
                    redTeam.remove(player);
                } else {
                    blueTeam.remove(player);
                }
                StealingCreation.updateInterfaces();
                player.closeInterfaces();
                player.getControllerManager().getController().removeControler();
            }
        });
    }

    public static void passToGame() {
        // createDynamicRegion
        for (final Player player : redTeam) {
            player.getControllerManager().startController(
                    StealingCreationGame.class, true);
        }
        for (final Player player : blueTeam) {
            player.getControllerManager().startController(
                    StealingCreationGame.class, false);
        }
    }

    public static boolean proccessKilnItems(final Player player,
                                            final int componentId, final int index, final int itemId,
                                            final int amount) {
        final int clayId = SACRED_CLAY[index];
        if (player.getInventory().containsItem(clayId, 1)) {
            if (player
                    .getInventory()
                    .addItem(
                            new Item(
                                    StealingCreation.CLASS_ITEMS_BASE[componentId - 37]
                                            + ((componentId == 57
                                            || componentId == 58 || componentId == 61) ? 0
                                            : componentId == 56 ? index
                                            : componentId >= 64 ? (-index * 2)
                                            : (index * 2)),
                                    (componentId >= 56 && componentId <= 58 ? 15 * (index + 1)
                                            : componentId == 61 ? index + 1 : 1)
                                            * amount))) {
                player.getInventory().deleteItem(itemId, amount);
                return true;
            }
        }
        player.getPackets().sendGameMessage("You have no clay to proccess.");
        return false;
    }

    public static boolean checkSkillRequriments(final Player player,
                                                final int requestedSkill, final int index) {
        final int level = getLevelForIndex(index);
        if (player.getSkills().getLevel(requestedSkill) <= level) {
            player.getPackets().sendGameMessage(
                    "You don't have the required "
                            + Skills.SKILL_NAME[requestedSkill]
                            + " level for that quality of clay.");
            return false;
        }
        return true;
    }

    public static void startDynamicSkill(final Player player,
                                         final WorldObject object, Animation animation, final int baseId,
                                         final int objectIndex) {
        if (!checkSkillRequriments(player, getRequestedObjectSkill(),
                objectIndex))
            return;
        final Item item = getBestItem(player, baseId);
        if (item.getId() == -1) {
            animation = new Animation(10602);
        } else if (player.getInventory().containsItem(item.getId(),
                item.getAmount())) {
            player.setNextAnimation(animation);
        }
        player.getActionManager().setAction(
                new CreationSkillsAction(object, animation, item, baseId,
                        objectIndex));
    }

    static Item getBestItem(final Player player, final int baseId) {
        for (int index = 4; index >= 0; index--) {
            final Item item = new Item(baseId + (index * 2), 1);
            if (player.getInventory().getItems().contains(item))
                return item;
        }
        return new Item(-1, 1);
    }

    public static int getRequestedObjectSkill() {
        return 0;
    }

    public static int getRequestedKilnSkill(final int indexedId) {
        if (indexedId >= 0 && indexedId <= 1 || indexedId >= 6
                && indexedId <= 8 || indexedId >= 15 && indexedId <= 17)
            return Skills.SMITHING;
        else if (indexedId >= 2 && indexedId <= 3 || indexedId >= 9
                && indexedId <= 14 || indexedId >= 18 && indexedId <= 19
                || indexedId == 23)
            return Skills.CRAFTING;
        else if (indexedId == 4)
            return Skills.CONSTRUCTION;
        else if (indexedId == 5)
            return Skills.COOKING;
        else if (indexedId >= 20 && indexedId <= 21)
            return Skills.RUNECRAFTING;
        else if (indexedId >= 22 && indexedId <= 24)
            return Skills.SUMMONING;
        else if (indexedId >= 25 && indexedId <= 32)
            return Skills.HERBLORE;
        return 1;
    }

    private static int getLevelForIndex(final int index) {
        int level = 0;
        for (int i = 0; i < index; i++) {
            if (i == index)
                return level;
            level += 20;
        }
        return level;
    }

    private static void sendGameEnding() {
        sendGameConfig(556, 1);
        WorldTasksManager.schedule(new WorldTask() {
            @Override
            public void run() {
                sendGameConfig(561, 1);
                for (final Player player : redTeam) {
                    player.getPackets().sendHideIComponent(809, 18, false);
                }
                for (final Player player : blueTeam) {
                    player.getPackets().sendHideIComponent(809, 18, false);
                }
            }
        });
    }

    private static void sendGameConfig(final int id, final int value) {
        for (final Player player : redTeam) {
            player.getPackets().sendGlobalConfig(id, value);
        }
        for (final Player player : blueTeam) {
            player.getPackets().sendGlobalConfig(id, value);
        }
    }

    private static boolean hasRequiredPlayers() {
        return redTeam.size() >= 1 && blueTeam.size() >= 1;
    }

    public static void updateInterfaces() {
        final boolean hidden = hasRequiredPlayers();
        for (final Player player : redTeam) {
            player.getPackets().sendHideIComponent(804, 2, hidden);
            updateTeamInterface(player, true, hidden);
        }
        for (final Player player : blueTeam) {
            player.getPackets().sendHideIComponent(804, 2, hidden);
            updateTeamInterface(player, false, hidden);
        }
    }

    public static void updateTeamInterface(final Player player,
                                           final boolean inRedTeam, final boolean hidden) {
        final int skillTotal = getTotalLevel(TOTAL_SKILL_IDS, inRedTeam);
        final int combatTotal = getTotalLevel(TOTAL_COMBAT_IDS, inRedTeam);
        final int otherSkillTotal = getTotalLevel(TOTAL_SKILL_IDS, !inRedTeam);
        final int otherCombatTotal = getTotalLevel(TOTAL_COMBAT_IDS, !inRedTeam);
        player.getPackets().sendIComponentText(804, 5, "" + skillTotal);
        player.getPackets().sendIComponentText(804, 4, "" + combatTotal);
        player.getPackets().sendIComponentText(804, 6, "" + otherSkillTotal);
        player.getPackets().sendIComponentText(804, 7, "" + otherCombatTotal);
        for (int i = 33; i < 34; i++) {
            player.getPackets().sendIComponentText(804, i, "" + (hidden ? 5 - (inRedTeam ? redTeam.size() : blueTeam.size()) : inRedTeam ? redTeam.size() : blueTeam.size()));
        }
    }

    private static boolean canEnter(final Player player, final boolean inRedTeam) {
        final int skillTotal = getTotalLevel(TOTAL_SKILL_IDS, inRedTeam);
        final int combatTotal = getTotalLevel(TOTAL_COMBAT_IDS, inRedTeam);
        final int otherSkillTotal = getTotalLevel(TOTAL_SKILL_IDS, !inRedTeam);
        final int otherCombatTotal = getTotalLevel(TOTAL_COMBAT_IDS, !inRedTeam);
        if ((skillTotal + combatTotal) > (otherSkillTotal + otherCombatTotal) && (!getRedTeam().isEmpty() && !getBlueTeam().isEmpty())) {
            player.getPackets().sendGameMessage("This team is too strong for you to join at present.");
            return false;
        } else if (player.getEquipment().wearingArmour() || player.getInventory().getFreeSlots() != 28 || player.getFamiliar() != null) {
            player.getPackets().sendGameMessage("You may not take any items into Stealing Creation. You can use the nearby bank deposit bank to empty your inventory and storn wore items.");
            return false;
        }
        return true;
    }

    private static int getTotalLevel(final int[] ids, final boolean inRedTeam) {
        int skillTotal = 0;
        for (final Player player : inRedTeam ? redTeam : blueTeam) {
            if (player == null) {
                continue;
            }
            for (final int skillRequested : ids) {
                skillTotal += player.getSkills().getLevel(skillRequested);
            }
        }
        return skillTotal;
    }

    public static List<Player> getRedTeam() {
        return redTeam;
    }

    public static List<Player> getBlueTeam() {
        return blueTeam;
    }

    public static Animation getAnimationForBase(final int baseId,
                                                final int index) {
        return new Animation(BASE_ANIMATIONS[index] + baseId);
    }

    private static class LobbyTask extends GameTask {

        private int minutes;

        public LobbyTask(ExecutionType executionType, long initialDelay, long tick, TimeUnit timeUnit) {
            super(executionType, initialDelay, tick, timeUnit);
            this.minutes = 0;
        }

        @Override
        public void run() {
            if (!hasRequiredPlayers()) {
                this.cancel(true);
            } else if (minutes++ == 2) {
                passToGame();
                this.cancel(true);
            }
        }
    }

    private static class DynamicRegion {

        int[][] boundChunks = {{240, 714 /* kiln square */},
                {240, 715 /* u jellies :D */}, {241, 714 /* altar */},
                {240, 715 /* rift, at least 1 */}, {241, 716 /*
                                                                 * agility climb
																 * at least 1
																 */}};
        boolean[] classCheck = new boolean[4];

    }
}
