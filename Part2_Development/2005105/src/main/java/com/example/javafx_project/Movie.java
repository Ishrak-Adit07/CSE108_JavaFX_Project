package com.example.javafx_project;

import java.io.Serializable;

public class Movie implements Comparable<Movie>, Serializable {
    private String title;
    private int release_year;
    private String genre1, genre2, genre3;
    private int running_time;
    private String producer;
    private int budget;
    private int revenue;
    private int profit;

    Movie(String line){
        String [] data = line.split(",");
        this.title=data[0];
        this.release_year = Integer.parseInt(data[1]);
        this.genre1=data[2];
        this.genre2=data[3];
        this.genre3=data[4];
        this.running_time=Integer.parseInt(data[5]);
        this.producer=data[6];
        this.budget=Integer.parseInt(data[7]);
        this.revenue=Integer.parseInt(data[8]);
        this.profit=this.revenue-this.budget;
    }

    public String print(){
        return title+","+release_year+","+genre1+","+genre2+","+genre3+","+running_time+","+producer+","+budget+","+revenue;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getRelease_year() {
        return release_year;
    }

    public void setRelease_year(int release_year) {
        this.release_year = release_year;
    }

    public String getGenre1() {
        return genre1;
    }

    public void setGenre1(String genre1) {
        this.genre1 = genre1;
    }

    public String getGenre2() {
        return genre2;
    }

    public void setGenre2(String genre2) {
        this.genre2 = genre2;
    }

    public String getGenre3() {
        return genre3;
    }

    public void setGenre3(String genre3) {
        this.genre3 = genre3;
    }

    public int getRunning_time() {
        return running_time;
    }

    public void setRunning_time(int running_time) {
        this.running_time = running_time;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public int getRevenue() {
        return revenue;
    }

    public void setRevenue(int revenue) {
        this.revenue = revenue;
    }

    public int getProfit() {
        return profit;
    }

    public void setProfit(int profit) {
        this.profit = profit;
    }

    @Override
    public int compareTo(Movie o) {
        return 0;
    }

    public boolean equalMovie(Movie movie){
        boolean equal = false;
        if(this.title.equalsIgnoreCase(movie.getTitle()) && (this.release_year== movie.getRelease_year()) && (this.running_time==movie.getRunning_time())){
            equal = true;
        }
        return equal;
    }
}
