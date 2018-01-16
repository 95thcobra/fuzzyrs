package com.rs.content.customskills.sailing.dialogues;

import com.rs.content.customskills.sailing.SailingController;
import com.rs.content.customskills.sailing.SailingMap;
import com.rs.content.customskills.sailing.ships.Ships;
import com.rs.content.dialogues.Dialogue;
import com.rs.content.dialogues.types.SimpleMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * @author John (FuzzyAvacado) on 3/6/2016.
 */
public class PickShipDialogue extends Dialogue {

    List<Ships> ships;
    int shipChoice = -1;
    private SailingMap.MapRequirements req;

    @Override
    public void start() {
        if (parameters.length > 0) {
            req = (SailingMap.MapRequirements) parameters[0];
        }
        ships = new ArrayList<>(player.getSailingManager().getPlayerShips().keySet());
        if (ships.size() < 1) {
            player.getInterfaceManager().closeScreenInterface();
            player.getDialogueManager().startDialogue(SimpleMessage.class, "You do not own any ships! Go buy a ship in Port Sarim.");
            return;
        }
        if (ships.size() == 1) {
            sendOptionsDialogue("Choose a ship:", ships.get(0).name());
        } else {
            sendOptionsDialogue("Choose a ship:", getShips(0, ships.size(), ships.size()));
        }
        stage = 1;
    }

    public String[] getShips(int initial, int max, int size) {
        String[] shipNames = new String[size];
        for (int i = initial; i < max; i++) {
            String name = ships.get(i).name();
            if (name == null || name.equals(""))
                continue;
            shipNames[i - initial] = name;
        }
        return shipNames;
    }

    @Override
    public void run(int interfaceId, int componentId) {
        switch (stage) {
            case 1:
                if (componentId == OPTION_1) {
                    shipChoice = 0;
                } else if (componentId == OPTION_2) {
                    shipChoice = 1;
                } else if (componentId == OPTION_3) {
                    shipChoice = 2;
                } else if (componentId == OPTION_4) {
                    shipChoice = 3;
                } else if (componentId == OPTION_5) {
                    shipChoice = 4;
                }
                complete();
                break;
        }
    }

    public void complete() {
        end();
        player.getInterfaceManager().closeScreenInterface();
        if (shipChoice != -1 && ships.size() - 1 >= shipChoice) {
            player.getControllerManager().startController(SailingController.class, req, player.getSailingManager().getPlayerShip(ships.get(shipChoice)), 0);
        }
    }

    @Override
    public void finish() {

    }
}
