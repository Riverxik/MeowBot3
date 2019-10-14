package com.github.riverxik.meowbot.commands;

import com.github.riverxik.meowbot.Configuration;
import com.github.riverxik.meowbot.modules.TwitchBot;
import com.github.twitch4j.chat.TwitchChat;
import org.junit.Assert;
import org.junit.Test;

public class AbstractTestCommandHandlerTest {

    @Test
    public void testExecute() throws Exception {
        try {
            Configuration.loadConfiguration("config.json");
            Configuration.loadCommands();
            Configuration.loadChannels();

            TwitchBot.initialize();
            TwitchBot.registerFeatures();
            TwitchBot.start();

            String channel = Configuration.loadingChannels.get(0).getName();
            String sender = Configuration.admin;
            Object[] args = {"test", 12};
            TwitchChat chat = TwitchBot.getTwitchClient().getChat();

            new AbstractTestCommandHandler().execute(channel, sender, args, chat);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }
}