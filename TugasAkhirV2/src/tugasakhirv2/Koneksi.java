/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tugasakhirv2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author ROG G513RM
 */
public class Koneksi {
    public static Connection getKoneksi() {
        Connection conn = null;
        try {
            String url = "jdbc:mysql://localhost:3306/db_pemdas";
            String user = "root";
            String pass = "";
            conn = DriverManager.getConnection(url, user, pass);
        } catch (SQLException e) {
            System.out.println("Koneksi Gagal: " + e.getMessage());
        }
        return conn;
    }
}
