package org.academiadecodigo.concurrentchatserver.server;


import com.sun.tools.doclets.formats.html.SourceToHTMLConverter;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by codecadet on 17/06/16.
 */
public class Server {

    private int portNumber;
    private int maxPlayer;
    private ServerSocket serverSocket = null;


    //private List<ServerWorker> threadsList = new ArrayList<>(maxPlayer);
    private List<ServerWorker> synThreadlist = Collections.synchronizedList(new ArrayList<>(maxPlayer));

    public Server(int portNumber, int maxPlayer) {
        this.portNumber = portNumber;
        this.maxPlayer = maxPlayer;
    }


    public void startServer() {

        ExecutorService pool = Executors.newFixedThreadPool(10);


        try {
            System.out.println("Welcome to littleCoins Server...");
            serverSocket = new ServerSocket(portNumber);
            System.out.println("Server Started...");
            System.out.println("Waiting for Players...");


     /*
     * Create a client socket for each connection and pass it to a new client
     * thread.
     */
            int i = 1;

            while (true) {

                Socket playerSocket = serverSocket.accept();

                System.out.println("Connection from : "+playerSocket.getInetAddress());

                ServerWorker sw = new ServerWorker(playerSocket, this);

                sw.setName("Player " + i);
                i++;

                synThreadlist.add(sw);

                Thread thread = new Thread(sw);

                pool.submit(thread);

            }





        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            pool.shutdown();


            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    public synchronized void sendToAll(String message, String name) {

        /* Broadcast the message to all other clients. */

        for (ServerWorker c : synThreadlist) {

                c.send(message, name);
        }
    }


    public synchronized void sendPrivateMessage(String message,String receiverName,String senderName){


        for (ServerWorker c : synThreadlist) {

           if(c.getName().equals(receiverName)){

               c.send(message,senderName);

           }
        }


    }



    public void removeFromList(ServerWorker sw) {

        synThreadlist.remove(sw);
    }

    public String listAll() {

        String list="";

        for (ServerWorker c : synThreadlist) {
            list =list + c.getName()+"\n";
        }
        return list;

    }

}
