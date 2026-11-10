CREATE DATABASE IF NOT EXISTS sistemkelayakan_umkm;

USE sistemkelayakan_umkm;


CREATE TABLE IF NOT EXISTS pengguna (
    id_pengguna INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('Admin', 'pemilik') NOT NULL,
    status ENUM('aktif', 'nonaktif') DEFAULT 'aktif'
);