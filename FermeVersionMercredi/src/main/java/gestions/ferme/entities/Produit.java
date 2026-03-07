package gestions.ferme.entities;

public class Produit {
    private int idProduit;
    private String nomProduit;
    private String typeProduit;
    private float quantiteDisponible;
    private String uniteProduit;
    private float prixUnitaire;
    private String image;

    public Produit() {}
    public Produit(int idProduit) {
        this.idProduit = idProduit;
    }

    public Produit(float quantiteDisponible, float prixUnitaire) {

        this.quantiteDisponible = quantiteDisponible;
        this.prixUnitaire = prixUnitaire;
    }
    public Produit(String nomProduit, String typeProduit, float quantiteDisponible, String uniteProduit, float prixUnitaire, String image) {

        this.nomProduit = nomProduit;
        this.typeProduit = typeProduit;
        this.quantiteDisponible = quantiteDisponible;
        this.uniteProduit = uniteProduit;
        this.prixUnitaire = prixUnitaire;
        this.image = image;
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
    public String getImage() {return image;}


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

    public void setImage(String image) { this.image = image; }

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
