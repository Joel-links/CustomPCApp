package com.example.perfectpc;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class CommandeDatabase {

    private SQLiteDatabase database;

    public CommandeDatabase(Context context) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        this.database = dbHelper.getWritableDatabase();
    }

    // Création d'une commande
    public void createCommande(Commande commande) {
        try {
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_COMMANDE_ID, commande.getId());
            values.put(DatabaseHelper.COLUMN_STOCKITEM_ID, commande.getStockItemId());
            values.put(DatabaseHelper.COLUMN_CREATED_DATE, commande.getCreatedDate());
            values.put(DatabaseHelper.COLUMN_UPDATED_DATE, commande.getUpdatedDate());
            values.put(DatabaseHelper.COLUMN_STATUS, "Pending"); // Assurez-vous d'initialiser le statut

            long result = database.insert(DatabaseHelper.TABLE_COMMANDE, null, values);
            if (result == -1) {
                Log.e("CommandeDatabase", "Échec de la création de la commande : " + commande.getId());
            } else {
                Log.d("CommandeDatabase", "Commande créée avec succès : " + commande.getId());
            }
        } catch (Exception e) {
            Log.e("CommandeDatabase", "Erreur lors de la création de la commande : " + e.getMessage(), e);
        }
    }

    // Récupération de toutes les commandes
    public List<Commande> getAllCommandes() {
        List<Commande> commandes = new ArrayList<>();
        try (Cursor cursor = database.query(
                DatabaseHelper.TABLE_COMMANDE,
                null, null, null, null, null, null
        )) {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Commande commande = new Commande(
                            cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_COMMANDE_ID)),
                            cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_STOCKITEM_ID)),
                            cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CREATED_DATE)),
                            cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_UPDATED_DATE))
                    );
                    commandes.add(commande);
                } while (cursor.moveToNext());
                Log.d("CommandeDatabase", "Commandes récupérées : " + commandes.size());
            }
        } catch (Exception e) {
            Log.e("CommandeDatabase", "Erreur lors de la récupération des commandes : " + e.getMessage(), e);
        }
        return commandes;
    }

    // Suppression d'une commande
    public void deleteCommande(String commandeId) {
        try {
            int rowsDeleted = database.delete(DatabaseHelper.TABLE_COMMANDE, DatabaseHelper.COLUMN_COMMANDE_ID + " = ?", new String[]{commandeId});
            if (rowsDeleted == 0) {
                Log.e("CommandeDatabase", "Aucune commande supprimée pour l'ID : " + commandeId);
            } else {
                Log.d("CommandeDatabase", "Commande supprimée avec succès pour l'ID : " + commandeId);
            }
        } catch (Exception e) {
            Log.e("CommandeDatabase", "Erreur lors de la suppression de la commande : " + e.getMessage(), e);
        }
    }

    // Mise à jour du statut d'une commande
    public void updateCommandeStatus(String commandeId, String status) {
        try {
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_STATUS, status);
            values.put(DatabaseHelper.COLUMN_UPDATED_DATE, System.currentTimeMillis());

            int rowsUpdated = database.update(DatabaseHelper.TABLE_COMMANDE, values,
                    DatabaseHelper.COLUMN_COMMANDE_ID + " = ?",
                    new String[]{commandeId});

            if (rowsUpdated == 0) {
                Log.e("CommandeDatabase", "Aucune commande mise à jour pour l'ID : " + commandeId);
            } else {
                Log.d("CommandeDatabase", "Statut mis à jour avec succès pour l'ID : " + commandeId + ", Nouveau statut : " + status);
            }
        } catch (Exception e) {
            Log.e("CommandeDatabase", "Erreur lors de la mise à jour du statut : " + e.getMessage(), e);
        }
    }

    // Récupération du statut d'une commande
    public String getCommandeStatus(String commandeId) {
        String status = null;
        try (Cursor cursor = database.query(
                DatabaseHelper.TABLE_COMMANDE,
                new String[]{DatabaseHelper.COLUMN_STATUS},
                DatabaseHelper.COLUMN_COMMANDE_ID + " = ?",
                new String[]{commandeId},
                null, null, null
        )) {
            if (cursor != null && cursor.moveToFirst()) {
                status = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_STATUS));
                Log.d("CommandeDatabase", "Statut récupéré pour l'ID : " + commandeId + " -> " + status);
            } else {
                Log.e("CommandeDatabase", "Aucun statut trouvé pour l'ID : " + commandeId);
            }
        } catch (Exception e) {
            Log.e("CommandeDatabase", "Erreur lors de la récupération du statut : " + e.getMessage(), e);
        }
        return status;
    }

    // Récupération d'une commande par son ID
    public Commande getCommandeById(String commandeId) {
        Commande commande = null;
        try (Cursor cursor = database.query(
                DatabaseHelper.TABLE_COMMANDE,
                null,
                DatabaseHelper.COLUMN_COMMANDE_ID + " = ?",
                new String[]{commandeId},
                null, null, null
        )) {
            if (cursor != null && cursor.moveToFirst()) {
                commande = new Commande(
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_COMMANDE_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_STOCKITEM_ID)),
                        cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CREATED_DATE)),
                        cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_UPDATED_DATE))
                );
                Log.d("CommandeDatabase", "Commande récupérée pour l'ID : " + commandeId);
            } else {
                Log.e("CommandeDatabase", "Aucune commande trouvée pour l'ID : " + commandeId);
            }
        } catch (Exception e) {
            Log.e("CommandeDatabase", "Erreur lors de la récupération de la commande : " + e.getMessage(), e);
        }
        return commande;
    }

    // Fermeture de la base de données
    public void close() {
        try {
            database.close();
            Log.d("CommandeDatabase", "Base de données fermée avec succès.");
        } catch (Exception e) {
            Log.e("CommandeDatabase", "Erreur lors de la fermeture de la base de données : " + e.getMessage(), e);
        }
    }
}
