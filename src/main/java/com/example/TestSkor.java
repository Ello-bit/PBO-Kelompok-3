package com.example;

import com.example.controller.SkorController;
import com.example.model.SkorKelayakan;

public class TestSkor {

    public static void main(String[] args) {
        try {
            SkorController controller = new SkorController();

            SkorKelayakan skor = controller.prosesSkor(1);

            System.out.println("=== HASIL PENGUJIAN SKOR ===");
            System.out.println("ID UMKM : 1");
            System.out.println("Periode : " + skor.getPeriode());
            System.out.println("K1      : " + skor.getSkorK1());
            System.out.println("K2      : " + skor.getSkorK2());
            System.out.println("K3      : " + skor.getSkorK3());
            System.out.println("K4      : " + skor.getSkorK4());
            System.out.println("Total   : " + skor.getNilaiSkor());
            System.out.println("Level   : " + skor.getLevel());

        } catch (Exception e) {
            System.out.println("Pengujian gagal:");
            e.printStackTrace();
        }
    }
}
