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
            String url ="jdbc:mysql://localhost/dbpw";
            String user="root";
            String pass="";
            
            // Registrasi driver dan membuat koneksi ke MySQL
            con = DriverManager.getConnection(url, user, pass);
            stm = con.createStatement();
            
            System.out.println("koneksi berhasil;");
            return con; // <-- PERBAIKAN 1: Kembalikan variabel con jika berhasil
            
        } catch (Exception e) {
            System.err.println("koneksi gagal: " + e.getMessage());
        }
        return con; // <-- PERBAIKAN 2: Kembalikan con (bukan null) jika terjadi catch/error
    }
}