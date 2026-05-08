/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package pemdas_quiz_final;

import java.sql.Connection;
import java.sql.ResultSet;
import java.awt.Frame;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ROG G513RM
 */
public class FrameBarang extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger
            .getLogger(FrameBarang.class.getName());

    /**
     * Creates new form FrameBarang
     */
    public FrameBarang() {
        initComponents();
        setExtendedState(Frame.MAXIMIZED_BOTH);
        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setTitle("Form Barang");

        // Bersihkan teks placeholder "jTextField1" dari semua field
        clearField();

        // Inisialisasi saat frame dibuka
        inisialisasiFrame();
    }

    // Mengosongkan semua JTextField (kecuali kode yang di-generate otomatis)
    private void clearField() {
        txtNamaBarang.setText("");
        txtSatuan.setText("");
        txtHargaJual.setText("");
        txtHargaBeli.setText("");
        txtStockBarang.setText("");
    }

    // Menyiapkan kode otomatis, tabel data, dan event listener
    private void inisialisasiFrame() {
        // Auto-generate kode barang dan buat field read-only
        txtKodeBarang.setText(Koneksi.generateIdMaster("tb_barang", "kd_barang", "B"));
        txtKodeBarang.setEditable(false);

        // Tampilkan data barang yang sudah ada di tabel
        muatDataBarang();

        // MouseListener: klik baris tabel → isi data ke form (Data Binding)
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int baris = jTable1.getSelectedRow();
                if (baris >= 0) {
                    txtKodeBarang.setText(jTable1.getValueAt(baris, 0).toString());
                    txtNamaBarang.setText(jTable1.getValueAt(baris, 1).toString());
                    txtSatuan.setText(jTable1.getValueAt(baris, 2).toString());
                    txtHargaJual.setText(jTable1.getValueAt(baris, 3).toString());
                    txtHargaBeli.setText(jTable1.getValueAt(baris, 4).toString());
                    txtStockBarang.setText(jTable1.getValueAt(baris, 5).toString());
                }
            }
        });

        // Tombol Batal: bersihkan form dan generate kode baru
        btnBatal.addActionListener(e -> bersihkanForm());

        // Tombol Tambah: simpan data ke database, lalu bersihkan form
        btnTambah.addActionListener(e -> simpanBarang());

        // Tombol Hapus: hapus baris yang dipilih dari tabel dan database
        btnHapus.addActionListener(e -> hapusBarang());

        // Tombol Simpan: validasi lalu simpan ke database
        btnSImpan.addActionListener(e -> simpanBarang());
    }

    // Memuat semua data barang dari database ke JTable
    private void muatDataBarang() {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0); // Bersihkan tabel terlebih dahulu

        try {
            Connection c = Koneksi.getKoneksi();
            String sql = "SELECT kd_barang, nama_barang, satuan, harga_jual, harga_beli_stok, stok_barang FROM tb_barang";
            ResultSet rs = c.createStatement().executeQuery(sql);

            while (rs.next()) {
                model.addRow(new Object[] {
                        rs.getString("kd_barang"),
                        rs.getString("nama_barang"),
                        rs.getString("satuan"),
                        rs.getDouble("harga_jual"),
                        rs.getDouble("harga_beli_stok"),
                        rs.getInt("stok_barang")
                });
            }
            rs.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data barang: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Menyimpan data barang baru ke database
    private void simpanBarang() {
        // Validasi: semua field harus terisi
        if (txtNamaBarang.getText().trim().isEmpty()
                || txtSatuan.getText().trim().isEmpty()
                || txtHargaJual.getText().trim().isEmpty()
                || txtHargaBeli.getText().trim().isEmpty()
                || txtStockBarang.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi!",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Ambil nilai dari form
            String kode = txtKodeBarang.getText().trim();
            String nama = txtNamaBarang.getText().trim();
            String satuan = txtSatuan.getText().trim();
            double hargaJual = Double.parseDouble(txtHargaJual.getText().trim());
            double hargaBeli = Double.parseDouble(txtHargaBeli.getText().trim());
            int stok = Integer.parseInt(txtStockBarang.getText().trim());

            // Eksekusi query UPSERT: INSERT baru atau UPDATE jika kode sudah ada
            String sql = "INSERT INTO tb_barang (kd_barang, nama_barang, satuan, harga_jual, harga_beli_stok, stok_barang) "
                    + "VALUES ('" + kode + "', '" + nama + "', '" + satuan + "', "
                    + hargaJual + ", " + hargaBeli + ", " + stok + ") "
                    + "ON DUPLICATE KEY UPDATE "
                    + "nama_barang=VALUES(nama_barang), satuan=VALUES(satuan), "
                    + "harga_jual=VALUES(harga_jual), harga_beli_stok=VALUES(harga_beli_stok), "
                    + "stok_barang=VALUES(stok_barang)";
            Koneksi.ubahData(sql);

            JOptionPane.showMessageDialog(this, "Data barang berhasil disimpan/diperbarui!",
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);

            // Refresh tampilan
            muatDataBarang();
            bersihkanForm();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Harga dan stok harus berupa angka!",
                    "Format Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Menghapus barang berdasarkan baris yang dipilih di tabel
    private void hapusBarang() {
        int baris = jTable1.getSelectedRow();
        if (baris < 0) {
            JOptionPane.showMessageDialog(this, "Pilih baris yang ingin dihapus terlebih dahulu!",
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String kode = jTable1.getValueAt(baris, 0).toString();
        int konfirmasi = JOptionPane.showConfirmDialog(this,
                "Apakah Anda yakin ingin menghapus barang " + kode + "?",
                "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);

        if (konfirmasi == JOptionPane.YES_OPTION) {
            Koneksi.ubahData("DELETE FROM tb_barang WHERE kd_barang = '" + kode + "'");
            JOptionPane.showMessageDialog(this, "Data barang berhasil dihapus!",
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);
            muatDataBarang();
            bersihkanForm();
        }
    }

    // Membersihkan semua field form dan generate kode baru
    private void bersihkanForm() {
        txtKodeBarang.setText(Koneksi.generateIdMaster("tb_barang", "kd_barang", "B"));
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
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        lblKodeBarang.setText("Kode Barang:");
        getContentPane().add(lblKodeBarang);
        lblKodeBarang.setBounds(38, 61, 71, 16);

        txtKodeBarang.setText("jTextField1");
        getContentPane().add(txtKodeBarang);
        txtKodeBarang.setBounds(140, 58, 71, 22);

        txtNamaBarang.setText("jTextField1");
        getContentPane().add(txtNamaBarang);
        txtNamaBarang.setBounds(140, 98, 71, 22);

        lblNamaBarang.setText("Nama Barang:");
        getContentPane().add(lblNamaBarang);
        lblNamaBarang.setBounds(38, 101, 75, 16);

        lblSatuan.setText("Satuan:");
        getContentPane().add(lblSatuan);
        lblSatuan.setBounds(38, 141, 38, 16);

        txtSatuan.setText("jTextField1");
        getContentPane().add(txtSatuan);
        txtSatuan.setBounds(140, 138, 71, 22);

        lblHargaJual.setText("Harga Jual (Rp):");
        getContentPane().add(lblHargaJual);
        lblHargaJual.setBounds(38, 181, 84, 16);

        txtHargaJual.setText("jTextField1");
        getContentPane().add(txtHargaJual);
        txtHargaJual.setBounds(140, 178, 71, 22);

        txtHargaBeli.setText("jTextField1");
        getContentPane().add(txtHargaBeli);
        txtHargaBeli.setBounds(140, 218, 71, 22);

        lblHargaBeli.setText("Harga Beli (Rp):");
        getContentPane().add(lblHargaBeli);
        lblHargaBeli.setBounds(38, 221, 84, 16);

        txtStockBarang.setText("jTextField1");
        getContentPane().add(txtStockBarang);
        txtStockBarang.setBounds(140, 258, 71, 22);

        lblStockBarang.setText("Stock Barang:");
        getContentPane().add(lblStockBarang);
        lblStockBarang.setBounds(38, 261, 72, 16);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {
                        { null, null, null, null, null, null },
                        { null, null, null, null, null, null },
                        { null, null, null, null, null, null },
                        { null, null, null, null, null, null },
                        { null, null, null, null, null, null },
                        { null, null, null, null, null, null }
                },
                new String[] {
                        "Kode Barang", "Nama Barang", "Satuan", "Harga Jual", "Harga Beli", "Stock Barang"
                }));
        jScrollPane1.setViewportView(jTable1);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(229, 6, 526, 382);

        btnTambah.setText("Tambah");
        getContentPane().add(btnTambah);
        btnTambah.setBounds(38, 298, 73, 23);

        btnHapus.setText("Hapus");
        getContentPane().add(btnHapus);
        btnHapus.setBounds(139, 298, 72, 23);

        btnSImpan.setText("Simpan");
        getContentPane().add(btnSImpan);
        btnSImpan.setBounds(38, 339, 72, 23);

        btnBatal.setText("Batal");
        getContentPane().add(btnBatal);
        btnBatal.setBounds(139, 339, 72, 23);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
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
        java.awt.EventQueue.invokeLater(() -> new FrameBarang().setVisible(true));
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
