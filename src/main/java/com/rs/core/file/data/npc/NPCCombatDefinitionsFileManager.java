package com.rs.core.file.data.npc;

import com.rs.core.file.FuzzyFileManager;
import com.rs.server.GameConstants;
import com.rs.world.npc.combat.NPCCombatDefinitions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public final class NPCCombatDefinitionsFileManager {

    private final static HashMap<Integer, NPCCombatDefinitions> npcCombatDefinitions = new HashMap<Integer, NPCCombatDefinitions>();
    private final static NPCCombatDefinitions DEFAULT_DEFINITION = new NPCCombatDefinitions(1, -1, -1, -1, 5, 1, 33, 0, NPCCombatDefinitions.MELEE, -1, -1, NPCCombatDefinitions.PASSIVE);

    private static final String COMBAT_DEFINITIONS_PATH = GameConstants.DATA_PATH + "/npcs/combatdefinitions.txt";

    public static NPCCombatDefinitions getNPCCombatDefinitions(final int npcId) {
        final NPCCombatDefinitions def = npcCombatDefinitions.get(npcId);
        if (def == null)
            return DEFAULT_DEFINITION;
        return def;
    }

    public static void loadNPCCombatDefinitions() throws IOException {
        List<String> lines = FuzzyFileManager.getFileLines(COMBAT_DEFINITIONS_PATH);
        String[] s1, s2;
        int npcId, attackStyle, hitpoints, attackAnim, defenceAnim, deathAnim, attackDelay, deathDelay, respawnDelay, maxHit, attackGfx, attackProjectile, aggressiveType;
        for (String line : lines) {
            s1 = line.split(" - ", 2);
            if (s1.length != 2)
                throw new RuntimeException("Invalid NPC Combat Definitions line: " + lines.indexOf(line) + ", " + line);
            npcId = Integer.parseInt(s1[0]);
            s2 = s1[1].split(" ", 12);
            if (s2.length != 12)
                throw new RuntimeException("Invalid NPC Combat Definitions line: " + lines.indexOf(line) + ", " + line);
            hitpoints = Integer.parseInt(s2[0]);
            attackAnim = Integer.parseInt(s2[1]);
            defenceAnim = Integer.parseInt(s2[2]);
            deathAnim = Integer.parseInt(s2[3]);
            attackDelay = Integer.parseInt(s2[4]);
            deathDelay = Integer.parseInt(s2[5]);
            respawnDelay = Integer.parseInt(s2[6]);
            maxHit = Integer.parseInt(s2[7]);
            if (s2[8].equalsIgnoreCase("MELEE")) {
                attackStyle = NPCCombatDefinitions.MELEE;
            } else if (s2[8].equalsIgnoreCase("RANGE")) {
                attackStyle = NPCCombatDefinitions.RANGE;
            } else if (s2[8].equalsIgnoreCase("MAGE")) {
                attackStyle = NPCCombatDefinitions.MAGE;
            } else if (s2[8].equalsIgnoreCase("SPECIAL")) {
                attackStyle = NPCCombatDefinitions.SPECIAL;
            } else if (s2[8].equalsIgnoreCase("SPECIAL2")) {
                attackStyle = NPCCombatDefinitions.SPECIAL2;
            } else
                throw new RuntimeException("Invalid NPC Combat Definitions line: " + line);
            attackGfx = Integer.parseInt(s2[9]);
            attackProjectile = Integer.parseInt(s2[10]);
            if (s2[11].equalsIgnoreCase("PASSIVE")) {
                aggressiveType = NPCCombatDefinitions.PASSIVE;
            } else if (s2[11].equalsIgnoreCase("AGRESSIVE")) {
                aggressiveType = NPCCombatDefinitions.AGRESSIVE;
            } else
                throw new RuntimeException("Invalid NPC Combat Definitions line: " + line);
            npcCombatDefinitions.put(npcId, new NPCCombatDefinitions(
                    hitpoints, attackAnim, defenceAnim, deathAnim,
                    attackDelay, deathDelay, respawnDelay, maxHit,
                    attackStyle, attackGfx, attackProjectile,
                    aggressiveType));
        }
    }

}
