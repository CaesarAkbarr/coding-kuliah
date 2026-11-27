# Tugas Praktik RPL - Fitur Barang Keluar (Persediaan)

Repositori ini berisi implementasi fungsi pengurangan stok barang keluar menggunakan Java, pengujian unit dengan JUnit 5, serta penerapan alur kerja cabang (*branch*) pada Git.

**Identitas Mahasiswa:**
- **Nama:** Caesar Akbar Hazimul Fikri
- **NIM:** 3253111006
- **Program Studi:** D3 Sistem Informasi, Universitas Teknologi Yogyakarta

---

## 1. Struktur File
- `Persediaan.java` : Kelas utama yang memuat fungsi `kurangiStok(int stok, int jumlah)` beserta input interaktif di terminal.
- `PersediaanTest.java` : Berkas pengujian unit JUnit 5 yang mencakup 7 skenario uji (kasus normal, nilai batas, dan input tidak valid).
- `lib/junit-platform-console-standalone.jar` : Pustaka JUnit 5 untuk menjalankan pengujian dari terminal.

---

## 2. Cara Menjalankan Program

### A. Menjalankan Program Secara Interaktif
Kompilasi dan jalankan kelas `Persediaan`:
```bash
javac Persediaan.java
java Persediaan
```

### B. Menjalankan Unit Test (JUnit 5)
Kompilasi kode beserta kelas pengujian, lalu jalankan menggunakan JUnit Standalone Runner:
```bash
javac -cp "lib/junit-platform-console-standalone.jar;." Persediaan.java PersediaanTest.java
java -jar lib/junit-platform-console-standalone.jar --class-path . --select-class PersediaanTest
```

---

## 3. Daftar Kasus Uji (Unit Test)
| Kode | Kategori | Input (Stok, Jumlah) | Hasil yang Diharapkan | Status |
| :--- | :--- | :--- | :--- | :--- |
| TC-01 | Normal | (10, 3) | Sisa stok = 7 | PASS |
| TC-02 | Nilai Batas (Habis) | (10, 10) | Sisa stok = 0 | PASS |
| TC-03 | Nilai Batas (Minimum) | (10, 1) | Sisa stok = 9 | PASS |
| TC-04 | Tidak Valid (Nol) | (10, 0) | Melempar `IllegalArgumentException` | PASS |
| TC-05 | Tidak Valid (Melebihi Stok) | (10, 11) | Melempar `IllegalArgumentException` | PASS |
| TC-06 | Tidak Valid (Jumlah Negatif) | (10, -5) | Melempar `IllegalArgumentException` | PASS |
| TC-07 | Tidak Valid (Stok Negatif) | (-1, 5) | Melempar `IllegalArgumentException` | PASS |

---

## 4. Jawaban Pertanyaan Diskusi
**Pertanyaan:** *Jika unit test lulus, mengapa aplikasi masih dapat gagal saat terhubung ke basis data?*

**Jawaban:**
Unit test hanya menguji logika fungsi `kurangiStok` secara terisolasi di dalam memori (RAM) tanpa menyentuh komponen luar. Ketika aplikasi dihubungkan ke basis data nyata, kegagalan tetap bisa terjadi karena faktor eksternal yang tidak diperiksa oleh unit test, antara lain:
1. **Koneksi dan Konfigurasi:** Server database mati, salah username/password, alamat host/port keliru, atau koneksi jaringan terputus (*timeout*).
2. **Ketidaksesuaian Skema Tabel:** Nama tabel atau kolom pada kueri SQL berbeda dengan yang ada di database, atau tipe data tidak cocok.
3. **Konflik Transaksi Bersamaan (*Race Condition*):** Saat dua petugas mengurangi stok barang yang sama pada waktu bersamaan, data di database bisa berbenturan atau mengalami *deadlock* jika tidak dikunci menggunakan transaksi database.
