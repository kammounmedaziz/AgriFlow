package gestions.ferme.services;


import gestions.ferme.entities.Utilisateur;
import gestions.ferme.interfaces.IService;
import gestions.ferme.tools.MyConnection;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurService implements IService<Utilisateur> {


    public void ajouterEntity(Utilisateur u) {

    }

    public void ajouterEntityVeretinaire(Utilisateur u) {
        try {
            // Contrôles de saisie
            if (u.getId() == null || !u.getId().matches("\\d{8}")) {
                throw new IllegalArgumentException("L'ID doit contenir exactement 8 chiffres.");
            }
            if (u.getNom() == null || u.getNom().isEmpty()) {
                throw new IllegalArgumentException("Nom invalide.");
            }
            if (u.getPrenom() == null || u.getPrenom().isEmpty()) {
                throw new IllegalArgumentException("Prénom invalide.");
            }


            if (u.getTelephone() == null || !u.getTelephone().matches("\\d{8,15}")) {
                throw new IllegalArgumentException("Numéro de téléphone invalide.");
            }
            if (u.getPassword() == null || u.getPassword().length() < 6) {
                throw new IllegalArgumentException("Mot de passe trop court (minimum 6 caractères).");
            }

            // Hachage du mot de passe
            String hashedPassword = hashPassword(u.getPassword());

            String requete = "INSERT INTO utilisateurs(id, nom, prenom, sex, role, telephone, email, password) VALUES (?,?,?,?,'vétérinaire',?,?,?)";
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setString(1, u.getId());
            pst.setString(2, u.getNom());
            pst.setString(3, u.getPrenom());
            pst.setString(4, u.getSex());
            pst.setString(5, u.getTelephone());
            pst.setString(6, u.getEmail());
            pst.setString(7, hashedPassword);

            pst.executeUpdate();
            System.out.println(" vétérinaireajouté avec succès.");

        } catch (SQLException e) {
            System.out.println("Erreur SQL : " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Erreur de validation : " + e.getMessage());
        }
    }

    public void ajouterEntityFermier(Utilisateur u) {
        try {
            // Contrôles de saisie
            if (u.getId() == null || !u.getId().matches("\\d{8}")) {
                throw new IllegalArgumentException("L'ID doit contenir exactement 8 chiffres.");
            }
            if (u.getNom() == null || u.getNom().isEmpty()) {
                throw new IllegalArgumentException("Nom invalide.");
            }
            if (u.getPrenom() == null || u.getPrenom().isEmpty()) {
                throw new IllegalArgumentException("Prénom invalide.");
            }


            if (u.getTelephone() == null || !u.getTelephone().matches("\\d{8,15}")) {
                throw new IllegalArgumentException("Numéro de téléphone invalide.");
            }
            if (u.getPassword() == null || u.getPassword().length() < 6) {
                throw new IllegalArgumentException("Mot de passe trop court (minimum 6 caractères).");
            }

            // Hachage du mot de passe
            String hashedPassword = hashPassword(u.getPassword());

            String requete = "INSERT INTO utilisateurs(id, nom, prenom, sex, role, telephone, email,password) VALUES (?,?,?,?,'fermier(e)',?,?,?)";
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setString(1, u.getId());
            pst.setString(2, u.getNom());
            pst.setString(3, u.getPrenom());
            pst.setString(4, u.getSex());
            pst.setString(5, u.getTelephone());
            pst.setString(6, u.getEmail());
            pst.setString(7, hashedPassword);

            pst.executeUpdate();
            System.out.println(" fermier ajouté avec succès.");

        } catch (SQLException e) {
            System.out.println("Erreur SQL : " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Erreur de validation : " + e.getMessage());
        }
    }

    public void ajouterEntityIngenieur(Utilisateur u) {
        try {
            // Contrôles de saisie
            if (u.getId() == null || !u.getId().matches("\\d{8}")) {
                throw new IllegalArgumentException("L'ID doit contenir exactement 8 chiffres.");
            }
            if (u.getNom() == null || u.getNom().isEmpty()) {
                throw new IllegalArgumentException("Nom invalide.");
            }
            if (u.getPrenom() == null || u.getPrenom().isEmpty()) {
                throw new IllegalArgumentException("Prénom invalide.");
            }


            if (u.getTelephone() == null || !u.getTelephone().matches("\\d{8,15}")) {
                throw new IllegalArgumentException("Numéro de téléphone invalide.");
            }
            if (u.getPassword() == null || u.getPassword().length() < 6) {
                throw new IllegalArgumentException("Mot de passe trop court (minimum 6 caractères).");
            }

            // Hachage du mot de passe
            String hashedPassword = hashPassword(u.getPassword());

            String requete = "INSERT INTO utilisateurs(id, nom, prenom, sex, role, telephone, email, password) VALUES (?,?,?,?,'ingénieur agriculture',?,?,?)";
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setString(1, u.getId());
            pst.setString(2, u.getNom());
            pst.setString(3, u.getPrenom());
            pst.setString(4, u.getSex());
            pst.setString(5, u.getTelephone());
            pst.setString(6, u.getEmail());
            pst.setString(7, hashedPassword);

            pst.executeUpdate();
            System.out.println(" igenieur ajouté avec succès.");

        } catch (SQLException e) {
            System.out.println("Erreur SQL : " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Erreur de validation : " + e.getMessage());
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



    @Override
    public void updateEntity(int idt, Utilisateur utilisateur) {

    }


    public void updateEntity2(String id, Utilisateur u ) {
        try {
            String requete = "UPDATE utilisateurs SET " +
                    "telephone = ?, " +
                    "email = ?, " +
                    "password = ? " +
                    "WHERE id = ?";

            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setString(1, u.getTelephone());
            pst.setString(2, u.getEmail());
            pst.setString(3, u.getPassword());
            pst.setString(4, id);

            int rowsUpdated = pst.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Mise à jour réussie pour l'utilisateur ID: " + id);
            } else {
                System.out.println("Aucun utilisateur trouvé avec l'ID: " + id);
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de la mise à jour: " + e.getMessage());
        }
    }

    @Override
    public void supprimerEntity(Utilisateur u ) {
        try {
            String requete = "DELETE FROM utilisateurs WHERE id = ?";
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setString(1, u.getId());
            int rowsDeleted = pst.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Utilisateur supprimé avec succès");
            } else {
                System.out.println("Aucun utilisateur trouvé avec cet ID");
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de la suppression: " + e.getMessage());
        }

    }

    @Override
    public List<Utilisateur> getAllData() {
        return List.of();
    }

    public List<Utilisateur> getAllDataVeterinaire() {
        List<Utilisateur> data = new ArrayList();
        try {
            String requete = "SELECT id, nom, prenom, sex,  telephone, email FROM utilisateurs where role= 'vétérinaire'";
            Statement st = MyConnection.getInstance().getCnx().createStatement();
            ResultSet rs = st.executeQuery(requete);
            while (rs.next()){
                Utilisateur utilisateur= new Utilisateur();
                utilisateur.setId(rs.getString("id"));
                utilisateur.setNom(rs.getString("nom"));
                utilisateur.setPrenom(rs.getString("prenom"));
                utilisateur.setSex(rs.getString("sex"));
                utilisateur.setTelephone(rs.getString("telephone"));
                utilisateur.setEmail(rs.getString("email"));

                data.add(utilisateur);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return data;


    }

    public List<Utilisateur> getAllDataFermier() {
        List<Utilisateur> data = new ArrayList();
        try {
            String requete = "SELECT id, nom, prenom, sex,  telephone, email FROM utilisateurs where role= 'fermier(e)'";
            Statement st = MyConnection.getInstance().getCnx().createStatement();
            ResultSet rs = st.executeQuery(requete);
            while (rs.next()){
                Utilisateur utilisateur= new Utilisateur();
                utilisateur.setId(rs.getString("id"));
                utilisateur.setNom(rs.getString("nom"));
                utilisateur.setPrenom(rs.getString("prenom"));
                utilisateur.setSex(rs.getString("sex"));
                utilisateur.setTelephone(rs.getString("telephone"));
                utilisateur.setEmail(rs.getString("email"));

                data.add(utilisateur);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return data;


    }

    public List<Utilisateur> getAllDataIngenieur() {
        List<Utilisateur> data = new ArrayList();
        try {
            String requete = "SELECT id, nom, prenom, sex,  telephone, email FROM utilisateurs where role= 'ingénieur agriculture'";
            Statement st = MyConnection.getInstance().getCnx().createStatement();
            ResultSet rs = st.executeQuery(requete);
            while (rs.next()){
                Utilisateur utilisateur= new Utilisateur();
                utilisateur.setId(rs.getString("id"));
                utilisateur.setNom(rs.getString("nom"));
                utilisateur.setPrenom(rs.getString("prenom"));
                utilisateur.setSex(rs.getString("sex"));
                utilisateur.setTelephone(rs.getString("telephone"));
                utilisateur.setEmail(rs.getString("email"));

                data.add(utilisateur);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return data;


    }
    public Utilisateur findById(String id) {
        Utilisateur utilisateur = null;

        try {
            String requete = "SELECT id, nom, prenom, sex, telephone, email FROM utilisateurs WHERE id = ?";
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setString(1, id);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                utilisateur = new Utilisateur();
                utilisateur.setId(rs.getString("id"));
                utilisateur.setNom(rs.getString("nom"));
                utilisateur.setPrenom(rs.getString("prenom"));
                utilisateur.setSex(rs.getString("sex"));
                utilisateur.setTelephone(rs.getString("telephone"));
                utilisateur.setEmail(rs.getString("email"));
            }

        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération : " + e.getMessage());
        }

        return utilisateur;
    }

}
