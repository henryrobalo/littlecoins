package org.academiadecodigo.littlecoins.server;

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
    public int bet = -1;    //TODO MUDAR ISTO PK ISTO ESTA MAL
    private boolean alreadyWin;
    private boolean correctName;
    private boolean correctBet;
    private boolean correctGuess;
    private volatile String name;
    private int hand;
    private int guess;


    public ServerWorker(Socket playerSocket, Server server) {

        this.playerSocket = playerSocket;
        this.server = server;
    }


    @Override
    public void run() {

        try {

       /*
       * Create input and output streams for this client.
       */
            out = new PrintWriter(playerSocket.getOutputStream(), true);

            in = new BufferedReader(new InputStreamReader(playerSocket.getInputStream()));


            out.println("Hi ... Welcome to little coins Game...");


            out.println("Please enter your name.");

            acceptName();


            send("Waiting for other users");
            synchronized (server) {
                System.out.println("Notify server");
                server.notifyAll();

                while (!server.gameStarts) {
                    try {
                        System.out.println("waiting for game start");
                        server.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }


            out.println("You have 3 Coins , choose how many coins you want to hand..");

            String line = "";
            while ((line = in.readLine()) != null) {
                if (line.contains("GUESS")) {
                    System.out.println("GUESS");
                    //server.guess
                } else {
                    System.out.println("BETING");
                    //server bet
                }
            }

            System.out.println("ENDED");


            if (in.readLine().contains("Guess")) {
                server.sendToAll(name, Integer.toString(guess));
            } else {

            }
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
                        System.out.println("entra aqui");
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


    /*
private  synchronized void acceptBet(){

while (!correctBet) {

 String tempBet = null;
 try {
     tempBet = in.readLine();
 } catch (IOException e) {
     e.printStackTrace();
 }

 int value = Integer.parseInt(tempBet);


 if (value >= 0 && value <= 3) {
     hand = value;
     out.println(hand);
     out.println("Bet accept!");//nao alterar este print é a condiçao de saida do while na playerThread
     System.out.println("Player " + name + " hand is: " + value);
     correctBet = true;

 } else {
     out.println("Make sure you hand between 0 or 3 coins!");
     out.println("Place your hand , between 0 - 3 : ");
 }

}

}

private synchronized void acceptGuess(){

while (!correctGuess) {

 String temGuess = null;
 try {
     temGuess = in.readLine();
 } catch (IOException e) {
     e.printStackTrace();
 }

 int value = Integer.parseInt(temGuess);


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


     server.sendToAll(Integer.toString(guess),name);

 }


}

}

*/
    public void send(String msg) {

        out.println(msg);

    }


    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getHand() {
        return hand;
    }

    public int getGuess() {
        return guess;
    }

    public boolean isAlreadyWin() {
        return alreadyWin;
    }
}
