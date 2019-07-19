package com.github.riverxik.meowbot.database;

import org.junit.Assert;
import org.junit.Test;

import java.sql.Connection;

public class DatabaseTest {

    @Test
    public void testConnect() throws Exception {
        Assert.assertEquals(true, Database.connect());
        Database.disconnect();
    }

    @Test
    public void testGetConnection() throws Exception {
        Database.connect();
        Connection connection = Database.getConnection();
        Assert.assertEquals(connection, Database.getConnection());
        Database.disconnect();
    }
}