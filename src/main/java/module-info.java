module flashcard {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires spring.security.crypto;
    requires transitive javafx.graphics;

    opens flashcard to javafx.fxml;
    exports flashcard;
}
