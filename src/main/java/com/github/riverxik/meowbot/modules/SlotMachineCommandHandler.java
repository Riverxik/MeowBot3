package com.github.riverxik.meowbot.modules;

import com.github.riverxik.meowbot.commands.AbstractCommand;
import com.github.riverxik.meowbot.modules.currency.CurrencyManager;
import com.github.twitch4j.chat.TwitchChat;

public class SlotMachineCommandHandler extends AbstractCommand{

    @Override
    public boolean execute(String channel, String sender, Object[] args, TwitchChat chat) {
        // !roll <money>
        if (args.length > 0 && args[0] instanceof Integer) {
            int bet = (int)args[0];
            if (bet > 0) {
                int availableMaxBet = CurrencyManager.getUserCurrency(channel, sender);
                if (availableMaxBet - bet >= 0) {
                    int[] roll = SlotMachineUtils.getRandomRoll();
                    int winScore = SlotMachineUtils.getWinValue(roll, bet);
                    String rollString = SlotMachineUtils.rollToSmileString(roll);
                    if (winScore > 0) {
                        //win
                        CurrencyManager.setUserCurrency(channel, sender, availableMaxBet+winScore);
                        chat.sendMessage(channel,
                                String.format("%s spinning the roll: %s. You win: %d! Now you have: %d",
                                        sender, rollString, winScore, availableMaxBet+winScore));
                    } else {
                        // lose
                        CurrencyManager.setUserCurrency(channel, sender, availableMaxBet-bet);
                        chat.sendMessage(channel,
                                String.format("%s spinning the roll: %s. You lose: %d! Now you have: %d",
                                        sender, rollString, bet, availableMaxBet-bet));
                    }
                    return true;
                } else {
                    chat.sendMessage(channel,
                            String.format("%s, Not enough currency! You have: %d", sender, availableMaxBet));
                }
            } else {
                chat.sendMessage(channel, String.format("%s, bet has to be positive integer number!", sender));
            }
        } else {
            chat.sendMessage(channel, String.format("%s, Use: !roll <bet>; F.e: !roll 5", sender));
        }
        return false;
    }
}
