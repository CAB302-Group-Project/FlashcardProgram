module com.example.flashcardai {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires spring.security.crypto;
    requires transitive javafx.graphics;
    requires com.auth0.jwt;

    opens com.example.flashcardai.app to javafx.fxml;
    opens com.example.flashcardai.controllers to javafx.fxml;
    opens com.example.flashcardai.models to javafx.fxml;
    opens session to javafx.fxml;

    exports com.example.flashcardai.app;
    exports com.example.flashcardai.controllers;
    exports com.example.flashcardai.models;
    exports session;
}
