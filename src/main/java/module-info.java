module com.example.flashcardai {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires spring.security.crypto;
    requires transitive javafx.graphics;
    requires com.auth0.jwt;
    requires org.json;
    requires org.apache.pdfbox;
    requires java.desktop;

    opens app to javafx.graphics, javafx.fxml;
    opens db to javafx.fxml;

    exports app;
    exports db;
    exports utilities.models;
    exports utilities.controllers;
    opens utilities.controllers to javafx.fxml, javafx.graphics;
    opens utilities.models to javafx.fxml, javafx.graphics;
    exports db.DAO;
    opens db.DAO to javafx.fxml;
    exports db.utils;
    opens db.utils to javafx.fxml;
}
