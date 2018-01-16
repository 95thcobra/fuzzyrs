package com.rs.content.customskills.sailing;

import com.rs.content.customskills.sailing.jobs.SailingJob;
import com.rs.content.customskills.sailing.ships.PlayerShip;
import com.rs.content.customskills.sailing.ships.Ships;
import com.rs.player.Player;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author John (FuzzyAvacado) on 12/18/2015.
 */
public class SailingManager implements Serializable {

    private static final long serialVersionUID = -373736188336462900L;

    private final Map<Ships, PlayerShip> playerShips;
    private SailingJob currentJob;

    private transient Player player;

    public SailingManager(Player player) {
        this.player = player;
        playerShips = new HashMap<>();
    }

    public Map<Ships, PlayerShip> getPlayerShips() {
        return playerShips;
    }

    public PlayerShip getPlayerShip(Ships ship) {
        return playerShips.get(ship);
    }

    public boolean hasShip(Ships ship) {
        return playerShips.containsKey(ship);
    }

    public void addShip(Ships ship) {
        PlayerShip playerShip = new PlayerShip();
        playerShip.setShip(ship);
        playerShips.put(ship, playerShip);
    }

    public void levelUpShip(Ships ship) {
        playerShips.get(ship).levelUp(player);
    }

    public void removeShip(Ships ship) {
        playerShips.remove(ship);
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public SailingJob getCurrentJob() {
        return currentJob;
    }

    public void startJob(SailingJob currentJob) {
        this.currentJob = currentJob;
        currentJob.start();
    }

    public void endJob() {
        this.currentJob.finish();
        this.currentJob = null;
    }
}
