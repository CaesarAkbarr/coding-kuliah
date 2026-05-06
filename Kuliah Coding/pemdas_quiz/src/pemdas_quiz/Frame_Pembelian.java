package pemdas_quiz;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.time.LocalDate;

/**
 * Frame transaksi pembelian - Langkah 1 (Header Pembelian).
 * Menerima referensi frame_master agar bisa dikembalikan saat ditutup.
 */
public class Frame_Pembelian extends javax.swing.JFrame {

    private final Frame_master parentFrame;

    // Konstruktor menerima referensi ke Frame_master
    public Frame_Pembelian(Frame_master parent) {
        this.parentFrame = parent;
        initComponents();
        setLocationRelativeTo(null);
        setupUI();
        generateNoPesanan();
        setTanggalHariIni();
        loadSupplier();
    }

    // Kustomisasi tampilan modern
    private void setupUI() {
        panelHeader.setBackground(new Color(30, 41, 59));
        lblJudul.setForeground(Color.WHITE);
        lblJudul.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblSub.setForeground(new Color(148, 163, 184));
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panelContent.setBackground(new Color(248, 250, 252));
        styleButton(btnNext, new Color(59, 130, 246));
        styleButton(btnKembali, new Color(107, 114, 128));
    }

    // Generate nomor pesanan otomatis dari helper Koneksi
    private void generateNoPesanan() {
        String noPsn = Koneksi.generateNoPesanan();
        txtNoPsn.setText(noPsn);
        txtNoPsn.setEditable(false);
        txtNoPsn.setBackground(new Color(241, 245, 249));
    }

    // Isi tanggal dengan tanggal hari ini
    private void setTanggalHariIni() {
        txtTglPsn.setText(LocalDate.now().toString());
        txtTglPsn.setEditable(false);
        txtTglPsn.setBackground(new Color(241, 245, 249));
    }

    // Load daftar supplier ke ComboBox
    private void loadSupplier() {
        cmbSupp.removeAllItems();
        ResultSet rs = Koneksi.executeQuery("SELECT kd_supplier, nama_supplier FROM tb_supplier ORDER BY kd_supplier");
        try {
            if (rs != null) {
                while (rs.next()) {
                    cmbSupp.addItem(rs.getString("kd_supplier") + " - " + rs.getString("nama_supplier"));
                }
                rs.close();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat supplier: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        if (cmbSupp.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this,
                    "Tidak ada data supplier. Silakan tambahkan supplier terlebih dahulu.",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Tombol Next - kirim data ke Frame_PembelianDetail
    private void prosesNext() {
        if (cmbSupp.getItemCount() == 0 || cmbSupp.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Pilih supplier terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String noPsn = txtNoPsn.getText();
        String tgl = txtTglPsn.getText();
        // Ambil kode supplier saja (sebelum " - ")
        String selectedSupp = cmbSupp.getSelectedItem().toString();
        String kdSupp = selectedSupp.split(" - ")[0];

        // Buka Frame_PembelianDetail dengan data yang dikirim
        Frame_PembelianDetail fpd = new Frame_PembelianDetail(parentFrame, noPsn, tgl, kdSupp);
        fpd.setVisible(true);
        this.dispose();
    }

    // Tombol Kembali - kembalikan ke frame master
    private void kembali() {
        parentFrame.setVisible(true);
        this.dispose();
    }

    // Styling tombol modern
    private void styleButton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(120, 38));
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        panelHeader = new javax.swing.JPanel();
        lblJudul = new javax.swing.JLabel();
        lblSub = new javax.swing.JLabel();
        panelContent = new javax.swing.JPanel();
        panelForm = new javax.swing.JPanel();
        panelTombol = new javax.swing.JPanel();
        txtNoPsn = new javax.swing.JTextField();
        txtTglPsn = new javax.swing.JTextField();
        cmbSupp = new javax.swing.JComboBox<>();
        btnNext = new javax.swing.JButton();
        btnKembali = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override public void windowClosing(java.awt.event.WindowEvent e) { kembali(); }
        });
        setTitle("Transaksi Pembelian - Langkah 1");
        setPreferredSize(new Dimension(520, 420));
        setResizable(false);

        // Panel header dengan dua baris teks
        panelHeader.setLayout(new java.awt.GridLayout(2, 1, 0, 2));
        panelHeader.setBorder(BorderFactory.createEmptyBorder(18, 25, 18, 25));
        lblJudul.setText("Form Pembelian");
        lblSub.setText("Langkah 1 dari 2: Isi data header transaksi");
        panelHeader.add(lblJudul);
        panelHeader.add(lblSub);

        // Panel konten utama
        panelContent.setLayout(new BorderLayout(0, 20));
        panelContent.setBorder(BorderFactory.createEmptyBorder(25, 40, 20, 40));

        // Panel form dengan GridBagLayout
        panelForm.setLayout(new GridBagLayout());
        panelForm.setOpaque(false);
        panelForm.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(226, 232, 240)),
                        "Data Transaksi",
                        javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                        javax.swing.border.TitledBorder.DEFAULT_POSITION,
                        new Font("Segoe UI", Font.BOLD, 12),
                        new Color(51, 65, 85)
                ),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Baris No. Pesanan
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.4;
        JLabel lblNoPsn = styledLabel("No. Pesanan:");
        panelForm.add(lblNoPsn, gbc);
        gbc.gridx = 1; gbc.weightx = 0.6;
        styleTextField(txtNoPsn);
        panelForm.add(txtNoPsn, gbc);

        // Baris Tanggal
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.4;
        panelForm.add(styledLabel("Tanggal:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.6;
        styleTextField(txtTglPsn);
        panelForm.add(txtTglPsn, gbc);

        // Baris Supplier
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.4;
        panelForm.add(styledLabel("Supplier:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.6;
        cmbSupp.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cmbSupp.setPreferredSize(new Dimension(250, 32));
        panelForm.add(cmbSupp, gbc);

        // Panel tombol
        panelTombol.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelTombol.setOpaque(false);
        btnKembali.setText("← Kembali");
        btnKembali.addActionListener(e -> kembali());
        btnNext.setText("Lanjut →");
        btnNext.addActionListener(e -> prosesNext());
        panelTombol.add(btnKembali);
        panelTombol.add(btnNext);

        panelContent.add(panelForm, BorderLayout.CENTER);
        panelContent.add(panelTombol, BorderLayout.SOUTH);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panelHeader, BorderLayout.NORTH);
        getContentPane().add(panelContent, BorderLayout.CENTER);

        pack();
    }

    // Helper membuat label dengan style konsisten
    private JLabel styledLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(new Color(71, 85, 105));
        return lbl;
    }

    // Helper styling text field
    private void styleTextField(JTextField tf) {
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tf.setPreferredSize(new Dimension(250, 32));
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225)),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
    }

    // Komponen GUI
    private javax.swing.JPanel panelHeader, panelContent, panelForm, panelTombol;
    private javax.swing.JLabel lblJudul, lblSub;
    private javax.swing.JTextField txtNoPsn, txtTglPsn;
    private javax.swing.JComboBox<String> cmbSupp;
    private javax.swing.JButton btnNext, btnKembali;
}
