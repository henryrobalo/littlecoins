package org.academiadecodigo.littlecoins.server;


import org.academiadecodigo.littlecoins.Game;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import java.util.Collections;
import java.util.LinkedList;
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
    private int counterPlayers=3;

    private Game game;




    //private List<ServerWorker> threadsList = new ArrayList<>(maxPlayer);
    private List<ServerWorker> synThreadlist = Collections.synchronizedList(new LinkedList<>());

    public Server(int portNumber, int maxPlayer) {
        this.portNumber = portNumber;
        this.maxPlayer = maxPlayer;
        game = new Game(this);
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

                System.out.println("Connection from : " + playerSocket.getInetAddress());

                ServerWorker sw = new ServerWorker(playerSocket, this);

                sw.setName("Player " + i);
                i++;

                synThreadlist.add(sw);
                System.out.println(synThreadlist.size());

                Thread thread = new Thread(sw);

                pool.submit(thread);

                if(i == 2){
                    game.start();
                }

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


    public void sendToAll(String message, String name) {

        /* Broadcast the message to all other clients. */
        synchronized (synThreadlist) {

            for (ServerWorker c : synThreadlist) {

                c.send(message, name);
            }
        }
    }


    public void sendPrivateMessage(String message, String receiverName, String senderName) {

        synchronized (synThreadlist) {

            for (ServerWorker c : synThreadlist) {

                if (c.getName().equals(receiverName)) {

                    c.send(message, senderName);

                }
            }
        }
    }


    public void removeFromList(ServerWorker sw) {

        synThreadlist.remove(sw);
    }

    public String listAll() {

        String list = "";

        for (ServerWorker c : synThreadlist) {
            list = list + c.getName() + "\n";
        }
        return list;
    }


    public boolean containsName(String name){

        for (ServerWorker c : synThreadlist) {

            if (c.getName().equals(name)) {

                return true;

            }

        }
        return false;
    }


    public List<ServerWorker> getSynThreadlist() {
        return synThreadlist;
    }

    public int getCounterPlayers() {
        System.out.println();
        return synThreadlist.size();
    }
}
