package com.example.javafx_project;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class CompanySoftwareMain extends Application {
    public static void main(String[] args) throws Exception {
        launch(args);
    }

    Stage window;

    Socket socket;
    NetworkConnection networkConnection;

    MovieListOps allMovieDatabase = new MovieListOps();
    MovieListOps companyMoviesDatabase = new MovieListOps();

    String companyName = new String();
    List<Movie> transferMovieList = new ArrayList<>();
    TableView<Movie> displayTable;

    @Override
    public void start(Stage primaryStage) throws IOException {
        window = primaryStage;
        window.setTitle("Movie Database");

        window.setOnCloseRequest(e->{
            e.consume();
            try {
                socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            closeProgram();
        });

        //Calling TextField Early for Getting List of Movies
        TextField companyInput = new TextField();
        companyInput.setPromptText("Enter Production Company Name");

        final int ALL_MOVIE_CALL = 1;
        final int COMPANY_MOVIE_CALL = 2;
        final int TRANSFER_MOVIE_CALL = 3;
        final int ADD_MOVIE_CALL = 4;
        final int UPDATE_SERVER_CALL = 5;
        final int SERVER_PORT = 57775;

        //Boolean Variables for Some Conditional Operations
        AtomicBoolean displayTableScope = new AtomicBoolean(false);
        AtomicBoolean addToTableScope = new AtomicBoolean(false);
        AtomicBoolean threadCheck = new AtomicBoolean(false);

        //Server Connection
        try {
            socket = new Socket();
            networkConnection=new NetworkConnection(socket.getLocalAddress().getHostAddress(),SERVER_PORT);

        } catch (IOException e) {
            e.printStackTrace();
        }

        //Initial Table Actions*************************
        //Table Columns
        TableColumn<Movie, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setMinWidth(180);
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Movie, Integer> releaseYearColumn = new TableColumn<>("Release Year");
        releaseYearColumn.setMinWidth(100);
        releaseYearColumn.setCellValueFactory(new PropertyValueFactory<>("release_year"));

        TableColumn<Movie, String> genre1Column = new TableColumn<>("Genre 1");
        genre1Column.setMinWidth(100);
        genre1Column.setCellValueFactory(new PropertyValueFactory<>("genre1"));

        TableColumn<Movie, String> genre2Column = new TableColumn<>("Genre 2");
        genre2Column.setMinWidth(100);
        genre2Column.setCellValueFactory(new PropertyValueFactory<>("genre2"));

        TableColumn<Movie, String> genre3Column = new TableColumn<>("Genre 3");
        genre3Column.setMinWidth(100);
        genre3Column.setCellValueFactory(new PropertyValueFactory<>("genre3"));

        TableColumn<Movie, Integer> runTimeColumn = new TableColumn<>("Running Time");
        runTimeColumn.setMinWidth(100);
        runTimeColumn.setCellValueFactory(new PropertyValueFactory<>("running_time"));

        TableColumn<Movie, String> companyColumn = new TableColumn<>("Production Company");
        companyColumn.setMinWidth(150);
        companyColumn.setCellValueFactory(new PropertyValueFactory<>("Producer"));

        TableColumn<Movie, Integer> budgetColumn = new TableColumn<>("Budget");
        budgetColumn.setMinWidth(100);
        budgetColumn.setCellValueFactory(new PropertyValueFactory<>("Budget"));

        TableColumn<Movie, Integer> revenueColumn = new TableColumn<>("Revenue");
        revenueColumn.setMinWidth(100);
        revenueColumn.setCellValueFactory(new PropertyValueFactory<>("Revenue"));

        //Table Functions
        displayTable = new TableView<>();
        displayTable.getColumns().addAll(titleColumn, releaseYearColumn, genre1Column, genre2Column, genre3Column, runTimeColumn, companyColumn, budgetColumn, revenueColumn);
        displayTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        //Scene Components
        //Log in Page Components
        Label logInText = new Label("Enter Production Company Name: ");
        //logInText.setFont(Font.font("Times New Roman", 25));
        Button logInButton = new Button("Log in");

        //Welcome Page Components
        Label welcomeText = new Label("Welcome to Movie Database!");
        Button welcomeButton = new Button("Main Menu");

        //Main menu Components
        MenuBar menuBar = new MenuBar();

        //Menus
        Menu generalMenu = new Menu("_General");
        Menu companyMenu = new Menu("_Company");
        Menu addMenu = new Menu("_Add");
        Menu transferMenu = new Menu("_Transfer");

        //General menu items
        MenuItem allGeneral = new MenuItem("All Movies...");
        MenuItem searchByTitleGeneral = new MenuItem("Movie By Title...");
        MenuItem searchByReleaseYearGeneral = new MenuItem("Movies By Release Year");
        MenuItem searchByGenreGeneral = new MenuItem("Movies By Genre...");
        //searchByTitleGeneral.setDisable(true);//Disables the item
        MenuItem searchByCompanyGeneral = new MenuItem("Movies of Other Companies...");

        //Own Company menu items
        MenuItem allByCompany = new MenuItem("All Movies...");
        MenuItem searchByTitleByCompany = new MenuItem("Movie By Title...");
        MenuItem searchByReleaseYearByCompany = new MenuItem("Movies By Release Year...");
        MenuItem searchByGenreByCompany = new MenuItem("Movies By Genre...");
        MenuItem searchMostRecentByCompany = new MenuItem("Movies Most Recent");
        MenuItem searchMaxRevenueByCompany = new MenuItem("Movies of Max Revenue");
        MenuItem searchTotalProfitByCompany = new MenuItem("Total Profit");

        //Add Menu Items
        MenuItem addMovieItem = new MenuItem("Add Movie...");

        //Transfer Menu Items
        MenuItem transferMovieItem = new MenuItem("Transfer Movie...");

        //Adding menu items
        generalMenu.getItems().addAll(allGeneral, searchByTitleGeneral, searchByReleaseYearGeneral, searchByGenreGeneral, searchByCompanyGeneral);
        companyMenu.getItems().addAll(allByCompany, new SeparatorMenuItem(), searchByTitleByCompany, searchByReleaseYearByCompany, searchByGenreByCompany, new SeparatorMenuItem(), searchMostRecentByCompany, searchMaxRevenueByCompany, searchTotalProfitByCompany);
        addMenu.getItems().addAll(addMovieItem);
        transferMenu.getItems().addAll(transferMovieItem);

        //Adding to menu bar
        menuBar.getMenus().addAll(generalMenu, companyMenu, addMenu, transferMenu);
        menuBar.setPadding(new Insets(20, 0, 0, 0));

        Button exitButton = new Button("Log Out");

        //General Menu Item Execution
        //All Movies in General
        Label allMovieGeneralLabel = new Label("All Movies By All Companies");

        Button backButton11 = new Button("Back");
        HBox backBox11 = new HBox(20);
        backBox11.setPadding(new Insets(20, 20, 20, 20));
        backBox11.setAlignment(Pos.BASELINE_RIGHT);
        backBox11.getChildren().addAll(backButton11);

        //Search By Title in General
        TextField titleInputGeneral = new TextField();
        titleInputGeneral.setPromptText("Enter Movie Title");
        Button searchByTitleGeneralButton = new Button("Search");

        Button backButton12 = new Button("Back");
        HBox backBox12 = new HBox(20);
        backBox12.setPadding(new Insets(20, 20, 20, 20));
        backBox12.setAlignment(Pos.BASELINE_RIGHT);
        backBox12.getChildren().addAll(backButton12);

        //Search By Release Year in General
        TextField releaseYearInputGeneral = new TextField();
        releaseYearInputGeneral.setPromptText("Enter Release Year");
        Button searchByReleaseYearGeneralButton = new Button("Search");

        Button backButton13 = new Button("Back");
        HBox backBox13 = new HBox(20);
        backBox13.setPadding(new Insets(20, 20, 20, 20));
        backBox13.setAlignment(Pos.BASELINE_RIGHT);
        backBox13.getChildren().addAll(backButton13);

        //Search By Genre in General
        TextField genreInputGeneral = new TextField();
        genreInputGeneral.setPromptText("Enter Genre");
        Button searchByGenreGeneralButton = new Button("Search");

        Button backButton14 = new Button("Back");
        HBox backBox14 = new HBox(20);
        backBox14.setPadding(new Insets(20, 20, 20, 20));
        backBox14.setAlignment(Pos.BASELINE_RIGHT);
        backBox14.getChildren().addAll(backButton14);

        //Search Movies of Other Production Companies
        TextField companyInputGeneral = new TextField();
        companyInputGeneral.setPromptText("Enter Production Company Name");
        Button searchByCompanyGeneralButton = new Button("Search");

        Button backButton15 = new Button("Back");
        HBox backBox15 = new HBox(20);
        backBox15.setPadding(new Insets(20, 20, 20, 20));
        backBox15.setAlignment(Pos.BASELINE_RIGHT);
        backBox15.getChildren().addAll(backButton15);

        //Own Company's Menu Item Execution
        //All Movies by The Company
        Label randomLabel1 = new Label("All Movies By The Company");

        Button backButton1 = new Button("Back");
        HBox backBox1 = new HBox(20);
        backBox1.setPadding(new Insets(20, 20, 20, 20));
        backBox1.setAlignment(Pos.BASELINE_RIGHT);
        backBox1.getChildren().addAll(backButton1);

        //Search By Title By The Company
        TextField titleInputByCompany = new TextField();
        titleInputByCompany.setPromptText("Enter Movie Title");
        Button searchByTitleByCompanyButton = new Button("Search");

        Button backButton2 = new Button("Back");
        HBox backBox2 = new HBox(20);
        backBox2.setPadding(new Insets(20, 20, 20, 20));
        backBox2.setAlignment(Pos.BASELINE_RIGHT);
        backBox2.getChildren().addAll(backButton2);

        //Search By Release Year By The Company
        TextField releaseYearInputByCompany = new TextField();
        releaseYearInputByCompany.setPromptText("Enter Release Year");
        Button searchByReleaseYearByCompanyButton = new Button("Search");

        Button backButton3 = new Button("Back");
        HBox backBox3 = new HBox(20);
        backBox3.setPadding(new Insets(20, 20, 20, 20));
        backBox3.setAlignment(Pos.BASELINE_RIGHT);
        backBox3.getChildren().addAll(backButton3);

        //Search By Genre By The Company
        TextField genreInputByCompany = new TextField();
        genreInputByCompany.setPromptText("Enter Genre");
        Button searchByGenreByCompanyButton = new Button("Search");

        Button backButton4 = new Button("Back");
        HBox backBox4 = new HBox(20);
        backBox4.setPadding(new Insets(20, 20, 20, 20));
        backBox4.setAlignment(Pos.BASELINE_RIGHT);
        backBox4.getChildren().addAll(backButton4);

        //Search Most Recent By The Company
        Label mostRecentMovieLabel = new Label("List of Most Recent Movies By This Company");

        Button backButton5 = new Button("Back");
        HBox backBox5 = new HBox(20);
        backBox5.setPadding(new Insets(20, 20, 20, 20));
        backBox5.setAlignment(Pos.BASELINE_RIGHT);
        backBox5.getChildren().addAll(backButton5);

        //Search Movie/s with Max Revenue By The Company
        Label maxRevenueBYCompanyLabel = new Label("List of Movies With Maximum Revenue By This Company");

        Button backButton6 = new Button("Back");
        HBox backBox6 = new HBox(20);
        backBox6.setPadding(new Insets(20, 20, 20, 20));
        backBox6.setAlignment(Pos.BASELINE_RIGHT);
        backBox6.getChildren().addAll(backButton6);

        //Search Total Profit By The Company
        Label totalProfitLabel = new Label("Total Profit Account For This Company");

        Button backButton7 = new Button("Back");
        HBox backBox7 = new HBox(20);
        backBox7.setPadding(new Insets(20, 20, 20, 20));
        backBox7.setAlignment(Pos.BASELINE_RIGHT);
        backBox7.getChildren().addAll(backButton7);

        //Add Menu Item Execution
        //Primary Add Menu Items Page
        Label addMovieLabel = new Label("Fill in the Boxes to Append Your New Movie!");

        Label addMovieNameLabel = new Label("New Movie Name: ");
        Label addMovieReleaseYearLabel = new Label("New Movie Release Year: ");
        Label addMovieGenre1Label = new Label("Genre(first): ");
        Label addMovieGenre2Label = new Label("Genre(second): ");
        Label addMovieGenre3Label = new Label("Genre(third): ");
        Label addMovieRunTimeLabel = new Label("New Movie Run Time: ");
        Label addMovieBudgetLabel = new Label("Movie Budget: ");
        Label addMovieRevenueLabel = new Label("Movie Revenue: ");

        TextField addMovieNameInput = new TextField();
        addMovieNameInput.setPromptText("New Movie Name");
        TextField addMovieReleaseYearInput = new TextField();
        addMovieReleaseYearInput.setPromptText("New Movie Release Year");
        TextField addMovieGenre1Input = new TextField();
        addMovieGenre1Input.setPromptText("Genre(first)");
        addMovieGenre1Input.setMaxWidth(100);
        TextField addMovieGenre2Input = new TextField();
        addMovieGenre2Input.setPromptText("Genre(second)");
        addMovieGenre2Input.setMaxWidth(100);
        TextField addMovieGenre3Input = new TextField();
        addMovieGenre3Input.setPromptText("Genre(third)");
        addMovieGenre3Input.setMaxWidth(100);
        TextField addMovieRunTimeInput = new TextField();
        addMovieRunTimeInput.setPromptText("New Movie Run Time");
        TextField addMovieBudgetInput = new TextField();
        addMovieBudgetInput.setPromptText("New Movie Budget");
        TextField addMovieRevenueInput = new TextField();
        addMovieRevenueInput.setPromptText("New Movie Revenue");

        Button addMovieButtonPrimary = new Button("Append New Movie");
        Button backButton31 = new Button("Back");
        HBox backBox31 = new HBox(20);
        backBox31.setPadding(new Insets(20, 20, 20, 20));
        backBox31.setAlignment(Pos.BASELINE_RIGHT);
        backBox31.getChildren().addAll(addMovieButtonPrimary, backButton31);

        //Final Add Movie Page
        Label addMovieFinalLabel = new Label("Confirm New Movie Information");

        Button addMovieButtonFinal = new Button("Append Movie");
        addMovieButtonFinal.setMinWidth(80);
        Button backButton311 = new Button("Back");
        backButton311.setMinWidth(80);


        //Transfer Menu Items
        //Primary Movie Transfer Page
        Label transferMovieListLabel = new Label("Select a Movie to Transfer");

        Button transferPrimary = new Button("Transfer");

        Button backButton41 = new Button("Back");
        HBox backBox41 = new HBox(20);
        backBox41.setPadding(new Insets(20, 20, 20, 20));
        backBox41.setAlignment(Pos.BASELINE_RIGHT);
        backBox41.getChildren().addAll(transferPrimary, backButton41);

        //Final Movie Transfer Page
        Label finalTransferMovieListLabel = new Label("Movies Selected for Transferring");

        TextField buyerCompanyNameInput = new TextField();
        buyerCompanyNameInput.setPromptText("Enter Buyer Production Company");

        Button transferFinal = new Button("Transfer");
        transferFinal.setMinWidth(80);

        Button backButton411 = new Button("Back");
        backButton411.setMinWidth(80);

        HBox backBox411 = new HBox(20);
        backBox411.setPadding(new Insets(20, 20, 20, 20));
        backBox411.setAlignment(Pos.BASELINE_RIGHT);
        backBox411.getChildren().addAll(buyerCompanyNameInput, transferFinal, backButton411);

        //Scene Layouts
        //Log in Layout
        VBox logInLayout = new VBox(20);
        logInLayout.setPadding(new Insets(50, 120, 50, 120));
        logInLayout.setAlignment(Pos.CENTER);
        logInLayout.getChildren().addAll(logInText, companyInput, logInButton);

        //Welcome Page Layout
        VBox welcomeLayout = new VBox(20);
        welcomeLayout.setAlignment(Pos.CENTER);
        welcomeLayout.getChildren().addAll(welcomeText, welcomeButton);

        //Menu Page Layout
        HBox exit = new HBox(20);
        exit.getChildren().addAll(exitButton);
        exit.setAlignment(Pos.BASELINE_RIGHT);
        exit.setPadding(new Insets(10, 20, 20, 10));

        //General Menu Item Layouts
        //General all Movies Page Layout
        VBox labelBoxGeneral = new VBox(20);
        labelBoxGeneral.setPadding(new Insets(10, 20, 10, 20));
        labelBoxGeneral.setAlignment(Pos.CENTER);
        labelBoxGeneral.getChildren().addAll(allMovieGeneralLabel);

        //General Search By Title Page layout
        VBox searchByTitleGeneralLayout = new VBox(20);
        searchByTitleGeneralLayout.setPadding(new Insets(20, 120, 20, 120));
        searchByTitleGeneralLayout.setAlignment(Pos.CENTER);
        searchByTitleGeneralLayout.getChildren().addAll(titleInputGeneral, searchByTitleGeneralButton);

        //General Search By Release Year Layout
        VBox searchByReleaseYearGeneralLayout = new VBox(20);
        searchByReleaseYearGeneralLayout.setPadding(new Insets(20, 120, 20, 120));
        searchByReleaseYearGeneralLayout.setAlignment(Pos.CENTER);
        searchByReleaseYearGeneralLayout.getChildren().addAll(releaseYearInputGeneral, searchByReleaseYearGeneralButton);

        //General Search By Genre Page Layout
        VBox searchByGenreGeneralLayout = new VBox(20);
        searchByGenreGeneralLayout.setPadding(new Insets(20, 120, 20, 120));
        searchByGenreGeneralLayout.setAlignment(Pos.CENTER);
        searchByGenreGeneralLayout.getChildren().addAll(genreInputGeneral, searchByGenreGeneralButton);

        //General Search For Movies By Other Companies
        VBox searchByCompanyGeneralLayout = new VBox(20);
        searchByCompanyGeneralLayout.setPadding(new Insets(20, 120, 20, 120));
        searchByCompanyGeneralLayout.setAlignment(Pos.CENTER);
        searchByCompanyGeneralLayout.getChildren().addAll(companyInputGeneral, searchByCompanyGeneralButton);

        //Company Menu Item Layouts
        //Companies all Movies Page Layout
        VBox labelBox = new VBox(20);
        labelBox.setPadding(new Insets(10, 20, 10, 20));
        labelBox.setAlignment(Pos.CENTER);
        labelBox.getChildren().addAll(randomLabel1);

        //Companies Search By Title Page layout
        VBox searchByTitleByCompanyLayout = new VBox(20);
        searchByTitleByCompanyLayout.setPadding(new Insets(20, 120, 20, 120));
        searchByTitleByCompanyLayout.setAlignment(Pos.CENTER);
        searchByTitleByCompanyLayout.getChildren().addAll(titleInputByCompany, searchByTitleByCompanyButton);

        //Companies Search By Release Year Layout
        VBox searchByReleaseYearByCompanyLayout = new VBox(20);
        searchByReleaseYearByCompanyLayout.setPadding(new Insets(20, 120, 20, 120));
        searchByReleaseYearByCompanyLayout.setAlignment(Pos.CENTER);
        searchByReleaseYearByCompanyLayout.getChildren().addAll(releaseYearInputByCompany, searchByReleaseYearByCompanyButton);

        //Companies Search By Genre Page Layout
        VBox searchByGenreByCompanyLayout = new VBox(20);
        searchByGenreByCompanyLayout.setPadding(new Insets(20, 120, 20, 120));
        searchByGenreByCompanyLayout.setAlignment(Pos.CENTER);
        searchByGenreByCompanyLayout.getChildren().addAll(genreInputByCompany, searchByGenreByCompanyButton);

        //Companies Search For Most Recent Movie/s
        VBox searchMostRecentByCompanyLayout = new VBox(20);
        searchMostRecentByCompanyLayout.setAlignment(Pos.CENTER);
        searchMostRecentByCompanyLayout.setPadding(new Insets(10, 20 , 10, 20));
        searchMostRecentByCompanyLayout.getChildren().addAll(mostRecentMovieLabel);

        //Companies Search For Movies with Maximum Revenue
        VBox searchMaxRevenueByCompanyLayout = new VBox(20);
        searchMaxRevenueByCompanyLayout.setAlignment(Pos.CENTER);
        searchMaxRevenueByCompanyLayout.setPadding(new Insets(10, 20 , 10, 20));
        searchMaxRevenueByCompanyLayout.getChildren().addAll(maxRevenueBYCompanyLabel);

        //Companies Search For Total Profit
        VBox searchTotalProfitByCompanyLayout = new VBox(20);
        searchTotalProfitByCompanyLayout.setAlignment(Pos.CENTER);
        searchTotalProfitByCompanyLayout.setPadding(new Insets(10, 20 , 10, 20));
        searchTotalProfitByCompanyLayout.getChildren().addAll(totalProfitLabel);

        //Add Movie Pages Layouts
        //Primary Add Movie Page Layout
        VBox addMoviePrimaryLabelBox = new VBox(20);
        addMoviePrimaryLabelBox.setPadding(new Insets(10, 20, 10, 20));
        addMoviePrimaryLabelBox.setAlignment(Pos.CENTER);
        addMoviePrimaryLabelBox.getChildren().addAll(addMovieLabel);

        HBox addMovieNameHBox = new HBox(10);
        addMovieNameHBox.setPadding(new Insets(10, 0, 10, 5));
        //addMovieNameHBox.setAlignment(Pos.CENTER);
        addMovieNameHBox.getChildren().addAll(addMovieNameLabel, addMovieNameInput);

        HBox addMovieReleaseYearHBox = new HBox(10);
        addMovieReleaseYearHBox.setPadding(new Insets(10, 0, 10, 5));
        addMovieReleaseYearHBox.setAlignment(Pos.CENTER);
        addMovieReleaseYearHBox.getChildren().addAll(addMovieReleaseYearLabel, addMovieReleaseYearInput);

        HBox addMovieGenre1HBox = new HBox(10);
        addMovieGenre1HBox.setPadding(new Insets(10, 0, 10, 5));
        addMovieGenre1HBox.setAlignment(Pos.CENTER);
        addMovieGenre1HBox.getChildren().addAll(addMovieGenre1Label, addMovieGenre1Input);

        HBox addMovieGenre2HBox = new HBox(10);
        addMovieGenre2HBox.setPadding(new Insets(10, 0, 10, 5));
        addMovieGenre2HBox.setAlignment(Pos.CENTER);
        addMovieGenre2HBox.getChildren().addAll(addMovieGenre2Label, addMovieGenre2Input);

        HBox addMovieGenre3HBox = new HBox(10);
        addMovieGenre3HBox.setPadding(new Insets(10, 0, 10, 5));
        addMovieGenre3HBox.setAlignment(Pos.CENTER);
        addMovieGenre3HBox.getChildren().addAll(addMovieGenre3Label, addMovieGenre3Input);

        HBox addMovieRunTimeHBox = new HBox(10);
        addMovieRunTimeHBox.setPadding(new Insets(10, 0, 10, 5));
        addMovieRunTimeHBox.setAlignment(Pos.CENTER);
        addMovieRunTimeHBox.getChildren().addAll(addMovieRunTimeLabel, addMovieRunTimeInput);

        HBox addMovieBudgetHBox = new HBox(10);
        addMovieBudgetHBox.setPadding(new Insets(10, 0, 10, 5));
        addMovieBudgetHBox.setAlignment(Pos.CENTER);
        addMovieBudgetHBox.getChildren().addAll(addMovieBudgetLabel, addMovieBudgetInput);

        HBox addMovieRevenueHBox = new HBox(10);
        addMovieRevenueHBox.setPadding(new Insets(10, 0, 10, 5));
        addMovieRevenueHBox.setAlignment(Pos.CENTER);
        addMovieRevenueHBox.getChildren().addAll(addMovieRevenueLabel, addMovieRevenueInput);

        HBox addMovieReleaseYearAndRunTimeHBox = new HBox(20);
        addMovieReleaseYearAndRunTimeHBox.setPadding(new Insets(10, 0, 10, 0));
        //addMovieReleaseYearAndRunTimeHBox.setAlignment(Pos.CENTER);
        addMovieReleaseYearAndRunTimeHBox.getChildren().addAll(addMovieReleaseYearHBox, addMovieRunTimeHBox);

        HBox addMovieAllGenresHBox = new HBox(20);
        addMovieAllGenresHBox.setPadding(new Insets(10, 0, 10, 0));
        //addMovieAllGenresHBox.setAlignment(Pos.CENTER);
        addMovieAllGenresHBox.getChildren().addAll(addMovieGenre1HBox, addMovieGenre2HBox, addMovieGenre3HBox);

        HBox addMovieBudgetAndRevenueTimeHBox = new HBox(20);
        addMovieBudgetAndRevenueTimeHBox.setPadding(new Insets(10, 0, 10, 0));
        // addMovieBudgetAndRevenueTimeHBox.setAlignment(Pos.CENTER);
        addMovieBudgetAndRevenueTimeHBox.getChildren().addAll(addMovieBudgetHBox, addMovieRevenueHBox);

        VBox addMoviePrimaryLayout = new VBox(20);
        addMoviePrimaryLayout.setPadding(new Insets(30, 30, 30, 30));
        addMoviePrimaryLayout.setAlignment(Pos.CENTER);
        addMoviePrimaryLayout.getChildren().addAll(addMovieNameHBox, addMovieReleaseYearAndRunTimeHBox, addMovieAllGenresHBox, addMovieBudgetAndRevenueTimeHBox);

        //Final Add Movie Page Layout
        VBox finalAddMovieLabelBox = new VBox(20);
        finalAddMovieLabelBox.setPadding(new Insets(10, 20, 10, 20));
        finalAddMovieLabelBox.setAlignment(Pos.CENTER);
        finalAddMovieLabelBox.getChildren().addAll(addMovieFinalLabel);

        VBox finalAddMovieVBox = new VBox(20);
        finalAddMovieVBox.setPadding(new Insets(10, 80, 10, 5));
        finalAddMovieVBox.setAlignment(Pos.CENTER);
        finalAddMovieVBox.getChildren().addAll(addMovieButtonFinal, backButton311);

        //Transfer Movies Pages Layouts
        //Transfer a Movie Page Layout
        VBox labelBoxTransfer = new VBox(20);
        labelBoxTransfer.setPadding(new Insets(10, 20, 10, 20));
        labelBoxTransfer.setAlignment(Pos.CENTER);
        labelBoxTransfer.getChildren().addAll(transferMovieListLabel);

        //Final Transfer Page Layout
        VBox labelBoxFinalTransfer = new VBox(20);
        labelBoxFinalTransfer.setPadding(new Insets(10, 20, 10, 20));
        labelBoxFinalTransfer.setAlignment(Pos.CENTER);
        labelBoxFinalTransfer.getChildren().addAll(finalTransferMovieListLabel);

        VBox transferFinalVBox = new VBox(20);
        transferFinalVBox.setPadding(new Insets(10, 110, 10, 20));
        transferFinalVBox.setAlignment(Pos.CENTER);
        transferFinalVBox.getChildren().addAll(buyerCompanyNameInput, transferFinal, backButton411);

        //Scene Anchoring
        //Log in Anchoring
        BorderPane logInBp = new BorderPane();
        logInBp.setCenter(logInLayout);

        //Menu Page Anchoring
        BorderPane menuBp = new BorderPane();
        menuBp.setTop(menuBar);
        menuBp.setBottom(exit);

        //All Movies General
        BorderPane allMoviesGeneralBp = new BorderPane();
        allMoviesGeneralBp.setTop(labelBoxGeneral);
        allMoviesGeneralBp.setBottom(backBox11);

        //Search By Title General
        BorderPane searchByTitleGeneralBp = new BorderPane();
        searchByTitleGeneralBp.setTop(searchByTitleGeneralLayout);
        searchByTitleGeneralBp.setBottom(backBox12);

        //Search By Release Year General
        BorderPane searchByReleaseYearGeneralBp = new BorderPane();
        searchByReleaseYearGeneralBp.setTop(searchByReleaseYearGeneralLayout);
        searchByReleaseYearGeneralBp.setBottom(backBox13);

        //Search By Genre General
        BorderPane searchByGenreGeneralBp = new BorderPane();
        searchByGenreGeneralBp.setTop(searchByGenreGeneralLayout);
        searchByGenreGeneralBp.setBottom(backBox14);

        //Search By Other Companies General
        BorderPane searchByCompanyGeneralBp = new BorderPane();
        searchByCompanyGeneralBp.setTop(searchByCompanyGeneralLayout);
        searchByCompanyGeneralBp.setBottom(backBox15);

        //All Movies By The Company
        BorderPane allMoviesByCompanyBp = new BorderPane();
        allMoviesByCompanyBp.setBottom(backBox1);

        //Search By Title By The Company
        BorderPane searchByTitleByCompanyBp = new BorderPane();
        searchByTitleByCompanyBp.setTop(searchByTitleByCompanyLayout);
        searchByTitleByCompanyBp.setBottom(backBox2);

        //Search By Release Year By The Company
        BorderPane searchByReleaseYearByCompanyBp = new BorderPane();
        searchByReleaseYearByCompanyBp.setTop(searchByReleaseYearByCompanyLayout);
        searchByReleaseYearByCompanyBp.setBottom(backBox3);

        //Search By Genre By The Company
        BorderPane searchByGenreByCompanyBp = new BorderPane();
        searchByGenreByCompanyBp.setTop(searchByGenreByCompanyLayout);
        searchByGenreByCompanyBp.setBottom(backBox4);

        //Search For Most Recent Movie By The Company
        BorderPane searchMostRecentByCompanyBp =  new BorderPane();
        searchMostRecentByCompanyBp.setTop(searchMostRecentByCompanyLayout);
        searchMostRecentByCompanyBp.setBottom(backBox5);

        //Search For Max Revenue Movie By The Company
        BorderPane searchMaxRevenueByCompanyBp =  new BorderPane();
        searchMaxRevenueByCompanyBp.setTop(searchMaxRevenueByCompanyLayout);
        searchMaxRevenueByCompanyBp.setBottom(backBox6);

        //Search Total Profit For The Company
        BorderPane searchTotalProfitByTheCompanyBp =  new BorderPane();
        searchTotalProfitByTheCompanyBp.setTop(searchTotalProfitByCompanyLayout);
        searchTotalProfitByTheCompanyBp.setBottom(backBox7);

        //Add Movie Pages Anchoring
        //Primary Add Movie Page
        BorderPane addMoviePrimaryBp = new BorderPane();
        addMoviePrimaryBp.setTop(addMoviePrimaryLabelBox);
        addMoviePrimaryBp.setCenter(addMoviePrimaryLayout);
        addMoviePrimaryBp.setBottom(backBox31);

        //Final Add Movie Page
        BorderPane addMovieFinalBp = new BorderPane();
        addMovieFinalBp.setTop(finalAddMovieLabelBox);
        addMovieFinalBp.setRight(finalAddMovieVBox);

        //Transfer Pages Anchoring
        //Primary Transfer Movie Page
        BorderPane transferMovieSelectBp = new BorderPane();
        transferMovieSelectBp.setTop(labelBoxTransfer);
        transferMovieSelectBp.setBottom(backBox41);

        //Final Transfer Movie Page
        BorderPane transferMovieFinalBp = new BorderPane();
        transferMovieFinalBp.setTop(labelBoxFinalTransfer);
        //transferMovieFinalBp.setBottom(backBox411);
        transferMovieFinalBp.setRight(transferFinalVBox);

        //All Scenes
        //Log in Scene
        Scene logInScene = new Scene(logInBp, 700, 500);
        Scene welcomeScene = new Scene(welcomeLayout, 700, 500);
        Scene mainMenuScene = new Scene(menuBp, 700, 500);

        Scene allMoviesGeneralScene = new Scene(allMoviesGeneralBp, 800, 550);
        Scene searchByTitleGeneralScene = new Scene(searchByTitleGeneralBp, 800, 350);
        Scene searchByReleaseYearGeneralScene = new Scene(searchByReleaseYearGeneralBp, 800, 550);
        Scene searchByGenreGeneralScene = new Scene(searchByGenreGeneralBp, 800, 550);
        Scene searchByCompanyGeneralScene = new Scene(searchByCompanyGeneralBp, 800, 550);

        Scene allMoviesByCompanyScene = new Scene(allMoviesByCompanyBp, 800, 550);
        Scene searchByTitleByCompanyScene = new Scene(searchByTitleByCompanyBp, 800, 350);
        Scene searchByReleaseYearByCompanyScene = new Scene(searchByReleaseYearByCompanyBp, 800, 500);
        Scene searchByGenreByCompanyScene = new Scene(searchByGenreByCompanyBp, 800, 500);
        Scene searchMostRecentByCompanyScene = new Scene(searchMostRecentByCompanyBp, 800, 350);
        Scene searchMaxRevenueByCompanyScene = new Scene(searchMaxRevenueByCompanyBp, 800, 350);
        Scene searchTotalProfitByCompanyScene = new Scene(searchTotalProfitByTheCompanyBp, 800, 350);

        Scene addMoviePrimaryScene = new Scene(addMoviePrimaryBp, 1000, 550);
        Scene addMovieFinalScene = new Scene(addMovieFinalBp, 800, 550);

        Scene transferMovieSelectScene = new Scene(transferMovieSelectBp, 800, 550);
        Scene transferMovieFinalScene = new Scene(transferMovieFinalBp, 800, 550);

        //All Button Actions
        //Log in
        logInButton.setOnAction(e -> {
            window.setScene(welcomeScene);
            companyName = companyInput.getText();
            //System.out.println(companyName);
            networkConnection.write(companyName);

            //Reading AllMovieList and CompanyMovieList From Server at Start
            ConferInfoWrapper dObjectAll = new ConferInfoWrapper();
            ConferInfoWrapper dObjectCompany = new ConferInfoWrapper();
            dObjectAll.operation = ALL_MOVIE_CALL;
            dObjectCompany.operation = COMPANY_MOVIE_CALL;

            try{
                networkConnection.write(dObjectAll);
                Object allMoviesReturned = networkConnection.read();
                allMovieDatabase.movieList = (List<Movie>) allMoviesReturned;
                networkConnection.write(dObjectCompany);
                Object companyMoviesReturned = networkConnection.read();
                companyMoviesDatabase.movieList = (List<Movie>) companyMoviesReturned;
            }catch (Exception exp){
                System.out.println("Lists Not Converted");
            }

            new Thread(()->{

                while(true){
                    //Receiving Transferred Movie Items
                    Object transferredMovieList = new Object();
                    try{
                        transferredMovieList = networkConnection.read();
                    }catch (Exception ex){
                        System.out.println("System failed in company software thread");
                    }
                    if(transferredMovieList instanceof List){
                        List<Movie> tempList = new ArrayList<>();
                        tempList = (List<Movie>)transferredMovieList;
                        if(tempList.size()==0){
                            break;
                        }
                        for(Movie x : tempList){
                            ConferInfoWrapper movieDataObject = new ConferInfoWrapper();
                            movieDataObject.sender = companyName;
                            movieDataObject.receiver = companyName;
                            movieDataObject.toTransfer = x.getTitle();
                            movieDataObject.operation = UPDATE_SERVER_CALL;

                            //Writing to Server to update Server List
                            try {
                                networkConnection.write(movieDataObject.clone());
                            } catch (CloneNotSupportedException ex) {
                                System.out.println("Updating Server failed");
                            }

                            //Updating All Lists and Tables Real Time
                            if(x.getProducer().equalsIgnoreCase(companyName)){
                                companyMoviesDatabase.movieList.add(x);
                            }
                            allMovieDatabase.replaceMovie(x, companyName);
                            if(displayTableScope.get()){
                                displayTable.getItems().add(x);
                            }
                        }
                    }

                    //Receiving New Added Movies
                    if(transferredMovieList instanceof Movie){
                        Movie movie = (Movie)  transferredMovieList;
                        allMovieDatabase.movieList.add(movie);
                        if(movie.getProducer().equalsIgnoreCase(companyName)){
                            companyMoviesDatabase.movieList.add(movie);
                        }
                        if(addToTableScope.get()){
                            displayTable.getItems().add(movie);
                        }
                    }

                    //Pressing Exit Button Breaks The Loop
                    if(threadCheck.get()){
                        break;
                    }
                }//while-true
            }).start();
        });

        //Welcome
        welcomeButton.setOnAction(e ->{
            window.setScene(mainMenuScene);
            Label companyNameLabel = new Label(companyName.toUpperCase());
            companyNameLabel.setFont(Font.font("courier", 25));
            companyNameLabel.getStyleClass().add("custom-label");
            menuBp.setCenter(companyNameLabel);
        });

        //General Menu Item Actions
        //All Movies General Button Actions
        allGeneral.setOnAction(e -> {
            window.setScene(allMoviesGeneralScene);
            ObservableList<Movie> items = FXCollections.observableArrayList();
            for (Movie x : allMovieDatabase.movieList) {
                items.add(x);
            }

            displayTable.getItems().removeAll(displayTable.getItems());
            displayTable.setItems(items);
            HBox tableBox = new HBox(20);
            tableBox.setPadding(new Insets(15, 30, 0, 30));
            tableBox.getChildren().addAll(displayTable);
            tableBox.setAlignment(Pos.BASELINE_CENTER);
            allMoviesGeneralBp.setCenter(tableBox);
            displayTableScope.set(true);
            addToTableScope.set(true);
        });
        backButton11.setOnAction(e ->backProgram(mainMenuScene, displayTableScope, addToTableScope));

        //Search Movie By Title General Actions
        searchByTitleGeneral.setOnAction(e -> {
            searchByTitleGeneralBp.setCenter(null);
            window.setScene(searchByTitleGeneralScene);
        });
        searchByTitleGeneralButton.setOnAction(e -> {
            ObservableList<Movie> items = FXCollections.observableArrayList();
            List<Movie> tempList = allMovieDatabase.searchMovieByTitle(titleInputGeneral.getText());
            for (Movie x : tempList) {
                items.add(x);
            }

            displayTable.getItems().removeAll(displayTable.getItems());
            displayTable.setItems(items);
            HBox tableBox = new HBox(20);
            tableBox.setPadding(new Insets(15, 30, 0, 30));
            tableBox.getChildren().addAll(displayTable);
            tableBox.setAlignment(Pos.BASELINE_CENTER);
            searchByTitleGeneralBp.setCenter(tableBox);
            titleInputGeneral.clear();

            if(tempList.size()==0){
                Label noLabel = new Label("No such Movie with This Title");
                searchByTitleGeneralBp.setCenter(noLabel);
            }
            displayTableScope.set(true);
        });
        backButton12.setOnAction(e ->backProgram(mainMenuScene, displayTableScope));

        //Search By Release Year General Actions
        searchByReleaseYearGeneral.setOnAction(e -> {
            searchByReleaseYearGeneralBp.setCenter(null);
            window.setScene(searchByReleaseYearGeneralScene);
        });
        searchByReleaseYearGeneralButton.setOnAction(e -> {
            ObservableList<Movie> items = FXCollections.observableArrayList();
            List<Movie> tempList = allMovieDatabase.searchMovieByReleaseYear(Integer.parseInt(releaseYearInputGeneral.getText()));
            for (Movie x : tempList) {
                items.add(x);
            }
            displayTable.getItems().removeAll(displayTable.getItems());
            displayTable.setItems(items);
            HBox tableBox = new HBox(20);
            tableBox.setPadding(new Insets(15, 30, 0, 30));
            tableBox.getChildren().addAll(displayTable);
            tableBox.setAlignment(Pos.BASELINE_CENTER);
            searchByReleaseYearGeneralBp.setCenter(tableBox);
            releaseYearInputGeneral.clear();

            if (tempList.size() == 0) {
                Label noLabel = new Label("No such Movie with that year in this company");
                searchByReleaseYearGeneralBp.setCenter(noLabel);
            }
            displayTableScope.set(true);
        });
        backButton13.setOnAction(e ->backProgram(mainMenuScene, displayTableScope));

        //Search Movie By Genre General Actions
        searchByGenreGeneral.setOnAction(e -> {
            searchByGenreGeneralBp.setCenter(null);
            window.setScene(searchByGenreGeneralScene);
        });
        searchByGenreGeneralButton.setOnAction(e -> {
            ObservableList<Movie> items = FXCollections.observableArrayList();
            List<Movie> tempList = allMovieDatabase.searchMovieByGenre(genreInputGeneral.getText());
            for (Movie x : tempList) {
                items.add(x);
            }

            displayTable.getItems().removeAll(displayTable.getItems());
            displayTable.setItems(items);
            HBox tableBox = new HBox(20);
            tableBox.setPadding(new Insets(15, 30, 0, 30));
            tableBox.getChildren().addAll(displayTable);
            tableBox.setAlignment(Pos.BASELINE_CENTER);
            searchByGenreGeneralBp.setCenter(tableBox);
            genreInputGeneral.clear();

            if(tempList.size()==0){
                Label noLabel = new Label("No such Movie with This Genre By This Company");
                searchByGenreGeneralBp.setCenter(noLabel);
            }
            displayTableScope.set(true);
        });
        backButton14.setOnAction(e ->backProgram(mainMenuScene, displayTableScope));

        //Search Movie By Other Companies General Actions
        searchByCompanyGeneral.setOnAction(e -> {
            searchByCompanyGeneralBp.setCenter(null);
            window.setScene(searchByCompanyGeneralScene);
        });
        searchByCompanyGeneralButton.setOnAction(e -> {
            ObservableList<Movie> items = FXCollections.observableArrayList();
            List<Movie> tempList = allMovieDatabase.searchMovieByPCompany(companyInputGeneral.getText());
            for (Movie x : tempList) {
                items.add(x);
            }

            displayTable.getItems().removeAll(displayTable.getItems());
            displayTable.setItems(items);
            HBox tableBox = new HBox(20);
            tableBox.setPadding(new Insets(15, 30, 0, 30));
            tableBox.getChildren().addAll(displayTable);
            tableBox.setAlignment(Pos.BASELINE_CENTER);
            searchByCompanyGeneralBp.setCenter(tableBox);
            companyInputGeneral.clear();

            if(tempList.size()==0){
                Label noLabel = new Label("No such Movie with This Title");
                searchByCompanyGeneralBp.setCenter(noLabel);
            }
            displayTableScope.set(true);
        });
        backButton15.setOnAction(e ->backProgram(mainMenuScene, displayTableScope));

        //Company Menu Item Actions
        //All Movies By Company Buttons
        allByCompany.setOnAction(e -> {
            window.setScene(allMoviesByCompanyScene);
            ObservableList<Movie> items = FXCollections.observableArrayList();
            for (Movie x : companyMoviesDatabase.movieList) {
                items.add(x);
            }

            displayTable.getItems().removeAll(displayTable.getItems());
            displayTable.setItems(items);
            HBox tableBox = new HBox(20);
            tableBox.setPadding(new Insets(15, 30, 0, 30));
            tableBox.getChildren().addAll(displayTable);
            tableBox.setAlignment(Pos.BASELINE_CENTER);
            allMoviesByCompanyBp.setTop(labelBox);
            allMoviesByCompanyBp.setCenter(tableBox);
            displayTableScope.set(true);
        });
        backButton1.setOnAction(e ->backProgram(mainMenuScene, displayTableScope));

        //Search Movie By Title By Company Actions
        searchByTitleByCompany.setOnAction(e -> {
            searchByTitleByCompanyBp.setCenter(null);
            window.setScene(searchByTitleByCompanyScene);
        });
        searchByTitleByCompanyButton.setOnAction(e -> {
            ObservableList<Movie> items = FXCollections.observableArrayList();
            List<Movie> tempList = companyMoviesDatabase.searchMovieByTitle(titleInputByCompany.getText());
            for (Movie x : tempList) {
                items.add(x);
            }

            displayTable.getItems().removeAll(displayTable.getItems());
            displayTable.setItems(items);
            HBox tableBox = new HBox(20);
            tableBox.setPadding(new Insets(15, 30, 0, 30));
            tableBox.getChildren().addAll(displayTable);
            tableBox.setAlignment(Pos.BASELINE_CENTER);
            searchByTitleByCompanyBp.setCenter(tableBox);
            titleInputByCompany.clear();

            if(tempList.size()==0){
                Label noLabel = new Label("No such Movie with This Title");
                searchByTitleByCompanyBp.setCenter(noLabel);
            }
            displayTableScope.set(true);
        });
        backButton2.setOnAction(e ->backProgram(mainMenuScene, displayTableScope));

        //Search By Release Year By Company Actions
        searchByReleaseYearByCompany.setOnAction(e -> {
            searchByReleaseYearByCompanyBp.setCenter(null);
            window.setScene(searchByReleaseYearByCompanyScene);
        });
        searchByReleaseYearByCompanyButton.setOnAction(e -> {
            ObservableList<Movie> items = FXCollections.observableArrayList();
            List<Movie> tempList = companyMoviesDatabase.searchMovieByReleaseYear(Integer.parseInt(releaseYearInputByCompany.getText()));
            for (Movie x : tempList) {
                items.add(x);
            }
            displayTable.getItems().removeAll(displayTable.getItems());
            displayTable.setItems(items);
            HBox tableBox = new HBox(20);
            tableBox.setPadding(new Insets(15, 30, 0, 30));
            tableBox.getChildren().addAll(displayTable);
            tableBox.setAlignment(Pos.BASELINE_CENTER);
            searchByReleaseYearByCompanyBp.setCenter(tableBox);
            releaseYearInputByCompany.clear();

            if (tempList.size() == 0) {
                Label noLabel = new Label("No such Movie with that year in this company");
                searchByReleaseYearByCompanyBp.setCenter(noLabel);
            }
            displayTableScope.set(true);
        });
        backButton3.setOnAction(e -> backProgram(mainMenuScene, displayTableScope));

        //Search Movie By Genre By Company Actions
        searchByGenreByCompany.setOnAction(e -> {
            searchByGenreByCompanyBp.setCenter(null);
            window.setScene(searchByGenreByCompanyScene);
        });
        searchByGenreByCompanyButton.setOnAction(e -> {
            ObservableList<Movie> items = FXCollections.observableArrayList();
            List<Movie> tempList = companyMoviesDatabase.searchMovieByGenre(genreInputByCompany.getText());
            for (Movie x : tempList) {
                items.add(x);
            }

            displayTable.getItems().removeAll(displayTable.getItems());
            displayTable.setItems(items);
            HBox tableBox = new HBox(20);
            tableBox.setPadding(new Insets(15, 30, 0, 30));
            tableBox.getChildren().addAll(displayTable);
            tableBox.setAlignment(Pos.BASELINE_CENTER);
            searchByGenreByCompanyBp.setCenter(tableBox);
            genreInputByCompany.clear();

            if(tempList.size()==0){
                Label noLabel = new Label("No such Movie with This Genre By This Company");
                searchByTitleByCompanyBp.setCenter(noLabel);
            }
            displayTableScope.set(true);
        });
        backButton4.setOnAction(e -> backProgram(mainMenuScene, displayTableScope));

        //Search Most Recent By Company
        searchMostRecentByCompany.setOnAction(e -> {
            window.setScene(searchMostRecentByCompanyScene);
            ObservableList<Movie> items = FXCollections.observableArrayList();
            List<Movie> tempList = companyMoviesDatabase.searchRecentMoviesOfCompany(companyName);
            for (Movie x : tempList) {
                items.add(x);
            }

            displayTable.getItems().removeAll(displayTable.getItems());
            displayTable.setItems(items);
            HBox tableBox = new HBox(20);
            tableBox.setPadding(new Insets(15, 30, 0, 30));
            tableBox.getChildren().addAll(displayTable);
            tableBox.setAlignment(Pos.BASELINE_CENTER);
            searchMostRecentByCompanyBp.setCenter(tableBox);
            displayTableScope.set(true);
        });
        backButton5.setOnAction(e -> backProgram(mainMenuScene, displayTableScope));

        //Search Max Revenue Movie By Company
        searchMaxRevenueByCompany.setOnAction(e -> {
            window.setScene(searchMaxRevenueByCompanyScene);
            ObservableList<Movie> items = FXCollections.observableArrayList();
            List<Movie> tempList = companyMoviesDatabase.searchMoviesWithMaxRevenueOfCompany(companyName);
            for (Movie x : tempList) {
                items.add(x);
            }

            displayTable.getItems().removeAll(displayTable.getItems());
            displayTable.setItems(items);
            HBox tableBox = new HBox(20);
            tableBox.setPadding(new Insets(20, 30, 0, 30));
            tableBox.getChildren().addAll(displayTable);
            tableBox.setAlignment(Pos.BASELINE_CENTER);
            searchMaxRevenueByCompanyBp.setCenter(tableBox);
            displayTableScope.set(true);
        });
        backButton6.setOnAction(e -> backProgram(mainMenuScene, displayTableScope));

        //Search Total Profit By Company
        searchTotalProfitByCompany.setOnAction(e -> {
            window.setScene(searchTotalProfitByCompanyScene);
            ObservableList<Movie> items = FXCollections.observableArrayList();
            TotalProfit temp = companyMoviesDatabase.findTotalProfitOfCompany(companyName);

            TableView<TotalProfit> displayTotalProfit = new TableView<>();

            TableColumn<TotalProfit, String> nameColumn = new TableColumn<>("Company Name");
            nameColumn.setMinWidth(200);
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

            TableColumn<TotalProfit, Long> totalProfitColumn = new TableColumn<>("Total Profit");
            totalProfitColumn.setMinWidth(120);
            totalProfitColumn.setCellValueFactory(new PropertyValueFactory<>("totalProfit"));

            displayTotalProfit.getColumns().addAll(nameColumn, totalProfitColumn);

            displayTotalProfit.getItems().removeAll(displayTable.getItems());
            displayTotalProfit.getItems().add(temp);
            HBox tableBox = new HBox(20);
            tableBox.setPadding(new Insets(30, 30, 0, 30));
            tableBox.getChildren().addAll(displayTotalProfit);
            tableBox.setAlignment(Pos.BASELINE_CENTER);
            searchTotalProfitByTheCompanyBp.setCenter(tableBox);
        });
        backButton7.setOnAction(e -> backProgram(mainMenuScene, displayTableScope));

        //Add Movie Menu Item Actions
        //Add Movie Primary Page
        addMovieItem.setOnAction(e->{
            window.setScene(addMoviePrimaryScene);
        });
        addMovieButtonPrimary.setOnAction(e->{
            window.setScene(addMovieFinalScene);

            addMovieFinalBp.setLeft(null);
            ListView<String> addMovieConfirmList = new ListView<>();
            addMovieConfirmList.getItems().removeAll(addMovieConfirmList.getItems());

            String listName = "Movie Name: " + addMovieNameInput.getText();
            String listReleaseYear = "Release Year: " + addMovieReleaseYearInput.getText();
            String listRunTime = "Run Time: " + addMovieRunTimeInput.getText();
            String listGenres = "Genres: " + addMovieGenre1Input.getText()+ ", "+ addMovieGenre2Input.getText() + ", " + addMovieGenre3Input.getText();
            String listBudget = "Budget: " + addMovieBudgetInput.getText();
            String listRevenue = "Revenue: " + addMovieRevenueInput.getText();

            addMovieConfirmList.getItems().addAll(listName, listReleaseYear, listRunTime, listGenres, listBudget, listRevenue);

            HBox tableBox = new HBox(20);
            tableBox.setPadding(new Insets(60, 50, 80, 150));
            tableBox.getChildren().addAll(addMovieConfirmList);
            tableBox.setAlignment(Pos.BASELINE_CENTER);
            addMovieFinalBp.setLeft(tableBox);

        });
        backButton31.setOnAction(e->window.setScene(mainMenuScene));

        //Add Movie Final Page
        addMovieButtonFinal.setOnAction(e->{
            boolean addMovieConfirm = ConfirmBox.display("Confirm to Add Movie", "Are You Sure You Want To Append This Movie?");
            if(addMovieConfirm){
                String movieLine = addMovieNameInput.getText()+","+addMovieReleaseYearInput.getText()+","+addMovieGenre1Input.getText()+","+addMovieGenre2Input.getText()+","+addMovieGenre3Input.getText()+","+addMovieRunTimeInput.getText()+","+companyName+","+addMovieBudgetInput.getText()+","+addMovieRevenueInput.getText();
                Movie movie = new Movie(movieLine);

                ConferInfoWrapper movieDataObject = new ConferInfoWrapper();
                movieDataObject.sender = companyName;
                movieDataObject.receiver = companyName;
                movieDataObject.toTransfer = movieLine;
                movieDataObject.operation = ADD_MOVIE_CALL;

                try {
                    networkConnection.write(movieDataObject.clone());
                } catch (CloneNotSupportedException ex) {
                    System.out.println("Add Movie failed");
                }
            }
        });
        backButton311.setOnAction(e->{
            window.setScene(addMoviePrimaryScene);
        });

        //Transfer Menu Items Actions
        transferMovieItem.setOnAction(e->{
            window.setScene(transferMovieSelectScene);
            ObservableList<Movie> items = FXCollections.observableArrayList();
            for (Movie x : companyMoviesDatabase.movieList) {
                items.add(x);
            }

            displayTable.getItems().removeAll(displayTable.getItems());
            displayTable.setItems(items);
            HBox tableBox = new HBox(20);
            tableBox.setPadding(new Insets(15, 30, 0, 30));
            tableBox.getChildren().addAll(displayTable);
            tableBox.setAlignment(Pos.BASELINE_CENTER);
            transferMovieSelectBp.setTop(labelBox);
            transferMovieSelectBp.setCenter(tableBox);
            displayTableScope.set(true);
        });

        transferPrimary.setOnAction(e->{
            window.setScene(transferMovieFinalScene);
            ObservableList<Movie> selectedItems;
            selectedItems = displayTable.getSelectionModel().getSelectedItems();
            transferMovieList.clear();
            for(Movie x : selectedItems){
                transferMovieList.add(x);
            }

            transferMovieFinalBp.setCenter(null);
            ListView<String> transferMovieConfirmList = new ListView<>();
            transferMovieConfirmList.getItems().removeAll(transferMovieConfirmList.getItems());
            for(Movie x : transferMovieList){
                transferMovieConfirmList.getItems().add(x.getTitle());
            }
            HBox tableBox = new HBox(20);
            tableBox.setPadding(new Insets(60, 50, 80, 150));
            tableBox.getChildren().addAll(transferMovieConfirmList);
            tableBox.setAlignment(Pos.BASELINE_CENTER);
            transferMovieFinalBp.setLeft(tableBox);
            displayTableScope.set(true);
        });
        backButton41.setOnAction(e-> backProgram(mainMenuScene, displayTableScope));

        AtomicBoolean transferConfirmForRemoving = new AtomicBoolean(false);
        transferFinal.setOnAction(e->{
            boolean transferConfirmAnswer = ConfirmBox.display("Confirm Transfer", "Transfer \nthe selected movie(s) to \n"+buyerCompanyNameInput.getText()+"?");
            transferConfirmForRemoving.set(transferConfirmAnswer);

            if(transferConfirmAnswer==true){
                for(Movie x : transferMovieList){
                    System.out.println(x.print());
                }

                for(Movie x : transferMovieList){

                    ConferInfoWrapper movieDataObject = new ConferInfoWrapper();
                    movieDataObject.sender = companyName;
                    movieDataObject.receiver = buyerCompanyNameInput.getText();
                    movieDataObject.toTransfer = x.getTitle();
                    movieDataObject.operation = TRANSFER_MOVIE_CALL;

                    try {
                        networkConnection.write(movieDataObject.clone());
                    } catch (CloneNotSupportedException ex) {
                        System.out.println("Transfer failed");
                    }

                    for(Movie y : companyMoviesDatabase.movieList){
                        if(y.equalMovie(x)){
                            allMovieDatabase.replaceMovie(x, buyerCompanyNameInput.getText());
                            companyMoviesDatabase.movieList.remove(y);
                        }
                    }
                }
                buyerCompanyNameInput.clear();
            }
            buyerCompanyNameInput.clear();
        });
        backButton411.setOnAction(e->{
            window.setScene(transferMovieSelectScene);
            buyerCompanyNameInput.clear();
            if(transferConfirmForRemoving.get()){
                for(Movie x : transferMovieList){
                    displayTable.getItems().remove(x);
                }
            }
        });

        //Log Out Button Action
        exitButton.setOnAction(e ->{
            threadCheck.set(true);
            closeProgram();
        });

        //All CSS Applications to Scenes
        try{
            logInScene.getStylesheets().add("styles.css");
            welcomeScene.getStylesheets().add("styles.css");
            mainMenuScene.getStylesheets().add("mainScene.css");

            allMoviesGeneralScene.getStylesheets().add("Tablepage.css");
            searchByTitleGeneralScene.getStylesheets().add("Tablepage.css");
            searchByReleaseYearGeneralScene.getStylesheets().add("Tablepage.css");
            searchByGenreGeneralScene.getStylesheets().add("Tablepage.css");
            searchByCompanyGeneralScene.getStylesheets().add("Tablepage.css");

            allMoviesByCompanyScene.getStylesheets().add("Tablepage.css");
            searchByTitleByCompanyScene.getStylesheets().add("styles.css");
            searchByReleaseYearByCompanyScene.getStylesheets().add("Tablepage.css");
            searchByGenreByCompanyScene.getStylesheets().add("Tablepage.css");
            searchMostRecentByCompanyScene.getStylesheets().add("Tablepage.css");
            searchMaxRevenueByCompanyScene.getStylesheets().add("Tablepage.css");
            searchTotalProfitByCompanyScene.getStylesheets().add("Tablepage.css");

            addMoviePrimaryScene.getStylesheets().add("styles.css");
            addMovieFinalScene.getStylesheets().add("Tablepage.css");

            transferMovieSelectScene.getStylesheets().add("Tablepage.css");
            transferMovieFinalScene.getStylesheets().add("Tablepage.css");

        }catch (ClassCastException e){
            e.printStackTrace();
        }

        window.setScene(logInScene);
        window.show();

    }//Start Method

    //To Confirm Closure of Program
    private void closeProgram(){
        boolean result = ConfirmBox.display("ConfirmBox", "Are you sure you want to close this Program?");
        if(result){
            window.close();
        }
    }

    private void backProgram(Scene mainMenuScene, AtomicBoolean displayTableScope){
        window.setScene(mainMenuScene);
        displayTableScope.set(false);
    }

    private void backProgram(Scene mainMenuScene, AtomicBoolean displayTableScope, AtomicBoolean addToTableScope){
        window.setScene(mainMenuScene);
        displayTableScope.set(false);
        addToTableScope.set(false);
    }
}//Class


