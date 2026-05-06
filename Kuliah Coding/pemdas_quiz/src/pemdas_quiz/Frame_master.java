package pemdas_quiz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Frame utama aplikasi kasir pembelian dengan menu navigasi.
 */
public class Frame_master extends javax.swing.JFrame {

    // Konstruktor utama
    public Frame_master() {
        initComponents();
        setLocationRelativeTo(null);
        setTitle("Aplikasi Kasir Pembelian - Master");
        setupUI();
    }

    // Kustomisasi tampilan modern
    private void setupUI() {
        // Warna dan font untuk panel header
        panelHeader.setBackground(new Color(30, 41, 59));
        lblJudul.setForeground(Color.WHITE);
        lblJudul.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblSubJudul.setForeground(new Color(148, 163, 184));
        lblSubJudul.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panelBody.setBackground(new Color(248, 250, 252));
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        panelHeader = new javax.swing.JPanel();
        lblJudul = new javax.swing.JLabel();
        lblSubJudul = new javax.swing.JLabel();
        lblLogo = new javax.swing.JLabel();
        panelBody = new javax.swing.JPanel();
        panelCards = new javax.swing.JPanel();
        btnCardSupplier = createMenuCard("Supplier", "Kelola data supplier", new Color(59, 130, 246));
        btnCardPelanggan = createMenuCard("Pelanggan", "Kelola data pelanggan", new Color(16, 185, 129));
        btnCardBarang = createMenuCard("Barang", "Kelola data barang", new Color(245, 158, 11));
        btnCardPembelian = createMenuCard("Pembelian", "Input transaksi pembelian", new Color(239, 68, 68));
        menuBar = new javax.swing.JMenuBar();
        menuPembelian = new javax.swing.JMenu();
        menuSupplier = new javax.swing.JMenu();
        menuPelanggan = new javax.swing.JMenu();
        menuBarang = new javax.swing.JMenu();

        // Konfigurasi JFrame
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(900, 580));

        // Panel header
        panelHeader.setLayout(new java.awt.BorderLayout(10, 0));
        panelHeader.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 30, 20, 30));

        lblLogo.setText("🛒");
        lblLogo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
        lblLogo.setForeground(Color.WHITE);
        panelHeader.add(lblLogo, java.awt.BorderLayout.WEST);

        JPanel panelTeks = new JPanel(new GridLayout(2, 1));
        panelTeks.setOpaque(false);
        lblJudul.setText("Aplikasi Kasir Pembelian");
        lblSubJudul.setText("Sistem Manajemen Transaksi Pembelian Barang");
        panelTeks.add(lblJudul);
        panelTeks.add(lblSubJudul);
        panelHeader.add(panelTeks, java.awt.BorderLayout.CENTER);

        // Panel body dengan kartu menu
        panelBody.setLayout(new java.awt.BorderLayout());
        panelBody.setBorder(javax.swing.BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JLabel lblWelcome = new JLabel("Pilih Menu");
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblWelcome.setForeground(new Color(51, 65, 85));
        lblWelcome.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        panelBody.add(lblWelcome, java.awt.BorderLayout.NORTH);

        panelCards.setLayout(new java.awt.GridLayout(2, 2, 20, 20));
        panelCards.setOpaque(false);
        panelCards.add(btnCardSupplier);
        panelCards.add(btnCardPelanggan);
        panelCards.add(btnCardBarang);
        panelCards.add(btnCardPembelian);
        panelBody.add(panelCards, java.awt.BorderLayout.CENTER);

        // Susun layout utama
        getContentPane().setLayout(new java.awt.BorderLayout());
        getContentPane().add(panelHeader, java.awt.BorderLayout.NORTH);
        getContentPane().add(panelBody, java.awt.BorderLayout.CENTER);

        // Setup menu bar
        menuBar.setBackground(new Color(30, 41, 59));
        menuBar.setBorderPainted(false);

        styleMenu(menuPembelian, "Pembelian");
        menuPembelian.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { bukaFrame("pembelian"); }
        });
        menuPembelian.addActionListener(e -> bukaFrame("pembelian"));

        styleMenu(menuSupplier, "Supplier");
        menuSupplier.addActionListener(e -> bukaFrame("supplier"));

        styleMenu(menuPelanggan, "Pelanggan");
        menuPelanggan.addActionListener(e -> bukaFrame("pelanggan"));

        styleMenu(menuBarang, "Barang");
        menuBarang.addActionListener(e -> bukaFrame("barang"));

        // Tambahkan item ke tiap menu agar bisa di-click
        JMenuItem itemPembelian = new JMenuItem("Buka Form Pembelian");
        itemPembelian.addActionListener(e -> bukaFrame("pembelian"));
        menuPembelian.add(itemPembelian);

        JMenuItem itemSupplier = new JMenuItem("Buka Form Supplier");
        itemSupplier.addActionListener(e -> bukaFrame("supplier"));
        menuSupplier.add(itemSupplier);

        JMenuItem itemPelanggan = new JMenuItem("Buka Form Pelanggan");
        itemPelanggan.addActionListener(e -> bukaFrame("pelanggan"));
        menuPelanggan.add(itemPelanggan);

        JMenuItem itemBarang = new JMenuItem("Buka Form Barang");
        itemBarang.addActionListener(e -> bukaFrame("barang"));
        menuBarang.add(itemBarang);

        menuBar.add(menuPembelian);
        menuBar.add(menuSupplier);
        menuBar.add(menuPelanggan);
        menuBar.add(menuBarang);
        setJMenuBar(menuBar);

        pack();
    }

    // Helper membuat kartu menu modern
    private JPanel createMenuCard(String title, String subtitle, Color color) {
        JPanel card = new JPanel(new BorderLayout(0, 8));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1, true),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JPanel accent = new JPanel();
        accent.setBackground(color);
        accent.setPreferredSize(new Dimension(0, 4));
        card.add(accent, BorderLayout.NORTH);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(new Color(30, 41, 59));

        JLabel lblSub = new JLabel(subtitle);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(new Color(100, 116, 139));

        JPanel txtPanel = new JPanel(new GridLayout(2, 1, 0, 4));
        txtPanel.setOpaque(false);
        txtPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        txtPanel.add(lblTitle);
        txtPanel.add(lblSub);
        card.add(txtPanel, BorderLayout.CENTER);

        // Efek hover
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(248, 250, 252));
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(color, 2, true),
                        BorderFactory.createEmptyBorder(20, 20, 20, 20)
                ));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(Color.WHITE);
                card.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(226, 232, 240), 1, true),
                        BorderFactory.createEmptyBorder(20, 20, 20, 20)
                ));
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                bukaFrameFromCard(title);
            }
        });

        return card;
    }

    // Membuka frame berdasarkan nama kartu
    private void bukaFrameFromCard(String title) {
        switch (title) {
            case "Supplier" -> bukaFrame("supplier");
            case "Pelanggan" -> bukaFrame("pelanggan");
            case "Barang" -> bukaFrame("barang");
            case "Pembelian" -> bukaFrame("pembelian");
        }
    }

    // Membuka frame yang sesuai
    private void bukaFrame(String namaFrame) {
        switch (namaFrame) {
            case "pembelian" -> {
                Frame_Pembelian fp = new Frame_Pembelian(this);
                fp.setVisible(true);
                this.setVisible(false);
            }
            case "supplier" -> {
                Frame_Supplier fs = new Frame_Supplier();
                fs.setVisible(true);
            }
            case "pelanggan" -> {
                Frame_Pelanggan fpl = new Frame_Pelanggan();
                fpl.setVisible(true);
            }
            case "barang" -> {
                Frame_Barang fb = new Frame_Barang();
                fb.setVisible(true);
            }
        }
    }

    // Styling untuk menu bar
    private void styleMenu(JMenu menu, String text) {
        menu.setText(text);
        menu.setForeground(Color.WHITE);
        menu.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        menu.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
    }

    // Entry point aplikasi
    public static void main(String args[]) {
        try {
            // Gunakan FlatLaf jika tersedia, fallback ke Nimbus
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        java.awt.EventQueue.invokeLater(() -> new Frame_master().setVisible(true));
    }

    // Komponen GUI
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelBody;
    private javax.swing.JPanel panelCards;
    private javax.swing.JLabel lblJudul;
    private javax.swing.JLabel lblSubJudul;
    private javax.swing.JLabel lblLogo;
    private javax.swing.JPanel btnCardSupplier;
    private javax.swing.JPanel btnCardPelanggan;
    private javax.swing.JPanel btnCardBarang;
    private javax.swing.JPanel btnCardPembelian;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenu menuPembelian;
    private javax.swing.JMenu menuSupplier;
    private javax.swing.JMenu menuPelanggan;
    private javax.swing.JMenu menuBarang;
}
