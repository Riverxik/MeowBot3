package com.github.riverxik.meowbot.modules;

import com.github.riverxik.meowbot.ConfigurationUtils;
import com.github.riverxik.meowbot.commands.AbstractCommand;
import com.github.riverxik.meowbot.database.DatabaseUtils;
import com.github.twitch4j.chat.TwitchChat;
import com.github.twitch4j.helix.domain.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class SubOfTheDayHandler extends AbstractCommand{

    private static final Logger log = LoggerFactory.getLogger(SubOfTheDayHandler.class);

    /**
     * Syntax of command: !sod [<string>]
     * Description of command: !sod [<username>]
     * Description: Choose sub of the day or show it [Show how much times username was sub of the day]
     * */

    @Override
    public boolean execute(String channel, String sender, Object[] args, TwitchChat chat) {
        if(!"".equals(ConfigurationUtils.getChannelByName(channel).getSettings().getAccessToken())) {
            if (args.length > 0 && args[0] instanceof String) {
                String username = args[0].toString();
                // Show user stat
                int numberOfWins = getNumberOfWinsForUser(channel, username);
                if (sender.equals(username)) {
                    chat.sendMessage(channel,
                            String.format("%s, You was subscriber of the day %d times! SeemsGood", sender, numberOfWins));
                } else {
                    chat.sendMessage(channel,
                            String.format("%s, %s was subscriber of the day %d times! SeemsGood",
                                    sender, username, numberOfWins));
                }
            } else {
                String winner = getResultSubOfTheDay(channel);
                if (!"".equals(winner))
                    chat.sendMessage(channel, String.format("%s, Subscriber of the day is: GivePLZ %s TakeNRG",
                            sender, winner));
                else {
                    winner = getSubOfTheDay(channel);
                    chat.sendMessage(channel, "Scanning for lucky one...");
                    chat.sendMessage(channel,
                            String.format("%s, Subscriber of the day is: GivePLZ %s TakeNRG", sender, winner));
                    return true;
                }
            }
        } else {
            chat.sendMessage(channel,
                    String.format("%s, I'm sorry, but seems like this channel doesn't partner or" +
                    " access token is not specified", sender));
        }
        return false;
    }

    private int getNumberOfWinsForUser(String channel, String username) {
        int result = 0;
        try {
            DatabaseUtils.connect();
            Statement statement = DatabaseUtils.getConnection().createStatement();
            String query = "SELECT COUNT(*) FROM `subOfTheDay` " +
                    "WHERE `username` = '"+username+"' and `channelName` = '"+channel+"'";
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                result = resultSet.getInt(1);
            }
            resultSet.close();
            statement.close();
            DatabaseUtils.disconnect();
        } catch (SQLException e) {
            log.error("Error getting number of user wins", e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    private String getSubOfTheDay(String channel) {
        // Get all subscribers of the channel
        List<Subscription> list = ConfigurationUtils.getChannelByName(channel).getSubscribers();
        int lucky = (int) (Math.random() * list.size());
        // Choose the one
        String winner = list.get(lucky).getUserName();
        // Write into the database
        setResultSubOfTheDay(channel, winner);
        // Return
        return winner;
    }

    private void setResultSubOfTheDay(String channel, String username) {
        String today = ConfigurationUtils.getCurrentDateString();
        try {
            DatabaseUtils.connect();
            Statement statement = DatabaseUtils.getConnection().createStatement();
            String query = "INSERT INTO `subOfTheDay` (`channelName`, `username`, `date`) " +
                    "VALUES ('"+channel+"', '"+username+"', '"+today+"')";
            statement.execute(query);
            statement.close();
            DatabaseUtils.disconnect();
        } catch (SQLException e) {
            log.error("Error setting result of sub of the day", e.getMessage());
            e.printStackTrace();
        }
    }

    private String getResultSubOfTheDay(String channel) {
        String winner = "";
        String todayDate = ConfigurationUtils.getCurrentDateString();
        try {
            DatabaseUtils.connect();
            Statement statement = DatabaseUtils.getConnection().createStatement();
            String query = "SELECT `username` FROM `subOfTheDay` " +
                    "WHERE `date` = '"+todayDate+"' and `channelName` = '"+channel+"'";
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                winner = resultSet.getString("username");
            }
            resultSet.close();
            statement.close();
            DatabaseUtils.disconnect();
        } catch (SQLException e) {
            log.error("Error getting result of sub of the day", e.getMessage());
            e.printStackTrace();
        }
        return winner;
    }
}
