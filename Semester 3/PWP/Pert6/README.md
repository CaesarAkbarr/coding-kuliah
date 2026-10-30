# Panduan Praktikum Pekan 6: Instalasi dan Konfigurasi REST API

Pada pekan ini, kita memasuki materi pengembangan backend berbasis antarmuka pemrograman aplikasi atau **REST API** (Representational State Transfer Application Programming Interface). Berbeda dengan pekan sebelumnya yang mengembalikan halaman HTML (Server-Side Rendering), aplikasi Flask kita kali ini akan bertindak sebagai penyedia data dengan mengembalikan format **JSON (JavaScript Object Notation)**.

Project ini juga sudah dilengkapi dengan **Frontend API Tester (Dashboard)** yang memungkinkan kita melakukan *testing* endpoint secara langsung di dalam browser menggunakan *dark-mode* UI yang interaktif.

Fokus pembelajaran pada modul ini meliputi:
1. Pemahaman konsep dasar REST API dan metode komunikasi Client-Server.
2. Instalasi dan konfigurasi dasar Flask untuk keperluan REST API.
3. Pembuatan berbagai *route* atau *endpoint* API yang mengembalikan response berformat JSON.
4. Pengelolaan konfigurasi *environment* aplikasi menggunakan `python-dotenv`.

---

## Persiapan Tools (Prerequisites)

Sebelum memulai, pastikan Anda telah menginstal dan menyiapkan:
1. **Python** (versi 3.8 ke atas).
2. **Teks Editor** (VS Code, Sublime Text, dll).
3. **Browser** untuk membuka UI Dashboard Pengujian API (atau bisa menggunakan Postman/Thunder Client jika ingin pengujian manual eksternal).

---

## Struktur Project

```text
root/
│
├── static/                  # Aset statis frontend (CSS & JS)
│   ├── css/
│   │   └── main.css         # Styling UI API Tester Dashboard
│   └── js/
│       └── main.js          # Logika Request AJAX & Session Log
│
├── templates/               # Folder khusus HTML Flask
│   └── index.html           # UI API Tester Dashboard
│
├── .env                     # File environment (Konfigurasi Port, Host, App)
├── app.py                   # File utama aplikasi Flask REST API
└── requirements.txt         # Daftar dependencies (Flask, python-dotenv)
```

---

## Langkah-langkah Menjalankan Aplikasi

### Langkah 1: Konfigurasi Environment (`.env`)
1. Buka folder project ini di teks editor.
2. Buat file baru bernama `.env` di dalam *root directory* project.
3. Sesuaikan isi file `.env` dengan konfigurasi berikut:
   ```env
   FLASK_APP=app.py
   FLASK_DEBUG=True
   FLASK_HOST=127.0.0.1
   FLASK_PORT=5000
   ```

### Langkah 2: Instalasi Library / Dependencies
1. Buka terminal atau *Command Prompt*, lalu arahkan (CD) ke direktori folder project ini.
2. (Disarankan) Buat dan aktifkan *Virtual Environment*:
   * Windows: `python -m venv .venv` lalu aktifkan dengan `.venv\Scripts\activate`
   * Mac/Linux: `python3 -m venv .venv` lalu aktifkan dengan `source .venv/bin/activate`
3. Install semua *library* yang dibutuhkan dengan menjalankan perintah:
   ```bash
   python -m pip install -r requirements.txt
   ```

### Langkah 3: Menjalankan Server Flask & Dashboard API
Aplikasi diatur untuk membaca konfigurasi host dan port langsung dari environment melalui module `os`.

1. Jalankan aplikasi menggunakan perintah:
   ```bash
   python -m flask run
   ```
2. Buka **Browser** Anda dan akses URL: `http://127.0.0.1:5000`.
3. Anda akan melihat halaman **API Tester Dashboard**. Anda bisa melakukan klik pada daftar *Available Endpoints* di sebelah kiri untuk mengisi URL otomatis, lalu tekan **Send** untuk melihat respon JSON dari API.

---

## Daftar Endpoint API

Aplikasi REST API ini menyediakan beberapa *endpoint* dengan metode **GET** sebagai berikut:

| Endpoint | Method | Deskripsi |
| :--- | :---: | :--- |
| `/` | `GET` | Merender antarmuka halaman Web API Tester Dashboard. |
| `/topics` | `GET` | Menampilkan daftar topik pembelajaran di kelas mata kuliah. |
| `/tasks` | `GET` | Menampilkan daftar tugas yang dibebankan kepada mahasiswa. |
| `/projects` | `GET` | Menampilkan daftar *project* (Responsi dan Tugas Besar). |
| `/projects/responsi` | `GET` | Menampilkan detail informasi dan topik mengenai *Practical Exam* (Responsi). |
| `/projects/final-project` | `GET` | Menampilkan detail informasi dan topik mengenai *Final Project* (Tugas Besar). |