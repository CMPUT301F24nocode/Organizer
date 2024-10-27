package com.example.kan;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FirebaseEventManager {

    private final DatabaseReference databaseReference;
    private final Context context;

    public FirebaseEventManager(Context context) {
        this.context = context;
        databaseReference = FirebaseDatabase.getInstance().getReference("Events");
    }

    public void fetchDataFromFirebase(ArrayList<String> itemList, ArrayList<String> eventDescription, ArrayList<String> eventKeys, ArrayAdapter<String> adapter) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itemList.clear();
                eventDescription.clear();
                eventKeys.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String eventName = snapshot.child("eventName").getValue(String.class);
                    String eventDesc = snapshot.child("description").getValue(String.class);
                    String eventKey = snapshot.getKey();

                    if (eventName != null && eventKey != null) {
                        itemList.add(eventName);
                        eventDescription.add(eventDesc);
                        eventKeys.add(eventKey);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context, "Failed to load events: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void deleteEvent(int position, ArrayList<String> itemList, ArrayList<String> eventKeys, ArrayAdapter<String> adapter) {
        if (position < 0 || position >= eventKeys.size()) {
            Log.e("DeleteEvent", "Invalid position for deletion: " + position);
            Toast.makeText(context, "Failed to delete: Invalid position", Toast.LENGTH_SHORT).show();
            return;
        }

        String eventKey = eventKeys.get(position);

        databaseReference.child(eventKey).removeValue()
                .addOnSuccessListener(aVoid -> {
                    Log.d("DeleteEvent", "Event deleted successfully.");
                    Toast.makeText(context, "Event deleted successfully.", Toast.LENGTH_SHORT).show();

                    // Remove the event from the local lists and update the ListView
                    itemList.remove(position);
                    eventKeys.remove(position);
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Log.e("DeleteEvent", "Failed to delete event: " + e.getMessage());
                    Toast.makeText(context, "Failed to delete event: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
