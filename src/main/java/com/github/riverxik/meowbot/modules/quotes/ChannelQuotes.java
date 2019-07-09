package com.github.riverxik.meowbot.modules.quotes;

public class ChannelQuotes {

    private String channelName = null;

    public ChannelQuotes(String channelName) {
        this.channelName = channelName;
    }
    public int addQuote(String quoteText) {
        int quoteId = QuotesManager.getNextChannelQuoteId(channelName);
        if(QuotesManager.addNewQuote(channelName, quoteId, quoteText)) return quoteId;

        return -1;
    }
}
