/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package pemdasquizlagi;

import java.awt.Frame;
import java.sql.Connection;
import java.sql.ResultSet;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ROG G513RM
 */
public class FramePembelianDetail extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger =
        java.util.logging.Logger.getLogger(
            FramePembelianDetail.class.getName()
        );

    /**
     * Creates new form FramePembelianDetail (default constructor)
     */
    public FramePembelianDetail() {
        initComponents();
    }

    /**
     * Creates new form FramePembelianDetail
     * @param noPesanan Nomor pesanan pembelian
     * @param tanggal Tanggal pesanan
     * @param kdSupplier Kode supplier
     */
    public FramePembelianDetail(
        String noPesanan,
        String tanggal,
        String kdSupplier
    ) {
        this.noPesanan = noPesanan;
        this.tanggal = tanggal;
        this.kdSupplier = kdSupplier;

        initComponents();

        txtBarang.setText("");
        txtJumlah.setText("");
        txtTotalHarga.setText("0");
        txtTotalHarga.setEditable(false);

        setTitle("Detail Pembelian: " + noPesanan);
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

        muatDataBarang();
        aturKolomTabel();
    }

    // Variabel untuk menyimpan data yang diterima dari FramePembelian
    private String noPesanan;
    private String tanggal;
    private String kdSupplier;
    private double grandTotal = 0;

    private void aturKolomTabel() {
        DefaultTableModel model = new DefaultTableModel(
            new String[] {
                "Kode Barang",
                "Nama Barang",
                "Harga Beli (Rp)",
                "Jumlah",
                "Subtotal (Rp)",
            },
            0
        ) {
            // Buat semua sel tidak bisa diedit langsung dari tabel
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        jTable1.setModel(model);
    }

    private void muatDataBarang() {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        model.addElement("-- Pilih Barang --");

        try {
            Connection c = Koneksi.getKoneksi();
            String sql =
                "SELECT kd_barang, nama_barang FROM tb_barang ORDER BY kd_barang";
            ResultSet rs = c.createStatement().executeQuery(sql);

            while (rs.next()) {
                // Format: "B00001 - Nama Barang"
                model.addElement(
                    rs.getString("kd_barang") +
                        " - " +
                        rs.getString("nama_barang")
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

        cmbBarang.setModel(model);
    }

    private void isiHargaOtomatis() {
        if (cmbBarang.getSelectedIndex() == 0) {
            txtBarang.setText("");
            txtBarang.setEditable(true);
            return;
        }

        String pilihan = cmbBarang.getSelectedItem().toString();
        String kdBarang = pilihan.split(" - ")[0];

        try {
            Connection c = Koneksi.getKoneksi();
            String sql =
                "SELECT harga_beli_stok FROM tb_barang WHERE kd_barang = '" +
                kdBarang +
                "'";
            ResultSet rs = c.createStatement().executeQuery(sql);

            if (rs.next()) {
                // Tampilkan harga beli di field txtBarang dan jadikan read-only
                txtBarang.setText(
                    String.valueOf(rs.getDouble("harga_beli_stok"))
                );
                txtBarang.setEditable(false);
            }
            rs.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                this,
                "Gagal mengambil harga: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void tambahItemKeTable() {
        // Validasi input
        if (cmbBarang.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(
                this,
                "Pilih barang terlebih dahulu!",
                "Peringatan",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        if (txtBarang.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Harga barang belum terbaca dari database!",
                "Peringatan",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }
        if (txtJumlah.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Masukkan jumlah barang!",
                "Peringatan",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        try {
            String pilihan = cmbBarang.getSelectedItem().toString();
            String kdBarang = pilihan.split(" - ")[0];
            String nmBarang = pilihan.split(" - ")[1];
            double harga = Double.parseDouble(txtBarang.getText().trim());
            int jumlah = Integer.parseInt(txtJumlah.getText().trim());

            // Validasi jumlah harus positif
            if (jumlah <= 0) {
                JOptionPane.showMessageDialog(
                    this,
                    "Jumlah harus lebih dari 0!",
                    "Peringatan",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            double subtotal = harga * jumlah;

            // Tambahkan baris ke tabel transaksi
            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.addRow(
                new Object[] { kdBarang, nmBarang, harga, jumlah, subtotal }
            );

            // Update grand total secara real-time
            grandTotal += subtotal;
            txtTotalHarga.setText(String.format("%,.2f", grandTotal));

            // Reset field pilihan setelah ditambahkan
            cmbBarang.setSelectedIndex(0);
            txtBarang.setText("");
            txtBarang.setEditable(true);
            txtJumlah.setText("");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                this,
                "Jumlah harus berupa angka bulat!",
                "Format Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void hapusItemDariTable() {
        int baris = jTable1.getSelectedRow();
        if (baris < 0) {
            JOptionPane.showMessageDialog(
                this,
                "Pilih baris yang ingin dihapus!",
                "Peringatan",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // Kurangi grand total sebelum baris dihapus
        double subtotal = Double.parseDouble(
            jTable1.getValueAt(baris, 4).toString()
        );
        grandTotal -= subtotal;
        txtTotalHarga.setText(String.format("%,.2f", grandTotal));

        // Hapus baris dari tabel
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.removeRow(baris);
    }

    private void simpanTransaksi() {
        // Validasi: tabel tidak boleh kosong
        if (jTable1.getRowCount() == 0) {
            JOptionPane.showMessageDialog(
                this,
                "Tambahkan minimal satu barang ke transaksi!",
                "Peringatan",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        Connection conn = null;
        try {
            conn = Koneksi.getKoneksi();

            // Mulai database transaction — nonaktifkan auto commit
            conn.setAutoCommit(false);

            // ——— INSERT ke tb_pembelian (header transaksi) ———
            String sqlHeader =
                "INSERT INTO tb_pembelian (kd_pembelian, tgl_pembelian, kd_supplier, total_bayar) " +
                "VALUES ('" +
                noPesanan +
                "', '" +
                tanggal +
                "', '" +
                kdSupplier +
                "', " +
                grandTotal +
                ")";
            conn.createStatement().executeUpdate(sqlHeader);

            // ——— INSERT ke tb_pembelian_detail (detail per item) + UPDATE stok ———
            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            for (int i = 0; i < model.getRowCount(); i++) {
                String kdBarang = model.getValueAt(i, 0).toString();
                double harga = Double.parseDouble(
                    model.getValueAt(i, 2).toString()
                );
                int jumlah = Integer.parseInt(
                    model.getValueAt(i, 3).toString()
                );
                double subtotal = Double.parseDouble(
                    model.getValueAt(i, 4).toString()
                );

                // INSERT baris detail pembelian
                String sqlDetail =
                    "INSERT INTO tb_pembelian_detail (kd_pembelian, kd_barang, jumlah, subtotal) " +
                    "VALUES ('" +
                    noPesanan +
                    "', '" +
                    kdBarang +
                    "', " +
                    jumlah +
                    ", " +
                    subtotal +
                    ")";
                conn.createStatement().executeUpdate(sqlDetail);

                // UPDATE stok barang: tambah stok sesuai jumlah yang dibeli
                String sqlUpdateStok =
                    "UPDATE tb_barang SET stok_barang = stok_barang + " +
                    jumlah +
                    " WHERE kd_barang = '" +
                    kdBarang +
                    "'";
                conn.createStatement().executeUpdate(sqlUpdateStok);
            }

            // Commit: semua query berhasil, simpan permanen ke database
            conn.commit();

            JOptionPane.showMessageDialog(
                this,
                "Transaksi pembelian " +
                    noPesanan +
                    " berhasil disimpan!\n" +
                    "Total: Rp " +
                    String.format("%,.2f", grandTotal),
                "Sukses",
                JOptionPane.INFORMATION_MESSAGE
            );

            this.dispose(); // Tutup frame setelah sukses
        } catch (Exception e) {
            // Rollback: batalkan semua perubahan jika terjadi error
            try {
                if (conn != null) conn.rollback();
            } catch (Exception ex) {
                System.err.println("Rollback gagal: " + ex.getMessage());
            }
            JOptionPane.showMessageDialog(
                this,
                "Transaksi gagal! Semua perubahan dibatalkan.\nError: " +
                    e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        } finally {
            // Kembalikan auto commit ke true setelah selesai
            try {
                if (conn != null) conn.setAutoCommit(true);
            } catch (Exception ex) {
                System.err.println(
                    "Gagal reset autoCommit: " + ex.getMessage()
                );
            }
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
        lblBarang = new javax.swing.JLabel();
        cmbBarang = new javax.swing.JComboBox<>();
        lblHarga = new javax.swing.JLabel();
        txtBarang = new javax.swing.JTextField();
        lblJumlah = new javax.swing.JLabel();
        txtJumlah = new javax.swing.JTextField();
        btnHapus = new javax.swing.JButton();
        btnTambah = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        lblTotalHarga = new javax.swing.JLabel();
        btnBeli = new javax.swing.JButton();
        txtTotalHarga = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        lblBarang.setText("Barang:");

        cmbBarang.setModel(
            new javax.swing.DefaultComboBoxModel<>(
                new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }
            )
        );
        cmbBarang.addActionListener(
            new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    cmbBarangActionPerformed(evt);
                }
            }
        );

        lblHarga.setText("Harga (Rp):");

        txtBarang.setText("jTextField1");
        txtBarang.addActionListener(
            new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    txtBarangActionPerformed(evt);
                }
            }
        );

        lblJumlah.setText("Jumlah:");

        txtJumlah.setText("jTextField1");
        txtJumlah.addActionListener(
            new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    txtJumlahActionPerformed(evt);
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

        btnTambah.setText("Tambah");
        btnTambah.addActionListener(
            new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnTambahActionPerformed(evt);
                }
            }
        );

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
                    "Kode Barang",
                    "Nama Barang",
                    "Harga Jual",
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

        lblTotalHarga.setText("Total (Rp):");

        btnBeli.setText("Beli");
        btnBeli.addActionListener(
            new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnBeliActionPerformed(evt);
                }
            }
        );

        txtTotalHarga.setText("jTextField1");
        txtTotalHarga.addActionListener(
            new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    txtTotalHargaActionPerformed(evt);
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
                        .addGroup(
                            layout
                                .createParallelGroup(
                                    javax.swing.GroupLayout.Alignment.TRAILING
                                )
                                .addGroup(
                                    layout
                                        .createSequentialGroup()
                                        .addContainerGap(
                                            javax.swing.GroupLayout.DEFAULT_SIZE,
                                            Short.MAX_VALUE
                                        )
                                        .addGroup(
                                            layout
                                                .createParallelGroup(
                                                    javax.swing.GroupLayout.Alignment.LEADING
                                                )
                                                .addGroup(
                                                    javax.swing.GroupLayout.Alignment.TRAILING,
                                                    layout
                                                        .createSequentialGroup()
                                                        .addComponent(
                                                            lblTotalHarga
                                                        )
                                                        .addGap(18, 18, 18)
                                                        .addComponent(
                                                            txtTotalHarga,
                                                            javax.swing.GroupLayout.PREFERRED_SIZE,
                                                            110,
                                                            javax.swing.GroupLayout.PREFERRED_SIZE
                                                        )
                                                )
                                                .addComponent(
                                                    btnBeli,
                                                    javax.swing.GroupLayout.Alignment.TRAILING
                                                )
                                        )
                                )
                                .addGroup(
                                    javax.swing.GroupLayout.Alignment.LEADING,
                                    layout
                                        .createSequentialGroup()
                                        .addGap(32, 32, 32)
                                        .addGroup(
                                            layout
                                                .createParallelGroup(
                                                    javax.swing.GroupLayout.Alignment.TRAILING,
                                                    false
                                                )
                                                .addGroup(
                                                    layout
                                                        .createSequentialGroup()
                                                        .addComponent(btnTambah)
                                                        .addGap(18, 18, 18)
                                                        .addComponent(btnHapus)
                                                )
                                                .addGroup(
                                                    layout
                                                        .createSequentialGroup()
                                                        .addGroup(
                                                            layout
                                                                .createParallelGroup(
                                                                    javax.swing.GroupLayout.Alignment.TRAILING
                                                                )
                                                                .addComponent(
                                                                    lblHarga
                                                                )
                                                                .addComponent(
                                                                    lblBarang
                                                                )
                                                        )
                                                        .addGap(18, 18, 18)
                                                        .addGroup(
                                                            layout
                                                                .createParallelGroup(
                                                                    javax.swing.GroupLayout.Alignment.LEADING,
                                                                    false
                                                                )
                                                                .addComponent(
                                                                    cmbBarang,
                                                                    javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                    394,
                                                                    javax.swing.GroupLayout.PREFERRED_SIZE
                                                                )
                                                                .addGroup(
                                                                    layout
                                                                        .createSequentialGroup()
                                                                        .addComponent(
                                                                            txtBarang,
                                                                            javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                            158,
                                                                            javax.swing.GroupLayout.PREFERRED_SIZE
                                                                        )
                                                                        .addGap(
                                                                            18,
                                                                            18,
                                                                            18
                                                                        )
                                                                        .addComponent(
                                                                            lblJumlah
                                                                        )
                                                                        .addGap(
                                                                            18,
                                                                            18,
                                                                            18
                                                                        )
                                                                        .addComponent(
                                                                            txtJumlah,
                                                                            javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                            161,
                                                                            javax.swing.GroupLayout.PREFERRED_SIZE
                                                                        )
                                                                )
                                                        )
                                                )
                                                .addComponent(
                                                    jScrollPane1,
                                                    javax.swing.GroupLayout.PREFERRED_SIZE,
                                                    473,
                                                    javax.swing.GroupLayout.PREFERRED_SIZE
                                                )
                                        )
                                )
                        )
                        .addContainerGap(33, Short.MAX_VALUE)
                )
        );
        layout.setVerticalGroup(
            layout
                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(
                    layout
                        .createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(
                            layout
                                .createParallelGroup(
                                    javax.swing.GroupLayout.Alignment.BASELINE
                                )
                                .addComponent(lblBarang)
                                .addComponent(
                                    cmbBarang,
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
                                .addComponent(lblHarga)
                                .addComponent(
                                    txtBarang,
                                    javax.swing.GroupLayout.PREFERRED_SIZE,
                                    javax.swing.GroupLayout.DEFAULT_SIZE,
                                    javax.swing.GroupLayout.PREFERRED_SIZE
                                )
                                .addComponent(lblJumlah)
                                .addComponent(
                                    txtJumlah,
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
                                .addComponent(btnHapus)
                                .addComponent(btnTambah)
                        )
                        .addGap(18, 18, 18)
                        .addComponent(
                            jScrollPane1,
                            javax.swing.GroupLayout.PREFERRED_SIZE,
                            338,
                            javax.swing.GroupLayout.PREFERRED_SIZE
                        )
                        .addGap(18, 18, 18)
                        .addGroup(
                            layout
                                .createParallelGroup(
                                    javax.swing.GroupLayout.Alignment.BASELINE
                                )
                                .addComponent(lblTotalHarga)
                                .addComponent(
                                    txtTotalHarga,
                                    javax.swing.GroupLayout.PREFERRED_SIZE,
                                    javax.swing.GroupLayout.DEFAULT_SIZE,
                                    javax.swing.GroupLayout.PREFERRED_SIZE
                                )
                        )
                        .addGap(18, 18, 18)
                        .addComponent(btnBeli)
                        .addContainerGap(25, Short.MAX_VALUE)
                )
        );

        pack();
    } // </editor-fold>//GEN-END:initComponents

    private void cmbBarangActionPerformed(java.awt.event.ActionEvent evt) {
        //GEN-FIRST:event_cmbBarangActionPerformed
        // TODO add your handling code here:
        isiHargaOtomatis();
    } //GEN-LAST:event_cmbBarangActionPerformed

    private void txtBarangActionPerformed(java.awt.event.ActionEvent evt) {
        //GEN-FIRST:event_txtBarangActionPerformed
        // TODO add your handling code here:
    } //GEN-LAST:event_txtBarangActionPerformed

    private void txtJumlahActionPerformed(java.awt.event.ActionEvent evt) {
        //GEN-FIRST:event_txtJumlahActionPerformed
        // TODO add your handling code here:
    } //GEN-LAST:event_txtJumlahActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {
        //GEN-FIRST:event_btnHapusActionPerformed
        // TODO add your handling code here:
        hapusItemDariTable();
    } //GEN-LAST:event_btnHapusActionPerformed

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {
        //GEN-FIRST:event_btnTambahActionPerformed
        // TODO add your handling code here:
        tambahItemKeTable();
    } //GEN-LAST:event_btnTambahActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {
        //GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
    } //GEN-LAST:event_jTable1MouseClicked

    private void btnBeliActionPerformed(java.awt.event.ActionEvent evt) {
        //GEN-FIRST:event_btnBeliActionPerformed
        // TODO add your handling code here:
        simpanTransaksi();
    } //GEN-LAST:event_btnBeliActionPerformed

    private void txtTotalHargaActionPerformed(java.awt.event.ActionEvent evt) {
        //GEN-FIRST:event_txtTotalHargaActionPerformed
        // TODO add your handling code here:
    } //GEN-LAST:event_txtTotalHargaActionPerformed

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
            new FramePembelianDetail().setVisible(true)
        );
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBeli;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnTambah;
    private javax.swing.JComboBox<String> cmbBarang;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblBarang;
    private javax.swing.JLabel lblHarga;
    private javax.swing.JLabel lblJumlah;
    private javax.swing.JLabel lblTotalHarga;
    private javax.swing.JTextField txtBarang;
    private javax.swing.JTextField txtJumlah;
    private javax.swing.JTextField txtTotalHarga;
    // End of variables declaration//GEN-END:variables
}
