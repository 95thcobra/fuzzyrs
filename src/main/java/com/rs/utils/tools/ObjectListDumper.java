package com.rs.utils.tools;

import com.rs.core.cache.Cache;
import com.rs.core.cache.loaders.ObjectDefinitions;
import com.rs.utils.Utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ObjectListDumper {

    public static void main(final String[] args) throws IOException {
        Cache.init();
        final File file = new File("information/objectlist.txt");
        if (file.exists()) {
            file.delete();
        } else {
            file.createNewFile();
        }
        final BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        // writer.append("//Version = 667.704\n");
        writer.flush();
        for (int id = 0; id < Utils.getObjectDefinitionsSize(); id++) {
            final ObjectDefinitions def = ObjectDefinitions
                    .getObjectDefinitions(id);
            // writer.append("FORMAT1"+id+"FORMAT2"+def.name.replaceAll("`",
            // "")+"FORMAT3\n");
            writer.append(id + " - " + def.name);
            writer.newLine();
            System.out.println(id + " - " + def.name);
            writer.flush();
        }
        writer.close();
    }

}
