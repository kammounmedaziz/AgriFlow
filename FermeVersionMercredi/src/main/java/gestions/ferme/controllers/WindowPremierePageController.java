/*package gestions.ferme.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.StageStyle;



public class WindowPremierePageController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnCommencer;

    @FXML
    void commencer(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/choixUtilisateur/WindowTypeUtilisateur.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            stage.show();

        }catch (Exception e) {
            e.printStackTrace();
        }



    }

    @FXML
    void initialize() {
        assert btnCommencer != null : "fx:id=\"btnCommencer\" was not injected: check your FXML file 'WindowPremierePage.fxml'.";

    }

}*/
package gestions.ferme.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class WindowPremierePageController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnCommencer;

    @FXML
    void commencer(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/choixUtilisateur/WindowTypeUtilisateur.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            stage.show();

            // Fermer la fenêtre actuelle
            Stage currentStage = (Stage) btnCommencer.getScene().getWindow();
            currentStage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void initialize() {
        assert btnCommencer != null;
    }
}

