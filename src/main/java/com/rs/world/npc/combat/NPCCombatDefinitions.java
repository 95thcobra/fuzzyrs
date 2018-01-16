package com.rs.world.npc.combat;

public class NPCCombatDefinitions {

    public static final int MELEE = 0;
    public static final int RANGE = 1;
    public static final int MAGE = 2;
    public static final int SPECIAL = 3;
    public static final int SPECIAL2 = 4; // follows no distance
    public static final int PASSIVE = 0;
    public static final int AGRESSIVE = 1;
    private final int attackAnim;
    private final int defenceAnim;
    private final int deathAnim;
    private final int attackDelay;
    private final int deathDelay;
    private final int respawnDelay;
    private final int attackStyle;
    private final int attackGfx;
    private final int attackProjectile;
    private final int agressivenessType;
    private int hitpoints;
    private int maxHit;

    public NPCCombatDefinitions(final int hitpoints, final int attackAnim,
                                final int defenceAnim, final int deathAnim, final int attackDelay,
                                final int deathDelay, final int respawnDelay, final int maxHit,
                                final int attackStyle, final int attackGfx,
                                final int attackProjectile, final int agressivenessType) {
        this.hitpoints = hitpoints;
        this.attackAnim = attackAnim;
        this.defenceAnim = defenceAnim;
        this.deathAnim = deathAnim;
        this.attackDelay = attackDelay;
        this.deathDelay = deathDelay;
        this.respawnDelay = respawnDelay;
        this.maxHit = maxHit;
        this.attackStyle = attackStyle;
        this.attackGfx = attackGfx;
        this.attackProjectile = attackProjectile;
        this.agressivenessType = agressivenessType;
    }

    public int getRespawnDelay() {
        return respawnDelay;
    }

    public int getDeathEmote() {
        return deathAnim;
    }

    public int getDefenceEmote() {
        return defenceAnim;
    }

    public int getAttackEmote() {
        return attackAnim;
    }

    public int getAttackGfx() {
        return attackGfx;
    }

    public int getAgressivenessType() {
        return agressivenessType;
    }

    public int getAttackProjectile() {
        return attackProjectile;
    }

    public int getAttackStyle() {
        return attackStyle;
    }

    public int getAttackDelay() {
        return attackDelay;
    }

    public int getMaxHit() {
        return maxHit;
    }

    public void setMaxHit(int i) {
        this.maxHit = i;
    }

    public int getHitpoints() {
        return hitpoints;
    }

    public void setHitpoints(final int amount) {
        this.hitpoints = amount;
    }

    public int getDeathDelay() {
        return deathDelay;
    }
}
