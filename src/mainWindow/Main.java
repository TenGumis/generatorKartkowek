package mainWindow;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Pair;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Stack;

public class Main extends Application {


    public static int sceneWidth=1280,sceneHeight=720;
    private Stage window,aboutStage=null;;
    private Scene mainMenuScene,loadDatabaseScene,newDatabaseScene,testScene;
    private GameCreator gameCreation;
    private Stack<Scene> sceneStack=new Stack<>();
    private MainDatabase mainDatabase=null;
    @Override
    public void start(Stage primaryStage) {

        databaseInitialization();

        window=primaryStage;
        newDatabaseScene=new NewDatabaseScene(window,sceneStack).getScene();
        testScene=new TestScene(window,sceneStack).getScene();
        gameCreation = new GameCreator(window, sceneStack, mainDatabase);


        //Menu Bar initialization
        MenuBar menuBar=new MenuBar();
        Menu file=new Menu("File");
        MenuItem newDatabase=new MenuItem("New database");
        MenuItem loadDatabase=new MenuItem("Load database");
        MenuItem exit=new MenuItem("Exit");
        SeparatorMenuItem separator=new SeparatorMenuItem();
        file.getItems().addAll(newDatabase,loadDatabase,separator,exit);

        Menu help=new Menu("Help");
        MenuItem about=new MenuItem("About");
        help.getItems().addAll(about);

        menuBar.getMenus().addAll(file,help);

        about.setOnAction(e->{
            aboutStage=new AboutScene().getWindow();
            aboutStage.show();
        });

        exit.setOnAction(e->{
            sceneStack.clear();
            if(aboutStage!=null) aboutStage.close();
            if(window!=null) window.close();
        });

        BorderPane mainMenuLayout=new BorderPane();
        VBox menuButtons=new VBox();

        Button loadDatabseButton=new Button("Load Database");
        loadDatabseButton.setOnAction(e-> {
            loadDatabaseScene=new LoadDatabaseScene(window,sceneStack,mainDatabase).getScene();
            sceneStack.push(loadDatabaseScene);
            window.setScene(loadDatabaseScene);
        });
        //loadDatabseButton.setMaxWidth(130);

        Button newDatabaseButton=new Button("New Database");
        newDatabaseButton.setOnAction(e-> {
            sceneStack.push(newDatabaseScene);
            window.setScene(newDatabaseScene);
        });
        //newDatabaseButton.setMaxWidth(130);

        Button startTestButton=new Button("Make a Test");
        startTestButton.setOnAction(e-> {
            gameCreation.updateCategories();
            sceneStack.push(gameCreation.getScene());
            window.setScene(gameCreation.getScene());
        });
        //startTestButton.setMaxWidth(130);

        Button exitButton=new Button("Exit");
        exitButton.setOnAction(e-> {
            sceneStack.clear();
            window.close();
        });
        //exitButton.setMaxWidth(130);

        menuButtons.getChildren().addAll(newDatabaseButton,loadDatabseButton,startTestButton,exitButton);
        menuButtons.setAlignment(Pos.CENTER);
        menuButtons.setSpacing(20);
        mainMenuLayout.setCenter(menuButtons);
        mainMenuLayout.setTop(menuBar);

        mainMenuScene=new Scene(mainMenuLayout,sceneWidth,sceneHeight);
        mainMenuScene.getStylesheets().add(getClass().getResource(".."+ File.separator+"styleScheets"+File.separator+"mainStyleScheet.css").toExternalForm());
        sceneStack.push(mainMenuScene);
        window.setTitle("Generator kartkówek");
        window.setScene(mainMenuScene);
        window.show();
    }

    void databaseInitialization(){
        mainDatabase=new MainDatabase();
        try {
            if(!Files.exists(Paths.get("src"+ File.separator+"databases"+ File.separator+"importedDatabases.txt"))){
                System.out.println("Nie ma pliku");
                Files.createFile(Paths.get("src"+ File.separator+"databases"+ File.separator+"importedDatabases.txt"));
            }
            BufferedReader br = new BufferedReader(
                    new FileReader("src"+ File.separator+"databases"+ File.separator+"importedDatabases.txt"));
            String line = br.readLine();
            while (line != null) {
                System.out.println(line+" loaded");
                System.out.println("|"+line+"|");
                mainDatabase.insert(line);
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