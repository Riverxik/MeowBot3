package com.github.riverxik.meowbot.modules.custom_commands;

import com.github.riverxik.meowbot.commands.AbstractCommand;
import com.github.twitch4j.chat.TwitchChat;

public class UpdateCustomCommand extends AbstractCommand{

    /**
     * Syntax of command: !cmd update <string> <string>
     * Description of command: !cmd update <cmdName> <cmdText>
     * Example of command: !cmd update command "test command"
     * */

    @Override
    public boolean execute(String channel, String sender, Object[] args, TwitchChat chat) {
        if (args.length > 1 && args[0] instanceof String && args[1] instanceof String) {
            String cmdName = args[0].toString();
            String cmdText = args[1].toString();
            if (CustomCommandUtils.isCommandExists(channel, cmdName)) {
                if (CustomCommandUtils.updateCommand(channel, cmdName, cmdText)) {
                    chat.sendMessage(channel,
                            String.format("%s, Command [%s] has been updated!", sender, cmdName));
                    return true;
                }
            } else {
                chat.sendMessage(channel, String.format("%s, Command [%s] doesn't exists!", sender, cmdName));
            }
        } else {
            chat.sendMessage(channel, String.format("%s, Use: !cmd update <cmdName> <cmdText>", sender));
        }
        return false;
    }
}
