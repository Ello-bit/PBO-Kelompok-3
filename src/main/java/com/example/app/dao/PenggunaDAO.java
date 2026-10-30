
package com.example.app.dao;

import com.example.config.DatabaseConnection;
import com.example.app.models.Admin;
import com.example.app.models.PemilikUMKM;
import com.example.app.models.Pengguna;
import com.example.app.util.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PenggunaDAO {

    public boolean registrasi(String username, String password) {

        String sql = """
            INSERT INTO pengguna (username, password, role, status)
            VALUES (?, ?, 'PEMILIK', 'AKTIF')
            """;

        String hashedPassword = PasswordUtil.hashPassword(password);

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Registrasi gagal: " + e.getMessage());
            return false;
        }
    }

    public Pengguna login(String username, String password) {

        String sql = """
            SELECT id_pengguna, username, password, role, status
            FROM pengguna
            WHERE username = ?
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {

                    String storedPassword = rs.getString("password");
                    String role = rs.getString("role");
                    String status = rs.getString("status");

                    if (!"AKTIF".equalsIgnoreCase(status)) {
                        return null;
                    }

                    if (!PasswordUtil.verifyPassword(
                            password, storedPassword)) {
                        return null;
                    }

                    int id = rs.getInt("id_pengguna");
                    String savedUsername = rs.getString("username");

                    if ("ADMIN".equalsIgnoreCase(role)) {
                        return new Admin(
                            id, savedUsername, storedPassword
                        );
                    }

                    if ("PEMILIK".equalsIgnoreCase(role)) {
                        return new PemilikUMKM(
                            id, savedUsername, storedPassword, null
                        );
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Login gagal: " + e.getMessage());
        }

        return null;
    }
}