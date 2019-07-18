package com.github.riverxik.meowbot;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ConfigurationTest {
    private int i;
    List<ILoggingEvent> logList;

    @Before
    public void setUp() throws Exception {
        i = 0;
        Logger log = (Logger) LoggerFactory.getLogger(Configuration.class);
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        log.addAppender(listAppender);
        logList = listAppender.list;
        Configuration.loadConfiguration();
    }

    @Test
    public void testIsCurrencyEnable() throws Exception {
        Assert.assertEquals(true, Configuration.isCurrencyEnable());
    }

    @Test
    public void testIsModerationEnable() throws Exception {
        Assert.assertEquals(true, Configuration.isModerationEnable());
    }

    @Test
    public void testIsCustomCommandsEnable() throws Exception {
        Assert.assertEquals(true, Configuration.isCustomCommandsEnable());
    }

    @Test
    public void testIsBetsEnable() throws Exception {
        Assert.assertEquals(true, Configuration.isBetsEnable());
    }

    @Test
    public void testIsStreamLiveEnable() throws Exception {
        Assert.assertEquals(true, Configuration.isStreamLiveEnable());
    }

    @Test
    public void testIsStreamOfflineEnable() throws Exception {
        Assert.assertEquals(true, Configuration.isStreamOfflineEnable());
    }

    @Test
    public void testIsStreamFollower() throws Exception {
        Assert.assertEquals(true, Configuration.isStreamFollower());
    }

    @Test
    public void testLoadConfiguration() throws Exception {
        Assert.assertEquals("File has been successful read", logList.get(i).getMessage());
        Assert.assertEquals(Level.INFO, logList.get(i++).getLevel());

        Assert.assertEquals("Reading configuration...", logList.get(i).getMessage());
        Assert.assertEquals(Level.INFO, logList.get(i++).getLevel());

        Assert.assertEquals("All channels has been successfully read!", logList.get(i).getMessage());
        Assert.assertEquals(Level.INFO, logList.get(i++).getLevel());

        Assert.assertEquals("Tokens has been successfully read!", logList.get(i).getMessage());
        Assert.assertEquals(Level.INFO, logList.get(i++).getLevel());

        Assert.assertEquals("Admin's nickname has been successfully read!", logList.get(i).getMessage());
        Assert.assertEquals(Level.INFO, logList.get(i).getLevel());
    }

    @Test
    public void testLoadConfigurationWhereConfigIsNull() throws Exception {
        Assert.assertNotEquals("Error!", logList.get(i).getMessage());
        Assert.assertNotEquals(Level.ERROR, logList.get(i).getLevel());

        Assert.assertNotEquals("Error. Couldn't read the configuration!", logList.get(i).getMessage());
        Assert.assertNotEquals(Level.ERROR, logList.get(i).getLevel());
    }

    @Test
    public void testLoadConfigurationWhereChannelIsNotJsonObject() throws Exception {
        Assert.assertEquals("File has been successful read", logList.get(i).getMessage());
        Assert.assertEquals(Level.INFO, logList.get(i++).getLevel());

        Assert.assertEquals("Reading configuration...", logList.get(i).getMessage());
        Assert.assertEquals(Level.INFO, logList.get(i++).getLevel());

        Assert.assertNotEquals("Illegal JSON file!", logList.get(i).getMessage());
        Assert.assertNotEquals(Level.ERROR, logList.get(i).getLevel());

    }

    @Test
    public void testCheckOrCreateDatabaseFile() throws Exception {

    }

    @Test
    public void testCreateConfigurationFile() throws Exception {

    }

    @Test
    public void testLoadChannels() throws Exception {

    }

    @Test
    public void testLoadCommands() throws Exception {

    }

    @Test
    public void testGetChannelByName() throws Exception {

    }
}