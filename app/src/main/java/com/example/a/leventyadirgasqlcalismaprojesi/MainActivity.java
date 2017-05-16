package com.example.a.leventyadirgasqlcalismaprojesi;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static final int DIALOG_HAKKINDA = 1;
    static final int DIALOG_DERS = 2;
    static final int DIALOG_TARIH = 3;
    TableLayout tablo;
    TextView tv;
    android.view.ActionMode actionMode;
   public long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tablo = (TableLayout)findViewById ( R.id.tablo);
        tv = (TextView)findViewById ( R.id.liste_tv );


        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.ab));

        try {
            Listele();
        }  catch (Exception e) {
            Toast.makeText ( this, "Kayıt yok", Toast.LENGTH_SHORT ).show ();
        }

    }

   public void Listele() {
        tablo.removeAllViews();

        VeriTabani db = new VeriTabani ( getApplicationContext () );
        List<Ogrenci> ogrenciList=new ArrayList<Ogrenci>();
        ogrenciList = db.TumKayitlar();

        //toplma ve ortalama soru sayısı kodlanacak
        long enKucuk= ogrenciList.get ( ogrenciList.size () -1 ). getTarih ();

        long enBuyuk = ogrenciList.get(0).getTarih ();

        Date fark = new Date ( enBuyuk - enKucuk );

        int farkGun = ((fark.getYear () %70) * 365) + (fark.getMonth () *30) + (fark.getDate () -1);
        farkGun++;
        int toplamSoru = 0;
        for (Ogrenci ogrenci : ogrenciList) {
            toplamSoru = toplamSoru + ogrenci.getSoru ();
        }
        //Ortalama Bulalım

        int ortalamaSoru = toplamSoru/farkGun;

        if (ortalamaSoru>=100){
            tv.setText ( "Tebrikler günlük 100 soru hedefinizi aştınız. " +
                    "\n Toplam Çözülen Soru Sayısı" + toplamSoru +
            "\n Günlük ortalam soru sayısı " + ortalamaSoru);
            tv.setTextColor ( Color.BLACK );
            tv.setBackgroundColor ( Color.parseColor ( "#123465"));
        } else {
            tv.setText ( "hedef gerçekleşemedi " +
                    "\n Toplam Çözülen Soru Sayısı" + toplamSoru +
                    "\n Günlük ortalam soru sayısı " + ortalamaSoru);
            tv.setTextColor ( Color.RED );
            tv.setBackgroundColor (Color.WHITE );

        }


        for (final Ogrenci ogrenci: ogrenciList) {

            TableRow satir = new TableRow ( getApplicationContext () );
            satir.setGravity ( Gravity.CENTER );


            TextView tv_tarih = new TextView ( getApplicationContext () );
            tv_tarih.setPadding ( 2,2,2,2 );
            tv_tarih.setTextColor ( Color.WHITE );

            java.text.DateFormat df = new java.text.SimpleDateFormat ( "dd/MM/yyyy" );
            Date date = new Date ( ogrenci.getTarih () );
            tv_tarih.setText ( df.format ( date ) + " " );

            TextView tv_ders = new TextView (  getApplicationContext ());
            tv_ders.setPadding ( 2,2,2,2 );
            tv_ders.setTextColor ( Color.WHITE );
            tv_ders.setText ( String.valueOf ( ogrenci.getDers ()) );

            TextView tv_soruSayisi = new TextView (  getApplicationContext ());
            tv_soruSayisi.setPadding ( 2,2,2,2 );
            tv_soruSayisi.setTextColor ( Color.WHITE );
            tv_soruSayisi.setText ( String.valueOf ( ogrenci.getSoru () ) );

            satir.addView ( tv_tarih );
            satir.addView ( tv_ders );
            satir.addView ( tv_soruSayisi );


            tablo.addView ( satir );

            satir.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    id=ogrenci.getId();

                    if(actionMode!=null){
                        return false;
                    }
                    MyActionModeCallBack callback=new MyActionModeCallBack();
                    actionMode=startActionMode(callback);
                    v.setSelected(true);

                    return true;
                }
            });



        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:


                return true;


            case R.id.ekle:
                showDialog(DIALOG_DERS);



                return true;


            case R.id.tarih:
                showDialog ( DIALOG_TARIH );


                return true;




            case R.id.paylas:

                paylasMesaj ( tv.getText () );

                return true;

            case R.id.hakkinda:

                showDialog(DIALOG_HAKKINDA);

                return true;

            case R.id.tumunusil:
                final AlertDialog.Builder builder = new AlertDialog.Builder ( MainActivity.this );
                builder.setTitle ( "UYARI" );
                builder.setMessage ( "Tümünü silmek istediğinizden Emin misiniz?" );

                builder.setPositiveButton ( "TAMAM", new DialogInterface.OnClickListener () {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        VeriTabani db = new VeriTabani ( getApplicationContext () );
                        db.Sil ();
                        tablo.removeAllViews();
                        Toast.makeText ( MainActivity.this, "Başarı ile Silindi", Toast.LENGTH_SHORT ).show ();

                    }

                });


                builder.setNegativeButton ( "İPTAL", new DialogInterface.OnClickListener () {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        builder.setCancelable ( false );

                    }
                } );

                builder.show ();


                return true;



            default:
                return super.onOptionsItemSelected(item);


        }



    }

    @Override
    protected Dialog onCreateDialog(int id) {

        Dialog dialog = null;

        switch (id) {
            case DIALOG_HAKKINDA:

                dialog = new Dialog(MainActivity.this);
                dialog.setTitle("HAKKINDA");
                dialog.setContentView(R.layout.hakkinda);

                break;

            case DIALOG_DERS:

                dialog = getEkleDialog();


                break;

            case DIALOG_TARIH:
                dialog = getIkiTarihArasi();

                break;

            default:
                dialog = null;
        }

        return dialog;
    }

    private Dialog getIkiTarihArasi() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View layout = inflater.inflate(R.layout.iki,null);
        Button kaydet = (Button) layout.findViewById(R.id.kaydet);
        Button vazgec = (Button) layout.findViewById(R.id.vazgec);
         final DatePicker dp_ilk = (DatePicker)layout.findViewById(R.id.dp_ilk);
        final DatePicker dp_iki = (DatePicker)layout.findViewById(R.id.dp_iki);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("İki Tarih Arası");
        builder.setView(layout);

        final AlertDialog dialog = builder.create();

        kaydet.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                java.text.DateFormat df = new java.text.SimpleDateFormat ( "dd//MM/yyyy" );
                int gun_ilk = dp_ilk.getDayOfMonth ();
                int ay_ilk = dp_ilk.getMonth () + 1;
                int yil_ilk = dp_ilk.getYear () ;

                Date date_ilk =null;
                try {
                    date_ilk  =df.parse ( gun_ilk + "/" + ay_ilk + "/" + yil_ilk );

                }catch (ParseException e) {
                    e.printStackTrace ();
                }
                long tarih_ilk = date_ilk.getTime ();


                int gun_iki = dp_iki.getDayOfMonth ();
                int ay_iki= dp_iki.getMonth () + 1;
                int yil_iki= dp_iki.getYear () ;

                Date date_iki =null;
                try {
                    date_iki  =df.parse ( gun_iki + "/" + ay_iki + "/" + yil_iki );

                }catch (ParseException e) {
                    e.printStackTrace ();
                }
                long tarih_iki = date_iki.getTime ();

                VeriTabani db = new VeriTabani ( getApplicationContext () );
                List<Ogrenci> ogrenciList = new ArrayList<Ogrenci> (  );
                ogrenciList = db.IkiTarihArasi(tarih_ilk, tarih_iki);

                dialog.dismiss ();


            }
        } );



        vazgec.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                dialog.cancel ();

            }
        } );

        return  dialog;

    }

    private Dialog getEkleDialog() {

        LayoutInflater inflater = LayoutInflater.from(this);
        View layout = inflater.inflate(R.layout.ders_ekle, null);
        Button kaydet = (Button) layout.findViewById(R.id.bt_kaydet);
        Button vazgec = (Button) layout.findViewById(R.id.bt_vazgec);


        final EditText soru = (EditText)layout.findViewById(R.id.et);
       final Spinner sp = (Spinner)layout.findViewById(R.id.spinner);

        final DatePicker dataPicker = (DatePicker)layout.findViewById(R.id.datePicker);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Kayıt Ekleyiniz.");
        builder.setView(layout);

        final AlertDialog dialog = builder.create();

        kaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {


                    int gun = dataPicker.getDayOfMonth();
                    int ay = dataPicker.getMonth() + 1;
                    int yil = dataPicker.getYear();

                    java.text.DateFormat df = new java.text.SimpleDateFormat ( "dd/MM/yyyy" );


                    Date date = null;


                    try {
                        date = df.parse(gun + "/" + ay + "/" + yil);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    long tarih = date.getTime();

                    int pozisyon = sp.getSelectedItemPosition();
                    String ders = (String) sp.getItemAtPosition(pozisyon);

                    int soruSayisi = Integer.valueOf(soru.getText().toString());

                    Ogrenci ogrenci = new Ogrenci(ders, soruSayisi, tarih);
                    VeriTabani db = new VeriTabani(getApplicationContext());
                    long id = db.kayitEkle(ogrenci);

                    if (id == -1) {
                        Toast.makeText(MainActivity.this, "kayıt Sırasında Bir hata oluştu", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Kayıt Baaşrılı", Toast.LENGTH_SHORT).show();
                    }
                    Listele ();


                    dialog.dismiss();

                } catch (Exception e) {
                    e.printStackTrace();
                   Toast.makeText(MainActivity.this, "Soru kısmı boş geçilemez", Toast.LENGTH_SHORT).show();
                }

            }
        });

        vazgec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        return dialog;
    }

    private void paylasMesaj(CharSequence mesaj) {
        Intent paylasIntent = new Intent ( Intent.ACTION_SEND );
        paylasIntent.setType ( "text/plain" );
        paylasIntent.putExtra ( Intent.EXTRA_TEXT, "paylaşın" );
        startActivity ( Intent.createChooser ( paylasIntent ,"Paylaşın" ) );
    }


    class MyActionModeCallBack implements  android.view.ActionMode.Callback {




        @Override
        public boolean onCreateActionMode(android.view.ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.contex_menu,menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(android.view.ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(android.view.ActionMode mode, MenuItem item) {
            switch (item.getItemId()){

                case R.id.sil:

                    VeriTabani db=new VeriTabani(getApplicationContext());
                    db.Sil(id);
                    Listele();
                    mode.finish();
                    return true;

                case R.id.guncelle:
                   // VeriTabani dbGuncelle = new VeriTabani ( getApplicationContext () );
                  //dbGuncelle.Guncelle ( id,tarih, ders, soru );

                    mode.finish();
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(android.view.ActionMode mode) {
            actionMode=null;
        }
    }
}
