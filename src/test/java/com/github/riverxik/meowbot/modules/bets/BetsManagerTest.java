package com.github.riverxik.meowbot.modules.bets;

import com.github.riverxik.meowbot.Configuration;
import com.github.riverxik.meowbot.commands.Command;
import com.github.riverxik.meowbot.commands.fsa.Lexer;
import com.github.riverxik.meowbot.commands.fsa.Parser;
import com.github.riverxik.meowbot.database.Database;
import com.github.riverxik.meowbot.modules.chat.Channel;
import com.github.riverxik.meowbot.modules.chat.ChannelUser;
import com.github.riverxik.meowbot.modules.currency.CurrencyManager;
import com.github.riverxik.meowbot.modules.quotes.QuotesManager;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Statement;


public class BetsManagerTest {
    private String channelName;
    private String userName;

    @Before
    public void setUp() throws Exception {
        Configuration.loadConfiguration();
        Configuration.checkOrCreateDatabaseFile();
        Configuration.loadChannels();

        QuotesManager quotesManager = new QuotesManager();
        BetsManager betsManager = new BetsManager();

        channelName = "byterbrodtv";
        userName = "irinafox1";
    }

    @After
    public void cleanUp() throws Exception {
        Database database = new Database();
        database.connect();
        Statement statement = database.getConnection().createStatement();

        String query = "DROP TABLE IF EXISTS lots";
        statement.execute(query);

        statement.close();
        database.disconnect();
    }

    /**
     * !lot add with wrong params
     * @throws Exception
     */
    @Test
    public void testProcessLotCommand() throws Exception {
        Assert.assertEquals("Can't create lot. You haven't access or write wrong parameters!",
                getLotCommandByUserMessage("byterbrodtv", "byterbrodtv", "!lot add"));
    }

    @Test
    public void testProcessLotAddCommand_CANT_ACCESS() throws Exception {
        ChannelUser user = new ChannelUser(userName, false, false, false);
        Configuration.getChannelByName(channelName).removeAllUsersFromChannel();
        Configuration.getChannelByName(channelName).addUserToChannel(user);

        Assert.assertEquals("Can't create lot. You haven't access or write wrong parameters!",
                getLotCommandByUserMessage(channelName, userName, "!lot add \"Caption\" 10 100"));
    }

    @Test
    public void testProcessLotAddCommand_OK() throws Exception {
        ChannelUser user = new ChannelUser("irinafox1", true, true, false);
        Configuration.getChannelByName(channelName).removeAllUsersFromChannel();
        Configuration.getChannelByName(channelName).addUserToChannel(user);

        int nextLogId = BetsManager.getNextChannelLotId(channelName);

        Assert.assertEquals("Lot #"+nextLogId+" has been created: Caption. You can bet between: 10 and 100",
                getLotCommandByUserMessage(channelName, userName, "!lot add \"Caption\" 10 100"));
    }

    @Test
    public void testProcessLotHelpCommand_OK() throws Exception {
        Assert.assertEquals("Available commands: lot add, lot close, lot price, lot list, lot info",
                getLotCommandByUserMessage(channelName, userName, "!lot"));
    }

    @Test
    public void testProcessLotAddCommand_WRONG_RANGE() throws Exception {
        ChannelUser user = new ChannelUser(userName, true, false, false);
        Configuration.getChannelByName(channelName).removeAllUsersFromChannel();
        Configuration.getChannelByName(channelName).addUserToChannel(user);

        Assert.assertEquals("Minimal bet must me less that maximal",
                getLotCommandByUserMessage(channelName, userName, "!lot add \"Caption\" 100 10"));
    }

    @Test
    public void testProcessLotAddCommand_NEGATIVE_INT() throws Exception {
        ChannelUser user = new ChannelUser(userName, true, false, false);
        Configuration.getChannelByName(channelName).removeAllUsersFromChannel();
        Configuration.getChannelByName(channelName).addUserToChannel(user);

        Assert.assertEquals("Range numbers must be positive",
                getLotCommandByUserMessage(channelName, userName, "!lot add \"Caption\" (-10) 10"));
    }

    @Test
    public void testProcessLotCloseCommand_OK() throws Exception {
        ChannelUser user = new ChannelUser(userName, true, false, false);
        Configuration.getChannelByName(channelName).removeAllUsersFromChannel();
        Configuration.getChannelByName(channelName).addUserToChannel(user);

        int lotId = BetsManager.getNextChannelLotId(channelName);
        BetsManager.addNewLot(channelName, lotId, "Test Caption", 10, 100);

        Assert.assertEquals(String.format("Lot #%d has been successfully closed!", lotId),
                getLotCommandByUserMessage(channelName, userName, String.format("!lot close %d", lotId)));
    }

    @Test
    public void testProcessLotCloseCommand_ALREADY_CLOSED() throws Exception {
        ChannelUser user = new ChannelUser(userName, true, false, false);
        Configuration.getChannelByName(channelName).removeAllUsersFromChannel();
        Configuration.getChannelByName(channelName).addUserToChannel(user);

        int lotId = BetsManager.getNextChannelLotId(channelName);
        BetsManager.addNewLot(channelName, lotId, "Test Caption", 10, 100);
        BetsManager.closeLot(channelName, lotId);

        Assert.assertEquals(String.format("Lot #%d already closed or not exists!", lotId),
                getLotCommandByUserMessage(channelName, userName, String.format("!lot close %d", lotId)));
    }

    @Test
    public void testProcessLotCloseCommand_CLOSE_NOT_EXISTS_LOT() throws Exception {
        ChannelUser user = new ChannelUser(userName, true, false, false);
        Configuration.getChannelByName(channelName).removeAllUsersFromChannel();
        Configuration.getChannelByName(channelName).addUserToChannel(user);

        int lotId = BetsManager.getNextChannelLotId(channelName);
        BetsManager.addNewLot(channelName, lotId, "Test Caption", 10, 100);

        Assert.assertEquals(String.format("Lot #%d already closed or not exists!", lotId+1),
                getLotCommandByUserMessage(channelName, userName, String.format("!lot close %d", lotId+1)));
    }

    @Test
    public void testProcessMoneyCommand() throws Exception {
        // TODO: NOT WORKING BECAUSE IT'S TRYING TO GET CHATTERS FROM TWITCH AND GOT NULL

        ChannelUser user = new ChannelUser(userName, true, false, false);
        Channel channel = Configuration.getChannelByName(channelName);
        channel.removeAllUsersFromChannel();
        channel.addUserToChannel(user);

        Object[] params = {"give", userName, 100};
        Command command = new Command("money", params);
        BetsManager.processMoneyCommand(channelName, Configuration.admin, command);

        Object[] params2 = {userName};
        Command command1 = new Command("money", params2);
        String expected = BetsManager.processMoneyCommand(channelName, Configuration.admin, command1);

        Assert.assertEquals(expected, getMoneyCommandByUserMessage(channelName, userName, "!money"));
    }

    private String getLotCommandByUserMessage(String channelName, String sender, String message) {
        Lexer lexer = new Lexer(message);
        lexer.tokenize();
        Parser parser = new Parser(lexer.getTokenList());
        parser.start(false);

        int sizeOfStackValues = parser.stackValues.size();
        String commandName = parser.stackValues.get(0).toString();

        Object[] params = new Object[sizeOfStackValues];
        for (int i = 1; i < sizeOfStackValues; i++) {
            params[i-1] = parser.stackValues.get(i);
        }
        Command command = new Command(commandName, params);
        return BetsManager.processLotCommand(channelName, sender, command);
    }

    private String getMoneyCommandByUserMessage(String channelName, String sender, String message) {
        Lexer lexer = new Lexer(message);
        lexer.tokenize();
        Parser parser = new Parser(lexer.getTokenList());
        parser.start(false);

        int sizeOfStackValues = parser.stackValues.size();
        String commandName = parser.stackValues.get(0).toString();

        Object[] params = new Object[sizeOfStackValues];
        for (int i = 1; i < sizeOfStackValues; i++) {
            params[i-1] = parser.stackValues.get(i);
        }
        Command command = new Command(commandName, params);
        return BetsManager.processMoneyCommand(channelName, sender, command);
    }

//    @Test
//    public void testProcessBetCommand() throws Exception {
//
//    }
}