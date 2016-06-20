package org.academiadecodigo.concurrentchatserver.client;

import org.academiadecodigo.concurrentchatserver.client.Client;

import java.io.IOException;

/**
 * Created by codecadet on 17/06/16.
 */
public class MainClient {

    public static void main(String[] args) {

        Client client = null;

        try {
            client = new Client(Integer.parseInt(args[0]), args[1]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        client.startClient();
    }
}

