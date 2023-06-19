import javafx.collections.transformation.FilteredList;
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
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import java.util.Optional;
import javafx.scene.control.cell.PropertyValueFactory;



import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class dede_layanan implements Initializable {
    private ObservableList<dede_layanan> dataLayanan;

    @FXML
    private Button bteditLY;

    @FXML
    private Button bthapusLY;

    @FXML
    private Button btkeluarLY;

    @FXML
    private Button btresetLY;

    @FXML
    private Button bttambahLY;

    @FXML
    private TableView<dede_layanan> tlayanan_dede;

    @FXML
    private TableColumn<dede_layanan, String> colidLY;

    @FXML
    private TableColumn<dede_layanan, String> colnamaLY;

    @FXML
    private TableColumn<dede_layanan, Integer> colhargaLY;

    @FXML
    private TextField txCariLY;

    @FXML
    private TextField txhargaLY;

    @FXML
    private TextField txidLY;

    @FXML
    private TextField txnamaLY;
    public dede_layanan() {
        // Konstruktor default
    }

    @FXML
    public void tambahData(ActionEvent event) {
        String idLayanan = txidLY.getText();
        String namaLayanan = txnamaLY.getText();
        String hargaLayanan = txhargaLY.getText();
        if (idLayanan.isEmpty() || namaLayanan.isEmpty() || hargaLayanan.isEmpty()) {
            Alert validationAlert = new Alert(Alert.AlertType.WARNING);
            validationAlert.setTitle("Peringatan");
            validationAlert.setHeaderText(null);
            validationAlert.setContentText("Mohon isi semua field input!");
            validationAlert.showAndWait();
            return;
        }
        int hargaLayananInt;
        try {
            hargaLayananInt = Integer.parseInt(hargaLayanan);
        } catch (NumberFormatException e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Kesalahan");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("Harga Layanan harus berupa angka!");
            errorAlert.showAndWait();
            return; //
        }

        dede_layanan layanan = new dede_layanan(idLayanan, namaLayanan, hargaLayananInt);
        try {
            Connection connection = koneksiKeDatabasedede();

            // Periksa apakah ID layanan sudah ada dalam database
            String checkQuery = "SELECT * FROM tlayanan_dede WHERE id_layanan = ?";
            PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
            checkStatement.setString(1, idLayanan);
            ResultSet resultSet = checkStatement.executeQuery();

            if (resultSet.next()) {
                // Jika ID layanan sudah ada, tampilkan peringatan
                Alert duplicateAlert = new Alert(Alert.AlertType.WARNING);
                duplicateAlert.setTitle("Peringatan");
                duplicateAlert.setHeaderText(null);
                duplicateAlert.setContentText("ID Layanan sudah ada dalam database!");
                duplicateAlert.showAndWait();
                resultSet.close();
                checkStatement.close();
                connection.close();
                return;
            }

            resultSet.close();
            checkStatement.close();

            // Jika ID layanan belum ada, lanjutkan dengan penambahan data
            String insertQuery = "INSERT INTO tlayanan_dede (id_layanan, nama_layanan, harga_layanan) VALUES (?, ?, ?)";
            PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
            insertStatement.setString(1, layanan.getId_layanan());
            insertStatement.setString(2, layanan.getNama_layanan());
            insertStatement.setInt(3, layanan.getHarga_layanan());
            insertStatement.executeUpdate();
            insertStatement.close();
            connection.close();

            // Menambahkan data layanan ke ObservableList
            dataLayanan.add(layanan);

            // Mengosongkan field input
            resetForm();

            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Informasi");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Data Layanan berhasil ditambahkan!");
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
        dede_layanan layananTerpilih = tlayanan_dede.getSelectionModel().getSelectedItem();
        if (layananTerpilih != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Konfirmasi");
            alert.setHeaderText(null);
            alert.setContentText("Apakah Anda yakin ingin mengedit data Layanan?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                String idLayanan = txidLY.getText();
                String namaLayanan = txnamaLY.getText();
                String hargaLayanan = txhargaLY.getText();
                layananTerpilih.setId_layanan(idLayanan);
                layananTerpilih.setNama_layanan(namaLayanan);
                layananTerpilih.setHarga_layanan(Integer.parseInt(hargaLayanan));


                try {
                    Connection connection = koneksiKeDatabasedede();
                    String query = "UPDATE tlayanan_dede SET nama_layanan = ?, harga_layanan = ? WHERE id_layanan = ?";
                    PreparedStatement statement = connection.prepareStatement(query);
                    statement.setString(1, layananTerpilih.getNama_layanan());
                    statement.setInt(2, layananTerpilih.getHarga_layanan());
                    statement.setString(3, layananTerpilih.getId_layanan());
                    statement.executeUpdate();
                    statement.close();
                    connection.close();

                    // Memperbarui data layanan di ObservableList
                    tlayanan_dede.refresh();

                    // Mengosongkan field input
                    resetForm();

                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Informasi");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("Data Layanan berhasil diupdate!");
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
            alert.setContentText("Tidak ada layanan yang dipilih!");
            alert.showAndWait();
        }
    }



    @FXML
    public void hapusData(ActionEvent event) {
        dede_layanan layananTerpilih = tlayanan_dede.getSelectionModel().getSelectedItem();
        if (layananTerpilih != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Konfirmasi");
            alert.setHeaderText(null);
            alert.setContentText("Apakah Anda yakin ingin menghapus data Layanan?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    Connection connection = koneksiKeDatabasedede();
                    String query = "DELETE FROM tlayanan_dede WHERE id_layanan = ?";
                    PreparedStatement statement = connection.prepareStatement(query);
                    statement.setString(1, layananTerpilih.getId_layanan());
                    statement.executeUpdate();
                    statement.close();
                    connection.close();

                    // Menghapus data layanan dari ObservableList
                    dataLayanan.remove(layananTerpilih);

                    // Mengosongkan field input
                    resetForm();

                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Informasi");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("Data Layanan berhasil dihapus!");
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
            alert.setContentText("Tidak ada Layanan yang dipilih!");
            alert.showAndWait();
        }
    }


    @FXML
    public void resetData(ActionEvent event) {
        resetForm();
    }

    @FXML
    public void TutupFormPL(ActionEvent event) {
        Stage stage = (Stage) btkeluarLY.getScene().getWindow();
        stage.close();
    }

    private void resetForm() {
        txidLY.clear();
        txnamaLY.clear();
        txhargaLY.clear();
        tlayanan_dede.getSelectionModel().clearSelection();
    }

    private Connection koneksiKeDatabasedede() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/uasservice_dede";
        String username = "root";
        String password = "";
        Connection connection = DriverManager.getConnection(url, username, password);
        System.out.println("Koneksi Berhasil!");
        return connection;
    }
    private FilteredList<dede_layanan> filteredDataLayanan;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Inisialisasi kolom pada TableView
        colidLY.setCellValueFactory(new PropertyValueFactory<>("id_layanan"));
        colnamaLY.setCellValueFactory(new PropertyValueFactory<>("nama_layanan"));
        colhargaLY.setCellValueFactory(new PropertyValueFactory<>("harga_layanan"));

        // Ambil data dari database dan masukkan ke dalam ObservableList
        dataLayanan = FXCollections.observableArrayList();

        try {
            Connection connection = koneksiKeDatabasedede();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM tlayanan_dede");

            while (resultSet.next()) {
                String idLayanan = resultSet.getString("id_layanan");
                String namaLayanan = resultSet.getString("nama_layanan");
                int hargaLayanan = resultSet.getInt("harga_layanan");

                dede_layanan layanan = new dede_layanan(idLayanan, namaLayanan, hargaLayanan);
                dataLayanan.add(layanan);
            }

            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Kesalahan: " + e.getMessage());
        }

        // Set data ke dalam TableView
        tlayanan_dede.setItems(dataLayanan);

        // Inisialisasi FilteredList
        filteredDataLayanan = new FilteredList<>(dataLayanan, p -> true);

        // Menerapkan filter berdasarkan teks pencarian pada txCariLY
        txCariLY.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredDataLayanan.setPredicate(layanan -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                // Sesuaikan dengan atribut dataLayanan yang ingin dijadikan kriteria pencarian
                if (layanan.getId_layanan().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (layanan.getNama_layanan().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(layanan.getHarga_layanan()).contains(lowerCaseFilter)) {
                    return true;
                }

                return false; // Jika tidak ada kriteria yang cocok, data akan disembunyikan
            });
        });

        // Menghubungkan FilteredList dengan TableView
        tlayanan_dede.setItems(filteredDataLayanan);

        // Memanggil isian data ketika tabel diklik
        tlayanan_dede.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                dede_layanan layananTerpilih = newSelection;
                txidLY.setText(layananTerpilih.getId_layanan());
                txnamaLY.setText(layananTerpilih.getNama_layanan());
                txhargaLY.setText(String.valueOf(layananTerpilih.getHarga_layanan()));
            }
        });
    }


    public dede_layanan(String id_layanan, String nama_layanan, int harga_layanan) {
        this.id_layanan = id_layanan;
        this.nama_layanan = nama_layanan;
        this.harga_layanan = harga_layanan;
    }


    // Getters and Setters

    private String id_layanan;

    public String getId_layanan() {
        return id_layanan;
    }

    public void setId_layanan(String id_layanan) {
        this.id_layanan = id_layanan;
    }

    private String nama_layanan;

    public String getNama_layanan() {
        return nama_layanan;
    }

    public void setNama_layanan(String nama_layanan) {
        this.nama_layanan = nama_layanan;
    }

    private int harga_layanan;

    public int getHarga_layanan() {
        return harga_layanan;
    }

    public void setHarga_layanan(int harga_layanan) {
        this.harga_layanan = harga_layanan;
    }

}
