package com.github.riverxik.meowbot.modules.chat;

import com.github.riverxik.meowbot.Configuration;
import com.github.riverxik.meowbot.database.Database;
import com.github.riverxik.meowbot.modules.TwitchBot;
import com.github.riverxik.meowbot.modules.currency.CurrencyManager;
import com.github.twitch4j.helix.domain.SubscriptionList;
import com.github.twitch4j.helix.domain.UserList;
import com.github.twitch4j.kraken.domain.KrakenSubscription;
import com.github.twitch4j.kraken.domain.KrakenSubscriptionList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Channel {
    private String name = null;
    private ChannelSettings settings = null;
    private List<ChannelUser> channelUsers = new ArrayList<>();

    public Channel(String name, ChannelSettings settings) {
        this.name = name;
        this.settings = settings;
    }

    public boolean isContainUser(String userName) {
        for (ChannelUser channelUser : channelUsers) {
            if (userName.equals(channelUser.getName()))
                return true;
        }
        return false;
    }

    public int getUsersCount() {
        return channelUsers.size();
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
                    "SELECT '"+name+"', '"+settings.getAccessToken()+"', " +
                    "'"+settings.isModerationEnabled()+"', '"+settings.isCurrencyEnabled()+"', " +
                    "'"+settings.isCustomCommandsEnabled()+"', '"+settings.isBetsEnabled()+"'\n" +
                    "WHERE NOT EXISTS(SELECT 1 FROM `channels`\n" +
                    "WHERE `channelName` = '"+name+"' AND `accessToken` = '"+ settings.getAccessToken()+"'\n" +
                    "AND `moderationEnabled` = '"+settings.isModerationEnabled()+"' " +
                    "AND `currencyEnabled` = '"+settings.isCurrencyEnabled()+"'\n" +
                    "AND `customCommandsEnabled` = '"+settings.isCustomCommandsEnabled()+"' " +
                    "AND `betsEnabled` = '"+settings.isBetsEnabled()+"')";
            statement.execute(query);
            statement.close();
            database.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** Creates a table for new channel in database */
    public void createUsersTable() {
        executeQuery("CREATE TABLE IF NOT EXISTS `"+name+"` (\n" +
                "\t`id`\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,\n" +
                "\t`userName`\tTEXT NOT NULL UNIQUE,\n" +
                "\t`currency`\tINTEGER NOT NULL DEFAULT 1,\n" +
                "\t`money`\tINTEGER NOT NULL\n" +
                ");");
    }

    /** Updates all user information of current channel */
    public void updateAllUsersInDatabase() {
        if(settings.isCurrencyEnabled()) {
            Database database = new Database();
            database.connect();
            String query = "";
            try {
                Statement statement = database.getConnection().createStatement();
                for(ChannelUser user : channelUsers) {
                    query = "SELECT `userName`, `currency` FROM `"+name+"` WHERE `userName` = '"+user.getName()+"' ";
                    ResultSet resultSet = statement.executeQuery(query);
                    if(resultSet.next()) { // Пользователь нашёлся
                        if(settings.isCurrencyEnabled()) { // Валюта включена
                            int newCurrency = calculateNewCurrency(user, resultSet.getInt(2));
                            query = "UPDATE `"+name+"` SET `currency` = "+newCurrency+" WHERE `userName` = '"+user.getName()+"' ";
                            statement.executeUpdate(query);
                        }
                    } else {
                        query = "replace into `"+name+"` (`userName`, `currency`, `money`)\n" +
                                "values ('"+user.getName()+"', '0', '0');";
                        statement.executeUpdate(query);
                    }
                }
                statement.close();
                database.disconnect();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public long getChannelId() {
        try {
            UserList resultList = TwitchBot.twitchClient.getHelix().getUsers(null, null, Collections.singletonList(name)).execute();
            return resultList.getUsers().get(0).getId();
        } catch (Exception e) {
            return -1;
        }
    }


    public void updateSubscribers() {
        long channelId = getChannelId();
        // TODO: if subscribers count more than 20 it will return not all of them
        KrakenSubscriptionList subList = TwitchBot.twitchClient.getKraken().getChannelSubscribers(
                settings.getAccessToken(),
                channelId,
                null,
                null,
                null
        ).execute();
        for (KrakenSubscription sub : subList.getSubscriptions()) {
            String name = sub.getUser().getDisplayName();
            ChannelUser user = getChannelUserByName(name);
            user.setSub(true);
        }
    }

    private int calculateNewCurrency(ChannelUser user, int oldCurrency) throws SQLException {
        int newCurrency = oldCurrency;
        if(user.isSub() || user.isVip()) {
            int multiplier = CurrencyManager.getChannelSubMultiplier(name);
            newCurrency += multiplier;
        } else {
            int increment = CurrencyManager.getChannelCurrencyInc(name);
            newCurrency += increment;
        }
        return newCurrency;
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

    public String getName() {
        return name;
    }

    public ChannelSettings getSettings() {
        return settings;
    }

    public void addUserToChannel(ChannelUser user) {
        this.channelUsers.add(user);
    }

    public void removeAllUsersFromChannel() {
        this.channelUsers.clear();
    }

    public ChannelUser getChannelUserByName(String userName) {
        for (ChannelUser user : channelUsers) {
            if (userName.equals(user.getName()))
                return user;
        }
        if (Configuration.admin.equals(userName))
            return new ChannelUser(userName, true, true, true, true);
        else
            return new ChannelUser("User not found", false, false, false, false);
    }
}
