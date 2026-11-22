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

        List<Transaksi> semuaTransaksi =
                transaksiDAO.getTransaksiByIdUsaha(idUsaha);

        LocalDateTime batasTanggal =
                YearMonth.now().minusMonths(5)
                        .atDay(1).atStartOfDay();

        List<Transaksi> daftarTransaksi =
                semuaTransaksi.stream()
                        .filter(transaksi ->
                                transaksi.getTanggal() != null
                                && !transaksi.getTanggal()
                                        .isBefore(batasTanggal))
                        .collect(Collectors.toList());

        String periode = YearMonth.now().toString();

        SkorKelayakan skor =
                new SkorKelayakan(0, periode);

        skor.hitungSkor(daftarTransaksi);

        skorDB.simpanSkor(idUsaha, skor);

        return skor;
    }
}
