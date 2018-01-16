package com.rs.core.utils.tools;

import com.rs.core.cache.Cache;
import com.rs.core.cache.loaders.ItemDefinitions;
import com.rs.core.utils.Utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ItemListDumper {

    public ItemListDumper() throws IOException {
        Cache.init();
        final File file = new File("itemList.txt"); // = new
        // File("information/itemlist.txt");
        if (file.exists()) {
            file.delete();
        } else {
            file.createNewFile();
        }
        final BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        // writer.append("//Version = 709\n");
        writer.append("//Version = 718\n");
        writer.flush();
        for (int id = 0; id < Utils.getItemDefinitionsSize(); id++) {
            final ItemDefinitions def = ItemDefinitions.getItemDefinitions(id);
            if (def.getName().equals("null")) {
                continue;
            }
            writer.append(id + " - " + def.getName());
            writer.newLine();
            writer.flush();
        }
        writer.close();
    }

    public static void main(final String[] args) {
        try {
            new ItemListDumper();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public static int convertInt(final String str) {
        try {
            final int i = Integer.parseInt(str);
            return i;
        } catch (final NumberFormatException e) {
        }
        return 0;
    }

}
