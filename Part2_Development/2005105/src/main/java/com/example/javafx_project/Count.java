package com.example.javafx_project;

import java.io.Serializable;

public class Count implements Comparable<Count>, Serializable {
    String name;
    int count;

    public Count(){
        name=new String();
        count=0;
    }
    public Count(String line){
        name=line;
        count=1;
    }

    public Count(String line, int call){
        name = line;
        count = call;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public int compareTo(Count o) {
        return 0;
    }
}
