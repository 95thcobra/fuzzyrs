package com.rs.content.customskills;

import com.rs.content.dialogues.Dialogue;
import com.rs.world.Graphics;

/**
 * @author John (FuzzyAvacado) on 3/11/2016.
 */
public class CustomSkillLevelUp extends Dialogue {

    @Override
    public void start() {
        CustomSkills skill = (CustomSkills) parameters[0];
        final int level = player.getCustomSkills().getLevel(skill);
        final String name = skill.getName();
        player.setNextGraphics(new Graphics(199));
        if (level == 99 || level == 120) {
            player.setNextGraphics(new Graphics(1765));
        }
        player.getPackets().sendGameMessage(
                "You've just advanced a" + (name.startsWith("A") ? "n" : "")
                        + " " + name + " level! You are now level " + level
                        + ".");
        player.getPackets().sendMusicEffect(30);
    }

    @Override
    public void run(int interfaceId, int componentId) {
        end();
    }

    @Override
    public void finish() {

    }
}
