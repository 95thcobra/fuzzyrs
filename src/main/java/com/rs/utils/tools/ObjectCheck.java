package com.rs.utils.tools;

import com.rs.core.cache.Cache;
import com.rs.core.cache.loaders.ObjectDefinitions;
import com.rs.utils.Utils;

import java.io.IOException;

public class ObjectCheck {

    public static void main(final String[] args) throws IOException {
        Cache.init();
        for (int i = 0; i < Utils.getObjectDefinitionsSize(); i++) {
            final ObjectDefinitions def = ObjectDefinitions
                    .getObjectDefinitions(i);
            if (def.containsOption("Steal-from")) {
                System.out.println(def.id + " - " + def.name);
            }
        }
    }

}
