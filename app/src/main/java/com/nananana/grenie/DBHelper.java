package com.nananana.grenie;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static SQLiteDatabase sqliteDb;

    private static DBHelper instance;

    private static final int DATABASE_VERSION = 1;

    public static final String database_name = "db_tanaman";
    public static final String table_name = "tabel_tanaman";

    public static final String row_id = "_id";
    public static final String row_gambar = "Gambar";
    public static final String row_nama = "Nama";
    public static final String row_deskripsi = "Deskripsi";
    public static final String row_temperatur = "Temperatur";
    public static final String row_cara = "Cara";
    public static final String row_jenis = "Jenis";

    private SQLiteDatabase db;

    public DBHelper(Context context) {
        super(context, database_name, null, 2);
        db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + table_name + "(" + row_id + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + row_gambar + " TEXT, " + row_nama + " TEXT, " + row_deskripsi + " TEXT, "
                + row_temperatur + " TEXT, " + row_cara + " TEXT, " + row_jenis + " TEXT)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int x) {
        db.execSQL("DROP TABLE IF EXISTS " + table_name);
    }

    //Get All SQLite Data
    public Cursor allData(){
        Cursor cur = db.rawQuery("SELECT * FROM " + table_name, null);
        return cur;
    }

    //Get 1 Data By ID
    public Cursor oneData(Long id){
        Cursor cur = db.rawQuery("SELECT * FROM " + table_name + " WHERE " + row_id + "=" + id, null);
        return cur;
    }

    //Insert Data to Database
    public void insertData(ContentValues values){
        db.insert(table_name, null, values);
    }

    //Update Data
    public void updateData(ContentValues values, long id){
        db.update(table_name, values, row_id + "=" + id, null);
    }

    //Delete Data
    public void deleteData(long id){
        db.delete(table_name, row_id + "=" + id, null);
    }
}

