package com.github.riverxik.meowbot;

import com.github.riverxik.meowbot.database.ChannelDb;
import com.github.riverxik.meowbot.database.ChannelUsers;
import com.github.riverxik.meowbot.database.Database;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
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

    /** Admin's nickname */
    public static String admin;

    /** Client's app id for this bot */
    public static String clientAppId;

    /** Client's app secret for this bot */
    public static String clientAppSecret;

    /** Chat token for twitch. Generates from: https://twitchapps.com/tmi/ */
    public static String chatToken;

    /** List of channels to connect */
    public static List<ChannelDb> loadingChannels = new ArrayList<>();

    /** Contains users for all channels.
     * @see ChannelUsers
     * */
    public static HashMap<String, ChannelUsers> channelUsersList = new HashMap<>();

    private static boolean moderationEnable = false; // TODO: use this for enabling/disabling features for all channels
    private static boolean cusomCommandsEnable = false;
    private static boolean currencyEnable = false;
    private static boolean betsEbable = false;
    private static boolean streamLiveEnable = false;
    private static boolean streamOfflineEnable = false;
    private static boolean streamFollower = false;

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
                    loadingChannels.add(
                            new ChannelDb(
                            String.valueOf(((JSONObject) channel).get("channelName")),
                            String.valueOf(((JSONObject) channel).get("accessToken")),
                            (boolean) ((JSONObject) channel).get("moderationEnabled"),
                            (boolean) ((JSONObject) channel).get("currencyEnabled"),
                            (boolean) ((JSONObject) channel).get("customCommandsEnabled"),
                            (boolean) ((JSONObject) channel).get("betsEnabled")
                            )
                    );
                }
            }
            log.info("All channels has been successfully read!");

            JSONObject tokens = (JSONObject) config.get("tokens");
            clientAppId = String.valueOf(tokens.get("clientAppId"));
            clientAppSecret = String.valueOf(tokens.get("clientAppSecret"));
            chatToken = String.valueOf(tokens.get("chatToken"));
            log.info("Tokens has been successfully read!");

            JSONObject modules = (JSONObject) config.get("modules");
            moderationEnable = Boolean.valueOf("moderation");
            cusomCommandsEnable = Boolean.valueOf("customCommands");
            currencyEnable = Boolean.valueOf("currency");
            betsEbable = Boolean.valueOf("bets");
            streamLiveEnable = Boolean.valueOf("streamLive");
            streamOfflineEnable = Boolean.valueOf("streamOffline");
            streamFollower = Boolean.valueOf("streamFollower");

            admin = String.valueOf(config.get("admin"));
            log.info("Admin's nickname has been successfully read!");

        } else log.error("Error. Couldn't read the configuration!");
    }

    private static JSONObject readJsonFromFile(String s) {
        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(new FileReader("config.json"));
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

    /**
     * Loads all channels in database from configuration file and creates table for users.
     * @see ChannelDb
     */
    public static void loadChannels() {
        for(ChannelDb channel : loadingChannels) {
            channel.addChannel();
            ChannelUsers users = new ChannelUsers();
            users.channelName = channel.getChannelName();
            users.createTable();
        }
        log.info("Channels has been loaded!");
    }
}
