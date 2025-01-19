package com.example.perfectpc;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

public class StoreKeeperActivity extends AppCompatActivity {

    private AdapterComponent adapterComponent;
    private ManageStock manageStock;
    private List<Component> initialComponents;
    private DatabaseHelper dbHelper;
    private  String role;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_keeper);  // Le layout pour cette activité

        role = "StoreKeeper";
        // Initialisation des composants de l'interface
        Button addComponentButton = findViewById(R.id.add_component_button);
        RecyclerView recyclerView = findViewById(R.id.recyclerview);

        // Initialisation du DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        //Affichage de la liste de Composante
        manageStock = new ManageStock();


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadInitialComponents();
        initialComponents = dbHelper.getAllComponents();
        manageStock = new ManageStock(initialComponents);


        adapterComponent = new AdapterComponent(initialComponents, manageStock, dbHelper, role, this::showModifyComponentDialog);
        recyclerView.setAdapter(adapterComponent);
        updateComponentList();

        // Gestion du clic sur le bouton "Ajouter un élément"
        addComponentButton.setOnClickListener(v -> {
            showAddComponentDialog();
        });
    }

    private void loadInitialComponents() {
        if (dbHelper.getAllComponents().isEmpty()) {
            List<Component> initialComponents = Arrays.asList(
                    new Component("Corsair 4000D AIRFLOW Tempered Glass (Noir)", "Boitier", "Matériel", 5, null),
                    new Component("Aerocool CS-106 (Noir)", "Boitier", "Matériel", 5, null),
                    new Component("Cooler Master MasterBox Q500L", "Boitier", "Matériel", 5, null),
                    new Component("Antec C8 (Noir)", "Boitier", "Matériel", 5, null),
                    new Component("Phanteks Eclipse G360A (Blanc)", "Boitier", "Matériel", 5, null),
                    new Component("ASUS TUF GAMING B650-PLUS WIFI", "Carte mère", "Matériel", 5, null),
                    new Component("Gigabyte B650 EAGLE AX", "Carte mère", "Matériel", 5, null),
                    new Component("MSI MAG B650 TOMAHAWK WIFI", "Carte mère", "Matériel", 5, null),
                    new Component("ASUS TUF GAMING B550-PLUS WIFI II", "Carte mère", "Matériel", 5, null),
                    new Component("MSI PRO B760-P WIFI DDR4", "Carte mère", "Matériel", 5, null),
                    new Component("G.Skill Aegis 32 Go (2 x 16 Go) DDR4 3200 MHz CL16", "Barrette mémoire", "Matériel", 5, null),
                    new Component("G.Skill Flare X5 Series Low Profile 32 Go (2x 16 Go) DDR5 6000 MHz CL30", "Barrette mémoire", "Matériel", 5, null),
                    new Component("Corsair Vengeance LPX Series Low Profile 32 Go (2x 16 Go) DDR4 3200 MHz CL16", "Barrette mémoire", "Matériel", 5, null),
                    new Component("Corsair Vengeance DDR5 32 Go (2 x 16 Go) 6000 MHz CL36 - Noir", "Barrette mémoire", "Matériel", 5, null),
                    new Component("Seagate BarraCuda 2 To (ST2000DM008)", "Disque dur", "Matériel", 5, null),
                    new Component("Seagate IronWolf 4 To (ST4000VN006)", "Disque dur", "Matériel", 5, null),
                    new Component("Western Digital WD Red Plus 4 To 256 Mo", "Disque dur", "Matériel", 5, null),
                    new Component("Acer 27 LED - Nitro VG271UM3bmiipx", "Écran", "Matériel", 5, null),
                    new Component("ASUS 27 OLED - ROG Strix XG27AQDMG", "Écran", "Matériel", 5, null),
                    new Component("iiyama 21.5 LED - G-Master G2245HSU-B1 Black Hawk", "Écran", "Matériel", 5, null),
                    new Component("MSI 27 LED - Optix G27CQ4 E2", "Écran", "Matériel", 5, null),
                    new Component("Logitech Wireless Desktop MK270 (AZERTY)", "Clavier-souris", "Matériel", 5, null),
                    new Component("Spirit of Gamer Ultimate 600 Wireless Noir", "Clavier-souris", "Matériel", 5, null),
                    new Component("Advance WorkMate Plus Wireless", "Clavier-souris", "Matériel", 5, null),
                    new Component("Brave", "Navigateur web", "Logiciel", 5, null),
                    new Component("Polypane", "Navigateur web", "Logiciel", 5, null),
                    new Component("Avast secure Browser", "Navigateur web", "Logiciel", 5, null),
                    new Component("Microsoft 365 Personnel", "Suite bureautique", "Logiciel", 5, null),
                    new Component("Druide Antidote 11", "Suite bureautique", "Logiciel", 5, null),
                    new Component("Microsoft Office Famille et Etudiant 2024", "Suite bureautique", "Logiciel", 0, null),
                    new Component("Website X5", "Outil de developement", "Logiciel", 5, null),
                    new Component("Website X5 Pro", "Outil de developement", "Logiciel", 5, null),
                    new Component("Website X5 Evolution", "Outil de developement", "Logiciel", 5, null)
            );
            for (Component component : initialComponents) {
                dbHelper.insertComponent(component); // Save to database
            }
        }
    }

    // Mettre à jour la liste des éléments affichée
    @SuppressLint("NotifyDataSetChanged")
    private void updateComponentList() {
        List<Component> updatedList = dbHelper.getAllComponents();
        manageStock.updateStockList(updatedList);
        adapterComponent.notifyDataSetChanged();
    }

    // Fenêtre de dialogue pour ajouter un nouvel élément
    private void showAddComponentDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_component, null);
        builder.setView(dialogView);

        EditText titleInput = dialogView.findViewById(R.id.add_title);
        EditText typeInput = dialogView.findViewById(R.id.add_type);
        EditText subTypeInput = dialogView.findViewById(R.id.add_sub_type);

        builder.setTitle("Ajouter un élément");
        builder.setPositiveButton("Ajouter", (dialog, which) -> {
            String title = titleInput.getText().toString();
            String type = typeInput.getText().toString();
            String subType = subTypeInput.getText().toString();

            if (title.isEmpty() || type.isEmpty() || subType.isEmpty()) {
                Toast.makeText(this, "Tous les champs sont obligatoires", Toast.LENGTH_SHORT).show();
                return;
            }

            // Ajouter un élément
            Component newComponent = new Component(title, type, subType, 0, null);
            dbHelper.insertComponent(newComponent); // Ajouter dans la base
            updateComponentList(); // Mettre à jour la list
        });
        builder.setNegativeButton("Annuler", null);
        builder.create().show();

    }

    // Fenêtre de dialogue pour supprimer un élément existant
    private void showModifyComponentDialog(Component component, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_delete_component, null);
        builder.setView(dialogView);


        /*builder.setTitle("Supprimer?");


        // Ajouter un bouton pour supprimer l'élément
        builder.setNegativeButton("Supprimer", (dialog, which) -> {
            dbHelper.deleteComponent(component.getTitle());// Supprimer de la bas
            updateComponentList();  // Mettre à jour la liste
        });*/

        builder.setNeutralButton("Annuler", null);
        builder.create().show();
    }
}
