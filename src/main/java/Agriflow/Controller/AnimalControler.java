/*package Agriflow.Controller;

import Agriflow.Models.Animal;
import Agriflow.Models.Champ;
import Agriflow.Models.enums.AnimalIdentificationType;
import Agriflow.Models.enums.AnimalSpecies;
import Agriflow.Models.enums.AnimalHealthStatus;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import Agriflow.Service.IService; // Import IService
import Agriflow.Service.ServiceAnimal; // Import ServiceAnimal

import java.sql.SQLException;
import java.util.List;

public class AnimalControler {

    @FXML
    private TableView<Animal> tableAnimaux;

    @FXML
    private TableColumn<Animal, Integer> colId;

    @FXML
    private TableColumn<Animal, String> colEspece;

    @FXML
    private TableColumn<Animal, String> colIdentification;

    @FXML
    private TableColumn<Animal, String> colEtatSante;

    @FXML
    private TableColumn<Animal, String> colChamp;

    @FXML
    private TextField especeField;

    @FXML
    private ComboBox<AnimalIdentificationType> identificationComboBox;

    @FXML
    private ComboBox<AnimalHealthStatus> etatSanteComboBox;

    @FXML
    private ComboBox<Champ> champComboBox;

    @FXML
    private Button btnAjouter;

    @FXML
    private Button btnModifier;

    @FXML
    private Button btnSupprimer;

    private IService<Animal> animalService; // Use IService interface

    @FXML
    public void initialize() {
        animalService = new ServiceAnimal(); // Initialize AnimalService (now implements IService)

        // Configure Table Columns
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colEspece.setCellValueFactory(cellData -> {
            AnimalSpecies espece = cellData.getValue().getEspece(); // Get String value
            return new SimpleStringProperty(espece != null ? String.valueOf(espece) : "");
        });
        colIdentification.setCellValueFactory(cellData -> {
            AnimalIdentificationType identification = cellData.getValue().getIdentification(); // Get String
            return new SimpleStringProperty(identification != null ? String.valueOf(identification) : "");
        });
        colEtatSante.setCellValueFactory(cellData -> {
            AnimalHealthStatus etatSante = cellData.getValue().getEtatSante();   // Get String
            return new SimpleStringProperty(etatSante != null ? String.valueOf(etatSante) : "");
        });
        colChamp.setCellValueFactory(cellData -> {
            Champ c = cellData.getValue().getChamp();
            return new SimpleStringProperty(c != null ? c.getNom() : "Aucun");
        });

        // Populate ComboBoxes
        identificationComboBox.setItems(FXCollections.observableArrayList(AnimalIdentificationType.values()));
        etatSanteComboBox.setItems(FXCollections.observableArrayList(AnimalHealthStatus.values()));
        loadChamps(); // Load available champs into the ComboBox

        // Load initial animal data
        loadAnimaux();

        // Add listener for table selection to populate fields for editing
        tableAnimaux.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                populateFields(newSelection);
            } else {
                clearFields();
            }
        });
    }

    private void loadChamps() {
        List<Champ> champs = ((ServiceAnimal) animalService).getAllChamps(); // Corrected and casted
        champComboBox.setItems(FXCollections.observableArrayList(champs));

        // Set a StringConverter to display Champ names in the ComboBox
        champComboBox.setConverter(new javafx.util.StringConverter<Champ>() {
            @Override
            public String toString(Champ champ) {
                return champ == null ? null : champ.getNom();
            }

            @Override
            public Champ fromString(String string) {
                // Not needed for display, but required by the converter
                return champComboBox.getItems().stream()
                        .filter(champ -> champ.getNom().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });
    }

    @FXML
    void ajouterAnimal() {
        String especeText = especeField.getText();
        AnimalIdentificationType identificationType = identificationComboBox.getValue();
        AnimalHealthStatus healthStatus = etatSanteComboBox.getValue();
        Champ selectedChamp = champComboBox.getValue();

        if (especeText.isEmpty() || identificationType == null || healthStatus == null || selectedChamp == null) {
            // Show an alert if any field is empty
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs.");
            alert.showAndWait();
            return;
        }

        Animal animal = new Animal();
        try {
            animal.setEspece(String.valueOf(AnimalSpecies.valueOf(especeText.toUpperCase()))); // Convert text to enum
            animal.setIdentification(String.valueOf(identificationType));
            animal.setEtatSante(String.valueOf(healthStatus));
            animal.setChamp(selectedChamp);

            animalService.create(animal);
            loadAnimaux();
            clearFields();
        } catch (IllegalArgumentException e) {
            // Handle the case where the entered species is not a valid enum value
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText(null);
            alert.setContentText("L'espèce entrée n'est pas valide.");
            alert.showAndWait();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de base de données");
            alert.setHeaderText("Erreur lors de l'ajout de l'animal");
            alert.setContentText("Une erreur s'est produite lors de l'ajout de l'animal à la base de données : " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void modifierAnimal() {
        Animal selectedAnimal = tableAnimaux.getSelectionModel().getSelectedItem();
        if (selectedAnimal != null) {
            String especeText = especeField.getText();
            AnimalIdentificationType identificationType = identificationComboBox.getValue();
            AnimalHealthStatus healthStatus = etatSanteComboBox.getValue();
            Champ selectedChamp = champComboBox.getValue();

            if (especeText.isEmpty() || identificationType == null || healthStatus == null || selectedChamp == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur de saisie");
                alert.setHeaderText(null);
                alert.setContentText("Veuillez remplir tous les champs.");
                alert.showAndWait();
                return;
            }

            try {
                selectedAnimal.setEspece(String.valueOf(AnimalSpecies.valueOf(especeText.toUpperCase())));
                selectedAnimal.setIdentification(String.valueOf(identificationType));
                selectedAnimal.setEtatSante(String.valueOf(healthStatus));
                selectedAnimal.setChamp(selectedChamp);

                animalService.update(selectedAnimal);
                loadAnimaux();
                clearFields();
            } catch (IllegalArgumentException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur de saisie");
                alert.setHeaderText(null);
                alert.setContentText("L'espèce entrée n'est pas valide.");
                alert.showAndWait();
            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur de base de données");
                alert.setHeaderText("Erreur lors de la modification de l'animal");
                alert.setContentText("Une erreur s'est produite lors de la modification de l'animal dans la base de données : " + e.getMessage());
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Avertissement");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un animal à modifier.");
            alert.showAndWait();
        }
    }

    @FXML
    void supprimerAnimal() {
        Animal selectedAnimal = tableAnimaux.getSelectionModel().getSelectedItem();
        if (selectedAnimal != null) {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirmation de suppression");
            confirmationAlert.setHeaderText(null);
            confirmationAlert.setContentText("Êtes-vous sûr de vouloir supprimer cet animal ?");

            confirmationAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        animalService.delete(selectedAnimal.getId());
                        loadAnimaux();
                        clearFields();
                    } catch (SQLException e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Erreur de base de données");
                        alert.setHeaderText("Erreur lors de la suppression de l'animal");
                        alert.setContentText("Une erreur s'est produite lors de la suppression de l'animal de la base de données : " + e.getMessage());
                        alert.showAndWait();
                    }
                }
            });
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Avertissement");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un animal à supprimer.");
            alert.showAndWait();
        }
    }

    private void loadAnimaux() {
        try {
            List<Animal> animaux = animalService.readAll();
            tableAnimaux.setItems(FXCollections.observableArrayList(animaux));
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de base de données");
            alert.setHeaderText("Erreur lors du chargement des animaux");
            alert.setContentText("Une erreur s'est produite lors du chargement des animaux depuis la base de données : " + e.getMessage());
            alert.showAndWait();
        }
    }

    private void populateFields(Animal animal) {
        especeField.setText(String.valueOf(animal.getEspece()));
        identificationComboBox.setValue(AnimalIdentificationType.valueOf(String.valueOf(animal.getIdentification())));
        etatSanteComboBox.setValue(AnimalHealthStatus.valueOf(String.valueOf(animal.getEtatSante())));
        champComboBox.setValue(animal.getChamp());
    }

    private void clearFields() {
        especeField.clear();
        identificationComboBox.setValue(null);
        etatSanteComboBox.setValue(null);
        champComboBox.setValue(null);
        tableAnimaux.getSelectionModel().clearSelection();
    }

    private List<Champ> getAllChamps() {
        return ((ServiceAnimal) animalService).getAllChamps();
    }
}
*/