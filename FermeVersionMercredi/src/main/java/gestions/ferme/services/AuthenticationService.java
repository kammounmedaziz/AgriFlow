package gestions.ferme.services;

import gestions.ferme.tools.MyConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AuthenticationService {

    // Méthode pour hacher le mot de passe
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erreur lors du hachage du mot de passe", e);
        }
    }

    // Méthode d'authentification
    public String authentificationEtRetournerRole(String id, String password) {
        if ("defaultClient".equals(id) && "defaultPassword".equals(password)) {
            return "client(e)";  // Rôle "client(e)" pour un utilisateur fictif
        }

        // Hachage du mot de passe entré par l'utilisateur
        String hashedPassword = hashPassword(password);

        // Requête SQL pour récupérer le rôle basé sur l'ID et le mot de passe haché
        String requete = "SELECT role FROM utilisateurs WHERE id = ? AND password = ?";
        try {
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setString(1, id);
            pst.setString(2, hashedPassword);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                return rs.getString("role"); // Retourne le rôle si l'utilisateur est trouvé
            } else {
                return null; // Identifiants incorrects
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'authentification : " + e.getMessage());
            return null;
        }
    }
}
