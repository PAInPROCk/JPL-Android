package com.example.jpl2.adapter;

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
import com.example.jpl2.model.Player;


import java.util.List;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.ViewHolder> {
    private List<Player> players;

    public PlayerAdapter(List<Player> players){
        this.players = players;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView name, price, role;

        public ViewHolder(View view){
            super(view);
            image = view.findViewById(R.id.playerImage);
            name = view.findViewById(R.id.playerName);
            price = view.findViewById(R.id.playerPrice);
            role = view.findViewById(R.id.playerRole);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_player, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        Player player = players.get(position);

        holder.name.setText(player.getName());
        holder.price.setText("₹ " + player.getBasePrice());
        holder.role.setText(player.getRole());

// IMAGE LOAD
        String imageUrl = "http://192.168.0.101:5000/" + player.getImagePath();

        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.image);

// CLICK → DETAILS
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), PlayerDetailsActivity.class);
            intent.putExtra("name", player.getName());
            intent.putExtra("image", player.getImagePath());
            intent.putExtra("price", player.getBasePrice());
            intent.putExtra("role", player.getRole());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount(){
        return players.size();
    }
}
