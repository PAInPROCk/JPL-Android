package com.example.jpl2.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jpl2.R;
import com.example.jpl2.adapter.NotificationAdapter;

import java.util.ArrayList;

public class Team_Auction_Activity extends AppCompatActivity {

    RecyclerView recyclerView;
    NotificationAdapter adapter;
    ArrayList<String> notifications;

    public void addNotification(String message){
        notifications.add(0, message); // Set newest bid on top
        adapter.notifyItemInserted(0);
        recyclerView.scrollToPosition(0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_team_auction);
        recyclerView = findViewById(R.id.notificationRecycler);

        notifications = new ArrayList<>();
        notifications.add("No Bids Yet");

        adapter = new NotificationAdapter(notifications);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}