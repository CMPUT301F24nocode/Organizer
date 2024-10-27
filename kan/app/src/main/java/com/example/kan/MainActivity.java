package com.example.kan;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<String> itemList; // To hold event names
    private ArrayList<String> eventDescription;
    private ArrayList<String> eventKeys; // To hold Firebase keys for deletion
    private ArrayAdapter<String> adapter;
    private Button addEventButton;

    // Firebase database reference
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        // Initialize
        addEventButton = findViewById(R.id.create_button);
        listView = findViewById(R.id.list_view);

        // Initialize data lists
        itemList = new ArrayList<>();
        eventDescription = new ArrayList<>();
        eventKeys = new ArrayList<>(); // To store Firebase keys


        // Set up adapter for ListView
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, itemList);
        listView.setAdapter(adapter);

        // Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Events");

        // Fetch data from Firebase
        fetchDataFromFirebase();

        // Set OnClickListener for Create Button
        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the EventCreator Activity when the button is clicked
                Intent intent = new Intent(MainActivity.this, EventCreator.class);
                startActivity(intent);
            }
        });

        // Set item click listener for ListView to view event details
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the Firebase key and event name
                String eventKey = eventKeys.get(position);
                String eventDesc = eventDescription.get(position);
                String eventName = itemList.get(position);

                // Start EventDetailActivity and pass the event key
                Intent intent = new Intent(MainActivity.this, EventDetailActivity.class);
                intent.putExtra("eventKey", eventKey);
                intent.putExtra("description", eventDesc);
                intent.putExtra("eventName", eventName);
                startActivity(intent);
            }
        });

        // Set long click listener for ListView to delete an event
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // Show confirmation dialog before deleting
                showDeleteConfirmationDialog(position);
                return true; // Returning true indicates the long-click event is handled
            }
        });
    }

    // Method to fetch data from Firebase
    private void fetchDataFromFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itemList.clear();
                eventDescription.clear(); // Clear descriptions
                eventKeys.clear(); // Clear previous data

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String eventName = snapshot.child("eventName").getValue(String.class);
                    String eventDesc = snapshot.child("description").getValue(String.class); // Get the event description
                    String eventKey = snapshot.getKey(); // Get the unique key for each event

                    if (eventName != null && eventKey != null) {
                        itemList.add(eventName);  // Add event name to the list
                        eventDescription.add(eventDesc);  // Add event description to the list
                        eventKeys.add(eventKey);  // Store the Firebase key for deletion
                    }
                }
                adapter.notifyDataSetChanged(); // Notify adapter to refresh the ListView
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Failed to load events: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    // Method to show confirmation dialog for deletion
    private void showDeleteConfirmationDialog(int position) {
        // Get the event name from itemList based on the position
        String eventName = itemList.get(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Event")
                .setMessage("Are you sure you want to delete the event" + eventName + " event?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    Log.d("DeleteEvent", "Attempting to delete event at position: " + position);
                    deleteEvent(position);
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }

    // Method to delete an event
    private void deleteEvent(int position) {
        // Check if the position is still valid to prevent crashing
        if (position < 0 || position >= eventKeys.size()) {
            Log.e("DeleteEvent", "Invalid position for deletion: " + position);
            Toast.makeText(MainActivity.this, "Failed to delete: Invalid position", Toast.LENGTH_SHORT).show();
            return;
        }

        String eventKey = eventKeys.get(position);  // Use Firebase key for deletion

        // Remove the event from Firebase
        databaseReference.child(eventKey).removeValue()
                .addOnSuccessListener(aVoid -> {
                    Log.d("DeleteEvent", "Event deleted successfully.");
                    Toast.makeText(MainActivity.this, "Event deleted successfully.", Toast.LENGTH_SHORT).show();

                    // Delay the list update to avoid UI refresh conflicts
                    runOnUiThread(() -> {
                        // Remove the event from the local lists and update the ListView
                        if (position >= 0 && position < itemList.size()) {
                            itemList.remove(position);
                            eventKeys.remove(position);
                            adapter.notifyDataSetChanged();
                        }
                    });
                })
                .addOnFailureListener(e -> {
                    Log.e("DeleteEvent", "Failed to delete event: " + e.getMessage());
                    Toast.makeText(MainActivity.this, "Failed to delete event: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
