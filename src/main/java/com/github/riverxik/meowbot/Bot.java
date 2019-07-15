package com.github.riverxik.meowbot;

import com.github.riverxik.meowbot.modules.currency.CurrencyManager;
import com.github.riverxik.meowbot.modules.TwitchBot;
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
        if(isConfigExists()) {

            Configuration.loadConfiguration();

            log.info("Looking for database...");
            Configuration.checkOrCreateDatabaseFile();

            log.info("Loading channels...");
            Configuration.loadChannels();

            log.info("Loading commands...");
            Configuration.loadCommands();

            log.info("Starting twitch-bot...");
            TwitchBot twitchBot = new TwitchBot();
            twitchBot.registerFeatures();
            twitchBot.start();
        }
    }

    private void loadModules() {
        if (Configuration.isCurrencyEnable()) {
            new CurrencyManager();
            new QuotesManager();
        }
    }

    private boolean isConfigExists() {
        JSONParser parser = new JSONParser();
        try {
            parser.parse(new FileReader("config.json"));
            log.info("Configuration has been found!");
            return true;

        } catch (ParseException | IOException e){
            if(e.getMessage().equals("config.json (Не удается найти указанный файл)")) {
                say(e.getMessage());
                log.error("Configuration file couldn't found!!");
                Configuration.createConfigurationFile(this);
            }
        }
        return false;
    }

    /**
     * Prints text in console.
     * @param text - String for print.
     */
    public void say(String text) {
        System.out.println(String.format("[Meowbot] >>> [%s]", text));
    }
}
