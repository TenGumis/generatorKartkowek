package games;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import mainWindow.MainDatabase;

import java.io.File;
import java.util.*;

/**
 * Created by filip on 5/21/16.
 */
public class BasicGame implements Game {
    final static private Random rG = new Random();
    private Scene scene;
    Map<String, String> gameContent;
    Map<String, String> testComponets;
    List<GamePair> matchings;
    private int wordsQuantity;

    public BasicGame() {}

    public Scene play(int wordsQuantity, List<String> categories, MainDatabase db) throws Exception {
        this.wordsQuantity= wordsQuantity;
        gameContent = new HashMap<>();

        //Grid Layout

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setAlignment(Pos.CENTER);


        // getting values for the game from database
        testComponets = new HashMap<>();
        matchings = new ArrayList<>();
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
            GridPane.setHalignment(label, HPos.CENTER);
            GridPane.setConstraints(label, 0, row);
            TextField textField = new TextField();
            GridPane.setConstraints(textField, 1, row++);
            grid.getChildren().addAll(label, textField);
            matchings.add(new GamePair(label, textField));
            testComponets.put(s1, s2); // first value - translate from, second translate to
        }


        // ScrollPane
        ScrollPane scrollPane = new ScrollPane();
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        scrollPane.setPrefHeight(720);
        scrollPane.setPrefWidth(1280);
        scrollPane.setContent(grid);
        scrollPane.setFitToWidth(true);


        // Scene
        Scene scene = new Scene(scrollPane);
        scene.getStylesheets().add(getClass().getResource(".."+ File.separator+"styleScheets"+File.separator+"gameStyleScheet.css").toExternalForm());

        return scene;
    }


    public int score() {
        int result=0;
        for (GamePair gp : matchings) {
            if (testComponets.get(gp.label.getText()).equals(gp.textField.getText())) {
                result++;
                gp.textField.setStyle("-fx-text-fill: green");
            }
            else {
                gp.textField.setStyle("-fx-text-fill: red");
                Tooltip tooltip = new Tooltip(testComponets.get(gp.label.getText()));
                Tooltip.install(gp.textField, tooltip);
                gp.textField.setTooltip(tooltip);
            }
        }
        return result;
    }

    public int getWordsQuantity() {
        return wordsQuantity;
    }

    public Scene getScene() {
        return scene;
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
