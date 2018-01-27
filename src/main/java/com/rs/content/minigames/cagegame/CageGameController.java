package com.rs.content.minigames.cagegame;

/**
 * @author John (FuzzyAvacado) on 1/7/2016.
 */

import com.rs.content.dialogues.types.SimpleMessage;
import com.rs.utils.Utils;
import com.rs.player.controlers.Controller;
import com.rs.world.Animation;
import com.rs.entity.Entity;
import com.rs.world.WorldTile;
import com.rs.task.worldtask.WorldTask;
import com.rs.task.worldtask.WorldTasksManager;

/**
 * @author Tyler
 */
public class CageGameController extends Controller {

    private boolean forfeit = false;

    @Override
    public void start() {
        CageGame.addLobbyPlayer(player);
        player.sendMessage("Welcome to the Cage Game");
        player.sendMessage(CageGame.getLobbyPlayers().size() == 1 ? "<col=3366FF><shad=000000>There is currently <shad=FFFFFF>[1]</shad><shad=000000> player in the lobby!"
                : "<col=3366FF><shad=000000>There is currently : <shad=FFFFFF>["
                + CageGame.getLobbyPlayers().size()
                + "]</shad><shad=000000> players in the lobby!");
        CageGame.setStartTime(Utils.currentTimeMillis());
    }

    @Override
    public void process() {
        CageGame.processCage();
    }

    @Override
    public boolean canEquip(int slotId, int itemId) {
        player.getPackets().sendGameMessage(
                "You cannot Equip/Remove anything during this minigame.");
        return false;
    }

    @Override
    public boolean canRemove(int slotId, int itemId) {
        player.getPackets().sendGameMessage(
                "You cannot Equip/Remove anything during this minigame.");
        return false;
    }

    @Override
    public boolean processButtonClick(int interfaceId, int componentId,
                                      int slotId, int packetId) {
        if (interfaceId == 182 && (componentId == 6 || componentId == 13)) {
            if (!forfeit && CageGame.isGameStarted() && CageGame.getGamePlayers().contains(player)) {
                player.getDialogueManager().startDialogue(SimpleMessage.class, "If you logout now, you'll forfeit the game!");
                forfeit = true;
            } else {
                CageGame.getGamePlayers().remove(player);
                player.getEquipment().reset();
                player.setCanPvp(false);
                player.getAppearance().generateAppearenceData();
                player.getEquipment().init();
                player.setNextWorldTile(CageGame.LOBBY);
                WorldTasksManager.schedule(new WorldTask() {
                    @Override
                    public void run() {
                        player.forceLogout();
                    }
                }, 3);
            }
            return false;
        }
        if (interfaceId == 192 || interfaceId == 193 || interfaceId == 430) {
            player.sendMessage("Magic has been disabled during this minigame!");
            return false;
        }
        return true;
    }

    @Override
    public boolean processMagicTeleport(WorldTile toTile) {
        player.getDialogueManager().startDialogue(SimpleMessage.class,
                "You can't leave the cage game!");
        return false;
    }

    @Override
    public boolean processItemTeleport(WorldTile toTile) {
        player.getDialogueManager().startDialogue(SimpleMessage.class,
                "You can't leave the cage game!");
        return false;
    }

    @Override
    public boolean processObjectTeleport(WorldTile toTile) {
        player.getDialogueManager().startDialogue(SimpleMessage.class,
                "You can't leave the cage game!");
        return false;
    }

    @Override
    public boolean canAttack(Entity target) {
        //Shouldn't happen, somehow if it does :L
        Integer tempCastSpell = (Integer) player.getTemporaryAttributtes().get("tempCastSpell");
        if (tempCastSpell != null) {
            player.sendMessage("You're trying to auto cast in the cage game! Magic has been DISABLED!");
            player.getCombatDefinitions().setAutoCastSpell(0);
            player.getCombatDefinitions().refreshAutoCastSpell();
            return false;
        }
        return true;
    }

    @Override
    public boolean login() {
        player.getEquipment().reset();
        player.setCanPvp(false);
        player.getAppearance().generateAppearenceData();
        player.getEquipment().init();
        player.setNextWorldTile(CageGame.LOBBY);
        return true;
    }

    @Override
    public boolean logout() {
        CageGame.getGamePlayers().remove(player);
        player.getEquipment().reset();
        player.getEquipment().init();
        player.setNextWorldTile(CageGame.LOBBY);
        return true;
    }

    @Override
    public boolean sendDeath() {
        player.lock(7);
        player.stopAll();
        WorldTasksManager.schedule(new WorldTask() {
            int loop;

            @Override
            public void run() {
                if (loop == 0) {
                    player.setNextAnimation(new Animation(836));
                } else if (loop == 1) {
                    player.getPackets().sendGameMessage("You've lost!");
                } else if (loop == 3) {
                    player.reset();
                    player.getEquipment().reset();
                    player.getAppearance().generateAppearenceData();
                    CageGame.getGamePlayers().remove(player);
                    player.setNextWorldTile(CageGame.LOBBY);
                    player.getControllerManager().getController().removeControler();
                    player.setCanPvp(false);
                    player.setNextAnimation(new Animation(-1));
                } else if (loop == 4) {
                    player.getPackets().sendMusicEffect(90);
                    stop();
                }
                loop++;
            }
        }, 0, 1);
        return false;
    }

}
