package gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("jbrowser.fxml"));
        stage.setScene(new Scene(root));
//        stage.initStyle(StageStyle.UNDECORATED);
        //TODO: Tirar a decoração da janela e fazer de forma personalizada
        stage.setTitle("JBrowser");
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.setResizable(true);
        stage.show();
    }
}
