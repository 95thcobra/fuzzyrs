package com.rs.core.file.data.npc;

import com.rs.core.file.FuzzyFileManager;
import com.rs.core.settings.GameConstants;
import com.rs.core.utils.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NPCExaminesFileManager {

    private static final Map<Integer, String> EXAMINES = new HashMap<>();

    private static final String NPC_EXAMINES_PATH = GameConstants.DATA_PATH + "/npcs/unpackedExamines.txt";

    public static void loadExamines() throws IOException {
        List<String> lines = FuzzyFileManager.getFileLines(NPC_EXAMINES_PATH);
        String[] s1;
        int npcId;
        for (String line : lines) {
            line = line.replace("﻿", "");
            s1 = line.split(" - ");
            npcId = Integer.parseInt(s1[0]);
            if (s1[1].length() > 255) {
                continue;
            }
            EXAMINES.put(npcId, s1[1]);
        }
        Logger.info(NPCExaminesFileManager.class, "Loaded " + EXAMINES.size() + " npc examines.");
    }

    public static String getExamine(final int npcId) {
        return EXAMINES.get(npcId) != null ? formatMessage(EXAMINES.get(npcId)) : "It's an npc.";
    }

    private static String formatMessage(String message) {
        message = message.replace("<i>", "");
        message = message.replace("</i>", "");
        return message.replace("<b>", "");
    }

}