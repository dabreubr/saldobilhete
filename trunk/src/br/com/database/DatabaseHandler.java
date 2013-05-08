package br.com.database;

import java.util.ArrayList;
import java.util.List;

import br.com.entities.Transacao;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

	// All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "saldoBilhete";
 
    // transações table name
    private static final String TABLE_TRANSACTIONS = "transacoes";
 
    // transações Table Columns names
    private static final String KEY_ID = "id";
    private static final String TRANS_DATA = "data";
    private static final String TRANS_TIPO = "tipo";
    private static final String TRANS_VALOR = "valor";
    
    // create table transações
    private static final String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_TRANSACTIONS + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + TRANS_DATA + " STRING,"
            + TRANS_TIPO + " INTEGER," + TRANS_VALOR + " FLOAT" + ")";
 
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CONTACTS_TABLE);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // create table if not exists
        db.execSQL("IF NOT EXISTS " + TABLE_TRANSACTIONS + " BEGIN" + CREATE_CONTACTS_TABLE + "END");
    }
    
    // Adding new transaction
    public void addTransacao(Transacao transacao) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(TRANS_DATA, transacao.getData()); 
        values.put(TRANS_TIPO, transacao.getTipo());
        values.put(TRANS_VALOR, transacao.getValor());
 
        // Inserting Row
        db.insert(TABLE_TRANSACTIONS, null, values);
        db.close(); // Closing database connection
    }
    
    // Getting All Transactions
    public List<Transacao> getAllTransactions() {
        List<Transacao> transacaoList = new ArrayList<Transacao>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TRANSACTIONS;
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
            	Transacao transacao = new Transacao();
            	transacao.setId(Integer.parseInt(cursor.getString(0)));
            	transacao.setData(cursor.getString(1));
            	transacao.setTipo(cursor.getInt(2));
            	transacao.setValor(cursor.getFloat(3));
                // Adding contact to list
            	transacaoList.add(transacao);
            } while (cursor.moveToNext());
        }
 
        // return contact list
        return transacaoList;
    }
}
