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
            String query = "replace into `channels` " +
                    "(`channelName`, `accessToken`, `moderationEnabled`, " +
                    "`currencyEnabled`, `customCommandsEnabled`, `betsEnabled`)\n" +
                    "values ('"+this.channelName+"', " +
                    "'"+this.accessToken+"', " +
                    "'"+this.isModerationEnabled+"', " +
                    "'"+this.isCurrencyEnabled+"', " +
                    "'"+this.isCustomCommandEnabled+"', " +
                    "'"+this.isBetsEnabled+"');";
            statement.execute(query);
            statement.close();
            database.disconnect();
        } catch (SQLException e) {
            // if channel already exists do nothing
        }
    }
}
