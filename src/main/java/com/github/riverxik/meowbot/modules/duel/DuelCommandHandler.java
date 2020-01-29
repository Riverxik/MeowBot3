package com.github.riverxik.meowbot.modules.duel;

import com.github.riverxik.meowbot.commands.AbstractCommand;
import com.github.twitch4j.chat.TwitchChat;

import java.util.Arrays;

public class DuelCommandHandler extends AbstractCommand{

    /**
     * Syntax of command: !duel accept/deny/cancel/<string> <int>
     * Description of command: !duel accept/deny/cancel <username> <amountCurrency>
     * Example of command: !duel username 100
     * Example of command: !duel accept user
     * Example of command: !duel deny user
     * Example of command: !duel cancel
     * */

    @Override
    public boolean execute(String channel, String sender, Object[] args, TwitchChat chat) {
        if (args.length > 0 && args[0] instanceof String) {
            Object[] subArgs = Arrays.copyOfRange(args, 1, args.length);
            String args0 = args[0].toString();
            switch (args0) {
                case "accept": {
                    return DuelUtils.acceptUserDuel(channel, sender, chat);
                }
                case "deny": {
                    return DuelUtils.denyUserDuel(channel, sender, chat);
                }
                case "cancel": {
                    return DuelUtils.cancelUserDuel(channel, sender, chat);
                }
                default: { // Should be username
                    return DuelUtils.throwDuelToUser(channel, sender, args0, subArgs, chat);
                }
            }
        } else {
            chat.sendMessage(channel, String.format("%s, Use: !duel <username>/accept/deny/cancel", sender));
        }
        return false;
    }
}
