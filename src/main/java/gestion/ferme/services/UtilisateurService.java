package gestion.ferme.services;

import gestion.ferme.entities.ProduitVendu;
import gestion.ferme.entities.Utilisateur;
import gestion.ferme.interfaces.IService;
import gestion.ferme.tools.MyConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurService implements IService<Utilisateur> {
    @Override
    public void ajouterEntity(Utilisateur u) {
        try {
            String requete ="INSERT INTO utilisateurs(id, nom, prenom, sex, role, telephone, password)" +
                    "VALUES (?,?,?,?,?,?,?)";

            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setString(1,u.getId());
            pst.setString(2,u.getNom());
            pst.setString(3,u.getPrenom());
            pst.setString(4,u.getSex());
            pst.setString(5,u.getRole());
            pst.setString(6,u.getTelephone());
            pst.setString(7,u.getPassword());


            pst.executeUpdate();
            System.out.println("User added...");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
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
    public void updateEntity(int idt, Utilisateur utilisateur) {

    }


    public void updateEntity2(String id, Utilisateur u ) {
        try {
            String requete = "UPDATE utilisateurs SET " +
                    "telephone = ?, " +
                    "password = ? " +
                    "WHERE id = ?";

            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setString(1, u.getTelephone());
            pst.setString(2, u.getPassword());
            pst.setString(3, id);  // Utiliser le paramètre id plutôt que u.getId()

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
    public List<Utilisateur> getAllData() {
        List<Utilisateur> data = new ArrayList();
        try {
            String requete = "SELECT * FROM utilisateurs";
            Statement st = MyConnection.getInstance().getCnx().createStatement();
            ResultSet rs = st.executeQuery(requete);
            while (rs.next()){
                Utilisateur utilisateur= new Utilisateur();
                utilisateur.setId(rs.getString(1));
                utilisateur.setNom(rs.getString(2));
                utilisateur.setPrenom(rs.getString(3));
                utilisateur.setSex(rs.getString(4));
                utilisateur.setRole(rs.getString(5));
                utilisateur.setTelephone(rs.getString(6));
                utilisateur.setPassword(rs.getString(7));
                data.add(utilisateur);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return data;


    }
}
