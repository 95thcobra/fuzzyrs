package com.rs.content.dialogues.impl;

import com.rs.content.actions.skills.Skills;
import com.rs.content.dialogues.Dialogue;
import com.rs.player.Player;
import com.rs.world.Graphics;
import com.rs.world.World;

import java.util.HashMap;
import java.util.Map;

public final class LevelUp extends Dialogue {

    public static final int[] SKILL_LEVEL_UP_MUSIC_EFFECTS = {30, 38, 66, 48,
            58, 56, 52, 34, 70, 44, 42, 40, 36, 64, 54, 46, 28, 68, 61, 10, 60,
            50, 32, 301, 417};

    public static void sendNews(final Player player, final int skill,
                                final int level) {
        boolean reachedAll = true;
        for (int i = 0; i < Skills.SKILL_NAME.length; i++) {
            if (player.getSkills().getLevelForXp(i) < 99) {
                reachedAll = false;
                break;
            }
        }
        if (!reachedAll) {
            World.sendWorldMessage(
                    "<img=6><col=DAA520>News: " + player.getDisplayName()
                            + " has achieved " + level + " "
                            + Skills.SKILL_NAME[skill] + ".", false);
            return;
        }
        World.sendWorldMessage(
                "<img=7><col=DAA520>News: " + player.getDisplayName()
                        + " has just achieved at least level 99 in all skills!",
                false);
    }

    public static int getIconValue(final int skill) {
        if (skill == Skills.ATTACK)
            return 1;
        if (skill == Skills.STRENGTH)
            return 2;
        if (skill == Skills.RANGE)
            return 3;
        if (skill == Skills.MAGIC)
            return 4;
        if (skill == Skills.DEFENCE)
            return 5;
        if (skill == Skills.HITPOINTS)
            return 6;
        if (skill == Skills.PRAYER)
            return 7;
        if (skill == Skills.AGILITY)
            return 8;
        if (skill == Skills.HERBLORE)
            return 9;
        if (skill == Skills.THIEVING)
            return 10;
        if (skill == Skills.CRAFTING)
            return 11;
        if (skill == Skills.RUNECRAFTING)
            return 12;
        if (skill == Skills.MINING)
            return 13;
        if (skill == Skills.SMITHING)
            return 14;
        if (skill == Skills.FISHING)
            return 15;
        if (skill == Skills.COOKING)
            return 16;
        if (skill == Skills.FIREMAKING)
            return 17;
        if (skill == Skills.WOODCUTTING)
            return 18;
        if (skill == Skills.FLETCHING)
            return 19;
        if (skill == Skills.SLAYER)
            return 20;
        if (skill == Skills.FARMING)
            return 21;
        if (skill == Skills.CONSTRUCTION)
            return 22;
        if (skill == Skills.DUNGEONEERING)
            return 23;
        if (skill == Skills.SUMMONING)
            return 24;
        return 25;
    }

    public static void switchFlash(final Player player, final int skill,
                                   final boolean on) {
        int id;
        if (skill == Skills.ATTACK) {
            id = 4732;
        } else if (skill == Skills.STRENGTH) {
            id = 4733;
        } else if (skill == Skills.DEFENCE) {
            id = 4734;
        } else if (skill == Skills.RANGE) {
            id = 4735;
        } else if (skill == Skills.PRAYER) {
            id = 4736;
        } else if (skill == Skills.MAGIC) {
            id = 4737;
        } else if (skill == Skills.HITPOINTS) {
            id = 4738;
        } else if (skill == Skills.AGILITY) {
            id = 4739;
        } else if (skill == Skills.HERBLORE) {
            id = 4740;
        } else if (skill == Skills.THIEVING) {
            id = 4741;
        } else if (skill == Skills.CRAFTING) {
            id = 4742;
        } else if (skill == Skills.FLETCHING) {
            id = 4743;
        } else if (skill == Skills.MINING) {
            id = 4744;
        } else if (skill == Skills.SMITHING) {
            id = 4745;
        } else if (skill == Skills.FISHING) {
            id = 4746;
        } else if (skill == Skills.COOKING) {
            id = 4747;
        } else if (skill == Skills.FIREMAKING) {
            id = 4748;
        } else if (skill == Skills.WOODCUTTING) {
            id = 4749;
        } else if (skill == Skills.RUNECRAFTING) {
            id = 4750;
        } else if (skill == Skills.SLAYER) {
            id = 4751;
        } else if (skill == Skills.FARMING) {
            id = 4752;
        } else if (skill == Skills.CONSTRUCTION) {
            id = 4753;
        } else if (skill == Skills.HUNTER) {
            id = 4754;
        } else if (skill == Skills.SUMMONING) {
            id = 4755;
        } else {
            id = 7756;
        }
        player.getPackets().sendConfigByFile(id, on ? 1 : 0);
    }

    @Override
    public void start() {
        int skill = (Integer) parameters[0];
        final int level = player.getSkills().getLevelForXp(skill);
        player.getTemporaryAttributtes().put("leveledUp", skill);
        player.getTemporaryAttributtes().put("leveledUp[" + skill + "]",
                Boolean.TRUE);
        player.setNextGraphics(new Graphics(199));
        if (level == 99 || level == 120) {
            player.setNextGraphics(new Graphics(1765));
        }
        final String name = Skills.SKILL_NAME[skill];
        player.getPackets().sendGameMessage(
                "You've just advanced a" + (name.startsWith("A") ? "n" : "")
                        + " " + name + " level! You are now level " + level
                        + ".");
        player.getPackets().sendConfigByFile(4757, getIconValue(skill));
        player.getInterfaceManager().sendTab(
                player.getInterfaceManager().hasRezizableScreen() ? 11 : 0,
                1216);
        final Configs levelup = Configs.levelup(skill);
        player.getPackets().sendGlobalConfig(1756, levelup.getConfigId());
        switchFlash(player, skill, true);
        final int musicEffect = SKILL_LEVEL_UP_MUSIC_EFFECTS[skill];
        if (musicEffect != -1) {
            player.getPackets().sendMusicEffect(musicEffect);
        }
        if (player.getRank().isStaff() && (level == 99 || level == 120)
                && skill > 6 && skill != 23) {
            sendNews(player, skill, level);
        }
        if (player.getSkills().getXp(skill) == 200000000) {
            for (final Player p : World.getPlayers()) {
                p.getPackets()
                        .sendGameMessage(
                                "<col=DAA520><img=6>News: "
                                        + player.getDisplayName()
                                        + " has just achieved 200 million experience in the "
                                        + name + " skill!");
            }
        }
    }

    @Override
    public void run(final int interfaceId, final int componentId) {
        end();
    }

    @Override
    public void finish() {
        // player.getPackets().sendConfig(1179, SKILL_ICON[skill]); //removes
        // random flash
    }

    public enum Configs {
        ATTACK(1, 0), STRENGTH(2, 2), DEFENCE(5, 1), HITPOINTS(6, 3), RANGE(3,
                4), MAGIC(4, 6), PRAYER(7, 5), AGILITY(8, 16), HERBLORE(9, 15), THIEVING(
                10, 17), CRAFTING(11, 12), RUNECRAFTING(12, 20), MINING(13, 14), SMITHING(
                14, 13), FISHING(15, 10), COOKING(16, 7), FIREMAKING(17, 11), WOODCUTTING(
                18, 8), FLETCHING(19, 9), SLAYER(20, 18), FARMING(21, 19), CONSTRUCTION(
                22, 22), HUNTER(23, 21), SUMMONING(24, 23), DUNGEONEERING(25,
                24);

        private static Map<Integer, Configs> configs = new HashMap<Integer, Configs>();

        static {
            for (final Configs config : Configs.values()) {
                configs.put(config.skillId, config);
            }
        }

        private final int configId;
        private final int skillId;

        Configs(final int configId, final int skillId) {
            this.configId = configId;
            this.skillId = skillId;
        }

        public static Configs levelup(final int skill) {
            return configs.get(skill);
        }

        public int getConfigId() {
            return configId;
        }
    }
}