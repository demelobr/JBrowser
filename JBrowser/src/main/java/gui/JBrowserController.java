package gui;

import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class JBrowserController implements Initializable {
    private Stage stg;
    private boolean isLightMode = true;
    private WebEngine engine;
    private WebHistory history;
    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private Button btnAddZoom;

    @FXML
    private Button btnArrowLeft;

    @FXML
    private Button btnArrowRight;

    @FXML
    private Button btnClear;

    @FXML
    private Button btnClose;

    @FXML
    private Button btnMaximize;

    @FXML
    private Button btnMinimize;

    @FXML
    private Button btnMinusZoom;

    @FXML
    private Button btnMode;

    @FXML
    private Button btnReload;

    @FXML
    private HBox hbWindowBar;

    @FXML
    private ImageView imgClose;

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
    private WebView wvWebWindow;

    public void setStg(Stage stg) {
        this.stg = stg;
    }

    @FXML
    private void close(ActionEvent event){
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
        history = wvWebWindow.getEngine().getHistory();
        if (history.getCurrentIndex() > 0) {
            history.go(-1);
            ObservableList<WebHistory.Entry> entries = history.getEntries();
            tfUrlBar.setText(entries.get(history.getCurrentIndex()).getUrl());
            btnClear.setVisible(true);
        }
    }

    @FXML
    private void forward(ActionEvent event){
        history = wvWebWindow.getEngine().getHistory();
        if (history.getCurrentIndex() < history.getEntries().size() - 1) {
            history.go(1);
            ObservableList<WebHistory.Entry> entries = history.getEntries();
            tfUrlBar.setText(entries.get(history.getCurrentIndex()).getUrl());
            btnClear.setVisible(true);
        }
    }

    @FXML
    private void reload(ActionEvent event){
        engine.reload();
    }

    @FXML
    private void textEnterKey(KeyEvent event){
        if(event.getCode() == KeyCode.ENTER){
            loadUrl(tfUrlBar.getText());
        }
    }

    @FXML
    private void clear(ActionEvent event){
        tfUrlBar.setText("");
        btnClear.setVisible(false);
    }

    @FXML
    private void zoomIn(ActionEvent event){
        wvWebWindow.setZoom(wvWebWindow.getZoom() + 0.10);
    }

    @FXML
    private void zoomOut(ActionEvent event){
        wvWebWindow.setZoom(wvWebWindow.getZoom() - 0.10);
    }

    private void loadUrl(String url){
        engine = wvWebWindow.getEngine();
        engine.load("https://"+url);
        pbProgressBar.progressProperty().bind(engine.getLoadWorker().progressProperty());

        engine.getLoadWorker().stateProperty().addListener((obs, oldValue, newValue) ->{
            if(newValue == Worker.State.SUCCEEDED){
                System.out.println("Page has been loaded!");
                history = wvWebWindow.getEngine().getHistory();
                ObservableList<WebHistory.Entry> entries = history.getEntries();
                tfUrlBar.setText(entries.get(history.getCurrentIndex()).getUrl());
                lbWindowTitle.setText(entries.get(history.getCurrentIndex()).getTitle());
                btnClear.setVisible(true);
            }else if(newValue == Worker.State.FAILED){
                System.out.println("Loading failed");
            }
        });
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
            }else{
                btnClear.setVisible(true);
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

        loadUrl("duckduckgo.com");
        btnClear.setVisible(true);
        if(isLightMode){
            setLightMode();
        }else{
            setDarkMode();
        }
    }

}
