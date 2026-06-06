/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package tes1;

import com.formdev.flatlaf.FlatDarkLaf; // Import tema dark mode-nya
import javax.swing.UIManager;

/**
 *
 * @author ROG G513RM
 */
public class Tes1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try {
            // Mengaktifkan FlatLaf Dark Mode
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("Gagal mengaktifkan FlatLaf! 😹");
        }

        // Baru setelah itu panggil JFrame / Tampilan UI lo di bawah sini
        java.awt.EventQueue.invokeLater(() -> {
            new TesForm().setVisible(true);
        });
    }
}
