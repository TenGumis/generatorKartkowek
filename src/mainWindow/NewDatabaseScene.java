package mainWindow;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Created by pabia on 12.05.2016.
 */
public class NewDatabaseScene {
    Scene newDatabaseScene;

    public NewDatabaseScene(Stage window, Stack<Scene> stackScene){
        BorderPane newDatabaseLayout=new BorderPane();
        VBox newDatabasePanel=new VBox();

        Label infoLabel=new Label("Create databse:");
        infoLabel.setId("newDatabaseScene_infoLabel");
       // infoLabel.setMaxWidth(130);

        TextField databaseName = new TextField();
        databaseName.setPromptText("Database name");
        databaseName.setAlignment(Pos.CENTER);
        //databaseName.setMaxWidth(130);
        databaseName.setFocusTraversable(false);

        Button okButton = new Button("OK");
       // okButton.setMaxWidth(130);
        okButton.setAlignment(Pos.CENTER);
        okButton.setDefaultButton(true);
        okButton.setOnAction(event -> {
            if(databaseName.getText().length() > 0) {
                CreatingNewDatabaseScene creatingNewDatabaseScene =
                        new CreatingNewDatabaseScene(stackScene,databaseName.getText());
            }
            else
                showAlertBadDataBaseName();
        });

        Button backButton=new Button("Back");
        backButton.setOnAction(e-> {
            stackScene.pop();
            window.setScene(stackScene.peek());
        });
        //backButton.setMaxWidth(130);

        newDatabasePanel.getChildren().addAll(infoLabel,databaseName,okButton,backButton);
        newDatabasePanel.setSpacing(20);
        newDatabasePanel.setAlignment(Pos.CENTER);

        newDatabaseLayout.setCenter(newDatabasePanel);

        newDatabaseScene=new Scene(newDatabaseLayout,Main.sceneWidth,Main.sceneHeight);
        newDatabaseScene.getStylesheets().add(getClass().getResource(".."+ File.separator+"styleScheets"+File.separator+"newDatabaseStyleScheet.css").toExternalForm());

    }

    private void showAlertBadDataBaseName() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Błąd");
        alert.setHeaderText("Błędna nazwa bazy danych");
        alert.setContentText("Podaj poprawną nazwę bazy danych");
        alert.showAndWait();
    }



    Scene getScene(){
        return newDatabaseScene;
    }
}
