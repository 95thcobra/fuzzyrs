package com.rs.content.dialogues.impl;

import com.rs.content.dialogues.Dialogue;
import com.rs.player.Player;
import com.rs.world.task.worldtask.WorldTask;
import com.rs.world.task.worldtask.WorldTasksManager;

import java.text.DecimalFormat;

public class MoneyVault extends Dialogue {

    int coins = 995;
    private int npcId;

    public static String FormatNumber(final long count) {
        return new DecimalFormat("#,###,##0").format(count).toString();
    }

    public static String FormatNumber(final int coins, final Player player) {
        return new DecimalFormat("#,###,##0").format(
                player.getInventory().getItems().getNumberOf(coins)).toString();
    }

    @Override
    public void start() {
        final String PDS = "<col=FF0000>" + player.getDisplayName() + "</col>";
        npcId = (Integer) parameters[0];
        sendPlayerDialogue(9827, "I'd like to open my Money Vault?");
        stage = 3;
    }

    @Override
    public void run(final int interfaceId, final int componentId) {

        if (stage == 3) {
            sendNPCDialogue(
                    npcId,
                    9827,
                    "You corrently have <col=B00000>"
                            + FormatNumber(player.count)
                            + " Coins</col> in your Money Vault, <col=FF0000>"
                            + player.getDisplayName()
                            + "</col>. Would you like to Withdraw or Add more money to your Vault?");
            stage = 4;

        } else if (stage == 4) {
            sendOptionsDialogue("Options", "Withdraw Money from Vault",
                    "Add Money To Vault");
            stage = 5;

        } else if (stage == 5) {
            if (componentId == OPTION_1) {
                end();
                player.lock();
                WorldTasksManager.schedule(new WorldTask() {
                    int step;

                    @Override
                    public void run() {
                        if (step == 1) {
                            player.getPackets().sendGameMessage(
                                    "<col=00FF00>Opening Withdraw interface..");
                        }
                        if (step == 3) {
                            player.getTemporaryAttributtes().put(
                                    "remove_X_money", 995);
                            player.getTemporaryAttributtes().put(
                                    "remove_money1", Boolean.TRUE);
                            player.getPackets()
                                    .sendRunScript(
                                            108,
                                            "                          Your Money Vault contains <col=FF0000>"
                                                    + FormatNumber(player.count)
                                                    + "</col> coins."
                                                    + "                           How many would you like to Withdraw?");
                            stop();
                        }
                        step++;
                    }
                }, 0, 1);
            } else if (componentId == OPTION_2) {
                end();
                player.lock();
                WorldTasksManager.schedule(new WorldTask() {
                    int step;

                    @Override
                    public void run() {
                        if (step == 1) {
                            player.getPackets().sendGameMessage(
                                    "<col=00FF00>Opening Deposit interface..");
                        }
                        if (step == 3) {
                            player.getTemporaryAttributtes().put(
                                    "remove_X_money", 995);
                            player.getTemporaryAttributtes().put("Add_money",
                                    Boolean.TRUE);
                            player.getPackets()
                                    .sendRunScript(
                                            108,
                                            "                          Your Money Vault contains <col=FF0000>"
                                                    + FormatNumber(player.count)
                                                    + "</col> coins."
                                                    + "                           and you have "
                                                    + FormatNumber(coins)
                                                    + " coins on you. How many would you like to Deposit");
                            stop();
                        }
                        step++;
                    }
                }, 0, 1);
            } else {
                end();
            }
        }
    }

    @Override
    public void finish() {

    }
}