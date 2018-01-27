package com.rs.utils.tools;

import com.rs.core.cache.Cache;
import com.rs.core.cache.loaders.ItemDefinitions;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DumpItemRenderIds {

    public static void main(final String[] args) throws IOException {
        Cache.init();
        dumpRenders();
    }

    public static void dumpRenders() throws IOException {
        final File file = new File("./renderids.txt");
        if (file.exists()) {
            file.delete();
        }
        final BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        for (int i = 0; i < 13247; i++) {
            final ItemDefinitions def = ItemDefinitions.getItemDefinitions(i);
            if (def == null) {
                continue;
            }
            writer.write(i + "=" + def.getRenderAnimId());
            writer.newLine();
        }
        writer.flush();
        writer.close();
        System.out.println("Dumped all render ids.");
    }
}