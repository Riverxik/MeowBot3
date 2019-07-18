package com.github.riverxik.meowbot;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

public class BotTest {
    List<ILoggingEvent> logList;
    Bot bot;

    @Before
    public void setUp() {
        Logger log = (Logger) LoggerFactory.getLogger(Bot.class);
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        log.addAppender(listAppender);
        logList = listAppender.list;
        bot = new Bot();
    }

    @After
    public void cleanUp() {
        bot = null;
    }

    @Test
    public void testBot() throws Exception {
        Assert.assertEquals("Launch", logList.get(0).getMessage());
        Assert.assertEquals(Level.INFO, logList.get(0).getLevel());

        Assert.assertEquals("Looking for configuration...", logList.get(1).getMessage());
        Assert.assertEquals(Level.INFO, logList.get(1).getLevel());

        Assert.assertEquals("Configuration has been found!", logList.get(2).getMessage());
        Assert.assertEquals(Level.INFO, logList.get(2).getLevel());

        Assert.assertNotEquals("Configuration file couldn't found!", logList.get(2).getMessage());
        Assert.assertNotEquals(Level.ERROR, logList.get(2).getLevel());

        Assert.assertEquals("Looking for database...", logList.get(3).getMessage());
        Assert.assertEquals(Level.INFO, logList.get(3).getLevel());

        Assert.assertEquals("Loading channels...", logList.get(4).getMessage());
        Assert.assertEquals(Level.INFO, logList.get(4).getLevel());

        Assert.assertEquals("Loading commands...", logList.get(5).getMessage());
        Assert.assertEquals(Level.INFO, logList.get(5).getLevel());

        Assert.assertEquals("Starting twitch-bot...", logList.get(6).getMessage());
        Assert.assertEquals(Level.INFO, logList.get(6).getLevel());
    }

    @Test
    public void testSay() throws Exception {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent)); // Setting output
        String test = "Hello";
        bot.say(test);
        Assert.assertEquals(String.format("[Meowbot] >>> [%s]\n", test), outContent.toString());
        System.setOut(originalOut); // restore default output
    }
}