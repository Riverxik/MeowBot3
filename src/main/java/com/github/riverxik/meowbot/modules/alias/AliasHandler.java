package com.github.riverxik.meowbot.modules.alias;

import com.github.riverxik.meowbot.commands.AbstractCommand;
import com.github.riverxik.meowbot.commands.CommandRights;
import com.github.twitch4j.chat.TwitchChat;

import java.util.Arrays;

public class AliasHandler extends AbstractCommand{

    @Override
    public boolean execute(String channel, String sender, Object[] args, TwitchChat chat) {
        if (args.length > 0) {
            if (args[0] instanceof String) {
                Object[] args2 = Arrays.copyOfRange(args, 1, args.length);
                switch (args[0].toString()) {
                    case "add": {
                        if (hasRight(channel, sender, CommandRights.MODERATOR)) {
                            return new AddAliasCommandHandler().execute(channel, sender, args2, chat);
                        }
                    } break;
                    case "remove": {
                        if (hasRight(channel, sender, CommandRights.OWNER)) {
                            return new RemoveAliasCommandHandler().execute(channel, sender, args2, chat);
                        }
                    } break;
                    case "list": {
                        return new ShowAllAliasForCommandHandler().execute(channel, sender, args2, chat);
                    }
                    default: break;
                }
            }
        } else {
            chat.sendMessage(channel, String.format("%s, Use: !alias add/remove", sender));
        }
        return false;
    }
}
