package Agriflow.Models;

import Agriflow.Models.enums.AnimalHealthStatus;
import Agriflow.Models.enums.AnimalIdentificationType;
import Agriflow.Models.enums.AnimalSpecies;

public class Animal {
    private int id;
    private AnimalSpecies espece;
    private AnimalIdentificationType identification;
    private AnimalHealthStatus etatSante;
    private Champ champ;

    public Animal(){

    }
    public Animal(String i, String cow, String earTag, String healthy, String champ1) {
        this.id = id;
        this.espece = AnimalSpecies.valueOf(String.valueOf(espece));
        this.identification = AnimalIdentificationType.valueOf(String.valueOf(identification));
        this.etatSante = AnimalHealthStatus.valueOf(String.valueOf(etatSante));
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEspece(String espece) {
        this.espece = AnimalSpecies.valueOf(espece);
    }

    public void setIdentification(String identification) {
        this.identification = AnimalIdentificationType.valueOf(identification);
    }

    public void setEtatSante(String etatSante) {
        this.etatSante = AnimalHealthStatus.valueOf(etatSante);
    }

    // Getters & Setters
    public int getId() {
        return id;
    }

    public AnimalSpecies getEspece() {
        return espece;
    }

    public AnimalIdentificationType getIdentification() {
        return identification;
    }

    public AnimalHealthStatus getEtatSante() {
        return etatSante;
    }

    public Champ getChamp() {
        return champ;
    }

    public void setChamp(Champ champ) { // Package-private
        this.champ = champ;
    }

    @Override
    public String toString() {
        return "Animal [id=" + id + ", " + espece + " (" + identification + ")]";
    }
}