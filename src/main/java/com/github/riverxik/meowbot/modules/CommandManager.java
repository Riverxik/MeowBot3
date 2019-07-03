package com.github.riverxik.meowbot.modules;

import com.github.riverxik.meowbot.Configuration;
import com.github.riverxik.meowbot.commands.Command;
import com.github.riverxik.meowbot.database.ChannelDb;

public class CommandManager {

    public static String currency(String channelName, Command command) {
        int paramLength = command.getParameters().length - 1;
        if (paramLength == 0) {
            return currencyStatusForChannel(channelName);
        }
        if (paramLength == 1) {
            Object[] objParam = command.getParameters();
            String param = String.valueOf(objParam[0]);
            if("on".equals(param.toLowerCase())) {
                return changeCurrencyStatusForChannel(channelName, true);
            }
            if("off".equals(param.toLowerCase())) {
                return changeCurrencyStatusForChannel(channelName, false);
            }
            if("help".equals(param.toLowerCase())) {
                return "Shows currency status. Use !currency [on/off]";
            }
            return String.format("Unknown parameter %s", param);
        } else
            return String.format("Can't recognize command with %d parameters", paramLength);
    }

    public static String currencyName(String channelName, Command command) {
        int paramLength = command.getParameters().length - 1;
        if (paramLength == 0) {
            return getCurrencyName(channelName);
        }
        if (paramLength == 1) {
            Object[] objParam = command.getParameters();
            String newName = String.valueOf(objParam[0]);
            if("help".equals(newName.toLowerCase())) {
                return "Shows currency name. Use !currencyName [newValue]";
            }
            return changeCurrencyNameForChannel(channelName, newName);
        } else
            return String.format("Can't recognize command with %d parameters", paramLength);
    }

    public static String currencyInc(String channelName, Command command) {
        int paramLength = command.getParameters().length - 1;
        if (paramLength == 0) {
            return getCurrencyInc(channelName);
        }
        if (paramLength == 1) {
            Object[] objParam = command.getParameters();
            if(objParam[0] instanceof String) {
                String param = String.valueOf(objParam[0]);
                if("help".equals(param.toLowerCase())) {
                    return "Shows currency increment. Use !currencyInc [newIntValue]";
                }
            }
            if(objParam[0] instanceof Integer) {
                if(isValidInteger(objParam[0])) {
                    int inc = (int) objParam[0];
                    return changeCurrencyIncForChannel(channelName, inc);
                } else {
                    return "Illegal integer number";
                }
            }
            return "Please use integer as parameter!";
        } else
            return String.format("Can't recognize command with %d parameters", paramLength);
    }

    private static boolean isValidInteger(Object integer) {
        try {
            int value = (int) integer;
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static String changeCurrencyIncForChannel(String channelName, int newInc) {
        if(CurrencyManager.setChannelCurrencyInc(channelName, newInc) == 1)
            return getCurrencyInc(channelName);
        else
            return String.format("Currency increment wasn't updated");
    }

    private static String getCurrencyInc(String channelName) {
        return String.format("Current currency increment for %s is [%s]",
                channelName, CurrencyManager.getChannelCurrencyInc(channelName));
    }

    private static String getCurrencyName(String channelName) {
        return String.format("Current currency name for %s is [%s]",
                channelName, CurrencyManager.getChannelCurrencyName(channelName));
    }

    private static String changeCurrencyNameForChannel(String channelName, String newCurrencyName) {
        return CurrencyManager.setChannelCurrencyName(channelName, newCurrencyName);
    }

    private static String changeCurrencyStatusForChannel(String channelName, boolean isEnable) {
        for (ChannelDb channel : Configuration.loadingChannels) {
            if(channelName.equals(channel.getChannelName())) {
                channel.setCurrencyEnabled(isEnable);
            }
        }
        if(isEnable)
            return "Channel currency now enabled!";
        else
            return "Channel currency now disabled!";
    }

    private static String currencyStatusForChannel(String channelName) {
        for (ChannelDb channel : Configuration.loadingChannels) {
            if(channelName.equals(channel.getChannelName())) {
                return String.format("Currency for %s is %s", channelName, channel.isCurrencyEnabled());
            }
        }
        return String.format("Couldn't find currency status for %s", channelName);
    }
}