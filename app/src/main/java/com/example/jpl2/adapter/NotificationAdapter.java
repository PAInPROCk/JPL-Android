package com.example.jpl2.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.jpl2.R;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private ArrayList<String> notifications;

    public NotificationAdapter(ArrayList<String>notifications){
        this.notifications = notifications;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtNotifications;

        public  ViewHolder(View itemView){
            super(itemView);
            txtNotifications = itemView.findViewById(R.id.txtNotification);
        }
    }

    @Override
    public ViewHolder
    onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent,false);
        return  new ViewHolder(view);
    }

    @Override
    public  void
    onBindViewHolder(ViewHolder holder, int position){
        holder.txtNotifications.setText(notifications.get(position));
    }

    @Override
    public int getItemCount(){
        return notifications.size();
    }
}
