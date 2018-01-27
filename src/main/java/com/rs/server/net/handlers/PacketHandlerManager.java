package com.rs.server.net.handlers;

import com.rs.utils.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * @author John (FuzzyAvacado) on 12/21/2015.
 */
public class PacketHandlerManager {

    private static final Map<Class<? extends PacketHandler>, PacketHandler> packetHandlers = new HashMap<>();

    public static PacketHandler get(Class<? extends PacketHandler> c) {
        if (!packetHandlers.containsKey(c)) {
            try {
                packetHandlers.put(c, c.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                Logger.handle(e);
            }
        }
        return packetHandlers.get(c);
    }

    public static void reset() {
        packetHandlers.clear();
    }
}
