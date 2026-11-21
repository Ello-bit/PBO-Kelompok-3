package com.example.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.model.KategoriTransaksi;
import com.example.model.Pemasukan;
import com.example.model.Pengeluaran;
import com.example.model.Transaksi;
import com.example.database.DatabaseConnection;


public class TransaksiDAO {


    public boolean simpanTransaksi(Transaksi transaksi) {
        String sql = "INSERT INTO transaksi (id_usaha, nominal, tanggal, catatan, kategori) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

        
            int idUsaha = transaksi.getIdUsaha() > 0 ? transaksi.getIdUsaha() : 1;
            stmt.setInt(1, idUsaha);
            stmt.setDouble(2, transaksi.getNominal());

            LocalDateTime tgl = transaksi.getTanggal() != null ? transaksi.getTanggal() : LocalDateTime.now();
            stmt.setTimestamp(3, Timestamp.valueOf(tgl));

            stmt.setString(4, transaksi.getCatatan());
            stmt.setString(5, transaksi.getKategori() != null ? transaksi.getKategori().name() : KategoriTransaksi.PENJUALAN.name());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
            
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

    
        if (kategori == KategoriTransaksi.OPERASIONAL) {
            return new Pengeluaran(id, idUsaha, nominal, tanggal, catatan, kategori);
        } else {
            return new Pemasukan(id, idUsaha, nominal, tanggal, catatan, kategori);
        }
    }
}
