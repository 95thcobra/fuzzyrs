package com.rs.content.customskills.sailing;

import com.rs.server.Server;
import com.rs.content.actions.impl.Rest;
import com.rs.content.customskills.CustomSkills;
import com.rs.content.customskills.sailing.ships.PlayerShip;
import com.rs.content.customskills.sailing.ships.ShipStorage;
import com.rs.content.customskills.sailing.ships.Ships;
import com.rs.core.cores.CoresManager;
import com.rs.core.net.decoders.impl.WorldPacketsDecoder;
import com.rs.core.net.handlers.button.ButtonHandler;
import com.rs.core.utils.Logger;
import com.rs.core.utils.Utils;
import com.rs.player.Bank;
import com.rs.player.content.FadingScreen;
import com.rs.player.controlers.Controller;
import com.rs.world.*;
import com.rs.world.npc.NPC;

/**
 * @author John (FuzzyAvacado) on 12/17/2015.
 */
public class SailingController extends Controller {

    private static final String[] SAYINGS = {
            "Yar har fiddle de de.",
            "The ocean is so beautiful this time of year!",
            "Geronimo!",
            "Allons-y!",
            "Touch my loot, feel my boot.",
            "Touch me parrot, me bite your carrot.",
            "Save thee Bilge rats!",
            "Let's drink grog before the fog.",
            "Shiver me timbers! Me wooden leg has termites.",
            "Avast! Pull Me Mast!",
            "Surrrrrender the booty!"
    };

    private SailingMap.MapRequirements requirements;
    private PlayerShip playerShip;
    private Ships ship;
    private WorldObject shipObject;
    private boolean sitting;
    private int restingIndex;
    private ShipStorage storage;
    private long fadeTime;

    /*
     * TODO clean up region once done sailing.
     */
    /**
     * The region base.
     */
    private int[] regionBase;
    /**
     * The base location of the region.
     */
    private WorldTile base;

    @Override
    public void start() {
        requirements = (SailingMap.MapRequirements) getArguments()[0];
        playerShip = (PlayerShip) getArguments()[1];
        ship = playerShip.getShip();
        restingIndex = Utils.random(Rest.REST_DEFS.length);
        storage = new ShipStorage(player, ship.getStorageSize());
        refreshStages();
        sendInterfaces();
    }

    @Override
    public void sendInterfaces() {
        sendActionButtonText();
        player.getInterfaceManager().closeCombatStyles();
        player.getInterfaceManager().closeMagicBook();
        player.getInterfaceManager().closeEquipment();
        player.getInterfaceManager().closePrayerBook();
        player.getInterfaceManager().closeEmotes();
        player.getInterfaceManager().closeSkills();
    }

    public int getStage() {
        return (Integer) getArguments()[2];
    }

    public void setStage(final int stage) {
        getArguments()[2] = stage;
    }

    private void refreshStages() {
        switch (getStage()) {
            case 0:
                player.lock();
                fadeTime = FadingScreen.fade(player);
                CoresManager.SLOW_EXECUTOR.execute(() -> {
                    try {
                        regionBase = RegionBuilder.findEmptyChunkBound(8, 8);
                        base = new WorldTile(regionBase[0] << 3, regionBase[1] << 3, 1);
                        RegionBuilder.copyAllPlanesMap(375, 312, regionBase[0], regionBase[1], 8, 8);
                        FadingScreen.unfade(player, fadeTime, () -> {
                            World.spawnObject(shipObject = new WorldObject(ship.getId(), 10, -1, base.getX() + 30, base.getY() + 27, 0), false);
                            player.setNextWorldTile(new WorldTile(shipObject.getX() + ship.getXOffset(), shipObject.getY() + ship.getYOffset(), shipObject.getPlane()));
                            sit();
                            player.faceObject(shipObject);
                            player.setLargeSceneView(true);
                            player.setForceMultiArea(true);
                            update(1);
                        });
                    } catch (final Throwable e) {
                        Logger.handle(e);
                    }
                });
                break;
            case 1:
                player.setNextForceTalk(new ForceTalk(getRandomPhrase()));
                spawnFishingSpots();
                break;
            case 5:
                fadeTime = FadingScreen.fade(player);
                CoresManager.SLOW_EXECUTOR.execute(() -> {
                    try {
                        FadingScreen.unfade(player, fadeTime, () -> {
                            player.setNextWorldTile(requirements.getTile());
                            player.setLargeSceneView(false);
                            player.setForceMultiArea(false);
                            player.setNextAnimation(new Animation(-1));
                            player.getEmotesManager().setNextEmoteEnd();
                            player.getAppearance().setRenderEmote(-1);
                            player.sendMessage("You have arrived at " + requirements.name().toLowerCase().replace("_", " ") + ".");
                            finishStorage();
                            player.getCustomSkills().addXp(CustomSkills.SAILING, 100 * Server.getInstance().getSettingsManager().getSettings().getSkillingXpRate());
                            player.unlock();
                            this.removeControler();
                            player.getInterfaceManager().sendInterfaces();
                            ButtonHandler.sendCustomText506(player);
                        });
                    } catch (final Throwable e) {
                        Logger.handle(e);
                    }
                });
                break;
        }
    }

    private void finishStorage() {
        if (storage.getSize() > 0) {
            if (player.getInventory().getFreeSlots() >= storage.getSize()) {
                storage.takeAll();
                player.sendMessage("Your storage items have been added to your inventory!");
            } else if (storage.getSize() > player.getInventory().getFreeSlots() && Bank.MAX_BANK_SIZE - player.getBank().getBankSize() > storage.getSize()) {
                player.getBank().addItems(storage.getStorageItems().getItems(), true);
                storage.empty();
                player.sendMessage("Your storage items have been added to your bank!");
            } else {
                storage.dropStorage();
                player.sendMessage("Your storage items have been dropped on the ground!");
            }
        }
    }

    private void sit() {
        if (!sitting) {
            player.setNextAnimation(new Animation(Rest.REST_DEFS[restingIndex][0]));
            player.getAppearance().setRenderEmote(Rest.REST_DEFS[restingIndex][1]);
        } else {
            player.setNextAnimation(new Animation(Rest.REST_DEFS[restingIndex][2]));
            player.getEmotesManager().setNextEmoteEnd();
            player.getAppearance().setRenderEmote(-1);
        }
        sitting = !sitting;
        sendInterfaces();
    }

    private void spawnFishingSpots() {
        World.spawnNPC(8842, new WorldTile(shipObject.getX() + ship.getXOffset() - 1, shipObject.getY() + ship.getYOffset() - 1, shipObject.getPlane()), -1, false);
        World.spawnNPC(313, new WorldTile(shipObject.getX() + ship.getXOffset() + 1, shipObject.getY() + ship.getYOffset() + 1, shipObject.getPlane()), -1, false);
        World.spawnNPC(6267, new WorldTile(shipObject.getX() + ship.getXOffset() + 1, shipObject.getY() + ship.getYOffset() - 1, shipObject.getPlane()), -1, false);
        World.spawnNPC(328, new WorldTile(shipObject.getX() + ship.getXOffset() - 1, shipObject.getY() + ship.getYOffset() + 1, shipObject.getPlane()), -1, false);
        World.spawnNPC(8841, new WorldTile(shipObject.getX() + ship.getXOffset(), shipObject.getY() + ship.getYOffset(), shipObject.getPlane()), -1, false);
    }

    @Override
    public boolean processNPCClick1(NPC npc) {
        if (sitting) {
            sit();
        }
        return true;
    }

    public void update(int stage) {
        setStage(stage);
        refreshStages();
    }

    public boolean canMove(int dir) {
        return false;
    }

    @Override
    public boolean processButtonClick(int interfaceId, int componentId, int slotId, int packetId) {
        switch (interfaceId) {
            case 506:
                if (componentId == 2) {
                    update(5);
                } else if (componentId == 4) {
                    sit();
                } else if (componentId == 6) {
                    storage.open();
                } else if (componentId == 8) {
                    update(0);
                } else if (componentId == 10) {
                } else if (componentId == 12) {
                } else if (componentId == 14) {
                }
                return false;
            case 665:
                if (componentId == 0) {
                    if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
                        storage.addItem(slotId, 1);
                    } else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
                        storage.addItem(slotId, 5);
                    } else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET) {
                        storage.addItem(slotId, 10);
                    } else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET) {
                        storage.addItem(slotId, Integer.MAX_VALUE);
                    } else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET) {
                        player.getTemporaryAttributtes().put("storage_item_X_Slot", slotId);
                        player.getTemporaryAttributtes().remove("storage_isRemove");
                        player.getPackets().sendRunScript(108, "Enter Amount:");
                    } else if (packetId == WorldPacketsDecoder.ACTION_BUTTON9_PACKET) {
                        player.getInventory().sendExamine(slotId);
                    }
                }
                return false;
            case 671:
                if (componentId == 27) {
                    if (packetId == WorldPacketsDecoder.ACTION_BUTTON1_PACKET) {
                        storage.removeItem(slotId, 1);
                    } else if (packetId == WorldPacketsDecoder.ACTION_BUTTON2_PACKET) {
                        storage.removeItem(slotId, 5);
                    } else if (packetId == WorldPacketsDecoder.ACTION_BUTTON3_PACKET) {
                        storage.removeItem(slotId, 10);
                    } else if (packetId == WorldPacketsDecoder.ACTION_BUTTON4_PACKET) {
                        storage.removeItem(slotId, Integer.MAX_VALUE);
                    } else if (packetId == WorldPacketsDecoder.ACTION_BUTTON5_PACKET) {
                        player.getTemporaryAttributtes().put("storage_item_X_Slot", slotId);
                        player.getTemporaryAttributtes().put("storage_isRemove", Boolean.TRUE);
                        player.getPackets().sendRunScript(108, "Enter Amount:");
                    }
                } else if (componentId == 29) {
                    storage.takeAll();
                }
                return false;
            default:
                return true;
        }
    }

    @Override
    public boolean canSummonFamiliar() {
        return false;
    }

    public ShipStorage getStorage() {
        return storage;
    }

    private void sendActionButtonText() {
        player.getPackets().sendIComponentText(506, 0, "Sailing");
        player.getPackets().sendIComponentText(506, 2, "Sail");
        player.getPackets().sendIComponentText(506, 4, sitting ? "Stand" : "Sit");
        player.getPackets().sendIComponentText(506, 6, "Storage");
        player.getPackets().sendIComponentText(506, 8, "Restart");
        player.getPackets().sendIComponentText(506, 10, "");
        player.getPackets().sendIComponentText(506, 12, "");
        player.getPackets().sendIComponentText(506, 14, "");
    }

    @Override
    public boolean processMagicTeleport(WorldTile tile) {
        player.getPackets().sendGameMessage("You can not teleport while sailing!");
        return false;
    }

    /*
     * return remove controller
	 */
    @Override
    public boolean login() {
        start();
        return false;
    }

    /*
     * return remove controller
     */
    @Override
    public boolean logout() {
        return false;
    }

    private String getRandomPhrase() {
        return SAYINGS[(int) (Math.random() * (SAYINGS.length - 1))];
    }
}
