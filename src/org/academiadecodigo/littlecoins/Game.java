package org.academiadecodigo.littlecoins;

import org.academiadecodigo.littlecoins.player.Player;
import org.academiadecodigo.littlecoins.server.Server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by codecadet on 20/06/16.
 */
public class Game {

    private static final int COINS = 3;
    private int totalCoins;
    private int totalPlayers=2;
    private Map<String, Integer> guessMap;
    //private String[] bet = new String[totalPlayers];
    private List betArray = new ArrayList<>();
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


    public  boolean hasBet(){
        if(betArray.size()<totalPlayers) {
            return false;
        }
        return true;
    }


    public synchronized boolean correctBet(String bet) {

        int value = Integer.parseInt(bet);

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

    public void setBet(String playerBet) {

       betArray.add(playerBet);

    }

    /*public void addBet(String bets){
        for (String s: bet) {
            s = bets;
        }
    }*/

}

