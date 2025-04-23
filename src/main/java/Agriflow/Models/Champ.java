package Agriflow.Models;

import Agriflow.Models.enums.FieldUseType;
import Agriflow.Models.enums.SoilType;

import java.util.ArrayList;
import java.util.List;

public class Champ {
    private int Champ_id;
    private String nom;
    private double superficie;
    private FieldUseType typeUtilisation;
    private SoilType typeSol;
    private List<Culture> cultures = new ArrayList<>();
    private List<Animal> animaux = new ArrayList<>();

    // Constructeur
    public Champ() {
        this.Champ_id = Champ_id;
        this.nom = nom;
        this.superficie = superficie;
        this.typeUtilisation = FieldUseType.valueOf(String.valueOf(typeUtilisation));
        this.typeSol = SoilType.valueOf(String.valueOf(typeSol));
    }

    // Getters & Setters
    public int getId() { return Champ_id; }
    public String getNom() { return nom; }
    public double getSuperficie() { return superficie; }
    public FieldUseType getTypeUtilisation() {
        return typeUtilisation;
    }
    public SoilType getTypeSol() { return typeSol; }
    public List<Culture> getCultures() { return cultures; }
    public List<Animal> getAnimaux() { return animaux; }

    public void setId(int id) {
        this.Champ_id = Champ_id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setSuperficie(double superficie) {
        this.superficie = superficie;
    }

    public void setTypeUtilisation(String typeUtilisation) {
        if (typeUtilisation != null) {
            this.typeUtilisation = FieldUseType.valueOf(typeUtilisation);
        }
    }

    public void setTypeSol(String typeSol) {
        if (typeSol != null) {
            this.typeSol = SoilType.valueOf(typeSol);
        }
    }

    public void setCultures(List<Culture> cultures) {
        this.cultures = cultures;
    }

    public void setAnimaux(List<Animal> animaux) {
        this.animaux = animaux;
    }

    public void ajouterCulture(Culture culture) {
        cultures.add(culture);
        culture.setChamp(this);
    }

    public void ajouterAnimal(Animal animal) {
        animaux.add(animal);
        animal.setChamp(this);
    }

    @Override
    public String toString() {
        return "Champ [id=" + Champ_id + ", nom=" + nom + ", superficie=" + superficie + " ha]";
    }
}