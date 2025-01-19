    package com.example.perfectpc;

    import android.content.ContentValues;
    import android.content.Context;
    import android.database.Cursor;
    import android.database.SQLException;
    import android.database.sqlite.SQLiteDatabase;
    import android.database.sqlite.SQLiteOpenHelper;

    import java.util.ArrayList;
    import java.util.List;

    public class DatabaseHelper extends SQLiteOpenHelper {

        // Nom et version de la base de données
        private static final String DATABASE_NAME = "perfectpc.db";
        private static final int DATABASE_VERSION = 4; // Version mis à jour pour les commandes

        // Table pour les Requesters
        public static final String TABLE_REQUESTERS = "requesters";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_FIRST_NAME = "first_name";
        public static final String COLUMN_LAST_NAME = "last_name";
        public static final String COLUMN_EMAIL = "email";
        public static final String COLUMN_PASSWORD = "password";
        public static final String COLUMN_ROLE = "role";
        public static final String COLUMN_IS_DEFAULT = "is_default"; // Nouvelle colonne

        // Table pour les éléments de stock
        public static final String TABLE_STOCK = "stock";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_SUB_TYPE = "sub_type";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_COMMENT = "comment";

        //Table pour les commandes
        public static final String TABLE_COMMANDE = "commandes";
        public static final String COLUMN_COMMANDE_ID = "Id";
        public static final String COLUMN_STOCKITEM_ID = "stockItemId";
        public static final String COLUMN_CREATED_DATE = "createdDate";
        public static final String COLUMN_UPDATED_DATE = "updatedDate";
        public static final String COLUMN_STATUS = "status";
        private static final String CREATE_COMMANDE_TABLE = "CREATE TABLE " + TABLE_COMMANDE + " (" +
                COLUMN_COMMANDE_ID + " TEXT PRIMARY KEY, " +
                COLUMN_STOCKITEM_ID + " TEXT, " +
                COLUMN_CREATED_DATE + " INTEGER, " +
                COLUMN_UPDATED_DATE + " INTEGER," +
                COLUMN_STATUS + " TEXT DEFAULT 'Pending'" + // Ajout de la colonne status
                ");";

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // Création de la table des Requesters
            String CREATE_REQUESTERS_TABLE = "CREATE TABLE " + TABLE_REQUESTERS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_FIRST_NAME + " TEXT, " +
                    COLUMN_LAST_NAME + " TEXT, " +
                    COLUMN_EMAIL + " TEXT UNIQUE, " +
                    COLUMN_PASSWORD + " TEXT, " +
                    COLUMN_ROLE + " TEXT, " +
                    COLUMN_IS_DEFAULT + " INTEGER DEFAULT 0," + // Nouvelle colonne
                    COLUMN_STATUS + " TEXT DEFAULT 'Pending' " + // Défaut : 'Pending'
                    ");";

            // Création de la table des éléments de stock
            String CREATE_STOCK_TABLE = "CREATE TABLE " + TABLE_STOCK + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TITLE + " TEXT UNIQUE, " +
                    COLUMN_TYPE + " TEXT, " +
                    COLUMN_SUB_TYPE + " TEXT, " +
                    COLUMN_QUANTITY + " INTEGER," +
                    COLUMN_COMMENT + " TEXT " +");";



            // Exécution des commandes SQL
            db.execSQL(CREATE_REQUESTERS_TABLE);
            db.execSQL(CREATE_STOCK_TABLE);
            db.execSQL(CREATE_COMMANDE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if (oldVersion < 4) {
                // Vérifiez et ajoutez la colonne "status" à la table commandes si elle n'existe pas
                try {
                    db.execSQL("ALTER TABLE " + TABLE_COMMANDE + " ADD COLUMN " + COLUMN_STATUS + " TEXT DEFAULT 'Pending'");
                } catch (SQLException e) {
                    // Si la colonne existe déjà, on ignore l'erreur
                    e.printStackTrace();
                }
            }

            if (oldVersion < 3) {
                // Ajoutez ici d'autres mises à jour pour les versions intermédiaires (si nécessaire)
            }

            if (oldVersion < 2) {
                // Exemple : recréez la table stock uniquement si elle est obsolète
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_STOCK);

                String CREATE_STOCK_TABLE = "CREATE TABLE " + TABLE_STOCK + " (" +
                        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_TITLE + " TEXT UNIQUE, " +
                        COLUMN_TYPE + " TEXT, " +
                        COLUMN_SUB_TYPE + " TEXT, " +
                        COLUMN_QUANTITY + " INTEGER, " +
                        COLUMN_COMMENT + " TEXT" +
                        ");";
                db.execSQL(CREATE_STOCK_TABLE);
            }
        }


        // Méthode pour ajouter un nouvel utilisateur Requester
        public boolean addRequester(String firstName, String lastName, String email, String password, String role, boolean isDefault) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_FIRST_NAME, firstName);
            values.put(COLUMN_LAST_NAME, lastName);
            values.put(COLUMN_EMAIL, email);
            values.put(COLUMN_PASSWORD, password);
            values.put(COLUMN_ROLE, role);
            values.put(COLUMN_IS_DEFAULT, isDefault ? 1 : 0);

            long result = db.insert(TABLE_REQUESTERS, null, values);
            db.close();
            return result != -1; // Retourne true si l'insertion a réussi
        }

        // Méthode pour mettre à jour un utilisateur Requester
        public boolean updateRequester(String email, String firstName, String lastName, String password) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_FIRST_NAME, firstName);
            values.put(COLUMN_LAST_NAME, lastName);
            values.put(COLUMN_PASSWORD, password);

            int result = db.update(TABLE_REQUESTERS, values, COLUMN_EMAIL + "=?", new String[]{email});
            db.close();
            return result > 0; // Retourne true si la mise à jour a réussi
        }

        // Méthode pour supprimer un utilisateur Requester
        public boolean deleteRequester(String email) {
            SQLiteDatabase db = this.getWritableDatabase();
            int result = db.delete(TABLE_REQUESTERS, COLUMN_EMAIL + "=?", new String[]{email});
            db.close();
            return result > 0; // Retourne true si la suppression a réussi
        }

        // Méthode pour vérifier si la base de données est vide
        public boolean isDatabaseEmpty() {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_REQUESTERS, null);
            cursor.moveToFirst();
            int count = cursor.getInt(0);
            cursor.close();
            return count == 0;
        }

        public boolean userExists(String email) {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.query(TABLE_REQUESTERS,
                    new String[]{COLUMN_EMAIL},
                    COLUMN_EMAIL + "=?",
                    new String[]{email},
                    null, null, null);

            boolean exists = (cursor != null && cursor.getCount() > 0);
            if (cursor != null) cursor.close();
            return exists;
        }

        // Méthode pour authentifier un utilisateur
        public User findRequesterByEmailAndPassword(String email, String password) {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.query(TABLE_REQUESTERS,
                    new String[]{COLUMN_FIRST_NAME, COLUMN_LAST_NAME, COLUMN_EMAIL, COLUMN_PASSWORD, COLUMN_ROLE},
                    COLUMN_EMAIL + "=? AND " + COLUMN_PASSWORD + "=?",
                    new String[]{email, password},
                    null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                User user = new User(
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FIRST_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LAST_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROLE))
                );
                cursor.close();
                return user;
            }
            if (cursor != null) cursor.close();
            return null; // Retourne null si l'utilisateur n'est pas trouvé
        }


        // Méthode pour obtenir uniquement les utilisateurs avec le rôle "Requester"
        public List<User> getRequesters() {
            List<User> requesters = new ArrayList<>();
            SQLiteDatabase db = this.getReadableDatabase();

            // Requête SQL pour sélectionner les utilisateurs ayant le rôle "Requester"
            String query = "SELECT * FROM " + TABLE_REQUESTERS + " WHERE " + COLUMN_ROLE + " = ?";
            Cursor cursor = db.rawQuery(query, new String[]{"Requester"});

            if (cursor.moveToFirst()) {
                do {
                    User user = new User(
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FIRST_NAME)),
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LAST_NAME)),
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)),
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD)),
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ROLE))
                    );
                    requesters.add(user);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return requesters;
        }



        // Méthode pour ajouter un élément au stock
        public boolean addComponent(String title,String type, String subtype, int quantity, String comment) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_TITLE, title);
            values.put(COLUMN_TYPE, type);
            values.put(COLUMN_SUB_TYPE, subtype);
            values.put(COLUMN_QUANTITY, quantity);
            values.put(COLUMN_COMMENT, comment);

            long result = db.insert(TABLE_STOCK, null, values);
            db.close();
            return result != -1; // Retourne true si l'insertion a réussi
        }

        public void insertComponent(Component component) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_TITLE, component.getTitle());
            values.put(COLUMN_TYPE, component.getType());
            values.put(COLUMN_SUB_TYPE, component.getSubType());
            values.put(COLUMN_QUANTITY, component.getQuantity());
            values.put(COLUMN_COMMENT, component.getComment());
            db.insert(TABLE_STOCK, null, values);
            db.close();
        }

        // Méthode pour obtenir tous les éléments de stock
        public List<Component> getAllComponents() {
            List<Component> components = new ArrayList<>();
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "SELECT * FROM " + TABLE_STOCK;
            Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    Component component = new Component(
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TYPE)),
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SUB_TYPE)),
                            cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUANTITY)),
                            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COMMENT))
                    );
                    components.add(component);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            return components;
        }

        // Méthode pour supprimer un élément de stock
        public boolean deleteComponent(String title) {
            SQLiteDatabase db = this.getWritableDatabase();
            int result = db.delete(TABLE_STOCK, COLUMN_TITLE + "=?", new String[]{title});
            db.close();
            return result > 0; // Retourne true si la suppression a réussi
        }

        // Méthode pour mettre à jour un élément de stock
        public boolean updateComponent(String itemName, int quantity) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_QUANTITY, quantity);

            int result = db.update(TABLE_STOCK, values, COLUMN_TITLE + "=?", new String[]{itemName});
            db.close();
            return result > 0; // Retourne true si la mise à jour a réussi
        }

        public boolean updateComponentQuantity(String title, int newQuantity) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_QUANTITY, newQuantity);

            int rowsUpdated = db.update(TABLE_STOCK, values, COLUMN_TITLE + "= ?", new String[]{title});
            return rowsUpdated > 0;
        }

        // Méthode pour mettre à jour un élément dans la base de données
        public boolean updateComponent(Component component) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_TITLE, component.getTitle());
            values.put(COLUMN_TYPE, component.getType());
            values.put(COLUMN_SUB_TYPE, component.getSubType());
            values.put(COLUMN_QUANTITY, component.getQuantity());
            values.put(COLUMN_COMMENT, component.getComment());

            int rowsUpdated = db.update(TABLE_STOCK, values, COLUMN_TITLE + " = ?", new String[]{component.getTitle()});
            db.close();
            return rowsUpdated > 0; // Retourne true si la mise à jour a réussi
        }


    }