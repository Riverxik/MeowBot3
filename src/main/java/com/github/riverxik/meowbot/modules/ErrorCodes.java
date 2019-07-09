package com.github.riverxik.meowbot.modules;

public enum ErrorCodes {
    DISABLED_CURRENCY("Currency is disabled for all channels!"),
    QUOTE_NOT_ADDED("Can't add new quote!"),
    QUOTE_NOT_REMOVED("Can't remove quote!"),
    ILLEGAL_NUMBER_OF_PARAMETERS("Can't recognize command with this parameters!"),
    ILLEGAL_PARAMETER_MUST_BE_STRING("Please use string as a parameter!"),
    ILLEGAL_PARAMETER_MUST_BE_INT("Please use integer as a parameter!"),
    ILLEGAL_NUMBER("Illegal number!"),
    ACCESS_DENIED("You haven't access to this command!");


    private String info;

    ErrorCodes(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }
}
