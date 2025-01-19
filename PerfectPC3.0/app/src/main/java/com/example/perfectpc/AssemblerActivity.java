package com.example.perfectpc;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AssemblerActivity extends AppCompatActivity {

    private RecyclerView stockView, commandesView;
    private AdapterComponent adapterComponent;
    private AdapterCommande adapterCommande;
    private ManageStock manageStock;
    private List<Component> stockComponent;
    private List<Commande> commandes;
    private DatabaseHelper dbHelper;
    private CommandeDatabase commandeDatabase;
    private String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assembler); // Le layout pour cette activité

        role = "Assembler";

        // Initialisation des bases de données
        dbHelper = new DatabaseHelper(this);
        commandeDatabase = new CommandeDatabase(this);

        // Charger le stock et les commandes
        stockComponent = dbHelper.getAllComponents();
        commandes = commandeDatabase.getAllCommandes();

        // Afficher le stock
        stockView = findViewById(R.id.recyclerview_stock);
        stockView.setLayoutManager(new LinearLayoutManager(this));
        adapterComponent = new AdapterComponent(stockComponent, null, dbHelper, role, null);
        stockView.setAdapter(adapterComponent);

        // Afficher les commandes
        commandesView = findViewById(R.id.recyclerview_commandes);
        commandesView.setLayoutManager(new LinearLayoutManager(this));
        adapterCommande = new AdapterCommande(commandes, this::showCommandeDialog);
        commandesView.setAdapter(adapterCommande);
    }

    private void showCommandeDialog(Commande commande, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_manage_commande, null);
        builder.setView(dialogView);

        Button acceptButton = dialogView.findViewById(R.id.accept_commande);
        Button refuseButton = dialogView.findViewById(R.id.refuse_commande);
        Button completeButton = dialogView.findViewById(R.id.complete_commande);

        builder.setTitle("Gérer la commande");
        AlertDialog dialog = builder.create();
        dialog.show();

        acceptButton.setOnClickListener(v -> {
            if (isCommandeInStock(commande.getStockItemId())) {
                Toast.makeText(this, "Commande acceptée.", Toast.LENGTH_SHORT).show();
                // Diminuer le stock
                updateStock(commande.getStockItemId());
                updateCommandeStatus(commande, "Accepted", position);
            } else {
                Toast.makeText(this, "Commande refusée : Article non disponible en stock.", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        });

        refuseButton.setOnClickListener(v -> {
            Toast.makeText(this, "Commande refusée.", Toast.LENGTH_SHORT).show();
            updateCommandeStatus(commande, "Refused", position); // Mise à jour du statut
            dialog.dismiss();
        });

        completeButton.setOnClickListener(v -> {
            if ("Accepted".equals(getCommandeStatus(commande))) {
                Toast.makeText(this, "Commande terminée.", Toast.LENGTH_SHORT).show();
                updateCommandeStatus(commande, "Completed", position); // Mise à jour du statut
            } else {
                Toast.makeText(this, "Vous ne pouvez terminer qu'une commande acceptée.", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        });



    }

    private boolean isCommandeInStock(String stockItemId) {
        for (Component component : stockComponent) {
            if (component.getTitle().equals(stockItemId) && component.getQuantity() > 0) {
                return true;
            }
        }
        return false;
    }

    private void updateStock(String stockItemId) {
        for (Component component : stockComponent) {
            if (component.getTitle().equals(stockItemId)) {
                component.setQuantity(component.getQuantity() - 1);
                dbHelper.updateComponent(component);
                break;
            }
        }
        adapterComponent.notifyDataSetChanged();
    }

    private void updateCommandeStatus(Commande commande, String status, int position) {
        try {
            if (commande == null || commande.getId() == null) {
                Toast.makeText(this, "Erreur : commande invalide.", Toast.LENGTH_SHORT).show();
                return;
            }

            Log.d("AssemblerActivity", "Mise à jour de la commande avec ID : " + commande.getId());

            // Mise à jour du statut dans la base de données
            commandeDatabase.updateCommandeStatus(commande.getId(), status);

            // Supprimer de la liste si le statut est "Refused" ou "Completed"
            if ("Refused".equals(status) || "Completed".equals(status)) {
                commandes.remove(position); // Retirer la commande de la liste locale
                adapterCommande.notifyItemRemoved(position); // Notifier l'adaptateur
            } else {
                // Mettre à jour la commande dans la liste
                Commande updatedCommande = commandeDatabase.getCommandeById(commande.getId());
                if (updatedCommande != null) {
                    commandes.set(position, updatedCommande);
                    adapterCommande.notifyItemChanged(position);
                }
            }

            Toast.makeText(this, "Commande mise à jour : " + status, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("AssemblerActivity", "Erreur dans updateCommandeStatus : " + e.getMessage());
            Toast.makeText(this, "Une erreur est survenue lors de la mise à jour.", Toast.LENGTH_SHORT).show();
        }
    }



    private String getCommandeStatus(Commande commande) {
        // Récupère le statut de la commande
        return commandeDatabase.getCommandeStatus(commande.getId());
    }
}