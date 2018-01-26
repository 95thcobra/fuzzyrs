package com.rs;

import com.rs.server.Server;

import java.io.IOException;

/**
 * Created by Fuzzy 1/25/2018
 */
public class Launcher {

    public static void main(String... args) throws ClassNotFoundException, IOException, InstantiationException, IllegalAccessException {
        Server.setInstance(new Server.Builder().build()).start();
    }
}
