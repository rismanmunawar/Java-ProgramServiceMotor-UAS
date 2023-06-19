import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import java.util.Optional;



import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class dede_pelanggan implements Initializable {

    @FXML
    private Button bteditPL;

    @FXML
    private Button bthapusPL;

    @FXML
    private Button btkeluarPL;

    @FXML
    private Button btresetPL;

    @FXML
    private Button bttambahPL;

    @FXML
    private TableView<dede_pelanggan> tpelanggan_dede;

    @FXML
    private TableColumn<dede_pelanggan, String> colidPL;

    @FXML
    private TableColumn<dede_pelanggan, String> colnamaPL;

    @FXML
    private TableColumn<dede_pelanggan, String> colalamatPL;

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

    @FXML
    public void tambahData(ActionEvent event) {
        String idPelanggan = txidPL.getText();
        String namaPelanggan = txnamaPL.getText();
        String alamatPelanggan = txalamatPL.getText();
        String teleponPelanggan = txnoPL.getText();

        // Periksa apakah ada field input yang kosong
        if (idPelanggan.isEmpty() || namaPelanggan.isEmpty() || alamatPelanggan.isEmpty() || teleponPelanggan.isEmpty()) {
            Alert validationAlert = new Alert(AlertType.WARNING);
            validationAlert.setTitle("Peringatan");
            validationAlert.setHeaderText(null);
            validationAlert.setContentText("Mohon isi semua field input!");
            validationAlert.showAndWait();
            return; // Hentikan eksekusi tambahData jika ada field kosong
        }

        dede_pelanggan pelanggan = new dede_pelanggan(idPelanggan, namaPelanggan, alamatPelanggan, teleponPelanggan);

        // Melanjutkan dengan kode tambah data ke database
        try {
            Connection connection = koneksiKeDatabasedede();

            // Periksa apakah ID pelanggan sudah ada dalam database
            String checkQuery = "SELECT * FROM tpelanggan_dede WHERE id_pelanggan = ?";
            PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
            checkStatement.setString(1, idPelanggan);
            ResultSet resultSet = checkStatement.executeQuery();

            if (resultSet.next()) {
                // Jika ID pelanggan sudah ada, tampilkan peringatan
                Alert duplicateAlert = new Alert(AlertType.WARNING);
                duplicateAlert.setTitle("Peringatan");
                duplicateAlert.setHeaderText(null);
                duplicateAlert.setContentText("ID pelanggan sudah ada dalam database!");
                duplicateAlert.showAndWait();
                resultSet.close();
                checkStatement.close();
                connection.close();
                return; // Hentikan eksekusi tambahData
            }

            resultSet.close();
            checkStatement.close();

            // Jika ID pelanggan belum ada, lanjutkan dengan penambahan data
            String insertQuery = "INSERT INTO tpelanggan_dede (id_pelanggan, nama_pelanggan, alamat_pelanggan, telepon_pelanggan) VALUES (?, ?, ?, ?)";
            PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
            insertStatement.setString(1, pelanggan.getId_pelanggan());
            insertStatement.setString(2, pelanggan.getNama_pelanggan());
            insertStatement.setString(3, pelanggan.getAlamat_pelanggan());
            insertStatement.setString(4, pelanggan.getTelepon_pelanggan());
            insertStatement.executeUpdate();
            insertStatement.close();
            connection.close();

            // Menambahkan data pelanggan ke ObservableList
            dataPelanggan.add(pelanggan);

            // Mengosongkan field input
            resetForm();

            Alert successAlert = new Alert(AlertType.INFORMATION);
            successAlert.setTitle("Informasi");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Data pelanggan berhasil ditambahkan!");
            successAlert.showAndWait();
        } catch (SQLException e) {
            Alert errorAlert = new Alert(AlertType.ERROR);
            errorAlert.setTitle("Kesalahan");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("Terjadi kesalahan: " + e.getMessage());
            errorAlert.showAndWait();
        }
    }




    @FXML
    public void editData(ActionEvent event) {
        dede_pelanggan pelangganTerpilih = tpelanggan_dede.getSelectionModel().getSelectedItem();
        if (pelangganTerpilih != null) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Konfirmasi");
            alert.setHeaderText(null);
            alert.setContentText("Apakah Anda yakin ingin mengedit data pelanggan?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                String idPelanggan = txidPL.getText();
                String namaPelanggan = txnamaPL.getText();
                String alamatPelanggan = txalamatPL.getText();
                String teleponPelanggan = txnoPL.getText();

                pelangganTerpilih.setId_pelanggan(idPelanggan);
                pelangganTerpilih.setNama_pelanggan(namaPelanggan);
                pelangganTerpilih.setAlamat_pelanggan(alamatPelanggan);
                pelangganTerpilih.setTelepon_pelanggan(teleponPelanggan);

                try {
                    Connection connection = koneksiKeDatabasedede();
                    String query = "UPDATE tpelanggan_dede SET nama_pelanggan = ?, alamat_pelanggan = ?, telepon_pelanggan = ? WHERE id_pelanggan = ?";
                    PreparedStatement statement = connection.prepareStatement(query);
                    statement.setString(1, pelangganTerpilih.getNama_pelanggan());
                    statement.setString(2, pelangganTerpilih.getAlamat_pelanggan());
                    statement.setString(3, pelangganTerpilih.getTelepon_pelanggan());
                    statement.setString(4, pelangganTerpilih.getId_pelanggan());
                    statement.executeUpdate();
                    statement.close();
                    connection.close();

                    // Memperbarui data pelanggan di ObservableList
                    tpelanggan_dede.refresh();

                    // Mengosongkan field input
                    resetForm();

                    Alert successAlert = new Alert(AlertType.INFORMATION);
                    successAlert.setTitle("Informasi");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("Data pelanggan berhasil diupdate!");
                    successAlert.showAndWait();
                } catch (SQLException e) {
                    Alert errorAlert = new Alert(AlertType.ERROR);
                    errorAlert.setTitle("Kesalahan");
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText("Terjadi kesalahan: " + e.getMessage());
                    errorAlert.showAndWait();
                }
            }
        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Peringatan");
            alert.setHeaderText(null);
            alert.setContentText("Tidak ada pelanggan yang dipilih!");
            alert.showAndWait();
        }
    }

    @FXML
    public void hapusData(ActionEvent event) {
        dede_pelanggan pelangganTerpilih = tpelanggan_dede.getSelectionModel().getSelectedItem();
        if (pelangganTerpilih != null) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Konfirmasi");
            alert.setHeaderText(null);
            alert.setContentText("Apakah Anda yakin ingin menghapus data pelanggan?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    Connection connection = koneksiKeDatabasedede();
                    String query = "DELETE FROM tpelanggan_dede WHERE id_pelanggan = ?";
                    PreparedStatement statement = connection.prepareStatement(query);
                    statement.setString(1, pelangganTerpilih.getId_pelanggan());
                    statement.executeUpdate();
                    statement.close();
                    connection.close();

                    // Menghapus data pelanggan dari ObservableList
                    dataPelanggan.remove(pelangganTerpilih);

                    // Mengosongkan field input
                    resetForm();

                    Alert successAlert = new Alert(AlertType.INFORMATION);
                    successAlert.setTitle("Informasi");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("Data pelanggan berhasil dihapus!");
                    successAlert.showAndWait();
                } catch (SQLException e) {
                    Alert errorAlert = new Alert(AlertType.ERROR);
                    errorAlert.setTitle("Kesalahan");
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText("Terjadi kesalahan: " + e.getMessage());
                    errorAlert.showAndWait();
                }
            }
        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Peringatan");
            alert.setHeaderText(null);
            alert.setContentText("Tidak ada pelanggan yang dipilih!");
            alert.showAndWait();
        }
    }

    @FXML
    public void resetData(ActionEvent event) {
        resetForm();
    }

    @FXML
    public void TutupFormPL(ActionEvent event) {
        Stage stage = (Stage) btkeluarPL.getScene().getWindow();
        stage.close();
    }
    private void resetForm() {
        txidPL.clear();
        txnamaPL.clear();
        txalamatPL.clear();
        txnoPL.clear();
        tpelanggan_dede.getSelectionModel().clearSelection();
    }

    private Connection koneksiKeDatabasedede() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/uasservice_dede";
        String username = "root";
        String password = "";
        Connection connection = DriverManager.getConnection(url, username, password);
        System.out.println("Koneksi Berhasil!");
        return connection;
    }
    private ObservableList<dede_pelanggan> dataPelanggan;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colidPL.setCellValueFactory(new PropertyValueFactory<>("id_pelanggan"));
        colnamaPL.setCellValueFactory(new PropertyValueFactory<>("nama_pelanggan"));
        colalamatPL.setCellValueFactory(new PropertyValueFactory<>("alamat_pelanggan"));
        colnoPL.setCellValueFactory(new PropertyValueFactory<>("telepon_pelanggan"));

        dataPelanggan = FXCollections.observableArrayList();

        try {
            Connection connection = koneksiKeDatabasedede();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM tpelanggan_dede");

            while (resultSet.next()) {
                String idPelanggan = resultSet.getString("id_pelanggan");
                String namaPelanggan = resultSet.getString("nama_pelanggan");
                String alamatPelanggan = resultSet.getString("alamat_pelanggan");
                String teleponPelanggan = resultSet.getString("telepon_pelanggan");

                dede_pelanggan pelanggan = new dede_pelanggan(idPelanggan, namaPelanggan, alamatPelanggan, teleponPelanggan);
                dataPelanggan.add(pelanggan);
            }

            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Kesalahan: " + e.getMessage());
        }

        tpelanggan_dede.setItems(dataPelanggan);

        tpelanggan_dede.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                dede_pelanggan pelangganTerpilih = newSelection;
                txidPL.setText(pelangganTerpilih.getId_pelanggan());
                txnamaPL.setText(pelangganTerpilih.getNama_pelanggan());
                txalamatPL.setText(pelangganTerpilih.getAlamat_pelanggan());
                txnoPL.setText(pelangganTerpilih.getTelepon_pelanggan());
            }
        });

        // Memfilter data berdasarkan teks pencarian
        FilteredList<dede_pelanggan> filteredDataPelanggan = new FilteredList<>(dataPelanggan);
        txCariPL.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredDataPelanggan.setPredicate(pelanggan -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true; // Tampilkan semua data jika teks pencarian kosong
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (pelanggan.getNama_pelanggan().toLowerCase().contains(lowerCaseFilter)
                        || pelanggan.getAlamat_pelanggan().toLowerCase().contains(lowerCaseFilter)
                        || pelanggan.getTelepon_pelanggan().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Cocok dengan nama, alamat, atau telepon pelanggan
                }

                return false;
            });
        });

        // Mengatur data yang akan ditampilkan ke dalam TableView berdasarkan hasil pemfilteran
        SortedList<dede_pelanggan> sortedDataPelanggan = new SortedList<>(filteredDataPelanggan);
        sortedDataPelanggan.comparatorProperty().bind(tpelanggan_dede.comparatorProperty());
        tpelanggan_dede.setItems(sortedDataPelanggan);
    }


    public dede_pelanggan() {
    }

    public dede_pelanggan(String id_pelanggan, String nama_pelanggan, String alamat_pelanggan, String telepon_pelanggan) {
        this.id_pelanggan = id_pelanggan;
        this.nama_pelanggan = nama_pelanggan;
        this.alamat_pelanggan = alamat_pelanggan;
        this.telepon_pelanggan = telepon_pelanggan;
    }

    // Getters and Setters

    private String id_pelanggan;

    public String getId_pelanggan() {
        return id_pelanggan;
    }

    public void setId_pelanggan(String id_pelanggan) {
        this.id_pelanggan = id_pelanggan;
    }

    private String nama_pelanggan;

    public String getNama_pelanggan() {
        return nama_pelanggan;
    }

    public void setNama_pelanggan(String nama_pelanggan) {
        this.nama_pelanggan = nama_pelanggan;
    }

    private String alamat_pelanggan;

    public String getAlamat_pelanggan() {
        return alamat_pelanggan;
    }

    public void setAlamat_pelanggan(String alamat_pelanggan) {
        this.alamat_pelanggan = alamat_pelanggan;
    }

    private String telepon_pelanggan;

    public String getTelepon_pelanggan() {
        return telepon_pelanggan;
    }

    public void setTelepon_pelanggan(String telepon_pelanggan) {
        this.telepon_pelanggan = telepon_pelanggan;
    }
}
