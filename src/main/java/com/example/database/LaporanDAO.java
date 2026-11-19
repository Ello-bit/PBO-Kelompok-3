package com.example.database;

import com.example.model.LaporanPengajuanKUR;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/*
 * KELAS: LaporanDAO
 * Semua query SQL untuk tabel "laporan_kur" ada di sini.
 *
 * Kolom tabel: id_laporan, id_usaha, tanggal_pengajuan, status, catatan_admin.
 * Nilai kolom status di database: 'menunggu', 'disetujui', 'ditolak'.
 * Nilai di objek laporan:         "MENUNGGU", "TERVERIFIKASI", "DITOLAK".
 * Dua method kecil di bawah (statusKeDatabase & statusDariDatabase) menerjemahkannya.
 */
public class LaporanDAO {

    // Mengubah status objek menjadi nilai kolom status di database.
    private static String statusKeDatabase(String statusObjek) {
        if (statusObjek.equals("TERVERIFIKASI")) {
            return "disetujui";
        }
        if (statusObjek.equals("DITOLAK")) {
            return "ditolak";
        }
        return "menunggu";
    }

    // Mengubah nilai kolom status di database menjadi status objek.
    private static String statusDariDatabase(String statusDatabase) {
        if (statusDatabase.equals("disetujui")) {
            return "TERVERIFIKASI";
        }
        if (statusDatabase.equals("ditolak")) {
            return "DITOLAK";
        }
        return "MENUNGGU";
    }

    // Membuat objek laporan dari satu baris hasil query.
    // idSkor dan lokasi PDF tidak disimpan di tabel, jadi diisi 0 dan teks kosong.
    private static LaporanPengajuanKUR buatLaporanDariBaris(ResultSet rs) throws Exception {
        int idLaporan = rs.getInt("id_laporan");
        int idUsaha = rs.getInt("id_usaha");
        String tanggal = rs.getString("tanggal_pengajuan");
        String status = statusDariDatabase(rs.getString("status"));
        String catatan = rs.getString("catatan_admin");
        if (catatan == null) {
            catatan = "-";
        }
        return new LaporanPengajuanKUR(idLaporan, idUsaha, 0, tanggal, "", status, catatan);
    }

    // Menyimpan laporan baru. Status awal selalu 'menunggu'.
    public static boolean simpanLaporan(LaporanPengajuanKUR laporan) {
        String sql = "INSERT INTO laporan_kur (id_usaha, status) VALUES (?, 'menunggu')";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, laporan.getIdUsaha());
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("Gagal simpan laporan: " + e.getMessage());
            return false;
        }
    }

    // Mengambil semua laporan yang masih berstatus 'menunggu' (untuk diverifikasi Admin).
    // JOIN ke profil_umkm supaya Admin bisa melihat NAMA USAHA, bukan hanya id usaha.
    public static ArrayList<LaporanPengajuanKUR> ambilMenunggu() {
        ArrayList<LaporanPengajuanKUR> daftar = new ArrayList<LaporanPengajuanKUR>();
        String sql = "SELECT l.*, p.nama_usaha FROM laporan_kur l "
                + "LEFT JOIN profil_umkm p ON l.id_usaha = p.id_usaha "
                + "WHERE l.status = 'menunggu' ORDER BY l.id_laporan";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                LaporanPengajuanKUR laporan = buatLaporanDariBaris(rs);
                laporan.setNamaUsaha(rs.getString("nama_usaha"));
                daftar.add(laporan);
            }
        } catch (Exception e) {
            System.out.println("Gagal ambil laporan menunggu: " + e.getMessage());
        }
        return daftar;
    }

    // Menyimpan hasil verifikasi Admin (status + catatan admin) ke database.
    public static boolean updateStatus(LaporanPengajuanKUR laporan) {
        String sql = "UPDATE laporan_kur SET status = ?, catatan_admin = ? WHERE id_laporan = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, statusKeDatabase(laporan.getStatusVerifikasi()));
            ps.setString(2, laporan.getAlasanPenolakan());
            ps.setInt(3, laporan.getIdLaporan());
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("Gagal update status laporan: " + e.getMessage());
            return false;
        }
    }

    // Mengambil semua laporan milik satu usaha, yang terbaru di paling atas.
    // Dipakai pemilik untuk melihat status verifikasi dan catatan admin.
    public static ArrayList<LaporanPengajuanKUR> ambilLaporanUsaha(int idUsaha) {
        ArrayList<LaporanPengajuanKUR> daftar = new ArrayList<LaporanPengajuanKUR>();
        String sql = "SELECT * FROM laporan_kur WHERE id_usaha = ? ORDER BY id_laporan DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsaha);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                daftar.add(buatLaporanDariBaris(rs));
            }
        } catch (Exception e) {
            System.out.println("Gagal ambil laporan: " + e.getMessage());
        }
        return daftar;
    }
}
