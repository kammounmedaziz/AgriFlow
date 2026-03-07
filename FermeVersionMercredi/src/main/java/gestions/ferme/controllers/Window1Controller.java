package gestions.ferme.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

import gestions.ferme.entities.Capteurs;
import gestions.ferme.services.Services;
import gestions.ferme.utils.TextFieldAPIValidator;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;
import org.json.JSONObject;

public class Window1Controller {

    // Éléments du header
    @FXML private HBox header;
    @FXML private ImageView logo;
    @FXML private Label title;
    @FXML private Button cartButton;

    // Sidebar gauche
    @FXML private VBox actionSidebar;
    @FXML private Button addCapteurButton;
    @FXML private Button editButton;
    @FXML private Button deleteButton;
    @FXML private Label totalCapteursLabel;
    @FXML private Label activeCapteursLabel;

    // Table centrale
    @FXML private TableView<Capteurs> capteurTable;
    @FXML private TableColumn<Capteurs, String> idCol;
    @FXML private TableColumn<Capteurs, String> nomCol;
    @FXML private TableColumn<Capteurs, String> typeCol;
    @FXML private TableColumn<Capteurs, String> emplacementCol;
    @FXML private TableColumn<Capteurs, String> statutCol;
    @FXML private TableColumn<Capteurs, String> actionsCol;

    // Formulaire en bas
    @FXML private VBox formContainer;
    @FXML private Label formTitle;
    @FXML private TextField nomField;
    @FXML private ComboBox<String> typeField;
    @FXML private TextField emplacementField;
    @FXML private WebView mapView;
    @FXML private Button saveButton;

    private Services capteurService = new Services();
    private ObservableList<Capteurs> capteursList = FXCollections.observableArrayList();
    private boolean isEditMode = false;
    private Capteurs capteurSelectionne;

    @FXML
    void initialize() {
        // Initialisation de la table
        configurerTable();

        // Initialisation de la carte
        initialiserCarte();

        // Initialisation du combo box des types
        typeField.getItems().addAll("Température/Humidité", "ultrason");

        // Désactiver les boutons tant qu'aucun capteur n'est sélectionné
        editButton.setDisable(true);
        deleteButton.setDisable(true);

        // Gestion de la sélection dans la table
        capteurTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            System.out.println("Selection changed - Old: " + oldSelection + ", New: " + newSelection);
            capteurSelectionne = newSelection;
            editButton.setDisable(newSelection == null);
            deleteButton.setDisable(newSelection == null);
            System.out.println("Edit button disabled: " + editButton.isDisabled());
        });

        // Ajouter la validation des gros mots uniquement au champ nom
        TextFieldAPIValidator.addProfanityListener(nomField);
        // Ne pas ajouter la validation à emplacementField
    }

    private void configurerTable() {
        // Configuration des colonnes sans utiliser les Property
        idCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject().asString());
        nomCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom()));
        typeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTypes()));
        emplacementCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmplacements()));

        // Si vous avez un statut dans votre table mais pas dans l'entité, vous pouvez l'ajouter comme ceci :
        statutCol.setCellValueFactory(cellData -> new SimpleStringProperty("Actif")); // Valeur par défaut ou à adapter

        chargerDonneesCapteurs();
    }

    private void initialiserCarte() {
        WebEngine engine = mapView.getEngine();
        engine.load(getClass().getResource("/capteur/map.html").toExternalForm());

        engine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                JSObject window = (JSObject) engine.executeScript("window");
                window.setMember("app", this);
            }
        });
    }

    private void chargerDonneesCapteurs() {
        capteursList.setAll(capteurService.getAllData());
        capteurTable.setItems(capteursList);
        mettreAJourStatistiques();
    }

    private void mettreAJourStatistiques() {
        int total = capteursList.size();
        long actifs = capteursList.stream().filter(c -> "Actif".equals(c.getStatut())).count();
        totalCapteursLabel.setText("Total capteurs: " + total);
        activeCapteursLabel.setText("Capteurs actifs: " + actifs);
    }

    // Méthodes des boutons
    @FXML
    void handleReturn(ActionEvent event) {
        // Implémentez la navigation retour ici
    }

    @FXML
    void showAddCapteurForm(ActionEvent event) {
        isEditMode = false;
        formTitle.setText("Ajouter un capteur");
        viderFormulaire();
        formContainer.setVisible(true);
    }

    @FXML
    void showEditCapteurForm(ActionEvent event) {
        System.out.println("showEditCapteurForm called - capteurSelectionne: " + capteurSelectionne);
        if (capteurSelectionne != null) {
            isEditMode = true;
            formTitle.setText("Modifier le capteur");
            remplirFormulaire(capteurSelectionne);
            formContainer.setVisible(true);
            System.out.println("Form should be visible now");
        } else {
            System.out.println("No capteur selected!");
        }
    }

    @FXML
    void deleteCapteur(ActionEvent event) {
        if (capteurSelectionne != null) {
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirmation de suppression");
            confirmation.setHeaderText("Supprimer le capteur");
            confirmation.setContentText("Êtes-vous sûr de vouloir supprimer ce capteur ?");

            if (confirmation.showAndWait().get() == ButtonType.OK) {
                capteurService.supprimerEntity(capteurSelectionne.getId());
                chargerDonneesCapteurs();
            }
        }
    }

    @FXML
    void chooseLocation(ActionEvent event) {
        // La sélection se fait via la carte (méthode receiveCoords)
    }

    @FXML
    void hideForm(ActionEvent event) {
        formContainer.setVisible(false);
    }

    @FXML
    void saveCapteur(ActionEvent event) {
        System.out.println("Sauvegarde du capteur - Nom: " + nomField.getText()); // Log pour débogage
        
        // Valider uniquement le champ nom pour les gros mots
        TextFieldAPIValidator.validateNoProfanity(nomField, result -> {
            System.out.println("Résultat de la validation: " + (result ? "Valide" : "Invalide")); // Log pour débogage
            
            if (result) {
                // Si le nom est valide, procéder à la sauvegarde sans vérifier l'emplacement
                proceedWithSaving(event);
            } else {
                System.out.println("Sauvegarde annulée en raison d'un contenu inapproprié");
            }
            // Si le nom n'est pas valide, la sauvegarde est annulée
        });
    }

    private void proceedWithSaving(ActionEvent event) {
        Capteurs capteur = new Capteurs(
                nomField.getText(),
                typeField.getValue(),
                emplacementField.getText()
        );

        if (isEditMode && capteurSelectionne != null) {
            capteurService.updateEntity(capteurSelectionne.getId(), capteur);
        } else {
            capteurService.ajouterEntity(capteur);
        }

        chargerDonneesCapteurs();
        hideForm(event);
    }

    private void viderFormulaire() {
        nomField.clear();
        typeField.getSelectionModel().clearSelection();
        emplacementField.clear();
    }

    private void remplirFormulaire(Capteurs capteur) {
        nomField.setText(capteur.getNom());
        typeField.setValue(capteur.getTypes());
        emplacementField.setText(capteur.getEmplacements());
    }

    // Méthode appelée depuis JavaScript
    public void receiveCoords(double lat, double lng) {
        String adresse = obtenirAdresseDepuisCoordonnees(lat, lng);
        emplacementField.setText(adresse != null ? adresse : "Adresse inconnue");
    }

    private String obtenirAdresseDepuisCoordonnees(double lat, double lon) {
        try {
            String urlStr = String.format(
                java.util.Locale.US,
                "https://nominatim.openstreetmap.org/reverse?format=json&lat=%f&lon=%f&zoom=18&addressdetails=1&accept-language=fr",
                lat, lon
            );

            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");

            try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                JSONObject json = new JSONObject(response.toString());
                return json.getString("display_name");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @FXML
    public void showGraphiques(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/capteur/graphiques.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Erreur de navigation");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("Impossible d'ouvrir l'interface des graphiques: " + e.getMessage());
            errorAlert.showAndWait();
            e.printStackTrace();
        }
    }
}
