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
 * Created by tengumis on 12.05.2016.
 */
public class LoadDatabaseScene {

    private Scene loadDatabaseScene;

    public LoadDatabaseScene(Stage window, Stack<Scene> stackScene){
        BorderPane loadDatabaseLayout=new BorderPane();
        VBox loadDatabasePanel=new VBox();

        Label infoLabel=new Label("Set database name");
        infoLabel.setMaxWidth(130);

        Button backButton=new Button("Back");
        backButton.setOnAction(e-> {
            System.out.println(stackScene.size());
            stackScene.pop();
            window.setScene(stackScene.peek());
        });
        backButton.setMaxWidth(130);

        loadDatabasePanel.getChildren().addAll(infoLabel,backButton);
        loadDatabasePanel.setSpacing(20);
        loadDatabasePanel.setAlignment(Pos.CENTER);

        loadDatabaseLayout.setCenter(loadDatabasePanel);

        loadDatabaseScene=new Scene(loadDatabaseLayout,400,400);
    }

    Scene getScene(){
        return loadDatabaseScene;
    }

}
