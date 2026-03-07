package gestions.ferme.controllers;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import gestions.ferme.entities.Animal;
import gestions.ferme.services.ServiceAnimal;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

public class WindowAnimalController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button AjoutButton;

    @FXML
    private Label animalCountLabel;

    @FXML
    private TableView<Animal> animalTable;

    @FXML
    private ComboBox<String> etatAnimalCombo;

    @FXML
    private TableColumn<Animal, String> dateNaissanceCol;

    @FXML
    private DatePicker dateNaissanceField;

    @FXML
    private Button deleteButton;

    @FXML
    private Button editButton;

    @FXML
    private TableColumn<Animal, String> especeCol;

    @FXML
    private ComboBox<String> especeField;

    @FXML
    private VBox formContainer;

    @FXML
    private Label formTitle;

    @FXML
    private TableColumn<Animal, Integer> idCol;

    @FXML
    private TableColumn<Animal, String> raceCol;

    @FXML
    private TextField raceField;

    @FXML
    private TableColumn<Animal, String> sexeCol;

    @FXML
    private ComboBox<String> sexeField;

    @FXML
    private TableColumn<Animal, Integer> traitementsCol;

    @FXML
    private Spinner<Integer> traitementsField;

    ServiceAnimal serviceAnimal = new ServiceAnimal();

    @FXML
    void deleteAnimal(ActionEvent event) {
        Animal selectedAnimal = animalTable.getSelectionModel().getSelectedItem();
        if (selectedAnimal != null) {
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirmation de suppression");
            confirmation.setHeaderText("Voulez-vous vraiment supprimer cet animal ?");
            confirmation.setContentText("Cette action est irréversible.");

            Optional<ButtonType> result = confirmation.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                serviceAnimal.supprimerEntity(selectedAnimal.getId());
                animalTable.getItems().remove(selectedAnimal);
                animalCountLabel.setText("Total d'animaux : " + animalTable.getItems().size());

                showInfo("Suppression réussie", "L'animal a été supprimé.");
            }
        } else {
            showAlert("Aucun animal sélectionné", "Veuillez sélectionner un animal à supprimer.");
        }
    }

    @FXML
    void handleReturn(ActionEvent event) {
        // À compléter si nécessaire
    }

    @FXML
    void hideForm(ActionEvent event) {
        formContainer.setVisible(false);
    }

    @FXML
    void saveAnimal(ActionEvent event) {
        String espece = especeField.getValue();
        String race = raceField.getText();
        String sexeSelectionne = sexeField.getValue();
        String dateNaissance = (dateNaissanceField.getValue() != null) ? dateNaissanceField.getValue().toString() : null;
        String etat = etatAnimalCombo.getValue();

        if (espece == null || race == null || race.isBlank() || sexeSelectionne == null || dateNaissance == null || etat == null) {
            showAlert("Champs requis manquants", "Veuillez remplir tous les champs du formulaire.");
            return;
        }

        int traitements = traitementsField.getValue();
        Animal selectedAnimal = animalTable.getSelectionModel().getSelectedItem();

        if (selectedAnimal != null) {
            selectedAnimal.setEspece(espece);
            selectedAnimal.setRace(race);
            selectedAnimal.setSexe(sexeSelectionne);
            selectedAnimal.setDateNaissance(dateNaissance);
            selectedAnimal.setTraitements(traitements);
            selectedAnimal.setEtat(etat);

            serviceAnimal.updateEntity(selectedAnimal);
            showInfo("Modification réussie", "L'animal a été modifié avec succès.");
        } else {
            Animal nouvelAnimal = new Animal();
            nouvelAnimal.setEspece(espece);
            nouvelAnimal.setRace(race);
            nouvelAnimal.setSexe(sexeSelectionne);
            nouvelAnimal.setDateNaissance(dateNaissance);
            nouvelAnimal.setTraitements(traitements);
            nouvelAnimal.setEtat(etat);

            serviceAnimal.ajouterEntity(nouvelAnimal);
            showInfo("Ajout réussi", "Un nouvel animal a été ajouté.");
        }

        formContainer.setVisible(false);
        populateTable();
    }

    @FXML
    void showAddAnimalForm(ActionEvent event) {
        formContainer.setVisible(true);
        formTitle.setText("Ajouter un nouvel animal");
        especeField.getSelectionModel().clearSelection();
        raceField.clear();
        sexeField.getSelectionModel().clearSelection();
        dateNaissanceField.setValue(null);
        etatAnimalCombo.setDisable(true);
    }

    @FXML
    void showEditAnimalForm(ActionEvent event) {
        Animal selectedAnimal = animalTable.getSelectionModel().getSelectedItem();
        if (selectedAnimal != null) {
            Animal animalFromDb = serviceAnimal.recupererAnimalParId(selectedAnimal.getId());
            if (animalFromDb != null) {
                especeField.setValue(animalFromDb.getEspece());
                raceField.setText(animalFromDb.getRace());
                sexeField.setValue(animalFromDb.getSexe());
                dateNaissanceField.setValue(LocalDate.parse(animalFromDb.getDateNaissance()));
                traitementsField.getValueFactory().setValue(animalFromDb.getTraitements());
                etatAnimalCombo.setValue(animalFromDb.getEtat());
                etatAnimalCombo.setDisable(false); // Permet de choisir l'état lors de la modif

                formContainer.setVisible(true);
                formTitle.setText("Modifier l'animal");
            }
        } else {
            showAlert("Aucun animal sélectionné", "Veuillez sélectionner un animal à modifier.");
        }
    }

    @FXML
    void initialize() {
        assert AjoutButton != null : "fx:id=\"AjoutButton\" was not injected.";
        assert animalCountLabel != null;
        assert animalTable != null;
        assert dateNaissanceCol != null;
        assert dateNaissanceField != null;
        assert deleteButton != null;
        assert editButton != null;
        assert especeCol != null;
        assert especeField != null;
        assert formContainer != null;
        assert formTitle != null;
        assert idCol != null;
        assert raceCol != null;
        assert raceField != null;
        assert sexeCol != null;
        assert sexeField != null;
        assert traitementsCol != null;
        assert traitementsField != null;

        traitementsField.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0));
        traitementsField.setDisable(true);

        especeField.getItems().addAll(serviceAnimal.getAllEspeces());
        especeField.setOnAction(event -> {
            String selected = especeField.getValue();
            if ("Autre".equals(selected)) {
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Nouvelle espèce");
                dialog.setHeaderText("Entrez une nouvelle espèce");
                dialog.setContentText("Espèce :");

                Optional<String> result = dialog.showAndWait();
                result.ifPresent(newEspece -> {
                    if (!newEspece.trim().isEmpty()) {
                        especeField.getItems().add(especeField.getItems().size() - 1, newEspece);
                        especeField.setValue(newEspece);
                    } else {
                        especeField.getSelectionModel().clearSelection();
                    }
                });
            }
        });

        sexeField.getItems().addAll("Male", "Female");
        populateTable();

        animalTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean rowSelected = newSelection != null;
            deleteButton.setDisable(!rowSelected);
            editButton.setDisable(!rowSelected);
        });

        deleteButton.setDisable(true);
        editButton.setDisable(true);
    }

    private void populateTable() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        especeCol.setCellValueFactory(new PropertyValueFactory<>("espece"));
        raceCol.setCellValueFactory(new PropertyValueFactory<>("race"));
        sexeCol.setCellValueFactory(new PropertyValueFactory<>("sexe"));
        dateNaissanceCol.setCellValueFactory(new PropertyValueFactory<>("dateNaissance"));
        traitementsCol.setCellValueFactory(new PropertyValueFactory<>("traitements"));

        List<Animal> animals = serviceAnimal.getAllData();
        animalTable.getItems().setAll(animals);
        animalCountLabel.setText("Total d'animaux : " + animals.size());
    }

    private void showAlert(String titre, String contenu) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(contenu);
        alert.showAndWait();
    }

    private void showInfo(String titre, String contenu) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(contenu);
        alert.showAndWait();
    }
}
