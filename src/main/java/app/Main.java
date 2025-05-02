package app;

import db.DBConnector;

import db.UserDAO;


import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        Connection conn = DBConnector.connect();
        if (conn != null) {
            System.out.println("Connection successful!");
        }

        UserDAO.insertTestUser();

    }
}