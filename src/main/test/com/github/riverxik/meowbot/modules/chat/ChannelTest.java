package com.github.riverxik.meowbot.modules.chat;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ChannelTest {
    private Channel channel;
    private ChannelUser user;

    @Before
    public void setUp() throws Exception {
        channel = new Channel("murameowbot", new ChannelSettings("sdfs"));
        channel.addChannel();
        channel.createUsersTable();
        user = new ChannelUser("murameowbot", true, true, true, true);
    }

    @After
    public void cleanUp() throws Exception {
        channel = null;
        user = null;
    }

    @Test
    public void testIsContainUser() throws Exception {
        channel = new Channel("murameowbot", new ChannelSettings(false, true, false, false, "sdfdsf"));
        ChannelUser user2 = new ChannelUser("test2", false, true, false, false); // mod
        ChannelUser user3 = new ChannelUser("test3", false, false, true, false); // sub
        ChannelUser user4 = new ChannelUser("test4", false, false, false, true); // vip
        ChannelUser user5 = new ChannelUser("test5", false, false, false, false); // viewer

        channel.addUserToChannel(user);
        channel.addUserToChannel(user2);
        channel.addUserToChannel(user3);
        channel.addUserToChannel(user4);

        channel.updateAllUsersInDatabase();

        Assert.assertEquals(true, channel.isContainUser(user.getName()));
        Assert.assertEquals(false, channel.isContainUser(user5.getName()));
    }

    @Test
    public void testGetChannelUserByName() throws Exception {
        ChannelUser meowbotExpected = new ChannelUser("murameowbot", true, true, true, true);
        channel.addUserToChannel(meowbotExpected);
        ChannelUser meowbotUser = channel.getChannelUserByName("murameowbot");

        Assert.assertEquals(meowbotExpected, meowbotUser);
    }

    @Test
    public void testGettingUsersCount() throws Exception {
        channel.addUserToChannel(user);
        Assert.assertEquals(1, channel.getUsersCount());
    }

    @Test
    public void testIsCurrencyDisabled() throws Exception {
        try {
            channel.getSettings().setCurrencyEnabled(false);
            channel.addUserToChannel(user);
            channel.updateAllUsersInDatabase();
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }
}