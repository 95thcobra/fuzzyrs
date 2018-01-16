package com.rs.core.file.data.npc;

import com.rs.core.file.FuzzyFileManager;
import com.rs.core.settings.GameConstants;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public final class NPCBonusesFileManager {

    private static final HashMap<Integer, int[]> npcBonuses = new HashMap<>();

    private static final String NPC_BONUSES_PATH = GameConstants.DATA_PATH + "/npcs/bonuses.txt";

    public static int[] getBonuses(final int id) {
        return npcBonuses.get(id);
    }

    public static void loadNpcBonuses() throws IOException {
        List<String> lines = FuzzyFileManager.getFileLines(NPC_BONUSES_PATH);
        String[] s1, s2;
        int npcId;
        int[] bonuses;
        for (String line : lines) {
            s1 = line.split(" - ", 2);
            if (s1.length != 2)
                throw new RuntimeException("Invalid NPC Bonuses line: " + line);
            npcId = Integer.parseInt(s1[0]);
            s2 = s1[1].split(" ", 10);
            if (s2.length != 10)
                throw new RuntimeException("Invalid NPC Bonuses line: " + line);
            bonuses = new int[10];
            npcBonuses.put(npcId, bonuses);
        }
    }

}
