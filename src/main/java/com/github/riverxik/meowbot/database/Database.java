package com.github.riverxik.meowbot.database;

import java.sql.*;

/**
 * Base class for SQLite database connection.
 * @author RiVeRx
 * @version 1.0
 */
public class Database {

    private Connection connection;

    private static final String path = "database.db";

    /**
     * Establishes connection with database.
     * @return - true if database is connected successfully.
     */
    public boolean connect() {
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
    public void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** Returns current connection
     * @return connection-instance
     */
    public Connection getConnection() {
        return this.connection;
    }
}
