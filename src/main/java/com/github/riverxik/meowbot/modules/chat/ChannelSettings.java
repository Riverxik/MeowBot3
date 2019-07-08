package com.github.riverxik.meowbot.modules.chat;

import com.github.riverxik.meowbot.modules.currency.ChannelCurrency;

public class ChannelSettings {
    private ChannelCurrency currency;
    private final String accessToken;
    private boolean moderationEnabled = false;
    private boolean currencyEnabled = false;
    private boolean betsEnabled = false;
    private boolean customCommandsEnabled = false;

    public ChannelSettings(boolean moderationEnabled,
                           boolean currencyEnabled,
                           boolean betsEnabled,
                           boolean customCommandsEnabled,
                           String accessToken) {
        this.moderationEnabled = moderationEnabled;
        this.currencyEnabled = currencyEnabled;
        this.betsEnabled = betsEnabled;
        this.customCommandsEnabled = customCommandsEnabled;
        this.accessToken = accessToken;
    }

    public void setCurrency(ChannelCurrency currency) {
        this.currency = currency;
    }

    public ChannelCurrency getCurrency() {
        return currency;
    }

    public boolean isModerationEnabled() {
        return moderationEnabled;
    }

    public boolean isCurrencyEnabled() {
        return currencyEnabled;
    }

    public boolean isBetsEnabled() {
        return betsEnabled;
    }

    public boolean isCustomCommandsEnabled() {
        return customCommandsEnabled;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setModerationEnabled(boolean moderationEnabled) {
        this.moderationEnabled = moderationEnabled;
    }

    public void setCurrencyEnabled(boolean currencyEnabled) {
        this.currencyEnabled = currencyEnabled;
    }

    public void setBetsEnabled(boolean betsEnabled) {
        this.betsEnabled = betsEnabled;
    }

    public void setCustomCommandsEnabled(boolean customCommandsEnabled) {
        this.customCommandsEnabled = customCommandsEnabled;
    }
}
