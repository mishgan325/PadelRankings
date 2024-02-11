package com.example.padelrankings;

public class PlayerPartnerInfo {
    private String partner;
    private int games;
    private int wins;
    private int wonPoints;
    private int lostPoints;
    private int tiebreaks;
    private int tiebreakWins;

    public PlayerPartnerInfo(String partner, int games, int wins, int wonPoints, int lostPoints, int tiebreaks, int tiebreakWins) {
        this.partner = partner;
        this.games = games;
        this.wins = wins;
        this.wonPoints = wonPoints;
        this.lostPoints = lostPoints;
        this.tiebreaks = tiebreaks;
        this.tiebreakWins = tiebreakWins;
    }


    public String getPartner() {
        return partner;
    }

    public int getGames() {
        return games;
    }

    public int getWins() {
        return wins;
    }

    public int getWonPoints() {
        return wonPoints;
    }

    public int getLostPoints() {
        return lostPoints;
    }

    public int getTiebreaks() {
        return tiebreaks;
    }

    public int getTiebreaksWins() {
        return tiebreakWins;
    }

    public String getScore() {
        return wonPoints + "-" + lostPoints;
    }

    public String getWinrate() {
        String winrate;
        if (games > 0) {
            winrate = String.format("%.0f%%", ((double) wins / games) * 100);
        } else {
            winrate = "0%";
        }
        return winrate;
    }

    public String getTiebreaksWinrate() {
        String winrate;
        if (tiebreaks > 0) {
            winrate = String.format("%.0f%%", ((double) tiebreakWins / tiebreaks) * 100);
        } else {
            winrate = "0%";
        }
        return winrate;
    }

    public double getDoubleWinrate() {
        if (games == 0) {
            return 0.0; // чтобы избежать деления на ноль
        }
        return (double) wins / games * 100.0;
    }
}