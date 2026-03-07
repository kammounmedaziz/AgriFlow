package gestions.ferme.controllers;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import gestions.ferme.entities.Champs;
import gestions.ferme.services.ServiceChamp;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

public class WindowChampController {

    @FXML private ResourceBundle resources;
    @FXML private URL location;
    @FXML private Button ajoutButton;

    @FXML private TableView<Champs> champTable;
    @FXML private TableColumn<Champs, String> datePlantationCol;
    @FXML private DatePicker datePlantationField;
    @FXML private TableColumn<Champs, String> dateRecolteCol;
    @FXML private DatePicker dateRecolteField;
    @FXML private Button deleteButton;
    @FXML private Button editButton;
    @FXML private VBox formContainer;
    @FXML private Label formTitle;
    @FXML private TableColumn<Champs, Integer> idCol;
    @FXML private TableColumn<Champs, String> nomCol;
    @FXML private TextField nomField;
    @FXML private TableColumn<Champs, String> etatCol;
    @FXML private ComboBox<String> etatField;
    @FXML private TableColumn<Champs, Float> superficieCol;
    @FXML private TextField superficieField;
    @FXML private Label totalSurfaceLabel;
    @FXML private TableColumn<Champs, Integer> traitementsCol;
    @FXML private Spinner<Integer> traitementsField;
    @FXML private TableColumn<Champs, String> typePlanteCol;
    @FXML private ComboBox<String> typePlanteField;
    @FXML private TableColumn<Champs, String> typeSolCol;
    @FXML private ComboBox<String> typeSolField;
    @FXML private TableColumn<Champs, String> typeUtilisationCol;
    @FXML private ComboBox<String> typeUtilisationField;

    private ServiceChamp serviceChamp = new ServiceChamp();

    @FXML
    void deleteChamp(ActionEvent event) {
        Champs selectedChamp = champTable.getSelectionModel().getSelectedItem();
        if (selectedChamp != null) {
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirmation de suppression");
            confirmation.setHeaderText("Supprimer ce champ ?");
            confirmation.setContentText("Êtes-vous sûr de vouloir supprimer ce champ ?");

            Optional<ButtonType> result = confirmation.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                serviceChamp.supprimerEntity(selectedChamp);
                populateTable();
            }
        } else {
            showAlert("Aucun champ sélectionné", "Veuillez sélectionner un champ à supprimer.");
        }
    }

    @FXML
    void handleReturn(ActionEvent event) {
        // Implémentez la navigation retour si nécessaire
    }

    @FXML
    void hideForm(ActionEvent event) {
        formContainer.setVisible(false);
    }

    @FXML
    void saveChamp(ActionEvent event) {
        try {
            Champs champ = new Champs();

            champ.setNom(nomField.getText().trim());
            champ.setSuperficie(Float.parseFloat(superficieField.getText().trim()));
            champ.setType_utilisation(typeUtilisationField.getValue());
            champ.setType_sol(typeSolField.getValue());
            champ.setType_plante(typePlanteField.getValue());
            champ.setEtat(etatField.getValue());
            champ.setN_traitement(traitementsField.getValue() != null ? traitementsField.getValue() : 0);
            champ.setDate_plantation(datePlantationField.getValue() != null ?
                    datePlantationField.getValue().toString() : null);
            champ.setDate_recolte(dateRecolteField.getValue() != null ?
                    dateRecolteField.getValue().toString() : null);

            Champs selectedChamp = champTable.getSelectionModel().getSelectedItem();
            if (selectedChamp != null) {
                champ.setId(selectedChamp.getId());
                serviceChamp.updateEntity(champ.getId(), champ);

                showInfoAlert("Modification réussie", "Le champ a été modifié avec succès.");
            } else {
                serviceChamp.ajouterEntity(champ);

                showInfoAlert("Ajout réussi", "Le nouveau champ a été ajouté avec succès.");
            }

            formContainer.setVisible(false);
            populateTable();

        } catch (NumberFormatException e) {
            showAlert("Erreur de format", "Veuillez entrer des valeurs numériques valides pour la superficie et la quantité.");
        } catch (Exception e) {
            showAlert("Erreur", "Une erreur est survenue lors de l'enregistrement: " + e.getMessage());
        }
    }

    @FXML
    void showAddChampForm(ActionEvent event) {
        formContainer.setVisible(true);
        formTitle.setText("Ajouter un nouveau champ");
        clearForm();

        // Réactivation des champs pour ajout
        nomField.setDisable(false);
        superficieField.setDisable(false);

        etatField.setDisable(true);
        traitementsField.setDisable(true);
        datePlantationField.setDisable(true);
        dateRecolteField.setDisable(true);

        champTable.getSelectionModel().clearSelection();
    }

    @FXML
    void showEditChampForm(ActionEvent event) {
        Champs selectedChamp = champTable.getSelectionModel().getSelectedItem();
        if (selectedChamp != null) {
            formContainer.setVisible(true);
            formTitle.setText("Modifier le champ");
            fillForm(selectedChamp);

            // Désactivation des champs non modifiables en modification
            nomField.setDisable(true);
            superficieField.setDisable(true);
            typePlanteField.setDisable(true);
            typeSolField.setDisable(true);

            typeUtilisationField.setDisable(true);
            etatField.setDisable(false);
            traitementsField.setDisable(true);
            datePlantationField.setDisable(false);
            dateRecolteField.setDisable(false);
        } else {
            showAlert("Aucun champ sélectionné", "Veuillez sélectionner un champ à modifier.");
        }
    }

    @FXML
    void initialize() {
        initializeAssertions();

        traitementsField.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0));

        initializeComboBoxes();

        configureTableColumns();

        populateTable();

        setupSelectionListener();
    }

    private void initializeAssertions() {
        assert ajoutButton != null : "fx:id=\"ajoutButton\" was not injected: check your FXML file 'champs.fxml'.";
        // Ajoutez toutes les autres assertions nécessaires...
    }

    private void initializeComboBoxes() {
        typePlanteField.getItems().addAll(serviceChamp.getAllTypePlante());
        typeSolField.getItems().addAll(serviceChamp.getAllTypeSol());
        typeUtilisationField.getItems().addAll(serviceChamp.getAllTypeUtilisation());

        setupComboBoxHandler(typePlanteField, "Nouveau Type de Plante");
        setupComboBoxHandler(typeSolField, "Nouveau Type de Sol");
        setupComboBoxHandler(typeUtilisationField, "Nouveau Type d'Utilisation");
    }

    private void setupComboBoxHandler(ComboBox<String> comboBox, String dialogTitle) {
        comboBox.setOnAction(event -> {
            if ("Autre".equals(comboBox.getValue())) {
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle(dialogTitle);
                dialog.setHeaderText("Entrez une nouvelle valeur");
                dialog.setContentText("Valeur :");

                Optional<String> result = dialog.showAndWait();
                result.ifPresent(newValue -> {
                    if (!newValue.trim().isEmpty()) {
                        comboBox.getItems().add(comboBox.getItems().size() - 1, newValue);
                        comboBox.setValue(newValue);
                    } else {
                        comboBox.getSelectionModel().clearSelection();
                    }
                });
            }
        });
    }

    private void configureTableColumns() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        superficieCol.setCellValueFactory(new PropertyValueFactory<>("superficie"));
        typeUtilisationCol.setCellValueFactory(new PropertyValueFactory<>("type_utilisation"));
        typeSolCol.setCellValueFactory(new PropertyValueFactory<>("type_sol"));
        typePlanteCol.setCellValueFactory(new PropertyValueFactory<>("type_plante"));
        etatCol.setCellValueFactory(new PropertyValueFactory<>("etat"));

        datePlantationCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDate_plantation() != null ?
                        cellData.getValue().getDate_plantation() : "Non planté"));

        dateRecolteCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDate_recolte() != null ?
                        cellData.getValue().getDate_recolte() : "Non récolté"));

        traitementsCol.setCellValueFactory(cellData -> {
            Integer traitements = cellData.getValue().getNombre_traitement_traitement();
            return new SimpleObjectProperty<>(traitements != null ? traitements : 0);
        });
    }

    private void populateTable() {
        List<Champs> champs = serviceChamp.getAllData();
        champTable.getItems().setAll(champs);
        updateStats();
    }

    private void updateStats() {
        float totalSurface = (float) champTable.getItems().stream()
                .mapToDouble(Champs::getSuperficie)
                .sum();
        totalSurfaceLabel.setText(String.format("Superficie totale: %.2f ha", totalSurface));

        // Calcul du rendement moyen si nécessaire
        // ...
    }

    private void setupSelectionListener() {
        champTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean rowSelected = newSelection != null;
            deleteButton.setDisable(!rowSelected);
            editButton.setDisable(!rowSelected);
        });

        deleteButton.setDisable(true);
        editButton.setDisable(true);
    }

    private void clearForm() {
        nomField.clear();
        superficieField.clear();
        etatField.getSelectionModel().clearSelection();
        typePlanteField.getSelectionModel().clearSelection();
        typeSolField.getSelectionModel().clearSelection();
        typeUtilisationField.getSelectionModel().clearSelection();
        datePlantationField.setValue(null);
        dateRecolteField.setValue(null);
        traitementsField.getValueFactory().setValue(0);
    }

    private void fillForm(Champs champ) {
        nomField.setText(champ.getNom());
        superficieField.setText(String.valueOf(champ.getSuperficie()));
        typePlanteField.setValue(champ.getType_plante());
        typeSolField.setValue(champ.getType_sol());
        typeUtilisationField.setValue(champ.getType_utilisation());

        if (champ.getDate_plantation() != null) {
            datePlantationField.setValue(LocalDate.parse(champ.getDate_plantation()));
        }

        if (champ.getDate_recolte() != null) {
            dateRecolteField.setValue(LocalDate.parse(champ.getDate_recolte()));
        }

        etatField.setValue(champ.getEtat());

        traitementsField.getValueFactory().setValue(champ.getNombre_traitement_traitement());
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfoAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
