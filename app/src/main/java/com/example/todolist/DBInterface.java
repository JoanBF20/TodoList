package com.example.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.ByteArrayOutputStream;

public class DBInterface {
    //Declaració de constants
    public static final String CLAU_TASK_ID = "_id";
    public static final String CLAU_TASK_TITOL = "titol";
    public static final String CLAU_TAK_DESCRIPCIO = "descripcio";
    public static final String CLAU_TASK_CATEGORIA = "id_categoria";

    public static final String CLAU_CAT_ID = "_id";
    public static final String CLAU_CAT_TITOL = "titol";
    public static final String CLAU_CAT_IMATGE = "imatge";

    public static final String TAG = "DBInterface";

    public static final String BD_NOM = "BDTasques";

    public static final String BD_TASK_TAULA = "tasques";

    public static final String BD_CAT_TAULA = "categories";

    public static final int VERSIO = 1;

    public static final String BD_TASK_CREATE = "create table " +BD_TASK_TAULA +
            "( " + CLAU_TASK_ID + " integer primary key autoincrement, " +
            CLAU_TASK_TITOL + " TEXT NOT NULL, " +
            CLAU_TAK_DESCRIPCIO + " TEXT, " +
            CLAU_TASK_CATEGORIA + " integer, " +
            "FOREIGN KEY ("+CLAU_TASK_CATEGORIA+") REFERENCES "+BD_CAT_TAULA+"("+CLAU_CAT_ID+")" + ");";

    public static final String BD_CAT_CREATE = "create table " +BD_CAT_TAULA +
            "( " + CLAU_CAT_ID + " integer primary key autoincrement, " +
            CLAU_CAT_TITOL + " TEXT NOT NULL, " +
            CLAU_CAT_IMATGE + " BLOB);";

    private final Context context;
    private AjudaDB  ajuda;
    private SQLiteDatabase bd;

    //Constructor, crea un nou objecte AjudaDB (que ens servira per donar suport a la BBDD)
    public DBInterface(Context con) {
        this.context = con;
        ajuda = new AjudaDB(context);
    }

    public com.example.todolist.DBInterface obre() throws SQLException {
        bd = ajuda.getWritableDatabase();
        return this;
    }

    public void tanca(){
        ajuda.close();
    }

    public long insereixTasca(String titol, String descripcio, int categoria) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(CLAU_TASK_TITOL, titol);
        initialValues.put(CLAU_TAK_DESCRIPCIO, descripcio);
        initialValues.put(CLAU_TASK_CATEGORIA, categoria);
        return bd.insert(BD_TASK_TAULA ,null, initialValues);
    }

    public long insereixCategoria(String titol, Bitmap imatge) {
        ByteArrayOutputStream objectByteArrayOutputStream = new ByteArrayOutputStream();
        imatge.compress(Bitmap.CompressFormat.JPEG,100,objectByteArrayOutputStream);
        byte[] imageInBytes=objectByteArrayOutputStream.toByteArray();
        ContentValues initialValues = new ContentValues();
        initialValues.put(CLAU_CAT_TITOL, titol);
        initialValues.put(CLAU_CAT_IMATGE, imageInBytes);
        return bd.insert(BD_CAT_TAULA ,null, initialValues);
    }

    public boolean esborraCategoria(long IDFila) {
        return bd.delete(BD_CAT_TAULA, CLAU_CAT_ID + " = " + IDFila, null) > 0;
    }

    public boolean esborraTasca(long IDFila) {
        return bd.delete(BD_TASK_TAULA, CLAU_TASK_ID + " = " + IDFila, null) > 0;
    }

    public Cursor obtenirCategoria(long IDFila) throws SQLException {
        Cursor mCursor = bd.query(true, BD_CAT_TAULA, new String[] {CLAU_CAT_ID, CLAU_CAT_TITOL,CLAU_CAT_IMATGE},CLAU_CAT_ID + " = " + IDFila, null, null, null, null, null);
        if(mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor obtenirTasca(long IDFila) throws SQLException {
        Cursor mCursor = bd.query(true, BD_TASK_TAULA, new String[] {CLAU_TASK_ID, CLAU_TASK_TITOL,CLAU_TAK_DESCRIPCIO,CLAU_TASK_CATEGORIA},CLAU_TASK_ID + " = " + IDFila, null, null, null, null, null);
        if(mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor obtenirTotesLesCategories() {
        return bd.query(BD_CAT_TAULA, new String[] {CLAU_CAT_ID, CLAU_CAT_TITOL,CLAU_CAT_IMATGE}, null,null, null, null, null);
    }

    public Cursor obtenirTotesLesTasques() {
        return bd.query(BD_TASK_TAULA, new String[] {CLAU_TASK_ID, CLAU_TASK_TITOL,CLAU_TAK_DESCRIPCIO,CLAU_TASK_CATEGORIA}, null,null, null, null, null);
    }

    public boolean actualitzarCategoria(long IDFila, String titol, Bitmap imatge) {
        ByteArrayOutputStream objectByteArrayOutputStream = new ByteArrayOutputStream();
        imatge.compress(Bitmap.CompressFormat.JPEG,100,objectByteArrayOutputStream);
        byte[] imageInBytes=objectByteArrayOutputStream.toByteArray();
        ContentValues args = new ContentValues();
        args.put(CLAU_CAT_TITOL, titol);
        args.put(CLAU_CAT_IMATGE, imageInBytes);
        return bd.update(BD_CAT_TAULA, args, CLAU_CAT_ID + " = " + IDFila, null) > 0;
    }

    public boolean actualitzarTasca(long IDFila, String titol, String descripcio, int categoria) {
        ContentValues args = new ContentValues();
        args.put(CLAU_TASK_TITOL, titol);
        args.put(CLAU_TAK_DESCRIPCIO, descripcio);
        args.put(CLAU_TASK_CATEGORIA, categoria);
        return bd.update(BD_TASK_TAULA, args, CLAU_TASK_ID + " = " + IDFila, null) > 0;
    }

    private class AjudaDB extends SQLiteOpenHelper {

        public AjudaDB(Context contexte){
            super(contexte, BD_NOM, null, VERSIO);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(BD_TASK_CREATE);
                db.execSQL(BD_CAT_CREATE);
            } catch (SQLException e){
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Actualitzant base da dades de la versio "+oldVersion+" a "+newVersion+". Destruira totes les dades");
            db.execSQL("DROP TABLE IF EXISTS "+ BD_TASK_TAULA);
            db.execSQL("DROP TABLE IF EXISTS "+ BD_CAT_TAULA);
            onCreate(db);
        }
    }
}

