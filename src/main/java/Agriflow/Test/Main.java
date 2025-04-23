package Agriflow.Test;

import Agriflow.Models.Animal;
import Agriflow.Models.Champ;
import Agriflow.Models.enums.AnimalHealthStatus;
import Agriflow.Models.enums.AnimalIdentificationType;
import Agriflow.Models.enums.AnimalSpecies;
import Agriflow.Service.ServiceAnimal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;


public class Main {

    public static void main(String[] args) {
        //  MyConnection mc = new MyConnection();
        Animal a = new Animal("1","COW","EAR_TAG","SICK","champ1");
        ServiceAnimal ps = new ServiceAnimal();
        //ps.ajouterEntity2(p);
    }
}