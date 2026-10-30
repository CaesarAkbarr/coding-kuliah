import os
import sqlite3
from flask import Flask, render_template
from dotenv import load_dotenv

load_dotenv()

app = Flask(__name__)

@app.route('/')
def index():
    return render_template('index.html')

@app.route('/topics')
def topics():
    return {
        'message' : 'Berhasil mendapatkan data topik pembelajaran di kelas!',
        'dataset' : [
            'Pengantar Praktikum',
            'Virtual Environtment',
            'Integrasi Flask dan Bootstrap',
            'Integrasi Flask dan MySQL',
            'Integrasi Flask Session dan JQuery',
            'Instalasi dan Konfigurasi Flask REST API',
            'Konfigurasi Database dan Migration',
            'Integrasi CRUD pada RESTful API',
            'Integrasi JWT Pada Flask',
        ],
    }

@app.route('/tasks')
def tasks():
    return {
        'message' : 'Berhasil mendapatkan data tugas yang dibebankan kepada mahasiswa!',
        'dataset' : [
            'Open Web Route',
            'Open API Route',
            'Mini Dynamic Web',
        ],
    }

@app.route('/projects')
def projects():
    return {
        'message' : 'Berhasil mendapatkan project yang dibebankan kepada mahasiswa!',
        'dataset' : {
            'Week 7' : {
                'activity' : 'Practical Exam (Responsi)',
                'endpoint' : '/responsi'
            },

            'Week 13' : {
                'activity' : 'Final Project (Tugas Besar)',
                'endpoint' : '/final-project'
            },
        },
    }

@app.route('/projects/responsi')
def projectsResponsi():
    return {
        'message' : 'Berhasil mendapatkan detail informasi responsi!',
        'dataset' : {
            'title' : 'Practical Exam (Responsi)',
            'description' : 'Responsi mahasiswa pertemuan 7',
            'topics' : [
                'Virtual Environtment',
                'Integrasi Flask dan Bootstrap',
                'Integrasi Flask dan MySQL',
                'Integrasi Flask Session dan JQuery',
            ],
        },
    }

@app.route('/projects/final-project')
def projectsFinalProject():
    return {
        'message' : 'Berhasil mendapatkan detail informasi tugas besar!',
        'dataset' : {
            'title' : 'Final Project (Tugas Besar)',
            'description' : 'Presentasi kelompok tugas besar',
            'topics' : [
                'Virtual Environtment',
                'Integrasi Flask dan Bootstrap',
                'Integrasi Flask dan MySQL',
                'Integrasi Flask Session dan JQuery',
                'Konfigurasi Database dan Migration',
                'Integrasi CRUD pada RESTful API',
            ],
        },
    }

if __name__ == '__main__':
    app.run(
        debug=os.getenv("FLASK_APP"), 
        host=os.getenv("FLASK_HOST"), 
        port=os.getenv("FLASK_PORT"),
    )