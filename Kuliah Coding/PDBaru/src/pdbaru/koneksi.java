/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pdbaru;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author Nowo
 */
public class koneksi {
    public static Connection con;
    public static Statement stm;
    public static Connection getKoneksi(){
        try {
            String url ="jdbc:mysql://localhost/db_pemdas";
            String user="root";
            String pass="";
            con = DriverManager.getConnection(url,user,pass);
            stm = con.createStatement();
            System.out.println("koneksi berhasil;");
        } catch (Exception e) {
            System.err.println("koneksi gagal"+e.getMessage());
        }
        return null;
    }

}
