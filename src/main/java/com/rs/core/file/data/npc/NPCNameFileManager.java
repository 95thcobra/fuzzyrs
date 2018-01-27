package com.rs.core.file.data.npc;

import com.rs.core.file.FuzzyFileManager;
import com.rs.server.GameConstants;
import com.rs.utils.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author John (FuzzyAvacado) on 1/2/2016.
 */
public class NPCNameFileManager {

    private static final Map<Integer, String> NPC_NAMES = new HashMap<>();

    private static final String NPC_NAMES_PATH = GameConstants.DATA_PATH + "/npcs/names.txt";

    public static void loadNpcNames() throws IOException {
        List<String> names = FuzzyFileManager.getFileLines(NPC_NAMES_PATH);
        int npcId;
        String npcName;
        for (String s : names) {
            String[] parts = s.split(":");
            npcId = Integer.parseInt(parts[0]);
            npcName = parts[1];
            NPC_NAMES.put(npcId, npcName);
        }
        Logger.info(NPCNameFileManager.class, "Loaded " + NPC_NAMES.size() + " custom NPC names.");
    }

    public static String getCustomName(int id) {
        String name = NPC_NAMES.get(id);
        if (name != null) {
            return name;
        }
        return null;
    }
}
