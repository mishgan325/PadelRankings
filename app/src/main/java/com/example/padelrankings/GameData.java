package com.example.padelrankings;

import java.io.Serializable;

public class GameData implements Serializable {
    private String team1player1;
    private String team1player2;
    private String team2player1;
    private String team2player2;
    private int team1score;
    private int team2score;

    public GameData(String team1player1, String team1player2, String team2player1, String team2player2, int score1, int score2) {
        this.team1player1 = team1player1;
        this.team1player2 = team1player2;

        this.team2player1 = team2player1;
        this.team2player2 = team2player2;

        this.team1score = score1;
        this.team2score = score2;
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

    public int getTeam1score() {
        return team1score;
    }

    public int getTeam2score() {
        return team2score;
    }

    public String returnData() {
        return team1player1 + " " + team1player2 + " " + team1score + " " + team2score + " " + team2player1 + " " + team2player2;
    }
}