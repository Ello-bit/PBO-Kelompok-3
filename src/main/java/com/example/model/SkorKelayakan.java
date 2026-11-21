package com.example.model;

import java.time.YearMonth;
import java.util.List;

/**
 * Kelas SkorKelayakan menghitung skor kelayakan usaha UMKM (skala 0 - 100)
 * berdasarkan riwayat transaksi 6 bulan terakhir.
 */
public class SkorKelayakan implements Scoreable {

    private int id;
    private String periode;
    private double nilaiSkor;
    private int level;

    private double skorK1; // Konsistensi pencatatan (maks 30)
    private double skorK2; // Tren pemasukan (maks 25)
    private double skorK3; // Margin laba (maks 25)
    private double skorK4; // Stabilitas laba bulanan (maks 20)

    public SkorKelayakan(int id, String periode) {
        this.id = id;
        this.periode = periode;
    }

    @Override
    public double hitungSkor() {
        return nilaiSkor;
    }

    /**
     * Menghitung skor kelayakan usaha dari daftar transaksi.
     */
    public double hitungSkor(List<Transaksi> daftarTransaksi) {
        // Jika belum ada transaksi sama sekali, semua skor = 0 dan Level 1
        if (daftarTransaksi == null || daftarTransaksi.isEmpty()) {
            skorK1 = 0;
            skorK2 = 0;
            skorK3 = 0;
            skorK4 = 0;
            nilaiSkor = 0;
            level = 1;
            return nilaiSkor;
        }

        // LANGKAH 1: Hitung total saldo kas keseluruhan menggunakan Polimorfisme
        double saldoKas = 0;
        for (Transaksi t : daftarTransaksi) {
            saldoKas += t.hitungDampakSaldo();
        }

        // LANGKAH 2: Rekap transaksi per bulan selama 6 bulan terakhir
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

            // Pisahkan pemasukan 3 bulan pertama (bulan 1-3) dan 3 bulan terakhir (bulan 4-6)
            if (i < 3) {
                pemasukan3BulanAwal += penjualanBulanIni;
            } else {
                pemasukan3BulanAkhir += penjualanBulanIni;
            }
        }

        double labaBersihTotal = totalPenjualan - totalOperasional;

        // LANGKAH 3: Hitung masing-masing kriteria (K1 - K4)

        // K1: Konsistensi pencatatan (maksimal 30 poin)
        skorK1 = 30.0 * jumlahBulanAktif / 6.0;

        // K2: Tren pemasukan (maksimal 25 poin)
        // Jika usaha sedang rugi (laba <= 0) atau saldo kas minus, tren dianggap tidak sehat (0 poin)
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
                rasioTren = 1.0; // Batasi maksimal 100%
            }
            skorK2 = 25.0 * rasioTren;
        }

        // K3: Margin laba bersih (maksimal 25 poin, target margin sehat minimal 20%)
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

        // K4: Stabilitas laba bulanan (maksimal 20 poin)
        if (saldoKas < 0) {
            skorK4 = 0;
        } else {
            skorK4 = 20.0 * jumlahBulanLabaPositif / 6.0;
        }

        // Bulatkan masing-masing skor ke 2 angka di belakang koma agar rapi di tampilan
        skorK1 = bulatkan(skorK1);
        skorK2 = bulatkan(skorK2);
        skorK3 = bulatkan(skorK3);
        skorK4 = bulatkan(skorK4);

        // LANGKAH 4: Jumlahkan total skor dan tentukan Level (1 sampai 5)
        nilaiSkor = bulatkan(skorK1 + skorK2 + skorK3 + skorK4);
        level = tentukanLevel(nilaiSkor);

        return nilaiSkor;
    }

    /**
     * Menentukan level kelayakan 1 sampai 5 berdasarkan total skor.
     */
    private int tentukanLevel(double skor) {
        if (skor >= 80) {
            return 5; // Sangat Baik
        } else if (skor >= 60) {
            return 4; // Baik
        } else if (skor >= 40) {
            return 3; // Cukup
        } else if (skor >= 20) {
            return 2; // Kurang
        } else {
            return 1; // Rendah
        }
    }

    /**
     * Membulatkan angka desimal menjadi 2 angka di belakang koma.
     */
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
