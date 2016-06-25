package org.academiadecodigo.littlecoins.server;


import org.academiadecodigo.littlecoins.Game;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by codecadet on 17/06/16.
 */
public class Server {

    private int portNumber;
    private int maxPlayer;
    private ServerSocket serverSocket = null;
    private boolean gameEnds;
    public boolean gameStarts;
    private Game game;
    private List<ServerWorker> serverWorkerList = Collections.synchronizedList(new LinkedList<>());

    public Server(int portNumber, int maxPlayer) {
        this.portNumber = portNumber;
        this.maxPlayer = maxPlayer;
        game = new Game();
    }

    public void startServer() {

        ExecutorService pool = Executors.newFixedThreadPool(10);

        try {
            System.out.println("Welcome to littleCoins Server...");
            serverSocket = new ServerSocket(portNumber);
            System.out.println("Server Started...");
            System.out.println("Waiting for Players...");

     /*
     * Create a client socket for each connection and pass it to a new client
     * thread.
     */
            int i = 0;
            game = new Game();

            while (i < maxPlayer) {

                Socket playerSocket = serverSocket.accept();

                System.out.println("Connection from : " + playerSocket.getInetAddress());

                ServerWorker sw = new ServerWorker(playerSocket, this);

                i++;

                serverWorkerList.add(sw);
                Thread thread = new Thread(sw);

                pool.submit(thread);
            }

            System.out.println("game starting");

            /**
             * Waiting for client connection
             */
            synchronized (this) {
                while (!this.checkNames()) {
                    System.out.println("server: Waiting for players");
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                this.gameStarts = true;
                this.notifyAll();
            }

            game.start();

            /**
             * Starts game logic
             */
            while (!gameEnds) {
                //Send token to unlock the client
                for (ServerWorker sw : serverWorkerList) {
                    sw.send("NAME");
                }

                //Waiting for bets
                synchronized (this) {
                    try {
                        System.out.println("waiting for bets");
                        while (!game.hasBet()) {
                            this.wait();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("START GUESSING");

                synchronized (this) {
                    System.out.println("Wait for guesses");
                    while (!game.hasGuess()) {
                        System.out.println("entrou");
                        try {
                            this.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

                //guess:

                //while(!game.ends)
                //  for each in array:
                //      send: "qual é a tua bet?"
                //      send: "TOKEN"

                // wait // notifica no metodo bet
                //what is the guess
                //  for each in array:
                //      sendall luis a jogar
                //      send: "qual é o teu guess?"
                //      send: "TOKEN"
                //      wait
                // game.hasWhin?  : game.removePlayer.
                //was the last player? yes: break;

                //send all last player was tal. he pays the beer.

            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            pool.shutdown();
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * Set players names and notify all players
     *
     * @param thread serverworker
     * @param name   players names
     * @return
     */
    public synchronized boolean setName(ServerWorker thread, String name) {
        thread.setName(name);
        this.notifyAll();
        return true;
    }

    /**
     * Check if all the players have set the name
     *
     * @return
     */
    public synchronized boolean checkNames() {
        for (ServerWorker c : serverWorkerList) {
            System.out.println("Checking names " + c.getName());
            if (c.getName() == null) return false;
        }
        return true;
    }

    /**
     * Checks for repeated names
     *
     * @param name
     * @return
     */
    public boolean containsName(String name) {
        for (ServerWorker c : serverWorkerList) {

            if (c.getName() != null && c.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Send message for all the players
     *
     * @param message
     * @param name
     */
    public void sendToAll(String message, String name) {

        /* Broadcast the message to all other clients. */
        synchronized (serverWorkerList) {

            for (ServerWorker c : serverWorkerList) {

                c.send(message);
            }
        }
    }

    /**
     * Total players
     *
     * @return
     */
    public int getCounterPlayers() {
        return serverWorkerList.size();
    }

    /**
     * Check if the bet is valid
     *
     * @param playerBet
     */
    public void bet(String playerBet) {

        if (game.correctBet(playerBet)) {
            game.setBet(playerBet);


            System.out.println("bet accepted");

        } else {
            System.out.println("quase que so faxzeis merdd");

        }
    }

    public boolean isCorrectBet(String bet) {
       return game.correctBet(bet);
    }

    public boolean isCorrectGuess(String guess) {
        return game.correctGuess(guess);
    }

    public void guess(String name, String guess) {
        if(game.correctGuess(guess)){
            game.add(name, guess);
            System.out.println("guess accepted");
        }
        System.out.println("vamos ver se dá merda");
    }

    public boolean containsGuess(int value) {

        for (ServerWorker c : serverWorkerList) {

            if (c.getGuess() >= 0 && c.getGuess() == value) {
                return true;
            }
        }
        return false;
    }

    public Game getGame() {
        return game;
    }

    public void addBet(String hand) {
        game.setBet(hand);
    }
}
