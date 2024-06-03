package ru.mishgan325.padelrankings.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.mishgan325.padelrankings.entities.PlayerPartnerInfo;
import ru.mishgan325.padelrankings.R;

import java.util.ArrayList;

public class PlayerStatsAdapter extends RecyclerView.Adapter<PlayerStatsAdapter.ViewHolder> {

    private ArrayList<PlayerPartnerInfo> playerStatsList;
    Context context;

    public PlayerStatsAdapter(Context context, ArrayList<PlayerPartnerInfo> playerStatsList) {
        this.playerStatsList = playerStatsList;
        this.context = context;
    }


    @NonNull
    @Override
    public PlayerStatsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.partner_list_item_layout, parent, false);
        return new PlayerStatsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerStatsAdapter.ViewHolder holder, int position) {
        PlayerPartnerInfo data = playerStatsList.get(position);

        holder.textViewPartner.setText(data.getPartner());
        holder.textViewGames.setText(String.valueOf(data.getGames()));
        holder.textViewWinrate.setText(data.getWinrate());
        holder.textViewScore.setText(data.getScore());
        holder.textViewTiebreaks.setText(String.valueOf(data.getTiebreaks()));
        holder.textViewTiebreaksWinrate.setText(data.getTiebreaksWinrate());
    }

    @Override
    public int getItemCount() {
        return playerStatsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewPartner;
        TextView textViewGames;
        TextView textViewWinrate;
        TextView textViewScore;
        TextView textViewTiebreaks;
        TextView textViewTiebreaksWinrate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewPartner = itemView.findViewById(R.id.activity_player_stats_list_textViewNickname);
            textViewGames = itemView.findViewById(R.id.activity_player_stats_list_textViewGames);
            textViewWinrate = itemView.findViewById(R.id.activity_player_stats_list_textViewWinrate);
            textViewScore = itemView.findViewById(R.id.activity_player_stats_list_textViewScore);
            textViewTiebreaks = itemView.findViewById(R.id.activity_player_stats_list_textViewTiebreaks);
            textViewTiebreaksWinrate = itemView.findViewById(R.id.activity_player_stats_list_textViewWinrateTb);
        }
    }
}