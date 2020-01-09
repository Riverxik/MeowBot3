package com.github.riverxik.meowbot.modules.custom_commands;

import com.github.riverxik.meowbot.commands.AbstractCommand;
import com.github.twitch4j.chat.TwitchChat;

public class RemoveCustomCommand extends AbstractCommand{

    /**
     * Syntax of command: !cmd remove <string>
     * Description of command: !cmd remove <cmdName>
     * Example of command: !cmd remove command
     * */

    @Override
    public boolean execute(String channel, String sender, Object[] args, TwitchChat chat) {
        if (args.length > 0 && args[0] instanceof String) {
            String cmdName = args[0].toString();
            if (CustomCommandUtils.isCommandExists(channel, cmdName)) {
                if (CustomCommandUtils.removeCommand(channel, cmdName)) {
                    chat.sendMessage(channel,
                            String.format("%s, Command [%s] has been removed!", sender, cmdName));
                    return true;
                }
            } else {
                chat.sendMessage(channel, String.format("%s, Can't remove command that doesn't exists!", sender));
            }
        } else {
            chat.sendMessage(channel, String.format("%s, Use: !cmd remove <cmdName>", sender));
        }
        return false;
    }
}
