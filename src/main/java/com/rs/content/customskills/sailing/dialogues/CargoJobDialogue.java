package com.rs.content.customskills.sailing.dialogues;

import com.rs.content.customskills.sailing.SailingMap;
import com.rs.content.customskills.sailing.jobs.impl.CargoSailingJob;
import com.rs.content.dialogues.Dialogue;
import com.rs.content.dialogues.DialogueExpressions;
import com.rs.core.cache.loaders.NPCDefinitions;
import com.rs.utils.TextUtils;

import java.util.Arrays;
import java.util.HashMap;

/**
 * @author John (FuzzyAvacado) on 3/16/2016.
 */
public class CargoJobDialogue extends Dialogue {

    private static final String[] INITIAL_OPTIONS = {"I would like a job!", "Never mind"};
    private static HashMap<SailingMap.MapRequirements, Integer> NPC_LOCATIONS = new HashMap<>();

    static {
        NPC_LOCATIONS.put(SailingMap.MapRequirements.KARAMJA, 676);
        NPC_LOCATIONS.put(SailingMap.MapRequirements.CRANDOR, 1688);
        NPC_LOCATIONS.put(SailingMap.MapRequirements.BRIMHAVEN, 2109);
        NPC_LOCATIONS.put(SailingMap.MapRequirements.CATHERBY, 2110);
        NPC_LOCATIONS.put(SailingMap.MapRequirements.MOS_LE_HARMLESS, 2956);
        NPC_LOCATIONS.put(SailingMap.MapRequirements.OO_GLOG, 539);
        NPC_LOCATIONS.put(SailingMap.MapRequirements.PORT_KHAZARD, 540);
        NPC_LOCATIONS.put(SailingMap.MapRequirements.PORT_PHASMATYS, 560);
        NPC_LOCATIONS.put(SailingMap.MapRequirements.PORT_SARIM, 573);
        NPC_LOCATIONS.put(SailingMap.MapRequirements.PORT_TYRAS, 588);
        NPC_LOCATIONS.put(SailingMap.MapRequirements.SHIPYARD, 873);
    }

    private int npcId;

    @Override
    public void start() {
        npcId = (int) parameters[0];
        sendNPCDialogue(npcId, DialogueExpressions.HAPPY_TALKING.getId(), "Hey " + player.getDisplayName() + "! What can I do for you today?");
        stage = 0;
    }

    @Override
    public void run(int interfaceId, int componentId) {
        switch (stage) {
            case 0:
                sendOptionsDialogue("Choose an option:", INITIAL_OPTIONS);
                stage = 1;
                break;
            case 1:
                switch (componentId) {
                    case OPTION_1:
                        SailingMap.MapRequirements location = SailingMap.MapRequirements.values()[(int) (Math.random() * SailingMap.MapRequirements.values().length)];
                        CargoSailingJob job = new CargoSailingJob(player, npcId, NPC_LOCATIONS.get(location));
                        player.getSailingManager().startJob(job);
                        sendNPCDialogue(npcId, DialogueExpressions.HAPPY_TALKING.getId(), "I have given you a cargo of " + Arrays.toString(job.getCargo().getItems())
                                + ". Please deliver this to " + new NPCDefinitions(job.getTargetNpcId()).getName() + " at "
                                + TextUtils.upperCase(location.name().toLowerCase().replace("_", " ")) + ".");
                        break;
                    case OPTION_2:
                        sendNPCDialogue(npcId, DialogueExpressions.CALM_TALK.getId(), "Have a good day " + player.getDisplayName());
                        break;
                }
                stage = 2;
                break;
            case 2:
                end();
                break;
        }
    }

    @Override
    public void finish() {

    }
}
