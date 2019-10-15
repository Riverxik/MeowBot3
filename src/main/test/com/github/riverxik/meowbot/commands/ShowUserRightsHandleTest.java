package com.github.riverxik.meowbot.commands;

import com.github.riverxik.meowbot.ConfigurationUtils;
import com.github.riverxik.meowbot.modules.TwitchBotHelper;
import com.github.riverxik.meowbot.modules.chat.Channel;
import com.github.riverxik.meowbot.modules.chat.ChannelSettings;
import com.github.riverxik.meowbot.modules.chat.ChannelUser;
import com.github.twitch4j.chat.TwitchChat;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ShowUserRightsHandleTest {
    private String channelName = null;
    private String senderName = null;
    private TwitchChat chat = null;

    @Before
    public void setUp() throws Exception {
        ConfigurationUtils.loadConfiguration("config.json");
        ConfigurationUtils.checkOrCreateDatabaseFile();
        ConfigurationUtils.loadChannels();
        ConfigurationUtils.loadCommands();

        TwitchBotHelper.initialize();
        TwitchBotHelper.registerFeatures();
        TwitchBotHelper.start();

        channelName = ConfigurationUtils.loadingChannels.get(0).getName();
        senderName = ConfigurationUtils.admin;
        chat = TwitchBotHelper.getTwitchClient().getChat();
    }

    @After
    public void tearDown() throws Exception {
        channelName = null;
        senderName = null;
        chat = null;
    }
    @Test
    public void testExecute() throws Exception {
        try {
            Object[] args = {senderName};
            Object[] argsEmpty = {};
            ChannelSettings settings = new ChannelSettings("sdfsdf");
            Channel channel = new Channel(channelName, settings);
            ChannelUser owner = new ChannelUser(senderName, true, true, true, true);
            ChannelUser viewer = new ChannelUser(senderName, false, false, false, false);

            channel.addUserToChannel(owner);
            new ShowUserRightsHandle().execute(channelName, senderName, args, chat);
            new ShowUserRightsHandle().execute(channelName, senderName, argsEmpty, chat);
            channel.removeAllUsersFromChannel();
            channel.addUserToChannel(viewer);
            new ShowUserRightsHandle().execute(channelName, senderName, args, chat);
            new ShowUserRightsHandle().execute(channelName, senderName, argsEmpty, chat);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }
}