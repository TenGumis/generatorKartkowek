package mainWindow;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Stack;

/**
 * Created by pabia on 12.05.2016.
 */
public class NewDatabaseScene {
    Scene newDatabaseScene;

    public NewDatabaseScene(Stage window, Stack<Scene> stackScene){
        BorderPane newDatabaseLayout=new BorderPane();
        VBox newDatabasePanel=new VBox();

        Label infoLabel=new Label("Create databse");
        infoLabel.setMaxWidth(130);

        Button backButton=new Button("Back");
        backButton.setOnAction(e-> {
            stackScene.pop();
            window.setScene(stackScene.peek());
        });
        backButton.setMaxWidth(130);

        newDatabasePanel.getChildren().addAll(infoLabel,backButton);
        newDatabasePanel.setSpacing(20);
        newDatabasePanel.setAlignment(Pos.CENTER);

        newDatabaseLayout.setCenter(newDatabasePanel);

        newDatabaseScene=new Scene(newDatabaseLayout,400,400);
    }

    Scene getScene(){
        return newDatabaseScene;
    }
}
