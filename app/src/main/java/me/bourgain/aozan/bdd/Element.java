package me.bourgain.aozan.bdd;

/**
 * Created by Matthieu on 04/12/2016.
 */

public class Element {
    private long idElement;
    private long idListe;
    private String nom;

    public Element() {
        this.idListe = -1;
        this.idElement = -1;
        this.nom = "";
    }

    public void setId(long id) {
        this.idElement = id;
    }

    public void setIdListe(long idListe) {
        this.idListe = idListe;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }


    public long getId() {
        return idElement;
    }

    public long getIdListe() {
        return idListe;
    }

    public String getNom() {
        return nom;
    }
}
