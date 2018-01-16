package com.rs.player;

import com.rs.content.clans.ClansManager;
import com.rs.core.cache.loaders.ClientScriptMap;
import com.rs.core.cache.loaders.ItemDefinitions;
import com.rs.core.cache.loaders.ItemsEquipIds;
import com.rs.core.cache.loaders.NPCDefinitions;
import com.rs.core.net.io.OutputStream;
import com.rs.core.utils.Utils;
import com.rs.world.World;
import com.rs.world.item.Item;

import java.io.Serializable;
import java.util.Arrays;

public class Appearance implements Serializable {

    private static final long serialVersionUID = 7655608569741626586L;

    private transient int renderEmote;
    private int title;
    private int[] lookI;
    private byte[] colour;
    private boolean male;
    private transient boolean glowRed;
    private transient byte[] appeareanceData;
    private transient byte[] md5AppeareanceDataHash;
    private transient short transformedNpcId;
    private transient boolean hidePlayer;

    private transient Player player;

    public Appearance() {
        male = true;
        renderEmote = -1;
        title = -1;
        resetAppearence();
    }

    public void setPlayer(final Player player) {
        this.player = player;
        transformedNpcId = -1;
        renderEmote = -1;
        if (lookI == null) {
            resetAppearence();
        }
    }

    public void transformIntoNPC(final int id) {
        transformedNpcId = (short) id;
        generateAppearenceData();
    }

    public void switchHidden() {
        hidePlayer = !hidePlayer;
        generateAppearenceData();
    }

    public boolean isHidden() {
        return hidePlayer;
    }

    public boolean isGlowRed() {
        return glowRed;
    }

    public void setGlowRed(final boolean glowRed) {
        this.glowRed = glowRed;
        generateAppearenceData();
    }

    public void generateAppearenceData() {
        final OutputStream stream = new OutputStream();
        int flag = 0;
        if (!male) {
            flag |= 0x1;
        }
        if (transformedNpcId >= 0
                && NPCDefinitions.getNPCDefinitions(transformedNpcId).aBoolean3190) {
            flag |= 0x2;
        }
        if (title != 0) {
            flag |= title >= 32 && title <= 37 ? 0x80 : 0x40; // after/before
        }
        stream.writeByte(flag);
        if (title != 0) {
            final String titleName = title == 88 ? "<col=C12006>Con Artist </col>"
                    : title == 89 ? "<col=C12006>Swindler </col>"
                    : title == 90 ? "<col=C12006>Artful Dodger </col>"
                    : title == 91 ? "<col=0174DF>Enforcer </col>"
                    : title == 92 ? "<col=0174DF>Wizard </col>"
                    : title == 93 ? "<col=0174DF>Man Slaughter </col>"
                    : title == 94 ? "<col=298A08>Adventurer </col>"
                    : title == 95 ? "<col=585858>Slayer </col>"
                    : title == 96 ? "<col=c12006>Meat Shield </col>"
                    : title == 97 ? "<col=c12006>Berserker </col>"
                    : title == 98 ? "<col=c12006>Cow Slayer </col>"
                    : title == 99 ? "<col=210B61>The Warrior </col>"
                    : title == 100 ? "<col=C12006>Daydreamer </col>"
                    : title == 101 ? "<col=C12006>master </col>"
                    : title == 102 ? "<col=C12006>hash 1 brid </col>"
                    : title == 103 ? "<col=C12006>stole my nuts </col>"
                    : title == 104 ? "<col=C12006>Gravedigger </col>"
                    : title == 105 ? "<col=C12006>frog prince </col>"
                    : title == 106 ? "<col=00aaff>Storm hunter </col>"
                    : title == 107 ? "<col=00aaff>Bunny </col>"
                    : title == 108 ? "<col=FA8072>Shit Brid </col>"
                    : title == 109 ? "<col=298A08>Cummy Bear </col>"
                    : title == 126 ? "<col=298A08>2gp </col>"
                    : title == 127 ? "<col=298A08>No Xp Waste </col>"
                    : title == 110 ? "<col=ADFF2F>Jake's Bitch </col>"
                    : title == 111 ? "<col=7CFC00>Lord of Below </col>"
                    : title == 129 ? "<col=7CFC00>Umattbro? </col>"
                    : title == 3678 ? "<col=7CFC00>Newbie </col>"
                    : title == 3875 ? "<col=7CFC00>Worst luck </col>"
                    : title == 112 ? "<col=ADFF2F>Trouble Maker </col>"
                    : title == 113 ? "<col=FF0000>Bloodthirsty </col>"
                    : title == 114 ? "<col=FF0000>Dominator </col>"
                    : title == 115 ? "<col=FF00FF>Booty Hunter </col>"
                    : title == 116 ? "<col=ADFF2F>Unique </col>"
                    : title == 117 ? "<col=ADFF2F>Fallen Angels </col>"
                    : title == 3481 ? "<col=FF0000><shad=FF0000>Pussy Destroyer </col></shad>"
                    : title == 3482 ? "<col=FF0000><shad=FF0000>DropTheBass </col></shad>"
                    : title == 118 ? "<col=ADFF2F>Pvm Pro </col>"
                    : title == 119 ? "<col=250517>Poisonous </col>"
                    : title == 120 ? "<col=250517>Livestreamer </col>"
                    : title == 121 ? "<col=FFA500>Dragonborn </col>"
                    : title == 122 ? "<col=00aaff>Redneck </col>"
                    : title == 123 ? "<col=FFFFFF><shad=00FFFB>Ice Wyrm </col></shad>"
                    : title == 9090 ? "<col=FFFFFF><shad=00FFFB>President </col></shad>"
                    : title == 7667 ? "<col=FFFFFF><shad=00FFFB>Pussy Destroyer </col></shad>"
                    : title == 7668 ? "<col=FFFFFF><shad=00FFFB>Pussy Killer </col></shad>"
                    : title == 7669 ? "<col=FFFFFF><shad=00FFFB>#1 Voter </col></shad>"
                    : title == 7777 ? "<col=4169E1>Human's Boy-Toy</col>"
                    : title == 7598 ? "<col=00FF04><shad=00FF04>Nuclear Waste "
                    : title == 4567 ? "<col=00FF04><shad=00FF04>DropTheBass</col></shad> "
                    : title == 4570 ? "<col=00FF04><shad=00FF04>Payton's Army</col></shad> "
                    : title == 1337 ? "<col=00aaff>Old School </col>"
                    : title == 128 ? "<col=808080>Iron man </col>"
                    : title == 125001 ? "<col=4169E1>Marquis </col>"
                    : title == 125002 ? "<col=4169E1>Knight </col>"
                    : title == 125003 ? "<col=4169E1>Ritter </col>"
                    : title == 125004 ? "<col=4169E1>Viscount </col>"
                    : title == 125005 ? "<col=4169E1>No-Life </col>"
                    : title == 125006 ? "<col=298A08>Get A Life </col>"
                    : title == 125007 ? "<col=298A08>Problematic </col>"
                    : title == 125008 ? "<col=298A08>Roflmatic </col>"
                    : title == 125009 ? "<col=298A08>Dard's Mate </col>"
                    : title == 125010 ? "<col=298A08>Jake's Mate </col>"
                    : title == 1341 ? "<col=4169E1>Jake's Guild </col>"
                    : title == 1342 ? "<col=4169E1>Human's Guild </col>"
                    : title == 3001 ? "<col=ADD8E6>Fx Original </col>"
                    : title == 5555 ? "<col=4169E1>Cap'n </col>"
                    : title == 8534 ? "<col=A020F0>Radioactive </col>"
                    : title == 10000 ? "<col=4169E1>Haters Gunna Hate </col>"
                    : title == 2013 ? "<col=4169E1>Sexy  </col>"
                    : title == 4445 ? "<col=800080>Anonymous  </col>"
                    : title == 4448 ? "<col=00FF00>Fully </col>"
                    : title == 124 ? "<col=00FF00>1337 L33T </col>"
                    : title == 25001 ? "<col=FFFFFF><shad=00FFFB>"
                    : title == 25002 ? "<col=FF0000><shad=FF0000>"
                    : title == 25003 ? "<col=00FF04><shad=00FF04>"
                    : title == 25004 ? "<col=FFB0F6><shad=FFB0F6>"
                    : title == 5873 ? "<col=FF00FF><shad=FF00FF>Beyond Gay</col></shad> "
                    : title == 5657 ? "<col=FF00FF><shad=FF00FF>Human #1 Gay</col></shad> "
                    : title == 8686 ? "<col=FF00FF><shad=FF00FF>#1</col></shad> "
                    : title == 125 ? "<col=FF00FF><shad=FF00FF>Pink Faggots</col></shad> "
                    : title == 5973 ? "<col=000000><shad=00FF04>$ Jake is sexy $</col></shad> "
                    : title == 987654 ? "<col=FFFFFF>Scourge of the  Wild </col>"
                    : title == 8989 ? "<col=FFFFFF><shad=00FFFB>Gangbanger </col></shad>"
                    : title == 6789 ? "<shad=B00000><col=FF0000>R<col=FF8000>i<col=FFFF00>d<col=00FF00>e<col=00FFFF> M<col=0000FF>e<col=8000FF>e<col=FF0000> "
                    : title == 25005 ? "<shad=B00000><col=FF0000>D<col=FF8000>o<col=FFFF00>n<col=00FF00>a<col=00FFFF>t<col=0000FF>o<col=8000FF>r "
                    : ClientScriptMap
                    .getMap(male ? 1093
                            : 3872)
                    .getStringValue(
                            title);
            stream.writeGJString(titleName);
        }
        stream.writeByte(player.hasSkull() ? player.getSkullId() : -1);
        stream.writeByte(player.getPrayer().getPrayerHeadIcon()); // prayer icon
        stream.writeByte(hidePlayer ? 1 : 0);
        // npc
        if (transformedNpcId >= 0) {
            stream.writeShort(-1); // 65535 tells it a npc
            stream.writeShort(transformedNpcId);
            stream.writeByte(0);
        } else {
            for (int index = 0; index < 4; index++) {
                final Item item = player.getEquipment().getItems().get(index);
                if (glowRed) {
                    if (index == 0) {
                        stream.writeShort(32768 + ItemsEquipIds
                                .getEquipId(2910));
                        continue;
                    }
                    if (index == 1) {
                        stream.writeShort(32768 + ItemsEquipIds
                                .getEquipId(14641));
                        continue;
                    }
                }
                if (item == null) {
                    stream.writeByte(0);
                } else {
                    stream.writeShort(32768 + item.getEquipId());
                }
            }
            Item item = player.getEquipment().getItems()
                    .get(Equipment.SLOT_CHEST);
            stream.writeShort(item == null ? 0x100 + lookI[2] : 32768 + item
                    .getEquipId());
            item = player.getEquipment().getItems().get(Equipment.SLOT_SHIELD);
            if (item == null) {
                stream.writeByte(0);
            } else {
                stream.writeShort(32768 + item.getEquipId());
            }
            item = player.getEquipment().getItems().get(Equipment.SLOT_CHEST);
            if (item == null || !Equipment.hideArms(item)) {
                stream.writeShort(0x100 + lookI[3]);
            } else {
                stream.writeByte(0);
            }
            item = player.getEquipment().getItems().get(Equipment.SLOT_LEGS);
            stream.writeShort(glowRed ? 32768 + ItemsEquipIds.getEquipId(2908)
                    : item == null ? 0x100 + lookI[5] : 32768 + item
                    .getEquipId());
            item = player.getEquipment().getItems().get(Equipment.SLOT_HAT);
            if (!glowRed && (item == null || !Equipment.hideHair(item))) {
                stream.writeShort(0x100 + lookI[0]);
            } else {
                stream.writeByte(0);
            }
            item = player.getEquipment().getItems().get(Equipment.SLOT_HANDS);
            stream.writeShort(glowRed ? 32768 + ItemsEquipIds.getEquipId(2912)
                    : item == null ? 0x100 + lookI[4] : 32768 + item
                    .getEquipId());
            item = player.getEquipment().getItems().get(Equipment.SLOT_FEET);
            stream.writeShort(glowRed ? 32768 + ItemsEquipIds.getEquipId(2904)
                    : item == null ? 0x100 + lookI[6] : 32768 + item
                    .getEquipId());
            // tits for female, bear for male
            item = player.getEquipment().getItems()
                    .get(male ? Equipment.SLOT_HAT : Equipment.SLOT_CHEST);
            if (item == null || (male && Equipment.showBear(item))) {
                stream.writeShort(0x100 + lookI[1]);
            } else {
                stream.writeByte(0);
            }
            item = player.getEquipment().getItems().get(Equipment.SLOT_AURA);
            if (item == null) {
                stream.writeByte(0);
            } else {
                stream.writeShort(32768 + item.getEquipId()); // Fixes the
                // winged auras
                // lookIing
                // fucked.
            }
            final int pos = stream.getOffset();
            stream.writeShort(0);
            int hash = 0;
            int slotFlag = -1;
            for (int slotId = 0; slotId < player.getEquipment().getItems()
                    .getSize(); slotId++) {
                if (Equipment.DISABLED_SLOTS[slotId] != 0) {
                    continue;
                }
                slotFlag++;
                if (slotId == Equipment.SLOT_HAT) {
                    final int hatId = player.getEquipment().getHatId();
                    if (hatId == 20768 || hatId == 20770 || hatId == 20772) {
                        final ItemDefinitions defs = ItemDefinitions
                                .getItemDefinitions(hatId - 1);
                        if ((hatId == 20768
                                && Arrays.equals(
                                player.getMaxedCapeCustomized(),
                                defs.originalModelColors) || ((hatId == 20770 || hatId == 20772) && Arrays
                                .equals(player.getCompletionistCapeCustomized(),
                                        defs.originalModelColors)))) {
                            continue;
                        }
                        hash |= 1 << slotFlag;
                        stream.writeByte(0x4); // modify 4 model colors
                        final int[] hat = hatId == 20768 ? player
                                .getMaxedCapeCustomized() : player
                                .getCompletionistCapeCustomized();
                        final int slots = 0 | 1 << 4 | 2 << 8 | 3 << 12;
                        stream.writeShort(slots);
                        for (int i = 0; i < 4; i++) {
                            stream.writeShort(hat[i]);
                        }
                    }
                } else if (slotId == Equipment.SLOT_CAPE) {
                    final int capeId = player.getEquipment().getCapeId();
                    if (capeId == 20767 || capeId == 20769 || capeId == 20771
                            || capeId == 28013 || capeId == 20708) {
                        final ItemDefinitions defs = ItemDefinitions
                                .getItemDefinitions(capeId);
                        if (capeId == 20708
                                && Arrays.equals(player.getClanCapeColors(),
                                defs.originalModelColors))
                            continue;

                        if ((capeId == 20767
                                && Arrays.equals(
                                player.getMaxedCapeCustomized(),
                                defs.originalModelColors) || ((capeId == 20769 || capeId == 20771) && Arrays
                                .equals(player.getCompletionistCapeCustomized(),
                                        defs.originalModelColors)))) {
                            continue;
                        }
                        hash |= 1 << slotFlag;
                        stream.writeByte(0x4); // modify 4 model colors
                        final int[] cape = capeId == 20767 ? player
                                .getMaxedCapeCustomized() : player
                                .getCompletionistCapeCustomized();
                        final int slots = 0 | 1 << 4 | 2 << 8 | 3 << 12;
                        stream.writeShort(slots);
                        for (int i = 0; i < 4; i++)
                            stream.writeShort(cape[i]);
                    } else if (capeId == 20708) {
                        ClansManager manager = player.getClanManager();
                        if (manager == null)
                            continue;
                        int[] colors = manager.getClan().getMottifColors();
                        ItemDefinitions defs = ItemDefinitions.getItemDefinitions(20709);
                        boolean modifyColor = !Arrays.equals(colors, defs.originalModelColors);
                        int bottom = manager.getClan().getMottifBottom();
                        int top = manager.getClan().getMottifTop();
                        if (bottom == 0 && top == 0 && !modifyColor)
                            continue;
                        hash |= 1 << slotFlag;
                        stream.writeByte((modifyColor ? 0x4 : 0) | (bottom != 0 || top != 0 ? 0x8 : 0));
                        if (modifyColor) {
                            int slots = 0 | 1 << 4 | 2 << 8 | 3 << 12;
                            stream.writeShort(slots);
                            for (int i = 0; i < 4; i++)
                                stream.writeShort(colors[i]);
                        }
                        if (bottom != 0 || top != 0) {
                            int slots = 0 | 1 << 4;
                            stream.writeByte(slots);
                            stream.writeShort(ClansManager.getMottifTexture(top));
                            stream.writeShort(ClansManager.getMottifTexture(bottom));
                        }

                    }
                } else if (slotId == Equipment.SLOT_AURA) {
                    final int auraId = player.getEquipment().getAuraId();
                    if (auraId == -1 || !player.getAuraManager().isActivated()) {
                        continue;
                    }
                    final ItemDefinitions auraDefs = ItemDefinitions
                            .getItemDefinitions(auraId);
                    if (auraDefs.getMaleWornModelId1() == -1
                            || auraDefs.getFemaleWornModelId1() == -1) {
                        continue;
                    }
                    hash |= 1 << slotFlag;
                    stream.writeByte(0x1); // modify model ids
                    final int modelId = player.getAuraManager()
                            .getAuraModelId();
                    stream.writeBigSmart(modelId); // male modelid1
                    stream.writeBigSmart(modelId); // female modelid1
                    if (auraDefs.getMaleWornModelId2() != -1
                            || auraDefs.getFemaleWornModelId2() != -1) {
                        final int modelId2 = player.getAuraManager()
                                .getAuraModelId2();
                        stream.writeBigSmart(modelId2);
                        stream.writeBigSmart(modelId2);
                    }
                }
            }
            final int pos2 = stream.getOffset();
            stream.setOffset(pos);
            stream.writeShort(hash);
            stream.setOffset(pos2);
        }

        for (final byte element : colour) {
            // colour length 10
            stream.writeByte(element);
        }

        stream.writeShort(getRenderEmote());
        stream.writeString(player.getDisplayName());
        final boolean pvpArea = World.isPvpArea(player);
        stream.writeByte(pvpArea ? player.getSkills().getCombatLevel() : player
                .getSkills().getCombatLevelWithSummoning());
        stream.writeByte(pvpArea ? player.getSkills()
                .getCombatLevelWithSummoning() : 0);
        stream.writeByte(-1); // higher level acc name appears in front :P
        stream.writeByte(transformedNpcId >= 0 ? 1 : 0); // to end here else id
        // need to send more
        // data
        if (transformedNpcId >= 0) {
            final NPCDefinitions defs = NPCDefinitions
                    .getNPCDefinitions(transformedNpcId);
            stream.writeShort(defs.anInt876);
            stream.writeShort(defs.anInt842);
            stream.writeShort(defs.anInt884);
            stream.writeShort(defs.anInt875);
            stream.writeByte(defs.anInt875);
        }

        // done separated for safe because of synchronization
        final byte[] appeareanceData = new byte[stream.getOffset()];
        System.arraycopy(stream.getBuffer(), 0, appeareanceData, 0,
                appeareanceData.length);
        final byte[] md5Hash = Utils.encryptUsingMD5(appeareanceData);
        this.appeareanceData = appeareanceData;
        md5AppeareanceDataHash = md5Hash;
    }

    public int getSize() {
        if (transformedNpcId >= 0)
            return NPCDefinitions.getNPCDefinitions(transformedNpcId).size;
        return 1;
    }

    private void writeCapeModelData(final OutputStream stream, final int itemId) {
        switch (itemId) {
            case 20708:
                int flag = 0;
                /**
                 * Flag indicated whether we should encode textures or models, 0x4
                 * and 0x8 is both.
                 */
                flag |= 0x4;
                flag |= 0x8;
                stream.writeByte(flag);
                /**
                 * The slot flags
                 */
                int slotFlag = 0 | 1 << 4 | 2 << 8 | 3 << 12;
                stream.writeShort(slotFlag);
                /**
                 * Encoding the colors
                 */
                for (int i = 0; i < 4; i++) {
                    stream.writeShort(player.getClanCapeColors()[i]);
                }
                slotFlag = 0 | 1 << 4;
                stream.writeByte(slotFlag);
        }
    }

    private boolean needsWeaponModelUpdate() {
        final int weapon = player.getEquipment().getWeaponId();
        final ItemDefinitions defs = ItemDefinitions.getItemDefinitions(weapon);
        if (weapon != 20709)
            return false;
        return !(weapon == 20708
                && Arrays.equals(player.getClanCapeColors(),
                defs.originalModelColors));
    }

    public int getRenderEmote() {
        if (renderEmote >= 0)
            return renderEmote;
        if (transformedNpcId >= 0)
            return NPCDefinitions.getNPCDefinitions(transformedNpcId).renderEmote;
        return player.getEquipment().getWeaponRenderEmote();
    }

    public void setRenderEmote(final int id) {
        this.renderEmote = id;
        generateAppearenceData();
    }

    public void resetAppearence() {
        lookI = new int[7];
        colour = new byte[10];
        male();
    }

    public void male() {
        lookI[0] = 3; // Hair
        lookI[1] = 14; // Beard
        lookI[2] = 18; // Torso
        lookI[3] = 26; // Arms
        lookI[4] = 34; // Bracelets
        lookI[5] = 38; // Legs
        lookI[6] = 42; // Shoes~

        colour[2] = 16;
        colour[1] = 16;
        colour[0] = 3;
        male = true;
    }

    public void female() {
        lookI[0] = 48; // Hair
        lookI[1] = 57; // Beard
        lookI[2] = 57; // Torso
        lookI[3] = 65; // Arms
        lookI[4] = 68; // Bracelets
        lookI[5] = 77; // Legs
        lookI[6] = 80; // Shoes

        colour[2] = 16;
        colour[1] = 16;
        colour[0] = 3;
        male = false;
    }

    public byte[] getAppeareanceData() {
        return appeareanceData;
    }

    public byte[] getMD5AppeareanceDataHash() {
        return md5AppeareanceDataHash;
    }

    public boolean isMale() {
        return male;
    }

    public void setMale(final boolean male) {
        this.male = male;
    }

    public void setLook(final int i, final int i2) {
        lookI[i] = i2;
    }

    public void setColor(final int i, final int i2) {
        colour[i] = (byte) i2;
    }

    public int getTopStyle() {
        return lookI[2];
    }

    public void setTopStyle(final int i) {
        lookI[2] = i;
    }

    public void setArmsStyle(final int i) {
        lookI[3] = i;
    }

    public void setWristsStyle(final int i) {
        lookI[4] = i;
    }

    public void setLegsStyle(final int i) {
        lookI[5] = i;
    }

    public int getHairStyle() {
        return lookI[0];
    }

    public void setHairStyle(final int i) {
        lookI[0] = i;
    }

    public int getBeardStyle() {
        return lookI[1];
    }

    public void setBeardStyle(final int i) {
        lookI[1] = i;
    }

    public int getFacialHair() {
        return lookI[1];
    }

    public void setFacialHair(final int i) {
        lookI[1] = i;
    }

    public int getSkinColor() {
        return colour[4];
    }

    public void setSkinColor(final int color) {
        colour[4] = (byte) color;
    }

    public void setTopColor(final int color) {
        colour[1] = (byte) color;
    }

    public void setLegsColor(final int color) {
        colour[2] = (byte) color;
    }

    public int getHairColor() {
        return colour[0];
    }

    public void setHairColor(final int color) {
        colour[0] = (byte) color;
    }

    public void setTitle(final int title) {
        this.title = title;
        generateAppearenceData();
    }

    public short getTransformedNpcId() {
        return transformedNpcId;
    }
}
