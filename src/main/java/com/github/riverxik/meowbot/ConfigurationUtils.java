package com.github.riverxik.meowbot;


import com.github.riverxik.meowbot.commands.AbstractCommand;
import com.github.riverxik.meowbot.commands.AbstractTestCommandHandler;
import com.github.riverxik.meowbot.commands.CalcCommandHandler;
import com.github.riverxik.meowbot.commands.CommandErrorHandler;
import com.github.riverxik.meowbot.commands.CooldownCommandHandle;
import com.github.riverxik.meowbot.commands.EncryptCommandHandler;
import com.github.riverxik.meowbot.commands.HelpCommandHandle;
import com.github.riverxik.meowbot.commands.ShowUserRightsHandle;
import com.github.riverxik.meowbot.database.DatabaseUtils;
import com.github.riverxik.meowbot.modules.SlotMachineCommandHandler;
import com.github.riverxik.meowbot.modules.alias.AliasHandler;
import com.github.riverxik.meowbot.modules.chat.Channel;
import com.github.riverxik.meowbot.modules.chat.ChannelSettings;
import com.github.riverxik.meowbot.modules.currency.commands.CurrencyStatusHandler;
import com.github.riverxik.meowbot.modules.quotes.commands.QuoteHandle;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Represents bot's configuration.
 * @author RiVeRx
 * @version 1.0
 */
public class ConfigurationUtils {
    private static final Logger log = LoggerFactory.getLogger(ConfigurationUtils.class);

    /** Debug mode */
    public static boolean debug = false;

    /** Admin's nickname */
    public static String admin;

    /** Loading from config file or database */
    public static boolean isLoadFromCfg;

    /** Client's app id for this bot */
    public static String clientAppId;

    /** Client's app secret for this bot */
    public static String clientAppSecret;

    /** Chat token for twitch. Generates from: https://twitchapps.com/tmi/ */
    public static String chatToken;

    /** List of channels to connect */
    public static List<Channel> loadingChannels = new ArrayList<>();

    public static HashMap<String, AbstractCommand> commandRegistry = new HashMap<>();

    private static boolean moderationEnable = false; // TODO: use this for enabling/disabling events for all channels
    private static boolean customCommandsEnable = false;
    private static boolean currencyEnable = false;
    private static boolean betsEnable = false;
    private static boolean streamLiveEnable = false;
    private static boolean streamOfflineEnable = false;
    private static boolean streamFollower = false;

    public static boolean isModerationEnable() { return moderationEnable; }
    public static boolean isCustomCommandsEnable() { return customCommandsEnable; }
    public static boolean isCurrencyEnable() { return currencyEnable; }
    public static boolean isBetsEnable() { return betsEnable; }
    public static boolean isStreamLiveEnable() { return streamLiveEnable; }
    public static boolean isStreamOfflineEnable() { return streamOfflineEnable; }
    public static boolean isStreamFollower() { return streamFollower; }

    /**
     * Loads configurations settings from config.json, if config file doesn't exist creates it.
     */
    public static void loadConfiguration(String fileName) {
        JSONObject config = readJsonFromFile(fileName);
        if(config != null) {
            log.info("Reading configuration...");
            JSONArray channels = (JSONArray) config.get("channels");
            for(Object channel : channels) {
                ChannelSettings tmpSettings = new ChannelSettings(
                        (boolean) ((JSONObject) channel).get("moderationEnabled"),
                        (boolean) ((JSONObject) channel).get("currencyEnabled"),
                        (boolean) ((JSONObject) channel).get("betsEnabled"),
                        (boolean) ((JSONObject) channel).get("customCommandsEnabled"),
                        String.valueOf(((JSONObject) channel).get("accessToken"))
                );
                Channel tmpChannel = new Channel(
                        String.valueOf(((JSONObject) channel).get("channelName")),
                        tmpSettings
                );
                loadingChannels.add(tmpChannel);
            }
            log.info("All channels has been successfully read!");

            JSONObject tokens = (JSONObject) config.get("tokens");
            clientAppId = String.valueOf(tokens.get("clientAppId"));
            clientAppSecret = String.valueOf(tokens.get("clientAppSecret"));
            chatToken = String.valueOf(tokens.get("chatToken"));
            log.info("Tokens has been successfully read!");

            JSONObject modules = (JSONObject) config.get("modules");
            moderationEnable = (boolean) modules.get("moderation");
            customCommandsEnable = (boolean) modules.get("customCommands");
            currencyEnable = (boolean) modules.get("currency");
            betsEnable = (boolean) modules.get("bets");
            streamLiveEnable = (boolean) modules.get("streamLive");
            streamOfflineEnable = (boolean) modules.get("streamOffline");
            streamFollower = (boolean) modules.get("streamFollower");

            admin = String.valueOf(config.get("admin"));
            isLoadFromCfg = (boolean) config.get("loadFromCfg");
            log.info("Admin's nickname has been successfully read!");

        } else log.error("Error. Couldn't read the configuration!");
    }

    private static JSONObject readJsonFromFile(String fileName) {
        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(new FileReader(fileName));
            log.info("File has been successful read");
            return (JSONObject) obj;

        } catch (ParseException | IOException e) {
            log.error("Error!", e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Checks if bot's database is exists, if not - creates new one.
     */
    public static void checkOrCreateDatabaseFile() {
        File file = new File("database.db");
        if(file.exists()) {
            log.info("Database is found!");
        } else {
            log.info("Database is doesn't exists. Trying to create...");
            try {
                file.createNewFile();
                log.info("Database has been successfully created!");
            } catch (IOException e) {
                log.error("Couldn't create the database!", e.getMessage());
                e.printStackTrace();
            }
        }
        initDatabase();
    }

    private static void initDatabase() {
        createChannelsTable();
        log.info("Channels table has been loaded!");
        createAliasesTable();
        log.info("Aliases table has been loaded!");
        createCommandCooldownTable();
        log.info("Command cooldown table has been loaded!");
        // Остальные таблицы тут
    }

    private static void createChannelsTable() {
        try {
            DatabaseUtils.connect();
            String query = "CREATE TABLE IF NOT EXISTS `channels` (\n" +
                    "\t`id`\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,\n" +
                    "\t`channelName`\tTEXT NOT NULL UNIQUE,\n" +
                    "\t`accessToken`\tTEXT NOT NULL,\n" +
                    "\t`moderationEnabled`\tBOOLEAN NOT NULL DEFAULT false,\n" +
                    "\t`currencyEnabled`\tBOOLEAN NOT NULL DEFAULT false,\n" +
                    "\t`customCommandsEnabled`\tBOOLEAN NOT NULL DEFAULT false,\n" +
                    "\t`betsEnabled`\tBOOLEAN NOT NULL DEFAULT false\n" +
                    ");";
            Statement statement = DatabaseUtils.getConnection().createStatement();
            statement.execute(query);
            statement.close();
            DatabaseUtils.disconnect();

        } catch (SQLException e) {
            log.error("Error while creating channels table!", e.getMessage());
            e.printStackTrace();
        }
    }

    private static void createAliasesTable() {
        try {
            DatabaseUtils.connect();
            String query = "CREATE TABLE IF NOT EXISTS `aliases` (\n" +
                    "\t`id`\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,\n" +
                    "\t`name`\tTEXT NOT NULL,\n" +
                    "\t`command`\tTEXT NOT NULL,\n" +
                    "\t`channel`\tTEXT NOT NULL\n" +
                    ");";
            Statement statement = DatabaseUtils.getConnection().createStatement();
            statement.execute(query);
            statement.close();
            DatabaseUtils.disconnect();

        } catch (SQLException e) {
            log.error("Error while creating aliases table", e.getMessage());
            e.printStackTrace();
        }
    }

    private static void createCommandCooldownTable() {
        try {
            DatabaseUtils.connect();
            Statement statement = DatabaseUtils.getConnection().createStatement();
            String query = "CREATE TABLE IF NOT EXISTS `cooldowns` (\n" +
                    "\t`id`\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,\n" +
                    "\t`commandName`\tTEXT NOT NULL,\n" +
                    "\t`channelName`\tTEXT NOT NULL,\n" +
                    "\t`cooldown`\tINTEGER NOT NULL\n" +
                    ");";
            statement.execute(query);
            query = "CREATE TABLE IF NOT EXISTS `cooldownUsers` (\n" +
                    "\t`id`\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,\n" +
                    "\t`channelName`\tTEXT NOT NULL,\n" +
                    "\t`commandName`\tTEXT NOT NULL,\n" +
                    "\t`userName`\tTEXT NOT NULL,\n" +
                    "\t`lastTimeUse`\tTEXT NOT NULL\n" +
                    ");";
            statement.execute(query);
            statement.close();
            DatabaseUtils.disconnect();

        } catch (SQLException e) {
            log.error("Error while creating command cooldown table", e.getMessage());
            e.printStackTrace();
        }
    }

    //@SuppressWarnings("unchecked")
    public static boolean createConfigurationFile() {
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
        channel.put("moderationEnabled", true);
        channel.put("currencyEnabled", true);
        channel.put("customCommandsEnabled", true);
        channel.put("betsEnabled", true);
        channels.add(channel);
        obj.put("channels", channels);

        // For disabling modules for all channel.
        // Not implemented right now.
        JSONObject modules = new JSONObject();
        modules.put("currency", true);
        modules.put("moderation", true);
        modules.put("bets", true);
        modules.put("customCommands", true);
        modules.put("streamLive", true);
        modules.put("streamOffline", true);
        modules.put("streamFollower", true);
        obj.put("modules", modules);

        obj.put("admin", "putYourNicknameHere");
        obj.put("loadFromCfg", true);

        // Write to file
        try (FileWriter file = new FileWriter("config.json")) {
            file.write(obj.toJSONString());
            log.info("Configuration file 'config.json' has been generated.");
            file.flush();
            file.close();
            return true;
        } catch (IOException e) {
            log.error("Couldn't create configuration file.", e.toString());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Loads all channels in database from configuration file and creates table for users.
     * @see Channel
     */
    public static void loadChannels() {
        for(Channel channel : loadingChannels) {
            if (isLoadFromCfg)
                channel.addChannel(); // Saves configuration settings to database
            else
                channel.loadChannel(); // Loads configuration from database
            channel.createUsersTable();
        }
        log.info("Channels has been loaded!");
    }

    public static void loadCommands() {
        commandRegistry.put("error", new CommandErrorHandler());
        commandRegistry.put("test", new AbstractTestCommandHandler());
        commandRegistry.put("right", new ShowUserRightsHandle());
        commandRegistry.put("help", new HelpCommandHandle());
        commandRegistry.put("currency", new CurrencyStatusHandler());
        commandRegistry.put("quote", new QuoteHandle());
        commandRegistry.put("calc", new CalcCommandHandler());
        commandRegistry.put("alias", new AliasHandler());
        commandRegistry.put("roll", new SlotMachineCommandHandler());
        commandRegistry.put("encrypt", new EncryptCommandHandler());
        commandRegistry.put("cooldown", new CooldownCommandHandle());
    }

    public static Channel getChannelByName(String channelName) {
        for (Channel channel : loadingChannels) {
            if(channelName.equals(channel.getName()))
                return channel;
        }
        return new Channel("Channel not found", null);
    }
}
