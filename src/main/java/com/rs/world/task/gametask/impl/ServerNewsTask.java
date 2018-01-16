package com.rs.world.task.gametask.impl;

import com.rs.core.utils.Utils;
import com.rs.core.utils.file.FileUtilities;
import com.rs.world.World;
import com.rs.world.task.gametask.GameTask;
import com.rs.world.task.gametask.GameTaskManager;
import com.rs.world.task.gametask.GameTaskType;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author FuzzyAvacado
 */
@GameTaskType(GameTaskManager.GameTaskType.SLOW)
public class ServerNewsTask extends GameTask {

    private static final String NEWS_LOC = "./data/news.txt";

    private static final String NEWS_STRING = "<img=1><col=008000>News:</col></img><col=008000> ";
    private static final String END_COLOR = "</col>";

    private static List<String> newsList;

    public ServerNewsTask() {
        super(GameTask.ExecutionType.FIXED_DELAY, 0, 3, TimeUnit.MINUTES);
        newsList = FileUtilities.readLines(NEWS_LOC);
    }

    public static void addNews(String value) throws IOException {
        File file = new File(NEWS_LOC);
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        newsList.add(value);
        FileWriter fileWriter = new FileWriter(NEWS_LOC);
        for (String s : newsList) {
            fileWriter.write(s + System.lineSeparator());
        }
        fileWriter.close();
        newsList = FileUtilities.readLines(NEWS_LOC);
    }

    @Override
    public void run() {
        final int index = Utils.random(newsList.size());
        World.sendGlobalMessage(NEWS_STRING + newsList.get(index) + END_COLOR);
    }
}
