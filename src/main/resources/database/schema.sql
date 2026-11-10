CREATE DATABASE IF NOT EXISTS sistemkelayakan_umkm;

USE sistemkelayakan_umkm;


CREATE TABLE IF NOT EXISTS pengguna (
    id_pengguna INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('Admin', 'pemilik') NOT NULL,
    status ENUM('aktif', 'nonaktif') DEFAULT 'aktif'
);


CREATE TABLE IF NOT EXISTS profil_umkm (
    id_usaha INT AUTO_INCREMENT PRIMARY KEY,
    id_pengguna INT NOT NULL UNIQUE,
    nama_usaha VARCHAR(100) NOT NULL,
    jenis_usaha VARCHAR(100) NOT NULL,
    alamat TEXT NOT NULL,

    CONSTRAINT fk_profil_pengguna
        FOREIGN KEY (id_pengguna)
        REFERENCES pengguna(id_pengguna)
);


CREATE TABLE IF NOT EXISTS transaksi (
    id_transaksi INT AUTO_INCREMENT PRIMARY KEY,
    id_usaha INT NOT NULL,
    nominal DECIMAL(15,2) NOT NULL,
    tanggal DATETIME NOT NULL,
    catatan TEXT,
    kategori ENUM(
        'MODAL',
        'OPERASIONAL',
        'PENJUALAN'
    ) NOT NULL,

    CONSTRAINT fk_transaksi_profil
        FOREIGN KEY (id_usaha)
        REFERENCES profil_umkm(id_usaha)
);


CREATE TABLE IF NOT EXISTS laporan_kur (
    id_laporan INT AUTO_INCREMENT PRIMARY KEY,
    id_usaha INT NOT NULL,
    tanggal_pengajuan DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status ENUM(
        'menunggu',
        'disetujui',
        'ditolak'
    ) NOT NULL DEFAULT 'menunggu',
    catatan_admin TEXT,

    CONSTRAINT fk_laporan_profil
        FOREIGN KEY (id_usaha)
        REFERENCES profil_umkm(id_usaha)
);
