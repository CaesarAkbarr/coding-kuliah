import os
from flask import Flask, jsonify, render_template
from dotenv import load_dotenv

# Import ekstensi dan model dari struktur baru
from database.configurations.extensions import db, migrate
from database.models.user import User
from database.models.todo import Todo

load_dotenv()

app = Flask(__name__)

# Konfigurasi Database (MySQL)
app.config['SQLALCHEMY_DATABASE_URI'] = f"mysql+pymysql://{os.getenv('DB_USERNAME')}:{os.getenv('DB_PASSWORD')}@{os.getenv('DB_HOST')}:{os.getenv('DB_PORT')}/{os.getenv('DB_NAME')}"
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False

# Inisialisasi Ekstensi
db.init_app(app)
# Menambahkan parameter directory agar Flask-Migrate memusatkan filenya di database/migrations
migrate.init_app(app, db, directory="database/migrations")

@app.route('/')
def index():
    return render_template('index.html')

@app.route('/users', methods=['GET'])
def get_users():
    users = User.query.all()
    return jsonify({
        'message': 'Berhasil mendapatkan data users!',
        'dataset': [{'id': u.id, 'name': u.name, 'email': u.email} for u in users]
    })

@app.route('/todos', methods=['GET'])
def get_todos():
    todos = Todo.query.all()
    return jsonify({
        'message': 'Berhasil mendapatkan data todos!',
        'dataset': [{
            'id': t.id, 
            'user_id': t.user_id, 
            'user_name': t.user.name,
            'title': t.title, 
            'description': t.description,
            'is_completed': t.is_completed
        } for t in todos]
    })

if __name__ == '__main__':
    app.run(
        debug=os.getenv("FLASK_DEBUG", "True").lower() in ['true', '1', 't'], 
        host=os.getenv("FLASK_HOST", "127.0.0.1"), 
        port=int(os.getenv("FLASK_PORT", 5000)),
    )
