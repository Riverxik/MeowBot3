package com.github.riverxik.meowbot.modules.chat;

public class ChannelUser {
    private String name = null;
    private boolean isMod = false;
    private boolean isSub = false;
    private boolean isVip = false;

    public ChannelUser(String name, boolean isMod, boolean isSub, boolean isVip) {
        this.name = name;
        this.isMod = isMod;
        this.isSub = isSub;
        this.isVip = isVip;
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

    public void setMod(boolean mod) {
        isMod = mod;
    }

    public void setSub(boolean sub) {
        isSub = sub;
    }

    public void setVip(boolean vip) {
        isVip = vip;
    }
}
