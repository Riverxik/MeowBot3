package com.github.riverxik.meowbot.modules;

public enum ErrorCodes {
    DISABLED_CURRENCY("Currency is disabled for all channels!"),
    ILLEGAL_CURRENCY_PARAMETERS("Can't recognize command with this parameters");


    private String info;

    ErrorCodes(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }
}
