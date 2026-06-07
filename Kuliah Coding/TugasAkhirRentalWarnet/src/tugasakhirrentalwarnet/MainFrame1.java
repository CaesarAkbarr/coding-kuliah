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
public class MainFrame1 extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger =
        java.util.logging.Logger.getLogger(MainFrame1.class.getName());

    /**
     * Creates new form MainFrame1
     */
    public MainFrame1() {
        initComponents();

        setLocationRelativeTo(null);
        setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
        ((javax.swing.JPanel)getContentPane()).setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));
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
                        // new FrameLoginMember(idPC).setVisible(true);
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
        } catch (Exception e) {
            System.err.println("Gagal load data PC Dinamis: " + e.getMessage());
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
        getContentPane().setLayout(new java.awt.FlowLayout());

        javax.swing.GroupLayout wadahCardPCLayout = new javax.swing.GroupLayout(wadahCardPC);
        wadahCardPC.setLayout(wadahCardPCLayout);
        wadahCardPCLayout.setHorizontalGroup(
            wadahCardPCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        wadahCardPCLayout.setVerticalGroup(
            wadahCardPCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(wadahCardPC);

        getContentPane().add(jScrollPane1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
        java.awt.EventQueue.invokeLater(() ->
            new MainFrame1().setVisible(true)
        );
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel wadahCardPC;
    // End of variables declaration//GEN-END:variables
}
