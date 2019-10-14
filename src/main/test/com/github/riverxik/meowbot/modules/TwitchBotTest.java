package com.github.riverxik.meowbot.modules;

import com.github.riverxik.meowbot.Configuration;
import org.junit.Assert;
import org.junit.Test;

public class TwitchBotTest {

    @Test
    public void testSendMessageToChat() throws Exception {
        try {
            Configuration.loadConfiguration("config.json");
            Configuration.checkOrCreateDatabaseFile();
            Configuration.loadChannels();
            Configuration.loadCommands();
            TwitchBot.initialize();
            TwitchBot.registerFeatures();
            TwitchBot.start();
            TwitchBot.sendMessageToChat(Configuration.loadingChannels.get(0).getName(), "Meow");
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }
}