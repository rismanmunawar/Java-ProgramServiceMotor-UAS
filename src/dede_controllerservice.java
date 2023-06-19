import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ResourceBundle;

public class dede_controllerservice implements Initializable {

    @FXML
    private MenuItem keluarMenuItem;

    public void BukaFormPelanggan(javafx.event.ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("dede_pelanggan.fxml"));
            Parent formPelanggan = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Form Pelanggan");
            stage.setScene(new Scene(formPelanggan));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void BukaFormLayanan(javafx.event.ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("dede_layanan.fxml"));
            Parent formPelanggan = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Form Layanan");
            stage.setScene(new Scene(formPelanggan));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void BukaFormMekanik(javafx.event.ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("dede_mekanik.fxml"));
            Parent formPelanggan = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Form Mekanik");
            stage.setScene(new Scene(formPelanggan));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void BukaFormTransaksi(javafx.event.ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("dede_transaksi.fxml"));
            Parent formPelanggan = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Form Transaksi");
            stage.setScene(new Scene(formPelanggan));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void BukaFormProgram(javafx.event.ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("dede_program.fxml"));
            Parent formPelanggan = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Form Program");
            stage.setScene(new Scene(formPelanggan));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void TutupForm(javafx.event.ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
    @FXML
    public void KeluarAplikasi() {
        Platform.exit();
    }


//    Untuk Pelanggan
@FXML
private Button bteditPL;

    @FXML
    private Button bthapusPL;

    @FXML
    private Button btkeluarDede;

    @FXML
    private Button btresetPL;

    @FXML
    private Button bttambahPL;
    @FXML
    private TableView<dede_pelanggan> tpelanggan_dede;

    @FXML
    private TableColumn<dede_pelanggan, String> colalamatPL;

    @FXML
    private TableColumn<dede_pelanggan, String> colidPL;

    @FXML
    private TableColumn<dede_pelanggan, String> colnamaPL;

    @FXML
    private TableColumn<dede_pelanggan, String> colnoPL;



    @FXML
    private TextField txCariPL;

    @FXML
    private TextField txalamatPL;

    @FXML
    private TextField txidPL;

    @FXML
    private TextField txnamaPL;

    @FXML
    private TextField txnoPL;
    public Connection koneksiKeDatabasedede() {
        Connection koneksi;
        try {
            koneksi = DriverManager.getConnection("jdbc:mysql://localhost:3306/uasservice_dede", "root", "");
            System.out.println("Koneksi Berhasil!");
            return koneksi;
        } catch (Exception e) {
            System.out.println("Kesalahan: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

}
