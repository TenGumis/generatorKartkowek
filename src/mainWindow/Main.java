package mainWindow;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Stack;

public class Main extends Application {

    private Stage window;
    private Scene mainMenuScene,loadDatabaseScene,newDatabaseScene,testScene,aboutScene;
    private Stack<Scene> sceneStack=new Stack<>();
    private MainDatabase mainDatabase=null;
    @Override
    public void start(Stage primaryStage) {

        databaseInitialization();

        window=primaryStage;
        aboutScene=new AboutScene(window,sceneStack).getScene();
        loadDatabaseScene=new LoadDatabaseScene(window,sceneStack,mainDatabase).getScene();
        newDatabaseScene=new NewDatabaseScene(window,sceneStack).getScene();
        testScene=new TestScene(window,sceneStack).getScene();

        BorderPane mainMenuLayout=new BorderPane();
        VBox menuButtons=new VBox();

        Button loadDatabseButton=new Button("Load Database");
        loadDatabseButton.setOnAction(e-> {
            sceneStack.push(loadDatabaseScene);
            window.setScene(loadDatabaseScene);
        });
        loadDatabseButton.setMaxWidth(130);

        Button newDatabaseButton=new Button("New Database");
        newDatabaseButton.setOnAction(e-> {
            sceneStack.push(newDatabaseScene);
            window.setScene(newDatabaseScene);
        });
        newDatabaseButton.setMaxWidth(130);

        Button startTestButton=new Button("Start Test");
        startTestButton.setOnAction(e-> {
            sceneStack.push(testScene);
            window.setScene(testScene);
        });
        startTestButton.setMaxWidth(130);

        Button exitButton=new Button("Exit");
        exitButton.setOnAction(e-> {
            sceneStack.clear();
            window.close();
        });
        exitButton.setMaxWidth(130);

        Button aboutButton=new Button("About");
        aboutButton.setOnAction(e-> {
            sceneStack.push(aboutScene);
            window.setScene(aboutScene);
        });
        aboutButton.setMaxWidth(130);


        menuButtons.getChildren().addAll(newDatabaseButton,loadDatabseButton,startTestButton,aboutButton,exitButton);
        menuButtons.setAlignment(Pos.CENTER);
        menuButtons.setSpacing(10);
        mainMenuLayout.setCenter(menuButtons);

        mainMenuScene=new Scene(mainMenuLayout,400,400);
        sceneStack.push(mainMenuScene);
        window.setTitle("Generator kartkówek");
        window.setScene(mainMenuScene);
        window.show();
    }

    void databaseInitialization(){
        mainDatabase=new MainDatabase();
        try {
            BufferedReader br = new BufferedReader(
                    new FileReader("src\\databases\\importedDatabases.txt"));
            String line = br.readLine();
            while (line != null) {
                mainDatabase.insert(line,"src\\databases\\" + line + ".txt");
                line = br.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}