package com.rs.utils.tools;

import com.rs.core.cache.Cache;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.ArrayList;

public class CompleteDump {

    private static ArrayList<WikiEqupSlotDumper.EquipSlot> slots = new ArrayList<WikiEqupSlotDumper.EquipSlot>();

    public static void addItemsByHand() {
        slots.add(new WikiEqupSlotDumper.EquipSlot(818, WikiEqupSlotDumper.EquipSlot.SlotType.WEAPON_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(1235, WikiEqupSlotDumper.EquipSlot.SlotType.WEAPON_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(1343, WikiEqupSlotDumper.EquipSlot.SlotType.WEAPON_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(2609, WikiEqupSlotDumper.EquipSlot.SlotType.LEGWEAR_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(4181, WikiEqupSlotDumper.EquipSlot.SlotType.HEAD_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(11367, WikiEqupSlotDumper.EquipSlot.SlotType.WEAPON_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(16009, WikiEqupSlotDumper.EquipSlot.SlotType.FEET_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(11698, WikiEqupSlotDumper.EquipSlot.SlotType.TWO_HANDED));
        slots.add(new WikiEqupSlotDumper.EquipSlot(28000, WikiEqupSlotDumper.EquipSlot.SlotType.TWO_HANDED));
        slots.add(new WikiEqupSlotDumper.EquipSlot(5648, WikiEqupSlotDumper.EquipSlot.SlotType.WEAPON_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(7388, WikiEqupSlotDumper.EquipSlot.SlotType.LEGWEAR_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(8839, WikiEqupSlotDumper.EquipSlot.SlotType.BODY_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(17021, WikiEqupSlotDumper.EquipSlot.SlotType.WEAPON_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(9139, WikiEqupSlotDumper.EquipSlot.SlotType.AMMUNITION_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(4335, WikiEqupSlotDumper.EquipSlot.SlotType.CAPE_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(3839, WikiEqupSlotDumper.EquipSlot.SlotType.SHIELD_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(9906, WikiEqupSlotDumper.EquipSlot.SlotType.WEAPON_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(15860, WikiEqupSlotDumper.EquipSlot.SlotType.WEAPON_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(9907, WikiEqupSlotDumper.EquipSlot.SlotType.WEAPON_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(9908, WikiEqupSlotDumper.EquipSlot.SlotType.WEAPON_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(9909, WikiEqupSlotDumper.EquipSlot.SlotType.WEAPON_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(9910, WikiEqupSlotDumper.EquipSlot.SlotType.WEAPON_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(9911, WikiEqupSlotDumper.EquipSlot.SlotType.WEAPON_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(10683, WikiEqupSlotDumper.EquipSlot.SlotType.BODY_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(10685, WikiEqupSlotDumper.EquipSlot.SlotType.BODY_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(16016, WikiEqupSlotDumper.EquipSlot.SlotType.BODY_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(16018, WikiEqupSlotDumper.EquipSlot.SlotType.BODY_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(10840, WikiEqupSlotDumper.EquipSlot.SlotType.WEAPON_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(18373, WikiEqupSlotDumper.EquipSlot.SlotType.WEAPON_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(10863, WikiEqupSlotDumper.EquipSlot.SlotType.LEGWEAR_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(19612, WikiEqupSlotDumper.EquipSlot.SlotType.SHIELD_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(10879, WikiEqupSlotDumper.EquipSlot.SlotType.SHIELD_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(12666, WikiEqupSlotDumper.EquipSlot.SlotType.HEAD_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(12667, WikiEqupSlotDumper.EquipSlot.SlotType.HEAD_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(13721, WikiEqupSlotDumper.EquipSlot.SlotType.WEAPON_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(13738, WikiEqupSlotDumper.EquipSlot.SlotType.SHIELD_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(13963, WikiEqupSlotDumper.EquipSlot.SlotType.HEAD_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(16138, WikiEqupSlotDumper.EquipSlot.SlotType.NECK_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(21485, WikiEqupSlotDumper.EquipSlot.SlotType.HEAD_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(16139, WikiEqupSlotDumper.EquipSlot.SlotType.NECK_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(16140, WikiEqupSlotDumper.EquipSlot.SlotType.NECK_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(16141, WikiEqupSlotDumper.EquipSlot.SlotType.NECK_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(16213, WikiEqupSlotDumper.EquipSlot.SlotType.WEAPON_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(20462, WikiEqupSlotDumper.EquipSlot.SlotType.HANDS_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(17169, WikiEqupSlotDumper.EquipSlot.SlotType.HANDS_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(19340, WikiEqupSlotDumper.EquipSlot.SlotType.SHIELD_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(21495, WikiEqupSlotDumper.EquipSlot.SlotType.WEAPON_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(22958, WikiEqupSlotDumper.EquipSlot.SlotType.BODY_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(22961, WikiEqupSlotDumper.EquipSlot.SlotType.HANDS_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(22966, WikiEqupSlotDumper.EquipSlot.SlotType.HEAD_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(19394, WikiEqupSlotDumper.EquipSlot.SlotType.NECK_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(19396, WikiEqupSlotDumper.EquipSlot.SlotType.NECK_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(20790, WikiEqupSlotDumper.EquipSlot.SlotType.LEGWEAR_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(19419, WikiEqupSlotDumper.EquipSlot.SlotType.LEGWEAR_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(19707, WikiEqupSlotDumper.EquipSlot.SlotType.LEGWEAR_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(20135, WikiEqupSlotDumper.EquipSlot.SlotType.FULL_HELMET));
        slots.add(new WikiEqupSlotDumper.EquipSlot(20446, WikiEqupSlotDumper.EquipSlot.SlotType.BODY_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(20857, WikiEqupSlotDumper.EquipSlot.SlotType.WEAPON_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(20859, WikiEqupSlotDumper.EquipSlot.SlotType.WEAPON_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(20953, WikiEqupSlotDumper.EquipSlot.SlotType.HEAD_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(20954, WikiEqupSlotDumper.EquipSlot.SlotType.HEAD_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(20955, WikiEqupSlotDumper.EquipSlot.SlotType.HEAD_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(21334, WikiEqupSlotDumper.EquipSlot.SlotType.SHIELD_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(18739, WikiEqupSlotDumper.EquipSlot.SlotType.TWO_HANDED));
        slots.add(new WikiEqupSlotDumper.EquipSlot(16939, WikiEqupSlotDumper.EquipSlot.SlotType.WEAPON_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(21335, WikiEqupSlotDumper.EquipSlot.SlotType.WEAPON_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(21487, WikiEqupSlotDumper.EquipSlot.SlotType.FEET_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(21513, WikiEqupSlotDumper.EquipSlot.SlotType.NECK_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(21519, WikiEqupSlotDumper.EquipSlot.SlotType.FEET_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(22288, WikiEqupSlotDumper.EquipSlot.SlotType.AURA_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(22289, WikiEqupSlotDumper.EquipSlot.SlotType.AURA_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(22290, WikiEqupSlotDumper.EquipSlot.SlotType.AURA_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(22291, WikiEqupSlotDumper.EquipSlot.SlotType.AURA_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(22336, WikiEqupSlotDumper.EquipSlot.SlotType.AMMUNITION_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(22337, WikiEqupSlotDumper.EquipSlot.SlotType.AMMUNITION_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(22911, WikiEqupSlotDumper.EquipSlot.SlotType.AURA_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(22912, WikiEqupSlotDumper.EquipSlot.SlotType.AURA_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(23856, WikiEqupSlotDumper.EquipSlot.SlotType.AURA_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(23857, WikiEqupSlotDumper.EquipSlot.SlotType.AURA_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(24172, WikiEqupSlotDumper.EquipSlot.SlotType.WEAPON_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(24173, WikiEqupSlotDumper.EquipSlot.SlotType.WEAPON_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(24174, WikiEqupSlotDumper.EquipSlot.SlotType.WEAPON_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(24186, WikiEqupSlotDumper.EquipSlot.SlotType.WEAPON_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(24187, WikiEqupSlotDumper.EquipSlot.SlotType.WEAPON_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(24188, WikiEqupSlotDumper.EquipSlot.SlotType.WEAPON_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(24189, WikiEqupSlotDumper.EquipSlot.SlotType.WEAPON_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(24210, WikiEqupSlotDumper.EquipSlot.SlotType.CAPE_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(24294, WikiEqupSlotDumper.EquipSlot.SlotType.WEAPON_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(24296, WikiEqupSlotDumper.EquipSlot.SlotType.HEAD_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(24297, WikiEqupSlotDumper.EquipSlot.SlotType.HEAD_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(24298, WikiEqupSlotDumper.EquipSlot.SlotType.HEAD_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(29999, WikiEqupSlotDumper.EquipSlot.SlotType.CAPE_SLOT));
        slots.add(new WikiEqupSlotDumper.EquipSlot(20760, WikiEqupSlotDumper.EquipSlot.SlotType.CAPE_SLOT));
    }

    public static void main(final String[] args) {
        addItemsByHand();
        try {
            Cache.init();
        } catch (final IOException e) {
            e.printStackTrace();
        }
        try {
            final RandomAccessFile in = new RandomAccessFile("./slots.s/", "r");
            final FileChannel channel = in.getChannel();
            final ByteBuffer buffer = channel.map(MapMode.READ_ONLY, 0,
                    channel.size());
            while (buffer.hasRemaining()) {
                final int id = buffer.getShort();
                final int length = buffer.get() & 0xff;
                final byte[] data = new byte[length];
                buffer.get(data, 0, length);
                final WikiEqupSlotDumper.EquipSlot ep = new WikiEqupSlotDumper.EquipSlot(id,
                        WikiEqupSlotDumper.EquipSlot.SlotType.valueOf(new String(data)));
                if (slots.contains(ep)) {
                    slots.remove(ep);
                }
                slots.add(ep);
            }
            final DataOutputStream out = new DataOutputStream(
                    new FileOutputStream("./data/items/equipslots.es/"));
            for (final WikiEqupSlotDumper.EquipSlot slot : slots) {
                out.writeShort(slot.getId());
                out.write(slot.getType().toString().getBytes());
            }
            out.close();
            System.out.println("Packed Defintions.");
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}
