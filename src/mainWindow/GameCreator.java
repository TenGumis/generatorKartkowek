package mainWindow;

import games.BasicGame;
import games.Game;
import games.TrueFalseGame;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.util.*;

/**
 * Created by filip on 6/6/16.
 */
public class GameCreator {
    private Stage window;
    private VBox layout = new VBox();
    private Scene myScene = new Scene(layout, Main.sceneWidth, Main.sceneHeight);
    private MainDatabase db;
    private static Map<String, Game> availableGames = new HashMap<>();
    private Map<String, GameHboxContent> choiceOfGameContent = new HashMap<>();
    private List<Game> gamesCurrentlyUsed = new LinkedList<>();
    private BorderPane borderPane=null;


    static {
        availableGames.put("BasicGame", new BasicGame());
        availableGames.put("Tralala", new BasicGame());
        availableGames.put("Bumschakalaka", new BasicGame());
        availableGames.put("True or False",new TrueFalseGame());
    }

    public GameCreator(Stage window, Stack<Scene> stackScene, MainDatabase db) {
        this.window = window;
        layout.setPadding(new Insets(20, 15, 20, 15));
        layout.setSpacing(15);
        this.db=db;
        window.setTitle("Game Menu");

        // Top Label
        Label choiceLabel = new Label("Choose a game:");
        choiceLabel.setStyle("-fx-text-fill: lightseagreen; -fx-font-size: 25;");
        choiceLabel.setAlignment(Pos.CENTER);
        layout.getChildren().add(choiceLabel);

        // Adding game choosing panel
        for (String game : availableGames.keySet()) {
            HBox hBox = new HBox(15);
            hBox.setAlignment(Pos.CENTER);
            choiceOfGameContent.put(game, new GameHboxContent(game, hBox, db));
            layout.getChildren().add(hBox);
        }

        // Start Button
        Button start = new Button("Let the fun begin...");
        Scene currentScene;
        start.setOnAction(event -> {
            gamesCurrentlyUsed.clear();
            boolean succesFullCreation = true;
            Stack<Scene> nextGames = new Stack<>(), previousGames = new Stack<>();

            for (String s : choiceOfGameContent.keySet()) {
                if (choiceOfGameContent.get(s).checkBox.isSelected()) {
                    gamesCurrentlyUsed.add(availableGames.get(s));

                    ArrayList<String> categoriesToPlay = new ArrayList<>();
                    Menu m=null;
                    for (Menu menu : choiceOfGameContent.get(s).categories.getMenus())
                        m=menu;
                    for (MenuItem menuItem : m.getItems()) {
                        CheckMenuItem checkMenuItem = (CheckMenuItem) menuItem;
                        if (checkMenuItem.isSelected())
                            categoriesToPlay.add(checkMenuItem.getText());
                    }
                    succesFullCreation = InputValidation.validate(() -> {
                        if (categoriesToPlay.isEmpty())
                            return false;
                        else
                            return true;
                    }, "At least one category for each selected game must be chosen.");
                    if (!succesFullCreation)
                        return;
                    succesFullCreation = InputValidation.validate(() -> {
                        try {
                            int a = new Integer(choiceOfGameContent.get(s).numberOfWords.getText());
                            if (a<=0)
                                return false;
                        }
                        catch (Exception e) { return false; }
                        return true;
                    }, "Number of words must be a positive integer.");
                    if (!succesFullCreation)
                        return;
                    try {
                        nextGames.push(availableGames.get(s).play(
                                new Integer(choiceOfGameContent.get(s).numberOfWords.getText()), categoriesToPlay, db));
                    } catch (Exception e) {  }
                }
            }
            succesFullCreation = InputValidation.validate(() -> {return !gamesCurrentlyUsed.isEmpty();},
                    "You must choose at least one game.");

            if (succesFullCreation) {
                for (String s : choiceOfGameContent.keySet()) {
                    if (choiceOfGameContent.get(s).checkBox.isSelected()) {
                        Menu m = null;
                        for (Menu menu : choiceOfGameContent.get(s).categories.getMenus())
                            m = menu;
                        for (MenuItem menuItem : m.getItems()) {
                            CheckMenuItem checkMenuItem = (CheckMenuItem) menuItem;
                            checkMenuItem.setSelected(false);
                        }
                        choiceOfGameContent.get(s).numberOfWords.setText("");
                        choiceOfGameContent.get(s).checkBox.fire();
                    }
                }


                while (!nextGames.isEmpty()) {
                    Scene current = nextGames.pop();
                    AnchorPane anchor = new AnchorPane();
                        AnchorPane.setBottomAnchor(current.getRoot(), 35.0);
                        AnchorPane.setTopAnchor(current.getRoot(), 0.1);
                        AnchorPane.setLeftAnchor(current.getRoot(), 0.1);
                        AnchorPane.setRightAnchor(current.getRoot(), 0.1);
                    anchor.getChildren().add(current.getRoot());
                    Button next = new Button("Next"), prev = new Button("Previous");
                    AnchorPane.setBottomAnchor(next, 5.0);
                    AnchorPane.setRightAnchor(next, 20.0);
                    AnchorPane.setBottomAnchor(prev, 5.0);
                    AnchorPane.setLeftAnchor(prev, 5.0);

                    Button quit = new Button("Quit");


                    quit.setOnAction(event1 -> window.setScene(stackScene.peek()));
                        AnchorPane.setRightAnchor(quit, 20.0);
                        AnchorPane.setTopAnchor(quit, 5.0);
                    anchor.getChildren().add(quit);

                    next.setOnAction(event1 -> {
                        previousGames.push(nextGames.peek());
                        nextGames.pop();
                        window.setScene(nextGames.peek());
                    });
                    prev.setOnAction(event1 -> {
                        nextGames.push(previousGames.peek());
                        previousGames.pop();
                        window.setScene(nextGames.peek());
                    });

                    if (!nextGames.isEmpty()) {
                        anchor.getChildren().add(next);
                    }
                    if (!previousGames.isEmpty()) {
                        anchor.getChildren().add(prev);
                    }
                    current.setRoot(anchor);
                    previousGames.push(current);
                }

                // Submit button on last page
                Button submit = new Button("Submit");
                submit.setOnAction(event1 -> {
                    List<Scene> sceneList = new LinkedList<>();
                    sceneList.addAll(nextGames); sceneList.addAll(previousGames);
                    for (Scene scene : sceneList) {
                        //Back Button
                        Button back = new Button("Back to Menu");
                        back.setAlignment(Pos.CENTER);
                        AnchorPane.setBottomAnchor(back, 5.0);
                        AnchorPane.setLeftAnchor(back, 100.0);
                        AnchorPane.setRightAnchor(back, 105.0);
                        back.setOnAction(event2 -> {
                            window.setScene(stackScene.peek());
                        });
                        ((AnchorPane)scene.getRoot()).getChildren().add(back);
                    }


                    Stage resultWindow = new Stage();

                    // Grid to put it together with the back button
                    GridPane grid = new GridPane();
                    grid.setPadding(new Insets(50, 50, 50, 50));
                    grid.setVgap(10);

                    int result=0, wordsQuantity=0;
                    int row=0;
                    //if (gamesCurrentlyUsed.size()>1)
                        for (Game game : gamesCurrentlyUsed) {
                            String name="";
                            for (String s : availableGames.keySet())
                                if (availableGames.get(s).equals(game))
                                    name=s;
                            result+= game.score();
                            wordsQuantity+= game.getWordsQuantity();

                            // Description Label
                            Label descriptionLabel = new Label(name + ": ");
                            descriptionLabel.setStyle("-fx-font-size: 25; -fx-text-fill: blueviolet");
                            GridPane.setConstraints(descriptionLabel, 0, row);
                            GridPane.setHalignment(descriptionLabel, HPos.CENTER);

                            //Game Score
                            Label scoreLabel = new Label(""+ game.score() + " / " + game.getWordsQuantity());
                            scoreLabel.setStyle("-fx-font-size: 25; -fx-text-fill: #8d87ff");
                            GridPane.setConstraints(scoreLabel, 1, row++);
                            GridPane.setHalignment(scoreLabel, HPos.CENTER);

                            grid.getChildren().addAll(descriptionLabel, scoreLabel);
                        }
                    row+=2;

                    // Overall Label
                    Label overall = new Label("Overall: ");
                    overall.setStyle("-fx-font-size: 30; -fx-text-fill: red");
                    GridPane.setConstraints(overall, 0, row++, 2, 1);
                    GridPane.setHalignment(overall, HPos.CENTER);

                    //Overall Score
                    Label overallScoreLabel = new Label(""+result+ " / " + wordsQuantity);
                    overallScoreLabel.setStyle("-fx-font-size: 30; -fx-text-fill: red");

                    // Image
                    ImageView imageView = new ImageView();
                    Image image = null;
                    double grade = (double)result / (double) wordsQuantity;

                    if (grade>0.75) {
                        image = new Image(GameCreator.class.getResourceAsStream("smallok.png"));
                    }
                    else
                        image = new Image(GameCreator.class.getResourceAsStream("smallok.png"));

                    imageView.setImage(image);

                    // Hbox
                    HBox hBox = new HBox(30);
                    hBox.setAlignment(Pos.CENTER);
                    GridPane.setConstraints(hBox, 0, row++, 2, 1);
                    GridPane.setHalignment(hBox, HPos.CENTER);
                    hBox.getChildren().addAll(overallScoreLabel, imageView);

                    // Back button
                    Button back = new Button("OK...");
                    back.setAlignment(Pos.CENTER);
                    back.setOnAction(e -> resultWindow.close());
                    GridPane.setConstraints(back, 0, ++row, 2, 1);
                    GridPane.setHalignment(back, HPos.CENTER);
                    back.setMinHeight(30); back.setMinWidth(100);
                    grid.setAlignment(Pos.CENTER);

                    grid.getChildren().addAll(overall, hBox, back);
                    resultWindow.setTitle("Result");
                    resultWindow.initModality(Modality.APPLICATION_MODAL);
                    resultWindow.setScene(new Scene(grid));
                    resultWindow.showAndWait();
                });
                AnchorPane.setRightAnchor(submit, 20.0);
                AnchorPane.setBottomAnchor(submit, 5.0);
                ((AnchorPane)previousGames.peek().getRoot()).getChildren().add(submit);

                while (!previousGames.isEmpty())
                    nextGames.push(previousGames.pop());

                window.setScene(nextGames.peek());
            }
        });
        layout.getChildren().add(start);


        // set up the layout on current window
        layout.setAlignment(Pos.CENTER);

        borderPane = new BorderPane();
        borderPane.setPadding(new Insets(10, 10, 10, 10));
        borderPane.setCenter(layout);
        //borderPane.setBottom(back);
        //borderPane.setAlignment(back, Pos.BOTTOM_RIGHT);
        myScene.setRoot(borderPane);
        myScene.getStylesheets().add(getClass().getResource(".."+ File.separator+"styleScheets"+File.separator+"gameStyleScheet.css").toExternalForm());



        stackScene.push(myScene);
        window.setScene(myScene);
    }


    static class GameHboxContent {
        String gameName;
        Label gameNameLabel;
        CheckBox checkBox;
        TextField numberOfWords;
        MenuBar categories;
        MainDatabase db;


        GameHboxContent(String gameName, HBox box, MainDatabase db) {
            this.gameName=gameName;
            this.db=db;

            // Label with Game Name
            this.gameNameLabel = new Label(gameName);

            // Checkbox for ticking games to be played
            this.checkBox = new CheckBox();
            this.checkBox.setTooltip(new Tooltip(gameName));
            checkBox.setSelected(false);
            this.checkBox.setOnAction(event -> {
                if (this.checkBox.isSelected()) {
                    box.getChildren().addAll(this.numberOfWords, this.categories);
                    box.getChildren().remove(this.gameNameLabel);
                }
                else {
                    box.getChildren().clear();
                    box.getChildren().addAll(this.gameNameLabel, this.checkBox);
                }
            });

            box.getChildren().addAll(this.gameNameLabel, this.checkBox);

            // TextField for words number typing
            this.numberOfWords = new TextField();
            this.numberOfWords.setPromptText("Number Of Words");

            // Menu with categooryChoice
            categories = makeCategoriesMenu(db);
        }

        void hboxContentUpdate() {
            if (checkBox.isSelected())
                checkBox.fire();
            categories = makeCategoriesMenu(db);
        }
    }

    static public MenuBar makeCategoriesMenu(MainDatabase db) {
        Menu categoriyMenu = new Menu("Categories");
        for (String s : db.getCategories())
            categoriyMenu.getItems().add(new CheckMenuItem(s));
        MenuBar menuBar = new MenuBar(categoriyMenu);
        return menuBar;
    }

    public void updateCategories() {
        for (GameHboxContent gameHboxContent : choiceOfGameContent.values()) {
            gameHboxContent.hboxContentUpdate();
        }
    }

    public Scene getScene() {
        return myScene;
    }

    public BorderPane getBorder() {return borderPane;}
}

























