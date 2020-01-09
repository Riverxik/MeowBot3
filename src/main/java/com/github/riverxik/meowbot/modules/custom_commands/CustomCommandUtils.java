package com.github.riverxik.meowbot.modules.custom_commands;

import com.github.riverxik.meowbot.database.DatabaseUtils;
import com.github.twitch4j.chat.TwitchChat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CustomCommandUtils {

    private static final Logger log = LoggerFactory.getLogger(CustomCommandUtils.class);

    public static boolean isCommandExists(String channelName, String commandName) {
        boolean result = false;
        try {
            DatabaseUtils.connect();
            Statement statement = DatabaseUtils.getConnection().createStatement();
            String query = "SELECT `commandName` FROM `customCommands` " +
                    "WHERE `channelName` = '"+channelName+"' and `commandName` = '"+commandName+"'";
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                result = true;
            }

            resultSet.close();
            statement.close();
            DatabaseUtils.disconnect();
        } catch (SQLException e) {
            log.error(String.format("Error while selecting command [%s][%s]",
                    channelName, commandName), e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    public static String getCommandText(String channelName, String commandName) {
        String text = "";
        try {
            DatabaseUtils.connect();
            Statement statement = DatabaseUtils.getConnection().createStatement();
            String query = "SELECT `commandText` FROM `customCommands` " +
                    "WHERE `channelName` = '"+channelName+"' and `commandName` = '"+commandName+"'";
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                text = resultSet.getString("commandText");
            }

            resultSet.close();
            statement.close();
            DatabaseUtils.disconnect();
        } catch (SQLException e) {
            log.error(String.format("Error getting command text for [%s]", commandName), e.getMessage());
            e.printStackTrace();
        }
        return text;
    }

    public static boolean createNewCommand(String channel, String cmdName, String cmdText) {
        try {
            DatabaseUtils.connect();
            Statement statement = DatabaseUtils.getConnection().createStatement();
            String query = "INSERT INTO `customCommands` (`channelName`, `commandName`, `commandText`) " +
                    "VALUES('"+channel+"', '"+cmdName+"', '"+cmdText+"')";
            statement.execute(query);

            statement.close();
            DatabaseUtils.disconnect();
            return true;
        } catch (SQLException e) {
            log.error(String.format("Error while creating new command [%s][%s] for [%s]",
                    cmdName, cmdText, channel), e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateCommand(String channel, String cmdName, String cmdText) {
        try {
            DatabaseUtils.connect();
            Statement statement = DatabaseUtils.getConnection().createStatement();
            String query = "UPDATE `customCommands` SET `commandText` = '"+cmdText+"' " +
                    "WHERE `channelName` = '"+channel+"' and `commandName` = '"+cmdName+"'";
            statement.executeUpdate(query);

            statement.close();
            DatabaseUtils.disconnect();
            return true;
        } catch (SQLException e) {
            log.error(String.format("Error while updating custom command [%s][%s] for [%s]",
                    channel, cmdName, cmdText), e.getMessage());
            e.printStackTrace();
        }
        return false;
    }


    public static boolean removeCommand(String channel, String cmdName) {
        try {
            DatabaseUtils.connect();
            Statement statement = DatabaseUtils.getConnection().createStatement();
            String query = "DELETE FROM `customCommands` WHERE `channelName` = '"+channel+"' " +
                    "AND `commandName` = '"+cmdName+"'";
            statement.execute(query);

            statement.close();
            DatabaseUtils.disconnect();
            return true;
        } catch (SQLException e) {
            log.error(String.format("Error while deleting custom command %s", cmdName), e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public static boolean showCommand(String channel, String sender, String cmdName, TwitchChat chat) {
        if (isCommandExists(channel, cmdName)) {
            String cmdText = getCommandText(channel, cmdName);
            chat.sendMessage(channel,
                    String.format("%s, [%s]->[%s]", sender, cmdName, cmdText));
            return true;
        } else {
            chat.sendMessage(channel, String.format("%s, Command [%s] doesn't exists", sender, cmdName));
        }
        return false;
    }

    public static List<String> getAllCustomCommands() {
        List<String> commandNames = new ArrayList<>();
        try {
            DatabaseUtils.connect();
            Statement statement = DatabaseUtils.getConnection().createStatement();
            String query = "SELECT DISTINCT `commandName` FROM `customCommands`";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                commandNames.add(resultSet.getString("commandName"));
            }

            resultSet.close();
            statement.close();
            DatabaseUtils.disconnect();
        } catch (SQLException e) {
            log.error("Error while getting list of custom commands.", e.getMessage());
            e.printStackTrace();
        }
        return commandNames;
    }
}
