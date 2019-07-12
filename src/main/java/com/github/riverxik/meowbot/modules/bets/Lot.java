package com.github.riverxik.meowbot.modules.bets;

import com.github.riverxik.meowbot.database.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public final class Lot {
    private static final Logger log = LoggerFactory.getLogger(Lot.class);

    private int id;
    private String caption;
    private String status;
    private int bank;
    private int minBet;
    private int maxBet;
    private List<Bet> bets;

    public Lot(int id, String caption, String status, int bank, int minBet, int maxBet) {
        this.id = id;
        this.caption = caption;
        this.status = status;
        this.bank = bank;
        this.minBet = minBet;
        this.maxBet = maxBet;
    }

    public boolean addBet(String channelName, String userName, int amount, int score) {
        bets.add(new Bet(userName, amount, score));
        Database database = new Database();
        database.connect();
        try {
            Statement statement = database.getConnection().createStatement();
            String query = "";
            statement.execute(query);
            statement.close();
            database.disconnect();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("SQL: " + e.getMessage());
        }
        return false;
    }
}
