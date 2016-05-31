package mainWindow;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.*;
import java.util.Stack;

/**
 * Created by tengumis on 12.05.2016.
 */
public class LoadDatabaseScene {

    private Scene loadDatabaseScene;
    private File selectedFile=null;
    private MainDatabase mainDatabase;

    public LoadDatabaseScene(Stage window, Stack<Scene> stackScene,MainDatabase mainDatabase){
        this.mainDatabase=mainDatabase;

        BorderPane loadDatabaseLayout=new BorderPane();
        VBox loadDatabasePanel=new VBox();

        Label infoLabel0=new Label("Your file:");
        infoLabel0.setAlignment(Pos.CENTER);
        infoLabel0.setMaxWidth(180);

        Label infoLabel1=new Label("Set your database name:");
        infoLabel1.setAlignment(Pos.CENTER);
        infoLabel1.setMaxWidth(180);

        TextField databaseName=new TextField();
        databaseName.setPromptText("Set your database name");
        databaseName.setAlignment(Pos.CENTER);
        databaseName.setMaxWidth(180);

        Button backButton=new Button("Back");
        backButton.setOnAction(e-> {
            System.out.println(stackScene.size());
            stackScene.pop();
            window.setScene(stackScene.peek());
        });
        backButton.setMaxWidth(180);

        Button okButton=new Button("OK");
        okButton.setOnAction(e-> {
            if (selectedFile != null) {
                System.out.println(databaseName.getText());
                try {
                    Files.copy(Paths.get(selectedFile.getAbsolutePath()),
                            Paths.get("src"+ File.separator+"databases"+ File.separator + databaseName.getText() + ".txt"), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                mainDatabase.insert(databaseName.getText(),"src"+ File.separator+"databases"+ File.separator + databaseName.getText() + ".txt");
                try {
                    if(!Files.exists(Paths.get("src"+ File.separator+"databases"+ File.separator+"importedDatabases.txt")))
                        Files.createFile(Paths.get("src"+ File.separator+"databases"+ File.separator+"importedDatabases.txt"));
                    Files.write(Paths.get("src"+ File.separator+"databases"+ File.separator+"importedDatabases.txt"), (databaseName.getText()+System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);
                }catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
        });
        okButton.setMaxWidth(180);

        Button loadDatabaseButton=new Button("Choose database file");
        loadDatabaseButton.setOnAction(e-> {
            FileChooser fileChooser=new FileChooser();
            fileChooser.setTitle("Choose database file");
            selectedFile=fileChooser.showOpenDialog(window);
            if (selectedFile!=null){
                BufferedReader bufferedReader=null;
                infoLabel0.setText("Your file: "+selectedFile.getName());
                try {
                    bufferedReader=new BufferedReader(new FileReader(selectedFile));
                    databaseName.setText(bufferedReader.readLine());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        loadDatabaseButton.setMaxWidth(180);


        loadDatabasePanel.getChildren().addAll(loadDatabaseButton,infoLabel0,databaseName,okButton,backButton);
        loadDatabasePanel.setSpacing(20);
        loadDatabasePanel.setAlignment(Pos.CENTER);

        loadDatabaseLayout.setCenter(loadDatabasePanel);

        loadDatabaseScene=new Scene(loadDatabaseLayout,400,400);
    }

    Scene getScene(){
        return loadDatabaseScene;
    }

}
