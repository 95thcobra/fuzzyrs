package com.rs.core.net.handlers.object;

import com.rs.content.actions.skills.cooking.Cooking;
import com.rs.content.actions.skills.firemaking.Bonfire;
import com.rs.content.actions.skills.prayer.GildedAltar;
import com.rs.content.actions.skills.runecrafting.Runecrafting;
import com.rs.content.actions.skills.smithing.Smithing;
import com.rs.content.dialogues.impl.CookingD;
import com.rs.content.dialogues.types.SimpleMessage;
import com.rs.content.ectofuntus.Ectofuntus;
import com.rs.core.cache.loaders.ObjectDefinitions;
import com.rs.core.net.handlers.PacketHandler;
import com.rs.core.settings.SettingsManager;
import com.rs.player.CoordsEvent;
import com.rs.player.Player;
import com.rs.player.combat.PlayerCombat;
import com.rs.world.Animation;
import com.rs.world.WorldObject;
import com.rs.world.item.Item;
import com.sun.istack.internal.NotNull;

/**
 * @author John (FuzzyAvacado) on 12/21/2015.
 */
public class ItemOnObjectHandler implements PacketHandler {
    @Override
    public boolean process(@NotNull Player player, Object... parameters) {
        final WorldObject object = (WorldObject) parameters[0];
        final int interfaceId = (int) parameters[1];
        final Item item = (Item) parameters[2];
        final int itemId = item.getId();
        final ObjectDefinitions objectDef = object.getDefinitions();
        if (player.getControllerManager().getController().processItemOnObject(object, item) || Ectofuntus.handleItemOnObject(player, itemId, object.getId())) {
            return true;
        }
        player.setCoordsEvent(new CoordsEvent(object, () -> {
            player.faceObject(object);
            if (itemId == 1438 && object.getId() == 2452) {
                Runecrafting.enterAirAltar(player);
            } else if (itemId == 1440 && object.getId() == 2455) {
                Runecrafting.enterEarthAltar(player);
            } else if (itemId == 1442 && object.getId() == 2456) {
                Runecrafting.enterFireAltar(player);
            } else if (itemId == 1444 && object.getId() == 2454) {
                Runecrafting.enterWaterAltar(player);
            } else if (itemId == 1446 && object.getId() == 2457) {
                Runecrafting.enterBodyAltar(player);
            } else if (itemId == 1448 && object.getId() == 2453) {
                Runecrafting.enterMindAltar(player);
            } else if (itemId == 2 && object.getId() == 6) {
                player.getDwarfCannon().loadDwarfCannon(object);
            } else if (object.getId() == 733 || object.getId() == 64729) {
                player.setNextAnimation(new Animation(PlayerCombat
                        .getWeaponAttackEmote(-1, 0)));
                ObjectHandler.slashWeb(player, object);
            } else if (object.getId() == 48803 && itemId == 954) {
                if (player.isKalphiteLairSetted())
                    return;
                player.getInventory().deleteItem(954, 1);
                player.setKalphiteLair();
            } else if (object.getId() == 48802 && itemId == 954) {
                if (player.isKalphiteLairEntranceSetted())
                    return;
                player.getInventory().deleteItem(954, 1);
                player.setKalphiteLairEntrance();
            } else if (itemId == 526
                    && objectDef.containsOption(0, "Pray-at")) {
                GildedAltar.bonestoOffer.offerprayerGod(player, item);
            } else if (itemId == 532
                    && objectDef.containsOption(0, "Pray-at")) {
                GildedAltar.bonestoOffer.offerprayerGod(player, item);
            } else if (itemId == 536
                    && objectDef.containsOption(0, "Pray-at")) {
                GildedAltar.bonestoOffer.offerprayerGod(player, item);
            } else if (itemId == 4834
                    && objectDef.containsOption(0, "Pray-at")) {
                GildedAltar.bonestoOffer.offerprayerGod(player, item);
            } else if (itemId == 6729
                    && objectDef.containsOption(0, "Pray-at")) {
                GildedAltar.bonestoOffer.offerprayerGod(player, item);
            } else if (itemId == 18830
                    && objectDef.containsOption(0, "Pray-at")) {
                GildedAltar.bonestoOffer.offerprayerGod(player, item);
            } else {
                switch (objectDef.name.toLowerCase()) {
                    case "anvil":
                        final Smithing.ForgingBar bar = Smithing.ForgingBar.forId(itemId);
                        if (bar != null) {
                            Smithing.ForgingInterface.sendSmithingInterface(player, bar);
                        }
                        break;
                    case "fire":
                        if (objectDef.containsOption(4, "Add-logs")
                                && Bonfire.addLog(player, object, item))
                            return;
                    case "range":
                    case "cooking range":
                    case "stove":
                        final Cooking.Cookables cook = Cooking.isCookingSkill(item);
                        if (cook != null) {
                            player.getDialogueManager().startDialogue(
                                    CookingD.class, cook, object);
                            return;
                        }
                        player.getDialogueManager()
                                .startDialogue(
                                        SimpleMessage.class,
                                        "You can't cook that on a "
                                                + (objectDef.name
                                                .equals("Fire") ? "fire"
                                                : "range") + ".");
                        break;
                    default:
                        player.getPackets().sendGameMessage(
                                "Nothing interesting happens.");
                        break;
                }
                if (SettingsManager.getSettings().DEBUG) {
                    System.out.println("Item on object: " + object.getId());
                }
            }
        }, objectDef.getSizeX(), objectDef.getSizeY(), object.getRotation()));
        return false;
    }
}
