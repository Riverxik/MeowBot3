package com.github.riverxik.meowbot.modules.alias;

import com.github.riverxik.meowbot.ConfigurationUtils;
import com.github.riverxik.meowbot.commands.AbstractCommand;
import com.github.twitch4j.chat.TwitchChat;

public class AddAliasCommandHandler extends AbstractCommand {

    /**
     * Syntax of command: !addAlias <string> <string>
     * Description of command: !addAlias <aliasName> <commandName>
     * Example of command: !addAlias wtf help
     * */

    @Override
    public boolean execute(String channel, String sender, Object[] args, TwitchChat chat) {
        if (args.length > 1 && args[0] instanceof String && args[1] instanceof String) {
            String aliasName = String.valueOf(args[0]);
            String commandName = String.valueOf(args[1]);
            // Проверка на существование такой команды
            if (ConfigurationUtils.commandRegistry.containsKey(commandName)) {
                // Проверка на существование такого алиаса
                if (!AliasManagerUtils.isAliasExists(channel, aliasName)) {
                    // Добавляем в базу данных
                    AliasManagerUtils.addNewAliasToDatabase(channel, aliasName, commandName);
                    chat.sendMessage(channel,
                            String.format("%s, Alias [%s]->[%s] has been added!", sender, aliasName, commandName));
                    return true;
                } else {
                    chat.sendMessage(channel, String.format("%s, Alias [%s] already exists!", sender, aliasName));
                }
            } else {
                chat.sendMessage(channel, String.format("%s, Can't link alias to unknown command!", sender));
            }
        } else {
            chat.sendMessage(channel, String.format("%s, Use !alias add <aliasName> <commandName>", sender));
        }
        return false;
    }
}
