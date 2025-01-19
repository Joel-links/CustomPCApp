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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RequesterActivity extends AppCompatActivity {

    private RecyclerView stockView;
    private AdapterComponent adapterComponent;
    private ManageStock manageStock;
    private List<Component> stockComponent;
    private DatabaseHelper dbHelper;
    private String role;

    private CommandeDatabase commandeDatabase;
    private List<Commande> commandes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requester);  // Le layout pour cette activité

        role = "Requester";
        commandeDatabase = new CommandeDatabase(this);

        // Initialisation des composants de l'interface
        dbHelper = new DatabaseHelper(this);
        stockComponent = dbHelper.getAllComponents();


        // Afficher le stock
        stockView = findViewById(R.id.recyclerview_view_stock);
        stockView.setLayoutManager(new LinearLayoutManager(this));

        adapterComponent = new AdapterComponent(stockComponent, null, dbHelper, role, this::showAddCommandeDialog);
        stockView.setAdapter(adapterComponent);

        // Gestion du clic sur le bouton "Voir mes commandes"
        Button viewCommandeButton = findViewById(R.id.view_commande);
        viewCommandeButton.setOnClickListener(v -> openCommandePage());


    }


    private void openCommandePage() {
        Intent intent = new Intent(this, RequesterCommandeActivity.class);
        startActivity(intent);
    }


    // Fenêtre de dialogue pour crée une nouvelle commande
    private void showAddCommandeDialog(Component component, int position) {
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
            commandeDatabase.close();

        });
        builder.setNegativeButton("Annuler", null);
        builder.create().show();

    }
}


