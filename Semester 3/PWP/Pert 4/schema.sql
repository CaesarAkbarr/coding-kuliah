CREATE DATABASE IF NOT EXISTS todo_list;
USE todo_list;

CREATE TABLE IF NOT EXISTS todos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    task VARCHAR(255) NOT NULL,
    deadline DATETIME NOT NULL,
    status ENUM('canceled', 'on progress', 'done', 'overdue') DEFAULT 'on progress'
);

-- Insert Dummy Data untuk ditampilkan di halaman Index
INSERT IGNORE INTO todos (task, deadline, status) VALUES 
('Mengerjakan laporan praktikum Web', '2026-10-20 23:59:00', 'on progress'),
('Rapat kepanitiaan', '2026-10-16 19:00:00', 'done'),
('Belajar integrasi Flask dan MySQL', '2026-10-15 08:00:00', 'on progress'),
('Membayar tagihan internet', '2026-10-10 12:00:00', 'overdue');
