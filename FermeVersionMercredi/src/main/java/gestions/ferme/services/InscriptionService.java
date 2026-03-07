package gestions.ferme.services;

import gestions.ferme.entities.Utilisateur;
import gestions.ferme.interfaces.IService;
import gestions.ferme.tools.MyConnection;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class InscriptionService implements IService<Utilisateur> {


    @Override
    public void ajouterEntity(Utilisateur u) {


            try {
                // Vérification des champs
                if (u.getId() == null || !u.getId().matches("\\d{8}")) {
                    throw new IllegalArgumentException("L'ID doit contenir exactement 8 chiffres.");
                }
                if (u.getNom() == null || u.getNom().isEmpty()) {
                    throw new IllegalArgumentException("Nom invalide.");
                }
                if (u.getPrenom() == null || u.getPrenom().isEmpty()) {
                    throw new IllegalArgumentException("Prénom invalide.");
                }
                if (u.getRole() == null || u.getRole().isEmpty()) {
                    throw new IllegalArgumentException("Rôle invalide.");
                }
                if (u.getTelephone() == null || !u.getTelephone().matches("\\d{8,15}")) {
                    throw new IllegalArgumentException("Numéro de téléphone invalide.");
                }
                if (u.getPassword() == null || u.getPassword().length() < 6) {
                    throw new IllegalArgumentException("Mot de passe trop court (minimum 6 caractères).");
                }
                if (u.getEmail() == null || !u.getEmail().matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                    throw new IllegalArgumentException("Email invalide.");
                }

                // Vérification de l'unicité de l'email et de l'ID dans la base de données
                if (isIdExist(u.getId())) {
                    throw new IllegalArgumentException("ID déjà utilisé.");
                }
                if (isEmailExist(u.getEmail())) {
                    throw new IllegalArgumentException("Email déjà utilisé.");
                }

                // Hachage du mot de passe
                String hashedPassword = hashPassword(u.getPassword());

                // Insertion dans la base de données
                String requete = "INSERT INTO utilisateurs(id, nom, prenom, sex, role, telephone, password, email) VALUES (?,?,?,?,?,?,?,?)";
                PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
                pst.setString(1, u.getId());
                pst.setString(2, u.getNom());
                pst.setString(3, u.getPrenom());
                pst.setString(4, u.getSex());
                pst.setString(5, u.getRole());
                pst.setString(6, u.getTelephone());
                pst.setString(7, hashedPassword);
                pst.setString(8, u.getEmail());

                pst.executeUpdate();
                System.out.println("Utilisateur ajouté avec succès.");

            } catch (SQLException e) {
                if (e.getMessage().contains("Duplicate entry") && e.getMessage().contains("PRIMARY")) {
                    System.out.println("Erreur : Un utilisateur avec cet ID existe déjà.");
                } else {
                    System.out.println("Erreur SQL : " + e.getMessage());
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Erreur de validation : " + e.getMessage());
                throw e;  // Re-throw exception to let the controller handle the alert
            }
    }

    // Méthode de hachage SHA-256
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
    // Vérifie si l'ID existe déjà dans la base de données
    public boolean isIdExist(String id) {
        String query = "SELECT COUNT(*) FROM utilisateurs WHERE id = ?";
        try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query)) {
            pst.setString(1, id);
            ResultSet rs = pst.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Vérifie si l'email existe déjà dans la base de données
    public boolean isEmailExist(String email) {
        String query = "SELECT COUNT(*) FROM utilisateurs WHERE email = ?";
        try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query)) {
            pst.setString(1, email);
            ResultSet rs = pst.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



    @Override
    public void supprimerEntity(Utilisateur utilisateur) {

    }

    @Override
    public void updateEntity(int idt, Utilisateur utilisateur) {

    }

    @Override
    public List<Utilisateur> getAllData() {
        return List.of();
    }
}
