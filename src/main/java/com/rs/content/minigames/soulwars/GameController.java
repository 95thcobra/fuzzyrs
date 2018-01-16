package com.rs.content.minigames.soulwars;

/**
 * @author John (FuzzyAvacado) on 1/6/2016.
 */

import com.rs.content.actions.skills.Skills;
import com.rs.content.dialogues.types.SimpleNPCMessage;
import com.rs.content.minigames.soulwars.SoulWarsManager.PlayerType;
import com.rs.content.minigames.soulwars.SoulWarsManager.Teams;
import com.rs.content.player.PlayerRank;
import com.rs.core.utils.Utils;
import com.rs.player.Equipment;
import com.rs.player.Inventory;
import com.rs.player.Player;
import com.rs.player.controlers.Controller;
import com.rs.world.*;
import com.rs.world.item.Item;
import com.rs.world.npc.NPC;
import com.rs.world.task.worldtask.WorldTask;
import com.rs.world.task.worldtask.WorldTasksManager;

/**
 * @author Savions Sw
 */
public class GameController extends Controller {

    private int activity = 100;

    private int team;

    private boolean resizable;

    @Override
    public void start() {
        team = (int) getArguments()[0];
        setAreaIndex(-1);
        sendInterfaces();
        player.getTemporaryAttributtes().put("sw_safe_zone", Boolean.TRUE);
        player.setNextWorldTile(World.getSoulWars().calculateRandomLocation(Teams.values()[team], PlayerType.IN_GAME));
        player.setCanPvp(true);
        player.getMusicsManager().playMusic(598);
        player.getDialogueManager().startDialogue(SimpleNPCMessage.class, team == 0 ? 8528 : 8526, "The time is now, Crush their souls!");
        resizable = player.getInterfaceManager().hasRezizableScreen();
        player.getInterfaceManager().sendTab(player.getInterfaceManager().hasRezizableScreen() ? 27 : 11, 836);
        sendInterfaces();
    }

    @Override
    public boolean canAttack(Entity target) {
        if (target instanceof Player) {
            if (target.getTemporaryAttributtes().get("sw_safe_zone") != null) {
                Boolean bool = (Boolean) target.getTemporaryAttributtes().get("sw_safe_zone");
                if (bool.equals(Boolean.TRUE))
                    return false;
            }
            if (canHit(target))
                return true;
            player.getPackets().sendGameMessage("You can't attack your team.");
            return false;
        }
        return canHit(target);
    }

    @Override
    public boolean processItemOnObject(WorldObject object, Item item) {
        if (object.getId() == 42010 && item.getId() == SoulWarsManager.SOUL_FRAGMENT) {
            final SoulWarsGameTask task = (SoulWarsGameTask) World.getSoulWars().getTasks().get(PlayerType.IN_GAME);
            final Teams team = Teams.values()[player.getEquipment().getCapeId() - SoulWarsManager.TEAM_CAPE_INDEX];
            if (team.equals(task.getTeamAreas()[1]) && task.getTeamAreaValues()[team.ordinal()] >= 10) {
                final int amount = player.getInventory().getNumberOf(SoulWarsManager.SOUL_FRAGMENT);
                player.getInventory().deleteItem(SoulWarsManager.SOUL_FRAGMENT, amount);
                task.increaseAvatarLevel(team.equals(Teams.BLUE) ? Teams.RED : Teams.BLUE, -amount);
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean canEquip(int slotId, int itemId) {
        if (slotId == Equipment.SLOT_CAPE) {
            player.getPackets().sendGameMessage("You can't remove your team's colours.");
            return false;
        }
        return true;
    }

    @Override
    public void sendInterfaces() {
        if (resizable != player.getInterfaceManager().hasRezizableScreen()) {
            resizable = player.getInterfaceManager().hasRezizableScreen();
            player.getInterfaceManager().sendTab(player.getInterfaceManager().hasRezizableScreen() ? 27 : 11, 836);
        }
        SoulWarsGameTask task = (SoulWarsGameTask) World.getSoulWars().getTasks().get(PlayerType.IN_GAME);
        final int index = (int) getArguments()[0];
        final Avatar BLUE = task.getAvatars()[0];
        final Avatar RED = task.getAvatars()[1];
        player.getPackets().sendHideIComponent(836, 47, index == -1 || index != 1);
        player.getPackets().sendHideIComponent(836, 49, index == -1 || index != 2);
        player.getPackets().sendHideIComponent(836, 50, index == -1 || index != 0);
        player.getPackets().sendGlobalConfig(641, task.getAvatarSlayerLevel(Teams.BLUE));
        player.getPackets().sendGlobalConfig(642, task.getAvatarSlayerLevel(Teams.RED));
        player.getPackets().sendGlobalConfig(643, task.getAvatarDies(Teams.BLUE));
        player.getPackets().sendGlobalConfig(644, task.getAvatarDies(Teams.RED));
        player.getPackets().sendGlobalConfig(636, 20 - (SoulWarsManager.MINUTES_BEFORE_NEXT_GAME.get() - 3));
        player.getPackets().sendGlobalConfig(640, BLUE == null || BLUE.hasFinished() || BLUE.isDead() ? -1 : (BLUE.getHitpoints() / 100));
        player.getPackets().sendGlobalConfig(639, RED == null || RED.hasFinished() || RED.isDead() ? -1 : (RED.getHitpoints() / 100));
        for (int objectIndex = 0; objectIndex < 3; objectIndex++) {
            final int config = objectIndex == 0 ? 649 : objectIndex == 1 ? 645 : 647;
            final int value = task.getTeamAreas()[objectIndex] == null ? 15 : (task.getTeamAreas()[objectIndex].equals(Teams.BLUE) ? 15 - task.getTeamAreaValues()[objectIndex] : 15 + task.getTeamAreaValues()[objectIndex]);
            player.getPackets().sendGlobalConfig(config, value);
        }
        player.getPackets().sendConfig(1380, activity * 10);
    }

    @Override
    public boolean sendDeath() {
        WorldTasksManager.schedule(new WorldTask() {
            int loop;

            @Override
            public void run() {
                if (SoulWarsManager.MINUTES_BEFORE_NEXT_GAME.get() < 4)
                    super.stop();
                if (loop == 0)
                    player.setNextAnimation(new Animation(836));
                else if (loop == 1)
                    player.getPackets().sendGameMessage(
                            "Oh dear, you have died.");
                else if (loop == 3) {
                    final Player killer = player.getMostDamageReceivedSourcePlayer();
                    final int amount = player.getInventory().getNumberOf(
                            SoulWarsManager.SOUL_FRAGMENT);
                    final int bones = player.getInventory().getNumberOf(
                            SoulWarsManager.BONES);
                    if (killer != null)
                        World.addGroundItem(new Item(3187), new WorldTile(player), killer,
                                false, 60, true);
                    if (amount > 0) {
                        player.getInventory().deleteItem(SoulWarsManager.SOUL_FRAGMENT, amount);
                        World.addGroundItem(new Item(
                                        SoulWarsManager.SOUL_FRAGMENT, amount),
                                new WorldTile(player), killer == null ? player : killer, false, 60, true);
                    }
                    if (bones > 0) {
                        player.getInventory().deleteItem(SoulWarsManager.BONES, amount);
                        for (int index = 0; index < bones; index++)
                            World.addGroundItem(new Item(SoulWarsManager.BONES, 1),
                                    new WorldTile(player), killer == null ? player : killer, false, 60, true);
                    }
                    player.getTemporaryAttributtes().put("sw_safe_zone", Boolean.TRUE);
                    player.reset();
                    player.setNextWorldTile(((SoulWarsGameTask) World.getSoulWars()
                            .getTasks().get(PlayerType.IN_GAME))
                            .getRespawnLocation(player, team));
                    increaseAvtivity(40);
                    player.resetReceivedDamage();
                    player.setNextAnimation(new Animation(-1));
                    player.getAppearance().transformIntoNPC(
                            SoulWarsManager.GHOST);
                } else if (loop == 4) {
                    player.getPackets().sendMusicEffect(90);
                } else if (loop == 20) {
                    player.setNextAnimation(null); // TODO
                    player.getAppearance().transformIntoNPC(-1);
                }
                loop++;
            }
        }, 0, 1);
        return false;
    }

    private void increaseAvtivity(int i) {
        activity = activity + i > 100 ? 100 : activity + i;
        player.getPackets().sendConfig(1380, activity * 10);
    }

    private void handleBandage(Item item) {
        int gloves = player.getEquipment().getGlovesId();
        player.heal((int) (player.getMaxHitpoints() * (gloves >= 11079
                && gloves <= 11084 ? 0.15 : 0.10)));
        int restoredEnergy = (int) (player.getRunEnergy() * 1.3);
        player.setRunEnergy(restoredEnergy > 100 ? 100 : restoredEnergy);
        if (player.getPoison().isPoisoned())
            player.getPoison().reset();
        player.getInventory().deleteItem(item);
        player.getPackets().sendGameMessage("You use a bandage and feel restored...", true);
    }

    public void setAreaIndex(int index) {
        getArguments()[0] = index;
    }

    @Override
    public boolean canMove(int dir) {
        final WorldTile location = player.transform(Utils.DIRECTION_DELTA_X[dir], Utils.DIRECTION_DELTA_Y[dir], 0);
        player.getTemporaryAttributtes().put("soul_wars_walked", location);
        return ((SoulWarsGameTask) World.getSoulWars().getTasks().get(PlayerType.IN_GAME)).containsBarricade(location);
    }

    public void decreaseActivity() {
        if (player.getActionManager().getAction() != null) {
            increaseAvtivity(5);
            return;
        }
        if (player.getRank().isMinimumRank(PlayerRank.ADMIN) && --activity == 0) {
            World.getSoulWars().resetPlayer(player, PlayerType.IN_GAME, false);
            player.getTemporaryAttributtes().put("soulwars_kicked", true);
            player.getDialogueManager().startDialogue(
                    SimpleNPCMessage.class, team == 0 ? 8528 : 8526,
                    "You were removed from the game due to your activity.");
        }
    }

    @Override
    public void forceClose() {
        player.getPackets().closeInterface(
                player.getInterfaceManager().hasRezizableScreen() ? 27 : 11);
        ((SoulWarsGameTask) World.getSoulWars().getTasks().get(PlayerType.IN_GAME)).getPlayers().remove(player);
    }

    @Override
    public boolean processMagicTeleport(WorldTile toTile) {
        player.getPackets().sendGameMessage(
                "You can't just leave like that!");
        return false;
    }

    @Override
    public boolean processItemTeleport(WorldTile toTile) {
        player.getPackets().sendGameMessage(
                "You can't just leave like that!");
        return false;
    }

    @Override
    public void magicTeleported(int type) {
        forceClose();
        removeControler();
    }

    @Override
    public boolean processButtonClick(int interfaceId, int componentId,
                                      int slotId, int packetId) {
        if (interfaceId == 590 & componentId == 8) {
            player.getPackets().sendGameMessage("This is a battleground, not a circus.");
            return false;
        }
        if (interfaceId == 387 && componentId == 9) {
            player.getPackets().sendGameMessage("You can't remove your team's colours.");
            return false;
        }
        if (interfaceId == Inventory.INVENTORY_INTERFACE || interfaceId == 670) {
            Item item = player.getInventory().getItem(slotId);
            if (item == null)
                return false;
            if (item.getId() == 4053) {
                if (!((SoulWarsGameTask) World.getSoulWars().getTasks().get(PlayerType.IN_GAME)).containsBarricade(player))
                    player.getPackets().sendGameMessage("You can't place a barricade here!");
                else
                    ((SoulWarsGameTask) World.getSoulWars().getTasks().get(PlayerType.IN_GAME)).placeBarricade(player, team);
                return false;
            } else if (item.getId() == 3187) {
                player.stopAll(true);
                player.lock(2);
                player.getPackets().sendSound(2738, 0, 1);
                player.setNextAnimation(new Animation(827));
                SoulWarsGameTask task = (SoulWarsGameTask) World.getSoulWars().getTasks().get(PlayerType.IN_GAME);
                player.getInventory().deleteItem(3187, 1);
                increaseAvtivity(10);
                if (Utils.getRandom(4) != 0)
                    task.increaseAvatarLevel(Teams.values()[team], Utils.getRandom(4));
                if (Utils.getRandom(1) == 0)
                    player.getPrayer().restorePrayer(10 + Utils.getRandom(100));
                return false;
            } else if (item.getId() == SoulWarsManager.TEAM_CAPE_INDEX || item.getId() == SoulWarsManager.TEAM_CAPE_INDEX + 1) {
                player.getPackets().sendGameMessage("You can't remove your team's colours.");
                return false;
            } else if (item != null && item.getId() == SoulWarsManager.BANDAGE_ID && interfaceId == Inventory.INVENTORY_INTERFACE) {
                handleBandage(item);
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean canHit(Entity target) {
        if (target instanceof NPC) {
            int requiredSlayerLevel = 0;
            switch (((NPC) target).getId()) {
                case SoulWarsManager.PYREFRIEND:
                    requiredSlayerLevel = 30;
                    break;
                case SoulWarsManager.JELLY:
                    requiredSlayerLevel = 52;
                    break;
                case SoulWarsManager.AVATAR_INDEX:
                case SoulWarsManager.AVATAR_INDEX + 1:
                    if (Teams.values()[((NPC) target).getId()
                            - SoulWarsManager.AVATAR_INDEX]
                            .equals(Teams.values()[player.getEquipment()
                                    .getCapeId() - SoulWarsManager.TEAM_CAPE_INDEX])) {
                        player.getPackets()
                                .sendGameMessage(
                                        "You cannot attack the avatar representing your own team!");
                        return false;
                    }
                    requiredSlayerLevel = ((SoulWarsGameTask) World.getSoulWars().getTasks().get(PlayerType.IN_GAME)).getAvatarSlayerLevel(Teams.values()[((NPC) target).getId() - SoulWarsManager.AVATAR_INDEX]) - 1;
            }
            if (player.getSkills().getLevel(Skills.SLAYER) < requiredSlayerLevel) {
                player.getPackets()
                        .sendGameMessage(
                                "Your slayer level is not high enough to fight this monster.");
                return false;
            }
            return true;
        }
        return ((Player) target).getEquipment().getCapeId() != player
                .getEquipment().getCapeId();
    }

    @Override
    public boolean processObjectClick1(WorldObject object) {
        switch (object.getId()) {
            case 42015:
            case 42018:
            case 42019:
            case 42020:
                World.getSoulWars().passBarrier(PlayerType.IN_GAME, player, object);
                return false;
            case 42021:
            case 42022:
                forceClose();
                removeControler();
                World.getSoulWars().resetPlayer(player, PlayerType.IN_GAME, false);
                player.getDialogueManager().startDialogue(SimpleNPCMessage.class, team == 0 ? 8528 : 8526, "You have left the game, therefore you will not be rewarded with any zeals.");
                return false;
            case 42023:
            case 42024:
                if (player.getAttackedByDelay() + 10000 > Utils.currentTimeMillis()) {
                    player.getPackets().sendGameMessage("You can't take while being in combat!");
                    return false;
                }
                player.getInventory().addItem(SoulWarsManager.BANDAGE_ID, 1);
                return false;
            case 42025:
            case 42026:
                if (player.getAttackedByDelay() + 10000 > Utils.currentTimeMillis()) {
                    player.getPackets().sendGameMessage("You can't take while being in combat!");
                    return false;
                }
                if (player.getInventory().getNumberOf(4053) > 0) {
                    player.getPackets().sendGameMessage("You can only have one barricade in your inventory!");
                    return false;
                }
                player.getInventory().addItem(4053, 1);
                return false;
            case 42027:
            case 42028:
                if (player.getAttackedByDelay() + 10000 > Utils.currentTimeMillis()) {
                    player.getPackets().sendGameMessage("You can't take while being in combat!");
                    return false;
                }
                player.getInventory().addItem(14644, 1);
                return false;
        }
        return true;
    }

    @Override
    public boolean processObjectClick2(WorldObject object) {
        switch (object.getId()) {
            case 42023:
            case 42024:
            case 42025:
            case 42026:
            case 42027:
            case 42028:
                if (player.getActionManager().getAction() != null) {
                    player.getPackets().sendGameMessage("You can't take while being in combat!");
                    return false;
                }
                if (!player.getInventory().hasFreeSlots()) {
                    player.getPackets().sendGameMessage("You don't have any space left for supplies!");
                    return false;
                }
                player.getTemporaryAttributtes().put("soul_wars_x", object.getId() == 42023 || object.getId() == 42024 ? SoulWarsManager.BANDAGE_ID : object.getId() == 42025 || object.getId() == 42026 ? 4053 : 14644);
                player.getPackets().sendInputIntegerScript(true, "How many would you like?");
                return false;
        }
        return true;
    }

    @Override
    public boolean login() {
        player.getControllerManager().startController(SoulWarsAreaController.class);
        return false;
    }

    @Override
    public boolean logout() {
        World.getSoulWars().resetPlayer(player, PlayerType.IN_GAME, true);
        return false;
    }
}
