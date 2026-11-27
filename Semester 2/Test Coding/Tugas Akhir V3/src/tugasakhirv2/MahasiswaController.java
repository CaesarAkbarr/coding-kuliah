/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package tugasakhirv2;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author ROG G513RM
 */
public class MahasiswaController implements Initializable {
    
    @FXML private TextField txtNim, txtNama, txtAlamat, txtNoHp, txtKota;
    @FXML private TableView<Mahasiswa> tableData;
    @FXML private TableColumn<Mahasiswa, String> colNim, colNama, colAlamat, colNoHp, colKota;

    private final ObservableList<Mahasiswa> listMahasiswa = FXCollections.observableArrayList();
    @FXML
    private Button btnSimpan;
    @FXML
    private Button btnHapus;
    @FXML
    private Button btnUbah;
    @FXML
    private Button btnBatal;
    @FXML
    private TextArea txtCari;

    @FXML
    private void handleButtonAction(ActionEvent event) {
        String nim = txtNim.getText();
        String nama = txtNama.getText();
        if (nim.isEmpty() || nama.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Peringatan");
            alert.setHeaderText(null);
            alert.setContentText("NIM dan Nama tidak boleh kosong!");
            alert.showAndWait();
            return; // Berhenti di sini, jangan lanjut simpan
        }
        
        String sql = "INSERT INTO mhs (nim, nama, alamat, hp, kota) VALUES (?,?,?,?,?)";
        try (Connection conn = Koneksi.getKoneksi(); 
            PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, nim);
            pst.setString(2, nama);
            pst.setString(3, txtAlamat.getText());
            pst.setString(4, txtNoHp.getText());
            pst.setString(5, txtKota.getText());
            pst.executeUpdate();
            loadData(); // Refresh tabel
        } catch (SQLException e) {
            tampilkanAlert("Error", "Gagal simpan: " + e.getMessage());
        }
    }
    
    private void loadData() {
    listMahasiswa.clear();
        try {
            Connection conn = Koneksi.getKoneksi();
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM mhs");
            while (rs.next()) {
                listMahasiswa.add(new Mahasiswa(
                    rs.getString("nim"), rs.getString("nama"), 
                    rs.getString("alamat"), rs.getString("hp"), rs.getString("kota")
                ));
            }
        } catch (SQLException e) {
            tampilkanAlert("Error", "Gagal load data: " + e.getMessage());
            }
    }

    private void clearFields() {
        txtNim.clear(); txtNama.clear(); txtAlamat.clear(); txtNoHp.clear(); txtKota.clear();
    }
    
    private void tampilkanAlert(String title, String content) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(content);
    alert.showAndWait();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // 1. Setup CellValueFactory (Binding kolom tabel)
        colNim.setCellValueFactory(new PropertyValueFactory<>("nim"));
        colNama.setCellValueFactory(new PropertyValueFactory<>("nama"));
        colAlamat.setCellValueFactory(new PropertyValueFactory<>("alamat"));
        colNoHp.setCellValueFactory(new PropertyValueFactory<>("noHp"));
        colKota.setCellValueFactory(new PropertyValueFactory<>("kota"));

        // 2. Bungkus listMahasiswa ke FilteredList buat pencarian
        FilteredList<Mahasiswa> filteredData = new FilteredList<>(listMahasiswa, p -> true);

        // 3. Listener pencarian (Pake txtCari)
        txtCari.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(mhs -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (mhs.getNim().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (mhs.getNama().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false; 
            });
        });

        // 4. Setup Sorting agar data tetap bisa diurutkan
        SortedList<Mahasiswa> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableData.comparatorProperty());

        // 5. SET TABEL KE SORTED DATA (BUKAN listMahasiswa!)
        tableData.setItems(sortedData);

        // 6. BARU TARIK DATA DARI DB (Biar kalau error di sini, search-nya udah terpasang)
        loadData();

        // 7. Selection Listener tetap ada
        tableData.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                txtNim.setText(newSelection.getNim());
                txtNama.setText(newSelection.getNama());
                txtAlamat.setText(newSelection.getAlamat());
                txtNoHp.setText(newSelection.getNoHp());
                txtKota.setText(newSelection.getKota());
            }
        });
    }

    @FXML
    private void handleHapus(ActionEvent event) {
        Mahasiswa selected = tableData.getSelectionModel().getSelectedItem();
        if (selected != null) {
            // Logika SQL Delete
            String sql = "DELETE FROM mhs WHERE nim = ?";
            try (Connection conn = Koneksi.getKoneksi(); 
                 PreparedStatement pst = conn.prepareStatement(sql)) {
                pst.setString(1, selected.getNim());
                pst.executeUpdate();

                tampilkanAlert("Success", "Data berhasil dihapus dari database!");
                loadData(); // Refresh total biar sinkron
                clearFields();
            } catch (SQLException e) {
                tampilkanAlert("Error", "Gagal hapus: " + e.getMessage());
            }
        } else {
            tampilkanAlert("Peringatan", "Pilih data di tabel dulu, Bro!");
        }
    }

    @FXML
    private void handleUbah(ActionEvent event) {
        String nim = txtNim.getText();
        if (nim.isEmpty()) {
            tampilkanAlert("Peringatan", "NIM nggak boleh kosong buat update!");
            return;
        }

        String sql = "UPDATE mhs SET nama=?, alamat=?, hp=?, kota=? WHERE nim=?";
        try (Connection conn = Koneksi.getKoneksi(); 
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, txtNama.getText());
            pst.setString(2, txtAlamat.getText());
            pst.setString(3, txtNoHp.getText());
            pst.setString(4, txtKota.getText());
            pst.setString(5, nim); // NIM jadi penentu baris mana yang diubah

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                tampilkanAlert("Success", "Data berhasil di-update!");
                loadData(); // Sync ulang
                clearFields();
            }
        } catch (SQLException e) {
            tampilkanAlert("Error", "Gagal update: " + e.getMessage());
        }
    }

    @FXML
    private void handleBatal(ActionEvent event) {
        clearFields();
        tableData.getSelectionModel().clearSelection(); // Lepas seleksi di tabel
    }

    @FXML
    private void handleMenuMahasiswa(ActionEvent event) throws IOException {
        Parent mainMenu = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        Scene mainScene = new Scene(mainMenu);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(mainScene);
        stage.setMaximized(true);
        stage.show();
    }
}
