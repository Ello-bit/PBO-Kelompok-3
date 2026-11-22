package com.example.model;

import com.example.database.PenggunaDAO;

import java.util.ArrayList;

public class Admin extends Pengguna {

    public Admin(int id, String username, String password, boolean aktif) {
        super(id, username, password, "ADMIN", aktif);
    }

    @Override
    public void getDashboard() {
        System.out.println("Dashboard Admin - Halo Admin, " + getUsername());
    }

    public ArrayList<Pengguna> kelolaPengguna() {
        return PenggunaDAO.ambilSemua();
    }

    public void verifikasiLaporan(LaporanPengajuanKUR laporan, boolean disetujui, String alasan) {
        if (disetujui) {
            laporan.setStatusVerifikasi("TERVERIFIKASI");
            laporan.setAlasanPenolakan("-");
        } else {
            laporan.setStatusVerifikasi("DITOLAK");
            laporan.setAlasanPenolakan(alasan);
        }
    }

    public void ubahStatusPengguna(Pengguna pengguna) {
        pengguna.setAktif(!pengguna.isAktif());
    }
}
