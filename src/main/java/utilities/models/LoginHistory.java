package utilities.models;

import javafx.beans.property.SimpleStringProperty;

public class LoginHistory {
    private final SimpleStringProperty date;
    private final SimpleStringProperty time;

    public LoginHistory() {
        this.date = new SimpleStringProperty("");
        this.time = new SimpleStringProperty("");
    }

    public LoginHistory(String date, String time) {
        this.date = new SimpleStringProperty(date);
        this.time = new SimpleStringProperty(time);
    }

    public String getDate() {
        return date.get();
    }

    public String getTime() {
        return time.get();
    }
}
