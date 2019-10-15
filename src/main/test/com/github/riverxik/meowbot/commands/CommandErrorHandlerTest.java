package com.github.riverxik.meowbot.commands;

import com.github.riverxik.meowbot.ConfigurationUtils;
import com.github.riverxik.meowbot.modules.TwitchBotHelper;
import com.github.twitch4j.chat.TwitchChat;
import org.junit.Assert;
import org.junit.Test;

public class CommandErrorHandlerTest {

    @Test
    public void testExecute() throws Exception {
        try {
            ConfigurationUtils.loadConfiguration("config.json");
            ConfigurationUtils.checkOrCreateDatabaseFile();
            ConfigurationUtils.loadCommands();
            ConfigurationUtils.loadChannels();

            TwitchBotHelper.initialize();
            TwitchBotHelper.registerFeatures();
            TwitchBotHelper.start();

            String channel = ConfigurationUtils.loadingChannels.get(0).getName();
            String sender = ConfigurationUtils.admin;
            String anotherSender = "testName";
            Object[] args = {"error"};
            Object[] argsToggle = {"toggle"};
            TwitchChat chat = TwitchBotHelper.getTwitchClient().getChat();

            ConfigurationUtils.debug = false;
            // admin & !toggle & !debug
            new CommandErrorHandler().execute(channel, sender, args, chat);
            // admin & toggle & !debug
            new CommandErrorHandler().execute(channel, sender, argsToggle, chat);

            // admin & !toggle & debug
            new CommandErrorHandler().execute(channel, sender, args, chat);
            // admin & toggle & debug
            new CommandErrorHandler().execute(channel, sender, argsToggle, chat);
            // !admin & !toggle
            new CommandErrorHandler().execute(channel, anotherSender, args, chat);
            // !admin & toggle
            new CommandErrorHandler().execute(channel, anotherSender, argsToggle, chat);
            // Param[] is null
            new CommandErrorHandler().execute(channel, sender, null, chat);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }
}