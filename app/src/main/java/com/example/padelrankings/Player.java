package com.example.padelrankings;

public class Player {
    private String nick;
    private int rank;

    private String name;

    private int id;

    public Player(String nick, int rank) {
        this.nick = nick;
        this.rank = rank;
    }

    public Player(int id, String nick, String name, int rank) {
        this.nick = nick;
        this.rank = rank;
        this.name = name;
        this.id = id;

    }

    public int getRank() {
        return rank;
    }

    public String getNick() {
        return nick;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", nick='" + nick + '\'' +
                ", name='" + name + '\'' +
                ", rank=" + rank +
                '}';
    }
}
