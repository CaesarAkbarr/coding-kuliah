/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tugas_lebaran;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 *
 * @author ROG G513RM
 */
public class KoneksiDtb {
    public static Connection con;
    public static Statement stmt;

    public static Connection getKoneksi() {
        try {
            String url = "jdbc:mysql://localhost/tugas_lebaran";
            String user = "root";
            String pass = "";
            con = DriverManager.getConnection(url, user, pass);
            stmt = con.createStatement();
            System.out.println("Koneksi berhasil");
        } catch (Exception e) {
            System.err.println("Koneksi gagal " + e.getMessage());
        }
        // Mengembalikan objek koneksi yang sudah dibuat
        return con;
    }
}
