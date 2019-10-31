package com.github.riverxik.meowbot.commands;

import com.github.riverxik.meowbot.modules.CooldownUtils;
import com.github.twitch4j.chat.TwitchChat;

public class CooldownCommandHandle extends AbstractCommand {
    // !cooldown <commandName> <cooldown>
    @Override
    public void execute(String channel, String sender, Object[] args, TwitchChat chat) {
        if (args.length > 1 && args[0] instanceof String && args[1] instanceof Integer) {
            if (hasRight(channel, sender, CommandRights.OWNER)) {
                String commandName = args[0].toString();
                int cooldown = (int) args[1] * 1000;
                CooldownUtils.setCooldown(channel, commandName, cooldown);
                chat.sendMessage(channel,
                        String.format("%s, Cooldown: %d has been set for command: %s", sender, cooldown, commandName));
            } else {
                chat.sendMessage(channel,
                        String.format("%s, Sorry, you haven't enough rights to use this command", sender));
            }
        } else {
            chat.sendMessage(channel, String.format("%s, Use: !cooldown <commandName> <intCooldownSec>", sender));
        }
    }
}
