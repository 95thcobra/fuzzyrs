/**
 * @author Zephyr. tense0087 - skype
 */
package com.rs.game.content.farming;

/**
 * @author Zephyr. tense0087 - skype
 */
public class FarmingConstants {
	
	public static final int[] Seedids = {5318};
	public static final int[] Levels = {1};
	public static final String[] rewardAmount = {"1-4"};
	public static final int[] Reward = {1942};
	public static final int[] XPatPlant = {8};
	public static final int[] XPatHarvest = {9};
	public static final String[] ProgressIds = {""};
	
	
	
	
	public static int GetIndex(int seed) {
		for(int i = 0; i < Seedids.length; i++) {
			if(Seedids[i] == seed)
				return i;
		}
		return -1;
	}
	
	
	public static boolean IsRaked(Patch patch) {
		if(patch.GetProgress() >= 3)
			return true;
		return false;
	}
	
	public static boolean IsGrowing(Patch patch) {
		if(patch.start > 3 && patch.GetProgress() < patch.limit)
			return true;
		return false;
	}
	
	public static void SetCorrupt(Patch patch) {
		patch.IsCorrupted = true;
	}

}
