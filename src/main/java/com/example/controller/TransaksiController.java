package com.example.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import com.example.App;
import com.example.model.KategoriTransaksi;
import com.example.model.Pemasukan;
import com.example.model.PemilikUMKM;
import com.example.model.Pengeluaran;
import com.example.model.ProfilUMKM;
import com.example.model.Transaksi;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

/**
 * Menghubungkan View (transaksi.fxml) dengan Model PBO (Transaksi, Pemasukan, Pengeluaran).
 */
public class TransaksiController {

    @FXML private Label lblTotalSaldo;
    @FXML private Label lblTotalPemasukan;
    @FXML private Label lblTotalPengeluaran;

    @FXML private RadioButton rbPemasukan;
    @FXML private RadioButton rbPengeluaran;
    @FXML private ToggleGroup jenisGroup;

    @FXML private TextField txtNominal;
    @FXML private ComboBox<KategoriTransaksi> cbKategori;
    @FXML private DatePicker dpTanggal;
    @FXML private TextField txtCatatan;
    @FXML private Label lblStatus;

    @FXML private DatePicker dpFilterDari;
    @FXML private DatePicker dpFilterSampai;
    @FXML private ComboBox<String> cbFilterKategori;

    @FXML private TableView<Transaksi> tblTransaksi;
    @FXML private TableColumn<Transaksi, String> colTanggal;
    @FXML private TableColumn<Transaksi, String> colJenis;
    @FXML private TableColumn<Transaksi, String> colKategori;
    @FXML private TableColumn<Transaksi, String> colNominal;
    @FXML private TableColumn<Transaksi, String> colDampak;
    @FXML private TableColumn<Transaksi, String> colCatatan;

    private static final ObservableList<Transaksi> daftarTransaksi = FXCollections.observableArrayList();
    private final ObservableList<Transaksi> daftarTampil = FXCollections.observableArrayList();
    private static final String SEMUA_KATEGORI = "Semua Kategori";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private final com.example.database.TransaksiDAO transaksiDAO = new com.example.database.TransaksiDAO();

    @FXML
    public void initialize() {
        daftarTransaksi.clear();
        ProfilUMKM profil = ambilProfilPengguna();
        if (profil != null) {
            java.util.List<Transaksi> dataDb = transaksiDAO.getTransaksiByIdUsaha(profil.getIdUsaha());
            daftarTransaksi.addAll(dataDb);
        }

        cbKategori.getItems().setAll(KategoriTransaksi.values());
        dpTanggal.setValue(LocalDate.now());

        colTanggal.setCellValueFactory(cellData -> {
            LocalDateTime tgl = cellData.getValue().getTanggal();
            return new SimpleStringProperty(tgl != null ? tgl.format(formatter) : "-");
        });

        colJenis.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getJenisTransaksi()));

        colKategori.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getKategori() != null ? cellData.getValue().getKategori().name() : "-"));

        colNominal.setCellValueFactory(cellData ->
            new SimpleStringProperty(String.format("Rp %,.2f", cellData.getValue().getNominal())));

        colDampak.setCellValueFactory(cellData ->
            new SimpleStringProperty(String.format("%+,.2f", cellData.getValue().hitungDampakSaldo())));

        colCatatan.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getCatatan() != null ? cellData.getValue().getCatatan() : "-"));

        cbFilterKategori.getItems().add(SEMUA_KATEGORI);
        for (KategoriTransaksi pilihan : KategoriTransaksi.values()) {
            cbFilterKategori.getItems().add(pilihan.name());
        }
        cbFilterKategori.setValue(SEMUA_KATEGORI);
        tblTransaksi.setItems(daftarTampil);
        terapkanFilter();
        updateRingkasanKeuangan();
    }

    @FXML
    private void onSimpanTransaksi() {
        lblStatus.setText("");

        String nominalStr = txtNominal.getText();
        if (nominalStr == null || nominalStr.trim().isEmpty()) {
            tampilkanError("Nominal transaksi tidak boleh kosong!");
            return;
        }

        double nominal;
        try {
            nominal = Double.parseDouble(nominalStr.trim().replace(",", ""));
        } catch (NumberFormatException e) {
            tampilkanError("Nominal transaksi harus berupa angka yang valid!");
            return;
        }

        KategoriTransaksi kategori = cbKategori.getValue();
        if (kategori == null) {
            tampilkanError("Silakan pilih kategori transaksi!");
            return;
        }

        LocalDate tglInput = dpTanggal.getValue() != null ? dpTanggal.getValue() : LocalDate.now();
        LocalDateTime waktuTransaksi = LocalDateTime.of(tglInput, LocalTime.now());

        String catatan = txtCatatan.getText() != null ? txtCatatan.getText().trim() : "";

        ProfilUMKM profil = ambilProfilPengguna();
        if (profil == null) {
            tampilkanError("Lengkapi Profil Usaha terlebih dahulu sebelum mencatat transaksi.");
            return;
        }

        try {
            Transaksi transaksiBaru;
            if (rbPemasukan.isSelected()) {
                transaksiBaru = new Pemasukan(0, nominal, waktuTransaksi, catatan, kategori);
            } else {
                transaksiBaru = new Pengeluaran(0, nominal, waktuTransaksi, catatan, kategori);
            }

            transaksiBaru.setIdUsaha(profil.getIdUsaha());

            boolean dbSuccess = transaksiDAO.simpanTransaksi(transaksiBaru);

            if (dbSuccess) {
                profil.tambahTransaksi(transaksiBaru);
            }

            daftarTransaksi.add(0, transaksiBaru);
            terapkanFilter();
            updateRingkasanKeuangan();

            onResetForm();
            lblStatus.setStyle("-fx-text-fill: #16A34A; -fx-font-weight: bold;");
            if (dbSuccess) {
                lblStatus.setText("✓ Transaksi (" + transaksiBaru.getJenisTransaksi() + ") berhasil disimpan ke Database!");
            } else {
                lblStatus.setText("✓ Transaksi (" + transaksiBaru.getJenisTransaksi() + ") berhasil dicatat ke tabel!");
            }

        } catch (IllegalArgumentException ex) {
            // Menangkap validasi FR-07 dari class Transaksi (nominal <= 0)
            tampilkanError(ex.getMessage());
        }
    }

    private void terapkanFilter() {
        LocalDate dari = dpFilterDari.getValue();
        LocalDate sampai = dpFilterSampai.getValue();
        String kategoriPilihan = cbFilterKategori.getValue();

        daftarTampil.clear();
        for (Transaksi transaksi : daftarTransaksi) {
            boolean lolos = true;

            LocalDate tanggalTransaksi = null;
            if (transaksi.getTanggal() != null) {
                tanggalTransaksi = transaksi.getTanggal().toLocalDate();
            }
            if (dari != null && tanggalTransaksi != null && tanggalTransaksi.isBefore(dari)) {
                lolos = false;
            }
            if (sampai != null && tanggalTransaksi != null && tanggalTransaksi.isAfter(sampai)) {
                lolos = false;
            }

            if (kategoriPilihan != null && !kategoriPilihan.equals(SEMUA_KATEGORI)) {
                if (transaksi.getKategori() == null || !transaksi.getKategori().name().equals(kategoriPilihan)) {
                    lolos = false;
                }
            }

            if (lolos) {
                daftarTampil.add(transaksi);
            }
        }
    }

    @FXML
    private void onTerapkanFilter() {
        LocalDate dari = dpFilterDari.getValue();
        LocalDate sampai = dpFilterSampai.getValue();
        if (dari != null && sampai != null && dari.isAfter(sampai)) {
            tampilkanError("Tanggal \"Dari\" tidak boleh lebih besar dari tanggal \"Sampai\".");
            return;
        }
        terapkanFilter();
    }

    @FXML
    private void onResetFilter() {
        dpFilterDari.setValue(null);
        dpFilterSampai.setValue(null);
        cbFilterKategori.setValue(SEMUA_KATEGORI);
        terapkanFilter();
    }

    // HAPUS TRANSAKSI: menghapus baris yang dipilih di tabel riwayat (dengan konfirmasi).
    @FXML
    private void onHapusTransaksi() {
        // LANGKAH 1: ambil baris yang dipilih (null kalau belum ada yang dipilih)
        Transaksi dipilih = tblTransaksi.getSelectionModel().getSelectedItem();
        if (dipilih == null) {
            tampilkanError("Pilih dulu transaksi yang ingin dihapus!");
            return;
        }

        // LANGKAH 2: minta konfirmasi, karena transaksi yang dihapus tidak bisa dikembalikan
        Alert konfirmasi = new Alert(Alert.AlertType.CONFIRMATION);
        konfirmasi.setHeaderText(null);
        konfirmasi.setContentText("Hapus transaksi ini? Tindakan ini tidak bisa dibatalkan.");
        Optional<ButtonType> jawaban = konfirmasi.showAndWait();
        if (!jawaban.isPresent() || jawaban.get() != ButtonType.OK) {
            return;
        }

        // LANGKAH 3: hapus
        hapusTransaksiTerpilih(dipilih);
    }

    // Menghapus transaksi dari database, dari daftar di tabel, dan dari profil di memori,
    // lalu menyegarkan tabel dan kartu ringkasan. Dipisah dari onHapusTransaksi() supaya satu method = satu tugas.
    private void hapusTransaksiTerpilih(Transaksi transaksi) {
        boolean berhasil = transaksiDAO.hapusTransaksi(transaksi.getId());
        if (!berhasil) {
            tampilkanError("Gagal menghapus transaksi dari database.");
            return;
        }

        daftarTransaksi.remove(transaksi);
        hapusDariProfil(transaksi.getId());
        terapkanFilter();
        updateRingkasanKeuangan();

        lblStatus.setStyle("-fx-text-fill: #16A34A; -fx-font-weight: bold;");
        lblStatus.setText("✓ Transaksi berhasil dihapus.");
    }

    // Menghapus transaksi dengan id tertentu dari daftar transaksi milik ProfilUMKM,
    // supaya angka di halaman Beranda ikut berubah.
    private void hapusDariProfil(int idTransaksi) {
        ProfilUMKM profil = ambilProfilPengguna();
        if (profil == null) {
            return;
        }
        java.util.List<Transaksi> daftarProfil = profil.getDaftarTransaksi();
        for (int i = 0; i < daftarProfil.size(); i++) {
            if (daftarProfil.get(i).getId() == idTransaksi) {
                daftarProfil.remove(i);
                return;
            }
        }
    }

    private ProfilUMKM ambilProfilPengguna() {
        if (!(App.currentUser instanceof PemilikUMKM)) {
            return null;
        }
        PemilikUMKM pemilik = (PemilikUMKM) App.currentUser;
        return pemilik.getProfilUMKM();
    }

    @FXML
    private void onResetForm() {
        txtNominal.clear();
        cbKategori.getSelectionModel().clearSelection();
        dpTanggal.setValue(LocalDate.now());
        txtCatatan.clear();
        rbPemasukan.setSelected(true);
    }

    private void updateRingkasanKeuangan() {
        double saldoKas = 0;
        double totalPemasukan = 0;
        double totalPengeluaran = 0;

        for (Transaksi t : daftarTransaksi) {
            saldoKas += t.hitungDampakSaldo();

            if (t instanceof Pemasukan) {
                totalPemasukan += t.getNominal();
            } else if (t instanceof Pengeluaran) {
                totalPengeluaran += t.getNominal();
            }
        }

        lblTotalSaldo.setText(String.format("Rp %,.2f", saldoKas));
        lblTotalPemasukan.setText(String.format("Rp %,.2f", totalPemasukan));
        lblTotalPengeluaran.setText(String.format("Rp %,.2f", totalPengeluaran));
    }

    private void tampilkanError(String pesan) {
        lblStatus.setStyle("-fx-text-fill: #DC2626; -fx-font-weight: bold;");
        lblStatus.setText("⚠ " + pesan);
    }
}
