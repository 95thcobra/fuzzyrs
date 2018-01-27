package com.rs.content.items;

import com.rs.core.cache.loaders.ItemDefinitions;
import com.rs.server.GameConstants;
import com.rs.utils.Logger;
import com.rs.utils.Utils;
import com.rs.utils.file.FileUtilities;
import com.rs.player.Player;
import com.rs.world.item.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author John (FuzzyAvacado) on 12/13/2015.
 */
public class WeightManager {

    private static final String WEIGHTS_PATH = GameConstants.DATA_PATH + "/items/weights.txt";
    private static final int[] WEIGHT_REDUCERS = {88, 10069, 10073,
            10663, 10071, 10074, 10664, 10553, 10554, 24210, 24211, 14938,
            14939, 24208, 24209, 14936, 14937, 24206, 24207, 24560, 24561,
            24562, 24563, 24654, 24801, 24802, 24803, 24804, 24805};
    private static Map<Integer, Double> itemWeight = new HashMap<>();

    public static void init() {
        List<String> values = FileUtilities.readLines(WEIGHTS_PATH);
        for (String s : values) {
            String[] info = s.split(" - ");
            itemWeight.put(Integer.valueOf(info[0]), Double.parseDouble(info[1]));
        }
        Logger.info(WeightManager.class, "Loaded " + itemWeight.size() + " Item weights.");
    }

    public static double calculateWeight(Player player) {
        player.setWeight(0);
        for (int REDUCERS : WEIGHT_REDUCERS) {
            if (player.getEquipment().getItems().contains(new Item(REDUCERS))) {
                player.setWeight(player.getWeight() - getReducersWeight(REDUCERS));
            }
        }
        for (int i = 0; i <= Utils.getItemDefinitionsSize(); i++) {
            if (player.getInventory().containsItem(i, 1)) {
                player.setWeight(player.getWeight() + getWeight(i) * player.getInventory().getNumberOf(i));
            } else if (player.getEquipment().getItems().contains(new Item(i))) {
                player.setWeight(player.getWeight() + getWeight(i));
            }
        }
        player.getPackets().sendWeight(player.getWeight());
        return player.getWeight();
    }

    private static double getWeight(int itemId) {
        if (ItemDefinitions.getItemDefinitions(itemId).isNoted()) {
            return 0;
        }
        for (int REDUCERS : WEIGHT_REDUCERS) {
            if (itemId == REDUCERS) {
                return 0.3;
            }
        }
        return itemWeight.get(itemId);
    }

    private static double getReducersWeight(int itemId) {
        if (ItemDefinitions.getItemDefinitions(itemId).isNoted()) {
            return 0;
        }
        return itemWeight.get(itemId);
    }


}