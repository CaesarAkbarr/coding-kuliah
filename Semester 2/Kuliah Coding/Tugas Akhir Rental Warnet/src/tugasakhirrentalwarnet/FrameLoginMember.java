/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package tugasakhirrentalwarnet;

import java.sql.*;
import javax.swing.JOptionPane;

/**
 *
 * @author ROG G513RM
 */
public class FrameLoginMember extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger =
        java.util.logging.Logger.getLogger(FrameLoginMember.class.getName());

    /**
     * Creates new form FrameLoginMember
     */
    public FrameLoginMember() {
        initComponents();

        this.setLocationRelativeTo(null);

        loadDataCustomer();
        initTablePopup();
    }

    // Variabel penampung ID komputer yang dikirim dari MainFrame
    private String idPCKirim;

    // Konstruktor kustom untuk menerima pengiriman data ID komputer
    public FrameLoginMember(String idPC) {
        initComponents();
        // Simpan ID komputer yang diterima untuk nanti dikirim ke FramePembayaran
        this.idPCKirim = idPC;

        setLocationRelativeTo(null);

        loadDataCustomer();
        initTablePopup();
    }

    public void loadDataCustomer() {
        javax.swing.table.DefaultTableModel model =
            new javax.swing.table.DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Nama Member");
        model.addColumn("No HP");

        try {
            java.sql.Connection conn = Koneksi.getKoneksi();
            java.sql.Statement stmt = conn.createStatement();
            java.sql.ResultSet rs = stmt.executeQuery(
                "SELECT * FROM customer WHERE phone != 'GUEST' AND status = 'ACTIVE'"
            );

            while (rs.next()) {
                model.addRow(new Object[] {
                    rs.getInt("customer_id"),
                    rs.getString("cust_name"),
                    rs.getString("phone"),
                });
            }
            tabelCustomer.setModel(model);
        } catch (Exception e) {
            System.err.println("Gagal load tabel member: " + e.getMessage());
        }
    }

    // --- Metode pemicu menu popup klik kanan untuk mengelola data member (Create-Read-Update-Delete) ---
    private void initTablePopup() {
        javax.swing.JPopupMenu memberPopup = new javax.swing.JPopupMenu();
        javax.swing.JMenuItem menuEditData = new javax.swing.JMenuItem(
            "Ubah Data Member"
        );
        javax.swing.JMenuItem menuHapusMember = new javax.swing.JMenuItem(
            "Hapus Member dari DB"
        );

        // Opsi perbaruan lanjutan: Mengubah Nama DAN Nomor HP Sekaligus dalam satu dialog (Opsi A)
        menuEditData.addActionListener(evt -> {
            int row = tabelCustomer.getSelectedRow();
            if (row == -1) return;

            // Ambil data lama langsung dari baris tabel JTable yang dipilih
            String idCust = tabelCustomer.getValueAt(row, 0).toString();
            String namaLama = tabelCustomer.getValueAt(row, 1).toString();
            String phoneLama = tabelCustomer.getValueAt(row, 2).toString();

            // Buat komponen input field kustom secara dinamis tanpa emoji
            javax.swing.JTextField txtNama = new javax.swing.JTextField(
                namaLama
            );
            javax.swing.JTextField txtPhone = new javax.swing.JTextField(
                phoneLama
            );

            // Atur tata letak berjejer rapi atas bawah menggunakan GridLayout 2x2
            javax.swing.JPanel panelInput = new javax.swing.JPanel(
                new java.awt.GridLayout(2, 2, 5, 5)
            );
            panelInput.add(new javax.swing.JLabel("Nama Baru:"));
            panelInput.add(txtNama);
            panelInput.add(new javax.swing.JLabel("No HP Baru:"));
            panelInput.add(txtPhone);

            // Tampilkan kotak dialog hybrid di tengah layar
            int result = javax.swing.JOptionPane.showConfirmDialog(
                this,
                panelInput,
                "Form Ubah Data Member ID " + idCust,
                javax.swing.JOptionPane.OK_CANCEL_OPTION,
                javax.swing.JOptionPane.PLAIN_MESSAGE
            );

            // Jika pengguna menekan tombol OK, lakukan validasi dan perbarui database
            if (result == javax.swing.JOptionPane.OK_OPTION) {
                String namaBaru = txtNama.getText().trim();
                String phoneBaru = txtPhone.getText().trim();

                // Validasi input kosong agar database tidak terkontaminasi data kosong
                if (namaBaru.isEmpty() || phoneBaru.isEmpty()) {
                    javax.swing.JOptionPane.showMessageDialog(
                        this,
                        "Nama dan No HP tidak boleh kosong!"
                    );
                    return;
                }

                try {
                    Connection conn = Koneksi.getKoneksi();
                    String sqlUpdate =
                        "UPDATE customer SET cust_name = ?, phone = ? WHERE customer_id = ?";
                    PreparedStatement ps = conn.prepareStatement(sqlUpdate);
                    ps.setString(1, namaBaru);
                    ps.setString(2, phoneBaru);
                    ps.setString(3, idCust);
                    ps.executeUpdate();

                    javax.swing.JOptionPane.showMessageDialog(
                        this,
                        "Data member berhasil diperbarui."
                    );
                    // Perbarui tampilan tabel member secara otomatis
                    loadDataCustomer();
                } catch (Exception e) {
                    javax.swing.JOptionPane.showMessageDialog(
                        this,
                        "Gagal merubah data: " + e.getMessage()
                    );
                }
            }
        });

        // Opsi penghapusan: Soft Delete dengan mengubah status menjadi DELETED
        menuHapusMember.addActionListener(evt -> {
            int row = tabelCustomer.getSelectedRow();
            if (row == -1) return;
            String idCust = tabelCustomer.getValueAt(row, 0).toString();
            String name = tabelCustomer.getValueAt(row, 1).toString();

            int konfirm = javax.swing.JOptionPane.showConfirmDialog(
                this,
                "Yakin ingin menghapus member '" + name + "'?",
                "Hapus Data",
                javax.swing.JOptionPane.YES_NO_OPTION
            );
            if (konfirm != javax.swing.JOptionPane.YES_OPTION) return;

            try {
                Connection conn = Koneksi.getKoneksi();
                PreparedStatement ps = conn.prepareStatement(
                    "UPDATE customer SET status = 'DELETED' WHERE customer_id = ?"
                );
                ps.setString(1, idCust);
                ps.executeUpdate();

                javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Member '" + name + "' berhasil dihapus dari sistem."
                );
                loadDataCustomer();
            } catch (Exception e) {
                javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Gagal menghapus data: " + e.getMessage()
                );
            }
        });

        memberPopup.add(menuEditData); // Tambahkan menu ubah data gabungan
        memberPopup.add(menuHapusMember);

        // Listener klik kanan mouse pada JTable
        tabelCustomer.addMouseListener(
            new java.awt.event.MouseAdapter() {
                @Override
                public void mousePressed(java.awt.event.MouseEvent e) {
                    handlePopup(e);
                }

                @Override
                public void mouseReleased(java.awt.event.MouseEvent e) {
                    handlePopup(e);
                }

                private void handlePopup(java.awt.event.MouseEvent e) {
                    if (e.isPopupTrigger()) {
                        int row = tabelCustomer.rowAtPoint(e.getPoint());
                        if (row >= 0 && row < tabelCustomer.getRowCount()) {
                            tabelCustomer.setRowSelectionInterval(row, row);
                            memberPopup.show(
                                e.getComponent(),
                                e.getX(),
                                e.getY()
                            );
                        }
                    }
                }
            }
        );
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelCustomer = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        btnLogin = new javax.swing.JButton();
        btnDaftar = new javax.swing.JButton();
        btnGuest = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtPhone = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(700, 550));
        setMinimumSize(new java.awt.Dimension(700, 550));
        setPreferredSize(new java.awt.Dimension(700, 550));
        setResizable(false);
        setType(java.awt.Window.Type.POPUP);

        jPanel1.setPreferredSize(new java.awt.Dimension(700, 550));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jLabel1.setFont(new java.awt.Font("SansSerif", 1, 22)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("=== STARK-COMP REGISTRASI PELANGGAN PC ===");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(6, 6, 0, 6);
        jPanel1.add(jLabel1, gridBagConstraints);

        jScrollPane1.setMaximumSize(new java.awt.Dimension(500, 350));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(400, 350));

        tabelCustomer.setModel(
            new javax.swing.table.DefaultTableModel(
                new Object[][] {
                    { null, null, null },
                    { null, null, null },
                    { null, null, null },
                    { null, null, null },
                },
                new String[] { "Title 1", "Title 2", "Title 3" }
            ) {
                boolean[] canEdit = new boolean[] { false, false, false };

                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return canEdit[columnIndex];
                }
            }
        );
        tabelCustomer.setMaximumSize(new java.awt.Dimension(700, 550));
        tabelCustomer.setMinimumSize(new java.awt.Dimension(0, 0));
        tabelCustomer.setPreferredSize(new java.awt.Dimension(400, 550));
        tabelCustomer.addMouseListener(
            new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    tabelCustomerMouseClicked(evt);
                }
            }
        );
        jScrollPane1.setViewportView(tabelCustomer);
        if (tabelCustomer.getColumnModel().getColumnCount() > 0) {
            tabelCustomer.getColumnModel().getColumn(0).setResizable(false);
            tabelCustomer.getColumnModel().getColumn(1).setResizable(false);
            tabelCustomer.getColumnModel().getColumn(2).setResizable(false);
        }

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 395;
        gridBagConstraints.ipady = 97;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(18, 6, 0, 6);
        jPanel1.add(jScrollPane1, gridBagConstraints);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        btnLogin.setBackground(new java.awt.Color(34, 139, 34));
        btnLogin.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        btnLogin.setForeground(new java.awt.Color(255, 255, 255));
        btnLogin.setText("Login");
        btnLogin.addActionListener(
            new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnLoginActionPerformed(evt);
                }
            }
        );
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(25, 5, 8, 5);
        jPanel2.add(btnLogin, gridBagConstraints);

        btnDaftar.setBackground(new java.awt.Color(218, 165, 32));
        btnDaftar.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        btnDaftar.setForeground(new java.awt.Color(255, 255, 255));
        btnDaftar.setText("Daftar");
        btnDaftar.addActionListener(
            new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnDaftarActionPerformed(evt);
                }
            }
        );
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.insets = new java.awt.Insets(25, 5, 8, 5);
        jPanel2.add(btnDaftar, gridBagConstraints);

        btnGuest.setBackground(new java.awt.Color(105, 105, 105));
        btnGuest.setFont(new java.awt.Font("SansSerif", 1, 12)); // NOI18N
        btnGuest.setForeground(new java.awt.Color(255, 255, 255));
        btnGuest.setText("Guest");
        btnGuest.addActionListener(
            new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    btnGuestActionPerformed(evt);
                }
            }
        );
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(25, 5, 8, 5);
        jPanel2.add(btnGuest, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 6, 0);
        jPanel1.add(jPanel2, gridBagConstraints);

        jPanel3.setLayout(new java.awt.GridBagLayout());

        jLabel2.setText("Masukkan No. HP :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(25, 8, 0, 8);
        jPanel3.add(jLabel2, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.insets = new java.awt.Insets(25, 8, 0, 8);
        jPanel3.add(txtPhone, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        jPanel1.add(jPanel3, gridBagConstraints);

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
                        .addContainerGap()
                        .addComponent(
                            jPanel1,
                            javax.swing.GroupLayout.DEFAULT_SIZE,
                            javax.swing.GroupLayout.DEFAULT_SIZE,
                            javax.swing.GroupLayout.PREFERRED_SIZE
                        )
                        .addContainerGap()
                )
        );
        layout.setVerticalGroup(
            layout
                .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(
                    layout
                        .createSequentialGroup()
                        .addContainerGap()
                        .addComponent(
                            jPanel1,
                            javax.swing.GroupLayout.DEFAULT_SIZE,
                            javax.swing.GroupLayout.DEFAULT_SIZE,
                            javax.swing.GroupLayout.PREFERRED_SIZE
                        )
                )
        );

        pack();
    } // </editor-fold>//GEN-END:initComponents

    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {
        //GEN-FIRST:event_btnLoginActionPerformed
        // TODO add your handling code here:
        String phoneCust = txtPhone.getText().trim();
        if (phoneCust.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Masukkan No HP Member terlebih dahulu!"
            );
            return;
        }

        Connection conn = Koneksi.getKoneksi();
        try {
            String sqlCek =
                "SELECT customer_id, cust_name FROM customer WHERE phone = ?";
            PreparedStatement psCek = conn.prepareStatement(sqlCek);
            psCek.setString(1, phoneCust);
            ResultSet rsCek = psCek.executeQuery();

            if (rsCek.next()) {
                int idCust = rsCek.getInt("customer_id");
                String namaCust = rsCek.getString("cust_name");

                JOptionPane.showMessageDialog(
                    this,
                    "Member Ditemukan! Selamat datang kembali, " +
                        namaCust +
                        " 🚀"
                );

                // OPER DATA: Bawa ID PC, ID Cust, Nama Cust, dan status isGuest = false ke FramePembayaran
                new FramePembayaran(
                    idPCKirim,
                    idCust,
                    namaCust,
                    false
                ).setVisible(true);
                this.dispose(); // Tutup frame login
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "No HP belum terdaftar! Silakan daftar member baru atau masuk sebagai Guest. 😂",
                    "Not Found",
                    JOptionPane.WARNING_MESSAGE
                );
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                this,
                "Error Login: " + e.getMessage()
            );
        }
    } //GEN-LAST:event_btnLoginActionPerformed

    private void btnDaftarActionPerformed(java.awt.event.ActionEvent evt) {
        //GEN-FIRST:event_btnDaftarActionPerformed
        // TODO add your handling code here:
        String phoneCust = txtPhone.getText().trim();
        if (phoneCust.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Isi No HP di kotak atas dulu untuk didaftarkan! 😹"
            );
            return;
        }

        String namaBaru = JOptionPane.showInputDialog(
            this,
            "No HP belum terdaftar.\nMasukkan Nama Lengkap Member Baru:"
        );
        if (namaBaru == null || namaBaru.trim().isEmpty()) return;

        Connection conn = Koneksi.getKoneksi();
        try {
            String sqlCustomer =
                "INSERT INTO customer (cust_name, phone) VALUES (?, ?)";
            PreparedStatement psCust = conn.prepareStatement(
                sqlCustomer,
                Statement.RETURN_GENERATED_KEYS
            );
            psCust.setString(1, namaBaru);
            psCust.setString(2, phoneCust);
            psCust.executeUpdate();

            ResultSet rsCust = psCust.getGeneratedKeys();
            int idCust = 0;
            if (rsCust.next()) idCust = rsCust.getInt(1);

            JOptionPane.showMessageDialog(
                this,
                "Pendaftaran Member Sukses atas nama: " + namaBaru
            );

            // Langsung lempar ke FramePembayaran
            new FramePembayaran(idPCKirim, idCust, namaBaru, false).setVisible(
                true
            );
            this.dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                this,
                "Gagal Daftar: " + e.getMessage()
            );
        }
    } //GEN-LAST:event_btnDaftarActionPerformed

    private void btnGuestActionPerformed(java.awt.event.ActionEvent evt) {
        //GEN-FIRST:event_btnGuestActionPerformed
        // TODO add your handling code here:
        // Lempar data khusus: ID customer diset 0, nama diset "Guest Pelanggan", status isGuest = true
        new FramePembayaran(idPCKirim, 0, "Guest Pelanggan", true).setVisible(
            true
        );
        this.dispose();
    } //GEN-LAST:event_btnGuestActionPerformed

    private void tabelCustomerMouseClicked(java.awt.event.MouseEvent evt) {
        //GEN-FIRST:event_tabelCustomerMouseClicked
        // TODO add your handling code here:
        int row = tabelCustomer.getSelectedRow();
        if (row != -1) {
            String phone = tabelCustomer.getValueAt(row, 2).toString();
            txtPhone.setText(phone); // Auto-fill ke textfield No HP! 🚀
        }
    } //GEN-LAST:event_tabelCustomerMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
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
            new FrameLoginMember().setVisible(true)
        );
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDaftar;
    private javax.swing.JButton btnGuest;
    private javax.swing.JButton btnLogin;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tabelCustomer;
    private javax.swing.JTextField txtPhone;
    // End of variables declaration//GEN-END:variables
}
