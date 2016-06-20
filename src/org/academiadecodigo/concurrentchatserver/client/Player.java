package org.academiadecodigo.concurrentchatserver.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


/**
 * Created by henry on 19/06/2016.
 */
public class Player {

    Socket clientSocket = null;

    PrintWriter out = null;
    BufferedReader inputLine = null;
    private int portNumber;
    private String hostName;
    private String bet;
    private int guess;


    public Player(int portNumber, String hostName) throws IOException {
        this.portNumber = portNumber;
        this.hostName = hostName;

    }

    public void startClient() {


        try {

            clientSocket = new Socket(hostName, portNumber);

            out = new PrintWriter(clientSocket.getOutputStream(), true);

            inputLine = new BufferedReader(new InputStreamReader(System.in));


            PlayerThread clientThread = new PlayerThread(clientSocket);
            Thread t = new Thread(clientThread);
            t.start();

            while (true) {

                System.out.println("Place your bet");


                bet = inputLine.readLine();

                out.println(bet);

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
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}

