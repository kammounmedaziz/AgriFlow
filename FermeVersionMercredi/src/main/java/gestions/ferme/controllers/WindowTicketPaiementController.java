package gestions.ferme.controllers;

import gestions.ferme.entities.Produit;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class WindowTicketPaiementController {

    @FXML private Label dateLabel;
    @FXML private Label clientNameLabel;
    @FXML private Label paymentMethodLabel;
    @FXML private Label addressLabel;
    @FXML private Label totalLabel;
    @FXML private VBox itemsContainer;
    @FXML private Button closeButton;

    private Map<Produit, Integer> cart;
    private boolean isPromoActive;

    public void setTicketData(String clientName, String paymentMethod, String address,
                              String total, Map<Produit, Integer> cart, boolean isPromoActive) {
        this.cart = cart;
        this.isPromoActive = isPromoActive;

        dateLabel.setText("Date: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        clientNameLabel.setText("Client: " + clientName);
        paymentMethodLabel.setText("Méthode de paiement: " + paymentMethod);
        addressLabel.setText("Adresse: " + address);
        totalLabel.setText(total);

        displayPurchasedItems();
    }

    private void displayPurchasedItems() {
        itemsContainer.getChildren().clear();

        // Header row
        HBox header = new HBox(10);
        header.getChildren().addAll(
                new Label("Produit"), new Label("Qté"), new Label("Prix Unitaire"), new Label("Total")
        );
        itemsContainer.getChildren().add(header);

        if (cart == null || cart.isEmpty()) {
            itemsContainer.getChildren().add(new Label("Aucun produit dans le panier."));
            return;
        }

        // Rows produits
        for (Map.Entry<Produit, Integer> entry : cart.entrySet()) {
            Produit p = entry.getKey();
            int qte = entry.getValue();
            double prixU = isPromoActive ? p.getPrixUnitaire() * 0.9 : p.getPrixUnitaire();
            double total = prixU * qte;

            HBox row = new HBox(10);
            row.getChildren().addAll(
                    new Label(p.getNomProduit()),
                    new Label(String.valueOf(qte)),
                    new Label(String.format("%.2f DT", prixU)),
                    new Label(String.format("%.2f DT", total))
            );
            itemsContainer.getChildren().add(row);
        }

        // Un peu d'espace à la fin
        Region separator = new Region();
        separator.setPrefHeight(10);
        itemsContainer.getChildren().add(separator);
    }

    @FXML
    private void closeWindow() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}
