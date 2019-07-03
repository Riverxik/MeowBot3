package com.github.riverxik.meowbot.database;
//TODO: Class documentation
public class ChannelCurrency {

    private String channelName = null;
    private String currencyName = null;
    private int currencyInc = 0;
    private boolean subEnable = false;
    private int subMultiplier = 2;

    public ChannelCurrency(String channelName, String currencyName, int currencyInc, boolean subEnable, int subMultiplier) {
        this.channelName = channelName;
        this.currencyName = currencyName;
        this.currencyInc = currencyInc;
        this.subEnable = subEnable;
        this.subMultiplier = subMultiplier;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public void setCurrencyInc(int currencyInc) {
        this.currencyInc = currencyInc;
    }

    public void setSubEnable(boolean subEnable) {
        this.subEnable = subEnable;
    }

    public void setSubMultiplier(int subMultiplier) {
        this.subMultiplier = subMultiplier;
    }

    public String getChannelName() {
        return channelName;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public int getCurrencyInc() {
        return currencyInc;
    }

    public boolean isSubEnable() {
        return subEnable;
    }

    public int getSubMultiplier() {
        return subMultiplier;
    }
}
