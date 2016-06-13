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
import mainWindow.Main;
import mainWindow.MainDatabase;

import java.io.File;
import java.util.*;
import java.util.concurrent.ExecutionException;

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
            this.wordsQuantity = wordsQuantity;
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

            List<Pair<String, String>> pairsFromDB = new ArrayList<>();
            List<Pair<String, String>> fakeRowsFromDB = new ArrayList<>();
            int numberOfCategories = categories.size();
            int k = 1;
            for (String s : categories) {
                try {
                    for (Pair<String, String> pair :
                            db.getSetOfElements(s, (k * wordsQuantity) / numberOfCategories - ((k - 1) * wordsQuantity) / numberOfCategories))
                        pairsFromDB.add(pair);
                    k++;
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            k = 1;
            for (String s : categories) {

                for (Pair<String, String> pair :
                        db.getSetOfElements(s, (k * wordsQuantity) / numberOfCategories - ((k - 1) * wordsQuantity) / numberOfCategories))
                    fakeRowsFromDB.add(pair);
                k++;

            }

            int row = 0;
            for (Pair<String, String> pair : pairsFromDB) {
                String s1, s2, s3;
                s1 = pair.getKey();
                s2 = pair.getValue();
                s3 = fakeRowsFromDB.get(row/2).getValue();
                boolean f = rG.nextBoolean();

                if (!f) {
                    String tmp = s2;
                    s2 = s3;
                    s3 = tmp;
                }


                Label label1 = new Label(s1);
                GridPane.setHalignment(label1, HPos.CENTER);
                GridPane.setConstraints(label1, 0, row);
                Label label2 = new Label(s2);
                GridPane.setHalignment(label2, HPos.CENTER);
                GridPane.setConstraints(label2, 1, row);
                row++;
                CheckBox trueBox = new CheckBox("true");
                CheckBox falseBox = new CheckBox("false");
                GridPane.setConstraints(trueBox, 0, row);
                GridPane.setHalignment(trueBox, HPos.CENTER);
                GridPane.setConstraints(falseBox, 1, row);
                GridPane.setHalignment(falseBox, HPos.CENTER);
                row++;
                grid.getChildren().addAll(label1, label2, trueBox, falseBox);
                matchings.add(new TrueFalseGame.GameItem(label1, label2, trueBox, falseBox, f, s3));
                testComponets.put(s1, s2); // first value - translate from, second translate to
            }


            // ScrollPane
            ScrollPane scrollPane = new ScrollPane();
            VBox.setVgrow(scrollPane, Priority.ALWAYS);
            scrollPane.setPrefHeight(Main.sceneHeight);
            scrollPane.setPrefWidth(Main.sceneWidth);
            scrollPane.setContent(grid);
            scrollPane.setFitToWidth(true);

            // Scene
            Scene scene = new Scene(scrollPane);
            scene.getStylesheets().add(getClass().getResource(".." + File.separator + "styleScheets" + File.separator + "trueFalseStyleScheet.css").toExternalForm());

            return scene;

    }


    public int score() {
        int result=0;
        for (TrueFalseGame.GameItem gp : matchings) {
            if (gp.answer==false && gp.falseBox.isSelected())  {
                result++;
                gp.falseBox.setStyle("-fx-text-fill: green");
                gp.label2.setText(gp.label2.getText()+" | it should be -> "+gp.correct);
            }
            else if (gp.answer==true && gp.trueBox.isSelected()){
                result++;
                gp.trueBox.setStyle("-fx-text-fill: green");
            }
            else if (gp.falseBox.isSelected()){
                gp.falseBox.setStyle("-fx-text-fill: red");
            }
            else if (gp.trueBox.isSelected()){
                gp.trueBox.setStyle("-fx-text-fill: red");
                gp.label2.setText(gp.label2.getText()+" | it should be -> "+gp.correct);
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
        CheckBox trueBox;
        CheckBox falseBox;
        boolean answer;
        String correct;
        GameItem() {}
        GameItem(Label l1, Label l2,CheckBox a,CheckBox b,boolean ans,String c) {
            label1 = l1;
            label2 = l2;
            trueBox =a;
            falseBox =b;
            answer=ans;
            correct=c;
            trueBox.setOnAction(e->{
                if(falseBox.isSelected() && trueBox.isSelected()) falseBox.setSelected(false);
            });
            falseBox.setOnAction(e->{
                if(falseBox.isSelected() && trueBox.isSelected()) trueBox.setSelected(false);
            });
        }
    }
}
