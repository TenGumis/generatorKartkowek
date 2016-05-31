package gameMenu;

import games.*;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javafx.scene.control.Button;
import games.BasicGame;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by filip on 5/21/16.
 */
public class GameMenu {
    static Stage window;
    static Scene scene;
    static Scene mainScene;
    static Map<String, Game> availableGames = new HashMap<>();

    static {
        availableGames.put("BasicGame", new BasicGame());
    }

    static {
        // Setting up GridPane
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setHgap(8);
        grid.setVgap(10);

        // Set The ChoiceBox
        ChoiceBox<String> chooseAGame = new ChoiceBox<>();
        chooseAGame.getItems().addAll(
                "BasicGame"
        );
        chooseAGame.setValue("BasicGame");
        grid.add(chooseAGame, 1, 0);
        GridPane.setHalignment(chooseAGame, HPos.CENTER);

        //Label for ChoiceBox
        Label label = new Label("Choose a game: ");
        grid.add(label, 0 , 0);
        GridPane.setHalignment(label, HPos.CENTER);

        // Number of words to use in the game
        TextField numberOfWords = new TextField();
        numberOfWords.setPromptText("Number of words");
        numberOfWords.setMaxWidth(150);
        grid.add(numberOfWords, 0, 1);


        // Start game button
        Button startGame = new Button("Let the fun begin...");
        startGame.setOnAction(event -> {
            try{
                availableGames.get(chooseAGame.getValue()).play(new Integer(numberOfWords.getText()));
            } catch (Exception e) {
                AlertBox.display("Number of words must be of integer type.");
            }
        });
        grid.add(startGame, 1, 1);

        // Back Button
        Button back = new Button("Back");
        back.setOnAction(event -> {
            window.setScene(mainScene);
            window.setTitle("Menu");
        });
        StackPane sp = new StackPane();
        sp.getChildren().add(back);
        grid.add(sp, 0, 2, 2, 1);


        scene = new Scene(grid);
    }

    public static void display(Stage primaryStage, Scene currentScene) {
        primaryStage.setScene(scene);
        mainScene = currentScene;
        window=primaryStage;
        window.setTitle("Game Menu");
    }
}
