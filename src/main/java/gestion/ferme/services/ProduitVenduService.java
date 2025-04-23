package gestion.ferme.services;

import gestion.ferme.entities.ProduitVendu;
import gestion.ferme.interfaces.IService;
import gestion.ferme.tools.MyConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProduitVenduService implements IService<ProduitVendu> {

    @Override
    public void ajouterEntity(ProduitVendu produitVendu) {
        try {
            String requete = " INSERT INTO produits (idPr, nomPr, typePr, quantite_disponible, unite, prix_unitaire) "+
                    "VALUES(?,?,?,?,?,?)";
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setInt(1, produitVendu.getIdProduit());
            pst.setString(2,produitVendu.getNomProduit());
            pst.setString(3,produitVendu.getTypeProduit());
            pst.setFloat(4,produitVendu.getQuantiteDisponible());
            pst.setString(5,produitVendu.getUniteProduit());
            pst.setFloat(6,produitVendu.getPrixUnitaire());
            pst.executeUpdate();
            System.out.println("Produit a vendu add");
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void supprimerEntity(ProduitVendu produitVendu) {
        try {
            String requete = "DELETE FROM produits WHERE idPr = ?";
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setInt(1, produitVendu.getIdProduit());
            int rowsDeleted = pst.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Produit supprimé avec succès");
            } else {
                System.out.println("Aucun produit trouvé avec cet ID");
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de la suppression: " + e.getMessage());
        }

    }

    @Override
    public void updateEntity(int id, ProduitVendu produitVendu) {
        try {
            String requete = "UPDATE produits SET " +
                    "quantite_disponible = ?, " +
                    "prix_unitaire = ? " +
                    "WHERE idPr = ?";

            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setFloat(1, produitVendu.getQuantiteDisponible());
            pst.setFloat(2, produitVendu.getPrixUnitaire());
            pst.setInt(3, id);

            int rowsUpdated = pst.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Mise à jour réussie pour le produit ID: " + id);
            } else {
                System.out.println("Aucun produit trouvé avec l'ID: " + id);
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de la mise à jour: " + e.getMessage());
        }

    }




    @Override
    public List<ProduitVendu> getAllData() {
        List<ProduitVendu> data = new ArrayList();
        try {
            String requete = "SELECT * FROM produits ";
            Statement st = MyConnection.getInstance().getCnx().createStatement();
            ResultSet rs = st.executeQuery(requete);
            while (rs.next()){
                ProduitVendu produitVendu = new ProduitVendu();
                produitVendu.setIdProduit(rs.getInt(1));
                produitVendu.setNomProduit(rs.getString(2));
                produitVendu.setTypeProduit(rs.getString(3));
                produitVendu.setQuantiteDisponible(rs.getFloat(4));
                produitVendu.setUniteProduit(rs.getString(5));
                produitVendu.setPrixUnitaire(rs.getFloat(6));
                data.add(produitVendu);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return data;

    }
}
