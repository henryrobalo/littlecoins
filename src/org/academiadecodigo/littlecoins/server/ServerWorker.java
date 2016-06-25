package org.academiadecodigo.littlecoins.server;

import org.academiadecodigo.littlecoins.FileManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by codecadet on 17/06/16.
 */
public class ServerWorker implements Runnable {

    private Server server;
    private PrintWriter out = null;
    private BufferedReader in = null;
    private Socket playerSocket = null;
    private boolean alreadyWin;
    private boolean correctName;
    private boolean correctBet;
    private boolean correctGuess;
    private volatile String name;
    private String hand;
    private int guess;

    public ServerWorker(Socket playerSocket, Server server) {

        this.playerSocket = playerSocket;
        this.server = server;
    }

    /**
     * Creates streams output & input
     */
    @Override
    public void run() {

        try {

            //Create streams and accept connections

            out = new PrintWriter(playerSocket.getOutputStream(), true);

            in = new BufferedReader(new InputStreamReader(playerSocket.getInputStream()));

            out.println(FileManager.readFile());

            out.println("Please enter your name.");

            acceptName();

            send("Waiting for other users");

            synchronized (server) {

                System.out.println("Notify server");
                server.notifyAll();

                //Wait for all the players write the name

                while (!server.gameStarts) {
                    try {
                        System.out.println("waiting for game start");
                        server.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            //Receive the bet from the players TODO

            out.println("You have 3 Coins , choose how many coins you want to hand..(0-3)");

            acceptBet();

            String line = "";

            //Receives and verifies

            while ((line = in.readLine()) != null) {

                if (line.contains("GUESS")) {
                    System.out.println("GUESS");
                    while (!server.isCorrectGuess(line)) {

                        System.out.println("im guessing");

                        try {
                            in.readLine();
                            System.out.println("incorrect guess");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    server.guess(name, line);

                } else {

                    System.out.println("BETING");

                    while (!server.isCorrectBet(line)) {

                        send("im beting");

                        try {
                            in.readLine();
                            System.out.println("incorrect bet");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    server.bet(line);

                }

            }

            System.out.println("ENDED");


         /*   if (in.readLine().contains("Guess")) {
                server.sendToAll(name, Integer.toString(guess));
            } else {

            }*/
            //while (in.readLine()) //blokear a ler a espera de aposta.             {
            //metodos do servidor que sejam relevantes
            //if line.contains("GUESS"){server.guess(NOME, GUESS)}
            //else{ server.bet(nome,APOSTA);}
            // servidor.jodata("NOMEDO GAJO",APOSTA);
            //}


            while (!alreadyWin) {


                // acceptBet();

                out.println("Please insert your guess between 0 and " + (server.getCounterPlayers() * 3) + " coins\"!!!");

                //acceptGuess();


                alreadyWin = false;
                // playerSocket.close()


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Fetches name, and check availability in the server.
     */
    private void acceptName() {

        //TODO TEST ACCEPT NAME

        //todo podia fazer o set da thread aqui depois de o server dar o ok  mas tinha k ter lock no server

        synchronized (server) {

            while (!correctName) {
                String tName = null;
                try {
                    tName = in.readLine();

                    System.out.println("" + tName);

                    if (server.containsName(tName) || tName.equals("")) {
                        out.println("This name is already in the list. Please change name");
                    } else {
                        server.setName(this, tName);
                        //setName(tName);
                        //out.println("Name changed to " + tName);
                        correctName = true;
                        out.println("Your name was accepted!");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private void acceptBet() {

        synchronized (server) {

            while (!correctBet) {

                String tempBet = null;
                try {
                    tempBet = in.readLine();


                if (server.getGame().correctBet(tempBet)) {
                    hand = tempBet;
                    out.println(hand);
                    out.println("Bet accept!");//nao alterar este print é a condiçao de saida do while na playerThread
                    System.out.println("Player " + name + " hand is: " + hand);
                    correctBet = true;
                    server.addBet(hand);

                } else {
                    out.println("Make sure you hand between 0 or 3 coins!");
                    out.println("Place your hand , between 0 - 3 : ");
                }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void acceptGuess() {

        synchronized (server) {

            while (!correctGuess) {

                String tempGuess = null;
                try {
                    tempGuess = in.readLine();


                int value = Integer.parseInt(tempGuess);

                if (server.containsGuess(value)) {
                    out.println("The guess " + value + " is already on the list");
                } else if (value > (server.getCounterPlayers() * 3)) {
                    out.println("Make sure you hand between 0 and " + (server.getCounterPlayers() * 3) + " coins");

                } else {
                    guess = value;
                    out.println(guess);
                    out.println("Guess accept!");//nao alterar este print é a condiçao de saida do while na playerThread
                    System.out.println("Player " + name + " guess is: " + value);
                    correctGuess = true;


                    server.sendToAll(Integer.toString(guess), name);

                }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void send(String msg) {

        out.println(msg);

    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getHand() {
        return hand;
    }

    public int getGuess() {
        return guess;
    }

    public boolean isAlreadyWin() {
        return alreadyWin;
    }

 /*int value = Integer.parseInt(tempBet);


 if (value >= 0 && value <= 3) {
     hand = value;
     out.println(hand);
     out.println("Bet accept!");//nao alterar este print é a condiçao de saida do while na playerThread
     System.out.println("Player " + name + " hand is: " + value);
     correctBet = true;

 } else {
     out.println("Make sure you hand between 0 or 3 coins!");
     out.println("Place your hand , between 0 - 3 : ");
 }*/

}

