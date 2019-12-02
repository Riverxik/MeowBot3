package com.github.riverxik.meowbot.commands;

import com.github.twitch4j.chat.TwitchChat;

public interface ICommand {
    boolean hasRight(String channel, String sender, CommandRights level);
    boolean execute(String channel, String sender, Object[] args, TwitchChat chat);
}
