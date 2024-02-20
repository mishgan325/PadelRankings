package com.example.padelrankings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ShowRankAdapter extends ArrayAdapter<Player> {

    private LayoutInflater inflater;
    private int layout;
    private List<Player> players;

    public ShowRankAdapter(Context context, int resource, List<Player> players) {
        super(context, resource, players);
        this.players = players;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }
    public View getView(int position, View convertView, ViewGroup parent) {

        View view=inflater.inflate(this.layout, parent, false);

        TextView placeView = view.findViewById(R.id.activity_show_rank_place);
        TextView nickView = view.findViewById(R.id.activity_show_rank_nick);
        TextView rankView = view.findViewById(R.id.activity_show_rank_rankValue);
        TextView modeView = view.findViewById(R.id.activity_show_rank_mode);

        Player player = players.get(position);

        placeView.setText(String.valueOf(position+1));
        nickView.setText(player.getNick());
        rankView.setText(String.valueOf(player.getRank()));
        modeView.setText(String.valueOf(player.getMode()));

        return view;
    }
}