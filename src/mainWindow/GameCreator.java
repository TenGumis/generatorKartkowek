package mainWindow;

import games.BasicGame;
import games.Game;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.*;

/**
 * Created by filip on 5/31/16.
 */
public class GameCreator {
    private Scene myScene;
    private MainDatabase db;
    private List<String> selectedCategories;
    private static Map<String, Game> availableGames = new HashMap<>();
    List<CheckMenuItem> listOfCategories = new ArrayList<>();

    private Menu categoryChoice = new Menu("Choose categories");
    private MenuBar mb = new MenuBar();

    static {
        availableGames.put("BasicGame", new BasicGame());
    }

    public GameCreator(Stage window, Stack<Scene> stackScene, MainDatabase db) {
        this.db=db;
        selectedCategories= new ArrayList<>();

        //Label for a ChoiceBox
        Label choiceLabel = new Label("Choose a game: ");

        // Set The ChoiceBox
        ChoiceBox<String> chooseAGame = new ChoiceBox<>();
        chooseAGame.getItems().addAll(availableGames.keySet());
        chooseAGame.setValue("BasicGame");

        // choice HBox line
        HBox choiceLine = new HBox();
        choiceLine.setSpacing(15);
        choiceLine.setAlignment(Pos.CENTER);
        choiceLine.getChildren().addAll(choiceLabel, chooseAGame);


        // Category choice
        this.refreshCategories();


        // Number of words to use in the game
        TextField numberOfWords = new TextField();
        numberOfWords.setPromptText("Number of words");
        numberOfWords.setMaxWidth(150);


        // Start game button
        Button startGame = new Button("Let the fun begin...");
        startGame.setOnAction(event -> {
            try{
                int g =new Integer(numberOfWords.getText());
                if (g<1) throw new Exception();
                List<String> categoriesToPlay = new ArrayList<String>();
                for (String s : selectedCategories)
                    categoriesToPlay.add(s);
                availableGames.get(chooseAGame.getValue()).play(g, categoriesToPlay, db);
                for (CheckMenuItem mi : listOfCategories)
                    mi.setSelected(false);
            } catch (Exception e) {
                AlertBox.display("Number of words must be of positive integer type.");
            }
        });

        // Back Button
        Button back = new Button("Back");
        back.setOnAction(event -> {
            stackScene.pop();
            window.setScene(stackScene.peek());
            for (CheckMenuItem mi : listOfCategories)
                mi.setSelected(false);
        });


        // VBox layout
        VBox vBox = new VBox();
        vBox.setSpacing(15);
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(choiceLine, mb, numberOfWords, startGame, back);


        // to wrap it all up nicely
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(vBox);


        //Scene
        myScene=new Scene(borderPane, 400, 400);

    }


    public void refreshCategories() {
        listOfCategories.clear();
        categoryChoice.getItems().clear();
        for (String s : db.getCategories()) {
            CheckMenuItem checkMenuItem = new CheckMenuItem(s);
            checkMenuItem.setOnAction(event -> {
                if (checkMenuItem.isSelected())
                    selectedCategories.add(s);
                else
                    selectedCategories.remove(s);
            });
            categoryChoice.getItems().add(checkMenuItem);
            listOfCategories.add(checkMenuItem);
        }

        //MenuBar mb = new MenuBar();
        mb = new MenuBar();
        mb.setMaxWidth(156);
        mb.getMenus().add(categoryChoice);
    }

    public Scene getScene() {
        return myScene;
    }
}
