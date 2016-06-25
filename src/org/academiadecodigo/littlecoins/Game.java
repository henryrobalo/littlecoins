package org.academiadecodigo.littlecoins;

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
    private int totalPlayers = 2;
    private Map<String, Integer> guessMap;
    private List betArray = new ArrayList<>();
    private int guess;
    private int totalBetCoins;


    public Game() {
        this.guessMap = new HashMap<>();

    }

    public int getTotalbets(){
        return betArray.size();
    }

    public void start() {

        //set the total coins depending on total players
        totalCoins = COINS * totalPlayers;


    }

    /**
     * Verify if the all the players have bet
     *
     * @return
     */
    public boolean hasBet() {
        return betArray.size() == totalPlayers;
    }

    /**
     * Verify if the bet is a integer between 0 - 3
     *
     * @param bet
     * @return
     */
    public boolean correctBet(String bet) {

        int value = Integer.parseInt(bet);

        return (value >= 0 && value <= 3);
    }

    public boolean hasGuess() {
        return guessMap.size() == totalPlayers;
    }

    /**
     * Verify the guess
     *
     * @param guess
     * @return
     */
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

    /**
     * Ends the game if total players reach 1
     *
     * @return
     */
    public boolean ends() {

        return totalPlayers == 1;
    }

    /**
     * Verify in one round if any player win
     *
     * @param guess
     * @return
     */
    public boolean hasWin(String guess) {

        int value = Integer.parseInt(guess);

        if (value == totalBetCoins) {
            return true;
        }
        return false;

    }

    /**
     * Calculate all coins are in game
     *
     * @param bet
     * @return
     */
    public int totalBetCoins(int bet) {

        totalBetCoins = totalBetCoins + bet;

        return totalBetCoins;

    }

    /**
     * Add guesses to the HashMap
     *
     * @param name
     * @param guess
     */
    public void add(String name, String guess) {

        int value = Integer.parseInt(guess);

        guessMap.put(name, value);
    }

    /**
     * Set bet on List of bets
     *
     * @param playerBet
     */
    public void setBet(String playerBet) {

        betArray.add(playerBet);

    }

    public int getTotalPlayers() {
        return totalPlayers;
    }

    public int getTotalCoins() {
        return totalCoins;
    }

}

