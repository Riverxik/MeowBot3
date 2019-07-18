package com.github.riverxik.meowbot.modules.chat;

import com.github.riverxik.meowbot.commands.CommandRights;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ChannelUserTest {
    ChannelUser owner;
    ChannelUser mod;
    ChannelUser sub;
    ChannelUser vip;
    ChannelUser viewer;

    @Before
    public void setUp() throws Exception {
        owner = new ChannelUser("owner", true, true, true, true);
        mod = new ChannelUser("mod", false, true, false, false);
        sub = new ChannelUser("sub", false, false, true, false);
        vip = new ChannelUser("vip", false, false, false, true);
        viewer = new ChannelUser("viewer", false, false, false, false);
    }

    @After
    public void tearDown() throws Exception {
        owner = null;
        mod = null;
        sub = null;
        vip = null;
        viewer = null;
    }

    @Test
    public void testGetName() throws Exception {

    }

    @Test
    public void testIsOwner() throws Exception {
        Assert.assertEquals(true, owner.isOwner());
        Assert.assertEquals(false, mod.isOwner());
        Assert.assertEquals(false, sub.isOwner());
        Assert.assertEquals(false, vip.isOwner());
        Assert.assertEquals(false, viewer.isOwner());

    }

    @Test
    public void testIsMod() throws Exception {
        Assert.assertEquals(true, owner.isMod());
        Assert.assertEquals(true, mod.isMod());
        Assert.assertEquals(false, sub.isMod());
        Assert.assertEquals(false, vip.isMod());
        Assert.assertEquals(false, viewer.isMod());
    }

    @Test
    public void testIsSub() throws Exception {
        Assert.assertEquals(true, owner.isSub());
        Assert.assertEquals(false, mod.isSub());
        Assert.assertEquals(true, sub.isSub());
        Assert.assertEquals(false, vip.isSub());
        Assert.assertEquals(false, viewer.isSub());
    }

    @Test
    public void testIsVip() throws Exception {
        Assert.assertEquals(true, owner.isVip());
        Assert.assertEquals(false, mod.isVip());
        Assert.assertEquals(false, sub.isVip());
        Assert.assertEquals(true, vip.isVip());
        Assert.assertEquals(false, viewer.isVip());
    }

    @Test
    public void testGetRightLevel() throws Exception {
        Assert.assertEquals(CommandRights.OWNER, owner.getRightLevel());
        Assert.assertEquals(CommandRights.MODERATOR, mod.getRightLevel());
        Assert.assertEquals(CommandRights.VIP_SUB, sub.getRightLevel());
        Assert.assertEquals(CommandRights.VIP_SUB, vip.getRightLevel());
        Assert.assertEquals(CommandRights.EVERYONE, viewer.getRightLevel());
    }

    @Test
    public void testSetMod() throws Exception {
        viewer.setMod(true);
        Assert.assertEquals(true, viewer.isMod());
    }

    @Test
    public void testSetSub() throws Exception {
        viewer.setSub(true);
        Assert.assertEquals(true, viewer.isSub());
    }

    @Test
    public void testSetVip() throws Exception {
        viewer.setVip(true);
        Assert.assertEquals(true, viewer.isVip());
    }

    @Test
    public void testSetRightLevel() throws Exception {
        viewer.setRightLevel(CommandRights.MODERATOR);
        Assert.assertEquals(CommandRights.MODERATOR, viewer.getRightLevel());
    }
}