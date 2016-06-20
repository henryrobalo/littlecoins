package org.academiadecodigo.concurrentchatserver.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientThread implements Runnable {


    private Socket clientSocket = null;
    private BufferedReader in = null;

    public ClientThread(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;

    }

    @Override
    public void run() {

        try {

            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String line;

            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }

            in.close();
            clientSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}