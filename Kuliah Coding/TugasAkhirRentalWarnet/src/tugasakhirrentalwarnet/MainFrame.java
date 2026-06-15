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
            // 1. Bersihkan wadah sebelum diisi ulang (biar gak numpuk pas di-refresh)
            wadahCardPC.removeAll();

            Connection conn = Koneksi.getKoneksi();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM computer");

            // 2. Looping data SQL untuk menciptakan kotak secara ajaib
            while (rs.next()) {
                String idPC = rs.getString("computer_id");
                String namaPC = rs.getString("pc_name");
                String tarif = "Rp " + rs.getInt("hourly_rate") + " / Jam";
                String status = rs.getString("status");

                // --- DESAIN KOTAK (CARD) ---
                JPanel card = new JPanel();
                card.setPreferredSize(new Dimension(220, 160)); // Ukuran kotak
                card.setLayout(new GridLayout(5, 1, 5, 5)); // Tata letak tumpuk ke bawah

                // Ubah warna border berdasarkan status (Biar kelihatan gahar!)
                if (status.equals("AVAILABLE")) {
                    card.setBorder(
                        BorderFactory.createTitledBorder(
                            BorderFactory.createLineBorder(
                                new Color(46, 204, 113),
                                2
                            ),
                            idPC
                        )
                    ); // Hijau
                } else {
                    card.setBorder(
                        BorderFactory.createTitledBorder(
                            BorderFactory.createLineBorder(
                                new Color(231, 76, 60),
                                2
                            ),
                            idPC
                        )
                    ); // Merah
                }

                // --- ISI KOTAK (LABEL) ---
                JLabel lblNama = new JLabel(namaPC, SwingConstants.CENTER);
                lblNama.setFont(new Font("Segoe UI", Font.BOLD, 14));

                JLabel lblTarif = new JLabel(tarif, SwingConstants.CENTER);
                JLabel lblStatus = new JLabel(
                    "[" + status + "]",
                    SwingConstants.CENTER
                );

                if (status.equals("AVAILABLE")) lblStatus.setForeground(
                    new Color(46, 204, 113)
                );
                else lblStatus.setForeground(new Color(231, 76, 60));

                // --- TOMBOL AKSI OTOMATIS ---
                JButton btnAksi = new JButton(
                    status.equals("AVAILABLE") ? "Check-In" : "Check-Out"
                );
                btnAksi.setFocusPainted(false);

                // Logika ketika tombol di dalam kotak tersebut diklik
                btnAksi.addActionListener(e -> {
                    if (status.equals("AVAILABLE")) {
                        // Masuk ke FrameLoginMember
                        javax.swing.JOptionPane.showMessageDialog(
                            this,
                            "Akan membuka Frame Login untuk " + namaPC
                        );
                        new FrameLoginMember(idPC).setVisible(true);
                    } else {
                        // PC lagi dipake? Kasih pop-up pilihan!
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
                            prosesCheckOutDinamis(idPC); // Jalankan checkout
                        } else if (
                            pilihan == javax.swing.JOptionPane.NO_OPTION
                        ) {
                            // Panggil fungsi Top-Up (Akan kita buat pintunya)
                            prosesTopUpDinamis(idPC);
                        }
                    }
                });

                // --- MASUKKAN KOMPONEN KE DALAM KOTAK ---
                card.add(lblNama);
                card.add(lblTarif);
                card.add(lblStatus);
                card.add(new JLabel("")); // Spasi kosong biar rapi
                card.add(btnAksi);

                // --- MASUKKAN KOTAK KE DALAM WADAH UTAMA ---
                wadahCardPC.add(card);
            }

            // 3. Render ulang wadah biar kotak-kotak barunya muncul di layar
            wadahCardPC.revalidate();
            wadahCardPC.repaint();
            // Paksa JScrollPane untuk mendeteksi perubahan ukuran panel di dalamnya
            wadahCardPC.getParent().revalidate();
            wadahCardPC.getParent().repaint();
        } catch (Exception e) {
            System.err.println("Gagal load data PC Dinamis: " + e.getMessage());
        }
    }

    public void prosesCheckOutDinamis(String idPC) {
        java.sql.Connection conn = Koneksi.getKoneksi();
        try {
            // 1. Cari data transaksi yang sedang aktif (end_time masih NULL) berdasarkan ID PC
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

                // Trik gaib mendeteksi tipe billing: Kalau total_cost di awal sudah terisi, berarti dia PAKET
                boolean isPaket = !rs.wasNull();
                int hourlyRate = rs.getInt("hourly_rate");

                conn.setAutoCommit(false); // Aktifkan fitur transaksi ACID

                if (isPaket) {
                    // ---------------------------------------------------------
                    // KONDISI A: PELANGGAN PAKETAN (Sudah Lunas di Awal)
                    // ---------------------------------------------------------
                    int konfirm = javax.swing.JOptionPane.showConfirmDialog(
                        this,
                        "Pelanggan PC " +
                            idPC +
                            " menggunakan PAKETAN dan sudah lunas.\nKosongkan PC sekarang?",
                        "CheckOut Paket",
                        javax.swing.JOptionPane.YES_NO_OPTION
                    );

                    if (konfirm != javax.swing.JOptionPane.YES_OPTION) return;

                    // Cukup update end_time saja menjadi waktu sekarang
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
                    // KONDISI B: PELANGGAN ARGO (Wajib Hitung Waktu & Bayar)
                    // ---------------------------------------------------------
                    java.sql.Timestamp endTime = new java.sql.Timestamp(
                        System.currentTimeMillis()
                    );

                    // Hitung selisih durasi bermain dalam satuan menit
                    long durationMinutes = java.time.Duration.between(
                        startTime.toLocalDateTime(),
                        endTime.toLocalDateTime()
                    ).toMinutes();
                    if (durationMinutes <= 0) durationMinutes = 1; // Minimal charge 1 menit biar gak gratisan

                    // Hitung total biaya rumus argo: (Menit * Tarif per jam) / 60
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

                    // Minta input duit pembayaran dari kasir
                    String inputBayar = javax.swing.JOptionPane.showInputDialog(
                        this,
                        "Durasi: " +
                            durationMinutes +
                            " Menit\nTotal Tagihan Argo: Rp " +
                            hitungBiaya +
                            "\n\nMasukkan Uang Pembayaran:"
                    );
                    if (
                        inputBayar == null || inputBayar.trim().isEmpty()
                    ) return; // Kasir klik cancel, checkout batal

                    long uangBayar = 0;
                    try {
                        uangBayar = Long.parseLong(inputBayar.trim());
                    } catch (NumberFormatException e) {
                        javax.swing.JOptionPane.showMessageDialog(
                            this,
                            "Cukup angka saja"
                        );
                        return; // Batal checkout
                    }

                    if (uangBayar < hitungBiaya) {
                        javax.swing.JOptionPane.showMessageDialog(
                            this,
                            "Duitnya kurang Rp " +
                                (hitungBiaya - uangBayar) +
                                "!\nPelanggan wajib membayar sebelum pulang.",
                            "Kurang Bayar",
                            javax.swing.JOptionPane.ERROR_MESSAGE
                        );
                        return; // BLOKIR KERAS! Jangan ganti PC jadi Available.
                    }

                    // Update end_time, durasi, dan total_cost argo ke database
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
                            "\n\nTerima kasih! 😊"
                    );
                }

                // ---------------------------------------------------------
                // RITUAL BERSAMA: Kembalikan status PC di MySQL jadi AVAILABLE
                // ---------------------------------------------------------
                String sqlUpPC =
                    "UPDATE computer SET status = 'AVAILABLE' WHERE computer_id = ?";
                java.sql.PreparedStatement psPC = conn.prepareStatement(
                    sqlUpPC
                );
                psPC.setString(1, idPC);
                psPC.executeUpdate();

                conn.commit(); // Eksekusi sukses permanen
                conn.setAutoCommit(true);

                // Panggil pabrik card lagi biar border PC langsung auto berubah jadi HIJAU REFRESH!
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
            // 1. Ambil data transaksi paket yang sedang berjalan
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
                boolean isArgo = rs.wasNull(); // True jika total_cost nilainya NULL (User Argo)
                int hourlyRate = rs.getInt("hourly_rate");

                // 2. VALIDASI: Kalau user bertipe ARGO, haram hukumnya buat di Top-Up!
                if (isArgo) {
                    javax.swing.JOptionPane.showMessageDialog(
                        this,
                        "Pelanggan ini pake sistem ARGO (Pascabayar)",
                        " cannot Top-Up",
                        javax.swing.JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                // 3. Tampilkan Pilihan Paket Top-Up
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

                if (pilihan == null) return; // User klik batal

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

                // 4. Nagih Duit Cash di Depan (Prabayar)
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
                        "Input harus angka murni tanpa titik/huruf!"
                    );
                    return;
                }

                if (uangBayar < tambahBiaya) {
                    javax.swing.JOptionPane.showMessageDialog(
                        this,
                        "Duit kurang Rp " +
                            (tambahBiaya - uangBayar) +
                            "!\nTop-Up batal.",
                        "Error",
                        javax.swing.JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                // 5. Eksekusi Akumulasi Data ke SQL (ACID Transaction)
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

                loadDataPC(); // Refresh dashboard
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
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.insets = new java.awt.Insets(25, 6, 6, 6);
        jPanel1.add(jButton1, gridBagConstraints);

        btnTutup.setBackground(new java.awt.Color(199, 0, 0));
        btnTutup.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        btnTutup.setForeground(new java.awt.Color(255, 255, 255));
        btnTutup.setText("Tutup");
        btnTutup.setMaximumSize(new java.awt.Dimension(72, 26));
        btnTutup.setPreferredSize(new java.awt.Dimension(72, 26));
        btnTutup.addActionListener(
            new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnTutupActionPerformed(evt);
                }
            }
        );
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 483;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.insets = new java.awt.Insets(25, 6, 6, 6);
        jPanel1.add(btnTutup, gridBagConstraints);

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
    private javax.swing.JButton btnTutup;
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel wadahCardPC;
    // End of variables declaration//GEN-END:variables
}
