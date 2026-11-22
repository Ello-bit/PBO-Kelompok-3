package com.example.database;

import com.example.model.ProfilUMKM;
import com.example.model.Transaksi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class ProfilDAO {

    public static ProfilUMKM ambilProfilPemilik(int idPengguna) {
        String sql = "SELECT * FROM profil_umkm WHERE id_pengguna = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPengguna);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                return null;
            }

            int idUsaha = rs.getInt("id_usaha");
            String namaUsaha = rs.getString("nama_usaha");
            String jenisUsaha = rs.getString("jenis_usaha");
            String alamat = rs.getString("alamat");
            ProfilUMKM profil = new ProfilUMKM(idUsaha, namaUsaha, jenisUsaha, alamat);

            isiTransaksi(profil);
            return profil;
        } catch (Exception e) {
            System.out.println("Gagal ambil profil: " + e.getMessage());
            return null;
        }
    }

    public static void isiTransaksi(ProfilUMKM profil) {
        TransaksiDAO transaksiDAO = new TransaksiDAO();
        List<Transaksi> daftar = transaksiDAO.getTransaksiByIdUsaha(profil.getIdUsaha());
        profil.getDaftarTransaksi().clear();
        for (Transaksi transaksi : daftar) {
            profil.tambahTransaksi(transaksi);
        }
    }

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
