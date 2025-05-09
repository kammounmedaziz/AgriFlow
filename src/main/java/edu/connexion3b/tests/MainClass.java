package edu.connexion3b.tests;

import edu.connexion3b.entities.Analyses;
import edu.connexion3b.entities.Capteurs;
import edu.connexion3b.services.Aservices;
import edu.connexion3b.services.Services;

public class MainClass {
    public static void main(String[] args) {
        //  MyConnection mc = new MyConnection();
        Analyses c = new Analyses("Bouchnek","Lotfi","moughani",4);
        Aservices ps = new Aservices();
        ps.ajouterEntity(c);
        //ps.ajouterEntity2(p);
        System.out.println(ps.getAllData());
    }
}
