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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by filip on 5/21/16.
 */
public class BasicGame implements Game {
    public BasicGame() {}

    public void play(int wordsQuantity) {

        Map<String, String> gameContent = new HashMap<>();
        Stage window = new Stage();
        window.setTitle("Basic Game");
        window.initModality(Modality.APPLICATION_MODAL);

        // Grid Layout
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);

        // getting values for the game from database
        Map<String, String> testComponets = new HashMap<>();
        List<GamePair> matchings = new ArrayList<>();
        for (int i=0; i<wordsQuantity; i++) {
            Label label = new Label(""+i);
            grid.add(label, 0, i);
            TextField textField = new TextField();
            grid.add(textField, 1, i);
            matchings.add(new GamePair(label, textField));
            testComponets.put(""+i, ""+i*10); // first value - translate from, second translate to
        }
        //

        // Submit button
        Button submit = new Button("Submit");
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
            HBox resultHBox= new HBox();
            resultHBox.getChildren().addAll(labelOfResult, resultIcon);
            resultHBox.setSpacing(20);
            resultHBox.setAlignment(Pos.CENTER);
            GridPane.setHalignment(resultHBox, HPos.CENTER);
            grid.add(resultHBox, 0, wordsQuantity+2, 2, 1);

            // Back to GameMenu button  --- needs to be properly bound, to resize the window / needs scroll implementation
            Button backToGameMenu = new Button("Back to GameMenu");
            backToGameMenu.setOnAction(event1 -> window.close());
            GridPane.setHalignment(backToGameMenu, HPos.CENTER);
            grid.add(backToGameMenu, 0, wordsQuantity+3, 2, 1);
        });
        GridPane.setHalignment(submit, HPos.CENTER);
        grid.add(submit, 0, wordsQuantity+1, 2, 1);

        //Quit button
        Button quit = new Button("Quit");
        quit.setOnAction(event -> window.close());

        // Group
        Group root = new Group();

        // Scene
        Scene scene = new Scene(root, 250, 300);

        // Scroll Bar
        ScrollBar sb = new ScrollBar();
        sb.setLayoutX(scene.getWidth()-sb.getWidth());
        sb.setMin(0);
        sb.setMax(30 * wordsQuantity);
        sb.setPrefHeight(300);
        sb.setOrientation(Orientation.VERTICAL);


        sb.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                grid.setLayoutY(-new_val.doubleValue());
            }
        });
        grid.getChildren().add(sb);

        root.getChildren().addAll(grid, sb);

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
