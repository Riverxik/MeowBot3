package com.github.riverxik.meowbot.database;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Works with user tables in database.
 * @author RiVeRx
 * @version 1.0
 */
public class ChannelUsers {

    /** Name of the current channel */
    public String channelName;
    /** List of all viewers nicknames of current channel */
    public List<String> allViewers = new ArrayList<>();
    /** List of all moderator nicknames of current channel */
    public List<String> moderators = new ArrayList<>();
    /** List of all vip's nicknames of current channel */
    public List<String> vips = new ArrayList<>();

    /** Creates a table for new channel in database */
    public void createTable() {
        executeQuery("CREATE TABLE IF NOT EXISTS `"+channelName+"` (\n" +
                "\t`id`\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,\n" +
                "\t`userName`\tTEXT NOT NULL UNIQUE,\n" +
                "\t`currency`\tINTEGER NOT NULL DEFAULT 1,\n" +
                "\t`money`\tINTEGER NOT NULL\n" +
                ");");
    }

    /** Updates all user information of current channel */
    public void update() {
        try {
            Database database = new Database();
            database.connect();
            Statement statement = database.getConnection().createStatement();
            String query = "";
            for(String user : allViewers) {
                query = "replace into `murochka_ua` (`userName`, `currency`, `money`)\n" +
                        "values ('username', 'currency', 'money');";
            }
            // TODO: Finish the update users script.

            statement.close();
            database.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void executeQuery(String query) {
        try {
            Database database = new Database();
            database.connect();
            Statement statement = database.getConnection().createStatement();
            statement.execute(query);
            statement.close();
            database.disconnect();
        } catch (SQLException e) {
            // throws if table exists or something went wrong.
        }
    }
}
