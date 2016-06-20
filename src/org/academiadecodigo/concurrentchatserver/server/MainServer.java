package org.academiadecodigo.concurrentchatserver.server;

import org.academiadecodigo.concurrentchatserver.server.Server;

/**
 * Created by codecadet on 17/06/16.
 */
public class MainServer {

    public static void main(String[] args) {

        Server server = new Server(Integer.parseInt(args[0]),10);

        server.startServer();
    }
}
