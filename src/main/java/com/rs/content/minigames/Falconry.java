package com.rs.content.minigames;

import com.rs.content.actions.skills.Skills;
import com.rs.content.actions.skills.hunter.Hunter;
import com.rs.content.dialogues.types.SimpleMessage;
import com.rs.utils.Logger;
import com.rs.utils.Utils;
import com.rs.player.Player;
import com.rs.player.controlers.Controller;
import com.rs.world.Animation;
import com.rs.world.World;
import com.rs.world.WorldTile;
import com.rs.world.item.Item;
import com.rs.world.npc.NPC;
import com.rs.task.worldtask.WorldTask;
import com.rs.task.worldtask.WorldTasksManager;

/**
 * @author John (FuzzyAvacado) on 1/5/2016.
 */
public class Falconry extends Controller {

    public static boolean hasSpawnedKebbits;
    public int[] xp = {103, 132, 156};
    public int[] furRewards = {10125, 10115, 10127};
    public int[] levels = {43, 57, 69};

    public static void beginFalconry(Player player) {
        if ((player.getEquipment().getItem(3) != null && player.getEquipment().getItem(3).getId() == -1) || (player.getEquipment().getItem(5) != null && player.getEquipment().getItem(5).getId() == -1)) {
            player.getDialogueManager().startDialogue(SimpleMessage.class, "You need both hands free to use a falcon.");
            return;
        } else if (player.getSkills().getLevel(Skills.HUNTER) < 43) {
            player.getDialogueManager().startDialogue(SimpleMessage.class, "You need a Hunter level of at least 43 to use a falcon, come back later.");
            return;
        }
        player.getControllerManager().startController(Falconry.class);
    }

    public static void spawnKebbits() {
        World.spawnNPC(5098, new WorldTile(2379, 3600, 0), -1, false, false);
        World.spawnNPC(5098, new WorldTile(2379, 3596, 0), -1, false, false);
        World.spawnNPC(5098, new WorldTile(2372, 3593, 0), -1, false, false);
        World.spawnNPC(5098, new WorldTile(2370, 3594, 0), -1, false, false);
        World.spawnNPC(5098, new WorldTile(2372, 3598, 0), -1, false, false);
        //End of 1 npc
        World.spawnNPC(5099, new WorldTile(2374, 3590, 0), -1, false, false);
        World.spawnNPC(5099, new WorldTile(2384, 3592, 0), -1, false, false);
        World.spawnNPC(5099, new WorldTile(2379, 3603, 0), -1, false, false);
        World.spawnNPC(5099, new WorldTile(2371, 3604, 0), -1, false, false);
        World.spawnNPC(5099, new WorldTile(2377, 3596, 0), -1, false, false);
        //End of 2 npc
        World.spawnNPC(5099, new WorldTile(2377, 3596, 0), -1, false, false);
        World.spawnNPC(5099, new WorldTile(2377, 3591, 0), -1, false, false);
        World.spawnNPC(5099, new WorldTile(2380, 3603, 0), -1, false, false);
        World.spawnNPC(5099, new WorldTile(2380, 3609, 0), -1, false, false);
        World.spawnNPC(5099, new WorldTile(2370, 3595, 0), -1, false, false);
        //End of 3 npc
        hasSpawnedKebbits = true;
    }

    @Override
    public void start() {
        player.setNextAnimation(new Animation(1560));
        WorldTasksManager.schedule(new WorldTask() {
            @Override
            public void run() {
                player.setNextWorldTile(new WorldTile(2371, 3619, 0));
                if (hasSpawnedKebbits) {
                    Logger.info(Falconry.class, "Kebbit's already spawned.");
                } else {
                    spawnKebbits();
                    hasSpawnedKebbits = true;
                }
            }
        });
        player.getEquipment().getItems().set(3, new Item(10024, 1));
        player.getEquipment().refresh(3);
        player.getAppearance().generateAppearenceData();
        player.getDialogueManager().startDialogue(SimpleMessage.class, "Simply click on the target and try your luck.");
    }

    @Override
    public boolean canEquip(int slotId, int itemId) {
        return !(slotId == 3 || slotId == 5) || itemId == 10024;
    }

    @Override
    public void magicTeleported(int type) {
        forceClose();
    }

    @Override
    public void forceClose() {
        player.getEquipment().getItems().remove(3, new Item(10024, 1));
        player.getEquipment().refresh(3);
        player.getAppearance().generateAppearenceData();
        hasSpawnedKebbits = false;
        removeControler();
    }

    @Override
    public boolean processNPCClick1(final NPC npc) {
        player.setNextFaceEntity(npc);
        if (npc.getDefinitions().name.toLowerCase().contains("kebbit")) {
            if (player.getTemporaryAttributtes().get("falconReleased") != null) {
                player.getDialogueManager().startDialogue(SimpleMessage.class, "You cannot catch a kebbit without your falcon.");
                return false;
            }
            int level = levels[(npc.getId() - 5098)];
            if (proccessFalconAttack(npc)) {
                if (player.getSkills().getLevel(Skills.HUNTER) < level) {
                    player.getDialogueManager().startDialogue(SimpleMessage.class, "You need a Hunter level of " + level + " to capture this kebbit.");
                    return true;
                } else if (Hunter.isSuccessful(player, level, player1 -> {
                    if (player1.getEquipment().getGlovesId() == 10075)
                        return 3;
                    return 1;
                })) {
                    player.getEquipment().getItems().set(3, new Item(10023, 1));
                    player.getEquipment().refresh(3);
                    player.getAppearance().generateAppearenceData();
                    player.getTemporaryAttributtes().put("falconReleased", true);
                    WorldTasksManager.schedule(new WorldTask() {
                        @Override
                        public void run() {
                            World.sendProjectile(player, npc, 918, 41, 16, 31, 35, 16, 0);
                            WorldTasksManager.schedule(new WorldTask() {
                                @Override
                                public void run() {
                                    npc.transformIntoNPC(npc.getId() - 4);
                                    player.getTemporaryAttributtes().put("ownedFalcon", npc);
                                    player.getPackets().sendGameMessage("The falcon successfully swoops down and captures the kebbit.");
                                    npc.setRandomWalk(false);
                                    player.getHintIconsManager().addHintIcon(npc, 1, -1, false);

                                }
                            });
                        }
                    });
                } else {
                    player.getEquipment().getItems().set(3, new Item(10023, 1));
                    player.getEquipment().refresh(3);
                    player.getAppearance().generateAppearenceData();
                    player.getTemporaryAttributtes().put("falconReleased", true);
                    WorldTasksManager.schedule(new WorldTask() {
                        @Override
                        public void run() {
                            World.sendProjectile(player, npc, 918, 41, 16, 31, 35, 16, 0);
                            WorldTasksManager.schedule(new WorldTask() {
                                @Override
                                public void run() {
                                    World.sendProjectile(npc, player, 918, 41, 16, 31, 35, 16, 0);
                                    WorldTasksManager.schedule(new WorldTask() {
                                        @Override
                                        public void run() {
                                            player.getEquipment().getItems().set(3, new Item(10024, 1));
                                            player.getEquipment().refresh(3);
                                            player.getAppearance().generateAppearenceData();
                                            player.getTemporaryAttributtes().remove("falconReleased");
                                            player.getPackets().sendGameMessage("The falcon swoops down on the kebbit, but just barely misses catching it.");
                                        }
                                    });
                                }
                            }, Utils.getDistance(player, npc) > 3 ? 2 : 1);
                        }
                    });
                }
            }
        } else if (npc.getDefinitions().name.toLowerCase().contains("gyr falcon")) {
            NPC kill = (NPC) player.getTemporaryAttributtes().get("ownedFalcon");
            if (kill == null)
                return false;
            if (kill != npc) {
                player.getDialogueManager().startDialogue(SimpleMessage.class, "This isn't your kill!");
                return false;
            }
            npc.setRespawnTask();
            player.getInventory().addItem(new Item(furRewards[(npc.getId() - 5094)], 1));
            player.getInventory().addItem(new Item(526, 1));
            player.getSkills().addXp(Skills.HUNTER, xp[(npc.getId() - 5094)]);
            player.getPackets().sendGameMessage("You retrieve the falcon as well as the fur of the dead kebbit.");
            player.getHintIconsManager().removeUnsavedHintIcon();
            player.getEquipment().getItems().set(3, new Item(10024, 1));
            player.getEquipment().refresh(3);
            player.getAppearance().generateAppearenceData();
            npc.transformIntoNPC(npc.getId() + 4);
            player.getTemporaryAttributtes().remove("ownedFalcon");
            player.getTemporaryAttributtes().remove("falconReleased");
            return true;
        }
        return true;
    }

    private boolean proccessFalconAttack(NPC target) {
        int distanceX = player.getX() - target.getX();
        int distanceY = player.getY() - target.getY();
        int size = player.getSize();
        int maxDistance = 16;
        player.resetWalkSteps();
        if ((!player.clipedProjectile(target, false)) || distanceX > size + maxDistance || distanceX < -1 - maxDistance || distanceY > size + maxDistance || distanceY < -1 - maxDistance) {
            if (!player.addWalkStepsInteract(target.getX(), target.getY(), 2, size, true))
                return true;
        }
        return true;
    }
}
