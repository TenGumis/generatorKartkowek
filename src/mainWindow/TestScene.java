package mainWindow;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Stack;

/**
 * Created by pabia on 12.05.2016.
 */
public class TestScene {
    Scene testScene;

    public TestScene(Stage window, Stack<Scene> stackScene){
        BorderPane testLayout=new BorderPane();
        VBox testPanel=new VBox();

        Label infoLabel=new Label("Tu odbywa się test");
        infoLabel.setMaxWidth(130);

        Button backButton=new Button("Back");
        backButton.setOnAction(e-> {
            System.out.println(stackScene.size());
            stackScene.pop();
            window.setScene(stackScene.peek());
        });
        backButton.setMaxWidth(130);

        testPanel.getChildren().addAll(infoLabel,backButton);
        testPanel.setSpacing(20);
        testPanel.setAlignment(Pos.CENTER);

        testLayout.setCenter(testPanel);

        testScene=new Scene(testLayout,400,400);
    }

    Scene getScene(){
        return testScene;
    }
}
