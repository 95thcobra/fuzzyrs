package com.rs.content.dialogues.impl;

import com.rs.content.actions.skills.Skills;
import com.rs.content.dialogues.Dialogue;

public class SkillGone extends Dialogue {

    private int npcId;

    @Override
    public void start() {
        npcId = (Integer) parameters[0];
        sendNPCDialogue(
                npcId,
                9827,
                "Hey there "
                        + player.getUsername()
                        + ", have you ever heard of resetting a skill? Well... here you can! Be careful though, it can not be undone.");
    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        if (stage == -1) {
            final int option;
            sendOptionsDialogue("Pick a Skill to <col=ff0000>RESET</col>",
                    "Attack", "Strength", "Defence", "Prayer",
                    "More Options...");
            stage = 2;
        } else if (stage == 2) {
            if (componentId == OPTION_1) {
                player.getSkills().set(0, 1);
                player.getSkills().setXp(0, Skills.getXPForLevel(1)); // Attack
                end();
            }
            if (componentId == OPTION_2) {
                player.getSkills().set(2, 1);
                player.getSkills().setXp(2, Skills.getXPForLevel(1)); // Strength
                end();
            }
            if (componentId == OPTION_3) {
                player.getSkills().set(1, 1);
                player.getSkills().setXp(1, Skills.getXPForLevel(1)); // Defence
                end();
            }
            if (componentId == OPTION_4) {
                player.getSkills().set(5, 1);
                player.getSkills().setXp(5, Skills.getXPForLevel(1)); // Prayer
                end();
            }
            if (componentId == OPTION_5) {
                sendOptionsDialogue("Pick a Skill to <col=ff0000>RESET</col>",
                        "Magic", "Ranged", "Constitution", "Summoning",
                        "<col=ff0000>Reset all</col>");
                stage = 4;
            }
        } else if (stage == 4) {
            if (componentId == OPTION_1) {
                player.getSkills().set(6, 1);
                player.getSkills().setXp(6, Skills.getXPForLevel(1)); // Magic
                end();
            }
            if (componentId == OPTION_2) {
                player.getSkills().set(4, 1);
                player.getSkills().setXp(4, Skills.getXPForLevel(1)); // Ranged
                end();
            }
            if (componentId == OPTION_3) {
                player.getSkills().set(3, 1);
                player.getSkills().setXp(3, Skills.getXPForLevel(1)); // Constitution
                end();
            }
            if (componentId == OPTION_4) {
                player.getSkills().set(5, 1);
                player.getSkills().setXp(23, Skills.getXPForLevel(1)); // Summoning
                end();
            }
            if (componentId == OPTION_5) {
                player.getSkills().set(0, 1);
                player.getSkills().setXp(0, Skills.getXPForLevel(1));
                player.getSkills().set(1, 1);
                player.getSkills().setXp(1, Skills.getXPForLevel(1));
                player.getSkills().set(2, 1);
                player.getSkills().setXp(2, Skills.getXPForLevel(1));
                player.getSkills().set(3, 1);
                player.getSkills().setXp(3, Skills.getXPForLevel(1));
                player.getSkills().set(3, 1);
                player.getSkills().setXp(4, Skills.getXPForLevel(1));
                player.getSkills().set(4, 1);
                player.getSkills().setXp(5, Skills.getXPForLevel(1));
                player.getSkills().set(5, 1);
                player.getSkills().setXp(6, Skills.getXPForLevel(1));
                player.getSkills().set(6, 1);
                player.getSkills().setXp(7, Skills.getXPForLevel(1));
                player.getSkills().set(7, 1);
                player.getSkills().setXp(8, Skills.getXPForLevel(1));
                player.getSkills().set(8, 1);
                player.getSkills().setXp(9, Skills.getXPForLevel(1));
                player.getSkills().set(9, 1);
                player.getSkills().setXp(10, Skills.getXPForLevel(1));
                player.getSkills().set(10, 1);
                player.getSkills().setXp(11, Skills.getXPForLevel(1));
                player.getSkills().set(11, 1);
                player.getSkills().setXp(12, Skills.getXPForLevel(1));
                player.getSkills().set(12, 1);
                player.getSkills().setXp(13, Skills.getXPForLevel(1));
                player.getSkills().set(13, 1);
                player.getSkills().setXp(14, Skills.getXPForLevel(1));
                player.getSkills().set(14, 1);
                player.getSkills().setXp(15, Skills.getXPForLevel(1));
                player.getSkills().set(15, 1);
                player.getSkills().setXp(16, Skills.getXPForLevel(1));
                player.getSkills().set(16, 1);
                player.getSkills().setXp(17, Skills.getXPForLevel(1));
                player.getSkills().set(17, 1);
                player.getSkills().setXp(18, Skills.getXPForLevel(1));
                player.getSkills().set(18, 1);
                player.getSkills().setXp(19, Skills.getXPForLevel(1));
                player.getSkills().set(19, 1);
                player.getSkills().setXp(20, Skills.getXPForLevel(1));
                player.getSkills().set(20, 1);
                player.getSkills().setXp(21, Skills.getXPForLevel(1));
                player.getSkills().set(21, 1);
                player.getSkills().setXp(22, Skills.getXPForLevel(1));
                player.getSkills().set(22, 1);
                player.getSkills().setXp(23, Skills.getXPForLevel(1));
                player.getSkills().set(23, 1);
                player.getSkills().setXp(24, Skills.getXPForLevel(1));
                player.getSkills().set(24, 1);
                player.getSkills().setXp(25, Skills.getXPForLevel(1));
                player.getSkills().set(25, 1);
                end();
            }
        } else if (stage == 3) {
            end();
        }
    }

    @Override
    public void finish() {

    }

}