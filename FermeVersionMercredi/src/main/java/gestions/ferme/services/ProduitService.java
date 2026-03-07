package gestions.ferme.services;

import gestions.ferme.entities.Produit;
import gestions.ferme.interfaces.IService;
import gestions.ferme.tools.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ProduitService implements IService<Produit> {


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


    private boolean isValidProduit(Produit p) {
        boolean isValid = true;

        if (p.getNomProduit() == null || p.getNomProduit().trim().isEmpty()) {
            System.err.println("Erreur : Le nom du produit est obligatoire.");
            isValid = false;
        } else if (p.getNomProduit().length() > 100) {
            System.err.println("Erreur : Le nom du produit est trop long (max 100 caractères).");
            isValid = false;
        }

        if (p.getTypeProduit() == null || p.getTypeProduit().trim().isEmpty()) {
            System.err.println("Erreur : Le type du produit est obligatoire.");
            isValid = false;
        }

        if (p.getQuantiteDisponible() < 0) {
            System.err.println("Erreur : La quantité disponible ne peut pas être négative.");
            isValid = false;
        }

        if (p.getUniteProduit() == null || p.getUniteProduit().trim().isEmpty()) {
            System.err.println("Erreur : L'unité du produit est obligatoire.");
            isValid = false;
        }

        if (p.getPrixUnitaire() < 0) {
            System.err.println("Erreur : Le prix unitaire ne peut pas être négatif.");
            isValid = false;
        }

        return isValid;
    }


    @Override
    public void ajouterEntity(Produit produitVendu) {
        if (!isValidProduit(produitVendu)) {
            System.err.println("Échec de l'ajout : données invalides.");
            return;
        }
            try {
                // 1. Vérifier si le produit existe déjà par son nom
                String checkQuery = "SELECT idPr, quantite_disponible FROM produits WHERE nomPr = ?";
                PreparedStatement checkStmt = MyConnection.getInstance().getCnx().prepareStatement(checkQuery);
                checkStmt.setString(1, produitVendu.getNomProduit());
                ResultSet rs = checkStmt.executeQuery();

                if (rs.next()) {
                    // 2. Si le produit existe, mettre à jour la quantité
                    int existingId = rs.getInt("idPr");
                    float existingQuantity = rs.getFloat("quantite_disponible");
                    float newQuantity = existingQuantity + produitVendu.getQuantiteDisponible();

                    String updateQuery = "UPDATE produits SET quantite_disponible = ? WHERE idPr = ?";
                    PreparedStatement updateStmt = MyConnection.getInstance().getCnx().prepareStatement(updateQuery);
                    updateStmt.setFloat(1, newQuantity);
                    updateStmt.setInt(2, existingId);
                    updateStmt.executeUpdate();

                    System.out.println("Quantité mise à jour pour le produit existant (ID: " + existingId + ")");
                } else {
                    // 3. Si le produit n'existe pas, générer un ID et l'insérer
                    if (produitVendu.getIdProduit() == 0) {
                        int newId;
                        do {
                            newId = generateRandomId();
                        } while (idExists(newId));
                        produitVendu.setIdProduit(newId);
                    }

                    String insertQuery = "INSERT INTO produits (idPr, nomPr, typePr, quantite_disponible, unite, prix_unitaire, image) "
                            + "VALUES (?, ?, ?, ?, ?, ?, ?)";
                    PreparedStatement insertStmt = MyConnection.getInstance().getCnx().prepareStatement(insertQuery);
                    insertStmt.setInt(1, produitVendu.getIdProduit());
                    insertStmt.setString(2, produitVendu.getNomProduit());
                    insertStmt.setString(3, produitVendu.getTypeProduit());
                    insertStmt.setFloat(4, produitVendu.getQuantiteDisponible());
                    insertStmt.setString(5, produitVendu.getUniteProduit());
                    insertStmt.setFloat(6, produitVendu.getPrixUnitaire());
                    insertStmt.setString(7, produitVendu.getImage());
                    insertStmt.executeUpdate();

                    System.out.println("Nouveau produit ajouté avec ID: " + produitVendu.getIdProduit());
                }
            } catch (SQLException e) {
                System.err.println("Erreur SQL: " + e.getMessage());
            }

    }

    @Override
    public void supprimerEntity(Produit produitVendu) {
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





   public void updateEntity(int id, Produit produit) {
        try {
            String req = "UPDATE produits SET nomPr = ?, typePr = ?, quantite_disponible = ?, unite = ?, prix_unitaire = ?, image = ? WHERE idPr = ?";
            PreparedStatement ps = MyConnection.getInstance().getCnx().prepareStatement(req);
            ps.setString(1, produit.getNomProduit());
            ps.setString(2, produit.getTypeProduit());
            ps.setFloat(3, produit.getQuantiteDisponible());
            ps.setString(4, produit.getUniteProduit());
            ps.setFloat(5, produit.getPrixUnitaire());
            ps.setString(6, produit.getImage());
            ps.setInt(7, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur updateEntity: " + e.getMessage());
        }
    }






    @Override

    public List<Produit> getAllData() {
        List<Produit> data = new ArrayList<>();
        try {
            String requete = "SELECT  idPr, image ,nomPr,  quantite_disponible, unite FROM produits";
            Statement st = MyConnection.getInstance().getCnx().createStatement();
            ResultSet rs = st.executeQuery(requete);

            while (rs.next()){
                Produit produitVendu = new Produit();
                produitVendu.setIdProduit(rs.getInt("idPr"));
                produitVendu.setImage(rs.getString("image"));
                produitVendu.setNomProduit(rs.getString("nomPr"));
                produitVendu.setQuantiteDisponible(rs.getFloat("quantite_disponible"));
                produitVendu.setUniteProduit(rs.getString("unite"));

                data.add(produitVendu);
            }

        } catch (SQLException e) {
            System.out.println("Erreur chargement produits: " + e.getMessage());
        }
        return data;
    }



    public List<Produit> getAllDataVentes() {
        List<Produit> data = new ArrayList<>();
        try {
            String requete = "SELECT  idPr, image ,nomPr,  quantite_disponible, prix_unitaire FROM produits";
            Statement st = MyConnection.getInstance().getCnx().createStatement();
            ResultSet rs = st.executeQuery(requete);

            while (rs.next()){
                Produit produitVendu = new Produit();
                produitVendu.setIdProduit(rs.getInt("idPr"));
                produitVendu.setImage(rs.getString("image"));
                produitVendu.setNomProduit(rs.getString("nomPr"));
                produitVendu.setQuantiteDisponible(rs.getFloat("quantite_disponible"));
                produitVendu.setPrixUnitaire(rs.getFloat("prix_unitaire"));

                data.add(produitVendu);
            }

        } catch (SQLException e) {
            System.out.println("Erreur chargement produits: " + e.getMessage());
        }
        return data;
    }


    public List<Produit> getAllDataFruit() {
        List<Produit> data = new ArrayList<>();
        try {
            String requete = "SELECT idPr, image, nomPr, quantite_disponible, prix_unitaire " +
                    "FROM produits " +
                    "WHERE LOWER(typePr) = 'fruit' " +
                    "OR LOWER(typePr) = 'fruits'";
            Statement st = MyConnection.getInstance().getCnx().createStatement();
            ResultSet rs = st.executeQuery(requete);

            while (rs.next()){
                Produit produitVendu = new Produit();
                produitVendu.setIdProduit(rs.getInt("idPr"));
                produitVendu.setImage(rs.getString("image"));
                produitVendu.setNomProduit(rs.getString("nomPr"));
                produitVendu.setQuantiteDisponible(rs.getFloat("quantite_disponible"));
                produitVendu.setPrixUnitaire(rs.getFloat("prix_unitaire"));

                data.add(produitVendu);
            }

        } catch (SQLException e) {
            System.out.println("Erreur chargement produits: " + e.getMessage());
        }
        return data;
    }

    public List<Produit> getAllDataLeguime() {
        List<Produit> data = new ArrayList<>();
        try {
            String requete = "SELECT idPr, image, nomPr, quantite_disponible, prix_unitaire " +
                    "FROM produits " +
                    "WHERE LOWER(typePr) = 'légume' " +
                    "OR LOWER(typePr) = 'légumes'";
            Statement st = MyConnection.getInstance().getCnx().createStatement();
            ResultSet rs = st.executeQuery(requete);

            while (rs.next()){
                Produit produitVendu = new Produit();
                produitVendu.setIdProduit(rs.getInt("idPr"));
                produitVendu.setImage(rs.getString("image"));
                produitVendu.setNomProduit(rs.getString("nomPr"));
                produitVendu.setQuantiteDisponible(rs.getFloat("quantite_disponible"));
                produitVendu.setPrixUnitaire(rs.getFloat("prix_unitaire"));

                data.add(produitVendu);
            }

        } catch (SQLException e) {
            System.out.println("Erreur chargement produits: " + e.getMessage());
        }
        return data;
    }

    public List<Produit> getAllDataProduitBio() {
        List<Produit> data = new ArrayList<>();
        try {
            String requete = "SELECT idPr, image, nomPr, quantite_disponible, prix_unitaire " +
                    "FROM produits " +
                    "WHERE LOWER(typePr) = 'produit bio' " ;
            Statement st = MyConnection.getInstance().getCnx().createStatement();
            ResultSet rs = st.executeQuery(requete);

            while (rs.next()){
                Produit produitVendu = new Produit();
                produitVendu.setIdProduit(rs.getInt("idPr"));
                produitVendu.setImage(rs.getString("image"));
                produitVendu.setNomProduit(rs.getString("nomPr"));
                produitVendu.setQuantiteDisponible(rs.getFloat("quantite_disponible"));
                produitVendu.setPrixUnitaire(rs.getFloat("prix_unitaire"));

                data.add(produitVendu);
            }

        } catch (SQLException e) {
            System.out.println("Erreur chargement produits: " + e.getMessage());
        }
        return data;
    }

    public List<Produit> getAllDataViande() {
        List<Produit> data = new ArrayList<>();
        try {
            String requete = "SELECT idPr, image, nomPr, quantite_disponible, prix_unitaire " +
                    "FROM produits " +
                    "WHERE LOWER(typePr) = 'Viande' " ;
            Statement st = MyConnection.getInstance().getCnx().createStatement();
            ResultSet rs = st.executeQuery(requete);

            while (rs.next()){
                Produit produitVendu = new Produit();
                produitVendu.setIdProduit(rs.getInt("idPr"));
                produitVendu.setImage(rs.getString("image"));
                produitVendu.setNomProduit(rs.getString("nomPr"));
                produitVendu.setQuantiteDisponible(rs.getFloat("quantite_disponible"));
                produitVendu.setPrixUnitaire(rs.getFloat("prix_unitaire"));

                data.add(produitVendu);
            }

        } catch (SQLException e) {
            System.out.println("Erreur chargement produits: " + e.getMessage());
        }
        return data;
    }

    public List<Produit> getAllDataProduitlaitiere() {
        List<Produit> data = new ArrayList<>();
        try {
            String requete = "SELECT idPr, image, nomPr, quantite_disponible, prix_unitaire " +
                    "FROM produits " +
                    "WHERE LOWER(typePr) = 'laitière' " ;
            Statement st = MyConnection.getInstance().getCnx().createStatement();
            ResultSet rs = st.executeQuery(requete);

            while (rs.next()){
                Produit produitVendu = new Produit();
                produitVendu.setIdProduit(rs.getInt("idPr"));
                produitVendu.setImage(rs.getString("image"));
                produitVendu.setNomProduit(rs.getString("nomPr"));
                produitVendu.setQuantiteDisponible(rs.getFloat("quantite_disponible"));
                produitVendu.setPrixUnitaire(rs.getFloat("prix_unitaire"));

                data.add(produitVendu);
            }

        } catch (SQLException e) {
            System.out.println("Erreur chargement produits: " + e.getMessage());
        }
        return data;
    }






    public List<String> getDistinctTypes() {
        List<String> types = new ArrayList<>();
        String query = "SELECT DISTINCT typePr FROM produits";

        try {
            Connection cnx = MyConnection.getInstance().getCnx();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                types.add(rs.getString("typePr"));
            }

            // Ajouter "Autre" à la fin
            types.add("Autre");

        } catch (SQLException e) {
            System.out.println("Erreur getDistinctTypes: " + e.getMessage());
        }

        return types;
    }

    public List<String> getDistinctUnites() {
        List<String> unites = new ArrayList<>();
        String query = "SELECT DISTINCT unite FROM produits";
        try {
            Connection cnx = MyConnection.getInstance().getCnx();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                unites.add(rs.getString("unite"));
            }

            // Ajouter "Autre" à la fin
            unites.add("Autre");

        } catch (SQLException e) {
            System.out.println("Erreur getDistinctUnites: " + e.getMessage());
        }
        return unites;
    }



    public Produit recupererProduitId(int idProduit) {
        Produit produit = null;
        try {
            String requete = "SELECT idPr,image ,nomPr, typePr, quantite_disponible, unite, prix_unitaire FROM produits WHERE idPr = ?";
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setInt(1, idProduit);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                produit = new Produit();
                produit.setIdProduit(rs.getInt("idPr"));
                produit.setImage(rs.getString("image"));
                produit.setNomProduit(rs.getString("nomPr"));
                produit.setTypeProduit(rs.getString("typePr"));
                produit.setQuantiteDisponible(rs.getFloat("quantite_disponible"));
                produit.setUniteProduit(rs.getString("unite"));
                produit.setPrixUnitaire(rs.getFloat("prix_unitaire"));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du produit: " + e.getMessage());
        }
        return produit;
    }


    public int getIdProduitParNom(String nomProduit) {
        try {
            String query = "SELECT idPr FROM produits WHERE nomPr = ?";
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query);
            pst.setString(1, nomProduit);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt("idPr");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'ID produit : " + e.getMessage());
        }
        return -1;
    }



}
