package org.academiadecodigo.littlecoins.server;

import org.academiadecodigo.littlecoins.Game;

/**
 * Created by codecadet on 17/06/16.
 */
public class MainServer {

    public static void main(String[] args) {

        Server server = new Server(7000,5);

        server.startServer();
    }
}
