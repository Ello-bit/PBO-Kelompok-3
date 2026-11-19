package com.example.database;

import com.example.database.DatabaseConnection;
import com.example.model.SkorKelayakan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SkorDAO {

    public void simpanSkor(int idUsaha, SkorKelayakan skor)
            throws SQLException {

        String sql = """
            INSERT INTO skor_kelayakan
                (id_usaha, periode, nilai_skor, level)
            VALUES (?, ?, ?, ?)
            ON DUPLICATE KEY UPDATE
                nilai_skor = VALUES(nilai_skor),
                level = VALUES(level)
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUsaha);
            stmt.setString(2, skor.getPeriode());
            stmt.setDouble(3, skor.getNilaiSkor());
            stmt.setInt(4, skor.getLevel());

            stmt.executeUpdate();
        }
    }
}