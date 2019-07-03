package com.github.riverxik.meowbot.modules;

import com.github.riverxik.meowbot.Configuration;
import com.github.riverxik.meowbot.database.ChannelCurrency;
import com.github.riverxik.meowbot.database.ChannelDb;
import com.github.riverxik.meowbot.database.ChannelUsers;
import com.github.riverxik.meowbot.database.Database;
import com.github.twitch4j.tmi.domain.Chatters;
import com.netflix.hystrix.HystrixCommandProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Works with channels currency.
 * Every 5 minutes loading new users and increase currency for existing.
 * @author RiVeRx
 * @version 1.0
 */
public class CurrencyManager {

    private final int DELAY_BETWEEN_QUERY = 30000; // Every 5 minutes check new users
    private final boolean TIMEOUT_ENABLED = false; // For getChatters() because of poor internet. Default: true
    private static List<ChannelCurrency> channelCurrencyList = new ArrayList<>();

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
        for (ChannelCurrency channel : channelCurrencyList) {
            if(channelName.equals(channel.getChannelName())) {
                return channel.getCurrencyName();
            }
        }
        return String.format("Couldn't found currency name for %s", channelName);
    }

    /**
     * Sets a new currency name for specific channel
     * @param channelName Name of the specific channel
     * @param currencyName New currency name
     * @return String with message about any changes
     */
    public static String setChannelCurrencyName(String channelName, String currencyName) {
        for (ChannelCurrency channel : channelCurrencyList) {
            if(channelName.equals(channel.getChannelName())) {
                channel.setCurrencyName(currencyName);
                updateCurrencySettings();
                return String.format("Currency name for %s has been updated to [%s]", channelName, currencyName);
            }
        }
        return String.format("Couldn't found currency name for %s", channelName);
    }

    /**
     * Returns currency increment for specific channel
     * @param channelName name of the specific channel
     * @return new currency increment or 0 if there was no change
     */
    public static int getChannelCurrencyInc(String channelName) {
        for (ChannelCurrency channel : channelCurrencyList) {
            if(channelName.equals(channel.getChannelName())) {
                return channel.getCurrencyInc();
            }
        }
        return 0;
    }

    /**
     * Sets new value for currency increment for specific channel
     * @param channelName String, name of the channel
     * @param currencyInc Integer, new value of currency increment
     * @return 1 if currency increment has been updated, else 0
     */
    public static int setChannelCurrencyInc(String channelName, int currencyInc) {
        for (ChannelCurrency channel : channelCurrencyList) {
            if(channelName.equals(channel.getChannelName())) {
                channel.setCurrencyInc(currencyInc);
                updateCurrencySettings();
                return 1;
            }
        }
        return 0;
    }

    /**
     * Returns is enable subscriber multiplier for specific channel
     * @param channelName name of the specific channel
     * @return is a subscriber multiplier enable
     */
    public static boolean getChannelSubEnable(String channelName) {
        for (ChannelCurrency channel : channelCurrencyList) {
            if(channelName.equals(channel.getChannelName())) {
                return channel.isSubEnable();
            }
        }
        return false;
    }

    /**
     * Sets a new status of subscriber multiplier
     * @param channelName name of the specific channel
     * @param isSubEnable a new value for status of subscriber multiplier
     * @return true if value was updated else false
     */
    public static boolean setChannelSubEnable(String channelName, boolean isSubEnable) {
        for (ChannelCurrency channel : channelCurrencyList) {
            if(channelName.equals(channel.getChannelName())) {
                channel.setSubEnable(isSubEnable);
                updateCurrencySettings();
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a value of specific channel subscriber multiplier
     * @param channelName name of the specific channel
     * @return value of subscriber multiplier
     */
    public static int getChannelSubMultiplier(String channelName) {
        for (ChannelCurrency channel : channelCurrencyList) {
            if(channelName.equals(channel.getChannelName())) {
                return channel.getSubMultiplier();
            }
        }
        return 1;
    }

    /**
     * Sets a new value of specific channel subscriber multiplier
     * @param channelName name of the specific channel
     * @param subMultiplier a new value for subscriber multiplier
     * @return 1 if value was updated else 0
     */
    public static int setChannelSubMultiplier(String channelName, int subMultiplier) {
        for (ChannelCurrency channel : channelCurrencyList) {
            if(channelName.equals(channel.getChannelName())) {
                channel.setSubMultiplier(subMultiplier);
                updateCurrencySettings();
                return 1;
            }
        }
        return 0;
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
        for (ChannelDb channel : Configuration.loadingChannels) {
            String channelName = channel.getChannelName();
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
            channelCurrencyList.add(
                    new ChannelCurrency(channelName, currencyName, currencyInc, subEnable, subMultiplier));
        }
    }

    private void setupChannelCurrency() {
        for(ChannelDb channel : Configuration.loadingChannels) {
            String channelName = channel.getChannelName();
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
        }
    }

    private void start() {
        Thread run = new Thread(new Runnable() {
            @Override
            public void run() {
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
            }
        });
        run.start();
    }

    private void loadUsers() {
        for(ChannelDb channel : Configuration.loadingChannels) {
            HystrixCommandProperties.Setter().withExecutionTimeoutEnabled(TIMEOUT_ENABLED);
            Chatters chatters = TwitchBot.twitchClient.getMessagingInterface().getChatters(channel.getChannelName()).execute();
            ChannelUsers channelUsers = new ChannelUsers();

            channelUsers.channelName = channel.getChannelName();
            channelUsers.allViewers = chatters.getAllViewers();
            channelUsers.moderators =  chatters.getModerators();
            channelUsers.vips = chatters.getVips();

            channelUsers.update(channel.isCurrencyEnabled());

            Configuration.channelUsersList.put(channel.getChannelName(),channelUsers);

            log.info(String.format("[%s] loaded users [%d]", channel.getChannelName(), chatters.getAllViewers().size()));

            // Delay between executing next channel
            try {
             Thread.sleep(1000);
            } catch (InterruptedException e) {
             e.printStackTrace();
            }

        }
    }

    private static void updateCurrencySettings() {
        for(ChannelDb channel : Configuration.loadingChannels) {
            Database database = new Database();
            database.connect();
            try {
                Statement statement = database.getConnection().createStatement();
                String channelName = channel.getChannelName();
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
