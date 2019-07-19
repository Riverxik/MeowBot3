package com.github.riverxik.meowbot.modules.quotes.commands;

import com.github.riverxik.meowbot.Bot;
import com.github.riverxik.meowbot.Configuration;
import com.github.riverxik.meowbot.modules.TwitchBot;
import com.github.riverxik.meowbot.modules.chat.Channel;
import com.github.riverxik.meowbot.modules.chat.ChannelSettings;
import com.github.riverxik.meowbot.modules.chat.ChannelUser;
import com.github.twitch4j.chat.TwitchChat;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class QuoteHandleTest {
    private String senderName = null;
    private TwitchChat chat = null;

    @Before
    public void setUp() throws Exception {
        new Bot();
        senderName = Configuration.admin;
        chat = TwitchBot.getTwitchClient().getChat();
    }

    @Test
    public void testExecute() throws Exception {
        // TODO: Finish it
        ChannelUser owner = new ChannelUser(senderName, true, true, true, true);
        ChannelUser viewer = new ChannelUser(senderName, false, false, false, false);
        ChannelSettings settings = new ChannelSettings("");
        Channel channel = new Channel(senderName, settings);
        channel.createUsersTable();

        Object[] argsEmpty = {""};
        Object[] argsAdd = {"add", "test"};
        Object[] argsRemove = {"remove", 1};
        Object[] argsString = {"SomeString"};
        Object[] argsInteger = {1};
        Object[] argsIntegerCantFind = {Integer.MAX_VALUE};

        try {
            for (int i = 0; i < 2; i++) {
                if (i == 0) {
                    channel.addUserToChannel(owner);
                }
                if (i == 1) {
                    channel.removeAllUsersFromChannel();
                    channel.addUserToChannel(viewer);
                }
                // no args
                new QuoteHandle().execute(senderName, senderName, argsEmpty, chat);
                // add
                new QuoteHandle().execute(senderName, senderName, argsAdd, chat);
                // remove
                new QuoteHandle().execute(senderName, senderName, argsRemove, chat);
                // some string
                new QuoteHandle().execute(senderName, senderName, argsString, chat);
                // correct integer
                new QuoteHandle().execute(senderName, senderName, argsInteger, chat);
                // incorrect integer
                new QuoteHandle().execute(senderName, senderName, argsIntegerCantFind, chat);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @After
    public void tearDown() throws Exception {
        senderName = null;
        chat = null;
    }
}