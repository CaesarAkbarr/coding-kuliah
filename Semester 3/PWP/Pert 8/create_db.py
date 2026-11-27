import os
import pymysql
from dotenv import load_dotenv

load_dotenv()

def create_database():
    host = os.getenv("DB_HOST", "127.0.0.1")
    port = int(os.getenv("DB_PORT", 3306))
    user = os.getenv("DB_USERNAME", "root")
    password = os.getenv("DB_PASSWORD", "")
    db_name = os.getenv("DB_NAME", "pertemuan8_db")

    try:
        # Connect to MySQL server without specifying the database
        connection = pymysql.connect(
            host=host,
            port=port,
            user=user,
            password=password
        )
        
        cursor = connection.cursor()
        # Create database if it does not exist
        cursor.execute(f"CREATE DATABASE IF NOT EXISTS {db_name}")
        print(f"[+] Berhasil! Database '{db_name}' dipastikan tersedia (dibuat jika sebelumnya belum ada).")
        
        cursor.close()
        connection.close()
    except Exception as e:
        print(f"[!] Terjadi kesalahan saat membuat database: {e}")

if __name__ == "__main__":
    create_database()
