package com.example.a.leventyadirgasqlcalismaprojesi;

/**
 * Created by a on 12.05.2017.
 */

public class Ogrenci {

    String ders;
    int soru;
    long tarih;
    long Id;

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }



    public Ogrenci() {
    }


    public Ogrenci(String ders, int soru, long tarih) {
        this.ders = ders;
        this.soru = soru;
        this.tarih = tarih;
    }

    public String getDers() {
        return ders;
    }



    public void setDers(String ders) {
        this.ders = ders;
    }

    public int getSoru() {
        return soru;
    }

    public void setSoru(int soru) {
        this.soru = soru;
    }

    public long getTarih() {
        return tarih;
    }

    public void setTarih(long tarih) {
        this.tarih = tarih;
    }
}
