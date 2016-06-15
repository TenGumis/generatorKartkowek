package games;

import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import mainWindow.MainDatabase;

import java.io.File;
import java.util.*;

public class DragAndDropGame implements Game {
    private int wordsQuantity;
    private Map<String, String> gameContent;
    private Map<String, String> testComponets;
    private List<GameItem> gameItems;
    private List<Node> nodes;
    private Pane pane;
    private ScrollPane scrollPane;
    private Scene scene;

    private double orgSceneX, orgSceneY;


    public DragAndDropGame() {
        pane = new Pane();
        pane.setPadding(new Insets(10, 10, 10, 10));
        pane.setPrefWidth(1270);
        pane.setPrefHeight(710);

        scrollPane = new ScrollPane();
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        scrollPane.setPrefHeight(720);
        scrollPane.setPrefWidth(1280);
        scrollPane.setContent(pane);

        scene = new Scene(scrollPane);
        scene.getStylesheets().add(getClass().getResource(".."+ File.separator+"styleScheets"+File.separator+"gameStyleScheet.css").toExternalForm());
    }

    public Scene play(int wordsQuantity, List<String> categories, MainDatabase db) throws Exception {
        this.wordsQuantity = wordsQuantity;
        gameContent = new HashMap<>();

        gameItems = new ArrayList<>();
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


        for(Pair<String, String> pair : pairsFromDB) {
            Pair<GameItem,GameItem> gameItemPair = getPairGameItems(pair.getKey(),pair.getValue());
            gameItems.add(gameItemPair.getKey());
            gameItems.add(gameItemPair.getValue());
            nodes.add(gameItemPair.getKey().getNode());
            nodes.add(gameItemPair.getValue().getNode());
        }
        for(Node node : nodes) {
            pane.getChildren().add(node);
        }
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
        private Label label;
        private GameItem friend;
        GameItem(String s) {
            label = new Label(s);
            setNextRandomPosition();
            setDraggedAndDropped();
        }

        public Label getNode() {
            return label;
        }

        public void setFriend(GameItem friend) {
            this.friend = friend;
        }

        private class Position {
            private double x;
            private double y;
            public Position(double x, double y) {
                this.x = x;
                this.y = y;
            }
            public double getX() {
                return x;
            }
            public double getY() {
                return y;
            }
        }

        void setNextRandomPosition() {
            /***
             * it should be changed to size from Pane or Scene
             */
            double minX=10, minY=10, maxX=1270, maxY=710;

            Random random = new Random();
            do {
                label.setLayoutX(random.nextDouble()*(maxX-minX)+minX);
                label.setLayoutY(random.nextDouble()*(maxY-minY)+minY);
            }
            while(checkBounds(label));
        }

        public void setDraggedAndDropped() {
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

            label.setOnMouseReleased((t) -> {
                if (label.getBoundsInParent().intersects(friend.getNode().getBoundsInParent()))
                    addPoint(this, friend);
            });
        }
    }

    Pair<GameItem,GameItem> getPairGameItems(String s1, String s2) {
        GameItem g1 = new GameItem(s1);
        GameItem g2 = new GameItem(s2);
        g1.setFriend(g2);
        g2.setFriend(g1);
        return new Pair<>(g1,g2);
    }

    private void addPoint(GameItem me, GameItem friend) {
        System.out.println("ok");
    }


    private boolean checkBounds(Node block) {
        boolean collisionDetected = false;
        for (Node static_bloc : nodes) {
            if (static_bloc != block) {
                if (block.getBoundsInParent().intersects(static_bloc.getBoundsInParent())) {
                    collisionDetected = true;
                }
            }
        }
        return collisionDetected;
    }

}
