package com.travelbuddy.guideapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    Button UProfile;
    Button MPlans;
    Intent profile;
    TextView welcome;
    Intent plan;
    SharedPreferences shared;
    String guide_id,uid,guideName;
    FirebaseFirestore db=FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        profile=new Intent(MainActivity.this,ProfileActivity.class);
        plan =new Intent(MainActivity.this,PlanActivity.class);
        shared = getSharedPreferences("Travel_Data",Context.MODE_PRIVATE);
        MPlans=(Button) findViewById(R.id.button2);
        UProfile=(Button) findViewById(R.id.button);
        guide_id =  shared.getString("guide_id","ERROR");
        guideName = shared.getString("guide_name","");
        welcome = findViewById(R.id.welcomeText);
        welcome.setText("Welcome!, "+guideName);
        Switch toggle = findViewById(R.id.availabilty);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    db.collection("Guides").document(guide_id).update("Available",true);
                    showMessage("Availabilty set to ON");

                    //Toast.makeText(this,"Availabilty set to True",Toast.LENGTH_SHORT).show();
                } else {
                    // The toggle is disabled
                    db.collection("Guides").document(guide_id).update("Available",false);
                    showMessage("Availability set to OFF");

                }
            }
        });
    }

    private void showMessage(String message) {

        //TODO: Make generic toast message
        Toast.makeText(getApplicationContext(),message,Toast
                .LENGTH_LONG).show();
    }
    public void profile()
    {
        startActivity(profile);
    }

    public void manage()
    {
        startActivity(plan);
    }
}
