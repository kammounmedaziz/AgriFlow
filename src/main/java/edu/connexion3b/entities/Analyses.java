package edu.connexion3b.entities;
public class Analyses {
    int id;
    String valeur;
    String date ;
    String recommendation;
    int idcap;
    public Analyses(int id, String valeur, String date, String recommendation,int idcap) {this.id=id;
        this.valeur=valeur;
        this.date=date;
        this.recommendation=recommendation;
        this.idcap=idcap;
    }
    public Analyses(String valeur, String date, String recommendation,int idcap) {this.valeur=valeur;
    this.date=date;
    this.recommendation=recommendation;
    this.idcap=idcap;}
    public int getId() {
        return id;
    }

    public int getIdcap() {
        return idcap;
    }

    public void setIdcap(int idcap) {
        this.idcap = idcap;
    }

    public void setId(int id) {this.id=id;}
    public String getValeur() {
        return valeur;
    }
    public void setValeur(String valeur) {this.valeur = valeur;}
    public String getDate() {return date;}
    public void setDate(String date) {this.date = date;}
    public String getRecommendation() {return recommendation;}
    public void setRecommendation(String recommendation) {this.recommendation = recommendation;}

    @Override
    public String toString() {
        return "Analyses{" +
                "id=" + id +
                ", valeur='" + valeur + '\'' +
                ", date='" + date + '\'' +
                ", recommendation='" + recommendation + '\'' +
                '}';
    }
}
