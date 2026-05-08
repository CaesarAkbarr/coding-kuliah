/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package pemdas_quiz_final;

import java.sql.Connection;
import java.sql.ResultSet;
import java.awt.Frame;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ROG G513RM
 */
public class FramePelanggan extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger
            .getLogger(FramePelanggan.class.getName());

    // Tombol yang ditambahkan secara programatik karena tidak ada di GUI Builder
    // private JButton btnSimpan;
    // private JButton btnBatal;
    // private JButton btnTambah;
    // private JButton btnHapus;

    /**
     * Creates new form FramePelanggan
     */
    public FramePelanggan() {
        initComponents();
        setExtendedState(Frame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setTitle("Form Pelanggan");

        // Bersihkan placeholder dan generate kode otomatis
        clearField();
        muatDataPelanggan();
    }

    // Mengosongkan field input dan generate kode pelanggan baru
    private void clearField() {
        txtKodePelanggan.setText(Koneksi.generateIdMaster("tb_pelanggan", "kd_pelanggan", "P"));
        txtKodePelanggan.setEditable(false);
        txtNamaPelanggan.setText("");
        txtNoTelp.setText("");
        txtAlamat.setText("");
    }

    // Memuat semua data pelanggan dari database ke JTable
    private void muatDataPelanggan() {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0); // Bersihkan tabel terlebih dahulu

        try {
            Connection c = Koneksi.getKoneksi();
            String sql = "SELECT kd_pelanggan, nama_pelanggan, alamat, no_telp FROM tb_pelanggan";
            ResultSet rs = c.createStatement().executeQuery(sql);

            while (rs.next()) {
                model.addRow(new Object[] {
                        rs.getString("kd_pelanggan"),
                        rs.getString("nama_pelanggan"),
                        rs.getString("alamat"),
                        rs.getString("no_telp")
                });
            }
            rs.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data pelanggan: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Menyimpan data pelanggan baru ke database
    private void simpanPelanggan() {
        // Validasi: semua field harus terisi
        if (txtNamaPelanggan.getText().trim().isEmpty()
                || txtNoTelp.getText().trim().isEmpty()
                || txtAlamat.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi!",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Ambil nilai dari form
            String kode = txtKodePelanggan.getText().trim();
            String nama = txtNamaPelanggan.getText().trim();
            String noTlp = txtNoTelp.getText().trim();
            String alamat = txtAlamat.getText().trim();

            // Eksekusi query UPSERT: INSERT baru atau UPDATE jika kode sudah ada
            String sql = "INSERT INTO tb_pelanggan (kd_pelanggan, nama_pelanggan, alamat, no_telp) "
                    + "VALUES ('" + kode + "', '" + nama + "', '" + alamat + "', '" + noTlp + "') "
                    + "ON DUPLICATE KEY UPDATE "
                    + "nama_pelanggan=VALUES(nama_pelanggan), alamat=VALUES(alamat), no_telp=VALUES(no_telp)";
            Koneksi.ubahData(sql);

            JOptionPane.showMessageDialog(this, "Data pelanggan berhasil disimpan/diperbarui!",
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);

            // Refresh tampilan
            muatDataPelanggan();
            bersihkanForm();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Menghapus pelanggan berdasarkan baris yang dipilih di tabel
    private void hapusPelanggan() {
        int baris = jTable1.getSelectedRow();
        if (baris < 0) {
            JOptionPane.showMessageDialog(this, "Pilih baris yang ingin dihapus terlebih dahulu!",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String kode = jTable1.getValueAt(baris, 0).toString();
        int konfirmasi = JOptionPane.showConfirmDialog(this,
                "Apakah Anda yakin ingin menghapus pelanggan " + kode + "?",
                "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);

        if (konfirmasi == JOptionPane.YES_OPTION) {
            Koneksi.ubahData("DELETE FROM tb_pelanggan WHERE kd_pelanggan = '" + kode + "'");
            JOptionPane.showMessageDialog(this, "Data pelanggan berhasil dihapus!",
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);
            muatDataPelanggan();
            bersihkanForm();
        }
    }

    // Membersihkan semua field form dan generate kode baru
    private void bersihkanForm() {
        txtKodePelanggan.setText(Koneksi.generateIdMaster("tb_pelanggan", "kd_pelanggan", "P"));
        txtNamaPelanggan.setText("");
        txtNoTelp.setText("");
        txtAlamat.setText("");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblKodePelanggan = new javax.swing.JLabel();
        txtKodePelanggan = new javax.swing.JTextField();
        txtNamaPelanggan = new javax.swing.JTextField();
        lblNamaPelanggan = new javax.swing.JLabel();
        lblNoTelp = new javax.swing.JLabel();
        txtNoTelp = new javax.swing.JTextField();
        lblAlamat = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtAlamat = new javax.swing.JTextArea();
        btnSImpan = new javax.swing.JButton();
        btnTambah = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        btnBatal = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lblKodePelanggan.setText("Kode Pelanggan:");

        txtKodePelanggan.setText("jTextField1");
        txtKodePelanggan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtKodePelangganActionPerformed(evt);
            }
        });

        txtNamaPelanggan.setText("jTextField1");
        txtNamaPelanggan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNamaPelangganActionPerformed(evt);
            }
        });

        lblNamaPelanggan.setText("Nama Pelanggan:");

        lblNoTelp.setText("No. Telp:");

        txtNoTelp.setText("jTextField1");
        txtNoTelp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNoTelpActionPerformed(evt);
            }
        });

        lblAlamat.setText("Alamat:");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null },
                        { null, null, null, null }
                },
                new String[] {
                        "Kode Pelanggan", "Nama Pelanggan", "Alamat", "No. Telp."
                }));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        txtAlamat.setColumns(20);
        txtAlamat.setRows(5);
        jScrollPane2.setViewportView(txtAlamat);

        btnSImpan.setText("Simpan");
        btnSImpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSImpanActionPerformed(evt);
            }
        });

        btnTambah.setText("Tambah");
        btnTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahActionPerformed(evt);
            }
        });

        btnHapus.setText("Hapus");
        btnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusActionPerformed(evt);
            }
        });

        btnBatal.setText("Batal");
        btnBatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBatalActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(47, 47, 47)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout
                                                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addGroup(layout.createSequentialGroup()
                                                        .addGroup(layout
                                                                .createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addComponent(lblKodePelanggan)
                                                                .addComponent(lblNamaPelanggan)
                                                                .addComponent(lblNoTelp)
                                                                .addComponent(lblAlamat,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 84,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGap(18, 18, 18)
                                                        .addGroup(layout
                                                                .createParallelGroup(
                                                                        javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addComponent(txtKodePelanggan,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 71,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(txtNamaPelanggan,
                                                                        javax.swing.GroupLayout.Alignment.TRAILING,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 71,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(txtNoTelp,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 71,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0,
                                                        Short.MAX_VALUE))
                                        .addGroup(layout
                                                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addGroup(layout.createSequentialGroup()
                                                        .addComponent(btnTambah)
                                                        .addPreferredGap(
                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(btnHapus))
                                                .addGroup(layout.createSequentialGroup()
                                                        .addComponent(btnSImpan)
                                                        .addPreferredGap(
                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(btnBatal))))
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 526,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(94, Short.MAX_VALUE)));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(58, 58, 58)
                                                .addGroup(layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(lblKodePelanggan)
                                                        .addComponent(txtKodePelanggan,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(18, 18, 18)
                                                .addGroup(layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(lblNamaPelanggan)
                                                        .addComponent(txtNamaPelanggan,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(18, 18, 18)
                                                .addGroup(layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(lblNoTelp)
                                                        .addComponent(txtNoTelp, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(18, 18, 18)
                                                .addComponent(lblAlamat)
                                                .addGap(18, 18, 18)
                                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addGroup(layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(btnHapus)
                                                        .addComponent(btnTambah))
                                                .addGap(18, 18, 18)
                                                .addGroup(layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(btnSImpan)
                                                        .addComponent(btnBatal))
                                                .addGap(0, 0, Short.MAX_VALUE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 380,
                                                        Short.MAX_VALUE)))
                                .addGap(19, 19, 19)));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnTambahActionPerformed
        // Simpan data ke database (UPSERT), lalu refresh tabel
        simpanPelanggan();
    }// GEN-LAST:event_btnTambahActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnHapusActionPerformed
        // Hapus data berdasarkan baris terpilih di tabel
        hapusPelanggan();
    }// GEN-LAST:event_btnHapusActionPerformed

    private void btnSImpanActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnSImpanActionPerformed
        // Simpan data ke database (UPSERT), lalu refresh tabel
        simpanPelanggan();
    }// GEN-LAST:event_btnSImpanActionPerformed

    private void btnBatalActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnBatalActionPerformed
        // Bersihkan form dan generate kode baru
        clearField();
    }// GEN-LAST:event_btnBatalActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jTable1MouseClicked
        // Klik baris tabel → isi data ke form (Data Binding)
        int baris = jTable1.getSelectedRow();
        if (baris >= 0) {
            txtKodePelanggan.setText(jTable1.getValueAt(baris, 0).toString());
            txtNamaPelanggan.setText(jTable1.getValueAt(baris, 1).toString());
            txtAlamat.setText(jTable1.getValueAt(baris, 2).toString());
            txtNoTelp.setText(jTable1.getValueAt(baris, 3).toString());
        }
    }// GEN-LAST:event_jTable1MouseClicked

    private void txtKodePelangganActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtKodePelangganActionPerformed
        // Tidak ada aksi khusus
    }// GEN-LAST:event_txtKodePelangganActionPerformed

    private void txtNamaPelangganActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtNamaPelangganActionPerformed
        // Pindah fokus ke No. Telp saat Enter
        txtNoTelp.requestFocus();
    }// GEN-LAST:event_txtNamaPelangganActionPerformed

    private void txtNoTelpActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtNoTelpActionPerformed
        // Pindah fokus ke Alamat saat Enter
        txtAlamat.requestFocus();
    }// GEN-LAST:event_txtNoTelpActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the default
         * look and feel.
         * For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new FramePelanggan().setVisible(true));
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
    private javax.swing.JLabel lblKodePelanggan;
    private javax.swing.JLabel lblNamaPelanggan;
    private javax.swing.JLabel lblNoTelp;
    private javax.swing.JTextArea txtAlamat;
    private javax.swing.JTextField txtKodePelanggan;
    private javax.swing.JTextField txtNamaPelanggan;
    private javax.swing.JTextField txtNoTelp;
    // End of variables declaration//GEN-END:variables
}
