package com.example.database;

import com.example.model.Admin;
import com.example.model.PemilikUMKM;
import com.example.model.Pengguna;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/*
 * KELAS: PenggunaDAO
 * Semua query SQL untuk tabel "pengguna" ada di sini.
 * Semua method dibuat static, jadi dipanggil langsung: PenggunaDAO.login(...)
 */
public class PenggunaDAO {

    // Mengubah true/false menjadi nilai kolom status di database: 'aktif' atau 'nonaktif'.
    private static String statusTeks(boolean aktif) {
        if (aktif) {
            return "aktif";
        }
        return "nonaktif";
    }

    // Mencari pengguna dengan username & password yang cocok.
    // Hasil: objek Admin / PemilikUMKM, atau null kalau username/password tidak cocok.
    // Kalau database tidak bisa dihubungi, method ini MELEMPAR SQLException supaya
    // pemanggil bisa membedakan "salah password" dengan "database bermasalah".
    public static Pengguna login(String username, String password) throws SQLException {
        String sql = "SELECT * FROM pengguna WHERE username = ? AND password = ?";
        // try (...) = koneksi otomatis ditutup setelah selesai, walaupun terjadi error
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);   // isi tanda tanya ke-1
            ps.setString(2, password);   // isi tanda tanya ke-2
            ResultSet rs = ps.executeQuery();   // executeQuery untuk SELECT

            if (rs.next()) {
                int id = rs.getInt("id_pengguna");
                String role = rs.getString("role");
                boolean aktif = "aktif".equalsIgnoreCase(rs.getString("status"));   // kolom status: aktif atau nonaktif (huruf besar/kecil tidak dibedakan)

                // Satu tabel untuk dua kelas anak: role menentukan objek yang dibuat
                // (POLYMORPHISM: dua-duanya dikembalikan sebagai tipe Pengguna)
                if (role.equalsIgnoreCase("Admin")) {
                    return new Admin(id, username, password, aktif);
                } else {
                    return new PemilikUMKM(id, username, password, "PEMILIK", aktif, null);
                }
            }
        }
        return null;
    }

    // Mengambil semua pengguna (Admin & Pemilik) untuk ditampilkan di tabel Admin.
    public static ArrayList<Pengguna> ambilSemua() {
        ArrayList<Pengguna> daftar = new ArrayList<Pengguna>();
        String sql = "SELECT * FROM pengguna ORDER BY id_pengguna";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id_pengguna");
                String username = rs.getString("username");
                String password = rs.getString("password");
                boolean aktif = "aktif".equalsIgnoreCase(rs.getString("status"));   // kolom status: aktif atau nonaktif (huruf besar/kecil tidak dibedakan)

                // Sama seperti login(): role menentukan objek anak yang dibuat (POLYMORPHISM)
                if (rs.getString("role").equalsIgnoreCase("Admin")) {
                    daftar.add(new Admin(id, username, password, aktif));
                } else {
                    daftar.add(new PemilikUMKM(id, username, password, "PEMILIK", aktif, null));
                }
            }
        } catch (Exception e) {
            System.out.println("Gagal ambil daftar pengguna: " + e.getMessage());
        }
        return daftar;
    }

    // Menyimpan status aktif/nonaktif milik satu pengguna ke database.
    public static boolean updateStatus(Pengguna pengguna) {
        String sql = "UPDATE pengguna SET status = ? WHERE id_pengguna = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, statusTeks(pengguna.isAktif()));
            ps.setInt(2, pengguna.getId());
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("Gagal update status: " + e.getMessage());
            return false;
        }
    }

    // Menyimpan akun baru (PemilikUMKM atau Admin) ke database. Mengembalikan true kalau berhasil.
    // Dipanggil dari Pengguna.registrasi(). Role diambil dari objek: "PEMILIK" atau "ADMIN".
    public static boolean daftarPengguna(Pengguna pengguna) {
        try (Connection conn = DatabaseConnection.getConnection()) {

            // LANGKAH 1: simpan akun ke tabel pengguna.
            // Kalau username sudah dipakai, MySQL melempar error (kolom UNIQUE) -> masuk catch -> false
            PreparedStatement ps1 = conn.prepareStatement(
                    "INSERT INTO pengguna (username, password, role, status) VALUES (?, ?, ?, ?)");
            ps1.setString(1, pengguna.getUsername());
            ps1.setString(2, pengguna.getPassword());
            ps1.setString(3, pengguna.getRole());
            ps1.setString(4, statusTeks(pengguna.isAktif()));
            ps1.executeUpdate();

            // Admin tidak punya profil usaha, jadi selesai di sini
            if (pengguna.getRole().equalsIgnoreCase("Admin")) {
                return true;
            }

            // LANGKAH 2: ambil id_pengguna yang baru dibuat
            PreparedStatement ps2 = conn.prepareStatement(
                    "SELECT id_pengguna FROM pengguna WHERE username = ?");
            ps2.setString(1, pengguna.getUsername());
            ResultSet rs = ps2.executeQuery();
            rs.next();
            int idPengguna = rs.getInt("id_pengguna");

            // LANGKAH 3: buat profil usaha awal (masih "Belum diisi") untuk pemilik ini
            PreparedStatement ps3 = conn.prepareStatement(
                    "INSERT INTO profil_umkm (id_pengguna, nama_usaha, jenis_usaha, alamat) "
                            + "VALUES (?, 'Belum diisi', 'Belum diisi', 'Belum diisi')");
            ps3.setInt(1, idPengguna);
            ps3.executeUpdate();

            return true;
        } catch (Exception e) {
            System.out.println("Gagal daftar: " + e.getMessage());
            return false;
        }
    }
}
