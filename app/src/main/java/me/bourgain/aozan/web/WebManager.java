package me.bourgain.aozan.web;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import me.bourgain.aozan.bdd.GestionBdd;
import me.bourgain.aozan.bdd.Liste;
import me.bourgain.aozan.bdd.Utilisateur;

/**
 * Created by Matthieu on 10/12/2016.
 */

public class WebManager {
    private Long idUser = null;
    private Context context;

    // Constructeur
    public WebManager(Context context){
        this.context = context;
        // On essaie de récupérer l'utilisateur courant
        try{
            GestionBdd db = new GestionBdd(context);
            this.idUser = db.getUtilisateur().getId();
        } catch (Exception ex) {
        }
    }

    // Synchronisation des listes en local
    public void SynchronizeListes(){
        ConnexionHTTP connexion = new ConnexionHTTP();
        connexion.execute(idUser.toString(), "GetListes", "0");
        try {
            if (connexion.get()) {
                /* Connexion réussie */
                GestionBdd db = new GestionBdd(context);
                // Effacement des listes
                db.deleteAllListes();
                // Récupération des listes
                JSONObject jObject = new JSONObject(connexion.getReponse());
                JSONArray jArray = jObject.getJSONArray("reponse");
                for (int i=0; i < jArray.length(); i++)
                {
                    JSONObject oneObject = jArray.getJSONObject(i);
                    // Récup des éléments l'objet
                    Long id = oneObject.getLong("id");
                    String nom = oneObject.getString("nom");
                    // Ajout de la nouvelle liste
                    db.addListe(id, nom);
                }
            }
        } catch (Exception e) {
        }
    }

    // Synchronisation des élements des listes en local
    public void SynchronizeElements(){
        GestionBdd db = new GestionBdd(context);
        // Effacement des éléments existants
        db.deleteAllElements();
        // Récupération des listes de l'user
        ArrayList<Liste> listesUser = db.getListListes();
        // Récupération des éléments de chaque liste remontée
        for (Liste liste : listesUser) {
            Long idListe = liste.getId();
            ConnexionHTTP connexion = new ConnexionHTTP();
            connexion.execute(idUser.toString(), "GetElements", idListe.toString());
            try {
                if (connexion.get()) {
                /* Connexion réussie */
                    // Récupération des listes
                    JSONObject jObject = new JSONObject(connexion.getReponse());
                    JSONArray jArray = jObject.getJSONArray("reponse");
                    for (int i=0; i < jArray.length(); i++)
                    {
                        JSONObject oneObject = jArray.getJSONObject(i);
                        // Récup des éléments l'objet
                        Long idElement = oneObject.getLong("id");
                        String nom = oneObject.getString("nom");
                        // Ajout de l'élément
                        db.addElement(idElement, nom, idListe);
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    // Ajout d'une liste
    public void AddListe(String nom){
        ConnexionHTTP connexion = new ConnexionHTTP();
        connexion.execute(idUser.toString(), "AddListe", nom);
    }

    // Ajout d'un élément
    public void AddElement(String nom, Long idListe){
        ConnexionHTTP connexion = new ConnexionHTTP();
        connexion.execute(idUser.toString(), "AddElement", nom, idListe.toString());
    }

    // Suppression d'une liste
    public void DelListe(Long id){
        ConnexionHTTP connexion = new ConnexionHTTP();
        connexion.execute(idUser.toString(), "DelListe", id.toString());
    }

    // Suppression d'un élement
    public void DelElement(Long id){
        ConnexionHTTP connexion = new ConnexionHTTP();
        connexion.execute(idUser.toString(), "DelElement", id.toString());
    }

    // Authentification, appelé depuis le service
    public String Authentification(String login, String password){
        ConnexionHTTP connexion = new ConnexionHTTP();
        connexion.execute(login, "Authentification", password);
        try {
            if (connexion.get()) {
                /* Connexion réussie */
                GestionBdd db = new GestionBdd(context);
                // Récupération des listes
                JSONObject jObject = new JSONObject(connexion.getReponse());
                JSONArray jArray = jObject.getJSONArray("reponse");
                for (int i=0; i < jArray.length(); i++)
                {
                    JSONObject oneObject = jArray.getJSONObject(i);
                    // Récup des éléments l'objet
                    Long id = oneObject.getLong("id");
                    String nom = oneObject.getString("nom");
                    // Ajout de la nouvelle liste
                    db.setUtilisateur(id, login, nom);
                    this.idUser = id;
                    return nom;
                }
            }
        } catch (Exception e) {
        }
        return null;
    }
}
