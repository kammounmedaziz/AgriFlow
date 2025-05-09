package edu.connexion3b.controllers;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import edu.connexion3b.entities.Capteurs;
import edu.connexion3b.services.Services;

public class SuppressionController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btvsupp;

    @FXML
    private TextField tfidcapsupp;

    @FXML
    void supp(ActionEvent event) {
        int id_cap = Integer.parseInt(tfidcapsupp.getText());
        Services rs = new Services();
        rs.supprimerEntity(id_cap);
    }

    @FXML
    void initialize() {
        assert btvsupp != null : "fx:id=\"btvsupp\" was not injected: check your FXML file 'Suppression.fxml'.";
        assert tfidcapsupp != null : "fx:id=\"tfidcapsupp\" was not injected: check your FXML file 'Suppression.fxml'.";

    }

}
