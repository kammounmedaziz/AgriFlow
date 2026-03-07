package gestions.ferme.entities;

public class Utilisateur {
    private String id;
    private String nom;
    private String prenom;
    private String sex;
    private String role;
    private String telephone;
    private String password;
    private String email;


    public Utilisateur() {}
    public Utilisateur(String id) {
        this.id = id;
    }
    public Utilisateur(String id, String telephone, String password ) {
        this.id = id;
        this.telephone = telephone;
        this.password = password;
    }
    public Utilisateur (String id, String nom, String prenom, String sex,
                        String role, String telephone, String password, String email) {

        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.sex=sex;
        this.role=role;
        this.telephone=telephone;
        this.email=email;
        this.password=password;
    }

    public String getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getSex() {
        return sex;
    }

    public String getRole() {
        return role;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getEmail() {return email;}

    public String getPassword() {
        return password;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setEmail(String email) { this.email = email; }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Utilisateur{" +
                "id='" + id + '\'' +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", sex='" + sex + '\'' +
                ", role='" + role + '\'' +
                ", telephone='" + telephone + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
