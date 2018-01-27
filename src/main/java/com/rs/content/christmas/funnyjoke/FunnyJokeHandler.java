package com.rs.content.christmas.funnyjoke;

import com.rs.server.GameConstants;
import com.rs.utils.Logger;
import com.rs.utils.Utils;
import com.rs.utils.file.FileUtilities;
import com.rs.player.Player;
import com.rs.world.item.Item;

import java.util.List;

/**
 * @author John (FuzzyAvacado) on 12/11/2015.
 */
public class FunnyJokeHandler {

    public static final String JOKES_LOC = GameConstants.DATA_PATH + "/jokes.txt";
    public static final int JOKE_ITEM_ID = 29998;

    private static List<String> jokes;

    public static void init() {
        jokes = FileUtilities.readLines(JOKES_LOC);
        Logger.info(FunnyJokeHandler.class, "Loaded " + jokes.size() + " Funny Jokes!");
    }

    public static void giveFunnyJoke(Player player) {
        player.getFunnyJoke().setJoke(getRandomFunnyJoke());
        Item jokeItem = new Item(JOKE_ITEM_ID, 1);
        if (player.getBank().getItem(JOKE_ITEM_ID) != null || player.getInventory().getItems().containsOne(jokeItem)) {
            player.getPackets().sendGameMessage("It appears you already have a funny joke...but what if it changed?!");
            return;
        }
        player.getInventory().addItem(jokeItem);
        player.getPackets().sendGameMessage("You have received a Funny Joke. Don't laugh too much!");
    }

    public static String getRandomFunnyJoke() {
        return jokes.get(Utils.random(jokes.size()));
    }
}
