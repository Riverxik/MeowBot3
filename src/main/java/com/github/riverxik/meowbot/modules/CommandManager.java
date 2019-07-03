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
