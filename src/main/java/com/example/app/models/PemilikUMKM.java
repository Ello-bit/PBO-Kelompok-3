package com.example.app.models;

/**
 * PemilikUMKM merupakan turunan (inheritance) dari Pengguna.
 * Memiliki atribut tambahan profilUMKM yang menghubungkan
 * pengguna dengan data usahanya (association 1:1 dengan ProfilUMKM).
 */
public class PemilikUMKM extends Pengguna {

    private ProfilUMKM profilUMKM; // association, bukan inheritance

    public PemilikUMKM(int id, String username, String password) {
        super(id, username, password); // memanggil constructor induk
    }

    public PemilikUMKM(int id, String username, String password, ProfilUMKM profilUMKM) {
        super(id, username, password);
        this.profilUMKM = profilUMKM;
    }

    @Override
    public void getDashboard() {
        // Dashboard milik PemilikUMKM berbeda dari Admin -> polymorphism
        if (profilUMKM == null) {
            System.out.println("Dashboard Pemilik UMKM: belum ada profil usaha. Silakan lengkapi profil usaha.");
        } else {
            System.out.println("Dashboard Pemilik UMKM - Usaha: " + profilUMKM.getNamaUsaha());
        }
    }

    /** Alias sesuai kesepakatan tim: dipakai oleh BerandaController & PenggunaDAO. */
    public ProfilUMKM getProfil() {
        return profilUMKM;
    }

    public ProfilUMKM getProfilUMKM() {
        return profilUMKM;
    }

    public void setProfilUMKM(ProfilUMKM profilUMKM) {
        this.profilUMKM = profilUMKM;
    }

    public boolean hasProfilUMKM() {
        return profilUMKM != null;
    }
}