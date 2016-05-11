package curso.mpgo.com.cursoandroid.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import curso.mpgo.com.cursoandroid.Posicao;

/**
 * Created by ricardoogliari on 5/11/16.
 */
public class PosicaoDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;

    public PosicaoDbHelper(Context ctx){
        super(ctx, PosicaoContract.TABLE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //PASSA NA PRIMEIRA QUE O APLICATIVO FOR EXECUTADO
        //CRIA AS TABELAS
        //DUMP
        db.execSQL(PosicaoContract.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //MUDA A VERSAO DO BANCO DE DADOS PARA CIMA
        //adicionar novas tabelas
        //refresh
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //nova versão da APP COM UMA VERS˜AO DO REDUZIDA DO BD
    }

    public void create(Posicao posicao){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(PosicaoContract.COLUMN_NAME_NAME, posicao.name);
        cv.put(PosicaoContract.COLUMN_NAME_LAT, posicao.latitude);
        cv.put(PosicaoContract.COLUMN_NAME_LNG, posicao.longitude);

        db.insert(PosicaoContract.TABLE_NAME, null, cv);

        db.close();
    }

    public List<Posicao> read(){
        SQLiteDatabase db = getReadableDatabase();
        List<Posicao> posicoes = new ArrayList<>();

        Cursor cursor = db.query(
                PosicaoContract.TABLE_NAME,
                null,//campos (null retorna todos os campos)
                null,//where
                null,//argumentos do where
                null,//group by
                null,//having
                PosicaoContract.COLUMN_NAME_NAME//order by
        );

        while (cursor.moveToNext()){
            Posicao posicao = new Posicao();
            posicao.id = cursor.getInt(cursor.getColumnIndex(PosicaoContract.COLUMN_NAME_ENTRY_ID));
            posicao.name = cursor.getString(cursor.getColumnIndex(PosicaoContract.COLUMN_NAME_NAME));
            posicao.latitude = cursor.getDouble(cursor.getColumnIndex(PosicaoContract.COLUMN_NAME_LAT));
            posicao.longitude = cursor.getDouble(cursor.getColumnIndex(PosicaoContract.COLUMN_NAME_LNG));
            posicoes.add(posicao);
        }

        db.close();
        return posicoes;
    }

    public void update(Posicao posicao){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(PosicaoContract.COLUMN_NAME_NAME, posicao.name);
        cv.put(PosicaoContract.COLUMN_NAME_LAT, posicao.latitude);
        cv.put(PosicaoContract.COLUMN_NAME_LNG, posicao.longitude);

        db.update(
                PosicaoContract.TABLE_NAME,
                cv,
                PosicaoContract.COLUMN_NAME_ENTRY_ID + " = ?",
                new String[]{"" + posicao.id});

        db.close();
    }

    public void delete(Posicao posicao){
        SQLiteDatabase db = getWritableDatabase();

        db.delete(PosicaoContract.TABLE_NAME,
                PosicaoContract.COLUMN_NAME_ENTRY_ID + " = ?",
                new String[]{"" + posicao.id});

        db.close();
    }

    public void clear(){
        SQLiteDatabase db = getWritableDatabase();

        db.delete(PosicaoContract.TABLE_NAME,
                null,
                null);

        db.close();
    }
}
