package org.academiadecodigo.littlecoins.player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class PlayerThread implements Runnable {


    private Socket playerSocket;
    private BufferedReader in;
    private boolean hasName;
    private boolean hasBet;
    private boolean hasGuess;

    public PlayerThread(Socket playerSocket) throws IOException {
        this.playerSocket = playerSocket;

    }

    @Override
    public void run() {

        try {

            in = new BufferedReader(new InputStreamReader(playerSocket.getInputStream()));

            String line;

            while ((line = in.readLine()) != null) {
                if(line.equals("Your name was accepted!")){
                    hasName = !hasName;
                }
                System.out.println(line);
            }

            //Quando ler "TOKEN" chama metodo de PLAYER.notify);
            while ((line = in.readLine()) != null) {
                if(line.equals("Bet accept!")){
                    hasBet = !hasBet;
                }
                System.out.println(line);
            }

            while ((line = in.readLine()) != null) {
                if(line.equals("Guess accept!")){
                    hasGuess = !hasGuess;
                }
                System.out.println(line);
            }







            in.close();
            playerSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean hasName() {
        return hasName;
    }

    public boolean hasGuess() {
        return hasGuess;
    }

    public boolean hasBet() {
        return hasBet;
    }
}