package com.github.riverxik.meowbot.database;

import org.junit.Assert;
import org.junit.Test;

import java.sql.Connection;

public class DatabaseUtilsTest {

    @Test
    public void testConnect() throws Exception {
        Assert.assertEquals(true, DatabaseUtils.connect());
        DatabaseUtils.disconnect();
    }

    @Test
    public void testGetConnection() throws Exception {
        DatabaseUtils.connect();
        Connection connection = DatabaseUtils.getConnection();
        Assert.assertEquals(connection, DatabaseUtils.getConnection());
        DatabaseUtils.disconnect();
    }
}