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
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Pair;
import mainWindow.MainDatabase;

import java.io.File;
import java.util.*;



public class DragAndDropGame implements Game {
    private int wordsQuantity;
    private List<GameItem> gameItems;
    private List<Node> nodes;
    private Pane pane;
    private ScrollPane scrollPane;
    private Scene scene;
    private int score;
    private int visibles;

    private Media sound = new Media(new File("src"+File.separator+"img"+File.separator+"mario_coin.mp3").toURI().toString());

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

        gameItems = new ArrayList<>();
        nodes = new ArrayList<>();

        Collection<Pair<String , String> > pairsFromDB = new HashSet<>();
        int numberOfCategories = categories.size();
        int k=1;
        for (String s : categories) {
            for (Pair<String, String> pair :
                    db.getSetOfElements(s, (k*wordsQuantity)/numberOfCategories-((k-1)*wordsQuantity)/numberOfCategories)) {
                pairsFromDB.add(pair);
            }
            k++;
        }

        score = 0;
        visibles = 0;
        for(Pair<String, String> pair : pairsFromDB) {
            GameItem g1 = new GameItem(pair.getKey());
            gameItems.add(g1);
            nodes.add(g1.getNode());
            pane.getChildren().add(g1.getNode());

            GameItem g2 = new GameItem(pair.getValue());
            gameItems.add(g2);
            nodes.add(g2.getNode());
            pane.getChildren().add(g2.getNode());

            g1.setFriend(g2);
            g2.setFriend(g1);
            visibles++;
        }
        return scene;
    }


    public int score() {
        return score;
    }

    public int getWordsQuantity() {
        return gameItems.size()/2;
    }

    public Scene getScene() {
        return scene;
    }

    class GameItem {
        private Label label;
        private GameItem friend;
        private boolean visible = true;
        GameItem(String s) {
            label = new Label(s);
            label.setStyle("-fx-font-size: 16pt");
            setNextRandomPosition();
            setDraggedAndDropped();
        }

        public Label getNode() {
            return label;
        }

        public void setFriend(GameItem friend) {
            this.friend = friend;
        }

        void setNextRandomPosition() {
            /***
             * it should be changed to size which is got from Pane or Scene
             */
            double minX=0, minY=0, maxX=1240, maxY=660;

            Random random = new Random();
            do {
                label.setLayoutX(random.nextDouble()*(maxX-minX)+minX);
                label.setLayoutY(random.nextDouble()*(maxY-minY)+minY);
            } while(checkBounds(label));
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
                if (label.getBoundsInParent().intersects(friend.getNode().getBoundsInParent())
                        && this.visible && friend.visible)
                    addPoint(this, friend);
            });
        }
    }

    private void addPoint(GameItem me, GameItem friend) {
        me.getNode().setVisible(false);
        friend.getNode().setVisible(false);
        me.visible = false;
        friend.visible = false;
        score++;
        visibles--;
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();

        if(visibles == 0) {
            // end of game
        }

    }


    private boolean checkBounds(Node block) {
        boolean collisionDetected = false;
        for (Node static_block : nodes) {
            if (static_block != block) {
                if (block.getBoundsInParent().intersects(static_block.getBoundsInParent())) {
                    collisionDetected = true;
                }
            }
        }
        return collisionDetected;
    }

}
