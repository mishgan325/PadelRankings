package com.example.padelrankings;

import android.util.Log;

import java.sql.Struct;
import java.util.ArrayList;

public class GameSession {
    private ArrayList<Player> players = new ArrayList<>();


    public GameSession(ArrayList<String> nicks, ArrayList<Integer> ranks) {
        for (int i = 0; i < nicks.size(); i++)
        {
            players.add(new Player(nicks.get(i), ranks.get(i)));
        }
    }


    private Player findPlayerByName(String nick) {
        for (Player player : players) {
            if (player.getNick().equals(nick)) {
                return player;
            }
        }

        return null;
    }

    void runGame(GameData game) {
        Player teamAplayer1 = findPlayerByName(game.getTeam1player1());
        Player teamAplayer2 = findPlayerByName(game.getTeam1player2());
        Player teamBplayer1 = findPlayerByName(game.getTeam2player1());
        Player teamBplayer2 = findPlayerByName(game.getTeam2player2());
        int score1 = game.getTeam1score();
        int score2 = game.getTeam2score();


        double exp_value_team1, exp_value_team2, real_value_team1 = 0.5, real_value_team2 = 0.5;
        int k = 20;
        //рассчитываем ожидаемые очки для обоих команд
        exp_value_team1 = 1 / (1 + Math.pow(10, ((double)(teamBplayer1.getRank() + teamBplayer2.getRank() -
                (teamAplayer1.getRank() + teamAplayer2.getRank())) / 400)));

        exp_value_team2 = 1 - exp_value_team1;

        //рассчитываем реальные очки для обоих команд
        if (score1 - score2 > 1) {
            real_value_team1 = 1;
        } else if (score2 - score1 > 1) {
            real_value_team1 = 0;
        } else if (score1 > score2) {
            real_value_team1 = 0.75;
        } else if (score1 < score2) {
            real_value_team1 = 0.25;
        }
        real_value_team2 = 1 - real_value_team1;

        //изменяем рейтинг
        teamAplayer1.setRank((int)Math.round((teamAplayer1.getRank() + k * (real_value_team1 - exp_value_team1))));
        teamAplayer2.setRank((int)Math.round((teamAplayer2.getRank() + k * (real_value_team1 - exp_value_team1))));
        teamBplayer1.setRank((int)Math.round((teamBplayer1.getRank() + k * (real_value_team2 - exp_value_team2))));
        teamBplayer2.setRank((int)Math.round((teamBplayer2.getRank() + k * (real_value_team2 - exp_value_team2))));
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

}