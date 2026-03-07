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

public class WindowDashboardController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button BtnGeAgriculteur;

    @FXML
    private Button BtnGeIngAgr;

    @FXML
    private Button BtnGeVeterinaire;

    @FXML
    private Button btnAccueil;



    @FXML
    private Button btnDcnx;

    @FXML
    void GereHome(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/choixUtilisateur/WindowTypeUtilisateur.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            stage.show();

            // Fermer la fenêtre actuelle
            Stage currentStage = (Stage) btnAccueil.getScene().getWindow();
            currentStage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    void GererAgriculteurs(ActionEvent event) {
        try {
        Parent root = FXMLLoader.load(getClass().getResource("/admin_controling_agricultures/userAgricul.fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.UTILITY);
        stage.show();

        // Fermer la fenêtre actuelle
        Stage currentStage = (Stage) BtnGeAgriculteur.getScene().getWindow();
        currentStage.close();

    } catch (Exception e) {
        e.printStackTrace();
    }


    }

    @FXML
    void GererIngenieurs(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/admin_controlling_engineers/userEngenier.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            stage.show();

            // Fermer la fenêtre actuelle
            Stage currentStage = (Stage) BtnGeIngAgr.getScene().getWindow();
            currentStage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    void GererVeterinaires(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/admin_controlling_veterenere/userveter.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            stage.show();

            // Fermer la fenêtre actuelle
            Stage currentStage = (Stage) BtnGeVeterinaire.getScene().getWindow();
            currentStage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }



    @FXML
    void RetourLogin(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/login/WindowLogin.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            stage.show();

            // Fermer la fenêtre actuelle
            Stage currentStage = (Stage) btnDcnx.getScene().getWindow();
            currentStage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }



    }

    @FXML
    void initialize() {
        assert BtnGeAgriculteur != null : "fx:id=\"BtnGeAgriculteur\" was not injected: check your FXML file 'Dashboard.fxml'.";
        assert BtnGeIngAgr != null : "fx:id=\"BtnGeIngAgr\" was not injected: check your FXML file 'Dashboard.fxml'.";
        assert BtnGeVeterinaire != null : "fx:id=\"BtnGeVeterinaire\" was not injected: check your FXML file 'Dashboard.fxml'.";
        assert btnAccueil != null : "fx:id=\"btnAccueil\" was not injected: check your FXML file 'Dashboard.fxml'.";
        assert btnDcnx != null : "fx:id=\"btnDcnx\" was not injected: check your FXML file 'Dashboard.fxml'.";

    }

}
