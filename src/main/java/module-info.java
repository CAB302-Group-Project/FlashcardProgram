module com.example.flashcardai {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens app to javafx.graphics, javafx.fxml;
    opens db to javafx.fxml;

    exports app;
    exports db;
}
