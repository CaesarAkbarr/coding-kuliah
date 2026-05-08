/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package pemdas_quiz_final;

import java.sql.Connection;
import java.sql.ResultSet;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ROG G513RM
 */
public class FramePelanggan extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FramePelanggan.class.getName());

    // Tombol yang ditambahkan secara programatik karena tidak ada di GUI Builder
    private JButton btnSimpan;
    private JButton btnBatal;
    private JButton btnTambah;
    private JButton btnHapus;

    /**
     * Creates new form FramePelanggan
     */
    public FramePelanggan() {
        initComponents();
        setLocationRelativeTo(null);

        // Tambahkan tombol-tombol aksi secara programatik
        tambahTombol();

        // Inisialisasi data saat frame dibuka
        inisialisasiFrame();
    }

    // Menambahkan tombol ke dalam panel karena GUI Builder tidak memilikinya
    private void tambahTombol() {
        btnSimpan = new JButton("Simpan");
        btnBatal  = new JButton("Batal");
        btnTambah = new JButton("Tambah");
        btnHapus  = new JButton("Hapus");

        // Buat panel khusus untuk tombol
        javax.swing.JPanel panelTombol = new javax.swing.JPanel();
        panelTombol.add(btnTambah);
        panelTombol.add(btnSimpan);
        panelTombol.add(btnHapus);
        panelTombol.add(btnBatal);

        // Tambahkan panel tombol ke bawah content pane
        getContentPane().add(panelTombol, java.awt.BorderLayout.SOUTH);
        getContentPane().setLayout(new java.awt.BorderLayout());

        // Re-add panel utama ke tengah
        javax.swing.JPanel panelUtama = new javax.swing.JPanel();
        panelUtama.setLayout(new javax.swing.GroupLayout(panelUtama));
        getContentPane().add(panelUtama, java.awt.BorderLayout.CENTER);

        pack();
    }

    // Menyiapkan kode otomatis dan tabel data pelanggan
    private void inisialisasiFrame() {
        // Auto-generate kode pelanggan dan buat field read-only
        txtKodePelanggan.setText(Koneksi.generateIdMaster("tb_pelanggan", "kd_pelanggan", "P"));
        txtKodePelanggan.setEditable(false);

        // Tampilkan data pelanggan yang sudah ada di tabel
        muatDataPelanggan();

        // Daftarkan action listener untuk tombol-tombol
        btnBatal.addActionListener(e  -> bersihkanForm());
        btnTambah.addActionListener(e -> {
            bersihkanForm();
            txtNamaPelanggan.requestFocus();
        });
        btnHapus.addActionListener(e  -> hapusPelanggan());
        btnSimpan.addActionListener(e -> simpanPelanggan());
    }

    // Memuat semua data pelanggan dari database ke JTable
    private void muatDataPelanggan() {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0); // Bersihkan tabel terlebih dahulu

        try {
            Connection c = Koneksi.getKoneksi();
            String sql   = "SELECT kd_pelanggan, nama_pelanggan, alamat, no_telp FROM tb_pelanggan";
            ResultSet rs = c.createStatement().executeQuery(sql);

            while (rs.next()) {
                model.addRow(new Object[]{
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
            String kode   = txtKodePelanggan.getText().trim();
            String nama   = txtNamaPelanggan.getText().trim();
            String noTlp  = txtNoTelp.getText().trim();
            String alamat = txtAlamat.getText().trim();

            // Eksekusi query INSERT
            String sql = "INSERT INTO tb_pelanggan (kd_pelanggan, nama_pelanggan, alamat, no_telp) "
                       + "VALUES ('" + kode + "', '" + nama + "', '" + alamat + "', '" + noTlp + "')";
            Koneksi.ubahData(sql);

            JOptionPane.showMessageDialog(this, "Data pelanggan berhasil disimpan!",
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
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lblKodePelanggan.setText("Kode Pelanggan:");

        txtKodePelanggan.setText("jTextField1");

        txtNamaPelanggan.setText("jTextField1");

        lblNamaPelanggan.setText("Nama Pelanggan:");

        lblNoTelp.setText("No. Telp:");

        txtNoTelp.setText("jTextField1");

        lblAlamat.setText("Alamat:");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Kode Pelanggan", "Nama Pelanggan", "Alamat", "No. Telp."
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        txtAlamat.setColumns(20);
        txtAlamat.setRows(5);
        jScrollPane2.setViewportView(txtAlamat);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblKodePelanggan)
                            .addComponent(lblNamaPelanggan)
                            .addComponent(lblNoTelp)
                            .addComponent(lblAlamat, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtKodePelanggan, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNamaPelanggan, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNoTelp, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 526, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(58, 58, 58)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblKodePelanggan)
                            .addComponent(txtKodePelanggan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblNamaPelanggan)
                            .addComponent(txtNamaPelanggan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblNoTelp)
                            .addComponent(txtNoTelp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(lblAlamat)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)))
                .addGap(19, 19, 19))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //GEN-BEGIN:variables
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
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new FramePelanggan().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
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
