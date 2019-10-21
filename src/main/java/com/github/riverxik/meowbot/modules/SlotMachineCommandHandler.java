package com.github.riverxik.meowbot.modules;

import com.github.riverxik.meowbot.commands.AbstractCommand;
import com.github.riverxik.meowbot.modules.currency.CurrencyManager;
import com.github.twitch4j.chat.TwitchChat;

public class SlotMachineCommandHandler extends AbstractCommand{

    @Override
    public void execute(String channel, String sender, Object[] args, TwitchChat chat) {
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
                                String.format("%s spinning the roll: %s. You win: %d!", sender, rollString, winScore));
                    } else {
                        // lose
                        CurrencyManager.setUserCurrency(channel, sender, availableMaxBet-bet);
                        chat.sendMessage(channel,
                                String.format("%s spinning the roll: %s. You lose: %d!", sender, rollString, bet));
                    }
                } else {
                    chat.sendMessage(channel, String.format("%s, Not enough currency!", sender));
                }
            } else {
                chat.sendMessage(channel, String.format("%s, bet has to be positive integer number!", sender));
            }
        }
    }
}