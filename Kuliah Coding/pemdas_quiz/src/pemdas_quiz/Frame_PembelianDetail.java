package pemdas_quiz;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Frame transaksi pembelian - Langkah 2 (Detail Item).
 * Menerima data header dari Frame_Pembelian via konstruktor berparameter.
 */
public class Frame_PembelianDetail extends javax.swing.JFrame {

    private final Frame_master parentFrame;
    private final String noPsn;
    private final String tgl;
    private final String kdSupp;
    private DefaultTableModel tableModel;
    private double totalHarga = 0.0;

    private static final DecimalFormat DF = new DecimalFormat("#,##0.00");
    private static final NumberFormat NF = NumberFormat.getNumberInstance(new Locale("id", "ID"));

    // Konstruktor menerima data dari Frame_Pembelian
    public Frame_PembelianDetail(Frame_master parent, String noPsn, String tgl, String kdSupp) {
        this.parentFrame = parent;
        this.noPsn = noPsn;
        this.tgl = tgl;
        this.kdSupp = kdSupp;
        initComponents();
        setLocationRelativeTo(null);
        setupUI();
        tampilkanInfoHeader();
        loadBarang();
    }

    // Kustomisasi tampilan modern
    private void setupUI() {
        panelHeader.setBackground(new Color(30, 41, 59));
        lblJudul.setForeground(Color.WHITE);
        lblJudul.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblSub.setForeground(new Color(148, 163, 184));
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panelContent.setBackground(new Color(248, 250, 252));
        panelInfo.setBackground(new Color(30, 41, 59).brighter());
        styleButton(btnTambah, new Color(59, 130, 246));
        styleButton(btnHapus, new Color(239, 68, 68));
        styleButton(btnSimpan, new Color(16, 185, 129));
        styleButton(btnBatal, new Color(107, 114, 128));
        updateTotalLabel();
    }

    // Tampilkan info header transaksi di panel atas
    private void tampilkanInfoHeader() {
        lblInfoNoPsn.setText("No. Pesanan: " + noPsn);
        lblInfoTgl.setText("Tanggal: " + tgl);
        lblInfoSupp.setText("Supplier: " + kdSupp);
    }

    // Load daftar barang ke ComboBox
    private void loadBarang() {
        cmbNamaBrg.removeAllItems();
        ResultSet rs = Koneksi.executeQuery("SELECT kd_barang, nama_barang, harga_beli_stok FROM tb_barang ORDER BY kd_barang");
        try {
            if (rs != null) {
                while (rs.next()) {
                    cmbNamaBrg.addItem(rs.getString("kd_barang") + " - " + rs.getString("nama_barang"));
                }
                rs.close();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat barang: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        // Trigger update harga untuk item pertama
        if (cmbNamaBrg.getItemCount() > 0) {
            updateHargaBarang();
        }
    }

    // Event: saat barang dipilih di ComboBox, fetch harga secara otomatis
    private void updateHargaBarang() {
        if (cmbNamaBrg.getSelectedItem() == null) return;
        String kdBarang = cmbNamaBrg.getSelectedItem().toString().split(" - ")[0];
        ResultSet rs = Koneksi.executeQuery("SELECT harga_beli_stok FROM tb_barang WHERE kd_barang='" + kdBarang + "'");
        try {
            if (rs != null && rs.next()) {
                txtHargaBrg.setText(String.valueOf(rs.getDouble("harga_beli_stok")));
            }
            if (rs != null) rs.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal fetch harga: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Tombol Tambah: hitung subtotal dan masukkan ke tabel
    private void tambahItem() {
        if (cmbNamaBrg.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Pilih barang terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String hargaStr = txtHargaBrg.getText().trim();
        String jumlahStr = txtJumlah.getText().trim();

        // Validasi input
        if (hargaStr.isEmpty() || jumlahStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Harga dan jumlah harus diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double harga;
        int jumlah;
        try {
            harga = Double.parseDouble(hargaStr);
            jumlah = Integer.parseInt(jumlahStr);
            if (jumlah <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Jumlah harus angka positif!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Hitung subtotal
        String selected = cmbNamaBrg.getSelectedItem().toString();
        String kdBarang = selected.split(" - ")[0];
        String namaBarang = selected.substring(selected.indexOf(" - ") + 3);
        double subtotal = harga * jumlah;

        // Tambah baris ke JTable
        tableModel.addRow(new Object[]{
            kdBarang, namaBarang, DF.format(harga), jumlah, DF.format(subtotal)
        });

        // Update total harga secara real-time
        totalHarga += subtotal;
        updateTotalLabel();

        // Bersihkan field input item
        txtJumlah.setText("");
        cmbNamaBrg.setSelectedIndex(0);
    }

    // Tombol Hapus: hapus baris yang dipilih dari JTable dan koreksi total
    private void hapusItem() {
        int row = tblBarang.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Pilih item yang ingin dihapus!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Koreksi total harga - hapus subtotal baris yang dihapus
        String subtotalStr = tableModel.getValueAt(row, 4).toString().replace(",", "").replace(".", "");
        try {
            // Parse format angka Indonesia (titik sebagai pemisah ribuan, koma sebagai desimal)
            String rawSubtotal = tableModel.getValueAt(row, 4).toString()
                    .replace(".", "").replace(",", ".");
            double subtotal = Double.parseDouble(rawSubtotal);
            totalHarga -= subtotal;
            if (totalHarga < 0) totalHarga = 0;
        } catch (Exception e) {
            totalHarga = 0;
        }

        tableModel.removeRow(row);
        updateTotalLabel();
    }

    // Update label total harga
    private void updateTotalLabel() {
        lblTotal.setText("Total: Rp " + DF.format(totalHarga));
    }

    // Tombol Simpan: insert ke tb_pembelian dan tb_pembelian_detail dengan database transaction
    private void simpanTransaksi() {
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Tambahkan minimal 1 item barang!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Konfirmasi sebelum simpan
        int konfirm = JOptionPane.showConfirmDialog(this,
                "Simpan transaksi " + noPsn + "?\nTotal: Rp " + DF.format(totalHarga),
                "Konfirmasi Simpan", JOptionPane.YES_NO_OPTION);
        if (konfirm != JOptionPane.YES_OPTION) return;

        // Dapatkan koneksi dan mulai transaction
        Connection conn = Koneksi.getKoneksi();
        if (conn == null) {
            JOptionPane.showMessageDialog(this, "Tidak dapat terhubung ke database!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Nonaktifkan auto-commit untuk mulai transaction
            conn.setAutoCommit(false);

            // INSERT ke tb_pembelian (header)
            String sqlHeader = "INSERT INTO tb_pembelian (kd_pembelian, tgl_pembelian, kd_supplier, total_bayar) VALUES (?, ?, ?, ?)";
            PreparedStatement psHeader = conn.prepareStatement(sqlHeader);
            psHeader.setString(1, noPsn);
            psHeader.setString(2, tgl);
            psHeader.setString(3, kdSupp);
            psHeader.setDouble(4, totalHarga);
            psHeader.executeUpdate();

            // INSERT detail per baris JTable ke tb_pembelian_detail
            String sqlDetail = "INSERT INTO tb_pembelian_detail (kd_pembelian, kd_barang, jumlah, subtotal) VALUES (?, ?, ?, ?)";
            PreparedStatement psDetail = conn.prepareStatement(sqlDetail);

            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String kdBarang = tableModel.getValueAt(i, 0).toString();
                int jumlah = Integer.parseInt(tableModel.getValueAt(i, 3).toString());
                // Parse subtotal dari format yang ditampilkan
                String rawSubtotal = tableModel.getValueAt(i, 4).toString()
                        .replace(".", "").replace(",", ".");
                double subtotal = Double.parseDouble(rawSubtotal);

                psDetail.setString(1, noPsn);
                psDetail.setString(2, kdBarang);
                psDetail.setInt(3, jumlah);
                psDetail.setDouble(4, subtotal);
                psDetail.executeUpdate();
            }

            // Commit semua insert jika berhasil
            conn.commit();
            conn.setAutoCommit(true);

            JOptionPane.showMessageDialog(this,
                    "Transaksi berhasil disimpan!\nNo. Pesanan: " + noPsn,
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);

            // Kembalikan ke frame master
            parentFrame.setVisible(true);
            this.dispose();

        } catch (Exception e) {
            // Rollback jika ada error agar data tidak setengah tersimpan
            try {
                conn.rollback();
                conn.setAutoCommit(true);
            } catch (Exception rollbackEx) {
                rollbackEx.printStackTrace();
            }
            JOptionPane.showMessageDialog(this,
                    "Transaksi gagal disimpan!\nError: " + e.getMessage() + "\nSeluruh data dibatalkan (rollback).",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Tombol Batal: kembalikan ke frame master tanpa menyimpan
    private void batalTransaksi() {
        int konfirm = JOptionPane.showConfirmDialog(this,
                "Batalkan transaksi ini? Data yang sudah diisi akan hilang.",
                "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (konfirm == JOptionPane.YES_OPTION) {
            parentFrame.setVisible(true);
            this.dispose();
        }
    }

    // Styling tombol modern
    private void styleButton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(110, 35));
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        panelHeader = new javax.swing.JPanel();
        lblJudul = new javax.swing.JLabel();
        lblSub = new javax.swing.JLabel();
        panelInfo = new javax.swing.JPanel();
        lblInfoNoPsn = new javax.swing.JLabel();
        lblInfoTgl = new javax.swing.JLabel();
        lblInfoSupp = new javax.swing.JLabel();
        panelContent = new javax.swing.JPanel();
        panelInput = new javax.swing.JPanel();
        panelTombolItem = new javax.swing.JPanel();
        panelTableArea = new javax.swing.JPanel();
        panelBottom = new javax.swing.JPanel();
        cmbNamaBrg = new javax.swing.JComboBox<>();
        txtHargaBrg = new javax.swing.JTextField();
        txtJumlah = new javax.swing.JTextField();
        tblBarang = new javax.swing.JTable();
        jScrollPane1 = new javax.swing.JScrollPane();
        lblTotal = new javax.swing.JLabel("Total: Rp 0,00");
        btnTambah = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        btnSimpan = new javax.swing.JButton();
        btnBatal = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override public void windowClosing(java.awt.event.WindowEvent e) { batalTransaksi(); }
        });
        setTitle("Transaksi Pembelian - Langkah 2");
        setPreferredSize(new Dimension(860, 600));

        // Panel header
        panelHeader.setLayout(new GridLayout(2, 1, 0, 2));
        panelHeader.setBorder(BorderFactory.createEmptyBorder(18, 25, 18, 25));
        lblJudul.setText("Detail Pembelian");
        lblSub.setText("Langkah 2 dari 2: Tambahkan item barang ke transaksi");
        panelHeader.add(lblJudul);
        panelHeader.add(lblSub);

        // Panel info header transaksi (summary)
        panelInfo.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10));
        panelInfo.setBackground(new Color(51, 65, 85));
        styleInfoLabel(lblInfoNoPsn);
        styleInfoLabel(lblInfoTgl);
        styleInfoLabel(lblInfoSupp);
        panelInfo.add(lblInfoNoPsn);
        JSeparator sep1 = new JSeparator(JSeparator.VERTICAL);
        sep1.setPreferredSize(new Dimension(1, 20));
        sep1.setForeground(new Color(100, 116, 139));
        panelInfo.add(sep1);
        panelInfo.add(lblInfoTgl);
        JSeparator sep2 = new JSeparator(JSeparator.VERTICAL);
        sep2.setPreferredSize(new Dimension(1, 20));
        sep2.setForeground(new Color(100, 116, 139));
        panelInfo.add(sep2);
        panelInfo.add(lblInfoSupp);

        // Panel input item barang
        panelInput.setLayout(new GridBagLayout());
        panelInput.setOpaque(false);
        panelInput.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(226, 232, 240)),
                        "Tambah Item Barang",
                        javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                        javax.swing.border.TitledBorder.DEFAULT_POSITION,
                        new Font("Segoe UI", Font.BOLD, 12),
                        new Color(51, 65, 85)
                ),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 5, 6, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Baris: Pilih Barang
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        panelInput.add(styledLabel("Barang:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.gridwidth = 3;
        cmbNamaBrg.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cmbNamaBrg.addActionListener(e -> updateHargaBarang());
        panelInput.add(cmbNamaBrg, gbc);

        // Baris: Harga dan Jumlah
        gbc.gridwidth = 1; gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panelInput.add(styledLabel("Harga (Rp):"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.5;
        styleTextField(txtHargaBrg);
        txtHargaBrg.setEditable(false);
        txtHargaBrg.setBackground(new Color(241, 245, 249));
        panelInput.add(txtHargaBrg, gbc);

        gbc.gridx = 2; gbc.weightx = 0;
        panelInput.add(styledLabel("Jumlah:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.5;
        styleTextField(txtJumlah);
        panelInput.add(txtJumlah, gbc);

        // Panel tombol tambah/hapus item
        panelTombolItem.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        panelTombolItem.setOpaque(false);
        btnTambah.setText("+ Tambah");
        btnTambah.addActionListener(e -> tambahItem());
        btnHapus.setText("- Hapus");
        btnHapus.addActionListener(e -> hapusItem());
        panelTombolItem.add(btnTambah);
        panelTombolItem.add(btnHapus);

        // Setup JTable barang yang dipilih
        tableModel = new DefaultTableModel(
                new String[]{"Kode Barang", "Nama Barang", "Harga", "Jumlah", "Subtotal"}, 0
        ) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tblBarang.setModel(tableModel);
        tblBarang.setRowHeight(28);
        tblBarang.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tblBarang.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tblBarang.setSelectionBackground(new Color(219, 234, 254));
        tblBarang.setGridColor(new Color(226, 232, 240));
        // Lebar kolom
        tblBarang.getColumnModel().getColumn(0).setPreferredWidth(90);
        tblBarang.getColumnModel().getColumn(1).setPreferredWidth(200);
        tblBarang.getColumnModel().getColumn(2).setPreferredWidth(110);
        tblBarang.getColumnModel().getColumn(3).setPreferredWidth(70);
        tblBarang.getColumnModel().getColumn(4).setPreferredWidth(120);
        jScrollPane1.setViewportView(tblBarang);

        // Panel area tabel + total
        panelTableArea = new JPanel(new BorderLayout(0, 5));
        panelTableArea.setOpaque(false);
        JLabel lblDaftarItem = new JLabel("Daftar Item Transaksi");
        lblDaftarItem.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblDaftarItem.setForeground(new Color(51, 65, 85));
        panelTableArea.add(lblDaftarItem, BorderLayout.NORTH);
        panelTableArea.add(jScrollPane1, BorderLayout.CENTER);

        // Label total harga
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTotal.setForeground(new Color(16, 185, 129));
        lblTotal.setHorizontalAlignment(SwingConstants.RIGHT);
        lblTotal.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 5));
        panelTableArea.add(lblTotal, BorderLayout.SOUTH);

        // Panel tombol simpan/batal
        panelBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panelBottom.setBackground(new Color(241, 245, 249));
        panelBottom.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(226, 232, 240)));
        btnBatal.setText("✕ Batal");
        btnBatal.addActionListener(e -> batalTransaksi());
        btnSimpan.setText("✓ Simpan");
        btnSimpan.addActionListener(e -> simpanTransaksi());
        panelBottom.add(btnBatal);
        panelBottom.add(btnSimpan);

        // Susun panel konten utama
        panelContent.setLayout(new BorderLayout(0, 10));
        panelContent.setBorder(BorderFactory.createEmptyBorder(15, 20, 5, 20));

        JPanel panelInputArea = new JPanel(new BorderLayout(0, 5));
        panelInputArea.setOpaque(false);
        panelInputArea.add(panelInput, BorderLayout.CENTER);
        panelInputArea.add(panelTombolItem, BorderLayout.SOUTH);
        panelInputArea.setPreferredSize(new Dimension(0, 145));

        panelContent.add(panelInputArea, BorderLayout.NORTH);
        panelContent.add(panelTableArea, BorderLayout.CENTER);

        // Susun layout utama
        JPanel panelTop = new JPanel(new BorderLayout());
        panelTop.add(panelHeader, BorderLayout.NORTH);
        panelTop.add(panelInfo, BorderLayout.SOUTH);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panelTop, BorderLayout.NORTH);
        getContentPane().add(panelContent, BorderLayout.CENTER);
        getContentPane().add(panelBottom, BorderLayout.SOUTH);

        pack();
    }

    // Helper label info header
    private void styleInfoLabel(JLabel lbl) {
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    }

    // Helper membuat label form
    private JLabel styledLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(new Color(71, 85, 105));
        return lbl;
    }

    // Helper styling text field
    private void styleTextField(JTextField tf) {
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tf.setPreferredSize(new Dimension(180, 32));
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225)),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
    }

    // Komponen GUI
    private javax.swing.JPanel panelHeader, panelInfo, panelContent, panelInput;
    private javax.swing.JPanel panelTombolItem, panelTableArea, panelBottom;
    private javax.swing.JLabel lblJudul, lblSub;
    private javax.swing.JLabel lblInfoNoPsn, lblInfoTgl, lblInfoSupp;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JComboBox<String> cmbNamaBrg;
    private javax.swing.JTextField txtHargaBrg, txtJumlah;
    private javax.swing.JTable tblBarang;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton btnTambah, btnHapus, btnSimpan, btnBatal;
}
