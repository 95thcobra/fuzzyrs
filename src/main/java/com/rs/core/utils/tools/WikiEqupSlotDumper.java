package com.rs.core.utils.tools;

import com.rs.core.cache.Cache;
import com.rs.core.utils.Utils;
import com.rs.core.utils.tools.WikiEqupSlotDumper.EquipSlot.SlotType;
import com.rs.world.item.Item;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class WikiEqupSlotDumper {

    private static ArrayList<EquipSlot> slots = new ArrayList<EquipSlot>();

    private static boolean dumpEquipmentSlot(final Item item) {
        if (item.getDefinitions().isWearItem() == false
                || item.getDefinitions().isNoted())
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
                    line = line.substring(
                            line.indexOf("title=") + "title=".length() + 1,
                            line.indexOf("\"><img alt=\""));
                    final EquipSlot ep = getEquipSlot(item, line);
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

    private static EquipSlot getEquipSlot(final Item item, final String line) {
        final int id = item.getId();
        if (line.equals("Neck slot"))
            return new EquipSlot(id, SlotType.NECK_SLOT);
        else if (line.equals("Weapon slot"))
            return new EquipSlot(id, SlotType.WEAPON_SLOT);
        else if (line.equals("Body slot"))
            return new EquipSlot(id, SlotType.BODY_SLOT);
        else if (line.equals("Feet slot"))
            return new EquipSlot(id, SlotType.FEET_SLOT);
        else if (line.equals("Ammunition slot"))
            return new EquipSlot(id, SlotType.AMMUNITION_SLOT);
        else if (line.equals("Legwear slot"))
            return new EquipSlot(id, SlotType.LEGWEAR_SLOT);
        else if (line.equals("Head slot"))
            /*
			 * if (Equipment.isFullHat(item)) new EquipSlot(id,
			 * SlotType.FULL_HELMET); else if (Equipment.isFullMask(item))
			 * return new EquipSlot(id, SlotType.FULL_MASK);
			 */
            return new EquipSlot(id, SlotType.HEAD_SLOT);
        else if (line.equals("Shield slot"))
            return new EquipSlot(id, SlotType.SHIELD_SLOT);
        else if (line.equals("Two-handed slot"))
            return new EquipSlot(id, SlotType.TWO_HANDED);
        else if (line.equals("Ring slot"))
            return new EquipSlot(id, SlotType.RING_SLOT);
        else if (line.equals("Hands slot"))
            return new EquipSlot(id, SlotType.HANDS_SLOT);
        else if (line.equals("Cape slot"))
            return new EquipSlot(id, SlotType.CAPE_SLOT);
        else if (line.equals("Aura slot"))
            return new EquipSlot(id, SlotType.AURA_SLOT);
        else {
            System.err.println("Unhandled Slot: " + line);
        }
        return new EquipSlot(id, SlotType.valueOf(line.toUpperCase().replace(
                "_", " ")));
    }

    public static ArrayList<String> getPage(final Item item) {
        try {
            String pageName = item.getDefinitions().getName()
                    .replace(" (black)", "").replace(" (white)", "")
                    .replace(" (yellow)", "").replace(" (red)", "");
            if (pageName == null || pageName.equals("null"))
                return null;
            pageName = pageName.replace(" (p)", "");
            pageName = pageName.replace(" (p+)", "");
            pageName = pageName.replace(" (p++)", "");
            pageName = pageName.replace(" Broken", "");
            pageName = pageName.replace(" 25", "");
            pageName = pageName.replace(" 50", "");
            pageName = pageName.replace(" 75", "");
            pageName = pageName.replace(" 100", "");
            pageName = pageName.replaceAll(" ", "_");
            final WebPage page = new WebPage("http://runescape.wikia.com/wiki/"
                    + pageName);
            try {
                page.load();
            } catch (final Exception e) {
                System.out.println("Invalid page: " + item.getId() + ", "
                        + pageName);
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
        for (int i = 0; i < Utils.getItemDefinitionsSize(); i++) {
            final Item item = new Item(i, 1);
            dumpEquipmentSlot(item);
        }
        try {
            final DataOutputStream out = new DataOutputStream(
                    new FileOutputStream("./slots.s/"));
            for (final EquipSlot slot : slots) {
                final byte[] bytes = slot.getType().toString().getBytes();
                out.writeShort(slot.getId());
                out.writeByte(bytes.length);
                out.write(bytes);
            }
            out.close();
            System.out.println("Packed Defintions.");
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public static class EquipSlot {

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

        public enum SlotType {
            WEAPON_SLOT, HEAD_SLOT, RING_SLOT, BODY_SLOT, LEGWEAR_SLOT, HANDS_SLOT, AURA_SLOT, FEET_SLOT, CAPE_SLOT, AMMUNITION_SLOT, NECK_SLOT, SHIELD_SLOT, FULL_HELMET, FULL_MASK, TWO_HANDED
        }
    }

}
