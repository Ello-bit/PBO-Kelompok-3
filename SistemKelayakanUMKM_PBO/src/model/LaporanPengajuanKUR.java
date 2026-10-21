package model;

import interfaces.Exportable;
import java.time.LocalDateTime;

public class LaporanPengajuanKUR implements Exportable {
    private int id;
    private LocalDateTime tanggalGenerate;
    private String filePath;

    public LaporanPengajuanKUR(int id, LocalDateTime tanggalGenerate, String filepath){
        this.id = id;
        this.tanggalGenerate = tanggalGenerate;
        this.filePath = filepath;
    }

    @Override 
    public void exportToPDF(){
        System.out.println("Lokasi file : " + filePath);
    }
}