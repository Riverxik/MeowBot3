package com.github.riverxik.meowbot.modules.quotes;

import com.github.riverxik.meowbot.Configuration;
import com.github.riverxik.meowbot.database.Database;
import com.github.riverxik.meowbot.modules.chat.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class QuotesManager {

    private static final Logger log = LoggerFactory.getLogger(QuotesManager.class);

    public QuotesManager() {
        createQuotesTable();
        loadChannelQuotes();
    }

    public static int getNextChannelQuoteId(String channelName) {
        Database.connect();
        try {
            Statement statement = Database.getConnection().createStatement();
            String query = "SELECT `quoteId` FROM `quotes` WHERE `channelName` = '"+channelName+"' " +
                    "ORDER BY `quoteId` DESC";
            ResultSet resultSet = statement.executeQuery(query);
            int id = 0;
            if (resultSet.next()) {
                id = resultSet.getInt("quoteId");
            }
            statement.close();
            Database.disconnect();
            return ++id;
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("SQL: " + e.getMessage());
        }
        return 1;
    }

    public static boolean addNewQuote(String channelName, int quoteId, String quoteText) {
        Database.connect();
        try {
            Statement statement = Database.getConnection().createStatement();
            String query = "INSERT INTO `quotes` (`channelName`, `quoteId`, `quoteText`) " +
                    "VALUES ('"+channelName+"', '"+quoteId+"', '"+quoteText+"')";
            statement.execute(query);
            statement.close();
            Database.disconnect();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("SQL: " + e.getMessage());
        }
        return false;
    }

    public static String showQuote(String channelName, int quoteId) {
        String quoteText = String.format("Can't find quote with id = [%d]", quoteId);
        Database.connect();
        try {
            Statement statement = Database.getConnection().createStatement();
            String query = "SELECT `quoteText` FROM `quotes` WHERE `channelName` = '"+channelName+"' " +
                    "AND `quoteId` = '"+quoteId+"'";
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                quoteText = resultSet.getString("quoteText");
            }
            statement.close();
            Database.disconnect();
            return quoteText;
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("SQL: " + e.getMessage());
        }
        return quoteText;
    }

    public static boolean removeQuote(String channelName, int quoteId) {
        Database.connect();
        try {
            Statement statement = Database.getConnection().createStatement();
            String query = "DELETE FROM `quotes` WHERE `channelName` = '"+channelName+"' " +
                    "AND `quoteId` = '"+quoteId+"'";
            statement.execute(query);
            statement.close();
            Database.disconnect();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("SQL: " + e.getMessage());
        }
        return false;
    }

    private void createQuotesTable() {
        log.info("Looking for quotes table...");
        Database.connect();
        try {
            Statement statement = Database.getConnection().createStatement();
            String query = "CREATE TABLE IF NOT EXISTS [quotes] (\n" +
                    "[id] INTEGER DEFAULT '0' NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                    "[channelName] VARCHAR(50) DEFAULT 'unknown' NOT NULL,\n" +
                    "[quoteId] INTEGER  NOT NULL,\n" +
                    "[quoteText] VARCHAR(250)  NOT NULL\n" +
                    ")";
            statement.execute(query);
            log.info("Quotes table successfully created!");
            statement.close();
            Database.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("SQL: " + e.getMessage());
        }
    }

    private void loadChannelQuotes() {
        for (Channel channel : Configuration.loadingChannels) {
            channel.getSettings().setQuotes(new ChannelQuotes(channel.getName()));
        }
    }
}
