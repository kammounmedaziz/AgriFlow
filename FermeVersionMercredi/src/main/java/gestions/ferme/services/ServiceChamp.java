package gestions.ferme.services;



import gestions.ferme.entities.Champs;
import gestions.ferme.interfaces.IService;
import gestions.ferme.tools.MyConnection;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ServiceChamp implements IService<Champs> {

    // Méthode pour générer un ID aléatoire entre 1000 et 9999
    private int generateRandomId() {
        return ThreadLocalRandom.current().nextInt(1000, 10000); // 1000 (inclus) à 9999 (inclus)
    }

    // Méthode pour vérifier si un ID existe déjà
    private boolean idExists(int id) {
        try {
            String query = "SELECT COUNT(*) FROM champs WHERE id = ?";
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

    private boolean nomChampExiste(String nom) {
        String query = "SELECT COUNT(*) FROM champs WHERE nom = ?";
        try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query)) {
            pst.setString(1, nom);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification du nom : " + e.getMessage());
        }
        return false;
    }

    @Override
    public void ajouterEntity(Champs champs) {
        try {
            // === Contrôles de saisie ===
            if (champs.getNom() == null || champs.getNom().trim().isEmpty()) {
                System.out.println("Erreur : Le nom ne peut pas être vide.");
                return;
            }

            if (champs.getSuperficie() <= 0) {
                System.out.println("Erreur : La superficie doit être un nombre positif.");
                return;
            }

            if (champs.getType_utilisation() == null || champs.getType_utilisation().trim().isEmpty()) {
                System.out.println("Erreur : Le type d'utilisation est obligatoire.");
                return;
            }

            if (champs.getType_sol() == null || champs.getType_sol().trim().isEmpty()) {
                System.out.println("Erreur : Le type de sol est obligatoire.");
                return;
            }

            if (champs.getType_plante() == null || champs.getType_plante().trim().isEmpty()) {
                System.out.println("Erreur : Le type de plante est obligatoire.");
                return;
            }

            if (nomChampExiste(champs.getNom())) {
                System.out.println("Erreur : Un champ avec ce nom existe déjà.");
                return;
            }

            // === Génération d'ID ===
            int id;
            do {
                id = generateRandomId();
            } while (idExists(id));
            champs.setId(id);

            // Requête d'insertion
            String requete = "INSERT INTO champs(id, nom, superficie, type_utilisation, type_sol, type_plante )" +
                    "VALUES (?,?,?,?,?,?)";

            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setInt(1, champs.getId());
            pst.setString(2, champs.getNom());
            pst.setFloat(3, champs.getSuperficie());
            pst.setString(4, champs.getType_utilisation());
            pst.setString(5, champs.getType_sol());
            pst.setString(6, champs.getType_plante());




            pst.executeUpdate();
            System.out.println("Champ ajouté avec succès avec l'ID: " + champs.getId());

        } catch (SQLException e) {
            System.out.println("Erreur SQL : " + e.getMessage());
        }
    }



    private boolean traitementIdExists(int id) {
        try {
            String query = "SELECT COUNT(*) FROM traitementchamps WHERE id = ?";
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.err.println("Erreur vérification ID traitement: " + e.getMessage());
            return true;
        }
    }
    public void AffecterTraitementChamps(int idChamp, String stadeCroissance, float quantiteMedic, String typeMedi) {
        try {
            Connection cnx = MyConnection.getInstance().getCnx();

            // 1. Vérifier si le champ existe et récupérer son nom
            String checkQuery = "SELECT nom FROM champs WHERE id = ?";
            PreparedStatement checkStmt = cnx.prepareStatement(checkQuery);
            checkStmt.setInt(1, idChamp);
            ResultSet rsCheck = checkStmt.executeQuery();

            String nomChamp = null;
            if (rsCheck.next()) {
                nomChamp = rsCheck.getString("nom");
            } else {
                System.out.println("❌ Erreur : Le champ avec l'ID " + idChamp + " n'existe pas.");
                return;
            }

            System.out.println("✅ Champ trouvé : " + nomChamp);

            // 2. Générer un ID unique
            int id;
            do {
                id = generateRandomId();
            } while (traitementIdExists(id)); // à implémenter pour éviter doublon

            // 3. Insérer dans traitementchamps
            String insertQuery = "INSERT INTO traitementchamps (id, idChamp, stade_croissance, quantite_Medic, typeMedi) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement insertStmt = cnx.prepareStatement(insertQuery);
            insertStmt.setInt(1, id);
            insertStmt.setInt(2, idChamp);
            insertStmt.setString(3, stadeCroissance);
            insertStmt.setFloat(4, quantiteMedic);
            insertStmt.setString(5, typeMedi);
            insertStmt.executeUpdate();

            // 4. Mettre à jour le nombre de traitements
            String updateQuery = "UPDATE champs SET nbre_traitement = nbre_traitement + 1 WHERE id = ?";
            PreparedStatement updateStmt = cnx.prepareStatement(updateQuery);
            updateStmt.setInt(1, idChamp);
            updateStmt.executeUpdate();

            // 5. Mettre à jour l'état du champ
            String etat = "champ en cours de traitement";
            String updateEtatQuery = "UPDATE champs SET etat = ? WHERE id = ?";
            PreparedStatement updateEtatStmt = cnx.prepareStatement(updateEtatQuery);
            updateEtatStmt.setString(1, etat);
            updateEtatStmt.setInt(2, idChamp);
            updateEtatStmt.executeUpdate();

            System.out.println("🎯 Traitement ajouté avec succès pour le champ : " + nomChamp + " (ID traitement : " + id + ").");

        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de l'affectation du traitement au champ : " + e.getMessage());
        }
    }


    @Override
    public void supprimerEntity(Champs champs) {
        try {
            String requete = "DELETE FROM champs WHERE id = ? ";

            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete);
            pst.setInt(1, champs.getId());
            int rowsDeleted = pst.executeUpdate();
            System.out.println(" champ supprimée avec succès.");

        } catch (SQLException e) {
            System.err.println(" Erreur lors de la suppression : " + e.getMessage());
        }

    }

    @Override
    public void updateEntity(int idt, Champs champs) {
        String requete = "UPDATE champs SET date_plantation = ?, date_recolte = ?,  etat = ? WHERE id = ?";

        try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(requete)) {
            pst.setString(1, champs.getDate_plantation()); // null autorisé si champ vide
            pst.setString(2, champs.getDate_recolte());
            pst.setString(3, champs.getEtat());
            pst.setInt(4, idt);

            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Champ mis à jour avec succès.");
            } else {
                System.out.println("Aucun champ trouvé avec l'ID fourni.");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du champ : " + e.getMessage());
        }


    }

    @Override
    public List<Champs> getAllData() {

            List<Champs> data = new ArrayList<>();
            try {
                String requete = "SELECT * FROM champs  ";
                Statement st = MyConnection.getInstance().getCnx().createStatement();
                ResultSet rs = st.executeQuery(requete);

                while (rs.next()) {
                    Champs champs = new Champs();
                    champs.setId(rs.getInt("id"));
                    champs.setNom(rs.getString("nom"));
                    champs.setSuperficie(rs.getFloat("superficie"));
                    champs.setType_utilisation(rs.getString("type_utilisation"));
                    champs.setType_sol(rs.getString("type_sol"));
                    champs.setType_plante(rs.getString("type_plante"));
                    champs.setEtat(rs.getString("etat"));

                    // Gestion des dates qui pourraient être NULL
                    java.sql.Date plantationDate = rs.getDate("date_plantation");
                    champs.setDate_plantation(plantationDate != null ? plantationDate.toString() : null);

                    java.sql.Date recolteDate = rs.getDate("date_recolte");
                    champs.setDate_recolte(recolteDate != null ? recolteDate.toString() : null);

                    // Gestion des valeurs numériques NULL
                    champs.setN_traitement(rs.getObject("nbre_traitement", Integer.class) != null ?
                            rs.getInt("nbre_traitement") : 0);



                    data.add(champs);
                }
            } catch (SQLException e) {
                System.err.println("Erreur lors de la récupération des champs: " + e.getMessage());
                // Vous pourriez aussi logger cette erreur ou la propager
            }
            return data;

    }

    public List<Champs> getAllDataTraitement0() {

        List<Champs> data = new ArrayList<>();
        try {
            String requete = "SELECT id, type_plante, date_plantation, etat FROM champs where etat = 'Champ Malade' ";
            Statement st = MyConnection.getInstance().getCnx().createStatement();
            ResultSet rs = st.executeQuery(requete);

            while (rs.next()) {
                Champs champs = new Champs();
                champs.setId(rs.getInt("id"));
                champs.setType_plante(rs.getString("type_plante"));
                // Gestion des dates qui pourraient être NULL
                java.sql.Date plantationDate = rs.getDate("date_plantation");
                champs.setDate_plantation(plantationDate != null ? plantationDate.toString() : null);
                champs.setEtat(rs.getString("etat"));
                data.add(champs);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des champs: " + e.getMessage());
            // Vous pourriez aussi logger cette erreur ou la propager
        }
        return data;

    }


    public List<Champs> getAllDataTraitement1() {


            List<Champs> data = new ArrayList<>();

            String requete = "SELECT id, type_plante, date_plantation, etat FROM champs WHERE etat = 'champ en cours de traitement'";

            try (
                    Statement st = MyConnection.getInstance().getCnx().createStatement();
                    ResultSet rs = st.executeQuery(requete)
            ) {
                while (rs.next()) {
                    Champs champs = new Champs();
                    champs.setId(rs.getInt("id"));
                    champs.setType_plante(rs.getString("type_plante"));

                    java.sql.Date plantationDate = rs.getDate("date_plantation");
                    champs.setDate_plantation(plantationDate != null ? plantationDate.toString() : null);

                    champs.setEtat(rs.getString("etat"));

                    data.add(champs);
                }
            } catch (SQLException e) {
                System.err.println("Erreur lors de la récupération des champs: " + e.getMessage());

            }

            return data;


    }


    public Champs getChampById(int id) {
        Champs champ = null;
        try {
            String query = "SELECT nom, etat FROM champs WHERE id = ?";
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                champ = new Champs();
                champ.setNom(rs.getString("nom"));
                champ.setEtat(rs.getString("etat"));
            }
        } catch (SQLException e) {
            System.err.println("Erreur getChampById : " + e.getMessage());
        }
        return champ;
    }





































































































































































































































    public List<String> getAllStadeCroissance() {
        List<String> stades = new ArrayList<>();
        try {
            String requete = "SELECT DISTINCT stade_croissance FROM champs WHERE stade_croissance IS NOT NULL";
            Statement st = MyConnection.getInstance().getCnx().createStatement();
            ResultSet rs = st.executeQuery(requete);
            while (rs.next()) {
                stades.add(rs.getString("stade_croissance"));
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des stades de croissance : " + e.getMessage());
        }
        return stades;
    }


    public List<String> getAllTypeUtilisation() {
        List<String> types = new ArrayList<>();
        String query = "SELECT DISTINCT type_plante FROM champs";

        try {
            Connection cnx = MyConnection.getInstance().getCnx();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                types.add(rs.getString("type_plante"));
            }

            types.add("Autre"); // Option facultative
        } catch (SQLException e) {
            System.out.println("Erreur getAllTypesPlante: " + e.getMessage());
        }

        return types;
    }

    public List<String> getAllTypeSol() {
        List<String> types = new ArrayList<>();
        String query = "SELECT DISTINCT type_sol FROM champs";

        try {
            Connection cnx = MyConnection.getInstance().getCnx();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                types.add(rs.getString("type_sol"));
            }

            types.add("Autre");
        } catch (SQLException e) {
            System.out.println("Erreur getAllTypesSol: " + e.getMessage());
        }

        return types;
    }
    public List<String> getAllTypePlante() {
        List<String> types = new ArrayList<>();
        String query = "SELECT DISTINCT type_plante FROM champs";

        try {
            Connection cnx = MyConnection.getInstance().getCnx();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                types.add(rs.getString("type_plante"));
            }

            types.add("Autre"); // Option facultative
        } catch (SQLException e) {
            System.out.println("Erreur getAllTypesPlante: " + e.getMessage());
        }

        return types;


}}
