package com.example.perfectpc;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class RequesterCommandeActivity extends AppCompatActivity {

    private CommandeDatabase commandeDatabase;
    private ArrayAdapter<String> commandeAdapter;
    private List<Commande> commandes;
    private ListView commandeListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requester_commande);  // Le layout pour cette activité

        commandeDatabase = new CommandeDatabase(this);

        // Initialisation des composants de l'interface
        //ListView commandeListView = findViewById(R.id.commande_list);
        //Button createCommandeButton = findViewById(R.id.create_commande);

        // Gestion du clic sur le bouton "Commander"
        //createCommandeButton.setOnClickListener(v -> showAddCommandeDialog());

        // Gestion du clic sur le bouton "Voir le stock"
        Button viewStockButton = findViewById(R.id.view_stock_list);
        viewStockButton.setOnClickListener(v -> openStockPage());


        commandes = commandeDatabase.getAllCommandes();

        commandeListView = findViewById(R.id.commande_list);
        updateCommandeList();


        // Gestion du clic sur une commande pour modification ou suppression
        commandeListView.setOnItemClickListener((parent, view, position, Id) ->{
            Commande commande = commandes.get(position);
            String commandeId = commande.getId();
            showDeleteCommandeDialog(commandeId);
        });
    }

    private void openStockPage() {
        Intent intent = new Intent(this, RequesterActivity.class);
        startActivity(intent);
    }

    // Mettre à jour la liste des commandes affichée

    private void updateCommandeList() {
        List<String> userCommandes = new ArrayList<>();
        for (Commande commande : commandes) {
            userCommandes.add(commande.getId() + " " + commande.getStockItemId());
        }
        commandeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userCommandes);
        ListView commandeListView = findViewById(R.id.commande_list);
        commandeListView.setAdapter(commandeAdapter);
    }



    // Fenêtre de dialogue pour crée une nouvelle commande
    private void showAddCommandeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_create_commande, null);
        builder.setView(dialogView);

        EditText idInput = dialogView.findViewById(R.id.id);
        EditText itemInput = dialogView.findViewById(R.id.component_name);

        builder.setTitle("Ajouter votre commande");
        builder.setPositiveButton("Ajouter", (dialog, which) -> {
            String id = idInput.getText().toString();
            String item = itemInput.getText().toString();

            // Ajouter une commande
            commandeDatabase.createCommande(new Commande(id,item));

            commandes = commandeDatabase.getAllCommandes();
            updateCommandeList();

        });
        builder.setNegativeButton("Annuler", null);
        builder.create().show();

    }

    // Fenêtre de dialogue pour supprimer une commande
    private void showDeleteCommandeDialog(String commandeId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_delete_component, null);
        builder.setView(dialogView);


        builder.setTitle("Supprimer?");


        // Ajouter un bouton pour supprimer la commande
        builder.setNegativeButton("Supprimer", (dialog, which) -> {

            commandeDatabase.deleteCommande(commandeId);

            commandes = commandeDatabase.getAllCommandes();
            updateCommandeList();
        });

        builder.setNeutralButton("Annuler", null);
        builder.create().show();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        commandeDatabase.close();
    }
}

