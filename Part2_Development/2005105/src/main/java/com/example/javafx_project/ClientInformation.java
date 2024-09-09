package com.example.javafx_project;

public class ClientInformation {
    public String userName;
    public NetworkConnection networkConnection;

    public ClientInformation(String user, NetworkConnection nConnection){
        userName=user;
        networkConnection=nConnection;
    }

    public ClientInformation(){
        userName = new String();
    }
}