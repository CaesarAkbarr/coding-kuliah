/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package pemdasquizlagi;

import java.awt.Frame;
import java.sql.Connection;
import java.sql.ResultSet;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ROG G513RM
 */
public class FrameSupplier extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger =
        java.util.logging.Logger.getLogger(FrameSupplier.class.getName());

    /**
     * Creates new form FrameSupplier
     */
    public FrameSupplier() {
        initComponents();
        setTitle("Form Supplier");

        // Mengambil content pane yang ada, yang berisi semua komponen UI Anda
        java.awt.Container contentPane = getContentPane();
        // Membuat panel pembungkus dengan GridBagLayout.
        // Layout ini akan menempatkan komponen di dalamnya (yaitu contentPane) ke tengah.
        javax.swing.JPanel wrapperPanel = new javax.swing.JPanel(
            new java.awt.GridBagLayout()
        );
        wrapperPanel.add(contentPane, new java.awt.GridBagConstraints());
        // Mengatur panel pembungkus sebagai content pane yang baru untuk frame ini.
        setContentPane(wrapperPanel);

        setExtendedState(Frame.MAXIMIZED_BOTH);
        clearField();
        muatDataSupplier();
    }

    private void clearField() {
        txtKodeSupplier.setText(
            Koneksi.generateIdMaster("tb_supplier", "kd_supplier", "S")
        );
        txtKodeSupplier.setEditable(false);
        txtNamaSupplier.setText("");
        txtNoTelp.setText("");
        txtAlamat.setText("");
    }

    private void muatDataSupplier() {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0); // Bersihkan tabel terlebih dahulu

        try {
            Connection c = Koneksi.getKoneksi();
            String sql =
                "SELECT kd_supplier, nama_supplier, alamat, no_telp FROM tb_supplier";
            ResultSet rs = c.createStatement().executeQuery(sql);

            while (rs.next()) {
                model.addRow(
                    new Object[] {
                        rs.getString("kd_supplier"),
                        rs.getString("nama_supplier"),
                        rs.getString("alamat"),
                        rs.getString("no_telp"),
                    }
                );
            }
            rs.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                this,
                "Gagal memuat data supplier: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void simpanSupplier() {
        // Validasi: semua field harus terisi
        if (
            txtNamaSupplier.getText().trim().isEmpty() ||
            txtNoTelp.getText().trim().isEmpty() ||
            txtAlamat.getText().trim().isEmpty()
        ) {
            JOptionPane.showMessageDialog(
                this,
                "Semua field harus diisi!",
                "Peringatan",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        try {
            // Ambil nilai dari form
            String kode = txtKodeSupplier.getText().trim();
            String nama = txtNamaSupplier.getText().trim();
            String noTlp = txtNoTelp.getText().trim();
            String alamat = txtAlamat.getText().trim();

            // Eksekusi query UPSERT: INSERT baru atau UPDATE jika kode sudah ada
            String sql =
                "INSERT INTO tb_supplier (kd_supplier, nama_supplier, alamat, no_telp) " +
                "VALUES ('" +
                kode +
                "', '" +
                nama +
                "', '" +
                alamat +
                "', '" +
                noTlp +
                "') " +
                "ON DUPLICATE KEY UPDATE " +
                "nama_supplier=VALUES(nama_supplier), alamat=VALUES(alamat), no_telp=VALUES(no_telp)";
            Koneksi.ubahData(sql);

            JOptionPane.showMessageDialog(
                this,
                "Data supplier berhasil disimpan/diperbarui!",
                "Sukses",
                JOptionPane.INFORMATION_MESSAGE
            );

            // Refresh tampilan
            muatDataSupplier();
            bersihkanForm();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                this,
                "Gagal menyimpan: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void hapusSupplier() {
        int baris = jTable1.getSelectedRow();
        if (baris < 0) {
            JOptionPane.showMessageDialog(
                this,
                "Pilih baris yang ingin dihapus terlebih dahulu!",
                "Peringatan",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String kode = jTable1.getValueAt(baris, 0).toString();
        int konfirmasi = JOptionPane.showConfirmDialog(
            this,
            "Apakah Anda yakin ingin menghapus supplier " + kode + "?",
            "Konfirmasi Hapus",
            JOptionPane.YES_NO_OPTION
        );

        if (konfirmasi == JOptionPane.YES_OPTION) {
            Koneksi.ubahData(
                "DELETE FROM tb_supplier WHERE kd_supplier = '" + kode + "'"
            );
            JOptionPane.showMessageDialog(
                this,
                "Data supplier berhasil dihapus!",
                "Sukses",
                JOptionPane.INFORMATION_MESSAGE
            );
            muatDataSupplier();
            bersihkanForm();
        }
    }

    private void bersihkanForm() {
        txtKodeSupplier.setText(
            Koneksi.generateIdMaster("tb_supplier", "kd_supplier", "S")
        );
        txtNamaSupplier.setText("");
        txtNoTelp.setText("");
        txtAlamat.setText("");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        lblKodeSupplier = new javax.swing.JLabel();
        txtKodeSupplier = new javax.swing.JTextField();
        txtNamaSupplier = new javax.swing.JTextField();
        lblNamaSupplier = new javax.swing.JLabel();
        lblNoTelp = new javax.swing.JLabel();
        txtNoTelp = new javax.swing.JTextField();
        lblAlamat = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtAlamat = new javax.swing.JTextArea();
        btnTambah = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        btnBatal = new javax.swing.JButton();
        btnSImpan = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        lblKodeSupplier.setText("Kode Supplier:");

        txtKodeSupplier.setText("jTextField1");
        txtKodeSupplier.addActionListener(
            new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    txtKodeSupplierActionPerformed(evt);
                }
            }
        );

        txtNamaSupplier.setText("jTextField1");
        txtNamaSupplier.addActionListener(
            new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    txtNamaSupplierActionPerformed(evt);
                }
            }
        );

        lblNamaSupplier.setText("Nama Supplier:");

        lblNoTelp.setText("No. Telp:");

        txtNoTelp.setText("jTextField1");
        txtNoTelp.addActionListener(
            new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    txtNoTelpActionPerformed(evt);
                }
            }
        );

        lblAlamat.setText("Alamat:");

        jTable1.setModel(
            new javax.swing.table.DefaultTableModel(
                new Object[][] {
                    { null, null, null, null },
                    { null, null, null, null },
                    { null, null, null, null },
                    { null, null, null, null },
                    { null, null, null, null },
                    { null, null, null, null },
                },
                new String[] {
                    "Kode Supplier",
                    "Nama Supplier",
                    "Alamat",
                    "No. Telp.",
                }
            )
        );
        jTable1.addMouseListener(
            new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    jTable1MouseClicked(evt);
                }
            }
        );
        jScrollPane1.setViewportView(jTable1);

        txtAlamat.setColumns(20);
        txtAlamat.setRows(5);
        jScrollPane2.setViewportView(txtAlamat);

        btnTambah.setText("Tambah");
        btnTambah.addActionListener(
            new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnTambahActionPerformed(evt);
                }
            }
        );

        btnHapus.setText("Hapus");
        btnHapus.addActionListener(
            new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnHapusActionPerformed(evt);
                }
            }
        );

        btnBatal.setText("Batal");
        btnBatal.addActionListener(
            new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnBatalActionPerformed(evt);
                }
            }
        );

        btnSImpan.setText("Simpan");
        btnSImpan.addActionListener(
            new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnSImpanActionPerformed(evt);
                }
            }
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
            getContentPane()
        );
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout
                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(
                    layout
                        .createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addGroup(
                            layout
                                .createParallelGroup(
                                    javax.swing.GroupLayout.Alignment.LEADING,
                                    false
                                )
                                .addGroup(
                                    layout
                                        .createSequentialGroup()
                                        .addGroup(
                                            layout
                                                .createParallelGroup(
                                                    javax.swing.GroupLayout.Alignment.LEADING
                                                )
                                                .addComponent(lblKodeSupplier)
                                                .addComponent(lblNamaSupplier)
                                                .addComponent(lblNoTelp)
                                                .addComponent(
                                                    lblAlamat,
                                                    javax.swing.GroupLayout.PREFERRED_SIZE,
                                                    84,
                                                    javax.swing.GroupLayout.PREFERRED_SIZE
                                                )
                                        )
                                        .addGap(18, 18, 18)
                                        .addGroup(
                                            layout
                                                .createParallelGroup(
                                                    javax.swing.GroupLayout.Alignment.LEADING
                                                )
                                                .addComponent(
                                                    txtKodeSupplier,
                                                    javax.swing.GroupLayout.PREFERRED_SIZE,
                                                    71,
                                                    javax.swing.GroupLayout.PREFERRED_SIZE
                                                )
                                                .addComponent(
                                                    txtNamaSupplier,
                                                    javax.swing.GroupLayout.Alignment.TRAILING,
                                                    javax.swing.GroupLayout.PREFERRED_SIZE,
                                                    71,
                                                    javax.swing.GroupLayout.PREFERRED_SIZE
                                                )
                                                .addComponent(
                                                    txtNoTelp,
                                                    javax.swing.GroupLayout.PREFERRED_SIZE,
                                                    71,
                                                    javax.swing.GroupLayout.PREFERRED_SIZE
                                                )
                                        )
                                )
                                .addComponent(
                                    jScrollPane2,
                                    javax.swing.GroupLayout.PREFERRED_SIZE,
                                    0,
                                    Short.MAX_VALUE
                                )
                                .addGroup(
                                    layout
                                        .createSequentialGroup()
                                        .addComponent(btnTambah)
                                        .addPreferredGap(
                                            javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                            javax.swing.GroupLayout.DEFAULT_SIZE,
                                            Short.MAX_VALUE
                                        )
                                        .addComponent(btnHapus)
                                )
                                .addGroup(
                                    layout
                                        .createSequentialGroup()
                                        .addComponent(btnSImpan)
                                        .addPreferredGap(
                                            javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                            javax.swing.GroupLayout.DEFAULT_SIZE,
                                            Short.MAX_VALUE
                                        )
                                        .addComponent(btnBatal)
                                )
                        )
                        .addGap(18, 18, 18)
                        .addComponent(
                            jScrollPane1,
                            javax.swing.GroupLayout.PREFERRED_SIZE,
                            526,
                            javax.swing.GroupLayout.PREFERRED_SIZE
                        )
                        .addContainerGap(
                            javax.swing.GroupLayout.DEFAULT_SIZE,
                            Short.MAX_VALUE
                        )
                )
        );
        layout.setVerticalGroup(
            layout
                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(
                    layout
                        .createSequentialGroup()
                        .addGroup(
                            layout
                                .createParallelGroup(
                                    javax.swing.GroupLayout.Alignment.LEADING
                                )
                                .addGroup(
                                    layout
                                        .createSequentialGroup()
                                        .addGap(58, 58, 58)
                                        .addGroup(
                                            layout
                                                .createParallelGroup(
                                                    javax.swing.GroupLayout.Alignment.BASELINE
                                                )
                                                .addComponent(lblKodeSupplier)
                                                .addComponent(
                                                    txtKodeSupplier,
                                                    javax.swing.GroupLayout.PREFERRED_SIZE,
                                                    javax.swing.GroupLayout.DEFAULT_SIZE,
                                                    javax.swing.GroupLayout.PREFERRED_SIZE
                                                )
                                        )
                                        .addGap(18, 18, 18)
                                        .addGroup(
                                            layout
                                                .createParallelGroup(
                                                    javax.swing.GroupLayout.Alignment.BASELINE
                                                )
                                                .addComponent(lblNamaSupplier)
                                                .addComponent(
                                                    txtNamaSupplier,
                                                    javax.swing.GroupLayout.PREFERRED_SIZE,
                                                    javax.swing.GroupLayout.DEFAULT_SIZE,
                                                    javax.swing.GroupLayout.PREFERRED_SIZE
                                                )
                                        )
                                        .addGap(18, 18, 18)
                                        .addGroup(
                                            layout
                                                .createParallelGroup(
                                                    javax.swing.GroupLayout.Alignment.BASELINE
                                                )
                                                .addComponent(lblNoTelp)
                                                .addComponent(
                                                    txtNoTelp,
                                                    javax.swing.GroupLayout.PREFERRED_SIZE,
                                                    javax.swing.GroupLayout.DEFAULT_SIZE,
                                                    javax.swing.GroupLayout.PREFERRED_SIZE
                                                )
                                        )
                                        .addGap(18, 18, 18)
                                        .addComponent(lblAlamat)
                                        .addGap(18, 18, 18)
                                        .addComponent(
                                            jScrollPane2,
                                            javax.swing.GroupLayout.PREFERRED_SIZE,
                                            javax.swing.GroupLayout.DEFAULT_SIZE,
                                            javax.swing.GroupLayout.PREFERRED_SIZE
                                        )
                                        .addPreferredGap(
                                            javax.swing.LayoutStyle.ComponentPlacement.UNRELATED
                                        )
                                        .addGroup(
                                            layout
                                                .createParallelGroup(
                                                    javax.swing.GroupLayout.Alignment.BASELINE
                                                )
                                                .addComponent(btnHapus)
                                                .addComponent(btnTambah)
                                        )
                                        .addGap(18, 18, 18)
                                        .addGroup(
                                            layout
                                                .createParallelGroup(
                                                    javax.swing.GroupLayout.Alignment.BASELINE
                                                )
                                                .addComponent(btnSImpan)
                                                .addComponent(btnBatal)
                                        )
                                        .addGap(0, 0, Short.MAX_VALUE)
                                )
                                .addGroup(
                                    layout
                                        .createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(
                                            jScrollPane1,
                                            javax.swing.GroupLayout.PREFERRED_SIZE,
                                            0,
                                            Short.MAX_VALUE
                                        )
                                )
                        )
                        .addGap(19, 19, 19)
                )
        );

        pack();
    } // </editor-fold>//GEN-END:initComponents

    private void txtKodeSupplierActionPerformed(
        java.awt.event.ActionEvent evt
    ) {
        //GEN-FIRST:event_txtKodeSupplierActionPerformed
        // TODO add your handling code here:
    } //GEN-LAST:event_txtKodeSupplierActionPerformed

    private void txtNamaSupplierActionPerformed(
        java.awt.event.ActionEvent evt
    ) {
        //GEN-FIRST:event_txtNamaSupplierActionPerformed
        // TODO add your handling code here:
        txtNoTelp.requestFocus();
    } //GEN-LAST:event_txtNamaSupplierActionPerformed

    private void txtNoTelpActionPerformed(java.awt.event.ActionEvent evt) {
        //GEN-FIRST:event_txtNoTelpActionPerformed
        // TODO add your handling code here:
        txtAlamat.requestFocus();
    } //GEN-LAST:event_txtNoTelpActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {
        //GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
        int baris = jTable1.getSelectedRow();
        if (baris >= 0) {
            txtKodeSupplier.setText(jTable1.getValueAt(baris, 0).toString());
            txtNamaSupplier.setText(jTable1.getValueAt(baris, 1).toString());
            txtAlamat.setText(jTable1.getValueAt(baris, 2).toString());
            txtNoTelp.setText(jTable1.getValueAt(baris, 3).toString());
        }
    } //GEN-LAST:event_jTable1MouseClicked

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {
        //GEN-FIRST:event_btnTambahActionPerformed
        // TODO add your handling code here:
        simpanSupplier();
    } //GEN-LAST:event_btnTambahActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {
        //GEN-FIRST:event_btnHapusActionPerformed
        // TODO add your handling code here:
        hapusSupplier();
    } //GEN-LAST:event_btnHapusActionPerformed

    private void btnBatalActionPerformed(java.awt.event.ActionEvent evt) {
        //GEN-FIRST:event_btnBatalActionPerformed
        // TODO add your handling code here:
        clearField();
    } //GEN-LAST:event_btnBatalActionPerformed

    private void btnSImpanActionPerformed(java.awt.event.ActionEvent evt) {
        //GEN-FIRST:event_btnSImpanActionPerformed
        // TODO add your handling code here:
        simpanSupplier();
    } //GEN-LAST:event_btnSImpanActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
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
            new FrameSupplier().setVisible(true)
        );
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBatal;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnSImpan;
    private javax.swing.JButton btnTambah;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblAlamat;
    private javax.swing.JLabel lblKodeSupplier;
    private javax.swing.JLabel lblNamaSupplier;
    private javax.swing.JLabel lblNoTelp;
    private javax.swing.JTextArea txtAlamat;
    private javax.swing.JTextField txtKodeSupplier;
    private javax.swing.JTextField txtNamaSupplier;
    private javax.swing.JTextField txtNoTelp;
    // End of variables declaration//GEN-END:variables
}
