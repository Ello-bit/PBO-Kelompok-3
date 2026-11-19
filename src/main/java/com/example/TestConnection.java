package com.example;

import com.example.config.DatabaseConnection;
import java.sql.Connection;

public class TestConnection {

    public static void main(String[] args) {

        try (Connection connection =
                 DatabaseConnection.getConnection()) {

            System.out.println("Koneksi berhasil!");

            System.out.println(
                "Database: " + connection.getCatalog()
            );

        } catch (Exception e) {

            System.out.println("Koneksi gagal!");

            e.printStackTrace();
        }
    }
}
