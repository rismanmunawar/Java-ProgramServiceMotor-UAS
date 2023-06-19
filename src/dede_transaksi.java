import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert.AlertType;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

public class dede_transaksi implements Initializable {

    @FXML
    private TableView<dede_transaksi> ttransaksi_dede;
    @FXML
    private TableColumn<dede_transaksi, String> colidTR;
    @FXML
    private TableColumn<dede_transaksi, LocalDate> coldateTR;
    @FXML
    private TableColumn<dede_transaksi, String> colnamaPL;
    @FXML
    private TableColumn<dede_transaksi, String> colnamaLY;
    @FXML
    private TableColumn<dede_transaksi, String> colnamaMK;
    @FXML
    private TableColumn<dede_transaksi, String> coltotalbiaya;
    @FXML
    private Button bttambahTR;
    @FXML
    private Button bteditTR;
    @FXML
    private Button bthapusTR;
    @FXML
    private Button btresetTR;
    @FXML
    private Button btKeluar;
    @FXML
    private TextField txTotal;
    @FXML
    private TextField txCariTR;
    @FXML
    private ComboBox<String> cbnamaPelanggan;
    @FXML
    private ComboBox<String> cbnamaMekanik;
    @FXML
    private ComboBox<String> cdnamaLayanan;
    @FXML
    private DatePicker dateTR;
    @FXML
    private TextField txidTR;

    private ObservableList<dede_transaksi> dataTransaksi;

    public dede_transaksi() {
    }

    public dede_transaksi(String idTransaksi, LocalDate tanggalTransaksi, String idPelanggan, String idMekanik, String idLayanan, String totalBiaya) {
        this.idTransaksi = idTransaksi;
        this.tanggalTransaksi = tanggalTransaksi;
        this.idPelanggan = idPelanggan;
        this.idMekanik = idMekanik;
        this.idLayanan = idLayanan;
        this.totalBiaya = totalBiaya;
    }
    private FilteredList<dede_transaksi> filteredDataTransaksi;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Ambil data dari database dan masukkan ke dalam ObservableList
        dataTransaksi = FXCollections.observableArrayList();

        try {
            Connection connection = koneksiKeDatabasedede();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM ttransaksi_dede");

            while (resultSet.next()) {
                String idTransaksi = resultSet.getString("idTransaksi");
                LocalDate tanggalTransaksi = resultSet.getDate("tanggalTransaksi").toLocalDate();
                String idPelanggan = resultSet.getString("idPelanggan");
                String idMekanik = resultSet.getString("idMekanik");
                String idLayanan = resultSet.getString("idLayanan");
                String totalBiaya = resultSet.getString("totalBiaya");

                dede_transaksi transaksi = new dede_transaksi(idTransaksi, tanggalTransaksi, idPelanggan, idMekanik, idLayanan, totalBiaya);
                dataTransaksi.add(transaksi);
            }

            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Kesalahan: " + e.getMessage());
        }

        // Inisialisasi FilteredList
        filteredDataTransaksi = new FilteredList<>(dataTransaksi, p -> true);

        // Menghubungkan FilteredList dengan TableView
        ttransaksi_dede.setItems(filteredDataTransaksi);

        // Menerapkan filter berdasarkan teks pencarian pada txCariTR
        txCariTR.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredDataTransaksi.setPredicate(transaksi -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                // Sesuaikan dengan atribut dataTransaksi yang ingin dijadikan kriteria pencarian
                if (transaksi.getIdTransaksi().toLowerCase().contains(lowerCaseFilter) ||
                        transaksi.getIdPelanggan().toLowerCase().contains(lowerCaseFilter) ||
                        transaksi.getIdMekanik().toLowerCase().contains(lowerCaseFilter) ||
                        transaksi.getIdLayanan().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }

                return false; // Jika tidak ada kriteria yang cocok, data akan disembunyikan
            });
        });

        // Inisialisasi kolom pada TableView
        colidTR.setCellValueFactory(new PropertyValueFactory<>("idTransaksi"));
        coldateTR.setCellValueFactory(new PropertyValueFactory<>("tanggalTransaksi"));
        colnamaPL.setCellValueFactory(new PropertyValueFactory<>("idPelanggan"));
        colnamaMK.setCellValueFactory(new PropertyValueFactory<>("idMekanik"));
        colnamaLY.setCellValueFactory(new PropertyValueFactory<>("idLayanan"));
        coltotalbiaya.setCellValueFactory(new PropertyValueFactory<>("totalBiaya"));

        // Mengisi ComboBox dengan data dari tabel tpelanggan_dede
        ObservableList<String> namaPelangganList = FXCollections.observableArrayList();
        try {
            Connection connection = koneksiKeDatabasedede();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT nama_pelanggan FROM tpelanggan_dede");

            while (resultSet.next()) {
                String namaPelanggan = resultSet.getString("nama_pelanggan");
                namaPelangganList.add(namaPelanggan);
            }

            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Kesalahan: " + e.getMessage());
        }
        cbnamaPelanggan.setItems(namaPelangganList);

        // Mengisi ComboBox dengan data dari tabel tmekanik_dede
        ObservableList<String> namaMekanikList = FXCollections.observableArrayList();
        try {
            Connection connection = koneksiKeDatabasedede();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT nama_mekanik FROM tmekanik_dede");

            while (resultSet.next()) {
                String namaMekanik = resultSet.getString("nama_mekanik");
                namaMekanikList.add(namaMekanik);
            }

            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Kesalahan: " + e.getMessage());
        }
        cbnamaMekanik.setItems(namaMekanikList);

        // Mengisi ComboBox dengan data dari tabel tlayanan_dede
        ObservableList<String> namaLayananList = FXCollections.observableArrayList();
        try {
            Connection connection = koneksiKeDatabasedede();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT nama_layanan FROM tlayanan_dede");

            while (resultSet.next()) {
                String namaLayanan = resultSet.getString("nama_layanan");
                namaLayananList.add(namaLayanan);
            }

            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Kesalahan: " + e.getMessage());
        }
        cdnamaLayanan.setItems(namaLayananList);

        // Memanggil isian data ketika tabel diklik
        ttransaksi_dede.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                dede_transaksi transaksiTerpilih = newSelection;
                txidTR.setText(transaksiTerpilih.getIdTransaksi());
                dateTR.setValue(transaksiTerpilih.getTanggalTransaksi());
                cbnamaPelanggan.setValue(transaksiTerpilih.getIdPelanggan());
                cbnamaMekanik.setValue(transaksiTerpilih.getIdMekanik());
                cdnamaLayanan.setValue(transaksiTerpilih.getIdLayanan());
                txTotal.setText(transaksiTerpilih.getTotalBiaya());
            }
        });

        // Menambahkan listener untuk ComboBox cdnamaLayanan
        cdnamaLayanan.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                String selectedLayanan = newSelection;

                try {
                    Connection connection = koneksiKeDatabasedede();
                    String query = "SELECT harga_layanan FROM tlayanan_dede WHERE nama_layanan = ?";
                    PreparedStatement statement = connection.prepareStatement(query);
                    statement.setString(1, selectedLayanan);
                    ResultSet resultSet = statement.executeQuery();

                    if (resultSet.next()) {
                        String hargaLayanan = resultSet.getString("harga_layanan");
                        txTotal.setText(hargaLayanan);
                    }

                    statement.close();
                    connection.close();
                } catch (SQLException e) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Kesalahan");
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText("Terjadi kesalahan: " + e.getMessage());
                    errorAlert.showAndWait();
                }
            }
        });
    }

    public void TambahData(ActionEvent actionEvent) {
        String idTransaksi = txidTR.getText();
        LocalDate tanggalTransaksi = dateTR.getValue();
        String idPelanggan = cbnamaPelanggan.getValue();
        String idMekanik = cbnamaMekanik.getValue();
        String idLayanan = cdnamaLayanan.getValue();
        String totalBiaya = txTotal.getText();

        // Validasi field-field kosong
        if (idTransaksi.isEmpty() || tanggalTransaksi == null || idPelanggan == null || idMekanik == null || idLayanan == null || totalBiaya.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Field kosong");
            alert.setContentText("Mohon lengkapi semua field sebelum menambahkan data.");
            alert.showAndWait();
            return;
        }

        try {
            Connection connection = koneksiKeDatabasedede();
            String query = "INSERT INTO ttransaksi_dede (idTransaksi, tanggalTransaksi, idPelanggan, idMekanik, idLayanan, totalBiaya) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, idTransaksi);
            statement.setDate(2, Date.valueOf(tanggalTransaksi));
            statement.setString(3, idPelanggan);
            statement.setString(4, idMekanik);
            statement.setString(5, idLayanan);
            statement.setString(6, totalBiaya);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Informasi");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Data Transaksi berhasil ditambahkan!");
                successAlert.showAndWait();
                dede_transaksi transaksi = new dede_transaksi(idTransaksi, tanggalTransaksi, idPelanggan, idMekanik, idLayanan, totalBiaya);
                dataTransaksi.add(transaksi);
                ttransaksi_dede.setItems(dataTransaksi);
                ResetForm();
            }

            statement.close();
            connection.close();
        } catch (SQLException e) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Kesalahan");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("Terjadi kesalahan: " + e.getMessage());
            errorAlert.showAndWait();
        }
    }


    public void EditData(ActionEvent actionEvent) {
        dede_transaksi transaksiTerpilih = ttransaksi_dede.getSelectionModel().getSelectedItem();

        // Validasi data terpilih
        if (transaksiTerpilih == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Data tidak dipilih");
            alert.setContentText("Mohon pilih data yang ingin diubah.");
            alert.showAndWait();
            return;
        }

        // Konfirmasi pengeditan
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi");
        alert.setHeaderText("Edit Data");
        alert.setContentText("Apakah Anda yakin ingin mengedit data ini?");

        ButtonType buttonTypeYes = new ButtonType("Ya");
        ButtonType buttonTypeNo = new ButtonType("Tidak");

        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        // Menunggu pengguna memilih tombol Ya atau Tidak
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == buttonTypeYes) {
            String idTransaksi = txidTR.getText();
            LocalDate tanggalTransaksi = dateTR.getValue();
            String idPelanggan = cbnamaPelanggan.getValue();
            String idMekanik = cbnamaMekanik.getValue();
            String idLayanan = cdnamaLayanan.getValue();
            String totalBiaya = txTotal.getText();

            try {
                Connection connection = koneksiKeDatabasedede();
                String query = "UPDATE ttransaksi_dede SET idTransaksi = ?, tanggalTransaksi = ?, idPelanggan = ?, idMekanik = ?, idLayanan = ?, totalBiaya = ? WHERE idTransaksi = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, idTransaksi);
                statement.setDate(2, Date.valueOf(tanggalTransaksi));
                statement.setString(3, idPelanggan);
                statement.setString(4, idMekanik);
                statement.setString(5, idLayanan);
                statement.setString(6, totalBiaya);
                statement.setString(7, transaksiTerpilih.getIdTransaksi());

                int rowsUpdated = statement.executeUpdate();
                if (rowsUpdated > 0) {
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Informasi");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("Data Transaksi berhasil diupdate!");
                    successAlert.showAndWait();
                    transaksiTerpilih.setIdTransaksi(idTransaksi);
                    transaksiTerpilih.setTanggalTransaksi(tanggalTransaksi);
                    transaksiTerpilih.setIdPelanggan(idPelanggan);
                    transaksiTerpilih.setIdMekanik(idMekanik);
                    transaksiTerpilih.setIdLayanan(idLayanan);
                    transaksiTerpilih.setTotalBiaya(totalBiaya);
                    ttransaksi_dede.refresh();
                    ResetForm();
                }

                statement.close();
                connection.close();
            } catch (SQLException e) {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Kesalahan");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("Terjadi kesalahan: " + e.getMessage());
                errorAlert.showAndWait();
            }
        }
    }

    public void HapusData(ActionEvent actionEvent) {
        dede_transaksi transaksiTerpilih = ttransaksi_dede.getSelectionModel().getSelectedItem();

        // Validasi data terpilih
        if (transaksiTerpilih == null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Data tidak dipilih");
            alert.setContentText("Mohon pilih data yang ingin dihapus.");
            alert.showAndWait();
            return;
        }

        // Konfirmasi penghapusan
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi");
        alert.setHeaderText("Hapus Data");
        alert.setContentText("Apakah Anda yakin ingin menghapus data ini?");

        ButtonType buttonTypeYes = new ButtonType("Ya");
        ButtonType buttonTypeNo = new ButtonType("Tidak");

        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        // Menunggu pengguna memilih tombol Ya atau Tidak
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == buttonTypeYes) {
            try {
                Connection connection = koneksiKeDatabasedede();
                String query = "DELETE FROM ttransaksi_dede WHERE idTransaksi = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, transaksiTerpilih.getIdTransaksi());

                int rowsDeleted = statement.executeUpdate();
                if (rowsDeleted > 0) {
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Informasi");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("Data Transaksi berhasil dihapus!");
                    successAlert.showAndWait();
                    dataTransaksi.remove(transaksiTerpilih);
                    ttransaksi_dede.setItems(dataTransaksi);
                    ResetForm();
                }

                statement.close();
                connection.close();
            } catch (SQLException e) {
                System.out.println("Kesalahan: " + e.getMessage());
            }
        }
    }

    public void ResetForm() {
        txidTR.setText("");
        dateTR.setValue(null);
        cbnamaPelanggan.setValue(null);
        cbnamaMekanik.setValue(null);
        cdnamaLayanan.setValue(null);
        txTotal.setText("");
    }

    public void CetakData(ActionEvent actionEvent) {
        JasperPrint jp;
        Map param = new HashMap<>();
        try {
            jp = JasperFillManager.fillReport("Report/ReportTransaksi.jasper", param, koneksiKeDatabasedede());
            JasperViewer jv = new JasperViewer(jp, false);
            jv.setTitle("Cetak Data Transaksi");
            jv.setVisible(true);
        } catch (JRException e) {
            System.out.println(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Connection koneksiKeDatabasedede() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/uasservice_dede";
        String username = "root";
        String password = "";
        return DriverManager.getConnection(url, username, password);
    }

    // Getter dan setter (termasuk konstruktor) untuk kelas dede_transaksi

    private String idTransaksi;
    private LocalDate tanggalTransaksi;
    private String idPelanggan;
    private String idLayanan;
    private String idMekanik;
    private String totalBiaya;

    public String getIdTransaksi() {
        return idTransaksi;
    }

    public void setIdTransaksi(String idTransaksi) {
        this.idTransaksi = idTransaksi;
    }

    public LocalDate getTanggalTransaksi() {
        return tanggalTransaksi;
    }

    public void setTanggalTransaksi(LocalDate tanggalTransaksi) {
        this.tanggalTransaksi = tanggalTransaksi;
    }

    public String getIdPelanggan() {
        return idPelanggan;
    }

    public void setIdPelanggan(String idPelanggan) {
        this.idPelanggan = idPelanggan;
    }

    public String getIdLayanan() {
        return idLayanan;
    }
    public String getIdMekanik() {
        return idMekanik;
    }

    public void setIdLayanan(String idLayanan) {
        this.idLayanan = idLayanan;
    }
    public void setIdMekanik(String idMekanik) {
        this.idMekanik = idMekanik;
    }

    public String getTotalBiaya() {
        return totalBiaya;
    }

    public void setTotalBiaya(String totalBiaya) {
        this.totalBiaya = totalBiaya;
    }
}
