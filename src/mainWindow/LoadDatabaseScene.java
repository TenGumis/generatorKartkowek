package mainWindow;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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

        Label infoLabel1=new Label("Database name:");
        infoLabel1.setAlignment(Pos.CENTER);
        infoLabel1.setMaxWidth(180);


        TextField databaseName=new TextField();
        databaseName.setPromptText("Set your database name:");
        databaseName.setAlignment(Pos.CENTER);
        databaseName.setMaxWidth(180);
        if(selectedFile == null) databaseName.setDisable(true);
        else databaseName.setDisable(false);




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
                    if(!selectedFile.getName().endsWith(".txt"))
                        throw new NotTxtExtensionException();

                    Files.copy(Paths.get(selectedFile.getAbsolutePath()),
                            Paths.get("src"+ File.separator+"databases"+ File.separator + databaseName.getText() + ".txt"), StandardCopyOption.REPLACE_EXISTING);

                    mainDatabase.insert(databaseName.getText(),"src"+ File.separator+"databases"+ File.separator + databaseName.getText() + ".txt");

                    if(!Files.exists(Paths.get("src"+ File.separator+"databases"+ File.separator+"importedDatabases.txt")))
                        Files.createFile(Paths.get("src"+ File.separator+"databases"+ File.separator+"importedDatabases.txt"));
                    Files.write(Paths.get("src"+ File.separator+"databases"+ File.separator+"importedDatabases.txt"), (databaseName.getText()+System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);

                    stackScene.pop();
                    window.setScene(stackScene.peek());

                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (InvalidPathException | NotTxtExtensionException e1) {
                    showAlertBadPath();
                }
            }
        });
        okButton.setMaxWidth(180);

        Button loadDatabaseButton=new Button("Choose database file");
        loadDatabaseButton.setOnAction(e-> {
            FileChooser fileChooser=new FileChooser();
            fileChooser.setInitialDirectory( // set default directory to databases
                    new File("src" + System.getProperty("file.separator") + "databases"));
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
                databaseName.setDisable(false);
            }
        });

        loadDatabaseButton.setMaxWidth(180);


        loadDatabasePanel.getChildren().addAll(loadDatabaseButton,infoLabel0,databaseName,okButton,backButton);
        loadDatabasePanel.setSpacing(20);
        loadDatabasePanel.setAlignment(Pos.CENTER);

        loadDatabaseLayout.setCenter(loadDatabasePanel);

        loadDatabaseScene=new Scene(loadDatabaseLayout,400,400);
    }

    private void showAlertBadPath() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Błąd odczytu");
        alert.setHeaderText("Błąd odczutu pliku");
        alert.setContentText("Błędna ścieżka lub plik nie jest bazą danych!");
        alert.showAndWait();
    }

    Scene getScene(){
        return loadDatabaseScene;
    }

    private class NotTxtExtensionException extends Exception {}

}
