package com.example.padelrankings;

import java.sql.Struct;
import java.util.ArrayList;

public class GameSession {
    ArrayList<Player> players = new ArrayList<>();


    public GameSession(ArrayList<String> nicks, ArrayList<Integer> ranks) {
        for (int i = 0; i < nicks.size(); i++)
        {
            players.add(new Player(nicks.get(i), ranks.get(i)));
        }
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }
}