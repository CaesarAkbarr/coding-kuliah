package pemdas_quiz;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;

/**
 * Form CRUD untuk data Supplier.
 */
public class Frame_Supplier extends javax.swing.JFrame {

    private DefaultTableModel tableModel;

    // Konstruktor - otomatis generate kode dan load data
    public Frame_Supplier() {
        initComponents();
        setLocationRelativeTo(null);
        setupUI();
        generateKode();
        loadDataTable();
    }

    // Kustomisasi tampilan modern
    private void setupUI() {
        panelHeader.setBackground(new Color(30, 41, 59));
        lblJudul.setForeground(Color.WHITE);
        lblJudul.setFont(new Font("Segoe UI", Font.BOLD, 18));
        panelForm.setBackground(new Color(248, 250, 252));
        panelTombol.setOpaque(false);
        styleButton(btnSimpan, new Color(59, 130, 246));
        styleButton(btnBatal, new Color(107, 114, 128));
        styleButton(btnHapus, new Color(239, 68, 68));
        styleButton(btnTutup, new Color(51, 65, 85));
    }

    // Auto-generate kode supplier berikutnya
    private void generateKode() {
        String kode = Koneksi.generateNextId("tb_supplier", "kd_supplier", "S", 5);
        txtKodeSupp.setText(kode);
        txtKodeSupp.setEditable(false);
        txtKodeSupp.setBackground(new Color(241, 245, 249));
    }

    // Load semua data supplier ke JTable
    private void loadDataTable() {
        tableModel.setRowCount(0);
        ResultSet rs = Koneksi.executeQuery("SELECT * FROM tb_supplier ORDER BY kd_supplier");
        try {
            if (rs != null) {
                while (rs.next()) {
                    tableModel.addRow(new Object[]{
                        rs.getString("kd_supplier"),
                        rs.getString("nama_supplier"),
                        rs.getString("alamat"),
                        rs.getString("no_telp")
                    });
                }
                rs.close();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Tombol Simpan - validasi dan insert ke database
    private void simpanData() {
        String kode = txtKodeSupp.getText().trim();
        String nama = txtNamaSupp.getText().trim();
        String alamat = txtAlamat.getText().trim();
        String telp = txtTelp.getText().trim();

        // Validasi field tidak boleh kosong
        if (nama.isEmpty() || alamat.isEmpty() || telp.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field wajib diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String query = String.format(
                "INSERT INTO tb_supplier (kd_supplier, nama_supplier, alamat, no_telp) VALUES ('%s','%s','%s','%s')",
                kode, nama, alamat, telp
        );

        if (Koneksi.executeUpdate(query)) {
            JOptionPane.showMessageDialog(this, "Data supplier berhasil disimpan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            bersihkanForm();
            generateKode();
            loadDataTable();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan data!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Tombol Hapus - hapus data yang dipilih di tabel
    private void hapusData() {
        int row = tblSupplier.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Pilih data yang ingin dihapus!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String kode = tableModel.getValueAt(row, 0).toString();
        int konfirm = JOptionPane.showConfirmDialog(this,
                "Yakin ingin menghapus supplier " + kode + "?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (konfirm == JOptionPane.YES_OPTION) {
            if (Koneksi.executeUpdate("DELETE FROM tb_supplier WHERE kd_supplier='" + kode + "'")) {
                JOptionPane.showMessageDialog(this, "Data berhasil dihapus!");
                generateKode();
                loadDataTable();
            }
        }
    }

    // Bersihkan semua field form
    private void bersihkanForm() {
        txtNamaSupp.setText("");
        txtAlamat.setText("");
        txtTelp.setText("");
        txtNamaSupp.requestFocus();
    }

    // Styling tombol modern
    private void styleButton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(100, 35));
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        panelHeader = new javax.swing.JPanel();
        lblJudul = new javax.swing.JLabel();
        panelForm = new javax.swing.JPanel();
        panelTombol = new javax.swing.JPanel();
        btnSimpan = new javax.swing.JButton();
        btnBatal = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        btnTutup = new javax.swing.JButton();
        tblSupplier = new javax.swing.JTable();
        jScrollPane1 = new javax.swing.JScrollPane();

        // Field-field form
        txtKodeSupp = new javax.swing.JTextField();
        txtNamaSupp = new javax.swing.JTextField();
        txtAlamat = new javax.swing.JTextField();
        txtTelp = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Data Supplier");
        setPreferredSize(new Dimension(800, 560));

        // Panel header
        panelHeader.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 15));
        lblJudul.setText("📦  Data Supplier");
        panelHeader.add(lblJudul);

        // Setup tabel
        tableModel = new DefaultTableModel(
                new String[]{"Kode", "Nama Supplier", "Alamat", "No. Telp"}, 0
        ) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tblSupplier.setModel(tableModel);
        tblSupplier.setRowHeight(28);
        tblSupplier.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tblSupplier.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tblSupplier.setSelectionBackground(new Color(219, 234, 254));
        tblSupplier.setGridColor(new Color(226, 232, 240));
        jScrollPane1.setViewportView(tblSupplier);

        // Panel form dengan GridBagLayout
        panelForm.setLayout(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(226, 232, 240)),
                        "Formulir Supplier",
                        javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                        javax.swing.border.TitledBorder.DEFAULT_POSITION,
                        new Font("Segoe UI", Font.BOLD, 12),
                        new Color(51, 65, 85)
                ),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 5, 6, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addFormRow(panelForm, gbc, 0, "Kode Supplier:", txtKodeSupp);
        addFormRow(panelForm, gbc, 1, "Nama Supplier:", txtNamaSupp);
        addFormRow(panelForm, gbc, 2, "Alamat:", txtAlamat);
        addFormRow(panelForm, gbc, 3, "No. Telepon:", txtTelp);

        // Panel tombol
        panelTombol.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        btnSimpan.setText("Simpan");
        btnSimpan.addActionListener(e -> simpanData());
        btnBatal.setText("Batal");
        btnBatal.addActionListener(e -> bersihkanForm());
        btnHapus.setText("Hapus");
        btnHapus.addActionListener(e -> hapusData());
        btnTutup.setText("Tutup");
        btnTutup.addActionListener(e -> dispose());
        panelTombol.add(btnSimpan);
        panelTombol.add(btnBatal);
        panelTombol.add(btnHapus);
        panelTombol.add(btnTutup);

        // Susun layout utama
        JPanel panelKiri = new JPanel(new BorderLayout(0, 10));
        panelKiri.setOpaque(false);
        panelKiri.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 5));
        panelKiri.add(panelForm, BorderLayout.CENTER);
        panelKiri.add(panelTombol, BorderLayout.SOUTH);
        panelKiri.setPreferredSize(new Dimension(320, 0));

        JPanel panelKanan = new JPanel(new BorderLayout());
        panelKanan.setOpaque(false);
        panelKanan.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 10));
        JLabel lblList = new JLabel("Daftar Supplier");
        lblList.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblList.setForeground(new Color(51, 65, 85));
        lblList.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        panelKanan.add(lblList, BorderLayout.NORTH);
        panelKanan.add(jScrollPane1, BorderLayout.CENTER);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panelHeader, BorderLayout.NORTH);
        getContentPane().add(panelKiri, BorderLayout.WEST);
        getContentPane().add(panelKanan, BorderLayout.CENTER);

        pack();
    }

    // Helper menambahkan baris label + field ke form
    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, String labelText, JComponent field) {
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0.3;
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(new Color(71, 85, 105));
        panel.add(lbl, gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        if (field instanceof JTextField tf) {
            tf.setPreferredSize(new Dimension(180, 32));
            tf.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            tf.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(203, 213, 225)),
                    BorderFactory.createEmptyBorder(4, 8, 4, 8)
            ));
        }
        panel.add(field, gbc);
    }

    // Komponen GUI
    private javax.swing.JPanel panelHeader, panelForm, panelTombol;
    private javax.swing.JLabel lblJudul;
    private javax.swing.JTextField txtKodeSupp, txtNamaSupp, txtAlamat, txtTelp;
    private javax.swing.JButton btnSimpan, btnBatal, btnHapus, btnTutup;
    private javax.swing.JTable tblSupplier;
    private javax.swing.JScrollPane jScrollPane1;
}
