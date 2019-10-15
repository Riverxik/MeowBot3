package com.github.riverxik.meowbot.modules;

import com.github.riverxik.meowbot.ConfigurationUtils;
import org.junit.Assert;
import org.junit.Test;

public class TwitchBotHelperTest {

    @Test
    public void testSendMessageToChat() throws Exception {
        try {
            ConfigurationUtils.loadConfiguration("config.json");
            ConfigurationUtils.checkOrCreateDatabaseFile();
            ConfigurationUtils.loadChannels();
            ConfigurationUtils.loadCommands();
            TwitchBotHelper.initialize();
            TwitchBotHelper.registerFeatures();
            TwitchBotHelper.start();
            TwitchBotHelper.sendMessageToChat(ConfigurationUtils.loadingChannels.get(0).getName(), "Meow");
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }
}