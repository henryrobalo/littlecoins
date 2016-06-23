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

    private boolean gameEnds;
    public boolean gameStarts;

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

            while (i < 2) {

                Socket playerSocket = serverSocket.accept();

                System.out.println("Connection from : " + playerSocket.getInetAddress());


                //server.createThread();

                ServerWorker sw = new ServerWorker(playerSocket, this);

                i++;

                serverWorkerList.add(sw);
                Thread thread = new Thread(sw);

                pool.submit(thread);
                //if number of players ? repeat, not stop on server accept
                //fecha o while aqui
            }
            System.out.println("game staring");

            synchronized (this) {
                while (!this.checkNames()) {
                    System.out.println("server: Waiting for players");
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("setting game start = true");
                this.gameStarts = true;
                this.notifyAll();
            }

            game.start();

            while (!gameEnds) {

                //bet :

                for (ServerWorker sw : serverWorkerList) {
                    sw.send("Qual é a bet?");
                    sw.send("TOKEN");
                    //sendPrivateMessage("TOKEN", sw);
                }
                synchronized (this) {
                    try {
                        System.out.println("waiting");
                        while (!game.hasBet()) {
                            //while (!this.hasBet()) {
                            this.wait();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("START GUESSING");

                //guess:

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


    /*public synchronized boolean hasBet(){
        for(ServerWorker c : serverWorkerList){
            if(c.bet==-1)return false;
        }
        return true;
    }*/


    public synchronized boolean setName(ServerWorker thread, String name) {
        System.out.println("Setting name " + name);
        //game.add(name,null);
        thread.setName(name);
        System.out.println("notify");
        this.notifyAll();
        return true;
    }

    public synchronized boolean checkNames() {
        for (ServerWorker c : serverWorkerList) {
            System.out.println("Checking names " + c.getName());
            if (c.getName() == null) return false;
        }
        return true;
    }


    public boolean containsName(String name) {
        for (ServerWorker c : serverWorkerList) {

            if (c.getName() != null && c.getName().equals(name)) {
                return true;
            }
        }

        return false;
    }


    public int countPlayers() {

        int count = 0;

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


    public List<ServerWorker> getServerWorkerList() {
        return serverWorkerList;
    }

    public int getCounterPlayers() {
        return serverWorkerList.size();
    }

    public void bet(String playerBet) {



        if(game.correctBet(playerBet)){

            game.setBet(playerBet);
            System.out.println("bet accepted");
        }else{

            System.out.println("quase que so faxzeis merdd");

        }



    }





}
