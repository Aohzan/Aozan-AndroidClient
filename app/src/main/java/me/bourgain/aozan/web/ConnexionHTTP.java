package me.bourgain.aozan.web;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by Matthieu on 10/12/2016.
 */

public class ConnexionHTTP extends AsyncTask<String, Void, Boolean> {
    private String reponse = null;
    // Adresse du serveur Web
    private String adresseServiceWeb = "http://dev.bourgain.me/aozan/index.php";
    // Admin à 1 permet d'afficher toutes les listes, sinon dépendant du idUser et des autorisations associées
    private String admin = "1";

    @Override
    protected Boolean doInBackground(String... args) {
        // Récup des paramètres et construction de l'adresse web
        String adresse = adresseServiceWeb + "?admin=" + admin + "&user=" + args[0] + "&type=" + args[1];
        if(args.length > 2){
            adresse += "&value=" + args[2];
        }
        if(args.length > 3){
            adresse += "&value2=" + args[3];
        }

        URL url = null;
        try {
            url = new URL(adresse);
        } catch(MalformedURLException e) {

        }

        URLConnection connexion = null;
        try {
            connexion = url.openConnection();
            connexion.setDoOutput(true);
        } catch(IOException e) {
            Log.e(this.getClass().getName(), "Connexion impossible : " + e);
            return false;
        }
        // Lecture de la réponse HTTP
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connexion.getInputStream()));
            reponse = "";
            String tmp = "";
            while ((tmp = reader.readLine()) != null) {
                reponse += tmp;
            }
            reader.close();
        } catch(Exception e) {
            Log.e(this.getClass().getName(), "Erreur lors de la lecture de la reponse : " + e);
            return false;
        }

        return true;
    }

    public String getReponse() {
        return reponse.toString();
    }
}