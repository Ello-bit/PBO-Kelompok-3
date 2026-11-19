package com.example.database;

import com.example.model.ProfilUMKM;
import com.example.model.Transaksi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

/*
 * KELAS: ProfilDAO
 * Semua query SQL untuk tabel "profil_umkm" ada di sini.
 * Semua method dibuat static, jadi dipanggil langsung: ProfilDAO.ambilProfilPemilik(...)
 *
 * Kolom tabel: id_usaha, id_pengguna, nama_usaha, jenis_usaha, alamat.
 * Satu pemilik = satu profil usaha (hubungan 1:1 lewat kolom id_pengguna).
 */
public class ProfilDAO {

    // Mengambil profil usaha milik satu pemilik, LENGKAP dengan daftar transaksinya.
    // Hasilnya null kalau pemilik itu belum punya baris di tabel profil_umkm.
    public static ProfilUMKM ambilProfilPemilik(int idPengguna) {
        String sql = "SELECT * FROM profil_umkm WHERE id_pengguna = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPengguna);
            ResultSet rs = ps.executeQuery();

            // LANGKAH 1: kalau tidak ada baris, berarti belum punya profil
            if (!rs.next()) {
                return null;
            }

            // LANGKAH 2: buat objek profil dari baris database
            int idUsaha = rs.getInt("id_usaha");
            String namaUsaha = rs.getString("nama_usaha");
            String jenisUsaha = rs.getString("jenis_usaha");
            String alamat = rs.getString("alamat");
            ProfilUMKM profil = new ProfilUMKM(idUsaha, namaUsaha, jenisUsaha, alamat);

            // LANGKAH 3: isi daftar transaksi milik usaha ini
            isiTransaksi(profil);
            return profil;
        } catch (Exception e) {
            System.out.println("Gagal ambil profil: " + e.getMessage());
            return null;
        }
    }

    // Mengisi daftar transaksi sebuah profil dari tabel transaksi (lewat TransaksiDAO milik Dewok).
    // Dipanggil lagi kalau transaksi baru sudah disimpan, supaya angka di Beranda ikut berubah.
    public static void isiTransaksi(ProfilUMKM profil) {
        TransaksiDAO transaksiDAO = new TransaksiDAO();
        List<Transaksi> daftar = transaksiDAO.getTransaksiByIdUsaha(profil.getIdUsaha());
        profil.getDaftarTransaksi().clear();
        for (Transaksi transaksi : daftar) {
            profil.tambahTransaksi(transaksi);
        }
    }

    // Menyimpan perubahan nama usaha, jenis usaha, dan alamat ke database.
    public static boolean simpanProfil(ProfilUMKM profil) {
        String sql = "UPDATE profil_umkm SET nama_usaha = ?, jenis_usaha = ?, alamat = ? WHERE id_usaha = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, profil.getNamaUsaha());
            ps.setString(2, profil.getJenisUsaha());
            ps.setString(3, profil.getAlamat());
            ps.setInt(4, profil.getIdUsaha());
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("Gagal simpan profil: " + e.getMessage());
            return false;
        }
    }
}
