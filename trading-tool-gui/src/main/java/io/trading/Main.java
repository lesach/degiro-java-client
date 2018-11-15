package io.trading;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;

/**
 * Add following options to execute the application in the VM Options execution configuration:
 * --module-path="/opt/javafx-sdk-11.0.1/lib" --add-modules=javafx.controls --add-exports=javafx.graphics/com.sun.javafx.util=ALL-UNNAMED --add-exports=javafx.base/com.sun.javafx.reflect=ALL-UNNAMED --add-exports=javafx.base/com.sun.javafx.beans=ALL-UNNAMED
 */
public class Main extends Application {
    private static final Logger logger = LogManager.getLogger(Main.class);

    /**
     * Entry point
     * @param args none expected
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            // Load FXML file path
            final URL url = getClass().getResource("/fxml/Main.fxml");
            // Loader creation
            final Parent root = FXMLLoader.load(url);
            primaryStage.setTitle("Hello World");
            final Scene scene = new Scene(root);
            scene.getStylesheets().add("/css/main.css");
            primaryStage.setScene(scene);
            primaryStage.show();
        }
        catch (Exception e) {
            logger.error("ERROR in main", e);
        }
    }
}
