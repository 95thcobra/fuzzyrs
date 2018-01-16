package com.rs.player;

import com.rs.content.actions.skills.Skills;
import com.rs.core.utils.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class QuestManager implements Serializable {

    private static final long serialVersionUID = -8085932531253271252L;
    private final List<Quests> completedQuests;
    private transient Player player;
    private HashMap<Quests, Integer> questStages;

    public QuestManager() {
        completedQuests = new ArrayList<>();
    }

    public void setPlayer(final Player player) {
        this.player = player;
        if (questStages == null) {
            questStages = new HashMap<>();
        }
    }

    public int getQuestStage(final Quests quest) {
        if (completedQuests.contains(quest))
            return -1;
        final Integer stage = questStages.get(quest);
        return stage == null ? -2 : stage;
    }

    public void setQuestStageAndRefresh(final Quests quest, final int stage) {
        setQuestStage(quest, stage);
        sendStageData(quest);
    }

    public void setQuestStage(final Quests quest, final int stage) {
        if (completedQuests.contains(quest))
            return;
        questStages.put(quest, stage);
    }

    public void init() {
        checkCompleted(); // temporary
        completedQuests.forEach(this::sendCompletedQuestsData);
        questStages.keySet().forEach(this::sendStageData);
    }

    public void checkCompleted() {
        if (!completedQuests.contains(Quests.PERIL_OF_ICE_MONTAINS)
                && player.getSkills().hasRequiriments(Skills.CONSTRUCTION, 10,
                Skills.FARMING, 10, Skills.HUNTER, 10, Skills.THIEVING,
                11)) {
            completeQuest(Quests.PERIL_OF_ICE_MONTAINS);
        }
    }

    public void completeQuest(final Quests quest) {
        completedQuests.add(quest);
        questStages.remove(quest);
        sendCompletedQuestsData(quest);
        player.getPackets().sendGameMessage(
                "<col=ff0000>You have completed quest: "
                        + Utils.formatPlayerNameForDisplay(quest.toString())
                        + ".");
        // message completed quest
    }

    public void sendCompletedQuestsData(final Quests quest) {
        switch (quest) {
            case PERIL_OF_ICE_MONTAINS:
                player.getPackets().sendConfigByFile(4684, 150); // air machine
                // instead of
                // dragons +
                // trapdoor
                // opened
                break;
        }
    }

    private void sendStageData(final Quests quest) {
        switch (quest) {
            case NOMADS_REQUIEM:
                player.getPackets().sendConfigByFile(6962, 3);
                break;
        }
    }

    public boolean completedQuest(final Quests quest) {
        return completedQuests.contains(quest);
    }

    public enum Quests {
        PERIL_OF_ICE_MONTAINS, NOMADS_REQUIEM

    }
}