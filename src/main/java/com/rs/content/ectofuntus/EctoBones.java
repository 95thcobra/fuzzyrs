package com.rs.content.ectofuntus;

import com.rs.content.actions.Action;
import com.rs.player.Player;

public class EctoBones extends Action {

    int itemId;
    int step = 0;

    public EctoBones(int itemId) {
        this.itemId = itemId;
    }

    @Override
    public boolean start(Player player) {
        step = 0;
        return true;
    }

    @Override
    public boolean process(Player player) {
        return step != -1;
    }

    @Override
    public int processWithDelay(Player player) {
        switch (step) {
            case 0:
                if (!Ectofuntus.handleObjects(player, itemId))
                    step = -1;
                else
                    step++;
                return 2;
            case 1:
                if (!Ectofuntus.handleObjects(player, itemId))
                    step = -1;
                else
                    step++;
                return 2;
            case 2:
                if (!Ectofuntus.handleObjects(player, itemId))
                    step = -1;
                else
                    step = 0;
                return 2;
            default:
                stop(player);
        }
        return 4;
    }

    @Override
    public void stop(Player player) {

    }
}
