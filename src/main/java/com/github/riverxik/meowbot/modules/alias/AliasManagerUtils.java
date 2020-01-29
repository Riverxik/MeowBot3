package com.github.riverxik.meowbot.modules.alias;

import com.github.riverxik.meowbot.database.DatabaseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class AliasManagerUtils {

    private static final Logger log = LoggerFactory.getLogger(AliasManagerUtils.class);

    public static boolean isAliasExists(String channelName, String aliasName) {
        try {
            DatabaseUtils.connect();
            String query = "SELECT `name` FROM `aliases` WHERE `channel` = '"+channelName+"' " +
                    "AND `name` = '"+aliasName+"'";

            Statement statement = DatabaseUtils.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                return true;
            }

            resultSet.close();
            statement.close();
            DatabaseUtils.disconnect();
            return false;

        } catch (SQLException e) {
            log.error("Error while selecting aliases: ", e.getMessage());
            e.printStackTrace();
            return true;
        }
    }

    public static void addNewAliasToDatabase(String channelName, String aliasName, String commandName) {
        try {
            DatabaseUtils.connect();
            String query = "INSERT INTO `aliases` (`name`, `command`, `channel`) " +
                    "VALUES ('"+aliasName+"', '"+commandName+"', '"+channelName+"')";

            Statement statement = DatabaseUtils.getConnection().createStatement();
            statement.execute(query);

            log.info(String.format("Alias [%s]->[%s] for [%s] was successful added!", aliasName, commandName, channelName));
            statement.close();
            DatabaseUtils.disconnect();
        } catch (SQLException e) {
            log.error("Error with adding new alias to database: ", e.getMessage());
            e.printStackTrace();
        }
    }

    public static boolean removeAliasFromDatabase(String channelName, String aliasName) {
        try {
            DatabaseUtils.connect();
            Statement statement = DatabaseUtils.getConnection().createStatement();
            String query = "DELETE FROM `aliases` WHERE `channel` = '"+channelName+"' " +
                    "AND `name` = '"+aliasName+"'";

            statement.execute(query);

            statement.close();
            DatabaseUtils.disconnect();
            log.info(String.format("Alias [%s]-> for [%s] was successful deleted!", aliasName, channelName));
            return true;
        } catch (SQLException e) {
            log.error("Error while deleting alias: ", e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static String findCommandByAlias(String channelName, String aliasName) {
        try {
            DatabaseUtils.connect();
            String query = "SELECT `command` FROM `aliases` WHERE `channel` = '"+channelName+"' " +
                    "AND `name` = '"+aliasName+"'";

            Statement statement = DatabaseUtils.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            String commandName = aliasName;
            if (resultSet.next()) {
                commandName = resultSet.getString("command");
            }

            resultSet.close();
            statement.close();
            DatabaseUtils.disconnect();
            return commandName;
        } catch (SQLException e) {
            log.error("Error while finding alias: ", e.getMessage());
            e.printStackTrace();
            return aliasName;
        }
    }

    public static ArrayList<String> getAllAliasForCommand(String channelName, String commandName) {
        ArrayList<String> aliases = new ArrayList<>();
        try {
            DatabaseUtils.connect();
            String query = "SELECT `name` FROM `aliases` " +
                    "WHERE `channel` = '"+channelName+"' AND `command` = '"+commandName+"'";
            Statement statement = DatabaseUtils.getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                aliases.add(resultSet.getString("name"));
            }

            resultSet.close();
            statement.close();
            DatabaseUtils.disconnect();
        } catch (SQLException e) {
            log.error("Error while getting alias list: ", e.getMessage());
            e.printStackTrace();
        }
        return aliases;
    }
}
