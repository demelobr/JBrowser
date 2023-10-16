package gui;

import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebHistory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class JBrowserController implements Initializable {
    private Stage stg;
    private boolean isLightMode = true;
    private boolean isFavorite = false;
    private double xOffset = 0;
    private double yOffset = 0;
    private List<String> favorites;

    @FXML
    private Button btnArrowLeft;

    @FXML
    private Button btnArrowRight;

    @FXML
    private Button btnClear;

    @FXML
    private Button btnClose;

    @FXML
    private Button btnFavorite;

    @FXML
    private Button btnMaximize;

    @FXML
    private Button btnMinimize;

    @FXML
    private Button btnMode;

    @FXML
    private Button btnNewTab;

    @FXML
    private Button btnReload;

    @FXML
    private HBox hbWindowBar;

    @FXML
    private ImageView imgClose;

    @FXML
    private ImageView imgFavorite;

    @FXML
    private ImageView imgMaximize;

    @FXML
    private ImageView imgMinimize;

    @FXML
    private ImageView imgMode;

    @FXML
    private Label lbCredits;

    @FXML
    private Label lbWindowTitle;

    @FXML
    private BorderPane parent;

    @FXML
    private ProgressBar pbProgressBar;

    @FXML
    private TextField tfUrlBar;

    @FXML
    private TabPane tpWindow;

    public JBrowserController(){
        favorites = this.loadFavorites();
    }

    public void setStg(Stage stg) {
        this.stg = stg;
    }

    @FXML
    private void close(ActionEvent event){
        this.savaFavorites();
        stg.close();
    }

    @FXML
    private void minimaze(ActionEvent event){
        stg.setIconified(true);
    }

    @FXML
    private void maxmize(ActionEvent event){
        stg.setMaximized(!stg.isMaximized());
        if(!stg.isMaximized()) imgMaximize.setImage(new Image("file:"+System.getProperty("user.dir").replace("\\", "/")+"/JBrowser/src/main/resources/gui/images/expand-svgrepo-com.png"));
        else imgMaximize.setImage(new Image("file:"+System.getProperty("user.dir").replace("\\", "/")+"/JBrowser/src/main/resources/gui/images/shrink-svgrepo-com.png"));
    }

    @FXML
    private void back(ActionEvent event){
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
    private void forward(ActionEvent event){
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
    private void reload(ActionEvent event){
        JBrowserTab selectedTab = (JBrowserTab) (tpWindow.getSelectionModel().getSelectedItem());
        selectedTab.getView().getEngine().reload();
        if(favorites.contains(tfUrlBar.getText())) imgFavorite.setImage(new Image("file:/"+System.getProperty("user.dir").replace("\\", "/")+"/JBrowser/src/main/resources/gui/images/star-blue-fill.png"));
        else imgFavorite.setImage(new Image("file:/"+System.getProperty("user.dir").replace("\\", "/")+"/JBrowser/src/main/resources/gui/images/star-white.png"));
    }

    @FXML
    private void textEnterKey(KeyEvent event){
        if(event.getCode() == KeyCode.ENTER){
            loadUrl((JBrowserTab) tpWindow.getSelectionModel().getSelectedItem(),tfUrlBar.getText());
        }
    }

    @FXML
    private void clear(ActionEvent event){
        tfUrlBar.setText("");
        btnClear.setVisible(false);
        imgFavorite.setImage(new Image("file:/"+System.getProperty("user.dir").replace("\\", "/")+"/JBrowser/src/main/resources/gui/images/star-white.png"));
        btnFavorite.setVisible(false);
    }

    @FXML
    private void favorite(ActionEvent event){
        isFavorite = !isFavorite;
        if(isFavorite){
            imgFavorite.setImage(new Image("file:/"+System.getProperty("user.dir").replace("\\", "/")+"/JBrowser/src/main/resources/gui/images/star-blue-fill.png"));
            favorite(tfUrlBar.getText());
        }else{
            imgFavorite.setImage(new Image("file:/"+System.getProperty("user.dir").replace("\\", "/")+"/JBrowser/src/main/resources/gui/images/star-white.png"));
            unfavorite(tfUrlBar.getText());
        }
        System.out.println(favorites);
    }

    @FXML
    private void addNewTab(ActionEvent event){
        JBrowserTab newTab = new JBrowserTab("New tab");
        loadUrl(newTab, "duckduckgo.com");
        tpWindow.getTabs().add(newTab);
        tpWindow.getSelectionModel().select(newTab);
    }

    @FXML
    private void zoomIn(ActionEvent event){
        JBrowserTab selectedTab = (JBrowserTab) (tpWindow.getSelectionModel().getSelectedItem());
        selectedTab.getView().setZoom(selectedTab.getView().getZoom() + 0.10);
    }

    @FXML
    private void zoomOut(ActionEvent event){
        JBrowserTab selectedTab = (JBrowserTab) (tpWindow.getSelectionModel().getSelectedItem());
        selectedTab.getView().setZoom(selectedTab.getView().getZoom() - 0.10);
    }

    private void loadUrl(JBrowserTab newTab, String url){
        newTab.getView().getEngine().load("https://"+url);
        pbProgressBar.progressProperty().bind(newTab.getView().getEngine().getLoadWorker().progressProperty());

        newTab.getView().getEngine().getLoadWorker().stateProperty().addListener((obs, oldValue, newValue) -> {
            if(newValue == Worker.State.SUCCEEDED){
                System.out.println("Page has been loaded!");
                newTab.setHistory(newTab.getView().getEngine().getHistory());
                ObservableList<WebHistory.Entry> entries = newTab.getHistory().getEntries();
                tfUrlBar.setText(entries.get(newTab.getHistory().getCurrentIndex()).getUrl());
                lbWindowTitle.setText(entries.get(newTab.getHistory().getCurrentIndex()).getTitle());
                newTab.setText(entries.get(newTab.getHistory().getCurrentIndex()).getTitle());
                if(favorites.contains(tfUrlBar.getText())) imgFavorite.setImage(new Image("file:/"+System.getProperty("user.dir").replace("\\", "/")+"/JBrowser/src/main/resources/gui/images/star-blue-fill.png"));
                else imgFavorite.setImage(new Image("file:/"+System.getProperty("user.dir").replace("\\", "/")+"/JBrowser/src/main/resources/gui/images/star-white.png"));
                btnClear.setVisible(true);
                btnFavorite.setVisible(true);
            }else if(newValue == Worker.State.FAILED){
                System.out.println("Loading failed");
            }
        });
    }

    private List<String> loadFavorites(){
        List<String> favorites = new ArrayList<>();
        //TODO LER OS FAVORITOS DO ARQUIVO "favorites.bin" E CARREGAR EM MEMÓRIA
        return favorites;
    }

    private void savaFavorites(){
        //TODO SALVAR A LISTA "favorites" NO ARQUIVO "favorites.bin"
        System.out.println("Favorites saved!");
    }

    private void favorite(String url){
        favorites.add(url);
    }

    private void unfavorite(String url){
        favorites.remove(url);
    }

    public void changeMode(ActionEvent event){
        isLightMode = !isLightMode;
        if(isLightMode){
            setLightMode();
        }else{
            setDarkMode();
        }
    }

    private void setLightMode(){
        parent.getStylesheets().remove("file:/"+System.getProperty("user.dir").replace("\\", "/")+"/JBrowser/src/main/resources/gui/style/darkMode.css");
        parent.getStylesheets().add("file:/"+System.getProperty("user.dir").replace("\\", "/")+"/JBrowser/src/main/resources/gui/style/lightMode.css");
        Image image = new Image("file:/"+System.getProperty("user.dir").replace("\\", "/")+"/JBrowser/src/main/resources/gui/images/moon-white.png");
        imgMode.setImage(image);
        lbWindowTitle.setStyle("-fx-text-fill: #282828;");
        lbCredits.setStyle("-fx-text-fill: #282828;");
    }

    private void setDarkMode(){
        parent.getStylesheets().remove("file:/"+System.getProperty("user.dir").replace("\\", "/")+"/JBrowser/src/main/resources/gui/style/lightMode.css");
        parent.getStylesheets().add("file:/"+System.getProperty("user.dir").replace("\\", "/")+"/JBrowser/src/main/resources/gui/style/darkMode.css");
        Image image = new Image("file:/"+System.getProperty("user.dir").replace("\\", "/")+"/JBrowser/src/main/resources/gui/images/sun-white.png");
        imgMode.setImage(image);
        lbWindowTitle.setStyle("-fx-text-fill: #f8f8f8;");
        lbCredits.setStyle("-fx-text-fill: #f8f8f8;");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        imgClose.setVisible(false);
        imgMinimize.setVisible(false);
        imgMaximize.setVisible(false);

        hbWindowBar.setOnMousePressed((event) -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        hbWindowBar.setOnMouseDragged((event) -> {
            stg.setX(event.getScreenX() - xOffset);
            stg.setY(event.getScreenY() - yOffset);
        });
        tfUrlBar.setPromptText("Enter your url here!");
        tfUrlBar.setOnKeyReleased((KeyEvent) -> {
            if(tfUrlBar.getText().isEmpty()){
                btnClear.setVisible(false);
                btnFavorite.setVisible(false);
            }else{
                btnClear.setVisible(true);
                btnFavorite.setVisible(true);
                if(favorites.contains(tfUrlBar.getText())) imgFavorite.setImage(new Image("file:/"+System.getProperty("user.dir").replace("\\", "/")+"/JBrowser/src/main/resources/gui/images/star-blue-fill.png"));
                else imgFavorite.setImage(new Image("file:/"+System.getProperty("user.dir").replace("\\", "/")+"/JBrowser/src/main/resources/gui/images/star-white.png"));
            }
        });
        btnClose.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                imgClose.setVisible(true);
            }
        });
        btnClose.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                imgClose.setVisible(false);
            }
        });
        btnMinimize.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                imgMinimize.setVisible(true);
            }
        });
        btnMinimize.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                imgMinimize.setVisible(false);
            }
        });
        btnMaximize.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                imgMaximize.setVisible(true);
            }
        });
        btnMaximize.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                imgMaximize.setVisible(false);
            }
        });
        tpWindow.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, selectTab) -> {
            if (selectTab != null) {
                lbWindowTitle.setText(((JBrowserTab)selectTab).getView().getEngine().getTitle());
                selectTab.setText(((JBrowserTab)selectTab).getView().getEngine().getTitle());
                tfUrlBar.setText(((JBrowserTab)selectTab).getView().getEngine().getLocation());
                if(favorites.contains(tfUrlBar.getText())) imgFavorite.setImage(new Image("file:/"+System.getProperty("user.dir").replace("\\", "/")+"/JBrowser/src/main/resources/gui/images/star-blue-fill.png"));
                else imgFavorite.setImage(new Image("file:/"+System.getProperty("user.dir").replace("\\", "/")+"/JBrowser/src/main/resources/gui/images/star-white.png"));
            }
        });

        JBrowserTab newTab = new JBrowserTab("New tab");
        loadUrl(newTab, "duckduckgo.com");
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
