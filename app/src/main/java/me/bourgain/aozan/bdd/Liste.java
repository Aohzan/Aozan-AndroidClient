package me.bourgain.aozan.bdd;

import java.util.Date;

/**
 * Created by Matthieu on 04/12/2016.
 */

public class Liste {
    private long idListe;
    private String nom;

    public Liste() {
        idListe = -1;
        this.nom = "";
    }

    public void setId(long id) {
        this.idListe = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public long getId() {
        return idListe;
    }

    public String getNom() {
        return nom;
    }
}
