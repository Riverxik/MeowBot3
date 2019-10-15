package com.github.riverxik.meowbot;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.codehaus.plexus.util.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

public class ConfigurationUtilsTest {
    private int i;
    private List<ILoggingEvent> logList;

    @Before
    public void setUp() throws Exception {
        i = 0;
        Logger log = (Logger) LoggerFactory.getLogger(ConfigurationUtils.class);
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        log.addAppender(listAppender);
        logList = listAppender.list;
        ConfigurationUtils.loadConfiguration("config.json");
    }

    @Test
    public void testIsCurrencyEnable() throws Exception {
        Assert.assertEquals(true, ConfigurationUtils.isCurrencyEnable());
    }

    @Test
    public void testIsModerationEnable() throws Exception {
        Assert.assertEquals(true, ConfigurationUtils.isModerationEnable());
    }

    @Test
    public void testIsCustomCommandsEnable() throws Exception {
        Assert.assertEquals(true, ConfigurationUtils.isCustomCommandsEnable());
    }

    @Test
    public void testIsBetsEnable() throws Exception {
        Assert.assertEquals(true, ConfigurationUtils.isBetsEnable());
    }

    @Test
    public void testIsStreamLiveEnable() throws Exception {
        Assert.assertEquals(true, ConfigurationUtils.isStreamLiveEnable());
    }

    @Test
    public void testIsStreamOfflineEnable() throws Exception {
        Assert.assertEquals(true, ConfigurationUtils.isStreamOfflineEnable());
    }

    @Test
    public void testIsStreamFollower() throws Exception {
        Assert.assertEquals(true, ConfigurationUtils.isStreamFollower());
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
        ConfigurationUtils.loadConfiguration("not.exists");
        Assert.assertEquals("Error!", logList.get(5).getMessage());
        Assert.assertEquals(Level.ERROR, logList.get(5).getLevel());

        Assert.assertEquals("Error. Couldn't read the configuration!", logList.get(6).getMessage());
        Assert.assertEquals(Level.ERROR, logList.get(6).getLevel());
    }

    @Test
    public void testCheckOrCreateDatabaseFileWhenDatabaseNotExists() throws Exception {
        try {
            File file = new File("database.db");
            if (file.exists()) {
                file.delete();
            }
            ConfigurationUtils.checkOrCreateDatabaseFile();
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testCheckOrCreateDatabaseFileWhenDatabaseIsExists() throws Exception {
        try {
            ConfigurationUtils.checkOrCreateDatabaseFile();
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testCreateConfigurationFile() throws Exception {
        File original = new File("config.json");
        File copied = new File("config.json.bak");
        if (original.exists()) {
            FileUtils.copyFile(original, copied);
            if (copied.exists() && original.delete()) {
                ConfigurationUtils.createConfigurationFile();
                FileUtils.copyFile(copied, original);
                if (!copied.delete())
                    Assert.fail();
            }
        } else {
            ConfigurationUtils.createConfigurationFile();
        }
    }
}