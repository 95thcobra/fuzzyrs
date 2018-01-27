package com.rs.server.net.handlers.inventory;

import com.rs.server.Server;
import com.rs.content.actions.skills.crafting.GemCutting;
import com.rs.content.actions.skills.crafting.LeatherCrafting;
import com.rs.content.actions.skills.firemaking.Firemaking;
import com.rs.content.actions.skills.fletching.Fletching;
import com.rs.content.actions.skills.herblore.Herblore;
import com.rs.content.dialogues.impl.ArmadylBattle;
import com.rs.content.dialogues.impl.FletchingD;
import com.rs.content.dialogues.impl.HerbloreD;
import com.rs.content.dialogues.impl.Polypore;
import com.rs.content.items.KitManager;
import com.rs.content.magic.MagicOnItem;
import com.rs.content.minigames.CrystalChest;
import com.rs.server.net.handlers.PacketHandler;
import com.rs.server.net.io.InputStream;
import com.rs.utils.Logger;
import com.rs.player.Inventory;
import com.rs.player.Player;
import com.rs.world.Animation;
import com.rs.world.ForceTalk;
import com.rs.world.Graphics;
import com.rs.world.item.Item;
import com.rs.world.npc.familiar.Familiar;
import com.sun.istack.internal.NotNull;

/**
 * @author John (FuzzyAvacado) on 12/21/2015.
 */
public class ItemOnItemHandler implements PacketHandler {

    @Override
    public boolean process(@NotNull Player player, Object... parameters) {
        InputStream stream = (InputStream) parameters[0];
        int itemUsedWithId = stream.readShort();
        int toSlot = stream.readShortLE128();
        int hash1 = stream.readInt();
        int hash2 = stream.readInt();
        int interfaceId = hash1 >> 16;
        int interfaceId2 = hash2 >> 16;
        int comp1 = hash1 & 0xFFFF;
        int fromSlot = stream.readShort();
        int itemUsedId = stream.readShortLE128();
        if (interfaceId == 192 && interfaceId2 == 679) {
            MagicOnItem.handleMagic(player, comp1, player.getInventory().getItem(toSlot));
        }
        if ((interfaceId2 == 747 || interfaceId2 == 662) && interfaceId == Inventory.INVENTORY_INTERFACE) {
            if (player.getFamiliar() != null) {
                player.getFamiliar().setSpecial(true);
                if (player.getFamiliar().getSpecialAttack() == Familiar.SpecialAttack.ITEM) {
                    if (player.getFamiliar().hasSpecialOn()) {
                        player.getFamiliar().submitSpecial(toSlot);
                    }
                }
            }
        }
        if (interfaceId == Inventory.INVENTORY_INTERFACE && interfaceId == interfaceId2 && !player.getInterfaceManager().containsInventoryInter()) {
            if (toSlot >= 28 || fromSlot >= 28)
                return true;
            final Item usedWith = player.getInventory().getItem(toSlot);
            final Item itemUsed = player.getInventory().getItem(fromSlot);
            if (itemUsed == null || usedWith == null
                    || itemUsed.getId() != itemUsedId
                    || usedWith.getId() != itemUsedWithId)
                return true;
            player.stopAll();
            if (!player.getControllerManager().canUseItemOnItem(itemUsed, usedWith))
                return true;
            if (KitManager.Kits.kits.containsKey(usedWith.getId())) {
                KitManager.createItem(player, itemUsed.getId(), usedWith.getId());
                return true;
            }
            final Fletching.Fletch fletch = Fletching.isFletching(usedWith, itemUsed);
            if (fletch != null) {
                player.getDialogueManager().startDialogue(FletchingD.class, fletch);
                return true;
            }
            if (itemUsed.getId() == CrystalChest.toothHalf() && usedWith.getId() == CrystalChest.loopHalf() || itemUsed.getId() == CrystalChest.loopHalf() && usedWith.getId() == CrystalChest.toothHalf()) {
                CrystalChest.makeKey(player);
                return true;
            }
            if (player.getEquipment().getWeaponId() == -1 && itemUsed.getId() == 21776 && usedWith.getId() == 1391) {
                if (player.getInventory().containsItem(21776, 250) && (player.getInventory().containsItem(1391, 1))) {
                    player.getInventory().deleteItem(21776, 250);
                    player.getInventory().deleteItem(1391, 1);
                    player.getInventory().addItem(21777, 1);
                    player.getDialogueManager().startDialogue(ArmadylBattle.class);
                    return true;
                }
            }
            if (player.getEquipment().getWeaponId() != -1 && itemUsed.getId() == 21776 && usedWith.getId() == 1391) {
                if (player.getInventory().containsItem(21776, 250)) {
                    player.getPackets().sendGameMessage("<col=B00000>Take off your weapon before doing this!");
                    return true;
                }
            }
            if (itemUsed.getId() == 21776 && usedWith.getId() == 1391) {
                if (!player.getInventory().containsItem(21776, 250) && (player.getInventory().containsItem(1391, 1))) {
                    player.getPackets().sendGameMessage("<col=B00000>You need 250 Shards of Armadyl to make an Armadyl Staff!");
                    return true;
                }
            }
            if (player.getEquipment().getWeaponId() == -1
                    && itemUsed.getId() == 22448 && usedWith.getId() == 22498) {
                if (player.getInventory().containsItem(22448, 3000)
                        && (player.getInventory().containsItem(554, 15000))) {
                    player.setNextAnimation(new Animation(15434));
                    player.setNextGraphics(new Graphics(2032));
                    player.getInventory().deleteItem(554, 15000);
                    player.getInventory().deleteItem(22448, 3000);
                    player.getInventory().deleteItem(22498, 1);
                    player.getInventory().addItem(22494, 1);
                    player.getDialogueManager().startDialogue(Polypore.class);
                    return true;
                }
            }
            if (player.getEquipment().getWeaponId() != -1
                    && itemUsed.getId() == 22448 && usedWith.getId() == 22498) {
                if (player.getInventory().containsItem(22448, 3000)
                        && (player.getInventory().containsItem(554, 15000))) {
                    player.getPackets()
                            .sendGameMessage(
                                    "<col=B00000>Take off your weapon before doing this!");
                    return true;
                }
            }
            if (itemUsed.getId() == 22448 && usedWith.getId() == 22498) {
                if (player.getInventory().containsItem(22448, 3000)
                        && (!player.getInventory().containsItem(554, 15000))) {
                    player.getPackets()
                            .sendGameMessage(
                                    "<col=B00000>You need 15000 Fire Rune's to make a Polypore Staff!");
                    return true;
                }
            }
            if (itemUsed.getId() == 22448 && usedWith.getId() == 22498) {
                if (!player.getInventory().containsItem(22448, 3000)
                        && (player.getInventory().containsItem(554, 15000))) {
                    player.getPackets()
                            .sendGameMessage(
                                    "<col=B00000>You need 3000 Polypore spore's to make a Polypore Staff!");
                    return true;
                }
            }
            if (itemUsed.getId() == 22448 && usedWith.getId() == 22498) {
                if (!player.getInventory().containsItem(22448, 3000)
                        && (!player.getInventory().containsItem(554, 15000))) {
                    player.getPackets()
                            .sendGameMessage(
                                    "<col=B00000>You need 3000 Polypore spore's and 15000 fire runes to make a Polypore Staff!");
                    return true;
                }
            }
            if (itemUsed.getId() == 11710 || usedWith.getId() == 11712
                    || usedWith.getId() == 11714) {
                if (player.getInventory().containsItem(11710, 1)
                        && player.getInventory().containsItem(11712, 1)
                        && player.getInventory().containsItem(11714, 1)) {
                    player.getInventory().deleteItem(11710, 1);
                    player.getInventory().deleteItem(11712, 1);
                    player.getInventory().deleteItem(11714, 1);
                    player.getInventory().addItem(11690, 1);
                    player.getPackets().sendGameMessage(
                            "You made a godsword blade.");
                }
            }
            if (itemUsed.getId() == 11286 || usedWith.getId() == 1540) {
                if (player.getInventory().containsItem(11286, 1)
                        && player.getInventory().containsItem(1540, 1)) {
                    player.getInventory().deleteItem(11286, 1);
                    player.getInventory().deleteItem(1540, 1);
                    player.getInventory().addItem(11283, 1);
                    player.getPackets()
                            .sendGameMessage(
                                    "You add the Visage to the Shield, it's looking at you.");
                }
            }
            if (itemUsed.getId() == 11690 || usedWith.getId() == 11702) {
                if (player.getInventory().containsItem(11690, 1)
                        && player.getInventory().containsItem(11702, 1)) {
                    player.getInventory().deleteItem(11690, 1);
                    player.getInventory().deleteItem(11702, 1);
                    player.getInventory().addItem(11694, 1);
                    player.getPackets()
                            .sendGameMessage(
                                    "You attach the hilt to the blade and make an Armadyl godsword.");
                }
            }
            if (itemUsed.getId() == 11690 || usedWith.getId() == 11704) {
                if (player.getInventory().containsItem(11690, 1)
                        && player.getInventory().containsItem(11704, 1)) {
                    player.getInventory().deleteItem(11690, 1);
                    player.getInventory().deleteItem(11704, 1);
                    player.getInventory().addItem(11696, 1);
                    player.getPackets()
                            .sendGameMessage(
                                    "You attach the hilt to the blade and make an Bandos godsword.");
                }
            }
            if (itemUsed.getId() == 11690 || usedWith.getId() == 11706) {
                if (player.getInventory().containsItem(11690, 1)
                        && player.getInventory().containsItem(11706, 1)) {
                    player.getInventory().deleteItem(11690, 1);
                    player.getInventory().deleteItem(11706, 1);
                    player.getInventory().addItem(11698, 1);
                    player.getPackets()
                            .sendGameMessage(
                                    "You attach the hilt to the blade and make an Saradomin godsword.");
                }
            }
            if (itemUsed.getId() == 11690 || usedWith.getId() == 11708) {
                if (player.getInventory().containsItem(11690, 1)
                        && player.getInventory().containsItem(11708, 1)) {
                    player.getInventory().deleteItem(11690, 1);
                    player.getInventory().deleteItem(11708, 1);
                    player.getInventory().addItem(11700, 1);
                    player.getPackets()
                            .sendGameMessage(
                                    "You attach the hilt to the blade and make an Zamorak godsword.");
                }
            }
            if (itemUsed.getId() == 6055 || usedWith.getId() == 590) {
                if (player.getInventory().containsItem(6055, 1)
                        && player.getInventory().containsItem(590, 1)) {
                    player.getInventory().deleteItem(6055, 1);
                    player.setNextAnimation(new Animation(1835));
                    player.setNextGraphics(new Graphics(86));
                    player.setNextForceTalk(new ForceTalk(
                            "Nothin' like smokin' sum pod in tha nice soft wind."));
                    player.getPackets()
                            .sendGameMessage(
                                    "<col=ff0000>I still need to make it give bonuses, this will be announed when done!");
                }
            }
            final int herblore = Herblore.isHerbloreSkill(itemUsed, usedWith);
            if (herblore > -1) {
                player.getDialogueManager().startDialogue(HerbloreD.class,
                        herblore, itemUsed, usedWith);
                return true;
            }
            if (itemUsed.getId() == LeatherCrafting.NEEDLE.getId()
                    || usedWith.getId() == LeatherCrafting.NEEDLE.getId()) {
                if (LeatherCrafting
                        .handleItemOnItem(player, itemUsed, usedWith))
                    return true;
            }
            if (Firemaking.isFiremaking(player, itemUsed, usedWith))
                return true;
            else if (InventoryOptionsHandler.contains(1755, GemCutting.Gem.OPAL.getUncut(), itemUsed, usedWith)) {
                GemCutting.cut(player, GemCutting.Gem.OPAL);
            } else if (InventoryOptionsHandler.contains(1755, GemCutting.Gem.JADE.getUncut(), itemUsed, usedWith)) {
                GemCutting.cut(player, GemCutting.Gem.JADE);
            } else if (InventoryOptionsHandler.contains(1755, GemCutting.Gem.RED_TOPAZ.getUncut(), itemUsed,
                    usedWith)) {
                GemCutting.cut(player, GemCutting.Gem.RED_TOPAZ);
            } else if (InventoryOptionsHandler.contains(1755, GemCutting.Gem.SAPPHIRE.getUncut(), itemUsed,
                    usedWith)) {
                GemCutting.cut(player, GemCutting.Gem.SAPPHIRE);
            } else if (InventoryOptionsHandler.contains(1755, GemCutting.Gem.EMERALD.getUncut(), itemUsed,
                    usedWith)) {
                GemCutting.cut(player, GemCutting.Gem.EMERALD);
            } else if (InventoryOptionsHandler.contains(1755, GemCutting.Gem.RUBY.getUncut(), itemUsed, usedWith)) {
                GemCutting.cut(player, GemCutting.Gem.RUBY);
            } else if (InventoryOptionsHandler.contains(1755, GemCutting.Gem.DIAMOND.getUncut(), itemUsed,
                    usedWith)) {
                GemCutting.cut(player, GemCutting.Gem.DIAMOND);
            } else if (InventoryOptionsHandler.contains(1755, GemCutting.Gem.DRAGONSTONE.getUncut(), itemUsed,
                    usedWith)) {
                GemCutting.cut(player, GemCutting.Gem.DRAGONSTONE);
            } else if (itemUsed.getId() == 21369 && usedWith.getId() == 4151) {
                player.getInventory().deleteItem(21369, 1);
                player.getInventory().deleteItem(4151, 1);
                player.getInventory().addItem(21371, 1);
                player.getPackets()
                        .sendGameMessage(
                                "Good job, you have succesfully combined a whip and vine into a vine whip.");
            } else if (itemUsed.getId() == 4151 && usedWith.getId() == 21369) {
                player.getInventory().deleteItem(21369, 1);
                player.getInventory().deleteItem(4151, 1);
                player.getInventory().addItem(21371, 1);
                player.getPackets()
                        .sendGameMessage(
                                "Good job, you have succesfully combined a whip and vine into a vine whip.");
            } else if (itemUsed.getId() == 13734 && usedWith.getId() == 13754) {
                player.getInventory().deleteItem(13734, 1);
                player.getInventory().deleteItem(13754, 1);
                player.getInventory().addItem(13736, 1);
                player.getPackets()
                        .sendGameMessage(
                                "You have poured the holy elixir on a spirit shield making it unleash Blessed powers.");
            } else if (itemUsed.getId() == 13754 && usedWith.getId() == 13734) {
                player.getInventory().deleteItem(13734, 1);
                player.getInventory().deleteItem(13754, 1);
                player.getInventory().addItem(13736, 1);
                player.getPackets()
                        .sendGameMessage(
                                "You have poured the holy elixir on a spirit shield making it unleash Blessed powers.");
            } else if (itemUsed.getId() == 13736 && usedWith.getId() == 13748) {
                player.getInventory().deleteItem(13736, 1);
                player.getInventory().deleteItem(13748, 1);
                player.getInventory().addItem(13740, 1);
                player.getPackets()
                        .sendGameMessage(
                                "You force the sigil upon the blessed spirit shield making it unleash Divine Powers.");
            } else if (itemUsed.getId() == 13736 && usedWith.getId() == 13750) {
                player.getInventory().deleteItem(13736, 1);
                player.getInventory().deleteItem(13750, 1);
                player.getInventory().addItem(13742, 1);
                player.getPackets()
                        .sendGameMessage(
                                "You force the sigil upon the blessed spirit shield making it unleash Elysian Powers.");
            } else if (itemUsed.getId() == 13736 && usedWith.getId() == 13746) {
                player.getInventory().deleteItem(13736, 1);
                player.getInventory().deleteItem(13746, 1);
                player.getInventory().addItem(13738, 1);
                player.getPackets()
                        .sendGameMessage(
                                "You force the sigil upon the blessed spirit shield making it unleash Arcane Powers.");
            } else if (itemUsed.getId() == 13746 && usedWith.getId() == 13736) {
                player.getInventory().deleteItem(13736, 1);
                player.getInventory().deleteItem(13746, 1);
                player.getInventory().addItem(13738, 1);
                player.getPackets()
                        .sendGameMessage(
                                "You force the sigil upon the blessed spirit shield making it unleash Arcane Powers.");
            } else if (itemUsed.getId() == 13736 && usedWith.getId() == 13752) {
                player.getInventory().deleteItem(13736, 1);
                player.getInventory().deleteItem(13752, 1);
                player.getInventory().addItem(13744, 1);
                player.getPackets()
                        .sendGameMessage(
                                "You force the sigil upon the blessed spirit shield making it unleash Spectral Powers.");
            } else if (itemUsed.getId() == 13752 && usedWith.getId() == 13736) {
                player.getInventory().deleteItem(13736, 1);
                player.getInventory().deleteItem(13752, 1);
                player.getInventory().addItem(13744, 1);
                player.getPackets()
                        .sendGameMessage(
                                "You force the sigil upon the blessed spirit shield making it unleash Spectral Powers.");
            } else if (InventoryOptionsHandler.contains(1755, GemCutting.Gem.ONYX.getUncut(), itemUsed, usedWith)) {
                GemCutting.cut(player, GemCutting.Gem.ONYX);
            } else {
                player.getPackets().sendGameMessage(
                        "Nothing interesting happens.");
            }
            if (Server.getInstance().getSettingsManager().getSettings().isDebug()) {
                Logger.info("ItemHandler", "Used:" + itemUsed.getId()
                        + ", With:" + usedWith.getId());
            }
        }
        return false;
    }
}
