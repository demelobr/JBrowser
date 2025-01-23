package com.example.browser;

import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebHistory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    private boolean isLightMode = true;
    private List<String> favorites;

    @FXML
    private Button btnArrowLeft;

    @FXML
    private Button btnArrowRight;

    @FXML
    private Button btnClear;

    @FXML
    private Button btnFavorite;

    @FXML
    private Button btnMenu;

    @FXML
    private Button btnMode;

    @FXML
    private Button btnNewTab;

    @FXML
    private Button btnReload;

    @FXML
    private ImageView imgFavorite;

    @FXML
    private ImageView imgMode;

    @FXML
    private BorderPane parent;

    @FXML
    private ProgressBar pbProgressBar;

    @FXML
    private TextField tfUrlBar;

    @FXML
    private TabPane tpWindow;

    public MainController() {
        favorites = new ArrayList<>();
    }

    @FXML
    private void back(){
        JBrowserTab selectedTab = (JBrowserTab) (tpWindow.getSelectionModel().getSelectedItem());
        selectedTab.setHistory(selectedTab.getView().getEngine().getHistory());

        if (selectedTab.getHistory().getCurrentIndex() > 0) {
            selectedTab.getHistory().go(-1);
            ObservableList<WebHistory.Entry> entries = selectedTab.getHistory().getEntries();
            tfUrlBar.setText(entries.get(selectedTab.getHistory().getCurrentIndex()).getUrl());
            btnClear.setVisible(true);
        }
    }

    @FXML
    private void forward(){
        JBrowserTab selectedTab = (JBrowserTab) (tpWindow.getSelectionModel().getSelectedItem());
        selectedTab.setHistory(selectedTab.getView().getEngine().getHistory());

        if (selectedTab.getHistory().getCurrentIndex() < selectedTab.getHistory().getEntries().size() - 1) {
            selectedTab.getHistory().go(1);
            ObservableList<WebHistory.Entry> entries = selectedTab.getHistory().getEntries();
            tfUrlBar.setText(entries.get(selectedTab.getHistory().getCurrentIndex()).getUrl());
            btnClear.setVisible(true);
        }
    }

    @FXML
    private void reload(){
        JBrowserTab selectedTab = (JBrowserTab) (tpWindow.getSelectionModel().getSelectedItem());
        selectedTab.getView().getEngine().reload();
        if(favorites.contains(tfUrlBar.getText())) imgFavorite.setImage(new Image("file:/"+System.getProperty("user.dir").replace("\\", "/")+"/src/main/resources/com/example/browser/star-blue-fill.png"));
        else imgFavorite.setImage(new Image("file:/"+System.getProperty("user.dir").replace("\\", "/")+"/src/main/resources/com/example/browser/star-white.png"));
    }

    @FXML
    private void textEnterKey(KeyEvent event){
        if(event.getCode() == KeyCode.ENTER){
            loadUrl((JBrowserTab) tpWindow.getSelectionModel().getSelectedItem(),tfUrlBar.getText());
        }
    }

    @FXML
    private void clear(){
        tfUrlBar.setText("");
        btnClear.setVisible(false);
        imgFavorite.setImage(new Image("file:/"+System.getProperty("user.dir").replace("\\", "/")+"/src/main/resources/com/example/browser/star-white.png"));
        btnFavorite.setVisible(false);
    }

    @FXML
    private void favorite(){
        String currentUrl = tfUrlBar.getText();
        System.out.println(favorites.contains(currentUrl));
        if(!favorites.contains(currentUrl)){
            imgFavorite.setImage(new Image("file:/"+System.getProperty("user.dir").replace("\\", "/")+"/src/main/resources/com/example/browser/star-blue-fill.png"));
            favorite(currentUrl);
        }else{
            imgFavorite.setImage(new Image("file:/"+System.getProperty("user.dir").replace("\\", "/")+"/src/main/resources/com/example/browser/star-white.png"));
            unfavorite(currentUrl);
        }
        System.out.println(favorites);
    }

    @FXML
    private void addNewTab(){
        JBrowserTab newTab = new JBrowserTab("New tab");
        loadUrl(newTab, "google.com");
        tpWindow.getTabs().add(newTab);
        tpWindow.getSelectionModel().select(newTab);
    }

    @FXML
    public void changeMode(){
        isLightMode = !isLightMode;
        if(isLightMode){
            setLightMode();
        }else{
            setDarkMode();
        }
    }

    @FXML
    public void menu(){
        System.out.println("menu");
    }

    private void loadUrl(JBrowserTab newTab, String url){
        newTab.getView().getEngine().load("https://"+url);
        pbProgressBar.progressProperty().bind(newTab.getView().getEngine().getLoadWorker().progressProperty());

        newTab.getView().getEngine().getLoadWorker().stateProperty().addListener((obs, oldValue, newValue) -> {
            if(newValue == Worker.State.SUCCEEDED){
                newTab.setHistory(newTab.getView().getEngine().getHistory());
                ObservableList<WebHistory.Entry> entries = newTab.getHistory().getEntries();
                tfUrlBar.setText(entries.get(newTab.getHistory().getCurrentIndex()).getUrl());
                newTab.setText(entries.get(newTab.getHistory().getCurrentIndex()).getTitle());
                if(favorites.contains(tfUrlBar.getText())) imgFavorite.setImage(new Image("file:/"+System.getProperty("user.dir").replace("\\", "/")+"/src/main/resources/com/example/browser/star-blue-fill.png"));
                else imgFavorite.setImage(new Image("file:/"+System.getProperty("user.dir").replace("\\", "/")+"/src/main/resources/com/example/browser/star-white.png"));
                btnClear.setVisible(true);
                btnFavorite.setVisible(true);
            }else if(newValue == Worker.State.FAILED){
                System.out.println("Loading failed");
            }
        });
    }

    private void favorite(String url){
        favorites.add(url);
    }

    private void unfavorite(String url){
        favorites.remove(url);
    }

    private void setLightMode(){
        String darkModePath = getClass().getResource("/com/example/browser/darkMode.css").toExternalForm();
        String lightModePath = getClass().getResource("/com/example/browser/lightMode.css").toExternalForm();
        parent.getStylesheets().remove(lightModePath);
        parent.getStylesheets().add(darkModePath);
        Image image = new Image("file:/"+System.getProperty("user.dir").replace("\\", "/")+"/src/main/resources/com/example/browser/sun-white.png");
        imgMode.setImage(image);
    }

    private void setDarkMode(){
        String darkModePath = getClass().getResource("/com/example/browser/darkMode.css").toExternalForm();
        String lightModePath = getClass().getResource("/com/example/browser/lightMode.css").toExternalForm();
        parent.getStylesheets().remove(darkModePath);
        parent.getStylesheets().add(lightModePath);
        Image image = new Image("file:/"+System.getProperty("user.dir").replace("\\", "/")+"/src/main/resources/com/example/browser/moon-white.png");
        imgMode.setImage(image);
    }

    public void setData(boolean isLightMode){
        this.isLightMode = isLightMode;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tfUrlBar.setPromptText("Enter your url here!");
        tfUrlBar.setOnKeyReleased((KeyEvent) -> {
            if(tfUrlBar.getText().isEmpty()){
                btnClear.setVisible(false);
                btnFavorite.setVisible(false);
            }else{
                btnClear.setVisible(true);
                btnFavorite.setVisible(true);
                if(favorites.contains(tfUrlBar.getText())) imgFavorite.setImage(new Image("file:/"+System.getProperty("user.dir").replace("\\", "/")+"/src/main/resources/com/example/browser/star-blue-fill.png"));
                else imgFavorite.setImage(new Image("file:/"+System.getProperty("user.dir").replace("\\", "/")+"/src/main/resources/com/example/browser/star-white.png"));
            }
        });

        tpWindow.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, selectTab) -> {
            if (selectTab != null) {
                selectTab.setText(((JBrowserTab)selectTab).getView().getEngine().getTitle());
                tfUrlBar.setText(((JBrowserTab)selectTab).getView().getEngine().getLocation());
                if(favorites.contains(tfUrlBar.getText())) imgFavorite.setImage(new Image("file:/"+System.getProperty("user.dir").replace("\\", "/")+"/src/main/resources/com/example/browser/star-blue-fill.png"));
                else imgFavorite.setImage(new Image("file:/"+System.getProperty("user.dir").replace("\\", "/")+"/src/main/resources/com/example/browser/star-white.png"));
            }
        });

        JBrowserTab newTab = new JBrowserTab("New tab");
        loadUrl(newTab, "google.com");
        tpWindow.getTabs().add(newTab);
        tpWindow.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);

        btnClear.setVisible(true);
        if(isLightMode){
            setLightMode();
        }else{
            setDarkMode();
        }
    }
}
