package org.academiadecodigo.littlecoins.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Calendar;


/**
 * Created by codecadet on 17/06/16.
 */
public class ServerWorker implements Runnable {

    private Server server;
    private PrintWriter out = null;
    private BufferedReader in = null;
    private Socket playerSocket = null;
    private boolean alreadyWin;
    private boolean correctName;
    private String name;
    private String bet;
    private String guess;
    private String tName;


    public ServerWorker(Socket playerSocket, Server server) {

        this.playerSocket = playerSocket;
        this.server = server;


    }

    @Override
    public void run() {

        try {

       /*
       * Create input and output streams for this client.
       */
            out = new PrintWriter(playerSocket.getOutputStream(), true);

            in = new BufferedReader(new InputStreamReader(playerSocket.getInputStream()));


            out.println("Hi ... Welcome to little coins Game...");


            out.println("Please enter your name.");


            while (!correctName) {
                tName = in.readLine();

                if (server.containsName(tName) || tName.equals("")) {

                    out.println("This name is already in the list. Please change name");
                    // tName = in.readLine();


                } else {
                    setName(tName);
                    //out.println("Name changed to " + tName);
                    correctName = true;
                    out.println("Your name has changed!");
                }


            }

            out.println("You have 3 Coins , choose how many coins you want to bet..");


            while (!alreadyWin) {

                bet = in.readLine();

                System.out.println(bet);
                int value = Integer.parseInt(bet);


                if (value >= 0 && value <= 3) {
                    out.println(bet);
                } else {
                    out.println("Make sure you bet between 0 or 3 coins!");
                    out.println("Place your bet , between 0 - 3 : ");
                }




























                /*if (bet.equals("/quit")) {

                    server.removeFromList(this);
                    server.sendToAll(name + " is out", name);
                    break;

                } else if (bet.equals("/list")) {

                    out.println(server.listAll());


                } else if (bet.equals("/rename")) {

                    setName(in.readLine());


                } else if (bet.equals("/private")) {

                    String nome =in.readLine();
                    String text =in.readLine();
                    server.sendPrivateMessage(text,nome,name);

                } else {

                    server.sendToAll(bet, name);
                }
*/

            }


            alreadyWin = false;
            // playerSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(String msg, String name) {

        out.println("(" + new java.sql.Timestamp(Calendar.getInstance().getTime().getTime()) + " ) " + name + " bet : " + msg);

    }


    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getBet() {
        return bet;
    }

    public String getGuess() {
        return guess;
    }
}
