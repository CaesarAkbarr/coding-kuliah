import os

import mysql.connector
from dotenv import load_dotenv
from flask import Flask, jsonify, redirect, render_template, request, session, url_for

load_dotenv()

app = Flask(__name__)
app.secret_key = os.getenv("SECRET_KEY", "kunci_rahasia_krs_pwp_2026")


# Fungsi Koneksi ke MySQL
def get_db_connection():
    return mysql.connector.connect(
        host=os.getenv("DB_HOST", "localhost"),
        user=os.getenv("DB_USER", "root"),
        password=os.getenv("DB_PASSWORD", ""),
        database=os.getenv("DB_NAME", "krs_db"),
    )


# Rute Autentikasi: Login
@app.route("/login", methods=["GET", "POST"])
def login():
    if "user_id" in session:
        return redirect(url_for("index"))

    success = request.args.get("success")

    if request.method == "POST":
        nim = request.form.get("nim", "").strip()
        password = request.form.get("password", "")

        # Validasi tipe data NIM di sisi server
        if not nim.isdigit():
            return render_template(
                "login.html",
                error_title="Tipe Data Salah",
                error="Format NIM harus berupa angka numerik, tidak boleh berisi karakter huruf atau simbol!",
            )

        conn = get_db_connection()
        cursor = conn.cursor(dictionary=True)
        cursor.execute("SELECT * FROM mahasiswa WHERE nim = %s", (nim,))
        user = cursor.fetchone()
        cursor.close()
        conn.close()

        # Validasi keberadaan akun di database
        if not user:
            return render_template(
                "login.html",
                error_title="Akun Tidak Terdaftar",
                error=f"NIM {nim} tidak ditemukan dalam database sistem. Silakan lakukan pendaftaran akun terlebih dahulu!",
            )

        # Validasi kecocokan password
        if user["password"] != password:
            return render_template(
                "login.html",
                error_title="Kata Sandi Salah",
                error="Kata sandi yang Anda masukkan tidak cocok dengan data akun!",
            )

        # Jika lolos seluruh validasi
        session["user_id"] = user["id"]
        session["nim"] = user["nim"]
        session["nama"] = user["nama"]
        return redirect(url_for("index"))

    return render_template("login.html", success=success)


# Rute Autentikasi: Register Mahasiswa Baru
@app.route("/register", methods=["GET", "POST"])
@app.route("/register", methods=["GET", "POST"])
def register():
    if "user_id" in session:
        return redirect(url_for("index"))

    if request.method == "POST":
        nim = request.form.get("nim", "").strip()
        nama = request.form.get("nama", "").strip()
        password = request.form.get("password", "")

        if not nim or not nama or not password:
            return render_template(
                "register.html",
                error_title="Kolom Belum Lengkap",
                error="Seluruh kolom pendaftaran wajib diisi!",
            )

        # Validasi tipe data NIM
        if not nim.isdigit():
            return render_template(
                "register.html",
                error_title="Tipe Data Salah",
                error="NIM wajib berupa angka numerik!",
            )

        if len(nim) < 8 or len(nim) > 15:
            return render_template(
                "register.html",
                error_title="Panjang Karakter Salah",
                error="NIM mahasiswa harus terdiri dari 8 hingga 15 digit angka!",
            )

        if len(password) < 6:
            return render_template(
                "register.html",
                error_title="Password Lemah",
                error="Kata sandi minimal harus terdiri dari 6 karakter!",
            )

        conn = get_db_connection()
        cursor = conn.cursor(dictionary=True)

        # Cek duplikasi di database
        cursor.execute("SELECT id FROM mahasiswa WHERE nim = %s", (nim,))
        existing_user = cursor.fetchone()

        if existing_user:
            cursor.close()
            conn.close()
            return render_template(
                "register.html",
                error_title="Data Sudah Terdaftar",
                error=f"NIM {nim} sudah terdaftar di database! Silakan langsung login atau gunakan NIM Anda sendiri.",
            )

        try:
            cursor.execute(
                "INSERT INTO mahasiswa (nim, nama, password) VALUES (%s, %s, %s)",
                (nim, nama, password),
            )
            conn.commit()
            cursor.close()
            conn.close()
            return redirect(
                url_for(
                    "login",
                    success="Registrasi berhasil! Silakan login dengan akun baru Anda.",
                )
            )
        except mysql.connector.Error as err:
            cursor.close()
            conn.close()
            return render_template(
                "register.html",
                error_title="Kesalahan Database",
                error=f"Terjadi kendala saat menyimpan data: {err}",
            )

    return render_template("register.html")


# Rute Autentikasi: Logout
@app.route("/logout")
def logout():
    session.clear()
    return redirect(url_for("login"))


# Rute Utama: Halaman Pengambilan KRS (Diproteksi Session)
@app.route("/")
def index():
    if "user_id" not in session:
        return redirect(url_for("login"))

    conn = get_db_connection()
    cursor = conn.cursor(dictionary=True)

    # Ambil katalog seluruh mata kuliah
    cursor.execute("SELECT * FROM matakuliah ORDER BY semester ASC, kode_mk ASC")
    daftar_mk = cursor.fetchall()

    # Ambil ID mata kuliah yang sudah diambil oleh mahasiswa yang sedang login
    cursor.execute(
        "SELECT matakuliah_id FROM krs WHERE mahasiswa_id = %s", (session["user_id"],)
    )
    mk_diambil_rows = cursor.fetchall()
    mk_diambil_ids = [row["matakuliah_id"] for row in mk_diambil_rows]

    # Hitung total SKS yang sudah diambil saat ini
    cursor.execute(
        """
        SELECT COALESCE(SUM(m.sks), 0) AS total_sks
        FROM krs k
        JOIN matakuliah m ON k.matakuliah_id = m.id
        WHERE k.mahasiswa_id = %s
    """,
        (session["user_id"],),
    )
    total_sks = cursor.fetchone()["total_sks"]

    cursor.close()
    conn.close()

    return render_template(
        "krs.html",
        daftar_mk=daftar_mk,
        mk_diambil_ids=mk_diambil_ids,
        total_sks=total_sks,
        nama=session.get("nama"),
        nim=session.get("nim"),
    )


# Endpoint AJAX: Ambil Mata Kuliah
@app.route("/api/krs/ambil", methods=["POST"])
def ambil_mk():
    if "user_id" not in session:
        return jsonify(
            {"status": "error", "message": "Sesi telah habis, silakan login kembali."}
        ), 401

    data = request.get_json() or request.form
    mk_id = data.get("matakuliah_id")

    if not mk_id:
        return jsonify(
            {"status": "error", "message": "ID mata kuliah tidak valid."}
        ), 400

    conn = get_db_connection()
    cursor = conn.cursor(dictionary=True)

    # Cek total SKS saat ini
    cursor.execute(
        """
        SELECT COALESCE(SUM(m.sks), 0) AS total_sks
        FROM krs k
        JOIN matakuliah m ON k.matakuliah_id = m.id
        WHERE k.mahasiswa_id = %s
    """,
        (session["user_id"],),
    )
    total_sks_sekarang = cursor.fetchone()["total_sks"]

    # Ambil data mata kuliah yang ingin diambil
    cursor.execute("SELECT * FROM matakuliah WHERE id = %s", (mk_id,))
    target_mk = cursor.fetchone()

    if not target_mk:
        cursor.close()
        conn.close()
        return jsonify(
            {"status": "error", "message": "Mata kuliah tidak ditemukan."}
        ), 404

    # Validasi batas maksimal SKS (Maksimal 24 SKS)
    if total_sks_sekarang + target_mk["sks"] > 24:
        cursor.close()
        conn.close()
        return jsonify(
            {
                "status": "error",
                "message": f"Batas SKS terlampaui! Maksimal 24 SKS. Total saat ini: {total_sks_sekarang} SKS, mata kuliah ini: {target_mk['sks']} SKS.",
            }
        ), 400

    try:
        cursor.execute(
            "INSERT INTO krs (mahasiswa_id, matakuliah_id) VALUES (%s, %s)",
            (session["user_id"], mk_id),
        )
        conn.commit()

        # Hitung total SKS baru setelah penambahan
        cursor.execute(
            """
            SELECT COALESCE(SUM(m.sks), 0) AS total_sks
            FROM krs k
            JOIN matakuliah m ON k.matakuliah_id = m.id
            WHERE k.mahasiswa_id = %s
        """,
            (session["user_id"],),
        )
        total_sks_baru = cursor.fetchone()["total_sks"]

        return jsonify(
            {
                "status": "success",
                "message": f"Berhasil mengambil mata kuliah {target_mk['nama_mk']}!",
                "matakuliah_id": int(mk_id),
                "total_sks": total_sks_baru,
            }
        )
    except mysql.connector.Error:
        return jsonify(
            {"status": "error", "message": "Mata kuliah ini sudah Anda ambil."}
        ), 400
    finally:
        cursor.close()
        conn.close()


# Endpoint AJAX: Batalkan Mata Kuliah
@app.route("/api/krs/batal", methods=["POST"])
def batal_mk():
    if "user_id" not in session:
        return jsonify(
            {"status": "error", "message": "Sesi telah habis, silakan login kembali."}
        ), 401

    data = request.get_json() or request.form
    mk_id = data.get("matakuliah_id")

    conn = get_db_connection()
    cursor = conn.cursor(dictionary=True)

    cursor.execute(
        "DELETE FROM krs WHERE mahasiswa_id = %s AND matakuliah_id = %s",
        (session["user_id"], mk_id),
    )
    conn.commit()

    # Hitung total SKS setelah pembatalan
    cursor.execute(
        """
        SELECT COALESCE(SUM(m.sks), 0) AS total_sks
        FROM krs k
        JOIN matakuliah m ON k.matakuliah_id = m.id
        WHERE k.mahasiswa_id = %s
    """,
        (session["user_id"],),
    )
    total_sks_baru = cursor.fetchone()["total_sks"]

    cursor.close()
    conn.close()

    return jsonify(
        {
            "status": "success",
            "message": "Mata kuliah berhasil dibatalkan dari KRS.",
            "matakuliah_id": int(mk_id),
            "total_sks": total_sks_baru,
        }
    )


# Runner Aplikasi
if __name__ == "__main__":
    app.run(
        debug=os.getenv("FLASK_DEBUG", "True").lower() == "true",
        host=os.getenv("FLASK_HOST", "127.0.0.1"),
        port=int(os.getenv("FLASK_PORT", 5000)),
    )
