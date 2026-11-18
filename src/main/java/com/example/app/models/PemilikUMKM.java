package com.example.app.models;

public class PemilikUMKM extends Pengguna {

    private ProfilUMKM profilUMKM;

    public PemilikUMKM(int id, String username, String password) {
        super(id, username, password);
    }

    public PemilikUMKM(int id, String username, String password,
                       ProfilUMKM profilUMKM) {
        super(id, username, password);
        this.profilUMKM = profilUMKM;
    }

    public PemilikUMKM(int id, String username, String password,
                       String role, boolean aktif,
                       ProfilUMKM profilUMKM) {
        super(id, username, password, role, aktif);
        this.profilUMKM = profilUMKM;
    }

    @Override
    public void getDashboard() {
        // Dashboard milik PemilikUMKM berbeda dari Admin
        if (profilUMKM == null) {
            System.out.println(
                "Dashboard Pemilik UMKM: belum ada profil usaha. Silakan lengkapi profil usaha."
            );
        } else {
            System.out.println(
                "Dashboard Pemilik UMKM - Usaha: "
                + profilUMKM.getNamaUsaha()
            );
        }
    }

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