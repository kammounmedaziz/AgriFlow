package gestions.ferme.controllers;

import gestions.ferme.tools.MyConnection;
import gestions.ferme.utils.TextFieldAPIValidator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GraphiquesController {

    @FXML private TextField capteurIdField;
    @FXML private BarChart<String, Number> tempHumidityChart;
    @FXML private LineChart<String, Number> ultrasonChart;
    @FXML private CheckBox temperatureCheck;
    @FXML private CheckBox humidityCheck;
    @FXML private CheckBox ultrasonCheck;

    @FXML
    void initialize() {
        // Initialisation des graphiques vides
        tempHumidityChart.setAnimated(false);
        ultrasonChart.setAnimated(false);
        
        // Ajouter la validation des gros mots
        TextFieldAPIValidator.addProfanityListener(capteurIdField);
    }

    @FXML
    void handleReturn(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/capteur/capteur.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) capteurIdField.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            afficherErreur("Erreur de navigation", e.getMessage());
        }
    }

    @FXML
    void afficherGraphiques(ActionEvent event) {
        // Vider les graphiques existants
        tempHumidityChart.getData().clear();
        ultrasonChart.getData().clear();

        // Valider le champ avant de continuer
        TextFieldAPIValidator.validateNoProfanity(capteurIdField, isValid -> {
            if (isValid) {
                String idCapteur = capteurIdField.getText().trim();
                if (idCapteur.isEmpty()) {
                    afficherErreur("Erreur", "Veuillez entrer un ID de capteur");
                    return;
                }

                try {
                    int capteurId = Integer.parseInt(idCapteur);
                    
                    // Charger les données selon les checkboxes sélectionnées
                    if (temperatureCheck.isSelected() || humidityCheck.isSelected()) {
                        chargerDonneesTemperatureHumidite(capteurId);
                    }
                    
                    if (ultrasonCheck.isSelected()) {
                        chargerDonneesUltrason(capteurId);
                    }
                    
                } catch (NumberFormatException e) {
                    afficherErreur("Erreur", "L'ID du capteur doit être un nombre entier");
                }
            }
        });
    }

    private void chargerDonneesTemperatureHumidite(int capteurId) {
        String query = "SELECT temperature, humidity, date FROM temperatureHumidity WHERE idCapteur = ? ORDER BY date";
        
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        try {
            conn = MyConnection.getInstance().getCnx();
            pst = conn.prepareStatement(query);
            pst.setInt(1, capteurId);
            rs = pst.executeQuery();
            
            XYChart.Series<String, Number> temperatureSeries = new XYChart.Series<>();
            temperatureSeries.setName("Température");
            
            XYChart.Series<String, Number> humiditySeries = new XYChart.Series<>();
            humiditySeries.setName("Humidité");
            
            boolean hasData = false;
            
            while (rs.next()) {
                String date = rs.getString("date");
                double temperature = rs.getDouble("temperature");
                double humidity = rs.getDouble("humidity");
                
                if (temperatureCheck.isSelected()) {
                    temperatureSeries.getData().add(new XYChart.Data<>(date, temperature));
                }
                
                if (humidityCheck.isSelected()) {
                    humiditySeries.getData().add(new XYChart.Data<>(date, humidity));
                }
                
                hasData = true;
            }
            
            if (hasData) {
                if (temperatureCheck.isSelected()) {
                    tempHumidityChart.getData().add(temperatureSeries);
                }
                
                if (humidityCheck.isSelected()) {
                    tempHumidityChart.getData().add(humiditySeries);
                }
            } else {
                afficherInfo("Information", "Aucune donnée de température/humidité trouvée pour ce capteur");
            }
            
        } catch (SQLException e) {
            afficherErreur("Erreur SQL", "Erreur lors de la récupération des données de température/humidité: " + e.getMessage());
        } finally {
            // Fermer uniquement les ressources ResultSet et PreparedStatement, pas la connexion
            try {
                if (rs != null) rs.close();
                if (pst != null) pst.close();
                // Ne pas fermer la connexion ici
            } catch (SQLException e) {
                System.out.println("Erreur lors de la fermeture des ressources: " + e.getMessage());
            }
        }
    }

    private void chargerDonneesUltrason(int capteurId) {
        String query = "SELECT valeur, date FROM ultrason WHERE idCapteur = ? ORDER BY date";
        
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        
        try {
            conn = MyConnection.getInstance().getCnx();
            pst = conn.prepareStatement(query);
            pst.setInt(1, capteurId);
            rs = pst.executeQuery();
            
            XYChart.Series<String, Number> ultrasonSeries = new XYChart.Series<>();
            ultrasonSeries.setName("Ultrason");
            
            boolean hasData = false;
            
            while (rs.next()) {
                String date = rs.getString("date");
                double valeur = rs.getDouble("valeur");
                ultrasonSeries.getData().add(new XYChart.Data<>(date, valeur));
                hasData = true;
            }
            
            if (hasData) {
                ultrasonChart.getData().add(ultrasonSeries);
            } else {
                afficherInfo("Information", "Aucune donnée ultrason trouvée pour ce capteur");
            }
            
        } catch (SQLException e) {
            afficherErreur("Erreur SQL", "Erreur lors de la récupération des données ultrason: " + e.getMessage());
        } finally {
            // Fermer uniquement les ressources ResultSet et PreparedStatement, pas la connexion
            try {
                if (rs != null) rs.close();
                if (pst != null) pst.close();
                // Ne pas fermer la connexion ici
            } catch (SQLException e) {
                System.out.println("Erreur lors de la fermeture des ressources: " + e.getMessage());
            }
        }
    }

    private void afficherErreur(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void afficherInfo(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}