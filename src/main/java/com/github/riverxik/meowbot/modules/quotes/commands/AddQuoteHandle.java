package com.github.riverxik.meowbot.modules.quotes.commands;

import com.github.riverxik.meowbot.commands.AbstractCommand;
import com.github.riverxik.meowbot.modules.quotes.QuotesManager;
import com.github.twitch4j.chat.TwitchChat;

public class AddQuoteHandle extends AbstractCommand{
    @Override
    public boolean execute(String channel, String sender, Object[] args, TwitchChat chat) {
        if (args.length > 0 && args[0] instanceof String) {
            int quoteId = QuotesManager.getNextChannelQuoteId(channel);
            String quoteText = "";
            for (int i = 0; i < args.length; i++) {
                quoteText += String.format("%s ", args[i].toString());
            }
            QuotesManager.addNewQuote(channel, quoteId, quoteText);
            chat.sendMessage(channel,
                    String.format("%s, Quote #%d has been added!", sender, quoteId));
            return true;
        } else {
            chat.sendMessage(channel, String.format("%s, Use: !quote add \"Quote text\"", sender));
        }
        return false;
    }
}
