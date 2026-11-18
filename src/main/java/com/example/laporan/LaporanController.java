package com.example.laporan;

import java.nio.file.Path;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.example.DataAplikasi;
import com.example.ProfilUMKM;
import com.example.app.models.Pemasukan;
import com.example.app.models.Pengeluaran;
import com.example.app.models.Transaksi;

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
    // Skor belum dihubungkan karena fitur skor kelayakan belum selesai.
    private int skor = -1;
    private int level = -1;
    private List<LaporanPengajuanKUR.TransaksiLaporan> transaksi = new ArrayList<>();

    @FXML
    public void initialize() {
        aturKolomTabel();
        muatDataAktual();
        tampilkanData();
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

    private void muatDataAktual() {
        ProfilUMKM profil = DataAplikasi.getProfilAktif();
        if (profil != null) {
            namaUsaha = kosongKeStrip(profil.getNamaUsaha());
            jenisUsaha = kosongKeStrip(profil.getJenisUsaha());
            alamat = kosongKeStrip(profil.getAlamat());
        }

        totalPemasukan = 0;
        totalPengeluaran = 0;
        labaBersih = 0;
        transaksi = new ArrayList<>();

        DateTimeFormatter formatTanggal = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        for (Transaksi item : DataAplikasi.getDaftarTransaksi()) {
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
            Path folderLaporan = Path.of(System.getProperty("user.home"), "KURTERA", "laporan");
            Path lokasiPdf = folderLaporan.resolve(idLaporan + ".pdf");

            LaporanPengajuanKUR laporan = new LaporanPengajuanKUR(
                    idLaporan, namaUsaha, jenisUsaha, alamat,
                    totalPemasukan, totalPengeluaran, labaBersih, skor, level,
                    transaksi, lokasiPdf.toString());
            laporan.exportToPDF();

            riwayatLaporanTable.getItems().setAll(
                    new RiwayatLaporanRow(idLaporan,
                            laporan.getTanggalGenerate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")),
                            "Menunggu verifikasi"));

            tampilkanAlert(Alert.AlertType.INFORMATION, "Ekspor berhasil",
                    "Laporan PDF berhasil dibuat.\nFile tersimpan di:\n" + laporan.getFilePath());
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
