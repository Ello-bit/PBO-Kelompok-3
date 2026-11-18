package com.example.model;

import com.example.app.models.Transaksi;
import com.example.app.models.KategoriTransaksi;

import java.time.YearMonth;
import java.util.List;

public class SkorKelayakan implements Scoreable {

    private int id;
    private String periode;
    private double nilaiSkor;
    private int level;

    private double skorK1;
    private double skorK2;
    private double skorK3;
    private double skorK4;

    public SkorKelayakan(int id, String periode) {
        this.id = id;
        this.periode = periode;
    }

    @Override
    public double hitungSkor() {
        return nilaiSkor;
    }

    public double hitungSkor(List<Transaksi> transaksi) {

        YearMonth bulanSekarang = YearMonth.now();

        int jumlahBulanAktif = 0;
        int jumlahBulanLabaPositif = 0;

        double pemasukanAwal = 0;
        double pemasukanAkhir = 0;

        double totalPenjualan = 0;
        double totalOperasional = 0;

        for (int i = 0; i < 6; i++) {

            YearMonth bulan = bulanSekarang.minusMonths(5 - i);

            boolean adaTransaksi = false;
            double penjualanBulanan = 0;
            double operasionalBulanan = 0;

            for (Transaksi t : transaksi) {

                YearMonth bulanTransaksi =
                        YearMonth.from(t.getTanggal());

                if (!bulanTransaksi.equals(bulan)) {
                    continue;
                }

                adaTransaksi = true;

                if (t.getKategori() == KategoriTransaksi.PENJUALAN) {
                    penjualanBulanan += t.getNominal();
                }

                if (t.getKategori() == KategoriTransaksi.OPERASIONAL) {
                    operasionalBulanan += t.getNominal();
                }
            }

            if (adaTransaksi) {
                jumlahBulanAktif++;
            }

            double labaBulanan =
                    penjualanBulanan - operasionalBulanan;

            if (labaBulanan > 0) {
                jumlahBulanLabaPositif++;
            }

            totalPenjualan += penjualanBulanan;
            totalOperasional += operasionalBulanan;

            if (i < 3) {
                pemasukanAwal += penjualanBulanan;
            } else {
                pemasukanAkhir += penjualanBulanan;
            }
        }

        // K1: Konsistensi
        skorK1 = 30.0 * jumlahBulanAktif / 6.0;

        // K2: Tren pemasukan
        if (pemasukanAwal == 0) {
            skorK2 = pemasukanAkhir > 0 ? 25 : 0;
        } else {
            skorK2 = 25.0 * Math.min(
                    1.0, pemasukanAkhir / pemasukanAwal
            );
        }

        // K3: Margin laba
        if (totalPenjualan == 0) {
            skorK3 = 0;
        } else {
            double labaBersih =
                    totalPenjualan - totalOperasional;

            double margin = labaBersih / totalPenjualan;

            skorK3 = 25.0 * Math.min(
                    1.0, Math.max(0, margin) / 0.20
            );
        }

        // K4: Stabilitas
        skorK4 = 20.0 * jumlahBulanLabaPositif / 6.0;

        nilaiSkor = skorK1 + skorK2 + skorK3 + skorK4;

        // Menentukan kategori skor
        if (nilaiSkor >= 70) {
            level = 3;
        } else if (nilaiSkor >= 40) {
            level = 2;
        } else {
            level = 1;
        }

        nilaiSkor = Math.round(nilaiSkor * 100.0) / 100.0;

        return nilaiSkor;
    }

    public int getId() {
        return id;
    }

    public String getPeriode() {
        return periode;
    }

    public double getNilaiSkor() {
        return nilaiSkor;
    }

    public int getLevel() {
        return level;
    }

    public double getSkorK1() {
        return skorK1;
    }

    public double getSkorK2() {
        return skorK2;
    }

    public double getSkorK3() {
        return skorK3;
    }

    public double getSkorK4() {
        return skorK4;
    }
}