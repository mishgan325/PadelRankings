package com.example.padelrankings;

import java.io.Serializable;

public class GameData implements Serializable {
    private String team1player1;
    private String team1player2;
    private String team2player1;
    private String team2player2;
    private double score;

    public GameData(String team1player1, String team1player2, String team2player1, String team2player2, double score) {
        this.team1player1 = team1player1;
        this.team1player2 = team1player2;

        this.team2player1 = team2player1;
        this.team2player2 = team2player2;

        this.score = score;
    }

    public String getTeam1player1() {
        return team1player1;
    }

    public String getTeam1player2() {
        return team1player2;
    }

    public String getTeam2player1() {
        return team2player1;
    }

    public String getTeam2player2() {
        return team2player2;
    }

    public double getScore() {
        return score;
    }

    public String returnData() {
        return team1player1 + " " + team1player2 + " " + score + " " + team2player1 + " " + team2player2;
    }
}