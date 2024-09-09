package com.example.javafx_project;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class ServerMain {

    public static void main(String[] args) throws IOException {
        final int PORT = 57775;
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Server Started...");
        HashMap<String, ClientInformation> clientList = new HashMap<>();

        while (true) {
            Socket socket = serverSocket.accept();
            NetworkConnection networkConnection = new NetworkConnection(socket);

            new Thread(new CreateConnection(clientList, networkConnection)).start();
        }
    }//Main
}//Class