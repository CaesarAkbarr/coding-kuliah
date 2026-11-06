import os
import mysql.connector
from flask import Flask, render_template, request, redirect, url_for, session, jsonify
from dotenv import load_dotenv

load_dotenv()

app = Flask(name)
app.secret_key = os.getenv('SECRET_KEY', 'kunci_rahasia_krs_pwp_2026')

1. Fungsi Koneksi ke MySQL
def get_db_connection():
    return mysql.connector.connect(
        host=os.getenv('DB_HOST', 'localhost'),
        user=os.getenv('DB_USER', 'root'),
        password=os.getenv('DB_PASSWORD', ''),
        database=os.getenv('DB_NAME', 'krs_db')
    )

2. Rute Autentikasi: Login
@app.route('/login', methods=['GET', 'POST'])
def login():
    if 'user_id' in session:
        return redirect(url_for('index'))

    if request.method == 'POST':
        nim = request.form.get('nim')
        password = request.form.get('password')

        conn = get_db_connection()
        cursor = conn.cursor(dictionary=True)
        cursor.execute("SELECT * FROM mahasiswa WHERE nim = %s", (nim,))
        user = cursor.fetchone()
        cursor.close()
        conn.close()

        if user and user['password'] == password:
            session['user_id'] = user['id']
            session['nim'] = user['nim']
            session['nama'] = user['nama']
            return redirect(url_for('index'))
        else:
            return render_template('login.html', error='NIM atau password salah!')

    return render_template('login.html')

3. Rute Autentikasi: Logout
@app.route('/logout')
def logout():
    session.clear()
    return redirect(url_for('login'))

4. Rute Utama: Halaman Pengambilan KRS (Diproteksi Session)
@app.route('/')
def index():
    if 'user_id' not in session:
        return redirect(url_for('login'))

    conn = get_db_connection()
    cursor = conn.cursor(dictionary=True)

    # Ambil katalog seluruh mata kuliah
    cursor.execute("SELECT * FROM matakuliah ORDER BY semester ASC, kode_mk ASC")
    daftar_mk = cursor.fetchall()

    # Ambil ID mata kuliah yang sudah diambil oleh mahasiswa yang sedang login
    cursor.execute("SELECT matakuliah_id FROM krs WHERE mahasiswa_id = %s", (session['user_id'],))
    mk_diambil_rows = cursor.fetchall()
    mk_diambil_ids = [row['matakuliah_id'] for row in mk_diambil_rows]

    # Hitung total SKS yang sudah diambil saat ini
    cursor.execute("""
        SELECT COALESCE(SUM(m.sks), 0) AS total_sks
        FROM krs k
        JOIN matakuliah m ON k.matakuliah_id = m.id
        WHERE k.mahasiswa_id = %s
    """, (session['user_id'],))
    total_sks = cursor.fetchone()['total_sks']

    cursor.close()
    conn.close()

    return render_template('krs.html',
                           daftar_mk=daftar_mk,
                           mk_diambil_ids=mk_diambil_ids,
                           total_sks=total_sks,
                           nama=session.get('nama'),
                           nim=session.get('nim'))

5. Endpoint AJAX: Ambil Mata Kuliah
@app.route('/api/krs/ambil', methods=['POST'])
def ambil_mk():
    if 'user_id' not in session:
        return jsonify({'status': 'error', 'message': 'Sesi telah habis, silakan login kembali.'}), 401

    data = request.get_json() or request.form
    mk_id = data.get('matakuliah_id')

    if not mk_id:
        return jsonify({'status': 'error', 'message': 'ID mata kuliah tidak valid.'}), 400

    conn = get_db_connection()
    cursor = conn.cursor(dictionary=True)

    # Cek total SKS saat ini
    cursor.execute("""
        SELECT COALESCE(SUM(m.sks), 0) AS total_sks
        FROM krs k
        JOIN matakuliah m ON k.matakuliah_id = m.id
        WHERE k.mahasiswa_id = %s
    """, (session['user_id'],))
    total_sks_sekarang = cursor.fetchone()['total_sks']

    # Ambil data mata kuliah yang ingin diambil
    cursor.execute("SELECT * FROM matakuliah WHERE id = %s", (mk_id,))
    target_mk = cursor.fetchone()

    if not target_mk:
        cursor.close()
        conn.close()
        return jsonify({'status': 'error', 'message': 'Mata kuliah tidak ditemukan.'}), 404

    # Validasi batas maksimal SKS (Maksimal 24 SKS)
    if total_sks_sekarang + target_mk['sks'] > 24:
        cursor.close()
        conn.close()
        return jsonify({
            'status': 'error',
            'message': f"Batas SKS terlampaui! Maksimal 24 SKS. Total saat ini: {total_sks_sekarang} SKS, mata kuliah ini: {target_mk['sks']} SKS."
        }), 400

    try:
        cursor.execute("INSERT INTO krs (mahasiswa_id, matakuliah_id) VALUES (%s, %s)", (session['user_id'], mk_id))
        conn.commit()

        # Hitung total SKS baru setelah penambahan
        cursor.execute("""
            SELECT COALESCE(SUM(m.sks), 0) AS total_sks
            FROM krs k
            JOIN matakuliah m ON k.matakuliah_id = m.id
            WHERE k.mahasiswa_id = %s
        """, (session['user_id'],))
        total_sks_baru = cursor.fetchone()['total_sks']

        return jsonify({
            'status': 'success',
            'message': f"Berhasil mengambil mata kuliah {target_mk['nama_mk']}!",
            'matakuliah_id': int(mk_id),
            'total_sks': total_sks_baru
        })
    except mysql.connector.Error:
        return jsonify({'status': 'error', 'message': 'Mata kuliah ini sudah Anda ambil.'}), 400
    finally:
        cursor.close()
        conn.close()

6. Endpoint AJAX: Batalkan Mata Kuliah
@app.route('/api/krs/batal', methods=['POST'])
def batal_mk():
    if 'user_id' not in session:
        return jsonify({'status': 'error', 'message': 'Sesi telah habis, silakan login kembali.'}), 401

    data = request.get_json() or request.form
    mk_id = data.get('matakuliah_id')

    conn = get_db_connection()
    cursor = conn.cursor(dictionary=True)

    cursor.execute("DELETE FROM krs WHERE mahasiswa_id = %s AND matakuliah_id = %s", (session['user_id'], mk_id))
    conn.commit()

    # Hitung total SKS setelah pembatalan
    cursor.execute("""
        SELECT COALESCE(SUM(m.sks), 0) AS total_sks
        FROM krs k
        JOIN matakuliah m ON k.matakuliah_id = m.id
        WHERE k.mahasiswa_id = %s
    """, (session['user_id'],))
    total_sks_baru = cursor.fetchone()['total_sks']

    cursor.close()
    conn.close()

    return jsonify({
        'status': 'success',
        'message': 'Mata kuliah berhasil dibatalkan dari KRS.',
        'matakuliah_id': int(mk_id),
        'total_sks': total_sks_baru
    })

7. Runner Aplikasi
if name == 'main':
    app.run(
        debug=os.getenv('FLASK_DEBUG', 'True').lower() == 'true',
        host=os.getenv('FLASK_HOST', '127.0.0.1'),
        port=int(os.getenv('FLASK_PORT', 5000))
    )
