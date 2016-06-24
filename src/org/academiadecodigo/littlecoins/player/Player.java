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

    /**
     * Creates a new socket, the output and keyboard input streams and the player threads
     */
    public void startPlayer() {

        try {

            playerSocket = new Socket(hostName, portNumber);

            out = new PrintWriter(playerSocket.getOutputStream(), true);

            inputLine = new BufferedReader(new InputStreamReader(System.in));

            PlayerThread playerThread = new PlayerThread(playerSocket,this);
            Thread t = new Thread(playerThread);
            t.start();


            while (!playerThread.hasName()) {

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

            while (!playerThread.hasBet()){
                String bet = inputLine.readLine();
                out.println(bet);
            }
            synchronized (this){
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            String bet = "BET ";
            bet += inputLine.readLine();
            out.println(bet);
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

