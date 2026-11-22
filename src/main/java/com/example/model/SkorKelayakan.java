package com.example.model;

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

    public double hitungSkor(List<Transaksi> daftarTransaksi) {
        if (daftarTransaksi == null || daftarTransaksi.isEmpty()) {
            skorK1 = 0;
            skorK2 = 0;
            skorK3 = 0;
            skorK4 = 0;
            nilaiSkor = 0;
            level = 1;
            return nilaiSkor;
        }

        double saldoKas = 0;
        for (Transaksi t : daftarTransaksi) {
            saldoKas += t.hitungDampakSaldo();
        }

        YearMonth bulanSekarang = YearMonth.now();

        int jumlahBulanAktif = 0;
        int jumlahBulanLabaPositif = 0;

        double pemasukan3BulanAwal = 0;
        double pemasukan3BulanAkhir = 0;

        double totalPenjualan = 0;
        double totalOperasional = 0;

        for (int i = 0; i < 6; i++) {
            YearMonth bulanDicek = bulanSekarang.minusMonths(5 - i);

            boolean adaTransaksi = false;
            double penjualanBulanIni = 0;
            double operasionalBulanIni = 0;

            for (Transaksi t : daftarTransaksi) {
                if (t.getTanggal() == null) {
                    continue;
                }

                YearMonth bulanTransaksi = YearMonth.from(t.getTanggal());
                if (!bulanTransaksi.equals(bulanDicek)) {
                    continue;
                }

                adaTransaksi = true;

                if (t.getKategori() == KategoriTransaksi.PENJUALAN) {
                    penjualanBulanIni += t.getNominal();
                } else if (t.getKategori() == KategoriTransaksi.OPERASIONAL) {
                    operasionalBulanIni += t.getNominal();
                }
            }

            if (adaTransaksi) {
                jumlahBulanAktif++;
            }

            double labaBulanIni = penjualanBulanIni - operasionalBulanIni;
            if (labaBulanIni > 0) {
                jumlahBulanLabaPositif++;
            }

            totalPenjualan += penjualanBulanIni;
            totalOperasional += operasionalBulanIni;

            if (i < 3) {
                pemasukan3BulanAwal += penjualanBulanIni;
            } else {
                pemasukan3BulanAkhir += penjualanBulanIni;
            }
        }

        double labaBersihTotal = totalPenjualan - totalOperasional;

        skorK1 = 30.0 * jumlahBulanAktif / 6.0;

        if (labaBersihTotal <= 0 || saldoKas < 0) {
            skorK2 = 0;
        } else if (pemasukan3BulanAwal == 0) {
            if (pemasukan3BulanAkhir > 0) {
                skorK2 = 25.0;
            } else {
                skorK2 = 0;
            }
        } else {
            double rasioTren = pemasukan3BulanAkhir / pemasukan3BulanAwal;
            if (rasioTren > 1.0) {
                rasioTren = 1.0;
            }
            skorK2 = 25.0 * rasioTren;
        }

        if (totalPenjualan == 0 || labaBersihTotal <= 0 || saldoKas < 0) {
            skorK3 = 0;
        } else {
            double margin = labaBersihTotal / totalPenjualan;
            double rasioMargin = margin / 0.20;
            if (rasioMargin > 1.0) {
                rasioMargin = 1.0;
            }
            skorK3 = 25.0 * rasioMargin;
        }

        if (saldoKas < 0) {
            skorK4 = 0;
        } else {
            skorK4 = 20.0 * jumlahBulanLabaPositif / 6.0;
        }

        skorK1 = bulatkan(skorK1);
        skorK2 = bulatkan(skorK2);
        skorK3 = bulatkan(skorK3);
        skorK4 = bulatkan(skorK4);

        nilaiSkor = bulatkan(skorK1 + skorK2 + skorK3 + skorK4);
        level = tentukanLevel(nilaiSkor);

        return nilaiSkor;
    }

    private int tentukanLevel(double skor) {
        if (skor >= 80) {
            return 5;
        } else if (skor >= 60) {
            return 4;
        } else if (skor >= 40) {
            return 3;
        } else if (skor >= 20) {
            return 2;
        } else {
            return 1;
        }
    }

    private double bulatkan(double angka) {
        return Math.round(angka * 100.0) / 100.0;
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
