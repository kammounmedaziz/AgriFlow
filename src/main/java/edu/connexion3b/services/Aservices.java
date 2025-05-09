package edu.connexion3b.services;

import edu.connexion3b.entities.Analyses;
import edu.connexion3b.interfaces.IServices;
import edu.connexion3b.tools.Myconnection;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Aservices implements IServices<Analyses> {

    @Override
    public void ajouterEntity(Analyses a) {
        try {
            if (a.getValeur() == null || a.getValeur().trim().isEmpty() ||
                    a.getDate() == null || a.getDate().trim().isEmpty() ||
                    a.getRecommendation() == null || a.getRecommendation().trim().isEmpty()) {

                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Champs vides");
                alert.setHeaderText(null);
                alert.setContentText("Tous les champs doivent être remplis !");
                alert.showAndWait();
                return;
            }
            if (!a.getDate().matches("\\d{2}/\\d{2}/\\d{4}")) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Format de date invalide");
                alert.setHeaderText(null);
                alert.setContentText("La date doit être au format jj/MM/aaaa (ex : 03/05/2025)");
                alert.showAndWait();
                return;
            }

            String requete = "INSERT INTO analyses (valeur, date, recommendation, idcap) VALUES (?, ?, ?, ?)";
            PreparedStatement pst = Myconnection.getInstance().getCnx().prepareStatement(requete);
            pst.setString(1, a.getValeur());
            pst.setString(2, a.getDate());
            pst.setString(3, a.getRecommendation());
            pst.setInt(4, a.getIdcap());
            pst.executeUpdate();
            Alert successAlert = new Alert(AlertType.INFORMATION);
            successAlert.setTitle("Succès");
            successAlert.setHeaderText(null);
            successAlert.setContentText("L'analyse a été ajoutée avec succès !");
            successAlert.showAndWait();
        } catch (SQLException e) {
            Alert errorAlert = new Alert(AlertType.ERROR);
            errorAlert.setTitle("Erreur SQL");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("Une erreur est survenue : " + e.getMessage());
            errorAlert.showAndWait();
        }
    }



    @Override
    public void supprimerEntity(int id) {
        try {
            String requete = "DELETE FROM analyses WHERE id = ?";
            PreparedStatement pst = Myconnection.getInstance().getCnx().prepareStatement(requete);
            pst.setInt(1, id);
            int rowsAffected = pst.executeUpdate();

            if (rowsAffected > 0) {

                Alert successAlert = new Alert(AlertType.INFORMATION);
                successAlert.setTitle("Suppression réussie");
                successAlert.setHeaderText(null);
                successAlert.setContentText("L'analyse a été supprimée avec succès !");
                successAlert.showAndWait();
            } else {
                Alert infoAlert = new Alert(AlertType.WARNING);
                infoAlert.setTitle("Aucun résultat");
                infoAlert.setHeaderText(null);
                infoAlert.setContentText("Aucune analyse trouvée avec l'ID : " + id);
                infoAlert.showAndWait();
            }
        } catch (SQLException e) {
            Alert errorAlert = new Alert(AlertType.ERROR);
            errorAlert.setTitle("Erreur SQL");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("Erreur lors de la suppression : " + e.getMessage());
            errorAlert.showAndWait();
        }
    }

    @Override
    public void updateEntity(int id, Analyses a) {
        try {
            String requete = "UPDATE analyses SET valeurs = ?, date = ?, recommendation = ?, idcap = ? WHERE id = ?";
            PreparedStatement pst = Myconnection.getInstance().getCnx().prepareStatement(requete);
            pst.setString(1, a.getValeur());
            pst.setString(2, a.getDate()); // Assure-toi que c'est bien une String ou adapte avec java.sql.Date si nécessaire
            pst.setString(3, a.getRecommendation());
            pst.setInt(4, a.getIdcap());
            pst.setInt(5, id);
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                Alert successAlert = new Alert(AlertType.INFORMATION);
                successAlert.setTitle("Mise à jour réussie");
                successAlert.setHeaderText(null);
                successAlert.setContentText("L'analyse a été mise à jour avec succès !");
                successAlert.showAndWait();
            } else {
                Alert infoAlert = new Alert(AlertType.WARNING);
                infoAlert.setTitle("Aucun résultat");
                infoAlert.setHeaderText(null);
                infoAlert.setContentText("Aucune analyse trouvée avec l'ID : " + id);
                infoAlert.showAndWait();
            }
        } catch (SQLException e) {
            Alert errorAlert = new Alert(AlertType.ERROR);
            errorAlert.setTitle("Erreur SQL");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("Erreur lors de la mise à jour : " + e.getMessage());
            errorAlert.showAndWait();
        }
    }
    @Override
    public List<Analyses> getAllData() {
        List<Analyses> data = new ArrayList<>();
        try {
            String requete = "SELECT * FROM analyses";
            Statement st = Myconnection.getInstance().getCnx().createStatement();
            ResultSet rs = st.executeQuery(requete);

            while (rs.next()) {
                Analyses a = new Analyses(
                        rs.getInt("id"),
                        rs.getString("valeurs"),
                        rs.getString("date"),
                        rs.getString("recommendation"),
                        rs.getInt("idcap")
                );
                data.add(a);
            }
        } catch (SQLException e) {
            Alert errorAlert = new Alert(AlertType.ERROR);
            errorAlert.setTitle("Erreur SQL");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("Erreur lors de la récupération des données : " + e.getMessage());
            errorAlert.showAndWait();
        }
        return data;
    }
}