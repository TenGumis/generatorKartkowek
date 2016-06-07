package games;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import mainWindow.MainDatabase;

import java.io.File;
import java.util.*;

/**
 * Created by tengumis on 07.06.2016.
 */
public class TrueFalseGame implements Game {

    final static private Random rG = new Random();
    private Scene scene;
    Map<String, String> gameContent;
    Map<String, String> testComponets;
    List<TrueFalseGame.GameItem> matchings;
    private int wordsQuantity;

    public TrueFalseGame() {}

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
        List<Pair<String , String>> pairsFromDB = new ArrayList<>();
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
            s1=pair.getKey();
            s2=pair.getValue();


            Label label1 = new Label(s1);
            GridPane.setHalignment(label1, HPos.CENTER);
            GridPane.setConstraints(label1, 0, row);
            Label label2 = new Label(s2);
            GridPane.setHalignment(label2, HPos.CENTER);
            GridPane.setConstraints(label2, 1, row);
            row++;
            CheckBox trueBox=new CheckBox("true");
            CheckBox falseBox=new CheckBox("false");
            GridPane.setConstraints(trueBox,0,row);
            GridPane.setHalignment(trueBox, HPos.CENTER);
            GridPane.setConstraints(falseBox,1,row);
            GridPane.setHalignment(falseBox, HPos.CENTER);
            row++;
            grid.getChildren().addAll(label1, label2,trueBox,falseBox);
            matchings.add(new TrueFalseGame.GameItem(label1, label2,trueBox,falseBox));
            testComponets.put(s1, s2); // first value - translate from, second translate to
        }


        // ScrollPane
        ScrollPane scrollPane = new ScrollPane();
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        scrollPane.setPrefHeight(400);
        scrollPane.setPrefWidth(400);
        scrollPane.setContent(grid);
        scrollPane.setFitToWidth(true);

        // Scene
        Scene scene = new Scene(scrollPane);
        scene.getStylesheets().add(getClass().getResource(".."+ File.separator+"styleScheets"+File.separator+"TrueFalseStyleScheet.css").toExternalForm());

        return scene;
    }


    public int score() {
        int result=0;
        for (TrueFalseGame.GameItem gp : matchings) {
            if (testComponets.get(gp.label1.getText()).equals(gp.label2.getText())) {
                result++;
                gp.label2.setStyle("-fx-text-fill: green");
            }
            else {
                gp.label2.setStyle("-fx-text-fill: red");
                Tooltip tooltip = new Tooltip(testComponets.get(gp.label1.getText()));
                Tooltip.install(gp.label2, tooltip);
                gp.label2.setTooltip(tooltip);
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

    static class GameItem {
        Label label1;
        Label label2;
        CheckBox checkBox1;
        CheckBox checkBox2;
        GameItem() {}
        GameItem(Label l1, Label l2,CheckBox a,CheckBox b) {
            label1 = l1;
            label2 = l2;
            checkBox1=a;
            checkBox2=b;
            checkBox1.setOnAction(e->{
                if(checkBox2.isSelected() && checkBox1.isSelected()) checkBox2.setSelected(false);
            });
            checkBox2.setOnAction(e->{
                if(checkBox2.isSelected() && checkBox1.isSelected()) checkBox1.setSelected(false);
            });
        }
    }
}
