package com.rs.core.net.handlers;

import com.rs.player.Player;
import com.sun.istack.internal.NotNull;

/**
 * @author John (FuzzyAvacado) on 12/21/2015.
 */
public interface PacketHandler {

    /**
     * Method is declared a boolean only for certain implementations. Not all implementations have to return true or false when processing.
     * By default just return false at the end of the process method unless using it as a check, then properly use it as a boolean method.
     *
     * @param player
     * @param parameters
     * @return
     */
    boolean process(@NotNull Player player, Object... parameters);
}
