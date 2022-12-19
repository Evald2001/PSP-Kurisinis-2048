package game2048.application;

import javafx.application.Application;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class MainWindow extends Application {
    GameManager manager = new GameManager();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(manager.getRoot());
        scene.setOnKeyPressed(event -> {
            manager.move(event.getCode());
        });

        {
            Bounds bounds = manager.getContainer().getLayoutBounds();
            Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
            double factor = Math.min(visualBounds.getWidth() / bounds.getWidth(),
                    visualBounds.getHeight() / bounds.getHeight());

            stage.setMinWidth(bounds.getWidth() / 1.25);
            stage.setMinHeight(bounds.getHeight() / 1.25);
            stage.setWidth(bounds.getWidth() * factor / 1.2);
            stage.setHeight(bounds.getHeight() * factor / 1.2);
        }

        stage.setTitle("2048");
        stage.setScene(scene);
        stage.show();

    }


}
