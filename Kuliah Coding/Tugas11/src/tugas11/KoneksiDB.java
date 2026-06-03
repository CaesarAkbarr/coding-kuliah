/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tugas11;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author Nowo
 */
public class KoneksiDB {
    // public static Connection con;
    // public static Statement stm;
    // public static Connection getKoneksi(){
    //     try {
    //         String url ="jdbc:mysql://localhost/pemdas_quiz";
    //         String user="root";
    //         String pass="";
    //         con = DriverManager.getConnection(url,user,pass);
    //         stm = con.createStatement();
    //         System.out.println("koneksi berhasil;");
    //     } catch (Exception e) {
    //         System.err.println("koneksi gagal"+e.getMessage());
    //     }
    //     return null;
    // }

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

}
