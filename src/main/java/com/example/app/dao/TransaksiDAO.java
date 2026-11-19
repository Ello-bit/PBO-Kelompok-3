package com.example.app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.app.models.KategoriTransaksi;
import com.example.app.models.Pemasukan;
import com.example.app.models.Pengeluaran;
import com.example.app.models.Transaksi;
import com.example.config.DatabaseConnection;

/**
 * Data Access Object (DAO) untuk mengelola data transaksi di database MySQL.
 * Menyediakan operasi CRUD (Create, Read, Update, Delete) sesuai skema tabel 'transaksi'.
 */
public class TransaksiDAO {

    /**
     * Menyimpan transaksi baru ke dalam database MySQL.
     * Menggunakan PreparedStatement untuk keamanan terhadap SQL Injection.
     * @param transaksi Objek Transaksi (Pemasukan atau Pengeluaran)
     * @return true jika berhasil disimpan, false jika gagal
     */
    public boolean simpanTransaksi(Transaksi transaksi) {
        String sql = "INSERT INTO transaksi (id_usaha, nominal, tanggal, catatan, kategori) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Default id_usaha = 1 jika belum diset (agar tetap bisa jalan untuk testing awal)
            int idUsaha = transaksi.getIdUsaha() > 0 ? transaksi.getIdUsaha() : 1;
            stmt.setInt(1, idUsaha);
            stmt.setDouble(2, transaksi.getNominal());

            LocalDateTime tgl = transaksi.getTanggal() != null ? transaksi.getTanggal() : LocalDateTime.now();
            stmt.setTimestamp(3, Timestamp.valueOf(tgl));

            stmt.setString(4, transaksi.getCatatan());
            stmt.setString(5, transaksi.getKategori() != null ? transaksi.getKategori().name() : KategoriTransaksi.PENJUALAN.name());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                // Ambil ID auto-increment yang baru di-generate oleh database
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        transaksi.setId(generatedKeys.getInt(1));
                        transaksi.setIdTransaksi(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Gagal menyimpan transaksi ke database: " + e.getMessage());
        }
        return false;
    }

    /**
     * Mengambil transaksi 6 bulan terakhir untuk keperluan perhitungan skor kelayakan.
     * Digunakan oleh SkorController dan SkorKelayakan.
     * @param idUsaha ID profil UMKM
     * @return List transaksi 6 bulan terakhir
     */
    public List<Transaksi> ambilTransaksiEnamBulan(int idUsaha) throws SQLException {
        List<Transaksi> list = new ArrayList<>();
        String sql = "SELECT id_transaksi, id_usaha, nominal, tanggal, catatan, kategori " +
                     "FROM transaksi WHERE id_usaha = ? AND tanggal >= DATE_SUB(NOW(), INTERVAL 6 MONTH) " +
                     "ORDER BY tanggal DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUsaha);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToTransaksi(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Gagal mengambil transaksi 6 bulan: " + e.getMessage());
            return getTransaksiByIdUsaha(idUsaha);
        }
        return list;
    }

    /**
     * Mengambil semua riwayat transaksi untuk id_usaha tertentu dari database.
     * Menerapkan Polimorfisme: Baris kategori OPERASIONAL dijadikan Pengeluaran,
     * sedangkan MODAL dan PENJUALAN dijadikan Pemasukan.
     * @param idUsaha ID profil UMKM
     * @return List transaksi yang terurut dari yang terbaru
     */
    public List<Transaksi> getTransaksiByIdUsaha(int idUsaha) {
        List<Transaksi> list = new ArrayList<>();
        String sql = "SELECT id_transaksi, id_usaha, nominal, tanggal, catatan, kategori " +
                     "FROM transaksi WHERE id_usaha = ? ORDER BY tanggal DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUsaha);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToTransaksi(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Gagal mengambil data transaksi: " + e.getMessage());
        }
        return list;
    }

    /**
     * Mengambil seluruh data transaksi dari database tanpa filter id_usaha.
     * @return List seluruh transaksi
     */
    public List<Transaksi> getAllTransaksi() {
        List<Transaksi> list = new ArrayList<>();
        String sql = "SELECT id_transaksi, id_usaha, nominal, tanggal, catatan, kategori " +
                     "FROM transaksi ORDER BY tanggal DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(mapResultSetToTransaksi(rs));
            }
        } catch (SQLException e) {
            System.err.println("Gagal mengambil seluruh transaksi: " + e.getMessage());
        }
        return list;
    }

    /**
     * Menghapus transaksi berdasarkan ID dari database.
     * @param idTransaksi ID transaksi yang akan dihapus
     * @return true jika berhasil dihapus
     */
    public boolean hapusTransaksi(int idTransaksi) {
        String sql = "DELETE FROM transaksi WHERE id_transaksi = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idTransaksi);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Gagal menghapus transaksi ID " + idTransaksi + ": " + e.getMessage());
        }
        return false;
    }

    /**
     * Helper method untuk mengonversi baris ResultSet SQL menjadi Objek PBO Transaksi.
     * Konsep PBO: Polimorfisme & Factory Mapping
     */
    private Transaksi mapResultSetToTransaksi(ResultSet rs) throws SQLException {
        int id = rs.getInt("id_transaksi");
        int idUsaha = rs.getInt("id_usaha");
        double nominal = rs.getDouble("nominal");
        Timestamp timestamp = rs.getTimestamp("tanggal");
        LocalDateTime tanggal = timestamp != null ? timestamp.toLocalDateTime() : LocalDateTime.now();
        String catatan = rs.getString("catatan");
        String kategoriStr = rs.getString("kategori");

        KategoriTransaksi kategori;
        try {
            kategori = KategoriTransaksi.valueOf(kategoriStr.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            kategori = KategoriTransaksi.OPERASIONAL;
        }

        // Jika OPERASIONAL -> Objek Pengeluaran
        // Jika MODAL / PENJUALAN -> Objek Pemasukan
        if (kategori == KategoriTransaksi.OPERASIONAL) {
            return new Pengeluaran(id, idUsaha, nominal, tanggal, catatan, kategori);
        } else {
            return new Pemasukan(id, idUsaha, nominal, tanggal, catatan, kategori);
        }
    }
}
