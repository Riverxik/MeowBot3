package com.github.riverxik.meowbot.commands;

import com.github.riverxik.meowbot.Configuration;
import com.github.twitch4j.chat.TwitchChat;

public class CommandErrorHandler extends AbstractCommand{
    @Override
    public void execute(String channel, String sender, Object[] args, TwitchChat chat) {
        if (Configuration.debug) {
            chat.sendMessage(channel, String.format("Error: %s", args[0].toString()));
        }
        if (Configuration.admin.equals(sender.toLowerCase()) && "toggle".equals(args[0].toString())) {
            Configuration.debug = !Configuration.debug;
            chat.sendMessage(channel, "Now debug is: " + Configuration.debug);
        }
    }
}
