package curso.mpgo.com.cursoandroid.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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

    }

    public List<Posicao> read(){

    }

    public void update(Posicao posicao){

    }

    public void delete(Posicao posicao){

    }
}
