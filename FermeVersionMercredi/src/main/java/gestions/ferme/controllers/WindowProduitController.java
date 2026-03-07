package gestions.ferme.controllers;

import java.io.File;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import gestions.ferme.entities.Produit;
import gestions.ferme.services.ProduitService;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

public class WindowProduitController {

    @FXML
    private FlowPane productContainer;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnshowAddProduitForm;

    @FXML
    private Button deleteButton;

    @FXML
    private Button editButton;

    @FXML
    private VBox formContainer;

    @FXML
    private Label formTitle;

    @FXML
    private TextField nomProduitField;

    @FXML
    private TextField prixUnitaireField;

    @FXML
    private ImageView productImageView;

    @FXML
    private TextField quantiteDisponibleField;

    @FXML
    private Label totalProductsLabel;

    @FXML
    private ComboBox<String> typeProduitField;

    @FXML
    private ComboBox<String> uniteField;

    ProduitService produitService = new ProduitService();

    private Produit selectedProduit;

    @FXML
    void initialize() {
        assert btnshowAddProduitForm != null : "fx:id=\"btnshowAddProduitForm\" was not injected: check your FXML file 'produit.fxml'.";
        assert deleteButton != null : "fx:id=\"deleteButton\" was not injected: check your FXML file 'produit.fxml'.";
        assert editButton != null : "fx:id=\"editButton\" was not injected: check your FXML file 'produit.fxml'.";
        assert formContainer != null : "fx:id=\"formContainer\" was not injected: check your FXML file 'produit.fxml'.";
        assert formTitle != null : "fx:id=\"formTitle\" was not injected: check your FXML file 'produit.fxml'.";
        assert nomProduitField != null : "fx:id=\"nomProduitField\" was not injected: check your FXML file 'produit.fxml'.";
        assert prixUnitaireField != null : "fx:id=\"prixUnitaireField\" was not injected: check your FXML file 'produit.fxml'.";
        assert productImageView != null : "fx:id=\"productImageView\" was not injected: check your FXML file 'produit.fxml'.";
        assert quantiteDisponibleField != null : "fx:id=\"quantiteDisponibleField\" was not injected: check your FXML file 'produit.fxml'.";
        assert totalProductsLabel != null : "fx:id=\"totalProductsLabel\" was not injected: check your FXML file 'produit.fxml'.";
        assert typeProduitField != null : "fx:id=\"typeProduitField\" was not injected: check your FXML file 'produit.fxml'.";
        assert uniteField != null : "fx:id=\"uniteField\" was not injected: check your FXML file 'produit.fxml'.";

        // Initialisation des composants
        refreshProductDisplay();
        updateTotals();
        initializeComboBoxes();

        // Désactiver les boutons d'édition/suppression initialement
        editButton.setDisable(true);
        deleteButton.setDisable(true);
    }

    private void createProductCard(Produit produit) {
        VBox card = new VBox();
        card.getStyleClass().add("product-card");
        card.setPrefWidth(200);
        card.setPrefHeight(250);
        card.setSpacing(10);
        card.setAlignment(javafx.geometry.Pos.CENTER);

        // Image du produit
        ImageView imageView = new ImageView();
        if (produit.getImage() != null && !produit.getImage().isEmpty()) {
            Image image = new Image(produit.getImage(), 150, 150, true, true);
            imageView.setImage(image);
        } else {
            Image defaultImage = new Image("/images/default-product.png", 150, 150, true, true);
            imageView.setImage(defaultImage);
        }
        imageView.setFitWidth(150);
        imageView.setFitHeight(150);
        imageView.getStyleClass().add("product-image");

        // ID du produit
        Label idLabel = new Label("ID: " + produit.getIdProduit());
        idLabel.getStyleClass().add("product-id");

        // Nom du produit
        Label nomLabel = new Label(produit.getNomProduit());
        nomLabel.getStyleClass().add("product-name");


        // Quantité
        Label quantiteLabel = new Label("Dispo: " + produit.getQuantiteDisponible() + " " + produit.getUniteProduit());
        quantiteLabel.getStyleClass().add("product-quantity");

        // Ajout des éléments à la carte
        card.getChildren().addAll(imageView, idLabel, nomLabel,  quantiteLabel);

        // Gestion du clic sur la carte
        card.setOnMouseClicked(event -> {
            if (selectedProduit == produit) {
                selectedProduit = null;
                editButton.setDisable(true);
                deleteButton.setDisable(true);
                card.getStyleClass().remove("selected-card");
            } else {
                if (selectedProduit != null) {
                    for (javafx.scene.Node node : productContainer.getChildren()) {
                        if (node instanceof VBox) {
                            node.getStyleClass().remove("selected-card");
                        }
                    }
                }

                selectedProduit = produit;
                editButton.setDisable(false);
                deleteButton.setDisable(false);
                card.getStyleClass().add("selected-card");
            }
        });

        productContainer.getChildren().add(card);
    }

    private void refreshProductDisplay() {
        productContainer.getChildren().clear();
        ObservableList<Produit> produits = FXCollections.observableArrayList(produitService.getAllData());
        for (Produit produit : produits) {
            createProductCard(produit);
        }
    }

    private void updateTotals() {
        ObservableList<Produit> produits = FXCollections.observableArrayList(produitService.getAllData());
        int totalProduits = produits.size();
        totalProductsLabel.setText("Total Produits : " + totalProduits);
    }

    private void initializeComboBoxes() {
        typeProduitField.getItems().setAll(produitService.getDistinctTypes());
        uniteField.getItems().setAll(produitService.getDistinctUnites());

        // Ajout de l'option "Autre" aux ComboBox
        typeProduitField.getItems().add("Autre");
        uniteField.getItems().add("Autre");

        // Gestion de l'option "Autre"
        setupComboBoxHandler(typeProduitField, "Nouveau Type de Produit");
        setupComboBoxHandler(uniteField, "Nouvelle Unité");
    }

    private void setupComboBoxHandler(ComboBox<String> comboBox, String dialogTitle) {
        comboBox.setOnAction(event -> {
            if ("Autre".equals(comboBox.getValue())) {
                showCustomInputDialog(comboBox, dialogTitle);
            }
        });
    }

    private void showCustomInputDialog(ComboBox<String> comboBox, String dialogTitle) {
        // Création d'une boîte de dialogue personnalisée
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle(dialogTitle);
        dialog.setHeaderText("Entrez les détails");

        // Configuration des boutons
        ButtonType confirmButtonType = new ButtonType("Confirmer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

        // Création des champs de saisie
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField newValueField = new TextField();
        newValueField.setPromptText("Nouvelle valeur");
        TextField abbreviationField = new TextField();
        abbreviationField.setPromptText("Abréviation (optionnel)");

        grid.add(new Label("Valeur:"), 0, 0);
        grid.add(newValueField, 1, 0);
        grid.add(new Label("Abréviation:"), 0, 1);
        grid.add(abbreviationField, 1, 1);

        // Validation
        Node confirmButton = dialog.getDialogPane().lookupButton(confirmButtonType);
        confirmButton.setDisable(true);

        newValueField.textProperty().addListener((observable, oldValue, newValue) -> {
            confirmButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getStylesheets().add(getClass().getResource("/styles/alerts.css").toExternalForm());
        dialog.getDialogPane().getStyleClass().add("custom-dialog");

        // Conversion du résultat
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButtonType) {
                return new Pair<>(newValueField.getText(), abbreviationField.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(pair -> {
            String newValue = pair.getKey();
            String abbreviation = pair.getValue();

            if (!newValue.trim().isEmpty()) {
                String displayValue = abbreviation.isEmpty() ? newValue : newValue + " (" + abbreviation + ")";
                comboBox.getItems().add(comboBox.getItems().size() - 1, displayValue);
                comboBox.setValue(displayValue);
            }
        });
    }

    @FXML
    void handleImageSelection(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionner une image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(productImageView.getScene().getWindow());
        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            productImageView.setImage(image);
        }
    }

    @FXML
    void showAddProduitForm(ActionEvent event) {
        selectedProduit = null;
        editButton.setDisable(true);
        deleteButton.setDisable(true);

        formTitle.setText("Ajouter un Produit");
        nomProduitField.clear();
        quantiteDisponibleField.clear();
        prixUnitaireField.clear();
        typeProduitField.getSelectionModel().clearSelection();
        uniteField.getSelectionModel().clearSelection();
        productImageView.setImage(null);

        showForm();
    }

    @FXML
    void showEditProduitForm(ActionEvent event) {
        if (selectedProduit != null) {
            Produit produitMisAJour = produitService.recupererProduitId(selectedProduit.getIdProduit());
            if (produitMisAJour != null) {
                formTitle.setText("Modifier un Produit");
                nomProduitField.setText(produitMisAJour.getNomProduit());
                typeProduitField.setValue(produitMisAJour.getTypeProduit());
                uniteField.setValue(produitMisAJour.getUniteProduit());
                prixUnitaireField.setText(String.valueOf(produitMisAJour.getPrixUnitaire()));
                quantiteDisponibleField.setText(String.valueOf(produitMisAJour.getQuantiteDisponible()));

                if (produitMisAJour.getImage() != null && !produitMisAJour.getImage().isEmpty()) {
                    productImageView.setImage(new Image(produitMisAJour.getImage()));
                }

                showForm();
            } else {
                showErrorAlert("Erreur", "Le produit n'a pas pu être récupéré depuis la base de données.");
            }
        }
    }

    @FXML
    void deleteProduit(ActionEvent event) {
        if (selectedProduit != null) {
            showDeleteConfirmation(selectedProduit.getNomProduit()).ifPresent(confirmed -> {
                if (confirmed) {
                    produitService.supprimerEntity(selectedProduit);
                    selectedProduit = null;
                    editButton.setDisable(true);
                    deleteButton.setDisable(true);
                    refreshProductDisplay();
                    updateTotals();
                    showSuccessAlert("Succès", "Le produit a été supprimé avec succès.");
                }
            });
        }
    }

    @FXML
    void saveProduit(ActionEvent event) {
        String nom = nomProduitField.getText();
        String type = typeProduitField.getValue();
        String unite = uniteField.getValue();
        String prixStr = prixUnitaireField.getText();
        String quantiteStr = quantiteDisponibleField.getText();

        if (nom == null || nom.isEmpty() || type == null || unite == null
                || prixStr == null || prixStr.isEmpty()
                || quantiteStr == null || quantiteStr.isEmpty()) {
            showErrorAlert("Erreur de validation", "Veuillez remplir tous les champs obligatoires.");
            return;
        }

        try {
            float prix = Float.parseFloat(prixStr);
            float quantite = Float.parseFloat(quantiteStr);

            Produit produit = new Produit();

            if (selectedProduit != null) {
                produit.setIdProduit(selectedProduit.getIdProduit());
            }

            produit.setNomProduit(nom);
            produit.setTypeProduit(type);
            produit.setUniteProduit(unite);
            produit.setPrixUnitaire(prix);
            produit.setQuantiteDisponible(quantite);

            if (productImageView.getImage() != null) {
                produit.setImage(productImageView.getImage().getUrl());
            }

            if (selectedProduit != null) {
                produitService.updateEntity(selectedProduit.getIdProduit(), produit);
                showSuccessAlert("Succès", "Produit modifié avec succès !");
            } else {
                produitService.ajouterEntity(produit);
                showSuccessAlert("Succès", "Produit ajouté avec succès !");
            }

            refreshProductDisplay();
            updateTotals();

            selectedProduit = null;
            editButton.setDisable(true);
            deleteButton.setDisable(true);
            hideForm(event);

        } catch (NumberFormatException e) {
            showErrorAlert("Erreur de format", "Le prix et la quantité doivent être des nombres valides.");
        }
    }

    @FXML
    void hideForm(ActionEvent event) {
        formContainer.getStyleClass().remove("visible");
        javafx.animation.PauseTransition delay = new javafx.animation.PauseTransition(javafx.util.Duration.millis(300));
        delay.setOnFinished(e -> formContainer.setVisible(false));
        delay.play();
    }

    @FXML
    void handleReturn(ActionEvent event) {
        // Implémentez la logique de retour si nécessaire
    }

    private void showForm() {
        formContainer.setVisible(true);
        javafx.application.Platform.runLater(() -> {
            formContainer.getStyleClass().add("visible");
        });
    }

    // Méthodes pour les alertes personnalisées
    private void showSuccessAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        styleAlert(alert, "success-alert");
        alert.showAndWait();
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        styleAlert(alert, "error-alert");
        alert.showAndWait();
    }

    private Optional<Boolean> showDeleteConfirmation(String productName) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Supprimer '" + productName + "' ?");
        alert.setContentText("Cette action est irréversible. Voulez-vous vraiment supprimer ce produit ?");

        styleAlert(alert, "delete-popup");

        // Personnalisation des boutons
        ButtonType confirmButton = new ButtonType("Confirmer", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(confirmButton, cancelButton);

        // Style des boutons
        Button confirmBtn = (Button) alert.getDialogPane().lookupButton(confirmButton);
        confirmBtn.getStyleClass().add("confirm-btn");
        Button cancelBtn = (Button) alert.getDialogPane().lookupButton(cancelButton);
        cancelBtn.getStyleClass().add("cancel-btn");

        Optional<ButtonType> result = alert.showAndWait();
        return result.map(buttonType -> buttonType == confirmButton);
    }

    private void styleAlert(Alert alert, String styleClass) {
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/styles/alerts.css").toExternalForm());
        dialogPane.getStyleClass().add(styleClass);
    }
}