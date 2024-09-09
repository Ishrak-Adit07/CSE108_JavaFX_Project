package com.example.javafx_project;

import java.util.HashMap;

public class CreateConnection implements Runnable{

    HashMap<String, ClientInformation> clientList;
    NetworkConnection networkConnection;
    public CreateConnection(HashMap<String, ClientInformation> cList, NetworkConnection nConnection){
        clientList=cList;
        networkConnection=nConnection;
    }

    @Override
    public void run() {
        Object user=networkConnection.read();
        String userName=(String)user;
        userName = userName.toUpperCase();

        System.out.println("User : "+userName+" connected");
        clientList.put(userName,new ClientInformation(userName, networkConnection));
        System.out.println("HashMap updated"+clientList);

        new Thread(new ServerReaderWriter(userName,networkConnection,clientList)).start();
    }
}//Class
