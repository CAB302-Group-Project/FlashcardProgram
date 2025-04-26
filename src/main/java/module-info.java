module flashcard {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires spring.security.crypto;
    requires transitive javafx.graphics;
    requires com.auth0.jwt;

    opens flashcard to javafx.fxml;
    exports flashcard;
    exports session;
    opens session to javafx.fxml;
}
