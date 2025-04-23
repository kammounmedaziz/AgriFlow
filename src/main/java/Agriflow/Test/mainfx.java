package Agriflow;

import javafx.application.Application;
import javafx.fxml.FXMLLoader; // Corrected import
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class mainfx extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        try {
            // Load the FXML file.  Make sure the path is correct.
            Parent root = FXMLLoader.load(getClass().getResource("fxmlfiles/animal.fxml")); // Use the correct path
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Agriculture Management Application"); // Set your title
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace(); // VERY IMPORTANT: Print the stack trace to see the error!
            //  Consider showing an alert to the user here, instead of just printing to the console.
            throw e; // rethrow the exception
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
