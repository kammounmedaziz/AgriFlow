/*package gestions.ferme.controllers;

import gestions.ferme.entities.Utilisateur;
import gestions.ferme.services.InscriptionService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class WindowInscriptionController {

    @FXML
    private Button btnCreerCmpt;
    @FXML
    private TextField cinInsp;
    @FXML
    private TextField emailInsp;
    @FXML
    private Hyperlink hyprlnk;
    @FXML
    private TextField nomInsp;
    @FXML
    private TextField prenomInsp;
    @FXML
    private PasswordField pwdCnfInsp;
    @FXML
    private PasswordField pwdInsp;
    @FXML
    private TextField telephoneInsp;

    @FXML
    void CompteDejaExiste(ActionEvent event) {
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

    @FXML
    void CreerCompte(ActionEvent event) {
        try {
            String id = cinInsp.getText().trim();
            String nom = nomInsp.getText().trim();
            String prenom = prenomInsp.getText().trim();
            String telephone = telephoneInsp.getText().trim();
            String email = emailInsp.getText().trim();
            String password = pwdInsp.getText();
            String confirmPassword = pwdCnfInsp.getText();

            // --- VALIDATIONS --- (similaires à celles dans ajouterEntity)
            if (id.isEmpty() || !id.matches("\\d{8}")) {
                showAlert(Alert.AlertType.WARNING, "Erreur", "L'ID doit contenir exactement 8 chiffres.");
                return;
            }
            if (nom.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Erreur", "Nom invalide.");
                return;
            }
            if (prenom.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Erreur", "Prénom invalide.");
                return;
            }
            if (telephone.isEmpty() || !telephone.matches("\\d{8,15}")) {
                showAlert(Alert.AlertType.WARNING, "Erreur", "Numéro de téléphone invalide.");
                return;
            }
            if (email.isEmpty() || !email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                showAlert(Alert.AlertType.WARNING, "Erreur", "Email invalide.");
                return;
            }
            if (password == null || password.length() < 6) {
                showAlert(Alert.AlertType.WARNING, "Erreur", "Mot de passe trop court (minimum 6 caractères).");
                return;
            }
            if (!password.equals(confirmPassword)) {
                showAlert(Alert.AlertType.WARNING, "Erreur", "Les mots de passe ne correspondent pas.");
                return;
            }

            // --- Création de l'utilisateur ---
            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setId(id);
            utilisateur.setNom(nom);
            utilisateur.setPrenom(prenom);
            utilisateur.setTelephone(telephone);
            utilisateur.setEmail(email);
            utilisateur.setPassword(password);
            utilisateur.setRole("client(e)");
            utilisateur.setSex(null); // sexe non utilisé ici

            // Appel du service pour ajouter l'utilisateur
            InscriptionService service = new InscriptionService();
            service.ajouterEntity(utilisateur); // Le service gère les doublons

            // Message de succès
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Compte créé avec succès.");
            clearFields();

        } catch (IllegalArgumentException e) {
            // Affichage de l'erreur de validation retournée par le service
            showAlert(Alert.AlertType.WARNING, "Erreur", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de la création du compte.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        cinInsp.clear();
        nomInsp.clear();
        prenomInsp.clear();
        telephoneInsp.clear();
        emailInsp.clear();
        pwdInsp.clear();
        pwdCnfInsp.clear();
    }

    @FXML
    void initialize() {
        assert btnCreerCmpt != null : "fx:id=\"btnCreerCmpt\" not injected.";
        assert cinInsp != null : "fx:id=\"cinInsp\" not injected.";
        assert emailInsp != null : "fx:id=\"emailInsp\" not injected.";
        assert hyprlnk != null : "fx:id=\"hyprlnk\" not injected.";
        assert nomInsp != null : "fx:id=\"nomInsp\" not injected.";
        assert prenomInsp != null : "fx:id=\"prenomInsp\" not injected.";
        assert pwdCnfInsp != null : "fx:id=\"pwdCnfInsp\" not injected.";
        assert pwdInsp != null : "fx:id=\"pwdInsp\" not injected.";
        assert telephoneInsp != null : "fx:id=\"telephoneInsp\" not injected.";
    }
}*/
package gestions.ferme.controllers;

import gestions.ferme.entities.Utilisateur;
import gestions.ferme.services.InscriptionService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class WindowInscriptionController {

    @FXML
    private Button btnCreerCmpt;
    @FXML
    private TextField cinInsp;
    @FXML
    private TextField emailInsp;
    @FXML
    private Hyperlink hyprlnk;
    @FXML
    private TextField nomInsp;
    @FXML
    private TextField prenomInsp;
    @FXML
    private PasswordField pwdCnfInsp;
    @FXML
    private PasswordField pwdInsp;
    @FXML
    private TextField telephoneInsp;

    @FXML
    void CompteDejaExiste(ActionEvent event) {
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
            Stage currentStage = (Stage) hyprlnk.getScene().getWindow();
            currentStage.close();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la fenêtre de connexion.");
        }
    }

    @FXML
    void CreerCompte(ActionEvent event) {
        try {
            String id = cinInsp.getText().trim();
            String nom = nomInsp.getText().trim();
            String prenom = prenomInsp.getText().trim();
            String telephone = telephoneInsp.getText().trim();
            String email = emailInsp.getText().trim();
            String password = pwdInsp.getText();
            String confirmPassword = pwdCnfInsp.getText();

            // --- VALIDATIONS ---
            if (id.isEmpty() || !id.matches("\\d{8}")) {
                showAlert(Alert.AlertType.WARNING, "Erreur", "L'ID doit contenir exactement 8 chiffres.");
                return;
            }
            if (nom.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Erreur", "Nom invalide.");
                return;
            }
            if (prenom.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Erreur", "Prénom invalide.");
                return;
            }
            if (telephone.isEmpty() || !telephone.matches("\\d{8,15}")) {
                showAlert(Alert.AlertType.WARNING, "Erreur", "Numéro de téléphone invalide.");
                return;
            }
            if (email.isEmpty() || !email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                showAlert(Alert.AlertType.WARNING, "Erreur", "Email invalide.");
                return;
            }
            if (password == null || password.length() < 6) {
                showAlert(Alert.AlertType.WARNING, "Erreur", "Mot de passe trop court (minimum 6 caractères).");
                return;
            }
            if (!password.equals(confirmPassword)) {
                showAlert(Alert.AlertType.WARNING, "Erreur", "Les mots de passe ne correspondent pas.");
                return;
            }

            // --- Création de l'utilisateur ---
            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setId(id);
            utilisateur.setNom(nom);
            utilisateur.setPrenom(prenom);
            utilisateur.setTelephone(telephone);
            utilisateur.setEmail(email);
            utilisateur.setPassword(password); // Le hash se fait dans InscriptionService
            utilisateur.setRole("client(e)");
            utilisateur.setSex(null); // sexe non utilisé ici

            InscriptionService service = new InscriptionService();
            service.ajouterEntity(utilisateur);

            showAlert(Alert.AlertType.INFORMATION, "Succès", "Compte créé avec succès.");
            clearFields();

        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.WARNING, "Erreur", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur s'est produite lors de la création du compte.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        cinInsp.clear();
        nomInsp.clear();
        prenomInsp.clear();
        telephoneInsp.clear();
        emailInsp.clear();
        pwdInsp.clear();
        pwdCnfInsp.clear();
    }

    @FXML
    void initialize() {
        assert btnCreerCmpt != null : "fx:id=\"btnCreerCmpt\" not injected.";
        assert cinInsp != null : "fx:id=\"cinInsp\" not injected.";
        assert emailInsp != null : "fx:id=\"emailInsp\" not injected.";
        assert hyprlnk != null : "fx:id=\"hyprlnk\" not injected.";
        assert nomInsp != null : "fx:id=\"nomInsp\" not injected.";
        assert prenomInsp != null : "fx:id=\"prenomInsp\" not injected.";
        assert pwdCnfInsp != null : "fx:id=\"pwdCnfInsp\" not injected.";
        assert pwdInsp != null : "fx:id=\"pwdInsp\" not injected.";
        assert telephoneInsp != null : "fx:id=\"telephoneInsp\" not injected.";
    }
}

