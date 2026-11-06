CREATE DATABASE IF NOT EXISTS krs_db;
USE krs_db;

CREATE TABLE IF NOT EXISTS mahasiswa (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nim VARCHAR(20) NOT NULL UNIQUE,
    nama VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS matakuliah (
    id INT AUTO_INCREMENT PRIMARY KEY,
    kode_mk VARCHAR(20) NOT NULL UNIQUE,
    nama_mk VARCHAR(100) NOT NULL,
    sks INT NOT NULL,
    semester INT NOT NULL,
    dosen VARCHAR(100) NOT NULL,
    kuota INT DEFAULT 30
);

CREATE TABLE IF NOT EXISTS krs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    mahasiswa_id INT NOT NULL,
    matakuliah_id INT NOT NULL,
    tanggal_ambil TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (mahasiswa_id) REFERENCES mahasiswa(id) ON DELETE CASCADE,
    FOREIGN KEY (matakuliah_id) REFERENCES matakuliah(id) ON DELETE CASCADE,
    UNIQUE KEY unique_pengambilan (mahasiswa_id, matakuliah_id)
);
