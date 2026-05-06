-- ============================================
-- Script Setup Database: pemdas_quiz
-- Jalankan di phpMyAdmin atau MySQL CLI
-- ============================================

CREATE DATABASE IF NOT EXISTS pemdas_quiz CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE pemdas_quiz;

-- Tabel Pelanggan
CREATE TABLE IF NOT EXISTS tb_pelanggan (
    kd_pelanggan VARCHAR(10) PRIMARY KEY,
    nama_pelanggan VARCHAR(100) NOT NULL,
    alamat TEXT,
    no_telp VARCHAR(15)
) ENGINE=InnoDB;

-- Tabel Supplier
CREATE TABLE IF NOT EXISTS tb_supplier (
    kd_supplier VARCHAR(10) PRIMARY KEY,
    nama_supplier VARCHAR(100) NOT NULL,
    alamat TEXT,
    no_telp VARCHAR(15)
) ENGINE=InnoDB;

-- Tabel Barang
CREATE TABLE IF NOT EXISTS tb_barang (
    kd_barang VARCHAR(10) PRIMARY KEY,
    nama_barang VARCHAR(100) NOT NULL,
    satuan VARCHAR(20),
    harga_jual DECIMAL(10,2) DEFAULT 0,
    harga_beli_stok DECIMAL(10,2) DEFAULT 0
) ENGINE=InnoDB;

-- Tabel Pembelian (Header Transaksi)
CREATE TABLE IF NOT EXISTS tb_pembelian (
    kd_pembelian VARCHAR(15) PRIMARY KEY,
    tgl_pembelian DATE,
    kd_supplier VARCHAR(10),
    total_bayar DECIMAL(10,2) DEFAULT 0,
    FOREIGN KEY (kd_supplier) REFERENCES tb_supplier(kd_supplier)
) ENGINE=InnoDB;

-- Tabel Detail Pembelian (Item per Transaksi)
CREATE TABLE IF NOT EXISTS tb_pembelian_detail (
    kd_pembelian_detail INT AUTO_INCREMENT PRIMARY KEY,
    kd_pembelian VARCHAR(15),
    kd_barang VARCHAR(10),
    jumlah INT DEFAULT 0,
    subtotal DECIMAL(10,2) DEFAULT 0,
    FOREIGN KEY (kd_pembelian) REFERENCES tb_pembelian(kd_pembelian),
    FOREIGN KEY (kd_barang) REFERENCES tb_barang(kd_barang)
) ENGINE=InnoDB;

-- Data contoh supplier (opsional untuk testing)
INSERT IGNORE INTO tb_supplier VALUES ('S00001', 'PT. Maju Jaya', 'Jl. Raya No. 1, Yogyakarta', '0274123456');
INSERT IGNORE INTO tb_barang VALUES ('B00001', 'Beras Premium 5kg', 'Karung', 75000.00, 65000.00);
INSERT IGNORE INTO tb_barang VALUES ('B00002', 'Minyak Goreng 2L', 'Botol', 35000.00, 30000.00);

SELECT 'Setup database berhasil!' AS status;
