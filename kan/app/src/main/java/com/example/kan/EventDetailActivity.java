package com.example.kan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EventDetailActivity extends AppCompatActivity {

    private TextView textViewEventName, textViewEventID, textViewDescription, textViewDate, textViewLocation;
    private Button buttonRSVP, buttonShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        // Initialize views
        textViewEventName = findViewById(R.id.textViewEventName);
        textViewEventID = findViewById(R.id.textViewEventID);
        textViewDescription = findViewById(R.id.textViewDescription);
        textViewDate = findViewById(R.id.textViewDate);
        textViewLocation = findViewById(R.id.textViewLocation);
        buttonRSVP = findViewById(R.id.buttonRSVP);
        buttonShare = findViewById(R.id.buttonShare);

        // Retrieve event details passed from MainActivity
        String eventName = getIntent().getStringExtra("eventName");
        String eventID = getIntent().getStringExtra("eventID");
        String description = getIntent().getStringExtra("description");
        String date = getIntent().getStringExtra("date");
        String location = getIntent().getStringExtra("location");
        String posterUrl = getIntent().getStringExtra("posterUrl");

        // Set event details in TextViews
        textViewEventName.setText(eventName);
        textViewEventID.setText(eventID);
        textViewDescription.setText(description);
        textViewDate.setText(date);
        textViewLocation.setText(location);

        // Load event poster directly if the URL is available

        // Button Click Listeners
        buttonRSVP.setOnClickListener(v -> {
            // Handle RSVP action (e.g., save to a database, show confirmation)
            Toast.makeText(this, "RSVPed to " + eventName, Toast.LENGTH_SHORT).show();
        });

        buttonShare.setOnClickListener(v -> {
            // Share the event details
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            String shareBody = "Check out this event: " + eventName + "\nDate: " + date + "\nLocation: " + location;
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(shareIntent, "Share Event"));
        });

        Button backButton = findViewById(R.id.buttonBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish current activity and go back to MainActivity
                finish(); // This will take you back to the previous activity
            }
        });
    }
}