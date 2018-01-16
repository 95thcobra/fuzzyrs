package com.rs.core.net;

import org.jboss.netty.channel.Channel;

import java.net.SocketAddress;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author John (FuzzyAvacado) on 12/13/2015.
 */
public class SessionLimiter {

    /**
     * The map of session connected to us
     */
    private final Map<String, Integer> CURRENT_CONNECTIONS;

    public SessionLimiter() {
        CURRENT_CONNECTIONS = Collections.synchronizedMap(new HashMap<>());
    }

    public static String getIP(SocketAddress socketAddress) {
        return socketAddress == null ? "" : socketAddress.toString().split(":")[0].replace("/", "");
    }

    /**
     * Getting the amount of connections for our channel
     *
     * @param channel The channel connected to us
     * @return
     */
    public Integer getConnections(Channel channel) {
        Integer count = CURRENT_CONNECTIONS.get(getIP(channel.getRemoteAddress()));
        return count == null ? 0 : count;
    }

    /**
     * Adds the connection to the map
     *
     * @param channel
     */
    public void addConnection(Channel channel) {
        String ipString = getIP(channel.getRemoteAddress());
        Integer count = CURRENT_CONNECTIONS.get(ipString);
        CURRENT_CONNECTIONS.put(ipString, count == null ? 1 : ++count);
    }

    /**
     * Removes the connection from the map
     *
     * @param channel
     */
    public void removeConnection(Channel channel) {
        CURRENT_CONNECTIONS.remove(getIP(channel.getRemoteAddress()));
    }
}
