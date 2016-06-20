package org.academiadecodigo.concurrentchatserver.server;

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
    private boolean closed = true;
    private String name;


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

            while (closed) {

                String mensagem = in.readLine();


                if (mensagem.equals("/quit")) {

                    server.removeFromList(this);
                    server.sendToAll(name + " is out", name);
                    break;

                } else if (mensagem.equals("/list")) {

                    out.println(server.listAll());


                } else if (mensagem.equals("/rename")) {

                    setName(in.readLine());


                } else if (mensagem.equals("/private")) {

                    String nome =in.readLine();
                    String text =in.readLine();
                    server.sendPrivateMessage(text,nome,name);

                } else {

                    server.sendToAll(mensagem, name);
                }


            }


            closed = false;
            playerSocket.close();

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
}
