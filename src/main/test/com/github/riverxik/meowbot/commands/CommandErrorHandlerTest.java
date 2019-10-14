package com.github.riverxik.meowbot.commands;

import com.github.riverxik.meowbot.Configuration;
import com.github.riverxik.meowbot.modules.TwitchBot;
import com.github.twitch4j.chat.TwitchChat;
import org.junit.Assert;
import org.junit.Test;

public class CommandErrorHandlerTest {

    @Test
    public void testExecute() throws Exception {
        try {
            Configuration.loadConfiguration("config.json");
            Configuration.checkOrCreateDatabaseFile();
            Configuration.loadCommands();
            Configuration.loadChannels();

            TwitchBot.initialize();
            TwitchBot.registerFeatures();
            TwitchBot.start();

            String channel = Configuration.loadingChannels.get(0).getName();
            String sender = Configuration.admin;
            String anotherSender = "testName";
            Object[] args = {"error"};
            Object[] argsToggle = {"toggle"};
            TwitchChat chat = TwitchBot.getTwitchClient().getChat();

            Configuration.debug = false;
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