package com.example.controller;

import com.example.App;
import com.example.database.LaporanDAO;
import com.example.model.Admin;
import com.example.model.LaporanPengajuanKUR;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/*
 * Controller halaman verifikasi_laporan.fxml (khusus Admin).
 * KONSEP OOP:
 *  - INHERITANCE / ENCAPSULATION : keputusan diberikan lewat method milik Admin
 *                                  (verifikasiLaporan), yang mengubah isi objek laporan lewat setter.
 *                                  Penyimpanan ke database dilakukan di sini, lewat LaporanDAO.
 */
public class VerifikasiLaporanController {

    @FXML private TableView<LaporanPengajuanKUR> tabelLaporan;
    @FXML private TableColumn<LaporanPengajuanKUR, Integer> kolomId;
    @FXML private TableColumn<LaporanPengajuanKUR, String> kolomNamaUsaha;
    @FXML private TableColumn<LaporanPengajuanKUR, String> kolomTanggal;
    @FXML private TableColumn<LaporanPengajuanKUR, String> kolomFile;
    @FXML private TextField alasanField;

    private Admin admin;

    @FXML
    public void initialize() {
        admin = (Admin) App.currentUser;   // CASTING

        kolomId.setCellValueFactory(new PropertyValueFactory<>("idLaporan"));
        kolomNamaUsaha.setCellValueFactory(new PropertyValueFactory<>("namaUsaha"));
        kolomTanggal.setCellValueFactory(new PropertyValueFactory<>("tanggalPembuatan"));
        kolomFile.setCellValueFactory(new PropertyValueFactory<>("lokasiFilePDF"));

        refreshTabel();
    }

    // Membuka file PDF laporan yang dipilih, supaya Admin bisa memeriksa isinya sebelum memutuskan
    @FXML
    private void bukaLaporan() {
        LaporanPengajuanKUR dipilih = tabelLaporan.getSelectionModel().getSelectedItem();
        if (dipilih == null) {
            App.tampilkanPesan("Pilih dulu laporan yang ingin dibuka.");
            return;
        }
        App.bukaFile(dipilih.getLokasiFilePDF());
    }

    @FXML
    private void setujui() {
        // LANGKAH 1: ambil laporan yang dipilih (null kalau belum ada yang dipilih)
        LaporanPengajuanKUR dipilih = tabelLaporan.getSelectionModel().getSelectedItem();
        if (dipilih == null) {
            App.tampilkanPesan("Pilih dulu laporan yang ingin disetujui.");
            return;
        }

        // LANGKAH 2: ubah status objek jadi TERVERIFIKASI (disetujui = true)
        admin.verifikasiLaporan(dipilih, true, "-");

        // LANGKAH 3: simpan ke database, lalu perbarui tabel
        simpanDanRefresh(dipilih);
    }

    @FXML
    private void tolak() {
        // LANGKAH 1: ambil laporan yang dipilih
        LaporanPengajuanKUR dipilih = tabelLaporan.getSelectionModel().getSelectedItem();
        if (dipilih == null) {
            App.tampilkanPesan("Pilih dulu laporan yang ingin ditolak.");
            return;
        }

        // LANGKAH 2: menolak WAJIB disertai alasan (supaya pemilik tahu apa yang harus diperbaiki)
        String alasan = alasanField.getText().trim();
        if (alasan.isEmpty()) {
            App.tampilkanPesan("Alasan penolakan wajib diisi.");
            return;
        }

        // LANGKAH 3: ubah status objek jadi DITOLAK beserta alasannya (disetujui = false)
        admin.verifikasiLaporan(dipilih, false, alasan);

        // LANGKAH 4: simpan ke database, lalu perbarui tabel
        simpanDanRefresh(dipilih);
    }

    // Dipakai oleh setujui() dan tolak(): simpan hasil verifikasi, kosongkan alasan, muat ulang tabel.
    // Laporan yang sudah diverifikasi otomatis hilang dari tabel karena statusnya bukan MENUNGGU lagi.
    private void simpanDanRefresh(LaporanPengajuanKUR laporan) {
        if (!LaporanDAO.updateStatus(laporan)) {
            App.tampilkanPesan("Gagal menyimpan hasil verifikasi.");
        }
        alasanField.clear();
        refreshTabel();
    }

    private void refreshTabel() {
        tabelLaporan.getItems().setAll(LaporanDAO.ambilMenunggu());
    }
}
