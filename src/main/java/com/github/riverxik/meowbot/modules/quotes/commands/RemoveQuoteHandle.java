package com.github.riverxik.meowbot.modules.quotes.commands;

import com.github.riverxik.meowbot.commands.AbstractCommand;
import com.github.riverxik.meowbot.modules.quotes.QuotesManager;
import com.github.twitch4j.chat.TwitchChat;

public class RemoveQuoteHandle extends AbstractCommand{
    @Override
    public void execute(String channel, String sender, Object[] args, TwitchChat chat) {
        if (args.length > 0 && args[0] instanceof Integer) {
            QuotesManager.removeQuote(channel, (int) args[0]);
            chat.sendMessage(channel,
                    String.format("%s, Quote #%d has been deleted!", sender, (int) args[0]));
        } else {
            chat.sendMessage(channel, String.format("%s, Use: !quote remove [quoteId]", sender));
        }
    }
}
