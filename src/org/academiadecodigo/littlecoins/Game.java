package org.academiadecodigo.littlecoins;

import org.academiadecodigo.littlecoins.player.Player;
import org.academiadecodigo.littlecoins.server.Server;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by codecadet on 20/06/16.
 */
public class Game {

    private static final int COINS = 3;
    private int totalCoins;
    private int totalPlayers;
    private Map<String, Integer> guessMap;

    private int guess;
    private int totalHands;



    public Game() {
        this.guessMap = new HashMap<>();

    }

    public int getTotalPlayers() {
        return totalPlayers;
    }

    public int getTotalCoins() {
        return totalCoins;
    }

    public void start() {

        totalCoins = COINS * totalPlayers;


    }


    public synchronized boolean correctHand(String hand) {


        int value = Integer.parseInt(hand);

        if (value >= 0 && value <= 3) {

            return true;

        }

        return false;
    }


    public synchronized boolean correctGuess(String guess) {

        int value = Integer.parseInt(guess);

        if (value < totalCoins) {

            for (String name : guessMap.keySet()) {

                if (guessMap.get(name) != value) ;

                return true;

            }

        }
        return false;


    }


    public boolean ends() {

        if(totalPlayers==1){
            return true;
        }

        return false;

    }


    public boolean hasWin(String guess){

        int value = Integer.parseInt(guess);

        if(value==totalHands){
            return true;
        }
        return false;

    }

    public int totalCoinsOnHand(int hands){


        totalHands = totalHands+hands;

        return totalHands;


    }


    public void add(String name, String guess) {

        int value = Integer.parseInt(guess);

        guessMap.put(name, value);

    }

}

