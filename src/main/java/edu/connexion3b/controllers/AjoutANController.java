package edu.connexion3b.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
//import edu.connexion3b.entities.GeminiAIService;
import edu.connexion3b.entities.Analyses;
import edu.connexion3b.entities.Capteurs;
import edu.connexion3b.services.Aservices;
import edu.connexion3b.services.Services;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class AjoutANController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button ajoutAn;

    @FXML
    private TextField tfidcap;

    @FXML
    private TextField tfrecom;

    @FXML
    private TextField tfdate;


    @FXML
    private Button btia;

    @FXML
    private TextField ia;

    @FXML
    private TextField reponse;

    @FXML
    private TextField tfval;

//    private final String apiKey = "AIzaSyBEHeH5I_7m2dtVmQf9NraBYrK4seYRARo"; // Replace with your API key
//    private final GeminiAIService geminiService = new GeminiAIService(apiKey);

    @FXML
    void ajoutan(ActionEvent event) {
        String  valeur = tfval.getText();
        String recommandation = tfrecom.getText();
        int idcap = Integer.parseInt(tfidcap.getText());
        String date = tfdate.getText();
        Analyses a = new Analyses(valeur,date,recommandation,idcap);
        Aservices ps = new Aservices();
        ps.ajouterEntity(a);

    }
//    @FXML
//    void genere(ActionEvent event) {
//        String prompt = ia.getText();
//
//        if (prompt.isEmpty()) {
//            reponse.setText("Veuillez entrer un prompt.");
//            return;
//        }
//
//        new Thread(() -> {
//            try {
//                String response = geminiService.generateText(prompt);
//                javafx.application.Platform.runLater(() ->
//                        reponse.setText(response));
//            } catch (IOException e) {
//                e.printStackTrace();
//                javafx.application.Platform.runLater(() -> reponse.setText("Erreur lors de la génération."));
//            }
//        }).start();
//    }
//


}
