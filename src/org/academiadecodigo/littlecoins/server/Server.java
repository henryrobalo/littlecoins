package org.academiadecodigo.littlecoins.server;


import org.academiadecodigo.littlecoins.Game;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

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
    private int counterPlayers = 3;

    private  boolean gameEnds;
    private Game game;


    //private List<ServerWorker> threadsList = new ArrayList<>(maxPlayer);
    private List<ServerWorker> serverWorkerList = Collections.synchronizedList(new LinkedList<>());

    public Server(int portNumber, int maxPlayer) {
        this.portNumber = portNumber;
        this.maxPlayer = maxPlayer;
        game = new Game();
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
            int i = 0;
            game = new Game();

            while (i >= 2) {

                Socket playerSocket = serverSocket.accept();

                System.out.println("Connection from : " + playerSocket.getInetAddress());


                //server.createThread();

                ServerWorker sw = new ServerWorker(playerSocket, this);

                sw.setName("Player " + i);
                i++;

                serverWorkerList.add(sw);
                Thread thread = new Thread(sw);

                pool.submit(thread);
                //if number of players ? repeat, not stop on server accept
                //fecha o while aqui
            }

                game.start();

            while (!gameEnds){

                for (ServerWorker sw: serverWorkerList) {
                    sendPrivateMessage("TOKEN", sw);


                }


                //while(!game.ends)
                //  for each in array:
                //      send: "qual é a tua bet?"
                //      send: "TOKEN"
                // wait // notifica no metodo bet
                //what is the guess
                //  for each in array:
                //      sendall luis a jogar
                //      send: "qual é o teu guess?"
                //      send: "TOKEN"
                //      wait
                // game.hasWhin?  : game.removePlayer.
                //was the last player? yes: break;

                //send all last player was tal. he pays the beer.

            } catch (IOException e1) {
            e1.printStackTrace();
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


    public boolean containsName(String name) {

        for (ServerWorker c : serverWorkerList) {

            if (c.getName().equals(name)) {

                return true;

            }

        }
        return false;
    }


    public boolean containsGuess(int guess){

        for (ServerWorker c : serverWorkerList) {

            if (c.getGuess() == guess) {

                return true;

            }
        }
        return false;
    }


    public int countPlayers() {

        int count=0;

        synchronized (serverWorkerList) {

            for (ServerWorker c : serverWorkerList) {

                if (!c.isAlreadyWin()) {
                    count++;
                }
            }
        }

        return count;
    }


    public void sendToAll(String message, String name) {

        /* Broadcast the message to all other clients. */
        synchronized (serverWorkerList) {

            for (ServerWorker c : serverWorkerList) {

                c.send(message);
            }
        }
    }


    public void sendPrivateMessage(String message, ServerWorker sw) {

        synchronized (serverWorkerList) {

            for (ServerWorker c : serverWorkerList) {

                if (c.equals(sw)) {

                    c.send(message);

                }
            }
        }
    }


    public void removeFromList(ServerWorker sw) {

        serverWorkerList.remove(sw);
    }

    public String listAll() {

        String list = "";

        for (ServerWorker c : serverWorkerList) {
            list = list + c.getName() + "\n";
        }
        return list;
    }




    public List<ServerWorker> getServerWorkerList() {
        return serverWorkerList;
    }

    public int getCounterPlayers() {
        return serverWorkerList.size();
    }
}
