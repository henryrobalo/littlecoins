package org.academiadecodigo.concurrentchatserver.client;

import java.io.IOException;

/**
 * Created by codecadet on 17/06/16.
 */
public class MainPlayer {

    public static void main(String[] args) {

        Player player = new Player(7000,"localhost");
        player.startPlayer();

    }
}

