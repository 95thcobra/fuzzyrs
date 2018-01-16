package com.rs.content.combat.pvp;

import com.rs.content.dialogues.types.SimpleMessage;
import com.rs.core.utils.Utils;
import com.rs.player.Player;

public class WildernessArtifacts {

    /*
     * By Tristam
     */

    public static boolean trade(Player player) {
        for (Artifacts artifacts : Artifacts.values()) {
            int amount = player.getInventory().getNumberOf(artifacts.getId());
            if (amount > 0) {
                String formattedNumber = Utils.getFormattedNumber(artifacts.getPrice() * amount);
                player.getDialogueManager().startDialogue(SimpleMessage.class, "You sold " + amount + " " + artifacts.getName() + " for a total of "
                        + formattedNumber);
                player.getInventory().deleteItem(artifacts.getId(), amount);
                player.getInventory().addItem(995, artifacts.getPrice() * amount);
            }
        }
        return false;
    }


    public enum Artifacts {

        AncientStatuette(5000000, 14876, "Ancient Statuette"),
        SerenStatuette(1000000, 14877, "Seren Statuette"),
        ArmadylStatuette(750000, 14878, "Armadyl Stauette"),
        ZamorakStatuette(500000, 14879, "Zamorak Statuette"),
        SaradominStatuette(400000, 14880, "Saradomin Statuette"),
        BandosStatuette(300000, 14881, "Bandos Statuette"),
        RubyChalice(250000, 14882, "Ruby Chalice"),
        GuthixianBrazier(200000, 14883, "Guthixian Brazier"),
        ArmadylTotem(150000, 14884, "Armadyl Totem"),
        ZamorakMedalion(100000, 14885, "Zamorak Medalion"),
        SaradominCarving(75000, 14886, "Saradomin Carving"),
        BandosScrimshaw(50000, 14887, "Bandos Scrimshaw"),
        SaradominAmphora(40000, 14888, "Saradomin Amphora"),
        AncientPsaltaryBridge(30000, 14889, "Ancient Psaltary Bridge"),
        BronzedDragonClaw(20000, 14890, "Bronzed Dragon Claw"),
        ThirdAgeCarafe(10000, 14891, "Third-Age Carafe"),
        BrokenStatueHeaddress(5000, 14892, "Broken Statue Headdress");



        private int price, id;
        private String name;

        Artifacts(int price, int id, String name) {
            this.price = price;
            this.id = id;
            this.name = name;
        }

        public int getPrice() {
            return price;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

    }


}