package me.bourgain.aozan.bdd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Matthieu on 04/12/2016.
 */

public class GestionBdd {
    public static final int VERSION_DATABASE = 4;
    public static final String NOM_DATABASE = "Aozan.db";

    private static class DatabaseHelper extends SQLiteOpenHelper {

        public static final String CREATION_TABLE_LISTE_SQL =
                "CREATE TABLE " + DefinitionBdd.Liste.NOM_TABLE + " (" +
                        DefinitionBdd.Liste.ID + " INTEGER PRIMARY KEY," +
                        DefinitionBdd.Liste.NOM + " TEXT NOT NULL)";

        public static final String SUPPRESSION_TABLE_LISTE_SQL =
                "DROP TABLE IF EXISTS " + DefinitionBdd.Liste.NOM_TABLE;

        public static final String CREATION_TABLE_ELEMENT_SQL =
                "CREATE TABLE " + DefinitionBdd.Element.NOM_TABLE + " (" +
                        DefinitionBdd.Element.ID + " INTEGER PRIMARY KEY," +
                        DefinitionBdd.Element.IDLISTE + " INTEGER NOT NULL," +
                        DefinitionBdd.Element.NOM + " TEXT NOT NULL)";

        public static final String SUPPRESSION_TABLE_ELEMENT_SQL =
                "DROP TABLE IF EXISTS " + DefinitionBdd.Element.NOM_TABLE;

        public static final String CREATION_TABLE_UTILISATEUR_SQL =
                "CREATE TABLE " + DefinitionBdd.Utilisateur.NOM_TABLE + " (" +
                        DefinitionBdd.Utilisateur.ID + " INTEGER PRIMARY KEY," +
                        DefinitionBdd.Utilisateur.LOGIN + " TEXT NOT NULL," +
                        DefinitionBdd.Utilisateur.NOM + " TEXT NOT NULL)";

        public static final String SUPPRESSION_TABLE_UTILISATEUR_SQL =
                "DROP TABLE IF EXISTS " + DefinitionBdd.Utilisateur.NOM_TABLE;

        public DatabaseHelper(Context context) {
            super(context, NOM_DATABASE, null, VERSION_DATABASE);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATION_TABLE_LISTE_SQL);
            db.execSQL(CREATION_TABLE_ELEMENT_SQL);
            db.execSQL(CREATION_TABLE_UTILISATEUR_SQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(SUPPRESSION_TABLE_LISTE_SQL);
            db.execSQL(SUPPRESSION_TABLE_ELEMENT_SQL);
            db.execSQL(SUPPRESSION_TABLE_UTILISATEUR_SQL);
            onCreate(db);
        }

        @Override
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }
    }

    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;

    public GestionBdd(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        db.close();
    }

    /*
    Gestion des listes
     */
    public long addListe(Long id, String nom) {
        open();
        ContentValues valeurs = new ContentValues();
        valeurs.put(DefinitionBdd.Liste.ID, id);
        valeurs.put(DefinitionBdd.Liste.NOM, nom);
        Long newId = db.insert(DefinitionBdd.Liste.NOM_TABLE, null, valeurs);
        close();
        return newId;
    }

    public ArrayList<Liste> getListListes () {
        open();
        ArrayList<Liste> listes = new ArrayList<Liste>();

        Cursor c = db.query(DefinitionBdd.Liste.NOM_TABLE, new String[] {
                DefinitionBdd.Liste.ID,
                DefinitionBdd.Liste.NOM}, null, null, null, null, null);

        c.moveToFirst();
        while (!c.isAfterLast()) {
            Liste l = new Liste();

            l.setId(c.getLong(c.getColumnIndex(DefinitionBdd.Liste.ID)));
            l.setNom(c.getString(c.getColumnIndex(DefinitionBdd.Liste.NOM)));

            listes.add(l);
            c.moveToNext();
        }
        c.close();
        close();
        return listes;
    }

    public void deleteListe(long id) {
        open();
        db.delete(DefinitionBdd.Liste.NOM_TABLE, DefinitionBdd.Liste.ID + " = " + id, null);
        close();
    }

    public void deleteAllListes() {
        open();
        db.execSQL("DELETE FROM " + DefinitionBdd.Liste.NOM_TABLE);
        close();
    }

    /*
    Gestion des éléments
    */
    public long addElement(long id, String nom, long idListe) {
        open();
        ContentValues valeurs = new ContentValues();
        valeurs.put(DefinitionBdd.Element.ID, id);
        valeurs.put(DefinitionBdd.Element.NOM, nom);
        valeurs.put(DefinitionBdd.Element.IDLISTE, idListe);
        Long newId = db.insert(DefinitionBdd.Element.NOM_TABLE, null, valeurs);
        close();
        return newId;
    }

    public ArrayList<Element> getListElements (long idListe) {
        open();
        ArrayList<Element> elements = new ArrayList<Element>();

        Cursor c = db.query(DefinitionBdd.Element.NOM_TABLE, new String[] {
                DefinitionBdd.Element.ID,
                DefinitionBdd.Element.NOM,
                DefinitionBdd.Element.IDLISTE}, DefinitionBdd.Element.IDLISTE + " = " + idListe, null, null, null, null);

        if (c.moveToFirst()) {
            do {
                Element e = new Element();

                e.setId(c.getLong(c.getColumnIndex(DefinitionBdd.Element.ID)));
                e.setNom(c.getString(c.getColumnIndex(DefinitionBdd.Element.NOM)));
                e.setIdListe(c.getLong(c.getColumnIndex(DefinitionBdd.Element.IDLISTE)));

                elements.add(e);
            } while (c.moveToNext());
        }
        c.close();
        close();
        return elements;
    }

    public void deleteElement(long id) {
        open();
        db.delete(DefinitionBdd.Element.NOM_TABLE, DefinitionBdd.Element.ID + " = " + id, null);
        close();
    }

    public void deleteAllElements() {
        open();
        db.execSQL("DELETE FROM " + DefinitionBdd.Element.NOM_TABLE);
        close();
    }

    /*
    Gestion de l'utilisateur
    */
    public void setUtilisateur(long id, String login, String nom) {
        open();
        // On supprime toutes les entrées précédentes, normalement aucune ou 1
        db.execSQL("DELETE FROM " + DefinitionBdd.Utilisateur.NOM_TABLE);
        // On ajoute la nouvelle
        ContentValues valeur = new ContentValues();
        valeur.put(DefinitionBdd.Utilisateur.ID, id);
        valeur.put(DefinitionBdd.Utilisateur.LOGIN, login);
        valeur.put(DefinitionBdd.Utilisateur.NOM, nom);
        db.insert(DefinitionBdd.Utilisateur.NOM_TABLE, null, valeur);
        close();
    }

    public long setUtilisateur(ContentValues valeur) {
        open();
        // On supprime toutes les entrées précédentes, normalement aucune ou 1
        db.execSQL("DELETE FROM " + DefinitionBdd.Utilisateur.NOM_TABLE);
        long id = db.insert(DefinitionBdd.Utilisateur.NOM_TABLE, null, valeur);
        close();
        return  id;
    }

    public Utilisateur getUtilisateur() {
        open();
        Utilisateur utilisateur = new Utilisateur();

        Cursor c = db.query(DefinitionBdd.Utilisateur.NOM_TABLE, new String[] {
                DefinitionBdd.Utilisateur.ID,
                DefinitionBdd.Utilisateur.LOGIN,
                DefinitionBdd.Utilisateur.NOM}, null, null, null, null, null);

        if (c.moveToFirst()) {
            utilisateur.setId(c.getLong(c.getColumnIndex(DefinitionBdd.Utilisateur.ID)));
            utilisateur.setLogin(c.getString(c.getColumnIndex(DefinitionBdd.Utilisateur.LOGIN)));
            utilisateur.setNom(c.getString(c.getColumnIndex(DefinitionBdd.Utilisateur.NOM)));
        }
        c.close();
        close();
        return utilisateur;
    }
}
