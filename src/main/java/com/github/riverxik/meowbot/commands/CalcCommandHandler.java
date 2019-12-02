package com.github.riverxik.meowbot.commands;

import com.github.twitch4j.chat.TwitchChat;

public class CalcCommandHandler extends AbstractCommand{

    @Override
    public boolean execute(String channel, String sender, Object[] args, TwitchChat chat) {
        if(args.length > 0) {
            String answer = "";
            for (int i = 0; i < args.length; i++) {
                answer += String.format("[%s]", args[i].toString());
            }

            chat.sendMessage(channel, answer);
            log.info(String.format("[%s][%s] - [%s]", channel, "Bot", answer));
            return true;
        }
        return false;
    }
}
