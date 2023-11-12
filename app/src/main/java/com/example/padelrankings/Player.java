package com.example.padelrankings;

public class Player {
    private String nick;
    private int rank;

    public Player(String nick, int rank) {
        this.nick = nick;
        this.rank = rank;
    }

    public int getRank() {
        return rank;
    }

    public String getNick() {
        return nick;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
