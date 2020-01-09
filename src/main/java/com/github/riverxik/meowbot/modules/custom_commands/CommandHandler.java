package com.github.riverxik.meowbot.modules.custom_commands;

import com.github.riverxik.meowbot.commands.AbstractCommand;
import com.github.riverxik.meowbot.commands.CommandRights;
import com.github.twitch4j.chat.TwitchChat;

import java.util.Arrays;

public class CommandHandler extends AbstractCommand{

    /**
     * Syntax of command: !cmd add/update/remove <string> <string>
     * Description of command: !cmd add/update/remove <cmdName> <cmdText>
     * Example of command: !cmd add calculate "%randomName was calculated!"
     * Example of command: !cmd update calculate "%randomName was calculated by %sender"
     * Example of command: !cmd remove calculate
     * Example of command: !cmd <cmdName> -> return <cmdText>
     * */

    @Override
    public boolean execute(String channel, String sender, Object[] args, TwitchChat chat) {
        if (args.length > 0 && args[0] instanceof String) {
            if (hasRight(channel, sender, CommandRights.OWNER)) {
                Object[] subArgs = Arrays.copyOfRange(args, 1, args.length);
                switch (args[0].toString()) {
                    case "add": {
                        return new AddCustomCommand().execute(channel, sender, subArgs, chat);
                    }
                    case "update": {
                        return new UpdateCustomCommand().execute(channel, sender, subArgs, chat);
                    }
                    case "remove": {
                        return new RemoveCustomCommand().execute(channel, sender, subArgs, chat);
                    }
                    default:
                        return CustomCommandUtils.showCommand(channel, sender, args[0].toString(), chat);
                }
            } else {
                chat.sendMessage(channel, String.format("%s, You haven't enough rights to do this!", sender));
            }
        } else {
            chat.sendMessage(channel, String.format("%s, Use: !cmd add/update/remove/<cmdName>", sender));
        }
        return false;
    }
}
