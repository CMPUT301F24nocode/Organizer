package com.example.kan;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kan.EventCreator;
import com.example.kan.EventDetailActivity;
import com.example.kan.EventEditActivity;
import com.example.kan.FirebaseEventManager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<String> itemList;
    private ArrayList<String> eventDescription;
    private ArrayList<String> eventKeys;
    private ArrayAdapter<String> adapter;
    private Button addEventButton;

    private FirebaseEventManager firebaseEventManager;

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
        eventKeys = new ArrayList<>();

        // Set up adapter for ListView
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, itemList);
        listView.setAdapter(adapter);

        // Initialize FirebaseEventManager
        firebaseEventManager = new FirebaseEventManager(this);

        // Fetch data from Firebase using FirebaseEventManager
        firebaseEventManager.fetchDataFromFirebase(itemList, eventDescription, eventKeys, adapter);

        // Set OnClickListener for Create Button
        addEventButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EventCreator.class);
            startActivity(intent);
        });

        // Set item click listener for ListView to view event details
        listView.setOnItemClickListener((parent, view, position, id) -> {
            String eventKey = eventKeys.get(position);
            String eventDesc = eventDescription.get(position);
            String eventName = itemList.get(position);

            Intent intent = new Intent(MainActivity.this, EventDetailActivity.class);
            intent.putExtra("eventKey", eventKey);
            intent.putExtra("description", eventDesc);
            intent.putExtra("eventName", eventName);
            startActivity(intent);
        });

        // Set long-click listener for editing and deleting events
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Select an Action")
                    .setItems(new CharSequence[]{"Edit", "Delete"}, (dialog, which) -> {
                        if (which == 0) {
                            String eventKey = eventKeys.get(position);
                            String eventName = itemList.get(position);
                            String eventDesc = eventDescription.get(position);

                            Intent intent = new Intent(MainActivity.this, EventEditActivity.class);
                            intent.putExtra("eventKey", eventKey);
                            intent.putExtra("eventName", eventName);
                            intent.putExtra("description", eventDesc);
                            startActivity(intent);
                        } else if (which == 1) {
                            showDeleteConfirmationDialog(position);
                        }
                    })
                    .show();
            return true;
        });
    }

    // Show confirmation dialog for deletion
    private void showDeleteConfirmationDialog(int position) {
        String eventName = itemList.get(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Event")
                .setMessage("Are you sure you want to delete the " + eventName + " event?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    firebaseEventManager.deleteEvent(position, itemList, eventKeys, adapter);
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
