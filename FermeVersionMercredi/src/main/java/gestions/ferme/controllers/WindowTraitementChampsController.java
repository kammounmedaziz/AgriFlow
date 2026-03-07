package gestions.ferme.controllers;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import gestions.ferme.entities.Champs;
import gestions.ferme.services.ServiceChamp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

public class WindowTraitementChampsController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button BtnDcnx;


    @FXML
    private Spinner qteMedicament;

    @FXML
    private TableColumn<Champs, String > EtatCol;

    @FXML
    private TableColumn<Champs, String> MedicCol;

    @FXML
    private ToggleButton btnAjoutTraitement;



    @FXML
    private Button btnSave;

    @FXML
    private ToggleButton btnTraitement;

    @FXML
    private ToggleButton btnListeRapel;

    @FXML
    private ComboBox<String> comboEtat;

    @FXML
    private TableColumn<Champs, String> dateCol;

    @FXML
    private TextField etatCroi;

    @FXML
    private TableColumn<Champs, String> idCol;

    @FXML
    private TextField nomChamps;



    @FXML
    private TableColumn<Champs, Float> qteCol;

    @FXML
    private GridPane qteMedica;

    @FXML
    private TableView<Champs> treatmentTable;

    @FXML
    private TextField typeMa;

    ServiceChamp serviceChamp = new ServiceChamp();

    @FXML
    void AjoutTraitement(ActionEvent event) {
        Champs selectedChamp = treatmentTable.getSelectionModel().getSelectedItem();
        if (selectedChamp != null) {
            // Récupère le champ complet depuis la base à l’aide de l’ID
            Champs champFromDb = serviceChamp.getChampById(selectedChamp.getId());
            if (champFromDb != null) {
                nomChamps.setText(champFromDb.getNom());
                comboEtat.setValue(champFromDb.getEtat());

            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger les données du champ.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Sélection requise", "Veuillez sélectionner un champ dans la liste.");
        }

    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void ListeRapel(ActionEvent event) {
        List<Champs> list = serviceChamp.getAllDataTraitement1();
        treatmentTable.getItems().setAll(list);

    }

    @FXML
    void Traitement(ActionEvent event) {
        List<Champs> list = serviceChamp.getAllDataTraitement0();
        treatmentTable.getItems().setAll(list);

    }

    @FXML
    void cancelTreatment(ActionEvent event) {

    }

    @FXML
    void closeForm(ActionEvent event) {

    }

    @FXML
    void handleSignOut(ActionEvent event) {

    }

    @FXML
    void saveTreatment(ActionEvent event) {

        Champs selectedChamp = treatmentTable.getSelectionModel().getSelectedItem();

        if (selectedChamp == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Aucun champ sélectionné.");
            return;
        }

        int idChamp = selectedChamp.getId();
        String stadeCroissance = etatCroi.getText().trim();
        String typeMedicament = typeMa.getText().trim();
        String etatChamp = comboEtat.getValue();
        float quantite = Float.parseFloat(qteMedicament.getValue().toString());

        if (stadeCroissance.isEmpty() || typeMedicament.isEmpty() || etatChamp == null) {
            showAlert(Alert.AlertType.WARNING, "Champs manquants", "Veuillez remplir tous les champs.");
            return;
        }

        // Récupérer la quantité depuis le Spinner;
        try {
            quantite = Float.parseFloat(qteMedicament.getValue().toString());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Quantité invalide", "Veuillez entrer une quantité valide.");
            return;
        }

        // Appel du service
        ServiceChamp service = new ServiceChamp();
        service.AffecterTraitementChamps(idChamp, stadeCroissance, quantite, typeMedicament);

        showAlert(Alert.AlertType.INFORMATION, "Succès", "Traitement affecté avec succès.");
        loadTreatmentTable(); // Recharger les données

        // Nettoyage des champs
        etatCroi.clear();
        typeMa.clear();
        comboEtat.getSelectionModel().clearSelection();

    }


    @FXML
    void initialize() {
        assert BtnDcnx != null : "fx:id=\"BtnDcnx\" was not injected: check your FXML file 'traitement champ.fxml'.";
        assert qteMedicament != null : "fx:id=\"Etat\" was not injected: check your FXML file 'traitement champ.fxml'.";
        assert EtatCol != null : "fx:id=\"EtatCol\" was not injected: check your FXML file 'traitement champ.fxml'.";
        assert MedicCol != null : "fx:id=\"MedicCol\" was not injected: check your FXML file 'traitement champ.fxml'.";
        assert btnAjoutTraitement != null : "fx:id=\"btnAjoutTraitement\" was not injected: check your FXML file 'traitement champ.fxml'.";
        assert btnListeRapel != null : "fx:id=\"btnListeRapel\" was not injected: check your FXML file 'traitement champ.fxml'.";
        assert btnSave != null : "fx:id=\"btnSave\" was not injected: check your FXML file 'traitement champ.fxml'.";
        assert btnTraitement != null : "fx:id=\"btnTraitement\" was not injected: check your FXML file 'traitement champ.fxml'.";
        assert comboEtat != null : "fx:id=\"comboEtat\" was not injected: check your FXML file 'traitement champ.fxml'.";
        assert dateCol != null : "fx:id=\"dateCol\" was not injected: check your FXML file 'traitement champ.fxml'.";
        assert etatCroi != null : "fx:id=\"etatCroi\" was not injected: check your FXML file 'traitement champ.fxml'.";
        assert idCol != null : "fx:id=\"idCol\" was not injected: check your FXML file 'traitement champ.fxml'.";
        assert nomChamps != null : "fx:id=\"nomChamps\" was not injected: check your FXML file 'traitement champ.fxml'.";
        assert qteCol != null : "fx:id=\"qteCol\" was not injected: check your FXML file 'traitement champ.fxml'.";
        assert qteMedica != null : "fx:id=\"qteMedica\" was not injected: check your FXML file 'traitement champ.fxml'.";
        assert treatmentTable != null : "fx:id=\"treatmentTable\" was not injected: check your FXML file 'traitement champ.fxml'.";
        assert typeMa != null : "fx:id=\"typeMa\" was not injected: check your FXML file 'traitement champ.fxml'.";

        comboEtat.getItems().addAll("Champ Sain", "Champ Malade","champ en cours de traitement");

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000, 1);
        qteMedicament.setValueFactory(valueFactory);
        qteMedicament.setEditable(true);

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        MedicCol.setCellValueFactory(new PropertyValueFactory<>("type_plante"));  // Remplacer si besoin
        EtatCol.setCellValueFactory(new PropertyValueFactory<>("etat"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date_plantation"));// À remplacer si nécessaire

        // Charger les données dans le tableau
        loadTreatmentTable();

        // Activer btnTraitement uniquement si des données sont présentes
        btnTraitement.setDisable(treatmentTable.getItems().isEmpty());

        btnTraitement.setDisable(treatmentTable.getItems().isEmpty());
        btnAjoutTraitement.setDisable(true); // Désactivé par défaut

        treatmentTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            boolean disable = (newVal == null);
            btnTraitement.setDisable(disable);
            btnAjoutTraitement.setDisable(disable);// Active/désactive selon sélection
            qteMedicament.setDisable(disable);
        });

    }

    private void loadTreatmentTable() {
        List<Champs> champsList = serviceChamp.getAllDataTraitement0();
        treatmentTable.getItems().setAll(champsList);
    }







}
