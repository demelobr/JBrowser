package com.example.browser;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class WelcomeController implements Initializable {

    private boolean isLightMode = true;

    @FXML
    private Button btnStart;

    @FXML
    private Button btnDark;

    @FXML
    private Button btnLight;

    @FXML
    private Text lbWelcome;

    @FXML
    private BorderPane parent;

    @FXML
    public void startBrowser(){
        ScreenManager sm = ScreenManager.getInstance();
        sm.getMainController().setData(isLightMode);
        sm.getMainController().changeMode();
        sm.chanceScene("main-window.fxml", "JBrowser!");
    }

    @FXML
    public void selectDarkMode(){
        if(isLightMode){
            btnDark.setDisable(true);
            btnLight.setDisable(false);
            this.setDarkMode();
            isLightMode = false;
        }
    }

    @FXML
    public void selectLightMode(){
        if(!isLightMode){
            btnLight.setDisable(true);
            btnDark.setDisable(false);
            this.setLightMode();
            isLightMode = true;
        }
    }

    private void setDarkMode(){
        String darkModePath = getClass().getResource("/com/example/browser/darkMode.css").toExternalForm();
        String lightModePath = getClass().getResource("/com/example/browser/lightMode.css").toExternalForm();
        parent.getStylesheets().remove(lightModePath);
        parent.getStylesheets().add(darkModePath);
        btnDark.getStyleClass().add("buttons-action");
        btnDark.getStyleClass().remove("buttons-disable");
        btnLight.getStyleClass().add("buttons-disable");
        btnLight.getStyleClass().remove("buttons-action");
        lbWelcome.setFill(Color.WHITE);
    }

    private void setLightMode(){
        String darkModePath = getClass().getResource("/com/example/browser/darkMode.css").toExternalForm();
        String lightModePath = getClass().getResource("/com/example/browser/lightMode.css").toExternalForm();
        parent.getStylesheets().remove(darkModePath);
        parent.getStylesheets().add(lightModePath);
        btnLight.getStyleClass().add("buttons-action");
        btnLight.getStyleClass().remove("buttons-disable");
        btnDark.getStyleClass().add("buttons-disable");
        btnDark.getStyleClass().remove("buttons-action");
        lbWelcome.setFill(Color.BLACK);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnLight.setDisable(true);
        btnDark.setDisable(false);
    }
}