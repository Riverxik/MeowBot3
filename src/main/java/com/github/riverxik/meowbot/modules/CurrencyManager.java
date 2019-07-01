package com.github.riverxik.meowbot.modules;

import com.github.riverxik.meowbot.Configuration;
import com.github.riverxik.meowbot.database.ChannelDb;
import com.github.riverxik.meowbot.database.ChannelUsers;
import com.github.riverxik.meowbot.database.Database;
import com.github.twitch4j.tmi.domain.Chatters;
import com.netflix.hystrix.HystrixCommandProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * Works with channels currency.
 * @author RiVeRx
 * @version 1.0
 */
public class CurrencyManager {

    private final int DELAY_BETWEEN_QUERY = 30000; // Every 5 minutes check new users
    private final boolean TIMEOUT_ENABLED = false; // For getChatters() because of poor internet. Default: true

    private static final Logger log = LoggerFactory.getLogger(CurrencyManager.class);

    /** Constructor for instance */
    public CurrencyManager() {
        /*
         * Подгружает необходимые данные
         * Запускает бесконечный цикл
         * Каждые 5 минут выполняет загрузку пользователей для каждого канала
         * */
        init();
        start();
    }

    private void init() {
        // Create currency table.
        // Try to load settings for current channels about currency.
        // If not exist - create
        createCurrencyTable();
        loadSettings();
    }

    private void loadSettings() {
        for(ChannelDb channel : Configuration.loadingChannels) {
            String channelName = channel.getChannelName();
            log.info(String.format("Loading currency settings for [%s]...", channelName));
        }
    }

    private void createCurrencyTable() {
        try {
            log.info("Looking for currency table...");
            Database database = new Database();
            database.connect();
            Statement statement = database.getConnection().createStatement();
            String query = "CREATE TABLE `currency` (\n" +
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
            //e.printStackTrace();
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
}
