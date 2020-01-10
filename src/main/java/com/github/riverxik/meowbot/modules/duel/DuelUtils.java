package com.github.riverxik.meowbot.modules.duel;

import com.github.riverxik.meowbot.ConfigurationUtils;
import com.github.riverxik.meowbot.database.DatabaseUtils;
import com.github.riverxik.meowbot.modules.chat.Channel;
import com.github.riverxik.meowbot.modules.currency.CurrencyManager;
import com.github.twitch4j.chat.TwitchChat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DuelUtils {

    private static final Logger log = LoggerFactory.getLogger(DuelUtils.class);
    private static final int DEFAULT_PRICE = 10;

    public static boolean throwDuelToUser(String channel, String sender, String username,
                                          Object[] subArgs, TwitchChat chat) {
        // Check all users is live
        Channel ch = ConfigurationUtils.getChannelByName(channel);
        String attacker = ch.getChannelUserByName(sender).getName();
        String defender = ch.getChannelUserByName(username).getName();
        if (!"User not found".equals(attacker) && !"User not found".equals(defender)) {
            if (!isUserInDuel(channel, attacker) && !isUserInDuel(channel, defender)) {
                int amount = DEFAULT_PRICE;
                if (subArgs.length > 0 && subArgs[0] instanceof Integer) {
                    amount = Integer.valueOf(subArgs[0].toString());
                    if (amount < 0)
                        amount = DEFAULT_PRICE;
                }
                // Check amount of currency sender & username
                if (canUserPlayDuel(channel, sender, amount) && canUserPlayDuel(channel, username, amount)) {
                    // Create new duel in database
                    if (createNewDuel(channel, sender, username, amount)) {
                        chat.sendMessage(channel,
                                String.format("%s initiate duel with %s. Send [!duel accept %s] to accept " +
                                                "or [!duel deny %s] to deny.",
                                        sender, username, sender, sender));
                        return true;
                    } else {
                        chat.sendMessage(channel, String.format("%s, Duel won't initiate because of error", sender));
                        log.error("Error with initializing duel. Something is broken");
                    }
                } else {
                    chat.sendMessage(channel,
                            String.format("%s, You or %s haven't enough currency to play. " +
                                    "Amount can't be more than half of max current users currency", sender, username));
                }
            } else {
                chat.sendMessage(channel,
                        String.format("%s, Can't initiate a new duel. Seems like users already in.", sender));
            }
        } else {
            chat.sendMessage(channel, String.format("%s, I don't see some of this players. Try again later ;)", sender));
        }
        return false;
    }

    public static boolean acceptUserDuel(String channel, String defender, TwitchChat chat) {
        // Check if duel is initiated
        if (isUserInDuel(channel, defender)) {
            String attacker = getDuelAttacker(channel, defender);
            // Roll
            int amount = getDuelAmount(channel, attacker, defender);
            int chance = (int) (Math.random() * 100);
            // Change user currency
            if (chance > 50) {
                int newWinnerCurrency = CurrencyManager.getUserCurrency(channel, attacker) + amount;
                int newLooserCurrency = CurrencyManager.getUserCurrency(channel, defender) - amount;
                CurrencyManager.setUserCurrency(channel, attacker, newWinnerCurrency);
                CurrencyManager.setUserCurrency(channel, defender, newLooserCurrency);
                chat.sendMessage(channel,
                        String.format("%s has been defeated by %s", defender, attacker));
            } else {
                int newWinnerCurrency = CurrencyManager.getUserCurrency(channel, defender) + amount;
                int newLooserCurrency = CurrencyManager.getUserCurrency(channel, attacker) - amount;
                CurrencyManager.setUserCurrency(channel, defender, newWinnerCurrency);
                CurrencyManager.setUserCurrency(channel, attacker, newLooserCurrency);
                chat.sendMessage(channel,
                        String.format("%s has been defeated by %s", attacker, defender));
            }
            // End duel
            endUserDuel(channel, attacker, defender, "end");
        } else {
            chat.sendMessage(channel,
                    String.format("%s, Can't accept duel because it doesn't initiated", defender));
        }
        return false;
    }

    public static boolean denyUserDuel(String channel, String defender, TwitchChat chat) {
        String attacker = getDuelAttacker(channel, defender);
        endUserDuel(channel, attacker, defender, "deny");
        chat.sendMessage(channel,
                String.format("%s, You denied duel from %s", defender, attacker));
        return false;
    }

    public static boolean cancelUserDuel(String channel, String attacker, TwitchChat chat) {
        String defender = getDuelDefender(channel, attacker);
        endUserDuel(channel, attacker, defender, "canceled");
        chat.sendMessage(channel,
                String.format("%s, You canceled your duel with %s", attacker, defender));
        return false;
    }

    private static boolean isUserInDuel(String channel, String name) {
        int amount = 0;
        try {
            DatabaseUtils.connect();
            Statement statement = DatabaseUtils.getConnection().createStatement();
            String query = "SELECT COUNT(*) FROM `duels`" +
                    "WHERE `channelName` = '"+channel+"' AND `userDefence` = '"+name+"' " +
                    "AND `state` = 'invite'";
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()){
                amount = resultSet.getInt(1);
            }
            resultSet.close();
            statement.close();
            DatabaseUtils.disconnect();
        } catch (SQLException e) {
            log.error("Error selecting user duel invites", e.getMessage());
            e.printStackTrace();
        }
        return amount != 0;
    }

    private static boolean createNewDuel(String channel, String sender, String username, int amount) {
        try {
            DatabaseUtils.connect();
            Statement statement = DatabaseUtils.getConnection().createStatement();
            String query = "INSERT INTO `duels` (`channelName`, `userAttack`, `userDefence`, `amount`, `state`) " +
                    "VALUES ('"+channel+"', '"+sender+"', '"+username+"', "+amount+", 'invite')";
            statement.execute(query);
            statement.close();
            DatabaseUtils.disconnect();
            return true;
        } catch (SQLException e) {
            log.error("Error creating new duel", e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    private static boolean canUserPlayDuel(String channel, String username, int amount) {
        return amount*2 < CurrencyManager.getUserCurrency(channel, username);
    }

    private static boolean endUserDuel(String channel, String attacker, String defender, String state) {
        try {
            DatabaseUtils.connect();
            Statement statement = DatabaseUtils.getConnection().createStatement();
            String query = "UPDATE `duels` SET `state` = '"+state+"' " +
                    "WHERE `channelName` = '"+channel+"' AND `userAttack` = '"+attacker+"' " +
                    "AND `userDefence` = '"+defender+"'";
            statement.executeUpdate(query);
            statement.close();
            DatabaseUtils.disconnect();
            return true;
        } catch (SQLException e) {
            log.error("Error ending duel to state " + state, e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    private static int getDuelAmount(String channel, String attacker, String defender) {
        int amount = 0;
        try {
            DatabaseUtils.connect();
            Statement statement = DatabaseUtils.getConnection().createStatement();
            String query = "SELECT `amount` FROM `duels`" +
                    "WHERE `channelName` = '"+channel+"' AND `userDefence` = '"+defender+"' " +
                    "AND `userAttack` = '"+attacker+"' AND `state` = 'invite'";
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()){
                amount = resultSet.getInt(1);
            }
            resultSet.close();
            statement.close();
            DatabaseUtils.disconnect();
        } catch (SQLException e) {
            log.error("Error getting duel amount", e.getMessage());
            e.printStackTrace();
        }
        return amount;
    }

    private static String getDuelAttacker(String channel, String defender) {
        String attacker = "null";
        try {
            DatabaseUtils.connect();
            Statement statement = DatabaseUtils.getConnection().createStatement();
            String query = "SELECT `userAttack` FROM `duels`" +
                    "WHERE `channelName` = '"+channel+"' AND `userDefence` = '"+defender+"' " +
                    "AND `state` = 'invite'";
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()){
                attacker = resultSet.getString("userAttack");
            }
            resultSet.close();
            statement.close();
            DatabaseUtils.disconnect();
        } catch (SQLException e) {
            log.error("Error getting duel attacker", e.getMessage());
            e.printStackTrace();
        }
        return attacker;
    }

    private static String getDuelDefender(String channel, String attacker) {
        String defender = "null";
        try {
            DatabaseUtils.connect();
            Statement statement = DatabaseUtils.getConnection().createStatement();
            String query = "SELECT `userDefence` FROM `duels`" +
                    "WHERE `channelName` = '"+channel+"' AND `userAttack` = '"+attacker+"' " +
                    "AND `state` = 'invite'";
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()){
                defender = resultSet.getString("userDefence");
            }
            resultSet.close();
            statement.close();
            DatabaseUtils.disconnect();
        } catch (SQLException e) {
            log.error("Error getting duel defender", e.getMessage());
            e.printStackTrace();
        }
        return defender;
    }
}
