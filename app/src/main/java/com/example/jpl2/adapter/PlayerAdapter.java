package com.example.jpl2.adapter;

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
        TextView name, price, role;

        public ViewHolder(View view){
            super(view);
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
        holder.price.setText("₹ "+ player.getBasePrice());
        holder.role.setText(player.getRole());
    }

    @Override
    public int getItemCount(){
        return players.size();
    }
}
