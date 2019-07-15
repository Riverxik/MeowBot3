package com.github.riverxik.meowbot.modules.chat;

import com.github.riverxik.meowbot.commands.CommandRights;

public class ChannelUser {
    private String name = null;
    private boolean isOwner = false;
    private boolean isMod = false;
    private boolean isSub = false;
    private boolean isVip = false;
    CommandRights rightLevel = CommandRights.EVERYONE;

    public ChannelUser(String name, boolean isOwner, boolean isMod, boolean isSub, boolean isVip) {
        this.name = name;
        this.isOwner = isOwner;
        this.isMod = isMod;
        this.isSub = isSub;
        this.isVip = isVip;
        if (isSub || isVip) this.rightLevel = CommandRights.VIP_SUB;
        if (isMod) this.rightLevel = CommandRights.MODERATOR;
        if (isOwner) this.rightLevel = CommandRights.OWNER;
    }

    public String getName() {
        return name;
    }

    public boolean isMod() {
        return isMod;
    }

    public boolean isSub() {
        return isSub;
    }

    public boolean isVip() {
        return isVip;
    }

    public CommandRights getRightLevel() { return this.rightLevel; }

    public void setMod(boolean mod) {
        isMod = mod;
    }

    public void setSub(boolean sub) {
        isSub = sub;
    }

    public void setVip(boolean vip) {
        isVip = vip;
    }

    public void setRightLevel(CommandRights level) { this.rightLevel = level; }
}
