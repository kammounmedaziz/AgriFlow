package gestions.ferme.entities;

public class Animal {
    private int id;
    private String Espece;
    String Race ;
    private String Sexe;
    private String dateNaissance;
    private String dateAchat;
    private int traitements;
    private String etat;





    public Animal() {}
    public Animal(int id) {
        this.id = id;
    }
    public Animal(int id, String espece, String race, String sexe, String dateNaissance, String dateAchat) {
        this.id = id;
        Espece = espece;
        Race = race;
        Sexe = sexe;
        this.dateNaissance = dateNaissance;
        this.dateAchat = dateAchat;
    }

    public Animal(String espece, String race, String sexe, String dateNaissance, String dateAchat) {
        Espece = espece;
        Race = race;
        Sexe = sexe;
        this.dateNaissance = dateNaissance;
        this.dateAchat = dateAchat;
    }

    public int getId() {
        return id;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getEspece() {
        return Espece;
    }

    public String getRace() {
        return Race;
    }

    public String getSexe() {
        return Sexe;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public int getTraitements() {return traitements;}



    public void setId(int id) {
        this.id = id;
    }

    public void setEspece(String espece) {
        Espece = espece;
    }

    public void setRace(String race) {
        Race = race;
    }

    public void setSexe(String sexe) {
        Sexe = sexe;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public void setTraitements(int traitements) {this.traitements = traitements;}



    @Override
    public String toString() {
        return "Animal{" +
                "id=" + id +
                ", Espece='" + Espece + '\'' +
                ", Race='" + Race + '\'' +
                ", Sexe='" + Sexe + '\'' +
                ", dateNaissance='" + dateNaissance + '\'' +
                ", dateAchat='" + dateAchat + '\'' +
                '}';
    }
}
