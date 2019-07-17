package com.github.riverxik.meowbot.modules.quotes.commands;

import com.github.riverxik.meowbot.commands.AbstractCommand;
import com.github.riverxik.meowbot.commands.CommandRights;
import com.github.riverxik.meowbot.modules.quotes.QuotesManager;
import com.github.twitch4j.chat.TwitchChat;

import java.util.Arrays;

public class QuoteHandle extends AbstractCommand{
    @Override
    public void execute(String channel, String sender, Object[] args, TwitchChat chat) {
        if (args.length > 0) {
            if (args[0] instanceof String) {
                switch (args[0].toString()) {
                    case "add" : {
                        if (hasRight(channel, sender, CommandRights.MODERATOR)) {
                            Object[] args2 = Arrays.copyOfRange(args, 1, args.length);
                            new AddQuoteHandle().execute(channel, sender, args2, chat);
                        }
                    } break;
                    case "remove" : {
                        if (hasRight(channel, sender, CommandRights.OWNER)) {
                            Object[] args2 = Arrays.copyOfRange(args, 1, args.length);
                            new RemoveQuoteHandle().execute(channel, sender, args2, chat);
                        }
                    } break;
                    default: break;
                }
            } else if (args[0] instanceof Integer) {
                String quote = QuotesManager.showQuote(channel, (int) args[0]);
                if (!quote.startsWith("Can't find"))
                    chat.sendMessage(channel, quote + " ["+args[0]+"]");
                else chat.sendMessage(channel, quote);
            }
        } else {
            chat.sendMessage(channel, String.format("%s, Use: !quote add/close/[id]", sender));
        }
    }
}
