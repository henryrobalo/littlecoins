package org.academiadecodigo.littlecoins.player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class PlayerThread implements Runnable {


    private Socket playerSocket = null;
    private BufferedReader in = null;

    public PlayerThread(Socket playerSocket) throws IOException {
        this.playerSocket = playerSocket;

    }

    @Override
    public void run() {

        try {

            in = new BufferedReader(new InputStreamReader(playerSocket.getInputStream()));

            String line;

            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }

            in.close();
            playerSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}