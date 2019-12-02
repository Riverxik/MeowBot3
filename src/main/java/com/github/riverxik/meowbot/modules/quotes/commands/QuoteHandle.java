package com.github.riverxik.meowbot.modules.quotes.commands;

import com.github.riverxik.meowbot.commands.AbstractCommand;
import com.github.riverxik.meowbot.commands.CommandRights;
import com.github.riverxik.meowbot.modules.quotes.QuotesManager;
import com.github.twitch4j.chat.TwitchChat;

import java.util.Arrays;

public class QuoteHandle extends AbstractCommand{
    @Override
    public boolean execute(String channel, String sender, Object[] args, TwitchChat chat) {
        if (args.length > 0) {
            if (args[0] instanceof String) {
                switch (args[0].toString()) {
                    case "add" : {
                        if (hasRight(channel, sender, CommandRights.VIP_SUB)) {
                            Object[] args2 = Arrays.copyOfRange(args, 1, args.length);
                            return new AddQuoteHandle().execute(channel, sender, args2, chat);
                        } else {
                            chat.sendMessage(channel,
                                    String.format("%s, You haven't enough rights for this command!", sender));
                            return true;
                        }
                    }
                    case "remove" : {
                        if (hasRight(channel, sender, CommandRights.OWNER)) {
                            Object[] args2 = Arrays.copyOfRange(args, 1, args.length);
                            return new RemoveQuoteHandle().execute(channel, sender, args2, chat);
                        } else {
                            chat.sendMessage(channel,
                                    String.format("%s, You haven't enough rights for this command!", sender));
                            return true;
                        }
                    }
                    default: break;
                }
            } else if (args[0] instanceof Integer) {
                String quote = QuotesManager.showQuote(channel, (int) args[0]);
                if (!quote.startsWith("Can't find"))
                    chat.sendMessage(channel, quote + " ["+args[0]+"]");
                else chat.sendMessage(channel, quote);
                return true;
            }
        } else {
            chat.sendMessage(channel, String.format("%s, Use: !quote add/remove/[id]", sender));
        }
        return false;
    }
}
