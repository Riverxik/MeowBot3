package com.github.riverxik.meowbot;

import com.github.riverxik.meowbot.commands.*;
import com.github.riverxik.meowbot.database.Database;
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

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
public class Configuration {
    private static final Logger log = LoggerFactory.getLogger(Configuration.class);

    /** Debug mode */
    public static boolean debug = false;

    /** Admin's nickname */
    public static String admin;

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
    public static void loadConfiguration() {
        JSONObject config = readJsonFromFile("config.json");
        if(config != null) {
            log.info("Reading configuration...");
            JSONArray channels = (JSONArray) config.get("channels");
            for(Object channel : channels) {
                if(channel instanceof JSONObject) {
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
            betsEbable = (boolean) modules.get("bets");
            streamLiveEnable = (boolean) modules.get("streamLive");
            streamOfflineEnable = (boolean) modules.get("streamOffline");
            streamFollower = (boolean) modules.get("streamFollower");

            admin = String.valueOf(config.get("admin"));
            log.info("Admin's nickname has been successfully read!");

        } else log.error("Error. Couldn't read the configuration!");
    }

    private static JSONObject readJsonFromFile(String fileName) {
        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(new FileReader(fileName));
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
        if(file.exists() && !file.isDirectory()) {
            log.info("Database is found!");
        } else {
            log.info("Database is doesn't exists. Trying to create...");
            try {
                if(file.createNewFile())
                    log.info("Database has been successfully created!");
            } catch (IOException e) {
                log.error("Couldn't create the database!", e.getMessage());
                e.printStackTrace();
            }
        }
        initDatabase();
    }

    private static void initDatabase() {
        if(createChannelsTable()) log.info("Channels table has been loaded!");
        // Остальные таблицы тут
    }

    private static boolean createChannelsTable() {
        try {
            Database database = new Database();
            database.connect();
            String query = "CREATE TABLE IF NOT EXISTS `channels` (\n" +
                    "\t`id`\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,\n" +
                    "\t`channelName`\tTEXT NOT NULL UNIQUE,\n" +
                    "\t`accessToken`\tTEXT NOT NULL,\n" +
                    "\t`moderationEnabled`\tBOOLEAN NOT NULL DEFAULT false,\n" +
                    "\t`currencyEnabled`\tBOOLEAN NOT NULL DEFAULT false,\n" +
                    "\t`customCommandsEnabled`\tBOOLEAN NOT NULL DEFAULT false,\n" +
                    "\t`betsEnabled`\tBOOLEAN NOT NULL DEFAULT false\n" +
                    ");";
            Statement statement = database.getConnection().createStatement();
            statement.execute(query);
            statement.close();
            database.disconnect();
            return true;

        } catch (SQLException e) {
            log.error("Error while creating channels table!", e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public static void createConfigurationFile(Bot bot) {
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
            log.info("Configuration file 'config.json' has been generated.");
            bot.say("Configuration file 'config.json' has been generated. Please fill it out ;)");
            file.flush();
            file.close();
            java.lang.System.exit(0);
        } catch (IOException e) {
            bot.say("Something wrong. Couldn't create configuration file.");
            log.error("Couldn't create configuration file.", e.toString());
            e.printStackTrace();
            java.lang.System.exit(0);
        }
    }

    /**
     * Loads all channels in database from configuration file and creates table for users.
     * @see Channel
     */
    public static void loadChannels() {
        for(Channel channel : loadingChannels) {
            channel.addChannel();
            channel.createUsersTable();
        }
        log.info("Channels has been loaded!");
    }

    public static void loadCommands() {
        commandRegistry.put("error", new CommandErrorHandler());
        commandRegistry.put("test", new TestAbstractCommandHandler());
        commandRegistry.put("right", new ShowUserRightsHandle());
        commandRegistry.put("help", new HelpCommandHandle());
        commandRegistry.put("currency", new CurrencyStatusHandler());
        commandRegistry.put("quote", new QuoteHandle());
    }

    public static Channel getChannelByName(String channelName) {
        for (Channel channel : loadingChannels) {
            if(channelName.equals(channel.getName()))
                return channel;
        }
        return new Channel("Channel not found", null);
    }
}
