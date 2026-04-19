package com.example.jpl2.adapter;

import android.content.Context;
import android.content.Intent;
import static com.example.jpl2.api.ApiClient.BASE_URL;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jpl2.R;
import com.example.jpl2.activities.TeamDetailsActivity;
import com.example.jpl2.model.TeamResponse;
import com.bumptech.glide.Glide;

import java.util.List;

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.TeamViewHolder> {

    private List<TeamResponse.Team> teams;
    private Context context;

    public TeamAdapter(Context context, List<TeamResponse.Team> teams) {
        this.context = context;
        this.teams = teams;
    }

    @NonNull
    @Override
    public TeamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_team, parent, false);
        return new TeamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamViewHolder holder, int position) {

        TeamResponse.Team team = teams.get(position);

        // ✅ Safe name
        String name = (team.getName() != null) ? team.getName() : "No Name";
        holder.teamName.setText(name);

        // ✅ Correct backend image URL
        String imagePath = team.getImagePath();
        String captain = team.getCaptain();
        String imageUrl = "https://jpl-backend-6ecq.onrender.com/" +
                (imagePath != null ? imagePath : "");

        Log.d("IMAGE_DEBUG", "Image URL: " + imageUrl);

        // ✅ Load image using Glide
        Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.teamLogo);

        // ✅ Click → open Team Details
        holder.itemView.setOnClickListener(v -> {

            Intent intent = new Intent(context, TeamDetailsActivity.class);

            intent.putExtra("team_id", team.getTeamId());
            intent.putExtra("team_name", team.getName());
            intent.putExtra("team_logo", team.getImagePath());
            intent.putExtra("captain", team.getCaptain());
            intent.putExtra("rank", team.getRank());
            intent.putExtra("total_budget", team.getTotalBudget());
            intent.putExtra("current_budget", team.getCurrentBudget());
            intent.putExtra("players_bought", team.getPlayersBought());

            context.startActivity(intent);
        });

        // TODO: load image using Glide later
    }

    @Override
    public int getItemCount() {
        return (teams != null) ? teams.size() : 0;
    }

    // ================= VIEW HOLDER =================
    static class TeamViewHolder extends RecyclerView.ViewHolder {

        TextView teamName;
        ImageView teamLogo;

        public TeamViewHolder(@NonNull View itemView) {
            super(itemView);

            teamName = itemView.findViewById(R.id.teamName);
            teamLogo = itemView.findViewById(R.id.teamImage); // match XML
        }
    }
}