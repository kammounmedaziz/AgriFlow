package Agriflow.Models;

import Agriflow.Models.enums.CropType;
import Agriflow.Models.enums.GrowthStage;
import Agriflow.Models.enums.IrrigationType;

import java.time.LocalDate;

public class Culture {
    private int Culture_id;
    private CropType typePlante;
    private LocalDate datePlantation;
    private GrowthStage stadeCroissance;
    private Champ champ;
    private IrrigationType typeirrigation;

    public Culture(){

    }


    public Culture(String typePlante, LocalDate datePlantation, String stadeCroissance, String typeirrigation) {
        this.typePlante = CropType.valueOf(typePlante);
        this.datePlantation = datePlantation;
        this.stadeCroissance = GrowthStage.valueOf(stadeCroissance);
        this.typeirrigation = IrrigationType.valueOf(typeirrigation);
    }

    // Getters & Setters
    public int getId() { return Culture_id; }
    public CropType getTypePlante() { return typePlante; }
    public LocalDate getDatePlantation() { return datePlantation; }
    public GrowthStage getStadeCroissance() { return stadeCroissance; }
    public Champ getChamp() { return champ; }
    public IrrigationType getTypeirrigation() {
        return typeirrigation;
    }

    public void setChamp(Champ champ) { // Package-private
        this.champ = champ;
    }

    @Override
    public String toString() {
        return "Culture [id=" + Culture_id + ", " + typePlante + " planté le " + datePlantation + "]";
    }

    public void setId(int id) {
        this.Culture_id = Culture_id;
    }

    public void setTypePlante(String typePlante) {
        this.typePlante = CropType.valueOf(typePlante);
    }

    public void setDatePlantation(LocalDate datePlantation) {
        this.datePlantation = datePlantation;
    }

    public void setStadeCroissance(String stadeCroissance) {
        this.stadeCroissance = GrowthStage.valueOf(stadeCroissance);
    }
    public void setTypeirrigation(String typeirrigation) {
        this.typeirrigation = IrrigationType.valueOf(typeirrigation);
    }
}