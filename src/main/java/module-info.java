module com.example.flashcardai {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires spring.security.crypto;
    requires transitive javafx.graphics;
    requires com.auth0.jwt;

    opens app to javafx.graphics, javafx.fxml;
    opens db to javafx.fxml;

    exports app;
    exports db;
}
