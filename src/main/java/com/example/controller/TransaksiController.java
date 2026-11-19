package com.example.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

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

    // Filter riwayat transaksi (tanggal dan kategori)
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

    // Disimpan secara static agar data tidak hilang saat pengguna berpindah menu sidebar
    private static final ObservableList<Transaksi> daftarTransaksi = FXCollections.observableArrayList();
    // Daftar yang tampil di tabel: hasil penyaringan dari daftarTransaksi
    private final ObservableList<Transaksi> daftarTampil = FXCollections.observableArrayList();
    private static final String SEMUA_KATEGORI = "Semua Kategori";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private final com.example.database.TransaksiDAO transaksiDAO = new com.example.database.TransaksiDAO();

    @FXML
    public void initialize() {
        // Muat ulang riwayat transaksi milik usaha pengguna yang sedang login
        // (bukan semua usaha), supaya data pemilik lain tidak ikut tampil.
        daftarTransaksi.clear();
        ProfilUMKM profil = ambilProfilPengguna();
        if (profil != null) {
            java.util.List<Transaksi> dataDb = transaksiDAO.getTransaksiByIdUsaha(profil.getIdUsaha());
            daftarTransaksi.addAll(dataDb);
        }

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

        // Isi pilihan filter kategori, lalu tampilkan data tersaring di tabel
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

        // 4b. Transaksi harus milik sebuah usaha: ambil profil pengguna yang login
        ProfilUMKM profil = ambilProfilPengguna();
        if (profil == null) {
            tampilkanError("Lengkapi Profil Usaha terlebih dahulu sebelum mencatat transaksi.");
            return;
        }

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

            // Tandai transaksi ini milik usaha pengguna yang login
            transaksiBaru.setIdUsaha(profil.getIdUsaha());

            // Simpan ke database MySQL lewat TransaksiDAO
            boolean dbSuccess = transaksiDAO.simpanTransaksi(transaksiBaru);

            // Kalau tersimpan, tambahkan juga ke profil di memori supaya Beranda ikut berubah
            if (dbSuccess) {
                profil.tambahTransaksi(transaksiBaru);
            }

            // Masukkan ke daftar tampilan tabel
            daftarTransaksi.add(0, transaksiBaru);
            terapkanFilter();

            // Perbarui ringkasan saldo
            updateRingkasanKeuangan();

            // Reset form dan berikan feedback sukses
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

    // FILTER RIWAYAT: memilih transaksi yang tanggalnya ada di antara "Dari" dan "Sampai"
    // dan kategorinya sesuai pilihan. Kolom filter yang kosong tidak membatasi apa pun.
    private void terapkanFilter() {
        LocalDate dari = dpFilterDari.getValue();
        LocalDate sampai = dpFilterSampai.getValue();
        String kategoriPilihan = cbFilterKategori.getValue();

        daftarTampil.clear();
        for (Transaksi transaksi : daftarTransaksi) {
            boolean lolos = true;

            // Cek batas tanggal
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

            // Cek kategori
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

    // Mengambil profil usaha milik pengguna yang sedang login.
    // Hasilnya null kalau yang login bukan pemilik atau profilnya belum ada.
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
}
