package com.example.database;

import com.example.model.Admin;
import com.example.model.PemilikUMKM;
import com.example.model.Pengguna;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class PenggunaDAO {

    
    private static String statusTeks(boolean aktif) {
        if (aktif) {
            return "aktif";
        }
        return "nonaktif";
    }

    
    public static Pengguna login(String username, String password) throws SQLException {
        String sql = "SELECT * FROM pengguna WHERE username = ? AND password = ?";
       
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);   // isi tanda tanya ke-1
            ps.setString(2, password);   // isi tanda tanya ke-2
            ResultSet rs = ps.executeQuery();   // executeQuery untuk SELECT

            if (rs.next()) {
                int id = rs.getInt("id_pengguna");
                String role = rs.getString("role");
                boolean aktif = "aktif".equalsIgnoreCase(rs.getString("status"));   // kolom status: aktif atau nonaktif (huruf besar/kecil tidak dibedakan)

                
                if (role.equalsIgnoreCase("Admin")) {
                    return new Admin(id, username, password, aktif);
                } else {
                    return new PemilikUMKM(id, username, password, "PEMILIK", aktif, null);
                }
            }
        }
        return null;
    }

    
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

 
    public static boolean daftarPengguna(Pengguna pengguna) {
        try (Connection conn = DatabaseConnection.getConnection()) {

          
            PreparedStatement ps1 = conn.prepareStatement(
                    "INSERT INTO pengguna (username, password, role, status) VALUES (?, ?, ?, ?)");
            ps1.setString(1, pengguna.getUsername());
            ps1.setString(2, pengguna.getPassword());
            ps1.setString(3, pengguna.getRole());
            ps1.setString(4, statusTeks(pengguna.isAktif()));
            ps1.executeUpdate();

           
            if (pengguna.getRole().equalsIgnoreCase("Admin")) {
                return true;
            }

           
            PreparedStatement ps2 = conn.prepareStatement(
                    "SELECT id_pengguna FROM pengguna WHERE username = ?");
            ps2.setString(1, pengguna.getUsername());
            ResultSet rs = ps2.executeQuery();
            rs.next();
            int idPengguna = rs.getInt("id_pengguna");

           
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
