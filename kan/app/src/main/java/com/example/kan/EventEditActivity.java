package com.example.kan;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class EventEditActivity extends AppCompatActivity {

    private EditText editTextEventName, editTextDescription, editTextDate, editTextLocation;
    private Button buttonUpdate;
    private DatabaseReference rootDatabase;
    private String eventKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit); // You will create this layout file

        // Initialize views
        editTextEventName = findViewById(R.id.textViewEventName);
        editTextDescription = findViewById(R.id.editTextDetails);
        editTextDate = findViewById(R.id.editTextDate);
        editTextLocation = findViewById(R.id.editTextLocation);
        buttonUpdate = findViewById(R.id.buttonUpdate);

        // Retrieve event details passed from MainActivity
        eventKey = getIntent().getStringExtra("eventKey");
        String eventName = getIntent().getStringExtra("eventName");
        String description = getIntent().getStringExtra("description");

        // Add logging
        Log.d("EventEditActivity", "eventKey: " + eventKey);
        Log.d("EventEditActivity", "eventName: " + eventName);
        Log.d("EventEditActivity", "description: " + description);

        // Populate fields with the existing event data
        if (eventName != null) {
            editTextEventName.setText(eventName);
        }
        if (description != null) {
            editTextDescription.setText(description);
        }

        // Firebase reference
        rootDatabase = FirebaseDatabase.getInstance().getReference().child("Events").child(eventKey);

        // Update Button listener
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateEventData();
            }
        });
    }

    // Method to update event data in Firebase
    private void updateEventData() {
        String updatedName = editTextEventName.getText().toString().trim();
        String updatedDescription = editTextDescription.getText().toString().trim();
        String updatedDate = editTextDate.getText().toString().trim();
        String updatedLocation = editTextLocation.getText().toString().trim();

        // Create a map for the updated event data
        Map<String, Object> updatedData = new HashMap<>();
        updatedData.put("eventName", updatedName);
        updatedData.put("description", updatedDescription);
        updatedData.put("date", updatedDate);
        updatedData.put("location", updatedLocation);

        // Try-catch block to handle any errors while updating
        try {
            // Update Firebase with new data
            rootDatabase.updateChildren(updatedData).addOnSuccessListener(aVoid -> {
                Toast.makeText(EventEditActivity.this, "Event updated successfully!", Toast.LENGTH_SHORT).show();
                finish(); // Return to MainActivity after updating
            }).addOnFailureListener(e -> {
                Toast.makeText(EventEditActivity.this, "Failed to update event: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        } catch (Exception e) {
            Log.e("EventEditActivity", "Error updating event", e);
            Toast.makeText(EventEditActivity.this, "An error occurred while updating event: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
