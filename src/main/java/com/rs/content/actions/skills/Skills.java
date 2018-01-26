package com.rs.content.actions.skills;

import com.rs.server.Server;
import com.rs.content.dialogues.impl.LevelUp;
import com.rs.player.Player;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;

public final class Skills implements Serializable {

	public static final double MAXIMUM_EXP = 200000000;
	public static final int ATTACK = 0, DEFENCE = 1, STRENGTH = 2,
			HITPOINTS = 3, RANGE = 4, PRAYER = 5, MAGIC = 6, COOKING = 7,
			WOODCUTTING = 8, FLETCHING = 9, FISHING = 10, FIREMAKING = 11,
			CRAFTING = 12, SMITHING = 13, MINING = 14, HERBLORE = 15,
			AGILITY = 16, THIEVING = 17, SLAYER = 18, FARMING = 19,
			RUNECRAFTING = 20, CONSTRUCTION = 22, HUNTER = 21, SUMMONING = 23,
			DUNGEONEERING = 24;
	public static final String[] SKILL_NAME = { "Attack", "Defence",
		"Strength", "Constitution", "Ranged", "Prayer", "Magic", "Cooking",
		"Woodcutting", "Fletching", "Fishing", "Firemaking", "Crafting",
		"Smithing", "Mining", "Herblore", "Agility", "Thieving", "Slayer",
            "Farming", "Runecrafting", "Hunter", "Construction", "Summoning", "Dungeoneering"
    };
    private static final long serialVersionUID = -7086829989489745985L;
    public short level[];
	private double xp[];
	private double[] xpTracks;
	private boolean[] trackSkills;
	private byte[] trackSkillsIds;
	private boolean xpDisplay, xpPopup;

	private transient int currentCounter;
	private transient Player player;

	public Skills() {
		level = new short[25];
		xp = new double[25];
		for (int i = 0; i < level.length; i++) {
			level[i] = 1;
			xp[i] = 0;
		}
		level[3] = 10;
		xp[3] = 1184;
		level[HERBLORE] = 3;
		xp[HERBLORE] = 250;
		xpPopup = true;
		xpTracks = new double[3];
		trackSkills = new boolean[3];
		trackSkillsIds = new byte[3];
		trackSkills[0] = true;
		for (int i = 0; i < trackSkillsIds.length; i++) {
			trackSkillsIds[i] = 30;
		}

	}

    public static int dayOfWeek() {
        final Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    public static boolean isWeekend() {
		int dayOfWeek = dayOfWeek();
		return dayOfWeek == 1 || dayOfWeek == 6 || dayOfWeek == 7;
	}

    public static int getXPForLevel(final int level) {
        int points = 0;
        int output = 0;
        for (int lvl = 1; lvl <= level; lvl++) {
            points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
            if (lvl >= level)
                return output;
            output = (int) Math.floor(points / 4);
        }
        return 0;
    }

    public void passLevels(final Player p) {
        this.level = p.getSkills().level;
        this.xp = p.getSkills().xp;
    }

	public void sendXPDisplay() {
		for (int i = 0; i < trackSkills.length; i++) {
			player.getPackets().sendConfigByFile(10444 + i,
					trackSkills[i] ? 1 : 0);
			player.getPackets().sendConfigByFile(10440 + i,
					trackSkillsIds[i] + 1);
			refreshCounterXp(i);
		}
	}

	public void setupXPCounter() {
		player.getInterfaceManager().sendXPDisplay(1214);
	}

	public void refreshCurrentCounter() {
		player.getPackets().sendConfig(2478, currentCounter + 1);
	}

	public void setCurrentCounter(final int counter) {
		if (counter != currentCounter) {
			currentCounter = counter;
			refreshCurrentCounter();
		}
	}

	public void switchTrackCounter() {
		trackSkills[currentCounter] = !trackSkills[currentCounter];
		player.getPackets().sendConfigByFile(10444 + currentCounter,
				trackSkills[currentCounter] ? 1 : 0);
	}

	public void resetCounterXP() {
		xpTracks[currentCounter] = 0;
		refreshCounterXp(currentCounter);
	}

	public void setCounterSkill(final int skill) {
		xpTracks[currentCounter] = 0;
		trackSkillsIds[currentCounter] = (byte) skill;
		player.getPackets().sendConfigByFile(10440 + currentCounter,
				trackSkillsIds[currentCounter] + 1);
		refreshCounterXp(currentCounter);
	}

	public void refreshCounterXp(final int counter) {
		player.getPackets().sendConfig(counter == 0 ? 1801 : 2474 + counter,
				(int) (xpTracks[counter] * 10));
	}

	public void handleSetupXPCounter(final int componentId) {
		if (componentId == 18) {
			player.getInterfaceManager().sendXPDisplay();
		} else if (componentId >= 22 && componentId <= 24) {
			setCurrentCounter(componentId - 22);
		} else if (componentId == 27) {
			switchTrackCounter();
		} else if (componentId == 61) {
			resetCounterXP();
		} else if (componentId >= 31 && componentId <= 57)
			if (componentId == 33) {
				setCounterSkill(4);
			} else if (componentId == 34) {
				setCounterSkill(2);
			} else if (componentId == 35) {
				setCounterSkill(3);
			} else if (componentId == 42) {
				setCounterSkill(18);
			} else if (componentId == 49) {
				setCounterSkill(11);
			} else {
				setCounterSkill(componentId >= 56 ? componentId - 27
						: componentId - 31);
			}

	}

	public void restoreSummoning() {
		level[23] = (short) getLevelForXp(23);
		refresh(23);
	}

	public void sendInterfaces() {
		if (xpDisplay) {
			player.getInterfaceManager().sendXPDisplay();
		}
		if (xpPopup) {
			player.getInterfaceManager().sendXPPopup();
		}
	}

	public void switchXPDisplay() {
		xpDisplay = !xpDisplay;
		if (xpDisplay) {
			player.getInterfaceManager().sendXPDisplay();
		} else {
			player.getInterfaceManager().closeXPDisplay();
		}
	}

	public void switchXPPopup() {
		xpPopup = !xpPopup;
		player.getPackets().sendGameMessage(
				"XP pop-ups are now " + (xpPopup ? "en" : "dis") + "abled.");
		if (xpPopup) {
			player.getInterfaceManager().sendXPPopup();
		} else {
			player.getInterfaceManager().closeXPPopup();
		}
	}

	public void restoreSkills() {
		for (int skill = 0; skill < level.length; skill++) {
			level[skill] = (short) getLevelForXp(skill);
			refresh(skill);
		}
	}

	public void setPlayer(final Player player) {
		this.player = player;
		// temporary
		if (xpTracks == null) {
			xpPopup = true;
			xpTracks = new double[3];
			trackSkills = new boolean[3];
			trackSkillsIds = new byte[3];
			trackSkills[0] = true;
			for (int i = 0; i < trackSkillsIds.length; i++) {
				trackSkillsIds[i] = 30;
			}
		}
	}

	public short[] getLevels() {
		return level;
	}

	public double[] getXp() {
		return xp;
	}

	public int getLevel(final int skill) {
		return level[skill];
	}

	public double getXp(final int skill) {
		return xp[skill];
	}

	public boolean hasRequiriments(final int... skills) {
		for (int i = 0; i < skills.length; i += 2) {
			final int skillId = skills[i];
			if (skillId == CONSTRUCTION || skillId == FARMING) {
				continue;
			}
			final int skillLevel = skills[i + 1];
			if (getLevelForXp(skillId) < skillLevel)
				return false;

		}
		return true;
	}

	public int getCombatLevel() {
		final int attack = getLevelForXp(0);
		final int defence = getLevelForXp(1);
		final int strength = getLevelForXp(2);
		final int hp = getLevelForXp(3);
		final int prayer = getLevelForXp(5);
		final int ranged = getLevelForXp(4);
		final int magic = getLevelForXp(6);
		int combatLevel = (int) ((defence + hp + Math.floor(prayer / 2)) * 0.25) + 1;
		final double melee = (attack + strength) * 0.325;
		final double ranger = Math.floor(ranged * 1.5) * 0.325;
		final double mage = Math.floor(magic * 1.5) * 0.325;
		if (melee >= ranger && melee >= mage) {
			combatLevel += melee;
		} else if (ranger >= melee && ranger >= mage) {
			combatLevel += ranger;
		} else if (mage >= melee && mage >= ranger) {
			combatLevel += mage;
		}
		return combatLevel;
	}

	public void set(final int skill, final int newLevel) {
		level[skill] = (short) newLevel;
		refresh(skill);
	}

	public int drainLevel(final int skill, final int drain) {
		int drainLeft = drain - level[skill];
		if (drainLeft < 0) {
			drainLeft = 0;
		}
		level[skill] -= drain;
		if (level[skill] < 0) {
			level[skill] = 0;
		}
		refresh(skill);
		return drainLeft;
	}

	public int getCombatLevelWithSummoning() {
		return getCombatLevel() + getSummoningCombatLevel();
	}

	public int getSummoningCombatLevel() {
		return getLevelForXp(Skills.SUMMONING) / 8;
	}

	public void drainSummoning(final int amt) {
		final int level = getLevel(Skills.SUMMONING);
		if (level == 0)
			return;
		set(Skills.SUMMONING, amt > level ? 0 : level - amt);
	}

	public int getLevelForXp(final int skill) {
		final double exp = xp[skill];
		int points = 0;
		int output;
		for (int lvl = 1; lvl <= (skill == DUNGEONEERING ? 120 : 99); lvl++) {
			points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
			output = (int) Math.floor(points / 4);
			if ((output - 1) >= exp)
				return lvl;
		}
		return skill == DUNGEONEERING ? 120 : 99;
	}

    public int getTotalLevel() {
        int totallevel = 0;
		for (int i = 0; i <= 24; i++) {
			totallevel += player.getSkills().getLevelForXp(i);
		}
		return totallevel;
	}

    public String getTotalXpString() {
        double totalxp = 0;
		for (final double xp : player.getSkills().getXp()) {
			totalxp += xp;
		}
		final NumberFormat formatter = new DecimalFormat("#######");
		return formatter.format(totalxp);
    }

    public long getTotalXp() {
        long totalxp = 0;
        for (double xp : getXp()) {
            totalxp += xp;
        }
        return totalxp;
    }

	public void init() {
		for (int skill = 0; skill < level.length; skill++) {
			refresh(skill);
		}
		sendXPDisplay();
	}

	public void refresh(final int skill) {
		player.getPackets().sendSkillLevel(skill);
	}

	/*
	 * if(componentId == 33) setCounterSkill(4); else if(componentId == 34)
	 * setCounterSkill(2); else if(componentId == 35) setCounterSkill(3); else
	 * if(componentId == 42) setCounterSkill(18); else if(componentId == 49)
	 * setCounterSkill(11);
	 */

	public int getCounterSkill(final int skill) {
		switch (skill) {
		case ATTACK:
			return 0;
		case STRENGTH:
			return 1;
		case DEFENCE:
			return 4;
		case RANGE:
			return 2;
		case HITPOINTS:
			return 5;
		case PRAYER:
			return 6;
		case AGILITY:
			return 7;
		case HERBLORE:
			return 8;
		case THIEVING:
			return 9;
		case CRAFTING:
			return 10;
		case MINING:
			return 12;
		case SMITHING:
			return 13;
		case FISHING:
			return 14;
		case COOKING:
			return 15;
		case FIREMAKING:
			return 16;
		case WOODCUTTING:
			return 17;
		case SLAYER:
			return 19;
		case FARMING:
			return 20;
		case CONSTRUCTION:
			return 21;
		case HUNTER:
			return 22;
		case SUMMONING:
			return 23;
		case DUNGEONEERING:
			return 24;
		case MAGIC:
			return 3;
		case FLETCHING:
			return 18;
		case RUNECRAFTING:
			return 11;
		default:
			return -1;
		}

	}

	public void addXp(final int skill, double exp) {
		player.getControllerManager().trackXP(skill, (int) exp);
		if (player.isXpLocked()) {
			xp[skill] += exp;
			for (int i = 0; i < trackSkills.length; i++) {
				if (trackSkills[i]) {
					if (trackSkillsIds[i] == 30
							|| (trackSkillsIds[i] == 29 && (skill == Skills.ATTACK
									|| skill == Skills.DEFENCE
									|| skill == Skills.STRENGTH
									|| skill == Skills.MAGIC
									|| skill == Skills.RANGE || skill == Skills.HITPOINTS))
							|| trackSkillsIds[i] == getCounterSkill(skill)) {
						xpTracks[i] += exp;
						refreshCounterXp(i);
					}
				}
			}
			return;
		}
		if (player.getEquipment().getRingId() == 773) {
			exp *= 2;
		}
		if (player.getPrestigeLevel() == 5) {
			exp *= 1.25;
		}
		if (player.getPrestigeLevel() == 10) {
			exp *= 1.35;
		}
		if (player.getEquipment().getGlovesId() == 20178) {
			exp *= 1.025;
		}
		if (isWeekend()) {
			exp *= 2;
		}
		if (skill != ATTACK && skill != DEFENCE && skill != STRENGTH
				&& skill != MAGIC && skill != RANGE && skill != HITPOINTS) {
			exp *= Server.getInstance().getSettingsManager().getSettings().getSkillingXpRate();
		} else {
			exp *= Server.getInstance().getSettingsManager().getSettings().getCombatXpRate();
		}
		if (player.getAuraManager().usingWisdom()) {
			exp *= 1.025;
		}
		final int oldLevel = getLevelForXp(skill);
		xp[skill] += exp;
		for (int i = 0; i < trackSkills.length; i++) {
			if (trackSkills[i]) {
				if (trackSkillsIds[i] == 30
						|| (trackSkillsIds[i] == 29 && (skill == Skills.ATTACK
								|| skill == Skills.DEFENCE
								|| skill == Skills.STRENGTH
								|| skill == Skills.MAGIC
								|| skill == Skills.RANGE || skill == Skills.HITPOINTS))
						|| trackSkillsIds[i] == getCounterSkill(skill)) {
					xpTracks[i] += exp;
					refreshCounterXp(i);
				}
			}
		}

		if (xp[skill] > MAXIMUM_EXP) {
			xp[skill] = MAXIMUM_EXP;
		}
		final int newLevel = getLevelForXp(skill);
		final int levelDiff = newLevel - oldLevel;
		if (newLevel > oldLevel) {
			level[skill] += levelDiff;
			player.getDialogueManager().startDialogue(LevelUp.class, skill);
			if (skill == SUMMONING || (skill >= ATTACK && skill <= MAGIC)) {
				player.getAppearance().generateAppearenceData();
				if (skill == HITPOINTS) {
					player.heal(levelDiff * 10);
				} else if (skill == PRAYER) {
					player.getPrayer().restorePrayer(levelDiff * 10);
				}
			}
			player.getQuestManager().checkCompleted();
		}
		refresh(skill);
	}

	public void addSkillXpRefresh(final int skill, final double xp) {
		this.xp[skill] += xp;
		level[skill] = (short) getLevelForXp(skill);
	}

	public void resetSkillNoRefresh(final int skill) {
		xp[skill] = 0;
		level[skill] = 1;
	}

	public void setXp(final int skill, final double exp) {
		xp[skill] = exp;
		refresh(skill);
	}
}