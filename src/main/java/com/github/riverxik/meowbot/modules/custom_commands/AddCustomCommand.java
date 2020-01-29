package com.github.riverxik.meowbot.modules.custom_commands;

import com.github.riverxik.meowbot.ConfigurationUtils;
import com.github.riverxik.meowbot.commands.AbstractCommand;
import com.github.twitch4j.chat.TwitchChat;

public class AddCustomCommand extends AbstractCommand{

    /**
     * Syntax of command: !cmd add <string> <string>
     * Description of command: !cmd add <cmdName> <cmdText>
     * Example of command: !cmd add command "test command"
     * */

    @Override
    public boolean execute(String channel, String sender, Object[] args, TwitchChat chat) {
        if (args.length > 1 && args[0] instanceof String && args[1] instanceof String) {
            String cmdName = args[0].toString();
            String cmdText = args[1].toString();
            if (!CustomCommandUtils.isCommandExists(channel, cmdName)) {
                if(CustomCommandUtils.createNewCommand(channel, cmdName, cmdText)) {
                    if (!ConfigurationUtils.customCommandRegistry.containsKey(cmdName))
                        ConfigurationUtils.customCommandRegistry.put(cmdName, new CustomCommandHandler());
                    chat.sendMessage(channel,
                            String.format("%s, Command [%s] has been successfully added!", sender, cmdName));
                    return true;
                }
            } else {
                chat.sendMessage(channel,
                        String.format("%s, Can't create command %s because it already exists!", sender, cmdName));
            }
        } else {
            chat.sendMessage(channel, String.format("%s, Use: !cmd add <cmdName> <cmdText>", sender));
        }
        return false;
    }
}
