package com.rs.player;

import com.rs.utils.Utils;
import com.rs.world.WorldObject;
import com.rs.world.WorldTile;
import com.rs.world.item.Item;

public class ClueScrolls {
	// chest id 2717
	public static int[] ScrollIds = { 2677, 2678, 2679, 2680, 2681, 2682, 2683,
			2684, 2685, 2686, 2687, 2688, 2689, 2690, 2691, 2692, 2693 };
	static Item[] EasyRewards = {new Item(88), new Item(7668),
			new Item(995, 1000000), new Item(995, 100000), new Item(537, 150),
			new Item(537, 250), new Item(7323), new Item(2364, 250),
			new Item(7327), new Item(7332), new Item(7334), new Item(7336),
			new Item(7338), new Item(7340), new Item(7342), new Item(74),
			new Item(995, 100000), new Item(995, 100000), new Item(88),
			new Item(74), new Item(995, 100000), new Item(995, 100000),
			new Item(7344), new Item(7346), new Item(7348), new Item(7350),
			new Item(7352), new Item(7354), new Item(7356), new Item(74),
			new Item(995, 100000), new Item(995, 100000), new Item(74),
			new Item(995, 100000), new Item(995, 100000), new Item(7358),
			new Item(7360), new Item(7362), new Item(15374), new Item(7366),
			new Item(7368), new Item(7370), new Item(74),
			new Item(995, 100000), new Item(995, 100000), new Item(74),
			new Item(995, 100000), new Item(995, 100000), new Item(7372),
			new Item(7374), new Item(23720), new Item(23724), new Item(23728),
			new Item(23732), new Item(23736), new Item(23740),
			new Item(995, 100000), new Item(995, 100000), new Item(19708),
			new Item(995, 100000), new Item(995, 100000), new Item(7386),
			new Item(7388), new Item(7390), new Item(7392), new Item(7394),
			new Item(7396), new Item(7398), new Item(74),
			new Item(995, 100000), new Item(995, 100000), new Item(74),
			new Item(995, 100000), new Item(995, 100000), new Item(7399),
			new Item(7400), new Item(10286), new Item(10288), new Item(10290),
			new Item(10292), new Item(10294), new Item(74),
			new Item(995, 100000), new Item(995, 100000), new Item(74),
			new Item(995, 100000), new Item(995, 100000), new Item(10296),
			new Item(10298), new Item(10300), new Item(10302), new Item(10304),
			new Item(10306), new Item(19707), new Item(74),
			new Item(995, 100000), new Item(995, 100000), new Item(74),
			new Item(995, 100000), new Item(995, 100000), new Item(10310),
			new Item(10330), new Item(10314), new Item(10368), new Item(10370),
			new Item(10372), new Item(10374), new Item(74),
			new Item(995, 100000), new Item(995, 100000), new Item(74),
			new Item(995, 100000), new Item(995, 100000), new Item(10376),
			new Item(10378), new Item(10380), new Item(10382), new Item(10384),
			new Item(10386), new Item(10388), new Item(74),
			new Item(995, 100000), new Item(995, 100000), new Item(74),
			new Item(995, 100000), new Item(995, 100000), new Item(10390),
			new Item(10392), new Item(10394), new Item(19706), new Item(10398),
			new Item(10400), new Item(24154), new Item(24155)};
	static Item[] ThirdAge = {new Item(10330), new Item(10332),
			new Item(10334), new Item(10336), new Item(10338), new Item(10340),
			new Item(10342), new Item(10344), new Item(10346), new Item(10348),
			new Item(10350), new Item(10352)};

	public static Scrolls hasClue(final Player p) {
		for (final Scrolls scroll : Scrolls.values()) {
			if (p.getInventory().containsOneItem(scroll.id))
				return scroll;
		}
		return null;
	}

	public static ObjectMaps hasObjectMapClue(final Player p, final int scrollid) {
		for (final Scrolls scroll : Scrolls.values()) {
			if (scroll.getLocationMap() == null) {
				continue;
			} else {
				if (scroll.id == scrollid) {
					if (p.getInventory().containsOneItem(scroll.id))
						return scroll.getLocationMap();
				}
			}
		}
		return null;
	}

	public static Maps hasMapClue(final Player p, final int scrollid) {
		for (final Scrolls scroll : Scrolls.values()) {
			if (scroll.getHiding() == null) {
				continue;
			} else {
				if (scroll.id == scrollid) {
					if (p.getInventory().containsOneItem(scroll.id))
						return scroll.getHiding();
				}
			}
		}
		return null;
	}

	public static Riddles hasRiddleClue(final Player p, final int scrollid) {
		for (final Scrolls scroll : Scrolls.values()) {
			if (scroll.getRiddle() == null) {
				continue;
			} else {
				if (scroll.id == scrollid) {
					if (p.getInventory().containsOneItem(scroll.id))
						return scroll.getRiddle();
				}
			}
		}
		return null;
	}

	public static boolean completedRiddle(final Player p, final int emoteid) {
		final Scrolls scroll = hasClue(p);
		if (scroll != null) {
			if (hasRiddleClue(p, scroll.id) != null) {
				final Riddles riddleclue = hasRiddleClue(p, scroll.id);
				final WorldTile lastloc = p.getLastWorldTile();
				if (lastloc.getX() >= riddleclue.locations[0]
						&& lastloc.getY() <= riddleclue.locations[1]
						&& lastloc.getX() <= riddleclue.locations[2]
						&& lastloc.getY() >= riddleclue.locations[3]) {
					if (emoteid == riddleclue.emoteid) {
						p.getInventory().deleteItem(scroll.id, 1);
						p.getInventory().addItem(2717, 1);
					}
				}
			}
		}
		return false;
	}

	public static boolean objectSpot(final Player p, final WorldObject obj) {
		final Scrolls scroll = hasClue(p);
		if (scroll != null) {
			if (hasObjectMapClue(p, scroll.id) != null) {
				final ObjectMaps mapclue = hasObjectMapClue(p, scroll.id);
				final WorldTile lastloc = p.getLastWorldTile();
				if (obj.getX() == mapclue.objectx
						&& obj.getY() == mapclue.objecty
						&& obj.getId() == mapclue.objectid) {
					p.getInventory().deleteItem(scroll.id, 1);
					p.getInventory().addItem(2717, 1);
				}
			}
		}
		return false;
	}

	public static boolean digSpot(final Player p) {
		final Scrolls scroll = hasClue(p);
		if (scroll != null) {
			if (hasMapClue(p, scroll.id) != null) {
				final Maps mapclue = hasMapClue(p, scroll.id);
				final WorldTile lastloc = p.getLastWorldTile();
				if (lastloc.getX() == mapclue.xcoord
						&& lastloc.getY() == mapclue.ycoord) {
					p.getInventory().deleteItem(scroll.id, 1);
					p.getInventory().addItem(2717, 1);
				}
			}
		}
		return false;

	}

	public static void showObjectMap(final Player p, final ObjectMaps objmap) {
		p.getPackets().sendInterface(false,
				p.getInterfaceManager().hasRezizableScreen() ? 746 : 548,
				p.getInterfaceManager().hasRezizableScreen() ? 28 : 27,
				objmap.interid);

	}

	public static void showMap(final Player p, final Maps map) {
		p.getPackets().sendInterface(false,
				p.getInterfaceManager().hasRezizableScreen() ? 746 : 548,
				p.getInterfaceManager().hasRezizableScreen() ? 28 : 27,
				map.interfaceId);
	}

	public static void giveReward(final Player p) {
		int random = Utils.getRandom(999) + 1;
		if (p.getClueScrollReward() == 1) {
			random += 100;
			p.setClueScrollReward(p.getClueScrollReward() + 1);
		} else if (p.getClueScrollReward() == 2) {
			random += 250;
			p.setClueScrollReward(p.getClueScrollReward() + 1);
		} else if (p.getClueScrollReward() == 3) {
			random += 450;
		}
		if (random > 675) {
			Item[] rewards;
			if (Utils.getRandom(500) > 475 && Utils.getRandom(999) > 950) {
				rewards = new Item[]{
						EasyRewards[Utils.getRandom(EasyRewards.length)],
						EasyRewards[Utils.getRandom(EasyRewards.length)],
						EasyRewards[Utils.getRandom(EasyRewards.length)],
						ThirdAge[Utils.getRandom(ThirdAge.length)]};

			} else {
				rewards = new Item[]{
						EasyRewards[Utils.getRandom(EasyRewards.length)],
						EasyRewards[Utils.getRandom(EasyRewards.length)],
						EasyRewards[Utils.getRandom(EasyRewards.length)]};
			}
			for (final Item item : rewards) {
				p.getInventory().addItem(item);
				// p.getInterfaceManager().sendInterface(364);

			}
			p.getInventory().deleteItem(2717, 1);
			p.setClueScrollReward(0);
		} else {
			p.getInventory().deleteItem(2717, 1);
			p.getInventory().addItem(
					ScrollIds[Utils.getRandom(ScrollIds.length)], 1);
			if (p.getClueScrollReward() == 0) {
				p.setClueScrollReward(p.getClueScrollReward() + 1);
			}

		}
	}

	public enum Scrolls {
		Scroll1(new int[] { ScrollIds[0], 1 }, Maps.Map1), Scroll2(new int[] {
				ScrollIds[1], 1 }, Maps.Map2), Scroll3(new int[] {
				ScrollIds[2], 1 }, Maps.Map3), Scroll4(new int[] {
				ScrollIds[3], 1 }, Maps.Map4), Scroll5(new int[] {
				ScrollIds[4], 1 }, Maps.Map5), Scroll6(new int[] {
				ScrollIds[5], 1 }, Maps.Map6), Scroll7(new int[] {
				ScrollIds[6], 1 }, Maps.Map7), Scroll8(new int[] {
				ScrollIds[7], 1 }, Maps.Map8), Scroll9(new int[] {
				ScrollIds[8], 1 }, Maps.Map9), Scroll10(new int[] {
				ScrollIds[9], 1 }, Maps.Map10), Scroll11(new int[] {
				ScrollIds[10], 1 }, Maps.Map11), Scroll12(new int[] {
				ScrollIds[11], 1 }, Maps.Map12), Scroll13(new int[] {
				ScrollIds[12], 1 }, Maps.Map13), Scroll14(new int[] {
				ScrollIds[13], 1 }, Maps.Map14), Scroll15(new int[] {
				ScrollIds[14], 1 }, Maps.Map15), Scroll16(new int[] {
				ScrollIds[15], 1 }, ObjectMaps.Map1), Scroll17(new int[] {
				ScrollIds[16], 1 }, ObjectMaps.Map2);

		int[] infos;
		Maps hiding;
		int id, level;
		ObjectMaps locationmap;
		Riddles riddle;

		Scrolls(final int[] info, final Riddles riddle) {
			this.infos = info;
			this.riddle = riddle;
			this.id = info[0];
			this.level = info[1];
		}

		Scrolls(final int[] info, final ObjectMaps map) {
			this.infos = info;
			this.locationmap = map;
			this.id = info[0];
			this.level = info[1];
		}

		Scrolls(final int[] info, final Maps hiden) {
			this.infos = info;
			this.id = info[0];
			this.level = info[1];
			this.hiding = hiden;
		}

		public static Maps getMap(final int itemid) {
			System.out.println("getting map");
			for (final Scrolls scroll : Scrolls.values()) {
				if (scroll.id == itemid) {
					if (scroll.getHiding() == null) {
						continue;
					} else
						return scroll.getHiding();
				}
			}
			return null;
		}

		public static Riddles getRiddles(final int itemid) {
			for (final Scrolls scroll : Scrolls.values()) {
				if (scroll.id == itemid) {
					if (scroll.getRiddle() == null) {
						continue;
					} else
						return scroll.getRiddle();
				}
			}
			return null;
		}

		public static ObjectMaps getObjMap(final int itemid) {
			for (final Scrolls scroll : Scrolls.values()) {
				if (scroll.id == itemid) {
					if (scroll.getLocationMap() == null) {
						continue;
					} else
						return scroll.getLocationMap();
				}
			}
			return null;
		}

		public Riddles getRiddle() {
			return riddle;
		}

		public ObjectMaps getLocationMap() {
			return locationmap;
		}

		public Maps getHiding() {
			return hiding;
		}
	}

	private enum Riddles {
		Riddle1(20, new int[] { 2967, 4386, 2970, 4380 }, new String[] {
				"There once was a villan", "of grey and white",
				"he also had a bit of bage", "do a clap outside his cave",
				"to scare him off", "", "", "" }), // Corp
		Riddle2(13, new int[] { 3190, 9828, 3193, 9825 }, new String[] {
				"I am a token of the greatest love",
				"I have no beginning or end",
				"Go to the place where money is lent",
				"Jig by the gate to be my friend!", "", "", "", "" }), // Varrock
																		// Bank
																		// Basement
		Riddle3(26, new int[] { 3162, 3255, 3171, 3244 }, new String[] {
				"For the reward you seek", "a city of lumber and bridge",
				"is west of a place that you", "must go to get some ham",
				"once outside do a lean", " to meat Mr. Mean!", "", "" }), // Ham
																			// Entrance
		Riddle4(12, new int[] { 2987, 3123, 3001, 3109 }, new String[] {
				"Near a ring known to teleport", "On a point full of mud",
				"A simple emote is needed",
				"An emote known as skipping or dance!", "", "", "", "" }), Riddle5(
				28, new int[] { 2884, 3449, 2898, 3438 }, new String[] {
						"This reward will require a bit",
						"For the first thing you will", "Need to be at a den",
						"and you have to be a rouge",
						"You must have an idea outside",
						"Of its entrance to get a reward!", "", "" });// Mudsckipper
																		// Point
		int[] locations;
		String[] riddles;
		int emoteid;

		Riddles(final int id, final int[] location,
				final String[] riddles) {
			this.locations = location;
			this.riddles = riddles;
			this.emoteid = id;
		}
		// Riddle interface 345
	}
	private enum ObjectMaps {
		Map1(358, new int[] { 18506, 2457, 3182 },
				"Near an observatory meant for getting a compas on RS!"), Map2(
				361, new int[] { 46331, 2565, 3248 },
				"Just south of a city known for thieving and outside a tower of clock!");

		int objectid, objectx, objecty;
		int[] objectinfo;
		String hint;
		int interid;

		ObjectMaps(final int interid, final int[] object,
				   final String text) {
			this.hint = text;
			this.interid = interid;
			this.objectinfo = object;
			this.objectid = object[0];
			this.objectx = object[1];
			this.objecty = object[2];
		}
	}

	private enum Maps {
		Map1(337, 2971, 3414,
				"If you Fala by A Door you might need help on this one!"), Map2(
				338, 3021, 3912,
				"Inbeetween a lava blaze and a near Deathly Agility Course!"), Map3(
				339, 2722, 3339,
				"South of where legneds may be placed, and east of great thieving!"), Map4(
				341,
				3435,
				3265,
				"South of a muchky mucky mucky mucky swamp lands, and barely north of Haunted Mines!"), Map5(
				344, 2665, 3561,
				"West of a murderous Mansion, and south of a city of vikings!"), Map6(
				346, 3166, 3359,
				"Slightly South of a city of great knights and lots of Shops!"), Map7(
				347, 3290, 3372,
				"A mining place located near a city of great knights and lots of Shops"), Map8(
				348, 3092, 3225,
				"Slightly south of a village known for thieving masters of farming!"), Map9(
				351, 3043, 3398,
				"NorthEast Corner of a city based around a castle with a mort around it!"), Map10(
				352, 2906, 3295,
				"Rite next to a guild known for people with skilled hands! [CRAFTING]"), Map11(
				353, 2616, 3077,
				"In a city that Rhymes with tan i will, if you say it really fast!"), Map12(
				354, 2612, 3482,
				"West of some woods that sound like Mc Jagger!"), Map13(356,
				3110, 3152, "South of a tower full of magical people!"), Map14(
				360,
				2652,
				3232,
				"North of a tower known to give life and south of a city that contains thieving!"), Map15(
				362, 2923, 3210,
				"West of the place best known for starting a house!");

		String chat;
		int interfaceId, xcoord, ycoord;

		Maps(final int interid, final int x, final int y,
			 final String hint) {
			this.interfaceId = interid;
			this.xcoord = x;
			this.ycoord = y;
			this.chat = hint;
		}
	}

}