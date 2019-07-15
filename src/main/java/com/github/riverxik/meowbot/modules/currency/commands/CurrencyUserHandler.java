package com.github.riverxik.meowbot.modules.currency.commands;

import com.github.riverxik.meowbot.commands.AbstractCommand;
import com.github.riverxik.meowbot.modules.currency.CurrencyManager;
import com.github.twitch4j.chat.TwitchChat;

public class CurrencyUserHandler extends AbstractCommand{
    @Override
    public void execute(String channel, String sender, Object[] args, TwitchChat chat) {
        if (args.length == 0) {
            int amount = CurrencyManager.getUserCurrency(channel, sender);
            String currencyName = CurrencyManager.getChannelCurrencyName(channel);
            chat.sendMessage(channel, String.format("%s, %s: %d", sender, currencyName, amount));
        } else {
            String user = args[0].toString();
            int amount = CurrencyManager.getUserCurrency(channel, user);
            String currencyName = CurrencyManager.getChannelCurrencyName(channel);
            chat.sendMessage(channel, String.format("%s, %s [%s]: %d", sender, currencyName, user, amount));
        }
    }
}
