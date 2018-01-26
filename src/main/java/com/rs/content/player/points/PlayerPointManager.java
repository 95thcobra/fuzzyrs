package com.rs.content.player.points;

import com.rs.content.economy.shops.ShopCurrency;

import java.io.Serializable;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/**
 * @author FuzzyAvacado
 */
public class PlayerPointManager implements Serializable {

    private static final long serialVersionUID = 5236225143248295313L;

    private Map<PlayerPoints, Integer> points;

    public PlayerPointManager() {
        points = new EnumMap<>(PlayerPoints.class);
        for (PlayerPoints playerPoints : PlayerPoints.values()) {
            setPoints(playerPoints, 0);
        }
    }

    public void setPoints(PlayerPoints playerPoints, int pointAmount) {
        points.put(playerPoints, pointAmount);
    }

    public void addPoints(PlayerPoints playerPoints, int pointAmount) {
        if (!points.containsKey(playerPoints)) {
            setPoints(playerPoints, pointAmount);
            return;
        }
        setPoints(playerPoints, getPoints(playerPoints) + pointAmount);
    }

    public void removePoints(PlayerPoints playerPoints, int pointAmount) {
        if (!points.containsKey(playerPoints)) {
            setPoints(playerPoints, 0);
            return;
        }
        setPoints(playerPoints, getPoints(playerPoints) - pointAmount);
    }

    public void resetPoints(PlayerPoints playerPoints) {
        setPoints(playerPoints, 0);
    }

    public Integer getPoints(PlayerPoints playerPoints) {
        if (!points.containsKey(playerPoints)) {
            setPoints(playerPoints, 0);
        }
        return points.get(playerPoints);
    }

    public PlayerPoints getPoints(ShopCurrency currency) {
        for (PlayerPoints p : points.keySet()) {
            if (p.toString().equalsIgnoreCase(currency.toString())) {
                return p;
            }
        }
        return null;
    }
}
