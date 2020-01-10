package com.github.riverxik.meowbot.modules.custom_commands;

import com.github.riverxik.meowbot.ConfigurationUtils;
import com.github.riverxik.meowbot.commands.AbstractCommand;
import com.github.twitch4j.chat.TwitchChat;

public class ShowCommandList extends AbstractCommand{

    @Override
    public boolean execute(String channel, String sender, Object[] args, TwitchChat chat) {
        String list = "";
        for (String key : ConfigurationUtils.customCommandRegistry.keySet())
            list += key += "; ";
        if (args.length > 0 && args[0] instanceof String && "public".equals(args[0].toString())) {
            chat.sendMessage(channel, String.format("%s List of commands: " + list, sender));
        } else {
            chat.sendMessage(channel, String.format("/w %s List of commands: " + list, sender));
        }
        return false;
    }
}
