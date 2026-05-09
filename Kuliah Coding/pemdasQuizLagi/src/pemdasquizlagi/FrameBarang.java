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
public class FrameBarang extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger =
        java.util.logging.Logger.getLogger(FrameBarang.class.getName());

    /**
     * Creates new form FrameBarang
     */
    public FrameBarang() {
        initComponents();
        setTitle("Form Barang");

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
        muatDataBarang();
    }

    private void muatDataBarang() {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0); // Bersihkan tabel terlebih dahulu

        try {
            Connection c = Koneksi.getKoneksi();
            String sql =
                "SELECT kd_barang, nama_barang, satuan, harga_jual, harga_beli_stok, stok_barang FROM tb_barang";
            ResultSet rs = c.createStatement().executeQuery(sql);

            while (rs.next()) {
                model.addRow(
                    new Object[] {
                        rs.getString("kd_barang"),
                        rs.getString("nama_barang"),
                        rs.getString("satuan"),
                        rs.getDouble("harga_jual"),
                        rs.getDouble("harga_beli_stok"),
                        rs.getInt("stok_barang"),
                    }
                );
            }
            rs.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                this,
                "Gagal memuat data barang: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void simpanBarang() {
        // Validasi: semua field harus terisi
        if (
            txtNamaBarang.getText().trim().isEmpty() ||
            txtSatuan.getText().trim().isEmpty() ||
            txtHargaJual.getText().trim().isEmpty() ||
            txtHargaBeli.getText().trim().isEmpty() ||
            txtStockBarang.getText().trim().isEmpty()
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
            String kode = txtKodeBarang.getText().trim();
            String nama = txtNamaBarang.getText().trim();
            String satuan = txtSatuan.getText().trim();
            double hargaJual = Double.parseDouble(
                txtHargaJual.getText().trim()
            );
            double hargaBeli = Double.parseDouble(
                txtHargaBeli.getText().trim()
            );
            int stok = Integer.parseInt(txtStockBarang.getText().trim());

            // Eksekusi query UPSERT: INSERT baru atau UPDATE jika kode sudah ada
            String sql =
                "INSERT INTO tb_barang (kd_barang, nama_barang, satuan, harga_jual, harga_beli_stok, stok_barang) " +
                "VALUES ('" +
                kode +
                "', '" +
                nama +
                "', '" +
                satuan +
                "', " +
                hargaJual +
                ", " +
                hargaBeli +
                ", " +
                stok +
                ") " +
                "ON DUPLICATE KEY UPDATE " +
                "nama_barang=VALUES(nama_barang), satuan=VALUES(satuan), " +
                "harga_jual=VALUES(harga_jual), harga_beli_stok=VALUES(harga_beli_stok), " +
                "stok_barang=VALUES(stok_barang)";
            Koneksi.ubahData(sql);

            JOptionPane.showMessageDialog(
                this,
                "Data barang berhasil disimpan/diperbarui!",
                "Sukses",
                JOptionPane.INFORMATION_MESSAGE
            );

            // Refresh tampilan
            muatDataBarang();
            bersihkanForm();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                this,
                "Harga dan stok harus berupa angka!",
                "Format Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void hapusBarang() {
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
            "Apakah Anda yakin ingin menghapus barang " + kode + "?",
            "Konfirmasi Hapus",
            JOptionPane.YES_NO_OPTION
        );

        if (konfirmasi == JOptionPane.YES_OPTION) {
            Koneksi.ubahData(
                "DELETE FROM tb_barang WHERE kd_barang = '" + kode + "'"
            );
            JOptionPane.showMessageDialog(
                this,
                "Data barang berhasil dihapus!",
                "Sukses",
                JOptionPane.INFORMATION_MESSAGE
            );
            muatDataBarang();
            bersihkanForm();
        }
    }

    private void bersihkanForm() {
        txtKodeBarang.setText(
            Koneksi.generateIdMaster("tb_barang", "kd_barang", "B")
        );
        txtNamaBarang.setText("");
        txtSatuan.setText("");
        txtHargaJual.setText("");
        txtHargaBeli.setText("");
        txtStockBarang.setText("");
    }

    private void clearField() {
        txtKodeBarang.setText(
            Koneksi.generateIdMaster("tb_barang", "kd_barang", "B")
        );
        txtKodeBarang.setEditable(false);
        txtNamaBarang.setText("");
        txtSatuan.setText("");
        txtHargaJual.setText("");
        txtHargaBeli.setText("");
        txtStockBarang.setText("");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        lblKodeBarang = new javax.swing.JLabel();
        txtKodeBarang = new javax.swing.JTextField();
        txtNamaBarang = new javax.swing.JTextField();
        lblNamaBarang = new javax.swing.JLabel();
        lblSatuan = new javax.swing.JLabel();
        txtSatuan = new javax.swing.JTextField();
        lblHargaJual = new javax.swing.JLabel();
        txtHargaJual = new javax.swing.JTextField();
        txtHargaBeli = new javax.swing.JTextField();
        lblHargaBeli = new javax.swing.JLabel();
        txtStockBarang = new javax.swing.JTextField();
        lblStockBarang = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        btnTambah = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        btnSImpan = new javax.swing.JButton();
        btnBatal = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        lblKodeBarang.setText("Kode Barang:");

        txtKodeBarang.setText("jTextField1");
        txtKodeBarang.addFocusListener(
            new java.awt.event.FocusAdapter() {
                public void focusLost(java.awt.event.FocusEvent evt) {
                    txtKodeBarangFocusLost(evt);
                }
            }
        );
        txtKodeBarang.addActionListener(
            new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    txtKodeBarangActionPerformed(evt);
                }
            }
        );

        txtNamaBarang.setText("jTextField1");
        txtNamaBarang.addActionListener(
            new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    txtNamaBarangActionPerformed(evt);
                }
            }
        );

        lblNamaBarang.setText("Nama Barang:");

        lblSatuan.setText("Satuan:");

        txtSatuan.setText("jTextField1");
        txtSatuan.addActionListener(
            new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    txtSatuanActionPerformed(evt);
                }
            }
        );

        lblHargaJual.setText("Harga Jual (Rp):");

        txtHargaJual.setText("jTextField1");
        txtHargaJual.addActionListener(
            new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    txtHargaJualActionPerformed(evt);
                }
            }
        );

        txtHargaBeli.setText("jTextField1");
        txtHargaBeli.addActionListener(
            new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    txtHargaBeliActionPerformed(evt);
                }
            }
        );

        lblHargaBeli.setText("Harga Beli (Rp):");

        txtStockBarang.setText("jTextField1");
        txtStockBarang.addActionListener(
            new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    txtStockBarangActionPerformed(evt);
                }
            }
        );

        lblStockBarang.setText("Stock Barang:");

        jTable1.setModel(
            new javax.swing.table.DefaultTableModel(
                new Object[][] {
                    { null, null, null, null, null, null },
                    { null, null, null, null, null, null },
                    { null, null, null, null, null, null },
                    { null, null, null, null, null, null },
                    { null, null, null, null, null, null },
                    { null, null, null, null, null, null },
                },
                new String[] {
                    "Kode Barang",
                    "Nama Barang",
                    "Satuan",
                    "Harga Jual",
                    "Harga Beli",
                    "Stock Barang",
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

        btnSImpan.setText("Simpan");
        btnSImpan.addActionListener(
            new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnSImpanActionPerformed(evt);
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
            getContentPane()
        );
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout
                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(
                    javax.swing.GroupLayout.Alignment.TRAILING,
                    layout
                        .createSequentialGroup()
                        .addContainerGap(33, Short.MAX_VALUE)
                        .addGroup(
                            layout
                                .createParallelGroup(
                                    javax.swing.GroupLayout.Alignment.LEADING
                                )
                                .addComponent(
                                    lblKodeBarang,
                                    javax.swing.GroupLayout.PREFERRED_SIZE,
                                    70,
                                    javax.swing.GroupLayout.PREFERRED_SIZE
                                )
                                .addComponent(lblNamaBarang)
                                .addComponent(
                                    lblSatuan,
                                    javax.swing.GroupLayout.PREFERRED_SIZE,
                                    39,
                                    javax.swing.GroupLayout.PREFERRED_SIZE
                                )
                                .addComponent(
                                    lblHargaJual,
                                    javax.swing.GroupLayout.PREFERRED_SIZE,
                                    83,
                                    javax.swing.GroupLayout.PREFERRED_SIZE
                                )
                                .addComponent(
                                    lblHargaBeli,
                                    javax.swing.GroupLayout.PREFERRED_SIZE,
                                    82,
                                    javax.swing.GroupLayout.PREFERRED_SIZE
                                )
                                .addComponent(lblStockBarang)
                                .addComponent(btnTambah)
                                .addComponent(btnSImpan)
                        )
                        .addGap(18, 18, 18)
                        .addGroup(
                            layout
                                .createParallelGroup(
                                    javax.swing.GroupLayout.Alignment.LEADING
                                )
                                .addComponent(
                                    btnHapus,
                                    javax.swing.GroupLayout.Alignment.TRAILING
                                )
                                .addComponent(
                                    btnBatal,
                                    javax.swing.GroupLayout.Alignment.TRAILING
                                )
                                .addGroup(
                                    layout
                                        .createSequentialGroup()
                                        .addGap(1, 1, 1)
                                        .addGroup(
                                            layout
                                                .createParallelGroup(
                                                    javax.swing.GroupLayout.Alignment.LEADING
                                                )
                                                .addComponent(
                                                    txtKodeBarang,
                                                    javax.swing.GroupLayout.Alignment.TRAILING,
                                                    javax.swing.GroupLayout.PREFERRED_SIZE,
                                                    71,
                                                    javax.swing.GroupLayout.PREFERRED_SIZE
                                                )
                                                .addComponent(
                                                    txtNamaBarang,
                                                    javax.swing.GroupLayout.Alignment.TRAILING,
                                                    javax.swing.GroupLayout.PREFERRED_SIZE,
                                                    71,
                                                    javax.swing.GroupLayout.PREFERRED_SIZE
                                                )
                                                .addComponent(
                                                    txtSatuan,
                                                    javax.swing.GroupLayout.Alignment.TRAILING,
                                                    javax.swing.GroupLayout.PREFERRED_SIZE,
                                                    71,
                                                    javax.swing.GroupLayout.PREFERRED_SIZE
                                                )
                                                .addComponent(
                                                    txtHargaJual,
                                                    javax.swing.GroupLayout.Alignment.TRAILING,
                                                    javax.swing.GroupLayout.PREFERRED_SIZE,
                                                    71,
                                                    javax.swing.GroupLayout.PREFERRED_SIZE
                                                )
                                                .addComponent(
                                                    txtHargaBeli,
                                                    javax.swing.GroupLayout.Alignment.TRAILING,
                                                    javax.swing.GroupLayout.PREFERRED_SIZE,
                                                    71,
                                                    javax.swing.GroupLayout.PREFERRED_SIZE
                                                )
                                                .addComponent(
                                                    txtStockBarang,
                                                    javax.swing.GroupLayout.Alignment.TRAILING,
                                                    javax.swing.GroupLayout.PREFERRED_SIZE,
                                                    71,
                                                    javax.swing.GroupLayout.PREFERRED_SIZE
                                                )
                                        )
                                )
                        )
                        .addGap(18, 18, 18)
                        .addComponent(
                            jScrollPane1,
                            javax.swing.GroupLayout.PREFERRED_SIZE,
                            526,
                            javax.swing.GroupLayout.PREFERRED_SIZE
                        )
                        .addGap(20, 20, 20)
                )
        );
        layout.setVerticalGroup(
            layout
                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(
                    layout
                        .createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(
                            layout
                                .createParallelGroup(
                                    javax.swing.GroupLayout.Alignment.LEADING
                                )
                                .addGroup(
                                    layout
                                        .createSequentialGroup()
                                        .addGroup(
                                            layout
                                                .createParallelGroup(
                                                    javax.swing.GroupLayout.Alignment.BASELINE
                                                )
                                                .addComponent(lblKodeBarang)
                                                .addComponent(
                                                    txtKodeBarang,
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
                                                .addComponent(lblNamaBarang)
                                                .addComponent(
                                                    txtNamaBarang,
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
                                                .addComponent(lblSatuan)
                                                .addComponent(
                                                    txtSatuan,
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
                                                .addComponent(lblHargaJual)
                                                .addComponent(
                                                    txtHargaJual,
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
                                                .addComponent(lblHargaBeli)
                                                .addComponent(
                                                    txtHargaBeli,
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
                                                .addComponent(lblStockBarang)
                                                .addComponent(
                                                    txtStockBarang,
                                                    javax.swing.GroupLayout.PREFERRED_SIZE,
                                                    javax.swing.GroupLayout.DEFAULT_SIZE,
                                                    javax.swing.GroupLayout.PREFERRED_SIZE
                                                )
                                        )
                                        .addGap(31, 31, 31)
                                        .addGroup(
                                            layout
                                                .createParallelGroup(
                                                    javax.swing.GroupLayout.Alignment.LEADING
                                                )
                                                .addGroup(
                                                    layout
                                                        .createSequentialGroup()
                                                        .addGap(41, 41, 41)
                                                        .addGroup(
                                                            layout
                                                                .createParallelGroup(
                                                                    javax.swing.GroupLayout.Alignment.BASELINE
                                                                )
                                                                .addComponent(
                                                                    btnSImpan
                                                                )
                                                                .addComponent(
                                                                    btnBatal
                                                                )
                                                        )
                                                )
                                                .addGroup(
                                                    layout
                                                        .createParallelGroup(
                                                            javax.swing.GroupLayout.Alignment.BASELINE
                                                        )
                                                        .addComponent(btnHapus)
                                                        .addComponent(btnTambah)
                                                )
                                        )
                                )
                                .addComponent(
                                    jScrollPane1,
                                    javax.swing.GroupLayout.PREFERRED_SIZE,
                                    382,
                                    javax.swing.GroupLayout.PREFERRED_SIZE
                                )
                        )
                        .addContainerGap(29, Short.MAX_VALUE)
                )
        );

        pack();
    } // </editor-fold>//GEN-END:initComponents

    private void txtKodeBarangActionPerformed(java.awt.event.ActionEvent evt) {
        //GEN-FIRST:event_txtKodeBarangActionPerformed
        // TODO add your handling code here:
    } //GEN-LAST:event_txtKodeBarangActionPerformed

    private void txtNamaBarangActionPerformed(java.awt.event.ActionEvent evt) {
        //GEN-FIRST:event_txtNamaBarangActionPerformed
        // TODO add your handling code here:
        txtSatuan.requestFocus();
    } //GEN-LAST:event_txtNamaBarangActionPerformed

    private void txtSatuanActionPerformed(java.awt.event.ActionEvent evt) {
        //GEN-FIRST:event_txtSatuanActionPerformed
        // TODO add your handling code here:
        txtHargaJual.requestFocus();
    } //GEN-LAST:event_txtSatuanActionPerformed

    private void txtHargaJualActionPerformed(java.awt.event.ActionEvent evt) {
        //GEN-FIRST:event_txtHargaJualActionPerformed
        // TODO add your handling code here:
        txtHargaBeli.requestFocus();
    } //GEN-LAST:event_txtHargaJualActionPerformed

    private void txtHargaBeliActionPerformed(java.awt.event.ActionEvent evt) {
        //GEN-FIRST:event_txtHargaBeliActionPerformed
        // TODO add your handling code here:
        txtStockBarang.requestFocus();
    } //GEN-LAST:event_txtHargaBeliActionPerformed

    private void txtStockBarangActionPerformed(java.awt.event.ActionEvent evt) {
        //GEN-FIRST:event_txtStockBarangActionPerformed
        // TODO add your handling code here:
    } //GEN-LAST:event_txtStockBarangActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {
        //GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
        int baris = jTable1.getSelectedRow();
        if (baris >= 0) {
            txtKodeBarang.setText(jTable1.getValueAt(baris, 0).toString());
            txtNamaBarang.setText(jTable1.getValueAt(baris, 1).toString());
            txtSatuan.setText(jTable1.getValueAt(baris, 2).toString());
            txtHargaJual.setText(jTable1.getValueAt(baris, 3).toString());
            txtHargaBeli.setText(jTable1.getValueAt(baris, 4).toString());
            txtStockBarang.setText(jTable1.getValueAt(baris, 5).toString());
        }
    } //GEN-LAST:event_jTable1MouseClicked

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {
        //GEN-FIRST:event_btnTambahActionPerformed
        // TODO add your handling code here:
        simpanBarang();
    } //GEN-LAST:event_btnTambahActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {
        //GEN-FIRST:event_btnHapusActionPerformed
        // TODO add your handling code here:
        hapusBarang();
    } //GEN-LAST:event_btnHapusActionPerformed

    private void btnSImpanActionPerformed(java.awt.event.ActionEvent evt) {
        //GEN-FIRST:event_btnSImpanActionPerformed
        // TODO add your handling code here:
        simpanBarang();
    } //GEN-LAST:event_btnSImpanActionPerformed

    private void btnBatalActionPerformed(java.awt.event.ActionEvent evt) {
        //GEN-FIRST:event_btnBatalActionPerformed
        // TODO add your handling code here:
        clearField();
    } //GEN-LAST:event_btnBatalActionPerformed

    private void txtKodeBarangFocusLost(java.awt.event.FocusEvent evt) {
        //GEN-FIRST:event_txtKodeBarangFocusLost
        // TODO add your handling code here:
    } //GEN-LAST:event_txtKodeBarangFocusLost

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
            new FrameBarang().setVisible(true)
        );
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBatal;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnSImpan;
    private javax.swing.JButton btnTambah;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblHargaBeli;
    private javax.swing.JLabel lblHargaJual;
    private javax.swing.JLabel lblKodeBarang;
    private javax.swing.JLabel lblNamaBarang;
    private javax.swing.JLabel lblSatuan;
    private javax.swing.JLabel lblStockBarang;
    private javax.swing.JTextField txtHargaBeli;
    private javax.swing.JTextField txtHargaJual;
    private javax.swing.JTextField txtKodeBarang;
    private javax.swing.JTextField txtNamaBarang;
    private javax.swing.JTextField txtSatuan;
    private javax.swing.JTextField txtStockBarang;
    // End of variables declaration//GEN-END:variables
}
