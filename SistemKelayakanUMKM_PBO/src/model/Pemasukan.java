package model;

import java.time.LocalDateTime;

public class Pemasukan extends Transaksi {

    public Pemasukan (int id, double nominal, LocalDateTime tanggal, String catatan){
        super(id, nominal, tanggal, catatan);
    }
    
    @Override 
    public double hitungDampakSaldo(){
        return getNominal();
    }
}
