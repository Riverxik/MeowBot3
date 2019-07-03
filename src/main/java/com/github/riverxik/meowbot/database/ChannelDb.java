package com.github.riverxik.meowbot.database;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * Works with channel tables in database.
 * @author RiVeRx
 * @version 1.0
 */
public final class ChannelDb extends Database {

    private String channelName;
    /** Returns channel name
     * @return String contains channel name.
     */
    public String getChannelName() { return channelName; }

    private String accessToken;

    private boolean isModerationEnabled;

    private boolean isCurrencyEnabled;
    /** Returns is currency enabled
     * @return True if currency is enabled.
     */
    public boolean isCurrencyEnabled() { return isCurrencyEnabled; }

    /**
     * Sets currency enabled
     * @param value true, if currency has to be enabled
     */
    public void setCurrencyEnabled(boolean value) { this.isCurrencyEnabled = value; }

    private boolean isCustomCommandEnabled;

    private boolean isBetsEnabled;

    /**
     * Constructor of the class.
     * @param channelName name of the channel
     * @param accessToken token for accessing some private data (for example subscribers count)
     * @param isModerationEnabled enables moderating chat (deletes abuse words, spam).
     * @param isCurrencyEnabled enables calculating currency of channel for users.
     * @param isCustomCommandEnabled enables custom (users) commands.
     * @param isBetsEnabled enables using bets.
     */
    public ChannelDb(String channelName, String accessToken,
                     boolean isModerationEnabled, boolean isCurrencyEnabled,
                     boolean isCustomCommandEnabled, boolean isBetsEnabled) {
        this.channelName = channelName;
        this.accessToken = accessToken;
        this.isModerationEnabled = isModerationEnabled;
        this.isCurrencyEnabled = isCurrencyEnabled;
        this.isCustomCommandEnabled = isCustomCommandEnabled;
        this.isBetsEnabled = isBetsEnabled;
    }

    /** Adds current channel information to database. */
    public void addChannel() {
        try {
            Database database = new Database();
            database.connect();
            Statement statement = database.getConnection().createStatement();
            String query = "REPLACE INTO `channels` (`channelName`, `accessToken`,\n" +
                    "`moderationEnabled`, `currencyEnabled`, `customCommandsEnabled`,\n" +
                    "`betsEnabled`)\n" +
                    "SELECT '"+channelName+"', '"+accessToken+"', " +
                    "'"+isModerationEnabled+"', '"+isCurrencyEnabled+"', " +
                    "'"+isCustomCommandEnabled+"', '"+isBetsEnabled+"'\n" +
                    "WHERE NOT EXISTS(SELECT 1 FROM `channels`\n" +
                    "WHERE `channelName` = '"+channelName+"' AND `accessToken` = '"+accessToken+"'\n" +
                    "AND `moderationEnabled` = '"+isModerationEnabled+"' " +
                    "AND `currencyEnabled` = '"+isCurrencyEnabled+"'\n" +
                    "AND `customCommandsEnabled` = '"+isCurrencyEnabled+"' " +
                    "AND `betsEnabled` = '"+isBetsEnabled+"')";
            statement.execute(query);
            statement.close();
            database.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
