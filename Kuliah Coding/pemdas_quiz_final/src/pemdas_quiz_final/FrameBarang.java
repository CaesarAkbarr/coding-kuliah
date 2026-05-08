/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package pemdas_quiz_final;

import java.sql.Connection;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ROG G513RM
 */
public class FrameBarang extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrameBarang.class.getName());

    /**
     * Creates new form FrameBarang
     */
    public FrameBarang() {
        initComponents();
        setLocationRelativeTo(null);

        // Inisialisasi saat frame dibuka
        inisialisasiFrame();
    }

    // Menyiapkan kode otomatis dan tabel data barang
    private void inisialisasiFrame() {
        // Auto-generate kode barang dan buat field read-only
        txtKodeBarang.setText(Koneksi.generateIdMaster("tb_barang", "kd_barang", "B"));
        txtKodeBarang.setEditable(false);

        // Tampilkan data barang yang sudah ada di tabel
        muatDataBarang();

        // Tombol Batal: bersihkan form dan generate kode baru
        btnBatal.addActionListener(e -> bersihkanForm());

        // Tombol Tambah: bersihkan form untuk input baru
        btnTambah.addActionListener(e -> {
            bersihkanForm();
            txtNamaBarang.requestFocus();
        });

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
            String sql   = "SELECT kd_barang, nama_barang, satuan, harga_jual, harga_beli_stok, stok_barang FROM tb_barang";
            ResultSet rs = c.createStatement().executeQuery(sql);

            while (rs.next()) {
                model.addRow(new Object[]{
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
            String  kode     = txtKodeBarang.getText().trim();
            String  nama     = txtNamaBarang.getText().trim();
            String  satuan   = txtSatuan.getText().trim();
            double  hargaJual = Double.parseDouble(txtHargaJual.getText().trim());
            double  hargaBeli = Double.parseDouble(txtHargaBeli.getText().trim());
            int     stok     = Integer.parseInt(txtStockBarang.getText().trim());

            // Eksekusi query INSERT
            String sql = "INSERT INTO tb_barang (kd_barang, nama_barang, satuan, harga_jual, harga_beli_stok, stok_barang) "
                       + "VALUES ('" + kode + "', '" + nama + "', '" + satuan + "', "
                       + hargaJual + ", " + hargaBeli + ", " + stok + ")";
            Koneksi.ubahData(sql);

            JOptionPane.showMessageDialog(this, "Data barang berhasil disimpan!",
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lblKodeBarang.setText("Kode Barang:");

        txtKodeBarang.setText("jTextField1");

        txtNamaBarang.setText("jTextField1");

        lblNamaBarang.setText("Nama Barang:");

        lblSatuan.setText("Satuan:");

        txtSatuan.setText("jTextField1");

        lblHargaJual.setText("Harga Jual (Rp):");

        txtHargaJual.setText("jTextField1");

        txtHargaBeli.setText("jTextField1");

        lblHargaBeli.setText("Harga Beli (Rp):");

        txtStockBarang.setText("jTextField1");

        lblStockBarang.setText("Stock Barang:");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Kode Barang", "Nama Barang", "Satuan", "Harga Jual", "Harga Beli", "Stock Barang"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        btnTambah.setText("Tambah");

        btnHapus.setText("Hapus");

        btnSImpan.setText("Simpan");

        btnBatal.setText("Batal");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblKodeBarang)
                            .addComponent(lblNamaBarang)
                            .addComponent(lblSatuan)
                            .addComponent(lblHargaJual, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblHargaBeli, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblStockBarang))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtKodeBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNamaBarang, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtSatuan, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtHargaJual, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtHargaBeli, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtStockBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnTambah)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnHapus))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnSImpan)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnBatal)))
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 526, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(151, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(58, 58, 58)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblKodeBarang)
                            .addComponent(txtKodeBarang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblNamaBarang)
                            .addComponent(txtNamaBarang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblSatuan)
                            .addComponent(txtSatuan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblHargaJual)
                            .addComponent(txtHargaJual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblHargaBeli)
                            .addComponent(txtHargaBeli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblStockBarang)
                            .addComponent(txtStockBarang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnHapus)
                            .addComponent(btnTambah))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnSImpan)
                            .addComponent(btnBatal))
                        .addGap(0, 26, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
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
