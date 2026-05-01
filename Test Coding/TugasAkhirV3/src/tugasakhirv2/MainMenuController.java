/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package tugasakhirv2;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ROG G513RM
 */
public class MainMenuController implements Initializable {
    
    @FXML
    private void handleMenuMahasiswa(ActionEvent event) throws IOException {
        // 1. Load file halaman mahasiswa
        Parent mhsPage = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        Scene mhsScene = new Scene(mhsPage);
        
        // 2. Ambil Stage (Jendela) yang lagi aktif
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        
        // 3. Switch halamannya
        stage.setScene(mhsScene);
        stage.setMaximized(true); // Biar tetep maximized
        stage.show();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
    }    
    
}
