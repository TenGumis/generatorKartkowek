package mainWindow;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sun.font.TextLabel;

import java.util.Stack;

/**
 * Created by pabia on 11.05.2016.
 */
public class AboutScene{

    Scene aboutScene;

    public AboutScene(Stage window,Stack<Scene> stackScene){
        BorderPane aboutLayout=new BorderPane();
        VBox aboutText=new VBox();

        Label aboutLabel=new Label("Projekt na Programowanie Obiektowe 2015/2016:\n" +
                "\n" +
                "> Bartodziej Filip\n" +
                "> Bąk Paweł\n" +
                "> Pabian Mateusz\n" +
                "\n");
        aboutLabel.setMaxWidth(300);

        Button backButton=new Button("Back");
        backButton.setOnAction(e-> {
            stackScene.pop();
            window.setScene(stackScene.peek());
        });
        backButton.setMaxWidth(130);

        aboutText.getChildren().addAll(aboutLabel,backButton);
        aboutText.setSpacing(20);
        aboutText.setAlignment(Pos.CENTER);

        aboutLayout.setCenter(aboutText);

        aboutScene=new Scene(aboutLayout,400,400);
    }

    Scene getScene(){
        return aboutScene;
    }
}
