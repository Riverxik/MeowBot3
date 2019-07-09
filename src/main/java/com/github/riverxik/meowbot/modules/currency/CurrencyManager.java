package com.github.riverxik.meowbot.modules.currency;

import com.github.riverxik.meowbot.Configuration;
import com.github.riverxik.meowbot.database.Database;
import com.github.riverxik.meowbot.modules.TwitchBot;
import com.github.riverxik.meowbot.modules.chat.Channel;
import com.github.riverxik.meowbot.modules.chat.ChannelUser;
import com.github.twitch4j.tmi.domain.Chatters;
import com.netflix.hystrix.HystrixCommandProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * Works with channels currency.
 * Every 5 minutes loading new users and increase currency for existing.
 * @author RiVeRx
 * @version 1.0
 */
public class CurrencyManager {

    private final int DELAY_BETWEEN_QUERY = 300000; // Every 5 minutes check new users
    private final boolean TIMEOUT_ENABLED = false; // For getChatters() because of poor internet. Default: true

    private static final Logger log = LoggerFactory.getLogger(CurrencyManager.class);

    /** Constructor for instance */
    public CurrencyManager() {
        init();
        start();
    }

    /**
     * Return a string that contains currency name of specific channel.
     * @param channelName String, name of the current channel
     * @return String, Currency name for specific channel.
     */
    public static String getChannelCurrencyName(String channelName) {
        try {
            return Configuration.getChannelByName(channelName).getSettings().getCurrency().getCurrencyName();
        } catch (NullPointerException e) {
            return String.format("Couldn't found currency name for %s", channelName);
        }
    }

    /**
     * Sets a new currency name for specific channel
     * @param channelName Name of the specific channel
     * @param currencyName New currency name
     * @return String with message about any changes
     */
    public static String setChannelCurrencyName(String channelName, String currencyName) {
        try {
            Configuration.getChannelByName(channelName).getSettings().getCurrency().setCurrencyName(currencyName);
            updateCurrencySettings();
            return String.format("Currency name for %s has been updated to [%s]", channelName, currencyName);
        } catch (NullPointerException e) {
            return String.format("Couldn't found currency name for %s", channelName);
        }
    }

    /**
     * Returns currency increment for specific channel
     * @param channelName name of the specific channel
     * @return new currency increment or 0 if there was no change
     */
    public static int getChannelCurrencyInc(String channelName) {
        try {
            return Configuration.getChannelByName(channelName).getSettings().getCurrency().getCurrencyInc();
        } catch (NullPointerException e) {
            return -1;
        }
    }

    /**
     * Sets new value for currency increment for specific channel
     * @param channelName String, name of the channel
     * @param currencyInc Integer, new value of currency increment
     * @return 1 if currency increment has been updated, else 0
     */
    public static int setChannelCurrencyInc(String channelName, int currencyInc) {
        try {
            Configuration.getChannelByName(channelName).getSettings().getCurrency().setCurrencyInc(currencyInc);
            updateCurrencySettings();
            return 1;
        } catch (NullPointerException e) {
            return -1;
        }
    }

    /**
     * Returns is enable subscriber multiplier for specific channel
     * @param channelName name of the specific channel
     * @return is a subscriber multiplier enable
     */
    public static boolean getChannelSubEnable(String channelName) {
        try {
            return Configuration.getChannelByName(channelName).getSettings().getCurrency().isSubEnable();
        } catch (NullPointerException e) {
            return false;
        }
    }

    /**
     * Sets a new status of subscriber multiplier
     * @param channelName name of the specific channel
     * @param isSubEnable a new value for status of subscriber multiplier
     * @return true if value was updated else false
     */
    public static boolean setChannelSubEnable(String channelName, boolean isSubEnable) {
        try {
            Configuration.getChannelByName(channelName).getSettings().getCurrency().setSubEnable(isSubEnable);
            updateCurrencySettings();
            return true;
        } catch (NullPointerException e) {
            return false;
        }
    }

    /**
     * Returns a value of specific channel subscriber multiplier
     * @param channelName name of the specific channel
     * @return value of subscriber multiplier
     */
    public static int getChannelSubMultiplier(String channelName) {
        try {
            return Configuration.getChannelByName(channelName).getSettings().getCurrency().getSubMultiplier();
        } catch (NullPointerException e) {
            return 1;
        }
    }

    /**
     * Sets a new value of specific channel subscriber multiplier
     * @param channelName name of the specific channel
     * @param subMultiplier a new value for subscriber multiplier
     * @return 1 if value was updated else 0
     */
    public static int setChannelSubMultiplier(String channelName, int subMultiplier) {
        try {
            Configuration.getChannelByName(channelName).getSettings().getCurrency().setSubMultiplier(subMultiplier);
            updateCurrencySettings();
            return 1;
        } catch (NullPointerException e) {
            return 0;
        }
    }

    public static int getUserCurrency(String channelName, String userName) {
        Database database = new Database();
        database.connect();
        int count = 0;
        try {
            Statement statement = database.getConnection().createStatement();
            String query = "SELECT `currency` FROM `"+channelName+"` " +
                    "WHERE `userName` = '"+userName.toLowerCase()+"'";
            ResultSet resultSet = statement.executeQuery(query);
            if(resultSet.next()) {
                count = resultSet.getInt("currency");
            }
            statement.close();
            database.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    private void init() {
        // Create currency table.
        // Try to load settings for current channels about currency.
        // If not exist - create
        createCurrencyTable();
        setupChannelCurrency();
        loadChannelCurrency();
    }

    private void loadChannelCurrency() {
        for (Channel channel : Configuration.loadingChannels) {
            String channelName = channel.getName();
            String currencyName = "Points";
            int currencyInc = 1;
            boolean subEnable = false;
            int subMultiplier = 2;
            log.info(String.format("Loading currency settings for [%s]...", channelName));
            Database database = new Database();
            database.connect();
            try {
                Statement statement = database.getConnection().createStatement();
                // Currency name.
                String query = "SELECT `currencyName`, `currencyInc`, " +
                        "`subEnable`, `subMultiplier` " +
                        "from `currency` WHERE `channelName` = '"+channelName+"'";
                ResultSet res = statement.executeQuery(query);
                if(res.next()) {
                    currencyName = res.getString("currencyName");
                    currencyInc = res.getInt("currencyInc");
                    subEnable = res.getBoolean("subEnable");
                    subMultiplier = res.getInt("subMultiplier");
                }
                statement.close();
                database.disconnect();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            channel.getSettings().setCurrency(new ChannelCurrency(currencyName, currencyInc, subEnable, subMultiplier));
        }
    }

    private void setupChannelCurrency() {
        for(Channel channel : Configuration.loadingChannels) {
            String channelName = channel.getName();
            log.info(String.format("Setup currency settings for [%s]...", channelName));
            Database database = new Database();
            database.connect();
            try {
                Statement statement = database.getConnection().createStatement();
                String query = "INSERT INTO `currency` (`channelName`) " +
                        "SELECT '"+channelName+"' " +
                        "WHERE NOT EXISTS(SELECT 1 FROM `currency` WHERE `channelName` = '"+channelName+"')";
                statement.execute(query);
                statement.close();
                database.disconnect();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void createCurrencyTable() {
        try {
            log.info("Looking for currency table...");
            Database database = new Database();
            database.connect();
            Statement statement = database.getConnection().createStatement();
            String query = "CREATE TABLE IF NOT EXISTS `currency` (\n" +
                    "\t`id`\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,\n" +
                    "\t`channelName`\tTEXT NOT NULL DEFAULT 'channel' UNIQUE,\n" +
                    "\t`currencyName`\tTEXT NOT NULL DEFAULT 'points',\n" +
                    "\t`currencyInc`\tINTEGER NOT NULL DEFAULT 1,\n" +
                    "\t`moneyName`\tTEXT NOT NULL DEFAULT 'money',\n" +
                    "\t`moneyInc`\tINTEGER NOT NULL DEFAULT 10,\n" +
                    "\t`subEnable`\tTEXT NOT NULL DEFAULT 'false',\n" +
                    "\t`subMultiplier`\tINTEGER NOT NULL DEFAULT 2\n" +
                    ");";
            statement.execute(query);
            log.info("Currency table successfully created!");
            statement.close();
            database.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("SQL: " + e.getMessage());
        }
    }

    private void start() {
        Thread run = new Thread(() -> {
            while(true)
            {
                try
                {
                    // Every 5 minutes.
                    loadUsers();
                    Thread.sleep(DELAY_BETWEEN_QUERY);
                } catch (InterruptedException e) {
                    log.error("Error with loading chat users", e.getMessage());
                    e.printStackTrace();
                    break;
                }
            }
        });
        run.start();
    }

    private void loadUsers() {
        for(Channel channel : Configuration.loadingChannels) {
            channel.removeAllUsersFromChannel();

            HystrixCommandProperties.Setter().withExecutionTimeoutEnabled(TIMEOUT_ENABLED);
            Chatters chatters = TwitchBot.twitchClient.getMessagingInterface().getChatters(channel.getName()).execute();

            List<String> userNameAllViewers = chatters.getAllViewers();
            List<String> userNameMods = chatters.getModerators();
            List<String> userNameVips = chatters.getVips();

            for (int i = 0; i < userNameAllViewers.size(); i++) {
                String userName = userNameAllViewers.get(i);
                boolean isMod = userNameMods.contains(userName);
                boolean isVip = userNameVips.contains(userName);
                ChannelUser user = new ChannelUser(userName, isMod, false, isVip);

                if(!channel.isContainUser(user.getName())) {
                    channel.addUserToChannel(user);
                }
            }

            if(!"".equals(channel.getSettings().getAccessToken()))
                channel.updateSubscribers();

            int userCount = channel.getUsersCount();
            log.info(String.format("[%s] loaded users [%d]", channel.getName(), userCount));
            channel.updateAllUsersInDatabase();

            // Delay between executing next channel
            try {
             Thread.sleep(1000);
            } catch (InterruptedException e) {
             e.printStackTrace();
            }

        }
    }

    private static void updateCurrencySettings() {
        for(Channel channel : Configuration.loadingChannels) {
            Database database = new Database();
            database.connect();
            try {
                Statement statement = database.getConnection().createStatement();
                String channelName = channel.getName();
                String currencyName = CurrencyManager.getChannelCurrencyName(channelName);
                int currencyInc = CurrencyManager.getChannelCurrencyInc(channelName);
                boolean subEnable = CurrencyManager.getChannelSubEnable(channelName);
                int subMultiplier = CurrencyManager.getChannelSubMultiplier(channelName);
                // Updates table if some value has changed
                String query = "REPLACE INTO `currency` (`channelName`, `currencyName`, " +
                        "`currencyInc`, `subEnable`, `subMultiplier`) " +
                        "SELECT '"+channelName+"', '"+currencyName+"', " +
                        "'"+currencyInc+"', '"+subEnable+"', '"+subMultiplier+"' " +
                        "WHERE NOT EXISTS(SELECT 1 FROM `currency` " +
                        "WHERE `channelName` = '"+channelName+"' " +
                        "AND `currencyName` = '"+currencyName+"' " +
                        "AND `currencyInc` = '"+currencyInc+"' " +
                        "AND `subEnable` = '"+subEnable+"' " +
                        "AND `subMultiplier` = '"+subMultiplier+"')";
                statement.execute(query);
                statement.close();
                database.disconnect();
            } catch (SQLException e) {
                e.getMessage();
            }
        }
    }
}
