from database.configurations.extensions import db
from database.models.user import User

def seed_users():
    users_data = [
        {"name": "Davis Arrizqi", "email": "davis@example.com"},
        {"name": "Sherlly Briliant", "email": "sherlly@example.com"}
    ]
    
    for data in users_data:
        try:
            if not User.query.filter_by(email=data['email']).first():
                new_user = User(name=data['name'], email=data['email'])
                db.session.add(new_user)
                db.session.commit()
                print(f"[+] User {data['email']} berhasil di-seed.")
            else:
                print(f"[-] User {data['email']} sudah ada, skip seeding.")
        except Exception as e:
            db.session.rollback()
            print(f"[!] Error seeding user {data['email']}: {e}")
