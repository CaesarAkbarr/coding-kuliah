from app import app
from database.seeders.user import seed_users
from database.seeders.todo import seed_todos

if __name__ == "__main__":
    with app.app_context():
        print("=== Memulai Proses Seeding ===")
        seed_users()
        seed_todos()
        print("=== Proses Seeding Selesai ===")
