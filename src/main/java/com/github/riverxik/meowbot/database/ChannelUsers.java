package com.github.riverxik.meowbot.database;

import com.github.riverxik.meowbot.modules.CurrencyManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Works with user tables in database.
 * @author RiVeRx
 * @version 1.0
 */
public class ChannelUsers {

    /** Name of the current channel */
    public String channelName;
    /** List of all viewers nicknames of current channel */
    public List<String> allViewers = new ArrayList<>();
    /** List of all moderator nicknames of current channel */
    public List<String> moderators = new ArrayList<>();
    /** List of all vip's nicknames of current channel */
    public List<String> vips = new ArrayList<>();
    /** List of all subscriber nicknames of current channel */
    public List<String> subscribers = new ArrayList<>();

    private Statement statement = null;

    /** Creates a table for new channel in database */
    public void createTable() {
        executeQuery("CREATE TABLE IF NOT EXISTS `"+channelName+"` (\n" +
                "\t`id`\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,\n" +
                "\t`userName`\tTEXT NOT NULL UNIQUE,\n" +
                "\t`currency`\tINTEGER NOT NULL DEFAULT 1,\n" +
                "\t`money`\tINTEGER NOT NULL\n" +
                ");");
    }

    /** Updates all user information of current channel */
    public void update(boolean isCurrencyEnabled) {
        Database database = new Database();
        database.connect();
        String query = "";
        try {
            statement = database.getConnection().createStatement();
            for(String user : allViewers) {
                query = "SELECT `userName`, `currency` FROM `"+channelName+"` WHERE `userName` = '"+user+"' ";
                ResultSet resultSet = statement.executeQuery(query);
                if(resultSet.next()) { // Пользователь нашёлся
                    if(isCurrencyEnabled) { // Валюта включена
                        calculateCurrency(user, resultSet.getInt(2));
                    }
                } else {
                    query = "replace into `"+channelName+"` (`userName`, `currency`, `money`)\n" +
                            "values ('"+user+"', '0', '0');";
                    statement.executeUpdate(query);
                }
            }
            statement.close();
            database.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void calculateCurrency(String user, int oldCurrency) throws SQLException {
        if(isSub(user) || isVip(user)) {
            if(CurrencyManager.getChannelSubEnable(channelName)) {
                int multiplier = CurrencyManager.getChannelSubMultiplier(channelName);
                oldCurrency += multiplier;
            }
        } else {
            int increment = CurrencyManager.getChannelCurrencyInc(channelName);
            oldCurrency += increment;
        }
        increaseCurrency(user, oldCurrency);
    }

    private void increaseCurrency(String user, int currency) throws SQLException {
        String query = "UPDATE `"+channelName+"` SET `currency` = "+currency+" WHERE `userName` = '"+user+"' ";
        statement.executeUpdate(query);
    }

    private boolean isVip(String userName) {
        return vips.contains(userName);
    }

    private boolean isSub(String userName) {
        return subscribers.contains(userName);
    }

    private void executeQuery(String query) {
        try {
            Database database = new Database();
            database.connect();
            Statement statement = database.getConnection().createStatement();
            statement.execute(query);
            statement.close();
            database.disconnect();
        } catch (SQLException e) {
            // throws if table exists or something went wrong.
        }
    }
}
