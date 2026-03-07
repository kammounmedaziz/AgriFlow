/*package gestions.ferme.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import gestions.ferme.services.EmailService;
import gestions.ferme.services.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class WindowOublierController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Hyperlink backToLogin;

    @FXML
    private TextField emailField;

    @FXML
    private Button resetButton;

    @FXML
    void handleBackToLogin(ActionEvent event) {
        try {
        // Charger la fenêtre de connexion
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/login/WindowLogin.fxml"));
        Parent root = loader.load();

        // Passer le rôle "client(e)" directement au contrôleur
        WindowLoginController controller = loader.getController();
        controller.setRoleAttendu("client(e)");  // Définit le rôle attendu pour cette connexion

        // Ouvrir la fenêtre de connexion
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.initStyle(StageStyle.UTILITY);
        stage.show();
    } catch (Exception e) {
        e.printStackTrace();
        showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la fenêtre de connexion.");


    }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void handleResetPassword(ActionEvent event) {

        String email = emailField.getText();

        // Vérifier si l'email est valide
        if (email != null && !email.isEmpty()) {
            UserService userService = new UserService();
            String password = userService.getPasswordByEmail(email);

            if (password != null) {
                // Essayer d'envoyer l'email et capturer les erreurs potentielles
                try {
                    // Envoyer le mot de passe par email
                    EmailService.sendPasswordByEmail(email, password);
                    System.out.println("Mot de passe envoyé à l'email : " + email);
                } catch (Exception e) {
                    // Afficher l'erreur si l'envoi échoue
                    System.out.println("Erreur lors de l'envoi de l'email : " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                System.out.println("Aucun utilisateur trouvé avec cet email.");
            }
        } else {
            System.out.println("Veuillez entrer un email valide.");
        }



    }

    @FXML
    void initialize() {
        assert backToLogin != null : "fx:id=\"backToLogin\" was not injected: check your FXML file 'WindowOublier.fxml'.";
        assert emailField != null : "fx:id=\"emailField\" was not injected: check your FXML file 'WindowOublier.fxml'.";
        assert resetButton != null : "fx:id=\"resetButton\" was not injected: check your FXML file 'WindowOublier.fxml'.";

    }

    private String roleAttendu;

    public void setRoleAttendu(String role) {
        this.roleAttendu = role;
    }
}*/

package gestions.ferme.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import gestions.ferme.services.EmailService;
import gestions.ferme.services.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class WindowOublierController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Hyperlink backToLogin;

    @FXML
    private TextField emailField;

    @FXML
    private Button resetButton;

    @FXML
    void handleBackToLogin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login/WindowLogin.fxml"));
            Parent root = loader.load();

            WindowLoginController controller = loader.getController();
            controller.setRoleAttendu("client(e)");

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initStyle(StageStyle.UTILITY);
            stage.show();

            // Fermer la fenêtre actuelle
            Stage currentStage = (Stage) backToLogin.getScene().getWindow();
            currentStage.close();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la fenêtre de connexion.");
        }
    }

    @FXML
    void handleResetPassword(ActionEvent event) {
        String email = emailField.getText();

        if (email != null && !email.isEmpty()) {
            UserService userService = new UserService();
            String password = userService.getPasswordByEmail(email);

            if (password != null) {
                try {
                    EmailService.sendPasswordByEmail(email, password);
                    showAlert(Alert.AlertType.INFORMATION, "Succès", "Mot de passe envoyé à l'email : " + email);
                } catch (Exception e) {
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Échec de l'envoi de l'email.");
                }
            } else {
                showAlert(Alert.AlertType.WARNING, "Aucun utilisateur", "Aucun utilisateur trouvé avec cet email.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Champ vide", "Veuillez entrer un email valide.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void initialize() {
        assert backToLogin != null : "fx:id=\"backToLogin\" was not injected: check your FXML file 'WindowOublier.fxml'.";
        assert emailField != null : "fx:id=\"emailField\" was not injected: check your FXML file 'WindowOublier.fxml'.";
        assert resetButton != null : "fx:id=\"resetButton\" was not injected: check your FXML file 'WindowOublier.fxml'.";
    }

    private String roleAttendu;

    public void setRoleAttendu(String role) {
        this.roleAttendu = role;
    }
}

