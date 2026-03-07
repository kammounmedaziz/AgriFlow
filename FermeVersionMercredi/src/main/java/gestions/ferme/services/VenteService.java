package gestions.ferme.services;

import gestions.ferme.entities.Vente;
import gestions.ferme.interfaces.IService;
import gestions.ferme.tools.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.time.DayOfWeek;
import java.time.LocalDate;

public class VenteService implements IService<Vente> {
    // Méthode pour générer un ID aléatoire entre 1000 et 9999
    private int generateRandomId() {
        return ThreadLocalRandom.current().nextInt(1000, 10000); // 1000 (inclus) à 9999 (inclus)
    }

    // Méthode pour vérifier si un ID existe déjà
    private boolean idExists(int id) {
        try {
            String query = "SELECT COUNT(*) FROM produits WHERE idPr = ?";
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification de l'ID: " + e.getMessage());
        }
        return false;
    }

    // Méthode de validation de la saisie
   private String validerSaisie(Vente vente) {
       // Vérifier si l'ID du produit est valide
       if (vente.getIdProduit() <= 0) {
           return "Erreur : L'ID du produit est invalide.";
       }

       // Vérifier si la quantité vendue est valide
       if (vente.getQuantiteVente() <= 0) {
           return "Erreur : La quantité vendue doit être positive.";
       }

       // Vérifier si l'ID du client est valide (non null et non vide)
       if (vente.getIdCliente() == null || vente.getIdCliente().isEmpty()) {
           return "Erreur : L'ID du client est requis.";
       }

       // Vérifier si la quantité demandée ne dépasse pas la quantité disponible
       try {
           String checkProduit = "SELECT quantite_disponible FROM produits WHERE idPr = ?";
           PreparedStatement checkProduitStmt = MyConnection.getInstance().getCnx().prepareStatement(checkProduit);
           checkProduitStmt.setInt(1, vente.getIdProduit());
           ResultSet produitResult = checkProduitStmt.executeQuery();

           if (!produitResult.next()) {
               return "Erreur : Le produit avec l'ID " + vente.getIdProduit() + " n'existe pas.";
           }

           float quantiteDisponible = produitResult.getFloat("quantite_disponible");
           if (vente.getQuantiteVente() > quantiteDisponible) {
               return "Erreur : Quantité insuffisante. Disponible: " + quantiteDisponible + ", Demandée: " + vente.getQuantiteVente();
           }
       } catch (SQLException e) {
           return "Erreur de base de données lors de la validation : " + e.getMessage();
       }

       // Vérifier si le client existe
       try {
           String checkClient = "SELECT COUNT(*) FROM utilisateurs WHERE id = ?";
           PreparedStatement checkClientStmt = MyConnection.getInstance().getCnx().prepareStatement(checkClient);
           checkClientStmt.setString(1, vente.getIdCliente());
           ResultSet clientResult = checkClientStmt.executeQuery();
           clientResult.next();
           if (clientResult.getInt(1) == 0) {
               return "Erreur : Le client avec l'ID " + vente.getIdCliente() + " n'existe pas.";
           }
       } catch (SQLException e) {
           return "Erreur de base de données lors de la validation du client : " + e.getMessage();
       }

       // Si tout est valide
       return null; // Aucun problème, tout est ok
   }


    @Override
    public void ajouterEntity(Vente vente) {
        try {
            // Appeler la méthode de validation de la saisie
            String validationMessage = validerSaisie(vente);
            if (validationMessage != null) {
                System.out.println(validationMessage);
                return; // Si la validation échoue, afficher le message d'erreur et sortir de la méthode
            }

            // Générer un ID aléatoire et vérifier qu'il n'existe pas déjà
            int idVente = generateRandomId();
            while (idExists(idVente)) {
                idVente = generateRandomId();  // Générer un autre ID si celui-ci existe déjà
            }
            vente.setId(idVente);  // Assigner l'ID généré à l'objet vente

            // Calcul du revenu total
            String checkProduit = "SELECT prix_unitaire FROM produits WHERE idPr = ?";
            PreparedStatement checkProduitStmt = MyConnection.getInstance().getCnx().prepareStatement(checkProduit);
            checkProduitStmt.setInt(1, vente.getIdProduit());
            ResultSet produitResult = checkProduitStmt.executeQuery();
            if (!produitResult.next()) {
                System.out.println("Erreur : Le produit avec l'ID " + vente.getIdProduit() + " n'existe pas.");
                return;
            }

            float prixUnitaire = produitResult.getFloat("prix_unitaire");
            float quantiteVendue = vente.getQuantiteVente();
            float revenuTotal = prixUnitaire * quantiteVendue;
            vente.setRevenuTotal(revenuTotal);

            // Insertion dans la table vente
            String requete = "INSERT INTO vente(id, id_produit, quantite_vendue, revenu_total, client_id,paiement) VALUES(?,?,?,?,?,0)";
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setInt(1, vente.getId());
            pst.setInt(2, vente.getIdProduit());
            pst.setFloat(3, quantiteVendue);
            pst.setFloat(4, revenuTotal);
            pst.setString(5, vente.getIdCliente());
            pst.executeUpdate();

            // Mise à jour de la quantité disponible dans produits
            String updateProduit = "UPDATE produits SET quantite_disponible = quantite_disponible - ? WHERE idPr = ?";
            PreparedStatement updateStmt = MyConnection.getInstance().getCnx().prepareStatement(updateProduit);
            updateStmt.setFloat(1, quantiteVendue);
            updateStmt.setInt(2, vente.getIdProduit());
            updateStmt.executeUpdate();

            System.out.println("Vente ajoutée avec succès. ID de la vente : " + vente.getId() + ", Revenu : " + revenuTotal + " dt. Nouvelle quantité disponible mise à jour.");
        } catch (Exception e) {
            System.out.println("Erreur lors de l'ajout de la vente : " + e.getMessage());
        }
    }


    @Override
    public void supprimerEntity(Vente vente) {
        try {
            String requete = "DELETE FROM vente WHERE id = ? AND paiement = 0 AND TIMESTAMPDIFF(HOUR, date_vente, NOW()) > 2";

            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setInt(1, vente.getId());
            int rowsDeleted = pst.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println(" Vente supprimée avec succès.");
            } else {
                System.out.println(" Vente non supprimée : soit elle n'existe pas, a déjà été payée, ou moins de 2h se sont écoulées.");
            }
        } catch (SQLException e) {
            System.err.println(" Erreur lors de la suppression : " + e.getMessage());
        }
    }






    // Vérifie si une vente avec cet ID existe
    private boolean venteExiste(int idt) {
        try {
            String query = "SELECT COUNT(*) FROM vente WHERE id = ?";
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query);
            pst.setInt(1, idt);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification de la vente : " + e.getMessage());
        }
        return false;
    }

    // Récupère la quantité vendue actuelle de la vente
    private float getQuantiteVendueActuelle(int idt) {
        try {
            String query = "SELECT quantite_vendue FROM vente WHERE id = ?";
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query);
            pst.setInt(1, idt);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) return rs.getFloat("quantite_vendue");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de la quantité actuelle : " + e.getMessage());
        }
        return 0;
    }
    @Override
    public void updateEntity(int idt, Vente vente) {
        try {
            if (!venteExiste(idt)) {
                System.out.println("Erreur : Vente ID " + idt + " introuvable.");
                return;
            }

            float ancienneQuantite = getQuantiteVendueActuelle(idt);
            int idProduit = vente.getIdProduit();

            // 1. Récupérer quantite_disponible, prix_unitaire
            String produitQuery = "SELECT quantite_disponible, prix_unitaire FROM produits WHERE idPr = ?";
            PreparedStatement produitStmt = MyConnection.getInstance().getCnx().prepareStatement(produitQuery);
            produitStmt.setInt(1, idProduit);
            ResultSet rsProduit = produitStmt.executeQuery();

            if (!rsProduit.next()) {
                System.out.println("Erreur : Produit introuvable.");
                return;
            }

            float quantiteDisponible = rsProduit.getFloat("quantite_disponible");
            float prixUnitaire = rsProduit.getFloat("prix_unitaire");

            // 2. Récupérer champ paiement de la vente
            int paiement = getPaiementActuel(idt);
            float nouvelleQuantite = vente.getQuantiteVente();
            float difference = nouvelleQuantite - ancienneQuantite;

            // 3. Traitement selon les cas
            float revenuTotal;
            if (difference > 0) {
                // Augmentation : vérifier stock
                if (quantiteDisponible < difference) {
                    System.out.println("Erreur : Stock insuffisant.");
                    return;
                }
                revenuTotal = nouvelleQuantite * prixUnitaire;

            } else {
                // Diminution
                float reduction = -difference;
                if (paiement == 1) {
                    // Appliquer pénalité de 10% sur la partie retirée
                    float penalite = reduction * prixUnitaire * 0.10f;
                    revenuTotal = nouvelleQuantite * prixUnitaire - penalite;
                    System.out.println("Pénalité appliquée : " + penalite);
                } else {
                    // Paiement non effectué → normal
                    revenuTotal = nouvelleQuantite * prixUnitaire;
                }
            }

            // 4. Mise à jour de la vente
            String updateVente = "UPDATE vente SET quantite_vendue = ?, revenu_total = ? WHERE id = ?";
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(updateVente);
            pst.setFloat(1, nouvelleQuantite);
            pst.setFloat(2, revenuTotal);
            pst.setInt(3, idt);
            pst.executeUpdate();

            // 5. Mise à jour du stock
            float nouvelleQteDisponible = quantiteDisponible - difference;
            String updateStock = "UPDATE produits SET quantite_disponible = ? WHERE idPr = ?";
            PreparedStatement updateStockStmt = MyConnection.getInstance().getCnx().prepareStatement(updateStock);
            updateStockStmt.setFloat(1, nouvelleQteDisponible);
            updateStockStmt.setInt(2, idProduit);
            updateStockStmt.executeUpdate();

            System.out.println("Mise à jour réussie. Nouveau revenu : " + revenuTotal);
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }

    private int getPaiementActuel(int idVente) {
        try {
            String query = "SELECT paiement FROM vente WHERE id = ?";
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query);
            pst.setInt(1, idVente);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt("paiement");
            }
        } catch (SQLException e) {
            System.err.println("Erreur récupération paiement : " + e.getMessage());
        }
        return 0;
    }

    public void calculerFactures(String idClient) {
        try {
            // 1. Calculer la somme des revenus impayés pour ce client
            String sumQuery = "SELECT SUM(revenu_total) AS total FROM vente WHERE client_id = ? AND paiement = 0";
            PreparedStatement sumStmt = MyConnection.getInstance().getCnx().prepareStatement(sumQuery);
            sumStmt.setString(1, idClient);
            ResultSet rs = sumStmt.executeQuery();

            if (rs.next()) {
                float totalRendu = rs.getFloat("total");

                if (totalRendu == 0) {
                    System.out.println("Aucune vente impayée pour le client ID: " + idClient);
                    return;
                }

                // 2. Générer un ID facture unique
                int idF = generateUniqueFactureId();

                // 3. Insertion dans la table factures
                String insertFacture = "INSERT INTO factures (idF, idClient, rendu) VALUES (?, ?, ?)";
                PreparedStatement insertStmt = MyConnection.getInstance().getCnx().prepareStatement(insertFacture);
                insertStmt.setInt(1, idF);
                insertStmt.setString(2, idClient);
                insertStmt.setFloat(3, totalRendu);
                insertStmt.executeUpdate();

                // 4. Mise à jour des ventes concernées
                String updatePaiement = "UPDATE vente SET paiement = 1 WHERE client_id = ? AND paiement = 0";
                PreparedStatement updateStmt = MyConnection.getInstance().getCnx().prepareStatement(updatePaiement);
                updateStmt.setString(1, idClient);
                int lignesModifiees = updateStmt.executeUpdate();

                System.out.println("Facture générée (ID: " + idF + ", Montant: " + totalRendu + ")");
                System.out.println("Ventes mises à jour: " + lignesModifiees);
            } else {
                System.out.println("Erreur : Client introuvable ou problème de lecture.");
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors du calcul de la facture : " + e.getMessage());
        }
    }

    // Génère un ID unique non utilisé
    private int generateUniqueFactureId() {
        int id;
        do {
            id = generateRandomId();
        } while (idExists(id));
        return id;
    }






    @Override
    public List<Vente> getAllData() {

            List<Vente> ventes = new ArrayList<>();
            try {
                String requete = "SELECT id,  id_produit, quantite_vendue, date_vente, revenu_total, client_id FROM vente";
                Statement st = MyConnection.getInstance().getCnx().createStatement();
                ResultSet rs = st.executeQuery(requete);

                while (rs.next()) {
                    Vente vente = new Vente();
                    vente.setId(rs.getInt("id"));
                    vente.setIdProduit(rs.getInt("id_produit"));
                    vente.setQuantiteVente(rs.getFloat("quantite_vendue"));

                    // Convertir la date_vente en LocalDate
                    vente.setDataVente(rs.getDate("date_vente").toLocalDate());

                    vente.setRevenuTotal(rs.getFloat("revenu_total"));
                    vente.setIdCliente(rs.getString("client_id"));

                    ventes.add(vente);
                }
            } catch (SQLException e) {
                System.out.println("Erreur chargement ventes: " + e.getMessage());
            }
            return ventes;

    }





    public List<String> getDistinctNomProduit() {
        List<String> nomPr = new ArrayList<>();
        String query = "SELECT DISTINCT nomPr FROM produits";
        try {
            Connection cnx = MyConnection.getInstance().getCnx();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                nomPr.add(rs.getString("nomPr"));
            }
        } catch (SQLException e) {
            System.out.println("Erreur getDistinctProduits: " + e.getMessage());
        }
        return nomPr;
    }

    public boolean isPromotionActive() {
        LocalDate today = LocalDate.now();
        return today.getDayOfWeek() == DayOfWeek.TUESDAY;
    }

    public double calculatePromoPrice(double originalPrice) {
        if (isPromotionActive()) {
            return originalPrice * 0.9; // 10% de réduction
        }
        return originalPrice;
    }

}
