package model;

import interfaces.Scoreable;

public class SkorKelayakan implements Scoreable {
    private int id;
    private String periode;
    private double nilaiSkor;
    private int level;

    public SkorKelayakan(int id, String periode){
        this.id = id;
        this.periode = periode;
        this.nilaiSkor = 0;
        this.level = 0;
    }

    @Override
    public double hitungSkor(){
        //logika perhitungan
        return nilaiSkor;
    }   
}