package edu.connexion3b.controllers;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.concurrent.Worker;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import edu.connexion3b.entities.Capteurs;
import edu.connexion3b.services.Services;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import netscape.javascript.JSObject;
import org.json.JSONObject;

public class ModificationController {

    @FXML
    private ResourceBundle resources;
    @FXML
    private TextField derncapmodif;
    @FXML
    private TextField nomcapmodif;

    @FXML
    private WebView map;
    @FXML
    private TextField emplcapmodif;

    @FXML
    private TextField idcapmodif;

    @FXML
    private TextField typecapmodif;

    @FXML
    private URL location;

    @FXML
    private Button btvmodif;

    @FXML
    void modif (ActionEvent event) {
        int k;
        int id_cap = Integer.parseInt(idcapmodif.getText());
        String nomcap = nomcapmodif.getText();
        String type = typecapmodif.getText();
        String emplac = emplcapmodif.getText();
        String dern = derncapmodif.getText();
        Capteurs c = new Capteurs(nomcap, type, emplac, dern);
        Services ts = new Services();
        ts.updateEntity(id_cap, c);

        }
    @FXML
    void initialize() {
        WebEngine engine = map.getEngine();
        engine.load(getClass().getResource("/map.html").toExternalForm());

        // Pour récupérer les coordonnées depuis JavaScript
        engine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                JSObject window = (JSObject) engine.executeScript("window");
                window.setMember("app", this); // liaison Java ↔ JS
            }
        });
    }

    // Méthode appelée depuis JavaScript
    public void receiveCoords(double lat, double lng) {
        System.out.println("Coordonnées cliquées : " + lat + ", " + lng);
        String emplacements = getAddressFromCoordinates(lat, lng);
        System.out.println("Adresse obtenue : " + emplacements);
        if (emplacements != null && !emplacements.trim().isEmpty()) {
            emplcapmodif.setText(emplacements);  // Met à jour le TextField
        } else {
            emplcapmodif.setText("Adresse non trouvée");  // Affiche un message d'erreur si l'adresse est invalide
        }


    }
    //public void setTfempl(String emplacements) {
    //  this.tfempl.setText(emplacements);;
    //}
    public String getAddressFromCoordinates(double lat, double lon) {
        try {
            // Ajouter le paramètre 'accept-language=fr' pour obtenir la réponse en français
            String urlStr = String.format(
                    java.util.Locale.US,
                    "https://nominatim.openstreetmap.org/reverse?format=json&lat=%f&lon=%f&zoom=18&addressdetails=1&accept-language=fr", lat, lon);

            URL url = new URL(urlStr);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Analyser la réponse JSON
            JSONObject json = new JSONObject(response.toString());

            // Retourner l'adresse en français
            return json.getString("display_name");

        } catch (Exception e) {
            e.printStackTrace();
            return "Adresse inconnue";
        }
    }

}



