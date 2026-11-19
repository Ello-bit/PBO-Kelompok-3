package com.example;

import com.example.model.PemilikUMKM;
import com.example.model.ProfilUMKM;
import com.example.model.Transaksi;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Penyimpanan sementara bersama selama aplikasi berjalan.
 * Data ini belum persisten ke MySQL; DAO akan diintegrasikan setelah siap.
 */
public final class DataAplikasi {
    private static PemilikUMKM pemilikAktif;
    private static final ObservableList<Transaksi> daftarTransaksi =
            FXCollections.observableArrayList();

    private DataAplikasi() {}

    public static synchronized PemilikUMKM getPemilikAktif() {
        if (pemilikAktif == null) {
            pemilikAktif = new PemilikUMKM(1, "pengguna", "");
        }
        return pemilikAktif;
    }

    public static synchronized ProfilUMKM getProfilAktif() {
        return getPemilikAktif().getProfilUMKM();
    }

    public static synchronized void setProfilAktif(ProfilUMKM profil) {
        getPemilikAktif().setProfilUMKM(profil);
    }

    public static ObservableList<Transaksi> getDaftarTransaksi() {
        return daftarTransaksi;
    }
}
