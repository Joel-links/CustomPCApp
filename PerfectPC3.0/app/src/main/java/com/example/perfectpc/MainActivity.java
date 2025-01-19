package com.example.perfectpc;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialisation de la base de données
        databaseHelper = new DatabaseHelper(this);

        // Ajouter les utilisateurs par défaut dans la base de données s'ils n'existent pas
        initializeDefaultUsers();

        // Références aux champs
        EditText emailInput = findViewById(R.id.email);
        EditText passwordInput = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.login_button);

        // Gestion du clic sur le bouton de connexion
        loginButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            // Authentification via DatabaseHelper
            User authenticatedUser = databaseHelper.findRequesterByEmailAndPassword(email, password);

            if (authenticatedUser != null) {
                Toast.makeText(MainActivity.this, "Connexion réussie en tant que " + authenticatedUser.getRole(), Toast.LENGTH_SHORT).show();
                // Rediriger selon le rôle
                redirectUser(authenticatedUser.getRole());
            } else {
                Toast.makeText(MainActivity.this, "E-mail ou mot de passe incorrect", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Initialiser les utilisateurs par défaut
    private void initializeDefaultUsers() {
        if (!databaseHelper.userExists("admin@example.com")) {
            databaseHelper.addRequester("Admin", "One", "admin@example.com", "admin123", "Administrator", true);
        }
        if (!databaseHelper.userExists("store@example.com")) {
            databaseHelper.addRequester("Store", "Keeper", "store@example.com", "store123", "StoreKeeper", true);
        }
        if (!databaseHelper.userExists("assembler@example.com")) {
            databaseHelper.addRequester("Assembler", "One", "assembler@example.com", "assemble123", "Assembler", true);
        }
        if (!databaseHelper.userExists("request@example.com")) {
            databaseHelper.addRequester("Request", "User", "request@example.com", "request123", "Requester", true);
        }
    }
    // Rediriger selon le rôle
    private void redirectUser(String role) {
        if (role.equals("Administrator")) {
            Intent intent = new Intent(this, AdminActivity.class);
            startActivity(intent);
        } else if (role.equals("StoreKeeper")) {
            Intent intent = new Intent(this, StoreKeeperActivity.class);
            startActivity(intent);
        } else if (role.equals("Assembler")) {
            Intent intent = new Intent(this, AssemblerActivity.class);
            startActivity(intent);
        } else if (role.equals("Requester")) {
            Intent intent = new Intent(this, RequesterActivity.class);
            startActivity(intent);
        }
    }
}
