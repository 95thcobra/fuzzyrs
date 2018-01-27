package com.rs.player;

import com.rs.utils.Utils;
import com.rs.world.Animation;
import com.rs.task.worldtask.WorldTask;
import com.rs.task.worldtask.WorldTasksManager;

import java.util.ArrayList;
import java.util.List;

public class FarmingManager {

	private static final int ALLOTMENT = 0, HERBS = 6, DIBBER = 5343,
			POTATO = 5318, WEEDS = 6055, BUCKET = 1925, RAKE = 5341,
			WATER = 5340, GUAM = 199, MARI = 5096;

	private final List<FarmingSpot> spots;
	private transient Player player;

	public FarmingManager() {
		spots = new ArrayList<FarmingSpot>();
	}

	@SuppressWarnings("unused")
	public static void useRake(final Player player, final int configId) {
		if (player.getInventory().containsItem(RAKE, 1))
			if (player.getInventory().containsItem(POTATO, 5))
				if (player.getInventory().containsItem(WATER, 1))
					if (player.getInventory().containsItem(DIBBER, 1)) {
						WorldTasksManager.schedule(new WorldTask() {
							int loop;
							@Override
							public void run() {
								if (loop == 0) {
									player.lock();
									player.setNextAnimation(new Animation(2273));
									player.getPackets().sendConfigByFile(
											configId, 1);
									player.getSkills().addXp(19, 80);
									player.getInventory().addItem(6055, 1);
								} else if (loop == 3) {
									player.setNextAnimation(new Animation(2273));
									player.getPackets().sendConfigByFile(
											configId, 2);
									player.getSkills().addXp(19, 80);
									player.getInventory().addItem(6055, 1);
								} else if (loop == 6) {
									player.setNextAnimation(new Animation(2273));
									player.getPackets().sendConfigByFile(
											configId, 3);
									player.getInventory().addItem(6055, 1);
									player.getSkills().addXp(19, 80);
								} else if (loop == 9) {
									player.setNextAnimation(new Animation(2291));
									player.getPackets().sendConfigByFile(
											configId, 6);
									player.getInventory().deleteItem(POTATO, 5);
									player.getSkills().addXp(19, 80);
								} else if (loop == 12) {
									player.setNextAnimation(new Animation(2293));
									player.getPackets().sendConfigByFile(
											configId, 7);
									player.getSkills().addXp(19, 80);
								} else if (loop == 15) {
									player.setNextAnimation(new Animation(2293));
									player.getPackets().sendConfigByFile(
											configId, 8);
									player.getSkills().addXp(19, 80);
								} else if (loop == 18) {
									player.setNextAnimation(new Animation(2293));
									player.getPackets().sendConfigByFile(
											configId, 9);
									player.getSkills().addXp(19, 80);
								} else if (loop == 21) {
									player.setNextAnimation(new Animation(2293));
									player.getPackets().sendConfigByFile(
											configId, 10);
									player.getSkills().addXp(19, 80);
								} else if (loop == 23) {
									player.setNextAnimation(new Animation(2286));
									player.getPackets().sendConfigByFile(
											configId, 0);
									player.getInventory().addItem(5438, 1);
									player.getSkills().addXp(19, 160);
									player.unlock();
								}
								loop++;
							}
						}, 0, 1);
					} else {
						player.getPackets()
								.sendGameMessage(
										"You need the following to make potato's: 1 Rake, 1 Dibber and 5 Potato seeds.");
					}
	}

	public static void useRake3(final Player player, final int configId) {
		if (player.getInventory().containsItem(RAKE, 1))
			if (player.getInventory().containsItem(GUAM, 1))
				if (player.getInventory().containsItem(WATER, 1))
					if (player.getInventory().containsItem(DIBBER, 1)) {

						WorldTasksManager.schedule(new WorldTask() {

							int loop;

							@Override
							public void run() {
								if (loop == 0) {
									player.lock();
									player.setNextAnimation(new Animation(2273));
									player.getPackets().sendConfigByFile(
											configId, 1);
									player.getSkills().addXp(19, 80);
									player.getInventory().addItem(6055, 1);
								} else if (loop == 3) {
									player.setNextAnimation(new Animation(2273));
									player.getPackets().sendConfigByFile(
											configId, 2);
									player.getSkills().addXp(19, 80);
									player.getInventory().addItem(6055, 1);
								} else if (loop == 6) {
									player.setNextAnimation(new Animation(2273));
									player.getPackets().sendConfigByFile(
											configId, 3);
									player.getInventory().addItem(6055, 1);
									player.getSkills().addXp(19, 80);
								} else if (loop == 9) {
									player.setNextAnimation(new Animation(2291));
									player.getPackets().sendConfigByFile(
											configId, 6);
									player.getInventory().deleteItem(GUAM, 1);
									player.getSkills().addXp(19, 80);
								} else if (loop == 12) {
									player.setNextAnimation(new Animation(2293));
									player.getPackets().sendConfigByFile(
											configId, 7);
									player.getSkills().addXp(19, 80);
								} else if (loop == 15) {
									player.setNextAnimation(new Animation(2293));
									player.getPackets().sendConfigByFile(
											configId, 8);
									player.getSkills().addXp(19, 80);
								} else if (loop == 21) {
									player.setNextAnimation(new Animation(2293));
									player.getPackets().sendConfigByFile(
											configId, 0);
									player.getSkills().addXp(19, 80);
									player.getInventory().addItem(199, 5);
									player.unlock();
								}
								loop++;
							}
						}, 0, 1);
					} else {
						player.getPackets()
								.sendGameMessage(
										"You need the following to make potato's: 1 Rake, 1 Dibber and 5 Potato seeds.");
					}
	}

	public static void useRake2(final Player player, final int configId) {
		if (player.getInventory().containsItem(RAKE, 1))
			if (player.getInventory().containsItem(MARI, 4))
				if (player.getInventory().containsItem(WATER, 1))
					if (player.getInventory().containsItem(DIBBER, 1)) {

						WorldTasksManager.schedule(new WorldTask() {

							int loop;

							@Override
							public void run() {
								if (loop == 0) {
									player.lock();
									player.setNextAnimation(new Animation(2273));
									player.getPackets().sendConfigByFile(
											configId, 1);
									player.getSkills().addXp(19, 80);
									player.getInventory().addItem(6055, 1);
								} else if (loop == 3) {
									player.setNextAnimation(new Animation(2273));
									player.getPackets().sendConfigByFile(
											configId, 2);
									player.getSkills().addXp(19, 80);
									player.getInventory().addItem(6055, 1);
								} else if (loop == 6) {
									player.setNextAnimation(new Animation(2273));
									player.getPackets().sendConfigByFile(
											configId, 3);
									player.getInventory().addItem(6055, 1);
									player.getSkills().addXp(19, 80);
								} else if (loop == 9) {
									player.setNextAnimation(new Animation(2291));
									player.getPackets().sendConfigByFile(
											configId, 8);
									player.getInventory().deleteItem(MARI, 4);
									player.getSkills().addXp(19, 80);
								} else if (loop == 12) {
									player.setNextAnimation(new Animation(2293));
									player.getPackets().sendConfigByFile(
											configId, 9);
									player.getSkills().addXp(19, 80);
								} else if (loop == 15) {
									player.setNextAnimation(new Animation(2293));
									player.getPackets().sendConfigByFile(
											configId, 10);
									player.getSkills().addXp(19, 80);
								} else if (loop == 18) {
									player.setNextAnimation(new Animation(2293));
									player.getPackets().sendConfigByFile(
											configId, 11);
									player.getSkills().addXp(19, 80);
								} else if (loop == 21) {
									player.setNextAnimation(new Animation(2293));
									player.getPackets().sendConfigByFile(
											configId, 12);
									player.getSkills().addXp(19, 80);
								} else if (loop == 23) {
									player.setNextAnimation(new Animation(2286));
									player.getPackets().sendConfigByFile(
											configId, 0);
									player.getInventory().addItem(6010, 4);
									player.getSkills().addXp(19, 160);
									player.unlock();
								}
								loop++;
							}
						}, 0, 1);
					} else {
						player.getPackets()
								.sendGameMessage(
										"You need the following to make potato's: 1 Rake, 1 Dibber and 5 Potato seeds.");
					}
	}

	public static void makeCompost(final Player player, final int configId) {
		if (player.getInventory().containsItem(WEEDS, 3))
			if (player.getInventory().containsItem(BUCKET, 1)) {
				WorldTasksManager.schedule(new WorldTask() {
					int loop;
					@Override
					public void run() {
						if (loop == 0) {
							player.setNextAnimation(new Animation(2283));
							player.getSkills().addXp(19, 400);
							player.getInventory().deleteItem(6055, 3);
							player.getInventory().deleteItem(BUCKET, 1);
							player.getInventory().addItem(6032, 1);
						}
						loop++;
					}
				}, 0, 1);
			} else {
				player.getPackets()
						.sendGameMessage(
								"You'll need 3 weeds and a bucket in order to make compost.");
			}
	}

	public void setPlayer(final Player player) {
		this.player = player;
	}

	public void init() {
		for (final FarmingSpot spot : spots) {
			spot.refresh();
		}
	}

	public boolean isFarming(final int objectId, final int optionId) {
		for (final SpotInfo info : SpotInfo.values()) {
			if (info.objectId == objectId) {
				handleFarming(info, optionId);
				return true;
			}
		}
		return false;
	}

	private FarmingSpot getSpot(final SpotInfo info) {
		for (final FarmingSpot spot : spots)
			if (spot.spotInfo.equals(info))
				return spot;
		return null;
	}

	public void handleFarming(final SpotInfo info, final int optionId) {
		final FarmingSpot spot = getSpot(info);
		if (spot == null) {
			switch (optionId) {
				case 0: // rake
					break;
				case 1: // inspect
					sendNeedsWeeding();
					break;
				case 2: // guide
					sendGuide();
					break;
			}
		} else {
			switch (info.type) {
				case ALLOTMENT:

					break;
			}
		}
	}

	private void sendGuide() {
		player.getTemporaryAttributtes().put("skillMenu", 21);
		player.getPackets().sendConfig(965, 21);
		player.getInterfaceManager().sendInterface(499);
	}

	public void sendNeedsWeeding() {
		player.getPackets().sendGameMessage("The patch needs weeding.");
	}

	private enum ProductInfo {
		Potato(5318, 1, 1942, 0, ALLOTMENT), Onion(5319, 5, 1957, 1, ALLOTMENT), Cabbage(
				5324, 7, 1965, 2, ALLOTMENT), Tomato(5322, 12, 1982, 3,
				ALLOTMENT), Sweetcorn(5320, 20, 5986, 4, ALLOTMENT), Strawberry(
				5323, 31, 5504, 5, ALLOTMENT), Watermelon(5321, 47, 5982, 6,
				ALLOTMENT);

		private int seedId;
		private int level;
		private int productId;
		private int configIndex;
		private int type;

		ProductInfo(final int seedId, final int level,
					final int productId, final int configIndex, final int type) {
			this.setSeedId(seedId);
			this.setLevel(level);
			this.setProductId(productId);
			this.configIndex = configIndex;
			this.type = type;
		}

		@SuppressWarnings("unused")
		public int getSeedId() {
			return seedId;
		}

		public void setSeedId(final int seedId) {
			this.seedId = seedId;
		}

		@SuppressWarnings("unused")
		public int getLevel() {
			return level;
		}

		public void setLevel(final int level) {
			this.level = level;
		}

		@SuppressWarnings("unused")
		public int getProductId() {
			return productId;
		}

		public void setProductId(final int productId) {
			this.productId = productId;
		}
	}

	private enum SpotInfo {
		Falador_Herb_patch(8550, 780, HERBS), Falador_Allotment_North(8550,
				708, ALLOTMENT), Falador_Allotment_South(8551, 709, ALLOTMENT);

		private int objectId;
		private int configFileId;
		private int type;

		SpotInfo(final int objectId, final int configFileId,
				 final int type) {
			this.objectId = objectId;
			this.configFileId = configFileId;
			this.type = type;
		}
	}

	private class FarmingSpot {

		private final SpotInfo spotInfo;
		private final ProductInfo productInfo;
		private int stage;
		private long cycleTime;
		@SuppressWarnings("unused")
		private boolean watered;

		@SuppressWarnings("unused")
		public FarmingSpot(final SpotInfo spotInfo,
				final ProductInfo productInfo) {
			this.spotInfo = spotInfo;
			this.productInfo = productInfo;
			cycleTime = Utils.currentTimeMillis();
			stage = 1; // stage 0 is default null
			renewCycle();
		}

		public int getConfigValue() {
			if (productInfo != null) {
				if (productInfo.type == ALLOTMENT)
					return 6 + (productInfo.configIndex * 7);
			}
			return 0;
		}

		@SuppressWarnings("unused")
		public void process() {
			if (cycleTime == 0)
				return;
			while (cycleTime < Utils.currentTimeMillis()) {
				if (productInfo != null) {
					increaseStage();
					if (reachedMaxStage()) {
						cycleTime = 0;
						break;
					}
				} else {
					desecreaseStage();
					if (stage == 0) {
						remove();
						break;
					}
				}
				renewCycle();
			}

		}

		public void increaseStage() {
			stage++;
			refresh();
		}

		public void desecreaseStage() {
			stage--;
			refresh();
		}

		public void renewCycle() {
			cycleTime += 5000; // 5sec atm
		}

		public boolean reachedMaxStage() {
			return spotInfo.type == ALLOTMENT && stage == 4; // max stage ready
		}

		private void refresh() {
			player.getPackets().sendConfigByFile(spotInfo.configFileId,
					getConfigValue() + stage);
		}

		private void remove() {
			spots.remove(this);
		}

	}
}
