from database.configurations.extensions import db
from database.models.user import User
from database.models.todo import Todo

def seed_todos():
    try:
        user1 = db.session.get(User, 1)
        user2 = db.session.get(User, 2)

        if not user1 or not user2:
            print("[!] User dengan ID 1 atau 2 tidak ditemukan. Skip seeding Todo untuk menghindari error.")
            return

        if Todo.query.filter_by(user_id=1).first() is None:
            todos_user1 = [
                Todo(user_id=user1.id, title="Setup Repository Praktikum", description="Menyiapkan .env, app.py, models, migrations", is_completed=True),
                Todo(user_id=user1.id, title="Review Modul 8", description="Mempelajari integrasi SQLAlchemy", is_completed=False),
                Todo(user_id=user1.id, title="Uji Coba Endpoint GET", description="Menguji dengan API Client", is_completed=False),
            ]
            db.session.add_all(todos_user1)
            print(f"[+] 3 Todos sampel untuk User ID 1 ({user1.name}) berhasil ditambahkan.")
        else:
            print(f"[-] Todos untuk User ID 1 ({user1.name}) sudah ada, skip.")

        if Todo.query.filter_by(user_id=2).first() is None:
            todos_user2 = [
                Todo(user_id=user2.id, title="Bimbingan Tugas Besar", description="Membahas struktur DB dengan mahasiswa", is_completed=True),
                Todo(user_id=user2.id, title="Cek Commit Responsi", description="Review repository Github mahasiswa", is_completed=False),
                Todo(user_id=user2.id, title="Menyusun Soal", description="Draft soal untuk minggu ke-9", is_completed=False),
                Todo(user_id=user2.id, title="Rapat Evaluasi Asisten", description="Evaluasi progres praktikum", is_completed=False),
            ]
            db.session.add_all(todos_user2)
            print(f"[+] 4 Todos sampel untuk User ID 2 ({user2.name}) berhasil ditambahkan.")
        else:
            print(f"[-] Todos untuk User ID 2 ({user2.name}) sudah ada, skip.")

        db.session.commit()

    except Exception as e:
        db.session.rollback()
        print(f"[!] Terjadi kesalahan saat seeding todos: {e}")
