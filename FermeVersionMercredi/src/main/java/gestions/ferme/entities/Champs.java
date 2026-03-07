package gestions.ferme.entities;

public class Champs {
    private int id;
    private String nom;
    private  float superficie;
    private String type_utilisation;
    private String type_sol;
    private String 	type_plante;
    private String date_plantation;

    private String 	date_recolte;
    private int nombre_traitement_traitement;
    private float 	quantite_recolte;

    private String etat;

    private String stade_croissance;



    public Champs(int id){
        this.id = id;
    }

    public Champs() {}

    public Champs(String nom, float superficie, String type_utilisation, String type_sol, String type_plante, String date_plantation, String date_recolte, int nombre_traitement_traitement, float quantite_recolte) {
        this.nom = nom;
        this.superficie = superficie;
        this.type_utilisation = type_utilisation;
        this.type_sol = type_sol;
        this.type_plante = type_plante;
        this.date_plantation = date_plantation;

        this.date_recolte = date_recolte;
        this.nombre_traitement_traitement = nombre_traitement_traitement;
        this.quantite_recolte = quantite_recolte;
    }

    public Champs(int id, String nom, float superficie, String type_utilisation, String type_sol, String type_plante, String date_plantation, String date_recolte, int nombre_traitement_traitement, float quantite_recolte) {
        this.id = id;
        this.nom = nom;
        this.superficie = superficie;
        this.type_utilisation = type_utilisation;
        this.type_sol = type_sol;
        this.type_plante = type_plante;
        this.date_plantation = date_plantation;
        this.date_recolte = date_recolte;
        this.nombre_traitement_traitement = nombre_traitement_traitement;
        this.quantite_recolte = quantite_recolte;
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public float getSuperficie() {
        return superficie;
    }

    public String getType_utilisation() {
        return type_utilisation;
    }

    public String getType_sol() {
        return type_sol;
    }

    public String getType_plante() {
        return type_plante;
    }

    public String getDate_plantation() {
        return date_plantation;
    }



    public String getDate_recolte() {
        return date_recolte;
    }

    public int getNombre_traitement_traitement() {
        return nombre_traitement_traitement;
    }

    public float getQuantite_recolte() {
        return quantite_recolte;
    }

    public String getEtat() {
        return etat;
    }

    public String getStade_croissance() {
        return stade_croissance;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setSuperficie(float superficie) {
        this.superficie = superficie;
    }

    public void setType_utilisation(String type_utilisation) {
        this.type_utilisation = type_utilisation;
    }

    public void setType_sol(String type_sol) {
        this.type_sol = type_sol;
    }

    public void setType_plante(String type_plante) {
        this.type_plante = type_plante;
    }

    public void setDate_plantation(String date_plantation) {
        this.date_plantation = date_plantation;
    }



    public void setDate_recolte(String date_recolte) {
        this.date_recolte = date_recolte;
    }

    public void setN_traitement(int date_traitement) {
        this.nombre_traitement_traitement = date_traitement;
    }

    public void setQuantite_recolte(float quantite_recolte) {
        this.quantite_recolte = quantite_recolte;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public void setStade_croissance(String stade_croissance) {
        this.stade_croissance = stade_croissance;
    }

    @Override
    public String toString() {
        return "Champs{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", superficie=" + superficie +
                ", type_utilisation='" + type_utilisation + '\'' +
                ", type_sol='" + type_sol + '\'' +
                ", type_plante='" + type_plante + '\'' +
                ", date_plantation='" + date_plantation + '\'' +
                ", date_recolte='" + date_recolte + '\'' +
                ", nombre_traitement='" + nombre_traitement_traitement + '\'' +
                ", quantite_recolte=" + quantite_recolte +
                '}';
    }
}
