package games;

import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.util.Pair;
import mainWindow.MainDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DragAndDropGame implements Game {
    private int wordsQuantity;
    Map<String, String> gameContent;
    Map<String, String> testComponets;
    List<Node> nodes;

    double orgSceneX, orgSceneY;


    Scene scene;
    public DragAndDropGame() {
    }

    public Scene play(int wordsQuantity, List<String> categories, MainDatabase db) throws Exception {
        this.wordsQuantity = wordsQuantity;
        gameContent = new HashMap<>();
        nodes = new ArrayList<>();

        List<Pair<String , String> > pairsFromDB = new ArrayList<>();
        int numberOfCategories = categories.size();
        int k=1;
        for (String s : categories) {
            for (Pair<String, String> pair :
                    db.getSetOfElements(s, (k*wordsQuantity)/numberOfCategories-((k-1)*wordsQuantity)/numberOfCategories))
                pairsFromDB.add(pair);
            k++;
        }

        Pane pane = new Pane();

        for(Pair<String, String> pair : pairsFromDB) {
            GameItem item = new GameItem(pair);
            nodes.add(item.getNode1());
            nodes.add(item.getNode2());
            pane.getChildren().add(item.getNode1());
            pane.getChildren().add(item.getNode2());
        }


        // Scene
        Scene scene = new Scene(pane,800,800);
        scene.getStylesheets().add(getClass().getResource(".." + File.separator + "styleScheets" + File.separator + "trueFalseStyleScheet.css").toExternalForm());

        return scene;

    }


    public int score() {
        int result=0;
        return result;
    }

    public int getWordsQuantity() {
        return wordsQuantity;
    }

    public Scene getScene() {
        return scene;
    }

    class GameItem {
        private Label label1;
        private Label label2;
        GameItem(Pair<String,String> pair) {
            label1 = createDraggedAndDroppedLabel(200,200,pair.getKey());
            label2 = createDraggedAndDroppedLabel(300,300,pair.getValue());
        }
        public Node getNode1() {
            return label1;
        }
        public Node getNode2() { return label2; }

    }

    public Label createDraggedAndDroppedLabel(double x, double y, String s) {
        Label label = new Label(s);

        label.setLayoutX(x);
        label.setLayoutY(y);

        label.setCursor(Cursor.HAND);

        label.setOnMousePressed((t) -> {
            orgSceneX = t.getSceneX();
            orgSceneY = t.getSceneY();

            Label l = (Label) (t.getSource());
            l.toFront();
        });

        label.setOnMouseDragged((t) -> {
            double offsetX = t.getSceneX() - orgSceneX;
            double offsetY = t.getSceneY() - orgSceneY;

            Label l = (Label) (t.getSource());

            l.setLayoutX(l.getLayoutX() + offsetX);
            l.setLayoutY(l.getLayoutY() + offsetY);


            orgSceneX = t.getSceneX();
            orgSceneY = t.getSceneY();



        });

        label.setOnMouseReleased((t) ->
            checkBounds(label)
        );

        return label;
    }

    private void checkBounds(Node block) {
        boolean collisionDetected = false;
        for (Node static_bloc : nodes) {
            if (static_bloc != block) {
                block.setStyle("");

                if (block.getBoundsInParent().intersects(static_bloc.getBoundsInParent())) {
                    collisionDetected = true;
                }
            }
        }

        if (collisionDetected) {
            block.setStyle("-fx-background-color: red");
        } else {
            block.setStyle("");
        }
    }

}
