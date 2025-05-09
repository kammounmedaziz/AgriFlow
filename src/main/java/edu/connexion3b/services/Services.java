package edu.connexion3b.services;

import edu.connexion3b.entities.Capteurs;
import edu.connexion3b.interfaces.IServices;
import edu.connexion3b.tools.Myconnection;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;


import java.sql.PreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Services implements IServices<Capteurs> {
    @Override
    public void ajouterEntity(Capteurs c) {
        try {
            // Vérification des champs non vides
            if (c.getTypes() == null || c.getTypes().trim().isEmpty() ||c.getNom() == null ||
                     c.getNom().trim().isEmpty() ||
                    c.getDerniere_lecteures() == null || c.getDerniere_lecteures().trim().isEmpty()) {

                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Champs vides");
                alert.setHeaderText(null);
                alert.setContentText("Tous les champs doivent être remplis !");
                alert.showAndWait();
                return;
            }

            String requete = "INSERT INTO capteurs (nom,types, emplacements, dernier_lectures) VALUES (" +
                    "'" + c.getNom() + "', '" + c.getTypes() + "', '" + c.getEmplacements() + "', '" + c.getDerniere_lecteures() + "')";

            Statement st = Myconnection.getInstance().getCnx().createStatement();
            st.executeUpdate(requete);

            // ✅ Alerte de succès
            Alert successAlert = new Alert(AlertType.INFORMATION);
            successAlert.setTitle("Succès");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Le capteur a été ajouté avec succès !");
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
            String requete = "DELETE FROM capteurs WHERE id = ?";
            PreparedStatement pst = Myconnection.getInstance().getCnx().prepareStatement(requete);
            pst.setInt(1, id);
            int rowsAffected = pst.executeUpdate();

            if (rowsAffected > 0) {
                // ✅ Alerte de succès
                Alert successAlert = new Alert(AlertType.INFORMATION);
                successAlert.setTitle("Suppression réussie");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Le capteur a été supprimé avec succès !");
                successAlert.showAndWait();
            } else {
                Alert infoAlert = new Alert(AlertType.WARNING);
                infoAlert.setTitle("Aucun résultat");
                infoAlert.setHeaderText(null);
                infoAlert.setContentText("Aucun capteur trouvé avec l'ID : " + id);
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
    public void updateEntity(int id, Capteurs c) {
        try {
            String requete = "UPDATE capteurs SET nom= ?, types = ?, emplacements = ?, dernier_lectures = ? WHERE id = ?";
            PreparedStatement pst = Myconnection.getInstance().getCnx().prepareStatement(requete);
            pst.setString(1, c.getNom());
            pst.setString(2, c.getTypes());
            pst.setString(3, c.getEmplacements());
            pst.setString(4, c.getDerniere_lecteures());
            pst.setInt(5, id);

            int rowsAffected = pst.executeUpdate();

            if (rowsAffected > 0) {
                Alert successAlert = new Alert(AlertType.INFORMATION);
                successAlert.setTitle("Mise à jour réussie");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Le capteur a été mis à jour avec succès !");
                successAlert.showAndWait();
            } else {
                Alert infoAlert = new Alert(AlertType.WARNING);
                infoAlert.setTitle("Aucun résultat");
                infoAlert.setHeaderText(null);
                infoAlert.setContentText("Aucun capteur trouvé avec l'ID : " + id);
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
    public List<Capteurs> getAllData() {
        List<Capteurs> data = new ArrayList();
        try {
            String requete = "SELECT * FROM capteurs";
            Statement st = Myconnection.getInstance().getCnx().createStatement();
            ResultSet rs = st.executeQuery(requete);
            while (rs.next()){
                Capteurs c = new Capteurs();
                c.setId(rs.getInt(1));
                c.setNom(rs.getString("nom/reference"));
                c.setTypes(rs.getString("types"));
                c.setEmplacements(rs.getString("emplacements"));
                c.setDerniere_lecteurs(rs.getString("dernier_lectures"));
                data.add(c);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return data;
    }

}
