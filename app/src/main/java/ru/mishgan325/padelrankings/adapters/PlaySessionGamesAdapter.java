package ru.mishgan325.padelrankings.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.mishgan325.padelrankings.entities.GameData;
import com.example.padelrankings.R;

import java.util.ArrayList;
import java.util.List;

public class PlaySessionGamesAdapter extends RecyclerView.Adapter<PlaySessionGamesAdapter.ViewHolder> {

    private List<GameData> games = new ArrayList<>();
    private Context context;

    private ArrayList<String> nickList = new ArrayList<>();

    public PlaySessionGamesAdapter(Context context, List<GameData> games) {
        for (int i = 0; i < games.size(); i++) {
            Log.i("Games data", games.get(i).toString());
        }
        this.context = context;
        this.games = games;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.check_list_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GameData data = games.get(position);

        holder.spinner1.setAdapter(createSpinnerAdapter());
        holder.spinner2.setAdapter(createSpinnerAdapter());
        holder.spinner3.setAdapter(createSpinnerAdapter());
        holder.spinner4.setAdapter(createSpinnerAdapter());

        holder.editText1.setText(String.valueOf(data.getTeam1score()));
        holder.editText2.setText(String.valueOf(data.getTeam2score()));

        holder.spinner1.setSelection(nickList.indexOf(data.getTeam1player1()));
        holder.spinner2.setSelection(nickList.indexOf(data.getTeam1player2()));
        holder.spinner3.setSelection(nickList.indexOf(data.getTeam2player1()));
        holder.spinner4.setSelection(nickList.indexOf(data.getTeam2player2()));
    }

    @Override
    public int getItemCount() {
        return games.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        Spinner spinner1, spinner2, spinner3, spinner4;
        EditText editText1, editText2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            spinner1 = itemView.findViewById(R.id.activity_check_team1player1);
            spinner2 = itemView.findViewById(R.id.activity_check_team1player2);
            spinner3 = itemView.findViewById(R.id.activity_check_team2player1);
            spinner4 = itemView.findViewById(R.id.activity_check_team2player2);

            editText1 = itemView.findViewById(R.id.activity_check_score1);
            editText2 = itemView.findViewById(R.id.activity_check_score2);
        }
    }

    public void setNickList(ArrayList<String> nickList) {
        this.nickList = nickList;
    }

    private ArrayAdapter<String> createSpinnerAdapter() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, nickList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }
}