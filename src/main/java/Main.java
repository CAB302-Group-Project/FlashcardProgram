import db.DBConnector;
import java.sql.Connection;
import db.UserDAO;

public class Main {
    public static void main(String[] args) {
        Connection conn = DBConnector.connect();
        if (conn != null) {
            System.out.println("Connection successful!");
        }
    }
}