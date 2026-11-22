==============================================================
KURTERA - Sistem Manajemen Keuangan UMKM
Tugas Kelompok PBO (Kelas C, Kelompok 3) - Universitas Udayana
==============================================================

TENTANG APLIKASI
----------------
KURTERA adalah aplikasi desktop (Java + JavaFX + MySQL) untuk:
- Pemilik UMKM : mengelola profil usaha, mencatat transaksi, melihat
                 ringkasan keuangan, menghitung skor kelayakan, dan
                 membuat laporan pengajuan KUR (PDF).
- Admin        : mengelola akun pengguna dan memverifikasi laporan
                 pengajuan KUR.


1. KEBUTUHAN
------------
- JDK 21 atau lebih baru (proyek dikompilasi dengan Java 21).
- Apache Maven 3.9 atau lebih baru.
- MySQL 8 atau 9 (disarankan lewat Laragon, port 3306).
- Koneksi internet saat pertama kali menjalankan, karena Maven
  mengunduh pustaka (JavaFX, PDFBox, MySQL Connector/J).

Cek instalasi di terminal:
    java -version
    mvn -v


2. SIAPKAN DATABASE
-------------------
a. Nyalakan MySQL di Laragon (klik "Start All"). MySQL harus berjalan
   di port 3306.
   Catatan: kalau Windows punya service MySQL resmi (misalnya "MySQL93")
   yang ikut menyala, matikan dulu agar tidak berebut port 3306.

b. Buat database dan tabel dengan menjalankan file:
       src/main/resources/database/schema.sql
   Cara 1 (terminal, dari folder proyek):
       mysql -u root -p < src/main/resources/database/schema.sql
   Cara 2: buka file itu di HeidiSQL (tombol "Database" di Laragon),
   lalu jalankan seluruh isinya.

   Hasilnya: database "sistemkelayakan_umkm" dengan 5 tabel
   (pengguna, profil_umkm, transaksi, laporan_kur, skor_kelayakan).
   Skrip ini aman dijalankan ulang (memakai IF NOT EXISTS), tetapi tidak
   mengubah tabel yang sudah ada.

c. Kalau database lama kamu dibuat sebelum kolom lokasi file PDF ada,
   jalankan sekali:
       ALTER TABLE laporan_kur ADD COLUMN lokasi_file_pdf VARCHAR(255);


3. ATUR PASSWORD MYSQL
----------------------
Password MySQL TIDAK ditulis di dalam kode. Aplikasi mencarinya dengan
urutan berikut:

  1) Variabel lingkungan DB_PASSWORD.
  2) File ".kurtera_db_password" di folder user Windows
     (contoh: C:\Users\NamaKamu\.kurtera_db_password). Isinya satu
     baris saja: password MySQL.
  3) Kalau keduanya tidak ada, dipakai password kosong
     (cocok untuk Laragon bawaan: user root tanpa password).

Jika MySQL kamu TANPA password, kamu tidak perlu melakukan apa-apa.

Jika MySQL kamu memakai password, pilih salah satu:

  Cara A - variabel lingkungan (PowerShell, cukup sekali):
      [Environment]::SetEnvironmentVariable("DB_PASSWORD", "ISI_PASSWORD_KAMU", "User")
  Setelah itu TUTUP LALU BUKA ULANG terminal atau VS Code, karena
  variabel baru hanya terbaca oleh program yang dibuka sesudahnya.

  Cara B - file (tidak perlu membuka ulang terminal):
      buat file .kurtera_db_password di folder user, isi dengan
      password MySQL pada baris pertama.

Koneksi yang dipakai aplikasi: 127.0.0.1:3306, database
sistemkelayakan_umkm, user root.


4. BUAT AKUN ADMIN PERTAMA
--------------------------
Halaman Daftar hanya membuat akun Pemilik UMKM. Akun Admin dibuat
lewat SQL, sekali saja. Ganti GANTI_PASSWORD dengan password pilihanmu:

    INSERT INTO pengguna (username, password, role, status)
    VALUES ('admin', 'GANTI_PASSWORD', 'Admin', 'aktif');

(Jalankan di HeidiSQL atau di terminal mysql pada database
sistemkelayakan_umkm.)


5. MENJALANKAN APLIKASI
-----------------------
Buka terminal di folder proyek (folder yang berisi pom.xml), lalu:

    mvn clean javafx:run

Jendela aplikasi akan terbuka di halaman Login.
Untuk hanya memeriksa bahwa kode bisa dikompilasi:

    mvn clean compile


6. CARA MEMAKAI
---------------
Pemilik UMKM
  1. Klik "Belum punya akun? Daftar", isi nama, username, (nama usaha
     boleh dikosongkan), password, dan konfirmasi password.
  2. Login dengan username dan password tadi.
  3. Menu "Profil Usaha": lengkapi nama usaha, jenis usaha, alamat.
  4. Menu "Transaksi": catat pemasukan atau pengeluaran beserta
     kategorinya (Modal, Operasional, Penjualan). Riwayat bisa
     disaring berdasarkan tanggal dan kategori, dan transaksi bisa
     dihapus lewat tombol "Hapus Transaksi Terpilih".
  5. Menu "Beranda": ringkasan keuangan dan grafik, dengan pilihan
     periode (semua periode atau per bulan).
  6. Menu "Skor Kelayakan": skor 0-100 dan level 1-5 dari transaksi
     enam bulan terakhir (dihitung otomatis saat halaman dibuka).
  7. Menu "Laporan KUR": buat laporan PDF. Laporan berstatus
     "menunggu verifikasi" sampai diperiksa Admin.

Admin
  1. Login dengan akun Admin (lihat langkah 4).
  2. "Kelola Pengguna": tambah akun pemilik, aktifkan atau nonaktifkan
     akun (Admin tidak bisa menonaktifkan akunnya sendiri).
  3. "Verifikasi Laporan": pilih laporan, buka file PDF-nya untuk
     diperiksa, lalu Setujui atau Tolak (menolak wajib disertai alasan).

Lokasi file PDF
  - Saat laporan dibuat: folder .kurtera\laporan di folder user
    (contoh: C:\Users\NamaKamu\.kurtera\laporan).
  - Setelah laporan disetujui Admin, pemilik bisa mengunduh salinannya
    ke folder Downloads lewat tombol unduh.


7. MASALAH UMUM
---------------
"Tidak dapat terhubung ke database"
   - MySQL belum menyala (Start All di Laragon).
   - Password salah, atau variabel DB_PASSWORD belum terbaca. Tutup dan
     buka ulang terminal/VS Code, atau pakai file .kurtera_db_password.
   - Port 3306 dipakai MySQL lain. Matikan service MySQL yang bentrok.

"Username atau password salah" padahal sudah benar
   - Biasanya akibat masalah koneksi di atas. Coba login ulang setelah
     MySQL dan password beres.

"Unknown column 'lokasi_file_pdf'"
   - Tabel laporan_kur versi lama. Jalankan perintah ALTER TABLE di
     bagian 2c.

Pustaka tidak terunduh / build gagal
   - Pastikan internet aktif, lalu ulangi: mvn clean javafx:run

Aplikasi tidak mau jalan dengan JDK lama
   - Gunakan JDK 21 atau lebih baru.


8. STRUKTUR FOLDER SINGKAT
--------------------------
src/main/java/com/example/
    App.java          titik masuk aplikasi dan perpindahan halaman
    model/            kelas objek (Pengguna, Admin, PemilikUMKM,
                      Transaksi, Pemasukan, Pengeluaran, ProfilUMKM,
                      SkorKelayakan, LaporanPengajuanKUR, dst.)
    database/         koneksi dan semua query SQL (kelas ...DAO)
    controller/       penghubung tampilan dengan model dan database
    util/             kelas pendukung
src/main/resources/
    com/example/      file tampilan (.fxml) dan style (style/*.css)
    database/         schema.sql
pom.xml               konfigurasi Maven (dependensi dan plugin JavaFX)
