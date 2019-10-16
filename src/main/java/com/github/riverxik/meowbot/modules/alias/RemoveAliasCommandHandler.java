package com.github.riverxik.meowbot.modules.alias;

import com.github.riverxik.meowbot.commands.AbstractCommand;
import com.github.twitch4j.chat.TwitchChat;

public class RemoveAliasCommandHandler extends AbstractCommand {

    @Override
    public void execute(String channel, String sender, Object[] args, TwitchChat chat) {
        if (args.length > 0 && args[0] instanceof String) {
            String aliasName = args[0].toString();
            if(AliasManager.removeAliasFromDatabase(channel, aliasName))
                chat.sendMessage(channel, String.format("%s, Alias [%s] was successful deleted!", sender, aliasName));
            else
                chat.sendMessage(channel, String.format("%s, Alias [%s] wasn't deleted.", sender, aliasName));
        } else {
            chat.sendMessage(channel, String.format("%s, Use: !alias remove <aliasName>", sender));
        }
    }
}
