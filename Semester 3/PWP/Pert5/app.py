import os
import sqlite3
from flask import Flask, render_template, request, redirect, url_for, session, jsonify
from werkzeug.security import generate_password_hash, check_password_hash
from dotenv import load_dotenv

load_dotenv()

app = Flask(__name__)
app.secret_key = os.getenv('SECRET_KEY', 'kunci_rahasia_default')
DB_FOLDER='.database'
DB_NAME = os.path.join(DB_FOLDER, 'ecommerce.db')

# Inisialisasi Database SQLite
def init_db():
    # CEK & BUAT FOLDER: Jika folder .database belum ada, buat foldernya
    if not os.path.exists(DB_FOLDER):
        os.makedirs(DB_FOLDER)
        print(f"Folder '{DB_FOLDER}' berhasil dibuat.")

    # Koneksi SQLite: Jika file db sqlite belum ada, akan otomatis terbuat
    conn = sqlite3.connect(DB_NAME)
    cursor = conn.cursor()
    
    # Tabel Users
    cursor.execute('''
        CREATE TABLE IF NOT EXISTS users (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            username TEXT UNIQUE NOT NULL,
            password TEXT NOT NULL
        )
    ''')
    
    # Tabel Cart 
    cursor.execute('''
        CREATE TABLE IF NOT EXISTS cart (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            user_id INTEGER,
            product_id INTEGER,
            title TEXT,
            price REAL,
            image TEXT,
            quantity INTEGER DEFAULT 1,
            FOREIGN KEY(user_id) REFERENCES users(id)
        )
    ''')
    conn.commit()
    conn.close()

init_db()

def get_db_connection():
    conn = sqlite3.connect(DB_NAME)
    conn.row_factory = sqlite3.Row
    return conn

# ROUTE: Register
@app.route('/register', methods=['GET', 'POST'])
def register():
    if request.method == 'POST':
        username = request.form['username']
        password = request.form['password']
        hashed_password = generate_password_hash(password)

        conn = get_db_connection()
        cursor = conn.cursor()
        try:
            cursor.execute('INSERT INTO users (username, password) VALUES (?, ?)', (username, hashed_password))
            conn.commit()
            return redirect(url_for('login'))
        except sqlite3.IntegrityError:
            return "Username sudah terdaftar!"
        finally:
            conn.close()
            
    return render_template('register.html')

# ROUTE: Login
@app.route('/login', methods=['GET', 'POST'])
def login():
    if 'user_id' in session:
        return redirect(url_for('home'))

    if request.method == 'POST':
        username = request.form['username']
        password = request.form['password']

        conn = get_db_connection()
        user = conn.execute('SELECT * FROM users WHERE username = ?', (username,)).fetchone()
        conn.close()

        if user and check_password_hash(user['password'], password):
            session['user_id'] = user['id']
            session['username'] = user['username']
            return redirect(url_for('home'))
        else:
            return "Username atau password salah!"

    return render_template('login.html')

# ROUTE: Logout
@app.route('/logout')
def logout():
    session.clear()
    return redirect(url_for('login'))

# ROUTE: Home (Katalog Produk)
@app.route('/')
def home():
    if 'user_id' not in session:
        return redirect(url_for('login'))
    
    # Flask HANYA mengambil data keranjang dari database lokal
    conn = get_db_connection()
    cart_rows = conn.execute('SELECT product_id, quantity FROM cart WHERE user_id = ?', (session['user_id'],)).fetchall()
    conn.close()
    
    # Konversi ke dictionary agar mudah dibaca oleh JavaScript (contoh: {"1": 2, "3": 1})
    cart_data = {str(row['product_id']): row['quantity'] for row in cart_rows}

    # Kita mengirimkan cart_data ke template untuk di-passing ke JavaScript
    return render_template('home.html', username=session['username'], cart_data=cart_data)

# ENDPOINT: Add to Cart (Digunakan oleh jQuery AJAX)
@app.route('/add_to_cart', methods=['POST'])
def add_to_cart():
    if 'user_id' not in session:
        return jsonify({'status': 'error', 'message': 'Unauthorized'}), 401
    
    data = request.json
    conn = get_db_connection()
    conn.execute('''
        INSERT INTO cart (user_id, product_id, title, price, image) 
        VALUES (?, ?, ?, ?, ?)
    ''', (session['user_id'], data['id'], data['title'], data['price'], data['image']))
    conn.commit()
    conn.close()

    return jsonify({'status': 'success', 'message': 'Produk ditambahkan ke keranjang!'})

# ENDPOINT: Remove from Cart (Digunakan oleh jQuery AJAX)
@app.route('/remove_from_cart', methods=['POST'])
def remove_from_cart():
    if 'user_id' not in session:
        return jsonify({'status': 'error', 'message': 'Unauthorized'}), 401
    
    data = request.json
    cart_id = data.get('cart_id')
    user_id = session['user_id']
    
    conn = get_db_connection()
    # Hapus item spesifik berdasarkan ID keranjang dan User ID
    conn.execute('DELETE FROM cart WHERE id = ? AND user_id = ?', (cart_id, user_id))
    
    # Hitung ulang total harga setelah item dihapus
    items = conn.execute('SELECT price FROM cart WHERE user_id = ?', (user_id,)).fetchall()
    new_total = sum(item['price'] for item in items)
    
    conn.commit()
    conn.close()

    return jsonify({
        'status': 'success', 
        'message': 'Item berhasil dihapus',
        'new_total': new_total
    })

@app.route('/update_cart', methods=['POST'])
def update_cart():
    if 'user_id' not in session:
        return jsonify({'status': 'error', 'message': 'Unauthorized'}), 401
    
    data = request.json
    user_id = session['user_id']
    product_id = data['product_id']
    qty = data['quantity']
    
    conn = get_db_connection()
    
    # Jika kuantitas diatur ke 0, hapus dari database
    if qty <= 0:
        conn.execute('DELETE FROM cart WHERE user_id = ? AND product_id = ?', (user_id, product_id))
    else:
        # Cek apakah produk sudah ada di keranjang
        existing = conn.execute('SELECT id FROM cart WHERE user_id = ? AND product_id = ?', (user_id, product_id)).fetchone()
        if existing:
            conn.execute('UPDATE cart SET quantity = ? WHERE id = ?', (qty, existing['id']))
        else:
            conn.execute('''
                INSERT INTO cart (user_id, product_id, title, price, image, quantity) 
                VALUES (?, ?, ?, ?, ?, ?)
            ''', (user_id, product_id, data.get('title'), data.get('price'), data.get('image'), qty))
    
    # Hitung ulang total harga keranjang
    items = conn.execute('SELECT price, quantity FROM cart WHERE user_id = ?', (user_id,)).fetchall()
    new_total = sum(item['price'] * item['quantity'] for item in items)
    
    conn.commit()
    conn.close()

    return jsonify({'status': 'success', 'new_total': new_total, 'quantity': qty})

# ROUTE: Keranjang
@app.route('/cart')
def cart():
    if 'user_id' not in session:
        return redirect(url_for('login'))
        
    conn = get_db_connection()
    items = conn.execute('SELECT * FROM cart WHERE user_id = ?', (session['user_id'],)).fetchall()
    conn.close()
    
    # Update kalkulasi total dikali kuantitas
    total = sum(item['price'] * item['quantity'] for item in items)
    return render_template('cart.html', items=items, total=total)

if __name__ == '__main__':
    app.run(
        debug=os.getenv("FLASK_APP"), 
        host=os.getenv("FLASK_HOST"), 
        port=os.getenv("FLASK_PORT"),
    )