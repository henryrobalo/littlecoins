package org.academiadecodigo.littlecoins.player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


/**
 * Created by henry on 19/06/2016.
 */
public class Player {

    Socket playerSocket = null;

    PrintWriter out = null;
    BufferedReader inputLine = null;
    private int portNumber;
    private String hostName;
    private String bet;
    private int guess;
    private int value;


    public Player(int portNumber, String hostName) {
        this.portNumber = portNumber;
        this.hostName = hostName;

    }

    public void startPlayer() {


        try {

            playerSocket = new Socket(hostName, portNumber);

            System.out.println("Hi ... Welcome to little coins Game...");
            System.out.println("You have 3 Coins , choose how many coins you want to bet..");

            out = new PrintWriter(playerSocket.getOutputStream(), true);

            inputLine = new BufferedReader(new InputStreamReader(System.in));


            PlayerThread clientThread = new PlayerThread(playerSocket);
            Thread t = new Thread(clientThread);
            t.start();

            System.out.println("Place your bet , between 0 - 3 : ");

            while (true) {

                bet = inputLine.readLine();
                value = Integer.parseInt(bet);


                if(value >= 0 && value <= 3){
                    out.println(bet);
                } else {
                    System.out.println("Make sure you bet between 0 or 3 coins!");
                    System.out.println("Place your bet , between 0 - 3 : ");
                }



            }






        } catch (IOException e) {
            e.printStackTrace();

        } finally {

            if (out != null) {
                out.close();
            }
            if (inputLine != null) {
                try {
                    inputLine.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                playerSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}

