package com.github.riverxik.meowbot;

import com.github.riverxik.meowbot.modules.currency.CurrencyManager;
import com.github.riverxik.meowbot.modules.TwitchBotHelper;
import com.github.riverxik.meowbot.modules.quotes.QuotesManager;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;

/**
 * Core of the bot. Used for initialize different modules (twitch, discord and another).
 * @author RiVeRx
 * @version 1.0
 */
public class Bot {

    private static final Logger log = LoggerFactory.getLogger(Bot.class);

    /**
     * Bot's constructor. Initializes configuration settings and loads modules.
     */
    public Bot() {
        log.info("Launch");
        init();
        loadModules();
    }

    private void init() {
        log.info("Looking for configuration...");

        createConfigIfItNotExists();
        ConfigurationUtils.loadConfiguration("config.json");

        log.info("Looking for database...");
        ConfigurationUtils.checkOrCreateDatabaseFile();

        log.info("Loading channels...");
        ConfigurationUtils.loadChannels();

        log.info("Loading commands...");
        ConfigurationUtils.loadCommands();

        log.info("Starting twitch-bot...");
        TwitchBotHelper.initialize();
        TwitchBotHelper.registerFeatures();
        TwitchBotHelper.start();

    }

    private void loadModules() {
        new CurrencyManager();
        new QuotesManager();
    }

    private void createConfigIfItNotExists() {
        JSONParser parser = new JSONParser();
        try {
            parser.parse(new FileReader("config.json"));
            log.info("Configuration has been found!");
        } catch (ParseException | IOException e){
            say(e.getMessage());
            log.error("Configuration file couldn't found!");
            ConfigurationUtils.createConfigurationFile();
        }
    }

    /**
     * Prints text in console.
     * @param text - String for print.
     */
    public void say(String text) {
        System.out.print(String.format("[Meowbot] >>> [%s]\n", text));
    }
}
