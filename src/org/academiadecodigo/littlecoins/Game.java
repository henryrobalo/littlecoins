package org.academiadecodigo.littlecoins;

import org.academiadecodigo.littlecoins.player.Player;
import org.academiadecodigo.littlecoins.server.Server;

/**
 * Created by codecadet on 20/06/16.
 */
public class Game {

    private static final int COINS = 3;
    private int totalCoins;
    Server server;
    Player player;
    private  int totalPlayers ;

    public Game(Server server) {
        this.server = server;
    }

    public int getTotalPlayers() {
        return totalPlayers;
    }

    public int getTotalCoins() {
        return totalCoins;
    }

    public void start() {


        //totalPlayers = server.getCounterPlayers();


        totalPlayers = server.getCounterPlayers();

        totalCoins = totalPlayers * COINS;

        System.out.println("Im the game"+totalPlayers);
        System.out.println("Im the game"+totalCoins);




    }
}

