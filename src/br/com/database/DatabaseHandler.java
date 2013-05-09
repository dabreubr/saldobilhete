package br.com.database;

import java.util.ArrayList;
import java.util.List;

import br.com.entities.Cartao;
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
    private static final String TABLE_CARTOES = "cartoes";
 
    // transações Table Columns names
    private static final String KEY_ID = "id";
    private static final String TRANS_DATA = "data";
    private static final String TRANS_TIPO = "tipo";
    private static final String TRANS_VALOR = "valor";
    private static final String TRANS_DEBITO_CREDITO = "debito_credito";
    private static final String TRANS_CARTAO = "cartao";
    // Cartoes Table Columns names
    private static final String CARTOES_SALDO = "saldo";
    
    // create table transações
    private static final String CREATE_TRANSACTIONS_TABLE = "CREATE TABLE " + TABLE_TRANSACTIONS + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + TRANS_DATA + " STRING,"
            + TRANS_TIPO + " INTEGER," + TRANS_VALOR + " FLOAT," 
            + TRANS_DEBITO_CREDITO + " CHAR(1)," + TRANS_CARTAO + " INT" + ")";
    private static final String CREATE_CARTAO_TABLE = "CREATE TABLE " + TABLE_CARTOES + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + CARTOES_SALDO + " FLOAT" + ")";
    private static final String INSERT_CARTAO = "INSERT OR IGNORE INTO " + TABLE_CARTOES + "(" + CARTOES_SALDO + ") VALUES (0)";
 
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TRANSACTIONS_TABLE);
        db.execSQL(CREATE_CARTAO_TABLE);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // create table if not exists
        db.execSQL("IF NOT EXISTS " + TABLE_TRANSACTIONS + " BEGIN" + CREATE_TRANSACTIONS_TABLE + " END");
        db.execSQL("IF NOT EXISTS " + TABLE_CARTOES + " BEGIN" + CREATE_CARTAO_TABLE + " END");
    }
    
    @Override
    public void onOpen(SQLiteDatabase db) {
    	//db.execSQL("DROP TABLE IF EXISTS " + TABLE_CARTOES);
    	//db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTIONS);        
        //onCreate(db);
        db.execSQL(INSERT_CARTAO);
    }
   
    // Adding new transaction
    public void addTransacao(Transacao transacao) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(TRANS_DATA, transacao.getData()); 
        values.put(TRANS_TIPO, transacao.getTipo());
        values.put(TRANS_VALOR, transacao.getValor());
        values.put(TRANS_DEBITO_CREDITO, transacao.getDebitoCredito());
        values.put(TRANS_CARTAO, transacao.getCartao().getId());
 
        // Inserting Row
        db.insert(TABLE_TRANSACTIONS, null, values);
        db.close(); // Closing database connection
    }
    
    public int updateSaldo(Cartao cartao) {
        SQLiteDatabase db = this.getWritableDatabase();
     
        ContentValues values = new ContentValues();
        values.put(CARTOES_SALDO, cartao.getSaldo());
     
        // updating row
        return db.update(TABLE_CARTOES, values, KEY_ID + " = ?",
                new String[] { String.valueOf(cartao.getId()) });
    }
    
    // Getting All Transactions
    public List<Transacao> getAllTransactions() {
        List<Transacao> transacaoList = new ArrayList<Transacao>();
        // Select All Query
        String selectQuery = "SELECT " + KEY_ID + ", " + TRANS_DATA + ", " + TRANS_TIPO + ", " + 
        TRANS_VALOR + ", " + TRANS_DEBITO_CREDITO + ", " + TRANS_CARTAO + " FROM " + TABLE_TRANSACTIONS;
 
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
            	transacao.setDebitoCredito(cursor.getString(4));
            	transacao.setCartao(getCartao(cursor.getInt(5)));
                // Adding to list
            	transacaoList.add(transacao);
            } while (cursor.moveToNext());
        }
 
        // return list
        return transacaoList;
    }
    
    public Cartao getCartao(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
     
        Cursor cursor = db.query(TABLE_CARTOES, new String[] { KEY_ID,
                CARTOES_SALDO }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
     
        Cartao cartao = new Cartao();
        cartao.setId(Integer.parseInt(cursor.getString(0)));
        cartao.setSaldo(cursor.getFloat(1));
        // return cartao
        return cartao;
    }
    
    public void limparDados() {
    	SQLiteDatabase db = this.getWritableDatabase();
    	db.delete(TABLE_CARTOES, null, null);
    	db.delete(TABLE_TRANSACTIONS, null, null);
    	onOpen(db);
    }
}
