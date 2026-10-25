
package com.neha.reservation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/reservation_db";

    private static final String USER = "root";

    
   String password = System.getenv("DB_PASSWORD");

    public static Connection getConnection()
            throws SQLException {

        return DriverManager.getConnection(
                URL, USER, PASSWORD
        );
    }
}

