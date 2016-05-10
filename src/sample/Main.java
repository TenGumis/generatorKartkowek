package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.HashMap;

public class Main extends Application {

    Stage stage;
    GridPane mainGridPane,rightGridPane;

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();

        MenuBar menuBar = createMenuBar();

        root.setTop(menuBar);
        mainGridPane=createMainGridPane();
        rightGridPane= createRightGridPane();
        root.setCenter(mainGridPane);
        root.setRight(rightGridPane);

        Scene scene = new Scene(root, 600, 500);
        primaryStage.setTitle("Generator kartkówek");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar=new MenuBar();

        Menu menuFile = new Menu("File");
        MenuItem loadDatabase=new MenuItem("Load database");
        loadDatabase.setOnAction(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent t) {
                        loadDatabase();
                    }
                });
        menuFile.getItems().addAll(loadDatabase);

        menuBar.getMenus().addAll(menuFile);

        return menuBar;
    }

    private void loadDatabase() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose your database");
        File file=fileChooser.showOpenDialog(stage);
        HashMap<String, String> map = new HashMap<String, String>();
        FileReader fr=null;
        BufferedReader in=null;
        try {
            fr= new FileReader(file);
            in = new BufferedReader(fr);
            String line = "";
            while ((line = in.readLine()) != null) {
                String parts[] = line.split(":");
                map.put(parts[0], parts[1]);
            }
            in.close();
        }  catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        System.out.println(map.toString());

    }

    public GridPane createMainGridPane() {
        GridPane grid = new GridPane();
        grid.setHgap(1);
        grid.setVgap(1);
        //grid.setPadding(new Insets(0, 10, 0, 10));

        // Category in column 2, row 1
        Text obwieszczenie = new Text("W tej części będą kartkówki'katkówki':");
        grid.add(obwieszczenie,0,0);

        return grid;
    }

    public GridPane createRightGridPane() {
        GridPane grid = new GridPane();
        grid.setHgap(1);
        grid.setVgap(2);

        Text obwieszczenie = new Text("Tu będą ustawienia, ilosc slowek itd.");
        TextField count = new TextField();
        count.setPromptText("Set number of words");
        count.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                if(!count.getText().equals("")) generateTest(new Integer(count.getText()));
            }
        });
        grid.add(obwieszczenie,0,0);
        grid.add(count,1,1);
        grid.add(new Label("Set number of words:"), 0, 1);
        return grid;
    }

    private void generateTest(Integer in) {
        if(in==null) System.out.println("????");
        try {
            mainGridPane.setHgap(in + 1);
        } catch (Exception e){
            System.out.println("sdf");
        }
        mainGridPane.setVgap(2);
        for(int i=0;i<in;i++){
            mainGridPane.add(new TextField(),0,i+1);
            mainGridPane.add(new TextField(),1,i+1);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}