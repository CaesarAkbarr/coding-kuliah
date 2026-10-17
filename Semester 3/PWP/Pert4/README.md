# Panduan Praktikum Pekan 4: Integrasi Flask dan MySQL

Pada pekan ini, kita akan fokus pada integrasi antara framework backend **Flask (Python)** dengan database relasional **MySQL**. Studi kasus yang digunakan adalah aplikasi **Todo List** sederhana.

Fokus pembelajaran pada modul ini meliputi:
1. Konfigurasi MySQL menggunakan phpMyAdmin.
2. Konfigurasi *environment variables* (`.env`) di Flask.
3. Konfigurasi koneksi MySQL di dalam aplikasi Flask.
4. Mengambil data dari database (HTTP GET Method) dan menampilkannya di halaman index.
5. Mengintegrasikan Bootstrap 5 untuk menampilkan data dalam bentuk tabel HTML yang rapi.

---

## Persiapan Tools (Prerequisites)

Sebelum memulai, pastikan Anda telah menginstal:
1. **Python** (versi 3.8 ke atas)
2. **XAMPP / Laragon** (untuk menjalankan MySQL server)
3. **Teks Editor** (VS Code, Sublime Text, dll)

---

## Langkah-langkah Menjalankan Aplikasi

### Langkah 1: Persiapan Database (MySQL)
1. Buka aplikasi **XAMPP Control Panel** atau **Laragon**, lalu jalankan service **MySQL**.
2. Buka browser dan akses **phpMyAdmin** (biasanya di `http://localhost/phpmyadmin`).
3. Buat database baru dengan nama `todo_list`.
4. Buka tab **SQL** pada database tersebut, lalu *copy-paste* seluruh isi dari file `schema.sql` yang ada di dalam folder project ini.
5. Klik **Go** atau **Kirim** untuk mengeksekusi query. Pastikan tabel `todos` berhasil dibuat beserta data *dummy*-nya.

### Langkah 2: Konfigurasi Environment (`.env`)
1. Buka folder project ini di teks editor (misal: VS Code).
2. Pastikan terdapat file bernama `.env`. Jika belum ada, buat file baru bernama `.env`.
3. Sesuaikan konfigurasi berikut dengan kredensial MySQL Anda (secara default *username* XAMPP adalah `root` dan *password* dikosongkan):
   ```env
   FLASK_APP=app.py
   FLASK_DEBUG=1
   DB_HOST=localhost
   DB_USER=root
   DB_PASSWORD=
   DB_NAME=todo_list
   ```

### Langkah 3: Instalasi Library / Dependencies
1. Buka terminal atau *Command Prompt*, lalu arahkan (CD) ke direktori folder project ini.
2. (Opsional namun disarankan) Buat dan aktifkan *Virtual Environment*:
   * Windows: `python -m venv venv` lalu aktifkan dengan `venv\Scripts\activate`
   * Mac/Linux: `python3 -m venv venv` lalu aktifkan dengan `source venv/bin/activate`
3. Install semua library yang dibutuhkan (Flask, python-dotenv, mysql-connector-python) dengan menjalankan perintah:
   ```bash
   pip install -r requirements.txt
   ```

### Langkah 4: Menjalankan Server Flask
1. Setelah semua library terinstal, jalankan aplikasi menggunakan perintah:
   ```bash
   python app.py
   ```
2. Jika berhasil, terminal akan menampilkan pesan bahwa server berjalan di `http://127.0.0.1:5000` atau `http://localhost:5000`.
3. Buka URL tersebut di browser Anda. Anda seharusnya melihat halaman **Daftar Tugas (Todo List)** dengan tabel rapi menggunakan Bootstrap 5 yang datanya bersumber dari database MySQL Anda.

---

## Struktur Project

```text
todolist_pekan4/
│
├── templates/
│   └── index.html       # Tampilan antarmuka HTML dengan integrasi Bootstrap 5 & Jinja2
│
├── .env                 # File untuk menyimpan konfigurasi rahasia (seperti user & pass DB)
├── app.py               # File utama berisi logika Flask dan koneksi database
├── requirements.txt     # Daftar library Python yang dibutuhkan
└── schema.sql           # Query SQL untuk membuat tabel dan insert data awal
```

---

## Troubleshooting

### XAMPP Issue Fix

**MySQL Cannot Be Runned**

Solusi:
1. Buka **Command Prompt** (CMD) sebagai Administrator.
2. Jalankan perintah untuk melihat port yang sedang digunakan:
   ```bash
   netstat -ano
   ```
3. Lihat proses mana yang menggunakan port MySQL (default port 3306):
   ```bash
   tasklist | findstr <PID>
   ```
4. Hentikan proses yang mengganggu dengan perintah:
   ```bash
   taskkill /PID <PID> /F
   ```
   (Ganti `<PID>` dengan Process ID yang Anda temukan)

---

### Laragon Issue Fix

**1. Invalid "default_authentication_plugin=mysql_native_password" at MySQL INI**

Solusi:
- Buka file `my.ini` (biasanya terletak di folder MySQL di dalam instalasi Laragon).
- Comment baris yang berisi `default_authentication_plugin=mysql_native_password` dengan menambahkan `#` di awal baris:
  ```ini
  # default_authentication_plugin=mysql_native_password
  ```
- Ubah file `my.ini` menjadi **Read-Only** (klik kanan → Properties → Check "Read-only").
- Clear data inisialisasi MySQL sebelumnya dengan menghapus folder data MySQL dan restart Laragon.

**2. PHP Version Invalid**

Solusi:
- Download versi PHP yang sesuai dari: https://www.php.net/downloads.php
- Extract dan ganti folder PHP pada instalasi Laragon Anda.

**3. PhpMyAdmin Not Found**

Solusi:
- Download PhpMyAdmin versi terbaru dari: https://www.phpmyadmin.net/downloads/
- Extract dan tempatkan di folder yang sesuai dalam instalasi Laragon.

**4. MySQL Version Invalid**

Solusi:
- Download MySQL Installer dari: https://downloads.mysql.com/archives/installer/
- Pilih versi yang kompatibel dan install/upgrade MySQL Anda.

**5. Laragon Not Installed Yet**

Solusi:
- Download Laragon versi terbaru dari: https://github.com/leokhoa/laragon/releases?page=2#release-6.0.0
- Install sesuai dengan panduan installer.

---

## Requirements / Persyaratan

Untuk menjalankan aplikasi ini dengan optimal, pastikan menggunakan versi-versi berikut:

| Komponen | Versi |
|----------|-------|
| PHP | 8.2 |
| MySQL | 8.0.30-winx64 |
| PhpMyAdmin | 5.2.3 |
| Laragon | v6 |