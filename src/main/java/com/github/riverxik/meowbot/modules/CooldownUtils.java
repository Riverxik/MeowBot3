package com.github.riverxik.meowbot.modules;

import com.github.riverxik.meowbot.database.DatabaseUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.GregorianCalendar;

public final class CooldownUtils {

    public static void setCooldown(String channelName, String commandName, int cooldown) {
        try {
            DatabaseUtils.connect();
            Statement statement = DatabaseUtils.getConnection().createStatement();
            long startTime = new GregorianCalendar().getTimeInMillis();
            String id = "(SELECT `id` FROM `cooldowns` " +
                    "WHERE `commandName` = '"+commandName+"' AND `channelName` = '"+channelName+"')";
            String query = "INSERT OR REPLACE INTO `cooldowns` " +
                    "VALUES ("+id+", '"+commandName+"', '"+channelName+"', "+cooldown+", '"+startTime+"') ";
            statement.execute(query);
            statement.close();
            DatabaseUtils.disconnect();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static long isCommandAvailable(String channelName, String commandName) {
        long deltaTime = 10;
        int cooldownTime = 0;
        try {
            DatabaseUtils.connect();
            Statement statement = DatabaseUtils.getConnection().createStatement();
            long starTime = new GregorianCalendar().getTimeInMillis();
            String query = "select `cooldown`, `lastTimeUse` from `cooldowns` " +
                    "where `commandName` = '"+commandName+"' and `channelName` = '"+channelName+"' ";
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                cooldownTime = resultSet.getInt("cooldown");
                deltaTime = starTime - Long.valueOf(resultSet.getString("lastTimeUse"));
            }
            resultSet.close();
            statement.close();
            DatabaseUtils.disconnect();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return deltaTime > cooldownTime ? 0 : cooldownTime-deltaTime;
    }

    public static void cooldownCommand(String channelName, String commandName) {
        try {
            DatabaseUtils.connect();
            Statement statement = DatabaseUtils.getConnection().createStatement();
            long startTime = new GregorianCalendar().getTimeInMillis();
            String query = "UPDATE `cooldowns` SET `lastTimeUse` = '"+startTime+"' " +
                    "WHERE `commandName` = '"+commandName+"' AND `channelName` = '"+channelName+"'";
            statement.executeUpdate(query);

            statement.close();
            DatabaseUtils.disconnect();

        } catch (SQLException e) {
            setCooldown(channelName, commandName, 0);
            e.printStackTrace();
        }
    }
}
