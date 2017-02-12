package me.bourgain.aozan.bdd;

import android.provider.BaseColumns;

import java.util.Date;

/**
 * Created by Matthieu on 04/12/2016.
 */

public class DefinitionBdd {
    private DefinitionBdd() {}

    public static abstract class Liste implements BaseColumns {
        public static final String NOM_TABLE = "liste";
        public static final String ID = "id";
        public static final String NOM = "nom";
    }

    public static abstract class Element implements BaseColumns {
        public static final String NOM_TABLE = "element";
        public static final String ID = "id";
        public static final String NOM = "nom";
        public static final String IDLISTE = "idListe";
    }

    public static abstract class Utilisateur implements BaseColumns {
        public static final String NOM_TABLE = "utilisateur";
        public static final String ID = "id";
        public static final String LOGIN = "login";
        public static final String NOM = "nom";
    }
}
