package com.rs.content.customskills;

/**
 * @author John (FuzzyAvacado) on 12/19/2015.
 */
public enum CustomSkills {

    SAILING("Sailing", 0, 1, 120);

    private final String name;
    private final int skillId;
    private final int baseLevel;
    private final int maxLevel;

    CustomSkills(String name, int skillId, int baseLevel, int maxLevel) {
        this.name = name;
        this.skillId = skillId;
        this.baseLevel = baseLevel;
        this.maxLevel = maxLevel;
    }

    public String getName() {
        return name;
    }

    public int getSkillId() {
        return skillId;
    }

    public int getBaseLevel() {
        return baseLevel;
    }

    public int getMaxLevel() {
        return maxLevel;
    }
}
