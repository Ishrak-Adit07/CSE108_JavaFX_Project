package com.example.javafx_project;

import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import javafx.collections.ObservableList;

public class MovieListOps {
    List<Movie> movieList = new ArrayList();
    static Scanner scn = new Scanner(System.in);
    static Scanner str = new Scanner(System.in);

    public List<Movie> searchMovieByTitle(String name){
        List<Movie> tempList = new ArrayList();
        for (Movie x : movieList) {
            if (x.getTitle().equalsIgnoreCase(name)) {
                tempList.add(x);
                break;
            }
        }
        return tempList;
    }

    public List<Movie> searchMovieByReleaseYear(int year){
        List<Movie> tempList = new ArrayList();
        for (Movie x : movieList) {
            if (x.getRelease_year() == year) {
                tempList.add(x);
            }
        }
        return tempList;
    }

    public List<Movie> searchMovieByGenre(String genre){
        List<Movie> tempList = new ArrayList();
        for (Movie x : movieList) {
            if (x.getGenre1().equalsIgnoreCase(genre) || x.getGenre2().equalsIgnoreCase(genre) || x.getGenre3().equalsIgnoreCase(genre)) {
                tempList.add(x);
            }
        }
        return tempList;
    }

    public List<Movie> searchMovieByPCompany(String pCompany){
        List<Movie> tempList = new ArrayList();
        for (Movie x : movieList) {
            if (x.getProducer().equalsIgnoreCase(pCompany)) {
                tempList.add(x);
            }
        }
        return tempList;
    }

    public List<Movie> searchRecentMoviesOfCompany(String company){
        List<Movie> tempList=new ArrayList();
        int latestYear = 0;
        for (Movie x : this.movieList) {
            if (x.getProducer().equalsIgnoreCase(company)) {
                if (x.getRelease_year() > latestYear) {
                    latestYear = x.getRelease_year();
                }
            }
        }
        for (Movie x : this.movieList) {
            if ((x.getProducer().equalsIgnoreCase(company)) && (x.getRelease_year() == latestYear)) {
                tempList.add(x);
            }
        }
        return tempList;
    }

    public List<Movie> searchMoviesWithMaxRevenueOfCompany(String company){
        List<Movie> tempList=new ArrayList();
        int maxRevenue = 0;
        for(Movie x : this.movieList) {
            if (x.getProducer().equalsIgnoreCase(company)) {
                if (x.getRevenue() > maxRevenue) {
                    maxRevenue = x.getRevenue();
                }
            }
        }
        for(Movie x : this.movieList) {
            if (x.getRevenue() == maxRevenue) {
                tempList.add(x);
            }
        }
        return tempList;
    }

    public TotalProfit findTotalProfitOfCompany(String company){
        long totalProfit = 0;
        for (Movie x : this.movieList) {
            if (x.getProducer().equalsIgnoreCase(company)) {
                totalProfit += x.getProfit();
                company = x.getProducer();
            }
        }
        TotalProfit temp = new TotalProfit(company, totalProfit);
        return temp;
    }

    public void replaceMovie(Movie movie, String companyName){
        for(Movie x : this.movieList){
            if(x.equalMovie(movie)){
                x.setProducer(companyName);
                break;
            }
        }
    }

}//class
