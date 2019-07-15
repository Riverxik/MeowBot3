package com.github.riverxik.meowbot.modules.currency.commands;

import com.github.riverxik.meowbot.Configuration;
import com.github.riverxik.meowbot.commands.AbstractCommand;
import com.github.riverxik.meowbot.commands.CommandRights;
import com.github.twitch4j.chat.TwitchChat;

public class CurrencyNameHandler extends AbstractCommand{
    @Override
    public void execute(String channel, String sender, Object[] args, TwitchChat chat) {
        if (hasRight(channel, sender, CommandRights.OWNER)) {
            if (args.length == 0) {
                String currentName = Configuration.getChannelByName(channel)
                        .getSettings().getCurrency().getCurrencyName();
                chat.sendMessage(channel,
                        String.format("%s, Currency name for this channel: '%s'", sender, currentName));
            }
            else if (args.length == 1 && args[0] instanceof String) {
                String newName = String.valueOf(args[0]);
                Configuration.getChannelByName(channel)
                        .getSettings().getCurrency().setCurrencyName(newName);
                chat.sendMessage(channel, String.format("%s, new currency name: %s", sender, newName));
            } else {
                chat.sendMessage(channel, String.format("%s, Use: !currencyName [newName]", sender));
            }
        } else {
            chat.sendMessage(channel, String.format("%s, you haven't enought rights", sender));
        }
    }
}
