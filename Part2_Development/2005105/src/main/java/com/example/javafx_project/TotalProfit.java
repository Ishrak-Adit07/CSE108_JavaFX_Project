package com.example.javafx_project;

public class TotalProfit {
    String title;
    long totalProfit;

    TotalProfit(String name, long n){
        this.title = name;
        this.totalProfit = n;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(long totalProfit) {
        this.totalProfit = totalProfit;
    }
}
