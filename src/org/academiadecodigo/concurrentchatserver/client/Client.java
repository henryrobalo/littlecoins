package org.academiadecodigo.concurrentchatserver.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


/**
 * Created by henry on 19/06/2016.
 */
public class Client {

    Socket clientSocket = null;
    String message = "";
    PrintWriter out = null;
    BufferedReader inputLine = null;
    private int portNumber;
    private String hostName;


    public Client(int portNumber, String hostName) throws IOException {
        this.portNumber = portNumber;
        this.hostName = hostName;

    }

    public void startClient() {


        try {

            clientSocket = new Socket(hostName, portNumber);

            out = new PrintWriter(clientSocket.getOutputStream(), true);

            inputLine = new BufferedReader(new InputStreamReader(System.in));


            ClientThread clientThread = new ClientThread(clientSocket);
            Thread t = new Thread(clientThread);
            t.start();

            while (true) {

                message = inputLine.readLine();

                out.println(message);

                if (message.equals("/quit")) {
                    out.println(message);
                    clientSocket.close();
                    break;
                }

            }



               // t.join();



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

        }
    }
}

