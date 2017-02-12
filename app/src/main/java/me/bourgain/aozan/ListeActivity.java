package me.bourgain.aozan;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import me.bourgain.aozan.adapter.ElementAdapter;
import me.bourgain.aozan.bdd.Element;
import me.bourgain.aozan.bdd.GestionBdd;
import me.bourgain.aozan.web.WebManager;

/**
 * Created by Matthieu on 05/12/2016.
 */

public class ListeActivity extends AppCompatActivity {
    // Variables nécessaires
    private GestionBdd bdd;
    private WebManager wm;
    private ArrayList<Element> elements = new ArrayList<Element>();
    private Long idListe;
    private SwipeRefreshLayout swipeElements;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste);
        // Récupération de l'Id de la liste choisie
        idListe = getIntent().getLongExtra("IDLISTE", 0);

        // Connexion à la bdd et au serveur web
        bdd = new GestionBdd(this);
        wm = new WebManager(this);

        // Récupération des éléments
        reloadElements();

        // Listeners
        ListView listView = (ListView) findViewById(R.id.elements);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapter, View v, int position, long id) {
                // Récupération de la liste sélectionnée
                final Element selected = elements.get(position);
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setTitle("Confirmation");
                alert.setMessage("Voulez-vous vraiment supprimer cet élément ?");
                alert.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        wm.DelElement(selected.getId());
                        reloadElements();
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
        swipeElements = (SwipeRefreshLayout) findViewById(R.id.swipeElements);
        swipeElements.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reloadElements();
                swipeElements.setRefreshing(false);
            }
        });
    }


    // Récupération des données en base et affichage
    private void reloadElements() {
        wm.SynchronizeElements();
        bdd.open();
        elements = bdd.getListElements(idListe);
        bdd.close();

        // Affichage des éléments
        ElementAdapter adapter = new ElementAdapter(this, elements);
        ListView listView = (ListView) findViewById(R.id.elements);
        listView.setAdapter(adapter);

        Toast.makeText(this, "Contenu mis à jour", Toast.LENGTH_LONG).show();
    }

    public void buttonAddElement(View view){
        EditText newElementNom = (EditText) findViewById(R.id.newElementNom);
        String nom = newElementNom.getText().toString();
        if (nom.length() < 3) {
            Toast.makeText(this, "Merci d'indiquer un nom d'au moins 3 caractères",
                    Toast.LENGTH_LONG).show();
        } else {
            // Old quand pas de web service bdd.addElement(nom, idListe);
            wm.AddElement(nom, idListe);
            newElementNom.setText("");
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            reloadElements();
        }
    }
}
