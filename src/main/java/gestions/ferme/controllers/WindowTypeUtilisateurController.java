package gestions.ferme.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class WindowTypeUtilisateurController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button idUtAdmin;

    @FXML
    private Button idUtClient;

    @FXML
    private Button idUtFermier;

    @FXML
    private Button idUtIng;

    @FXML
    private Button idUtVeterenaire;

    @FXML
    void VerifierExistantClient(ActionEvent event) {
        ouvrirFenetreLogin("client(e)");
    }

    @FXML
    void VerifierExistantAdmin(ActionEvent event) {
        ouvrirFenetreLogin("administrateur");
    }

    @FXML
    void VerifierExistantFermier(ActionEvent event) {
        ouvrirFenetreLogin("fermier(e)");
    }

    @FXML
    void VerifierExistantIngenier(ActionEvent event) {
        ouvrirFenetreLogin("ngénieur agriculture");
    }

    @FXML
    void VerifierExistantVeterinaire(ActionEvent event) {
        ouvrirFenetreLogin("vétérinaire");
    }

    private void ouvrirFenetreLogin(String role) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/WindowLogin.fxml"));
            Parent root = loader.load();

            // Passer le rôle au contrôleur de login
            WindowLoginController controller = loader.getController();
            controller.setRoleAttendu(role);

            Stage stage = new Stage();
            stage.setTitle("Connexion - " + role.toUpperCase());
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
