package com.example.listycity5;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.firebase.firestore.CollectionReference;

import java.util.ArrayList;

public class CityArrayAdapter extends ArrayAdapter<City> {
    private ArrayList<City> cities;
    private Context context;
    private CollectionReference citiesRef;

    public CityArrayAdapter(Context context, ArrayList<City> cities, CollectionReference citiesRef){
        super(context,0, cities);
        this.cities = cities;
        this.context = context;
        this.citiesRef = citiesRef;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.content, parent,false);
        }

        City city = cities.get(position);

        TextView cityName = view.findViewById(R.id.city_text);
        TextView provinceName = view.findViewById(R.id.province_text);

        cityName.setText(city.getCityName());
        provinceName.setText(city.getProvinceName());

        view.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Delete City")
                        .setMessage("Delete City: " + city.getCityName() + ", " + city.getProvinceName() + "?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            cities.remove(position);
                            notifyDataSetChanged();

                            citiesRef.document(city.getCityName())
                                    .delete()
                                    .addOnSuccessListener(aVoid -> {
                                    });
                        })
                        .setNegativeButton("No", null)
                        .show();
                return true;
            }
        });
        return view;
    }
}
