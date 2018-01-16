package com.rs.content.commands;

import com.rs.player.Player;

/**
 * @author FuzzyAvacado
 */
public interface Command {

    void handle(Player player, String[] cmd);
}
