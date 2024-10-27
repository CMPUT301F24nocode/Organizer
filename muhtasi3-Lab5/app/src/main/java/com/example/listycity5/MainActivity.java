package com.example.listycity5;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    ListView cityList;
    ArrayList<City> cityDataList;
    CityArrayAdapter cityArrayAdapter;
    private Button addCityButton;
    private EditText addCityEditText;
    private EditText addProvinceEditText;
    private FirebaseFirestore db;
    private CollectionReference citiesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();
        citiesRef = db.collection("cities");

        cityList = findViewById(R.id.city_list);
        addCityEditText = findViewById(R.id.city_name_edit);
        addProvinceEditText = findViewById(R.id.province_name_edit);
        addCityButton = findViewById(R.id.add_city_button);
        cityDataList = new ArrayList<>();

        cityArrayAdapter = new CityArrayAdapter(this, cityDataList, citiesRef);
        cityList.setAdapter(cityArrayAdapter);

        citiesRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshots, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Firestore", error.toString());
                    return;
                }
                if (querySnapshots != null) {
                    cityDataList.clear();
                    for (QueryDocumentSnapshot doc: querySnapshots) {
                        String city = doc.getId();
                        String province = doc.getString("Province");
                        Log.d("Firestore", String.format("City(%s, %s) fetched", city, province));
                        cityDataList.add(new City(city, province));
                    }
                    cityArrayAdapter.notifyDataSetChanged();
                }
            }
        });

        // Add a click listener to the addCityButton to read the EditTexts and make a new City object
        addCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cityName = addCityEditText.getText().toString();
                String provinceName = addProvinceEditText.getText().toString();

                if (!cityName.isEmpty() && !provinceName.isEmpty()) {
                    City newCity = new City(cityName, provinceName);
                    cityDataList.add(newCity);
                    addNewCity(newCity);

                    cityArrayAdapter.notifyDataSetChanged();
                    addCityEditText.setText("");
                    addProvinceEditText.setText("");
                }
            }
        });
    }

    private void addNewCity(City city) {
        // Add the city to the local list
        cityDataList.add(city);
        cityArrayAdapter.notifyDataSetChanged(); // Notify the adapter of the change

        // Add the city to the Firestore collection with the city name as the document Id
        HashMap<String, String> data = new HashMap<>();
        data.put("Province", city.getProvinceName());

        citiesRef
                .document(city.getCityName())
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Log successful write operation
                        Log.d("Firestore", "DocumentSnapshot successfully written!");
                    }
                });
    }

}