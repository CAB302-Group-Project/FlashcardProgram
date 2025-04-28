module com.example.flashcardai {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.flashcardai.app to javafx.fxml;
    opens com.example.flashcardai.controllers to javafx.fxml;
    opens com.example.flashcardai.models to javafx.fxml;

    exports com.example.flashcardai.app;
    exports com.example.flashcardai.controllers;
    exports com.example.flashcardai.models;
}
