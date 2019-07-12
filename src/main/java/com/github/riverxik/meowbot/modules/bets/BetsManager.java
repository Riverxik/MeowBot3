package com.github.riverxik.meowbot.modules.bets;

import com.github.riverxik.meowbot.Configuration;
import com.github.riverxik.meowbot.commands.Command;
import com.github.riverxik.meowbot.database.Database;
import com.github.riverxik.meowbot.modules.CommandManager;
import com.github.riverxik.meowbot.modules.ErrorCodes;
import com.github.riverxik.meowbot.modules.chat.Channel;
import com.github.riverxik.meowbot.modules.chat.ChannelUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BetsManager {
    private static final Logger log = LoggerFactory.getLogger(BetsManager.class);

    public BetsManager() {
        createTables();
        loadChannelBets();
    }

    public static String processLotCommand(String channelName, String sender, Command command) {
        if(Configuration.getChannelByName(channelName).getSettings().isBetsEnabled()) {
            int paramLength = command.getParameters().length - 1;
            if (paramLength == 0) {
                return "Available commands: lot add, lot close, lot price, lot list, lot info";
            }
            if (paramLength < 5) {
                Object[] objParam = command.getParameters();
                if (objParam[0] instanceof String) {
                    String commandName = String.valueOf(objParam[0]);
                    if (hasUserAccess(channelName, commandName, sender) && isParamValid(objParam)) {
                        switch (commandName) {
                            case "add": {
                                int lotId = getNextChannelLotId(channelName);
                                String caption = objParam[1].toString();
                                int min = (int) objParam[2];
                                int max = (int) objParam[3];
                                if (min < 0 || max < 0) return "Range numbers must be positive";
                                if (min > max) return "Minimal bet must me less that maximal";
                                if (addNewLot(channelName, lotId, caption, min, max))
                                    return String.format("Lot #%d has been created: %s. You can bet between: %d and %d", lotId, caption, min, max);
                                else
                                    return "Error while creating lot!";
                            }
                            case "close": {
                                int lotId = (int) objParam[1];
                                if (canCloseLot(channelName, lotId)) {
                                    if (closeLot(channelName, lotId))
                                        return String.format("Lot #%d has been successfully closed!", lotId);
                                    else
                                        return "Error while closing lot!";
                                } else return String.format("Lot #%d already closed or not exists!", lotId);
                            }
                        }
                    }
                    return "Can't create lot. You haven't access or write wrong parameters!";
                } else return ErrorCodes.ILLEGAL_PARAMETER_MUST_BE_STRING.getInfo();
            }
            return ErrorCodes.ILLEGAL_NUMBER_OF_PARAMETERS.getInfo();

        } else return ErrorCodes.DISABLED_BETS.getInfo();
    }

    public static String processMoneyCommand(String channelName, String sender, Command command) {
        if(Configuration.getChannelByName(channelName).getSettings().isBetsEnabled()) {
            int paramLength = command.getParameters().length - 1;
            if (paramLength == 0) {
                return String.format("Your balance: %d", getUserMoney(channelName, sender));
            }
            if (paramLength == 1) {
                Object[] objParam = command.getParameters();
                if (objParam[0] instanceof String) {
                    String userName = String.valueOf(objParam[0]);
                    return String.format("%s balance: %d", userName, getUserMoney(channelName, userName));

                } else return ErrorCodes.ILLEGAL_PARAMETER_MUST_BE_STRING.getInfo();
            }
            if (paramLength == 2) {
                Object[] objParam = command.getParameters();
                if (objParam[0] instanceof String && "give".equals(objParam[0].toString())
                        && objParam[1] instanceof String) {
                    String userName = objParam[1].toString();
                    if (CommandManager.isValidInteger(objParam[2])) {
                        int amount = (int) objParam[2];
                        if (giveMoneyToUser(channelName, userName, amount))
                            return String.format("%d money has been added to %s account", amount, userName);
                        else return "Error while adding money!";
                    } else ErrorCodes.ILLEGAL_NUMBER.getInfo();

                } else ErrorCodes.ILLEGAL_PARAMETER_MUST_BE_STRING.getInfo();
            }
            return ErrorCodes.ILLEGAL_NUMBER_OF_PARAMETERS.getInfo();
        } else return ErrorCodes.DISABLED_BETS.getInfo();
    }

    private static boolean giveMoneyToUser(String channelName, String userName, int amount) {
        Database database = new Database();
        database.connect();
        try {
            Statement statement = database.getConnection().createStatement();
            String query =
                    "UPDATE ["+channelName+"] SET `money` = '"+amount+"' WHERE `userName` = '"+userName+"'";
            statement.executeUpdate(query);
            statement.close();
            database.disconnect();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("SQL: " + e.getMessage());
        }
        return false;
    }

    private static int getUserMoney(String channelName, String sender) {
        int userMoney = 0;
        Database database = new Database();
        database.connect();
        try {
            Statement statement = database.getConnection().createStatement();
            String query = "SELECT `money` FROM ["+channelName+"] WHERE `userName` = '"+sender+"'";
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                userMoney = resultSet.getInt("money");
            }
            statement.close();
            database.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("SQL: " + e.getMessage());
        }
        return userMoney;
    }

    private static boolean canCloseLot(String channelName, int lotId) {
        Database database = new Database();
        database.connect();
        try {
            Statement statement = database.getConnection().createStatement();
            String query = "SELECT `lotStatus` FROM [lots] " +
                    "WHERE `channelName` = '"+channelName+"' " +
                    "AND `lotId` = '"+lotId+"'";
            ResultSet resultSet = statement.executeQuery(query);
            String status = "Close";
            if (resultSet.next()) {
                status = resultSet.getString("lotStatus");
            }
            statement.close();
            database.disconnect();
            return !"Close".equals(status);
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("SQL: " + e.getMessage());
        }
        return false;
    }

    private static boolean isParamValid(Object[] param) {
        switch (param[0].toString()) {
            case "add": {
                return param[1] != null && param[1] instanceof String
                       && param[2] != null && param[2] instanceof Integer
                       && param[3] != null && param[3] instanceof Integer;
            }
            case "close": {
                return param[1] != null && param[1] instanceof Integer;
            }
            case "price": {
                return param[1] != null && param[1] instanceof Integer
                        && param[2] != null && param[2] instanceof Integer;
            }
        }
        return false;
    }

    private static boolean hasUserAccess(String channelName, String commandName, String userName) {
        switch (commandName) {
            case "add":
            case "close":
            case "price": {
                ChannelUser user = Configuration.getChannelByName(channelName).getChannelUserByName(userName);
                return channelName.equals(userName) || Configuration.admin.equals(userName) || user.isMod();
            }
            case "list" :
            case "info" :
                return true;
            default:
                return false;
        }
    }

    public static String processBetCommand(String channelName, String sender, Command command) {
        if(Configuration.getChannelByName(channelName).getSettings().isBetsEnabled()) {

        }
        return null;
    }

    public static int getNextChannelLotId(String channelName) {
        Database database = new Database();
        database.connect();
        try {
            Statement statement = database.getConnection().createStatement();
            String query = "SELECT `lotId` FROM `lots` WHERE `channelName` = '"+channelName+"' " +
                    "ORDER BY `lotId` DESC";
            ResultSet resultSet = statement.executeQuery(query);
            int lotId = 0;
            if (resultSet.next()) {
                lotId = resultSet.getInt("lotId");
            }
            statement.close();
            database.disconnect();
            return ++lotId;
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("SQL: " + e.getMessage());
        }
        return 1;
    }

    public static boolean addNewLot(String channelName, int lotId, String caption, int min, int max) {
        Database database = new Database();
        database.connect();
        try {
            Statement statement = database.getConnection().createStatement();
            String query =
                    "INSERT INTO `lots` (`channelName`, `lotId`, `lotCaption`, " +
                            "`lotStatus`, `lotBank`, `minBet`, `maxBet`) " +
                    "VALUES ('"+channelName+"', '"+lotId+"', '"+caption+"', " +
                            "'Open', '0', '"+min+"', '"+max +"' )";
            statement.execute(query);
            statement.close();
            database.disconnect();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("SQL: " + e.getMessage());
        }
        return false;
    }

    public static boolean closeLot(String channelName, int lotId) {
        Database database = new Database();
        database.connect();
        try {
            Statement statement = database.getConnection().createStatement();
            String query = "UPDATE [lots] SET `lotStatus` = 'Close' " +
                    "WHERE `channelName` = '"+channelName+"'" +
                    "AND `lotId` = '"+lotId+"' ";
            statement.execute(query);
            statement.close();
            database.disconnect();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("SQL: " + e.getMessage());
        }
        return false;
    }

    public static void addNewBet(String channelName, String userName, int lotId, int score, int amount) {
        Database database = new Database();
        database.connect();
        try {
            Statement statement = database.getConnection().createStatement();
            String query = "INSERT INTO [bets] (`channelName`, `lotId`, `userName`, `amount`, `score`) " +
                    "VALUES ('"+channelName+"', " +
                    "'"+lotId+"', " +
                    "'"+userName+"', " +
                    "'"+amount+"', " +
                    "'"+score+"' " +
                    ")";
            statement.execute(query);
            statement.close();
            database.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("SQL: " + e.getMessage());
        }
    }

    private void createTables() {
        Database database = new Database();
        database.connect();
        try {
            log.info("Looking for lots table...");
            Statement statement = database.getConnection().createStatement();
            String query = "CREATE TABLE IF NOT EXISTS [lots] (\n" +
                    "[id] INTEGER DEFAULT '0' NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                    "[channelName] VARCHAR(50) DEFAULT 'unknown' NOT NULL,\n" +
                    "[lotId] INTEGER  NOT NULL,\n" +
                    "[lotCaption] VARCHAR(500)  NOT NULL,\n" +
                    "[lotStatus] VARCHAR(20) NOT NULL, \n" +
                    "[lotBank] INTEGER NOT NULL, " +
                    "[minBet] INTEGER NOT NULL, " +
                    "[maxBet] INTEGER NOT NULL" +
                    ")";
            statement.execute(query);
            log.info("Lots table successfully created!");

            log.info("Looking for bets table...");
            query = "CREATE TABLE IF NOT EXISTS [bets] (\n" +
                    "[id] INTEGER DEFAULT '0' NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                    "[channelName] VARCHAR(50) NOT NULL, \n" +
                    "[lotId] INTEGER  NOT NULL,\n" +
                    "[userName] VARCHAR(50)  NOT NULL,\n" +
                    "[amount] INTEGER NOT NULL, " +
                    "[score] INTEGER NOT NULL" +
                    ")";
            statement.execute(query);
            log.info("Bets table successfully created!");

            statement.close();
            database.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("SQL: " + e.getMessage());
        }
    }

    private void loadChannelBets() {
        for (Channel channel : Configuration.loadingChannels) {
            channel.getSettings().setBets(new ChannelBets(channel.getName()));
        }
    }
}
