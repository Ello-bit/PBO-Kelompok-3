package com.example.app.models;

public class Admin extends Pengguna {

    public Admin (int id, String username, String password){
        super(id, username, password);
    }
    
    @Override 
    public void registrasi(){
        System.out.println("Akun Admin Dikelola oleh Sistem");
    }

    @Override 
    public void getDashboard(){
        System.out.println("=== DASHBOARD ADMIN ===");
    }

    public void kelolaPengguna() {
        System.out.println("Admin sedang Mengelola Pengguna");
    }

    public void verifikasiLaporan() {
        System.out.println("Admin sedang Memverifikasi Laporan");
    }
}