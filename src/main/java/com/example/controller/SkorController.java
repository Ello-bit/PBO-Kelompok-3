package com.example.controller;

import com.example.app.dao.TransaksiDAO;
import com.example.app.models.Transaksi;
import com.example.database.SkorDB;
import com.example.model.SkorKelayakan;

import java.sql.SQLException;
import java.time.YearMonth;
import java.util.List;

public class SkorController {

    private final TransaksiDAO transaksiDAO;
    private final SkorDB skorDB;

    public SkorController() {
        this.transaksiDAO = new TransaksiDAO();
        this.skorDB = new SkorDB();
    }

    public SkorKelayakan prosesSkor(int idUsaha)
            throws SQLException {

        // 1. Mengambil transaksi enam bulan terakhir
        List<Transaksi> daftarTransaksi =
                transaksiDAO.ambilTransaksiEnamBulan(idUsaha);

        // 2. Membuat objek skor berdasarkan periode
        String periode = YearMonth.now().toString();

        SkorKelayakan skor =
                new SkorKelayakan(0, periode);

        // 3. Menghitung skor K1-K4
        skor.hitungSkor(daftarTransaksi);

        // 4. Menyimpan hasil ke database
        skorDB.simpanSkor(idUsaha, skor);

        return skor;
    }
}