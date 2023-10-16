package gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("jbrowser.fxml"));
        Parent root = loader.load();
        JBrowserController controller = loader.getController();
        controller.setStg(stage);
        stage.setScene(new Scene(root));
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("JBrowser");
//        stage.getIcons().add(new Image("file:" + System.getProperty("user.dir").replace("/","\\") + ""));
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.setResizable(true);
        stage.show();
    }
}
