package gestions.ferme.entities;

import java.time.LocalDate;

public class Vente {
    private int id;
    private int idProduit;
    private float quantiteVente;
    private LocalDate dataVente;  // Change String en LocalDate
    private float revenuTotal;
    private String idCliente;

    // Constructeurs
    public Vente() {}
    public Vente(int id) {
        this.id = id;
    }

    public Vente(int id, float quantiteVente, float revenuTotal) {
        this.id = id;
        this.quantiteVente = quantiteVente;
        this.revenuTotal = revenuTotal;
    }

    public Vente(int idProduit, float quantiteVente, float revenuTotal, String idCliente) {
        this.idProduit = idProduit;
        this.quantiteVente = quantiteVente;
        this.revenuTotal = revenuTotal;
        this.idCliente = idCliente;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public int getIdProduit() {
        return idProduit;
    }

    public float getQuantiteVente() {
        return quantiteVente;
    }

    public LocalDate getDataVente() {
        return dataVente;
    }

    public float getRevenuTotal() {
        return revenuTotal;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIdProduit(int idProduit) {
        this.idProduit = idProduit;
    }

    public void setQuantiteVente(float quantiteVente) {
        this.quantiteVente = quantiteVente;
    }

    public void setDataVente(LocalDate dataVente) {
        this.dataVente = dataVente;
    }

    public void setRevenuTotal(float revenuTotal) {
        this.revenuTotal = revenuTotal;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }


    @Override
    public String toString() {
        return "Vente{" +
                "id=" + id +
                ", idProduit=" + idProduit +
                ", quantiteVente=" + quantiteVente +
                ", dataVente=" + dataVente +
                ", revenuTotal=" + revenuTotal +
                ", idCliente='" + idCliente + '\'' +
                '}';
    }
}
