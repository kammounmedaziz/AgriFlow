package gestions.ferme.services;


import gestions.ferme.entities.Animal;
import gestions.ferme.interfaces.IService;
import gestions.ferme.tools.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class ServiceAnimal implements IService<Animal> {


    private int generateRandomId() {
        return ThreadLocalRandom.current().nextInt(1000, 10000);
    }

    private boolean idExists(int id) {
        try {
            String query = "SELECT COUNT(*) FROM animeanx WHERE id = ?";
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.out.println("Erreur lors de la vérification d'ID : " + e.getMessage());
        }
        return false;
    }

    @Override
    public void ajouterEntity(Animal animal) {
        try {
            // === Contrôle de saisie ===

            if (animal.getEspece() == null || animal.getEspece().trim().isEmpty()) {
                System.out.println("Erreur : L'espèce est obligatoire.");
                return;
            }

            if (animal.getRace() == null || animal.getRace().trim().isEmpty()) {
                System.out.println("Erreur : La race est obligatoire.");
                return;
            }


            // === Génération d'ID unique ===
            int id;
            do {
                id = generateRandomId();
            } while (idExists(id));
            animal.setId(id);

            // === Insertion dans la base ===
            String sql = "INSERT INTO animeanx (id, espece, race, sex, date_naissance) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(sql);
            pst.setInt(1, animal.getId());
            pst.setString(2, animal.getEspece());
            pst.setString(3, animal.getRace());
            pst.setString(4, animal.getSexe());
            pst.setString(5, animal.getDateNaissance());


            pst.executeUpdate();
            System.out.println("Animal ajouté avec succès (ID: " + animal.getId() + ").");

        } catch (SQLException e) {
            System.out.println("Erreur SQL : " + e.getMessage());
        }


    }

    private boolean traitementAnimalIdExists(int id) {
        try {
            String query = "SELECT COUNT(*) FROM traitementanimal WHERE id = ?";
            PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification de l'ID traitementanimal : " + e.getMessage());
        }
        return false;
    }


    public void AffecterTraitementAnimal(int idAnimal, String typeTraitement, String typeMedi, String anomalie, String etat) {
        try {
            Connection cnx = MyConnection.getInstance().getCnx();

            // Étape 1 : Vérifier si l'animal existe
            String checkAnimalQuery = "SELECT COUNT(*) FROM animeanx WHERE id = ?";
            PreparedStatement checkStmt = cnx.prepareStatement(checkAnimalQuery);
            checkStmt.setInt(1, idAnimal);
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            int count = rs.getInt(1);

            if (count == 0) {
                System.err.println("Erreur : Aucun animal trouvé avec l'ID " + idAnimal);
                return; // arrêter la méthode si l'animal n'existe pas
            }

            // Étape 2 : Générer un ID unique pour le traitement
            int idTraitement;
            do {
                idTraitement = ThreadLocalRandom.current().nextInt(1000, 10000);
            } while (traitementAnimalIdExists(idTraitement));

            // Étape 3 : Insérer le traitement
            String insertQuery = "INSERT INTO traitementanimal (id, idAnimal, typetaitement, typeMedi, anomalie) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement insertStmt = cnx.prepareStatement(insertQuery);
            insertStmt.setInt(1, idTraitement);
            insertStmt.setInt(2, idAnimal);
            insertStmt.setString(3, typeTraitement);
            insertStmt.setString(4, typeMedi);
            insertStmt.setString(5, anomalie);
            insertStmt.executeUpdate();

            // Étape 4 : Incrémenter le nombre de traitements dans animeaux
            String updateQuery = "UPDATE animeanx SET nbre_traitement = nbre_traitement + 1, etat = ? WHERE id = ?";
            PreparedStatement updateStmt = cnx.prepareStatement(updateQuery);
            updateStmt.setString(1, etat);
            updateStmt.setInt(2, idAnimal);
            updateStmt.executeUpdate();

            System.out.println("Traitement ajouté avec succès pour l'animal ID " + idAnimal + " et état changé en: " + etat);

        } catch (SQLException e) {
            System.err.println("Erreur lors de l'affectation du traitement : " + e.getMessage());
        }
    }
    public void supprimerEntity(int id) {
        String query = "DELETE FROM animeanx WHERE id = ?";
        try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query)) {
            pst.setInt(1, id);
            pst.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de l'animal: " + e.getMessage());
        }
    }


    @Override
    public void supprimerEntity(Animal animal) {

        try {
            Connection cnx = MyConnection.getInstance().getCnx();

            // Vérifier si l'animal existe
            String checkQuery = "SELECT COUNT(*) FROM animeanx WHERE id = ?";
            PreparedStatement checkStmt = cnx.prepareStatement(checkQuery);
            checkStmt.setInt(1, animal.getId());
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            if (rs.getInt(1) == 0) {
                System.err.println("Erreur : L'animal avec l'ID " + animal.getId() + " n'existe pas.");
                return;
            }

            // Supprimer l'animal
            String deleteQuery = "DELETE FROM animeanx WHERE id = ?";
            PreparedStatement deleteStmt = cnx.prepareStatement(deleteQuery);
            deleteStmt.setInt(1, animal.getId());
            deleteStmt.executeUpdate();

            System.out.println("Animal supprimé avec succès. ID : " + animal.getId());

        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de l'animal : " + e.getMessage());
        }
    }


    @Override
    public void updateEntity(int idt, Animal animal) {
        try {
            Connection cnx = MyConnection.getInstance().getCnx();

            // Vérifier si l'animal existe
            String checkQuery = "SELECT COUNT(*) FROM animeanx WHERE id = ?";
            PreparedStatement checkStmt = cnx.prepareStatement(checkQuery);
            checkStmt.setInt(1, idt);
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            if (rs.getInt(1) == 0) {
                System.err.println("Erreur : L'animal avec l'ID " + idt + " n'existe pas.");
                return;
            }

            // Mise à jour : nom et race seulement
            String updateQuery = "UPDATE animeaux SET espece = ?, race = ? WHERE id = ?";
            PreparedStatement updateStmt = cnx.prepareStatement(updateQuery);
            updateStmt.setString(1, animal.getEspece());
            updateStmt.setString(2, animal.getRace());
            updateStmt.setInt(3, idt);

            int rows = updateStmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Animal mis à jour avec succès (ID " + idt + ")");
            } else {
                System.out.println("Aucune mise à jour effectuée.");
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de l'animal : " + e.getMessage());
        }
    }


    public List<Animal> getAllData() {
        List<Animal> animals = new ArrayList<>();

        try {
            Connection cnx = MyConnection.getInstance().getCnx();
            String query = "SELECT id, espece, race, sex, date_naissance, nbre_traitement FROM animeanx";
            PreparedStatement stmt = cnx.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Animal animal = new Animal();
                animal.setId(rs.getInt("id"));
                animal.setEspece(rs.getString("espece"));
                animal.setRace(rs.getString("race"));
                animal.setSexe(rs.getString("sex"));
                animal.setDateNaissance(rs.getString("date_naissance")); // CORRECTION
                animal.setTraitements(rs.getInt("nbre_traitement")); // CORRECTION

                animals.add(animal);
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des animaux : " + e.getMessage());
        }

        return animals;
    }

    public List<Animal> getAllData0() {
        List<Animal> animals = new ArrayList<>();

        try {
            Connection cnx = MyConnection.getInstance().getCnx();
            String query = "SELECT id,   race  ,date_naissance,  etat FROM animeanx where etat = 'animal Malad'";
            PreparedStatement stmt = cnx.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Animal animal = new Animal();
                animal.setId(rs.getInt("id"));
                animal.setRace(rs.getString("race"));
                animal.setDateNaissance(rs.getString("date_naissance")); // CORRECTION
                animal.setEtat(rs.getString("etat")); // CORRECTION

                animals.add(animal);
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des animaux : " + e.getMessage());
        }

        return animals;
    }

    public List<Animal> getAllData1() {
        List<Animal> animals = new ArrayList<>();

        try {
            Connection cnx = MyConnection.getInstance().getCnx();
            
            // Afficher la requête pour le débogage
            String query = "SELECT id, race, date_naissance, etat FROM animeanx WHERE etat = 'en cours'";
            System.out.println("Exécution de la requête: " + query);
            
            // Vérifier s'il y a des animaux avec cet état dans la base de données
            String countQuery = "SELECT COUNT(*) FROM animeanx WHERE etat = 'en cours'";
            Statement countStmt = cnx.createStatement();
            ResultSet countRs = countStmt.executeQuery(countQuery);
            if (countRs.next()) {
                int count = countRs.getInt(1);
                System.out.println("Nombre d'animaux en cours de traitement dans la base de données: " + count);
            }
            
            // Exécuter la requête principale
            PreparedStatement stmt = cnx.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Animal animal = new Animal();
                animal.setId(rs.getInt("id"));
                animal.setRace(rs.getString("race"));
                animal.setDateNaissance(rs.getString("date_naissance"));
                animal.setEtat(rs.getString("etat"));

                animals.add(animal);
            }
            
            System.out.println("Nombre d'animaux récupérés par getAllData1(): " + animals.size());

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des animaux en traitement: " + e.getMessage());
            e.printStackTrace();
        }

        return animals;
    }

    public List<String> getAllEspeces() {
        List<String> especes = new ArrayList<>();
        String query = "SELECT DISTINCT espece FROM animeanx";

        try {
            Connection cnx = MyConnection.getInstance().getCnx();
            Statement st = cnx.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                especes.add(rs.getString("espece"));
            }

            // Ajouter l'option "Autre" à la fin
            especes.add("Autre");

        } catch (SQLException e) {
            System.out.println("Erreur getAllEspeces: " + e.getMessage());
        }

        return especes;
    }



    public Animal recupererAnimalParId(int id) {
        Animal animal = null;
        
        try {
            Connection cnx = MyConnection.getInstance().getCnx();
            String query = "SELECT * FROM animeanx WHERE id = ?";
            PreparedStatement stmt = cnx.prepareStatement(query);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                animal = new Animal();
                animal.setId(rs.getInt("id"));
                animal.setEspece(rs.getString("espece"));
                animal.setRace(rs.getString("race"));
                animal.setSexe(rs.getString("sex"));
                animal.setDateNaissance(rs.getString("date_naissance"));
                animal.setEtat(rs.getString("etat")); // Définir l'état une seule fois
                animal.setTraitements(rs.getInt("nbre_traitement"));
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'animal par ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return animal;
    }


    public void updateEntity(Animal animal) {
        String query = "UPDATE animeanx SET espece = ?, race = ?, sex = ?, date_naissance = ?, etat = ? WHERE id = ?";
        try (PreparedStatement pst = MyConnection.getInstance().getCnx().prepareStatement(query)) {
            pst.setString(1, animal.getEspece());
            pst.setString(2, animal.getRace());
            pst.setString(3, animal.getSexe());
            pst.setString(4, animal.getDateNaissance());
            pst.setString(5, animal.getEtat());
            pst.setInt(6, animal.getId());

            pst.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur updateEntity (Animal): " + e.getMessage());
            throw new RuntimeException("Erreur lors de la mise à jour de l'animal");
        }
    }

    // Ajoutons une méthode pour insérer un animal en cours de traitement pour tester
    public void ajouterAnimalEnTraitement() {
        try {
            Connection cnx = MyConnection.getInstance().getCnx();
            
            // Vérifier s'il existe déjà un animal en cours de traitement
            String checkQuery = "SELECT COUNT(*) FROM animeanx WHERE etat = 'animal en cours de traitement'";
            Statement checkStmt = cnx.createStatement();
            ResultSet checkRs = checkStmt.executeQuery(checkQuery);
            checkRs.next();
            int count = checkRs.getInt(1);
            
            if (count > 0) {
                System.out.println("Il existe déjà " + count + " animal(aux) en cours de traitement.");
                return;
            }
            
            // Générer un ID unique
            int id;
            do {
                id = ThreadLocalRandom.current().nextInt(1000, 10000);
            } while (idExists(id));
            
            // Insérer un animal en cours de traitement pour tester
            String insertQuery = "INSERT INTO animeanx (id, espece, race, sex, date_naissance, etat) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement insertStmt = cnx.prepareStatement(insertQuery);
            insertStmt.setInt(1, id);
            insertStmt.setString(2, "Test");
            insertStmt.setString(3, "Test Race");
            insertStmt.setString(4, "Male");
            insertStmt.setString(5, "2023-01-01");
            insertStmt.setString(6, "animal en cours de traitement");
            
            int rowsInserted = insertStmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Animal en cours de traitement ajouté avec succès pour test (ID: " + id + ")");
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout d'un animal en cours de traitement: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Ajoutons une méthode pour changer l'état d'un animal existant
    public void changerEtatAnimal(int idAnimal, String nouvelEtat) {
        try {
            Connection cnx = MyConnection.getInstance().getCnx();
            
            // Vérifier si l'animal existe
            String checkQuery = "SELECT COUNT(*) FROM animeanx WHERE id = ?";
            PreparedStatement checkStmt = cnx.prepareStatement(checkQuery);
            checkStmt.setInt(1, idAnimal);
            ResultSet checkRs = checkStmt.executeQuery();
            checkRs.next();
            int count = checkRs.getInt(1);
            
            if (count == 0) {
                System.err.println("Aucun animal trouvé avec l'ID " + idAnimal);
                return;
            }
            
            // Mettre à jour l'état de l'animal
            String updateQuery = "UPDATE animeanx SET etat = ? WHERE id = ?";
            PreparedStatement updateStmt = cnx.prepareStatement(updateQuery);
            updateStmt.setString(1, nouvelEtat);
            updateStmt.setInt(2, idAnimal);
            
            int rowsUpdated = updateStmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("État de l'animal " + idAnimal + " changé en: " + nouvelEtat);
            } else {
                System.err.println("Échec de la mise à jour de l'état de l'animal " + idAnimal);
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors du changement d'état de l'animal: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
