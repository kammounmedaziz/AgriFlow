package gestions.ferme.controllers;

import gestions.ferme.entities.Produit;
import gestions.ferme.services.ProduitService;
import gestions.ferme.services.VenteService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

public class WindowVenteController implements Initializable {

    @FXML
    private FlowPane productContainer;

    @FXML
    private ToggleButton btnTousProduits;

    @FXML
    private ToggleButton btnFruits;

    @FXML
    private ToggleButton btnLegumes;

    @FXML
    private ToggleButton btnProduitsLaitiers;

    @FXML
    private ToggleButton btnViandes;

    @FXML
    private ToggleButton btnProduitsBio;

    @FXML
    private CheckBox radioDispoMaint;

    @FXML
    private CheckBox radioPromo;

    @FXML
    private Button btnPanier;

    @FXML
    private BorderPane mainContainer;

    private ProduitService produitService = new ProduitService();
    private VenteService venteService = new VenteService();
    private ObservableList<Produit> produits = FXCollections.observableArrayList();
    private Map<Produit, Integer> cart = new HashMap<>();
    private boolean isCartView = false;
    private boolean isPromoActive = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadProducts();
        updateCartButton();
        checkPromoStatus();
    }

    private void loadProducts() {
        productContainer.getChildren().clear();
        produits.setAll(produitService.getAllDataVentes());
        displayProducts();
    }

    private void displayProducts() {
        productContainer.getChildren().clear();
        for (Produit produit : produits) {
            VBox productCard = createProductCard(produit);
            productContainer.getChildren().add(productCard);
        }
    }

    private VBox createProductCard(Produit produit) {
        VBox card = new VBox(10);
        card.getStyleClass().add("product-card");
        card.setAlignment(Pos.CENTER);

        // Image du produit
        ImageView imageView = new ImageView();
        if (produit.getImage() != null && !produit.getImage().isEmpty()) {
            imageView.setImage(new Image(produit.getImage()));
        }
        imageView.setFitWidth(150);
        imageView.setFitHeight(150);
        imageView.getStyleClass().add("product-image");

        // Nom du produit
        Label nameLabel = new Label(produit.getNomProduit());
        nameLabel.getStyleClass().add("product-name");

        // Prix (avec promotion si applicable)
        double price = isPromoActive ? venteService.calculatePromoPrice(produit.getPrixUnitaire()) : produit.getPrixUnitaire();
        Label priceLabel = new Label(String.format("%.2f dt", price));
        priceLabel.getStyleClass().add("product-price");
        if (isPromoActive) {
            priceLabel.getStyleClass().add("promo-price");
        }

        // Quantité disponible
        Label quantityLabel = new Label("Quantité: " + produit.getQuantiteDisponible() + " " + produit.getUniteProduit());
        quantityLabel.getStyleClass().add("product-quantity");

        // Spinner pour la quantité à ajouter
        Spinner<Integer> quantitySpinner = new Spinner<>(1, (int)produit.getQuantiteDisponible(), 1);
        quantitySpinner.setEditable(true);
        quantitySpinner.getStyleClass().add("quantity-spinner");
        quantitySpinner.setPrefWidth(80);

        // Bouton ajouter
        Button addButton = new Button("Ajouter");
        addButton.getStyleClass().add("add-button");
        addButton.setOnAction(e -> handleAddToCart(produit, quantitySpinner.getValue()));

        // Conteneur pour le spinner et le bouton
        HBox actionBox = new HBox(10);
        actionBox.setAlignment(Pos.CENTER);
        actionBox.getChildren().addAll(quantitySpinner, addButton);

        card.getChildren().addAll(imageView, nameLabel, priceLabel, quantityLabel, actionBox);
        return card;
    }

    private void handleAddToCart(Produit produit, int quantity) {
        // Vérifier si le produit existe déjà dans le panier
        Produit existingProduct = findExistingProduct(produit);
        if (existingProduct != null) {
            // Si le produit existe déjà, mettre à jour sa quantité
            int currentQuantity = cart.get(existingProduct);
            cart.put(existingProduct, currentQuantity + quantity);
        } else {
            // Si c'est un nouveau produit, l'ajouter au panier
            cart.put(produit, quantity);
        }
        updateCartButton();
    }

    private Produit findExistingProduct(Produit newProduct) {
        for (Produit existingProduct : cart.keySet()) {
            if (existingProduct.getNomProduit().equals(newProduct.getNomProduit())) {
                return existingProduct;
            }
        }
        return null;
    }

    private void updateCartButton() {
        int totalProducts = cart.size(); // Nombre de produits différents dans le panier
        btnPanier.setText("Panier (" + totalProducts + ")");
    }

    private String getProductType(Produit produit) {
        // Vérifier si le produit est dans la liste des fruits
        if (produitService.getAllDataFruit().contains(produit)) {
            return "Fruits";
        }
        // Vérifier si le produit est dans la liste des légumes
        if (produitService.getAllDataLeguime().contains(produit)) {
            return "Légumes";
        }
        // Vérifier si le produit est dans la liste des produits laitiers
        if (produitService.getAllDataProduitlaitiere().contains(produit)) {
            return "Produits laitiers";
        }
        // Vérifier si le produit est dans la liste des viandes
        if (produitService.getAllDataViande().contains(produit)) {
            return "Viandes";
        }
        // Vérifier si le produit est dans la liste des produits bio
        if (produitService.getAllDataProduitBio().contains(produit)) {
            return "Produits bio";
        }
        return null;
    }

    private void displayCart() {
        VBox mainCartContainer = new VBox(20);
        mainCartContainer.setStyle("-fx-padding: 20;");

        ScrollPane scrollPane = new ScrollPane();
        FlowPane cartContainer = new FlowPane();
        cartContainer.setHgap(10);
        cartContainer.setVgap(10);
        cartContainer.setStyle("-fx-padding: 20;");

        for (Map.Entry<Produit, Integer> entry : cart.entrySet()) {
            Produit produit = entry.getKey();
            int quantity = entry.getValue();
            double price = isPromoActive ? venteService.calculatePromoPrice(produit.getPrixUnitaire()) : produit.getPrixUnitaire();

            VBox cartItem = new VBox(10);
            cartItem.getStyleClass().add("cart-item");
            cartItem.setAlignment(Pos.CENTER);

            // Image du produit
            ImageView imageView = new ImageView();
            if (produit.getImage() != null && !produit.getImage().isEmpty()) {
                imageView.setImage(new Image(produit.getImage()));
            }
            imageView.setFitWidth(100);
            imageView.setFitHeight(100);
            imageView.getStyleClass().add("product-image");

            // Nom du produit
            Label nameLabel = new Label(produit.getNomProduit());
            nameLabel.getStyleClass().add("product-name");

            // Prix total (avec promotion si applicable)
            Label totalLabel = new Label(String.format("%.2f dt", price * quantity));
            totalLabel.getStyleClass().add("product-total");
            if (isPromoActive) {
                totalLabel.getStyleClass().add("promo-price");
            }

            // Conteneur pour le spinner et les boutons
            HBox actionBox = new HBox(10);
            actionBox.setAlignment(Pos.CENTER);

            // Spinner pour la quantité
            Spinner<Integer> quantitySpinner = new Spinner<>(1, (int)produit.getQuantiteDisponible(), quantity);
            quantitySpinner.setEditable(true);
            quantitySpinner.getStyleClass().add("quantity-spinner");
            quantitySpinner.setPrefWidth(80);

            // Bouton mettre à jour
            Button updateButton = new Button("Mettre à jour");
            updateButton.getStyleClass().add("update-button");
            updateButton.setOnAction(e -> {
                int newQuantity = quantitySpinner.getValue();
                if (newQuantity > 0) {
                    cart.put(produit, newQuantity);
                    updateCartButton();
                    displayCart();
                }
            });

            // Bouton supprimer
            Button removeButton = new Button("Supprimer");
            removeButton.getStyleClass().add("remove-button");
            removeButton.setOnAction(e -> {
                cart.remove(produit);
                updateCartButton();
                displayCart();
            });

            actionBox.getChildren().addAll(quantitySpinner, updateButton, removeButton);
            cartItem.getChildren().addAll(imageView, nameLabel, totalLabel, actionBox);
            cartContainer.getChildren().add(cartItem);
        }

        scrollPane.setContent(cartContainer);

        // Calculer le total (avec promotion si applicable)
        double total = 0;
        for (Map.Entry<Produit, Integer> entry : cart.entrySet()) {
            Produit produit = entry.getKey();
            int quantity = entry.getValue();
            double price = isPromoActive ? venteService.calculatePromoPrice(produit.getPrixUnitaire()) : produit.getPrixUnitaire();
            total += price * quantity;
        }

        // Conteneur pour le total et le bouton de paiement
        HBox bottomContainer = new HBox(20);
        bottomContainer.setAlignment(Pos.CENTER_RIGHT);
        bottomContainer.setStyle("-fx-padding: 20; -fx-background-color: white;");

        Label totalLabel = new Label(String.format("Total: %.2f dt", total));
        totalLabel.getStyleClass().add("cart-total");

        Button checkoutButton = new Button("Passer au paiement");
        checkoutButton.getStyleClass().add("checkout-button");
        checkoutButton.setOnAction(e -> handleCheckout());

        bottomContainer.getChildren().addAll(totalLabel, checkoutButton);

        mainCartContainer.getChildren().addAll(scrollPane, bottomContainer);
        mainContainer.setCenter(mainCartContainer);
    }

    private void handleCheckout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/paiement/WindowPaiement.fxml"));
            Parent root = loader.load();
            
            // Calculer le total du panier
            double total = 0;
            for (Map.Entry<Produit, Integer> entry : cart.entrySet()) {
                Produit produit = entry.getKey();
                int quantity = entry.getValue();
                double price = isPromoActive ? venteService.calculatePromoPrice(produit.getPrixUnitaire()) : produit.getPrixUnitaire();
                total += price * quantity;
            }
            
            // Passer le total au contrôleur de paiement
            WindowPaiementController paymentController = loader.getController();
            paymentController.setOrderTotal(String.format("%.2f dt", total));
            
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UTILITY);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void TousProduits() {
        if (isCartView) {
            isCartView = false;
            mainContainer.setCenter(productContainer);
        }
        loadProducts();
    }

    @FXML
    private void FruitsVentes() {
        if (isCartView) {
            isCartView = false;
            mainContainer.setCenter(productContainer);
        }
        productContainer.getChildren().clear();
        produits.setAll(produitService.getAllDataFruit());
        displayProducts();
    }

    @FXML
    private void LegumesVentes() {
        if (isCartView) {
            isCartView = false;
            mainContainer.setCenter(productContainer);
        }
        productContainer.getChildren().clear();
        produits.setAll(produitService.getAllDataLeguime());
        displayProducts();
    }

    @FXML
    private void ProduitsLaitiersVentes() {
        if (isCartView) {
            isCartView = false;
            mainContainer.setCenter(productContainer);
        }
        productContainer.getChildren().clear();
        produits.setAll(produitService.getAllDataProduitlaitiere());
        displayProducts();
    }

    @FXML
    private void ViandesVentes() {
        if (isCartView) {
            isCartView = false;
            mainContainer.setCenter(productContainer);
        }
        productContainer.getChildren().clear();
        produits.setAll(produitService.getAllDataViande());
        displayProducts();
    }

    @FXML
    private void ProduitsBioVentes() {
        if (isCartView) {
            isCartView = false;
            mainContainer.setCenter(productContainer);
        }
        productContainer.getChildren().clear();
        produits.setAll(produitService.getAllDataProduitBio());
        displayProducts();
    }

    @FXML
    private void DispoMaintVentes() {

    }

    @FXML
    private void PromoVentes() {

    }

    @FXML
    private void AvisReclamationVentes() {

    }

    @FXML
    private void PanierVentes() {
        isCartView = true;
        displayCart();
    }

    private void checkPromoStatus() {
        isPromoActive = venteService.isPromotionActive();
        radioPromo.setSelected(isPromoActive);
        radioPromo.setDisable(true); // Désactive la case à cocher car elle est automatique
        if (isPromoActive) {
            displayProducts(); // Rafraîchir l'affichage pour montrer les prix en promotion
        }
    }
}
