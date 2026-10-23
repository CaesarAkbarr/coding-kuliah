# Panduan Praktikum Pekan 5: E-Commerce Minimalis (Session, AJAX, API & SQLite)

Pada pekan ini, kita akan mengembangkan aplikasi **E-Commerce Minimalis**. Kita akan mempelajari cara mengelola *state* pengguna menggunakan **Session**, melakukan manipulasi antarmuka secara asinkron menggunakan **jQuery (AJAX)**, mengonsumsi API eksternal, dan beralih menggunakan **SQLite** sebagai basis data lokal.

Fokus pembelajaran pada modul ini meliputi:
1. Implementasi autentikasi (Register & Login) dan proteksi halaman menggunakan Flask Session.
2. Pengambilan data (Fetch) dari API eksternal (`FakeStoreAPI`) menggunakan AJAX di sisi Klien (Client-Side Rendering).
3. Penerapan *Optimistic UI Updates* untuk memberikan *feedback* instan pada keranjang belanja tanpa *reload* halaman.
4. Penggunaan SQLite dengan inisialisasi folder `.database` secara otomatis.
5. Kustomisasi konfigurasi server Flask melalui `.env`.

---

## Persiapan Tools (Prerequisites)

Sebelum memulai, pastikan Anda telah menginstal:
1. **Python** (versi 3.8 ke atas)
2. **Teks Editor** (VS Code, Sublime Text, dll)
3. *Browser* modern (Chrome, Edge, Firefox) beserta akses internet aktif (untuk *fetch* FakeStoreAPI dan CDN Bootstrap/jQuery).

> **Catatan:** XAMPP/Laragon tidak diperlukan pada praktikum ini karena kita menggunakan **SQLite**, yaitu sistem *database serverless* yang beroperasi langsung di dalam file lokal.

---

## Langkah-langkah Menjalankan Aplikasi

### Langkah 1: Konfigurasi Environment (`.env`)
1. Buka folder project ini di teks editor.
2. Salin file `.env.example` dan ubah namanya menjadi `.env`.
3. Sesuaikan isi file `.env` dengan konfigurasi berikut:
   ```env
   FLASK_APP=app.py
   FLASK_DEBUG=True
   FLASK_HOST=127.0.0.1
   FLASK_PORT=5000
   SECRET_KEY=ganti_dengan_secret_key_yang_aman
   ```
   *(Catatan: `SECRET_KEY` wajib ada untuk mengamankan data enkripsi Session di Flask).*

### Langkah 2: Instalasi Library / Dependencies
1. Buka terminal atau *Command Prompt*, lalu arahkan (CD) ke direktori folder project ini.
2. (Disarankan) Buat dan aktifkan *Virtual Environment*:
   * Windows: `python -m venv venv` lalu aktifkan dengan `venv\Scripts\activate`
   * Mac/Linux: `python3 -m venv venv` lalu aktifkan dengan `source venv/bin/activate`
3. Install semua *library* yang dibutuhkan dengan menjalankan perintah:
   ```bash
   pip install -r requirements.txt
   ```

### Langkah 3: Menjalankan Server Flask
Aplikasi ini telah dimodifikasi agar membaca *host* dan *port* langsung dari `.env`. Struktur `app.py` bagian bawah telah diatur seperti ini:
```python
if __name__ == '__main__':
    app.run(
        debug=os.getenv("FLASK_DEBUG") == "True", 
        host=os.getenv("FLASK_HOST"), 
        port=os.getenv("FLASK_PORT")
    )
```

1. Jalankan aplikasi menggunakan perintah:
   ```bash
   python app.py
   ```
2. Saat pertama kali dijalankan, sistem akan otomatis mendeteksi dan membuat folder `.database/` beserta file `ecommerce.db` di dalamnya.
3. Buka URL `http://127.0.0.1:5000` di *browser*. Anda akan diarahkan ke halaman Login. Silakan melakukan Registrasi akun terlebih dahulu untuk mengakses Katalog Produk.

---

## Struktur Project

```text
pertemuan5_ecommerce/
│
├── .database/
│   └── ecommerce.db     # File database SQLite (Terbuat otomatis, diabaikan oleh Git)
│
├── static/
│   └── js/
│       └── app.js       # Logika Client-Side: Fetch API, AJAX Cart, dan Optimistic UI
│
├── templates/
│   ├── base.html        # Master layout (Bootstrap 5 & Navigasi)
│   ├── register.html    # Halaman pendaftaran
│   ├── login.html       # Halaman autentikasi
│   ├── home.html        # Halaman katalog produk (dirender oleh jQuery)
│   └── cart.html        # Halaman keranjang belanja
│
├── .env                 # File environment (Konfigurasi Port, Host, Secret Key)
├── .env.example         # Template konfigurasi environment
├── .gitignore           # Aturan file yang tidak diunggah ke repositori
├── app.py               # Backend Flask (Route, Session, SQLite queries)
└── requirements.txt     # Daftar dependencies (Flask, python-dotenv, dll)
```

---

## Troubleshooting

### 1. Data Katalog Tidak Muncul / Terus Loading
**Penyebab:** Perangkat tidak terhubung ke internet atau *FakeStoreAPI* sedang mengalami gangguan (down).
**Solusi:** Pastikan koneksi internet Anda stabil. Buka tab *Network* pada *Developer Tools* browser (F12) untuk melihat apakah *request* ke `https://fakestoreapi.com/products` berstatus `200 OK`.

### 2. Error "OperationalError: database is locked" pada SQLite
**Penyebab:** Ada proses lain yang sedang membaca/menulis ke file `ecommerce.db` secara bersamaan, atau *browser* mengirim *request* AJAX terlalu cepat secara beruntun.
**Solusi:** Hentikan server Flask (Ctrl + C di terminal), lalu jalankan ulang `python app.py`. 

### 3. Session / Login Tidak Berfungsi
**Penyebab:** Variabel `SECRET_KEY` di file `.env` kosong atau file `.env` tidak terbaca.
**Solusi:** Pastikan file bernama tepat `.env` (bukan `.env.txt`) dan `SECRET_KEY` telah diisi dengan *string* acak.

### 4. Tombol Plus (+) dan Minus (-) di Keranjang Tidak Merespons
**Penyebab:** *Cache* JavaScript lama masih tersimpan di *browser*.
**Solusi:** Lakukan *Hard Refresh* pada *browser* dengan menekan `Ctrl + F5` (Windows) atau `Cmd + Shift + R` (Mac) agar *browser* memuat file `app.js` terbaru.