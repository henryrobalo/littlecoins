package org.academiadecodigo.concurrentchatserver.client;

import java.io.IOException;

/**
 * Created by codecadet on 17/06/16.
 */
public class MainPlayer {

    public static void main(String[] args) {

        Player client = null;

        try {
            client = new Player(Integer.parseInt(args[0]), args[1]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        client.startClient();
    }
}

