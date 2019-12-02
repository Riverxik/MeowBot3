package com.github.riverxik.meowbot.modules.chat;

import com.github.riverxik.meowbot.ConfigurationUtils;
import com.github.riverxik.meowbot.commands.CommandRights;
import com.github.riverxik.meowbot.database.DatabaseUtils;
import com.github.riverxik.meowbot.modules.TwitchBotHelper;
import com.github.riverxik.meowbot.modules.currency.CurrencyManager;
import com.github.twitch4j.helix.domain.Subscription;
import com.github.twitch4j.helix.domain.UserList;

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
            DatabaseUtils.connect();
            Statement statement = DatabaseUtils.getConnection().createStatement();
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
            DatabaseUtils.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** Loads current channel information from database. */
    public void loadChannel() {
        boolean modEnabled = false;
        boolean curEnabled = false;
        boolean customComEnabled = false;
        boolean betsEnabled = false;
        try {
            DatabaseUtils.connect();
            Statement statement = DatabaseUtils.getConnection().createStatement();
            String query = "SELECT `moderationEnabled`, `currencyEnabled`, `customCommandsEnabled`, `betsEnabled` " +
                    "FROM `channels` WHERE `channelName` = '"+name+"' ";
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                modEnabled = Boolean.valueOf(resultSet.getString("moderationEnabled"));
                curEnabled = Boolean.valueOf(resultSet.getString("currencyEnabled"));
                customComEnabled = Boolean.valueOf(resultSet.getString("customCommandsEnabled"));
                betsEnabled = Boolean.valueOf(resultSet.getString("betsEnabled"));
            }
            settings.setModerationEnabled(modEnabled);
            settings.setCurrencyEnabled(curEnabled);
            settings.setCustomCommandsEnabled(customComEnabled);
            settings.setBetsEnabled(betsEnabled);

            resultSet.close();
            statement.close();
            DatabaseUtils.disconnect();
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
        DatabaseUtils.connect();
        String query = "";
        try {
            Statement statement = DatabaseUtils.getConnection().createStatement();
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
            DatabaseUtils.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getChannelId() {
        try {
            UserList resultList = TwitchBotHelper.getTwitchClient().getHelix().getUsers(null, null, Collections.singletonList(name)).execute();
            return resultList.getUsers().get(0).getId();
        } catch (Exception e) {
            return "-1";
        }
    }


    public void updateSubscribers() {
        String channelId = getChannelId();
        // TODO: if subscribers count more than 20 it will return not all of them
        List<Subscription> list = TwitchBotHelper.getTwitchClient().getHelix().getSubscriptions(
                this.getSettings().getAccessToken(),
                channelId,
                null,
                null,
                100
        ).execute().getSubscriptions();
        for (Subscription sub : list) {
            String userName = sub.getUserName();
            ChannelUser user = getChannelUserByName(userName);
            user.setSub(true);
            if (user.getRightLevel().index() < CommandRights.VIP_SUB.index())
                user.setRightLevel(CommandRights.VIP_SUB);
        }
    }

    private int calculateNewCurrency(ChannelUser user, int oldCurrency) throws SQLException {
        int newCurrency = oldCurrency;
        int incForActivity = user.getMessages() / 10;
        if(user.isSub() || user.isVip()) {
            int multiplier = CurrencyManager.getChannelSubMultiplier(name);
            newCurrency += multiplier + incForActivity;
        } else {
            int increment = CurrencyManager.getChannelCurrencyInc(name);
            newCurrency += increment + incForActivity;
        }
        return newCurrency;
    }

    private void executeQuery(String query) {
        try {
            DatabaseUtils.connect();
            Statement statement = DatabaseUtils.getConnection().createStatement();
            statement.execute(query);
            statement.close();
            DatabaseUtils.disconnect();
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
        if (ConfigurationUtils.admin.equals(userName))
            return new ChannelUser(userName, true, true, true, true);
        else
            return new ChannelUser("User not found", false, false, false, false);
    }
}
