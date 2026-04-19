package com.example.jpl2.adapter;

import static com.example.jpl2.api.ApiClient.BASE_URL;

import android.content.Context;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import android.content.Intent;
import com.example.jpl2.activities.PlayerDetailsActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.jpl2.R;
import com.example.jpl2.api.ApiClient;
import com.example.jpl2.model.Player;


import java.util.List;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.ViewHolder> {
    private List<Player> players;
    Context context;

    public PlayerAdapter(Context context,List<Player> players) {
        this.players = players;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView name, price, role;

        public ViewHolder(View view){
            super(view);
            image = view.findViewById(R.id.playerImage);
            name = view.findViewById(R.id.playerName);
//            price = view.findViewById(R.id.playerPrice);
//            role = view.findViewById(R.id.playerRole);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_players, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        Player player = players.get(position);

        holder.name.setText(player.getName() != null ? player.getName() : "N/A");
//        holder.price.setText("₹ " + player.getBasePrice());
//        holder.role.setText(player.getRole() != null ? player.getRole() : "N/A");

// IMAGE LOAD
        String imageUrl = player.getImagePath() != null ? ApiClient.BASE_URL + player.getImagePath() : null;

        Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.player)
                .error(R.drawable.player)
                .circleCrop()
                .into(holder.image);

// CLICK → DETAILS
        holder.itemView.setOnClickListener(v -> {

            Intent intent = new Intent(v.getContext(), PlayerDetailsActivity.class);

            intent.putExtra("name", player.getName());
            intent.putExtra("nickname", player.getNickName());
            intent.putExtra("category", player.getCategory());
            intent.putExtra("type", player.getType());
            intent.putExtra("role", player.getRole());
            intent.putExtra("age", player.getAge());
            intent.putExtra("price", player.getBasePrice());
            intent.putExtra("gender", player.getGender());
            intent.putExtra("teams", player.getTeamsPlayed());
            intent.putExtra("image", player.getImagePath());

            intent.putExtra("total_runs", player.getTotalRuns());
            intent.putExtra("highest_runs", player.getHighestRuns());
            intent.putExtra("wickets_taken", player.getWicketsTaken());
            intent.putExtra("times_out", player.getTimesOut());

            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount(){
        return players.size();
    }
}
