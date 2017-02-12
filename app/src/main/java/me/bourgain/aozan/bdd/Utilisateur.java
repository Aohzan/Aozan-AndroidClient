package me.bourgain.aozan.bdd;

/**
 * Created by Matthieu on 04/12/2016.
 */

public class Utilisateur {
    private long id;
    private String login;
    private String nom;

    public Utilisateur() {
        this.id = -1;
        this.login = "";
        this.nom = "";
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getNom() {
        return nom;
    }
}
