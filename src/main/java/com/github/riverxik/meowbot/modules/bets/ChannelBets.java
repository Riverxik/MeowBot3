package com.github.riverxik.meowbot.modules.bets;

public class ChannelBets {
    private String channelName;

    public ChannelBets(String channelName) {
        this.channelName = channelName;
    }

    public int addLot(String caption, int min, int max) {
        int lotId = BetsManager.getNextChannelLotId(channelName);
        if (BetsManager.addNewLot(channelName, lotId, caption, min, max)) return lotId;
        return -1;
    }

    public void addBet(String userName, int lotId, int score, int amount) {
        BetsManager.addNewBet(channelName, userName, lotId, score, amount);
    }
}
