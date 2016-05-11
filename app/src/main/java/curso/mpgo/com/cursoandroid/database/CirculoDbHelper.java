package curso.mpgo.com.cursoandroid.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import curso.mpgo.com.cursoandroid.Circulo;
import curso.mpgo.com.cursoandroid.Circulo;

/**
 * Created by ricardoogliari on 5/11/16.
 */
public class CirculoDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;

    public CirculoDbHelper(Context ctx){
        super(ctx, CirculoContract.TABLE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CirculoContract.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    public void create(Circulo circulo){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(CirculoContract.COLUMN_NAME_RADIUS, circulo.raio);
        cv.put(CirculoContract.COLUMN_NAME_LAT, circulo.latitude);
        cv.put(CirculoContract.COLUMN_NAME_LNG, circulo.longitude);

        db.insert(CirculoContract.TABLE_NAME, null, cv);

        db.close();
    }

    public void fullCreate(List<Circulo> posicoes){
        SQLiteDatabase db = getWritableDatabase();

        for (Circulo circulo : posicoes) {
            ContentValues cv = new ContentValues();
            cv.put(CirculoContract.COLUMN_NAME_RADIUS, circulo.raio);
            cv.put(CirculoContract.COLUMN_NAME_LAT, circulo.latitude);
            cv.put(CirculoContract.COLUMN_NAME_LNG, circulo.longitude);

            db.insert(CirculoContract.TABLE_NAME, null, cv);
        }

        db.close();
    }

    public List<Circulo> read(){
        SQLiteDatabase db = getReadableDatabase();
        List<Circulo> circulos = new ArrayList<>();

        Cursor cursor = db.query(
                CirculoContract.TABLE_NAME,
                null,//campos (null retorna todos os campos)
                null,//where
                null,//argumentos do where
                null,//group by
                null,//having
                null//order by
        );

        while (cursor.moveToNext()){
            Circulo circulo = new Circulo();
            circulo.id = cursor.getInt(cursor.getColumnIndex(CirculoContract.COLUMN_NAME_ENTRY_ID));
            circulo.raio = cursor.getInt(cursor.getColumnIndex(CirculoContract.COLUMN_NAME_RADIUS));
            circulo.latitude = cursor.getDouble(cursor.getColumnIndex(CirculoContract.COLUMN_NAME_LAT));
            circulo.longitude = cursor.getDouble(cursor.getColumnIndex(CirculoContract.COLUMN_NAME_LNG));
            circulos.add(circulo);
        }

        db.close();
        return circulos;
    }

    public void update(Circulo circulo){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(CirculoContract.COLUMN_NAME_RADIUS, circulo.raio);
        cv.put(CirculoContract.COLUMN_NAME_LAT, circulo.latitude);
        cv.put(CirculoContract.COLUMN_NAME_LNG, circulo.longitude);

        db.update(
                CirculoContract.TABLE_NAME,
                cv,
                CirculoContract.COLUMN_NAME_ENTRY_ID + " = ?",
                new String[]{"" + circulo.id});

        db.close();
    }

    public void delete(Circulo circulo){
        SQLiteDatabase db = getWritableDatabase();

        db.delete(CirculoContract.TABLE_NAME,
                CirculoContract.COLUMN_NAME_ENTRY_ID + " = ?",
                new String[]{"" + circulo.id});

        db.close();
    }

    public void clear(){
        SQLiteDatabase db = getWritableDatabase();

        db.delete(CirculoContract.TABLE_NAME,
                null,
                null);

        db.close();
    }
}
