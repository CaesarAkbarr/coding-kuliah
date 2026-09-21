/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML.java to edit this template
 */
package tugasakhirv2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author ROG G513RM
 */
public class Main extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
    // GANTI baris ini dari FXMLDocument ke MainMenu
    Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
    
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.setMaximized(true); 
    stage.show();
}

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
