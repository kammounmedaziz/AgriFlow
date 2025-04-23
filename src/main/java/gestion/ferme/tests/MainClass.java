package gestion.ferme.tests;

import gestion.ferme.entities.ProduitVendu;
import gestion.ferme.entities.Utilisateur;
import gestion.ferme.entities.Vente;
import gestion.ferme.services.ProduitVenduService;
import gestion.ferme.services.UtilisateurService;
import gestion.ferme.services.VenteService;

public class MainClass {
    public static void main(String[] args) {
       ProduitVendu produitVendu1= new ProduitVendu(1236,"Laitiers ","culture", 12.9F,"Litre (L)",2);
        ProduitVendu produitVendu2= new ProduitVendu(1237,"Laitiers ","culture", 40F,"Litre (L)",2);
        ProduitVendu produitVendu3 = new ProduitVendu(100,3);
        ProduitVenduService pvs = new ProduitVenduService();
       //pvs.ajouterEntity(produitVendu1);
        //pvs.ajouterEntity(produitVendu2);


        //ProduitVendu pv = new ProduitVendu(1234);
        //pvs.supprimerEntity(pv);

        //pvs.updateEntity(1237,produitVendu3);

        //System.out.println(pvs.getAllData());

        //les Ventes
        VenteService vs = new VenteService();
        Vente v1= new Vente(215,1236, 1.8F, 5.400F,"14400053");
        Vente v2= new Vente(110,1236, 1.8F, 5.400F,"14400053");
        Vente v4 = new Vente(100, 2.9F, 6.400F);
        vs.ajouterEntity(v2);
        //Vente v3= new Vente(100);
        //vs.supprimerEntity(v3);
        //vs.updateEntity(100,v4);


        //les utilisateurs
       // Utilisateur u= new Utilisateur("14400053","Jlassi","Nour","Femme","Admin","27156234","nourjlassi123");
        //Utilisateur u1= new Utilisateur("15400053","Boukar","Baraa","Femme","veterinaire","27156234","nourjlassi123");
       // UtilisateurService us = new UtilisateurService();
        //us.ajouterEntity(u);
        //us.ajouterEntity(u1);


        //Utilisateur u3 = new Utilisateur("14400053");
        //us.supprimerEntity(u3);


        //Utilisateur u4 = new Utilisateur("14400053","99990611","jlassiYoussef123");
        //us.updateEntity2("14400053",u4);


        //System.out.println(us.getAllData());




    }
}
