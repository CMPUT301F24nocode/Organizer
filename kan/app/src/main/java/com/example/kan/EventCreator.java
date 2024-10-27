package com.example.kan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class EventCreator extends AppCompatActivity {

    private DatabaseReference rootDatabase;

    private Button sendBtn;
    private EditText editTextEventName, editTextEventID, editTextDescription, editTextDate, editTextLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_creator_page);

        // Initialize views
        editTextEventName = findViewById(R.id.editTextEventName);
        editTextEventID = findViewById(R.id.editTextEventID);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextDate = findViewById(R.id.editTextDate);
        editTextLocation = findViewById(R.id.editTextLocation);
        sendBtn = findViewById(R.id.buttonSend);

        rootDatabase = FirebaseDatabase.getInstance().getReference().child("Events");

        // Send Button to upload data
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadDataWithoutImage();
            }
        });
    }

    // Upload data to Firebase without an image
    private void uploadDataWithoutImage() {
        // Retrieve data from EditTexts
        String eventName = editTextEventName.getText().toString().trim();
        String eventID = editTextEventID.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String date = editTextDate.getText().toString().trim();
        String location = editTextLocation.getText().toString().trim();

        // Create a map to hold event data
        Map<String, Object> eventData = new HashMap<>();
        eventData.put("eventName", eventName);
        eventData.put("eventID", eventID);
        eventData.put("description", description);
        eventData.put("date", date);
        eventData.put("location", location);
        eventData.put("posterUrl", ""); // No poster URL since we removed the image selection

        rootDatabase.push().setValue(eventData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(EventCreator.this, "Data successfully added!", Toast.LENGTH_SHORT).show();
                        // Return to the home screen (MainActivity)
                        Intent intent = new Intent(EventCreator.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();  // Close the EventCreator activity
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EventCreator.this, "Failed to add data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

