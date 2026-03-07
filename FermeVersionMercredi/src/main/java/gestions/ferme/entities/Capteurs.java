package gestions.ferme.entities;

public class Capteurs {
    private int id;
    private String nom;
    private String types;
    private String emplacements;
    private String statut;
    public Capteurs(String nom,String type, String emplacement) {
        this.nom=nom;
        this.types=type;
        this.emplacements=emplacement;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut (String statut) {
        this.statut = statut;
    }

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


    @Override
    public String toString() {
        return  "capteur{" +
                "nom=" + nom +
                ", id=" + id +
                ", type='" + types + '\'' +
                ", emlacement='" + emplacements + '\'' + '\'' +
                '}';
    }
}

