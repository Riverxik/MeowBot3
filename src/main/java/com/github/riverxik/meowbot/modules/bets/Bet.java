package com.github.riverxik.meowbot.modules.bets;

public final class Bet {
    private String userName;
    private int amout;
    private int score;

    public Bet(String userName, int amount, int score) {
        this.userName = userName;
        this.amout = amount;
        this.score = score;
    }
}
