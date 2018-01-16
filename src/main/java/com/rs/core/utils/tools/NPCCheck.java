package com.rs.core.utils.tools;

import com.rs.core.cache.Cache;
import com.rs.core.cache.loaders.NPCDefinitions;
import com.rs.core.utils.Utils;

import java.io.IOException;

public class NPCCheck {

    public static void main(final String[] args) throws IOException {
        Cache.init();
        for (int id = 0; id < Utils.getNPCDefinitionsSize(); id++) {
            final NPCDefinitions def = NPCDefinitions.getNPCDefinitions(id);
            if (def.name.contains("Elemental")) {
                System.out.println(id + " - " + def.name);
            }
        }
    }

}
