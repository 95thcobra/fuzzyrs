package com.rs.content.customskills;

import com.rs.content.actions.skills.Skills;
import com.rs.player.Player;

import java.io.Serializable;

/**
 * @author John (FuzzyAvacado) on 12/19/2015.
 */
public class CustomSkillManager implements Serializable {

    public static final int MAXIMUM_EXP = 200000000;

    private static final long serialVersionUID = 5155017687055419685L;

    private int[] xp;
    private int[] levels;

    private transient Player player;

    public CustomSkillManager(Player player) {
        this.player = player;
        int size = CustomSkills.values().length;
        xp = new int[size];
        levels = new int[size];
        setDefaultLevelsAndXp();
    }

    private void setDefaultLevelsAndXp() {
        for (CustomSkills s : CustomSkills.values()) {
            levels[s.getSkillId()] = s.getBaseLevel();
            xp[s.getSkillId()] = getXPForLevel(s.getBaseLevel());
        }
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getXp(CustomSkills skill) {
        return xp[skill.getSkillId()];
    }

    public int getLevel(CustomSkills skill) {
        return levels[skill.getSkillId()];
    }

    public void setXp(CustomSkills skill, int xpValue) {
        xp[skill.getSkillId()] = xpValue;
    }

    public void addXpValue(CustomSkills skill, int xpValue) {
        setXp(skill, getXp(skill) + xpValue);
    }

    public void removeXp(CustomSkills skill, int xpValue) {
        setXp(skill, getXp(skill) - xpValue);
    }

    public void setLevel(CustomSkills skill, int levelValue) {
        levels[skill.getSkillId()] = levelValue;
        setXp(skill, getXPForLevel(levelValue));
    }

    public int getLevelForXp(CustomSkills skill) {
        final double exp = getXp(skill);
        int points = 0;
        int output;
        for (int lvl = 1; lvl <= skill.getSkillId(); lvl++) {
            points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
            output = (int) Math.floor(points / 4);
            if ((output - 1) >= exp)
                return lvl;
        }
        return skill.getMaxLevel();
    }

    public int getXPForLevel(CustomSkills skill) {
        int points = 0;
        int output = 0;
        int levelValue = getLevel(skill);
        for (int lvl = 1; lvl <= levelValue; lvl++) {
            points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
            if (lvl >= levelValue)
                return output;
            output = (int) Math.floor(points / 4);
        }
        return 0;
    }

    public int getXPForLevel(int level) {
        int points = 0;
        int output = 0;
        for (int lvl = 1; lvl <= level; lvl++) {
            points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
            if (lvl >= level)
                return output;
            output = (int) Math.floor(points / 4);
        }
        return 0;
    }

    public long getTotalXp() {
        long totalXp = 0;
        for (int i : levels) {
            totalXp += i;
        }
        return totalXp;
    }

    public int getTotalLevel() {
        int totalLevel = 0;
        for (CustomSkills s : CustomSkills.values()) {
            totalLevel += getXp(s);
        }
        return totalLevel;
    }

    public void addXp(final CustomSkills skill, int exp) {
        if (player.isXpLocked()) {
            addXpValue(skill, exp);
            return;
        }
        if (player.getEquipment().getRingId() == 773) {
            exp *= 2;
        }
        if (player.getPrestigeLevel() == 5) {
            exp *= 1.25;
        }
        if (player.getPrestigeLevel() == 10) {
            exp *= 1.35;
        }
        if (player.getEquipment().getGlovesId() == 20178) {
            exp *= 1.025;
        }
        if (Skills.isWeekend()) {
            exp *= 2;
        }
        if (player.getAuraManager().usingWisdom()) {
            exp *= 1.025;
        }
        final int oldLevel = getLevelForXp(skill);
        addXpValue(skill, exp);
        if (getXp(skill) > MAXIMUM_EXP) {
            setXp(skill, MAXIMUM_EXP);
        }
        final int newLevel = getLevelForXp(skill);
        final int levelDiff = newLevel - oldLevel;
        if (newLevel > oldLevel) {
            setLevel(skill, getLevel(skill) + levelDiff);
            player.getDialogueManager().startDialogue(CustomSkillLevelUp.class, skill);
            player.getQuestManager().checkCompleted();
        }
    }
}
