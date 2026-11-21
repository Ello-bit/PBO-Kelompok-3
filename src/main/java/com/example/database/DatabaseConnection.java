package com.example.database;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class DatabaseConnection {

    private static final String URL =
        "jdbc:mysql://127.0.0.1:3306/sistemkelayakan_umkm";

    private static final String USER = "root";

    private static String ambilPasswordDariVariabel() {

        String password = System.getenv("DB_PASSWORD");
        if (password == null) {
            return "";
        }
        return password;
    }

    private static String ambilPasswordDariFile() {
        Path lokasiFile = Path.of(System.getProperty("user.home"), ".kurtera_db_password");

        try {
            if (Files.exists(lokasiFile)) {
                List<String> semuaBaris = Files.readAllLines(lokasiFile);
                if (!semuaBaris.isEmpty()) {
                    return semuaBaris.get(0).trim();
                }
            }
        } catch (Exception e) {
            System.out.println("Gagal membaca file password database: " + e.getMessage());
        }
        return "";
    }

    private static String ambilPassword() {
        String password = ambilPasswordDariVariabel();

        if (password.isEmpty()) {
            password = ambilPasswordDariFile();
        }
        return password;
    }

    public static Connection getConnection()
            throws SQLException {

        String password = ambilPassword();
        return DriverManager.getConnection(
            URL, USER, password
        );
    }
}