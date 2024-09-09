import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class MovieDatabase {
    static List<Movie> movieList = new ArrayList();
    static Scanner scn= new Scanner(System.in);
    static Scanner str= new Scanner(System.in);
    private static final String INPUT_FILE_NAME = "movies.txt";
    private static final String OUTPUT_FILE_NAME = "movies.txt";

    public static void mainMenu1(){
        int searchChoice=0;

        while(searchChoice!=7) {
            System.out.println("1.By Movie Title\n2.By Release Year\n3.By Genre");
            System.out.println("4.By Production Company\n5.By Running Time");
            System.out.println("6.Top Ten Movies\n7.Back to the main menu");
            searchChoice= scn.nextInt();
            switch(searchChoice){
                case 1:
                    System.out.println("Enter Movie Title: ");
                    String name=str.nextLine();
                    int flag1=0;
                    for(Movie x : movieList){
                        if(x.getTitle().equalsIgnoreCase(name)){
                            x.print();
                            flag1=1;
                            break;
                        }
                    }
                    if(flag1==0) System.out.println("No such movie with this name");
                    break;
                case 2:
                    System.out.println("Enter Movie Year: ");
                    int year=scn.nextInt();
                    int flag2=0;
                    for(Movie x : movieList){
                        if(x.getRelease_year()==year){
                            x.print();
                            flag2=1;
                        }
                    }
                    if(flag2==0) System.out.println("No such movie with this release year");
                    break;
                case 3:
                    System.out.println("Enter genre: ");
                    String genre= str.nextLine();
                    int flag3=0;
                    for(Movie x : movieList){
                        if(x.getGenre1().equalsIgnoreCase(genre) || x.getGenre2().equalsIgnoreCase(genre) || x.getGenre3().equalsIgnoreCase(genre)){
                            x.print();
                            flag3=1;
                        }
                    }
                    if(flag3==0) System.out.println("No such movie with this genre");
                    break;
                case 4:
                    System.out.println("Enter production company: ");
                    String pCompany= str.nextLine();
                    int flag4=0;
                    for(Movie x : movieList){
                        if(x.getProducer().equalsIgnoreCase(pCompany)){
                            x.print();
                            flag4=1;
                        }
                    }
                    if(flag4==0) System.out.println("No such movie with this production company");
                    break;
                case 5:
                    int start, end, flag5=0;
                    System.out.println("Enter minimum and maximum running time range: ");
                    start=scn.nextInt();
                    end=scn.nextInt();
                    for(Movie x : movieList){
                        if(x.getRunning_time()>=start && x.getRunning_time()<=end){
                            x.print();
                            flag5=1;
                        }
                    }
                    if(flag5==0) System.out.println("No such movie with this running time range");
                    break;
                case 6:
                    int profits[]=new int[movieList.size()];
                    int i=0;
                    for(Movie x: movieList){
                        profits[i++]=x.getProfit();
                    }
                    Arrays.sort(profits);
                    int j=1;
                    while(j<11){
                        int temp=profits[movieList.size()-j];
                        for(Movie x: movieList){
                            if(x.getProfit()==temp){
                                x.print();
                            }
                        }
                        j++;
                    }
                    break;
                case 7:
                    return;
                default:
                    System.out.println("Invalid Option!");
                    break;
            }
        }
    }

    public static void mainMenu2(){
        int searchChoice=0;

        while(searchChoice!=5){
            System.out.println("1.Most Recent Movies\n2.Movies with the Maximum Revenue\n3.Total Profit");
            System.out.println("4.List of Production Companies and the Count of their Produced Movies");
            System.out.println("5.Back to the main menu");
            searchChoice= scn.nextInt();
            switch(searchChoice){
                case 1:
                    System.out.println("Enter a Production Company: ");
                    String company=str.nextLine();
                    int latestYear=0, flag1=0;
                    for(Movie x: movieList){
                        if(x.getProducer().equalsIgnoreCase(company)){
                            flag1=1;
                            if(x.getRelease_year()>latestYear){
                                latestYear=x.getRelease_year();
                            }
                        }
                    }
                    for(Movie x: movieList){
                        if(x.getRelease_year()==latestYear){
                            x.print();
                        }
                    }
                    if(flag1==0) System.out.println("No such production company with this name");
                    break;
                case 2:
                    System.out.println("Enter a Production Company: ");
                    company=str.nextLine();
                    int maxRevenue=0, flag2=0;
                    for(Movie x: movieList){
                        if(x.getProducer().equalsIgnoreCase(company)){
                            flag2=1;
                            if(x.getRevenue()>maxRevenue){
                                maxRevenue=x.getRevenue();
                            }
                        }
                    }
                    for(Movie x: movieList){
                        if(x.getRevenue()==maxRevenue){
                            x.print();
                        }
                    }
                    if(flag2==0) System.out.println("No such production company with this name");
                    break;
                case 3:
                    int totalProfit=0;
                    System.out.println("Enter a Production Company: ");
                    company=str.nextLine();
                    int flag3=0;
                    for(Movie x: movieList){
                        if(x.getProducer().equalsIgnoreCase(company)){
                            totalProfit+=x.getProfit();
                        }
                    }
                    System.out.println("Total Profit: "+totalProfit);
                    if(flag3==0) System.out.println("No such production company with this name");
                    break;
                case 4:
                    List<Count> nameList=new ArrayList();
                    int flag=0;
                    for(Movie x: movieList){
                        for(Count y: nameList){
                            if(x.getProducer().equalsIgnoreCase(y.name)){
                                y.count+=1;
                                flag=1;
                            }
                        }
                        if(flag == 0){
                            nameList.add(new Count(x.getProducer()));
                            flag=0;
                        }
                        flag=0;
                    }
                    for(Count x : nameList){
                        x.print();
                    }
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid Option!");
                    break;
            }
        }
    }

    public static void mainMenu3() throws Exception{

        System.out.println("Entering a new Movie-");
        System.out.println("Enter Movie name: ");
        String name=str.nextLine();
        for(Movie x: movieList){
            if(x.getTitle().equalsIgnoreCase(name)){
                System.out.println("A movie with this name already exists.");
                return;
            }
        }
        System.out.println("Enter Release Year: ");
        int releaseYear=scn.nextInt();
        System.out.println("How many genres do you want to enter(1-3): ");
        int genreCount= scn.nextInt();
        String genre1=" ";
        String genre2=" ";
        String genre3=" ";
        if(genreCount==1 || genreCount==2 || genreCount==3){
            System.out.println("Enter genre1: ");
            genre1=str.nextLine();
        }
        if(genreCount==2 || genreCount==3){
            System.out.println("Enter genre2: ");
            genre2=str.nextLine();
        }
        if(genreCount==3){
            System.out.println("Enter genre3: ");
            genre3=str.nextLine();
        }
        System.out.println("Enter Running time: ");
        int running_time=scn.nextInt();
        System.out.println("Enter Production Company: ");
        String company=str.nextLine();
        System.out.println("Enter Budget of the movie: ");
        int budget=scn.nextInt();
        System.out.println("Enter revenue of the movie: ");
        int revenue=scn.nextInt();
        String line=name+","+String.valueOf(releaseYear)+","+genre1+","+genre2+","+genre3+","+String.valueOf(running_time)+","+company+","+String.valueOf(budget)+","+String.valueOf(revenue);
        movieList.add(new Movie(line));

        BufferedWriter bw = new BufferedWriter(new FileWriter(OUTPUT_FILE_NAME, true));
        bw.write(line);
        bw.write(System.lineSeparator());
        bw.close();
        return;
    }


    public static void main(String [] args)throws Exception {

        BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE_NAME));
        while (true) {
            String line = br.readLine();
            if (line == null) break;
            movieList.add(new Movie(line));
        }
        br.close();

        int choice=0;
        Scanner scn= new Scanner(System.in);

        while(choice!=4){
            System.out.println("1.Search Movies\n2.Search Production Companies");
            System.out.println("3.Add Movie\n4.Exit System");
            choice= scn.nextInt();
            switch(choice){
                case 1:
                    mainMenu1();
                    break;
                case 2:
                    mainMenu2();
                    break;
                case 3:
                    mainMenu3();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid Option");
                    break;
            }//switch
        }//while
    }//main
}//class
