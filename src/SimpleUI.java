import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SimpleUI extends Application {

    @Override
    public void start(Stage stage) {
        Label label = new Label("Hello AP4B");
        Button button = new Button("Click me");

        button.setOnAction(e -> {
            label.setText("Button clicked!");
        });

        VBox root = new VBox(10);
        root.getChildren().addAll(label, button);

        Scene scene = new Scene(root, 300, 200);

        stage.setTitle("Simple UI Test");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

