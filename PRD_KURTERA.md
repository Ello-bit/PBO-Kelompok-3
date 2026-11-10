# PRODUCT REQUIREMENTS DOCUMENT
# KURTERA — Sistem Manajemen Keuangan UMKM

## Informasi Dokumen

| Informasi | Keterangan |
|---|---|
| Status | Draf; dapat disesuaikan kembali |
| Jenis Produk | Aplikasi desktop |
| Bahasa Pemrograman | Java |
| Framework GUI | JavaFX |
| Basis Data | MySQL |
| Koneksi Basis Data | JDBC |

Dokumen ini digunakan sebagai acuan tugas kelompok Pemrograman Berorientasi Objek sekaligus panduan awal implementasi aplikasi.

---

## 1. Pendahuluan

### 1.1 Latar Belakang

Usaha Mikro, Kecil, dan Menengah (UMKM) merupakan salah satu penggerak perekonomian. Namun, sebagian pelaku UMKM masih melakukan pencatatan keuangan secara sederhana sehingga mengalami kesulitan dalam memantau arus kas, mengelompokkan transaksi, dan mengevaluasi kondisi keuangan usahanya.

KURTERA merupakan aplikasi desktop yang dirancang untuk membantu pemilik UMKM mengelola keuangan usaha secara terstruktur. Aplikasi menyediakan pencatatan transaksi pemasukan dan pengeluaran, ringkasan keuangan, perhitungan skor kelayakan usaha, serta pembuatan laporan PDF sebagai dokumen pendukung pengajuan Kredit Usaha Rakyat (KUR). Sistem juga menyediakan peran Admin untuk mengelola pengguna dan memverifikasi laporan.

### 1.2 Tujuan Produk

1. Mempermudah pencatatan transaksi keuangan UMKM.
2. Membantu pemilik usaha memantau pemasukan, pengeluaran, dan laba bersih.
3. Memberikan gambaran awal kelayakan usaha berdasarkan data keuangan.
4. Menyimpan riwayat skor kelayakan berdasarkan periode.
5. Menghasilkan laporan PDF yang memuat profil UMKM, transaksi, dan skor kelayakan.
6. Memungkinkan Admin mengelola akun pengguna dan memverifikasi laporan pengajuan KUR.

### 1.3 Sasaran Pengembangan

PRD ini digunakan sebagai dokumen kebutuhan produk untuk tugas PBO, pedoman bagi tim dalam merancang antarmuka dan mengembangkan fitur, serta acuan untuk mengevaluasi kesesuaian implementasi dengan rancangan sistem. Dokumen ini merupakan draf yang dapat diperbarui berdasarkan diskusi tim dan arahan dosen.

---

## 2. Gambaran Umum Produk

### 2.1 Deskripsi Produk

KURTERA adalah aplikasi desktop berbasis Java yang mengintegrasikan pencatatan transaksi keuangan dan penilaian kelayakan usaha. Pemilik UMKM dapat mengelola profil usaha, mencatat transaksi, melihat ringkasan keuangan, menghitung skor kelayakan, serta membuat laporan PDF. Admin dapat mengelola pengguna dan memeriksa laporan.

JavaFX digunakan untuk membangun antarmuka, MySQL untuk menyimpan data, dan JDBC untuk menghubungkan aplikasi dengan basis data.

### 2.2 Target Pengguna

**A. Pemilik UMKM**

- Mengakses aplikasi menggunakan akun.
- Mengelola profil UMKM.
- Mencatat pemasukan dan pengeluaran.
- Melihat ringkasan keuangan.
- Mengetahui skor dan level kelayakan usaha.
- Membuat laporan PDF.

**B. Admin**

- Melihat daftar pengguna.
- Menambahkan, mengubah, dan menonaktifkan akun pengguna.
- Melihat laporan yang menunggu verifikasi.
- Menyetujui atau menolak laporan berdasarkan hasil pemeriksaan.

### 2.3 Platform dan Teknologi

| Komponen | Teknologi |
|---|---|
| Platform | Desktop |
| Bahasa pemrograman | Java |
| Antarmuka pengguna | JavaFX |
| Basis data | MySQL |
| Koneksi basis data | JDBC |
| Format laporan | PDF |
| Metode autentikasi | Username dan password |

---

## 3. Ruang Lingkup Produk

### 3.1 Autentikasi Pengguna

Sistem menyediakan registrasi, login, dan logout. Pengguna login menggunakan username dan password. Setelah berhasil masuk, sistem menampilkan dashboard sesuai peran Pemilik UMKM atau Admin.

- Sistem memvalidasi kredensial pengguna.
- Login gagal menghasilkan pesan kesalahan.
- Menu menyesuaikan peran pengguna.
- Pengguna dapat mengakhiri sesi melalui logout.

### 3.2 Pengelolaan Profil UMKM

Pemilik UMKM dapat memasukkan dan mengelola ID usaha, nama usaha, jenis usaha, dan alamat usaha. Profil menjadi penghubung antara identitas usaha, transaksi keuangan, hasil penilaian, dan laporan pengajuan KUR. Berdasarkan rancangan awal, satu Pemilik UMKM memiliki satu Profil UMKM.

### 3.3 Pencatatan Transaksi Keuangan

Fitur ini digunakan oleh Pemilik UMKM untuk mencatat aktivitas keuangan usaha, baik berupa pemasukan maupun pengeluaran melalui method `tambahTransaksi()`. Setiap transaksi menyimpan informasi berupa nominal, tanggal, catatan, serta kategori transaksi yang terdiri dari Modal, Operasional, dan Penjualan.

Setiap transaksi akan diproses melalui method `hitungDampakSaldo()` untuk menghitung pengaruhnya terhadap saldo usaha. Transaksi Modal dan Penjualan akan menambah saldo usaha, sedangkan transaksi Operasional akan mengurangi saldo usaha. Selain itu, kategori transaksi digunakan untuk membedakan perhitungan laba bersih, yaitu Penjualan dihitung sebagai pendapatan usaha, Operasional sebagai beban usaha, sedangkan Modal tidak dihitung sebagai pendapatan. Dengan demikian, sistem dapat menampilkan saldo usaha dan laba bersih secara terpisah.

### 3.4 Dashboard Keuangan

Dashboard menampilkan total pemasukan, total pengeluaran, dan laba bersih berdasarkan periode yang dipilih.

**Belum ditentukan:** bentuk visualisasi, penggunaan grafik, dan rincian filter periode.

### 3.5 Skor Kelayakan Usaha

Sistem mengolah data keuangan untuk menghasilkan skor kelayakan pada periode tertentu. Rancangan awal menyebutkan konsistensi pencatatan transaksi, rasio laba, dan kestabilan arus kas sebagai faktor penilaian. Hasil ditampilkan sebagai skor dan level kelayakan 1–5, serta disimpan sebagai riwayat.

**BELUM DITENTUKAN:** rumus perhitungan, bobot indikator, rentang nilai setiap level, periode minimum data, penanganan data yang tidak mencukupi, dan aturan rekomendasi perbaikan. Ketentuan ini perlu disepakati sebelum fitur penilaian diimplementasikan secara final.

Skor merupakan gambaran awal kondisi usaha dan bukan jaminan persetujuan KUR oleh lembaga pembiayaan.

### 3.6 Laporan Pengajuan KUR

Pemilik UMKM dapat menghasilkan laporan PDF yang memuat profil UMKM, ringkasan atau riwayat transaksi, dan skor kelayakan usaha. Sistem menyimpan tanggal pembuatan dan lokasi file PDF. Laporan kemudian memasuki proses verifikasi Admin.

**Belum ditentukan:** desain akhir PDF, format periode transaksi, dan informasi tambahan yang dicantumkan.

### 3.7 Manajemen Pengguna

Admin dapat melihat daftar pengguna, menambah pengguna, mengubah data, dan menonaktifkan akun setelah memberikan konfirmasi. Daftar pengguna diperbarui setelah perubahan.

**Belum ditentukan:** apakah registrasi mandiri hanya tersedia untuk Pemilik UMKM dan bagaimana akun Admin pertama kali dibuat.

### 3.8 Verifikasi Laporan

1. Sistem menampilkan laporan yang menunggu verifikasi.
2. Admin memilih dan memeriksa laporan.
3. Jika sesuai, status diubah menjadi Terverifikasi.
4. Jika tidak sesuai, status diubah menjadi Ditolak dan disertai alasan.
5. Pemilik UMKM dapat mengetahui hasil pemeriksaan dan memperbaiki laporan yang ditolak.

**Belum ditentukan:** apakah laporan yang ditolak dibuat ulang atau diperbarui melalui pengajuan ulang pada laporan yang sama.

---

## 4. Kebutuhan Fungsional

| ID | Nama Kebutuhan | Deskripsi |
|---|---|---|
| FR-01 | Registrasi | Pengguna dapat membuat akun sesuai aturan registrasi. |
| FR-02 | Login | Sistem memvalidasi username dan password. |
| FR-03 | Logout | Pengguna dapat mengakhiri sesi. |
| FR-04 | Dashboard berdasarkan peran | Menu menyesuaikan peran Pemilik UMKM atau Admin. |
| FR-05 | Profil UMKM | Pemilik UMKM mengelola informasi usaha. |
| FR-06 | Tambah transaksi | Pemilik UMKM mencatat pemasukan dan pengeluaran. |
| FR-07 | Validasi transaksi | Sistem menolak nominal kurang dari atau sama dengan nol. |
| FR-08 | Riwayat transaksi | Sistem menyimpan transaksi agar dapat ditelusuri. |
| FR-09 | Dashboard keuangan | Sistem menghitung pemasukan, pengeluaran, dan laba bersih. |
| FR-10 | Hitung skor kelayakan | Sistem menghitung skor menggunakan aturan yang disepakati. |
| FR-11 | Riwayat skor | Sistem menyimpan skor berdasarkan periode. |
| FR-12 | Ekspor PDF | Laporan memuat profil UMKM, transaksi, dan skor kelayakan. |
| FR-13 | Manajemen pengguna | Admin melihat, menambah, mengubah, dan menonaktifkan akun. |
| FR-14 | Verifikasi laporan | Admin memeriksa laporan dan menetapkan status. |
| FR-15 | Alasan penolakan | Sistem menyimpan atau menampilkan alasan penolakan. |
| FR-16 | Penyimpanan data | Data aplikasi disimpan pada MySQL. |

---

## 5. Kebutuhan Nonfungsional

### 5.1 Kemudahan Penggunaan

Antarmuka harus terstruktur dan menyediakan navigasi yang jelas agar pengguna dapat menemukan fitur sesuai perannya.

### 5.2 Keamanan

Sistem membatasi akses fitur berdasarkan peran dan mengelola kredensial serta informasi usaha dengan memperhatikan keamanan data.

**Belum ditentukan:** mekanisme penyimpanan password, kompleksitas password, dan kebijakan sesi login.

### 5.3 Integritas Data

Data profil, transaksi, skor, dan laporan harus saling terhubung secara konsisten. Sistem melakukan validasi input sebelum data disimpan.

### 5.4 Kompatibilitas

Aplikasi dikembangkan sebagai aplikasi desktop menggunakan Java dan JavaFX, dengan MySQL sebagai basis data.

**Belum ditentukan:** sistem operasi target, spesifikasi minimum komputer, dan metode distribusi aplikasi.

### 5.5 Pemeliharaan

Kode dikembangkan menggunakan konsep Pemrograman Berorientasi Objek agar terorganisasi dan lebih mudah dipelihara.

---

## 6. Struktur Kelas dan Tanggung Jawab

| Kelas / Interface | Tanggung Jawab |
|---|---|
| Pengguna | Kelas abstrak yang menyimpan identitas dan menyediakan fungsi registrasi, login, logout, dan dashboard. |
| PemilikUMKM | Turunan Pengguna yang memiliki profil UMKM. |
| Admin | Turunan Pengguna yang mengelola pengguna dan memverifikasi laporan. |
| ProfilUMKM | Menyimpan informasi usaha dan menyediakan `tambahTransaksi()`. |
| Transaksi | Kelas abstrak untuk data transaksi dan method `hitungDampakSaldo()`. |
| Pemasukan | Turunan Transaksi yang menambah saldo usaha. |
| Pengeluaran | Turunan Transaksi yang mengurangi saldo usaha. |
| KategoriTransaksi | Enum MODAL, OPERASIONAL, dan PENJUALAN. |
| SkorKelayakan | Menyimpan data penilaian dan menyediakan `hitungSkor()`. |
| Scoreable | Interface yang mendefinisikan `hitungSkor()`. |
| LaporanPengajuanKUR | Menyimpan informasi laporan dan menyediakan `exportToPDF()`. |
| Exportable | Interface yang mendefinisikan `exportToPDF()`. |

Konsep PBO yang digunakan meliputi abstract class, inheritance, interface, polymorphism, encapsulation, enumeration, dan association.

---

## 7. Rancangan Data

| Entitas | Data Utama |
|---|---|
| Pengguna | ID, username, password |
| PemilikUMKM | Referensi pengguna dan profil usaha |
| Admin | Referensi pengguna |
| ProfilUMKM | ID usaha, nama usaha, jenis usaha, alamat |
| Transaksi | ID transaksi, ID usaha, nominal, tanggal, catatan |
| SkorKelayakan | ID skor, ID usaha, periode, nilai skor, level |
| LaporanPengajuanKUR | ID laporan, ID usaha, tanggal pembuatan, lokasi file PDF |
| Pemasukan | Referensi transaksi pemasukan |
| Pengeluaran | Referensi transaksi pengeluaran |

Relasi mengikuti rancangan awal: satu profil usaha dapat memiliki banyak transaksi, riwayat skor, dan laporan. Satu skor kelayakan dapat digunakan oleh beberapa laporan. Pemilik UMKM dan Admin merupakan turunan Pengguna, sedangkan Pemasukan dan Pengeluaran merupakan turunan Transaksi.

**Catatan implementasi:** struktur tabel, foreign key, pemetaan pewarisan kelas ke tabel, dan atribut status verifikasi perlu dipastikan kembali saat skema basis data final disusun.

---

## 8. Alur Penggunaan Sistem

### 8.1 Alur Pemilik UMKM

1. Pengguna membuka aplikasi.
2. Pengguna melakukan registrasi atau login.
3. Sistem memvalidasi akun dan menampilkan dashboard Pemilik UMKM.
4. Pengguna melengkapi profil usaha.
5. Pengguna mencatat transaksi pemasukan dan pengeluaran.
6. Sistem menyimpan transaksi dan menghitung dampaknya terhadap saldo.
7. Pengguna melihat ringkasan keuangan.
8. Pengguna menjalankan penilaian kelayakan berdasarkan periode yang dipilih.
9. Sistem menampilkan skor dan level kelayakan.
10. Pengguna membuat laporan PDF.
11. Laporan menunggu pemeriksaan Admin.
12. Pengguna melihat hasil verifikasi dan memperbaiki laporan jika diperlukan.

### 8.2 Alur Admin

1. Admin login menggunakan akun Admin.
2. Sistem menampilkan dashboard Admin.
3. Admin membuka daftar pengguna untuk mengelola akun jika diperlukan.
4. Admin membuka daftar laporan yang menunggu verifikasi.
5. Admin memilih dan memeriksa laporan.
6. Admin menetapkan status Terverifikasi atau Ditolak.
7. Jika laporan ditolak, sistem menyertakan alasan penolakan.
8. Sistem menyimpan hasil pemeriksaan.

---

## 9. Kriteria Penerimaan Produk

1. Pengguna dapat login dan logout menggunakan username dan password.
2. Sistem menampilkan fitur sesuai peran pengguna.
3. Pemilik UMKM dapat mengelola profil usaha.
4. Pemilik UMKM dapat mencatat transaksi pemasukan dan pengeluaran.
5. Sistem memvalidasi nominal transaksi sebelum menyimpannya.
6. Dashboard menampilkan ringkasan keuangan berdasarkan periode.
7. Sistem menghitung skor menggunakan rumus yang telah disepakati.
8. Sistem menyimpan riwayat skor kelayakan.
9. Sistem menghasilkan PDF berisi profil UMKM, transaksi, dan skor kelayakan.
10. Admin dapat mengelola akun pengguna.
11. Admin dapat memverifikasi atau menolak laporan beserta alasan penolakan.
12. Data utama tersimpan di MySQL dan dapat diakses kembali.

Kriteria yang bergantung pada aturan yang belum ditentukan, khususnya perhitungan skor, diuji setelah aturan tersebut disepakati.

---

## 10. Batasan dan Hal yang Belum Diputuskan

| No. | Hal yang Perlu Dipastikan | Status |
|---:|---|---|
| 1 | Rumus, bobot indikator, dan rentang skor kelayakan | Belum ditentukan |
| 2 | Bentuk rekomendasi perbaikan usaha | Belum ditentukan |
| 3 | Detail dashboard dan grafik | Belum ditentukan |
| 4 | Desain akhir PDF | Belum ditentukan |
| 5 | Aturan edit dan hapus transaksi | Belum ditentukan |
| 6 | Mekanisme pembuatan akun Admin | Belum ditentukan |
| 7 | Pengajuan ulang laporan yang ditolak | Belum ditentukan |
| 8 | Keamanan password dan sesi login | Belum ditentukan |
| 9 | Sistem operasi dan distribusi aplikasi | Belum ditentukan |
| 10 | Skema basis data final dan status verifikasi | Perlu dipastikan saat implementasi |

---

## 11. Penutup

PRD KURTERA menjadi acuan awal dalam pengembangan aplikasi pengelolaan keuangan UMKM dan penilaian kelayakan usaha. Dokumen disusun berdasarkan tujuan proyek, diagram kelas, deskripsi fungsionalitas, serta rancangan basis data pada dokumen tugas kelompok.

Fitur utama mengacu pada rancangan yang telah dibuat. Detail teknis yang belum disepakati akan ditinjau dan diperbarui berdasarkan diskusi tim, kebutuhan implementasi, serta arahan dosen. Dengan adanya PRD ini, tim diharapkan memiliki pemahaman yang sama mengenai tujuan produk, target pengguna, ruang lingkup fitur, struktur sistem, dan kriteria penerimaan.