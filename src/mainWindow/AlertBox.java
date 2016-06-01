package mainWindow;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Button;

/**
 * Created by filip on 5/21/16.
 */
public class AlertBox {
    static public void display(String message) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);

        // Close Button
        Button close = new Button("Close");
        close.setOnAction(event -> window.close());

        // Label
        Label label = new Label(message);

        // VBox layout
        VBox vbox = new VBox(8);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.getChildren().addAll(label, close);
        vbox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(vbox);
        window.setScene(scene);
        window.setTitle("ERROR");
        window.showAndWait();
    }
}
