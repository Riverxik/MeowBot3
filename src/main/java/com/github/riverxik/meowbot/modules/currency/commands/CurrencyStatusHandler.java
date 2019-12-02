package com.github.riverxik.meowbot.modules.currency.commands;

import com.github.riverxik.meowbot.ConfigurationUtils;
import com.github.riverxik.meowbot.commands.AbstractCommand;
import com.github.riverxik.meowbot.commands.CommandRights;
import com.github.twitch4j.chat.TwitchChat;

import java.util.Arrays;

public class CurrencyStatusHandler extends AbstractCommand{

    @Override
    public boolean execute(String channel, String sender, Object[] args, TwitchChat chat) {
        if (hasRight(channel, sender, CommandRights.OWNER)) {
            if (args.length == 0) {
                if (getCurrencyStatus(channel)) {
                    chat.sendMessage(channel, String.format("%s, Currency system enabled for this channel", sender));
                } else {
                    chat.sendMessage(channel, String.format("%s, Currency system disabled for this channel", sender));
                }
            }
            if (args.length > 0 && args[0] instanceof String) {
                String arg = String.valueOf(args[0]);
                switch (arg) {
                    case "on" : {
                        ConfigurationUtils.getChannelByName(channel).getSettings().setCurrencyEnabled(true);
                        ConfigurationUtils.getChannelByName(channel).addChannel(); // Saves information to database
                        chat.sendMessage(channel, String.format("%s, Currency system now enabled", sender));
                    } break;
                    case "off" : {
                        ConfigurationUtils.getChannelByName(channel).getSettings().setCurrencyEnabled(false);
                        ConfigurationUtils.getChannelByName(channel).addChannel(); // Saves information to database
                        chat.sendMessage(channel, String.format("%s, Currency system now disabled", sender));
                    } break;
                    case "name" : {
                        Object[] args2 = Arrays.copyOfRange(args, 1, args.length);
                        return new CurrencyNameHandler().execute(channel, sender, args2, chat);
                    }
                    case "inc" : {
                        Object[] args2 = Arrays.copyOfRange(args, 1, args.length);
                        return new CurrencyIncHandler().execute(channel, sender, args2, chat);
                    }
                    case "sub" : {
                        Object[] args2 = Arrays.copyOfRange(args, 1, args.length);
                        return new CurrencySubEnableHandler().execute(channel, sender, args2, chat);
                    }
                    case "help": {
                        chat.sendMessage(channel,
                                String.format("%s, Available commands: currency, currency on/off/name/inc/sub", sender));
                    } break;
                    default: return new CurrencyUserHandler().execute(channel, sender, args, chat);
                }
            }
        } else {
            if (args.length == 0) {
                return new CurrencyUserHandler().execute(channel, sender, args, chat);
            } else if (args.length > 0 && args[0] instanceof String) {
                return new CurrencyUserHandler().execute(channel, sender, args, chat);
            }
        }
        return false;
    }

    private boolean getCurrencyStatus(String channelName) {
        return ConfigurationUtils.getChannelByName(channelName).getSettings().isCurrencyEnabled();
    }
}
