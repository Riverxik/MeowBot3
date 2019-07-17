package com.github.riverxik.meowbot.modules.quotes;

public class Quote {
    private int quoteId = 0;
    private String quoteText = null;

    public Quote(int quoteId, String quoteText) {
        this.quoteId = quoteId;
        this.quoteText = quoteText;
    }

    public int getQuoteId() {
        return quoteId;
    }

    public String getQuoteText() {
        return quoteText;
    }
}
