package com.example.perfectpc;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {

    private List<User> requesters = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private ListView userListView;
    private Button addUserButton;
    private Button clearDatabaseButton;
    private Button resetDatabaseButton;
    private Button resetStockButton;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Initialisation du DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Initialisation des composants de l'interface
        userListView = findViewById(R.id.user_list);
        addUserButton = findViewById(R.id.add_user_button);
        clearDatabaseButton = findViewById(R.id.clear_database_button);
        resetDatabaseButton = findViewById(R.id.reset_database_button);
        resetStockButton = findViewById(R.id.reset_stock_button);

        // Chargement des utilisateurs avec le rôle "Requester"
        requesters = dbHelper.getRequesters(); // Nouvelle méthode dans DatabaseHelper
        updateUserList();

        // Gestion du clic sur le bouton "Ajouter un utilisateur"
        addUserButton.setOnClickListener(v -> showAddUserDialog());

        // Gestion du clic sur le bouton "Vider la base de données"
        clearDatabaseButton.setOnClickListener(v -> clearDatabase());

        // Gestion du clic sur le bouton "Réinitialiser la base de données"
        resetDatabaseButton.setOnClickListener(v -> resetDatabase());

        // Gestion du clic sur le bouton "Réinitialiser le stock"
        resetStockButton.setOnClickListener(v -> resetStock());

        // Gestion du clic sur un utilisateur pour modification ou suppression
        userListView.setOnItemClickListener((parent, view, position, id) -> {
            User selectedUser = requesters.get(position);
            showModifyUserDialog(selectedUser, position);
        });
    }

    // Mettre à jour la liste des utilisateurs affichée
    private void updateUserList() {
        List<String> userNames = new ArrayList<>();
        for (User user : requesters) {
            userNames.add(user.getFirstName() + " " + user.getLastName() + " (" + user.getRole() + ")");
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userNames);
        userListView.setAdapter(adapter);
    }

    // Fenêtre de dialogue pour ajouter un nouvel utilisateur Requester
    private void showAddUserDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_user, null);
        builder.setView(dialogView);

        EditText firstNameInput = dialogView.findViewById(R.id.first_name);
        EditText lastNameInput = dialogView.findViewById(R.id.last_name);
        EditText emailInput = dialogView.findViewById(R.id.email);
        EditText passwordInput = dialogView.findViewById(R.id.password);

        builder.setTitle("Ajouter un utilisateur Requester");
        builder.setPositiveButton("Ajouter", (dialog, which) -> {
            String firstName = firstNameInput.getText().toString().trim();
            String lastName = lastNameInput.getText().toString().trim();
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Tous les champs sont obligatoires.", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean success = dbHelper.addRequester(firstName, lastName, email, password, "Requester", false);

            if (success) {
                requesters = dbHelper.getRequesters(); // Mise à jour uniquement avec les Requesters
                updateUserList();
                Toast.makeText(this, "Utilisateur ajouté avec succès.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Erreur : cet e-mail existe déjà.", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Annuler", null);
        builder.create().show();
    }

    // Fenêtre de dialogue pour modifier ou supprimer un utilisateur existant
    private void showModifyUserDialog(User user, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_user, null);
        builder.setView(dialogView);

        EditText firstNameInput = dialogView.findViewById(R.id.first_name);
        EditText lastNameInput = dialogView.findViewById(R.id.last_name);
        EditText emailInput = dialogView.findViewById(R.id.email);
        EditText passwordInput = dialogView.findViewById(R.id.password);

        firstNameInput.setText(user.getFirstName());
        lastNameInput.setText(user.getLastName());
        emailInput.setText(user.getEmail());
        emailInput.setEnabled(false); // L'e-mail ne peut pas être modifié
        passwordInput.setText(user.getPassword());

        builder.setTitle("Modifier ou supprimer un utilisateur");

        builder.setPositiveButton("Modifier", (dialog, which) -> {
            String newFirstName = firstNameInput.getText().toString().trim();
            String newLastName = lastNameInput.getText().toString().trim();
            String newPassword = passwordInput.getText().toString().trim();

            if (newFirstName.isEmpty() || newLastName.isEmpty() || newPassword.isEmpty()) {
                Toast.makeText(this, "Tous les champs sont obligatoires.", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean success = dbHelper.updateRequester(user.getEmail(), newFirstName, newLastName, newPassword);

            if (success) {
                requesters = dbHelper.getRequesters(); // Mise à jour uniquement avec les Requesters
                updateUserList();
                Toast.makeText(this, "Utilisateur modifié avec succès.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Erreur lors de la modification.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Supprimer", (dialog, which) -> {
            boolean success = dbHelper.deleteRequester(user.getEmail());

            if (success) {
                requesters = dbHelper.getRequesters(); // Mise à jour uniquement avec les Requesters
                updateUserList();
                Toast.makeText(this, "Utilisateur supprimé avec succès.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Erreur lors de la suppression.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNeutralButton("Annuler", null);
        builder.create().show();
    }

    // Méthode pour vider la base de données
    private void clearDatabase() {
        SQLiteDatabase db = null;
        try {
            db = dbHelper.getWritableDatabase();
            db.delete(DatabaseHelper.TABLE_REQUESTERS, DatabaseHelper.COLUMN_ROLE + " = ?", new String[]{"Requester"}); // Supprime uniquement les Requesters
            Toast.makeText(this, "Base de données vidée avec succès.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Erreur lors de la suppression : " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            if (db != null) {
                db.close();
            }
        }
        requesters.clear();
        updateUserList();
    }

    private void resetDatabase() {
        clearDatabase(); // Supprime tous les utilisateurs Requester
        dbHelper.addRequester("Request", "User", "request@example.com", "request123", "Requester", true);

        requesters = dbHelper.getRequesters(); // Recharge uniquement les Requesters
        updateUserList(); // Met à jour la liste dans l'interface utilisateur

        Toast.makeText(this, "Base de données réinitialisée.", Toast.LENGTH_SHORT).show();
    }

    // Méthode pour réinitialiser le stock
    private void resetStock() {
        SQLiteDatabase db = null;
        try {
            db = dbHelper.getWritableDatabase();
            db.delete(DatabaseHelper.TABLE_STOCK, null, null);
            Toast.makeText(this, "Stock réinitialisé.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Erreur lors de la réinitialisation du stock : " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }
}
