package com.example.database;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

/*
 * KELAS: DatabaseConnection
 * Membuka koneksi ke MySQL (Laragon, port 3306).
 * Password TIDAK ditulis di kode supaya aman ikut ke GitHub.
 *
 * Urutan mencari password:
 *   1. Variabel lingkungan DB_PASSWORD (kalau ada).
 *   2. File ".kurtera_db_password" di folder user Windows (contoh: C:\Users\Bryan\.kurtera_db_password).
 *      Isinya satu baris: password MySQL. File ini ada di luar proyek, jadi tidak ikut ke GitHub.
 *   3. Kalau dua-duanya tidak ada, dipakai password kosong (Laragon bawaan).
 */
public class DatabaseConnection {

    private static final String URL =
        "jdbc:mysql://127.0.0.1:3306/sistemkelayakan_umkm";

    private static final String USER = "root";

    // LANGKAH 1: cari password dari variabel lingkungan
    private static String ambilPasswordDariVariabel() {
        String password = System.getenv("DB_PASSWORD");
        if (password == null) {
            return "";
        }
        return password;
    }

    // LANGKAH 2: cari password dari file di folder user (baris pertama saja)
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
