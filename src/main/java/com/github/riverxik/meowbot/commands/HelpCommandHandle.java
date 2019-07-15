package com.github.riverxik.meowbot.commands;

import com.github.twitch4j.chat.TwitchChat;

public class HelpCommandHandle extends AbstractCommand{

    @Override
    public void execute(String channel, String sender, Object[] args, TwitchChat chat) {
        chat.sendMessage(channel, String.format("%s All commands: https://github.com/Riverxik/MeowBot3/blob/develop/commands.md", sender));
    }
}
