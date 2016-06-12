package mainWindow;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.*;
import java.nio.file.*;

/**
 * Created by tengumis on 12.05.2016.
 */
public class LoadDatabaseScene {

    private File selectedFile=null;
    private Stage loadDatabaseStage =null;

    public LoadDatabaseScene(GameCreator gameCreator,Stage window1, MainDatabase mainDatabase){
        loadDatabaseStage =new Stage();
        loadDatabaseStage.setTitle("Load database");
        loadDatabaseStage.initModality(Modality.WINDOW_MODAL);
        loadDatabaseStage.initOwner(window1);

        BorderPane loadDatabaseLayout=new BorderPane();
        VBox loadDatabasePanel=new VBox();

        Label infoLabel0=new Label("Your file:");
        infoLabel0.setAlignment(Pos.CENTER);

        Label infoLabel1=new Label("Database name:");
        infoLabel1.setAlignment(Pos.CENTER);

        TextField databaseName=new TextField();
        databaseName.setPromptText("Set your database name:");
        databaseName.setAlignment(Pos.CENTER);

        if(selectedFile == null) databaseName.setDisable(true);
        else databaseName.setDisable(false);

        Button closeButton=new Button("Close");
        closeButton.setOnAction(e-> loadDatabaseStage.close());

        Button okButton=new Button("OK");
        okButton.setOnAction(e-> {
            if (selectedFile != null && !mainDatabase.contain(databaseName.getText()) && !databaseName.getText().equals("")) {

                    try {

                        Files.copy(Paths.get(selectedFile.getAbsolutePath()),
                                Paths.get("src" + File.separator + "databases" + File.separator + mainDatabase.getSize() + ".txt"), StandardCopyOption.REPLACE_EXISTING);

                        mainDatabase.insert(databaseName.getText());

                        if (!Files.exists(Paths.get("src" + File.separator + "databases" + File.separator + "importedDatabases.txt")))
                            Files.createFile(Paths.get("src" + File.separator + "databases" + File.separator + "importedDatabases.txt"));
                        Files.write(Paths.get("src" + File.separator + "databases" + File.separator + "importedDatabases.txt"), (databaseName.getText() + System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);

                        System.out.println("Loaded: " + databaseName.getText());
                        gameCreator.updateCategories();
                        loadDatabaseStage.close();

                    } catch (IOException e1) {
                        e1.printStackTrace();
                        mainDatabase.remove(databaseName.getText());
                    }
            } else{
                infoLabel0.setStyle("-fx-text-fill: red");
                if (selectedFile==null) {
                    infoLabel0.setText("No file selected!");
                }else if(mainDatabase.contain(databaseName.getText())){
                    infoLabel0.setText("This category name already exist in database!");
                }
                else{
                    infoLabel0.setText("Category name format is wrong!");
                }

            }
        });

        Button loadDatabaseButton=new Button("Choose database file");
        loadDatabaseButton.setOnAction(e-> {
            FileChooser fileChooser=new FileChooser();
            fileChooser.setInitialDirectory( // set default directory to databases
                    new File("src" + System.getProperty("file.separator") + "databases"));
            fileChooser.setTitle("Choose database file");
            selectedFile=fileChooser.showOpenDialog(loadDatabaseStage);
            if (selectedFile!=null){
                BufferedReader bufferedReader=null;
                infoLabel0.setText("Your file: "+selectedFile.getName());
                infoLabel0.setStyle("-fx-text-fill: black");
                try {
                    bufferedReader=new BufferedReader(new FileReader(selectedFile));
                    databaseName.setText(bufferedReader.readLine());
                    databaseName.setDisable(false);
                } catch (Exception e1) {
                    infoLabel0.setStyle("-fx-text-fill: red");
                    infoLabel0.setText("No file selected!");
                    selectedFile=null;
                    databaseName.setDisable(true);
                }
            }
        });

        loadDatabasePanel.getChildren().addAll(loadDatabaseButton,infoLabel0,databaseName,okButton,closeButton);
        loadDatabasePanel.setSpacing(20);
        loadDatabasePanel.setAlignment(Pos.CENTER);

        loadDatabaseLayout.setCenter(loadDatabasePanel);

        Scene loadDatabaseScene;
        loadDatabaseScene=new Scene(loadDatabaseLayout,350,400);
        loadDatabaseScene.getStylesheets().add(getClass().getResource(".."+ File.separator+"styleScheets"+File.separator+"mainStyleScheet.css").toExternalForm());
        loadDatabaseStage.setScene(loadDatabaseScene);
        loadDatabaseStage.show();
    }

}
