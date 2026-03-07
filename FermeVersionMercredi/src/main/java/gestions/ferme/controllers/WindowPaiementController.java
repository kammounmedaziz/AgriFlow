package gestions.ferme.controllers;

import gestions.ferme.entities.Produit;
import gestions.ferme.tools.MyConnection;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;
import org.json.JSONObject;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class WindowPaiementController {

    @FXML private WebView mapView;
    @FXML private TextArea addressField;
    @FXML private Button btnConfimePaimenet;
    @FXML private RadioButton cardPaymentRadio;
    @FXML private RadioButton cashOnDeliveryRadio;
    @FXML private TextField fullNameField;
    @FXML private VBox orderSummaryItems;
    @FXML private Label orderTotalLabel;
    @FXML private TextField phoneField;

    // Panier et promotion
    private Map<Produit, Integer> cart = new HashMap<>();
    private boolean isPromoActive = false;

    // Méthode appelée depuis JavaScript
    public void receiveCoords(double lat, double lng) {
        String adresse = obtenirAdresseDepuisCoordonnees(lat, lng);
        addressField.setText(adresse != null ? adresse : "Adresse inconnue");
    }

    private String obtenirAdresseDepuisCoordonnees(double lat, double lon) {
        try {
            String urlStr = String.format(Locale.US,
                    "https://nominatim.openstreetmap.org/reverse?format=json&lat=%f&lon=%f&zoom=18&addressdetails=1&accept-language=fr",
                    lat, lon);

            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();

            JSONObject json = new JSONObject(response.toString());
            return json.getString("display_name");

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void initialiserCarte() {
        WebEngine engine = mapView.getEngine();
        engine.load(getClass().getResource("/paiement/map.html").toExternalForm());

        engine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                JSObject window = (JSObject) engine.executeScript("window");
                window.setMember("app", this);
            }
        });
    }

    @FXML
    void ConfimePaimenet(ActionEvent event) {
        try {
            if (fullNameField.getText().isEmpty() || phoneField.getText().isEmpty() || addressField.getText().isEmpty()) {
                showAlert("Erreur", "Veuillez remplir tous les champs obligatoires.");
                return;
            }

            String paymentMethod = cashOnDeliveryRadio.isSelected() ? "Paiement à la livraison" : "Carte bancaire";
            String clientName = fullNameField.getText();
            String clientPhone = phoneField.getText();
            String clientAddress = addressField.getText();
            String total = orderTotalLabel.getText();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tecket/facture.fxml"));
            Parent root = loader.load();

            WindowTicketPaiementController controller = loader.getController();
            controller.setTicketData(clientName, paymentMethod, clientAddress, total, cart, isPromoActive);

            saveVenteToDatabase(clientName, clientPhone, clientAddress, paymentMethod, total);

            Stage stage = new Stage();
            stage.setTitle("Ticket de Paiement");
            stage.setScene(new Scene(root));
            stage.show();

            ((Stage) btnConfimePaimenet.getScene().getWindow()).close();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur est survenue lors de la génération du ticket : " + e.getMessage());
        }
    }

    private void saveVenteToDatabase(String clientName, String clientPhone, String clientAddress,
                                     String paymentMethod, String totalAmount) {
        try {
            double total = Double.parseDouble(totalAmount.replace(" dt", ""));
            Connection conn = MyConnection.getInstance().getCnx();

            String factureQuery = "INSERT INTO facture (date_facture, montant_total, mode_paiement) VALUES (NOW(), ?, ?)";
            PreparedStatement factureStmt = conn.prepareStatement(factureQuery, Statement.RETURN_GENERATED_KEYS);
            factureStmt.setDouble(1, total);
            factureStmt.setString(2, paymentMethod);
            factureStmt.executeUpdate();

            ResultSet keys = factureStmt.getGeneratedKeys();
            int factureId = keys.next() ? keys.getInt(1) : 0;

            for (Map.Entry<Produit, Integer> entry : cart.entrySet()) {
                Produit produit = entry.getKey();
                int quantity = entry.getValue();
                double unitPrice = isPromoActive ? produit.getPrixUnitaire() * 0.9 : produit.getPrixUnitaire();
                double totalPrice = unitPrice * quantity;

                String venteQuery = "INSERT INTO vente (id_produit, quantite_vendue, revenu_total, client_id, paiement, facture_id) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement venteStmt = conn.prepareStatement(venteQuery);
                venteStmt.setInt(1, produit.getIdProduit());
                venteStmt.setInt(2, quantity);
                venteStmt.setDouble(3, totalPrice);
                venteStmt.setString(4, clientName + " - " + clientPhone);
                venteStmt.setString(5, paymentMethod);
                venteStmt.setInt(6, factureId);
                venteStmt.executeUpdate();

                String updateQuery = "UPDATE produits SET quantite_disponible = quantite_disponible - ? WHERE idPr = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
                updateStmt.setInt(1, quantity);
                updateStmt.setInt(2, produit.getIdProduit());
                updateStmt.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors de l'enregistrement dans la base de données.");
        }
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    @FXML
    void initialize() {
        initialiserCarte();
    }

    // Méthodes d’interface
    public void setOrderTotal(String total) {
        orderTotalLabel.setText(total);
    }

    public void setCart(Map<Produit, Integer> cart, boolean isPromoActive) {
        this.cart = cart;
        this.isPromoActive = isPromoActive;
    }
}
