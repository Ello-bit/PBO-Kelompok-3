package com.example.model;

import com.example.database.PenggunaDAO;


import java.util.ArrayList;

/*
 * KELAS: Admin
 * KONSEP OOP:
 *  - INHERITANCE  : "extends Pengguna".
 *  - POLYMORPHISM : getDashboard() berbeda isinya dengan milik PemilikUMKM.
 *
 * Method sesuai diagram kelas: kelolaPengguna() dan verifikasiLaporan().
 * CATATAN: method yang mengubah data (ubahStatusPengguna, verifikasiLaporan)
 * HANYA mengubah isi objek. Menyimpan ke database dilakukan oleh controller.
 */
public class Admin extends Pengguna {

    public Admin(int id, String username, String password, boolean aktif) {
        super(id, username, password, "ADMIN", aktif);
    }

    @Override
    public void getDashboard() {
        System.out.println("Dashboard Admin - Halo Admin, " + getUsername());
    }

    // KELOLA PENGGUNA: mengambil daftar semua pengguna (Admin & Pemilik) untuk ditampilkan.
    // Aksi tambah pengguna dan aktifkan/nonaktifkan akun dilakukan lewat controller + ubahStatusPengguna().
    public ArrayList<Pengguna> kelolaPengguna() {
        return PenggunaDAO.ambilSemua();
    }

    // Menyetujui atau menolak laporan.
    // disetujui = true  -> status "TERVERIFIKASI", alasan tidak dipakai
    // disetujui = false -> status "DITOLAK", alasan diisi admin
    public void verifikasiLaporan(LaporanPengajuanKUR laporan, boolean disetujui, String alasan) {
        if (disetujui) {
            laporan.setStatusVerifikasi("TERVERIFIKASI");
            laporan.setAlasanPenolakan("-");
        } else {
            laporan.setStatusVerifikasi("DITOLAK");
            laporan.setAlasanPenolakan(alasan);
        }
    }

    // Membalik status akun: aktif jadi nonaktif, nonaktif jadi aktif.
    // RUMUS: aktif baru = kebalikan dari aktif lama (! berarti "bukan")
    public void ubahStatusPengguna(Pengguna pengguna) {
        pengguna.setAktif(!pengguna.isAktif());
    }
}
