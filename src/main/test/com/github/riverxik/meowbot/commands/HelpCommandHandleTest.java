package com.github.riverxik.meowbot.commands;

import com.github.riverxik.meowbot.Configuration;
import com.github.riverxik.meowbot.modules.TwitchBot;
import com.github.twitch4j.chat.TwitchChat;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class HelpCommandHandleTest {
    private String channel = null;
    private String sender = null;
    private TwitchChat chat = null;

    @Before
    public void setUp() throws Exception {
        Configuration.loadConfiguration("config.json");
        Configuration.checkOrCreateDatabaseFile();
        Configuration.loadChannels();
        Configuration.loadCommands();

        TwitchBot.initialize();
        TwitchBot.registerFeatures();
        TwitchBot.start();

        channel = Configuration.loadingChannels.get(0).getName();
        sender = Configuration.admin;
        chat = TwitchBot.getTwitchClient().getChat();
    }

    @After
    public void tearDown() throws Exception {
        channel = null;
        sender = null;
        chat = null;
    }

    @Test
    public void testExecute() throws Exception {
        try {
            new HelpCommandHandle().execute(channel, sender, null, chat);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }
}