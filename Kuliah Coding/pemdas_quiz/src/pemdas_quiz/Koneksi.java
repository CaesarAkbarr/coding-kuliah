package pemdas_quiz;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Kelas helper untuk koneksi database MySQL dan operasi umum.
 */
public class Koneksi {

    public static Connection con;
    public static Statement stmt;
    private static final String URL = "jdbc:mysql://localhost/pemdas_quiz";
    private static final String USER = "root";
    private static final String PASS = "";

    // Mendapatkan koneksi ke database
    public static Connection getKoneksi() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(URL, USER, PASS);
            stmt = con.createStatement();
            System.out.println("Koneksi berhasil.");
        } catch (Exception e) {
            System.err.println("Koneksi gagal: " + e.getMessage());
        }
        return con;
    }

    // Memastikan koneksi selalu aktif sebelum digunakan
    private static void ensureConnected() {
        try {
            if (con == null || con.isClosed()) {
                getKoneksi();
            }
        } catch (Exception e) {
            getKoneksi();
        }
    }

    // Generate nomor pesanan otomatis format TRX-YYYYMMDD-XXX
    public static String generateNoPesanan() {
        ensureConnected();
        try {
            String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String prefix = "TRX-" + today + "-";
            String query = "SELECT MAX(kd_pembelian) as last_no FROM tb_pembelian WHERE kd_pembelian LIKE '" + prefix + "%'";
            ResultSet rs = stmt.executeQuery(query);
            int nextNum = 1;
            if (rs.next() && rs.getString("last_no") != null) {
                // Ambil 3 digit terakhir nomor urut
                String lastNo = rs.getString("last_no");
                String lastSeq = lastNo.substring(lastNo.lastIndexOf("-") + 1);
                nextNum = Integer.parseInt(lastSeq) + 1;
            }
            rs.close();
            return prefix + String.format("%03d", nextNum);
        } catch (Exception e) {
            System.err.println("Error generate no pesanan: " + e.getMessage());
            String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            return "TRX-" + today + "-001";
        }
    }

    // Generate ID otomatis dengan prefix dan jumlah digit (misal: S00001)
    public static String generateNextId(String table, String column, String prefix, int digitCount) {
        ensureConnected();
        try {
            String query = "SELECT MAX(CAST(SUBSTRING(" + column + ", " + (prefix.length() + 1)
                    + ") AS UNSIGNED)) as max_num FROM " + table;
            ResultSet rs = stmt.executeQuery(query);
            int nextNum = 1;
            if (rs.next()) {
                int maxNum = rs.getInt("max_num");
                if (maxNum > 0) {
                    nextNum = maxNum + 1;
                }
            }
            rs.close();
            return prefix + String.format("%0" + digitCount + "d", nextNum);
        } catch (Exception e) {
            System.err.println("Error generate ID: " + e.getMessage());
            return prefix + String.format("%0" + digitCount + "d", 1);
        }
    }

    // Eksekusi query SELECT dan mengembalikan ResultSet
    public static ResultSet executeQuery(String query) {
        ensureConnected();
        try {
            // Buat statement baru agar ResultSet tidak conflict
            Statement newStmt = con.createStatement();
            return newStmt.executeQuery(query);
        } catch (Exception e) {
            System.err.println("Error execute query: " + e.getMessage());
            return null;
        }
    }

    // Eksekusi query INSERT/UPDATE/DELETE
    public static boolean executeUpdate(String query) {
        ensureConnected();
        try {
            Statement newStmt = con.createStatement();
            newStmt.executeUpdate(query);
            return true;
        } catch (Exception e) {
            System.err.println("Error execute update: " + e.getMessage());
            return false;
        }
    }

    // Eksekusi PreparedStatement (lebih aman untuk data dinamis)
    public static PreparedStatement prepareStatement(String sql) {
        ensureConnected();
        try {
            return con.prepareStatement(sql);
        } catch (Exception e) {
            System.err.println("Error prepare statement: " + e.getMessage());
            return null;
        }
    }
}
