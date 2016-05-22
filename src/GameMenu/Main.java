package gameMenu;

import gameMenu.GameMenu;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;


public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    Stage window;
    Scene scene;

    @Override
    public void start(Stage primaryStage) throws Exception{
        window= primaryStage;
        window.setTitle("Menu");

        //Game Menu Button
        Button gameMenuButton = new Button("Games");
        gameMenuButton.setOnAction(event -> {
            GameMenu.display(window, scene);
        });



        // layout
        StackPane layout = new StackPane();
        layout.getChildren().add(gameMenuButton);


        scene = new Scene(layout, 150, 150);


        window.setScene(scene);
        window.show();
    }



}