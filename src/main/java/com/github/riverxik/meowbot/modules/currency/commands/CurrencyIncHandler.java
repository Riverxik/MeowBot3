package com.github.riverxik.meowbot.modules.currency.commands;

import com.github.riverxik.meowbot.Configuration;
import com.github.riverxik.meowbot.commands.AbstractCommand;
import com.github.riverxik.meowbot.commands.CommandRights;
import com.github.riverxik.meowbot.modules.currency.CurrencyManager;
import com.github.twitch4j.chat.TwitchChat;

public class CurrencyIncHandler extends AbstractCommand{
    @Override
    public void execute(String channel, String sender, Object[] args, TwitchChat chat) {
        if (hasRight(channel, sender, CommandRights.OWNER)) {
            if (args.length == 0) {
                int currentInc = Configuration.getChannelByName(channel)
                        .getSettings().getCurrency().getCurrencyInc();
                chat.sendMessage(channel, String.format("%s, current increment: %d", sender, currentInc));
            }
            else if (args.length == 1 && args[0] instanceof Integer) {
                int newCurrencyInc = (int) args[0];
                CurrencyManager.setChannelCurrencyInc(channel, newCurrencyInc);
                chat.sendMessage(channel, String.format("%s, new currency increment: %d", sender, newCurrencyInc));
            } else {
                chat.sendMessage(channel, String.format("%s, Use: !currency inc [newInt]", sender));
            }
        } else {
            chat.sendMessage(channel, String.format("%s, you haven't enough rights!", sender));
        }
    }
}
