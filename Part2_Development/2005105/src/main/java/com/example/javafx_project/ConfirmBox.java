package com.example.javafx_project;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfirmBox {
    static boolean answer;
    public static boolean display(String title, String message){
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMaxHeight(600);

        Label label1 = new Label(message);

        Button yes = new Button("Yes");
        Button no = new Button("No");

        yes.setOnAction(e->{
            answer=true;
            window.close();
        });
        no.setOnAction(e->{
            answer=false;
            window.close();
        });
        label1.setAlignment(Pos.CENTER);

        VBox layoutForLabel = new VBox(10);
        layoutForLabel.setAlignment(Pos.CENTER);
        layoutForLabel.getChildren().addAll(label1);

        VBox layout1 = new VBox(25);
        layout1.setAlignment(Pos.CENTER);
        layout1.getChildren().addAll(layoutForLabel, yes, no);

        Scene scene1 = new Scene(layout1, 400, 250);
        window.setScene(scene1);

        window.showAndWait();

        return answer;
    }
}

/*
Button button4 = new Button("ConfirmBox");
        button4.setOnAction(e->{
            boolean result=ConfirmBox.display("Goblet of Fire", "Harry did ypynitgof?");
            System.out.println(result);
        });
*/