package com.rs.utils.tools;

import com.rs.core.cache.Cache;
import com.rs.core.cache.loaders.RenderAnimDefinitions;

import java.io.IOException;

public class RenderEmotes {
    public static void main(final String[] args) throws IOException {
        Cache.init();
        final int emoteId = 16652;
        for (int i = 0; i < 2000; i++) {
            final RenderAnimDefinitions defs = RenderAnimDefinitions
                    .getRenderAnimDefinitions(i);
            if (defs.anInt972 == emoteId || defs.anInt963 == emoteId) {
                System.out.println("RenderID: " + i);
            }
        }
    }
}
