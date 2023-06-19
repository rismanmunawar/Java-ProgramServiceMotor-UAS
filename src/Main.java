import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stageUtama) throws Exception{
        Parent sceneUtama = FXMLLoader.load(getClass().getResource("dede_menuservice.fxml"));
        stageUtama.setTitle("UAS JAVA Dede Munawar Risman");
        stageUtama.setScene(new Scene(sceneUtama));
        stageUtama.show();
    }
    public static void main(String[] args){
        launch(args);
    }
}