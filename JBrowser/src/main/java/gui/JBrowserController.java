package gui;

import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class JBrowserController implements Initializable {
    private boolean isLightMode = true;
    private WebEngine engine;
    private WebHistory history;

    @FXML
    private BorderPane parent;

    @FXML
    private Label lbCredits;

    @FXML
    private Button btnAddZoom;

    @FXML
    private Button btnArrowLeft;

    @FXML
    private Button btnArrowRight;

    @FXML
    private Button btnMinusZoom;

    @FXML
    private Button btnClear;

    @FXML
    private Button btnMode;

    @FXML
    private ImageView imgMode;

    @FXML
    private Button btnReload;

    @FXML
    private ProgressBar pbProgressBar;

    @FXML
    private TextField tfUrlBar;

    @FXML
    private WebView wvWebWindow;

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
                Stage stage = (Stage) tfUrlBar.getScene().getWindow();
                stage.setTitle(entries.get(history.getCurrentIndex()).getTitle());
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
        lbCredits.setStyle("-fx-text-fill: #282828;");
    }

    private void setDarkMode(){
        parent.getStylesheets().remove("file:/"+System.getProperty("user.dir").replace("\\", "/")+"/JBrowser/src/main/resources/gui/style/lightMode.css");
        parent.getStylesheets().add("file:/"+System.getProperty("user.dir").replace("\\", "/")+"/JBrowser/src/main/resources/gui/style/darkMode.css");
        Image image = new Image("file:/"+System.getProperty("user.dir").replace("\\", "/")+"/JBrowser/src/main/resources/gui/images/sun-white.png");
        imgMode.setImage(image);
        lbCredits.setStyle("-fx-text-fill: #f8f8f8;");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tfUrlBar.setPromptText("Enter your url here!");
        tfUrlBar.setOnKeyReleased((KeyEvent) -> {
            if(tfUrlBar.getText().isEmpty()){
                btnClear.setVisible(false);
            }else{
                btnClear.setVisible(true);
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
