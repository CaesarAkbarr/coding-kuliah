/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tugas3;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;

/**
 *
 * @author ROG G513RM
 */
public class Koneksi {
    public static Connection con;
    public static Statement stmt;

    public static Connection getKoneksi() {
        try {
            String url = "jdbc:mysql://localhost/db_pemdas";
            String user = "root";
            String pass = "";
            con = DriverManager.getConnection(url, user, pass);
            stmt = con.createStatement();
            System.out.println("Koneksi berhasil dari java");
        } catch (Exception e) {
            System.err.println("Koneksi gagal" + e.getMessage());
        }
        return null;
    }

    public static void ubahData(String perintah) {
        try {
            String url = "jdbc:mysql://localhost/db_pemdas";
            String user = "root";
            String pass = "";
            con = DriverManager.getConnection(url, user, pass);
            stmt = con.createStatement();
            String query = perintah;
            stmt.executeUpdate(query);
            System.out.println("Data berhasil diupdate");
        } catch (Exception e) {
            System.err.println("Data gagal diupdate " + e.getMessage());
        }
    }
}
