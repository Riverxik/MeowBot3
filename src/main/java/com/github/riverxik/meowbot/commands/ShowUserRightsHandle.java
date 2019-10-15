package com.github.riverxik.meowbot.commands;

import com.github.riverxik.meowbot.ConfigurationUtils;
import com.github.twitch4j.chat.TwitchChat;

public class ShowUserRightsHandle extends AbstractCommand{

    @Override
    public void execute(String channel, String sender, Object[] args, TwitchChat chat) {
        if (args.length > 0 && hasRight(channel, sender, CommandRights.MODERATOR)) {
            String userName = args[0].toString().toLowerCase();
            String right = ConfigurationUtils.getChannelByName(channel)
                    .getChannelUserByName(userName)
                    .getRightLevel().toString();
            chat.sendMessage(channel, String.format("%s, %s roles: %s", sender, userName, right));
        } else {
            String right = ConfigurationUtils.getChannelByName(channel)
                    .getChannelUserByName(sender)
                    .getRightLevel().toString();
            chat.sendMessage(channel, String.format("%s, you role: %s", sender, right));
        }
    }
}
