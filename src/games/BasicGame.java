package games;

import games.Game;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;
import mainWindow.MainDatabase;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by filip on 5/21/16.
 */
public class BasicGame implements Game {
    final static private Random rG = new Random();
    public BasicGame() {}

    public void play(int wordsQuantity, List<String> categories, MainDatabase db) throws Exception {

        Map<String, String> gameContent = new HashMap<>();
        Stage window = new Stage();
        window.setTitle("Basic Game");
        window.initModality(Modality.APPLICATION_MODAL);

        //Grid Layout

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setAlignment(Pos.CENTER);


        // getting values for the game from database
        Map<String, String> testComponets = new HashMap<>();
        List<GamePair> matchings = new ArrayList<>();
        List<Pair<String , String> > pairsFromDB = new ArrayList<>();
        int numberOfCategories = categories.size();
        int k=1;
        for (String s : categories) {
            for (Pair<String, String> pair :
                    db.getSetOfElements(s, (k*wordsQuantity)/numberOfCategories-((k-1)*wordsQuantity)/numberOfCategories))
                pairsFromDB.add(pair);
            k++;
        }

        int row=0;

        for (Pair<String, String > pair : pairsFromDB) {
            String s1, s2;
            if (rG.nextBoolean()) {
                s1=pair.getKey(); s2=pair.getValue();
            }
            else {
                s1=pair.getValue(); s2=pair.getKey();
            }

            Label label = new Label(s1);
            GridPane.setConstraints(label, 0, row);
            TextField textField = new TextField();
            GridPane.setConstraints(textField, 1, row++);
            grid.getChildren().addAll(label, textField);
            matchings.add(new GamePair(label, textField));
            testComponets.put(s1, s2); // first value - translate from, second translate to
        }
        //

        // Submit button
        Button submit = new Button("Submit");
        // Submit action
        submit.setOnAction(event -> {
            int result=0;
            for (GamePair gp : matchings) {
                if (testComponets.get(gp.label.getText()).equals(gp.textField.getText())) {
                    result++;
                    gp.textField.setStyle("-fx-text-fill: green");
                }
                else
                    gp.textField.setStyle("-fx-text-fill: red");

            }
            grid.getChildren().remove(submit);

            // Result label
            Label labelOfResult = new Label();
            labelOfResult.setText(""+result+"/"+wordsQuantity);
            labelOfResult.setTextFill(Color.RED);
            labelOfResult.setStyle("-fx-text-fill: lightseagreen; -fx-font-size: 20;");

            // Result icon
            double grade = (double)result / (double)wordsQuantity;
            ImageView resultIcon;
            if (grade>0.75)
                resultIcon = new ImageView(new Image(BasicGame.class.getResourceAsStream("smallok.png")));
            else
                resultIcon = new ImageView(new Image(BasicGame.class.getResourceAsStream("smallnotok.png")));

            // Result HBox
            HBox resultHBox= new HBox(20);
            resultHBox.getChildren().addAll(labelOfResult, resultIcon);
            resultHBox.setAlignment(Pos.CENTER);
            GridPane.setConstraints(resultHBox, 0, wordsQuantity+1, 2, 1);
            GridPane.setHalignment(resultHBox, HPos.CENTER);
            grid.getChildren().add(resultHBox);

            // Back to gameMenu button  --- needs to be properly bound, to resize the window / needs scroll implementation
            Button backToGameMenu = new Button("Back to Game Menu");
            backToGameMenu.setOnAction(event1 -> window.close());
            GridPane.setConstraints(backToGameMenu, 0, wordsQuantity+2, 2, 1);
            GridPane.setHalignment(backToGameMenu, HPos.CENTER);
            grid.getChildren().add(backToGameMenu);
        });
        GridPane.setConstraints(submit, 0, row, 2, 1);
        GridPane.setHalignment(submit, HPos.CENTER);
        grid.getChildren().add(submit);


        //Quit button
        Button quit = new Button("Quit");
        quit.setOnAction(event -> window.close());

        // ScrollPane
        ScrollPane scrollPane = new ScrollPane();
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        scrollPane.setPrefHeight(500);
        scrollPane.setPrefWidth(400);
        scrollPane.setContent(grid);
        //scrollPane.setFitToWidth(true);

        // Scene
        Scene scene = new Scene(scrollPane);

        window.setScene(scene);
        window.showAndWait();
    }


    static class GamePair {
        Label label;
        TextField textField;
        GamePair() {}
        GamePair(Label l, TextField tf) {
            label = l; textField = tf;
        }
    }
}
