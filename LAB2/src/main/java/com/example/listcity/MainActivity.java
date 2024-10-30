package com.example.listcity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView cityList;
    ArrayAdapter<String> cityAdapter;
    ArrayList<String> dataList;
    String namesCity = null;

    Button addCity;
    Button deleteCity;
    Button confirm;
    TextView name;
    String cityName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        cityList = findViewById(R.id.city_list);

        dataList = new ArrayList<>();

        cityAdapter = new ArrayAdapter<>(this, R.layout.content, dataList);
        cityList.setAdapter(cityAdapter);


        addCity = findViewById(R.id.button1);
        deleteCity = findViewById(R.id.button2);
        confirm = findViewById(R.id.button3);
        name = findViewById(R.id.nameOfCity);

        confirm.setEnabled(false);
        name.setEnabled(false);

        addCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name.setEnabled(true);
                name.setText("");
                confirm.setEnabled(true);
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cityName = name.getText().toString().trim();

                if (!cityName.isEmpty()) {
                    dataList.add(cityName);
                    cityAdapter.notifyDataSetChanged();
                    name.setText("");
                    name.setEnabled(false);
                    confirm.setEnabled(false);
                }
            }
        });

        cityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                namesCity = dataList.get(i);
            }
        });

        deleteCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataList.remove(namesCity);
                cityAdapter.notifyDataSetChanged();
                namesCity = null;
            }
        });



                ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                    Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                    return insets;
                });


    }
}