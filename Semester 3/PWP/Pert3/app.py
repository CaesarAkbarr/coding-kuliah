from flask import Flask, render_template

app = Flask(__name__)
app.config['TEMPLATES_AUTO_RELOAD'] = True

@app.route('/')
def home():
    return render_template('index.html')

@app.route('/proyek')
def proyek():
    return render_template('proyek.html')

@app.route('/pendidikan')
def pendidikan():
    return render_template('pendidikan.html')

@app.route('/keahlian')
def keahlian():
    return render_template('keahlian.html')

@app.route('/kontak')
def kontak():
    return render_template('kontak.html')

if __name__ == '__main__':
    app.run(debug=True)
