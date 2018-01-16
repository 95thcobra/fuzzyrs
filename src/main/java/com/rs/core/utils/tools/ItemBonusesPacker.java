package com.rs.core.utils.tools;

import com.rs.core.cache.Cache;
import com.rs.core.settings.GameConstants;
import com.rs.core.utils.Utils;

import java.io.*;

public class ItemBonusesPacker {

    public static void main(final String[] args) throws IOException {
        Cache.init();
        final DataOutputStream out = new DataOutputStream(new FileOutputStream(
                GameConstants.DATA_PATH + "/items/bonuses.ib"));
        for (int itemId = 0; itemId < Utils.getItemDefinitionsSize(); itemId++) {
            final File file = new File(GameConstants.DATA_PATH + "/items/bonuses/" + itemId + ".txt");
            if (file.exists()) {
                final BufferedReader reader = new BufferedReader(
                        new FileReader(file));
                out.writeShort(itemId);
                reader.readLine();
                // att bonuses
                out.writeShort(Integer.valueOf(reader.readLine()));
                out.writeShort(Integer.valueOf(reader.readLine()));
                out.writeShort(Integer.valueOf(reader.readLine()));
                out.writeShort(Integer.valueOf(reader.readLine()));
                out.writeShort(Integer.valueOf(reader.readLine()));
                reader.readLine();
                // def bonuses
                out.writeShort(Integer.valueOf(reader.readLine()));
                out.writeShort(Integer.valueOf(reader.readLine()));
                out.writeShort(Integer.valueOf(reader.readLine()));
                out.writeShort(Integer.valueOf(reader.readLine()));
                out.writeShort(Integer.valueOf(reader.readLine()));
                out.writeShort(Integer.valueOf(reader.readLine()));
                reader.readLine();
                // Damage absorption
                out.writeShort(Integer.valueOf(reader.readLine()));
                out.writeShort(Integer.valueOf(reader.readLine()));
                out.writeShort(Integer.valueOf(reader.readLine()));
                reader.readLine();
                // Other bonuses
                out.writeShort(Integer.valueOf(reader.readLine()));
                out.writeShort(Integer.valueOf(reader.readLine()));
                out.writeShort(Integer.valueOf(reader.readLine()));
                out.writeShort(Integer.valueOf(reader.readLine()));
                if (reader.readLine() != null)
                    throw new RuntimeException("Should be null line" + itemId);
            }
        }
        out.flush();
        out.close();
    }

}
