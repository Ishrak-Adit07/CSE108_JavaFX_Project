package com.example.javafx_project;

import java.io.Serializable;

public class ConferInfoWrapper implements Serializable, Cloneable {
    public String sender;
    public String receiver;
    public String toTransfer;
    public int operation;

    ConferInfoWrapper(String sender, String receiver, String toTransfer, int operation){
        this.sender = sender;
        this.receiver = receiver;
        this.toTransfer = toTransfer;
        this.operation = operation;
    }

    ConferInfoWrapper(){
        sender = new String();
        receiver = new String();
        toTransfer = new String();
    }

    public Object clone()throws CloneNotSupportedException{
        return super.clone();
    }
}
