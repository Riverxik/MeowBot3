package com.github.riverxik.meowbot.modules.currency.commands;

import com.github.riverxik.meowbot.Configuration;
import com.github.riverxik.meowbot.commands.AbstractCommand;
import com.github.riverxik.meowbot.commands.CommandRights;
import com.github.twitch4j.chat.TwitchChat;

public class CurrencyStatusHandler extends AbstractCommand{

    @Override
    public boolean hasRight(String channel, String sender, CommandRights level) {
        return level == Configuration.getChannelByName(channel).getChannelUserByName(sender).getRightLevel();
    }

    @Override
    public void execute(String channel, String sender, String[] args, TwitchChat chat) {
        if (hasRight(channel, sender, CommandRights.ADMIN)) {
            chat.sendMessage(channel, String.format("%s, you have enough rights to do this", sender));
        } else {
            chat.sendMessage(channel, String.format("%s, you haven't enough rights to do this", sender));
        }
    }
}
