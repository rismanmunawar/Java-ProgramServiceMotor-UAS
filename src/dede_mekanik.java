import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;

public class dede_mekanik implements Initializable {
    private ObservableList<dede_mekanik> dataMekanik;

    @FXML
    private Button bteditMK;

    @FXML
    private Button bthapusMK;

    @FXML
    private Button btkeluarMK;

    @FXML
    private Button btresetMK;

    @FXML
    private Button bttambahMK;

    @FXML
    private TableView<dede_mekanik> tmekanik_dede;

    @FXML
    private TableColumn<dede_mekanik, String> colidMK;

    @FXML
    private TableColumn<dede_mekanik, String> colnamaMK;

    @FXML
    private TableColumn<dede_mekanik, String> colalamatMK;

    @FXML
    private TableColumn<dede_mekanik, String> colnoMK;

    @FXML
    private TextField txCariMK;

    @FXML
    private TextField txalamatMK;

    @FXML
    private TextField txidMK;

    @FXML
    private TextField txnamaMK;

    @FXML
    private TextField txnoMK;


    @FXML
    public void tambahData(ActionEvent event) {
        String idMekanik = txidMK.getText();
        String namaMekanik = txnamaMK.getText();
        String alamatMekanik = txalamatMK.getText();
        String teleponMekanik = txnoMK.getText();

        // Memeriksa apakah semua field telah terisi
        if (idMekanik.isEmpty() || namaMekanik.isEmpty() || alamatMekanik.isEmpty() || teleponMekanik.isEmpty()) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Kesalahan");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("Harap lengkapi semua field sebelum menyimpan data!");
            errorAlert.showAndWait();
            return;
        }

        dede_mekanik mekanik = new dede_mekanik(idMekanik, namaMekanik, alamatMekanik, teleponMekanik);

        try {
            Connection connection = koneksiKeDatabasedede();
            String query = "INSERT INTO tmekanik_dede (id_mekanik, nama_mekanik, alamat_mekanik, telepon_mekanik) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, mekanik.getId_mekanik());
            statement.setString(2, mekanik.getNama_mekanik());
            statement.setString(3, mekanik.getAlamat_mekanik());
            statement.setString(4, mekanik.getTelepon_mekanik());
            statement.executeUpdate();
            statement.close();
            connection.close();

            // Menambahkan data pelanggan ke ObservableList
            dataMekanik.add(mekanik);

            // Mengosongkan field input
            resetForm();

            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Informasi");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Data Mekanik berhasil ditambahkan!");
            successAlert.showAndWait();
        } catch (SQLException e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Kesalahan");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("Terjadi kesalahan: " + e.getMessage());
            errorAlert.showAndWait();
        }
    }



    @FXML
    public void editData(ActionEvent event) {
        dede_mekanik mekanikTerpilih = tmekanik_dede.getSelectionModel().getSelectedItem();
        if (mekanikTerpilih != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Konfirmasi");
            alert.setHeaderText(null);
            alert.setContentText("Apakah Anda yakin ingin mengedit data mekanik?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                String idMekanik = txidMK.getText();
                String namaMekanik = txnamaMK.getText();
                String alamatMekanik = txalamatMK.getText();
                String teleponMekanik = txnoMK.getText();

                mekanikTerpilih.setId_mekanik(idMekanik);
                mekanikTerpilih.setNama_mekanik(namaMekanik);
                mekanikTerpilih.setAlamat_mekanik(alamatMekanik);
                mekanikTerpilih.setTelepon_mekanik(teleponMekanik);

                try {
                    Connection connection = koneksiKeDatabasedede();
                    String query = "UPDATE tmekanik_dede SET nama_mekanik = ?, alamat_mekanik = ?, telepon_mekanik = ? WHERE id_mekanik = ?";
                    PreparedStatement statement = connection.prepareStatement(query);
                    statement.setString(1, mekanikTerpilih.getNama_mekanik());
                    statement.setString(2, mekanikTerpilih.getAlamat_mekanik());
                    statement.setString(3, mekanikTerpilih.getTelepon_mekanik());
                    statement.setString(4, mekanikTerpilih.getId_mekanik());
                    statement.executeUpdate();
                    statement.close();
                    connection.close();

                    // Memperbarui data pelanggan di ObservableList
                    tmekanik_dede.refresh();

                    // Mengosongkan field input
                    resetForm();

                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Informasi");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("Data mekanik berhasil diupdate!");
                    successAlert.showAndWait();
                } catch (SQLException e) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Kesalahan");
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText("Terjadi kesalahan: " + e.getMessage());
                    errorAlert.showAndWait();
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Peringatan");
            alert.setHeaderText(null);
            alert.setContentText("Tidak ada mekanik yang dipilih!");
            alert.showAndWait();
        }
    }



    @FXML
    public void hapusData(ActionEvent event) {
        dede_mekanik mekanikTerpilih = tmekanik_dede.getSelectionModel().getSelectedItem();
        if (mekanikTerpilih != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Konfirmasi");
            alert.setHeaderText(null);
            alert.setContentText("Apakah Anda yakin ingin menghapus data pelanggan?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    Connection connection = koneksiKeDatabasedede();
                    String query = "DELETE FROM tmekanik_dede WHERE id_mekanik = ?";
                    PreparedStatement statement = connection.prepareStatement(query);
                    statement.setString(1, mekanikTerpilih.getId_mekanik());
                    statement.executeUpdate();
                    statement.close();
                    connection.close();

                    // Menghapus data pelanggan dari ObservableList
                    dataMekanik.remove(mekanikTerpilih);

                    // Mengosongkan field input
                    resetForm();

                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Informasi");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("Data Mekanik berhasil dihapus!");
                    successAlert.showAndWait();
                } catch (SQLException e) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Kesalahan");
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText("Terjadi kesalahan: " + e.getMessage());
                    errorAlert.showAndWait();
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Peringatan");
            alert.setHeaderText(null);
            alert.setContentText("Tidak ada mekanik yang dipilih!");
            alert.showAndWait();
        }
    }


    @FXML
    public void resetData(ActionEvent event) {
        resetForm();
    }

    @FXML
    public void TutupForm(ActionEvent event) {
        Stage stage = (Stage) btkeluarMK.getScene().getWindow();
        stage.close();
    }

    private void resetForm() {
        txidMK.clear();
        txnamaMK.clear();
        txalamatMK.clear();
        txnoMK.clear();
        tmekanik_dede.getSelectionModel().clearSelection();
    }

    private Connection koneksiKeDatabasedede() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/uasservice_dede";
        String username = "root";
        String password = "";
        Connection connection = DriverManager.getConnection(url, username, password);
        System.out.println("Koneksi Berhasil!");
        return connection;
    }

    private FilteredList<dede_mekanik> filteredDataMekanik;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Inisialisasi kolom pada TableView
        colidMK.setCellValueFactory(new PropertyValueFactory<>("id_mekanik"));
        colnamaMK.setCellValueFactory(new PropertyValueFactory<>("nama_mekanik"));
        colalamatMK.setCellValueFactory(new PropertyValueFactory<>("alamat_mekanik"));
        colnoMK.setCellValueFactory(new PropertyValueFactory<>("telepon_mekanik"));

        // Ambil data dari database dan masukkan ke dalam ObservableList
        dataMekanik = FXCollections.observableArrayList();

        try {
            Connection connection = koneksiKeDatabasedede();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM tmekanik_dede");

            while (resultSet.next()) {
                String idMekanik = resultSet.getString("id_mekanik");
                String namaMekanik = resultSet.getString("nama_mekanik");
                String alamatMekanik = resultSet.getString("alamat_mekanik");
                String teleponMekanik = resultSet.getString("telepon_mekanik");

                dede_mekanik mekanik = new dede_mekanik(idMekanik, namaMekanik, alamatMekanik, teleponMekanik);
                dataMekanik.add(mekanik);
            }

            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Kesalahan: " + e.getMessage());
        }

        // Inisialisasi FilteredList
        filteredDataMekanik = new FilteredList<>(dataMekanik, p -> true);

        // Menghubungkan FilteredList dengan TableView
        tmekanik_dede.setItems(filteredDataMekanik);

        // Menerapkan filter berdasarkan teks pencarian pada txCariMK
        txCariMK.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredDataMekanik.setPredicate(mekanik -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                // Sesuaikan dengan atribut dataMekanik yang ingin dijadikan kriteria pencarian
                if (mekanik.getId_mekanik().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (mekanik.getNama_mekanik().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (mekanik.getAlamat_mekanik().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (mekanik.getTelepon_mekanik().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }

                return false; // Jika tidak ada kriteria yang cocok, data akan disembunyikan
            });
        });

        // Memanggil isian data ketika tabel diklik
        tmekanik_dede.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                dede_mekanik mekanikTerpilih = newSelection;
                txidMK.setText(mekanikTerpilih.getId_mekanik());
                txnamaMK.setText(mekanikTerpilih.getNama_mekanik());
                txalamatMK.setText(mekanikTerpilih.getAlamat_mekanik());
                txnoMK.setText(mekanikTerpilih.getTelepon_mekanik());
            }
        });

    }


    public dede_mekanik() {
    }

    public dede_mekanik(String id_mekanik, String nama_mekanik, String alamat_mekanik, String telepon_mekanik) {
        this.id_mekanik = id_mekanik;
        this.nama_mekanik = nama_mekanik;
        this.alamat_mekanik = alamat_mekanik;
        this.telepon_mekanik = telepon_mekanik;
    }

    // Getters and Setters

    private String id_mekanik;

    public String getId_mekanik() {
        return id_mekanik;
    }

    public void setId_mekanik(String id_mekanik) {
        this.id_mekanik = id_mekanik;
    }

    private String nama_mekanik;

    public String getNama_mekanik() {
        return nama_mekanik;
    }

    public void setNama_mekanik(String nama_mekanik) {
        this.nama_mekanik = nama_mekanik;
    }

    private String alamat_mekanik;

    public String getAlamat_mekanik() {
        return alamat_mekanik;
    }

    public void setAlamat_mekanik(String alamat_mekanik) {
        this.alamat_mekanik = alamat_mekanik;
    }

    private String telepon_mekanik;

    public String getTelepon_mekanik() {
        return telepon_mekanik;
    }

    public void setTelepon_mekanik(String telepon_mekanik) {
        this.telepon_mekanik = telepon_mekanik;
    }
}
