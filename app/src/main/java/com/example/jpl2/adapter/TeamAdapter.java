package com.example.jpl2.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import android.content.Context;
import android.content.Intent;
import com.example.jpl2.model.TeamResponse;
import com.example.jpl2.activities.TeamDetailsActivity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jpl2.R;
import com.bumptech.glide.Glide;
import com.example.jpl2.model.TeamResponse;

import java.util.List;


public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.TeamViewHolder> {

    List<TeamResponse.Team> teams;
    Context context;

    public TeamAdapter(Context context, List<TeamResponse.Team> teams) {
        this.context = context;
        this.teams = teams;
    }

    @NonNull
    @Override
    public TeamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_team, parent, false);
        return new TeamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamViewHolder holder, int position) {
        TeamResponse.Team team = teams.get(position);

        holder.teamName.setText(team.name != null ? team.name : "No Name");

        Log.d("IMAGE_DEBUG", "Logo: " + team.getImagePath());

        String imageurl = "http://192.168.0.103:5000/" + team.getImagePath();

        Glide.with(holder.itemView.getContext())
                .load(imageurl)
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.teamLogo);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, TeamDetailsActivity.class);
            intent.putExtra("team_id", team.getTeamId());
            intent.putExtra("team_name", team.getName());
            intent.putExtra("team_logo", team.getImagePath());

            context.startActivity(intent);
        });

        // TODO: load image using Glide later
    }

    @Override
    public int getItemCount() {
        return teams.size();
    }

    static class TeamViewHolder extends RecyclerView.ViewHolder {

        TextView teamName;
        ImageView teamLogo;

        public TeamViewHolder(@NonNull View itemView) {
            super(itemView);

            teamName = itemView.findViewById(R.id.teamName);
            teamLogo = itemView.findViewById(R.id.teamLogo);
        }
    }
}