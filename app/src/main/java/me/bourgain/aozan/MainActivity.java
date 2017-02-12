package me.bourgain.aozan;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import me.bourgain.aozan.adapter.ListeAdapter;
import me.bourgain.aozan.bdd.GestionBdd;
import me.bourgain.aozan.bdd.Liste;
import me.bourgain.aozan.bdd.Utilisateur;
import me.bourgain.aozan.web.WebManager;

public class MainActivity extends AppCompatActivity {
    // Variables nécessaires
    private GestionBdd bdd;
    private WebManager wm;
    private ArrayList<Liste> listes = new ArrayList<Liste>();
    private SwipeRefreshLayout swipeListes;
    private Utilisateur utilisateur = new Utilisateur();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Instanciation de la bdd (création si besoin, accès aux méthodes...)
        bdd = new GestionBdd(this);

        // Connexion au service Web
        wm = new WebManager(this);

        // Récupération des listes
        reloadContent();

        // Ajouts de listeners pour actions spécifiques
        // Au click sur une liste
        ListView listView = (ListView) findViewById(R.id.listes);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                // Récupération de la liste sélectionnée
                Liste selected = listes.get(position);
                // Création d'une nouvelle activité
                Intent intent = new Intent(v.getContext(), ListeActivity.class);
                // Transmission de l'Id de la liste
                intent.putExtra("IDLISTE", selected.getId());
                // Démarrage de l'activité
                startActivity(intent);
            }
        });
        // Au long clic sur une liste
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter, View v, int position, long id) {
                // Récupération de la liste sélectionnée
                final Liste selected = listes.get(position);
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setTitle("Confirmation");
                alert.setMessage("Voulez-vous vraiment supprimer cette liste ?");
                alert.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        wm.DelListe(selected.getId());
                        reloadContent();
                        dialog.dismiss();
                    }
                });
                alert.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alert.show();
                return true;
            }
        });

        // Au swipe vers le bas pour refresh
        swipeListes = (SwipeRefreshLayout) findViewById(R.id.swipeElements);
        swipeListes.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reloadContent();
                swipeListes.setRefreshing(false);
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        reloadContent();
    }

    private void getUtilisateur(){
        // Récupération et affiche de l'utilisateur
        utilisateur = bdd.getUtilisateur();
        TextView titleList = (TextView) findViewById(R.id.titleList);
        titleList.setText(getResources().getString(R.string.titleList) + " (" + utilisateur.getNom() + ")");
    }

    // Rachargement du contenu via la bdd puis affichage dans les listes
    private void reloadContent() {
        try {
            getUtilisateur();
            // Récupération des infos du web en bdd
            wm.SynchronizeListes();
            wm.SynchronizeElements();
            // Récupération des listes
            listes = bdd.getListListes();
            // Affichage des listes
            ListeAdapter adapter = new ListeAdapter(this, listes);
            ListView listView = (ListView) findViewById(R.id.listes);
            listView.setAdapter(adapter);
            Toast.makeText(this, "Contenu mis à jour", Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(this, "Erreur mise à jour : " + ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    // Bouton ajout de liste
    public void buttonAddListe(View view){
        EditText newListeNom = (EditText) findViewById(R.id.newListeNom);
        String nom = newListeNom.getText().toString();
        if (nom.length() < 3) {
            Toast.makeText(this, "Merci d'indiquer un nom d'au moins 3 caractères", Toast.LENGTH_LONG).show();
        } else {
            // Old quand pas de web service bdd.addListe(nom);
            wm.AddListe(nom);
            newListeNom.setText("");
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            reloadContent();
        }
    }

    /*
    Plus utilisée depuis récup données du serveur, permet d'ajouter des données de test dans la bdd locale
    private void InsertionDonneesTest(){
        bdd.deleteAllElements();
        bdd.deleteAllListes();
        long idListeViennoiseries = bdd.addListe("Viennoiseries");
        long idListeCourses = bdd.addListe("Liste de courses");
        bdd.addElement("Croissant", idListeViennoiseries);
        bdd.addElement("Pépito", idListeViennoiseries);
        bdd.addElement("Pain au raisin", idListeViennoiseries);
        bdd.addElement("Pain", idListeCourses);
        bdd.addElement("Fromage", idListeCourses);
    }*/
}
