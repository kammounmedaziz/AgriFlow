package gestions.ferme.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import gestions.ferme.services.AuthenticationService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class WindowLoginController {


        @FXML
        private Button btnLogin;

        @FXML
        private TextField idUtAuth;

        @FXML
        private TextField pwdUtAuth;

        private AuthenticationService authService = new AuthenticationService();

        private String roleAttendu; // Le rôle que l'utilisateur doit avoir

        public void setRoleAttendu(String role) {
            this.roleAttendu = role;
        }

        @FXML
        void Authentifier(ActionEvent event) {
            String id = idUtAuth.getText().trim();
            String password = pwdUtAuth.getText().trim();

            if (id.isEmpty() || password.isEmpty()) {
                showAlert("Champs manquants", "Veuillez remplir tous les champs.");
                return;
            }

            String roleUtilisateur = authService.authentificationEtRetournerRole(id, password);

            if (roleUtilisateur == null) {
                showAlert("Échec", "Identifiants incorrects.");
            } else if (roleUtilisateur.equalsIgnoreCase(roleAttendu)) {
                showAlert("Succès", "Connexion réussie en tant que " + roleAttendu);
                // TODO : ouvrir la fenêtre principale selon rôle
            } else {
                showAlert("Refusé", "Vous n'êtes pas autorisé(e) à accéder à cette section.");
            }
        }

        private void showAlert(String titre, String message) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(titre);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        }


}




