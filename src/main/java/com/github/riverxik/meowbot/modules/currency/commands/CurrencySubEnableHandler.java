package com.github.riverxik.meowbot.modules.currency.commands;

import com.github.riverxik.meowbot.commands.AbstractCommand;
import com.github.riverxik.meowbot.commands.CommandRights;
import com.github.riverxik.meowbot.modules.currency.CurrencyManager;
import com.github.twitch4j.chat.TwitchChat;

import java.util.Arrays;

public class CurrencySubEnableHandler extends AbstractCommand{
    @Override
    public boolean execute(String channel, String sender, Object[] args, TwitchChat chat) {
        if (hasRight(channel, sender, CommandRights.OWNER)) {
            if (args.length == 0) {
                if (getCurrencySubStatus(channel)) {
                    chat.sendMessage(channel, String.format("%s, Sub multiplier enabled for this channel", sender));
                } else {
                    chat.sendMessage(channel, String.format("%s, Sub multiplier disabled for this channel", sender));
                }
            }
            if (args.length > 0 && args[0] instanceof String) {
                String arg = String.valueOf(args[0]);
                switch (arg) {
                    case "on" : {
                        CurrencyManager.setChannelSubEnable(channel, true);
                        chat.sendMessage(channel, String.format("%s, Sub multiplier now enabled", sender));
                    } break;
                    case "off" : {
                        CurrencyManager.setChannelSubEnable(channel, false);
                        chat.sendMessage(channel, String.format("%s, Sub multiplier now disabled", sender));
                    } break;
                    case "inc" : {
                        Object[] args2 = Arrays.copyOfRange(args, 1, args.length);
                        return new CurrencySubMultiplierHandler().execute(channel, sender, args2, chat);
                    }
                    default:
                        chat.sendMessage(channel,
                                String.format("%s, Available commands: currency sub, currency sub on/off/inc", sender));
                        break;
                }
            }
        } else {
            chat.sendMessage(channel, String.format("%s, you haven't enough rights to do this", sender));
        }
        return false;
    }

    private boolean getCurrencySubStatus(String channelName) {
        return CurrencyManager.getChannelSubEnable(channelName);
    }
}
