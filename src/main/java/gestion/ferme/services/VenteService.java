package gestion.ferme.services;

import gestion.ferme.entities.Vente;
import gestion.ferme.interfaces.IService;
import gestion.ferme.tools.MyConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class VenteService implements IService<Vente> {
    @Override
    public void ajouterEntity(Vente vente) {

           /* try {
                // Vérifier si le produit existe
                String checkProduit = "SELECT COUNT(*) FROM produits WHERE idPr = ?";
                PreparedStatement checkProduitStmt = MyConnection.getInstance().getCnx().prepareStatement(checkProduit);
                checkProduitStmt.setInt(1, vente.getIdProduit());
                ResultSet produitResult = checkProduitStmt.executeQuery();
                produitResult.next();
                int produitCount = produitResult.getInt(1);

                float quantiteDisponible = produitResult.getFloat("quantite_disponible");
                float quantiteVendue = vente.getQuantiteVente();

                // 2. Vérifier si la quantité est suffisante
                if (quantiteDisponible < quantiteVendue) {
                    System.out.println("Erreur: Quantité insuffisante. Disponible: " + quantiteDisponible + ", Demandée: " + quantiteVendue);
                    return;
                }

                // Vérifier si le client existe
                String checkClient = "SELECT COUNT(*) FROM utilisateurs WHERE id = ?";
                PreparedStatement checkClientStmt = MyConnection.getInstance().getCnx().prepareStatement(checkClient);
                checkClientStmt.setString(1, vente.getIdCliente());
                ResultSet clientResult = checkClientStmt.executeQuery();
                clientResult.next();
                int clientCount = clientResult.getInt(1);

                if (produitCount > 0 && clientCount > 0) {
                    // Les deux existent, on peut procéder à l'insertion
                    String requete = "INSERT INTO vente(id, id_produit, quantite_vendue, revenu_total, client_id) "+
                            "VALUES(?,?,?,?,?)";
                    PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
                    pst.setInt(1, vente.getId());
                    pst.setInt(2, vente.getIdProduit());
                    pst.setFloat(3, vente.getQuantiteVente());
                    pst.setFloat(4, vente.getRevenuTotal());
                    pst.setString(5, vente.getIdCliente());
                    pst.executeUpdate();
                    System.out.println("Vente ajoutée avec succès");
                } else {
                    if (produitCount == 0) {
                        System.out.println("Erreur: Le produit avec l'ID " + vente.getIdProduit() + " n'existe pas");
                    }
                    if (clientCount == 0) {
                        System.out.println("Erreur: Le client avec l'ID " + vente.getIdCliente() + " n'existe pas");
                    }
                }
            } catch (Exception e) {
                System.out.println("Erreur lors de l'ajout de la vente: " + e.getMessage());
            }*/

            try {
                // 1. Vérifier si le produit existe et récupérer la quantité disponible
                String checkProduit = "SELECT quantite_disponible FROM produits WHERE idPr = ?";
                PreparedStatement checkProduitStmt = MyConnection.getInstance().getCnx().prepareStatement(checkProduit);
                checkProduitStmt.setInt(1, vente.getIdProduit());
                ResultSet produitResult = checkProduitStmt.executeQuery();

                if (!produitResult.next()) {
                    System.out.println("Erreur: Le produit avec l'ID " + vente.getIdProduit() + " n'existe pas");
                    return;
                }

                float quantiteDisponible = produitResult.getFloat("quantite_disponible");
                float quantiteVendue = vente.getQuantiteVente();

                // 2. Vérifier la quantité disponible
                if (quantiteDisponible < quantiteVendue) {
                    System.out.println("Erreur: Quantité insuffisante. Disponible: " + quantiteDisponible + ", Demandée: " + quantiteVendue);
                    return;
                }

                // 3. Vérifier si le client existe
                String checkClient = "SELECT COUNT(*) FROM utilisateurs WHERE id = ?";
                PreparedStatement checkClientStmt = MyConnection.getInstance().getCnx().prepareStatement(checkClient);
                checkClientStmt.setString(1, vente.getIdCliente());
                ResultSet clientResult = checkClientStmt.executeQuery();
                clientResult.next();
                int clientCount = clientResult.getInt(1);

                if (clientCount == 0) {
                    System.out.println("Erreur: Le client avec l'ID " + vente.getIdCliente() + " n'existe pas");
                    return;
                }

                // 4. Insertion dans la table vente
                String requete = "INSERT INTO vente(id, id_produit, quantite_vendue, revenu_total, client_id) VALUES(?,?,?,?,?)";
                PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
                pst.setInt(1, vente.getId());
                pst.setInt(2, vente.getIdProduit());
                pst.setFloat(3, vente.getQuantiteVente());
                pst.setFloat(4, vente.getRevenuTotal());
                pst.setString(5, vente.getIdCliente());
                pst.executeUpdate();

                // 5. Mise à jour de la quantité disponible dans produits
                float nouvelleQuantite = quantiteDisponible - quantiteVendue;
                String updateProduit = "UPDATE produits SET quantite_disponible = ? WHERE idPr = ?";
                PreparedStatement updateStmt = MyConnection.getInstance().getCnx().prepareStatement(updateProduit);
                updateStmt.setFloat(1, nouvelleQuantite);
                updateStmt.setInt(2, vente.getIdProduit());
                updateStmt.executeUpdate();

                System.out.println("Vente ajoutée avec succès. Nouvelle quantité disponible: " + nouvelleQuantite);
            } catch (Exception e) {
                System.out.println("Erreur lors de l'ajout de la vente: " + e.getMessage());
            }



    }

    @Override
    public void supprimerEntity(Vente vente) {
        try {
            String requete = "DELETE FROM vente WHERE id = ?";
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setInt(1, vente.getId());
            int rowsDeleted = pst.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Vente supprimé avec succès");
            } else {
                System.out.println("Aucun vente trouvé avec cet ID");
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de la suppression: " + e.getMessage());
        }


    }

    @Override
    public void updateEntity(int idt, Vente vente) {
        try {
            String requete = "UPDATE vente SET " +
                    "quantite_vendue = ?, " +
                    "revenu_total = ? " +
                    "WHERE id = ?";

            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setFloat(1, vente.getQuantiteVente());
            pst.setFloat(2, vente.getRevenuTotal());
            pst.setInt(3, idt);

            int rowsUpdated = pst.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Mise à jour réussie pour le vente ID: " + idt);
            } else {
                System.out.println("Aucun vente trouvé avec l'ID: " + idt);
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de la mise à jour: " + e.getMessage());
        }

    }

    @Override
    public List<Vente> getAllData() {
        return List.of();
    }
}
