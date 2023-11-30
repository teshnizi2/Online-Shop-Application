package graphics;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AlertBox {

    public static void showMessage(String title, String message) {
        Stage window = new Stage();
        window.setTitle(title);
        window.setScene(new Scene(makeAlertBox(window, message)));
        window.show();
    }

    private static Parent makeAlertBox(Stage window, String message) {
        VBox vBox = new VBox(30);
        vBox.setPrefSize(300, 130);
        Label label = new Label(message);
        label.setWrapText(true);
        Button OKButton = new Button("OK!");
        OKButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                window.close();
            }
        });
        vBox.getChildren().addAll(label, OKButton);
        vBox.setAlignment(Pos.CENTER);
        return vBox;
    }
}
