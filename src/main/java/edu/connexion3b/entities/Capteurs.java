package edu.connexion3b.entities;

public class Capteurs {
    private int id;
    private String nom;
    private String types;
    private String emplacements;
    private String derniere_lecteures;
    public Capteurs(String nom,String type, String emplacement, String derniere_lecteures ) {
        this.nom=nom;
        this.types=type;
        this.emplacements=emplacement;
        this.derniere_lecteures=derniere_lecteures;}
    public Capteurs() {}

    public void setNom(String nom) {
        this.nom = nom;
    }
    public String getNom() {return nom;}

    public int getId() {
        return id;
    }
    public void setId(int id) { this.id = id; }
    public String getTypes() {
        return types;
    }
    public void setTypes(String types) {this.types = types;}
    public String getEmplacements() {return emplacements;}
    public void setEmplacements(String emplacements) {this.emplacements = emplacements;}
    public String getDerniere_lecteures() {return derniere_lecteures;}

    public void setDerniere_lecteurs(String derniere_lecteurs) {
        this.derniere_lecteures = derniere_lecteurs;
    }

    @Override
    public String toString() {
        return  "capteur{" +
                "nom=" + nom +
                "id=" + id +
                ", type='" + types + '\'' +
                ", emlacement='" + emplacements + '\'' +",dernier lecture='" + derniere_lecteures + '\'' +
                '}';
    }
}

