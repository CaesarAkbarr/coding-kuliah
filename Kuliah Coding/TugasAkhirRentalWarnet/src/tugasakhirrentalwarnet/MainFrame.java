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
                        // Lari ke FrameLoginMember (Langkah lo selanjutnya)
                        new FrameLoginMember(idPC).setVisible(true);
                        javax.swing.JOptionPane.showMessageDialog(
                            this,
                            "Akan membuka Frame Login untuk " + namaPC + " 😂"
                        );
                    } else {
                        // Nanti lari ke logika Check-Out
                        javax.swing.JOptionPane.showMessageDialog(
                            this,
                            "Akan memproses Check-Out untuk " + namaPC + " 🚀"
                        );
                        prosesCheckOutDinamis(idPC);
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
                        "Sesi paket selesai! PC " +
                            idPC +
                            " kembali AVAILABLE. 🚀"
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
                    if (durationMinutes <= 0) durationMinutes = 1; // Minimal charge 1 menit biar gak gratisan 😹

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
                                "!\nPelanggan gak boleh pulang sebelum lunas! 😹",
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        jScrollPane1 = new javax.swing.JScrollPane();
        wadahCardPC = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jScrollPane1.setViewportView(wadahCardPC);

        getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);

        pack();
    } // </editor-fold>//GEN-END:initComponents

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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel wadahCardPC;
    // End of variables declaration//GEN-END:variables
}
