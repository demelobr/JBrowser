package com.example.browser;

import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;

public class JBrowserTab extends Tab {
    public WebEngine engine;
    public WebHistory history;
    private WebView view;

    public JBrowserTab(String title){
        this.engine = new WebEngine();
        this.view = new WebView();
        setText(title);
        HBox content = new HBox(this.view);
        HBox.setHgrow(view, javafx.scene.layout.Priority.ALWAYS);
        setContent(content);
    }

    public WebEngine getEngine() {
        return engine;
    }

    public WebHistory getHistory() {
        return history;
    }

    public WebView getView() {
        return view;
    }

    public void setEngine(WebEngine engine) {
        this.engine = engine;
    }

    public void setHistory(WebHistory history) {
        this.history = history;
    }

    public void setView(WebView view) {
        this.view = view;
    }
}
