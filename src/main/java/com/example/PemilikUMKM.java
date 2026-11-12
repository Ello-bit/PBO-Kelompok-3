package com.example;

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
    public String getDashboard() {
        // Dashboard milik PemilikUMKM berbeda dari Admin -> polymorphism
        if (profilUMKM == null) {
            return "Dashboard Pemilik UMKM: belum ada profil usaha. Silakan lengkapi profil usaha.";
        }
        return "Dashboard Pemilik UMKM - Usaha: " + profilUMKM.getNamaUsaha();
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