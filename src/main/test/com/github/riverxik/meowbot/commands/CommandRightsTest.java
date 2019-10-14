package com.github.riverxik.meowbot.commands;

import org.junit.Assert;
import org.junit.Test;

public class CommandRightsTest {

    @Test
    public void testIndex() throws Exception {
        Assert.assertEquals(0, CommandRights.EVERYONE.index());
    }
}