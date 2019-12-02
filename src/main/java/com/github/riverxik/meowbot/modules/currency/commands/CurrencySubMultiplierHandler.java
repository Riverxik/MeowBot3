package com.github.riverxik.meowbot.modules.currency.commands;

import com.github.riverxik.meowbot.ConfigurationUtils;
import com.github.riverxik.meowbot.commands.AbstractCommand;
import com.github.riverxik.meowbot.commands.CommandRights;
import com.github.riverxik.meowbot.modules.currency.CurrencyManager;
import com.github.twitch4j.chat.TwitchChat;

public class CurrencySubMultiplierHandler extends AbstractCommand{
    @Override
    public boolean execute(String channel, String sender, Object[] args, TwitchChat chat) {
        if (hasRight(channel, sender, CommandRights.OWNER)) {
            if (args.length == 0) {
                int currentSubMultiplier = ConfigurationUtils.getChannelByName(channel)
                        .getSettings().getCurrency().getSubMultiplier();
                chat.sendMessage(channel,
                        String.format("%s, Current sub multiplier: %d", sender, currentSubMultiplier));
            }
            else if (args.length == 1 && args[0] instanceof Integer) {
                int newSubMultiplier = (int) args[0];
                CurrencyManager.setChannelSubMultiplier(channel, newSubMultiplier);
                chat.sendMessage(channel,
                        String.format("%s, New currency sub multiplier: %d", sender, newSubMultiplier));
            } else {
                chat.sendMessage(channel,
                        String.format("%s, Use: !currency sub inc [newInt]", sender));
            }
        } else {
            chat.sendMessage(channel, String.format("%s, you haven't enough rights!", sender));
            return true;
        }
        return false;
    }
}
