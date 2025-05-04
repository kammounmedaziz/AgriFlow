package gestions.ferme.services;

import gestions.ferme.tools.MyConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthenticationService {
    public String authentificationEtRetournerRole(String id, String password) {
        String requete = "SELECT role FROM utilisateurs WHERE id = ? AND password = ?";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setString(1, id);
            pst.setString(2, password);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                return rs.getString("role"); // retourne le rôle
            } else {
                return null; // identifiants incorrects
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'authentification : " + e.getMessage());
            return null;
        }
    }
}
