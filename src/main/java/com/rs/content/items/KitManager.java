package com.rs.content.items;

/**
 * @author John (FuzzyAvacado) on 12/13/2015.
 */

import com.rs.player.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Corleone
 */

public class KitManager {

    public static void createItem(Player player, int kitId, int armourPiece) {
        Kits kit = Kits.forId(armourPiece);
        if (player.getInventory().containsItem(kit.getKit(), 1) && player.getInventory().containsItem(kit.getArmourPiece(), 1)) {
            player.getInventory().deleteItem(kit.getKit(), 1);
            player.getInventory().deleteItem(kit.getArmourPiece(), 1);
            player.getInventory().addItem(kit.getProduct(), 1);
        } else {
            player.getPackets().sendGameMessage("Nothing interesting happens.");
        }
    }

    public static void splitItem(Player player, int itemId) {
        KitsMade kitMade = KitsMade.forId(itemId);
        if (player.getInventory().getFreeSlots() >= 2) {
            player.getInventory().deleteItem(kitMade.getMadeId(), 1);
            player.getInventory().addItem(kitMade.getKitId(), 1);
            player.getInventory().addItem(kitMade.getItemUsedId(), 1);
        } else {
            player.getPackets().sendGameMessage("You need to have at least 2 free spaces to do that.");
        }
    }

    public enum Kits {
        FURY_ORNAMENT(6585, 19333, 19335),
        DRAGON_PLATEBODY_OR(14479, 19350, 19337),
        DRAGON_PLATELEGS_OR(4087, 19348, 19338),
        DRAGON_PLATESKIR_OR(4585, 19348, 19339),
        DRAGON_SQ_SHIELD_OR(1187, 19352, 19340),
        DRAGON_FULL_HELM_OR(11335, 19346, 19336),
        DRAGON_KITESHIELD_OR(24365, 25312, 25320);

        public static Map<Integer, Kits> kits = new HashMap<>();

        static {
            for (Kits kit : Kits.values()) {
                kits.put(kit.armourPiece, kit);
            }
        }

        private int kit;
        private int armourPiece;
        private int product;

        Kits(int armourPiece, int kit, int product) {
            this.kit = kit;
            this.armourPiece = armourPiece;
            this.product = product;
        }

        public static Kits forId(int id) {
            return kits.get(id);
        }

        public int getKit() {
            return kit;
        }

        public int getArmourPiece() {
            return armourPiece;
        }

        public int getProduct() {
            return product;
        }
    }

    public enum KitsMade {

        FURY_ORNAMENT(19335, 6585, 19333),
        DRAGON_PLATEBODY_OR(19337, 14479, 19350),
        DRAGON_PLATELEGS_OR(19338, 4087, 19348),
        DRAGON_PLATESKIR_OR(19339, 4585, 19348),
        DRAGON_SQ_SHIELD_OR(19352, 1187, 19340),
        DRAGON_FULL_HELM_OR(19336, 11335, 19346),
        DRAGON_KITESHIELD_OR(25320, 24365, 25312);

        public static Map<Integer, KitsMade> kitsMade = new HashMap<>();

        static {
            for (KitsMade kitMade : KitsMade.values()) {
                kitsMade.put(kitMade.madeId, kitMade);
            }
        }

        private int madeId;
        private int itemUsedId;
        private int kitId;

        KitsMade(int madeId, int itemUsedId, int kitId) {
            this.madeId = madeId;
            this.itemUsedId = itemUsedId;
            this.kitId = kitId;
        }

        public static KitsMade forId(int id) {
            return kitsMade.get(id);
        }

        public int getKitId() {
            return kitId;
        }

        public int getItemUsedId() {
            return itemUsedId;
        }

        public int getMadeId() {
            return madeId;
        }
    }
}
