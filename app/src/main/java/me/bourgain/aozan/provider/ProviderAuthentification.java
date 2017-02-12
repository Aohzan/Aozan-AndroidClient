package me.bourgain.aozan.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import me.bourgain.aozan.bdd.GestionBdd;
import me.bourgain.aozan.bdd.Utilisateur;
import me.bourgain.aozan.web.WebManager;

/**
 * Created by Matthieu on 10/02/2017.
 */

public class ProviderAuthentification extends ContentProvider {

    public static final Uri URI_CONTENT_PROVIDER = Uri.parse("content://me.bourgain.aozan.provider.ProviderAuthentification");
    public static final String MIME_CONTENT_PROVIDER = "vnd.android.cursor.item/vnd.me.bourgain.aozan.provider.Utilisateur";

    private GestionBdd db;

    @Override
    public boolean onCreate() {
        db = new GestionBdd(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        return MIME_CONTENT_PROVIDER;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id = db.setUtilisateur(values);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return Integer.parseInt(null);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return Integer.parseInt(null);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }
}