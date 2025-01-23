package com.example.browser;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ScreenManager {
    private static ScreenManager instance;
    private static Stage stg;

    private Scene welcomeScene;
    private Scene mainScene;

    private WelcomeController welcomeController;
    private MainController mainController;

    public ScreenManager(){
        this.loadScreens();
    }

    public static ScreenManager getInstance(){
        if(instance == null){
            instance = new ScreenManager();
        }
        return instance;
    }

    // GET/SET Stage
    public static Stage getStg() {
        return stg;
    }
    public static void setStg(Stage stg) {
        ScreenManager.stg = stg;
    }


    // GETs Scenes
    public Scene getWelcomeScene() {
        return welcomeScene;
    }

    public Scene getMainScene() {
        return mainScene;
    }


    // GETs Controllers
    public WelcomeController getWelcomeController() {
        return welcomeController;
    }

    public MainController getMainController() {
        return mainController;
    }

    private void loadScreens(){
        try {
            FXMLLoader welcomeScreenPane = new FXMLLoader(getClass().getResource("initial-window.fxml"));
            this.welcomeScene = new Scene(welcomeScreenPane.load());
            this.welcomeController = welcomeScreenPane.getController();

            FXMLLoader mainScreenPane = new FXMLLoader(getClass().getResource("main-window.fxml"));
            this.mainScene = new Scene(mainScreenPane.load());
            this.mainController = mainScreenPane.getController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void chanceScene(String fxml, String title){
        boolean max = stg.isMaximized();
        if(max) stg.setMaximized(false);
        switch (fxml){
            case "initial-window.fxml" -> stg.setScene(welcomeScene);
            case "main-window.fxml" -> stg.setScene(mainScene);
        }
        stg.setTitle(title);
        if(max) stg.setMaximized(true);
    }

}
