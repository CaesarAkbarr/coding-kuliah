/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pemdas_quiz_final;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.DriverManager;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author ROG G513RM
 */
public class Koneksi {

    // Konstanta koneksi database
    private static final String URL  = "jdbc:mysql://localhost/pemdas_quiz";
    private static final String USER = "root";
    private static final String PASS = "";

    public static Connection con;
    public static Statement stmt;

    // Membuat dan mengembalikan objek Connection ke database
    public static Connection getKoneksi() {
        try {
            con  = DriverManager.getConnection(URL, USER, PASS);
            stmt = con.createStatement();
            System.out.println("Koneksi berhasil ke pemdas_quiz");
        } catch (Exception e) {
            System.err.println("Koneksi gagal: " + e.getMessage());
        }
        return con;
    }

    // Menjalankan query INSERT/UPDATE/DELETE sederhana
    public static void ubahData(String perintah) {
        try {
            Connection c = getKoneksi();
            Statement s  = c.createStatement();
            s.executeUpdate(perintah);
            System.out.println("Data berhasil diubah");
        } catch (Exception e) {
            System.err.println("Data gagal diubah: " + e.getMessage());
        }
    }

    /**
     * Auto-generate No Pesanan dengan format TRX-YYYYMMDD-XXX.
     * Setiap hari counter di-reset dari 001.
     */
    public static String generateNoPesanan() {
        String noPesanan = "";
        try {
            Connection c = getKoneksi();

            // Ambil tanggal hari ini sebagai prefix
            String tanggal = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String prefix  = "TRX-" + tanggal + "-";

            // Cari nomor urut terakhir pada hari ini
            String sql = "SELECT kd_pembelian FROM tb_pembelian "
                       + "WHERE kd_pembelian LIKE '" + prefix + "%' "
                       + "ORDER BY kd_pembelian DESC LIMIT 1";

            ResultSet rs = c.createStatement().executeQuery(sql);
            int urutan = 1;
            if (rs.next()) {
                // Ambil 3 digit terakhir lalu increment
                String last = rs.getString("kd_pembelian");
                String noStr = last.substring(last.lastIndexOf("-") + 1);
                urutan = Integer.parseInt(noStr) + 1;
            }
            rs.close();

            // Format nomor urut 3 digit
            noPesanan = String.format("%s%03d", prefix, urutan);
        } catch (Exception e) {
            System.err.println("Gagal generate No Pesanan: " + e.getMessage());
        }
        return noPesanan;
    }

    /**
     * Auto-generate ID master data.
     * tabel    : nama tabel (contoh: "tb_barang")
     * kolom    : nama kolom PK (contoh: "kd_barang")
     * prefix   : huruf awalan (contoh: "B" → B00001)
     */
    public static String generateIdMaster(String tabel, String kolom, String prefix) {
        String id = "";
        try {
            Connection c = getKoneksi();
            String sql   = "SELECT " + kolom + " FROM " + tabel
                         + " WHERE " + kolom + " LIKE '" + prefix + "%'"
                         + " ORDER BY " + kolom + " DESC LIMIT 1";

            ResultSet rs = c.createStatement().executeQuery(sql);
            int urutan   = 1;
            if (rs.next()) {
                String last = rs.getString(kolom);
                // Ambil bagian angka setelah prefix
                String noStr = last.substring(prefix.length());
                urutan = Integer.parseInt(noStr) + 1;
            }
            rs.close();

            // Format 5 digit angka setelah prefix
            id = String.format("%s%05d", prefix, urutan);
        } catch (Exception e) {
            System.err.println("Gagal generate ID master: " + e.getMessage());
        }
        return id;
    }
}
