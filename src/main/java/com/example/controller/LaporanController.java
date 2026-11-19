package com.example.controller;

import java.nio.file.Path;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.example.App;
import com.example.database.LaporanDAO;
import com.example.model.LaporanPengajuanKUR;
import com.example.model.Pemasukan;
import com.example.model.PemilikUMKM;
import com.example.model.Pengeluaran;
import com.example.model.ProfilUMKM;
import com.example.model.SkorKelayakan;
import com.example.model.Transaksi;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class LaporanController {

    @FXML private Label namaUsahaLabel;
    @FXML private Label jenisUsahaLabel;
    @FXML private Label alamatLabel;
    @FXML private Label totalPemasukanLabel;
    @FXML private Label totalPengeluaranLabel;
    @FXML private Label labaBersihLabel;
    @FXML private Label skorLabel;
    @FXML private Label levelLabel;

    @FXML private TableView<RiwayatLaporanRow> riwayatLaporanTable;
    @FXML private TableColumn<RiwayatLaporanRow, String> idColumn;
    @FXML private TableColumn<RiwayatLaporanRow, String> tanggalColumn;
    @FXML private TableColumn<RiwayatLaporanRow, String> statusColumn;

    @FXML private Label nomorLaporanLabel;
    @FXML private Label previewNamaUsaha;
    @FXML private Label previewJenisUsaha;
    @FXML private Label previewAlamat;
    @FXML private Label previewPemasukan;
    @FXML private Label previewPengeluaran;
    @FXML private Label previewLaba;
    @FXML private Label previewSkor;
    @FXML private Label previewLevel;
    @FXML private Label previewTanggal;

    @FXML private TableView<LaporanPengajuanKUR.TransaksiLaporan> previewTransaksiTable;
    @FXML private TableColumn<LaporanPengajuanKUR.TransaksiLaporan, String> transaksiTanggalColumn;
    @FXML private TableColumn<LaporanPengajuanKUR.TransaksiLaporan, String> transaksiCatatanColumn;
    @FXML private TableColumn<LaporanPengajuanKUR.TransaksiLaporan, String> transaksiJenisColumn;
    @FXML private TableColumn<LaporanPengajuanKUR.TransaksiLaporan, String> transaksiNominalColumn;

    private final String idLaporan = "KUR-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    private String namaUsaha = "-";
    private String jenisUsaha = "-";
    private String alamat = "-";
    private double totalPemasukan;
    private double totalPengeluaran;
    private double labaBersih;
    // Skor diisi dari SkorController; -1 berarti belum tersedia.
    private int skor = -1;
    private int level = -1;
    private List<LaporanPengajuanKUR.TransaksiLaporan> transaksi = new ArrayList<>();

    @FXML
    public void initialize() {
        aturKolomTabel();
        muatDataAktual();
        tampilkanData();
        muatRiwayat();
    }

    private void aturKolomTabel() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        tanggalColumn.setCellValueFactory(new PropertyValueFactory<>("tanggal"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        transaksiTanggalColumn.setCellValueFactory(new PropertyValueFactory<>("tanggal"));
        transaksiCatatanColumn.setCellValueFactory(new PropertyValueFactory<>("keterangan"));
        transaksiJenisColumn.setCellValueFactory(new PropertyValueFactory<>("jenis"));
        transaksiNominalColumn.setCellValueFactory(new PropertyValueFactory<>("nominalFormat"));
    }

    // Mengambil profil usaha milik pengguna yang sedang login.
    // Hasilnya null kalau yang login bukan pemilik atau profilnya belum ada.
    private ProfilUMKM ambilProfilPengguna() {
        if (!(App.currentUser instanceof PemilikUMKM)) {
            return null;
        }
        PemilikUMKM pemilik = (PemilikUMKM) App.currentUser;
        return pemilik.getProfilUMKM();
    }

    // Menghitung skor kelayakan terbaru untuk usaha ini (lewat SkorController).
    // Kalau gagal (misalnya database mati), skor dan level dibiarkan "-1" yang tampil sebagai "-".
    private void muatSkor(int idUsaha) {
        skor = -1;
        level = -1;
        try {
            SkorController skorController = new SkorController();
            SkorKelayakan hasil = skorController.prosesSkor(idUsaha);
            skor = (int) Math.round(hasil.getNilaiSkor());
            level = hasil.getLevel();
        } catch (Exception e) {
            System.out.println("Gagal memuat skor untuk laporan: " + e.getMessage());
        }
    }

    // Mengisi tabel riwayat dengan laporan milik usaha ini dari database.
    private void muatRiwayat() {
        riwayatLaporanTable.getItems().clear();
        ProfilUMKM profil = ambilProfilPengguna();
        if (profil == null) {
            return;
        }
        List<LaporanPengajuanKUR> daftar = LaporanDAO.ambilLaporanUsaha(profil.getIdUsaha());
        for (LaporanPengajuanKUR laporan : daftar) {
            String status = "Menunggu verifikasi";
            if (laporan.getStatusVerifikasi().equals("TERVERIFIKASI")) {
                status = "Terverifikasi";
            }
            if (laporan.getStatusVerifikasi().equals("DITOLAK")) {
                status = "Ditolak: " + laporan.getAlasanPenolakan();
            }
            riwayatLaporanTable.getItems().add(new RiwayatLaporanRow(
                    String.valueOf(laporan.getIdLaporan()),
                    laporan.getTanggalPembuatan(),
                    status));
        }
    }

    private void muatDataAktual() {
        ProfilUMKM profil = ambilProfilPengguna();
        if (profil != null) {
            namaUsaha = kosongKeStrip(profil.getNamaUsaha());
            jenisUsaha = kosongKeStrip(profil.getJenisUsaha());
            alamat = kosongKeStrip(profil.getAlamat());
            muatSkor(profil.getIdUsaha());
        }

        totalPemasukan = 0;
        totalPengeluaran = 0;
        labaBersih = 0;
        transaksi = new ArrayList<>();

        List<Transaksi> daftarTransaksi = new ArrayList<>();
        if (profil != null) {
            daftarTransaksi = profil.getDaftarTransaksi();
        }

        DateTimeFormatter formatTanggal = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        for (Transaksi item : daftarTransaksi) {
            String tanggal = item.getTanggal() == null ? "-" : item.getTanggal().format(formatTanggal);
            String jenis = item.getJenisTransaksi();
            String catatan = kosongKeStrip(item.getCatatan());

            if (item instanceof Pemasukan pemasukan) {
                totalPemasukan += item.getNominal();
                if (pemasukan.isPendapatanUsaha()) {
                    labaBersih += item.getNominal();
                }
            } else if (item instanceof Pengeluaran pengeluaran) {
                totalPengeluaran += item.getNominal();
                if (pengeluaran.isBebanUsaha()) {
                    labaBersih -= item.getNominal();
                }
            }

            transaksi.add(new LaporanPengajuanKUR.TransaksiLaporan(
                    tanggal, catatan, jenis, item.getNominal()));
        }
    }

    private void tampilkanData() {
        String pemasukan = rupiah(totalPemasukan);
        String pengeluaran = rupiah(totalPengeluaran);
        String labaFormat = rupiah(labaBersih);
        String tanggal = LocalDate.now().format(
                DateTimeFormatter.ofPattern("dd MMMM yyyy", new Locale("id", "ID")));
        String skorText = skor < 0 ? "-" : String.valueOf(skor);
        String levelText = level < 0 ? "-" : "Level " + level;

        namaUsahaLabel.setText(namaUsaha);
        jenisUsahaLabel.setText(jenisUsaha);
        alamatLabel.setText(alamat);
        totalPemasukanLabel.setText(pemasukan);
        totalPengeluaranLabel.setText(pengeluaran);
        labaBersihLabel.setText(labaFormat);
        skorLabel.setText(skorText);
        levelLabel.setText(levelText);

        nomorLaporanLabel.setText("Nomor: " + idLaporan);
        previewNamaUsaha.setText(namaUsaha);
        previewJenisUsaha.setText(jenisUsaha);
        previewAlamat.setText(alamat);
        previewPemasukan.setText(pemasukan);
        previewPengeluaran.setText(pengeluaran);
        previewLaba.setText(labaFormat);
        previewSkor.setText(skorText);
        previewLevel.setText(levelText);
        previewTanggal.setText("Tanggal pembuatan: " + tanggal);

        previewTransaksiTable.setItems(FXCollections.observableArrayList(transaksi));
    }

    private String kosongKeStrip(String nilai) {
        return nilai == null || nilai.isBlank() ? "-" : nilai;
    }

    private String rupiah(double nilai) {
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        format.setMaximumFractionDigits(0);
        return format.format(nilai);
    }

    @FXML
    private void handlePratinjau() {
        muatDataAktual();
        tampilkanData();
    }

    @FXML
    private void handleBuatLaporan() {
        muatDataAktual();

        if (namaUsaha.equals("-") || jenisUsaha.equals("-") || alamat.equals("-")) {
            tampilkanAlert(Alert.AlertType.WARNING, "Profil belum lengkap",
                    "Isi dan simpan Profil UMKM terlebih dahulu sebelum mengekspor laporan.");
            return;
        }

        if (transaksi.isEmpty()) {
            tampilkanAlert(Alert.AlertType.WARNING, "Transaksi belum tersedia",
                    "Catat minimal satu transaksi terlebih dahulu agar laporan berisi data keuangan aktual.");
            return;
        }

        try {
            Path folderLaporan = Path.of(System.getProperty("user.home"), "Downloads");
            Path lokasiPdf = folderLaporan.resolve(idLaporan + ".pdf");

            LaporanPengajuanKUR laporan = new LaporanPengajuanKUR(
                    idLaporan, namaUsaha, jenisUsaha, alamat,
                    totalPemasukan, totalPengeluaran, labaBersih, skor, level,
                    transaksi, lokasiPdf.toString());
            laporan.exportToPDF();

            // Catat pengajuan di database supaya Admin bisa memverifikasinya
            laporan.setIdUsaha(ambilProfilPengguna().getIdUsaha());
            boolean tersimpan = LaporanDAO.simpanLaporan(laporan);
            muatRiwayat();

            String pesan = "Laporan PDF berhasil dibuat.\nFile tersimpan di:\n" + laporan.getFilePath();
            if (!tersimpan) {
                pesan = pesan + "\n\nPengajuan belum tercatat di database (periksa koneksi MySQL).";
            }
            tampilkanAlert(Alert.AlertType.INFORMATION, "Ekspor berhasil", pesan);
        } catch (Exception e) {
            tampilkanAlert(Alert.AlertType.ERROR, "Ekspor gagal",
                    "Laporan PDF tidak dapat dibuat:\n" + e.getMessage());
        }
    }

    private void tampilkanAlert(Alert.AlertType tipe, String judul, String pesan) {
        Alert alert = new Alert(tipe);
        alert.setTitle(judul);
        alert.setHeaderText(null);
        alert.setContentText(pesan);
        alert.showAndWait();
    }

    public static class RiwayatLaporanRow {
        private final String id;
        private final String tanggal;
        private final String status;
        public RiwayatLaporanRow(String id, String tanggal, String status) {
            this.id = id;
            this.tanggal = tanggal;
            this.status = status;
        }
        public String getId() { return id; }
        public String getTanggal() { return tanggal; }
        public String getStatus() { return status; }
    }
}
