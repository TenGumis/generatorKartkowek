package mainWindow;

import games.BasicGame;
import games.Game;
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
    private GridPane grid;
    private MainDatabase db;
    private static Map<String, Game> availableGames = new HashMap<>();
    private Map<String, GameHboxContent> choiceOfGameContent = new HashMap<>();
    List<CheckMenuItem> listOfCategories = new ArrayList<>();
    private List<Game> gamesCurrentlyUsed = new LinkedList<>();


    static {
        availableGames.put("BasicGame", new BasicGame());
        availableGames.put("Tralala", new BasicGame());
        availableGames.put("Bumschakalaka", new BasicGame());
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
                                new Integer(choiceOfGameContent.get(s).numberOfWords.getText()),
                                categoriesToPlay, db
                        ));
                    } catch (Exception e) {}
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
                    int result=0, wordsQuantity=0;
                    for (Game game : gamesCurrentlyUsed) {
                        result+= game.score();
                        wordsQuantity+= game.getWordsQuantity();
                    }

                    // Grade calculated in percents
                    double grade = (double)result / (double)wordsQuantity;


                    Stage resultWindow = new Stage();

                    // Result image
                    ImageView resultIcon;
                    if (grade>0.75)
                        resultIcon = new ImageView(new Image(GameCreator.class.getResourceAsStream("smallok.png")));
                    else
                        resultIcon = new ImageView(new Image(GameCreator.class.getResourceAsStream("smallnotok.png")));

                    // Result Label
                    Label resultLabel = new Label(""+result +"/"+wordsQuantity);
                    resultLabel.setStyle("-fx-text-fill: darkcyan; -fx-font-size: 25");

                    // Back button
                    Button back = new Button("OK...");
                    back.setAlignment(Pos.CENTER);
                    back.setOnAction(e -> resultWindow.close());

                    // Hbox for Label with image
                    HBox hBox = new HBox(20);
                    hBox.getChildren().addAll(resultIcon, resultLabel);
                    hBox.setAlignment(Pos.CENTER);

                    // Vbox to put it together with the back button
                    VBox vBox = new VBox(20);
                    vBox.setPadding(new Insets(10, 10, 10, 10));
                    vBox.setAlignment(Pos.CENTER);
                    vBox.getChildren().addAll(hBox, back);

                    resultWindow.setTitle("Result");
                    resultWindow.initModality(Modality.APPLICATION_MODAL);
                    resultWindow.setScene(new Scene(vBox));
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



        // Back Button
        Button back = new Button("Back to Main Menu");
        back.setOnAction(event -> {
            for (String s : availableGames.keySet())
                if (choiceOfGameContent.get(s).checkBox.isSelected())
                    choiceOfGameContent.get(s).checkBox.fire();
            stackScene.pop();
            window.setScene(stackScene.peek());
        });
        layout.getChildren().add(back);


        // set up the layout on current window
        layout.setAlignment(Pos.CENTER);

        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(10, 10, 10, 10));
        borderPane.setCenter(layout);
        borderPane.setBottom(back);
        borderPane.setAlignment(back, Pos.BOTTOM_RIGHT);
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


        GameHboxContent(String gameName, HBox box, MainDatabase db) {
            this.gameName=gameName;

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
    }

    static public MenuBar makeCategoriesMenu(MainDatabase db) {
        Menu categoriyMenu = new Menu("Categories");
        for (String s : db.getCategories())
            categoriyMenu.getItems().add(new CheckMenuItem(s));
        MenuBar menuBar = new MenuBar(categoriyMenu);
        return menuBar;
    }

    public Scene getScene() {
        return myScene;
    }
}

























