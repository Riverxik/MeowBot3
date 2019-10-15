package com.github.riverxik.meowbot.commands;

import com.github.riverxik.meowbot.ConfigurationUtils;
import com.github.riverxik.meowbot.modules.TwitchBotHelper;
import com.github.twitch4j.chat.TwitchChat;
import org.junit.Assert;
import org.junit.Test;

public class AbstractTestCommandHandlerTest {

    @Test
    public void testExecute() throws Exception {
        try {
            ConfigurationUtils.loadConfiguration("config.json");
            ConfigurationUtils.loadCommands();
            ConfigurationUtils.loadChannels();

            TwitchBotHelper.initialize();
            TwitchBotHelper.registerFeatures();
            TwitchBotHelper.start();

            String channel = ConfigurationUtils.loadingChannels.get(0).getName();
            String sender = ConfigurationUtils.admin;
            Object[] args = {"test", 12};
            TwitchChat chat = TwitchBotHelper.getTwitchClient().getChat();

            new AbstractTestCommandHandler().execute(channel, sender, args, chat);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }
}