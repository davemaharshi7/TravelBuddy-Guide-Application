package com.travelbuddy.guideapplication;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    private long male = 0;
    private long female = 1;

    Hashtable<String,String> map;
    Hashtable<String,Long> mapGender;
    Spinner genderSpinner,searchSpinner;

    @Override
    protected void onStart() {
        super.onStart();
        List<String> genderList= new ArrayList<String>();
        genderList.clear();
        genderList.add("Male");
        genderList.add("Female");
        mapGender.put("Male",male);
        mapGender.put("Female",female);


        ArrayAdapter<String> gAdapter = new ArrayAdapter<String>
                (getApplicationContext(),
                        android.R.layout.simple_spinner_item, genderList);
        gAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(gAdapter);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Cities")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<String> city = new ArrayList<String>();
                            city.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("EEEE", document.getId() + " => " + document.get("CityName"));
                                city.add((String) document.get("CityName"));
                                map.put(document.getString("CityName"),document.getId());
                            }
                            Collections.sort(city);
                            ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>
                                    (getApplicationContext(),
                                            android.R.layout.simple_spinner_item, city);
                            areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            searchSpinner.setAdapter(areasAdapter);
//                            submit.setEnabled(true);
//                            loading.setVisibility(View.INVISIBLE);

                        } else {
                            Log.d("EEEE", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        map = new Hashtable<>();
        mapGender = new Hashtable<>();
        genderSpinner = findViewById(R.id.spGender);

        searchSpinner = findViewById(R.id.spCity);

    }
}
