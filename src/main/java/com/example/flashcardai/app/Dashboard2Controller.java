package com.example.flashcardai.app;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

import com.example.flashcardai.session.Session;

public class Dashboard2Controller implements Initializable {
    @FXML
    private Label textWelcome;

    @FXML
    private Button btnLogout;

    @FXML
    private Button btnPage1;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Session session = App.getAppInstance().getSession();
        String email = session.getEmail();
        textWelcome.setText("Welcome, " + email);

        btnLogout.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                session.logout();
                App.getAppInstance().navigate("login");
            }
        });

        btnPage1.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                App.getAppInstance().navigate("dashboard");
            }
        });
    }
}
