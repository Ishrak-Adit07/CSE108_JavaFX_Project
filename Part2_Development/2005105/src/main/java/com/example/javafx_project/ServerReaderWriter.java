package com.example.javafx_project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static java.lang.System.exit;

public class ServerReaderWriter implements Runnable {

    String userName;
    NetworkConnection networkConnection;
    HashMap<String, ClientInformation> clientList;
    MovieListOps allMovieDatabase;
    MovieListOps movieDatabase;

    public ServerReaderWriter(String user, NetworkConnection netConnection, HashMap<String, ClientInformation> cList) {
        userName = user;
        networkConnection = netConnection;
        clientList = cList;

        movieDatabase=new MovieListOps();
        allMovieDatabase = new MovieListOps();
        final String INPUT_FILE_NAME = "movies.txt";

        try{
            BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE_NAME));
            if(br==null){
                System.out.println("Text File for Movies is Not Found");
                exit(1);
            }
            while (true) {
                String line = br.readLine();
                if (line == null) break;
                allMovieDatabase.movieList.add(new Movie(line));
            }
            br.close();

            movieDatabase.movieList = allMovieDatabase.searchMovieByPCompany(userName);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        final int ALL_MOVIE_CALL = 1;
        final int COMPANY_MOVIE_CALL = 2;
        final int TRANSFER_MOVIE_CALL = 3;
        final int ADD_MOVIE_CALL = 4;
        final int UPDATE_SERVER_CALL = 5;

        while (true) {

            Object obj = new Object();
            try{
                obj = networkConnection.read();
            }catch (Exception e){
                //e.printStackTrace();
            }
            ConferInfoWrapper dataObj=(ConferInfoWrapper)obj;
            dataObj.sender = userName;

            ClientInformation info = new ClientInformation();
            String m = new String();
            List<Movie> tempList = new ArrayList<>();
            Object object = new Object();

            if(dataObj.operation==ALL_MOVIE_CALL){
                tempList=allMovieDatabase.movieList;
                info = clientList.get(dataObj.sender);
                System.out.println("Server sent All Movie List to " + userName);
                object = tempList;

                try{
                    info.networkConnection.write(object);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            if(dataObj.operation==COMPANY_MOVIE_CALL){
                tempList = movieDatabase.movieList;
                info = clientList.get(dataObj.sender);
                System.out.println("Server sent Company Movie List to " + userName);
                object = tempList;

                try{
                    info.networkConnection.write(object);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            if(dataObj.operation==TRANSFER_MOVIE_CALL){
                m = dataObj.toTransfer;
                tempList = allMovieDatabase.searchMovieByTitle(m);

                for(Movie x : tempList){
                    if (!(x.getProducer().equalsIgnoreCase(userName))) {
                        tempList.remove(x);
                    }
                }

                if(tempList.size()!=0){
                    for(Movie x : tempList){
                        allMovieDatabase.replaceMovie(x, dataObj.receiver);
                        movieDatabase.movieList.remove(x);
                        x.setProducer(dataObj.receiver);
                    }
                    String receiver = dataObj.receiver.toUpperCase();
                    info = clientList.get(receiver);
                    System.out.println(dataObj.sender + " transferred " + dataObj.toTransfer + " to " + dataObj.receiver);
                    object = tempList;

                    try{
                        info.networkConnection.write(object);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

            if(dataObj.operation==ADD_MOVIE_CALL){
                String movieLine = dataObj.toTransfer;
                Movie movie = new Movie(movieLine);
                object = movie;

                allMovieDatabase.movieList.add(movie);
                movieDatabase.movieList.add(movie);

                for(ClientInformation clientInformation : clientList.values()){
                    info = clientInformation;
                    try{
                        info.networkConnection.write(object);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }

            }

            if(dataObj.operation == UPDATE_SERVER_CALL){
                String movieTitle = dataObj.toTransfer;
                List<Movie> updateList = new ArrayList<>();
                updateList = allMovieDatabase.searchMovieByTitle(movieTitle);

                for(Movie x : updateList){
                    allMovieDatabase.replaceMovie(x, dataObj.sender);
                    movieDatabase.movieList.add(x);
                }

            }

        }//while-true loop
    }//run
}//Class