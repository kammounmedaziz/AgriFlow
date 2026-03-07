package gestions.ferme.controllers;

import gestions.ferme.services.AuthenticationService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class WindowLoginController {

    @FXML
    private Button btnLogin;

    @FXML
    private TextField idUtAuth;

    @FXML
    private PasswordField pwdUtAuth;

    @FXML
    private Hyperlink createAccountLink;

    @FXML
    private Hyperlink forgetPassword;

    private AuthenticationService authService = new AuthenticationService();

    private String roleAttendu; // Le rôle que l'utilisateur doit avoir

    // Méthode pour définir le rôle attendu pour la connexion
    public void setRoleAttendu(String role) {
        this.roleAttendu = role;
    }

    // Méthode pour contrôler la visibilité du lien de création de compte
    public void setCreateAccountVisible(boolean visible) {
        createAccountLink.setVisible(visible);
    }

    // Méthode pour l'authentification lors de la connexion
    @FXML
    void Authentifier(ActionEvent event) {

            String id = idUtAuth.getText().trim();
            String password = pwdUtAuth.getText().trim();

            if (roleAttendu != null && roleAttendu.equalsIgnoreCase("client(e)")) {
                id = "defaultClient";
                password = "defaultPassword";
            }

            if (id.isEmpty() || password.isEmpty()) {
                showAlert("Champs manquants", "Veuillez remplir tous les champs.");
                return;
            }

            String roleUtilisateur = authService.authentificationEtRetournerRole(id, password);

            if (roleUtilisateur == null) {
                showAlert("Échec", "Identifiants incorrects.");
                return;
            }

            if (!roleUtilisateur.equalsIgnoreCase(roleAttendu)) {
                showAlert("Refusé", "Vous n'êtes pas autorisé(e) à accéder à cette section.");
                return;
            }

            showAlert("Succès", "Connexion réussie en tant que " + roleUtilisateur);

            try {
                String fxmlPath = "";

                switch (roleUtilisateur.toLowerCase()) {
                    case "client(e)":
                        fxmlPath = "/vente/vente.fxml";
                        break;
                    case "administrateur":
                        fxmlPath = "/admin _dashboard/Dashboard.fxml";
                        break;
                    case "fermier(e)":
                        fxmlPath = "/tache agriculteur/agriculteur_tasks.fxml";
                        break;
                    case "ingénieur agriculture":
                        fxmlPath = "/traitementChamps/traitement champ.fxml";
                        break;
                    case "vétérinaire":
                        fxmlPath = "/traitementsAnimals/traitement animal.fxml";
                        break;
                    default:
                        showAlert("Erreur", "Rôle inconnu : " + roleUtilisateur);
                        return;
                }

                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
                Parent root = loader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.initStyle(StageStyle.DECORATED);
                stage.show();

                // Fermer la fenêtre de login actuelle
                Stage currentStage = (Stage) btnLogin.getScene().getWindow();
                currentStage.close();

            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger l'interface.");
            }
        }


    // Méthode pour ouvrir la fenêtre d'inscription
    @FXML
    void CreerCompte(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/inscription/WindowInscription.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Méthode pour afficher les alertes
    private void showAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Méthode pour gérer le clic sur le lien "Oublier mot de passe"
    @FXML
    void OublierMotDePasse(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/oublierMp/WindowOublier.fxml"));
            Parent root = loader.load();

            // Récupérer le bon contrôleur (WindowOublierController)
            WindowOublierController controller = loader.getController();
            controller.setRoleAttendu("client(e)"); // À implémenter dans WindowOublierController si nécessaire

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initStyle(StageStyle.UTILITY);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la fenêtre de réinitialisation.");
        }
    }

    @FXML
    void initialize() {
        assert btnLogin != null : "fx:id=\"btnLogin\" was not injected: check your FXML file 'WindowLogin.fxml'.";
        assert createAccountLink != null : "fx:id=\"createAccountLink\" was not injected: check your FXML file 'WindowLogin.fxml'.";
        assert idUtAuth != null : "fx:id=\"idUtAuth\" was not injected: check your FXML file 'WindowLogin.fxml'.";
        assert pwdUtAuth != null : "fx:id=\"pwdUtAuth\" was not injected: check your FXML file 'WindowLogin.fxml'.";
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
