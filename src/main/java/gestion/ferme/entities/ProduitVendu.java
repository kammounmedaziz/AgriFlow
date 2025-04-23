package gestion.ferme.entities;

public class ProduitVendu {
    private int idProduit;
    private String nomProduit;
    private String typeProduit;
    private float quantiteDisponible;
    private String uniteProduit;
    private float prixUnitaire;

    public ProduitVendu() {}
    public ProduitVendu(int idProduit) {
        this.idProduit = idProduit;
    }

    public ProduitVendu(  float quantiteDisponible, float prixUnitaire) {

        this.quantiteDisponible = quantiteDisponible;
        this.prixUnitaire = prixUnitaire;
    }
    public ProduitVendu( int idProduit,String nomProduit, String typeProduit, float quantiteDisponible, String uniteProduit, float prixUnitaire) {
        this.idProduit= idProduit;
        this.nomProduit = nomProduit;
        this.typeProduit = typeProduit;
        this.quantiteDisponible = quantiteDisponible;
        this.uniteProduit = uniteProduit;
        this.prixUnitaire = prixUnitaire;
    }

    public int getIdProduit() {
        return idProduit;
    }
    public String getNomProduit() {
        return nomProduit;
    }

    public String getTypeProduit() {
        return typeProduit;
    }

    public float getQuantiteDisponible() {
        return quantiteDisponible;
    }

    public String getUniteProduit() {
        return uniteProduit;
    }

    public float getPrixUnitaire() {
        return prixUnitaire;
    }


    public void setIdProduit(int idProduit) {
        this.idProduit = idProduit;
    }
    public void setNomProduit(String nomProduit) {
        this.nomProduit = nomProduit;
    }

    public void setTypeProduit(String typeProduit) {
        this.typeProduit = typeProduit;
    }

    public void setQuantiteDisponible(float quantiteDisponible) {
        this.quantiteDisponible = quantiteDisponible;
    }

    public void setUniteProduit(String uniteProduit) {
        this.uniteProduit = uniteProduit;
    }

    public void setPrixUnitaire(float prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    @Override
    public String toString() {
        return "ProduitVendu{" +
                "idProduit=" + idProduit +
                ", nomProduit='" + nomProduit + '\'' +
                ", typeProduit='" + typeProduit + '\'' +
                ", quantiteDisponible=" + quantiteDisponible +
                ", uniteProduit='" + uniteProduit + '\'' +
                ", prixUnitaire=" + prixUnitaire +
                '}';
    }
}
