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




    public Player(int portNumber, String hostName) {
        this.portNumber = portNumber;
        this.hostName = hostName;

    }

    public void startPlayer() {


        try {

            playerSocket = new Socket(hostName, portNumber);

           // System.out.println("Hi ... Welcome to little coins Game...");
            //System.out.println("You have 3 Coins , choose how many coins you want to bet..");

            out = new PrintWriter(playerSocket.getOutputStream(), true);

            inputLine = new BufferedReader(new InputStreamReader(System.in));


            PlayerThread clientThread = new PlayerThread(playerSocket,this);
            Thread t = new Thread(clientThread);
            t.start();

            //
            // in.readline();
            //
            //while(true){
            //while(afaf)
                //wait;
                //in.readline();
            //
            //}
            while (!clientThread.hasName()) {
            System.out.println();
                String name = inputLine.readLine();
                out.println(name);
            }
            synchronized (this){
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("TECLADO DESBLOKEOU");
            String bet = "BET ";
            bet += inputLine.readLine();
            System.out.println("A minha bet é: "+bet);
















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

