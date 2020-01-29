package com.github.riverxik.meowbot.modules.alias;

import com.github.riverxik.meowbot.ConfigurationUtils;
import com.github.riverxik.meowbot.commands.AbstractCommand;
import com.github.twitch4j.chat.TwitchChat;

import java.util.ArrayList;

public class ShowAllAliasForCommandHandler extends AbstractCommand{

    /**
     * Syntax of command: !alias list <string>
     * Description of command: !alias list <commandName>
     * Example of command: !alias list help
     * */

    @Override
    public boolean execute(String channel, String sender, Object[] args, TwitchChat chat) {
        if (args.length > 0 && args[0] instanceof String) {
            String cmdName = args[0].toString();
            if (ConfigurationUtils.commandRegistry.containsKey(cmdName) ||
                    ConfigurationUtils.customCommandRegistry.containsKey(cmdName)) {
                ArrayList<String> aliasList = AliasManagerUtils.getAllAliasForCommand(channel, cmdName);
                if (aliasList.size() > 0) {
                    String list = "";
                    for (String alias : aliasList) {
                        list += alias + "; ";
                    }
                    chat.sendMessage(channel,
                            String.format("%s, Aliases for %s: %s", sender, cmdName, list));
                } else {
                    chat.sendMessage(channel,
                            String.format("%s, Command %s doesn't have aliases.", sender, cmdName));
                }
            } else {
                chat.sendMessage(channel,
                        String.format("%s, Sorry, command %s doesn't exists!", sender, cmdName));
            }
        } else {
            chat.sendMessage(channel,
                    String.format("%s, Use: !alias list <cmdName>", sender));
        }
        return false;
    }
}
