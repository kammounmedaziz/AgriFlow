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

public class WindowIngenieurController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button IngBtnGererAgriculteur;

    @FXML
    private Button IngBtnGererVeterinaire;

    @FXML
    private Button IngBtnRetourAccueil;

    @FXML
    void IngGererAgriculteur(ActionEvent event) {
        loadNewWindow("/admin_controling_agricultures/agriculteurs.fxml", IngBtnGererAgriculteur);
    }

    @FXML
    void IngGererVeterinaire(ActionEvent event) {
        loadNewWindow("/admin_controlling_veterenere/veterenere.fxml", IngBtnGererVeterinaire);
    }

    @FXML
    void IngRetourAccueil(ActionEvent event) {
        loadNewWindow("/admin_dashboard/Dashboard.fxml", IngBtnRetourAccueil);
    }

    private void loadNewWindow(String fxmlPath, Button button) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            stage.show();

            // Close current window
            Stage currentStage = (Stage) button.getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void initialize() {
        assert IngBtnGererAgriculteur != null : "fx:id=\"IngBtnGererAgriculteur\" was not injected: check your FXML file 'engineers.fxml'.";
        assert IngBtnGererVeterinaire != null : "fx:id=\"IngBtnGererVeterinaire\" was not injected: check your FXML file 'engineers.fxml'.";
        assert IngBtnRetourAccueil != null : "fx:id=\"IngBtnRetourAccueil\" was not injected: check your FXML file 'engineers.fxml'.";
    }
}

 */

package gestions.ferme.controllers;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import gestions.ferme.entities.Utilisateur;
import gestions.ferme.services.UtilisateurService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class WindowIngenieurController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField cinField;

    @FXML
    private Button BtReturnPageAdmin;

    @FXML
    private Button addButton;

    @FXML
    private Label adminCountLabel;

    @FXML
    private Button deleteButton;

    @FXML
    private Button editButton;

    @FXML
    private TableColumn<Utilisateur, String> emailCol;

    @FXML
    private TextField emailField;

    @FXML
    private VBox formContainer;

    @FXML
    private Label formTitle;

    @FXML
    private TableColumn<Utilisateur, Integer> idCol;

    @FXML
    private TextField idField;

    @FXML
    private TableColumn<Utilisateur, String> nomCol;

    @FXML
    private TextField nomField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TableColumn<Utilisateur, String> prenomCol;

    @FXML
    private TextField prenomField;



    @FXML
    private TableColumn<Utilisateur, String> sexeCol;

    @FXML
    private ComboBox<String> sexeField;

    @FXML
    private TableColumn<Utilisateur, String> telephoneCol;

    @FXML
    private TextField telephoneField;

    @FXML
    private Label totalUsersLabel;

    @FXML
    private TableView<Utilisateur> userTable;

    UtilisateurService service = new UtilisateurService();

    @FXML
    void ReturnPageAdmin(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/admin _dashboard/Dashboard.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            stage.show();

            // Fermer la fenêtre actuelle
            Stage currentStage = (Stage) BtReturnPageAdmin.getScene().getWindow();
            currentStage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    void deleteUser(ActionEvent event) {
        Utilisateur selected = userTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            service.supprimerEntity(selected);
            chargerTable();
        }


    }

    @FXML
    void hideForm(ActionEvent event) {
        formContainer.setVisible(false);

    }

    @FXML
    void saveUser(ActionEvent event) {
        try {
            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setId(cinField.getText());
            utilisateur.setNom(nomField.getText());
            utilisateur.setPrenom(prenomField.getText());
            utilisateur.setSex(sexeField.getValue());
            utilisateur.setTelephone(telephoneField.getText());
            utilisateur.setEmail(emailField.getText());

            String password = passwordField.getText();
            if (!password.isEmpty()) {
                utilisateur.setPassword(password); // Mot de passe modifié
            }

            // Vérifier si utilisateur existe déjà
            boolean exists = service.findById(utilisateur.getId()) != null;
            if (exists) {
                service.updateEntity2(utilisateur.getId(), utilisateur);  // ✅ appel direct
            } else {
                utilisateur.setRole("ingénieur agriculture");
                service.ajouterEntityIngenieur(utilisateur);
            }

            hideForm(null);
            chargerTable();

        } catch (Exception e) {
            System.out.println("Erreur lors de l'enregistrement : " + e.getMessage());
        }


    }

    @FXML
    void showAddUserForm(ActionEvent event) {
        formContainer.setVisible(true);
        formTitle.setText("Ajouter un nouvel ingénieur");

        cinField.clear();
        nomField.clear();
        prenomField.clear();
        sexeField.getSelectionModel().clearSelection();
        telephoneField.clear();
        emailField.clear();
        passwordField.clear();

    }

    @FXML
    void showEditUserForm(ActionEvent event) {
        Utilisateur selected = userTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            // Charger les données à partir de la base si nécessaire
            Utilisateur utilisateurFromDb = service.findById(selected.getId());

            formContainer.setVisible(true);
            formTitle.setText("Modifier l'ingénieur");

            cinField.setText(utilisateurFromDb.getId());
            nomField.setText(utilisateurFromDb.getNom());
            prenomField.setText(utilisateurFromDb.getPrenom());
            sexeField.setValue(utilisateurFromDb.getSex());
            telephoneField.setText(utilisateurFromDb.getTelephone());
            emailField.setText(utilisateurFromDb.getEmail());
            passwordField.clear(); // Ne jamais afficher le mot de passe existant

        }

    }

    @FXML
    void initialize() {
        assert addButton != null : "fx:id=\"addButton\" was not injected: check your FXML file 'userEngenier.fxml'.";
        assert adminCountLabel != null : "fx:id=\"adminCountLabel\" was not injected: check your FXML file 'userEngenier.fxml'.";
        assert deleteButton != null : "fx:id=\"deleteButton\" was not injected: check your FXML file 'userEngenier.fxml'.";
        assert editButton != null : "fx:id=\"editButton\" was not injected: check your FXML file 'userEngenier.fxml'.";
        assert emailCol != null : "fx:id=\"emailCol\" was not injected: check your FXML file 'userEngenier.fxml'.";
        assert emailField != null : "fx:id=\"emailField\" was not injected: check your FXML file 'userEngenier.fxml'.";
        assert formContainer != null : "fx:id=\"formContainer\" was not injected: check your FXML file 'userEngenier.fxml'.";
        assert formTitle != null : "fx:id=\"formTitle\" was not injected: check your FXML file 'userEngenier.fxml'.";
        assert idCol != null : "fx:id=\"idCol\" was not injected: check your FXML file 'userEngenier.fxml'.";
        assert idField != null : "fx:id=\"idField\" was not injected: check your FXML file 'userEngenier.fxml'.";
        assert nomCol != null : "fx:id=\"nomCol\" was not injected: check your FXML file 'userEngenier.fxml'.";
        assert nomField != null : "fx:id=\"nomField\" was not injected: check your FXML file 'userEngenier.fxml'.";
        assert passwordField != null : "fx:id=\"passwordField\" was not injected: check your FXML file 'userEngenier.fxml'.";
        assert prenomCol != null : "fx:id=\"prenomCol\" was not injected: check your FXML file 'userEngenier.fxml'.";
        assert prenomField != null : "fx:id=\"prenomField\" was not injected: check your FXML file 'userEngenier.fxml'.";
        assert sexeCol != null : "fx:id=\"sexeCol\" was not injected: check your FXML file 'userEngenier.fxml'.";
        assert sexeField != null : "fx:id=\"sexeField\" was not injected: check your FXML file 'userEngenier.fxml'.";
        assert telephoneCol != null : "fx:id=\"telephoneCol\" was not injected: check your FXML file 'userEngenier.fxml'.";
        assert telephoneField != null : "fx:id=\"telephoneField\" was not injected: check your FXML file 'userEngenier.fxml'.";
        assert totalUsersLabel != null : "fx:id=\"totalUsersLabel\" was not injected: check your FXML file 'userEngenier.fxml'.";
        assert userTable != null : "fx:id=\"userTable\" was not injected: check your FXML file 'userEngenier.fxml'.";
        sexeField.getItems().addAll("Homme", "Femme");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomCol.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        sexeCol.setCellValueFactory(new PropertyValueFactory<>("sex"));
        telephoneCol.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

        chargerTable();
        // 2. Remplir les données depuis le service

        List<Utilisateur> utilisateurs = service.getAllDataIngenieur();
        ObservableList<Utilisateur> observableList = FXCollections.observableArrayList(utilisateurs);
        userTable.setItems(observableList);

        // 3. Mettre à jour le compteur de vétérinaires
        totalUsersLabel.setText("Total Utilisateurs: " + utilisateurs.size());

        userTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean userSelected = newSelection != null;
            editButton.setDisable(!userSelected);
            deleteButton.setDisable(!userSelected);});
    }

    private void chargerTable() {

        ObservableList<Utilisateur> liste = FXCollections.observableArrayList(service.getAllDataVeterinaire());

        // Lier les colonnes aux propriétés de Utilisateur
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomCol.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        sexeCol.setCellValueFactory(new PropertyValueFactory<>("sex"));
        telephoneCol.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email")); // Ajoute ça si email est utilisé

        userTable.setItems(liste);
    }

}
