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
        admin = (Admin) App.currentUser;
        
        kolomId.setCellValueFactory(new PropertyValueFactory<>("idLaporan"));
        kolomNamaUsaha.setCellValueFactory(new PropertyValueFactory<>("namaUsaha"));
        kolomTanggal.setCellValueFactory(new PropertyValueFactory<>("tanggalPembuatan"));
        kolomFile.setCellValueFactory(new PropertyValueFactory<>("lokasiFilePDF"));

        refreshTabel();
    }
    
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
        
        LaporanPengajuanKUR dipilih = tabelLaporan.getSelectionModel().getSelectedItem();
        if (dipilih == null) {
            App.tampilkanPesan("Pilih dulu laporan yang ingin disetujui.");
            return;
        }

        
        admin.verifikasiLaporan(dipilih, true, "-");

        
        simpanDanRefresh(dipilih);
    }

    @FXML
    private void tolak() {
        
        LaporanPengajuanKUR dipilih = tabelLaporan.getSelectionModel().getSelectedItem();
        if (dipilih == null) {
            App.tampilkanPesan("Pilih dulu laporan yang ingin ditolak.");
            return;
        }

        
        String alasan = alasanField.getText().trim();
        if (alasan.isEmpty()) {
            App.tampilkanPesan("Alasan penolakan wajib diisi.");
            return;
        }

        
        admin.verifikasiLaporan(dipilih, false, alasan);

        
        simpanDanRefresh(dipilih);
    }
    
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
