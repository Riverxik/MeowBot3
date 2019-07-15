package com.github.riverxik.meowbot.modules.currency.commands;

import com.github.riverxik.meowbot.Configuration;
import com.github.riverxik.meowbot.commands.AbstractCommand;
import com.github.riverxik.meowbot.commands.CommandRights;
import com.github.riverxik.meowbot.modules.currency.CurrencyManager;
import com.github.twitch4j.chat.TwitchChat;

import java.util.Arrays;

public class CurrencyStatusHandler extends AbstractCommand{

    @Override
    public void execute(String channel, String sender, Object[] args, TwitchChat chat) {
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
                        Configuration.getChannelByName(channel).getSettings().setCurrencyEnabled(true);
                        chat.sendMessage(channel, String.format("%s, Currency system now enabled", sender));
                    } break;
                    case "off" : {
                        Configuration.getChannelByName(channel).getSettings().setCurrencyEnabled(false);
                        chat.sendMessage(channel, String.format("%s, Currency system now disabled", sender));
                    } break;
                    case "name" : {
                        Object[] args2 = Arrays.copyOfRange(args, 1, args.length);
                        new CurrencyNameHandler().execute(channel, sender, args2, chat);
                    } break;
                    case "inc" : {
                        Object[] args2 = Arrays.copyOfRange(args, 1, args.length);
                        new CurrencyIncHandler().execute(channel, sender, args2, chat);
                    } break;
                    case "sub" : {
                        Object[] args2 = Arrays.copyOfRange(args, 1, args.length);
                        new CurrencySubEnableHandler().execute(channel, sender, args2, chat);
                    } break;
                    case "help": {
                        chat.sendMessage(channel,
                                String.format("%s, Available commands: currency, currency on/off/name/inc/sub", sender));
                    } break;
                    default:
                        new CurrencyUserHandler().execute(channel, sender, args, chat);
                }
            }
        } else {
            if (args.length == 0) {
                new CurrencyUserHandler().execute(channel, sender, args, chat);
            } else if (args.length > 0 && args[0] instanceof String) {
                new CurrencyUserHandler().execute(channel, sender, args, chat);
            }
        }
    }

    private boolean getCurrencyStatus(String channelName) {
        return Configuration.getChannelByName(channelName).getSettings().isCurrencyEnabled();
    }
}
