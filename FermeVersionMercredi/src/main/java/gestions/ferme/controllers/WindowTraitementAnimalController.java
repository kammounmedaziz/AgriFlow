package gestions.ferme.controllers;

import gestions.ferme.services.ServiceAnimal;
import gestions.ferme.entities.Animal;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class WindowTraitementAnimalController implements Initializable {

    @FXML
    private TableView<Animal> treatmentTable;
    
    @FXML
    private TableColumn<Animal, Integer> idCol;
    
    @FXML
    private TableColumn<Animal, String> EtatCol;
    
    @FXML
    private TableColumn<Animal, String> RaceCol;
    
    @FXML
    private TableColumn<Animal, String> dateCol;
    
    @FXML
    private ToggleButton btnAjoutTrai;
    
    @FXML
    private ToggleButton btnAnimauuxMalad;
    
    @FXML
    private ToggleButton btnRapelTraitement;
    
    @FXML
    private TextField idAnimalField;
    
    @FXML
    private ComboBox<String> etatAnimalCombo;
    
    @FXML
    private TextField typeTraitementField;
    
    @FXML
    private TextField typeMedicamentField;
    
    @FXML
    private TextField anomalieField;
    
    @FXML
    private VBox treatmentForm;
    
    private ServiceAnimal serviceAnimal = new ServiceAnimal();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Configuration des colonnes du tableau
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        EtatCol.setCellValueFactory(new PropertyValueFactory<>("etat"));
        RaceCol.setCellValueFactory(new PropertyValueFactory<>("race"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("dateNaissance"));
        

        
        // Charger les données initiales
        loadAnimauxMalades();
        
        // Ajouter un écouteur pour la sélection dans le tableau
        treatmentTable.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                // Activer le bouton d'ajout de traitement seulement si un animal est sélectionné
                btnAjoutTrai.setDisable(newValue == null);
            }
        );
        
        // Désactiver le bouton d'ajout de traitement par défaut (aucune sélection)
        btnAjoutTrai.setDisable(true);
        
        // Déboguer le service
        debugServiceAnimal();
    }
    
    @FXML
    void AnimauuxMalad(ActionEvent event) {
        try {
            // Récupérer la liste des animaux malades avec getAllData0
            List<Animal> animauxMalades = serviceAnimal.getAllData0();
            
            // Remplir le tableau avec les animaux malades
            treatmentTable.getItems().clear();
            treatmentTable.getItems().addAll(animauxMalades);
            
            // Mettre à jour l'état des boutons
            btnAnimauuxMalad.setSelected(true);
            btnRapelTraitement.setSelected(false);
            btnAjoutTrai.setSelected(false);
            
            // Vérifier si treatmentForm n'est pas null avant de l'utiliser
            if (treatmentForm != null) {
                treatmentForm.setVisible(false);
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                    "Une erreur est survenue lors de la récupération des animaux malades: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    void RapelTraitement(ActionEvent event) {
        try {
            // Récupérer la liste des animaux en cours de traitement avec getAllData1
            List<Animal> animauxEnTraitement = serviceAnimal.getAllData1();
            
            // Vérifier si la liste est vide
            if (animauxEnTraitement == null || animauxEnTraitement.isEmpty()) {
                showAlert(Alert.AlertType.INFORMATION, "Information", 
                        "Aucun animal en cours de traitement n'a été trouvé.");
            }
            
            // Remplir le tableau avec les animaux en cours de traitement
            treatmentTable.getItems().clear();
            if (animauxEnTraitement != null) {
                treatmentTable.getItems().addAll(animauxEnTraitement);
            }
            
            // Mettre à jour l'état des boutons
            btnRapelTraitement.setSelected(true);
            btnAnimauuxMalad.setSelected(false);
            btnAjoutTrai.setSelected(false);
            
            // Vérifier si treatmentForm n'est pas null avant de l'utiliser
            if (treatmentForm != null) {
                treatmentForm.setVisible(false);
            }
            
            // Afficher dans la console pour le débogage
            System.out.println("Nombre d'animaux en traitement récupérés: " + 
                    (animauxEnTraitement != null ? animauxEnTraitement.size() : 0));
            
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                    "Une erreur est survenue lors de la récupération des animaux en traitement: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    void AjoutTraitement(ActionEvent event) {
        // Récupérer l'animal sélectionné dans le tableau
        Animal selectedAnimal = treatmentTable.getSelectionModel().getSelectedItem();
        
        if (selectedAnimal != null) {
            // Remplir le formulaire avec les données de l'animal sélectionné
            idAnimalField.setText(String.valueOf(selectedAnimal.getId()));
            
            // Récupérer l'état actuel de l'animal
            String etatActuel = selectedAnimal.getEtat();
            if (etatActuel != null && !etatActuel.isEmpty()) {
                etatAnimalCombo.setValue(etatActuel);
            }
            
            // Mettre à jour l'état des boutons
            btnAjoutTrai.setSelected(true);
            btnAnimauuxMalad.setSelected(false);
            btnRapelTraitement.setSelected(false);
            
            // Afficher le formulaire
            if (treatmentForm != null) {
                treatmentForm.setVisible(true);
            } else {
                System.out.println("ATTENTION: treatmentForm est null, impossible de l'afficher");
            }
        } else {
            // Aucun animal sélectionné, afficher une alerte
            showAlert(Alert.AlertType.WARNING, "Sélection requise", 
                    "Veuillez sélectionner un animal dans le tableau avant d'ajouter un traitement.");
            
            // Réinitialiser l'état des boutons
            btnAjoutTrai.setSelected(false);
        }
    }
    
    @FXML
    void saveTreatment(ActionEvent event) {
        try {
            // Récupérer les valeurs des champs
            int idAnimal = Integer.parseInt(idAnimalField.getText().trim());
            String etatAnimal = etatAnimalCombo.getValue();
            String typeTraitement = typeTraitementField.getText().trim();
            String typeMedicament = typeMedicamentField.getText().trim();
            String anomalie = anomalieField.getText().trim();
            
            // Validation des champs
            if (etatAnimal == null || typeTraitement.isEmpty() || 
                typeMedicament.isEmpty() || anomalie.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Champs manquants", 
                        "Veuillez remplir tous les champs du formulaire.");
                return;
            }
            
            // Affecter le traitement à l'animal avec le nouvel état
            serviceAnimal.AffecterTraitementAnimal(idAnimal, typeTraitement, typeMedicament, anomalie, etatAnimal);
            
            // Réinitialiser le formulaire
            resetForm();
            
            // Masquer le formulaire
            if (treatmentForm != null) {
                treatmentForm.setVisible(false);
            }
            
            // Réinitialiser l'état des boutons
            btnAjoutTrai.setSelected(false);
            
            // Rafraîchir le tableau selon le nouvel état de l'animal
            if (etatAnimal.equals("animal en cours de traitement")) {
                // Si l'animal est en cours de traitement, rafraîchir la liste des animaux en traitement
                RapelTraitement(null);
                btnRapelTraitement.setSelected(true);
                btnAnimauuxMalad.setSelected(false);
            } else {
                // Sinon, rafraîchir la liste des animaux malades
                AnimauuxMalad(null);
                btnAnimauuxMalad.setSelected(true);
                btnRapelTraitement.setSelected(false);
            }
            
            showAlert(Alert.AlertType.INFORMATION, "Succès", 
                    "Le traitement a été enregistré avec succès et l'état de l'animal a été mis à jour.");
            
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de format", 
                    "L'ID de l'animal doit être un nombre entier.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", 
                    "Une erreur est survenue lors de l'enregistrement du traitement: " + e.getMessage());
        }
    }
    
    @FXML
    void cancelTreatment(ActionEvent event) {
        resetForm();
    }
    
    @FXML
    void handleSignOut(ActionEvent event) {
        // Logique de déconnexion
    }
    
    private void resetForm() {
        // Réinitialiser tous les champs du formulaire
        idAnimalField.clear();
        etatAnimalCombo.setValue(null);
        typeTraitementField.clear();
        typeMedicamentField.clear();
        anomalieField.clear();
    }
    
    private void loadAnimauxMalades() {
        try {
            // Récupérer la liste des animaux malades
            List<Animal> animauxMalades = serviceAnimal.getAllData0();
            
            // Vérifier si la liste est vide
            if (animauxMalades == null || animauxMalades.isEmpty()) {
                System.out.println("Aucun animal malade trouvé.");
            } else {
                System.out.println("Nombre d'animaux malades trouvés: " + animauxMalades.size());
            }
            
            // Remplir le tableau avec les animaux malades
            treatmentTable.getItems().clear();
            if (animauxMalades != null) {
                treatmentTable.getItems().addAll(animauxMalades);
            }
            
            // Mettre à jour l'état des boutons
            btnAnimauuxMalad.setSelected(true);
            btnRapelTraitement.setSelected(false);
            btnAjoutTrai.setSelected(false);
            
            // Masquer le formulaire
            if (treatmentForm != null) {
                treatmentForm.setVisible(false);
            }
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des animaux malades: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Ajoutons également une méthode pour déboguer la méthode getAllData1
    private void debugServiceAnimal() {
        try {
            List<Animal> animauxMalades = serviceAnimal.getAllData0();
            List<Animal> animauxEnTraitement = serviceAnimal.getAllData1();
            
            System.out.println("Nombre d'animaux malades (getAllData0): " + 
                    (animauxMalades != null ? animauxMalades.size() : 0));
            System.out.println("Nombre d'animaux en traitement (getAllData1): " + 
                    (animauxEnTraitement != null ? animauxEnTraitement.size() : 0));
            
            // Si getAllData1 retourne null ou une liste vide, mais getAllData0 fonctionne
            if ((animauxEnTraitement == null || animauxEnTraitement.isEmpty()) && 
                    animauxMalades != null && !animauxMalades.isEmpty()) {
                System.out.println("ATTENTION: getAllData1 ne retourne pas de données alors que getAllData0 fonctionne.");
            }
        } catch (Exception e) {
            System.err.println("Erreur lors du débogage de ServiceAnimal: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
