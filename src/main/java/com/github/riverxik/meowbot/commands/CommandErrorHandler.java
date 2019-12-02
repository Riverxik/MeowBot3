package com.github.riverxik.meowbot.commands;

import com.github.riverxik.meowbot.ConfigurationUtils;
import com.github.twitch4j.chat.TwitchChat;

public class CommandErrorHandler extends AbstractCommand{
    @Override
    public boolean execute(String channel, String sender, Object[] args, TwitchChat chat) {
        try {
            if (ConfigurationUtils.debug) {
                chat.sendMessage(channel, String.format("Error: %s", args[0].toString()));
            }
            if (ConfigurationUtils.admin.equals(sender.toLowerCase()) && "toggle".equals(args[0].toString())) {
                ConfigurationUtils.debug = !ConfigurationUtils.debug;
                chat.sendMessage(channel, "Now debug is: " + ConfigurationUtils.debug);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            chat.sendMessage(channel, "You can't use this command without parameters!");
        }
        return false;
    }
}
