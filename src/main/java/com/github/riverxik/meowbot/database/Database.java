package com.github.riverxik.meowbot.database;

import java.sql.*;

/**
 * Base class for SQLite database connection.
 * @author RiVeRx
 * @version 1.0
 */
public class Database {

    private static Connection connection;

    private static final String path = "database.db";

    /**
     * Establishes connection with database.
     * @return - true if database is connected successfully.
     */
    public static boolean connect() {
        try {
            connection = null;
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + path);
            return true;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /** Disconnects current connection */
    public static void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** Returns current connection
     * @return connection-instance
     */
    public static Connection getConnection() {
        return connection;
    }
}
