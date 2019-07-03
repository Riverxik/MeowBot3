package com.github.riverxik.meowbot;

import com.github.riverxik.meowbot.modules.CurrencyManager;
import com.github.riverxik.meowbot.modules.TwitchBot;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.FileWriter;
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

            log.info("Starting twitch-bot...");
            TwitchBot twitchBot = new TwitchBot();
            twitchBot.registerFeatures();
            twitchBot.start();
        }
    }

    private void loadModules() {
        if (Configuration.isCurrencyEnable()) {
            CurrencyManager currencyManager = new CurrencyManager();
        }
    }

    private boolean isConfigExists() {
        JSONParser parser = new JSONParser();
        try {
            parser.parse(new FileReader("config.json"));
            log.info("Конфигурация найдена!");
            return true;

        } catch (ParseException | IOException e){
            if(e.getMessage().equals("config.json (Не удается найти указанный файл)")) {
                say(e.getMessage());
                log.error("Не удалось найти файл конфигурации!");
                createConfigurationFile();
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    private void createConfigurationFile() {
        JSONObject obj = new JSONObject();

        JSONObject tokens = new JSONObject();
        tokens.put("clientAppId", "EnterYourApplicationIdHere");
        tokens.put("clientAppSecret", "EnterYourApplicationSecretHere");
        tokens.put("chatToken", "EnterYourChatTokenHere");
        obj.put("tokens", tokens);

        JSONArray channels = new JSONArray();
        JSONObject channel = new JSONObject();
        channel.put("channelName", "murameowbot");
        channel.put("accessToken", "");
        channel.put("moderationEnabled", false);
        channel.put("currencyEnabled", false);
        channel.put("customCommandsEnabled", false);
        channel.put("betsEnabled", false);
        channels.add(channel);
        obj.put("channels", channels);

        // For disabling modules for all channel.
        // Not implemented right now.
        JSONObject modules = new JSONObject();
        modules.put("currency", false);
        modules.put("moderation", false);
        modules.put("bets", false);
        modules.put("customCommands", false);
        modules.put("streamLive", false);
        modules.put("streamOffline", false);
        modules.put("streamFollower", false);
        obj.put("modules", modules);

        obj.put("admin", "putYourNicknameHere");

        // Write to file
        try (FileWriter file = new FileWriter("config.json")) {
            file.write(obj.toJSONString());
            log.info("Файл конфигурации 'config.json' сгенерирован.");
            say("Файл конфигурации 'config.json' сгенерирован. Пожалуйста заполните его ;)");
            file.flush();
            file.close();
            java.lang.System.exit(0);
        } catch (IOException e) {
            say("Что-то пошло не так, нет возможности создать файл конфигурации.");
            log.error("Не удалось создать файл конфигурации.", e.toString());
            e.printStackTrace();
            java.lang.System.exit(0);
        }
    }

    /**
     * Prints text in console.
     * @param text - String for print.
     */
    public void say(String text) {
        System.out.println(String.format("[Meowbot] >>> [%s]", text));
    }
}
