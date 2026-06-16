/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package tugasakhirrentalwarnet;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 * @author ROG G513RM
 */
public class MainFrame extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger =
        java.util.logging.Logger.getLogger(MainFrame.class.getName());

    /**
     * Creates new form MainFrame1
     */
    public MainFrame() {
        initComponents();

        setLocationRelativeTo(null);
        setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
        ((javax.swing.JPanel) getContentPane()).setBorder(
            javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        loadDataPC();
    }

    public void loadDataPC() {
        try {
            wadahCardPC.removeAll();

            Connection conn = Koneksi.getKoneksi();
            Statement stmt = conn.createStatement();

            String sql =
                "SELECT c.computer_id, c.pc_name, c.hourly_rate, c.status, s.description " +
                "FROM computer c " +
                "LEFT JOIN computer_spec s ON c.computer_id = s.computer_id " +
                "WHERE c.status != 'DELETED' " +
                "ORDER BY c.computer_id ASC";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String idPC = rs.getString("computer_id");
                String namaPC = rs.getString("pc_name");
                String tarif = "Rp " + rs.getInt("hourly_rate") + " / Jam";
                String status = rs.getString("status");

                String deskripsi = rs.getString("description");
                // Jika deskripsi masih kosong, tampilkan pesan placeholder
                if (rs.wasNull() || deskripsi == null) {
                    deskripsi = "Spek belum diisi!";
                }
                // Variabel untuk digunakan di dalam event listener tombol info
                final String deskripsiFinal = deskripsi;

                // --- Desain kotak kartu komputer ---
                JPanel card = new JPanel();
                card.setPreferredSize(new Dimension(220, 160));
                card.setLayout(new GridLayout(5, 1, 5, 5));

                // Tooltip standar tetap aktif sebagai cadangan
                card.setToolTipText(
                    "<html><body><b>Spesifikasi " +
                        namaPC +
                        ":</b><br>" +
                        deskripsiFinal.replace("\n", "<br>") +
                        "</body></html>"
                );

                // --- Trik khusus baris atas (Nama PC + Tombol Info "i") ---
                JPanel panelAtas = new JPanel(new java.awt.BorderLayout());
                panelAtas.setOpaque(false);

                JLabel lblNama = new JLabel(namaPC, SwingConstants.CENTER);
                lblNama.setFont(new Font("Segoe UI", Font.BOLD, 14));

                // Buat huruf "i" sebagai label bergaya tombol di pojok kanan atas
                JLabel lblInfo = new JLabel(" i  ", SwingConstants.CENTER);
                lblInfo.setFont(new Font("Segoe UI", Font.BOLD, 14));
                lblInfo.setForeground(new Color(52, 152, 219));
                // Ubah kursor menjadi tangan saat didekatkan ke tombol
                lblInfo.setCursor(
                    new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR)
                );

                // Logika ketika huruf "i" ditekan oleh pengguna
                lblInfo.addMouseListener(
                    new java.awt.event.MouseAdapter() {
                        @Override
                        public void mouseClicked(java.awt.event.MouseEvent e) {
                            javax.swing.JOptionPane.showMessageDialog(
                                MainFrame.this,
                                "=== RENCANA SPESIFIKASI " +
                                    namaPC +
                                    " ===\n\n" +
                                    deskripsiFinal,
                                "Spesifikasi & Perangkat Komputer",
                                javax.swing.JOptionPane.INFORMATION_MESSAGE
                            );
                        }
                    }
                );

                // Tambahkan nama PC di tengah dan tombol info di kanan atas
                panelAtas.add(lblNama, java.awt.BorderLayout.CENTER);
                panelAtas.add(lblInfo, java.awt.BorderLayout.EAST);

                // --- Menu popup klik kanan kartu (untuk kelola PC) ---
                javax.swing.JPopupMenu pcPopup = new javax.swing.JPopupMenu();
                javax.swing.JMenuItem menuEditTarif = new javax.swing.JMenuItem(
                    "Ubah Tarif / Jam"
                );
                javax.swing.JMenuItem menuEditSpec = new javax.swing.JMenuItem(
                    "Ubah Deskripsi Spek"
                );
                javax.swing.JMenuItem menuHapusPC = new javax.swing.JMenuItem(
                    "Hapus PC dari Aset"
                );

                menuEditTarif.addActionListener(evt -> {
                    String input = javax.swing.JOptionPane.showInputDialog(
                        this,
                        "Masukkan Tarif Baru:",
                        "5000"
                    );
                    if (input != null && !input.trim().isEmpty()) {
                        try {
                            Connection c = Koneksi.getKoneksi();
                            PreparedStatement ps = c.prepareStatement(
                                "UPDATE computer SET hourly_rate = ? WHERE computer_id = ?"
                            );
                            ps.setInt(1, Integer.parseInt(input.trim()));
                            ps.setString(2, idPC);
                            ps.executeUpdate();
                            loadDataPC();
                        } catch (Exception ex) {}
                    }
                });

                menuEditSpec.addActionListener(evt -> {
                    String specBaru = javax.swing.JOptionPane.showInputDialog(
                        this,
                        "Ubah Deskripsi Spek:",
                        deskripsiFinal
                    );
                    if (specBaru != null) {
                        try {
                            Connection c = Koneksi.getKoneksi();
                            PreparedStatement psCek = c.prepareStatement(
                                "SELECT * FROM computer_spec WHERE computer_id = ?"
                            );
                            psCek.setString(1, idPC);
                            ResultSet rCek = psCek.executeQuery();
                            if (rCek.next()) {
                                PreparedStatement psUp = c.prepareStatement(
                                    "UPDATE computer_spec SET description = ? WHERE computer_id = ?"
                                );
                                psUp.setString(1, specBaru.trim());
                                psUp.setString(2, idPC);
                                psUp.executeUpdate();
                            } else {
                                PreparedStatement psIn = c.prepareStatement(
                                    "INSERT INTO computer_spec (computer_id, pc_type, description) VALUES (?, 'REGULAR', ?)"
                                );
                                psIn.setInt(1, Integer.parseInt(idPC));
                                psIn.setString(2, specBaru.trim());
                                psIn.executeUpdate();
                            }
                            loadDataPC();
                        } catch (Exception ex) {}
                    }
                });

                menuHapusPC.addActionListener(evt -> {
                    int konfirm = javax.swing.JOptionPane.showConfirmDialog(
                        this,
                        "Yakin hapus?",
                        "Hapus PC",
                        javax.swing.JOptionPane.YES_NO_OPTION
                    );
                    if (konfirm == javax.swing.JOptionPane.YES_OPTION) {
                        try {
                            Connection c = Koneksi.getKoneksi();
                            PreparedStatement ps1 = c.prepareStatement(
                                "DELETE FROM computer_spec WHERE computer_id = ?"
                            );
                            ps1.setString(1, idPC);
                            ps1.executeUpdate();
                            PreparedStatement ps2 = c.prepareStatement(
                                "UPDATE computer SET status = 'DELETED' WHERE computer_id = ?"
                            );
                            ps2.setString(1, idPC);
                            ps2.executeUpdate();
                            loadDataPC();
                        } catch (Exception ex) {
                            javax.swing.JOptionPane.showMessageDialog(
                                this,
                                "Gagal hapus, data masih berelasi keuangan!"
                            );
                        }
                    }
                });

                pcPopup.add(menuEditTarif);
                pcPopup.add(menuEditSpec);
                pcPopup.add(new javax.swing.JSeparator());
                pcPopup.add(menuHapusPC);

                card.addMouseListener(
                    new java.awt.event.MouseAdapter() {
                        public void mousePressed(java.awt.event.MouseEvent e) {
                            if (e.isPopupTrigger()) showMenu(e);
                        }

                        public void mouseReleased(java.awt.event.MouseEvent e) {
                            if (e.isPopupTrigger()) showMenu(e);
                        }

                        private void showMenu(java.awt.event.MouseEvent e) {
                            pcPopup.show(e.getComponent(), e.getX(), e.getY());
                        }
                    }
                );

                if (status.equals("AVAILABLE")) {
                    card.setBorder(
                        BorderFactory.createTitledBorder(
                            BorderFactory.createLineBorder(
                                new Color(46, 204, 113),
                                2
                            ),
                            idPC
                        )
                    );
                } else {
                    card.setBorder(
                        BorderFactory.createTitledBorder(
                            BorderFactory.createLineBorder(
                                new Color(231, 76, 60),
                                2
                            ),
                            idPC
                        )
                    );
                }

                JLabel lblTarif = new JLabel(tarif, SwingConstants.CENTER);
                JLabel lblStatus = new JLabel(
                    "[" + status + "]",
                    SwingConstants.CENTER
                );

                if (status.equals("AVAILABLE")) lblStatus.setForeground(
                    new Color(46, 204, 113)
                );
                else lblStatus.setForeground(new Color(231, 76, 60));

                JButton btnAksi = new JButton(
                    status.equals("AVAILABLE") ? "Check-In" : "Check-Out"
                );
                btnAksi.setFocusPainted(false);
                btnAksi.addActionListener(e -> {
                    if (status.equals("AVAILABLE")) {
                        new FrameLoginMember(idPC).setVisible(true);
                    } else {
                        Object[] options = {
                            "Check-Out",
                            "Tambah Billing",
                            "Batal",
                        };
                        int pilihan = javax.swing.JOptionPane.showOptionDialog(
                            this,
                            "Check-Out " + namaPC + "?",
                            "Aksi PC Aktif",
                            javax.swing.JOptionPane.YES_NO_CANCEL_OPTION,
                            javax.swing.JOptionPane.QUESTION_MESSAGE,
                            null,
                            options,
                            options[2]
                        );
                        if (pilihan == javax.swing.JOptionPane.YES_OPTION) {
                            prosesCheckOutDinamis(idPC);
                        } else if (
                            pilihan == javax.swing.JOptionPane.NO_OPTION
                        ) {
                            prosesTopUpDinamis(idPC);
                        }
                    }
                });

                // --- Susun komponen ke dalam kartu (Baris 1 diisi panel atas kustom) ---
                card.add(panelAtas);
                card.add(lblTarif);
                card.add(lblStatus);
                card.add(new JLabel(""));
                card.add(btnAksi);

                wadahCardPC.add(card);
            }

            wadahCardPC.revalidate();
            wadahCardPC.repaint();
            wadahCardPC.getParent().revalidate();
            wadahCardPC.getParent().repaint();
        } catch (Exception e) {
            System.err.println("Gagal load data PC Dinamis: " + e.getMessage());
        }
    }

    public void prosesCheckOutDinamis(String idPC) {
        java.sql.Connection conn = Koneksi.getKoneksi();
        try {
            // Cari data transaksi yang sedang aktif (end_time masih NULL) berdasarkan ID PC
            String sqlCari =
                "SELECT r.tran_id, r.start_time, r.total_cost, c.hourly_rate FROM rental_tran r " +
                "JOIN computer c ON r.computer_id = c.computer_id " +
                "WHERE r.computer_id = ? AND r.end_time IS NULL";
            java.sql.PreparedStatement psCari = conn.prepareStatement(sqlCari);
            psCari.setString(1, idPC);
            java.sql.ResultSet rs = psCari.executeQuery();

            if (rs.next()) {
                String idTrans = rs.getString("tran_id");
                java.sql.Timestamp startTime = rs.getTimestamp("start_time");
                long totalCostExist = rs.getLong("total_cost");

                // Deteksi tipe billing: Jika total_cost sudah terisi sejak awal, maka tipe PAKET
                boolean isPaket = !rs.wasNull();
                int hourlyRate = rs.getInt("hourly_rate");

                conn.setAutoCommit(false); // Aktifkan mode transaksi aman

                if (isPaket) {
                    // ---------------------------------------------------------
                    // Kondisi A: Pelanggan menggunakan paket (Sudah lunas di awal)
                    // ---------------------------------------------------------
                    int konfirm = javax.swing.JOptionPane.showConfirmDialog(
                        this,
                        "Pelanggan PC " +
                            idPC +
                            " menggunakan paket dan sudah lunas.\nKosongkan PC sekarang?",
                        "Proses Checkout Paket",
                        javax.swing.JOptionPane.YES_NO_OPTION
                    );

                    if (konfirm != javax.swing.JOptionPane.YES_OPTION) return;

                    // Hanya perlu perbarui waktu selesai menjadi waktu sekarang
                    String sqlUpTrans =
                        "UPDATE rental_tran SET end_time = NOW() WHERE tran_id = ?";
                    java.sql.PreparedStatement psUp = conn.prepareStatement(
                        sqlUpTrans
                    );
                    psUp.setString(1, idTrans);
                    psUp.executeUpdate();

                    javax.swing.JOptionPane.showMessageDialog(
                        this,
                        "Sesi paket selesai! PC " + idPC + " kembali AVAILABLE."
                    );
                } else {
                    // ---------------------------------------------------------
                    // Kondisi B: Pelanggan menggunakan argo (Harus hitung waktu & bayar)
                    // ---------------------------------------------------------
                    java.sql.Timestamp endTime = new java.sql.Timestamp(
                        System.currentTimeMillis()
                    );

                    // Hitung selisih durasi bermain dalam satuan menit
                    long durationMinutes = java.time.Duration.between(
                        startTime.toLocalDateTime(),
                        endTime.toLocalDateTime()
                    ).toMinutes();
                    // Tarif minimal 1 menit agar tidak ada transaksi gratisan
                    if (durationMinutes <= 0) durationMinutes = 1;

                    long hitungBiaya = (durationMinutes * hourlyRate) / 60;

                    javax.swing.JOptionPane.showMessageDialog(
                        this,
                        "--- BILLING ARGO SELESAI ---\n" +
                            "Durasi Bermain: " +
                            durationMinutes +
                            " Menit\n" +
                            "Total Tagihan  : Rp " +
                            hitungBiaya,
                        "Tagihan Pascabayar",
                        javax.swing.JOptionPane.INFORMATION_MESSAGE
                    );

                    // Minta input pembayaran dari pengguna
                    String inputBayar = javax.swing.JOptionPane.showInputDialog(
                        this,
                        "Durasi: " +
                            durationMinutes +
                            " Menit\nTotal Tagihan Argo: Rp " +
                            hitungBiaya +
                            "\n\nMasukkan Uang Pembayaran:"
                    );
                    // Jika pengguna klik cancel, checkout dibatalkan
                    if (
                        inputBayar == null || inputBayar.trim().isEmpty()
                    ) return;

                    long uangBayar = 0;
                    try {
                        uangBayar = Long.parseLong(inputBayar.trim());
                    } catch (NumberFormatException e) {
                        javax.swing.JOptionPane.showMessageDialog(
                            this,
                            "Masukkan hanya angka saja"
                        );
                        return; // Batalkan checkout
                    }

                    if (uangBayar < hitungBiaya) {
                        javax.swing.JOptionPane.showMessageDialog(
                            this,
                            "Pembayaran kurang Rp " +
                                (hitungBiaya - uangBayar) +
                                "!\nPelanggan wajib membayar sebelum pulang.",
                            "Kurang Bayar",
                            javax.swing.JOptionPane.ERROR_MESSAGE
                        );
                        return; // Blokir keras! Jangan ubah status PC menjadi Available.
                    }

                    // Perbarui waktu selesai, durasi, dan total biaya argo ke database
                    String sqlUpTrans =
                        "UPDATE rental_tran SET end_time = NOW(), duration_minutes = ?, total_cost = ? WHERE tran_id = ?";
                    java.sql.PreparedStatement psUp = conn.prepareStatement(
                        sqlUpTrans
                    );
                    psUp.setLong(1, durationMinutes);
                    psUp.setLong(2, hitungBiaya);
                    psUp.setString(3, idTrans);
                    psUp.executeUpdate();

                    javax.swing.JOptionPane.showMessageDialog(
                        this,
                        "Argo Lunas!\nKembalian: Rp " +
                            (uangBayar - hitungBiaya) +
                            "\n\nTerima kasih!"
                    );
                }

                // ---------------------------------------------------------
                // Ritual bersama: Kembalikan status PC di database menjadi AVAILABLE
                // ---------------------------------------------------------
                String sqlUpPC =
                    "UPDATE computer SET status = 'AVAILABLE' WHERE computer_id = ?";
                java.sql.PreparedStatement psPC = conn.prepareStatement(
                    sqlUpPC
                );
                psPC.setString(1, idPC);
                psPC.executeUpdate();

                conn.commit(); // Simpan semua perubahan secara permanen
                conn.setAutoCommit(true);

                // Panggil pembuat kartu lagi agar border PC langsung berubah warna menjadi hijau
                loadDataPC();
            }
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception ex) {}
            javax.swing.JOptionPane.showMessageDialog(
                this,
                "Error CheckOut: " + e.getMessage(),
                "Error",
                javax.swing.JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public void prosesTopUpDinamis(String idPC) {
        Connection conn = Koneksi.getKoneksi();
        try {
            // Ambil data transaksi paket yang sedang berjalan
            String sqlCari =
                "SELECT r.tran_id, r.duration_minutes, r.total_cost, c.hourly_rate FROM rental_tran r " +
                "JOIN computer c ON r.computer_id = c.computer_id " +
                "WHERE r.computer_id = ? AND r.end_time IS NULL";
            java.sql.PreparedStatement psCari = conn.prepareStatement(sqlCari);
            psCari.setString(1, idPC);
            ResultSet rs = psCari.executeQuery();

            if (rs.next()) {
                String idTrans = rs.getString("tran_id");
                int durasiLama = rs.getInt("duration_minutes");
                long biayaLama = rs.getLong("total_cost");
                // True jika total_cost nilainya NULL (pengguna tipe Argo)
                boolean isArgo = rs.wasNull();
                int hourlyRate = rs.getInt("hourly_rate");

                // Validasi: Jika pengguna tipe ARGO, tidak boleh melakukan Top-Up
                if (isArgo) {
                    javax.swing.JOptionPane.showMessageDialog(
                        this,
                        "Pelanggan ini menggunakan sistem ARGO (Pascabayar)",
                        "Tidak Dapat Top-Up",
                        javax.swing.JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                // Tampilkan pilihan paket Top-Up
                String[] opsiTopUp = {
                    "Tambah 1 Jam",
                    "Tambah 3 Jam (Diskon Rp 3.000)",
                    "Tambah 5 Jam (Diskon Rp 5.000)",
                };
                String pilihan =
                    (String) javax.swing.JOptionPane.showInputDialog(
                        this,
                        "Pilih Durasi Tambahan Paket:",
                        "Menu Top-Up Billing",
                        javax.swing.JOptionPane.QUESTION_MESSAGE,
                        null,
                        opsiTopUp,
                        opsiTopUp[0]
                    );

                if (pilihan == null) return; // Pengguna klik batal

                int tambahMenit = 0;
                long tambahBiaya = 0;

                if (pilihan.equals(opsiTopUp[0])) {
                    tambahMenit = 60;
                    tambahBiaya = hourlyRate * 1;
                } else if (pilihan.equals(opsiTopUp[1])) {
                    tambahMenit = 180;
                    tambahBiaya = (hourlyRate * 3) - 3000;
                } else if (pilihan.equals(opsiTopUp[2])) {
                    tambahMenit = 300;
                    tambahBiaya = (hourlyRate * 5) - 5000;
                }

                // Minta pembayaran secara tunai di depan (Prabayar)
                String inputBayar = javax.swing.JOptionPane.showInputDialog(
                    this,
                    "Harga Top-Up: Rp " +
                        tambahBiaya +
                        "\nMasukkan Uang Pembayaran:"
                );
                if (inputBayar == null || inputBayar.trim().isEmpty()) return;

                long uangBayar = 0;
                try {
                    uangBayar = Long.parseLong(inputBayar.trim());
                } catch (NumberFormatException e) {
                    javax.swing.JOptionPane.showMessageDialog(
                        this,
                        "Input harus berupa angka murni tanpa titik atau huruf!"
                    );
                    return;
                }

                if (uangBayar < tambahBiaya) {
                    javax.swing.JOptionPane.showMessageDialog(
                        this,
                        "Pembayaran kurang Rp " +
                            (tambahBiaya - uangBayar) +
                            "!\nTop-Up dibatalkan.",
                        "Error",
                        javax.swing.JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                // Eksekusi akumulasi data ke SQL (mode transaksi aman)
                conn.setAutoCommit(false);

                int durasiBaru = durasiLama + tambahMenit;
                long biayaBaru = biayaLama + tambahBiaya;

                String sqlUpdate =
                    "UPDATE rental_tran SET duration_minutes = ?, total_cost = ? WHERE tran_id = ?";
                java.sql.PreparedStatement psUp = conn.prepareStatement(
                    sqlUpdate
                );
                psUp.setInt(1, durasiBaru);
                psUp.setLong(2, biayaBaru);
                psUp.setString(3, idTrans);
                psUp.executeUpdate();

                conn.commit();
                conn.setAutoCommit(true);

                javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "=== NOTA TOP-UP LUNAS ===\n" +
                        "ID Transaksi : " +
                        idTrans +
                        "\n" +
                        "Tambahan     : " +
                        pilihan +
                        "\n" +
                        "Total Durasi : " +
                        durasiBaru +
                        " Menit\n" +
                        "Kembalian    : Rp " +
                        (uangBayar - tambahBiaya) +
                        "\n\nBilling sukses diperpanjang!"
                );

                // Perbarui dasbor
                loadDataPC();
            }
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (Exception ex) {}
            javax.swing.JOptionPane.showMessageDialog(
                this,
                "Gagal Top-Up: " + e.getMessage()
            );
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        btnTutup = new javax.swing.JButton();
        btnTambahPC = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        wadahCardPC = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(500, 350));

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jButton1.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jButton1.setText("Lihat Histori Transaksi & Omset");
        jButton1.addActionListener(
            new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton1ActionPerformed(evt);
                }
            }
        );
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.insets = new java.awt.Insets(25, 6, 6, 6);
        jPanel1.add(jButton1, gridBagConstraints);

        btnTutup.setBackground(new java.awt.Color(199, 0, 0));
        btnTutup.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        btnTutup.setForeground(new java.awt.Color(255, 255, 255));
        btnTutup.setText("Tutup");
        btnTutup.setPreferredSize(new java.awt.Dimension(250, 26));
        btnTutup.addActionListener(
            new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnTutupActionPerformed(evt);
                }
            }
        );
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.insets = new java.awt.Insets(25, 6, 6, 6);
        jPanel1.add(btnTutup, gridBagConstraints);

        btnTambahPC.setBackground(new java.awt.Color(218, 165, 32));
        btnTambahPC.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        btnTambahPC.setForeground(new java.awt.Color(255, 255, 255));
        btnTambahPC.setText("Tambah PC");
        btnTambahPC.setPreferredSize(new java.awt.Dimension(250, 26));
        btnTambahPC.addActionListener(
            new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnTambahPCActionPerformed(evt);
                }
            }
        );
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.insets = new java.awt.Insets(25, 6, 6, 6);
        jPanel1.add(btnTambahPC, gridBagConstraints);

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_END);

        jScrollPane1.setViewportView(wadahCardPC);

        getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);

        pack();
    } // </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        //GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        new FrameHistoriTransaksi().setVisible(true);
    } //GEN-LAST:event_jButton1ActionPerformed

    private void btnTutupActionPerformed(java.awt.event.ActionEvent evt) {
        //GEN-FIRST:event_btnTutupActionPerformed
        // TODO add your handling code here:
        this.dispose();
    } //GEN-LAST:event_btnTutupActionPerformed

    private void btnTambahPCActionPerformed(java.awt.event.ActionEvent evt) {
        //GEN-FIRST:event_btnTambahPCActionPerformed
        // TODO add your handling code here:
        new FrameTambahPC().setVisible(true);
    } //GEN-LAST:event_btnTambahPCActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (
            ReflectiveOperationException
            | javax.swing.UnsupportedLookAndFeelException ex
        ) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new MainFrame().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnTambahPC;
    private javax.swing.JButton btnTutup;
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel wadahCardPC;
    // End of variables declaration//GEN-END:variables
}
