package com.example.a.leventyadirgasqlcalismaprojesi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by a on 12.05.2017.
 */

public class VeriTabani extends SQLiteOpenHelper{

    static final String VERITABANI_ISMI="veritabanim";
    static final int VERITABANI_VERSION=1;
    static final String    TABLO_ISMI="ders_takip_tablosu";

    static final String ID = "_id";
    static final String DERS = "ders";
    static final String SORUSAYISI = "soru_sayisi";
    static final String TARIH = "tarih";

    public VeriTabani(Context context) {
        super(context,VERITABANI_ISMI, null, VERITABANI_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {

        String tabloOlustur="CREATE TABLE " + TABLO_ISMI +
                " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DERS + " TEXT, " +
                SORUSAYISI + " INTEGER NOT NULL, " +
                TARIH + " INTEGER NOT NULL); ";
        db.execSQL(tabloOlustur);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLO_ISMI);
        onCreate(db);


    }

    public long kayitEkle(Ogrenci ogrenci) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DERS, ogrenci.getDers());
        cv.put(SORUSAYISI, ogrenci.getSoru());
        cv.put(TARIH, ogrenci.getTarih());

      long id =   db.insert(TABLO_ISMI, null , cv);
        db.close();
        return  id;
    }

    public List<Ogrenci> TumKayitlar() {
        SQLiteDatabase db = this.getReadableDatabase ();
        String [] sutunlar = new String[] {DERS, SORUSAYISI,TARIH, ID};

        Cursor c = db.query ( TABLO_ISMI,sutunlar, null,null, null, null, TARIH + " desc " );
        // Son Sutunda ki ifade sıralamaya yarıyor tarıh + " desc "

        int dersSiraNo = c.getColumnIndex ( DERS );
        int soruSayisiSiraNo = c.getColumnIndex ( SORUSAYISI );
        int tarihNo = c.getColumnIndex ( TARIH );
        int idSıraNo = c.getColumnIndex ( ID );

        List<Ogrenci> ogrenciLsit = new ArrayList<Ogrenci> (  );
        for (c.moveToFirst (); !c.isAfterLast (); c.moveToNext ()){
            Ogrenci ogrenci = new Ogrenci (  );
            ogrenci.setDers ( c.getString ( dersSiraNo ) );
            ogrenci.setSoru ( c.getInt ( soruSayisiSiraNo ) );
            ogrenci.setTarih ( c.getLong  ( tarihNo )  );
            ogrenci.setId ( c.getLong  ( idSıraNo )  );
            ogrenciLsit.add ( ogrenci );
        }
        db.close ();

        return ogrenciLsit;

    }

    public void Sil(long id) {

        SQLiteDatabase db = this.getWritableDatabase ();
        db.delete ( TABLO_ISMI,ID +"=?" , new String[]{String.valueOf ( id )});
        db.close ();
    }

    public void Sil() {

        SQLiteDatabase db = this.getWritableDatabase ();
        db.delete ( TABLO_ISMI,null, null);
        db.close ();
    }

    public void Guncelle(long id, long tarih, String ders, int soru) {

        SQLiteDatabase db = this.getWritableDatabase ();
        ContentValues cv = new ContentValues (  );
        cv.put ( DERS,ders );
        cv.put ( TARIH,tarih );
        cv.put ( DERS,ders );
        cv.put ( SORUSAYISI,soru );

        db.update ( TABLO_ISMI, cv, ID+"="+id, null );
        db.close ();


    }


    public List<Ogrenci> IkiTarihArasi(long tarih_ilk, long tarih_iki) {

        SQLiteDatabase db = this.getReadableDatabase ();
        String [] sutunlar = new String[] {DERS, SORUSAYISI,TARIH, ID};
        String [] tarihler = new String[] {String.valueOf ( tarih_ilk ), String.valueOf ( tarih_iki ) };

        Cursor c = db.query ( TABLO_ISMI,sutunlar, " BETWEEN ? AND  ?",tarihler, null, null, TARIH + " desc " );
        // Son Sutunda ki ifade sıralamaya yarıyor tarıh + " desc "

        int dersSiraNo = c.getColumnIndex ( DERS );
        int soruSayisiSiraNo = c.getColumnIndex ( SORUSAYISI );
        int tarihNo = c.getColumnIndex ( TARIH );
        int idSıraNo = c.getColumnIndex ( ID );

        List<Ogrenci> ogrenciLsit = new ArrayList<Ogrenci> (  );

        /*for (c.moveToFirst (); !c.isAfterLast (); c.moveToNext ()){
            Ogrenci ogrenci = new Ogrenci (  );
            ogrenci.setDers ( c.getString ( dersSiraNo ) );
            ogrenci.setSoru ( c.getInt ( soruSayisiSiraNo ) );
            ogrenci.setTarih ( c.getLong  ( tarihNo )  );
            ogrenci.setId ( c.getLong  ( idSıraNo )  );
            ogrenciLsit.add ( ogrenci );
        }*/

        if (c.moveToFirst ()) {
            do {
                Ogrenci ogrenci = new Ogrenci (  );
                ogrenci.setDers ( c.getString ( dersSiraNo ) );
                ogrenci.setSoru ( c.getInt ( soruSayisiSiraNo ) );
                ogrenci.setTarih ( c.getLong  ( tarihNo )  );
                ogrenci.setId ( c.getLong  ( idSıraNo )  );
                ogrenciLsit.add ( ogrenci );

            } while (c.moveToNext ());

        } else  {
            ogrenciLsit = null;
        }
        db.close ();

        return ogrenciLsit;


    }
}
