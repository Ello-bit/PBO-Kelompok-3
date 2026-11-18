package com.example;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import com.example.app.models.KategoriTransaksi;
import com.example.app.models.Pemasukan;
import com.example.app.models.Pengeluaran;
import com.example.app.models.Transaksi;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

/**
 * Controller untuk menangani tampilan dan interaksi fitur Transaksi.
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

    @FXML private TableView<Transaksi> tblTransaksi;
    @FXML private TableColumn<Transaksi, String> colTanggal;
    @FXML private TableColumn<Transaksi, String> colJenis;
    @FXML private TableColumn<Transaksi, String> colKategori;
    @FXML private TableColumn<Transaksi, String> colNominal;
    @FXML private TableColumn<Transaksi, String> colDampak;
    @FXML private TableColumn<Transaksi, String> colCatatan;

    // Daftar transaksi bersama agar dapat dibaca halaman laporan.
    private static final ObservableList<Transaksi> daftarTransaksi = DataAplikasi.getDaftarTransaksi();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @FXML
    public void initialize() {
        // Inisialisasi ComboBox Kategori
        cbKategori.getItems().setAll(KategoriTransaksi.values());
        dpTanggal.setValue(LocalDate.now());

        // Konfigurasi kolom TableView menggunakan getter dari Model Transaksi
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

        // Polimorfisme: memanggil hitungDampakSaldo() langsung pada objek Transaksi
        colDampak.setCellValueFactory(cellData ->
            new SimpleStringProperty(String.format("%+,.2f", cellData.getValue().hitungDampakSaldo())));

        colCatatan.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getCatatan() != null ? cellData.getValue().getCatatan() : "-"));

        tblTransaksi.setItems(daftarTransaksi);
        updateRingkasanKeuangan();
    }

    @FXML
    private void onSimpanTransaksi() {
        lblStatus.setText("");

        // 1. Validasi Input Nominal
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

        // 2. Validasi Kategori
        KategoriTransaksi kategori = cbKategori.getValue();
        if (kategori == null) {
            tampilkanError("Silakan pilih kategori transaksi!");
            return;
        }

        // 3. Ambil Tanggal
        LocalDate tglInput = dpTanggal.getValue() != null ? dpTanggal.getValue() : LocalDate.now();
        LocalDateTime waktuTransaksi = LocalDateTime.of(tglInput, LocalTime.now());

        // 4. Catatan
        String catatan = txtCatatan.getText() != null ? txtCatatan.getText().trim() : "";

        // 5. Instansiasi Objek PBO (Polymorphism & Inheritance)
        try {
            Transaksi transaksiBaru;
            if (rbPemasukan.isSelected()) {
                // Subclass Pemasukan (+ dampak saldo)
                transaksiBaru = new Pemasukan(0, nominal, waktuTransaksi, catatan, kategori);
            } else {
                // Subclass Pengeluaran (- dampak saldo)
                transaksiBaru = new Pengeluaran(0, nominal, waktuTransaksi, catatan, kategori);
            }

            // Masukkan ke daftar transaksi
            daftarTransaksi.add(transaksiBaru);

            // Perbarui ringkasan saldo
            updateRingkasanKeuangan();

            // Reset form dan berikan feedback sukses
            onResetForm();
            lblStatus.setStyle("-fx-text-fill: #16A34A; -fx-font-weight: bold;");
            lblStatus.setText("Transaksi (" + transaksiBaru.getJenisTransaksi() + ") berhasil disimpan!");

        } catch (IllegalArgumentException ex) {
            // Menangkap validasi FR-07 dari class Transaksi (nominal <= 0)
            tampilkanError(ex.getMessage());
        }
    }

    @FXML
    private void onResetForm() {
        txtNominal.clear();
        cbKategori.getSelectionModel().clearSelection();
        dpTanggal.setValue(LocalDate.now());
        txtCatatan.clear();
        rbPemasukan.setSelected(true);
    }

    /**
     * Menghitung total saldo kas secara otomatis menggunakan Polimorfisme Transaksi.
     */
    private void updateRingkasanKeuangan() {
        double saldoKas = 0;
        double totalPemasukan = 0;
        double totalPengeluaran = 0;

        for (Transaksi t : daftarTransaksi) {
            // Polimorfisme: hitungDampakSaldo() otomatis mengembalikan (+) jika Pemasukan dan (-) jika Pengeluaran
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

    public static ObservableList<Transaksi> getDaftarTransaksi() {
        return daftarTransaksi;
    }
}
