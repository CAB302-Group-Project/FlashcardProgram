package flashcard;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class App extends Application implements Initializable {
    @FXML
    private Button btnLogin;

    @FXML
    private Button btnRegister;

    private static App appInstance;

    private Stage stage;

    private String token;

    @Override
    public void init() {
        appInstance = this;
    }

    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;

        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("main.fxml"));
        fxmlLoader.setController(this);
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnLogin.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent event) {
                navigate("login");
            }
        });

        btnRegister.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent event) {
                navigate("register");
            }
        });
    }

    public String getEmail() {
        // TODO: Parse token and get email
        return token;
    }

    public void alert(String message) {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(stage);
        VBox dialogVbox = new VBox(20);
        dialogVbox.getChildren().add(new Text(message));
        Scene dialogScene = new Scene(dialogVbox, 300, 200);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    public void navigate(String page) {
        FXMLLoader loader = new FXMLLoader(App.class.getResource(page + ".fxml"));
        if (page.equals("main")) {
            loader.setController(this);
        }

        try {
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public static App getAppInstance() {
        return appInstance;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static void main(String[] args) {
        launch();
    }
}
