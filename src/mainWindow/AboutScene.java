package mainWindow;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sun.font.TextLabel;

import java.io.File;
import java.util.Stack;

/**
 * Created by pabia on 11.05.2016.
 */
public class AboutScene{

    Stage aboutStage=null;

    public AboutScene(){

        aboutStage=new Stage();
        aboutStage.setTitle("About");
        Scene aboutScene;

        BorderPane aboutLayout=new BorderPane();
        VBox aboutText=new VBox();

        Label aboutLabel=new Label("Projekt na Programowanie Obiektowe 2015/2016:\n" +
                "\n" +
                "> Bartodziej Filip\n" +
                "> Bąk Paweł\n" +
                "> Pabian Mateusz\n" +
                "\n");


        aboutText.getChildren().addAll(aboutLabel);
        aboutText.setSpacing(20);
        aboutText.setAlignment(Pos.CENTER);

        aboutLayout.setCenter(aboutText);

        aboutScene=new Scene(aboutLayout,550,300);
        aboutScene.getStylesheets().add(getClass().getResource(".."+ File.separator+"styleScheets"+File.separator+"newDatabaseStyleScheet.css").toExternalForm());
        aboutStage.setScene(aboutScene);
    }

    Stage getWindow(){
        return aboutStage;
    }
}
