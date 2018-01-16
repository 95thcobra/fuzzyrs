package com.rs.core.utils.tools;

import com.rs.core.cache.Cache;
import com.rs.core.utils.tools.RsWikiEquipSlotsDumper.EquipSlot.SlotType;
import com.rs.world.item.Item;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class RsWikiEquipSlotsDumper {

    private static ArrayList<EquipSlot> slots = new ArrayList<EquipSlot>();

    private static boolean dumpEquipmentSlot(final Item item) {
        if (item.getDefinitions() != null
                && item.getDefinitions().isWearItem() != true)
            return false;
        final ArrayList<String> lines = getPage(item);
        if (lines == null)
            return false;
        final Iterator<String> iterator = lines.iterator();
        try {
            while (iterator.hasNext()) {
                String line = iterator.next();
                if (line.startsWith("</th><th rowspan=\"3\" colspan=\"2\" width=\"30\" align=\"center\">")) {
                    line = iterator.next();
                    // System.out.println(line.indexOf("\"><img alt=\""));
                    line = line.substring(
                            line.indexOf("title=") + "title=".length() + 1,
                            line.indexOf("\"><img alt=\""));
                    final EquipSlot ep = getEquipSlot(item.getId(), line);
                    if (ep != null) {
                        slots.add(ep);
                    }
                }
            }
            return true;
        } catch (final Exception e) {
            return false;
        }
    }

    private static EquipSlot getEquipSlot(final int id, final String line) {
        return new EquipSlot(id, SlotType.valueOf(line.toUpperCase()));
    }

    public static ArrayList<String> getPage(final Item item) {
        try {
            final WebPage page = new WebPage("http://runescape.wikia.com/wiki/"
                    + item.getName());
            try {
                page.load();
            } catch (final Exception e) {
                System.out.println("Invalid page: " + item.getId() + ", "
                        + item.getName());
                return null;
            }
            return page.getLines();
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(final String[] args) {
        try {
            Cache.init();
        } catch (final IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < 75; i++) {
            final Item item = new Item(i, 1);
            if (dumpEquipmentSlot(item)) {
                System.out.println("Dumped Item " + item.getName());
            }
        }
        try {
            final DataOutputStream out = new DataOutputStream(
                    new FileOutputStream(new File("./slots.s")));
            for (final EquipSlot slot : slots) {
                out.writeShort(slot.getId());
                out.write(slot.getType().toString().getBytes());
            }
            out.flush();
            out.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    static class EquipSlot {

        private final int id;
        private final SlotType type;

        public EquipSlot(final int id, final SlotType type) {
            this.id = id;
            this.type = type;
        }

        public int getId() {
            return id;
        }

        public SlotType getType() {
            return type;
        }

        enum SlotType {
            WEAPON_SLOT, HEAD_SLOT, RING_SLOT, BODY_SLOT, LEGWEAR_SLOT, HANDS_SLOT, AURA_SLOT, FEET_SLOT, CAPE_SLOT, AMMUNITION_SLOT, NECK_SLOT, SLOT_SHIELD
        }
    }
}
