import os
import sqlite3
from flask import Flask, render_template
from dotenv import load_dotenv

load_dotenv()

app = Flask(__name__)

@app.route('/')
def index():
    return render_template('index.html')

@app.route('/api/v1/profile')
def profile():
    return {
        'status': 'success',
        'message': 'Berhasil mendapatkan profil organisasi HMSI UTY',
        'data': {
            'organization': 'Himpunan Mahasiswa Sistem Informasi (HMSI)',
            'institution': 'Universitas Teknologi Yogyakarta',
            'faculty': 'Fakultas Sains & Teknologi',
            'period': '2026/2027',
            'vision': 'Mewujudkan HMSI UTY sebagai wadah aspirasi, kolaborasi, dan pengembangan kompetensi teknologi informasi yang inklusif serta berdaya saing.'
        }
    }

@app.route('/api/v1/divisions')
def divisions():
    return {
        'status': 'success',
        'message': 'Berhasil mendapatkan daftar divisi kepengurusan HMSI',
        'data': [
            {'code': 'BPH', 'name': 'Badan Pengurus Harian', 'lead': 'Ketua & Wakil Ketua'},
            {'code': 'LITBANG', 'name': 'Penelitian dan Pengembangan', 'lead': 'Kepala Divisi Litbang'},
            {'code': 'KOMINFO', 'name': 'Komunikasi dan Informasi', 'lead': 'Kepala Divisi Kominfo'},
            {'code': 'KADER', 'name': 'Kaderisasi dan Organisasi', 'lead': 'Kepala Divisi Kaderisasi'},
            {'code': 'PSDM', 'name': 'Pengembangan Sumber Daya Mahasiswa', 'lead': 'Kepala Divisi PSDM'},
            {'code': 'DANUS', 'name': 'Dana dan Usaha', 'lead': 'Kepala Divisi Danus'}
        ]
    }

@app.route('/api/v1/work-programs')
def work_programs():
    return {
        'status': 'success',
        'message': 'Berhasil mendapatkan daftar program kerja unggulan HMSI',
        'data': [
            {'id': 'PROKER-01', 'title': 'Makrab Sisfornite', 'endpoint': '/api/v1/work-programs/sisfornite'},
            {'id': 'PROKER-02', 'title': 'Fusion Tech Competition', 'endpoint': '/api/v1/work-programs/fusion-tech'},
            {'id': 'PROKER-03', 'title': 'Seminar Nasional Teknologi Informasi', 'endpoint': '/api/v1/work-programs/seminar-nasional'},
            {'id': 'PROKER-04', 'title': 'Kunjungan Industri & Ekskursi IT', 'endpoint': '/api/v1/work-programs/kunjungan-industri'},
            {'id': 'PROKER-05', 'title': 'Pengadaan Seragam Korsa HMSI', 'endpoint': '/api/v1/work-programs/korsa'}
        ]
    }

@app.route('/api/v1/work-programs/sisfornite')
def program_sisfornite():
    return {
        'status': 'success',
        'message': 'Berhasil mendapatkan detail program kerja Makrab Sisfornite',
        'data': {
            'title': 'Malam Keakraban Mahasiswa Sistem Informasi (Sisfornite)',
            'target': 'Mahasiswa Baru Program Studi Sistem Informasi D3 & S1',
            'location': 'Kaliurang, Sleman, D.I. Yogyakarta',
            'agenda': [
                'Pengenalan Kurikulum dan Budaya Akademik Kampus',
                'Sharing Session Alumni dan Praktisi IT',
                'Team Building & Inagurasi Mahasiswa Baru'
            ],
            'pic': 'Divisi Kaderisasi & PSDM'
        }
    }

@app.route('/api/v1/work-programs/fusion-tech')
def program_fusion_tech():
    return {
        'status': 'success',
        'message': 'Berhasil mendapatkan detail program kerja Fusion Tech',
        'data': {
            'title': 'Fusion Tech Competition 2026',
            'scale': 'Tingkat Nasional',
            'categories': [
                'Web Application Development',
                'UI/UX Design Challenge',
                'Business Plan & IT Inovation'
            ],
            'target_participants': 150,
            'pic': 'Divisi Litbang & Kominfo'
        }
    }

if __name__ == '__main__':
    app.run(
        debug=os.getenv("FLASK_DEBUG", "True").lower() == "true",
        host=os.getenv("FLASK_HOST", "127.0.0.1"),
        port=int(os.getenv("FLASK_PORT", 5000))
    )
