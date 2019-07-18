package com.github.riverxik.meowbot.modules.chat;

import com.github.riverxik.meowbot.modules.quotes.ChannelQuotes;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ChannelSettingsTest {
    private ChannelSettings settings;
    private Channel channel;

    @Before
    public void setUp() throws Exception {
        settings = new ChannelSettings(true, true, true, true, "fdsf");
        channel = new Channel("test", settings);
    }

    @After
    public void tearDown() throws Exception {
        settings = null;
        channel = null;
    }

    @Test
    public void testGetQuotes() throws Exception {
        ChannelQuotes testChannelQuote = new ChannelQuotes("test");
        channel.getSettings().setQuotes(testChannelQuote);

        Assert.assertEquals(testChannelQuote, settings.getQuotes());
    }

    @Test
    public void testSetModerationEnabled() throws Exception {
        settings.setModerationEnabled(true);
        Assert.assertEquals(true, settings.isModerationEnabled());
        settings.setModerationEnabled(false);
        Assert.assertEquals(false, settings.isModerationEnabled());
    }

    @Test
    public void testSetCurrencyEnabled() throws Exception {
        settings.setCurrencyEnabled(true);
        Assert.assertEquals(true, settings.isCurrencyEnabled());
        settings.setCurrencyEnabled(false);
        Assert.assertEquals(false, settings.isCurrencyEnabled());
    }

    @Test
    public void testSetBetsEnabled() throws Exception {
        settings.setBetsEnabled(true);
        Assert.assertEquals(true, settings.isBetsEnabled());
        settings.setBetsEnabled(false);
        Assert.assertEquals(false, settings.isBetsEnabled());
    }

    @Test
    public void testSetCustomCommandsEnabled() throws Exception {
        settings.setCustomCommandsEnabled(true);
        Assert.assertEquals(true, settings.isCustomCommandsEnabled());
        settings.setCustomCommandsEnabled(false);
        Assert.assertEquals(false, settings.isCustomCommandsEnabled());
    }
}