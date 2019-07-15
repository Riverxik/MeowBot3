package com.github.riverxik.meowbot.commands;

import com.github.twitch4j.chat.TwitchChat;

public interface ICommand {
    boolean hasRight(String channel, String sender, CommandRights level);
    void execute(String channel, String sender, Object[] args, TwitchChat chat);
}
