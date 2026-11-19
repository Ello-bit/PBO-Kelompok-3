package com.example.controller;

import com.example.database.TransaksiDAO;
import com.example.model.Transaksi;
import com.example.database.SkorDAO;
import com.example.model.SkorKelayakan;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

public class SkorController {

    private final TransaksiDAO transaksiDAO;
    private final SkorDAO skorDB;

    public SkorController() {
        this.transaksiDAO = new TransaksiDAO();
        this.skorDB = new SkorDAO();
    }

    public SkorKelayakan prosesSkor(int idUsaha)
            throws SQLException {

        // 1. Mengambil seluruh transaksi milik UMKM
        List<Transaksi> semuaTransaksi =
                transaksiDAO.getTransaksiByIdUsaha(idUsaha);

        // 2. Menentukan batas enam bulan terakhir
        LocalDateTime batasTanggal =
                YearMonth.now().minusMonths(5)
                        .atDay(1).atStartOfDay();

        // 3. Memfilter transaksi enam bulan terakhir
        List<Transaksi> daftarTransaksi =
                semuaTransaksi.stream()
                        .filter(transaksi ->
                                transaksi.getTanggal() != null
                                && !transaksi.getTanggal()
                                        .isBefore(batasTanggal))
                        .collect(Collectors.toList());

        // 4. Membuat objek skor berdasarkan periode
        String periode = YearMonth.now().toString();

        SkorKelayakan skor =
                new SkorKelayakan(0, periode);

        // 5. Menghitung skor K1-K4
        skor.hitungSkor(daftarTransaksi);

        // 6. Menyimpan hasil ke database
        skorDB.simpanSkor(idUsaha, skor);

        return skor;
    }
}